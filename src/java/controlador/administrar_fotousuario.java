/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.foto_usuario;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_fotousuario", urlPatterns = {"/administrar_fotousuario.control"})
public class administrar_fotousuario extends HttpServlet {

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
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        System.out.println(idUsuario);
        boolean flag = false;
        String fileName = "";
        List fileItems = null;
        String jrxmlFileName = "/newmedia/imagen_usu/";
        File archivo = new File(jrxmlFileName);
        String path = archivo.getPath() + "/";
        String rutaFichero = "";
        String nombre_foto = "";
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(4096);
            factory.setRepository(new File(path + "/"));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(1024 * 512);
            fileItems = upload.parseRequest(request);
            if (fileItems == null) {
                flag = false;
            }
            Iterator i = fileItems.iterator();
            FileItem actual = null;
            if (i.hasNext()) {
                actual = (FileItem) i.next();
                nombre_foto = fileName = actual.getName();
                File fichero = new File(fileName);
                fichero = new File(path + fichero.getName());
                actual.write(fichero);
                flag = true;
                rutaFichero = fichero.getName();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        foto_usuario elemento = new foto_usuario(idUsuario, path, nombre_foto);
        try {
            enlace.ActualizarFotoUsuario(idUsuario, elemento);
            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                response.sendRedirect("principal.jsp?step=3");
            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                response.sendRedirect("principal_dir.jsp?step=3");
            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                response.sendRedirect("principal_fun.jsp?step=3");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                response.sendRedirect("principal.jsp?step=3");
            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                response.sendRedirect("principal_dir.jsp?step=3");
            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                response.sendRedirect("principal_fun.jsp?step=3");
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
