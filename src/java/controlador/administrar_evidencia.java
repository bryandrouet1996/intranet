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
import javax.servlet.http.Part;
import modelo.conexion;
import modelo.evidencia_actividad;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_evidencia", urlPatterns = {"/administrar_evidencia.control"})
public class administrar_evidencia extends HttpServlet {

    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String FILENAME_KEY = "filename=";

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
        try {
            int id_actividad = Integer.parseInt(request.getParameter("txtidact"));
            PrintWriter out = response.getWriter();
            Part part = request.getPart("txtadjunto");
            String nombre = request.getParameter("txtnombre");
            String imagePath = "/newmedia/doc_actividad";
            File fileDir = new File(imagePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String imageName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
            String ruta_completa = imagePath + "/" + id_actividad + "_" + imageName;
            try {
                evidencia_actividad elemento = new evidencia_actividad(id_actividad, nombre, ruta_completa);
                System.out.println(elemento.getNombre());
                System.out.println(part.getSize());
                try {
                    if (!imageName.isEmpty()) {
                        part.write(imagePath + File.separator + id_actividad + "_" + imageName);
                        if (enlace.registroEvidenciaActividad(elemento)) {
                            out.print("ok");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private String extractFilename(String contentDisposition) {
        if (contentDisposition == null) {
            return null;
        }
        int startIndex = contentDisposition.indexOf(FILENAME_KEY);
        if (startIndex == -1) {
            return null;
        }
        String filename = contentDisposition.substring(startIndex + FILENAME_KEY.length());
        if (filename.startsWith("\"")) {
            int endIndex = filename.indexOf("\"", 1);
            if (endIndex != -1) {
                return filename.substring(1, endIndex);
            }
        } else {
            int endIndex = filename.indexOf(";");
            if (endIndex != -1) {
                return filename.substring(0, endIndex);
            }
        }
        return filename;
    }

    private boolean validateImage(String imageName) {
        String fileExt = imageName.substring(imageName.length() - 3);
        if ("jpg".equals(fileExt) || "png".equals(fileExt) || "gif".equals(fileExt) || "docx".equals(fileExt) || "pdf".equals(fileExt) || "xls".equals(fileExt)) {
            return true;
        }
        return false;
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
