<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.estado_usuario"%>
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
    ArrayList<estado_usuario> listadoResumen = null;
    estado_usuario ultimo=null;
    estado_usuario primero=null;
    usuario ele=null;
    double desviacion_estandar=0;
    double promedio=0;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    try {
        int id_estado_fin=0;
        int id_estado_inicio=0;
        String fecha_ini = request.getParameter("fechi");
        fecha_inicio = Date.valueOf(fecha_ini);
        String fecha_fi = request.getParameter("fechf");
        fecha_fin = Date.valueOf(fecha_fi);
        int id_usuario = Integer.parseInt(request.getParameter("iu"));
        ele=enlace.buscar_usuarioID(id_usuario);
        listadoResumen = enlace.resumenAccesosIntranetUsuarioIDRangoFecha(id_usuario, fecha_inicio, fecha_fin);
        id_estado_fin=enlace.idUltimoAccesoUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin);
        id_estado_inicio=enlace.idPrimerAccesoUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin);
        ultimo=enlace.buscarEstadoUsuarioID(id_estado_fin);
        primero=enlace.buscarEstadoUsuarioID(id_estado_inicio);
        desviacion_estandar=enlace.desviacionEstandarAccesosUsuario(listadoResumen);
        promedio=enlace.promedioAccesosUsuario(listadoResumen);
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
                    <canvas id="myChart111122" height="100"></canvas>
                </div>
            </div>
            <br>
            <%if(primero.getFecha_acceso()!=null){%>
            <div class="col-6">
                <label><b>Primer acceso: <%= primero.getFecha_acceso() %> <%= primero.getHora_acceso() %></b></label>
            </div>
            <%}else{%>
            <%}%>
            <%if(ultimo.getFecha_acceso()!=null){%>
            <div class="col-6">
                <label><b>Último acceso: <%= ultimo.getFecha_acceso() %> <%= ultimo.getHora_acceso() %></b></label>
            </div>
            <%}else{%>
            <%}%>
            <div class="col-6">
                <label><b>Promedio de accesos: <%= enlace.limitarDecimales(promedio) %></b></label>
            </div>
            <div class="col-6">
                <label><b>Desviación estandar de accesos: <%= enlace.limitarDecimales(desviacion_estandar) %></b></label>
            </div>
        </div>
        <script src="assets/modules/chart.min.js"></script>
        <script src="assets/js/page/modules-chartjs.js"></script>
        <script>
            var ctx = document.getElementById("myChart111122").getContext('2d');
            var myChart = new Chart(ctx, {
            type: 'bar',
                    data: {
                    labels: [
            <%for (estado_usuario resumen : listadoResumen) {%>
                    "<%= resumen.getFecha_acceso()%>",
            <%}%>
                    ],
                            datasets: [
                            {
                            label: 'Número de Accesos',
                                    data: [
            <%for (estado_usuario resumen : listadoResumen) {%>
                                    "<%= resumen.getId_estado()%>",
            <%}%>
                                    ],
                                    borderWidth: 2,
                                    borderColor: '#fc544b',
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
                            },
                                    ticks: {
                                    beginAtZero: true,
                                            stepSize: 1
                                    }, scaleLabel: {
            display: true,
            labelString: 'Cantidad de accesos'
          }
                            }],
                                    xAxes: [{
                                    ticks: {
                                    display: true
                                    },
                                            gridLines: {
                                            display: true
                                            }, scaleLabel: {
            display: true,
            labelString: 'Fecha de acceso'
          }
                                    }]
                            },
                    }
            });
        </script>
    </body>
</html>
