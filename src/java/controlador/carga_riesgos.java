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
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.ConexionRiesgos;
import modelo.FichaRiesgos;
import modelo.PersonaRiesgos;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 500)
@WebServlet(name = "carga_riesgos", urlPatterns = {"/carga_riesgos.control"})
public class carga_riesgos extends HttpServlet {

    private static final String PATH = "/newmedia/riesgos/";//PRODUCCIÓN
//    private static final String PATH = "C:/prueba/riesgos/";//PRUEBA

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
        ConexionRiesgos mysql = new ConexionRiesgos();
        HttpSession sesion = request.getSession();
        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        if (accion.equalsIgnoreCase("cargar")) {
            int idUsu = Integer.parseInt(request.getParameter("idusu"));
            Part archivo = request.getPart("adjunto");
            File fileDir = new File(PATH + idUsu + ".xls");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            archivo.write(PATH + idUsu + ".xls");
            FileInputStream fis = new FileInputStream(new File(PATH + idUsu + ".xls"));
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);
            String codAnterior = "";
            try {
                for (Row row : sheet) {
                    int celda = 1;
                    boolean principal = true;
                    if (row.getCell(0).getStringCellValue().equals("")) {
                        principal = false;
                    } else {
                        codAnterior = row.getCell(0).getStringCellValue();
                    }
                    if (principal) {
                        FichaRiesgos ficha = new FichaRiesgos();
                        PersonaRiesgos per = new PersonaRiesgos();
                        for (Cell cell : row) {
                            String valor = cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? Double.toString(cell.getNumericCellValue()) : cell.getStringCellValue();
                            switch (celda) {
                                case 1:
                                    ficha.setCodigo(valor);
                                    per.setCodFicha(valor);
                                    break;
                                case 2:
                                    ficha.setFecha(valor);
                                    break;
                                case 3:
                                    ficha.setLatitud(valor);
                                    break;
                                case 4:
                                    ficha.setLongitud(valor);
                                    break;
                                case 5:
                                    ficha.setDireccion(valor);
                                    break;
                                case 6:
                                    ficha.setTecnico(valor);
                                    break;
                                case 7:
                                    ficha.setFicha(Integer.parseInt(valor));
                                    break;
                                case 8:
                                    ficha.setHora(valor);
                                    break;
                                case 9:
                                    ficha.setFecha(valor);
                                    break;
                                case 12:
                                    ficha.setParroquia(valor);
                                    break;
                                case 13:
                                    ficha.setLugar(valor);
                                    break;
                                case 14:
                                    ficha.setBarrio(valor);
                                    break;
                                case 15:
                                    ficha.setSector(valor);
                                    break;
                                case 16:
                                    ficha.setZona(valor);
                                    break;
                                case 17:
                                    ficha.setDistrito(valor);
                                    break;
                                case 18:
                                    ficha.setDistancia(Integer.parseInt(valor));
                                    break;
                                case 19:
                                    ficha.setTiempo(valor);
                                    break;
                                case 20:
                                    ficha.setPunRef(valor);
                                    break;
                                case 21:
                                    ficha.setCoorX(valor);
                                    break;
                                case 22:
                                    ficha.setCoorY(valor);
                                    break;
                                case 23:
                                    ficha.setAltitud(valor);
                                    break;
                                case 24:
                                    ficha.setAccesibilidad(valor);
                                    break;
                                case 25:
                                    ficha.setFechaIni(valor);
                                    break;
                                case 26:
                                    ficha.setHoraIni(valor);
                                    break;
                                case 27:
                                    ficha.setDescripcion(valor);
                                    break;
                                case 28:
                                    ficha.setEvento(valor);
                                    break;
                                case 29:
                                    ficha.setEfectos(valor);
                                    break;
                                case 30:
                                    ficha.setSNGRE(valor);
                                    break;
                                case 31:
                                    ficha.setMIES(valor);
                                    break;
                                case 32:
                                    ficha.setMSP(valor);
                                    break;
                                case 33:
                                    ficha.setMINEDUC(valor);
                                    break;
                                case 34:
                                    ficha.setGADMCE(valor);
                                    break;
                                case 35:
                                    ficha.setBomberos(valor);
                                    break;
                                case 36:
                                    ficha.setPN(valor);
                                    break;
                                case 37:
                                    ficha.setCoopInt(valor);
                                    break;
                                case 38:
                                    ficha.setPrefectura(valor);
                                    break;
                                case 39:
                                    ficha.setMAGAP(valor);
                                    break;
                                case 40:
                                    ficha.setTurismo(valor);
                                    break;
                                case 41:
                                    per.setNombre(valor);
                                    break;
                                case 42:
                                    per.setEdad(Integer.parseInt(valor));
                                    break;
                                case 43:
                                    per.setCedula(valor);
                                    break;
                                case 44:
                                    per.setTelefono(valor);
                                    break;
                                case 45:
                                    per.setOcupacion(valor);
                                    break;
                                case 46:
                                    per.setBono(!valor.equals(""));
                                    break;
                                case 47:
                                    per.setDiscapacidad(!valor.equals(""));
                                    break;
                                case 48:
                                    per.setEstudiante(!valor.equals(""));
                                    break;
                                case 49:
                                    ficha.setObservacion(valor);
                                    break;
                                case 50:
                                    ficha.setEfectos(valor);
                                    break;
                                case 51:
                                    ficha.setmVida(valor);
                                    break;
                                case 52:
                                    ficha.setConclusiones(valor);
                                    break;
                                case 53:
                                    ficha.setnViv(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 54:
                                    ficha.setnFam(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 55:
                                    ficha.setnPer(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 56:
                                    ficha.setnEsco(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 57:
                                    ficha.setnAdul(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 58:
                                    ficha.setnDis(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 59:
                                    ficha.setnBono(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 60:
                                    ficha.setnFalle(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 61:
                                    ficha.setnHeri(Integer.parseInt(!valor.equals("") ? valor : "0"));
                                    break;
                                case 62:
                                    ficha.setAcciones(valor);
                                    break;
                                case 63:
                                    ficha.setRecomendaciones(valor);
                                    break;
                                default:
                                    break;
                            }
                            celda++;
                        }
                        mysql.registrarPrincipal(ficha);
                        mysql.registrarCiudadano(per);
                    } else {
                        PersonaRiesgos per = new PersonaRiesgos();
                        per.setCodFicha(codAnterior);
                        for (Cell cell : row) {
                            String valor = cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? Double.toString(cell.getNumericCellValue()) : cell.getStringCellValue();
                            switch (celda) {
                                case 41:
                                    per.setNombre(valor);
                                    break;
                                case 42:
                                    per.setEdad(Integer.parseInt(valor));
                                    break;
                                case 43:
                                    per.setCedula(valor);
                                    break;
                                case 44:
                                    per.setTelefono(valor);
                                    break;
                                case 45:
                                    per.setOcupacion(valor);
                                    break;
                                case 46:
                                    per.setBono(!valor.equals(""));
                                    break;
                                case 47:
                                    per.setDiscapacidad(!valor.equals(""));
                                    break;
                                case 48:
                                    per.setEstudiante(!valor.equals(""));
                                    break;
                                default:
                                    break;
                            }
                            celda++;
                        }
                        mysql.registrarCiudadano(per);
                    }
                }
                sesion.setAttribute("carga", "x");
            } catch (Exception e) {
                System.out.println("Carga: " + e);
                sesion.setAttribute("carga", "error");
            }
            new File(PATH + idUsu + ".xls").delete();
            response.sendRedirect("cargaRiesgos.jsp");
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
