/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.Manual;
import modelo.VersionManual;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
@WebServlet(name = "administrar_man", urlPatterns = {"/administrar_man.control"})
public class administrar_man extends HttpServlet {

    private static final String PATH = "/newmedia/repositorio/";
//    private static final String PATH = "C:/prueba/repositorio/";

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
        HttpSession sesion = request.getSession();
        String accion = request.getParameter("accion");
        PrintWriter out;
        if (accion.equalsIgnoreCase("registrar")) {
            out = response.getWriter();
            int idUsu = Integer.parseInt(request.getParameter("idUsu")),
                    manual = Integer.parseInt(request.getParameter("cmbMan"));
            String titulo = request.getParameter("titulo"),
                    descripcion = request.getParameter("desc");
            Part archivo = request.getPart("archivo");
            VersionManual v = new VersionManual();
            v.setIdUsuario(idUsu);
            v.setIdManual(manual);
            v.setTitulo(titulo);
            v.setDescripcion(descripcion);
            int idVer = 0;
            try {
                idVer = mysql.registrarVersionManual(v);
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                final String path = PATH + idVer + "_" + archivo.getSubmittedFileName();
                archivo.write(path);
                if (mysql.actualizarAdjuntoVersionManual(idVer, path)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println("registrarVersionManual | actualizarAdjuntoVersionManual | " + ex);
                mysql.eliminarVersionManual(idVer);
            }
        } else if (accion.equalsIgnoreCase("registrar_doc")) {
            out = response.getWriter();
            int tipo = Integer.parseInt(request.getParameter("cmbTipo"));
            String titulo = request.getParameter("titulo"),
                    descripcion = request.getParameter("desc");
            Manual m = new Manual();
            m.setIdTipo(tipo);
            m.setTitulo(titulo);
            m.setDescripcion(descripcion);
            try {
                mysql.registrarManual(m);
                out.print("ok");
            } catch (Exception ex) {
                System.out.println("registrarManual | " + ex);
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
