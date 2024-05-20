/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.conexion;
import modelo.tramite;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_tramite", urlPatterns = {"/administrar_tramite.control"})
public class administrar_tramite extends HttpServlet {

    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String FILENAME_KEY = "filename=";
    private static final String PATH = "/newmedia/doc_tramite/";
//    private static final String PATH = "C:/prueba/doc_tramite/";

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
        String accion = request.getParameter("accion");
        conexion enlace = new conexion();
        if (accion.equalsIgnoreCase("registrar_tramite")) {
            int id_tramite = Integer.parseInt(request.getParameter("txtidtra"));
            if (id_tramite == 0) {
                int id_envia = Integer.parseInt(request.getParameter("txtiusu"));
                int id_para = Integer.parseInt(request.getParameter("combodestinatario"));
                Date fecha_elaboracion = Date.valueOf(request.getParameter("txtfechaelaboracion"));
                String hora_elaboracion = request.getParameter("txthoraelaboracion");
                String numero_memo = request.getParameter("txtnumeromemo");
                int tipo_tramite = Integer.parseInt(request.getParameter("combotipo"));
                String asunto = request.getParameter("areaasunto");
                Part part = request.getPart("txtadjunto");
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                String imageName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
                try {
                    PrintWriter out = response.getWriter();
                    try {
                        if (!imageName.isEmpty()) {
                            tramite elemento = new tramite(numero_memo, fecha_elaboracion, hora_elaboracion, id_envia, id_para, asunto, tipo_tramite);
                            elemento.setId_estado(2);
                            int id_tra = enlace.registroTramite(elemento);
                            String path = PATH + id_tra + "_" + imageName;
                            elemento.setAdjunto(path);
                            if (enlace.actualizarAdjuntoTramiteID(id_tra, elemento)) {
                                part.write(path);
                                response.sendRedirect("tramite_salida.jsp");
                            }
                        }
                    } catch (SQLException ex) {
                        System.out.println("actualizarAdjuntoTramiteID | " + ex);
                    }

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            } else {

            }
        } else if (accion.equalsIgnoreCase("colocar_enviado")) {
            int id_tra = Integer.parseInt(request.getParameter("id_tra"));
            System.out.println(id_tra);
            try {
                if (enlace.actualizarEstadoTramiteID(id_tra, 2)) {
                    PrintWriter out = response.getWriter();
                    out.print(id_tra);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }

        } else if (accion.equalsIgnoreCase("descargar_documento")) {
            int id_tramite = Integer.parseInt(request.getParameter("id_tramite"));
            String adjunto = enlace.obtenerTramiteID(id_tramite).getAdjunto();
            File ficheroXLS = new File(adjunto);
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
        } else if (accion.equalsIgnoreCase("eliminar_tramite")) {
            int id_tramite = Integer.parseInt(request.getParameter("id_tramite"));
            tramite t = enlace.obtenerTramiteID(id_tramite);
            new File(t.getAdjunto()).delete();
            if (enlace.eliminarTramiteID(id_tramite)) {
                PrintWriter out = response.getWriter();
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("marcar_leido")) {
            int idTramite = Integer.parseInt(request.getParameter("id"));
            if (enlace.cambiarEstadoTramite(idTramite, 1)) {
                PrintWriter out = response.getWriter();
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("actualizar_adjunto")) {
            int id_tramite = Integer.parseInt(request.getParameter("id"));
            int tipo = Integer.parseInt(request.getParameter("tipo"));
            String numero = request.getParameter("numero");
            String asunto = request.getParameter("asun");
            Part adjunto = request.getPart("archivo");
            tramite t = enlace.obtenerTramiteID(id_tramite);
            new File(t.getAdjunto()).delete();
            String path = PATH + id_tramite + "_" + adjunto.getSubmittedFileName();
            adjunto.write(path);
            if (enlace.cambiarEstadoTramite(id_tramite, 0) && enlace.cambiarAdjuntoTramite(id_tramite, tipo, numero, asunto, path)) {
                PrintWriter out = response.getWriter();
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("devolver")) {
            int idTramite = Integer.parseInt(request.getParameter("id"));
            String motivo = request.getParameter("motivo");
            if (enlace.cambiarEstadoTramite(idTramite, 2) && enlace.devolverTramite(idTramite, motivo)) {
                PrintWriter out = response.getWriter();
                out.print("ok");
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
