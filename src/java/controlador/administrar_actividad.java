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
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.actividad;
import modelo.comentario_actividad;
import modelo.conexion;
import modelo.formato_intervalo;
import modelo.usuario;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_actividad", urlPatterns = {"/administrar_actividad.control"})
public class administrar_actividad extends HttpServlet {

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

        /* TODO output your page here. You may use following sample code. */
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registro")) {
            PrintWriter out = response.getWriter();
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            java.sql.Date fecha_actividad = Date.valueOf(request.getParameter("txtfechaactividad"));
            java.sql.Date fecha_limite = Date.valueOf(request.getParameter("txtfechalimite"));
            String hora_inicio = request.getParameter("txthorainicio");
            String hora_fin = request.getParameter("txthorafin");
            String indicador = request.getParameter("txtindicador");
            String avance = request.getParameter("comboavance");
            String grado = request.getParameter("combogrado");
            String tarea = request.getParameter("txttarea");
            String requiriente = request.getParameter("txtrequiriente");
            String herramienta = "";
            String herramienta_otro = "";
            String observacion = request.getParameter("areaobservacion");
            if (request.getParameter("sw1") != null) {
                herramienta = herramienta + request.getParameter("sw1") + ",";
            }
            if (request.getParameter("sw2") != null) {
                herramienta = herramienta + request.getParameter("sw2") + ",";
            }
            if (request.getParameter("sw3") != null) {
                herramienta = herramienta + request.getParameter("sw3") + ",";
            }
            if (request.getParameter("sw4") != null) {
                herramienta = herramienta + request.getParameter("sw4") + ",";
            }
            if (request.getParameter("sw5") != null) {
                herramienta = herramienta + request.getParameter("sw5") + ",";
            }
            if (request.getParameter("sw6") != null) {
                herramienta = herramienta + request.getParameter("sw6") + ",";
            }
            if (request.getParameter("sw7") != null) {
                herramienta = herramienta + request.getParameter("sw7") + ",";
            }
            if (request.getParameter("sw8") != null) {
                herramienta_otro = request.getParameter("sw8");
            }
            if (request.getParameter("sw9") != null) {
                herramienta = herramienta + request.getParameter("sw9") + ",";
            }
            if (request.getParameter("sw10") != null) {
                herramienta = herramienta + request.getParameter("sw10") + ",";
            }
            actividad elemento = new actividad(id_usuario, tarea, fecha_actividad, enlace.fechaActual(), hora_inicio, hora_fin, herramienta, herramienta_otro, observacion, requiriente, fecha_limite, indicador, grado, avance);
            try {

                if (Integer.parseInt(request.getParameter("iact")) != 0) {

                    int iact = Integer.parseInt(request.getParameter("iact"));
                    herramienta = "";
                    if (request.getParameter("sw1") != null) {
                        herramienta = herramienta + request.getParameter("sw1") + ",";
                    }
                    if (request.getParameter("sw2") != null) {
                        herramienta = herramienta + request.getParameter("sw2") + ",";
                    }
                    if (request.getParameter("sw3") != null) {
                        herramienta = herramienta + request.getParameter("sw3") + ",";
                    }
                    if (request.getParameter("sw4") != null) {
                        herramienta = herramienta + request.getParameter("sw4") + ",";
                    }
                    if (request.getParameter("sw5") != null) {
                        herramienta = herramienta + request.getParameter("sw5") + ",";
                    }
                    if (request.getParameter("sw6") != null) {
                        herramienta = herramienta + request.getParameter("sw6") + ",";
                    }
                    if (request.getParameter("sw7") != null) {
                        herramienta = herramienta + request.getParameter("sw7") + ",";
                    }
                    if (request.getParameter("sw8") != null) {
                        herramienta_otro = request.getParameter("sw8");
                    }
                    if (request.getParameter("sw9") != null) {
                        herramienta = herramienta + request.getParameter("sw9") + ",";
                    }
                    if (request.getParameter("sw10") != null) {
                        herramienta = herramienta + request.getParameter("sw10") + ",";
                    }
                    elemento = new actividad();
                    elemento.setTarea(tarea);
                    elemento.setHora_inicio(hora_inicio);
                    elemento.setHora_fin(hora_fin);
                    elemento.setRequiriente(requiriente);
                    elemento.setFecha_actividad(fecha_actividad);
                    elemento.setHerramienta(herramienta);
                    elemento.setHerramienta_otro(herramienta_otro);
                    elemento.setObservacion(observacion);
                    elemento.setFecha_limite(fecha_limite);
                    elemento.setGrado(grado);
                    elemento.setIndicador(indicador);
                    elemento.setAvance(avance);
                    if (!request.getParameter("txttarea").equalsIgnoreCase("") && !request.getParameter("txtrequiriente").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("") && !request.getParameter("txthorafin").equalsIgnoreCase("") && (herramienta.length() > 1 || herramienta_otro.length() > 1)) {
                        if (enlace.tienePermisoEdicionActividadesUsuarioID(id_usuario)) {
                            formato_intervalo formato = enlace.obtenerIntervaloTiempoRegistroActividadesUsuarioID(id_usuario);
                            if (enlace.esRangoCorrectoIndividual(fecha_actividad, formato.getCantidad(), formato.getFormato())) {
                                if (enlace.ActualizarActividad(iact, elemento)) {
                                    comentario_actividad elem = null;
                                    elem = enlace.buscarComentarioActividadID(iact);
                                    if (elem != null) {
                                        enlace.reenviarActividadUsuario(elem.getId_comentario());
                                    }
                                    out.println(iact);
                                } else {
                                    out.println(-2);
                                }
                            } else {
                                out.println(-1);
                            }
                        } else {
                            int intervalo = enlace.obtenerIntervaloDiasRegistroActividad();
                            if (enlace.esRangoCorrecto(fecha_actividad, intervalo)) {
                                if (enlace.ActualizarActividad(iact, elemento)) {
                                    comentario_actividad elem = null;
                                    elem = enlace.buscarComentarioActividadID(iact);
                                    if (elem != null) {
                                        enlace.reenviarActividadUsuario(elem.getId_comentario());
                                    }
                                    out.println(iact);
                                } else {
                                    out.println(-2);
                                }
                            } else {
                                out.println(-1);
                            }
                        }
                    }
                } else {
                    if (!request.getParameter("txttarea").equalsIgnoreCase("") && !request.getParameter("txtrequiriente").equalsIgnoreCase("") && !request.getParameter("areaobservacion").equalsIgnoreCase("") && !request.getParameter("txthorafin").equalsIgnoreCase("") && (herramienta.length() > 1 || herramienta_otro.length() > 1)) {
                        if (enlace.tienePermisoEdicionActividadesUsuarioID(id_usuario)) {
                            formato_intervalo formato = enlace.obtenerIntervaloTiempoRegistroActividadesUsuarioID(id_usuario);
                            if (enlace.esRangoCorrectoIndividual(fecha_actividad, formato.getCantidad(), formato.getFormato())) {
                                if (enlace.registroActividad(elemento)) {
                                    int id_actividad = enlace.ActividadRecienteUsuario(id_usuario);
                                    out.println(id_actividad);
                                } else {
                                    out.println(-2);
                                }
                            } else {
                                out.println(-1);
                            }
                        } else {
                            int intervalo = enlace.obtenerIntervaloDiasRegistroActividad();
                            if (enlace.esRangoCorrecto(fecha_actividad, intervalo)) {
                                if (enlace.registroActividad(elemento)) {
                                    int id_actividad = enlace.ActividadRecienteUsuario(id_usuario);
                                    out.println(id_actividad);
                                } else {
                                    out.println(-2);
                                }
                            } else {
                                out.println(-1);
                            }
                        }
                    }
                }

            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("modificar")) {
            int id_actividad = Integer.parseInt(request.getParameter("act"));
            java.sql.Date fecha_actividad = Date.valueOf(request.getParameter("txtfechaactividad"));
            String tarea = request.getParameter("txttarea");
            String herramienta = request.getParameter("areaherramienta");
            String observacion = request.getParameter("areaobservacion");
            String hora_inicio = request.getParameter("txthorainicio");
            String hora_fin = request.getParameter("txthorafin");
            String requiriente = request.getParameter("txtrequiriente");
            String indicador = request.getParameter("txtindicador");
            String avance = request.getParameter("comboavance");
            String grado = request.getParameter("combogrado");
            java.sql.Date fecha_limite = Date.valueOf(request.getParameter("txtfechalimite"));
            actividad elemento = new actividad();
            elemento.setTarea(tarea);
            elemento.setHora_inicio(hora_inicio);
            elemento.setHora_fin(hora_fin);
            elemento.setRequiriente(requiriente);
            elemento.setFecha_actividad(fecha_actividad);
            elemento.setHerramienta(herramienta);
            elemento.setObservacion(observacion);
            elemento.setFecha_limite(fecha_limite);
            elemento.setGrado(grado);
            elemento.setIndicador(indicador);
            elemento.setAvance(avance);
            try {
                if (enlace.ActualizarActividad(id_actividad, elemento)) {
                    response.sendRedirect("teletrabajo/index.jsp?step=2&act=" + id_actividad);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registro_comentario")) {
            PrintWriter out = response.getWriter();
            int id_actividad = Integer.parseInt(request.getParameter("iact"));
            int id_usuario = Integer.parseInt(request.getParameter("icom"));
            String descripcion = request.getParameter("descrip");
            actividad elemen = enlace.BuscarActividadID(id_actividad);
            usuario autor = enlace.buscar_usuarioID(elemen.getId_usuario());
            comentario_actividad elemento = new comentario_actividad(id_usuario, id_actividad, descripcion, enlace.fechaActual());
            try {
                if (enlace.registroComentarioActividad(elemento)) {
                    out.print("ok");
                    String mensaje = "Estimado funcionario se ha registrado un comentario en su actividad N#" + elemen.getId_actividad();
                    enlace.enviarCorreo(autor.getCorreo(), "Correccion actividad", mensaje);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_evidencia")) {
            PrintWriter out = response.getWriter();
            int id_evidencia = Integer.parseInt(request.getParameter("iev"));
            if (enlace.eliminarEvidenciaActividad(id_evidencia)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("eliminar_actividad")) {
            PrintWriter out = response.getWriter();
            int id_actividad = Integer.parseInt(request.getParameter("iact"));
            if (enlace.eliminarActividad(id_actividad)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("aprobar_actividad")) {
            PrintWriter out = response.getWriter();
            int id_actividad = Integer.parseInt(request.getParameter("iact"));
            if (enlace.aprobarActividad(id_actividad)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("aprobar_todo")) {
            String codigo_unidad = request.getParameter("cunid");
            if (enlace.aprobarActividadesDireccion(codigo_unidad)) {
                response.sendRedirect("control_actividades.jsp?iu=0");
            } else {
                response.sendRedirect("control_actividades.jsp?iu=0");
            }
        } else if (accion.equalsIgnoreCase("aprobar_actual")) {
            int id_usuario = Integer.parseInt(request.getParameter("iu"));
            String inicio = request.getParameter("fec_in");
            String fin = request.getParameter("fec_fn");
            java.sql.Date fecha_inicio = Date.valueOf(inicio);
            java.sql.Date fecha_fin = Date.valueOf(fin);
            if (enlace.aprobarActividadesUsuario(id_usuario, fecha_inicio, fecha_fin)) {
                response.sendRedirect("control_actividades.jsp?iu=" + id_usuario + "&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin);
            } else {
                response.sendRedirect("control_actividades.jsp?iu=0");
            }
        } else if (accion.equalsIgnoreCase("aprobar_actuales")) {
            String codigo_funcion = request.getParameter("cf");
            String inicio = request.getParameter("fec_in");
            String fin = request.getParameter("fec_fn");
            java.sql.Date fecha_inicio = Date.valueOf(inicio);
            java.sql.Date fecha_fin = Date.valueOf(fin);
            if (enlace.aprobarActividadesDireccion(codigo_funcion, fecha_inicio, fecha_fin)) {
                response.sendRedirect("control_actividades.jsp?iu=0&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&op=1");
            } else {
                response.sendRedirect("control_actividades.jsp?iu=0&op=0");
            }
        } else if (accion.equalsIgnoreCase("filtrar")) {
            try {
                int id_usuario = Integer.parseInt(request.getParameter("combofuncionario"));
                if (id_usuario != 0) {
                    java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                    java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                    response.sendRedirect("control_actividades.jsp?iu=" + id_usuario + "&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&op=0");
                } else {
                    java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                    java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                    response.sendRedirect("control_actividades.jsp?iu=0&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&op=1");
                }
            } catch (Exception ex) {
                response.sendRedirect("control_actividades.jsp?iu=0&op=0");
            }
        } else if (accion.equalsIgnoreCase("filtrar_monitor")) {
            try {
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("monitoreo_direccion.jsp?ind=1&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin);
            } catch (Exception ex) {
                response.sendRedirect("monitoreo_direccion.jsp?ind=0");
            }
        } else if (accion.equalsIgnoreCase("filtrar_general")) {
            try {
                String direccion = request.getParameter("combodireccion");
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("monitoreo_actividades.jsp?ind=1&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&direccion=" + direccion);
            } catch (Exception ex) {
                response.sendRedirect("monitoreo_actividades.jsp?ind=0");
            }
        } else if (accion.equalsIgnoreCase("filtrar_ctgeneral")) {
            try {
                String direccion = request.getParameter("combodireccion");
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("control_general.jsp?fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&direccion=" + direccion);
            } catch (Exception ex) {
                response.sendRedirect("control_general.jsp");
            }
        } else if (accion.equalsIgnoreCase("descargar_evidencia")) {
            int id_evidencia = Integer.parseInt(request.getParameter("id_evi"));
            String ruta = enlace.buscarEvidenciaActividadID(id_evidencia).getRuta();
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
            System.out.println("\nDescargado\n");
        } else if (accion.equalsIgnoreCase("filtrar_act")) {
            try {
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("listado_actividades.jsp?fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin);
            } catch (Exception ex) {
                response.sendRedirect("listado_actividades.jsp");
            }
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
