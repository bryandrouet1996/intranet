/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.conexion;
import modelo.conexion_oracle;
import modelo.usuario;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_encuesta", urlPatterns = {"/administrar_encuesta.control"})

public class administrar_encuesta extends HttpServlet {

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
        conexion mysql = new conexion();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("verificar_usu")) {
            try {
                int idUsu = Integer.parseInt(request.getParameter("id_usu"));
                if (mysql.verificarUsuarioEncuesta(idUsu)) {
                    PrintWriter out = response.getWriter();
                    out.print("x");
                }
            } catch (Exception e) {
                System.out.println("verificar_usu | " + e);
            }
        } else if (accion.equalsIgnoreCase("abrir_encuesta")) {
            try {
                int idUsu = Integer.parseInt(request.getParameter("id_usu"));
                if (!mysql.verificarUsuarioEncuesta(idUsu)) {
                    if (mysql.registrarEncuestado(idUsu)) {
                        PrintWriter out = response.getWriter();
                        out.print("x");
                    }
                }
            } catch (Exception e) {
                System.out.println("abrir_encuesta | " + e);
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
