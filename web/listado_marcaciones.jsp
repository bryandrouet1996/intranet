<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.asistencia"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page import="modelo.TipoAsistencia"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    int id = 0;
    String funcion_usuario = null;
    usuario informacion = null;
    String codigo_direccion = null;
    String codigo_funcion = null;
    String tipo_usuario = null;
    ArrayList<asistencia> listadoRegistradas = null;
    ArrayList<asistencia> listadoAtrasos = null;
    ArrayList<asistencia> listadoFaltas = null;
    ArrayList<asistencia> listadoJustificadas = null;
    Date fecha_inicio = null;
    Date fecha_fin = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<TipoAsistencia> tiposA = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        if (request.getParameter("fecha_inicio") != null && request.getParameter("fecha_fin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("fecha_inicio"));
            fecha_fin = Date.valueOf(request.getParameter("fecha_fin"));
            listadoRegistradas = enlace.listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, informacion.getId_usuario(), 0);
            listadoAtrasos = enlace.listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, informacion.getId_usuario(), 1);
            listadoFaltas = enlace.listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, informacion.getId_usuario(), 2);
            listadoJustificadas = enlace.listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, informacion.getId_usuario(), 3);
        } else {
            listadoRegistradas = enlace.listadoAsistenciaEstadoUsuarioID(informacion.getId_usuario(), 0);
            listadoAtrasos = enlace.listadoAsistenciaEstadoUsuarioID(informacion.getId_usuario(), 1);
            listadoFaltas = enlace.listadoAsistenciaEstadoUsuarioID(informacion.getId_usuario(), 2);
            listadoJustificadas = enlace.listadoAsistenciaEstadoUsuarioID(informacion.getId_usuario(), 3);
        }
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        tiposA = enlace.GetTiposAsistencias();
    } catch (Exception e) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Mis marcaciones</title>
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/modules/fullcalendar/fullcalendar.min.css">
        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/jqvmap/dist/jqvmap.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons-wind.min.css">
        <link rel="stylesheet" href="assets/modules/summernote/summernote-bs4.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">

        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
        <!-- Start GA -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-94034622-3"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());

            gtag('config', 'UA-94034622-3');
        </script>
        <!-- /END GA --></head>

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
                                <img src="imagen.control?id=<%= informacion.getId_usuario()%>" alt="..." class="avatar-img rounded-circle">
                                <%}%>
                                <div class="d-sm-none d-lg-inline-block"><%= informacion.getApellido()%> <%= informacion.getNombre()%></div></a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <a href="sesion.control?accion=perfil&iu=<%=informacion.getId_usuario()%>" class="dropdown-item has-icon">
                                    <i class="far fa-user"></i> Perfil
                                </a>
                                <a href="sesion.control?accion=configurar_cuenta&iu=<%=informacion.getId_usuario()%>" class="dropdown-item has-icon">
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
                            <a href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>">
                                <img src="assets/img/aaa.png" height="50px">
                            </a>
                        </div>
                        <div class="sidebar-brand sidebar-brand-sm">
                            <a href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>">DS</a>
                        </div>
                        <ul class="sidebar-menu">
                            <li class="dropdown">
                                <a href="sesion.control?accion=principal&iu=<%=informacion.getId_usuario()%>" class="nav-link"><i class="fas fa-home"></i><span>Principal</span></a>
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
                <br>
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Control de asistencia - Listado de marcaciones</h1>
                        </div>
                        <div class="section-body">
                            <%
                                int ultimaA = enlace.GetTipoUltimaAsistenciaHoy(id);
                                if (ultimaA != 4) {%>
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form action="administrar_marcacion.control?accion=marcar" method="post">
                                                <div class="form-group col-md-4">
                                                    <h4>Tipo de marcación</h4>
                                                    <select class="form-control" name="tipo" id="tipo" required>
                                                        <option value="" disabled="">Seleccione tipo</option>
                                                        <%
                                                            for (TipoAsistencia tipo : tiposA) {
                                                                if (tipo.getId() == ultimaA + 1 || tipo.getId() == 5) {%>
                                                        <option value="<%= tipo.getId()%>"><%= tipo.getDescripcion()%></option>
                                                        <%} else {%>                                                            
                                                        <option value="<%= tipo.getId()%>" disabled=""><%= tipo.getDescripcion()%></option>
                                                        <%}
                                                            }%>
                                                    </select>
                                                </div>
                                                <input type="text" class="form-control" hidden="" name="id_usu" id="id_usu" readonly="" value="<%=id%>" required=""/>
                                                <div class="form-group col-md-4">
                                                    <button type="submit" class="btn btn-primary"><i class="fas fa-tasks"></i> Marcar</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%}
                            %>
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="row" action="administrar_marcacion.control?accion=filtrar" method="post">
                                                <%if (request.getParameter("fecha_inicio") == null && request.getParameter("fecha_fin") == null) {%>
                                                <div class="col-2">
                                                    <input type="text" class="form-control" name="txtini" id="txtini" readonly=""/>
                                                </div>
                                                <div class="col-2">
                                                    <input type="text" class="form-control" name="txtfin" id="txtfin" readonly=""/>
                                                </div>
                                                <%} else {%>
                                                <div class="col-2">
                                                    <input type="text" class="form-control" name="txtini" id="txtini" value="<%= fecha_inicio%>" readonly=""/>
                                                </div>
                                                <div class="col-2">
                                                    <input type="text" class="form-control" name="txtfin" id="txtfin" value="<%= fecha_fin%>" readonly=""/>
                                                </div>
                                                <%}%>
                                                <div class="form-group">
                                                    <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango
                                                    </a>
                                                </div>
                                                <div class="col-1">
                                                    <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                                </div>
                                            </form>
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-tasks"></i> Registradas <%if (!listadoRegistradas.isEmpty()) {%><span class="badge badge-primary"><%= listadoRegistradas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-user-clock"></i> Atrasos <%if (!listadoAtrasos.isEmpty()) {%><span class="badge badge-primary"><%= listadoAtrasos.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-ban"></i> Faltas <%if (!listadoFaltas.isEmpty()) {%><span class="badge badge-primary"><%= listadoFaltas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="anul-tab" data-toggle="tab" href="#anul" role="tab" aria-controls="anul" aria-selected="false"><i class="fas fa-exclamation-circle"></i> Justificadas <%if (!listadoJustificadas.isEmpty()) {%><span class="badge badge-primary"><%= listadoJustificadas.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-resumen">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Fecha de registro</th>
                                                                    <th>Hora de registro</th>
                                                                    <th>Tipo de marcación</th>
                                                                    <th>Observación</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (asistencia registradas : listadoRegistradas) {%>
                                                                <tr>
                                                                    <td><%= registradas.getFecha_creacion()%></td>
                                                                    <td><%= registradas.getHora_creacion()%></td>
                                                                    <td><%= enlace.GetTipoAsistencia(registradas.getId_tipo()).getDescripcion()%></td>
                                                                    <td>N/A
<!--                                                                        <a href="javascript:" class="btn btn-primary btn-sm" type="button" onclick="mostrarUbicacion(<%= registradas.getId_asistencia()%>)" data-toggle="modal" data-target="#modalUbicacion"><i class="fa fa-map-marked-alt" data-toggle="tooltip" data-original-title="Ver ubicación"></i></a>-->
                                                                    </td>
                                                                    <td></td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-resumen1">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Fecha de registro</th>
                                                                    <th>Hora de registro</th>
                                                                    <th>Tipo de marcación</th>
                                                                    <th>Observación</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (asistencia atrasos : listadoAtrasos) {%>
                                                                <tr>
                                                                    <td><%= atrasos.getFecha_creacion()%></td>
                                                                    <td><%= atrasos.getHora_creacion()%></td>
                                                                    <td><%= enlace.GetTipoAsistencia(atrasos.getId_tipo()).getDescripcion()%></td>
                                                                    <td>
                                                                        N/A
                                                                    </td>
                                                                    <td></td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-resumen2">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Fecha de registro</th>
                                                                    <th>Hora de registro</th>
                                                                    <th>Tipo de marcación</th>
                                                                    <th>Observación</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (asistencia faltas : listadoFaltas) {%>
                                                                <tr>
                                                                    <td><%= faltas.getFecha_creacion()%></td>
                                                                    <td><%= faltas.getHora_creacion()%></td>
                                                                    <td><%= enlace.GetTipoAsistencia(faltas.getId_tipo()).getDescripcion()%></td>
                                                                    <td>
                                                                        N/A
                                                                    </td>
                                                                    <td></td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="anul" role="tabpanel" aria-labelledby="anul-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-resumen3">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Fecha de registro</th>
                                                                    <th>Hora de registro</th>
                                                                    <th>Tipo de marcación</th>
                                                                    <th>Observación</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (asistencia justificadas : listadoJustificadas) {%>
                                                                <tr>
                                                                    <td><%= justificadas.getFecha_creacion()%></td>
                                                                    <td><%= justificadas.getHora_creacion()%></td>
                                                                    <td><%= enlace.GetTipoAsistencia(justificadas.getId_tipo()).getDescripcion()%></td>
                                                                    <td>
                                                                        N/A
                                                                    </td>
                                                                    <td></td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </section>
                </div>
                <div class="modal fade" id="modalUbicacion" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Ubicación de marcación
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body" id="form_ubicacion">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                            </div>
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

        <!-- Page Specific JS File -->
        <script src="assets/js/page/index-0.js"></script>
        <!-- JS Libraies -->
        <script src="assets/modules/fullcalendar/fullcalendar.min.js"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
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
        <script src="assets/modules/datatables/datatables.min.js"></script>
        <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
        <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>
        <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>

        <script src="assets/js/page/modules-datatables.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-calendar.js"></script>
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <script>
                                                                            $('.daterange-btn').daterangepicker({
                                                                                ranges: {
                                                                                    'Hoy': [moment(), moment()],
                                                                                    'Ayer': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                                                                                    'Hace 7 días': [moment().subtract(6, 'days'), moment()],
                                                                                    'Hace 30 días': [moment().subtract(29, 'days'), moment()],
                                                                                    'Este mes': [moment().startOf('month'), moment().endOf('month')],
                                                                                    'Mes pasado': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                                                                                },
                                                                                startDate: moment().subtract(29, 'days'),
                                                                                endDate: moment()
                                                                            }, function (start, end) {
                                                                                $('.daterange-btn span').html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'))
                                                                                var fechaInicial = start.format('YYYY-MM-DD');
                                                                                var fechaFinal = end.format('YYYY-MM-DD');
                                                                                document.getElementById("txtini").value = fechaInicial;
                                                                                document.getElementById("txtfin").value = fechaFinal;
                                                                            },
                                                                                    window.onload = function () {
            <%if (request.getParameter("fecha_inicio") == null && request.getParameter("fecha_fin") == null) {%>
                                                                                        document.getElementById("txtini").value = moment().subtract(29, 'days').format('YYYY-MM-DD');
                                                                                        document.getElementById("txtfin").value = moment().format('YYYY-MM-DD');
            <%}%>
                                                                                    });

        </script>
        <script src="fun_js/formulario_asistencia.js" type="text/javascript"></script>
        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>

    </body>
</html>
