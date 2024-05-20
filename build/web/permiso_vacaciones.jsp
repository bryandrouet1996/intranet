<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.ReportePermiso"%>
<%@page import="modelo.PeriodoVaca"%>
<%@page import="modelo.revision_vacaciones"%>
<%@page import="modelo.aprobacion_vacaciones"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.rechazo_vacaciones"%>
<%@page import="modelo.permiso_vacaciones"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="modelo.informacion_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.motivo_vacaciones"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    conexion_oracle link = new conexion_oracle();
    int id = 0;
    int dias_disponibles = 0, habiles = 0, horas = 0, minutos = 0, fines = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String codigo_funcion = null;
    String cargo_funcionario = null;
    String direccion_funcionario = null;
    informacion_usuario infor = null;
    java.sql.Date fecha_ingreso = null;
    ArrayList<permiso_vacaciones> listadoPendientes = null;
    ArrayList<permiso_vacaciones> listadoRevisadas = null;
    ArrayList<permiso_vacaciones> listadoAprobadas = null;
    ArrayList<permiso_vacaciones> listadoRechazadas = null;
    ArrayList<motivo_vacaciones> listadoMotivos = null;
    ArrayList<PeriodoVaca> listadoVacas = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    boolean checkPendientes = true;
    ArrayList<ReportePermiso> listadoAuditoria = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoMotivos = enlace.listadoMotivosVacaciones();
        cargo_funcionario = enlace.buscarCargoUsuarioCodigo(informacion.getCodigo_cargo());
        direccion_funcionario = enlace.direccionPerteneceUsuario(codigo_funcion);
        listadoPendientes = enlace.listadoVacacionesUsuario(informacion.getId_usuario(), 0);
        listadoRevisadas = enlace.listadoVacacionesUsuario(informacion.getId_usuario(), 1);
        listadoAprobadas = enlace.listadoVacacionesUsuario(informacion.getId_usuario(), 2);
        listadoRechazadas = enlace.listadoVacacionesUsuario(informacion.getId_usuario(), 3);
        listadoVacas = link.getPeriodosVacaUsuario(Integer.parseInt(informacion.getCodigo_usuario()));
        for (PeriodoVaca p : listadoVacas) {
            dias_disponibles += p.getDiasDisp();
            habiles += p.getDiasHabDisp();
            horas += p.getHorasDisp();
            minutos += p.getMinDisp();
            fines += p.getDiasFinDisp();
        }
        checkPendientes = link.comprobarPendientes(listadoVacas);
        infor = link.vacacionesUltimoPeriodoDisponibleUsuario(informacion.getCodigo_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoAuditoria = link.getPermisos(Integer.parseInt(informacion.getCodigo_usuario()), "VACACIONES", true);
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
        <title>Intranet Alcaldía - Vacaciones</title>
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
                        </li>

                        </ul>
                    </aside>
                </div>

                <!-- Main Content -->
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Mis solicitudes de vacaciones</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <%if (!checkPendientes) {%>
                                    <a onclick="alertarPend()" class="btn btn-primary active" type="button"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                    <%} else if (infor != null) {
                                        if (infor.getModalidad().equals("LOSEP") && dias_disponibles <= 60 || infor.getModalidad().equals("CODIGO DEL TRABAJO") && dias_disponibles <= 90) {
                                            if (enlace.verificarPermisosVacaciones(id)) {%>
                                    <a onclick="alertarSolicitudes()" class="btn btn-primary active" type="button"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                    <%} else {%>
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevopermiso"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                    <%}
                                        }
                                    } else {%>
                                    <a onclick="alertar()" class="btn btn-primary active" type="button"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                        <div class="alert alert-light alert-has-icon col-12">
                            <div class="alert-icon">
                                <i class="fas fa-lightbulb"></i>
                            </div>
                            <div class="alert-body">
                                <div class="alert-title">Total de días disponibles</div>
                                Para poder visualizar el detalle de la totalidad de sus días disponibles haga <a href="javascript:" data-toggle="modal" data-target="#modalDetalleDisponibilidad" class="active badge badge-info">click aquí</a>
                            </div>
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
                                        <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="reject-tab" data-toggle="tab" href="#reject" role="tab" aria-controls="reject" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoRechazadas.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="auditoria-tab" data-toggle="tab" href="#auditoria" role="tab" aria-controls="auditoria" aria-selected="false"><i class="fas fa-history"></i> Histórico <%if (listadoAuditoria.size() != 0) {%><span class="badge badge-primary"><%= listadoAuditoria.size()%></span><%}%></a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-1">
                                                <thead>                                 
                                                    <tr>
                                                        <th>Fecha</th>
                                                        <th>Fecha inicio</th>
                                                        <th>Fecha fin</th>
                                                        <th>Motivo</th>
                                                        <th>Dias solicitados</th>
                                                        <th>Fecha retorno</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_vacaciones pendientes : listadoPendientes) {
                                                            motivo_vacaciones da = enlace.obtenerMotivoID(pendientes.getId_motivo());
                                                            usuario us = enlace.buscar_usuarioID(pendientes.getId_usuario());
                                                            String nombre_usuario = us.getApellido() + " " + us.getNombre();
                                                            String cargo_usuario = enlace.buscarCargoUsuarioCodigo(us.getCodigo_cargo());
                                                            String codigo_func = enlace.obtenerCodigoFuncionUsuario(us.getId_usuario());
                                                            String direccion_usuario = enlace.direccionPerteneceUsuario(codigo_func);
                                                    %>
                                                    <tr>
                                                        <td><%= pendientes.getFecha_solicitud()%></td>
                                                        <td><%= pendientes.getFecha_inicio()%></td>
                                                        <td><%= pendientes.getFecha_fin()%></td>
                                                        <td><%= da.getDescripcion()%></td>
                                                        <td><%= (int) pendientes.getDias_habiles() + (int) pendientes.getDias_recargo()%></td>
                                                        <td><%= pendientes.getFecha_ingreso()%></td>
                                                        <td>
                                                            <a onclick="verDetalle(<%= Math.round(pendientes.getDias_solicitados())%>,<%= Math.round(pendientes.getDias_descuento())%>)" href="javascript:" type="button" data-toggle="modal" data-target="#modaldetallepermiso" data-modalidad="<%= pendientes.getModalidad()%>" data-dirusu="<%= direccion_usuario%>" data-total="<%= pendientes.getDias_descuento()%>" data-cargo="<%= cargo_usuario%>" data-nombre="<%= nombre_usuario%>" data-feclab="<%= pendientes.getFecha_labor()%>" data-fecsoli="<%= pendientes.getFecha_solicitud()%>" data-periodo="<%= pendientes.getPeriodo()%>" data-motivo="<%= pendientes.getId_motivo()%>" data-fecini="<%= pendientes.getFecha_inicio()%>" data-fecfin="<%= pendientes.getFecha_fin()%>" data-fecretor="<%= pendientes.getFecha_ingreso()%>" data-diasoli="<%= (int) pendientes.getDias_habiles() + " día(s)"%>" data-recargo="<%= (int) pendientes.getDias_recargo() + " día(s)"%>" data-observacion="<%= pendientes.getObservacion()%>"  class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                <%if (pendientes.getDias_solicitados() > 4) {%>
                                                            <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= pendientes.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                <%} else {%>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= pendientes.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
                                                                <%}%>
                                                            <a onclick="eliminarPermiso(<%= pendientes.getId_permiso()%>)" class="btn btn-primary btn-sm active"><i class="fa fa-times" data-toggle="tooltip" data-original-title="Eliminar"></i></a>
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
                                                        <th>Fecha</th>
                                                        <th>Fecha inicio</th>
                                                        <th>Fecha fin</th>
                                                        <th>Motivo</th>
                                                        <th>Dias solicitados</th>
                                                        <th>Fecha retorno</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_vacaciones revisadas : listadoRevisadas) {
                                                            motivo_vacaciones da = enlace.obtenerMotivoID(revisadas.getId_motivo());
                                                            revision_vacaciones elemen = enlace.obtenerRevisionVacacionesID(revisadas.getId_permiso());
                                                            usuario revisa = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                    %>
                                                    <tr>
                                                        <td><%= revisadas.getFecha_solicitud()%></td>
                                                        <td><%= revisadas.getFecha_inicio()%></td>
                                                        <td><%= revisadas.getFecha_fin()%></td>
                                                        <td><%= da.getDescripcion()%></td>
                                                        <td><%= (int) revisadas.getDias_habiles() + (int) revisadas.getDias_recargo()%></td>
                                                        <td><%= revisadas.getFecha_ingreso()%></td>
                                                        <td>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                <%if (revisadas.getDias_solicitados() > 4) {%>
                                                            <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= revisadas.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                <%} else {%>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= revisadas.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
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
                                            <table class="table table-striped" id="table-3">
                                                <thead>                                 
                                                    <tr>
                                                        <th>Fecha</th>
                                                        <th>Fecha inicio</th>
                                                        <th>Fecha fin</th>
                                                        <th>Motivo</th>
                                                        <th>Dias solicitados</th>
                                                        <th>Fecha retorno</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_vacaciones aprobadas : listadoAprobadas) {
                                                            motivo_vacaciones da = enlace.obtenerMotivoID(aprobadas.getId_motivo());
                                                            aprobacion_vacaciones elemen = enlace.obtenerAprobacionVacacionesID(aprobadas.getId_permiso());
                                                            usuario aprueba = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                    %>
                                                    <tr>
                                                        <td><%= aprobadas.getFecha_solicitud()%></td>
                                                        <td><%= aprobadas.getFecha_inicio()%></td>
                                                        <td><%= aprobadas.getFecha_fin()%></td>
                                                        <td><%= da.getDescripcion()%></td>
                                                        <td><%= (int) aprobadas.getDias_habiles() + (int) aprobadas.getDias_recargo()%></td>
                                                        <td><%= aprobadas.getFecha_ingreso()%></td>
                                                        <td>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="reject" role="tabpanel" aria-labelledby="reject-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-4">
                                                <thead>                                 
                                                    <tr>
                                                        <th>Fecha</th>
                                                        <th>Fecha inicio</th>
                                                        <th>Fecha fin</th>
                                                        <th>Motivo</th>
                                                        <th>Dias solicitados</th>
                                                        <th>Fecha retorno</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_vacaciones rechazadas : listadoRechazadas) {
                                                            motivo_vacaciones da = enlace.obtenerMotivoID(rechazadas.getId_motivo());
                                                            rechazo_vacaciones elemen = enlace.obtenerRechazoSolicitudVacaciones(rechazadas.getId_permiso());
                                                            usuario rechaza = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                    %>
                                                    <tr>
                                                        <td><%= rechazadas.getFecha_solicitud()%></td>
                                                        <td><%= rechazadas.getFecha_inicio()%></td>
                                                        <td><%= rechazadas.getFecha_fin()%></td>
                                                        <td><%= da.getDescripcion()%></td>
                                                        <td><%= (int) rechazadas.getDias_habiles() + (int) rechazadas.getDias_recargo()%></td>
                                                        <td><%= rechazadas.getFecha_ingreso()%></td>
                                                        <td>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" data-descri="<%= elemen.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="auditoria" role="tabpanel" aria-labelledby="auditoria-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xx">
                                                <thead>
                                                    <tr>
                                                        <th style="width: 5%">Periodo</th>
                                                        <th style="width: 5%">Días</th>
                                                        <th style="width: 5%">Horas</th>
                                                        <th style="width: 5%">Minutos</th>
                                                        <th style="width: 10%">Inicio</th>
                                                        <th style="width: 10%">Fin</th>
                                                        <th style="width: 10%">Retorno</th>
                                                        <th style="width: 20%">Descripción</th>
                                                        <th style="width: 10%">Régimen</th>
                                                        <th style="width: 20%">Departamento</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (ReportePermiso rep : listadoAuditoria) {
                                                    %>
                                                    <tr>
                                                        <td><%= rep.getPeriodo()%></td>
                                                        <td><%= rep.getDias()%></td>
                                                        <td><%= rep.getHoras()%></td>
                                                        <td><%= rep.getMinutos()%></td>
                                                        <td><%= rep.getFechaInicio()%></td>
                                                        <td><%= rep.getFechaFin()%></td>
                                                        <td><%= rep.getFechaRetorno()%></td>
                                                        <td><%= rep.getDescripcion()%></td>
                                                        <td><%= rep.getRegimen()%></td>
                                                        <td><%= rep.getDepartamento()%></td>
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

            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleDisponibilidad">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Disponibilidad por período
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
                                    <div class="table-responsive">
                                        <table class="table table-striped">
                                            <thead>                                 
                                                <tr>
                                                    <th>Período</th>
                                                    <th>Fecha ingreso</th>
                                                    <th>Días hábiles</th>
                                                    <th>Horas</th>
                                                    <th>Minutos</th>
                                                    <th>Fines de semana</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    for (PeriodoVaca p : listadoVacas) {
                                                %>
                                                <tr>
                                                    <td><%= p.getPeriodo()%></td>
                                                    <td><%= p.getFechaIngreso()%></td>
                                                    <td><%= p.getDiasHabDisp()%></td>
                                                    <td><%= p.getHorasDisp()%></td>
                                                    <td><%= p.getMinDisp()%></td>
                                                    <td><%= p.getDiasFinDisp()%></td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <td><b>Total</b></td>
                                                    <td></td>
                                                    <td><b><%= habiles%></b></td>
                                                    <td><b><%= horas%></b></td>
                                                    <td><b><%= minutos%></b></td>
                                                    <td><b><%= fines%></b></td>
                                                </tr>
                                            </tfoot>
                                        </table>
                                        <div>Si existe alguna inconsistencia, por favor acérquese a la Dirección de Administración del Talento Humano</div>
                                    </div>
                                    <%if (infor != null) {
                                            if (infor.getModalidad().equals("LOSEP") && dias_disponibles > 60 || infor.getModalidad().equals("CODIGO DEL TRABAJO") && dias_disponibles > 90) {%>
                                    <p style="color: red">Existe un error en la disponibilidad de días, acérquese a la Dirección de Talento Humano</p>
                                    <%}
                                        }%>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalnuevopermiso">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Solicitud de vacaciones
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <%if (link.consultarJefeUsuario(Integer.parseInt(informacion.getCodigo_usuario())).equals("DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO")) {%>
                            <h4 style="color: red">No existe jefe en la dirección que tiene asignada, acérquese a la Dirección de Administración de Talento Humano para solventar este inconveniente.</h4>
                            <%} else {%>
                            <form id="formvacaciones" action="administrar_permiso.control?accion=registro_vacaciones" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" name="txtidusuario" id="txtidusuario" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" name="codusu" id="codusu" value="<%= informacion.getCodigo_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud *</label>
                                        <input type="text" class="form-control datepicker" placeholder="Ingrese fecha de solicitud" name="txtfechasolicitud" id="txtfechasolicitud" required="" onblur="calcularDiferenciaDias()">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtnombreservidor" id="txtnombreservidor" required readonly value="<%= informacion.getApellido()%> <%= informacion.getNombre()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo *</label>
                                        <select class="form-control" name="combomotivo" id="combomotivo" required>
                                            <option disabled="" value="0">Seleccione motivo</option>
                                            <%for (motivo_vacaciones busq : listadoMotivos) {%>
                                            <%if (busq.getId_motivo() == 1) {%>
                                            <option selected value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%} else {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha ing. labores</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="txtfechaingreso" id="txtfechaingreso" required="" value="<%= link.getFechaIngreso(Integer.parseInt(informacion.getCodigo_usuario()))%>" readonly >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtcargoservidor" id="txtcargoservidor" required readonly value="<%= cargo_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtunidadservidor" id="txtunidadservidor" required readonly value="<%= direccion_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="txtmodalidadservidor" id="txtmodalidadservidor" required value="<%= infor == null ? "NO ASIGNADA" : infor.getModalidad()%>" readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias pendientes</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtdiaspendientes" id="txtdiaspendientes" required="" readonly value="<%= dias_disponibles%>" >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Desde *</label>
                                        <input type="text" class="form-control datepicker" onblur="hidGuardar()" placeholder="fecha inicio" name="txtinicio" id="txtinicio" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Hasta *</label>
                                        <input type="text" class="form-control datepicker" onblur="calcularDiferenciaDiasInicio()" placeholder="Fecha fin" name="txtfin" id="txtfin" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="txtingreso" id="txtingreso" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Días solicitados</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiassolicitados" id="txtdiassolicitados" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Dias recargo</label>
                                        <input type="text" class="form-control" placeholder="Dias recargo" name="txtdiasrecargo" id="txtdiasrecargo" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Dias habiles</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiashabiles" id="txtdiashabiles" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Dias no habiles</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiasnohabiles" id="txtdiasnohabiles" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>                                        
                                    <div class="form-group col-md-2">
                                        <label>Días a descontar</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiasdescuento" id="txtdiasdescuento" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>    
                                    <div class="form-group col-md-12">
                                        <label>Observaciones *</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="areaobservacion" id="areaobservacion" required="" maxlength="100"></textarea>
                                        <div class="invalid-feedback">
                                            No ha escrito ninguna observación
                                        </div>
                                    </div>
                                    <div class="alert alert-info">
                                        <b>NOTA:</b> Todos los campos con (*) en este formulario son obligatorios, por favor complete correctamente el formulario
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" id="botonguardar" class="btn btn-primary">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                            <%}%>
                        </div>
                    </div>
                </div>
            </div>            
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modaldetallepermiso">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalles de solicitud
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
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="txtfechasolicitud1" id="txtfechasolicitud1" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtnombreservidor1" id="txtnombreservidor1" required readonly value="<%= informacion.getApellido()%> <%= informacion.getNombre()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo</label>
                                        <select class="form-control" name="combomotivo1" id="combomotivo1" required readonly>
                                            <option value="0">Seleccione motivo</option>
                                            <%for (motivo_vacaciones busq : listadoMotivos) {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha ing. labores</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="txtfechaingreso1" id="txtfechaingreso1" required="" readonly value="<%= fecha_ingreso%>">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtcargoservidor1" id="txtcargoservidor1" required readonly value="<%= cargo_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtunidadservidor1" id="txtunidadservidor1" required readonly value="<%= direccion_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="txtmodalidadservidor1" id="txtmodalidadservidor1" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Desde</label>
                                        <input type="text" class="form-control"  placeholder="fecha inicio" name="txtinicio1" id="txtinicio1" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Hasta</label>
                                        <input type="text" class="form-control" placeholder="Fecha fin" name="txtfin1" id="txtfin1" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="txtingreso1" id="txtingreso1" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias solicitados</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiassolicitados1" id="txtdiassolicitados1" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Recargo</label>
                                        <input type="text" class="form-control" placeholder="Recargo" name="txtrecargo" id="txtrecargo" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Total a descontar</label>
                                        <input type="text" class="form-control" placeholder="Total a descontar" name="txttotal" id="txttotal" required="" readonly >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observaciones</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="areaobservacion1" id="areaobservacion1" required="" readonly=""></textarea>
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
    <script src="fun_js/formulario_vacaciones.js" type="text/javascript"></script>
    <script type="text/javascript">
                                            $(document).ready(function () {
                                                $('#formvacaciones').submit(function (event) {
                                                    var btn = document.getElementById("botonguardar");
                                                    btn.hidden = true;
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
                                                                title: 'Registrando solicitud',
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
                                                                var respuesta = parseInt(response);
                                                                if (respuesta === 1) {
                                                                    Swal.fire({
                                                                        title: 'Registro exitoso',
                                                                        icon: 'success',
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_vacaciones.jsp";
                                                                    });
                                                                } else if (respuesta === -1) {
                                                                    Swal.fire({
                                                                        title: 'Error',
                                                                        text: 'No se pudo completar el registro',
                                                                        icon: 'error',
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_vacaciones.jsp";
                                                                    });
                                                                } else if (respuesta === -2) {
                                                                    Swal.fire({
                                                                        title: 'Error',
                                                                        text: 'Formulario incompleto',
                                                                        icon: 'error',
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    })
                                                                    btn.hidden = false;
                                                                }
                                                            } else {
                                                                Swal.fire({
                                                                    title: 'Error',
                                                                    text: 'No se pudo completar el registro',
                                                                    icon: 'error',
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_vacaciones.jsp";
                                                                });
                                                            }
                                                        },
                                                        error: function () {
                                                            Swal.fire({
                                                                title: 'Error crítico',
                                                                text: 'No se pudo completar el registro',
                                                                icon: 'error',
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "permiso_vacaciones.jsp";
                                                            });
                                                        }
                                                    });
                                                    return false;
                                                });
                                            });

                                            function eliminarPermiso(id_permiso) {
                                                Swal.fire({
                                                    title: '¿Desea eliminar este registro?',
                                                    text: "Una vez eliminado no se podrá recuperar",
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Sí, eliminar',
                                                    cancelButtonText: 'No, cancelar',
                                                    customClass: {
                                                        confirmButton: 'btn btn-success',
                                                        cancelButton: 'btn btn-danger'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        Swal.fire({
                                                            title: 'Eliminando permiso',
                                                            timerProgressBar: true,
                                                            showConfirmButton: false,
                                                            allowOutsideClick: () => !Swal.isLoading(),
                                                            allowEscapeKey: () => !Swal.isLoading(),
                                                            didOpen: () => {
                                                                Swal.showLoading();
                                                            }
                                                        })
                                                        $.post('administrar_permiso.control?accion=eliminar_vacacion', {
                                                            ipe: id_permiso
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                Swal.fire({
                                                                    title: "Permiso eliminado",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_vacaciones.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error",
                                                                    text: "No se pudo eliminar el permiso",
                                                                    icon: "warning",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_vacaciones.jsp";
                                                                });
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "No se eliminó la solicitud",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "permiso_vacaciones.jsp";
                                                            });
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Acción cancelada",
                                                            text: "No se eliminó la solicitud",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        })
                                                    }
                                                });
                                            }

                                            $("#table-xx").dataTable({
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

                                            function verDetalle(soli, total) {
                                                var mod = $('#modaldetallepermiso');
                                                var rec = soli - total;
                                                if (rec < 0) {
                                                    rec = 0;
                                                }
                                                mod.find('.modal-body #txtrecargo').val(rec + ' día(s)');
                                                mod.find('.modal-body #txttotal').val(total + ' día(s)');
                                            }

                                            function alertar() {
                                                Swal.fire({
                                                    title: "Sin vacaciones",
                                                    text: "No tienes días disponibles para solicitar vacaciones",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                })
                                            }

                                            function alertarPend() {
                                                Swal.fire({
                                                    title: "Errores en sus vacaciones",
                                                    text: "Existen inconsistencias en sus registros de vacaciones. Acérquese a la Dirección de Administración del Talento Humano para solventar este inconveniente",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                })
                                            }

                                            function alertarSolicitudes() {
                                                Swal.fire({
                                                    title: "Existencia de solicitudes pendientes o revisadas",
                                                    text: "Para generar nuevos permisos de vacaciones no deben existir solicitudes pendientes o revisadas",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                })
                                            }

                                            $('#modaldetallepermiso').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var fecha_solicitud = button.data('fecsoli');
                                                var fecha_laboral = button.data('feclab');
                                                var periodo = button.data('periodo');
                                                var motivo = button.data('motivo');
                                                var cargo = button.data('cargo');
                                                var nombre = button.data('nombre');
                                                var modalidad = button.data('modalidad');
                                                var fecha_inicio = button.data('fecini');
                                                var fecha_fin = button.data('fecfin');
                                                var fecha_retorno = button.data('fecretor');
                                                var dias_solicitados = button.data('diasoli');
                                                var recargo = button.data('recargo');
                                                var direccion_pertenece = button.data('dirusu');
                                                var observacion = button.data('observacion');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtnombreservidor1').val(nombre);
                                                modal.find('.modal-body #txtunidadservidor1').val(direccion_pertenece);
                                                modal.find('.modal-body #txtcargoservidor1').val(cargo);
                                                modal.find('.modal-body #txtfechaingreso1').val(fecha_laboral);
                                                modal.find('.modal-body #txtfechasolicitud1').val(fecha_solicitud);
                                                modal.find('.modal-body #combomotivo1').val(motivo);
                                                modal.find('.modal-body #txtperiodo1').val(periodo);
                                                modal.find('.modal-body #txtmodalidadservidor1').val(modalidad);
                                                modal.find('.modal-body #txtdiassolicitados1').val(dias_solicitados);
                                                modal.find('.modal-body #txtrecargo').val(recargo);
                                                modal.find('.modal-body #txtinicio1').val(fecha_inicio);
                                                modal.find('.modal-body #txtingreso1').val(fecha_retorno);
                                                modal.find('.modal-body #txtfin1').val(fecha_fin);
                                                modal.find('.modal-body #areaobservacion1').val(observacion);
                                            })

                                            $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('aprueba');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtaprueba').val(rechaza);
                                                modal.find('.modal-body #txtfechaaprueba').val(fecha);
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

                                            $('#modalDetalleRevision').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var revisa = button.data('revisa');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtrevisa').val(revisa);
                                                modal.find('.modal-body #txtfecharevisa').val(fecha);
                                            })



                                            function calcularDiferenciaDias() {
                                                document.getElementById("botonguardar").hidden = true;
                                                var fecha = $('#txtfechasolicitud').val();
                                                Swal.fire({
                                                    title: 'Calculando diferencia de días',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                $.post('administrar_permiso.control?accion=diferencia_dias', {
                                                    fecha: fecha
                                                }, function (responseText) {
                                                    if (parseInt(responseText) <= 0) {
                                                        Swal.fire({
                                                            title: "Fecha de solicitud válida",
                                                            icon: "success",
                                                            showConfirmButton: false,
                                                            timer: 1000
                                                        });
                                                        document.getElementById("botonguardar").hidden = false;
                                                    } else {
                                                        Swal.fire({
                                                            title: "Fecha de solicitud inválida",
                                                            text: "La fecha de solicitud no puede ser anterior a la fecha actual",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                        var curdate = new Date();
                                                        $('#txtfechasolicitud').val(curdate.getUTCFullYear() + '-' + (curdate.getUTCMonth() + 1).toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}) + '-' + curdate.getUTCDate().toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}));
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "No se calculó la diferencia de días",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    document.getElementById("botonguardar").hidden = false;
                                                });
                                            }

                                            function calcularDiferenciaDiasInicio() {
                                                document.getElementById("botonguardar").hidden = true;
                                                var fecha = $('#txtinicio').val();
                                                if (fecha) {
                                                    Swal.fire({
                                                        title: 'Calculando diferencia de días',
                                                        timerProgressBar: true,
                                                        showConfirmButton: false,
                                                        allowOutsideClick: () => !Swal.isLoading(),
                                                        allowEscapeKey: () => !Swal.isLoading(),
                                                        didOpen: () => {
                                                            Swal.showLoading();
                                                        }
                                                    })
                                                    $.post('administrar_permiso.control?accion=diferencia_dias', {
                                                        fecha: fecha
                                                    }, function (responseText) {
                                                        if (parseInt(responseText) > -8) {
                                                            Swal.fire({
                                                                title: "Fecha de inicio de vacaciones inválida",
                                                                text: "Las vacaciones deben solicitarse con al menos 8 días de anticipación",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                        } else if (parseInt(responseText) > 0) {
                                                            Swal.fire({
                                                                title: "Fecha de inicio inválida",
                                                                text: "La fecha de inicio no puede ser anterior a la fecha actual",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                        } else {
                                                            diaIngreso();
                                                        }
                                                    }, ).fail(function () {
                                                        Swal.fire({
                                                            title: "Error crítico",
                                                            text: "No se calculó la diferencia de días",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                        document.getElementById("botonguardar").hidden = false;
                                                    });
                                                }
                                            }

                                            function diasSolicitadosRecargo() {
                                                var inicio = $('#txtinicio').val();
                                                var fin = $('#txtfin').val();
                                                var codusu = $('#codusu').val();
                                                var motivo = $('#combomotivo').val();
                                                Swal.fire({
                                                    title: 'Calculando días solicitados',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                $.post('administrar_permiso.control?accion=dias_solicitados', {
                                                    txtinicio: inicio,
                                                    txtfin: fin,
                                                    idusu: codusu,
                                                    motivo: motivo
                                                }, function (responseText) {
                                                    if (responseText) {
                                                        var variables = responseText.split(",");
                                                        if (variables[3] == -1) {
                                                            Swal.fire({
                                                                title: "Disponibilidad insuficiente",
                                                                text: "Los días solicitados sobrepasan sus días disponibles",
                                                                icon: "warning",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                            document.getElementById("txtdiassolicitados").value = "";
                                                            document.getElementById("txtdiasrecargo").value = "";
                                                        } else if (parseInt(variables[0]) < 5) {
                                                            Swal.fire({
                                                                title: "Días solicitados insuficientes",
                                                                text: "Las solicitudes de vacaciones deben ser de al menos 5 días",
                                                                icon: "warning",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                            document.getElementById("txtdiassolicitados").value = "";
                                                            document.getElementById("txtdiasrecargo").value = "";
                                                            document.getElementById("txtingreso").value = "";
                                                        } else {
                                                            document.getElementById("txtdiassolicitados").value = variables[0];
                                                            document.getElementById("txtdiasnohabiles").value = variables[1];
                                                            document.getElementById("txtdiashabiles").value = variables[2];
                                                            document.getElementById("txtdiasrecargo").value = variables[3];
                                                            document.getElementById("txtdiasdescuento").value = parseInt(variables[4]);
                                                            document.getElementById("botonguardar").hidden = false;                                                
                                                            Swal.fire({
                                                                title: "Datos validados",
                                                                icon: "success",
                                                                showConfirmButton: false,
                                                                timer: 1000
                                                            });
                                                        }
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "No se calcularon los días solicitados",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    document.getElementById("botonguardar").hidden = false;
                                                });
                                            }

                                            function diaIngreso() {
                                                var inicio = $('#txtinicio').val();
                                                var fin = $('#txtfin').val();
                                                if (inicio > fin) {
                                                    Swal.fire({
                                                        title: "Fechas incorrectas",
                                                        text: "La fecha de inicio no puede ser mayor a la fecha de finalización",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    document.getElementById("txtinicio").value = fin;
                                                    document.getElementById("txtingreso").value = "";
                                                    document.getElementById("txtdiassolicitados").value = "";
                                                    document.getElementById("txtdiasrecargo").value = "";
                                                } else {
                                                    Swal.fire({
                                                        title: 'Calculando fecha de ingreso',
                                                        timerProgressBar: true,
                                                        showConfirmButton: false,
                                                        allowOutsideClick: () => !Swal.isLoading(),
                                                        allowEscapeKey: () => !Swal.isLoading(),
                                                        didOpen: () => {
                                                            Swal.showLoading();
                                                        }
                                                    })
                                                    $.post('administrar_permiso.control?accion=fecha_ingreso', {
                                                        txtinicio: inicio,
                                                        txtfin: fin
                                                    }, function (responseText) {
                                                        if (responseText) {
                                                            document.getElementById("txtingreso").value = responseText;
                                                            diasSolicitadosRecargo();
                                                        }
                                                    }, ).fail(function () {
                                                        Swal.fire({
                                                            title: "Error crítico",
                                                            text: "No se calculó la fecha de ingreso",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    });
                                                }
                                            }

                                            function hidGuardar() {
                                                document.getElementById("botonguardar").hidden = true;
                                            }
    </script>
</body>
</html>