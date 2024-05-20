<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.compromiso_acta"%>
<%@page import="modelo.verificable_compromiso"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion enlace = new conexion();
    int id_compromiso = 0;
    ArrayList<verificable_compromiso> listadoVerificables = null;
    try {
        id_compromiso = Integer.parseInt(request.getParameter("id_compromiso"));
        listadoVerificables = enlace.listadoVerificablesCompromiso(id_compromiso);
    } catch (Exception e) {
        System.out.println(e);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
    </head>
    <body>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>                                 
                    <tr>
                        <th>Fecha de registro</th>
                        <th>Nombre</th>
                        <th>Acción</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if(!listadoVerificables.isEmpty()){
                        for (verificable_compromiso buscar : listadoVerificables) {
                            compromiso_acta compromiso = enlace.obtenerCompromisoID(buscar.getId_compromiso());
                    %>
                    <tr>
                        <td><%= buscar.getFecha_registro() %></td>
                        <td><%= buscar.getNombre() %></td>
                        <td>
                            <%if(compromiso.getEstado()==0){%>
                            <a href="administrar_acta.control?accion=descargar_verificable&id_veri=<%= buscar.getId_verificable() %>" title="Descargar verificable" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Descargar" class="fas fa-cloud-download-alt"></i> Descargar</a>
                            <a href="javascript:" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Quitar" class="fas fa-times"></i> Quitar</a>
                            <%}else{%>
                            <a href="administrar_acta.control?accion=descargar_verificable&id_veri=<%= buscar.getId_verificable() %>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Descargar" class="fas fa-cloud-download-alt"></i> Descargar</a>
                            <%}%>
                        </td>
                        <td></td>
                        <td></td>
                    </tr>
                    <%}}%>
                </tbody>
            </table>
        </div>
    </body>
</html>
