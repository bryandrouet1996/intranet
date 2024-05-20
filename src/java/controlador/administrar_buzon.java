/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobile.Solicitud;
import mobile.MySql;
import javax.servlet.http.HttpSession;
import mobile.API;
import mobile.SolicitudApi;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
@WebServlet(name = "administrar_buzon", urlPatterns = {"/administrar_buzon.control"})

public class administrar_buzon extends HttpServlet {

    private static final String PATH = "/newmedia/buzon/";
//    private static final String PATH = "C:/prueba/buzon/";

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
        MySql mysql = new MySql();
        HttpSession sesion = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            String accion = request.getParameter("accion");
            if (accion.equalsIgnoreCase("gestionar")) {
                int idSolicitud = Integer.parseInt(request.getParameter("id")),
                        idUsu = Integer.parseInt(sesion.getAttribute("user").toString());
                Solicitud s = new Solicitud();
                s.setId(idSolicitud);
                try {
                    mysql.gestionarSolicitud(idUsu, s);
                    out.println("1");
                } catch (Exception e) {
                    System.out.println("gestionarSolicitud | " + e);
                }
            } else if (accion.equalsIgnoreCase("observar")) {
                int id = Integer.parseInt(request.getParameter("id")),
                        estado = Integer.parseInt(request.getParameter("estado"));
                String obs = request.getParameter("obs").toUpperCase();
                try {
                    Solicitud s = new Solicitud();
                    s.setId(id);
                    s.getEstado().setId(estado);
                    s.setObservacion(obs);
//                    mysql.observarSolicitud(s);
                    API mobileAPI = new API();
                    if (mobileAPI.atenderSolicitud(s)) {
                        out.println("1");
                    }
                } catch (Exception e) {
                    System.out.println("observarSolicitud | " + e);
                }
            } else if (accion.equalsIgnoreCase("devolver")) {
                int id = Integer.parseInt(request.getParameter("id")),
                        idUsu = Integer.parseInt(sesion.getAttribute("user").toString());
                try {
                    Solicitud s = new Solicitud();
                    s.setId(id);
                    mysql.cancelarGestionSolicitud(idUsu, s);
                    out.println("1");
                } catch (Exception e) {
                    System.out.println("cancelarGestionSolicitud | " + e);
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
