/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.conexion;
import modelo.usuario;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_sesion", urlPatterns = {"/administrar_sesion.control"})
public class administrar_sesion extends HttpServlet {

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
        String accion = request.getParameter("accion");
        int id_bot = 0;
        String mensaje = "";
        HttpSession sesion = request.getSession();
        conexion enlace = new conexion();
        try (PrintWriter out = response.getWriter()) {
            if (accion.equalsIgnoreCase("mensaje")) {
                int id_usuario = Integer.parseInt(request.getParameter("iu"));
                String clave_actual = request.getParameter("txtclaveactual");
                String clave_nueva = request.getParameter("txtclavenueva");
                String clave_repetir = request.getParameter("txtclaverepetir");
                usuario elemento = enlace.buscar_usuarioID(id_usuario);
                if (elemento.getCodigo_usuario() != null) {
                    String clave = enlace.encriptacionClave(clave_actual);
                    if (clave.equalsIgnoreCase(elemento.getClave())) {
                        if (clave_nueva.equalsIgnoreCase(clave_repetir)) {
                            usuario nuevo = enlace.buscar_usuarioID(id_usuario);
                            String tipo_usuario = enlace.tipoUsuario(nuevo.getId_usuario());
                            if (tipo_usuario.equalsIgnoreCase("administrador")) {
                                if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                    try {
                                        enlace.actualizarEstadoSesion(nuevo.getId_usuario());
                                        enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva);
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
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
                                    try {
                                        enlace.actualizarEstadoSesion(nuevo.getId_usuario());
                                        enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva);
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
                                    sesion.setAttribute("usuario_ad", nuevo.getId_usuario());
                                    response.sendRedirect("principal.jsp");
                                }
                            } else if (tipo_usuario.equalsIgnoreCase("funcionario")) {
                                if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                    try {
                                        System.out.println(nuevo.getId_usuario() + "AQUI");
                                        if (enlace.actualizarEstadoSesion(nuevo.getId_usuario())) {
                                            System.out.println("ACTUALIZO");
                                        } else {
                                            System.out.println("NO ACTUALIZO ESTADO");
                                        }
                                        if (enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva)) {
                                            System.out.println("ACTUALIZO");
                                        } else {
                                            System.out.println("NO ACTUALIZO CLAVE");
                                        }
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
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
                                    try {
                                        if (enlace.actualizarEstadoSesion(nuevo.getId_usuario())) {
                                            System.out.println("ACTUALIZO");
                                        } else {
                                            System.out.println("NO ACTUALIZO ESTADO");
                                        }
                                        if (enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva)) {
                                            System.out.println("ACTUALIZO");
                                        } else {
                                            System.out.println("NO ACTUALIZO CLAVE");
                                        }
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
                                    sesion.setAttribute("usuario_fu", nuevo.getId_usuario());
                                    response.sendRedirect("principal_fun.jsp");
                                }
                            } else if (tipo_usuario.equalsIgnoreCase("director")) {
                                if (enlace.estadoTelegramUsuario(nuevo.getId_usuario())) {
                                    try {
                                        enlace.actualizarEstadoSesion(nuevo.getId_usuario());
                                        enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva);
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
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
                                    try {
                                        enlace.actualizarEstadoSesion(nuevo.getId_usuario());
                                        enlace.ActualizarClaveUsuario(nuevo.getId_usuario(), clave_nueva);
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
                                    sesion.setAttribute("usuario_dir", nuevo.getId_usuario());
                                    response.sendRedirect("principal_dir.jsp");
                                }
                            }
                        } else {
                            response.sendRedirect("sesion.jsp?step=0&m=cti&iu=" + id_usuario);
                        }
                    } else {
                        response.sendRedirect("sesion.jsp?step=0&m=ctr&iu=" + id_usuario);
                    }
                } else {
                    response.sendRedirect("sesion.jsp?step=0&m=us&iu=" + id_usuario);
                }
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
