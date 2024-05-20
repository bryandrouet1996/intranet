/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.actividad_plan;
import modelo.conexion;
import modelo.plan_operativo;

/**
 *
 * @author Kevin Druet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_plan", urlPatterns = {"/administrar_plan.control"})
public class administrar_plan extends HttpServlet {

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
        if (accion.equalsIgnoreCase("registrar_plan")) {
            int id_usuario = Integer.parseInt(request.getParameter("txtiu"));
            String anio = request.getParameter("txtanio");
            double presupuesto = Double.parseDouble(request.getParameter("txtpresupuesto"));
            plan_operativo elemento = new plan_operativo(anio, id_usuario, presupuesto);
            try {
                if(enlace.registroPlanOperativo(elemento)){
                    int ultimo_plan = enlace.idUltimoPlanUsuarioID(id_usuario);
                    out.print(ultimo_plan);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }else if (accion.equalsIgnoreCase("registrar_actividad")) {
            int id_plan = Integer.parseInt(request.getParameter("txtipa"));
            String actividad = request.getParameter("txtactividad");
            int id_tipo = Integer.parseInt(request.getParameter("combotipo"));
            String indicador = request.getParameter("txtindicador");
            String meta = request.getParameter("txtmeta");
            String partida = request.getParameter("txtpartida");
            String descripcion = request.getParameter("txtdescripcion");
            int id_orientador = Integer.parseInt(request.getParameter("comboorientador"));
            double presupuesto = Double.parseDouble(request.getParameter("txtpresupuesto"));
            double presupuesto_actual = Double.parseDouble(request.getParameter("txtpresupuestoac"));
            String programacion ="";
            if (request.getParameter("txtpro1") != null) {
               programacion = programacion + request.getParameter("txtpro1") + ",";
            }
            if (request.getParameter("txtpro2") != null) {
               programacion = programacion + request.getParameter("txtpro2") + ",";
            }
            if (request.getParameter("txtpro3") != null) {
               programacion = programacion + request.getParameter("txtpro3") + ",";
            }
            if (request.getParameter("txtpro4") != null) {
               programacion = programacion + request.getParameter("txtpro4") + ",";
            }
            String meses = "";
            if (request.getParameter("sw1") != null) {
                meses = meses + request.getParameter("sw1") + ",";
            }
            if (request.getParameter("sw2") != null) {
                meses = meses + request.getParameter("sw2") + ",";
            }
            if (request.getParameter("sw3") != null) {
                meses = meses + request.getParameter("sw3") + ",";
            }
            if (request.getParameter("sw4") != null) {
                meses = meses + request.getParameter("sw4") + ",";
            }
            if (request.getParameter("sw5") != null) {
                meses = meses + request.getParameter("sw5") + ",";
            }
            if (request.getParameter("sw6") != null) {
                meses = meses + request.getParameter("sw6") + ",";
            }
            if (request.getParameter("sw7") != null) {
                meses = meses + request.getParameter("sw7") + ",";
            }
            if (request.getParameter("sw8") != null) {
                meses = meses + request.getParameter("sw8") + ",";
            }
            if (request.getParameter("sw9") != null) {
                meses = meses + request.getParameter("sw9") + ",";
            }
            if (request.getParameter("sw10") != null) {
                meses = meses + request.getParameter("sw10") + ",";
            }
            if (request.getParameter("sw11") != null) {
                meses = meses + request.getParameter("sw11") + ",";
            }
            if (request.getParameter("sw12") != null) {
                meses = meses + request.getParameter("sw12") + ",";
            }
            actividad_plan elemento = new actividad_plan(id_plan, actividad, meses, id_tipo, indicador, meta, partida, descripcion, id_orientador, programacion, presupuesto, presupuesto_actual);
            try {
                if(enlace.registroActividadPlanOperativo(elemento)){
                    out.print(id_plan);
                }
            } catch (SQLException ex) {
                System.out.println(ex);
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
