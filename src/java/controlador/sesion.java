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
import java.net.InetAddress;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.ConexionVentanilla;
import modelo.conexion;
import modelo.conexion_oracle;
import modelo.estado_usuario;
import modelo.usuario;
import sinarp.Ciudadano;
import sinarp.SINARP;

/**
 *
 * @author usuario
 */
@WebServlet(name = "sesion", urlPatterns = {"/sesion.control"})
public class sesion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs PRUEBA
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        int id_bot = 0;
        conexion enlace = new conexion();
        HttpSession sesion = request.getSession();
        String mensaje = null;
        try (PrintWriter out = response.getWriter()) {
            String accion = request.getParameter("accion");
            String correo = request.getParameter("txtcorreo");
            String clave = request.getParameter("txtclave");
            usuario elemento = new usuario(correo, clave);
            if (accion.equalsIgnoreCase("normal")) {
                if (enlace.existeUsuario(elemento.getCorreo())) {
                    if (enlace.comprobacionUsuario(elemento)) {
                        usuario nuevo = enlace.buscar_usuarioCorreo(elemento.getCorreo());
                        if (!enlace.verificarUsuarioEncuesta(nuevo.getId_usuario())) {
                            sesion.setAttribute("user", nuevo.getId_usuario());
                            response.sendRedirect("encuestas.jsp");
                            return;
                        }
                        if (enlace.esprimeraSesion(nuevo.getId_usuario()) == 1) {
                            if (enlace.accesoUsuario(nuevo.getId_usuario())) {
                                if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 1) || enlace.isTipoUsuarioID(nuevo.getId_usuario(), 19)) {
                                    if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                        id_bot = 1;
                                        mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                        if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                            sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                            response.sendRedirect("principal.jsp");
                                        } else {
                                            sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                            response.sendRedirect("principal.jsp");
                                        }
                                    } else {
                                        sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                        response.sendRedirect("principal.jsp");
                                    }
                                } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 3)) {
                                    if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                        id_bot = 1;
                                        mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                        if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                            sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                            response.sendRedirect("principal_fun.jsp");
                                        } else {
                                            sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                            response.sendRedirect("principal_fun.jsp");
                                        }
                                    } else {
                                        sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                        response.sendRedirect("principal_fun.jsp");
                                    }
                                } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 4)) {
                                    if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                        id_bot = 1;
                                        mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                        if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                            sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                            response.sendRedirect("principal_dir.jsp");
                                        } else {
                                            sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                            response.sendRedirect("principal_dir.jsp");
                                        }
                                    } else {
                                        sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                        response.sendRedirect("principal_dir.jsp");
                                    }
                                } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 9)) {
                                    sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                    response.sendRedirect("principal_fun.jsp");
                                }
                                estado_usuario acceso = new estado_usuario(nuevo.getId_usuario(), enlace.fechaActual(), enlace.hora_actual());
                                try {
                                    if (enlace.registroEstadoUsuario(acceso)) {
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("sesion registroEstadoUsuario | " + ex);
                                }
                            } else {
                                sesion.setAttribute("usuario", 0);
                                response.sendRedirect("index.jsp");
                            }
                        } else {
                            response.sendRedirect("sesion.jsp?step=0&m&iu=" + nuevo.getId_usuario());
                        }
                    } else {
                        sesion.setAttribute("usuario", 0);
                        response.sendRedirect("index.jsp");
                    }
                } else {
                    sesion.setAttribute("usuario", 2);
                    response.sendRedirect("index.jsp");
                }
            } else if (accion.equalsIgnoreCase("registro_memorandum_coordinador")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("registro_memorandum_coordinador.jsp");
                } else {
                    response.sendRedirect("registro_memorandum_coordinador.jsp");
                }
            }else if (accion.equalsIgnoreCase("registro_memorandum")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("registro_memorandum.jsp");
                } else {
                    response.sendRedirect("registro_memorandum.jsp");
                }
            } else if (accion.equalsIgnoreCase("asignaciones")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                String tipo=enlace.tipoUsuario(iu);
                response.sendRedirect("listado_memorandums.jsp");
            } else if (accion.equalsIgnoreCase("asignaciones_coordinador")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                String tipo=enlace.tipoUsuario(iu);
                response.sendRedirect("memoradums_coordinador.jsp");
            }else if (accion.equalsIgnoreCase("asignaciones_director")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                String tipo=enlace.tipoUsuario(iu);
                response.sendRedirect("memoradums_direccion.jsp");
            }else if (accion.equalsIgnoreCase("asignaciones_th")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                String tipo=enlace.tipoUsuario(iu);
                response.sendRedirect("memoradums_th.jsp");
            } else if (accion.equalsIgnoreCase("consultar_datos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                String cedula = request.getParameter("txtcedula");
                String remoteAddr = request.getHeader("X-Forwarded-For");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    remoteAddr = request.getRemoteAddr();
                    if (remoteAddr.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        String ipAddress = inetAddress.getHostAddress();
                        remoteAddr = ipAddress;
                    }
                }
                enlace.auditarDINARDAP(iu, remoteAddr, cedula, 1, 0, 0);
                ConexionVentanilla ventanilla = new ConexionVentanilla();
                SINARP sinarp = new SINARP();
                Ciudadano rcd = sinarp.getRCD(cedula, true);
                String[] nombres = rcd.getNombres().split(" ");
                final String nacimiento = rcd.getFechaNacimiento();
                String res = nombres.length == 3 ? nombres[0] + " " + nombres[1] + "*" + nombres[2] + "*" : nombres[0] + " " + nombres[1] + "*" + nombres[2] + " " + nombres[3] + "*";
                res += nacimiento;
                if (rcd.getActaDefuncion() != 0) {
                    if (!ventanilla.existeFallecido(cedula)) {
                        ventanilla.insertarFallecido(cedula);
                    }
                }
                out.println(res);
            } else if (accion.equalsIgnoreCase("mis_contactos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_contactos.jsp");
                } else {
                    response.sendRedirect("listado_contactos.jsp");
                }
            } else if (accion.equalsIgnoreCase("helpdesk")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("helpdesk.jsp");
            } else if (accion.equalsIgnoreCase("viaticos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_viaticos.jsp");
                } else {
                    response.sendRedirect("listado_viaticos.jsp");
                }
            } else if (accion.equalsIgnoreCase("revision_viaticos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("revision_viaticos.jsp");
            } else if (accion.equalsIgnoreCase("aprobacion_viaticos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("aprobacion_viaticos.jsp");
            } else if (accion.equalsIgnoreCase("bandeja_general")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("administrar_tickets.jsp");
                } else {
                    response.sendRedirect("administrar_tickets.jsp");
                }
            } else if (accion.equalsIgnoreCase("listado_planificaciones")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_planificaciones.jsp");
                } else {
                    response.sendRedirect("listado_planificaciones.jsp");
                }
            } else if (accion.equalsIgnoreCase("plan_operativo")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("plan_operativo.jsp");
                } else {
                    response.sendRedirect("plan_operativo.jsp");
                }
            } else if (accion.equalsIgnoreCase("recurso_desarrollo")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("recurso_desarrollo.jsp");
                } else {
                    response.sendRedirect("recurso_desarrollo.jsp");
                }
            } else if (accion.equalsIgnoreCase("listado_recursos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                int id_almacenamiento = Integer.parseInt(request.getParameter("ial"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_recursos.jsp?ial=" + id_almacenamiento);
                } else {
                    response.sendRedirect("listado_recursos.jsp");
                }
            } else if (accion.equalsIgnoreCase("mi_soporte")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("mis_soportes.jsp");
                } else {
                    response.sendRedirect("mis_soportes.jsp");
                }
            } else if (accion.equalsIgnoreCase("reporteria_soporte_individual")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_soporte_individual.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_soporte_general")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_soporte_general.jsp");
            } else if (accion.equalsIgnoreCase("teletrabajo")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_actividades.jsp");
                } else {
                    response.sendRedirect("listado_actividades.jsp");
                }
            } else if (accion.equalsIgnoreCase("registro_actividad")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("registro_actividad.jsp?idact=0");
                } else {
                    response.sendRedirect("registro_actividad.jsp?idact=0");
                }
            } else if (accion.equalsIgnoreCase("control_actividad")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("control_actividades.jsp?iu=0&op=0");
                } else {
                    response.sendRedirect("control_actividades.jsp?iu=0&op=0");
                }
            } else if (accion.equalsIgnoreCase("control_general")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("control_general.jsp");
                } else {
                    response.sendRedirect("control_general.jsp");
                }
            } else if (accion.equalsIgnoreCase("registro_usuario")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("registro_usuario.jsp");
                } else {
                    response.sendRedirect("registro_usuario.jsp");
                }
            } else if (accion.equalsIgnoreCase("registro_equipos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("registro_equipos.jsp");
                } else {
                    response.sendRedirect("registro_equipos.jsp");
                }
            } else if (accion.equalsIgnoreCase("monitoreo_actividad")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("monitoreo_actividades.jsp?ind=0");
                } else {
                    response.sendRedirect("monitoreo_actividades.jsp?ind=0");
                }
            } else if (accion.equalsIgnoreCase("monitoreo_direccion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("monitoreo_direccion.jsp?ind=0");
                } else {
                    response.sendRedirect("monitoreo_direccion.jsp?ind=0");
                }
            } else if (accion.equalsIgnoreCase("monitoreo_encargado")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("monitoreo_encargado.jsp");
                } else {
                    response.sendRedirect("monitoreo_encargado.jsp");
                }
            } else if (accion.equalsIgnoreCase("configurar_cuenta")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("configurar_cuenta.jsp");
                } else {
                    response.sendRedirect("configurar_cuenta.jsp");
                }
            } else if (accion.equalsIgnoreCase("perfil")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("perfil.jsp");
            } else if (accion.equalsIgnoreCase("contacto")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("contacto.jsp");
                } else {
                    response.sendRedirect("contacto.jsp");
                }
            } else if (accion.equalsIgnoreCase("permiso_vacaciones")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                int codigoUsu = enlace.consultarCodigoUsuario(iu);
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                conexion_oracle oracle = new conexion_oracle();
                if (oracle.verificarHorario(codigoUsu)) {
                    response.sendRedirect("permiso_vacaciones.jsp");
                } else {
                    response.sendRedirect("permiso_horas_ecu_x.jsp");
                    //response.sendRedirect("permiso_vacaciones_ecu.jsp");
                }
            } else if (accion.equalsIgnoreCase("vacaciones_direccion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("vacaciones_direccion.jsp");
                } else {
                    response.sendRedirect("vacaciones_direccion.jsp");
                }
            } else if (accion.equalsIgnoreCase("horas_direccion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("horas_direccion.jsp");
                } else {
                    response.sendRedirect("horas_direccion.jsp");
                }
            } else if (accion.equalsIgnoreCase("solicitud_tinta")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_solicitudes.jsp");
                } else {
                    response.sendRedirect("listado_solicitudes.jsp");
                }
            } else if (accion.equalsIgnoreCase("listado_convocatorias")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_convocatorias.jsp");
                } else {
                    response.sendRedirect("listado_convocatorias.jsp");
                }
            } else if (accion.equalsIgnoreCase("listado_actas")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_actas.jsp");
                } else {
                    response.sendRedirect("listado_actas.jsp");
                }
            } else if (accion.equalsIgnoreCase("listado_marcaciones")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("listado_marcaciones.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_marcaciones")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_marcaciones.jsp");
            } else if (accion.equalsIgnoreCase("permiso_horas")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                int codigoUsu = enlace.consultarCodigoUsuario(iu);
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                conexion_oracle oracle = new conexion_oracle();
                if (oracle.verificarHorario(codigoUsu)) {
                    response.sendRedirect("permiso_horas.jsp");
                } else {
                    response.sendRedirect("permiso_horas_ecu_x.jsp");
                    //response.sendRedirect("permiso_horas_ecu.jsp");
                }
            } else if (accion.equalsIgnoreCase("manuales")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("manuales.jsp");
                } else {
                    response.sendRedirect("manuales.jsp");
                }
            } else if (accion.equalsIgnoreCase("formularios")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("formularios.jsp");
            } else if (accion.equalsIgnoreCase("monitoreo_cumplimientodir")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("monitoreo_cumplimientodir.jsp");
                } else {
                    response.sendRedirect("monitoreo_cumplimientodir.jsp");
                }
            } else if (accion.equalsIgnoreCase("mis_compromisos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("listado_compromisos.jsp");
                } else {
                    response.sendRedirect("listado_compromisos.jsp");
                }
            } else if (accion.equalsIgnoreCase("administrar_usuarios")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("administrar_usuarios.jsp");
                } else {
                    response.sendRedirect("administrar_usuarios.jsp");
                }
            } else if (accion.equalsIgnoreCase("administrar_inventario")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("administrar_inventario.jsp");
                } else {
                    response.sendRedirect("administrar_inventario.jsp");
                }
            } else if (accion.equalsIgnoreCase("servicios_ciudadanos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("servicios.jsp");
                } else {
                    response.sendRedirect("servicios.jsp");
                }
            } else if (accion.equalsIgnoreCase("rol_pago")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("rol_pago.jsp");
                } else {
                    response.sendRedirect("rol_pago.jsp");
                }
            } else if (accion.equalsIgnoreCase("tramite_nuevo")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("tramite_nuevo.jsp");
            } else if (accion.equalsIgnoreCase("tramite_entrada")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("tramite_entrada.jsp");
            } else if (accion.equalsIgnoreCase("tramite_registro")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("tramite_salida.jsp");
            } else if (accion.equalsIgnoreCase("panel_director")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("panel_director/index.jsp?step=0");
                } else {
                    response.sendRedirect("panel_director/index.jsp?step=0");
                }
            } else if (accion.equalsIgnoreCase("panel_administracion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("panel_administracion/index.jsp?step=0");
                } else {
                    response.sendRedirect("panel_administracion/index.jsp?step=0");
                }
            } else if (accion.equalsIgnoreCase("panel_talento")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (el != null) {
                    response.sendRedirect("panel_talento/index.jsp?step=0");
                } else {
                    response.sendRedirect("panel_talento/index.jsp?step=0");
                }
            } else if (accion.equalsIgnoreCase("recuperar")) {
                if (enlace.comprobacionUsuario(elemento)) {
                    usuario nuevo = enlace.buscar_usuarioCorreo(elemento.getCorreo());
                    if (enlace.accesoUsuario(nuevo.getId_usuario())) {
                        if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 1)) {
                            if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                id_bot = 1;
                                mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                    sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                    response.sendRedirect("principal.jsp");
                                } else {
                                    sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                    response.sendRedirect("principal.jsp");
                                }
                            } else {
                                sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                response.sendRedirect("principal.jsp");
                            }
                        } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 3)) {
                            if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                id_bot = 1;
                                mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                    sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                    response.sendRedirect("principal_fun.jsp");
                                } else {
                                    sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                    response.sendRedirect("principal_fun.jsp");
                                }
                            } else {
                                sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                response.sendRedirect("principal_fun.jsp");
                            }
                        } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 4)) {
                            if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                id_bot = 1;
                                mensaje = "Ha iniciado sesión en el Sistema de Intranet GADMCE a las " + enlace.hora_actual() + " del día " + enlace.fechaActual();
                                if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(nuevo.getId_usuario()), id_bot)) {
                                    sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                    response.sendRedirect("principal_dir.jsp");
                                } else {
                                    sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                    response.sendRedirect("principal_dir.jsp");
                                }
                            } else {
                                sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                response.sendRedirect("principal_dir.jsp");
                            }
                        }
                        estado_usuario acceso = new estado_usuario(nuevo.getId_usuario(), enlace.fechaActual(), enlace.hora_actual());
                        try {
                            if (enlace.registroEstadoUsuario(acceso)) {

                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    } else {
                        response.sendRedirect("error.jsp?step=0");
                    }
                } else {
                    usuario el = enlace.buscar_usuarioCorreo(elemento.getCorreo());
                    response.sendRedirect("recuperar_clave.jsp?step=1&m=cod&iu=" + el.getId_usuario());
                }
            } else if (accion.equalsIgnoreCase("principal")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario nuevo = enlace.buscar_usuarioID(iu);
                if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 1)) {
                    sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                    response.sendRedirect("principal.jsp");
                } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 3)) {
                    sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                    response.sendRedirect("principal_fun.jsp");
                } else if (enlace.isTipoUsuarioID(nuevo.getId_usuario(), 4)) {
                    sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                    response.sendRedirect("principal_dir.jsp");
                }
            } else if (accion.equalsIgnoreCase("pdf")) {
                String ruta = request.getParameter("ruta");
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
            } else if (accion.equalsIgnoreCase("doc")) {
                String ruta = request.getParameter("ruta");
                File fichero = new File(ruta);
                FileInputStream fis = new FileInputStream(fichero);
                int tamano = fis.available();
                byte[] bytes = new byte[tamano];
                int read = 0;
                String fileName = fichero.getName();
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                ServletOutputStream arch = response.getOutputStream();
                while ((read = fis.read(bytes)) != -1) {
                    arch.write(bytes, 0, read);
                }
                arch.flush();
                arch.close();
            } else if (accion.equalsIgnoreCase("seguimiento_tributario")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("seguimiento_tributario.jsp");
            } else if (accion.equalsIgnoreCase("tribu")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                if (request.getParameter("txtanio") != null) {
                    sesion.setAttribute("y_tribu", Integer.parseInt(request.getParameter("txtanio")));
                }
                response.sendRedirect("seguimiento_tributario.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_dinardap")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_dinardap.jsp");
            } else if (accion.equalsIgnoreCase("dinardap")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("dinardap.jsp");
            } else if (accion.equalsIgnoreCase("hash")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("hash.jsp");
            } else if (accion.equalsIgnoreCase("versionador")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("versionador.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_versionador")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_versionador.jsp");
            } else if (accion.equalsIgnoreCase("carga_riesgos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("cargaRiesgos.jsp");
            } else if (accion.equalsIgnoreCase("riesgos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("riesgos.jsp");
            } else if (accion.equalsIgnoreCase("roles")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("roles.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_permisos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_permisos.jsp");
            } else if (accion.equalsIgnoreCase("permiso_iess")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("permiso_horas_iess.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_permisos_direccion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_permisos_direccion.jsp");
            } else if (accion.equalsIgnoreCase("permiso_manual")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("permiso_manual.jsp");
            } else if (accion.equalsIgnoreCase("ausencias_admin")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ausencias_admin.jsp");
            } else if (accion.equalsIgnoreCase("encuestas")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("encuestas.jsp");
            } else if (accion.equalsIgnoreCase("sigcal")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("http://192.168.120.6/sigcal/seg_Login/");
            } else if (accion.equalsIgnoreCase("sigcal_prueba")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("http://192.168.120.5/sigcal/seg_Login/");
            } else if (accion.equalsIgnoreCase("sigcal_manual")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("sigcal_manuales.jsp");
            } else if (accion.equalsIgnoreCase("capacitacion_disp")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("capacitacion_disp.jsp");
            } else if (accion.equalsIgnoreCase("capacitacion")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("capacitacion.jsp");
            } else if (accion.equalsIgnoreCase("capacitacion_admin")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("capacitacion_admin.jsp");
            } else if (accion.equalsIgnoreCase("repositorio")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("repositorio.jsp");
            } else if (accion.equalsIgnoreCase("reporteria_repositorio")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("reporteria_repositorio.jsp");
            } else if (accion.equalsIgnoreCase("biometrico")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("bio_mis_asistencias.jsp");
            } else if (accion.equalsIgnoreCase("biometrico_admin")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("bio_admin.jsp");
            } else if (accion.equalsIgnoreCase("gastos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("gastos_personales.jsp");
            } else if (accion.equalsIgnoreCase("gastos_admin")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("gastos_personales_admin.jsp");
            } else if (accion.equalsIgnoreCase("mobile")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("administrar_mobile.jsp");
            } else if (accion.equalsIgnoreCase("carga_gaceta")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("carga_gaceta.jsp");
            } else if (accion.equalsIgnoreCase("nueva_ficha")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("con_sec_nueva_ficha.jsp");
            } else if (accion.equalsIgnoreCase("fichas_registradas")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("con_sec_fichas_registradas.jsp");
            } else if (accion.equalsIgnoreCase("documento_inicio")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_inicio.jsp");
            } else if (accion.equalsIgnoreCase("nuevo_documento")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_nuevo_documento.jsp");
            } else if (accion.equalsIgnoreCase("documentos_recibidos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_documentos_recibidos.jsp");
            } else if (accion.equalsIgnoreCase("mis_documentos")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_mis_documentos.jsp");
            } else if (accion.equalsIgnoreCase("documentos_imprimir")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_por_imprimir.jsp");
            } else if (accion.equalsIgnoreCase("documentos_borradores")) {
                int iu = Integer.parseInt(request.getParameter("iu"));
                usuario el = enlace.buscar_usuarioID(iu);
                sesion.setAttribute("user", el.getId_usuario());
                response.sendRedirect("ges_doc_borradores.jsp");
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
