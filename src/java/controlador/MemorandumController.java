/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.Memorandum;
import modelo.conexion;
import modelo.usuario;
import utilidades.Utility;

/**
 *
 * @author Bryan Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "MemorandumControlller", urlPatterns = {"/memorandum.ct"})
public class MemorandumController extends HttpServlet {

    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String FILENAME_KEY = "filename=";
    private static final String PATH = "/newmedia/doc_memorandum/";
    //private static final String PATH = "C:/prueba/doc_memorandum/";

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
        PrintWriter out = response.getWriter();
        Utility tool = new Utility();
        conexion enlace = new conexion();

        String opcion = request.getParameter("opcion");
        if (opcion.equalsIgnoreCase("registro")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario"));
            int id_asignado = Integer.parseInt(request.getParameter("comboasignacion"));
            String actividad = request.getParameter("txtactividad");
            String participante = request.getParameter("txtparticipante");
            Date fecha_limite = Date.valueOf(request.getParameter("txtfecha"));
            String documento = request.getParameter("txtdocumento");
            String descripcion = request.getParameter("txtdescripcion");
            String observacion = request.getParameter("txtobservacion");
            String resultado = request.getParameter("txtresultados");
            Part part = request.getPart("txtadjunto");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            
            usuario asignador = enlace.buscar_usuarioID(id_usuario);
            String nombre_asignador =asignador.getNombre().concat(" ").concat(asignador.getApellido());
            usuario encargado = enlace.buscar_usuarioID(id_asignado);
            String adjuntoName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
            try {
                if (!adjuntoName.isEmpty()) {
                    Memorandum memorandumInfo = new Memorandum(id_usuario, id_asignado, actividad, participante, fecha_limite, documento, descripcion, observacion, resultado);
                    if (enlace.CrearMemorandum(memorandumInfo)) {
                        Memorandum elemento = enlace.ObtenerMemorandumRecienteUsuarioID(id_usuario);
                        String path = PATH + elemento.getId() + "_" + adjuntoName;
                        elemento.setAdjunto(path);
                        if (enlace.actualizarAdjuntoMemorandumID(elemento.getId(), elemento)) {
                            part.write(path);
                            enlace.enviarCorreoHotmail(encargado.getCorreo(), "Estimado funcionario, su director "+nombre_asignador+" le ha asignado el memorandum "+memorandumInfo.getDocumento()+",dicho documento debe ser resuelto hasta la fecha "+memorandumInfo.getFecha_limite()+" en ("+elemento.getDias_restantes()+") DIAS, para más detalles ingrese a la plataforma intranet con su respectivo correo institucional y la debida contraseña ingresar estos datos y hacer clic al módulo de asignaciones.C");
                            out.print(1);
                        }
                    }else{
                        out.print(-1);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (opcion.equalsIgnoreCase("completar")) {
            int id_memorandum = Integer.parseInt(request.getParameter("txtmemorandum"));
            DecimalFormat format = new DecimalFormat("#.000000000");
            Instant ts2 = Instant.now();
            String resultado = request.getParameter("txtresultados");
            Part part = request.getPart("txtadjunto");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String adjuntoName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
            try {
                if (!adjuntoName.isEmpty()) {
                    Memorandum elemento = enlace.ObtenerMemorandumPorID(id_memorandum);
                    String path = PATH + ts2.getEpochSecond() + "_" + adjuntoName;
                    elemento.setAdjunto_final(path);
                    elemento.setObservacion_final(resultado);
                    if (enlace.completarInformeMemorandumID(elemento.getId(), elemento)) {
                        part.write(path);
                        out.print(1);
                    }else{
                        out.print(-1);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (opcion.equalsIgnoreCase("eliminar")) {
            int id_memorandum = Integer.parseInt(request.getParameter("txtidmemorandum"));
            if (enlace.EliminarMemorandumID(id_memorandum)) {
                out.println(1);
            } else {
                out.println(-1);
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
