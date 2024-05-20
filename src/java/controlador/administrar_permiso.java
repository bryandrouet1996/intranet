/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.AnulacionManual;
import modelo.AnulacionSolicitud;
import modelo.AnulacionVacaciones;
import modelo.RevisionHoras;
import modelo.AprobacionHoras;
import modelo.PeriodoVaca;
import modelo.PermisoECU;
import modelo.PermisoManual;
import modelo.aprobacion_vacaciones;
import modelo.conexion;
import modelo.conexion_oracle;
import modelo.permiso_horas;
import modelo.permiso_vacaciones;
import modelo.rechazo_solicitud;
import modelo.rechazo_vacaciones;
import modelo.revision_vacaciones;
import modelo.usuario;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_permiso", urlPatterns = {"/administrar_permiso.control"})

public class administrar_permiso extends HttpServlet {

    private static final String PATH = "/newmedia/adjuntos_permisos/";
//    private static final String PATH = "C:/prueba/adjuntos_permisos/";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        conexion enlace = new conexion();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registro_horas")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario"));
            int codUsu = Integer.parseInt(request.getParameter("codusu"));
            Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
            String observacion = request.getParameter("areaobservacion");
            conexion_oracle link = new conexion_oracle();
            if (id_tipo == 1) {
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio1"));
                String hora_salida = request.getParameter("txtinicio");
                String hora_entrada = request.getParameter("txtfin");
                String tiempo = request.getParameter("txttiempo");
                int horas = Integer.parseInt(request.getParameter("txthoras"));
                int minutos = Integer.parseInt(request.getParameter("txtminutos"));
                if ((horas == 7 && minutos == 0) || horas < 7) {
                    permiso_horas elemento = new permiso_horas(id_usuario, hora_entrada, hora_salida, id_motivo, fecha_solicitud, tiempo, horas, minutos, observacion, id_tipo);
                    elemento.setFecha_inicio(fecha_inicio);
                    elemento.setFecha_fin(fecha_inicio);
                    elemento.setFecha_ingreso(fecha_inicio);
                    elemento.setCodigoUsu(codUsu);
                    try {
                        if (id_motivo != 1) {
                            Part part = request.getPart("txtadjunto");
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            int idPermiso = enlace.registroPermisoHoras(elemento);
                            if (!request.getParameter("txtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("txtinicio").equalsIgnoreCase("") && !request.getParameter("txtfin").equalsIgnoreCase("") && !request.getParameter("combomotivo").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("") && !request.getPart("txtadjunto").equals("")) {
                                String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                                part.write(path);
                                if (enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path)) {
                                    PrintWriter out = response.getWriter();
                                    out.print("ok");
                                }
                            }
                        } else {
                            if (!request.getParameter("txtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("txtinicio").equalsIgnoreCase("") && !request.getParameter("txtfin").equalsIgnoreCase("") && !request.getParameter("combomotivo").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("")) {
                                int recargo = 0, diasPend, horasPend, minPend, horasTemp;
                                ArrayList<PeriodoVaca> vacas = link.getPeriodosVacaUsuario(enlace.consultarCodigoUsuario(id_usuario));
                                for (PeriodoVaca v : vacas) {
                                    diasPend = v.getDiasPend();
                                    horasPend = v.getHorasPend();
                                    minPend = v.getMinPend();
                                    if (minutos + minPend >= 60) {
                                        horasTemp = horas + 1;
                                    } else {
                                        horasTemp = horas;
                                    }
                                    if (horasTemp + horasPend >= 8) {
                                        if (diasPend + 1 == 5) {
                                            recargo = v.getDiasDisp() < 2 ? v.getDiasDisp() : 2;
                                        }
                                    }
                                    elemento.setCierre(recargo * 8 * 60 + horas * 60 + minutos >= v.getDiasDisp() * 8 * 60 + v.getHorasDisp() * 60 + v.getMinDisp() ? v.getPeriodo().substring(5, v.getPeriodo().length()) : "");
                                    break;
                                }
                                elemento.setDias_habiles(0);
                                elemento.setDias_nohabiles(0);
                                elemento.setDias_recargo(recargo);
                                elemento.setDias_descuento(recargo);
                                if (enlace.registroPermisoHorasVacaciones(elemento) > 0) {
                                    PrintWriter out = response.getWriter();
                                    out.print("ok");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("registroPermisoHoras | " + ex);
                    }
                }
            } else if (id_tipo == 2) {
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio1"));
                Date fecha_fin = Date.valueOf(request.getParameter("txtfin1"));
                Date fecha_retorno = Date.valueOf(request.getParameter("txtingreso"));
                double dias_pendientes = Double.parseDouble(request.getParameter("txtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("txtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("txtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("txtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("txtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                permiso_horas elemento = new permiso_horas(id_usuario, id_motivo, fecha_solicitud, fecha_inicio, fecha_fin, fecha_retorno, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion, id_tipo);
                elemento.setCodigoUsu(codUsu);
                try {
                    if (id_motivo != 1) {
                        Part part = request.getPart("txtadjunto");
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        int idPermiso = enlace.registroPermisoHorasDias(elemento);
                        if (!request.getParameter("txtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("txtinicio1").equalsIgnoreCase("") && !request.getParameter("txtfin1").equalsIgnoreCase("") && !request.getParameter("combomotivo").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("") && !request.getPart("txtadjunto").equals("")) {
                            String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                            part.write(path);
                            if (enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path)) {
                                PrintWriter out = response.getWriter();
                                out.print("ok");
                            }
                        }
                    } else {
                        if (!request.getParameter("txtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("txtinicio1").equalsIgnoreCase("") && !request.getParameter("txtfin1").equalsIgnoreCase("") && !request.getParameter("combomotivo").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("")) {
                            enlace.registroPermisoHorasDias(elemento);
                            PrintWriter out = response.getWriter();
                            out.print("ok");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("registroPermisoHorasDias | " + ex);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_horas_ecu")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario"));
            int cod_usu = Integer.parseInt(request.getParameter("codusu"));
            Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
            String descrip = request.getParameter("areaobservacion");
            conexion_oracle oracle = new conexion_oracle();
            int codJefe = oracle.consultarCodigoJefeUsuario(cod_usu);
            PermisoECU p = new PermisoECU(id_usuario, id_motivo, fecha_solicitud, descrip, oracle.consultarDireccionUsuario(cod_usu), oracle.consultarDenominacionUsuario(cod_usu), oracle.consultarJefeUsuario(cod_usu), oracle.consultarDenominacionUsuario(codJefe));
            if (id_tipo == 1) {
                p.setTiempoSoli(request.getParameter("txttiempo"));
                p.setDiasHabiles(0);
                p.setDiasFinde(0);
                try {
                    java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(request.getParameter("txtinicio1") + "_" + request.getParameter("txtinicio") + ":00");
                    p.setInicio(new Timestamp(utilDate.getTime()));
                    utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(request.getParameter("txtinicio1") + "_" + request.getParameter("txtfin") + ":00");
                    p.setFin(new Timestamp(utilDate.getTime()));
                } catch (Exception e) {
                    System.out.println("parseo utilDate | " + e);
                }
                int horas = Integer.parseInt(request.getParameter("txthoras"));
                int minutos = Integer.parseInt(request.getParameter("txtminutos"));
                if ((horas == 7 && minutos == 0) || horas < 7) {
                    try {
                        PrintWriter out = response.getWriter();
                        int idPermiso = enlace.registroPermisoHorasECU(p);
                        if (id_motivo != 1) {
                            Part part = request.getPart("txtadjunto");
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            String path = PATH + idPermiso + "_ECU_" + part.getSubmittedFileName();
                            part.write(path);
                            if (enlace.actualizarAdjuntoPermisoHorasECU(idPermiso, path)) {
                                out.print("ok");
                            }
                        } else {
                            out.print("ok");
                        }
                    } catch (SQLException ex) {
                        System.out.println("registroPermisoHorasECU | " + ex);
                    }
                }
            } else if (id_tipo == 2) {
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                int dias_habiles = Integer.parseInt(request.getParameter("txtdiashabiles"));
                int dias_nohabiles = Integer.parseInt(request.getParameter("txtdiasnohabiles"));
                p.setTiempoSoli(dias_solicitados + (dias_solicitados > 1 ? " días" : " día"));
                p.setDiasHabiles(dias_habiles);
                p.setDiasFinde(dias_nohabiles);
                try {
                    try {
                        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(request.getParameter("txtinicio1") + "_08:00:00");
                        p.setInicio(new Timestamp(utilDate.getTime()));
                        utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(request.getParameter("txtfin1") + "_17:00:00");
                        p.setFin(new Timestamp(utilDate.getTime()));
                    } catch (Exception e) {
                        System.out.println("parseo utilDate | " + e);
                    }
                    PrintWriter out = response.getWriter();
                    int idPermiso = enlace.registroPermisoHorasECU(p);
                    if (id_motivo != 1) {
                        Part part = request.getPart("txtadjunto");
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        String path = PATH + idPermiso + "_ECU_" + part.getSubmittedFileName();
                        part.write(path);
                        if (enlace.actualizarAdjuntoPermisoHorasECU(idPermiso, path)) {
                            out.print("ok");
                        }
                    } else {
                        out.print("ok");
                    }
                } catch (SQLException ex) {
                    System.out.println("registroPermisoHorasECU | " + ex);
                }
            }
        } else if (accion.equalsIgnoreCase("editar_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("eid"));
            Date fecha_solicitud = Date.valueOf(request.getParameter("etxtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("ecombotipo"));
            int id_motivo = Integer.parseInt(request.getParameter("ecombomotivo"));
            String observacion = request.getParameter("eareaobservacion");
            conexion_oracle link = new conexion_oracle();
            permiso_horas perm = enlace.buscarPermisoHoras(id_permiso);
            if (id_tipo == 1) {
                String hora_salida = request.getParameter("etxtinicio");
                String hora_entrada = request.getParameter("etxtfin");
                String tiempo = request.getParameter("etxttiempo");
                int horas = Integer.parseInt(request.getParameter("etxthoras"));
                int minutos = Integer.parseInt(request.getParameter("etxtminutos"));
                if ((horas == 7 && minutos == 0) || horas < 7) {
                    permiso_horas elemento = new permiso_horas(hora_entrada, hora_salida, id_motivo, fecha_solicitud, tiempo, horas, minutos, observacion, id_tipo);
                    elemento.setFecha_inicio(fecha_solicitud);
                    elemento.setFecha_fin(fecha_solicitud);
                    elemento.setFecha_ingreso(fecha_solicitud);
                    try {
                        if (id_motivo != 1) {
                            new File(perm.getAdjunto()).delete();
                            Part part = request.getPart("etxtadjunto");
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            if (enlace.actualizarPermisoHoras(id_permiso, elemento)) {
                                if (!request.getParameter("etxtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("etxtinicio").equalsIgnoreCase("") && !request.getParameter("etxtfin").equalsIgnoreCase("") && !request.getParameter("ecombomotivo").equalsIgnoreCase("") && !request.getParameter("eareaobservacion").equalsIgnoreCase("") && !request.getPart("etxtadjunto").equals("")) {
                                    String path = PATH + id_permiso + "_" + part.getSubmittedFileName();
                                    part.write(path);
                                    if (enlace.actualizarAdjuntoPermisoHorasID(id_permiso, path)) {
                                        PrintWriter out = response.getWriter();
                                        out.print("ok");
                                    }
                                }
                            }
                        } else {
                            if (!request.getParameter("etxtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("etxtinicio").equalsIgnoreCase("") && !request.getParameter("etxtfin").equalsIgnoreCase("") && !request.getParameter("ecombomotivo").equalsIgnoreCase("") && !request.getParameter("eareaobservacion").equalsIgnoreCase("")) {
                                int recargo = 0, diasPend, horasPend, minPend, horasTemp;
                                ArrayList<PeriodoVaca> vacas = link.getPeriodosVacaUsuario(enlace.consultarCodigoUsuario(perm.getId_usuario()));
                                for (PeriodoVaca v : vacas) {
                                    diasPend = v.getDiasPend();
                                    horasPend = v.getHorasPend();
                                    minPend = v.getMinPend();
                                    if (minutos + minPend >= 60) {
                                        horasTemp = horas + 1;
                                    } else {
                                        horasTemp = horas;
                                    }
                                    if (horasTemp + horasPend >= 8) {
                                        if (diasPend + 1 == 5) {
                                            recargo = v.getDiasDisp() < 2 ? v.getDiasDisp() : 2;
                                        }
                                    }
                                    elemento.setCierre(recargo * 8 * 60 + horas * 60 + minutos >= v.getDiasDisp() * 8 * 60 + v.getHorasDisp() * 60 + v.getMinDisp() ? v.getPeriodo().substring(5, v.getPeriodo().length()) : "");
                                    break;
                                }
                                elemento.setDias_habiles(0);
                                elemento.setDias_nohabiles(0);
                                elemento.setDias_recargo(recargo);
                                elemento.setDias_descuento(recargo);
                                if (enlace.actualizarPermisoHorasVacaciones(id_permiso, elemento)) {
                                    PrintWriter out = response.getWriter();
                                    HttpSession sesion = request.getSession();
                                    int idUsuLog = Integer.parseInt(sesion.getAttribute("user").toString());
                                    if (enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_permisos")) {
                                        int permiso_pendiente = enlace.getPermisoHorasPendiente(idUsuLog);
                                        enlace.reemplazarPermisoHoras(permiso_pendiente);
                                    }
                                    out.print("ok");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("actualizarPermisoHoras | actualizarPermisoHorasVacaciones | " + ex);
                    }
                }
            } else if (id_tipo == 2) {
                Date fecha_inicio = Date.valueOf(request.getParameter("etxtinicio1"));
                Date fecha_fin = Date.valueOf(request.getParameter("etxtfin1"));
                Date fecha_retorno = Date.valueOf(request.getParameter("etxtingreso"));
                double dias_pendientes = Double.parseDouble(request.getParameter("etxtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("etxtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("etxtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("etxtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("etxtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("etxtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                permiso_horas elemento = new permiso_horas(id_motivo, fecha_solicitud, fecha_inicio, fecha_fin, fecha_retorno, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion, id_tipo);
                try {
                    if (id_motivo != 1) {
                        Part part = request.getPart("etxtadjunto");
                        new File(perm.getAdjunto()).delete();
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        if (enlace.actualizarPermisoHorasDias(id_permiso, elemento)) {
                            if (!request.getParameter("etxtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("etxtinicio1").equalsIgnoreCase("") && !request.getParameter("etxtfin1").equalsIgnoreCase("") && !request.getParameter("ecombomotivo").equalsIgnoreCase("") && !request.getParameter("eareaobservacion").equalsIgnoreCase("") && !request.getPart("etxtadjunto").equals("")) {
                                String path = PATH + id_permiso + "_" + part.getSubmittedFileName();
                                part.write(path);
                                if (enlace.actualizarAdjuntoPermisoHorasID(id_permiso, path)) {
                                    PrintWriter out = response.getWriter();
                                    out.print("ok");
                                }
                            }
                        }
                    } else {
                        if (!request.getParameter("etxtfechasolicitud").equalsIgnoreCase("") && !request.getParameter("etxtinicio1").equalsIgnoreCase("") && !request.getParameter("etxtfin1").equalsIgnoreCase("") && !request.getParameter("ecombomotivo").equalsIgnoreCase("") && !request.getParameter("eareaobservacion").equalsIgnoreCase("")) {
                            if (enlace.actualizarPermisoHorasDias(id_permiso, elemento)) {
                                PrintWriter out = response.getWriter();
                                out.print("ok");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("actualizarPermisoHorasDias | " + ex);
                }
            }
        } else if (accion.equalsIgnoreCase("revisar_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            PrintWriter out = response.getWriter();
            RevisionHoras elemento = new RevisionHoras(id_permiso, id_usuario);
            try {
                if (enlace.registroRevisionHoras(elemento)) {
                    if (enlace.actualizarEstadoPermisoHorasID(1, id_permiso)) {
                        usuario func = enlace.getUsuarioId(enlace.buscarPermisoHoras(id_permiso).getId_usuario());
                        usuario jefe = enlace.getUsuarioId(id_usuario);
                        String[] dest = {func.getCorreo(), jefe.getCorreo()};
                        enlace.enviarCorreoMasivo(dest, "El permiso con ID " + id_permiso + " fue revisado por " + jefe.getApellido() + " " + jefe.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "REVISIÓN DE PERMISO");
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                enlace.actualizarEstadoPermisoHorasID(0, id_permiso);
                System.out.println("registroRevisionHoras | " + ex);
            }
        } else if (accion.equalsIgnoreCase("tiempo_solicitado")) {
            String hora_inicio = request.getParameter("txtinicio");
            String hora_fin = request.getParameter("txtfin");
            String dif = enlace.tiempoPermisoHoras(hora_inicio, hora_fin);
            PrintWriter out = response.getWriter();
            out.print(dif);
        } else if (accion.equalsIgnoreCase("justificante_motivo")) {
            try {
                int id_motivo = Integer.parseInt(request.getParameter("idmov"));
                String descripcion = enlace.obtenerJustificativoMotivoId(id_motivo);
                PrintWriter out = response.getWriter();
                if (!descripcion.isEmpty()) {
                    out.print(descripcion);
                }
            } catch (IOException | NumberFormatException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            PrintWriter out = response.getWriter();
            permiso_horas p = enlace.buscarPermisoHoras(id_permiso);
            if (enlace.eliminarPermisoHorasID(id_permiso)) {
                try {
                    String adjunto = p.getAdjunto(), asistencia = p.getAsistencia();
                    if (!adjunto.equals("ninguno")) {
                        new File(adjunto).delete();
                        if (asistencia != null) {
                            new File(asistencia).delete();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("borrar | " + e);
                }
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("eliminar_horas_ecu")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            PrintWriter out = response.getWriter();
            PermisoECU p = enlace.buscarPermisoHorasECU(id_permiso);
            if (enlace.eliminarPermisoHorasECU(id_permiso)) {
                try {
                    String adjunto = p.getAdjunto();
                    if (adjunto != null) {
                        new File(adjunto).delete();
                    }
                } catch (Exception e) {
                    System.out.println("borrar | " + e);
                }
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("validar_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            try {
                enlace.validarPermisoHorasID(id_permiso);
                PrintWriter out = response.getWriter();
                out.print("ok");
            } catch (Exception e) {
                System.out.println("validarPermisoHorasID | " + e);
            }
        } else if (accion.equalsIgnoreCase("validar_hora_ecu")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            int idUsu = Integer.parseInt(request.getParameter("iu"));
            Part archivo = request.getPart("archivo");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String path = PATH + id_permiso + "_FORMULARIO_ECU_" + archivo.getSubmittedFileName();
            try {
                archivo.write(path);
                enlace.validarPermisoHorasECU(id_permiso, idUsu, path);
                PrintWriter out = response.getWriter();
                out.print("ok");
            } catch (Exception e) {
                System.out.println("validarPermisoHorasECU | " + e);
            }
        } else if (accion.equalsIgnoreCase("adjuntar_asistencia")) {
            int idPermiso = Integer.parseInt(request.getParameter("idper"));
            Part archivo = request.getPart("archivo");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String path = PATH + idPermiso + "_ASISTENCIA_" + archivo.getSubmittedFileName();
            try {
                archivo.write(path);
                enlace.adjuntarAsistenciaPermisoHoras(idPermiso, path);
                PrintWriter out = response.getWriter();
                out.print("ok");
            } catch (Exception e) {
                System.out.println("adjuntarAsistenciaPermisoHoras | " + e);
            }
        } else if (accion.equalsIgnoreCase("aprobar_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            PrintWriter out = response.getWriter();
            permiso_horas permiso = enlace.buscarPermisoHoras(id_permiso);
            AprobacionHoras elemento = new AprobacionHoras(id_permiso, id_usuario);
            try {
                if (enlace.registroAprobacionSolicitud(elemento)) {
                    if (enlace.actualizarEstadoPermisoHorasID(2, id_permiso)) {
                        conexion_oracle link = new conexion_oracle();
                        if (link.registroSolicitudHoras(permiso)) {
                            int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                            usuario func = enlace.getUsuarioId(permiso.getId_usuario());
                            usuario jefe = enlace.getUsuarioId(idJefe);
                            usuario aprueba = enlace.getUsuarioId(id_usuario);
                            String[] dest = {func.getCorreo(), jefe.getCorreo(), aprueba.getCorreo()};
                            enlace.enviarCorreoMasivo(dest, "El permiso con ID " + id_permiso + " fue aprobado por " + aprueba.getApellido() + " " + aprueba.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "APROBACIÓN DE PERMISO");
                            if (permiso.getCierre() != null) {
                                enlace.enviarCorreoMod(enlace.getUsuarioId(permiso.getId_usuario()).getCorreo(), "CIERRE DE PERÍODO DE VACACIONES", "El período de vacaciones " + permiso.getCierre() + " fue cerrado.");
                            }
                            out.print("ok");
                        } else {
                            enlace.actualizarEstadoPermisoHorasID(0, id_permiso);
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("aprobar_horas | " + ex);
            }
        } else if (accion.equalsIgnoreCase("rechazar_hora")) {
            int id_permiso = Integer.parseInt(request.getParameter("txtidper1"));
            int id_usuario = Integer.parseInt(request.getParameter("txtiu1"));
            String descripcion = request.getParameter("arearazon1");
            rechazo_solicitud elemento = new rechazo_solicitud(id_permiso, id_usuario, descripcion);
            PrintWriter out = response.getWriter();
            try {
                if (enlace.registroRechazoHoras(elemento)) {
                    if (enlace.actualizarEstadoPermisoHorasID(3, id_permiso)) {
                        out.print("ok");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("registroRechazoHoras | " + ex);
            }
        } else if (accion.equalsIgnoreCase("rechazar_hora_ecu")) {
            int id_permiso = Integer.parseInt(request.getParameter("txtidper1"));
            int id_usuario = Integer.parseInt(request.getParameter("txtiu1"));
            String descripcion = request.getParameter("arearazon1");
            rechazo_solicitud elemento = new rechazo_solicitud(id_permiso, id_usuario, descripcion);
            PrintWriter out = response.getWriter();
            try {
                if (enlace.registroRechazoHorasECU(elemento)) {
                    out.print("ok");
                }
            } catch (SQLException ex) {
                System.out.println("registroRechazoHorasECU | " + ex);
            }
        } else if (accion.equalsIgnoreCase("anular_hora")) {
            int id_permiso = Integer.parseInt(request.getParameter("txtper"));
            int id_usuario = Integer.parseInt(request.getParameter("txtusu"));
            int motivo = Integer.parseInt(request.getParameter("motivo"));
            Part archivo = request.getPart("archivo");
            AnulacionSolicitud elemento = new AnulacionSolicitud(id_permiso, id_usuario, motivo);
            PrintWriter out = response.getWriter();
            try {
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                archivo.write(PATH + "ANULACION_H_" + id_permiso + ".pdf");
                if (enlace.registroAnulacionHoras(elemento)) {
                    if (enlace.actualizarEstadoPermisoHorasID(4, id_permiso)) {
                        conexion_oracle link = new conexion_oracle();
                        if (link.anularPermiso(id_permiso, "H")) {
                            if (motivo == 1) {
                                out.print("x");
                            } else {
                                out.print("ok");
                            }
                        } else {
                            enlace.actualizarEstadoPermisoHorasID(0, id_permiso);
                            new File(PATH + "ANULACION_H_" + id_permiso + ".pdf").delete();
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("anular_hora | " + ex);
            }
        } else if (accion.equalsIgnoreCase("adjunto_horas")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            String ruta = enlace.buscarPermisoHoras(id_permiso).getAdjunto();
            File ficheroXLS = new File(ruta);
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("descargar_asistencia")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            String ruta = enlace.buscarPermisoHoras(id_permiso).getAsistencia();
            File ficheroXLS = new File(ruta);
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("dias_solicitados")) {
            conexion_oracle oracle = new conexion_oracle();
            Date fecha_ini = Date.valueOf(request.getParameter("txtinicio"));
            Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
            int idUsu = Integer.parseInt(request.getParameter("idusu"));
            int motivo = Integer.parseInt(request.getParameter("motivo"));
            int dias_solicitados = enlace.diferenciaRangoFecha(fecha_ini, fecha_fin);
            int habiles = enlace.obtenerDiasHabilesRangoFecha(fecha_ini, fecha_fin);
            int nohabiles = dias_solicitados - habiles;
            int recargoTest = 0, habilesSol = (int) habiles;
            if (motivo == 1) {
                ArrayList<PeriodoVaca> vacas = oracle.getPeriodosVacaUsuario(idUsu);
                int minTemp = 480, horasConsumo = 0, minutosConsumo = 0, recTemp, diasNoHabDisponibles, minDisp, minSol;
                if (vacas.size() > 0) {
                    PeriodoVaca v = vacas.get(0);
                    if (v.getDiasPend() + v.getHorasPend() + v.getMinPend() > 0) {
                        //HORAS O MINUTOS PENDIENTES
                        if (v.getHorasPend() + v.getMinPend() > 0) {
                            habilesSol--;
                            minTemp -= v.getHorasDisp() * 60 + v.getMinDisp();
                            horasConsumo = (int) minTemp / 60;
                            minutosConsumo = minTemp % 60;
                            v.setDiasPend(v.getDiasPend() + 1);
                            int aux = 0;
                            while ((v.getDiasPend() + aux) % 5 != 0 && habilesSol > 0) {
                                aux++;
                                habilesSol--;
                            }
                            if (habilesSol < v.getDiasHabDisp() - aux) {
                                recTemp = (((int) ((v.getDiasPend() + aux) / 5) + (int) (habilesSol / 5)) * 2);
                                habilesSol = 0;
                            } else {
                                habilesSol -= v.getDiasHabDisp() - aux;
                                recTemp = (((int) ((v.getDiasPend() + aux) / 5) + (int) ((v.getDiasHabDisp() - aux) / 5)) * 2);
                            }
                            diasNoHabDisponibles = v.getDiasDisp() - v.getDiasHabDisp();
                            recTemp = (recTemp > diasNoHabDisponibles ? diasNoHabDisponibles : recTemp);
                            recargoTest += recTemp;
                        } else {
                            //SÓLO PUEDEN HABER DIAS PENDIENTES
                            int aux = 0;
                            while ((v.getDiasPend() + aux) % 5 != 0 && habilesSol > 0) {
                                aux++;
                                habilesSol--;
                            }
                            if (habilesSol < v.getDiasHabDisp() - aux) {
                                recTemp = (((int) ((v.getDiasPend() + aux) / 5) + (int) (habilesSol / 5)) * 2);
                                habilesSol = 0;
                            } else {
                                habilesSol -= v.getDiasHabDisp() - aux;
                                recTemp = (((int) ((v.getDiasPend() + aux) / 5) + (int) ((v.getDiasHabDisp() - aux) / 5)) * 2);
                            }
                            diasNoHabDisponibles = v.getDiasDisp() - v.getDiasHabDisp();
                            recTemp = (recTemp > diasNoHabDisponibles ? diasNoHabDisponibles : recTemp);
                            recargoTest += recTemp;
                        }
                    } else {
                        //CUANDO NO HAY NADA PENDIENTE
                        if (habilesSol - v.getDiasHabDisp() < 0) {
                            recTemp = ((int) (habilesSol / 5) * 2);
                            habilesSol = 0;
                        } else {
                            recTemp = ((int) (v.getDiasHabDisp() / 5) * 2);
                            habilesSol -= v.getDiasHabDisp();
                        }
                        diasNoHabDisponibles = v.getDiasDisp() - v.getDiasHabDisp();
                        recTemp = (recTemp > diasNoHabDisponibles ? diasNoHabDisponibles : recTemp);
                        recargoTest += recTemp;
                    }
                    minDisp = v.getDiasDisp() * 8 * 60 + v.getHorasDisp() * 60 + v.getMinDisp();
                    minSol = (int) habiles * 8 * 60 + recargoTest * 8 * 60;
                    if (vacas.size() == 1 && minDisp >= minSol) {
                        horasConsumo = minutosConsumo = 0;
                    }
                }
                int numPeriodo = 1;
                for (PeriodoVaca v : vacas) {
                    if (numPeriodo > 1) {
                        if (habilesSol + horasConsumo + minutosConsumo > 0) {
                            if (horasConsumo + minutosConsumo > 0) {
                                minDisp = v.getDiasHabDisp() * 8 * 60;
                                minSol = habilesSol * 8 * 60 + horasConsumo * 60 + minutosConsumo;
                                if (minSol >= minDisp) {
                                    recTemp = (int) (v.getDiasHabDisp() / 5) * 2;
                                    recTemp = (recTemp > v.getNoHabiles() ? v.getNoHabiles() : recTemp);
                                    habilesSol -= v.getDiasHabDisp();
                                } else {
                                    recTemp = (int) (habilesSol / 5) * 2;
                                    recTemp = (recTemp > v.getNoHabiles() ? v.getNoHabiles() : recTemp);
                                    habilesSol = horasConsumo = minutosConsumo = 0;
                                }
                            } else {
                                if (habilesSol >= v.getDiasHabDisp()) {
                                    recTemp = (int) (v.getDiasHabDisp() / 5) * 2;
                                    recTemp = (recTemp > v.getNoHabiles() ? v.getNoHabiles() : recTemp);
                                    habilesSol -= v.getDiasHabDisp();
                                } else {
                                    recTemp = (int) (habilesSol / 5) * 2;
                                    recTemp = (recTemp > v.getNoHabiles() ? v.getNoHabiles() : recTemp);
                                    habilesSol = 0;
                                }
                            }
                            recargoTest += recTemp;
                        } else {
                            break;
                        }
                    }
                    numPeriodo++;
                }
                try {
                    if (habilesSol + horasConsumo + minutosConsumo != 0) {
                        throw new Exception("Días solicitados exceden a los disponibles");
                    }
                } catch (Exception ex) {
                    recargoTest = -1;
                    System.out.println("descuentoPeriodoVaca | " + ex);
                }
            } else {
                recargoTest = nohabiles;
            }
            double descuento = habiles + recargoTest;
            PrintWriter out = response.getWriter();
            out.print(dias_solicitados + "," + nohabiles + "," + habiles + "," + recargoTest + "," + descuento);
        } else if (accion.equalsIgnoreCase("dias_solicitados_ecu")) {
            Date fecha_ini = Date.valueOf(request.getParameter("txtinicio"));
            Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
            int dias_disponibles = Integer.parseInt(request.getParameter("disponibles"));
            int dias_solicitados = enlace.diferenciaRangoFecha(fecha_ini, fecha_fin);
            int habiles = enlace.obtenerDiasHabilesRangoFecha(fecha_ini, fecha_fin);
            int nohabiles = dias_solicitados - habiles;
            int recargoTest = 0;
            double descuento = habiles + nohabiles;
            if (dias_disponibles < dias_solicitados) {
                recargoTest = -1;
                System.out.println("Días solicitados exceden a los disponibles");
            }
            PrintWriter out = response.getWriter();
            out.print(dias_solicitados + "," + nohabiles + "," + habiles + "," + recargoTest + "," + descuento);
        } else if (accion.equalsIgnoreCase("registro_vacaciones")) {
            try {
                int idUsuario = Integer.parseInt(request.getParameter("txtidusuario"));
                int codUsu = Integer.parseInt(request.getParameter("codusu"));
                int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio"));
                Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                Date fecha_ingreso = Date.valueOf(request.getParameter("txtfechaingreso"));
                Date fecha_retorno = Date.valueOf(request.getParameter("txtingreso"));
                Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
                double dias_pendientes = Double.parseDouble(request.getParameter("txtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("txtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("txtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("txtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("txtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                String observacion = request.getParameter("areaobservacion");
                String modalidad = request.getParameter("txtmodalidadservidor");
                String periodo = request.getParameter("txtperiodo");
                conexion_oracle link = new conexion_oracle();
                ArrayList<PeriodoVaca> periodos = link.getPeriodosVacaUsuario(enlace.consultarCodigoUsuario(idUsuario));
                String consumo = "", yearsRestantes = "año(s): ";
                int diasDescuentoConsumo = (int) dias_descuento;
                int minTemp = 480, horasConsumo = 0, minutosConsumo = 0;
                int numPeriodo = 1;
                if (periodos.size() > 0) {
                    PeriodoVaca p = periodos.get(0);
                    if (p.getHorasDisp() + p.getMinDisp() > 0) {
                        diasDescuentoConsumo--;
                        minTemp -= p.getHorasDisp() * 60 + p.getMinDisp();
                        horasConsumo = (int) minTemp / 60;
                        minutosConsumo = minTemp % 60;
                        if (diasDescuentoConsumo >= p.getDiasDisp()) {
                            consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            diasDescuentoConsumo -= p.getDiasDisp();
                        } else {
                            consumo += ", " + (diasDescuentoConsumo + 1) + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo < p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                horasConsumo = minutosConsumo = 0;
                            }
                            diasDescuentoConsumo = 0;
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                }
                for (PeriodoVaca p : periodos) {
                    if (diasDescuentoConsumo + horasConsumo + minutosConsumo > 0) {
                        if (horasConsumo + minutosConsumo > 0) {
                            if (numPeriodo > 1) {
                                if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo >= p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                    consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo -= p.getDiasDisp();
                                } else {
                                    consumo += ", " + diasDescuentoConsumo + " día(s)" + (horasConsumo > 0 ? " " + horasConsumo + " hora(s)" : "") + (minutosConsumo > 0 ? " " + minutosConsumo + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo = horasConsumo = minutosConsumo = 0;
                                    yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                                }
                            }
                        } else {
                            if (diasDescuentoConsumo >= p.getDiasDisp()) {
                                consumo += ", " + p.getDiasDisp() + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo -= p.getDiasDisp();
                            } else {
                                consumo += ", " + diasDescuentoConsumo + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo = 0;
                                yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                            }
                        }
                    } else {
                        if (p.getHorasDisp() + p.getMinDisp() == 0) {
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                    numPeriodo++;
                }
                consumo = consumo.substring(2, consumo.length());
                yearsRestantes = yearsRestantes.equals("año(s): ") || dias_restantes == 0 ? "" : yearsRestantes.substring(0, yearsRestantes.length() - 2);
                permiso_vacaciones elemento = new permiso_vacaciones(idUsuario, id_motivo, fecha_inicio, fecha_fin, fecha_retorno, fecha_ingreso, fecha_solicitud, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion, modalidad, periodo, consumo, yearsRestantes);
                int codigo = dias_solicitados > 4 ? 6824 : 6829;
                elemento.setCodigoMotivo(codigo);
                elemento.setCodigoUsu(codUsu);
                PrintWriter out = response.getWriter();
                if (!request.getParameter("areaobservacion").equalsIgnoreCase("")) {
                    if (enlace.registroPermisoVacaciones(elemento) > 0) {
                        out.print("1");
                    } else {
                        out.print("-1");
                    }
                } else {
                    out.print("-2");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (accion.equalsIgnoreCase("editar_vacaciones")) {
            try {
                int idUsuario = Integer.parseInt(request.getParameter("etxtidusuario"));
                int id_permiso = Integer.parseInt(request.getParameter("eid"));
                int id_motivo = Integer.parseInt(request.getParameter("ecombomotivo"));
                Date fecha_inicio = Date.valueOf(request.getParameter("etxtinicio"));
                Date fecha_fin = Date.valueOf(request.getParameter("etxtfin"));
                Date fecha_ingreso = Date.valueOf(request.getParameter("etxtfechaingreso"));
                Date fecha_retorno = Date.valueOf(request.getParameter("etxtingreso"));
                Date fecha_solicitud = Date.valueOf(request.getParameter("etxtfechasolicitud"));
                double dias_pendientes = Double.parseDouble(request.getParameter("etxtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("etxtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("etxtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("etxtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("etxtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("etxtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                String observacion = request.getParameter("eareaobservacion");
                String modalidad = request.getParameter("etxtmodalidadservidor");
                String periodo = request.getParameter("etxtperiodo");
                conexion_oracle link = new conexion_oracle();
                ArrayList<PeriodoVaca> periodos = link.getPeriodosVacaUsuario(enlace.consultarCodigoUsuario(idUsuario));
                String consumo = "", yearsRestantes = "año(s): ";
                int diasDescuentoConsumo = (int) dias_descuento;
                int minTemp = 480, horasConsumo = 0, minutosConsumo = 0;
                int numPeriodo = 1;
                if (periodos.size() > 0) {
                    PeriodoVaca p = periodos.get(0);
                    if (p.getHorasDisp() + p.getMinDisp() > 0) {
                        diasDescuentoConsumo--;
                        minTemp -= p.getHorasDisp() * 60 + p.getMinDisp();
                        horasConsumo = (int) minTemp / 60;
                        minutosConsumo = minTemp % 60;
                        if (diasDescuentoConsumo >= p.getDiasDisp()) {
                            consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            diasDescuentoConsumo -= p.getDiasDisp();
                        } else {
                            consumo += ", " + (diasDescuentoConsumo + 1) + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo < p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                horasConsumo = minutosConsumo = 0;
                            }
                            diasDescuentoConsumo = 0;
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                }
                for (PeriodoVaca p : periodos) {
                    if (diasDescuentoConsumo + horasConsumo + minutosConsumo > 0) {
                        if (horasConsumo + minutosConsumo > 0) {
                            if (numPeriodo > 1) {
                                if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo >= p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                    consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo -= p.getDiasDisp();
                                } else {
                                    consumo += ", " + diasDescuentoConsumo + " día(s)" + (horasConsumo > 0 ? " " + horasConsumo + " hora(s)" : "") + (minutosConsumo > 0 ? " " + minutosConsumo + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo = horasConsumo = minutosConsumo = 0;
                                    yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                                }
                            }
                        } else {
                            if (diasDescuentoConsumo >= p.getDiasDisp()) {
                                consumo += ", " + p.getDiasDisp() + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo -= p.getDiasDisp();
                            } else {
                                consumo += ", " + diasDescuentoConsumo + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo = 0;
                                yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                            }
                        }
                    } else {
                        if (p.getHorasDisp() + p.getMinDisp() == 0) {
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                    numPeriodo++;
                }
                consumo = consumo.substring(2, consumo.length());
                yearsRestantes = yearsRestantes.equals("año(s): ") || dias_restantes == 0 ? "" : yearsRestantes.substring(0, yearsRestantes.length() - 2);
                permiso_vacaciones elemento = new permiso_vacaciones(idUsuario, id_motivo, fecha_inicio, fecha_fin, fecha_retorno, fecha_ingreso, fecha_solicitud, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion, modalidad, periodo, consumo, yearsRestantes);
                int codigo = dias_solicitados > 4 ? 6824 : 6829;
                elemento.setCodigoMotivo(codigo);
                PrintWriter out = response.getWriter();
                if (!request.getParameter("eareaobservacion").equalsIgnoreCase("")) {
                    if (enlace.actualizarPermisoVacaciones(id_permiso, elemento)) {
                        HttpSession sesion = request.getSession();
                        int idUsuLog = Integer.parseInt(sesion.getAttribute("user").toString());
                        if (enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_permisos")) {
                            int permiso_pendiente = enlace.getVacacionPendiente(idUsuLog);
                            enlace.reemplazarVacacion(permiso_pendiente);
                        }
                        out.print("1");
                    } else {
                        out.print("-1");
                    }
                } else {
                    out.print("-2");
                }
            } catch (Exception e) {
                System.out.println("actualizarPermisoVacaciones | " + e);
            }
        } else if (accion.equalsIgnoreCase("eliminar_vacacion")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            PrintWriter out = response.getWriter();
            if (enlace.eliminarPermisoVacacionID(id_permiso)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("aprobar_vacacion")) {
            conexion_oracle link = new conexion_oracle();
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            PrintWriter out = response.getWriter();
            aprobacion_vacaciones elemento = new aprobacion_vacaciones(id_permiso, id_usuario);
            try {
                if (enlace.registroAprobacionVacaciones(elemento)) {
                    if (enlace.actualizarEstadoPermisoVacacionID(2, id_permiso)) {
                        permiso_vacaciones vacacion = enlace.buscarPermisoVacacion(id_permiso);
                        String[] periodosConsumo = vacacion.getConsumo().split(",");
                        String[] yearsRestantes = vacacion.getYearsRestantes().split(",");
                        String periodos = "";
                        if (periodosConsumo.length - yearsRestantes.length > 0) {
                            int numPer = 1;
                            for (String perCon : periodosConsumo) {
                                if (numPer <= periodosConsumo.length - yearsRestantes.length) {
                                    periodos += perCon.split("año ")[1] + ", ";
                                    numPer++;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (link.registroSolicitudVacaciones(vacacion)) {
                            int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(vacacion.getDireccion())));
                            usuario func = enlace.getUsuarioId(vacacion.getId_usuario());
                            usuario jefe = enlace.getUsuarioId(idJefe);
                            usuario aprueba = enlace.getUsuarioId(id_usuario);
                            String[] dest = {func.getCorreo(), jefe.getCorreo(), aprueba.getCorreo()};
                            enlace.enviarCorreoMasivo(dest, "El permiso de vacaciones con ID " + id_permiso + " fue aprobado por " + aprueba.getApellido() + " " + aprueba.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "APROBACIÓN DE VACACIONES");
                            if (!periodos.equals("")) {
                                enlace.enviarCorreoMod(enlace.getUsuarioId(vacacion.getId_usuario()).getCorreo(), "CIERRE DE PERÍODO DE VACACIONES", "El(los) período(s) de vacaciones " + periodos.substring(0, periodos.length() - 2) + " fue(ron) cerrado(s).");
                            }
                            out.print("1");
                        } else {
                            enlace.actualizarEstadoPermisoVacacionID(0, id_permiso);
                            out.print("-1");
                        }
                    } else {
                        out.print("-2");
                    }
                } else {
                    out.print("-3");
                }
            } catch (SQLException ex) {
                System.out.println("aprobar_vacacion | " + ex);
            }
        } else if (accion.equalsIgnoreCase("rechazar_vacacion")) {
//            int id_permiso = Integer.parseInt(request.getParameter("txtidper1"));
//            int id_usuario = Integer.parseInt(request.getParameter("txtiu11"));
//            String descripcion = request.getParameter("arearazon1");
            int id_permiso, id_usuario;
            String descripcion;
            if (request.getParameter("txtidper1") != null) {
                id_permiso = Integer.parseInt(request.getParameter("txtidper1"));
                id_usuario = Integer.parseInt(request.getParameter("txtiu11"));
                descripcion = request.getParameter("arearazon1");
            } else {
                id_permiso = Integer.parseInt(request.getParameter("txtidper1vac"));
                id_usuario = Integer.parseInt(request.getParameter("txtiu11vac"));
                descripcion = request.getParameter("arearazon1vac");
            }
            rechazo_vacaciones elemento = new rechazo_vacaciones(id_permiso, id_usuario, descripcion);
            PrintWriter out = response.getWriter();
            try {
                if (enlace.registroRechazoVacaciones(elemento)) {
                    if (enlace.actualizarEstadoPermisoVacacionID(3, id_permiso)) {
                        out.print("ok");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("rechazar_vacacion | " + ex);
            }
        } else if (accion.equalsIgnoreCase("anular_vacacion")) {
//            int id_permiso = Integer.parseInt(request.getParameter("txtper"));
//            int id_usuario = Integer.parseInt(request.getParameter("txtusu"));
//            int motivo = Integer.parseInt(request.getParameter("motivo"));
//            Part archivo = request.getPart("archivo");
            int id_permiso, id_usuario, motivo;
            Part archivo;
            if (request.getParameter("txtper") != null) {
                id_permiso = Integer.parseInt(request.getParameter("txtper"));
                id_usuario = Integer.parseInt(request.getParameter("txtusu"));
                motivo = Integer.parseInt(request.getParameter("motivo"));
                archivo = request.getPart("archivo");
            } else {
                id_permiso = Integer.parseInt(request.getParameter("txtpervac"));
                id_usuario = Integer.parseInt(request.getParameter("txtusuvac"));
                motivo = Integer.parseInt(request.getParameter("motivovac"));
                archivo = request.getPart("archivovac");
            }
            AnulacionVacaciones elemento = new AnulacionVacaciones(id_permiso, id_usuario, motivo);
            PrintWriter out = response.getWriter();
            try {
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                archivo.write(PATH + "ANULACION_" + id_permiso + ".pdf");
                if (enlace.registroAnulacionVacaciones(elemento)) {
                    if (enlace.actualizarEstadoPermisoVacacionID(4, id_permiso)) {
                        conexion_oracle link = new conexion_oracle();
                        if (link.anularPermiso(id_permiso, "V")) {
                            permiso_vacaciones vacacion = enlace.buscarPermisoVacacion(id_permiso);
                            AnulacionVacaciones anulacion = enlace.obtenerAnulacionSolicitudVacaciones(id_permiso);
                            int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(vacacion.getDireccion())));
                            usuario func = enlace.getUsuarioId(vacacion.getId_usuario());
                            usuario jefe = enlace.getUsuarioId(idJefe);
                            usuario anula = enlace.getUsuarioId(id_usuario);
                            String[] dest = {func.getCorreo(), jefe.getCorreo(), anula.getCorreo()};
                            enlace.enviarCorreoMasivo(dest, "El permiso de vacaciones con ID " + id_permiso + " fue anulado por " + anula.getApellido() + " " + anula.getNombre() + ", con el motivo: " + anulacion.getMotivo() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "ANULACIÓN DE VACACIONES");
                            if (motivo == 1) {
                                out.print("x");
                            } else {
                                out.print("ok");
                            }
                        } else {
                            enlace.actualizarEstadoPermisoVacacionID(0, id_permiso);
                            new File(PATH + "ANULACION_" + id_permiso + ".pdf").delete();
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("anular_vacacion | " + ex);
            }
        } else if (accion.equalsIgnoreCase("revisar_vacacion")) {
            int id_permiso = Integer.parseInt(request.getParameter("ipe"));
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            PrintWriter out = response.getWriter();
            revision_vacaciones elemento = new revision_vacaciones(id_permiso, id_usuario);
            try {
                if (enlace.registroRevisionVacaciones(elemento)) {
                    if (enlace.actualizarEstadoPermisoVacacionID(1, id_permiso)) {
                        usuario func = enlace.getUsuarioId(enlace.permisoVacacionesID(id_permiso).getId_usuario());
                        usuario jefe = enlace.getUsuarioId(id_usuario);
                        String[] dest = {func.getCorreo(), jefe.getCorreo()};
                        enlace.enviarCorreoMasivo(dest, "El permiso de vacaciones con ID " + id_permiso + " fue revisado por " + jefe.getApellido() + " " + jefe.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "REVISIÓN DE VACACIONES");
                        out.print("ok");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("fecha_ingreso")) {
            java.sql.Date fecha_ingreso = null;
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
            fecha_ingreso = enlace.diaIngreso(fecha_fin);
            PrintWriter out = response.getWriter();
            out.print(fecha_ingreso);
        } else if (accion.equalsIgnoreCase("fecha_ingreso_calendario")) {
            java.sql.Date fecha_ingreso = null;
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
            fecha_ingreso = enlace.diaIngresoCalendario(fecha_fin);
            PrintWriter out = response.getWriter();
            out.print(fecha_ingreso);
        } else if (accion.equalsIgnoreCase("disponibilidad_periodo")) {
            conexion_oracle link = new conexion_oracle();
            String codigo_usuario = request.getParameter("cod_usu");
            String periodo = request.getParameter("perio");
            int disponibilidad = link.obtenerDisponibilidadUsuarioPeriodo(periodo, codigo_usuario);
            PrintWriter out = response.getWriter();
            out.print(disponibilidad);
        } else if (accion.equalsIgnoreCase("consultar_permiso")) {
            int id = Integer.parseInt(request.getParameter("id"));
            PrintWriter out = response.getWriter();
            permiso_horas p = enlace.buscarPermisoHoras(id);
            if (p.getId_permiso() != 0) {
                if (p.getId_estado() == 0 || p.getId_estado() == 1) {
                    usuario u = null;
                    String res = p.getFecha() + "*";
                    if (p.getId_usuario() != 0) {
                        u = enlace.buscar_usuarioID(p.getId_usuario());
                        res += u.getApellido() + " " + u.getNombre() + "*";
                    } else {
                        res += p.getNombreUsu() + "*";
                    }
                    res += p.getId_motivo() + "*"
                            + p.getDenominacion() + "*"
                            + p.getDireccion() + "*";
                    if (p.getId_tipo() == 1) {
                        res += p.getHora_salida() + "*"
                                + p.getHora_entrada() + "*"
                                + p.getTiempo_solicita() + "*";
                    } else {
                        res += p.getFecha_inicio() + "*"
                                + p.getFecha_fin() + "*"
                                + p.getDias_solicitados() + " día(s)*";
                    }
                    conexion_oracle oracle = new conexion_oracle();
                    String modalidad = oracle.getRegimen(u == null ? p.getCodigoUsu() : Integer.parseInt(u.getCodigo_usuario()));
                    res += modalidad + "*" + p.getObservacion() + "*" + (!p.getAdjunto().equals("ninguno") ? "ADJ" : "x") + "*" + (p.getAsistencia() != null ? "ASIS" : "x");
                    if (p.getId_motivo() == 4 && p.getAsistencia() == null) {
                        res = "incompleto";
                    }
                    out.print(res);
                } else {
                    out.print("x");
                }
            } else {
                out.print("null");
            }
        } else if (accion.equalsIgnoreCase("aprobdir")) {
            int id = Integer.parseInt(request.getParameter("id"));
            permiso_horas permiso = enlace.buscarPermisoHoras(id);
            int ap = Integer.parseInt(request.getParameter("ap"));
            PrintWriter out = response.getWriter();
            try {
                conexion_oracle link = new conexion_oracle();
                int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                if (idJefe != 0) {
                    RevisionHoras rh = new RevisionHoras(id, idJefe);
                    AprobacionHoras ah = new AprobacionHoras(id, ap);
                    if (enlace.registroRevisionHoras(rh)) {
                        if (enlace.registroAprobacionSolicitud(ah)) {
                            if (enlace.actualizarEstadoPermisoHorasID(2, id)) {
                                if (link.registroSolicitudHoras(permiso)) {
                                    usuario func = enlace.getUsuarioId(permiso.getId_usuario());
                                    usuario jefe = enlace.getUsuarioId(idJefe);
                                    usuario aprueba = enlace.getUsuarioId(ap);
                                    String[] dest = {func.getCorreo(), jefe.getCorreo(), aprueba.getCorreo()};
                                    enlace.enviarCorreoMasivo(dest, "El permiso con ID " + id + " fue aprobado directamente por " + aprueba.getApellido() + " " + aprueba.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "APROBACIÓN DIRECTA DE PERMISO");
                                    if (permiso.getCierre() != null) {
                                        enlace.enviarCorreoMod(enlace.getUsuarioId(permiso.getId_usuario()).getCorreo(), "CIERRE DE PERÍODO DE VACACIONES", "El período de vacaciones " + permiso.getCierre() + " fue cerrado.");
                                    }
                                    out.print("x");
                                } else {
                                    enlace.actualizarEstadoPermisoHorasID(0, id);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("registroRevisionHoras || registroAprobacionSolicitud || registroSolicitudHoras | " + ex);
            }
        } else if (accion.equalsIgnoreCase("consultar_vaca")) {
            int id = Integer.parseInt(request.getParameter("id"));
            PrintWriter out = response.getWriter();
            permiso_vacaciones p = enlace.buscarPermisoVacacion(id);
            if (p.getId_permiso() != 0) {
                if (p.getEstado() == 0 || p.getEstado() == 1) {
                    usuario u = null;
                    String res = p.getFecha_solicitud() + "*";
                    if (p.getId_usuario() != 0) {
                        u = enlace.buscar_usuarioID(p.getId_usuario());
                        res += u.getApellido() + " " + u.getNombre() + "*";
                    } else {
                        res += p.getNombreUsu() + "*";
                    }
                    res += p.getId_motivo() + "*"
                            + p.getFecha_labor() + "*"
                            + p.getDenominacion() + "*"
                            + p.getDireccion() + "*"
                            + p.getModalidad() + "*"
                            + p.getFecha_inicio() + "*"
                            + p.getFecha_fin() + "*"
                            + p.getFecha_ingreso() + "*"
                            + p.getDias_solicitados() + " día(s)*"
                            + p.getConsumo() + "*"
                            + p.getDias_pendientes() + "*"
                            + p.getObservacion() + "*";
                    out.print(res);
                } else {
                    out.print("x");
                }
            } else {
                out.print("null");
            }
        } else if (accion.equalsIgnoreCase("aprobdirv")) {
            int id = Integer.parseInt(request.getParameter("id"));
            int ap = Integer.parseInt(request.getParameter("ap"));
            permiso_vacaciones permiso = enlace.buscarPermisoVacacion(id);
            PrintWriter out = response.getWriter();
            try {
                conexion_oracle link = new conexion_oracle();
                int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                if (idJefe != 0) {
                    revision_vacaciones r = new revision_vacaciones(id, idJefe);
                    aprobacion_vacaciones a = new aprobacion_vacaciones(id, ap);
                    permiso_vacaciones vacacion = enlace.buscarPermisoVacacion(id);
                    if (enlace.registroRevisionVacaciones(r)) {
                        if (enlace.registroAprobacionVacaciones(a)) {
                            if (enlace.actualizarEstadoPermisoVacacionID(2, id)) {
                                String[] periodosConsumo = vacacion.getConsumo().split(",");
                                String[] yearsRestantes = vacacion.getYearsRestantes().split(",");
                                String periodos = "";
                                if (periodosConsumo.length - yearsRestantes.length > 0) {
                                    int numPer = 1;
                                    for (String perCon : periodosConsumo) {
                                        if (numPer <= periodosConsumo.length - yearsRestantes.length) {
                                            periodos += perCon.split("año ")[1] + ", ";
                                            numPer++;
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                if (link.registroSolicitudVacaciones(vacacion)) {
                                    usuario func = enlace.getUsuarioId(permiso.getId_usuario());
                                    usuario jefe = enlace.getUsuarioId(idJefe);
                                    usuario aprueba = enlace.getUsuarioId(ap);
                                    String[] dest = {func.getCorreo(), jefe.getCorreo(), aprueba.getCorreo()};
                                    enlace.enviarCorreoMasivo(dest, "El permiso de vacaciones con ID " + id + " fue aprobado directamente por " + aprueba.getApellido() + " " + aprueba.getNombre() + ". Solicitante: " + func.getApellido() + " " + func.getNombre() + " - " + enlace.getDireccionUsuarioId(func.getId_usuario()), "APROBACIÓN DIRECTA DE VACACIONES");
                                    if (!periodos.equals("")) {
                                        enlace.enviarCorreoMod(enlace.getUsuarioId(vacacion.getId_usuario()).getCorreo(), "CIERRE DE PERÍODO DE VACACIONES", "El(los) período(s) de vacaciones " + periodos.substring(0, periodos.length() - 2) + " fue(ron) cerrado(s).");
                                    }
                                    out.print("x");
                                } else {
                                    enlace.actualizarEstadoPermisoVacacionID(0, id);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("registroRevisionVacaciones || registroAprobacionVacaciones || registroSolicitudVacaciones | " + ex);
                out.print(ex);
            }
        } else if (accion.equalsIgnoreCase("actdir")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String idDir = request.getParameter("idDir");
            String cargo = request.getParameter("cargo");
            PrintWriter out = response.getWriter();
            conexion_oracle oracle = new conexion_oracle();
            String dir = oracle.consultarDireccion(idDir), jefe = oracle.consultarJefeDireccion(idDir);
            int codJefe = oracle.consultarCodigoJefeDireccion(idDir);
            String cargoJefe = oracle.consultarDenominacionUsuario(codJefe);
            if (!dir.equals("") && !jefe.equals("")) {
                if (enlace.actualizarDireccionVacacion(id, cargo, dir, jefe, cargoJefe)) {
                    out.print("x");
                }
            } else {
                out.print("Error al consultar datos de dirección");
            }
        } else if (accion.equalsIgnoreCase("actdirhoras")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String idDir = request.getParameter("idDir");
            PrintWriter out = response.getWriter();
            conexion_oracle oracle = new conexion_oracle();
            String dir = oracle.consultarDireccion(idDir);
            if (!dir.equals("")) {
                if (enlace.actualizarDireccionHoras(id, dir)) {
                    out.print("x");
                }
            } else {
                out.print("Error al consultar datos de dirección");
            }
        } else if (accion.equalsIgnoreCase("descargar_anulacion_vaca")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            File ficheroXLS = new File(PATH + "ANULACION_" + id_permiso + ".pdf");
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("descargar_anulacion_h")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            File ficheroXLS = new File(PATH + "ANULACION_H_" + id_permiso + ".pdf");
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("diferencia_dias")) {
            java.sql.Date fecha = Date.valueOf(request.getParameter("fecha"));
            PrintWriter out = response.getWriter();
            out.print(enlace.getDiferenciaDias(fecha));
        } else if (accion.equalsIgnoreCase("diferencia_dias_habiles")) {
            java.sql.Date fecha = Date.valueOf(request.getParameter("fecha"));
            PrintWriter out = response.getWriter();
            out.print(enlace.getDiferenciaDiasHabiles(fecha));
        } else if (accion.equalsIgnoreCase("registro_horas_iess")) {
            int id_admin = Integer.parseInt(request.getParameter("txtadmin"));
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario"));
            Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
            String observacion = request.getParameter("areaobservacion");
            String direccion = request.getParameter("txtunidadservidor");
            String cargo = request.getParameter("txtcargoservidor");
            conexion_oracle link = new conexion_oracle();
            int idPermiso = 0;
            if (id_tipo == 1) {
                String hora_salida = request.getParameter("txtinicio");
                String hora_entrada = request.getParameter("txtfin");
                String tiempo = request.getParameter("txttiempo");
                int horas = Integer.parseInt(request.getParameter("txthoras"));
                int minutos = Integer.parseInt(request.getParameter("txtminutos"));
                if ((horas == 7 && minutos == 0) || horas < 7) {
                    permiso_horas elemento = new permiso_horas(id_usuario, hora_entrada, hora_salida, id_motivo, fecha_solicitud, tiempo, horas, minutos, observacion.equals("") ? "N/A" : observacion, id_tipo);
                    elemento.setFecha_inicio(fecha_solicitud);
                    elemento.setFecha_fin(fecha_solicitud);
                    elemento.setFecha_ingreso(fecha_solicitud);
                    elemento.setDireccion(direccion);
                    elemento.setDenominacion(cargo);
                    elemento.setCodigoUsu(id_usuario);
                    try {
                        idPermiso = enlace.registroPermisoHorasIESS(elemento);
                        if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                            Part part = request.getPart("txtadjunto");
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                            part.write(path);
                            enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path);
                        }
                    } catch (SQLException ex) {
                        System.out.println("registroPermisoHoras_iess | " + ex);
                    }
                }
            } else {
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio1"));
                Date fecha_fin = Date.valueOf(request.getParameter("txtfin1"));
                Date fecha_retorno = Date.valueOf(request.getParameter("txtingreso"));
                double dias_pendientes = Double.parseDouble(request.getParameter("txtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("txtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("txtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("txtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("txtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                permiso_horas elemento = new permiso_horas(id_usuario, id_motivo, fecha_solicitud, fecha_inicio, fecha_fin, fecha_retorno, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion.equals("") ? "N/A" : observacion, id_tipo);
                elemento.setDireccion(direccion);
                elemento.setDenominacion(cargo);
                elemento.setCodigoUsu(id_usuario);
                try {
                    idPermiso = enlace.registroPermisoHorasDiasIESS(elemento);
                    if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                        Part part = request.getPart("txtadjunto");
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                        part.write(path);
                        enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path);
                    }
                } catch (SQLException ex) {
                    System.out.println("registroPermisoHoras_iess | " + ex);
                }
            }
            //APROBACIÓN
            int id = idPermiso;
            permiso_horas permiso = enlace.buscarPermisoHoras(id);
            int ap = id_admin;
            PrintWriter out = response.getWriter();
            try {
                int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                RevisionHoras rh = new RevisionHoras(id, idJefe == 0 ? ap : idJefe);
                AprobacionHoras ah = new AprobacionHoras(id, ap);
                if (enlace.registroRevisionHoras(rh)) {
                    if (enlace.registroAprobacionSolicitud(ah)) {
                        if (enlace.actualizarEstadoPermisoHorasID(2, id)) {
                            if (link.registroSolicitudHoras(permiso)) {
                                out.print("x");
                            } else {
                                enlace.eliminarPermisoHorasID(id);
                                if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                                    String path = PATH + idPermiso + "_" + request.getPart("txtadjunto").getSubmittedFileName();
                                    new File(path).delete();
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("IESS registroRevisionHoras | registroAprobacionSolicitud |" + ex);
            }
        } else if (accion.equalsIgnoreCase("registro_horas_admin")) {
            int id_admin = Integer.parseInt(request.getParameter("txtadmin"));
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario"));
            String nombreUsu = request.getParameter("txtnombreusu");
            Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
            String observacion = request.getParameter("areaobservacion");
            conexion_oracle link = new conexion_oracle();
            int idPermiso = 0;
            if (id_tipo == 1) {
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio1"));
                String hora_salida = request.getParameter("txtinicio");
                String hora_entrada = request.getParameter("txtfin");
                String tiempo = request.getParameter("txttiempo");
                int horas = Integer.parseInt(request.getParameter("txthoras"));
                int minutos = Integer.parseInt(request.getParameter("txtminutos"));
                if ((horas == 7 && minutos == 0) || horas < 7) {
                    permiso_horas elemento = new permiso_horas(id_usuario, hora_entrada, hora_salida, id_motivo, fecha_solicitud, tiempo, horas, minutos, observacion.equals("") ? "N/A" : observacion, id_tipo);
                    elemento.setFecha_inicio(fecha_inicio);
                    elemento.setFecha_fin(fecha_inicio);
                    elemento.setFecha_ingreso(fecha_inicio);
                    elemento.setCodigoUsu(id_usuario);
                    elemento.setNombreUsu(nombreUsu);
                    try {
                        if (id_motivo != 1) {
                            idPermiso = enlace.registroPermisoHoras(elemento);
                            if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                                Part part = request.getPart("txtadjunto");
                                File fileDir = new File(PATH);
                                if (!fileDir.exists()) {
                                    fileDir.mkdirs();
                                }
                                String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                                part.write(path);
                                enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path);
                            }
                        } else {
                            int recargo = 0, diasPend, horasPend, minPend, horasTemp;
                            ArrayList<PeriodoVaca> vacas = link.getPeriodosVacaUsuario(id_usuario);
                            for (PeriodoVaca v : vacas) {
                                diasPend = v.getDiasPend();
                                horasPend = v.getHorasPend();
                                minPend = v.getMinPend();
                                if (minutos + minPend >= 60) {
                                    horasTemp = horas + 1;
                                } else {
                                    horasTemp = horas;
                                }
                                if (horasTemp + horasPend >= 8) {
                                    if (diasPend + 1 == 5) {
                                        recargo = v.getDiasDisp() < 2 ? v.getDiasDisp() : 2;
                                    }
                                }
                                elemento.setCierre(recargo * 8 * 60 + horas * 60 + minutos >= v.getDiasDisp() * 8 * 60 + v.getHorasDisp() * 60 + v.getMinDisp() ? v.getPeriodo().substring(5, v.getPeriodo().length()) : "");
                                break;
                            }
                            elemento.setDias_habiles(0);
                            elemento.setDias_nohabiles(0);
                            elemento.setDias_recargo(recargo);
                            elemento.setDias_descuento(recargo);
                            idPermiso = enlace.registroPermisoHorasVacaciones(elemento);
                        }
                    } catch (SQLException ex) {
                        System.out.println("registroPermisoHoras | " + ex);
                    }
                }
            } else if (id_tipo == 2) {
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio1"));
                Date fecha_fin = Date.valueOf(request.getParameter("txtfin1"));
                Date fecha_retorno = Date.valueOf(request.getParameter("txtingreso"));
                double dias_pendientes = Double.parseDouble(request.getParameter("txtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("txtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("txtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("txtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("txtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                permiso_horas elemento = new permiso_horas(id_usuario, id_motivo, fecha_solicitud, fecha_inicio, fecha_fin, fecha_retorno, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion.equals("") ? "N/A" : observacion, id_tipo);
                elemento.setCodigoUsu(id_usuario);
                elemento.setNombreUsu(nombreUsu);
                try {
                    if (id_motivo != 1) {
                        idPermiso = enlace.registroPermisoHorasDias(elemento);
                        if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                            Part part = request.getPart("txtadjunto");
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            String path = PATH + idPermiso + "_" + part.getSubmittedFileName();
                            part.write(path);
                            enlace.actualizarAdjuntoPermisoHorasID(idPermiso, path);
                        }
                    } else {
                        idPermiso = enlace.registroPermisoHorasDias(elemento);
                    }
                } catch (SQLException ex) {
                    System.out.println("registroPermisoHorasDias | " + ex);
                }
            }
            //APROBACIÓN
            if (idPermiso != 0) {
                int id = idPermiso;
                permiso_horas permiso = enlace.buscarPermisoHoras(id);
                int ap = id_admin;
                PrintWriter out = response.getWriter();
                try {
                    int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                    RevisionHoras rh = new RevisionHoras(id, idJefe == 0 ? ap : idJefe);
                    AprobacionHoras ah = new AprobacionHoras(id, ap);
                    if (enlace.registroRevisionHoras(rh)) {
                        if (enlace.registroAprobacionSolicitud(ah)) {
                            if (enlace.actualizarEstadoPermisoHorasID(2, id)) {
                                if (link.registroSolicitudHoras(permiso)) {
                                    HttpSession sesion = request.getSession();
                                    int idUsuLog = Integer.parseInt(sesion.getAttribute("user").toString());
                                    if (enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_permisos") || enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_iess")) {
                                        int permiso_pendiente = enlace.getPermisoHorasPendiente(idUsuLog);
                                        enlace.reemplazarPermisoHoras(permiso_pendiente);
                                    }
                                    out.print("x");
                                } else {
                                    enlace.eliminarPermisoHorasID(id);
                                    if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                                        String path = PATH + idPermiso + "_" + request.getPart("txtadjunto").getSubmittedFileName();
                                        new File(path).delete();
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("registro_horas_admin | registroAprobacionSolicitud |" + ex);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_vacaciones_admin")) {
            try {
                int id_admin = Integer.parseInt(request.getParameter("txtadmin"));
                int idUsuario = Integer.parseInt(request.getParameter("txtidusuario"));
                String nombreUsu = request.getParameter("txtnombreusu");
                int id_motivo = Integer.parseInt(request.getParameter("combomotivo"));
                Date fecha_inicio = Date.valueOf(request.getParameter("txtinicio"));
                Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                Date fecha_ingreso = Date.valueOf(request.getParameter("txtfechaingreso"));
                Date fecha_retorno = Date.valueOf(request.getParameter("txtingreso"));
                Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
                double dias_pendientes = Double.parseDouble(request.getParameter("txtdiaspendientes"));
                int dias_solicitados = Integer.parseInt(request.getParameter("txtdiassolicitados"));
                double dias_habiles = Double.parseDouble(request.getParameter("txtdiashabiles"));
                double dias_nohabiles = Double.parseDouble(request.getParameter("txtdiasnohabiles"));
                double dias_recargo = Double.parseDouble(request.getParameter("txtdiasrecargo"));
                double dias_descuento = Double.parseDouble(request.getParameter("txtdiasdescuento"));
                double restantes = dias_pendientes - dias_descuento;
                double dias_restantes = enlace.limitarDecimales(restantes);
                String observacion = request.getParameter("areaobservacion");
                String modalidad = request.getParameter("txtmodalidadservidor");
                String periodo = request.getParameter("txtperiodo");
                conexion_oracle link = new conexion_oracle();
                ArrayList<PeriodoVaca> periodos = link.getPeriodosVacaUsuario(idUsuario);
                String consumo = "", yearsRestantes = "año(s): ";
                int diasDescuentoConsumo = (int) dias_descuento;
                int minTemp = 480, horasConsumo = 0, minutosConsumo = 0;
                int numPeriodo = 1;
                if (periodos.size() > 0) {
                    PeriodoVaca p = periodos.get(0);
                    if (p.getHorasDisp() + p.getMinDisp() > 0) {
                        diasDescuentoConsumo--;
                        minTemp -= p.getHorasDisp() * 60 + p.getMinDisp();
                        horasConsumo = (int) minTemp / 60;
                        minutosConsumo = minTemp % 60;
                        if (diasDescuentoConsumo >= p.getDiasDisp()) {
                            consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            diasDescuentoConsumo -= p.getDiasDisp();
                        } else {
                            consumo += ", " + (diasDescuentoConsumo + 1) + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                            if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo < p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                horasConsumo = minutosConsumo = 0;
                            }
                            diasDescuentoConsumo = 0;
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                }
                for (PeriodoVaca p : periodos) {
                    if (diasDescuentoConsumo + horasConsumo + minutosConsumo > 0) {
                        if (horasConsumo + minutosConsumo > 0) {
                            if (numPeriodo > 1) {
                                if (diasDescuentoConsumo * 8 * 60 + horasConsumo * 60 + minutosConsumo >= p.getDiasDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp()) {
                                    consumo += ", " + p.getDiasDisp() + " día(s)" + (p.getHorasDisp() > 0 ? " " + p.getHorasDisp() + " hora(s)" : "") + (p.getMinDisp() > 0 ? " " + p.getMinDisp() + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo -= p.getDiasDisp();
                                } else {
                                    consumo += ", " + diasDescuentoConsumo + " día(s)" + (horasConsumo > 0 ? " " + horasConsumo + " hora(s)" : "") + (minutosConsumo > 0 ? " " + minutosConsumo + " minuto(s)" : "") + " del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                    diasDescuentoConsumo = horasConsumo = minutosConsumo = 0;
                                    yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                                }
                            }
                        } else {
                            if (diasDescuentoConsumo >= p.getDiasDisp()) {
                                consumo += ", " + p.getDiasDisp() + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo -= p.getDiasDisp();
                            } else {
                                consumo += ", " + diasDescuentoConsumo + " día(s) del año " + p.getPeriodo().substring(5, p.getPeriodo().length());
                                diasDescuentoConsumo = 0;
                                yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                            }
                        }
                    } else {
                        if (p.getHorasDisp() + p.getMinDisp() == 0) {
                            yearsRestantes += p.getPeriodo().substring(5, p.getPeriodo().length()) + ", ";
                        }
                    }
                    numPeriodo++;
                }
                consumo = consumo.substring(2, consumo.length());
                yearsRestantes = yearsRestantes.equals("año(s): ") || dias_restantes == 0 ? "" : yearsRestantes.substring(0, yearsRestantes.length() - 2);
                permiso_vacaciones elemento = new permiso_vacaciones(idUsuario, id_motivo, fecha_inicio, fecha_fin, fecha_retorno, fecha_ingreso, fecha_solicitud, dias_solicitados, dias_restantes, dias_habiles, dias_nohabiles, dias_recargo, dias_descuento, observacion.equals("") ? "N/A" : observacion, modalidad, periodo, consumo, yearsRestantes);
                elemento.setCodigoMotivo(dias_solicitados > 4 ? 6824 : 6829);
                elemento.setCodigoUsu(idUsuario);
                elemento.setNombreUsu(nombreUsu);
                PrintWriter out = response.getWriter();
                int idVaca = enlace.registroPermisoVacaciones(elemento);
                //APROBACIÓN
                int id = idVaca;
                int ap = id_admin;
                permiso_vacaciones permiso = enlace.buscarPermisoVacacion(id);
                int idJefe = enlace.consultarIdUsuarioByCodigo(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(permiso.getDireccion())));
                revision_vacaciones r = new revision_vacaciones(id, idJefe == 0 ? ap : idJefe);
                aprobacion_vacaciones a = new aprobacion_vacaciones(id, ap);
                if (enlace.registroRevisionVacaciones(r)) {
                    if (enlace.registroAprobacionVacaciones(a)) {
                        if (enlace.actualizarEstadoPermisoVacacionID(2, id)) {
                            if (link.registroSolicitudVacaciones(permiso)) {
                                HttpSession sesion = request.getSession();
                                int idUsuLog = Integer.parseInt(sesion.getAttribute("user").toString());
                                if (enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_permisos") || enlace.verificarUsuarioCumpleRol(idUsuLog, "encargado_iess")) {
                                    int permiso_pendiente = enlace.getVacacionPendiente(idUsuLog);
                                    enlace.reemplazarVacacion(permiso_pendiente);
                                }
                                out.print("1");
                            } else {
                                enlace.eliminarPermisoVacacionID(id);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                PrintWriter out = response.getWriter();
                out.print("-1");
                System.out.println("registro_vacaciones_admin | " + e);
            }
        } else if (accion.equalsIgnoreCase("configurar_firma")) {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                enlace.setFirmaPerVac(id);
                PrintWriter out = response.getWriter();
                out.print("X");
            } catch (SQLException e) {
                System.out.println("setFirmaPerVac | " + e);
            }
        } else if (accion.equalsIgnoreCase("registro_manual")) {
            int idAmin = Integer.parseInt(request.getParameter("idadmin"));
            int codFunc = Integer.parseInt(request.getParameter("codusu"));
            String denominacion = request.getParameter("txtcargoservidor");
            String direccion = request.getParameter("txtunidadservidor");
            String jefe = request.getParameter("jefe");
            String cargoJefe = request.getParameter("cargojefe");
            Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            String observacion = request.getParameter("areaobservacion");
            conexion_oracle link = new conexion_oracle();
            int diasHabiles = 0, finesSemana = 0, horas = Integer.parseInt(request.getParameter("txthoras")), minutos = Integer.parseInt(request.getParameter("txtminutos"));
            String hora_inicio = "", hora_fin = "";
            try {
                Timestamp fechaInicio, fechaFin, fechaRetorno;
                if (id_tipo == 1) {
                    hora_inicio = request.getParameter("txtinicio");
                    hora_fin = request.getParameter("txtfin");
                    fechaInicio = new Timestamp(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(fecha_solicitud.toString() + "_" + hora_inicio + ":00").getTime());
                    fechaFin = new Timestamp(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(fecha_solicitud.toString() + "_" + hora_fin + ":00").getTime());
                    fechaRetorno = fechaFin;
                } else {
                    diasHabiles = Integer.parseInt(request.getParameter("txtdiashabiles"));
                    finesSemana = Integer.parseInt(request.getParameter("txtdiasnohabiles"));
                    fechaInicio = new Timestamp(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(Date.valueOf(request.getParameter("txtinicio1")).toString() + "_08:00:00").getTime());
                    fechaFin = new Timestamp(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(Date.valueOf(request.getParameter("txtfin1")).toString() + "_17:00:00").getTime());
                    fechaRetorno = new Timestamp(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(Date.valueOf(request.getParameter("txtingreso")).toString() + "_08:00:00").getTime());
                }
                PermisoManual permiso = new PermisoManual(
                        idAmin,
                        codFunc,
                        fechaInicio,
                        fechaFin,
                        fechaRetorno,
                        diasHabiles,
                        finesSemana,
                        hora_inicio,
                        hora_fin,
                        horas,
                        minutos,
                        observacion.equals("") ? "N/A" : observacion,
                        denominacion,
                        direccion,
                        jefe,
                        cargoJefe
                );
                int idPermiso = enlace.registroPermisoManual(permiso);
                permiso.setId(idPermiso);
                permiso.setCreacion(new Timestamp(Date.from(new java.util.Date().toInstant()).getTime()));
                if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                    Part part = request.getPart("txtadjunto");
                    File fileDir = new File(PATH);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    String path = PATH + "REGISTRO_MANUAL_" + idPermiso + "_" + part.getSubmittedFileName();
                    part.write(path);
                    enlace.actualizarAdjuntoPermisoManual(idPermiso, path);
                }
                if (link.registroSolicitudManual(permiso)) {
                    PrintWriter out = response.getWriter();
                    out.print("ok");
                } else {
                    enlace.eliminarPermisoManual(idPermiso);
                    if (!request.getPart("txtadjunto").getSubmittedFileName().equals("")) {
                        Part part = request.getPart("txtadjunto");
                        String path = PATH + "REGISTRO_MANUAL_" + idPermiso + "_" + part.getSubmittedFileName();
                        new File(path).delete();
                    }
                }
            } catch (Exception ex) {
                System.out.println("registroPermisoManual | " + ex);
            }
        } else if (accion.equalsIgnoreCase("anular_manual")) {
            int id_permiso = Integer.parseInt(request.getParameter("txtper"));
            int id_usuario = Integer.parseInt(request.getParameter("txtusu"));
            int motivo = Integer.parseInt(request.getParameter("motivo"));
            Part archivo = request.getPart("archivo");
            AnulacionManual elemento = new AnulacionManual(id_permiso, id_usuario, motivo);
            PrintWriter out = response.getWriter();
            try {
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                archivo.write(PATH + "ANULACION_M_" + id_permiso + ".pdf");
                if (enlace.registroAnulacionManual(elemento)) {
                    if (enlace.actualizarEstadoPermisoManual(4, id_permiso)) {
                        conexion_oracle link = new conexion_oracle();
                        if (link.anularPermiso(id_permiso, "M")) {
                            out.print("x");
                        } else {
                            enlace.actualizarEstadoPermisoManual(0, id_permiso);
                            new File(PATH + "ANULACION_M_" + id_permiso + ".pdf").delete();
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("anular_manual | " + ex);
            }
        } else if (accion.equalsIgnoreCase("descargar_anulacion_m")) {
            int id_permiso = Integer.parseInt(request.getParameter("id_permiso"));
            File ficheroXLS = new File(PATH + "ANULACION_M_" + id_permiso + ".pdf");
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
