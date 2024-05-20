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
import modelo.calendario_google;
import modelo.conexion;
import modelo.conexion_oracle;
import modelo.foto_usuario;
import modelo.usuario;
import static org.apache.commons.fileupload.FileUploadBase.CONTENT_DISPOSITION;

/**
 *
 * @author usuario
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_usuario", urlPatterns = {"/administrar_usuario.control"})

public class administrar_usuario extends HttpServlet {

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
        conexion enlace = new conexion();
        try (PrintWriter out = response.getWriter()) {
            String accion = request.getParameter("accion");
            boolean estado = false;
            if (accion.equalsIgnoreCase("registrar")) {
                int id_usuario = 0;
                String nombres = request.getParameter("txtnombre");
                String apellidos = request.getParameter("txtapellido");
                String cedula = request.getParameter("txtcedula");
                String correo = request.getParameter("txtcorreo");
                String cargo = request.getParameter("combocargo");
                String unidad = request.getParameter("combounidad");
                java.sql.Date fecha_nacimiento = Date.valueOf(request.getParameter("txtfechanacimiento"));
                String iniciales = enlace.iniciales(nombres + " " + apellidos);
                int tipo_usuario = Integer.parseInt(request.getParameter("combotipo"));
                conexion_oracle oracle = new conexion_oracle();
                usuario usuCabildo = oracle.getUsuarioActivo(cedula);
                if (usuCabildo != null) {
                    usuario anterior = enlace.buscarUsuarioCedula(cedula);
                    if (anterior.getId_usuario() == 0) {
                        usuario elemento = new usuario(usuCabildo.getCodigo_usuario(), nombres, apellidos, cedula, correo, cedula, iniciales, cargo, unidad, fecha_nacimiento, enlace.fechaActual());
                        try {
                            if (enlace.registroUsuario(elemento)) {
                                //ACTUALIZACIÓN CORREO EN CABILDO
                                usuario nuevo = enlace.buscarUsuarioCedula(cedula);
                                id_usuario = nuevo.getId_usuario();
                                enlace.registroFotoVaciaUsuario(id_usuario);
                                enlace.registroPermisoUsuario(id_usuario);
                                enlace.registroSesionUsuario(id_usuario);
                                enlace.registroTipoUsuario(id_usuario, tipo_usuario);
                                out.print("1");
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                            out.print("-3");
                        }
                    } else {
                        out.print("-2");
                    }
                } else {
                    out.print("-1");
                }
            } else if (accion.equalsIgnoreCase("modificarTelegram")) {
                String chat_id = null, swich = null;
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                chat_id = request.getParameter("txtchat");
                swich = request.getParameter("swichtelegram");
                if (swich != null) {
                    estado = true;
                } else {
                    estado = false;
                }
                enlace.actualizarTelegramUsuario(chat_id, estado, idUsuario);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=3");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=3");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=3");
                }
            } else if (accion.equalsIgnoreCase("modificarWebmail")) {
                String webmailclave = request.getParameter("txtclavemail");
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                if (!webmailclave.equals("")) {
                    enlace.actualizarTClaveWebmail(webmailclave, idUsuario);
                    out.print("x");
                }
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=3");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=3");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=3");
                }
            } else if (accion.equalsIgnoreCase("cambioClave")) {
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                usuario elemento = new usuario();
                usuario el = enlace.buscar_usuarioID(idUsuario);
                String contraactual = request.getParameter("txtcontraactual");
                String nuevacontra = request.getParameter("txtnuevacontra");
                String repitacontra = request.getParameter("txtrepitacontra");
                elemento.setCorreo(el.getCorreo());
                elemento.setClave(contraactual);
                if (enlace.comprobacionUsuario(elemento)) {
                    if (nuevacontra.equalsIgnoreCase(repitacontra)) {
                        try {
                            enlace.ActualizarClaveUsuario(idUsuario, nuevacontra);
                            out.print("ok");
                        } catch (SQLException ex) {
                        }
                    }
                }
            } else if (accion.equalsIgnoreCase("bloqueo")) {
                int idUsuario = Integer.parseInt(request.getParameter("iu"));
                if (enlace.deshabilitarUsuario(idUsuario)) {
                    out.print("ok");
                } else {

                }
            } else if (accion.equalsIgnoreCase("desbloqueo")) {
                int idUsuario = Integer.parseInt(request.getParameter("iu"));
                if (enlace.habilitarUsuario(idUsuario)) {
                    out.print("ok");
                } else {

                }
            } else if (accion.equalsIgnoreCase("reseteo")) {
                int idUsuario = Integer.parseInt(request.getParameter("idusu"));
                String pass = request.getParameter("pass");
                if (enlace.reseteoClaveUsuario(idUsuario, pass)) {
                    out.print("ok");
                }
            } else if (accion.equalsIgnoreCase("foto_usuario")) {
                int idUsuario = Integer.parseInt(request.getParameter("iu"));
                Part part = request.getPart("txtfoto");
                String docPath = "/newmedia/imagen_usu";
                File fileDir = new File(docPath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                String docName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
                try {
                    String ruta_completa = docPath + "/" + idUsuario + "_" + docName;
                    part.write(docPath + File.separator + idUsuario + "_" + docName);
                    foto_usuario elemento = new foto_usuario(idUsuario, ruta_completa);
                    if (enlace.ActualizarFotoUsuario(idUsuario, elemento)) {
                        out.print("ok");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (accion.equalsIgnoreCase("modificarinfo")) {
                usuario elemento = new usuario();
                int idUsuario = Integer.parseInt(request.getParameter("iu"));
                String nombre = request.getParameter("txtnombre");
                String apellido = request.getParameter("txtapellido");
                String correo = request.getParameter("txtcorreo");
                String cedula = request.getParameter("txtcedula");
                if (!request.getParameter("combocargo").equalsIgnoreCase("")) {
                    elemento.setCodigo_cargo(request.getParameter("combocargo"));
                }
                if (!request.getParameter("combounidad").equalsIgnoreCase("")) {
                    elemento.setCodigo_unidad(request.getParameter("combounidad"));
                }
                String tipo_usuario = request.getParameter("combotipo");
                elemento.setCorreo(correo);
                elemento.setCedula(cedula);
                elemento.setNombre(nombre);
                elemento.setApellido(apellido);
                System.out.println(elemento.toString() + " " + tipo_usuario);
                if (elemento.getCodigo_unidad() != null && elemento.getCodigo_cargo() != null) {
                    if (enlace.actualizarUsuarioInformacion(elemento, idUsuario)) {
                        out.print("ok");
                    } else {

                    }
                } else if (elemento.getCodigo_unidad() == null && elemento.getCodigo_cargo() != null) {
                    if (enlace.actualizarUsuarioInformacionCargo(elemento, idUsuario)) {
                        out.print("ok");
                    } else {

                    }

                } else if (elemento.getCodigo_unidad() != null && elemento.getCodigo_cargo() == null) {
                    if (enlace.actualizarUsuarioInformacionUnidad(elemento, idUsuario)) {
                        out.print("ok");
                    } else {

                    }
                }
            } else if (accion.equalsIgnoreCase("eliminar")) {
                int idUsuario = Integer.parseInt(request.getParameter("iu"));
                if (enlace.eliminarUsuarioId(idUsuario)) {
                    out.print("ok");
                } else {

                }
            } else if (accion.equalsIgnoreCase("eliminar_calendario")) {
                int id_calendario = Integer.parseInt(request.getParameter("ic"));
                if (enlace.eliminarCalendarioGoogleID(id_calendario)) {
                    out.print("ok");
                } else {

                }
            } else if (accion.equalsIgnoreCase("registro_calendario")) {
                int id_usuario = Integer.parseInt(request.getParameter("idUsuario"));
                String nombre = request.getParameter("txtcalendario");
                calendario_google elemento = new calendario_google(id_usuario, nombre);
                try {
                    if (enlace.registroCalendarioGoogle(elemento)) {
                        out.print("ok");
                    } else {

                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else if (accion.equalsIgnoreCase("firma_usuario")) {
                int id_usuario = Integer.parseInt(request.getParameter("txtidusuario11"));
                Part part = request.getPart("txtfirma");
                String imagePath = "/newmedia/firma_usuario";
                File fileDir = new File(imagePath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                String imageName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
                String ruta_completa = imagePath + "/" + imageName;
                try {
                    part.write(imagePath + File.separator + imageName);
                } catch (Exception ex) {
                }
                try {
                    if (validateImage(imageName)) {
                        enlace.ActualizarFirmaUsuario(id_usuario, ruta_completa);
                        out.print("ok");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
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

    private boolean validateImage(String imageName) {
        String fileExt = imageName.substring(imageName.length() - 3);
        if ("jpg".equals(fileExt) || "png".equals(fileExt)) {
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
