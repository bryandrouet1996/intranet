/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.evidencia_actividad;
import modelo.historia;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "historias", urlPatterns = {"/historias.control"})
public class historias extends HttpServlet {

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
        response.setContentType("image/jpg");
        conexion enlace = new conexion();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("historia")) {
            historia elemento = new historia();
            int id = Integer.parseInt(request.getParameter("id"));
            elemento = enlace.buscarFotoHistoriaID(id);
            File ima = new File(elemento.getRuta() + "" + elemento.getNombre());
            if (!elemento.getNombre().equals("ninguno") && !elemento.getRuta().equals("ninguno")) {
                InputStream foto = new FileInputStream(ima);
                int tamano = foto.available();
                byte[] datos = new byte[tamano];
                foto.read(datos, 0, tamano);
                response.getOutputStream().write(datos);
            }else{
                System.out.println("");
            }
        } else if (accion.equalsIgnoreCase("login")) {
            historia elemento = new historia();
            int id = Integer.parseInt(request.getParameter("id"));
            elemento = enlace.buscarFotoHistoriaID(id);
            File ima = new File(elemento.getRuta() + "" + elemento.getNombre());
            if (!elemento.getNombre().equals("ninguno") && !elemento.getRuta().equals("ninguno")) {
                InputStream foto = new FileInputStream(ima);
                int tamano = foto.available();
                byte[] datos = new byte[tamano];
                foto.read(datos, 0, tamano);
                response.getOutputStream().write(datos);
            }else{
                System.out.println("");
            }
        }else if (accion.equalsIgnoreCase("evidencia")) {
            evidencia_actividad elemento = new evidencia_actividad();
            int id = Integer.parseInt(request.getParameter("id"));
            elemento = enlace.buscarEvidenciaActividadID(id);
            File ima = new File(elemento.getRuta());
            if (!elemento.getRuta().equals("ninguno")) {
                InputStream foto = new FileInputStream(ima);
                int tamano = foto.available();
                byte[] datos = new byte[tamano];
                foto.read(datos, 0, tamano);
                response.getOutputStream().write(datos);
            }else{
                System.out.println("");
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
