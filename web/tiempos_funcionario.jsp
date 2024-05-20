<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.sql.Date"%>
<%@page import="modelo.evidencia_actividad"%>
<%@page import="modelo.comentario_actividad"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.actividad"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    double promedio=0;
    int hora_maxima=0;
    int hora_minima=0;
    conexion enlace=new conexion();
    
    try {
        promedio=Double.parseDouble(request.getParameter("prom"));
        hora_maxima=Integer.parseInt(request.getParameter("max"));
        hora_minima=Integer.parseInt(request.getParameter("min"));
   
    } catch (Exception e) {
        System.out.println(e);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
    </head>
    <body>
        <div class="table-responsive">
            <table class="table table-striped" id="table-4">
                <thead>                                 
                    <tr>
                        <th>Promedio de horas por actividad registrada</th>
                        <th>Hora máxima registrada</th>
                        <th>Hora minima registrada</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><%= enlace.limitarDecimales(promedio)+"h" %></td>
                        <td><%= hora_maxima+"h" %></td>
                        <td><%= hora_minima+"h" %></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </body>
</html>
