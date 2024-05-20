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
    conexion enlace=new conexion();
    ArrayList<usuario> listadoUsuarios=null;
    int cantidad=0;
    int tipo=0;
    java.sql.Date fecha_inicio=null;
    java.sql.Date fecha_fin=null;
    try {
        String fecha_ini = request.getParameter("fechi");
        fecha_inicio=Date.valueOf(fecha_ini);
        String fecha_fi = request.getParameter("fechf");
        fecha_fin = Date.valueOf(fecha_fi);
        String codigo_funcion= request.getParameter("cf");
        tipo=Integer.parseInt(request.getParameter("tipo"));
        if(tipo==1){
            listadoUsuarios=enlace.listadoUsuarioActividadesAprobadasRangoDireccion(codigo_funcion, fecha_inicio, fecha_fin);
        }else if(tipo==0){
            listadoUsuarios=enlace.listadoUsuarioActividadesRegistradasRangoDireccion(codigo_funcion, fecha_inicio, fecha_fin);
        }    
    } catch (Exception e) {
        System.out.println(e);
        request.getRequestDispatcher("index.jsp").forward(request, response);
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
                        <th>Funcionario</th>
                        <th>Número de registros</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(usuario busc:listadoUsuarios){
                        int id_usuario = busc.getId_usuario();
                         usuario elemento = enlace.buscar_usuarioID(id_usuario);
                         String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                         if(tipo==0){
                            cantidad=enlace.numeroActividadesEstadoRegistradasRangoFecha(id_usuario,fecha_inicio,fecha_fin);
                         }else if(tipo==1){
                            cantidad=enlace.numeroActividadesEstadoAprobadasRangoFecha(id_usuario,fecha_inicio,fecha_fin);  
                         }
                    %>
                    <tr>
                        <td><%= nombre_completo %></td>
                        <td><%= cantidad %></td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
    </body>
</html>
