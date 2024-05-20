<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.evidencia_actividad"%>
<%@page import="modelo.comentario_actividad"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.actividad"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion enlace=new conexion();
    int id_actividad=0;
    ArrayList<evidencia_actividad> listadoAdjuntos=null;
 try {
        id_actividad=Integer.parseInt(request.getParameter("id_actividad"));
        listadoAdjuntos=enlace.listadoEvidenciaActividad(id_actividad);
    } catch (Exception e) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
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
            <table class="table table-striped" id="table-4">
                <thead>                                 
                    <tr>
                        <th>Nombre</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        for(evidencia_actividad buscar:listadoAdjuntos){
                    %>
                    <tr>
                        <td><%= buscar.getNombre() %></td>
                        <td>
                            <a href="adjunto.control?id_adjunto=<%= buscar.getId_evidencia() %>" class="btn btn-primary btn-sm" title="Descargar"><i class="fas fa-cloud-download-alt"></i></a>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
    </body>
</html>
