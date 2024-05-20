/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Formulario;
import modelo.Version;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "descargar_archivo", urlPatterns = {"/descargar_archivo.control"})
public class descargar_archivo extends HttpServlet {

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
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("descargar_formulario")) {
            int idForm = Integer.parseInt(request.getParameter("id_form"));
            Formulario f = enlace.getFormulario(idForm);
            File ficheroXLS = new File(f.getDoc());
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("descargar_version")) {
            int idVer = Integer.parseInt(request.getParameter("id_ver"));
            Version ver = enlace.getVersionById(idVer);
            File ficheroXLS = new File(ver.getAdjunto());
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            arch.flush();
            arch.close();
        } else if (accion.equalsIgnoreCase("descargar_archivo")) {
            File ficheroXLS = new File(request.getParameter("ruta"));
            FileInputStream fis = new FileInputStream(ficheroXLS);
            int tamano = fis.available();
            byte[] bytes = new byte[tamano];
            int read = 0;
            String fileName = ficheroXLS.getName();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream arch = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
                arch.write(bytes, 0, read);
            }
            fis.close();
            arch.flush();
            arch.close();
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
