/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileOutputStream;
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
import modelo.UsuarioConfig;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
@WebServlet(name = "administrar_excel", urlPatterns = {"/administrar_excel.control"})
public class administrar_excel extends HttpServlet {

//    private static final String PATH = "/newmedia/excel/";
    private static final String PATH = "C:/prueba/excel/";

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
        File fileDir = new File(PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (accion.equalsIgnoreCase("crear")) {
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(request.getParameter("idUsu"));
            try {
                usuario usu = mysql.buscar_usuarioID(idUsu);
                if (usu.getId_usuario() != 0) {
                    String path = PATH + idUsu + ".xls";
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Datos");
                    HSSFRow rowhead = sheet.createRow((short) 0);
                    rowhead.createCell(0).setCellValue("ID");
                    rowhead.createCell(1).setCellValue("Código");
                    rowhead.createCell(2).setCellValue("Cédula");
                    rowhead.createCell(3).setCellValue("Nombres");
                    int i = 1;
                    for (UsuarioConfig u : mysql.getUsers()) {
                        rowhead = sheet.createRow((short) i);
                        rowhead.createCell(0).setCellValue(u.getId());
                        rowhead.createCell(1).setCellValue(u.getCodigo());
                        rowhead.createCell(2).setCellValue(u.getCedula());
                        rowhead.createCell(3).setCellValue(u.getNombres());
                        if (i == 3) {
                            break;
                        }
                        i++;
                    }
                    FileOutputStream fileOut = new FileOutputStream(path);
                    workbook.write(fileOut);
                    fileOut.close();
                    workbook.close();
                    out.print(path);
                }
            } catch (Exception ex) {
                System.out.println("buscar_usuarioID | creación Excel | " + ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar")) {
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(request.getParameter("idUsu"));
            String path = PATH + idUsu + ".xls";
            try {
                new File(path).delete();
                out.print("ok");
            } catch (Exception e) {
                System.out.println("eliminar | " + e);
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
