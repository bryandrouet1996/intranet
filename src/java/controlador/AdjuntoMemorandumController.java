/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;

/**
 *
 * @author Bryan Druet
 */
@WebServlet(name = "AdjuntoMemorandumController", urlPatterns = {"/AdjuntoMemorandumController.ct"})
public class AdjuntoMemorandumController extends HttpServlet {

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
        conexion enlace = new conexion();
        String opcion = request.getParameter("opcion");
        if (opcion.equalsIgnoreCase("inicial")) {
            int id_memorandum = Integer.parseInt(request.getParameter("id_memorandum"));
            String ruta = enlace.ObtenerMemorandumPorID(id_memorandum).getAdjunto();
            File ficheroXLS = new File(ruta);
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("\nDescargado\n");
        }else if(opcion.equalsIgnoreCase("final")) {
            int id_memorandum = Integer.parseInt(request.getParameter("id_memorandum"));
            String ruta = enlace.ObtenerMemorandumPorID(id_memorandum).getAdjunto_final();
            File ficheroXLS = new File(ruta);
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            System.out.println("\nDescargado\n");
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
