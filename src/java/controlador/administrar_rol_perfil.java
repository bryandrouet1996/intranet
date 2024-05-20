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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.Rol;
import modelo.Version;
import modelo.conexion;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
@WebServlet(name = "administrar_rol_perfil", urlPatterns = {"/administrar_rol_perfil.control"})
public class administrar_rol_perfil extends HttpServlet {

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
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registrar_rol")) {
            int idUsu = Integer.parseInt(request.getParameter("idUsu"));
            String des = request.getParameter("des").toLowerCase();
            Rol rol = new Rol();
            rol.setDescripcion(des);
            try {
                enlace.registrarRol(rol);
                out.print("ok");
            } catch (SQLException ex) {
                System.out.println("registrarRol | " + ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_rol")) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (enlace.eliminarRol(id)) {
                out.print("x");
            }
        } else if (accion.equalsIgnoreCase("modificar_rol")) {
            int admin = Integer.parseInt(request.getParameter("admin"));
            int usu = Integer.parseInt(request.getParameter("usu"));
            String rolesStr = request.getParameter("roles");
            String[] roles = rolesStr.split(",");
            try {
                enlace.eliminarRolesNoElegidosParaUsuario(admin, usu, roles);
                for (String rol : roles) {
                    if (!enlace.verificarUsuarioCumpleRol(usu, Integer.parseInt(rol))) {
                        enlace.registroRolUsuario(admin, usu, Integer.parseInt(rol));
                    }
                }
                out.print("x");
            } catch (SQLException e) {
                System.out.println("modificar_rol | " + e);
            }
        } else if (accion.equalsIgnoreCase("modificar_modulos_rol")) {
            int admin = Integer.parseInt(request.getParameter("admin"));
            int rol = Integer.parseInt(request.getParameter("rol"));
            String modulosStr = request.getParameter("modulos");
            String[] modulos = modulosStr.split(",");
            try {
                enlace.eliminarModulosNoElegidosParaRol(rol, modulos);
                for (String m : modulos) {
                    if (!enlace.verificarRolTieneModulo(rol, Integer.parseInt(m))) {
                        enlace.registrarModuloParaRol(rol, Integer.parseInt(m));
                    }
                }
                out.print("x");
            } catch (SQLException e) {
                System.out.println("modificar_modulos_rol | " + e);
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
