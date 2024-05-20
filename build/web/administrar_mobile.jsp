<%@page import="mobile.EstadoSolicitud"%>
<%@page import="mobile.ArchivoSolicitud"%>
<%@page import="mobile.MySql"%>
<%@page import="mobile.Solicitud"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.componente"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.actividad"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    MySql mysql = new MySql();
    int id = 0, tipo = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    ArrayList<Solicitud> listadoSolicitudesNuevas = new ArrayList<>(),
            listadoMisSolicitudes = new ArrayList<>(),
            listadoMisSolicitudesAtendidas = new ArrayList<>(),
            listadoMisSolicitudesRechazadas = new ArrayList<>();
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        if (!(enlace.verificarUsuarioCumpleRol(id, "administrador") || enlace.verificarUsuarioCumpleRol(id, "denuncias"))) {
            throw new Exception("Rol no habilitado");
        }
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        if (request.getParameter("tipo") != null) {
            tipo = Integer.parseInt(request.getParameter("tipo"));
        }
        listadoSolicitudesNuevas = tipo == 0 ? (fecha_inicio == null ? mysql.getSolicitudes(1) : mysql.getSolicitudes(1, fecha_inicio, fecha_fin)) : (fecha_inicio == null ? mysql.getSolicitudes(1, tipo) : mysql.getSolicitudes(1, tipo, fecha_inicio, fecha_fin));
        listadoMisSolicitudes = tipo == 0 ? (fecha_inicio == null ? mysql.getMisSolicitudes(id, 2) : mysql.getMisSolicitudes(id, 2, fecha_inicio, fecha_fin)) : (fecha_inicio == null ? mysql.getMisSolicitudes(id, 2, tipo) : mysql.getMisSolicitudes(id, 2, tipo, fecha_inicio, fecha_fin));
        listadoMisSolicitudesAtendidas = tipo == 0 ? (fecha_inicio == null ? mysql.getMisSolicitudes(id, 3) : mysql.getMisSolicitudes(id, 3, fecha_inicio, fecha_fin)) : (fecha_inicio == null ? mysql.getMisSolicitudes(id, 3, tipo) : mysql.getMisSolicitudes(id, 3, tipo, fecha_inicio, fecha_fin));
        listadoMisSolicitudesRechazadas = tipo == 0 ? (fecha_inicio == null ? mysql.getMisSolicitudes(id, 4) : mysql.getMisSolicitudes(id, 4, fecha_inicio, fecha_fin)) : (fecha_inicio == null ? mysql.getMisSolicitudes(id, 4, tipo) : mysql.getMisSolicitudes(id, 4, tipo, fecha_inicio, fecha_fin));
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
        <title>Intranet Alcaldía - Administración Aplicación Móvil</title>
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
                            <h1>Administración del Buzón Ciudadano</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form action="">
                                                <div class="row">
                                                    <div class="col-3">
                                                        <label>Tipo</label>
                                                        <select class="form-control select2" name="tipo" id="tipo" required="">
                                                            <option value="0" <%=tipo == 0 ? "selected" : ""%>>Todas</option>
                                                            <option value="1" <%=tipo == 1 ? "selected" : ""%>>Sugerencias</option>
                                                            <option value="2" <%=tipo == 2 ? "selected" : ""%>>Denuncias</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-3">
                                                        <label>Fecha inicio</label>
                                                        <%if (fecha_inicio != null) {%>
                                                        <input type="text" class="form-control datepicker" name="txtini" id="txtini" value="<%= fecha_inicio%>"/>
                                                        <%} else {%>
                                                        <input type="text" class="form-control datepicker" name="txtini" id="txtini"/>
                                                        <%}%>
                                                    </div>
                                                    <div class="col-3">
                                                        <label>Fecha fin</label>
                                                        <%if (fecha_fin != null) {%>
                                                        <input type="text" class="form-control datepicker" name="txtfin" id="txtfin" value="<%= fecha_fin%>"/>
                                                        <%} else {%>
                                                        <input type="text" class="form-control datepicker" name="txtfin" id="txtfin"/>
                                                        <%}%>
                                                    </div>
                                                    <div class="col-3">
                                                        <label>Acciones</label></br>
                                                        <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango</a>
                                                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div> 
                                </div>
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-exclamation"></i> Nuevas <%if (listadoSolicitudesNuevas.size() != 0) {%><span class="badge badge-primary"><%= listadoSolicitudesNuevas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="second-tab" data-toggle="tab" href="#second" role="tab" aria-controls="second" aria-selected="false"><i class="fas fa-eye"></i> En revisión <%if (listadoMisSolicitudes.size() != 0) {%><span class="badge badge-primary"><%= listadoMisSolicitudes.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="third-tab" data-toggle="tab" href="#third" role="tab" aria-controls="third" aria-selected="false"><i class="fas fa-check"></i> Atendidas <%if (listadoMisSolicitudesAtendidas.size() != 0) {%><span class="badge badge-primary"><%= listadoMisSolicitudesAtendidas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="fourth-tab" data-toggle="tab" href="#fourth" role="tab" aria-controls="fourth" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoMisSolicitudesRechazadas.size() != 0) {%><span class="badge badge-primary"><%= listadoMisSolicitudesRechazadas.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Tipo</th>
                                                                    <th style="width: 10%">Cédula</th>
                                                                    <th style="width: 30%">Ciudadano</th>
                                                                    <th style="width: 10%">Celular</th>
                                                                    <th style="width: 15%">Archivos</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Solicitud s : listadoSolicitudesNuevas) {%>
                                                                <tr>
                                                                    <td><%= s.getId()%></td>
                                                                    <td><%= s.getCreacion()%></td>
                                                                    <td><%= s.getTipoDes()%></td>
                                                                    <td><%= s.getCedula()%></td>
                                                                    <td><%= s.getNombre()%></td>
                                                                    <td><%= s.getCelular()%></td>
                                                                    <td>
                                                                        <%int i = 1;
                                                                            for (ArchivoSolicitud a : s.getArchivos()) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getPath()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar archivo <%= i%>"></i></a>                                                                        
                                                                            <%i++;
                                                                                }%>
                                                                    </td>
                                                                    <td>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalle" data-id="<%= s.getId()%>" data-fecha="<%= s.getCreacion()%>" data-tipo="<%= s.getTipoDes()%>" data-est="<%= s.getEstado().getNombre()%>" data-cat="<%= s.getCategoria().getNombre()%>" data-sub="<%= s.getSubcategoria().getNombre()%>" data-ciu="<%= s.getNombre()%>" data-ced="<%= s.getCedula()%>" data-mail="<%= s.getCorreo()%>" data-cel="<%= s.getCelular()%>" data-dir="<%= s.getDireccion()%>" data-des="<%= s.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a type="button" onclick="gestionar(<%= s.getId()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Gestionar"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade show" id="second" role="tabpanel" aria-labelledby="second-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xx">
                                                            <thead>
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Tipo</th>
                                                                    <th style="width: 10%">Cédula</th>
                                                                    <th style="width: 30%">Ciudadano</th>
                                                                    <th style="width: 10%">Celular</th>
                                                                    <th style="width: 15%">Archivos</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Solicitud s : listadoMisSolicitudes) {%>
                                                                <tr>
                                                                    <td><%= s.getId()%></td>
                                                                    <td><%= s.getCreacion()%></td>
                                                                    <td><%= s.getTipoDes()%></td>
                                                                    <td><%= s.getCedula()%></td>
                                                                    <td><%= s.getNombre()%></td>
                                                                    <td><%= s.getCelular()%></td>
                                                                    <td>                                                                    
                                                                        <%int i = 1;
                                                                            for (ArchivoSolicitud a : s.getArchivos()) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getPath()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar archivo <%= i%>"></i></a>                                                                        
                                                                            <%i++;
                                                                                }%>
                                                                    </td>
                                                                    <td>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalle" data-id="<%= s.getId()%>" data-fecha="<%= s.getCreacion()%>" data-tipo="<%= s.getTipoDes()%>" data-est="<%= s.getEstado().getNombre()%>" data-cat="<%= s.getCategoria().getNombre()%>" data-sub="<%= s.getSubcategoria().getNombre()%>" data-ciu="<%= s.getNombre()%>" data-ced="<%= s.getCedula()%>" data-mail="<%= s.getCorreo()%>" data-cel="<%= s.getCelular()%>" data-dir="<%= s.getDireccion()%>" data-des="<%= s.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAtencion" data-id="<%= s.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Atender"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazo" data-id="<%= s.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                                        <a type="button" onclick="devolver(<%= s.getId()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-undo" data-toggle="tooltip" data-original-title="Devolver"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade show" id="third" role="tabpanel" aria-labelledby="third-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xxx">
                                                            <thead>
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Tipo</th>
                                                                    <th style="width: 10%">Cédula</th>
                                                                    <th style="width: 30%">Ciudadano</th>
                                                                    <th style="width: 10%">Celular</th>
                                                                    <th style="width: 15%">Archivos</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Solicitud s : listadoMisSolicitudesAtendidas) {%>
                                                                <tr>
                                                                    <td><%= s.getId()%></td>
                                                                    <td><%= s.getCreacion()%></td>
                                                                    <td><%= s.getTipoDes()%></td>
                                                                    <td><%= s.getCedula()%></td>
                                                                    <td><%= s.getNombre()%></td>
                                                                    <td><%= s.getCelular()%></td>
                                                                    <td>
                                                                        <%int i = 1;
                                                                            for (ArchivoSolicitud a : s.getArchivos()) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getPath()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar archivo <%= i%>"></i></a>                                                                        
                                                                            <%i++;
                                                                                }%>
                                                                    </td>
                                                                    <td>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalle" data-id="<%= s.getId()%>" data-fecha="<%= s.getCreacion()%>" data-tipo="<%= s.getTipoDes()%>" data-est="<%= s.getEstado().getNombre()%>" data-cat="<%= s.getCategoria().getNombre()%>" data-sub="<%= s.getSubcategoria().getNombre()%>" data-ciu="<%= s.getNombre()%>" data-ced="<%= s.getCedula()%>" data-mail="<%= s.getCorreo()%>" data-cel="<%= s.getCelular()%>" data-dir="<%= s.getDireccion()%>" data-des="<%= s.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade show" id="fourth" role="tabpanel" aria-labelledby="fourth-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xxxx">
                                                            <thead>
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Tipo</th>
                                                                    <th style="width: 10%">Cédula</th>
                                                                    <th style="width: 30%">Ciudadano</th>
                                                                    <th style="width: 10%">Celular</th>
                                                                    <th style="width: 15%">Archivos</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Solicitud s : listadoMisSolicitudesRechazadas) {%>
                                                                <tr>
                                                                    <td><%= s.getId()%></td>
                                                                    <td><%= s.getCreacion()%></td>
                                                                    <td><%= s.getTipoDes()%></td>
                                                                    <td><%= s.getCedula()%></td>
                                                                    <td><%= s.getNombre()%></td>
                                                                    <td><%= s.getCelular()%></td>
                                                                    <td>
                                                                        <%int i = 1;
                                                                            for (ArchivoSolicitud a : s.getArchivos()) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getPath()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar archivo <%= i%>"></i></a>                                                                        
                                                                            <%i++;
                                                                                }%>
                                                                    </td>
                                                                    <td>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalle" data-id="<%= s.getId()%>" data-fecha="<%= s.getCreacion()%>" data-tipo="<%= s.getTipoDes()%>" data-est="<%= s.getEstado().getNombre()%>" data-cat="<%= s.getCategoria().getNombre()%>" data-sub="<%= s.getSubcategoria().getNombre()%>" data-ciu="<%= s.getNombre()%>" data-ced="<%= s.getCedula()%>" data-mail="<%= s.getCorreo()%>" data-cel="<%= s.getCelular()%>" data-dir="<%= s.getDireccion()%>" data-des="<%= s.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
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
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalle">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de solicitud
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form class="needs-validation" novalidate="">
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label>Código</label>
                                        <input class="form-control" id="id" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input class="form-control" id="fecha" readonly>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label>Tipo</label>
                                        <input class="form-control" id="tipo" readonly>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label>Estado</label>
                                        <input class="form-control" id="est" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Categoría</label>
                                        <input class="form-control" id="cat" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Subcategoría</label>
                                        <input class="form-control" id="sub" readonly>
                                    </div>
                                    <div class="form-group col-md-8">
                                        <label>Ciudadano</label>
                                        <input class="form-control" id="ciu" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cédula</label>
                                        <input class="form-control" id="ced" readonly>
                                    </div>
                                    <div class="form-group col-md-8">
                                        <label>Correo</label>
                                        <input class="form-control" id="mail" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Celular</label>
                                        <input class="form-control" id="cel" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Dirección</label>
                                        <textarea class="form-control" id="dir" readonly></textarea>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Descripción</label>
                                        <textarea class="form-control" id="des" readonly></textarea>
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalAtencion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Atender denuncia
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formAtencion" action="administrar_buzon.control?accion=observar" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" name="id" id="id">
                                        <input type="text" name="estado" id="estado" value="3">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observación</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve la atención efectuada" name="obs" id="obs" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha ingresado la observación
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" class="btn btn-primary" id="btnAtender">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRechazo">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Rechazar denuncia
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formRechazo" action="administrar_buzon.control?accion=observar" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" name="id" id="id">
                                        <input type="text" name="estado" id="estado" value="4">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observación</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve la razón por la que se rechaza la denuncia" name="obs" id="obs" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha ingresado la observación
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" class="btn btn-primary" id="btnRechazar">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <footer class="main-footer">
                <div class="footer-center">
                    Copyright &copy; <%= LocalDate.now().getYear()%> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
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
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/formulario_soporte.js" type="text/javascript"></script>
        <script src="fun_js/funciones_soporte.js" type="text/javascript"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@3.0.0/dist/chart.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>

        <script type="text/javascript">
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

        $("#table-x").dataTable({
            "ordering": false,
            "order": [0, 'desc'],
            "columnDefs": [
                {"sortable": false, "targets": [0, 4]}
            ], "pageLength": 25,
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
                , {
                    extend: 'print',
                    text: 'Imprimir <i class="fas fa-print"></i>'
                }
            ],
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
            "order": [0, 'desc'],
            "columnDefs": [
                {"sortable": false, "targets": [0, 4]}
            ], "pageLength": 25,
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
                , {
                    extend: 'print',
                    text: 'Imprimir <i class="fas fa-print"></i>'
                }
            ],
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
            "order": [0, 'desc'],
            "columnDefs": [
                {"sortable": false, "targets": [0, 4]}
            ], "pageLength": 25,
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
                , {
                    extend: 'print',
                    text: 'Imprimir <i class="fas fa-print"></i>'
                }
            ],
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
            "order": [0, 'desc'],
            "columnDefs": [
                {"sortable": false, "targets": [0, 4]}
            ], "pageLength": 25,
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
                , {
                    extend: 'print',
                    text: 'Imprimir <i class="fas fa-print"></i>'
                }
            ],
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

        function gestionar(id) {
            Swal.fire({
                title: '¿Desea gestionar esta solicitud?',
                icon: 'warning',
                buttonsStyling: false,
                showCancelButton: true,
                confirmButtonText: 'Sí, gestionar',
                cancelButtonText: 'No, cancelar',
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                }
            }).then((willDelete) => {
                if (willDelete.isConfirmed) {
                    Swal.fire({
                        title: 'Aceptando solicitud',
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: () => !Swal.isLoading(),
                        allowEscapeKey: () => !Swal.isLoading(),
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    })
                    $.post('administrar_buzon.control?accion=gestionar', {
                        id: id
                    }, function (responseText) {
                        if (responseText) {
                            Swal.fire({
                                title: "Solicitud aceptada",
                                icon: "success",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        } else {
                            Swal.fire({
                                title: "Error",
                                text: "No se aceptó la solicitud",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        }
                    }, ).fail(function () {
                        Swal.fire({
                            title: "Error crítico",
                            text: "No se aceptó la solicitud",
                            icon: "error",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "administrar_mobile.jsp";
                        });
                    });
                } else {
                    Swal.fire({
                        title: "Acción cancelada",
                        icon: "warning",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    });
                }
            });
        }

        function devolver(id) {
            Swal.fire({
                title: '¿Desea devolver esta solicitud?',
                icon: 'warning',
                buttonsStyling: false,
                showCancelButton: true,
                confirmButtonText: 'Sí, devolver',
                cancelButtonText: 'No, cancelar',
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                }
            }).then((willDelete) => {
                if (willDelete.isConfirmed) {
                    Swal.fire({
                        title: 'Devolviendo solicitud',
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: () => !Swal.isLoading(),
                        allowEscapeKey: () => !Swal.isLoading(),
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    })
                    $.post('administrar_buzon.control?accion=devolver', {
                        id: id
                    }, function (responseText) {
                        if (responseText) {
                            Swal.fire({
                                title: "Solicitud devuelta",
                                icon: "success",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        } else {
                            Swal.fire({
                                title: "Error",
                                text: "No se devolvió la solicitud",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        }
                    }, ).fail(function () {
                        Swal.fire({
                            title: "Error crítico",
                            text: "No se devolvió la solicitud",
                            icon: "error",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "administrar_mobile.jsp";
                        });
                    });
                } else {
                    Swal.fire({
                        title: "Acción cancelada",
                        icon: "warning",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    });
                }
            });
        }

        $('#modalDetalle').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #id').val(button.data('id'));
            modal.find('.modal-body #fecha').val(button.data('fecha'));
            modal.find('.modal-body #tipo').val(button.data('tipo'));
            modal.find('.modal-body #est').val(button.data('est'));
            modal.find('.modal-body #cat').val(button.data('cat'));
            modal.find('.modal-body #sub').val(button.data('sub'));
            modal.find('.modal-body #ciu').val(button.data('ciu'));
            modal.find('.modal-body #ced').val(button.data('ced'));
            modal.find('.modal-body #mail').val(button.data('mail'));
            modal.find('.modal-body #cel').val(button.data('cel'));
            modal.find('.modal-body #dir').val(button.data('dir'));
            modal.find('.modal-body #des').val(button.data('des'));
        })

        $('#modalAtencion').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var id = button.data('id');
            var modal = $(this);
            modal.find('.modal-body #id').val(id);
        })

        $('#modalRechazo').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var id = button.data('id');
            var modal = $(this);
            modal.find('.modal-body #id').val(id);
        })

        $(document).ready(function () {
            $('#formAtencion').submit(function (event) {
                document.getElementById('btnAtender').hidden = true;
                event.preventDefault();
                $.ajax({
                    url: $(this).attr('action'),
                    type: $(this).attr('method'),
                    data: new FormData(this),
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function () {
                        Swal.fire({
                            title: 'Atendiendo denuncia',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                    },
                    success: function (response) {
                        if (response) {
                            Swal.fire({
                                title: "Solicitud atendida",
                                icon: "success",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        } else {
                            Swal.fire({
                                title: "Error",
                                text: "No se atendió la denuncia",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        }
                    },
                    error: function () {
                        Swal.fire({
                            title: 'Error crítico',
                            text: 'No se atendió la denuncia',
                            icon: "error",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "administrar_mobile.jsp";
                        });
                    }
                });
                return false;
            });

            $('#formRechazo').submit(function (event) {
                document.getElementById('btnRechazar').hidden = true;
                event.preventDefault();
                $.ajax({
                    url: $(this).attr('action'),
                    type: $(this).attr('method'),
                    data: new FormData(this),
                    contentType: false,
                    cache: false,
                    processData: false,
                    beforeSend: function () {
                        Swal.fire({
                            title: 'Rechazando denuncia',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                    },
                    success: function (response) {
                        if (response) {
                            Swal.fire({
                                title: "Solicitud rechazada",
                                icon: "success",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        } else {
                            Swal.fire({
                                title: "Error",
                                text: "No se rechazó la denuncia",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "administrar_mobile.jsp";
                            });
                        }
                    },
                    error: function () {
                        Swal.fire({
                            title: 'Error crítico',
                            text: 'No se rechazó la denuncia',
                            icon: "error",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "administrar_mobile.jsp";
                        });
                    }
                });
                return false;
            });
        });
        </script>
    </body>
</html>
