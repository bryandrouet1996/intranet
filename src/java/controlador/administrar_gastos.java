/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.AprobacionGastosPersonales;
import modelo.GastosPersonales;
import modelo.RechazoGastosPersonales;
import modelo.ValidacionGastosPersonales;
import modelo.conexion;
import modelo.usuario;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_gastos", urlPatterns = {"/administrar_gastos.control"})

public class administrar_gastos extends HttpServlet {

    private static final String PATH = "/newmedia/gastos_personales/";
//    private static final String PATH = "C:/prueba/gastos_personales/";
    private static final double CANASTA_BASICA = 763.44, CB7 = CANASTA_BASICA * 7;
    private static final int FRACCION_BASICA = 11722;

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
        if (accion.equalsIgnoreCase("registro_formulario")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString());
            double c105 = 0, c112 = 0, c113, c103 = Double.parseDouble(request.getParameter("c103")),
                    c104 = Double.parseDouble(request.getParameter("c104")),
                    c106 = Double.parseDouble(request.getParameter("c106")),
                    c107 = Double.parseDouble(request.getParameter("c107")),
                    c108 = Double.parseDouble(request.getParameter("c108")),
                    c109 = Double.parseDouble(request.getParameter("c109")),
                    c110 = Double.parseDouble(request.getParameter("c110")),
                    c111 = Double.parseDouble(request.getParameter("c111"));
            if (c103 < c104) {
                out.print("-1");
                return;
            }
            c105 = c103 + c104;
            c112 = c106 + c107 + c108 + c109 + c110 + c111;
            if (c112 > CB7) {
                out.print("-2");
                return;
            }
            double L = c112 < CB7 ? c112 : CB7;
            if (c105 <= 2.13 * FRACCION_BASICA) {
                c113 = L * .2;
            } else {
                c113 = L * .1;
            }
            GastosPersonales g = new GastosPersonales(
                    idUsu,
                    c103,
                    c104,
                    c105,
                    c106,
                    c107,
                    c108,
                    c109,
                    c110,
                    c111,
                    c112,
                    c113
            );
            try {
                int idGasto = enlace.registrarGastoPersonal(g);
                if (c104 != 0) {
                    Part part = request.getPart("adjunto");
                    if (!part.getSubmittedFileName().equals("")) {
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        String path = PATH + idGasto + "_" + part.getSubmittedFileName();
                        part.write(path);
                        g.setId(idGasto);
                        g.setAdjunto(path);
                        enlace.ActualizarGastoPersonal(g);
                        out.print("1");
                    }
                } else {
                    out.print("1");
                }
            } catch (SQLException ex) {
                System.out.println("registrarGastoPersonal | ActualizarGastoPersonal | " + ex);
                out.print("0");
            }
        } else if (accion.equalsIgnoreCase("generar_excel")) {
            HttpSession sesion = request.getSession();
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString()),
                    idGasto = Integer.parseInt(request.getParameter("id_gasto"));
            GastosPersonales g = enlace.GetGastoPersonal(idGasto);
            final String originalPath = PATH + "SRI-GP_2023.xls", newPath = PATH + "SRI-GP_" + idGasto + "_" + g.getUsuario().getCedula() + ".xls";
            File original = new File(originalPath), newFile = new File(newPath);
            FileUtils.copyFile(original, newFile);
            FileInputStream fis = new FileInputStream(newFile);
            try {
                HSSFWorkbook wb = new HSSFWorkbook(fis);
                HSSFSheet sheet = wb.getSheetAt(0);
                LocalDateTime hoy = LocalDateTime.now();
                int year = hoy.getYear(), mes = hoy.getMonthValue(), dia = hoy.getDayOfMonth();
                Row row = sheet.getRow(7);
                Cell cell = row.getCell(26);
                cell.setCellValue(Integer.parseInt(Integer.toString(year).substring(0, 1)));
                cell = row.getCell(27);
                cell.setCellValue(Integer.parseInt(Integer.toString(year).substring(1, 2)));
                cell = row.getCell(28);
                cell.setCellValue(Integer.parseInt(Integer.toString(year).substring(2, 3)));
                cell = row.getCell(29);
                cell.setCellValue(Integer.parseInt(Integer.toString(year).substring(3, 4)));
                cell = row.getCell(30);
                cell.setCellValue(mes > 9 ? 1 : 0);
                cell = row.getCell(31);
                cell.setCellValue(mes > 9 ? Integer.parseInt(Integer.toString(mes).substring(1, 2)) : Integer.parseInt(Integer.toString(mes).substring(0, 1)));
                cell = row.getCell(32);
                cell.setCellValue(dia > 9 ? Integer.parseInt(Integer.toString(dia).substring(0, 1)) : 0);
                cell = row.getCell(33);
                cell.setCellValue(dia > 9 ? Integer.parseInt(Integer.toString(dia).substring(1, 2)) : Integer.parseInt(Integer.toString(dia).substring(0, 1)));
                row = sheet.getRow(11);
                cell = row.getCell(2);
                cell.setCellValue(g.getUsuario().getCedula());
                cell = row.getCell(16);
                cell.setCellValue(g.getUsuario().getNombre());
                row = sheet.getRow(13);
                cell = row.getCell(24);
                cell.setCellValue(g.getC103());
                row = sheet.getRow(14);
                cell = row.getCell(24);
                cell.setCellValue(g.getC104());
                row = sheet.getRow(15);
                cell = row.getCell(24);
                cell.setCellValue(g.getC105());
                row = sheet.getRow(17);
                cell = row.getCell(24);
                cell.setCellValue(g.getC106());
                row = sheet.getRow(18);
                cell = row.getCell(24);
                cell.setCellValue(g.getC107());
                row = sheet.getRow(19);
                cell = row.getCell(24);
                cell.setCellValue(g.getC108());
                row = sheet.getRow(20);
                cell = row.getCell(24);
                cell.setCellValue(g.getC109());
                row = sheet.getRow(21);
                cell = row.getCell(24);
                cell.setCellValue(g.getC110());
                row = sheet.getRow(22);
                cell = row.getCell(24);
                cell.setCellValue(g.getC111());
                row = sheet.getRow(23);
                cell = row.getCell(24);
                cell.setCellValue(g.getC112());
                row = sheet.getRow(24);
                cell = row.getCell(24);
                cell.setCellValue(g.getC113());
                fis.close();
                FileOutputStream os = new FileOutputStream(newFile);
                wb.write(os);
                wb.close();
                os.close();
                fis = new FileInputStream(newFile);
                int tamano = fis.available();
                byte[] bytes = new byte[tamano];
                int read = 0;
                String fileName = newFile.getName();
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                ServletOutputStream arch = response.getOutputStream();
                while ((read = fis.read(bytes)) != -1) {
                    arch.write(bytes, 0, read);
                }
                arch.flush();
                arch.close();
            } catch (Exception e) {
                System.out.println("Exception | " + e);
            }
        } else if (accion.equalsIgnoreCase("actualizar_formulario")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idGasto = Integer.parseInt(request.getParameter("uid"));
            double c105 = 0, c112 = 0, c113, c103 = Double.parseDouble(request.getParameter("uc103")),
                    c104 = Double.parseDouble(request.getParameter("uc104")),
                    c106 = Double.parseDouble(request.getParameter("uc106")),
                    c107 = Double.parseDouble(request.getParameter("uc107")),
                    c108 = Double.parseDouble(request.getParameter("uc108")),
                    c109 = Double.parseDouble(request.getParameter("uc109")),
                    c110 = Double.parseDouble(request.getParameter("uc110")),
                    c111 = Double.parseDouble(request.getParameter("uc111"));
            GastosPersonales g = enlace.GetGastoPersonal(idGasto);
            if (g.getC105() == c105) {
                out.print("-1");
                return;
            }
            if (c103 < c104) {
                out.print("-2");
                return;
            }
            c105 = c103 + c104;
            c112 = c106 + c107 + c108 + c109 + c110 + c111;
            if (c112 > CB7) {
                out.print("-3");
                return;
            }
            double L = c112 < CB7 ? c112 : CB7;
            if (c105 <= 2.13 * FRACCION_BASICA) {
                c113 = L * .2;
            } else {
                c113 = L * .1;
            }
            g.setC103(c103);
            g.setC104(c104);
            g.setC105(c105);
            g.setC106(c106);
            g.setC107(c107);
            g.setC108(c108);
            g.setC109(c109);
            g.setC110(c110);
            g.setC111(c111);
            g.setC112(c112);
            g.setC113(c113);
            try {
                if (g.getAdjunto() != null) {
                    new File(g.getAdjunto()).delete();
                }
                if (c104 != 0) {
                    Part part = request.getPart("uadjunto");
                    if (!part.getSubmittedFileName().equals("")) {
                        File fileDir = new File(PATH);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        String path = PATH + idGasto + "_" + part.getSubmittedFileName();
                        part.write(path);
                        g.setAdjunto(path);
                        enlace.ActualizarGastoPersonal(g);
                        out.print("1");
                    }
                } else {
                    enlace.ActualizarGastoPersonal(g);
                    out.print("1");
                }
            } catch (SQLException ex) {
                System.out.println("ActualizarGastoPersonal | " + ex);
                out.print("0");
            }
        } else if (accion.equalsIgnoreCase("eliminar_formulario")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idGasto = Integer.parseInt(request.getParameter("id_gasto"));
            GastosPersonales g = enlace.GetGastoPersonal(idGasto);
            try {
                if (g.getEstado() == 0) {
                    if (g.getAdjunto() != null) {
                        new File(g.getAdjunto()).delete();
                    }
                    enlace.EliminarGastoPersonal(g);
                    out.print("1");
                } else {
                    out.print("-1");
                }
            } catch (SQLException ex) {
                System.out.println("EliminarGastoPersonal | " + ex);
                out.print("0");
            }
        } else if (accion.equalsIgnoreCase("aprobar")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString()),
                    idGasto = Integer.parseInt(request.getParameter("id_gasto"));
            try {
                if (!(enlace.verificarUsuarioCumpleRol(idUsu, "administrador") || enlace.verificarUsuarioCumpleRol(idUsu, "aprueba_gastos"))) {
                    throw new Exception("Rol no habilitado");
                }
                GastosPersonales g = enlace.GetGastoPersonal(idGasto);
                if (g.getEstado() == 0) {
                    AprobacionGastosPersonales a = new AprobacionGastosPersonales();
                    a.setIdGasto(idGasto);
                    usuario u = new usuario();
                    u.setId_usuario(idUsu);
                    a.setAprueba(u);
                    enlace.aprobarGastoPersonal(a);
                    out.print("1");
                } else {
                    out.print("-1");
                }
            } catch (Exception e) {
                System.out.println("aprobarGastoPersonal | " + e);
                out.print("0");
            }
        } else if (accion.equalsIgnoreCase("validar")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString()),
                    idGasto = Integer.parseInt(request.getParameter("id_gasto"));
            try {
                if (!(enlace.verificarUsuarioCumpleRol(idUsu, "administrador") || enlace.verificarUsuarioCumpleRol(idUsu, "valida_gastos"))) {
                    throw new Exception("Rol no habilitado");
                }
                GastosPersonales g = enlace.GetGastoPersonal(idGasto);
                if (g.getEstado() == 1) {
                    ValidacionGastosPersonales v = new ValidacionGastosPersonales();
                    v.setIdGasto(idGasto);
                    usuario u = new usuario();
                    u.setId_usuario(idUsu);
                    v.setValida(u);
                    enlace.validarGastoPersonal(v);
                    out.print("1");
                } else {
                    out.print("-1");
                }
            } catch (Exception e) {
                System.out.println("validarGastoPersonal | " + e);
                out.print("0");
            }
        } else if (accion.equalsIgnoreCase("rechazar")) {
            HttpSession sesion = request.getSession();
            PrintWriter out = response.getWriter();
            int idUsu = Integer.parseInt(sesion.getAttribute("user").toString()),
                    idGasto = Integer.parseInt(request.getParameter("id"));
            String motivo = request.getParameter("motivo");
            try {
                if (!(enlace.verificarUsuarioCumpleRol(idUsu, "administrador") || enlace.verificarUsuarioCumpleRol(idUsu, "aprueba_gastos") || enlace.verificarUsuarioCumpleRol(idUsu, "valida_gastos"))) {
                    throw new Exception("Rol no habilitado");
                }
                GastosPersonales g = enlace.GetGastoPersonal(idGasto);
                if (g.getEstado() < 2) {
                    RechazoGastosPersonales r = new RechazoGastosPersonales();
                    r.setIdGasto(idGasto);
                    usuario u = new usuario();
                    u.setId_usuario(idUsu);
                    r.setRechaza(u);
                    r.setMotivo(motivo);
                    enlace.rechazarGastoPersonal(r);
                    out.print("1");
                } else {
                    out.print("-1");
                }
            } catch (Exception e) {
                System.out.println("rechazarGastoPersonal | " + e);
                out.print("0");
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
