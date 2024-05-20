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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.almacenamiento;
import modelo.conexion;
import modelo.recurso;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_almacenamiento", urlPatterns = {"/administrar_almacenamiento.control"})
public class administrar_almacenamiento extends HttpServlet {

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
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registrar_almacenamiento")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtiu"));
            String nombre = request.getParameter("txtnombre");
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            almacenamiento elemento = new almacenamiento(id_usuario, nombre, id_tipo);
            try {
                PrintWriter out = response.getWriter();
                if (enlace.registroAlmacenamiento(elemento)) {
                    out.print(1);
                } else {
                    out.print(0);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("actualizar_almacenamiento")) {
            int id_almacenamiento = Integer.parseInt(request.getParameter("txtial"));
            String nombre = request.getParameter("txtnombre");
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            almacenamiento elemento = new almacenamiento(nombre, id_tipo);
            try {
                PrintWriter out = response.getWriter();
                if (enlace.actualizarAlmacenamientoID(id_almacenamiento, elemento)) {
                    out.print(1);
                } else {
                    out.print(0);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("inhabilitar_almacenamiento")) {
            int id_almacenamiento = Integer.parseInt(request.getParameter("txtial"));
            try {
                PrintWriter out = response.getWriter();
                if (enlace.inhabilitarAlmacenamientoID(id_almacenamiento)) {
                    out.print(1);
                } else {
                    out.print(0);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registrar_recurso")) {
            int id_almacenamiento = Integer.parseInt(request.getParameter("txtial"));
            int id_usuario = Integer.parseInt(request.getParameter("txtiu"));
            String nombre = request.getParameter("txtnombre");
            String descripcion = request.getParameter("areadescripcion");
            Part part = request.getPart("txtadjunto");
            String imagePath = "/newmedia/doc_actividad";
            File fileDir = new File(imagePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String imageName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
            try {
                PrintWriter out = response.getWriter();
                try {
                    if (!imageName.isEmpty()) {
                        recurso elemento = new recurso(id_almacenamiento, id_usuario, nombre, descripcion);
                        if (enlace.registroRecurso(elemento)) {
                            int id_recurso = enlace.idUltimoRecurso();
                            String ruta_completa = imagePath + "/" + id_almacenamiento + "_" + id_recurso + "_" + imageName;
                            elemento.setRuta(ruta_completa);
                            if (enlace.actualizarAdjuntoRecursoID(id_recurso, elemento)) {
                                part.write(imagePath + File.separator + id_almacenamiento + "_" + id_recurso + "_" + imageName);
                                out.print("ok");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
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
