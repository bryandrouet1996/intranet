/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_marcacion", urlPatterns = {"/administrar_marcacion.control"})
public class administrar_marcacion extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("filtrar")) {
            try {
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("listado_marcaciones.jsp?fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin);
            } catch (Exception ex) {
                response.sendRedirect("listado_marcaciones.jsp?iu=0&op=0");
            }
        } else if (accion.equalsIgnoreCase("filtrar_rep")) {
            try {
                java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtini"));
                java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfin"));
                response.sendRedirect("reporteria_marcaciones.jsp?fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin);
            } catch (Exception ex) {
                response.sendRedirect("reporteria_marcaciones.jsp");
            }
        } else if (accion.equalsIgnoreCase("marcar")) {
            try {
                int id_usu = Integer.parseInt(request.getParameter("id_usu"));
                int tipo = Integer.parseInt(request.getParameter("tipo"));
                java.sql.Date fecha = Date.valueOf(LocalDate.now());
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                String ip = new BufferedReader(new InputStreamReader(whatismyip.openStream())).readLine();
                if (ip.equals("186.46.57.100")) {
                    conexion mysql = new conexion();
                    try {
                        mysql.RegistrarAsistencia(id_usu, tipo);
                    } catch (ParseException ex) {
                        System.out.println("ex reg asis " + ex);
                    }
                    response.sendRedirect("listado_marcaciones.jsp?fecha_inicio=" + fecha + "&fecha_fin=" + fecha);
                }
            } catch (IOException | NumberFormatException ex) {
                System.out.println(ex);
                response.sendRedirect("listado_marcaciones.jsp?iu=0&op=0");
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
