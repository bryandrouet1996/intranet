<%-- 
    Document   : seguimiento_tributario
    Created on : 09/12/2021, 10:20:15
    Author     : Luis Torres F
--%>

<%@page import="modelo.ComponenteImpuesto"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="modelo.seguimiento_tributario3"%>
<%@page import="modelo.seguimiento_tributario2"%>
<%@page import="modelo.seguimiento_tributario1"%>
<%@page import="modelo.seguimiento_tributario"%>
<%@page import="modelo.rechazo_solicitud"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.permiso_horas"%>
<%@page import="modelo.motivo_permiso"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="modelo.informacion_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    conexion_oracle link = new conexion_oracle();
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<seguimiento_tributario> listadoPendientes = new ArrayList<>();
    ArrayList<seguimiento_tributario> listadoRecaudado = new ArrayList<>();
    ArrayList<ComponenteImpuesto> listadoComponentesRecaudado = new ArrayList<>();
    ArrayList<seguimiento_tributario> listadoDadoBaja = new ArrayList<>();
    ArrayList<seguimiento_tributario> listadoNotaCredito = new ArrayList<>();
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
    int y_tribu = 0, totalPen = 0, totalRec = 0, totalBajas = 0, totalNotas = 0;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (sesion.getAttribute("y_tribu")!=null) {
            y_tribu = Integer.parseInt(sesion.getAttribute("y_tribu").toString());
            listadoPendientes = link.listadoEmisiones(y_tribu);
            listadoRecaudado = link.getEmisionesRecaudadas(y_tribu);
            listadoDadoBaja = link.listadoEmisiones2(y_tribu);
            listadoNotaCredito = link.listadoEmisiones3(y_tribu);
        }
    } catch (Exception e) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Seguimiento tributario</title>
        <!-- General CSS Files -->
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/modules/fullcalendar/fullcalendar.min.css">
        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/jqvmap/dist/jqvmap.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons-wind.min.css">
        <link rel="stylesheet" href="assets/modules/summernote/summernote-bs4.css">
        <link rel="stylesheet" href="assets/modules/izitoast/css/iziToast.min.css">
        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    <body>
        <div id="app">
            <div class="main-wrapper main-wrapper-1">
                <div class="navbar-bg"></div>
                <nav class="navbar navbar-expand-lg main-navbar">
                    <form class="form-inline mr-auto">
                        <ul class="navbar-nav mr-3">
                            <li><a href="#" data-toggle="sidebar" class="nav-link nav-link-lg"><i class="fas fa-bars"></i></a></li>
                            <li><a href="#" data-toggle="search" class="nav-link nav-link-lg d-sm-none"><i class="fas fa-search"></i></a></li>
                        </ul>
                        <div class="search-element">
                            <input class="form-control" type="search" placeholder="Buscar" aria-label="Search" data-width="250">
                            <button class="btn" type="submit"><i class="fas fa-search"></i></button>
                            <div class="search-backdrop"></div>
                        </div>
                    </form>
                    <ul class="navbar-nav navbar-right">
                        <li class=""><a target="_blank" href="https://www.esmeraldas.gob.ec:2096/login/?user=<%= informacion.getCorreo()%>&pass=<%= enlace.webmailClaveUsuario(informacion.getId_usuario())%>" class="nav-link nav-link-lg  beep"><i class="far fa-envelope"></i> Webmail</a>
                        </li>
                        <li class="dropdown dropdown-list-toggle"><a href="#" data-toggle="dropdown" class="nav-link notification-toggle nav-link-lg beep"><i class="far fa-bell"></i></a>
                            <div class="dropdown-menu dropdown-list dropdown-menu-right">
                                <div class="dropdown-header">Notificaciones
                                    <div class="float-right">
                                        <a href="#">Marcar todo como leído</a>
                                    </div>
                                </div>
                                <div class="dropdown-list-content dropdown-list-icons">
                                    <a href="#" class="dropdown-item">
                                        <div class="dropdown-item-icon bg-info text-white">
                                            <i class="fas fa-bell"></i>
                                        </div>
                                        <div class="dropdown-item-desc">
                                            Bienvenido a la intranet!!
                                            <div class="time">Hoy</div>
                                        </div>
                                    </a>
                                </div>
                                <div class="dropdown-footer text-center">
                                    <a href="#">Ver todo <i class="fas fa-chevron-right"></i></a>
                                </div>
                            </div>
                        </li>
                        <li class="dropdown"><a href="#" data-toggle="dropdown" class="nav-link dropdown-toggle nav-link-lg nav-link-user">
                                <%if (foto.getNombre().equalsIgnoreCase("ninguno") && foto.getRuta().equalsIgnoreCase("ninguno")) {%>
                                <img alt="image" src="assets/img/avatar/avatar-1.png" class="rounded-circle mr-1">
                                <%} else {%>
                                <img src="imagen.control?id=<%= informacion.getId_usuario()%>" alt="..." class="rounded-circle mr-1">
                                <%}%>
                                <div class="d-sm-none d-lg-inline-block"><%= informacion.getApellido()%> <%= informacion.getNombre()%></div></a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <a href="sesion.control?accion=perfil&iu=<%=informacion.getId_usuario()%>" class="dropdown-item has-icon">
                                    <i class="far fa-user"></i> Perfil
                                </a>
                                <a  href="sesion.control?accion=configurar_cuenta&iu=<%=informacion.getId_usuario()%>" class="dropdown-item has-icon">
                                    <i class="fas fa-cog"></i> Configuración de cuenta
                                </a>
                                <div class="dropdown-divider"></div>
                                <a href="logout.control" class="dropdown-item has-icon text-danger">
                                    <i class="fas fa-sign-out-alt"></i> Cerrar sesión
                                </a>
                            </div>
                        </li>
                    </ul>
                </nav>
                <div class="main-sidebar sidebar-style-2">
                    <aside id="sidebar-wrapper">
                        <div class="sidebar-brand">
                            <a  href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>">
                                <img src="assets/img/aaa.png" height="50px">
                            </a>
                        </div>
                        <div class="sidebar-brand sidebar-brand-sm">
                            <a  href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>">DS</a>
                        </div>
                        <ul class="sidebar-menu">
                            <li class="dropdown">
                                <a  href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>" class="nav-link"><i class="fas fa-home"></i><span>Principal</span></a>
                            </li>
                            <li class="menu-header">COMPONENTES</li>
                                <%for (modulo mod : listaModulos) {
                                        listaComponente = enlace.listadoComponentesTipoUsuarioID(informacion.getId_usuario(), mod.getId_modulo());
                                %>
                                <%if(mod.isBlank()){%>
                            <li><a class="nav-link" target="_blank" href="sesion.control?accion=<%= mod.getRuta_enlace()%>&iu=<%= id%>"><i class="<%= mod.getIcono()%>"></i> <span><%= mod.getDescripcion()%></span></a></li>
                                <%}else if (listaComponente.size() > 1) {%>
                            <li class="dropdown">
                                <a href="#" class="nav-link has-dropdown" data-toggle="dropdown"><i class="<%= mod.getIcono()%>"></i> <span><%= mod.getDescripcion()%></span></a>
                                <ul class="dropdown-menu">
                                    <%for (componente item : listaComponente) {
                                            listaSubcomponente = enlace.listadoSubcomponentesTipoUsuarioID(informacion.getId_usuario(), item.getId_componente());
                                    %>
                                    <%if (listaSubcomponente.size() <= 1) {%>
                                    <li><a class="nav-link" href="sesion.control?accion=<%= item.getRuta_enlace()%>&iu=<%= id%>"><%= item.getDescripcion()%></a></li>
                                        <%} else if (listaSubcomponente.size() > 1) {%> 
                                    <li class="dropdown">
                                        <a href="#" class="nav-link has-dropdown"><%= item.getDescripcion()%></a>
                                        <ul class="dropdown-menu">
                                            <%for (subcomponente subitem : listaSubcomponente) {%>
                                            <li><a class="nav-link" href="sesion.control?accion=<%= subitem.getRuta_enlace()%>&iu=<%= id%>"><%= subitem.getDescripcion()%></a></li>
                                                <%}%>
                                        </ul>
                                    </li>
                                    <%}%>
                                    <%}%>
                                </ul>
                            </li>
                            <%} else if (listaComponente.size() <= 1) {%>
                            <li><a class="nav-link" href="sesion.control?accion=<%= mod.getRuta_enlace()%>&iu=<%= id%>"><i class="<%= mod.getIcono()%>"></i> <span><%= mod.getDescripcion()%></span></a></li>
                                <%}%>
                                <%}%>
                        </ul>
                    </aside>
                </div>

                <!-- Main Content -->
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Emisiones tributarias</h1>
                        </div>
                        <%if(y_tribu!=0){%>
                            <div class="row">
                                <div class="col-9">
                                    <div class="card-header bg-primary">
                                        <b class="text-white">Estadística de emisiones</b>
                                    </div>
                                    <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                                        <canvas id="canvas-x" height="100"></canvas>
                                    </div>
                                </div>
                            </div>
                            <br>
                        <%}%>
                        <form action="sesion.control?accion=tribu&iu=<%= informacion.getId_usuario()%>" method="post">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="txtanio">Año</label>
                                        <input type="number" class="form-control" onchange="validacionAnio(<%= enlace.anioActual()%>)" id="txtanio" name="txtanio" placeholder="Año" value="<%= y_tribu!=0 ? y_tribu : enlace.anioActual()%>" required>
                                    </div>
                                </div>   
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label for="txtanio"> </label>
                                        <input type="submit" class="form-control btn btn-primary" value="Buscar" />
                                    </div>
                                </div>  
                            </div>
                        </form>
                        <div class="card">
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-adjust"></i> Pendientes <%if (listadoPendientes.size() != 0) {%><span class="badge badge-primary"><%= listadoPendientes.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="revi-tab" data-toggle="tab" href="#revi" role="tab" aria-controls="revi" aria-selected="false"><i class="fas fa-check"></i> Recaudado <%if (listadoRecaudado.size() != 0) {%><span class="badge badge-primary"><%= listadoRecaudado.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-angle-double-down"></i> Bajas <%if (listadoDadoBaja.size() != 0) {%><span class="badge badge-primary"><%= listadoDadoBaja.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="reject-tab" data-toggle="tab" href="#reject" role="tab" aria-controls="reject" aria-selected="false"><i class="fas fa-credit-card"></i> Notas de crédito <%if (listadoNotaCredito.size() != 0) {%><span class="badge badge-primary"><%= listadoNotaCredito.size()%></span><%}%></a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-x">
                                                <thead>                                 
                                                    <tr>
                                                        <th style="width: 30%">Descripción</th>
                                                        <th style="width: 10%">Cantidad</th>
                                                        <th style="width: 10%">Valor total</th>
                                                        <th style="width: 50%">Componentes</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (seguimiento_tributario p : listadoPendientes) {
                                                        totalPen+=p.getTotal();
                                                    %>
                                                    <tr>
                                                        <td><a href="javascript:void(0);" onclick="abrirDesglose(<%= p.getId()%>,<%= y_tribu%>)"><%=p.getDescripcion()%></a></td>
                                                        <td><%= p.getCantidad()%></td>
                                                        <td><%= formatter.format(p.getTotal())%></td>
                                                        <td><%= "C1 - C2 - C3"%></td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="revi" role="revipanel" aria-labelledby="revi-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xx">
                                                <thead>                                 
                                                    <tr>
                                                        <th style="width: 30%">Descripción</th>
                                                        <th style="width: 10%">Cantidad</th>
                                                        <th style="width: 10%">Valor total</th>
                                                        <th style="width: 50%">Componentes</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (seguimiento_tributario p : listadoRecaudado) {
                                                        listadoComponentesRecaudado = link.getComponentesEmisionesRecaudadas(y_tribu, p.getId());
                                                        totalRec+=p.getTotal();
                                                    %>
                                                    <tr>
                                                        <td><%=p.getDescripcion()%></td>
                                                        <td><%= p.getCantidad()%></td>
                                                        <td><%= formatter.format(p.getTotal())%></td>
                                                        <td>
                                                            <%for(ComponenteImpuesto cr : listadoComponentesRecaudado){%>
                                                                <a onclick="abrirDesglose(<%= cr.getId()%>,<%= y_tribu%>)"><%=cr.getComponente()%></a>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xxx">
                                                <thead>                                 
                                                    <tr>
                                                        <th style="width: 30%">Descripción</th>
                                                        <th style="width: 10%">Cantidad</th>
                                                        <th style="width: 10%">Valor total</th>
                                                        <th style="width: 50%">Componentes</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (seguimiento_tributario p : listadoDadoBaja) {
                                                        totalBajas+=p.getTotal();
                                                    %>
                                                    <tr>
                                                        <td><a href="javascript:void(0);" onclick="abrirDesglose(<%= p.getId()%>,<%= y_tribu%>)"><%=p.getDescripcion()%></a></td>
                                                        <td><%= p.getCantidad()%></td>
                                                        <td><%= formatter.format(p.getTotal())%></td>
                                                        <td><%= "C1 - C2 - C3"%></td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="reject" role="tabpanel" aria-labelledby="reject-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xxxx">
                                                <thead>                                 
                                                    <tr>
                                                        <th style="width: 30%">Descripción</th>
                                                        <th style="width: 10%">Cantidad</th>
                                                        <th style="width: 10%">Valor total</th>
                                                        <th style="width: 50%">Componentes</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (seguimiento_tributario p : listadoNotaCredito) {
                                                        totalNotas+=p.getTotal();
                                                    %>
                                                    <tr>
                                                        <td><a href="javascript:void(0);" onclick="abrirDesglose(<%= p.getId()%>,<%= y_tribu%>)"><%=p.getDescripcion()%></a></td>
                                                        <td style="width: 10%"><%= p.getCantidad()%></td>
                                                        <td><%= formatter.format(p.getTotal())%></td>
                                                        <td><%= "C1 - C2 - C3"%></td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleMensual">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Desglose mensual
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body" id="desglose"></div>                                            
                    </div>
                </div>
            </div>
            <footer class="main-footer">
                <div class="footer-center">
                    Copyright &copy; <%= LocalDate.now().getYear() %> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
                </div>
                <div class="footer-right">

                </div>
            </footer>
        </div>
    </div>

    <!-- General JS Scripts -->
    <script src="assets/modules/jquery.min.js"></script>
    <script src="assets/modules/popper.js"></script>
    <script src="assets/modules/tooltip.js"></script>
    <script src="assets/modules/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
    <script src="assets/modules/moment.min.js"></script>
    <script src="assets/js/stisla.js"></script>
    <!-- JS Libraies -->
    <script src="assets/modules/simple-weather/jquery.simpleWeather.min.js"></script>
    <script src="assets/modules/chart.min.js"></script>
    <script src="assets/modules/jqvmap/dist/jquery.vmap.min.js"></script>
    <script src="assets/modules/jqvmap/dist/maps/jquery.vmap.world.js"></script>
    <script src="assets/modules/summernote/summernote-bs4.js"></script>
    <script src="assets/modules/chocolat/dist/js/jquery.chocolat.min.js"></script>
    <script src="assets/modules/bootstrap-daterangepicker/daterangepicker.js"></script>
    <script src="assets/modules/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
    <script src="assets/modules/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
    <script src="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
    <script src="assets/modules/select2/dist/js/select2.full.min.js"></script>
    <script src="assets/modules/jquery-selectric/jquery.selectric.min.js"></script>
    <script src="assets/modules/datatables/datatables.min.js"></script>
    <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
    <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/index-0.js"></script>
    <!-- JS Libraies -->
    <script src="assets/modules/fullcalendar/fullcalendar.min.js"></script>
    <script src="assets/modules/fullcalendar/locale/es.js"></script>
    <script src="assets/modules/izitoast/js/iziToast.min.js"></script>
    <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

    <!-- Page Specific JS File -->
    <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>
    <script src="assets/js/page/modules-datatables.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/forms-advanced-forms.js"></script>
    <script src="assets/js/page/modules-sweetalert.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-toastr.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-calendar.js"></script>
    <!-- Template JS File -->
    <script src="assets/js/scripts.js"></script>
    <script src="assets/js/custom.js"></script>
    <script src="fun_js/formulario_horas.js" type="text/javascript"></script>
    <script src="fun_js/funciones_horas.js" type="text/javascript"></script>
    <script src="fun_js/validacion.js" type="text/javascript"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.0.0/dist/chart.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
    <script>
        $("#table-x").dataTable({
            "ordering": false,
            "order": [[0, 'asc']],
            "columnDefs": [
                {"sortable": false, "targets": [2, 3]}
            ], "pageLength": 10,
            language: {
                "decimal": "",
                "emptyTable": "No hay datos",
                "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                "infoFiltered": "(Filtro de _MAX_ total registros)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "Mostrar _MENU_ registros",
                "loadingRecords": "Cargando...",
                "processing": "Procesando...",
                "search": "Buscar:",
                "zeroRecords": "No se encontraron coincidencias",
                "paginate": {
                    "first": "Primero",
                    "last": "Ultimo",
                    "next": "Próximo",
                    "previous": "Anterior"
                }
            }
        });
        
        $("#table-xx").dataTable({
            "ordering": false,
            "order": [[0, 'asc']],
            "columnDefs": [
                {"sortable": false, "targets": [2, 3]}
            ], "pageLength": 10,
            language: {
                "decimal": "",
                "emptyTable": "No hay datos",
                "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                "infoFiltered": "(Filtro de _MAX_ total registros)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "Mostrar _MENU_ registros",
                "loadingRecords": "Cargando...",
                "processing": "Procesando...",
                "search": "Buscar:",
                "zeroRecords": "No se encontraron coincidencias",
                "paginate": {
                    "first": "Primero",
                    "last": "Ultimo",
                    "next": "Próximo",
                    "previous": "Anterior"
                }
            }
        });
        
        $("#table-xxx").dataTable({
            "ordering": false,
            "order": [[0, 'asc']],
            "columnDefs": [
                {"sortable": false, "targets": [2, 3]}
            ], "pageLength": 10,
            language: {
                "decimal": "",
                "emptyTable": "No hay datos",
                "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                "infoFiltered": "(Filtro de _MAX_ total registros)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "Mostrar _MENU_ registros",
                "loadingRecords": "Cargando...",
                "processing": "Procesando...",
                "search": "Buscar:",
                "zeroRecords": "No se encontraron coincidencias",
                "paginate": {
                    "first": "Primero",
                    "last": "Ultimo",
                    "next": "Próximo",
                    "previous": "Anterior"
                }
            }
        });
        
        $("#table-xxxx").dataTable({
            "ordering": false,
            "order": [[0, 'asc']],
            "columnDefs": [
                {"sortable": false, "targets": [2, 3]}
            ], "pageLength": 10,
            language: {
                "decimal": "",
                "emptyTable": "No hay datos",
                "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                "infoFiltered": "(Filtro de _MAX_ total registros)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "Mostrar _MENU_ registros",
                "loadingRecords": "Cargando...",
                "processing": "Procesando...",
                "search": "Buscar:",
                "zeroRecords": "No se encontraron coincidencias",
                "paginate": {
                    "first": "Primero",
                    "last": "Ultimo",
                    "next": "Próximo",
                    "previous": "Anterior"
                }
            }
        });
        
        new Chart(document.getElementById("canvas-x"), {
                type: 'bar',
                data: {
                  labels: ["Pendientes", "Recaudado", "Bajas", "Notas de crédito"],
                  datasets: [
                    {
                      label: "Emisiones",
                      backgroundColor: ["#FFF26D", "#B4FF89","#FFB937","#6FCDF8"],
                      data: [<%=totalPen%>,<%=totalRec%>,<%=totalBajas%>,<%=totalNotas%>]
                    }
                  ]
                },
                options: {
                  plugins: {
                    legend: {
                      display: false
                    },
                    datalabels: {
                        formatter: function(value, context) {
                            return '$' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                        }
                    }
                  },
                  title: {
                    display: false,
                    text: 'Resumen de emisiones'
                  },
                  scaleLabel: function(value, context) {
                        return '$' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    }
                },
                plugins: [ChartDataLabels]
            });
            
            function abrirDesglose(id,year) {
                $('#modalDetalleMensual').modal('show');
                $("#desglose").load("desglose_segtri.jsp?id="+id+"&year="+year);
            }
    </script>
</body>
</html>