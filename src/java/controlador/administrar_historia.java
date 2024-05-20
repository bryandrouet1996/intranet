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
import modelo.historia;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_historia", urlPatterns = {"/administrar_historia.control"})
public class administrar_historia extends HttpServlet {

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
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("historia")) {
            boolean flag = false;
            String fileName = "";
            List fileItems = null;
            String jrxmlFileName = "/WEB-INF/classes/imagen_historia/";
            File archivo = new File(request.getRealPath(jrxmlFileName));
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
            historia elemento = new historia(idUsuario, enlace.generarToken(), path, nombre_foto, enlace.generarToken(), enlace.fechaActual(), enlace.hora_actual(), 2);
            try {
                enlace.registroHistoria(elemento);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=27");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=27");
                }
            }
        } else if (accion.equalsIgnoreCase("login")) {
            boolean flag = false;
            String fileName = "";
            List fileItems = null;
            String jrxmlFileName = "/WEB-INF/classes/imagen_login/";
            File archivo = new File(request.getRealPath(jrxmlFileName));
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
            historia elemento = new historia(idUsuario, enlace.generarToken(), path, nombre_foto, enlace.generarToken(), enlace.fechaActual(), enlace.hora_actual(), 1);
            try {
                enlace.registroHistoria(elemento);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=26");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=26");
                }
            }
        } else if (accion.equalsIgnoreCase("login_eliminar")) {
            int id_historia = Integer.parseInt(request.getParameter("ih"));
            if (enlace.eliminarloginImagen(id_historia)) {
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=26");
                }
            } else {
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=26");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=26");
                }
            }
        }else if (accion.equalsIgnoreCase("historia_eliminar")) {
            int id_historia = Integer.parseInt(request.getParameter("ih"));
            if (enlace.eliminarloginImagen(id_historia)) {
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=27");
                }
            } else {
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=27");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=27");
                }
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
