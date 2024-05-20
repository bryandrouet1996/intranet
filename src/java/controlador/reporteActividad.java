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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.conexion_oracle;
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
@WebServlet(name = "reporteActividad", urlPatterns = {"/reporteActividad.control"})
public class reporteActividad extends HttpServlet {

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
        String accion=request.getParameter("accion");
        conexion enlace=new conexion();
        
        if(accion.equalsIgnoreCase("actividades")){
            int id_usuario=Integer.parseInt(request.getParameter("txtidusuario2"));
            usuario funcionario= enlace.buscar_usuarioID(id_usuario);
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtin2"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfi2"));
            conexion_oracle enlace2=new conexion_oracle();
String vacacionestomadas=enlace2.vacacionesTomadas(fecha_inicio,fecha_fin,funcionario.getCodigo_usuario() );
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividades_fun.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_usuario", id_usuario);
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                parametro.put("diastomados", vacacionestomadas);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("general")){
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtin2"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfi2"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_general.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("revisadas")){
            int id_usuario=Integer.parseInt(request.getParameter("txtidusuario1"));
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtin1"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfi1"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividadesTipo.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_usuario", id_usuario);
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                parametro.put("tipo", true);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }if(accion.equalsIgnoreCase("registradas")){
            int id_usuario=Integer.parseInt(request.getParameter("txtidusuario"));
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtin"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfi"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividadesTipo.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_usuario", id_usuario);
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                parametro.put("tipo", false);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("actividad_registro")){
            int id_usuario=Integer.parseInt(request.getParameter("iu"));
            int id_actividad=enlace.ActividadRecienteUsuario(id_usuario);
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividad.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_actividad", id_actividad);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
                } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("actividad")){
            int id_actividad=Integer.parseInt(request.getParameter("iact"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividad.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_actividad", id_actividad);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
                } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("actividades_direccion")){
            int id_usuario=Integer.parseInt(request.getParameter("txtidusuario"));
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("txtin"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("txtfi"));
            usuario elemento=enlace.buscar_usuarioID(id_usuario);
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_direccion.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("codigo_direccion", elemento.getCodigo_unidad());
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
                } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("control_actividades")){
            int id_usuario=Integer.parseInt(request.getParameter("iu"));
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("ini"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("fin"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actividades.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_usuario", id_usuario);
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
            } catch (JRException j) {
                System.out.println("Mensaje de Error:" + j.getMessage());
            }
        }else if(accion.equalsIgnoreCase("actividades_final")){
            int id_usuario=Integer.parseInt(request.getParameter("iu"));
            java.sql.Date fecha_inicio = Date.valueOf(request.getParameter("ini"));
            java.sql.Date fecha_fin = Date.valueOf(request.getParameter("fin"));
            ServletOutputStream out = response.getOutputStream();
            try {
                String jrxmlFileName = "/WEB-INF/classes/reporte/informe_actifinal.jrxml";
                File archivoReporte = new File(request.getRealPath(jrxmlFileName));
                if (archivoReporte.getPath() == null) {
                    System.out.println("No encuentro el archivo del reporte.");
                    System.exit(2);
                }
                Map parametro = new HashMap();
                parametro.put("id_usuario", id_usuario);
                parametro.put("fecha_inicio", fecha_inicio);
                parametro.put("fecha_fin", fecha_fin);
                JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.flush();
                out.close();
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
