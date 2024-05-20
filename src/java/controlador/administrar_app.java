/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.Version;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
@WebServlet(name = "administrar_app", urlPatterns = {"/administrar_app.control"})
public class administrar_app extends HttpServlet {

    private static final String PATH = "/newmedia/versiones/";
//    private static final String PATH = "C:/prueba/versiones/";

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
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registrar_version")) {
            int idUsu = Integer.parseInt(request.getParameter("idUsu"));
            int idApp = Integer.parseInt(request.getParameter("cmbApp"));
            int tipo = Integer.parseInt(request.getParameter("cmbTipo"));
            String des = request.getParameter("txtdes");
            Part archivo = request.getPart("archivo");
            Version ver = new Version(idUsu, idApp, tipo, des);
            try {
                int idVersion = enlace.registrarVersion(ver);
                if (archivo.getSubmittedFileName().equals("")) {
                    out.print("ok");
                } else {
                    File fileDir = new File(PATH);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    archivo.write(PATH + idVersion + "_" + archivo.getSubmittedFileName());
                    if (enlace.actualizarAdjuntoVersion(idVersion, PATH + idVersion + "_" + archivo.getSubmittedFileName())) {
                        out.print("ok");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("registrarVersion | " + ex);
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
