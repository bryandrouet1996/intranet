<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.AprobacionHoras"%>
<%@page import="modelo.RevisionHoras"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.HorasGeneral"%>
<%@page import="modelo.rechazo_solicitud"%>
<%@page import="modelo.motivo_permiso"%>
<%@page import="modelo.permiso_horas"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
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
    String codigo_funcion = null;
    ArrayList<HorasGeneral> listadoPendientes = null;
    ArrayList<HorasGeneral> listadoRevisadas = null;
    ArrayList<HorasGeneral> listadoRechazadas = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<HorasGeneral> listadoAprobadas = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoPendientes = enlace.listadoEnHorasDireccionEstadoID(codigo_funcion, 0);
        listadoRevisadas = enlace.listadoEnHorasDireccionEstadoID(codigo_funcion, 1);
        listadoAprobadas = enlace.listadoEnHorasDireccionEstadoID(codigo_funcion, 2);
        listadoRechazadas = enlace.listadoEnHorasDireccionEstadoID(codigo_funcion, 3);
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Administración de permisos</title>
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
                            <h1>Administración de solicitudes de permisos</h1>
                        </div>
                        <div class="card">
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-bars"></i> Pendientes <%if (listadoPendientes.size() != 0) {%><span class="badge badge-primary"><%= listadoPendientes.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-check"></i> Revisadas <%if (listadoRevisadas.size() != 0) {%><span class="badge badge-primary"><%= listadoRevisadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="aprobadas-tab" data-toggle="tab" href="#aprobadas" role="tab" aria-controls="aprobadas" aria-selected="false"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                    </li>                                    
                                    <li class="nav-item">
                                        <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoRechazadas.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazadas.size()%></span><%}%></a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-1">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Funcionario</th>
                                                        <th>Motivo</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (HorasGeneral pendientes : listadoPendientes) {
                                                            permiso_horas permiso = pendientes.getPermiso();
                                                            usuario us = pendientes.getFuncionario();
                                                            String nombre_usuario = us.getApellido() + " " + us.getNombre();
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= permiso.getId_permiso()%></td>
                                                        <td style="width: 10%"><%= permiso.getFecha()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                        <td style="width: 20%"><%= nombre_usuario%></td>
                                                        <td style="width: 10%"><%= pendientes.getMotivo()%></td>
                                                        <td style="width: 10%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 5%">
                                                            <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (permiso.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 10%">
                                                            <%if ((permiso.getId_motivo() == 2 || permiso.getId_motivo() == 6) && !permiso.isValido()) {%>
                                                            <a href="javascript:" type="button" onclick="mostrarInvalido()"  class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar documento"></i></a>
                                                            <a type="button" onclick="mostrarInvalido()" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Revisar"></i></a>
                                                                <%} else {%>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                            <a type="button" onclick="revisarSolicitudHoras(<%= permiso.getId_permiso()%>,<%= informacion.getId_usuario()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Revisar"></i></a>
                                                                <%}%>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazo" data-ipe="<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-2">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Funcionario</th>
                                                        <th>Motivo</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (HorasGeneral revisadas : listadoRevisadas) {
                                                            permiso_horas permiso = revisadas.getPermiso();
                                                            usuario us = revisadas.getFuncionario();
                                                            String nombre_usuario = us.getApellido() + " " + us.getNombre();
                                                            RevisionHoras elemen = enlace.obtenerRevisionHorasID(permiso.getId_permiso());
                                                            usuario revisa = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= permiso.getId_permiso()%></td>
                                                        <td style="width: 10%"><%= permiso.getFecha()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                        <td style="width: 20%"><%= nombre_usuario%></td>
                                                        <td style="width: 10%"><%= revisadas.getMotivo()%></td>
                                                        <td style="width: 10%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 5%">
                                                            <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (permiso.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 10%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="aprobadas" role="tabpanel" aria-labelledby="aprobadas-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-x">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Funcionario</th>
                                                        <th>Motivo</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (HorasGeneral ap : listadoAprobadas) {
                                                            permiso_horas permiso = ap.getPermiso();
                                                            usuario us = ap.getFuncionario();
                                                            String nombre_usuario = us.getApellido() + " " + us.getNombre();
                                                            AprobacionHoras elem = enlace.obtenerAprobacionHorasID(permiso.getId_permiso());
                                                            usuario aprueba = enlace.buscar_usuarioID(elem.getId_aprueba());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= permiso.getId_permiso()%></td>
                                                        <td style="width: 10%"><%= permiso.getFecha()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                        <td style="width: 20%"><%= nombre_usuario%></td>
                                                        <td style="width: 10%"><%= ap.getMotivo()%></td>
                                                        <td style="width: 10%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 5%">
                                                            <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (permiso.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 10%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elem.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-4">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Funcionario</th>
                                                        <th>Motivo</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (HorasGeneral rechazadas : listadoRechazadas) {
                                                            permiso_horas permiso = rechazadas.getPermiso();
                                                            usuario us = rechazadas.getFuncionario();
                                                            String nombre_usuario = us.getApellido() + " " + us.getNombre();
                                                            rechazo_solicitud elem = enlace.obtenerRechazoSolicitudHoras(permiso.getId_permiso());
                                                            usuario rechaza = enlace.buscar_usuarioID(elem.getId_rechaza());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= permiso.getId_permiso()%></td>
                                                        <td style="width: 10%"><%= permiso.getFecha()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                        <td style="width: 20%"><%= nombre_usuario%></td>
                                                        <td style="width: 10%"><%= rechazadas.getMotivo()%></td>
                                                        <td style="width: 10%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 5%">
                                                            <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (permiso.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 10%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getRazon()%>" data-fech="<%= elem.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
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
                    </section>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRechazo">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registar motivo de rechazo
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formrechazo" action="administrar_permiso.control?accion=rechazar_hora" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtiu1" id="txtiu1" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtidper1" id="txtidper1">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de rechazo</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" id="btnRechazar" value="Upload" class="btn btn-primary">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRechazo">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de rechazo
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
                                        <label>Funcionario que rechaza</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrechaza" id="txtrechaza" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharechazo1" id="txtfecharechazo1" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAprobacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de aprobación
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
                                        <label>Funcionario que aprueba</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtaprueba" id="txtaprueba" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfechaaprueba" id="txtfechaaprueba" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRevision">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de revisión
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
                                        <label>Funcionario que revisa</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrevisa" id="txtrevisa" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharevisa" id="txtfecharevisa" required="" readonly >
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
            <footer class="main-footer">
                <div class="footer-center">
                    Copyright &copy; <%= LocalDate.now().getYear()%> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
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
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Page Specific JS File -->
    <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>
    <script src="assets/js/page/modules-datatables.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/forms-advanced-forms.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-toastr.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-calendar.js"></script>
    <!-- Template JS File -->

    <script src="assets/js/scripts.js"></script>
    <script src="assets/js/custom.js"></script>
    <script src="fun_js/formulario_horas.js" type="text/javascript"></script>

    <script type="text/javascript">
                                                                $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                                                    var button = $(event.relatedTarget);
                                                                    var rechaza = button.data('aprueba');
                                                                    var fecha = button.data('fech');
                                                                    var modal = $(this);
                                                                    modal.find('.modal-body #txtaprueba').val(rechaza);
                                                                    modal.find('.modal-body #txtfechaaprueba').val(fecha);
                                                                })

                                                                $("#table-x").dataTable({
                                                                    "ordering": false,
                                                                    "order": [[0, 'asc']],
                                                                    "columnDefs": [
                                                                        {"sortable": false, "targets": [0]}
                                                                    ], "pageLength": 25,
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

                                                                $(document).ready(function () {
                                                                    $(document).ready(function () {
                                                                        $('#formrechazo').submit(function (event) {
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
                                                                                        title: 'Rechazando permiso',
                                                                                        text: 'Por favor espere',
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
                                                                                            text: "",
                                                                                            icon: "success",
                                                                                            buttonsStyling: false,
                                                                                            customClass: {
                                                                                                confirmButton: 'btn btn-success'
                                                                                            }
                                                                                        }).then(function () {
                                                                                            location.href = "horas_direccion.jsp";
                                                                                        });
                                                                                    } else {
                                                                                        Swal.fire({
                                                                                            title: "Error",
                                                                                            text: "No se rechazó la solicitud",
                                                                                            icon: "error",
                                                                                            buttonsStyling: false,
                                                                                            customClass: {
                                                                                                confirmButton: 'btn btn-success'
                                                                                            }
                                                                                        }).then(function () {
                                                                                            location.href = "horas_direccion.jsp";
                                                                                        });
                                                                                    }
                                                                                },
                                                                                error: function () {
                                                                                    Swal.fire({
                                                                                        title: 'Error crítico',
                                                                                        text: 'No se rechazó la solicitud',
                                                                                        icon: "error",
                                                                                        buttonsStyling: false,
                                                                                        customClass: {
                                                                                            confirmButton: 'btn btn-success'
                                                                                        }
                                                                                    }).then(function () {
                                                                                        location.href = "horas_direccion.jsp";
                                                                                    });
                                                                                }
                                                                            });
                                                                            return false;
                                                                        });
                                                                    });
                                                                });

                                                                function mostrarInvalido() {
                                                                    Swal.fire({
                                                                        title: "Permiso pendiente de validar",
                                                                        text: "Los permisos de tipo calamidad/otros requieren de la validación previa de la Dirección de Administración del Talento Humano para ser generados",
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    });
                                                                }

                                                                function revisarSolicitudHoras(id_solicitud, id_usuario) {
                                                                    Swal.fire({
                                                                        title: '¿Desea revisar esta solicitud?',
                                                                        icon: 'warning',
                                                                        buttonsStyling: false,
                                                                        showCancelButton: true,
                                                                        confirmButtonText: 'Sí, revisar',
                                                                        cancelButtonText: 'No, cancelar',
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success',
                                                                            cancelButton: 'btn btn-danger'
                                                                        }
                                                                    }).then((willDelete) => {
                                                                        if (willDelete.isConfirmed) {
                                                                            Swal.fire({
                                                                                title: 'Revisando solicitud',
                                                                                text: 'Por favor espere',
                                                                                timerProgressBar: true,
                                                                                showConfirmButton: false,
                                                                                allowOutsideClick: () => !Swal.isLoading(),
                                                                                allowEscapeKey: () => !Swal.isLoading(),
                                                                                didOpen: () => {
                                                                                    Swal.showLoading();
                                                                                }
                                                                            })
                                                                            $.post('administrar_permiso.control?accion=revisar_horas', {
                                                                                ipe: id_solicitud,
                                                                                iu: id_usuario
                                                                            }, function (responseText) {
                                                                                if (responseText) {
                                                                                    Swal.fire({
                                                                                        title: "Solicitud revisada",
                                                                                        icon: "success",
                                                                                        buttonsStyling: false,
                                                                                        customClass: {
                                                                                            confirmButton: 'btn btn-success'
                                                                                        }
                                                                                    }).then(function () {
                                                                                        location.href = "horas_direccion.jsp";
                                                                                    });
                                                                                } else {
                                                                                    Swal.fire({
                                                                                        title: "Error",
                                                                                        text: "No se revisó la solicitud",
                                                                                        icon: "error",
                                                                                        buttonsStyling: false,
                                                                                        customClass: {
                                                                                            confirmButton: 'btn btn-success'
                                                                                        }
                                                                                    }).then(function () {
                                                                                        location.href = "horas_direccion.jsp";
                                                                                    });
                                                                                }
                                                                            }, ).fail(function () {
                                                                                Swal.fire({
                                                                                    title: "Error crítico",
                                                                                    text: "No se aprobó la solicitud",
                                                                                    icon: "error",
                                                                                    buttonsStyling: false,
                                                                                    customClass: {
                                                                                        confirmButton: 'btn btn-success'
                                                                                    }
                                                                                }).then(function () {
                                                                                    location.href = "horas_direccion.jsp";
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

                                                                $('#modalDetalleRevision').on('show.bs.modal', function (event) {
                                                                    var button = $(event.relatedTarget);
                                                                    var revisa = button.data('revisa');
                                                                    var fecha = button.data('fech');
                                                                    var modal = $(this);
                                                                    modal.find('.modal-body #txtrevisa').val(revisa);
                                                                    modal.find('.modal-body #txtfecharevisa').val(fecha);
                                                                })

                                                                $('#modalDetalleRechazo').on('show.bs.modal', function (event) {
                                                                    var button = $(event.relatedTarget);
                                                                    var rechaza = button.data('rechaza');
                                                                    var descripcion = button.data('descri');
                                                                    var fecha = button.data('fech');
                                                                    var modal = $(this);
                                                                    modal.find('.modal-body #txtrechaza').val(rechaza);
                                                                    modal.find('.modal-body #arearazon1').val(descripcion);
                                                                    modal.find('.modal-body #txtfecharechazo1').val(fecha);
                                                                })
    </script>
</body>
</html>