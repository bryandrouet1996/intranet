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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.Capacitacion;
import modelo.Horario;
import modelo.InscritoCap;
import modelo.conexion;
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
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
@WebServlet(name = "administrar_cap", urlPatterns = {"/administrar_cap.control"})
public class administrar_cap extends HttpServlet {

    private static final String PATH = "/newmedia/capacitaciones/";
//    private static final String PATH = "C:/prueba/capacitaciones/";

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
        if (accion.equalsIgnoreCase("registrar")) {
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(request.getParameter("idUsu"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            String hora_ini = request.getParameter("hora_ini");
            String hora_fin = request.getParameter("hora_fin");
            String tema = request.getParameter("tema");
            String enlace = request.getParameter("enlace");
            String desc = request.getParameter("desc");
            Part archivo = request.getPart("archivo");
            Capacitacion c = new Capacitacion();
            c.setIdUsuario(idUsu);
            c.setTema(tema);
            c.setEnlace(enlace);
            c.setDescripcion(desc);
            int idCap = 0;
            try {
                idCap = mysql.registrarCapacitacion(c);
                if (archivo.getSubmittedFileName().equals("")) {
                    out.print("ok");
                } else {
                    File fileDir = new File(PATH);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    archivo.write(PATH + idCap + "_" + archivo.getSubmittedFileName());
                    if (mysql.actualizarAdjuntoCapacitacion(idCap, PATH + idCap + "_" + archivo.getSubmittedFileName())) {
                        mysql.registrarHorarioCapacitacion(idCap, fecha, hora_ini, hora_fin);
                        out.print("ok");
                    }
                }
            } catch (Exception ex) {
                System.out.println("registrarCapacitacion | actualizarAdjuntoCapacitacion | registrarHorarioCapacitacion | " + ex);
                mysql.eliminarCapacitacion(idCap);
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            PrintWriter out = response.getWriter();
            int id_cap = Integer.parseInt(request.getParameter("id_cap"));
            Capacitacion c = mysql.getCapacitacion(id_cap);
            if (mysql.eliminarCapacitacion(id_cap)) {
                try {
                    String adjunto = c.getAdjunto(), informe = c.getInforme();
                    if (adjunto != null) {
                        new File(adjunto).delete();
                    }
                    if (informe != null) {
                        new File(informe).delete();
                    }
                } catch (Exception e) {
                    System.out.println("borrar | " + e);
                }
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("inscribir")) {
            PrintWriter out = response.getWriter();
            int id_hor = Integer.parseInt(request.getParameter("id_hor"));
            int id_usu = Integer.parseInt(request.getParameter("id_usu"));
            String horario = request.getParameter("horario"), tema = request.getParameter("tema");
            try {
                usuario func = mysql.buscar_usuarioID(id_usu);
                mysql.inscribirCapacitacion(id_hor, id_usu);
                mysql.enviarCorreoMod(func.getCorreo(), "REGISTRO EN CAPACITACIÓN: " + tema, "Se efectuó su registro en la capacitación '" + tema + "' en el horario " + horario);
                out.print("ok");
            } catch (Exception e) {
                System.out.println("inscribirCapacitacion | " + e);
            }
        } else if (accion.equalsIgnoreCase("registrar_hor")) {
            PrintWriter out = response.getWriter();
            int idCap = Integer.parseInt(request.getParameter("cap"));
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            String hora_ini = request.getParameter("hora_ini");
            String hora_fin = request.getParameter("hora_fin");
            Horario h = new Horario();
            h.setIdCap(idCap);
            h.setFecha(fecha);
            h.setHoraIni(hora_ini);
            h.setHoraFin(hora_fin);
            try {
                mysql.registrarHorarioCapacitacion(idCap, fecha, hora_ini, hora_fin);
                out.print("ok");
            } catch (Exception ex) {
                System.out.println("registrarHorarioCapacitacion | " + ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_hor")) {
            PrintWriter out = response.getWriter();
            int idHor = Integer.parseInt(request.getParameter("id_hor"));
            if (mysql.eliminarHorarioCapacitacion(idHor)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("calificar")) {
            PrintWriter out = response.getWriter();
            int asis = Integer.parseInt(request.getParameter("asis"));
            int sat = Integer.parseInt(request.getParameter("sat"));
            try {
                mysql.calificarCapacitacion(asis, sat);
                out.print("ok");
            } catch (Exception e) {
                System.out.println("calificarCapacitacion | " + e);
            }
        } else if (accion.equalsIgnoreCase("cambiar_est_cap")) {
            PrintWriter out = response.getWriter();
            int idCap = Integer.parseInt(request.getParameter("id_cap"));
            int estado = Integer.parseInt(request.getParameter("estado"));
            if (mysql.cambiarEstadoCapacitacion(idCap, estado)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("asistencia")) {
            PrintWriter out = response.getWriter();
            int idHor = Integer.parseInt(request.getParameter("hor"));
            ArrayList<InscritoCap> inscritos = mysql.getInscritos(idHor);
            ArrayList<InscritoCap> asistentes = new ArrayList<>();
            try {
                for (InscritoCap i : inscritos) {
                    if (Integer.parseInt(request.getParameter("asi" + i.getId())) == 1) {
                        asistentes.add(i);
                    }
                }
                mysql.registrarAsistencia(idHor, inscritos, asistentes);
                out.print("ok");
            } catch (Exception e) {
                System.out.println("registrarAsistencia | " + e);
            }
        } else if (accion.equalsIgnoreCase("informe")) {
            PrintWriter out = response.getWriter();
            int idCap = Integer.parseInt(request.getParameter("cap"));
            Part archivo = request.getPart("archivo");
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String path = PATH + idCap + "_INFORME_" + archivo.getSubmittedFileName();
            archivo.write(path);
            if (mysql.actualizarInformeCapacitacion(idCap, path)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("certificado")) {
            int idAsis = Integer.parseInt(request.getParameter("asi"));
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/certificado_cap.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_asi", idAsis);
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
