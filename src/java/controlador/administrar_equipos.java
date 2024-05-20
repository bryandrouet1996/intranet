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
import modelo.foto_usuario;
import modelo.inventario;
import modelo.usuario;
import static org.apache.commons.fileupload.FileUploadBase.CONTENT_DISPOSITION;
/**
 *
 * @author Usuario
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_equipos", urlPatterns = {"/administrar_equipos.control"})
public class administrar_equipos extends HttpServlet{
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
                String usuariodominios = request.getParameter("txtusuariodominio");
                String tipodispositivo = request.getParameter("combotipo");
                String macaddress = request.getParameter("txtmacaddress");
                String memoria = request.getParameter("txtmemoria");
                String procesador = request.getParameter("txtprocesador");
                String direccion_ip = request.getParameter("txtdireccion_ip");
                String conexion_dhcp = request.getParameter("comboestado");
                String conexion_permanente = request.getParameter("combopermanente");
                String antivirus = request.getParameter("comboantivirus");
                String cabildo = request.getParameter("combocabildo");
                String sigame = request.getParameter("combosigame");
                String office365 = request.getParameter("combooffice");
                String arquitectura_so = request.getParameter("comboarquitectura");
                String codigo_bodega = request.getParameter("txtcodigo_bodega");
                String observaciones = request.getParameter("txtobservaciones");
                String nombre_edificio = request.getParameter("comboedificio");
                String piso = request.getParameter("combopiso");
                String unidad_administrativa = request.getParameter("txtunidad_administrativa");
                String funcionario = request.getParameter("txtfuncionario");
                
                
                inventario elemento = new inventario( nombres, usuariodominios, tipodispositivo, macaddress, memoria, procesador, direccion_ip, conexion_dhcp, conexion_permanente, antivirus, cabildo, sigame, office365, arquitectura_so, codigo_bodega, observaciones, nombre_edificio, piso, unidad_administrativa, funcionario);
                
                try {
                    if (enlace.registroEquipo(elemento))  {
                        out.print("ok");
                    /*if(elemento!=null||elemento.trim().length()>0) {
                    System.out.println("no está vació");
                    }*/
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
     }else if (accion.equalsIgnoreCase("eliminar")) {
                int idInventario = Integer.parseInt(request.getParameter("iu"));
                if (enlace.eliminarEquipo(idInventario)) {
                    out.print("ok");
                } else {

                }
            }else if (accion.equalsIgnoreCase("modificar")) {
                inventario elemento = new inventario();
                int idInventario = Integer.parseInt(request.getParameter("iu"));
                String nombres = request.getParameter("txtnombre");
                String usuariodominios = request.getParameter("txtusuariodominio");
                String tipodispositivo = request.getParameter("combotipo");
                String macaddress = request.getParameter("txtmacaddress");
                String memoria = request.getParameter("txtmemoria");
                String procesador = request.getParameter("txtprocesador");
                String direccion_ip = request.getParameter("txtdireccion_ip");
                String conexion_dhcp = request.getParameter("comboestado");
                String conexion_permanente = request.getParameter("combopermanente");
                String antivirus = request.getParameter("comboantivirus");
                String cabildo = request.getParameter("combocabildo");
                String sigame = request.getParameter("combosigame");
                String office365 = request.getParameter("combooffice365");
                String arquitectura_so = request.getParameter("comboarquitectura");
                String codigo_bodega = request.getParameter("txtcodigo_bodega");
                String observaciones = request.getParameter("txtobservaciones");
                String nombre_edificio = request.getParameter("comboedificio");
                String piso = request.getParameter("combopiso");
                String unidad_administrativa = request.getParameter("txtunidad_administrativa");
                String funcionario = request.getParameter("txtfuncionario");
                elemento.setNombre(nombres);
                elemento.setUsuariodominio(usuariodominios);
                elemento.setTipodispositivo(tipodispositivo);
                elemento.setMacaddress(macaddress);
                elemento.setMemoria(memoria);
                elemento.setProcesador(procesador);
                elemento.setDireccion_ip(direccion_ip);
                elemento.setConexion_dhcp(conexion_dhcp);
                elemento.setConexion_permanente(conexion_permanente);
                elemento.setAntivirus(antivirus);
                elemento.setCabildo(cabildo);
                elemento.setSigame(sigame);
                elemento.setOffice365(office365);
                elemento.setArquitectura_so(arquitectura_so);
                elemento.setCodigo_bodega(codigo_bodega);
                elemento.setObservaciones(observaciones);
                elemento.setNombre_edificio(nombre_edificio);
                elemento.setPiso(piso);
                elemento.setUnidad_administrativa(unidad_administrativa);
                elemento.setFuncionario(funcionario);
                
               if (enlace.actualizarInventario(elemento, idInventario)) {
                    out.print("ok");
                } else {

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
