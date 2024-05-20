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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.AnulacionSoporte;
import modelo.DevolucionSoporte;
import modelo.asignacion_soporte;
import modelo.atencion_soporte;
import modelo.atendido_soporte;
import modelo.calificacion_soporte;
import modelo.conexion;
import modelo.diagnostico_soporte;
import modelo.rechazo_soporte;
import modelo.solicitud_soporte;
import modelo.usuario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_ticket", urlPatterns = {"/administrar_ticket.control"})
public class administrar_ticket extends HttpServlet {

    private static final String PATH = "/newmedia/adjuntos_soportes/";
//    private static final String PATH = "C:/prueba/adjuntos_soportes/";

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
        PrintWriter out;
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("registro_ticket")) {
            out = response.getWriter();
            int id_usuario = Integer.parseInt(request.getParameter("txtidusu"));
            int id_solicitante = Integer.parseInt(request.getParameter("combosolicitante"));
            int id_forma = Integer.parseInt(request.getParameter("comboforma"));
            int id_sugerido = Integer.parseInt(request.getParameter("combosugerido"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            String incidente = request.getParameter("areadescripcion").replaceAll("\"", "\'");
            Part archivo = request.getPart("adjunto");
            solicitud_soporte elemento = new solicitud_soporte(id_usuario, id_solicitante, id_sugerido, id_tipo, id_forma, incidente);
            try {
                elemento.setReferencia(Integer.parseInt(request.getParameter("referencia")));
            } catch (NumberFormatException e) {
            }
            try {
                int id_solicitud = enlace.registroSolicitudSoporte(elemento);
                if (archivo.getSubmittedFileName().equals("")) {
                    out.print("ok");
                } else {
                    File fileDir = new File(PATH);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    final String path = PATH + "ADJUNTO_" + id_solicitud + "_" + archivo.getSubmittedFileName();
                    archivo.write(path);
                    enlace.ActualizarAdjuntoSoporte(id_solicitud, path);
                    out.print("ok");
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_solicitud")) {
            out = response.getWriter();
            int id_soporte = Integer.parseInt(request.getParameter("isop"));
            if (enlace.eliminarSolicitudSoporteID(id_soporte)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("editar_ticket")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsoli"));
            int id_solicitante = Integer.parseInt(request.getParameter("combosolicitante"));
            int id_forma = Integer.parseInt(request.getParameter("comboforma"));
            int id_sugerido = Integer.parseInt(request.getParameter("combosugerido"));
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            String incidente = request.getParameter("areadescripcion").replaceAll("\"", "\'");
            solicitud_soporte elemento = new solicitud_soporte(id_solicitante, id_sugerido, id_tipo, id_forma, incidente);
            try {
                if (enlace.actualizarSolicitudSoporteID(elemento, id_solicitud)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("asignar_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("isop"));
            int id_administrador = Integer.parseInt(request.getParameter("idad"));
            int id_tecnico = Integer.parseInt(request.getParameter("idtec"));
            asignacion_soporte elemento = new asignacion_soporte(id_solicitud, id_administrador, id_tecnico);
            try {
                if (enlace.registroAsignacionSoporteTecnicoID(elemento)) {
                    if (enlace.actualizarEstadoSolicitudID(1, id_solicitud)) {
                        solicitud_soporte sop = enlace.obtenerSolicitudSoporte(id_solicitud);
                        usuario soli = enlace.obtenerSolicitanteSoporte(id_solicitud);
                        enlace.enviarCorreoMod(enlace.getUsuarioId(id_tecnico).getCorreo(), "ASIGNACIÓN DE SOPORTE SP-" + id_solicitud, "El soporte SP-" + id_solicitud + " te fue asignado, ingresa a la Intranet para gestionarlo. Solicitante: " + soli.getApellido() + " " + soli.getNombre() + " - " + enlace.getDireccionUsuarioId(soli.getId_usuario()) + ". Descripción: " + sop.getIncidente());
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("reasignar_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("isop"));
            int id_administrador = Integer.parseInt(request.getParameter("idad"));
            int id_tecnico = Integer.parseInt(request.getParameter("idtec"));
            asignacion_soporte elemento = new asignacion_soporte(id_solicitud, id_administrador, id_tecnico);
            try {
                if (enlace.eliminarAsignacionSoporteID(id_solicitud)) {
                    if (enlace.registroAsignacionSoporteTecnicoID(elemento)) {
                        solicitud_soporte sop = enlace.obtenerSolicitudSoporte(id_solicitud);
                        usuario soli = enlace.obtenerSolicitanteSoporte(id_solicitud);
                        enlace.enviarCorreoMod(enlace.getUsuarioId(id_tecnico).getCorreo(), "REASIGNACIÓN DE SOPORTE SP-" + id_solicitud, "El soporte SP-" + id_solicitud + " te fue reasignado, ingresa a la Intranet para gestionarlo. Solicitante: " + soli.getApellido() + " " + soli.getNombre() + " - " + enlace.getDireccionUsuarioId(soli.getId_usuario()) + ". Descripción: " + sop.getIncidente());
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }

        } else if (accion.equalsIgnoreCase("atender_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("isop"));
            int id_tecnico = Integer.parseInt(request.getParameter("idtec"));
            atencion_soporte elemento = new atencion_soporte(id_solicitud, id_tecnico);
            try {
                if (enlace.registroAtencionSoporteTecnico(elemento)) {
                    if (enlace.actualizarEstadoSolicitudID(2, id_solicitud)) {
                        usuario tecnico = enlace.obtenerTecnicoAsignadoSolicitudSoporte(id_solicitud);
                        enlace.enviarCorreoMod(enlace.getUsuarioSoporte(id_solicitud).getCorreo(), "ATENCIÓN DE SOPORTE SP-" + id_solicitud, "El soporte SP-" + id_solicitud + " está siendo atendido por: " + tecnico.getApellido() + " " + tecnico.getNombre() + ".");
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("cerrar_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("isop"));
            int id_tecnico = Integer.parseInt(request.getParameter("idtec"));
            atendido_soporte elemento = new atendido_soporte(id_solicitud, id_tecnico);
            try {
                if (enlace.registroAtendidoSoporteTecnico(elemento)) {
                    if (enlace.actualizarEstadoSolicitudID(3, id_solicitud)) {
                        enlace.enviarCorreoMod(enlace.getUsuarioSoporte(id_solicitud).getCorreo(), "CIERRE DE SOPORTE SP-" + id_solicitud, "El soporte SP-" + id_solicitud + " fue atendido, ingresa a la Intranet y califícalo.");
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registrar_diagnostico")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsolicitud1"));
            int id_tecnico = Integer.parseInt(request.getParameter("txtidtecnico1"));
            int tipo_sol = Integer.parseInt(request.getParameter("combotiposol"));
            String observacion = request.getParameter("areadiagnostico").replaceAll("\"", "\'");
            Part archivo = request.getPart("adjunto");
            if (!observacion.equals("")) {
                diagnostico_soporte elemento = new diagnostico_soporte(id_solicitud, id_tecnico, observacion, !archivo.getSubmittedFileName().equals("") ? id_solicitud + "_" + archivo.getSubmittedFileName() : "", tipo_sol);
                try {
                    if (enlace.registroDiagnosticoSoporteTecnico(elemento)) {
                        if (archivo.getSubmittedFileName().equals("")) {
                            out.print("ok");
                        } else {
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            archivo.write(PATH + id_solicitud + "_" + archivo.getSubmittedFileName());
                            out.print("ok");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("registrar_diagnostico | " + ex);
                }
            }
        } else if (accion.equalsIgnoreCase("registrar_calificacion")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsolu"));
            int id_usuario = Integer.parseInt(request.getParameter("txtidusuario1"));
            int id_satisfaccion = Integer.parseInt(request.getParameter("combocalificar"));
            String comentario = request.getParameter("areacomentario1").replaceAll("\"", "\'");
            calificacion_soporte elemento = new calificacion_soporte(id_solicitud, id_usuario, id_satisfaccion, comentario);
            try {
                if (enlace.registroCalificacionSoporteTecnico(elemento)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("rechazar_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsolicitudre"));
            int id_tecnico = Integer.parseInt(request.getParameter("txtidtecnicore"));
            String motivo = request.getParameter("arearechazo").replaceAll("\"", "\'");
            rechazo_soporte elemento = new rechazo_soporte(id_solicitud, id_tecnico, motivo);
            try {
                if (enlace.eliminarAsignacionSoporteID(id_solicitud)) {
                    enlace.actualizarEstadoSolicitudID(5, id_solicitud);
                    if (enlace.registroRechazoSoporteTecnico(elemento)) {
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("anular_solicitud")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsolicitudanula"));
            int id_tecnico = Integer.parseInt(request.getParameter("txtidtecnicoanula"));
            String motivo = request.getParameter("areaanula").replaceAll("\"", "\'");
            AnulacionSoporte elemento = new AnulacionSoporte(id_solicitud, id_tecnico, motivo);
            try {
                enlace.actualizarEstadoSolicitudID(69, id_solicitud);
                if (enlace.registroAnulacionSoporte(elemento)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println("registroAnulacionSoporte | " + ex);
            }
        } else if (accion.equalsIgnoreCase("devolver_funcionario")) {
            out = response.getWriter();
            int id_solicitud = Integer.parseInt(request.getParameter("txtidsolicitudanula"));
            String motivo = request.getParameter("areaanula").replaceAll("\"", "\'");
            DevolucionSoporte elemento = new DevolucionSoporte(id_solicitud, motivo);
            try {
                diagnostico_soporte d = enlace.obtenerDiagnosticoSoporteID(id_solicitud);
                if (d.getAdjunto() != null) {
                    new File(PATH + d.getAdjunto()).delete();
                }
                enlace.actualizarEstadoSolicitudID(70, id_solicitud);
                if (enlace.registrarDevolucionFuncionarioSoporte(elemento)) {
                    out.print("ok");
                }
            } catch (Exception ex) {
                System.out.println("registroDevolucionFuncionarioSoporte | " + ex);
            }
        } else if (accion.equalsIgnoreCase("informe")) {            
            int idSol = Integer.parseInt(request.getParameter("sol"));
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/reporte_helpdesk.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_solicitud", idSol);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                ServletOutputStream outRep = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outRep);
                outRep.flush();
                outRep.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        } else if (accion.equalsIgnoreCase("solicitud_referencia")) {
            out = response.getWriter();
            int idSol = Integer.parseInt(request.getParameter("idSol"));
            solicitud_soporte sop = enlace.obtenerSolicitudSoporte(idSol);
            if (sop.getId_solicitud() != 0) {
                if (sop.getEstado() == 3) {
                    out.print("1");
                } else {
                    out.print("0");
                }
            } else {
                out.print("-1");
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
