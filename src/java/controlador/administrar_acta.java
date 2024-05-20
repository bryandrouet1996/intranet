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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.acta;
import modelo.anular_convocatoria;
import modelo.compromiso_acta;
import modelo.conexion;
import modelo.convocados;
import modelo.convocatoria;
import modelo.usuario;
import modelo.verificable_compromiso;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_acta", urlPatterns = {"/administrar_acta.control"})
public class administrar_acta extends HttpServlet {

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
        if (accion.equalsIgnoreCase("registro_acta")) {
            PrintWriter out = response.getWriter();
            int id_ac = Integer.parseInt(request.getParameter("txtidacta"));
            int id_convocatoria = Integer.parseInt(request.getParameter("txtconvoca"));
            java.sql.Date fecha_acta = Date.valueOf(request.getParameter("txtfechaacta"));
            java.sql.Date fecha_convocatoria = Date.valueOf(request.getParameter("txtfechaconvocatoria"));
            String str[] = request.getParameterValues("comboasistentes");
            int id_medio = Integer.parseInt(request.getParameter("combomedio"));
            String hora_inicio = request.getParameter("txthorainicio");
            String hora_fin = request.getParameter("txthorafin");
            String asunto = request.getParameter("txtasunto");
            String lugar = request.getParameter("txtlugar");
            String desarrollo = request.getParameter("areadesarrollo");
            String orden_dia = request.getParameter("areaobservacion");
            acta elemento = new acta(id_convocatoria, fecha_acta, fecha_convocatoria, asunto, lugar, id_medio, hora_inicio, hora_fin, orden_dia, desarrollo, true);
            try {
                if (id_ac == 0) {
                    if (enlace.registroActa(elemento)) {
                        int id_acta = enlace.idMaxActa();
                        if (enlace.registrarAsistentesActaArray(id_acta, str)) {
                            out.print(id_acta);
                        }
                    }
                } else {
                    if (enlace.actualizarActa(id_ac, elemento)) {
                        out.print(id_ac);
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registro_compromiso")) {
            PrintWriter out = response.getWriter();
            int id_acta = Integer.parseInt(request.getParameter("txtacta"));
            int id_conv = Integer.parseInt(request.getParameter("txtconv"));
            java.sql.Date fecha_convocatoria = Date.valueOf(request.getParameter("txtfechacumplimiento"));
            System.out.println(fecha_convocatoria);
            int id_responsable = Integer.parseInt(request.getParameter("comboresponsable"));
            String compromiso = request.getParameter("areacompromiso");
            compromiso_acta elemento = new compromiso_acta(id_acta, compromiso, id_responsable, 0, fecha_convocatoria);
            try {
                if (enlace.registroCompromiso(elemento)) {
                    out.print("registro_acta.jsp?ic=" + id_conv + "&iact=" + id_acta);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registro_convocatoria")) {
            PrintWriter out = response.getWriter();
            int id_usuar = Integer.parseInt(request.getParameter("txtiusu"));
            int id_convoca = Integer.parseInt(request.getParameter("txticconv"));
            java.sql.Date fecha_convocatoria = Date.valueOf(request.getParameter("txtfechaconvocatoria"));
            String asunto = request.getParameter("txtasunto");
            String hora_inicio = request.getParameter("txthorainicio");
            String hora_fin = request.getParameter("txthorafin");
            String lugar = request.getParameter("txtlugar");
            String orden_dia = request.getParameter("areaobservacion");
            String str[] = request.getParameterValues("comboasistentes");
            int id_convocatoria = 0;
            String funcion_usuario = enlace.ObtenerFuncionUsuarioID(id_usuar);
            String codigo_funcion = enlace.obtenerCodigoFuncionUsuario(id_usuar);
            convocatoria elemento = new convocatoria(fecha_convocatoria, id_usuar, asunto, lugar, hora_inicio, hora_fin, orden_dia);
            try {
                usuario us = enlace.buscar_usuarioID(id_usuar);
                String mensaje = "<p align='justify'><br>Estimado funcionario usted ha sido convocado desde módulo de redacción de actas y compromisos por <b>" + us.getApellido() + " " + us.getNombre() + "</b> con el motivo de <b>" + asunto + "</b>, la reunión se llevará acabo el día <b>" + fecha_convocatoria + "</b> en <b>" + lugar + "</b> a las <b>" + hora_inicio + "</b> con estimación a finalizar a las <b>" + hora_fin + "</b><br><br><br><b>DIRECCIÓN DE TECNOLOGÍAS DE LA INFORMACIÓN</b></p>";
                if (id_convoca == 0) {
                    if (enlace.registroConvocatoria(elemento)) {
                        id_convocatoria = enlace.idUltimaConvocatoria();
                        out.print(id_convocatoria);
                        if (str[0].equalsIgnoreCase("0")) {
                            ArrayList<usuario> listado = null;
                            if (funcion_usuario.equalsIgnoreCase("coordinador") || enlace.tipoUsuario(us.getId_usuario()).equalsIgnoreCase("director")) {
                                listado = enlace.listadosDirectoresDireccionesUnidades();
                            }
                            for (usuario ele : listado) {
                                convocados element = new convocados(id_convocatoria, ele.getId_usuario());
                                if (enlace.registroConvocados(element)) {

                                }
                            }
                            String[] para = enlace.listadoCorreosConvocadosIdConvocatoria(id_convocatoria);
                            enlace.enviarCorreoMasivo(para, mensaje, "Convocatoria");
                        } else {
                            for (int paso = 0; paso < str.length; paso++) {
                                int id_usuario = Integer.parseInt(str[paso]);
                                convocados element = new convocados(id_convocatoria, id_usuario);
                                if (enlace.registroConvocados(element)) {

                                }
                            }
                            String[] para = enlace.listadoCorreosConvocadosIdConvocatoria(id_convocatoria);
                            enlace.enviarCorreoMasivo(para, mensaje, "Convocatoria");
                        }
                    }
                } else {
                    if (enlace.actualizarConvocatoriaID(id_convoca, elemento)) {
                        id_convocatoria = id_convoca;
                        out.print(id_convocatoria);
                        if (str[0].equalsIgnoreCase("0")) {
                            ArrayList<usuario> listado = null;
                            if (funcion_usuario.equalsIgnoreCase("coordinador") || enlace.tipoUsuario(us.getId_usuario()).equalsIgnoreCase("director")) {
                                listado = enlace.listadosDirectoresDireccionesUnidades();
                            }
                            enlace.eliminarConvocadosIDConvocatoria(id_convocatoria);
                            for (usuario ele : listado) {
                                convocados element = new convocados(id_convocatoria, ele.getId_usuario());
                                if (enlace.registroConvocados(element)) {

                                }
                            }
                            String[] para = enlace.listadoCorreosConvocadosIdConvocatoria(id_convocatoria);
                            enlace.enviarCorreoMasivo(para, mensaje, "Convocatoria");
                        } else {
                            //System.out.println("hola");
                            enlace.eliminarConvocadosIDConvocatoria(id_convocatoria);
                            for (int paso = 0; paso < str.length; paso++) {
                                int id_usuario = Integer.parseInt(str[paso]);
                                convocados element = new convocados(id_convocatoria, id_usuario);
                                if (enlace.registroConvocados(element)) {

                                }
                            }
                            String[] para = enlace.listadoCorreosConvocadosIdConvocatoria(id_convocatoria);
                            enlace.enviarCorreoMasivo(para, mensaje, "Convocatoria");
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("anular_convocatoria")) {
            PrintWriter out = response.getWriter();
            int id_convocatoria = Integer.parseInt(request.getParameter("iconv"));
            String razon = request.getParameter("txtrazon");
            anular_convocatoria elemento = new anular_convocatoria(id_convocatoria, razon);
            try {
                if (enlace.registroAnularConvocatoria(elemento)) {
                    out.print("ok");
                    enlace.actualizarConvocatoriaEstado(id_convocatoria, 2);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }

        } else if (accion.equalsIgnoreCase("eliminar_compromiso")) {
            PrintWriter out = response.getWriter();
            int id_compromiso = Integer.parseInt(request.getParameter("icomp"));
            try {
                if (enlace.eliminarCompromisoID(id_compromiso)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("finalizar_acta")) {
            PrintWriter out = response.getWriter();
            int ic = Integer.parseInt(request.getParameter("ic"));
            int id_ac = Integer.parseInt(request.getParameter("iact"));
            java.sql.Date fecha_acta = Date.valueOf(request.getParameter("txtfechaacta"));
            java.sql.Date fecha_convocatoria = Date.valueOf(request.getParameter("txtfechaconvocatoria"));
            String str[] = request.getParameterValues("comboasistentes");
            int id_medio = Integer.parseInt(request.getParameter("combomedio"));
            String hora_inicio = request.getParameter("txthorainicio");
            String hora_fin = request.getParameter("txthorafin");
            String asunto = request.getParameter("txtasunto");
            String lugar = request.getParameter("txtlugar");
            String desarrollo = request.getParameter("areadesarrollo");
            String orden_dia = request.getParameter("areaorden");
            acta elemento = new acta(ic, fecha_acta, fecha_convocatoria, asunto, lugar, id_medio, hora_inicio, hora_fin, orden_dia, desarrollo, true);
            if (enlace.actualizarActa(id_ac, elemento)) {
                try {
                    enlace.actualizarEstadoConvocatoria(ic);
                    out.print(id_ac);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_cumplimiento")) {
            PrintWriter out = response.getWriter();
            compromiso_acta elemento = new compromiso_acta();
            int id_compromiso = Integer.parseInt(request.getParameter("txtidcompromiso"));
            String grado_cumplimiento = request.getParameter("combogrado");
            String descripcion = request.getParameter("txtdescripcion");
            elemento.setGrado(grado_cumplimiento);
            elemento.setDescripcion(descripcion);
            elemento.setEstado(1);
            try {
                if (enlace.actualizarCompromisoGradoAccion(id_compromiso, elemento)) {
                    out.print("ok");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registro_verificable")) {
            PrintWriter out = response.getWriter();
            int id_compromiso = Integer.parseInt(request.getParameter("txtidcompromiso1"));
            String nombre_archivo = request.getParameter("txtnombre");
            Part part = request.getPart("txtarchivo");
            String docPath = "/newmedia/doc_verificable";
            File fileDir = new File(docPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String docName = extractFilename(part.getHeader(CONTENT_DISPOSITION));
            try {
                if (!docName.isEmpty()) {
                    String ruta_completa = docPath + "/" + id_compromiso + "_" + docName;
                    part.write(docPath + File.separator + id_compromiso + "_" + docName);
                    verificable_compromiso elemento = new verificable_compromiso(id_compromiso, nombre_archivo, ruta_completa);
                    if (enlace.registroVerificableCompromiso(elemento)) {
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("existe_verificable")) {
            PrintWriter out = response.getWriter();
            int id_compromiso = Integer.parseInt(request.getParameter("idcomp"));
            try {
                if (enlace.existeVerificableCompromiso(id_compromiso)) {
                    out.print(1);
                } else {
                    out.print(0);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("descargar_verificable")) {
            int id_verificable = Integer.parseInt(request.getParameter("id_veri"));
            String ruta = enlace.obtenerVerificablesID(id_verificable).getRuta();
            File ficheroXLS = new File(ruta);
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
            System.out.println("\nDescargado\n");
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
