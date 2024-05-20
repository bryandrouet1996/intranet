<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.solicitud_soporte"%>
<%@page import="modelo.forma_soporte"%>
<%@page import="modelo.tipo_soporte"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion enlace = new conexion();
    int id_solicitud = 0;
    usuario informacion = null;
    ArrayList<usuario> listadoFuncionarios = null;
    ArrayList<tipo_soporte> listadoTipoSoportes = null;
    ArrayList<forma_soporte> listadoFormaSoportes = null;
    ArrayList<usuario> listadoTecnicos = null;
    solicitud_soporte soli = null;
    try {
        id_solicitud = Integer.parseInt(request.getParameter("id_sop"));
        soli = enlace.obtenerSolicitudSoporte(id_solicitud);
        informacion = enlace.buscar_usuarioID(soli.getId_usuario());
        listadoFuncionarios = enlace.listadoUsuariosDireccion();
        listadoTipoSoportes = enlace.listadoTiposSoporte();
        listadoFormaSoportes = enlace.listadoFormasSoporte();
        listadoTecnicos = enlace.listadoUsuarioUnidad("D.02.15");
    } catch (Exception e) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
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
        <form id="formEditarSoportes" action="administrar_ticket.control?accion=editar_ticket" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-2" hidden>
                    <div class="form-group">
                        <input type="text" class="form-control" id="txtidsoli" name="txtidsoli" value="<%= soli.getId_solicitud() %>" required="">
                    </div>
                </div>
                <div class="form-group col-md-12">
                    <label>Funcionario solicitante*</label>
                    <select class="form-control select2" name="combosolicitante" id="combosolicitante" required=""> 
                        <option>Seleccione</option>
                        <%for (usuario funcionario : listadoFuncionarios) {%>
                            <%if(funcionario.getId_usuario()==soli.getId_solicitante()){%>
                                <option selected value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}else{%>
                                <option value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-3">
                    <label>Forma solicitud*</label>
                    <select class="form-control select2" name="comboforma" id="comboforma" required="">
                        <option>Seleccione</option>
                        <%for (forma_soporte forma : listadoFormaSoportes) {%>
                            <%if(forma.getId_forma()==soli.getId_forma()){%>
                                <option selected value="<%= forma.getId_forma()%>"><%= forma.getDescripcion()%></option>
                            <%}else{%>
                                <option value="<%= forma.getId_forma()%>"><%= forma.getDescripcion()%></option>
                            <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-6">
                    <label>Técnico sugerido*</label>
                    <select class="form-control select2" name="combosugerido" id="combosugerido" required="">
                        <option>Seleccione</option>
                        <%for (usuario funcionario : listadoTecnicos) {%>
                            <%if(funcionario.getId_usuario()==soli.getId_sugerido()){%>
                                <option selected value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}else{%>
                                <option value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-3">
                    <label>Tipo de solicitud*</label>
                    <select class="form-control select2" name="combotipo" id="combotipo" required="">
                        <option>Seleccione</option>
                        <%for (tipo_soporte tipo : listadoTipoSoportes) {%>
                            <%if(tipo.getId_tipo()==soli.getId_tipo()){%>
                                <option selected value="<%= tipo.getId_tipo()%>"><%= tipo.getDescripcion()%></option>
                            <%}else{%>
                                <option value="<%= tipo.getId_tipo()%>"><%= tipo.getDescripcion()%></option>
                            <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-12">
                    <div class="form-group">
                        <label for="areadescripcion">Descripción de problemática*</label>
                        <textarea type="text" class="form-control" id="areadescripcion" name="areadescripcion" placeholder="Describa la problemática reportada por el usuario." required><%= soli.getIncidente() %></textarea>
                    </div>
                </div>
            </div>
            <div class="alert alert-info">
                <b>NOTA:</b> Todos los campos de este formulario son obligatorios (*).
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary">Actualizar</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
            </div>
        </form>
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
