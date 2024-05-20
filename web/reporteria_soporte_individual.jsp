<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.componente"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.satisfaccion_soporte"%>
<%@page import="modelo.diagnostico_soporte"%>
<%@page import="modelo.asignacion_soporte"%>
<%@page import="modelo.forma_soporte"%>
<%@page import="modelo.tipo_soporte"%>
<%@page import="modelo.solicitud_soporte"%>
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
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String codigo_direccion = null;
    String funcion_usuario = null;
    String tipo_usuario = null;
    String codigo_funcion = null;
    ArrayList<solicitud_soporte> listadoSoportesSolicitados = null;
    ArrayList<usuario> listadoTecnicos = null;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    int numEstado = 0;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        listadoTecnicos = enlace.listadoUsuarioUnidad("D.02.15.03");
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        if(request.getParameter("txtestado")!= null){
            numEstado = Integer.parseInt(request.getParameter("txtestado").toString());
        }
        listadoSoportesSolicitados = fecha_inicio == null && fecha_fin == null ? enlace.listadoSolicitudesAsignadasUsuarioIDRecienteMes(informacion.getId_usuario()) : (numEstado == 0 ? enlace.listadoSolicitudesUsuarioFecha(informacion.getId_usuario(), fecha_inicio, fecha_fin) : enlace.listadoSolicitudesUsuarioFechaEstado(informacion.getId_usuario(), fecha_inicio, fecha_fin, numEstado));
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Reportería de soportes - Individual</title>
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
                                <a href="#" class="nav-link has-dropdown" data-toggle="dropdown"><i class="<%= mod.getIcono() %>"></i> <span><%= mod.getDescripcion()%></span></a>
                                <ul class="dropdown-menu">
                                    <%for (componente item : listaComponente) {
                                        listaSubcomponente = enlace.listadoSubcomponentesTipoUsuarioID(informacion.getId_usuario(), item.getId_componente());
                                    %>
                                    <%if(listaSubcomponente.size()<=1){%>
                                    <li><a class="nav-link" href="sesion.control?accion=<%= item.getRuta_enlace()%>&iu=<%= id %>"><%= item.getDescripcion()%></a></li>
                                    <%}else if (listaSubcomponente.size() > 1){%> 
                                        <li class="dropdown">
                                        <a href="#" class="nav-link has-dropdown"><%= item.getDescripcion() %></a>
                                        <ul class="dropdown-menu">
                                            <%for(subcomponente subitem:listaSubcomponente){%>
                                            <li><a class="nav-link" href="sesion.control?accion=<%= subitem.getRuta_enlace()%>&iu=<%= id %>"><%= subitem.getDescripcion() %></a></li>
                                            <%}%>
                                        </ul>
                                    </li>
                                    <%}%>
                                    <%}%>
                                </ul>
                            </li>
                            <%} else if (listaComponente.size() <= 1) {%>
                            <li><a class="nav-link" href="sesion.control?accion=<%= mod.getRuta_enlace() %>&iu=<%= id %>"><i class="<%= mod.getIcono() %>"></i> <span><%= mod.getDescripcion()%></span></a></li>
                                <%}%>
                                <%}%>
                        </ul>
                    </aside>
                </div>
                <br>
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Reportería de soportes - Individual</h1>
                        </div>
                        <div class="section-body">
                            <form action="">
                                <div class="row">
                                    <%if(fecha_inicio!=null && fecha_fin!=null){%>
                                    <div class="col-9">
                                        <div class="card-header bg-primary">
                                            <b class="text-white">Resumen de soportes</b>
                                        </div>
                                        <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                                            <canvas id="canvas-x" height="120"></canvas>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        
                                    </div>
                                    <%}%>
                                    <div class="col-3">
                                        <%if (fecha_inicio != null) {%>
                                        <input type="text" class="form-control datepicker" name="txtini" id="txtini" value="<%= fecha_inicio%>"/>
                                        <%} else {%>
                                        <input type="text" class="form-control datepicker" name="txtini" id="txtini"/>
                                        <%}%>
                                    </div>
                                    <div class="col-3">
                                        <%if (fecha_fin != null) {%>
                                        <input type="text" class="form-control datepicker" name="txtfin" id="txtfin" value="<%= fecha_fin%>"/>
                                        <%} else {%>
                                        <input type="text" class="form-control datepicker" name="txtfin" id="txtfin"/>
                                        <%}%>

                                    </div>
                                    <div class="col-2">
                                        <select class="form-control select2" name="txtestado" id="txtestado">
                                            <option <%=numEstado==0?"selected":""%> value="0">Todos</option>
                                            <option <%=numEstado==1?"selected":""%> value="1">Pendientes</option>
                                            <option <%=numEstado==2?"selected":""%> value="2">Atendiendo</option>
                                            <option <%=numEstado==3?"selected":""%> value="3">Atendidos</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango
                                        </a>
                                    </div>
                                    <div class="col-1">
                                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                    </div>
                                </div>
                            </form>
                            <br>
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-users"></i> Soportes <%=fecha_inicio == null && fecha_fin == null ? "recientes" : "consultados"%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>
                                                                <tr>
                                                                    <th>Código</th>
                                                                    <th>Fecha</th>
                                                                    <th>Tipo de soporte</th>
                                                                    <th>Funcionario Solicitante</th>
                                                                    <th>Fecha asignación</th>
                                                                    <th>Calificación</th>
                                                                    <th>Estado</th>
                                                                    <th>Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (solicitud_soporte soli : listadoSoportesSolicitados) {
                                                                        usuario func = enlace.buscar_usuarioID(soli.getId_solicitante());
                                                                        tipo_soporte tp = enlace.ObtenerTipoSoporteID(soli.getId_tipo());
                                                                        forma_soporte fp = enlace.ObtenerFormaSoporteID(soli.getId_forma());
                                                                        String estado = enlace.obtenerEstadoSolicitudSoporteID(soli.getId_solicitud());
                                                                        usuario sugerido = enlace.buscar_usuarioID(soli.getId_sugerido());
                                                                        usuario registra = enlace.buscar_usuarioID(soli.getId_usuario());
                                                                        asignacion_soporte asig = enlace.obtenerAsignacionSoporteID(soli.getId_solicitud());
                                                                        diagnostico_soporte diag = enlace.obtenerDiagnosticoSoporteID(soli.getId_solicitud());
                                                                %>
                                                                <tr>
                                                                    <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleSolicitud" data-incidente="<%= soli.getIncidente()%>" data-forma="<%= fp.getDescripcion()%>" data-sugerido="<%= sugerido.getApellido()%> <%= sugerido.getNombre()%>" data-registra="<%= registra.getApellido()%> <%= registra.getNombre()%>"><%= "SP-" + soli.getId_solicitud()%></a></td>
                                                                    <td><%= soli.getFecha_creacion()%></td>
                                                                    <td><%= tp.getDescripcion()%></td>
                                                                    <td><%= func.getApellido()%> <%= func.getNombre()%></td>
                                                                    <td>
                                                                        <%= asig.getFecha_asignacion()%>
                                                                    </td>
                                                                    <td>
                                                                        <%if (enlace.tieneCalificacionSolicitudSoporte(soli.getId_solicitud())) {
                                                                                satisfaccion_soporte satis = enlace.obtenerSatisfaccionSolicitudSoporteID(soli.getId_solicitud());
                                                                        %>
                                                                        <%if (satis.getId_satisfaccion() == 1) {%>
                                                                        <a class="badge badge-danger text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 2) {%>
                                                                        <a class="badge badge-warning text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 3) {%>
                                                                        <a class="badge badge-warning text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 4) {%>
                                                                        <a class="badge badge-secondary text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 5) {%>
                                                                        <a class="badge badge-primary text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 6) {%>
                                                                        <a class="badge badge-primary text-white"><%= satis.getDescripcion()%></a>
                                                                        <%} else if (satis.getId_satisfaccion() == 7) {%>
                                                                        <a class="badge badge-success text-white"><%= satis.getDescripcion()%></a>
                                                                        <%}%>
                                                                        <%} else {%>
                                                                        <%= "¡Sin calificar!"%>
                                                                        <%}%>
                                                                    </td>
                                                                    <td>
                                                                        <%if (soli.getEstado() == 0) {%>
                                                                        <a class="badge badge-warning text-white"><%= estado%></a>
                                                                        <%} else if (soli.getEstado() == 1) {%>
                                                                        <a class="badge badge-primary text-white"><%= estado%></a>
                                                                        <%} else if (soli.getEstado() == 2) {%>
                                                                        <a class="badge badge-info text-white"><%= estado%></a>
                                                                        <%} else if (soli.getEstado() == 3) {%>
                                                                        <a class="badge badge-success text-white"><%= estado%></a>
                                                                        <%} else if (soli.getEstado() == 4) {%>
                                                                        <a class="badge badge-danger text-white"><%= estado%></a>
                                                                        <%} else if (soli.getEstado() == 5) {%>
                                                                        <a class="badge badge-dark text-white"><%= estado%></a>
                                                                        <%}%>                                                                        
                                                                    </td>
                                                                    <td>
                                                                        <%if(soli.getEstado() > 0){%>
                                                                            <a target="_blank" href="administrar_ticket.control?accion=informe&sol=<%=soli.getId_solicitud()%>" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar informe"></i></a>
                                                                        <%}%>
                                                                        <%if(enlace.existeAdjuntoSolicitud(soli.getId_solicitud())){%>
                                                                            <a target="_blank" href="descargar_adjunto.control?accion=descargar_adjunto&id_solicitud=<%=soli.getId_solicitud()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                        <%}%>
                                                                    </td>
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
                        </div>
                    </section>
                </div>
                <div class="modal fade" id="modalDetalleSolicitud" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de solicitud
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="form-group col-md-12">
                                            <label>Descripción de incidente</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaincidente" id="areaincidente" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>forma de petición</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtforms" id="txtforms" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-5">
                                            <label>Técnico sugerido</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtsugerido" id="txtsugerido" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-5">
                                            <label>Registrado por</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtregistra" id="txtregistra" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modalDiagnosticoSolicitud" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formDiagnosticoSoporte" action="administrar_ticket.control?accion=registrar_diagnostico" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden="">
                                            <label>id solicitud</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtidsolicitud1" id="txtidsolicitud1" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden="">
                                            <label>id tecnico</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtidtecnico1" id="xtidtecnico1" required="" readonly="" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Diagnóstico</label>
                                            <textarea type="text" class="form-control" placeholder="Describa que solución dio al problema de manera breve" name="areadiagnostico" id="areadiagnostico" required=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Registrar</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>                                    
                <div class="modal fade" id="modalDetalleDiagnostico" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <form  class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="form-group col-md-12">
                                            <label>Diagnóstico</label>
                                            <textarea type="text" class="form-control" placeholder="Describa que solución dio al problema de manera breve" name="areadiagnostico1" id="areadiagnostico1" required="" readonly></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha registro</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfechadiagnostico" id="txtfechadiagnostico" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
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
        <script src="fun_js/formulario_actividades.js" type="text/javascript"></script>

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

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/formulario_soporte.js" type="text/javascript"></script>
        <script src="fun_js/funciones_soporte.js" type="text/javascript"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@3.0.0/dist/chart.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
        <script>
            $("#table-x").dataTable({
                "ordering": false,
                "order": [ 0, 'desc' ],
                "columnDefs": [
                  { "sortable": false, "targets": [0,4] }
                ],"pageLength": 25,
                dom: 'Bfrtip',
                buttons: [
                    {
                      extend: 'copy',
                      text: 'Copiar <i class="fas fa-copy"></i>'
                    },
                    {
                      extend: 'excel',
                      text: 'Exportar a Excel <i class="fas fa-file-excel"></i>'
                    },
                    {
                      extend: 'pdf',
                      text: 'Exportar a PDF <i class="far fa-file-pdf"></i>'
                    }
                    ,{
                      extend: 'print',
                      text: 'Imprimir <i class="fas fa-print"></i>'
                    }
                  ],
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
            });
            new Chart(document.getElementById("canvas-x"), {
                type: 'bar',
                data: {
                  labels: ["Nuevos", "Atendiendo", "Atendidos"],
                  datasets: [
                    {
                      label: "Soportes",
                      backgroundColor: ["#FFF26D", "#6FCDF8","#B9FF7F"],
                      data: [<%=enlace.totalSolicitudesEstadoUsuarioFecha(informacion.getId_usuario(),1,fecha_inicio,fecha_fin)%>,<%=enlace.totalSolicitudesEstadoUsuarioFecha(informacion.getId_usuario(),2,fecha_inicio,fecha_fin)%>,<%=enlace.totalSolicitudesEstadoUsuarioFecha(informacion.getId_usuario(),3,fecha_inicio,fecha_fin)%>]
                    }
                  ]
                },
                options: {
                  plugins: {
                    legend: {
                      display: false
                    }
                  },
                  title: {
                    display: false,
                    text: 'Resumen de solicitudes'
                  }
                },
                plugins: [ChartDataLabels]
            });
        </script>
    </body>
</html>
