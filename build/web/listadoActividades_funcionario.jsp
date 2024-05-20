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
    usuario ele = null;
    Date fecha_inicio = null;
    Date fecha_fin = null;
    ArrayList<actividad> listadoTodasActividades = null;
    try {
        int id_usuario = Integer.parseInt(request.getParameter("iu"));
        ele = enlace.buscar_usuarioID(id_usuario);
        if (request.getParameter("fechi") != null && request.getParameter("fechf") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("fechi"));
            fecha_fin = Date.valueOf(request.getParameter("fechf"));
            listadoTodasActividades = enlace.listadoActividadesTodasRangoFechaUsuarioId(id_usuario, fecha_inicio, fecha_fin);
        }
    } catch (Exception e) {
        System.out.println(e);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">

        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    </head>
    <body>
        <div class="row col-12">
            <div class="col-6">
                <label><%= ele.getApellido()%> <%= ele.getNombre()%></label>
            </div>
            <div class="col-6">
                <label > <b>desde </b><%= fecha_inicio%><b> hasta </b><%= fecha_fin%></label>
            </div>
            <br>
            <br>
            <div class="col-12">
                <div class="table-responsive">
                    <table class="table table-striped" id="table-5">
                        <thead>                                 
                            <tr>
                                <th>Fecha</th>
                                <th>Hora inicio</th>
                                <th>Hora fin</th>
                                <th>Tiempo de ejecución</th>
                                <th>Descripción de tarea</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (actividad todas : listadoTodasActividades) {
                                    int id_actividad = todas.getId_actividad();
                                    String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                            %>
                            <tr>
                                <td><%= todas.getFecha_actividad()%></td>
                                <td><%= todas.getHora_inicio()%></td>
                                <td><%= todas.getHora_fin()%></td>
                                <td><%= tiempo_ejecucion%></td>
                                <td><%= todas.getTarea()%></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
        <script src="assets/modules/moment.min.js"></script>
        <script src="assets/modules/cleave-js/dist/cleave.min.js"></script>
        <script src="assets/modules/tooltip.js"></script>
        <script src="assets/modules/cleave-js/dist/addons/cleave-phone.us.js"></script>
        <script src="assets/modules/jquery-pwstrength/jquery.pwstrength.min.js"></script>
        <script src="assets/modules/bootstrap-daterangepicker/daterangepicker.js"></script>
        <script src="assets/modules/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
        <script src="assets/modules/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
        <script src="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
        <script src="assets/modules/select2/dist/js/select2.full.min.js"></script>
        <script src="assets/modules/jquery-selectric/jquery.selectric.min.js"></script>
        <script src="assets/modules/datatables/datatables.min.js"></script>
        <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
        <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>
        <script src="fun_js/formulario_actividades.js" type="text/javascript"></script>
        <script>
            $("#table-5").dataTable({
  "ordering": true,
  "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
  "columnDefs": [
    { "sortable": false, "targets": [2,3] }
  ],"pageLength": 25,
  language: {
        "decimal":        "",
    "emptyTable":     "No hay datos",
    "info":           "Mostrando _START_ a _END_ de _TOTAL_ registros",
    "infoEmpty":      "Mostrando 0 a 0 de 0 registros",
    "infoFiltered":   "(Filtro de _MAX_ total registros)",
    "infoPostFix":    "",
    "thousands":      ",",
    "lengthMenu":     "Mostrar _MENU_ registros",
    "loadingRecords": "Cargando...",
    "processing":     "Procesando...",
    "search":         "Buscar:",
    "zeroRecords":    "No se encontraron coincidencias",
    "paginate": {
        "first":      "Primero",
        "last":       "Ultimo",
        "next":       "Próximo",
        "previous":   "Anterior"
    }
  }
});
        </script>
    </body>
</html>
