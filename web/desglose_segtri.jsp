<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="modelo.Desglose"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="modelo.forma_soporte"%>
<%@page import="modelo.tipo_soporte"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion_oracle oracle = new conexion_oracle();
    int idTribu = 0, year = 0;
    ArrayList<Desglose> listado = new ArrayList<>();
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
    try {
        idTribu = Integer.parseInt(request.getParameter("id"));
        year = Integer.parseInt(request.getParameter("year"));
        listado = oracle.listadoEmisionesConceptoMes(idTribu, year);
    } catch (Exception e) {
        System.out.println("ex " + e);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">

        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">

        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    </head>
    <body>
        <p class="small"></p>
        <div class="modal-body">
            <p class="small"></p>
            <form class="needs-validation" novalidate="">
                <div class="form-row">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>                                 
                                <tr>
                                    <th>Mes</th>
                                    <th>Estado</th>
                                    <th>Cantidad</th>
                                    <th>Valor total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (Desglose d : listado) {
                                %>
                                <tr>
                                    <td><%= d.getMes()%></td>
                                    <td><%= d.getEstado()%></td>
                                    <td><%= d.getCantidad()%></td>
                                    <td><%= formatter.format(d.getValor())%></td>
                                </tr>
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                </div>
            </form>
        </div>
        <script src="assets/modules/jquery.min.js"></script>
        <script src="assets/modules/popper.js"></script>
        <script src="assets/modules/tooltip.js"></script>
        <script src="assets/modules/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
        <script src="assets/modules/moment.min.js"></script>
        <script src="assets/js/stisla.js"></script>

        <!-- JS Libraies -->
        <script src="assets/modules/cleave-js/dist/cleave.min.js"></script>
        <script src="assets/modules/cleave-js/dist/addons/cleave-phone.us.js"></script>
        <script src="assets/modules/jquery-pwstrength/jquery.pwstrength.min.js"></script>
        <script src="assets/modules/bootstrap-daterangepicker/daterangepicker.js"></script>
        <script src="assets/modules/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
        <script src="assets/modules/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
        <script src="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
        <script src="assets/modules/select2/dist/js/select2.full.min.js"></script>
        <script src="assets/modules/jquery-selectric/jquery.selectric.min.js"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>
        <script src="fun_js/funciones_soporte.js" type="text/javascript"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
    </body>
</html>
