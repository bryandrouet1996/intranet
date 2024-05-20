<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.resumen_servicio"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.usuario_servicio"%>
<%@page import="modelo.tramite_funcionario"%>
<%@page import="modelo.estados_solicitud"%>
<%@page import="modelo.solicitud"%>
<%@page import="modelo.conexion_servicios"%>
<%@page import="modelo.rol_pago"%>
<%@page import="modelo.rol_usuario"%>
<%@page import="modelo.organizacion"%>
<%@page import="modelo.cargo"%>
<%@page import="modelo.comentario_actividad"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.actividad"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    conexion_servicios link = new conexion_servicios();
    int id = 0;
    String funcion_usuario = null;
    usuario informacion = null;
    foto_usuario foto = null;
    String codigo_direccion = null;
    String codigo_funcion = null;
    String tipo_usuario = null;
    String anio = null;
    float total_solicitudes = 0;
    int cantidad_tramite = 0;
    int cantidad_anulada = 0;
    int cantidad_finalizada = 0;
    int cantidad_nueva = 0;
    double porcentaje_nueva = 0;
    double porcentaje_anulada = 0;
    double porcentaje_tramite = 0;
    double porcentaje_finalizada = 0;
    int direc = 0;
    ArrayList<organizacion> listadoDirecciones = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<resumen_servicio> listadoResumenAnuladas = null;
    ArrayList<resumen_servicio> listadoResumenFinalizadas = null;
    ArrayList<resumen_servicio> listadoResumenEnTramite = null;
    ArrayList<resumen_servicio> listadoResumenPendientes = null;
    ArrayList<resumen_servicio> listadoResumen = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoDirecciones = link.listadoDirecciones();
        if (request.getParameter("anio") != null && request.getParameter("direccion") != null) {
            anio = request.getParameter("anio");
            direc = Integer.parseInt(request.getParameter("direccion"));
            listadoResumen = link.listadoResumenDireccionAnio(anio, direc);
            listadoResumenPendientes = link.listadoSolicitudesPendientesDireccionAnio(anio, direc);
            listadoResumenFinalizadas = link.listadoSolicitudesFinalizadasResumenDireccionAnio(anio, direc);
            listadoResumenEnTramite = link.listadoSolicitudesEnTramiteResumenDireccionAnio(anio, direc);
            listadoResumenAnuladas = link.listadoSolicitudesAnuladasDireccionAnio(anio, direc);
            cantidad_finalizada = link.cantidadSolicitudesFinalizadasResumenDireccionAnio(anio, direc);
            cantidad_tramite = link.cantidadSolicitudesEnTramiteResumenDireccionAnio(anio, direc);
            cantidad_nueva = link.cantidadSolicitudesNuevasDireccionAnio(anio, direc);
            cantidad_anulada = link.cantidadSolicitudesAnuladasDireccionAnio(anio, direc);
            total_solicitudes = cantidad_finalizada + cantidad_tramite + cantidad_nueva + cantidad_anulada;
            porcentaje_finalizada = enlace.limitarDecimales((cantidad_finalizada / total_solicitudes) * 100);
            porcentaje_tramite = enlace.limitarDecimales((cantidad_tramite / total_solicitudes) * 100);
            porcentaje_nueva = enlace.limitarDecimales((cantidad_nueva / total_solicitudes) * 100);
            porcentaje_anulada = enlace.limitarDecimales((cantidad_anulada / total_solicitudes)*100);
        }
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
        <title>Intranet Alcaldía - Reporte de ventanilla web</title>
        <!-- General CSS Files -->
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
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/izitoast/css/iziToast.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">

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
        <div id="fb-root"></div>
        <script async defer crossorigin="anonymous" src="https://connect.facebook.net/es_ES/sdk.js#xfbml=1&version=v5.0"></script>
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
                            <h1>Reporte - Ventanilla web municipal</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <form action="administrar_servicio.control?accion=filtro_reporte" method="post">
                                                <div class="row">
                                                    <%if (request.getParameter("anio") == null && request.getParameter("direccion") == null) {%>
                                                    <div class="col-3">
                                                        <input type="number" class="form-control" onchange="validacionAnio(<%= enlace.anioActual()%>)" id="txtanio" name="txtanio" placeholder="Año" value="<%= enlace.anioActual()%>" min="2020" required>
                                                    </div>
                                                    <div class="col-5">
                                                        <div class="form-group">
                                                            <select class="form-control" name="combodir" required>
                                                                <option value="">Elija Dirección</option>
                                                                <%for (organizacion direccion : listadoDirecciones) {%>
                                                                <%if (direccion.getId_organizacion() == 2) {%>
                                                                <option selected value="<%= direccion.getId_organizacion()%>"><%= direccion.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= direccion.getId_organizacion()%>"><%= direccion.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <%} else {%>
                                                    <div class="col-3">
                                                        <input type="number" class="form-control" onchange="validacionAnio(<%= enlace.anioActual()%>)" id="txtanio" name="txtanio" placeholder="Año" value="<%= anio%>" min="2020" required>
                                                    </div>
                                                    <div class="col-5">
                                                        <div class="form-group">
                                                            <select class="form-control" name="combodir" required>
                                                                <option value="">Elija Dirección</option>
                                                                <%for (organizacion direccion : listadoDirecciones) {%>
                                                                <%if (direccion.getId_organizacion() == direc) {%>
                                                                <option selected value="<%= direccion.getId_organizacion()%>"><%= direccion.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= direccion.getId_organizacion()%>"><%= direccion.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <%}%>
                                                    <div class="col-2">
                                                        <button class="btn btn-primary btn-sm"><i class="fas fa-search"></i> Filtrar</button>
                                                    </div>
                                                </div>
                                            </form>
                                            <div class="card-header-action">
                                                <a href="#summary-chart" data-tab="summary-tab" class="btn active">Gráfica</a>
                                                <a href="#summary-text" data-tab="summary-tab" class="btn">Resultado total</a>
                                            </div>
                                        </div>
                                        <div class="card-body">
                                            <%if (request.getParameter("anio") == null && request.getParameter("direccion") == null) {%>
                                            <div class="summary">
                                                <div class="summary-info" data-tab-group="summary-tab" id="summary-text">
                                                    <h4>0</h4>
                                                    <div class="text-muted">Total de solicitudes registradas</div>
                                                </div>
                                                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                                                    <canvas id="myChart222" height="60"></canvas>
                                                </div>
                                                <div class="summary-item">
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted">0</div>
                                                        <div class="font-weight-bold mb-1">Nuevas</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar" style="background: #ffe933" role="progressbar" data-width="0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted">0</div>
                                                        <div class="font-weight-bold mb-1">En trámite</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-info" role="progressbar" data-width="0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted">0</div>
                                                        <div class="font-weight-bold mb-1">Finalizadas</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-success" role="progressbar" data-width="0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-wsght-bold text-muted">0</div>
                                                        <div class="font-weight-bold mb-1">Anuladas</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar" style="background: #fa1e00" role="progressbar" data-width="0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-resumen">
                                                    <thead>                                 
                                                        <tr>
                                                            <th>#</th>
                                                            <th>Mes</th>
                                                            <th>N° solicitudes</th>
                                                            <th>Acciones</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>

                                                    </tbody>
                                                </table>
                                            </div>
                                            <%} else {%>
                                            <div class="summary col-12">
                                                <div class="summary-info" data-tab-group="summary-tab" id="summary-text">
                                                    <h4><%= total_solicitudes%></h4>
                                                    <div class="text-muted">Total de solicitudes registradas</div>
                                                </div>
                                                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                                                    <canvas id="myChart222" height="60"></canvas>
                                                </div>
                                                <div class="summary-item">
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted"><%= cantidad_nueva%></div>
                                                        <div class="font-weight-bold mb-1">Nuevas (<%= porcentaje_nueva%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar" style="background: #ffe933" role="progressbar" data-width="<%= porcentaje_nueva%>%" aria-valuenow="<%= porcentaje_nueva%>" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted"><%= cantidad_tramite%></div>
                                                        <div class="font-weight-bold mb-1">En trámite (<%= porcentaje_tramite%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-info" role="progressbar" data-width="<%= porcentaje_tramite%>%" aria-valuenow="<%= porcentaje_tramite%>" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted"><%= cantidad_finalizada%></div>
                                                        <div class="font-weight-bold mb-1">Finalizadas (<%= porcentaje_finalizada%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-success" role="progressbar" data-width="<%= porcentaje_finalizada%>%" aria-valuenow="<%= porcentaje_finalizada%>" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted"><%= cantidad_anulada%></div>
                                                        <div class="font-weight-bold mb-1">Anuladas (<%= porcentaje_anulada%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar" style="background: #fa1e00" role="progressbar" data-width="<%= porcentaje_anulada%>%" aria-valuenow="<%= porcentaje_anulada%>" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="table-responsive col-12">
                                                <table class="table table-striped" id="table-resumen">
                                                    <thead>                                 
                                                        <tr>
                                                            <th>#</th>
                                                            <th>Mes</th>
                                                            <th>N° solicitudes</th>
                                                            <th></th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%for (resumen_servicio servicio : listadoResumen) {%>
                                                        <tr>
                                                            <td><%= servicio.getId_resumen()%></td>
                                                            <td><%= link.mesNombre(servicio.getId_resumen())%></td>
                                                            <td><%= servicio.getCantidad()%></td>
                                                            <td></td>
                                                        </tr>
                                                        <%}%>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <%}%>

                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-6 col-lg-6" hidden="">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Cantidad de solicitudes por mes</h4>
                                        </div>
                                        <div class="card-body">
                                            <canvas id="myChart2"></canvas>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-6 col-lg-6" hidden="">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Porcentaje por estado de solicitud</h4>
                                        </div>
                                        <div class="card-body">
                                            <canvas id="myChart331"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
                <div class="modal fade" id="generarReporte" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Seleccionar rango
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form action="administrar_servicios.control" method="post">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label for="combomes">Tipo de reporte</label>
                                                <select name="combotipo" class="form-control">
                                                    <option value="0">Seleccione</option>
                                                    <option value="1">Ranking de atención ciudadana</option>
                                                    <option value="2">Número de solicitudes por tipo</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="combomes">Fecha inicio</label>
                                                <input type="text" class="form-control datepicker" id="txtin" name="txtin" placeholder="Fecha inicio" required>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="txtanio">Fecha fin</label>
                                                <input type="text" class="form-control datepicker" id="txtfin" name="txtfin" placeholder="Fecha fin" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Generar</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
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
        <script src="assets/modules/izitoast/js/iziToast.min.js"></script>
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
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <script src="assets/modules/chart.min.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-chartjs.js"></script>
        <script src="fun_js/validacion.js" type="text/javascript"></script>
        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <%if (request.getParameter("anio") != null && request.getParameter("direccion") != null) {%>
        <script>
                                                            var ctx = document.getElementById("myChart222").getContext('2d');
                                                            var myChart = new Chart(ctx, {
                                                            type: 'line',
                                                                    data: {
                                                                    labels: ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"],
                                                                            datasets: [
                                                                            {
                                                                            label: 'Nuevas',
                                                                                    data: [
            <%for (resumen_servicio servicio : listadoResumenPendientes) {%>
            <%= servicio.getCantidad()%>,
            <%}%>
                                                                                    ],
                                                                                    borderWidth: 2,
                                                                                    borderColor: '#ffe933',
                                                                                    borderWidth: 2.5,
                                                                                    pointBackgroundColor: '#ffffff',
                                                                                    pointRadius: 4
                                                                            },
                                                                            {
                                                                            label: 'En trámite',
                                                                                    data: [
            <%for (resumen_servicio servicio : listadoResumenEnTramite) {%>
            <%= servicio.getCantidad()%>,
            <%}%>
                                                                                    ],
                                                                                    borderWidth: 2,
                                                                                    borderColor: '#3abaf4',
                                                                                    borderWidth: 2.5,
                                                                                    pointBackgroundColor: '#ffffff',
                                                                                    pointRadius: 4
                                                                            },
                                                                            {
                                                                            label: 'Finalizada',
                                                                                    data: [
            <%for (resumen_servicio servicio : listadoResumenFinalizadas) {%>
            <%= servicio.getCantidad()%>,
            <%}%>
                                                                                    ],
                                                                                    borderWidth: 2,
                                                                                    borderColor: '#63ed7a',
                                                                                    borderWidth: 2.5,
                                                                                    pointBackgroundColor: '#ffffff',
                                                                                    pointRadius: 4
                                                                            },{
                                                                            label: 'Anuladas',
                                                                                    data: [
            <%for (resumen_servicio servicio : listadoResumenAnuladas) {%>
            <%= servicio.getCantidad()%>,
            <%}%>
                                                                                    ],
                                                                                    borderWidth: 2,
                                                                                    borderColor: '#fa1e00',
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
                                                                            drawBorder: true,
                                                                                    color: '#f2f2f2',
                                                                            },
                                                                                    ticks: {
                                                                                    beginAtZero: true,
                                                                                            stepSize: 150
                                                                                    }, scaleLabel: {
                                                                            display: true,
                                                                                    labelString: 'Cantidad de solicitudes'
                                                                            },
                                                                            }],
                                                                                    xAxes: [{
                                                                                    ticks: {
                                                                                    display: true
                                                                                    }, scaleLabel: {
                                                                                    display: true,
                                                                                            labelString: 'Meses'
                                                                                    },
                                                                                            gridLines: {
                                                                                            display: true
                                                                                            }
                                                                                    }]
                                                                            },
                                                                    }
                                                            });
                                                            var ctx = document.getElementById("myChart2").getContext('2d');
                                                            var myChart = new Chart(ctx, {
                                                            type: 'bar',
                                                                    data: {
                                                                    labels: ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"],
                                                                            datasets: [{
                                                                            label: 'Solicitudes',
                                                                                    data: [
            <%for (int paso = 1; paso <= 12; paso++) {%>
            <%= link.totalSolicitudesMensuales(paso)%>,
            <%}%>
                                                                                    ],
                                                                                    borderWidth: 2,
                                                                                    backgroundColor: '#00793e',
                                                                                    borderColor: '#00793e',
                                                                                    borderWidth: 2.5,
                                                                                    pointBackgroundColor: '#ffffff',
                                                                                    pointRadius: 4
                                                                            }]
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
                                                                                            stepSize: 150
                                                                                    }
                                                                            }],
                                                                                    xAxes: [{
                                                                                    ticks: {
                                                                                    display: false
                                                                                    },
                                                                                            gridLines: {
                                                                                            display: false
                                                                                            }
                                                                                    }]
                                                                            },
                                                                    }
                                                            });
                                                            var ctx = document.getElementById("myChart331").getContext('2d');
                                                            var myChart = new Chart(ctx, {
                                                            type: 'doughnut',
                                                                    data: {
                                                                    datasets: [{
                                                                    data: [
            <%= link.porcentajeSolicitudesEstados(0)%>,
            <%= link.porcentajeSolicitudesEnTramite()%>,
            <%= link.porcentajeSolicitudesEstados(1)%>,
            <%= link.porcentajeSolicitudesEstados(2)%>,
            <%= link.porcentajeSolicitudesEstados(400)%>,
                                                                    ],
                                                                            backgroundColor: [
                                                                                    '#fc544b',
                                                                                    '#3abaf4',
                                                                                    '#ffa426',
                                                                                    '#63ed7a',
                                                                                    '#cdd3d8',
                                                                            ],
                                                                            label: 'Dataset 1'
                                                                    }],
                                                                            labels: [
                                                                                    'Pendiente',
                                                                                    'En tramite',
                                                                                    'Por finalizar',
                                                                                    'Finalizada',
                                                                                    'Anulada'
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
        <%}%>
    </body>
</html>
