/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.firma_viatico;
import modelo.participacion_viatico;
import modelo.ruta_viatico;
import modelo.viatico;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administar_viatico", urlPatterns = {"/administrar_viatico.control"})
public class administrar_viatico extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        conexion enlace = new conexion();
        String accion = request.getParameter("accion");

        if (accion.equalsIgnoreCase("viatico")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtiusu"));
            int id_viatico = Integer.parseInt(request.getParameter("txtidviatico"));
            int tipo_viatico = Integer.parseInt(request.getParameter("combotipoviatico"));
            String descripcion = request.getParameter("areadescripcion");
            String tipo_cuenta = request.getParameter("combotipocuenta");
            String numero_cuenta = request.getParameter("txtnumerocuenta");
            String nombre_banco = request.getParameter("txtnombrebanco");
            int id_responsable = Integer.parseInt(request.getParameter("comboresponsable"));
            String str[] = request.getParameterValues("comboasistentes");
            int id_autoridad = Integer.parseInt(request.getParameter("comboautoridad"));
            viatico elemento = new viatico(tipo_viatico, id_usuario, descripcion, tipo_cuenta, numero_cuenta, nombre_banco);
            try {
                if (id_viatico == 0) {
                    if (enlace.registroViatico(elemento)) {
                        int id_viati = enlace.idMaxViatico();
                        out.print(id_viati);
                        firma_viatico eleme = new firma_viatico(id_viati, id_responsable, id_autoridad);
                        try {
                            if (enlace.registroFirmaViatico(eleme)) {
                                for (int paso = 0; paso < str.length; paso++) {
                                    int id_asistente = Integer.parseInt(str[paso]);
                                    participacion_viatico element = new participacion_viatico(id_viati, id_asistente);
                                    if (enlace.registroParticipanteViatico(element)) {
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    }
                } else {
                    if (enlace.actualizarViatico(elemento, id_viatico)) {
                        int id_viati = enlace.idMaxViatico();
                        out.print(id_viati);
                        firma_viatico eleme = new firma_viatico(id_viati, id_responsable, id_autoridad);
                        try {
                            if (enlace.ActualizarFirmaViatico(id_viatico, eleme)) {
                                enlace.eliminarParticipacionViaticoID(id_viatico);
                                for (int paso = 0; paso < str.length; paso++) {
                                    int id_asistente = Integer.parseInt(str[paso]);
                                    participacion_viatico element = new participacion_viatico(id_viati, id_asistente);
                                    if (enlace.registroParticipanteViatico(element)) {
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("registro_transporte")) {
            String viatico = request.getParameter("txtviatico");
            int id_viatico = Integer.parseInt(viatico);
            String tipo_transporte = request.getParameter("combotipotransporte");
            String nombre_transporte = request.getParameter("txtnombretransporte");
            String partida = request.getParameter("combolugarpartida");
            int lugar_partida = Integer.parseInt(partida);
            java.sql.Date fecha_salida = Date.valueOf(request.getParameter("txtfechasalida"));
            String hora_salida = request.getParameter("txthorasalida");
            java.sql.Date fecha_llegada = Date.valueOf(request.getParameter("txtfechallegada"));
            String hora_llegada = request.getParameter("txthorallegada");
            String destino = request.getParameter("combolugardestino");
            int lugar_destino = Integer.parseInt(destino);
            ruta_viatico elemento = new ruta_viatico(id_viatico, lugar_partida, lugar_destino, tipo_transporte, nombre_transporte, fecha_salida, hora_salida, fecha_llegada, hora_llegada);
            try {
                if (enlace.registroRutaViatico(elemento)) {
                    out.print("registro_viatico.jsp?iv=" + id_viatico);
                    if (enlace.rutasCompletasViaticos(id_viatico)) {
                        out.print("2");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_ruta")) {
            int idRuta = Integer.parseInt(request.getParameter("iu"));
            if (enlace.eliminarRuta(idRuta)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("eliminar_viatico")) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (enlace.eliminarViatico(id)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("rechazar_viatico")) {
            int id = Integer.parseInt(request.getParameter("idmrrec")),
                    idUsu = Integer.parseInt(request.getParameter("usumrrec"));
            String motivo = request.getParameter("motivomrrec");
            if (enlace.rechazarViatico(id, idUsu, motivo)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("revisar_viatico")) {
            int id = Integer.parseInt(request.getParameter("idmrr")),
                    idUsu = Integer.parseInt(request.getParameter("usumrr"));
            String motivo = request.getParameter("motivomrr");
            if (enlace.revisarViatico(id, idUsu, motivo)) {
                out.print("ok");
            }
        } else if (accion.equalsIgnoreCase("aprobar_viatico")) {
            int id = Integer.parseInt(request.getParameter("idmra")),
                    idUsu = Integer.parseInt(request.getParameter("usumra"));
            String motivo = request.getParameter("motivomra");
            if (enlace.aprobarViatico(id, idUsu, motivo)) {
                out.print("ok");
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
