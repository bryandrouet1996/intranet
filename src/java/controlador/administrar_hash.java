/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 500)
@WebServlet(name = "administrar_hash", urlPatterns = {"/administrar_hash.control"})
public class administrar_hash extends HttpServlet {

    private static final String PATH = "/newmedia/hash/";
//    private static final String PATH = "C:/prueba/hash/";

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
        if (accion.equalsIgnoreCase("hash")) {
            int idUsu = Integer.parseInt(request.getParameter("idusu"));
            Part archivo = request.getPart("adjunto");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            archivo.write(PATH + idUsu + "_" + archivo.getSubmittedFileName());
            try {
                String hash = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-512").digest(Files.readAllBytes(Paths.get(PATH + idUsu + "_" + archivo.getSubmittedFileName()))));
                sesion.setAttribute("hash", hash);
                out.print(hash);
                response.sendRedirect("hash.jsp");
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("Hash ex: " + ex);
            }
            new File(PATH + idUsu + "_" + archivo.getSubmittedFileName()).delete();
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
