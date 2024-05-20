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
    ArrayList<comentario_actividad> listadoCorrectivos=null;
    int id_usuario=0;
    java.sql.Date fecha_inicio=null;
    java.sql.Date fecha_fin=null;
    double porcentaje_realizadas=0;
    double porcentaje_norealizadas=0;
    usuario ele=null;
    try {
        String fecha_ini = request.getParameter("fechi");
        fecha_inicio=Date.valueOf(fecha_ini);
        String fecha_fi = request.getParameter("fechf");
        fecha_fin = Date.valueOf(fecha_fi);
        id_usuario=Integer.parseInt(request.getParameter("iu"));
        ele=enlace.buscar_usuarioID(id_usuario);
        listadoCorrectivos = enlace.listadoComentarioActividadRangoFechaID(id_usuario,fecha_inicio,fecha_fin);
        float cantidad_correctivos = enlace.cantidadComentariosRangoIdUsuario(id_usuario, fecha_inicio, fecha_fin);
        int cantidad_realizadas = enlace.cantidadComentariosCorregidosRangoIdUsuario(id_usuario, fecha_inicio, fecha_fin);
        int cantidad_norealizadas = enlace.cantidadComentariosNoCorregidosRangoIdUsuario(id_usuario, fecha_inicio, fecha_fin);;
        porcentaje_realizadas = enlace.porcentajeActividades(cantidad_realizadas, cantidad_correctivos);
        porcentaje_norealizadas = enlace.porcentajeActividades(cantidad_norealizadas, cantidad_correctivos);
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
        <div class="row">
            <div class="col-6">
                <label><%= ele.getApellido() %> <%= ele.getNombre() %></label>
            </div>
            <div class="col-6">
                <label > desde <%= fecha_inicio %> hasta <%= fecha_fin %></label>
            </div>
            <br>
            <div class="col-12">
                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                    <canvas id="myChart1111222" height="50"></canvas>
                </div>
            </div>
        <div class="table-responsive">
            <table class="table table-striped" id="table-4">
                <thead>                                 
                    <tr>
                        <th>Fecha de actividad</th>
                        <th>Descripción de actividad</th>
                        <th>Corrección</th>
                        <th>Estado de correción</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(comentario_actividad busc:listadoCorrectivos){
                        int id_acti = busc.getId_actividad();
                        String estado="";
                        actividad eleme = enlace.BuscarActividadID(id_acti);
                        int estado_comentario=enlace.estadoComentarioActividad(busc.getId_comentario());
                        if(estado_comentario==0){
                            estado="No realizada";
                        }else{
                            estado="Realizada";
                        }
                    %>
                    <tr>
                        <td><%= eleme.getFecha_actividad() %></td>
                        <td><%= eleme.getTarea() %></td>
                        <td><%= busc.getDescripcion() %></td>
                        <td><%= estado %></td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
        </div>
                <script src="assets/modules/chart.min.js"></script>
                <script src="assets/js/page/modules-chartjs.js"></script>
                <script>
                    var ctx = document.getElementById("myChart1111222").getContext('2d');
            var myChart = new Chart(ctx, {
            type: 'pie',
                    data: {
                    datasets: [{
                    data: [
            <%= porcentaje_realizadas %>,
            <%= porcentaje_norealizadas %>
                    ],
                            backgroundColor: [
                                    '#fc544b',
                                    '#3abaf4'
                            ],
                            label: 'Dataset 1'
                    }],
                            labels: [
                                    'Realizadas',
                                    'No realizadas'
                            ],
                    },
                    options: {
                    responsive: true,
                            legend: {
                            position: 'bottom',
                            },
                    }
            });
            </script>
    </body>
</html>
