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
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.MySqlGaceta;
import modelo.Notificacion;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
@WebServlet(name = "GacetaTributaria", urlPatterns = {"/GacetaTributaria.control"})
public class GacetaTributaria extends HttpServlet {

    private static final String PATH = "/newmedia/gaceta_tributaria/";//PRODUCCIÓN
//    private static final String PATH = "C:/prueba/gaceta_tributaria/";//PRUEBA

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
        MySqlGaceta mysql = new MySqlGaceta();
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("cargar")) {
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString());
            Part archivo = request.getPart("adjunto");
            final String path = PATH + idUsu + ".xls";
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            archivo.write(path);
            FileInputStream fis = new FileInputStream(new File(path));
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);
            int i = 1;
            try {
                mysql.setAutoCommit(false);
                for (Row row : sheet) {
                    final Cell c1 = row.getCell(0),
                            c2 = row.getCell(1),
                            c3 = row.getCell(2),
                            c4 = row.getCell(3),
                            c5 = row.getCell(4),
                            c6 = row.getCell(5),
                            c7 = row.getCell(6),
                            c8 = row.getCell(7),
                            c9 = row.getCell(8);
                    final String vc1 = c1.getStringCellValue(),
                            vc2 = c2.getStringCellValue(),
                            vc4 = c4.getStringCellValue(),
                            vc5 = c5.getStringCellValue(),
                            vc6 = c6.getStringCellValue(),
                            vc7 = c7.getStringCellValue(),
                            vc9 = c9.getDateCellValue() != null ? new SimpleDateFormat("yyyy-MM-dd").format(c9.getDateCellValue()) : "";
                    if (vc1.equals("")) {
                        break;
                    }
                    final int vc3 = (int) c3.getNumericCellValue();
                    final double vc8 = c8.getNumericCellValue();
                    Notificacion n = new Notificacion();
                    n.setTipoDocumento(vc1);
                    n.setNumeroDocumento(vc2);
                    n.setCiu(vc3);
                    n.setIdentificacion(vc4);
                    n.setRazonSocial(vc5);
                    n.setIdentificacionRepresentante(vc6);
                    n.setNombreRepresentante(vc7);
                    n.setValor((float) vc8);
                    n.setFecha(vc9);
                    mysql.registrarNotificacion(n);
                    i++;
                }
                mysql.confirmCommit();
                sesion.setAttribute("carga_gaceta", "x");
                new File(path).delete();
            } catch (Exception e) {
                mysql.rollback();
                System.out.println("Carga " + i + ": " + e);
                sesion.setAttribute("carga_gaceta", "error");
            }
            response.sendRedirect("carga_gaceta.jsp");
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
