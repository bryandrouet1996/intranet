/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_sectorial;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author USUARIO
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_ficha", urlPatterns = {"/administrar_ficha.control"})
public class administrar_ficha extends HttpServlet {

    private static final String PATH = "/newmedia/control_sectorial/";
//    private static final String PATH = "C:/prueba/control_sectorial/";
    private static final String JRXML_DOCUMENTO = "/WEB-INF/classes/reporte/Ficha.jrxml";

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
        final String accion = request.getParameter("accion");
        PG pg = new PG();
        final conexion mysql = new conexion();
        final HttpSession sesion = request.getSession();
        if (accion.equalsIgnoreCase("registrar")) {
            pg = new PG();
            final PrintWriter out = response.getWriter();
            try {
                final int id_barrio = Integer.parseInt(request.getParameter("id_barrio")),
                        id_creador = Integer.parseInt(sesion.getAttribute("user").toString());
                final usuario u = mysql.buscar_usuarioID(id_creador);
                final boolean visita_alcalde = Integer.parseInt(request.getParameter("visita_alcalde")) != 0,
                        brigada_medica = Integer.parseInt(request.getParameter("brigada_medica")) != 0,
                        brigada_veterinaria = Integer.parseInt(request.getParameter("brigada_veterinaria")) != 0,
                        olla_solidaria = Integer.parseInt(request.getParameter("olla_solidaria")) != 0,
                        minga_ciudadana = Integer.parseInt(request.getParameter("minga_ciudadana")) != 0,
                        capacitacion = Integer.parseInt(request.getParameter("capacitacion")) != 0,
                        wifi = Integer.parseInt(request.getParameter("wifi")) != 0;
                final Ficha f = new Ficha();
                f.setId_barrio(id_barrio);
                f.setId_creador(id_creador);
                f.setNombre_creador(u.getApellido().toUpperCase() + " " + u.getNombre().toUpperCase());
                f.setVisita_alcalde(visita_alcalde);
                f.setBrigada_medica(brigada_medica);
                f.setBrigada_veterinaria(brigada_veterinaria);
                f.setOlla_solidaria(olla_solidaria);
                f.setMinga_ciudadana(minga_ciudadana);
                f.setCapacitacion(capacitacion);
                f.setWifi(wifi);
                f.setId(pg.registrarFicha(f));
                PG.cerrarConexion();
                out.print("1");
            } catch (Exception ex) {
                System.out.println("registrar | " + ex);
                out.print(ex.getMessage());
            }
        } else if (accion.equalsIgnoreCase("buscar_ficha")) {
            pg = new PG();
            final PrintWriter out = response.getWriter();
            final int id_barrio = Integer.parseInt(request.getParameter("id_barrio"));
            final Ficha f = pg.getFicha(id_barrio);
            PG.cerrarConexion();
            out.print(f.stringify());
        } else if (accion.equalsIgnoreCase("reporte_ficha")) {            
            final ServletOutputStream out = response.getOutputStream();
            try {
                final int id_ficha = Integer.parseInt(request.getParameter("id_ficha"));
                final Ficha f = pg.getFichaHistorial(id_ficha);
                if (f.getId() == 0) {
                    throw new Exception("No existe la ficha que se trata de visualizar");
                }
                try {
                    File archivoReporte = new File(request.getRealPath(JRXML_DOCUMENTO));
                    if (archivoReporte.getPath() == null) {
                        System.out.println("No encuentro el archivo del reporte.");
                        System.exit(2);
                    }
                    Map parametro = new HashMap();
                    parametro.put("id", id_ficha);
                    parametro.put("conexion_mysql", mysql.getEnlace());
                    JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, pg.getEnlace());
                    PG.cerrarConexion();
                    response.setContentType("application/pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                    out.flush();
                    out.close();
                } catch (JRException j) {
                    System.out.println("Mensaje de Error:" + j.getMessage());
                    throw new Exception("Error al generar el reporte");
                }
            } catch (Exception ex) {
                System.out.println("reporte_ficha | " + ex);
                out.print(ex.getMessage());
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
