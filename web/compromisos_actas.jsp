<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.usuario"%>
<%@page import="modelo.acta"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.compromiso_acta"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion enlace = new conexion();
    ArrayList<compromiso_acta> listadoCompromisos = null;
    int id_acta = 0;
    java.sql.Date fecha_acta = null;
    String asunto = null;
    int cumplido = 0;
    float total = 0;
    double porcentaje_cumplido = 0;
    double porcentaje_incumplido = 0;
    try {
        id_acta = Integer.parseInt(request.getParameter("id_acta"));
        acta elem = enlace.buscarActaID(id_acta);
        listadoCompromisos = enlace.listadoCompromisosActa(id_acta);
        cumplido = enlace.cantidadCompromisosActaID(id_acta, 1);
        total = listadoCompromisos.size();
        fecha_acta = elem.getFecha_acta();
        porcentaje_cumplido = ((cumplido/total)*100);
        porcentaje_incumplido = 100-porcentaje_cumplido;
        asunto = elem.getAsunto();
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
                <label><b>Fecha acta:</b> <%= fecha_acta%></label>
            </div>
            <div class="col-6">
                <label><b>Asunto:</b> <%= asunto%></label>
            </div>
            <br>
            <div class="col-12">
                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                    <canvas id="myChart1111222" height="100"></canvas>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>                                 
                        <tr>
                            <th>Fecha límite de cumplimiento</th>
                            <th>Compromiso</th>
                            <th>Responsable</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%for(compromiso_acta compromiso:listadoCompromisos){
                            String estado = null;
                            usuario usu = enlace.buscar_usuarioID(compromiso.getId_responsable());
                            String responsable = usu.getApellido()+" "+ usu.getNombre();
                        %>
                        <tr>
                            <td><%= compromiso.getFecha_cumplimiento() %></td>
                            <td><%= compromiso.getDescripcion() %></td>
                            <td><%= responsable %></td>
                            <td>
                                <%if(compromiso.getEstado()==0){
                                    estado = "Incumplido";
                                %>
                                    <a class="badge badge-danger text-white"><%= estado %></a>
                                <%}else if(compromiso.getEstado()==1){
                                    estado = "Cumplido";
                                %>
                                <a class="badge badge-primary text-white"><%= estado %></a>
                                <%}%>
                            </td>
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
                                <%= porcentaje_cumplido %>, <%= porcentaje_incumplido %>
                            ],
                            backgroundColor: [
                                '#00793e',
                                '#fc544b'
                            ],
                            label: 'Dataset 1'
                        }],
                    labels: [
                        'Cumplido',
                        'Incumplido'
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
