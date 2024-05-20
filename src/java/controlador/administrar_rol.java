/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

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
@WebServlet(name = "administrar_rol", urlPatterns = {"/administrar_rol.control"})
public class administrar_rol extends HttpServlet {

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
        conexion enlace = new conexion();
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("roles")) {
            String mes = request.getParameter("combomes");
            String anio = request.getParameter("txtanio");
            int idUsuario = Integer.parseInt(request.getParameter("iu"));
            usuario elemento = enlace.buscar_usuarioID(idUsuario);
            try {
                enlace.ProcesarRolesPago(elemento.getCodigo_usuario(), anio, mes);
                response.sendRedirect("rol_pago.jsp");
            } catch (IOException | SQLException ex) {
                System.out.println("ProcesarRolesPago | " + ex);
                response.sendRedirect("rol_pago.jsp");
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            int id_rol = Integer.parseInt(request.getParameter("ir"));
            if (enlace.eliminarRolUsuarioID(id_rol)) {
                out.print("ok");
            } else {

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
