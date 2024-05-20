package controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.usuario;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(urlPatterns = {"/recuperacion_clave.control"})
public class recuperacion_clave extends HttpServlet {

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
        response.setCharacterEncoding("UTF-8");
        conexion enlace = new conexion();
        String accion = request.getParameter("accion");
        try (PrintWriter out = response.getWriter()) {
            if (accion.equalsIgnoreCase("mensaje")) {
                String correo = request.getParameter("txtcorreo");
                String tipo = "Correo";
                String token = enlace.generarToken();
                usuario elemento = enlace.buscar_usuarioCorreo(correo);
                if (elemento.getCodigo_usuario() != null) {
                    if (tipo.equalsIgnoreCase("Telegram")) {
                        if (enlace.estadoTelegramUsuario(elemento.getId_usuario())) {
                            int id_bot = 1;
                            String mensaje = "Su código de seguridad es " + token;
                            if (enlace.notificacionTelegram(mensaje, enlace.buscarChatidUsuario(elemento.getId_usuario()), id_bot)) {
                                try {
                                    enlace.ActualizarClaveUsuario(elemento.getId_usuario(), token);
                                    request.setAttribute("correo", elemento.getCorreo());
                                    response.sendRedirect("recuperar_clave.jsp?step=1&m=ok&iu="+elemento.getId_usuario());
                                } catch (SQLException ex) {
                                    System.out.println(ex);
                                    response.sendRedirect("recuperar_clave.jsp?step=0&m=envio");
                                }
                            } else {
                                response.sendRedirect("recuperar_clave.jsp?step=0&m=envio");
                            }
                        } else {
                            response.sendRedirect("recuperar_clave.jsp?step=0&m=bot");
                        }
                    } else if (tipo.equalsIgnoreCase("Correo")) {
                        if (enlace.enviarCorreoMod(elemento.getCorreo(), "Recuperación de clave", "Su contraseña temporal es: " + token + "\nNo olvide actualizar su clave luego de ingresar.")) {
                            try {
                                enlace.ActualizarClaveUsuario(elemento.getId_usuario(), token);
                                request.setAttribute("correo", elemento.getCorreo());
                                response.sendRedirect("recuperar_clave.jsp?step=1&m=ok&iu="+elemento.getId_usuario());
                            } catch (SQLException ex) {
                                System.out.println(ex);
                                response.sendRedirect("recuperar_clave.jsp?step=0&m=envio");
                            }
                        } else {
                            response.sendRedirect("recuperar_clave.jsp?step=0&m=envio");
                        }
                    }
                } else {
                    response.sendRedirect("recuperar_clave.jsp?step=0&m=us");
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
