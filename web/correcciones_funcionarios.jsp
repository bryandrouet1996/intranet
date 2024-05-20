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
    conexion enlace = new conexion();
    double porcentaje_aprobadas = 0;
    double porcentaje_registradas = 0;
    ArrayList<actividad> listadoResumen = null;
    ArrayList<actividad> listadoResumenAprobadas = null;
    ArrayList<actividad> listadoResumenRegistradas = null;
    usuario ele=null;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    try {
        String fecha_ini = request.getParameter("fechi");
        fecha_inicio = Date.valueOf(fecha_ini);
        String fecha_fi = request.getParameter("fechf");
        fecha_fin = Date.valueOf(fecha_fi);
        int id_usuario = Integer.parseInt(request.getParameter("iu"));
        ele=enlace.buscar_usuarioID(id_usuario);
        listadoResumen = enlace.resumenActividadesUsuarioIDRangoFecha(id_usuario, fecha_inicio, fecha_fin);
        listadoResumenRegistradas= enlace.resumenActividadesRegistradasUsuarioIDRangoFecha(id_usuario, fecha_inicio, fecha_fin);
        listadoResumenAprobadas = enlace.resumenActividadesAprobadasUsuarioIDRangoFecha(id_usuario, fecha_inicio, fecha_fin);
        int cantidad_registradas = enlace.cantidadActividadesRegistradasUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin);
        int cantidad_aprobadas = enlace.cantidadActividadesAprobadasUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin);
        float cantidad_total = enlace.cantidadActividadesUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin);
        porcentaje_aprobadas = enlace.porcentajeActividades(cantidad_aprobadas, cantidad_total);
        porcentaje_registradas = enlace.porcentajeActividades(cantidad_registradas, cantidad_total);
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
            <div class="col-7">
                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                    <canvas id="myChart1111" height="200"></canvas>
                </div>
            </div>
            <div class="col-5">
                <div class="card-body">
                    <canvas id="myChart11112"></canvas>
                </div>
            </div>
        </div>
        <script src="assets/modules/chart.min.js"></script>
        <script src="assets/js/page/modules-chartjs.js"></script>
        <script>
            var ctx = document.getElementById("myChart1111").getContext('2d');
            var myChart = new Chart(ctx, {
            type: 'bar',
                    data: {
                    labels: [
            <%for (actividad resumen : listadoResumen) {%>
                    "<%= resumen.getFecha_actividad()%>",
            <%}%>
                    ],
                            datasets: [
                            {
                            label: 'Registradas',
                                    data: [
            <%for (actividad resumen : listadoResumenRegistradas) {%>
                                    "<%= resumen.getId_actividad()%>",
            <%}%>
                                    ],
                                    borderWidth: 2,
                                    borderColor: '#fc544b',
                                    borderWidth: 2.5,
                                    pointBackgroundColor: '#ffffff',
                                    pointRadius: 4
                            },
                            {
                            label: 'Aprobadas',
                                    data: [
            <%for (actividad resumen : listadoResumenAprobadas) {%>
                                    "<%= resumen.getId_actividad()%>",
            <%}%>
                                    ],
                                    borderWidth: 2,
                                    borderColor: '#3abaf4',
                                    borderWidth: 2.5,
                                    pointBackgroundColor: '#ffffff',
                                    pointRadius: 4
                            }
                            ]

                    },
                    options: {
                    legend: {
                    display: true
                    },
                            scales: {
                            yAxes: [{
                            gridLines: {
                            drawBorder: false,
                                    color: '#f2f2f2',
                            },scaleLabel: {
            display: true,
            labelString: 'Cantidad de registros'
          },
                                    ticks: {
                                    beginAtZero: true,
                                            stepSize: 2
                                    }
                            }],
                                    xAxes: [{
                                    ticks: {
                                    display: true
                                    },
                                            gridLines: {
                                            display: true
                                            },scaleLabel: {
            display: true,
            labelString: 'Fecha de registro'
          }
                                    }]
                            },
                    }
            });
            var ctx = document.getElementById("myChart11112").getContext('2d');
            var myChart = new Chart(ctx, {
            type: 'pie',
                    data: {
                    datasets: [{
                    data: [
            <%= porcentaje_registradas %>,
            <%= porcentaje_aprobadas%>
                    ],
                            backgroundColor: [
                                    '#fc544b',
                                    '#3abaf4'
                            ],
                            label: 'Dataset 1'
                    }],
                            labels: [
                                    'Registradas',
                                    'Aprobadas'
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
