<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.MotivoAnulacion"%>
<%@page import="modelo.UsuarioIESS"%>
<%@page import="modelo.AnulacionSolicitud"%>
<%@page import="modelo.ReportePermiso"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.RevisionHoras"%>
<%@page import="modelo.AprobacionHoras"%>
<%@page import="modelo.PeriodoVaca"%>
<%@page import="java.time.LocalDate"%>
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
    int id = 0, id_x = 0;
    int dias_disponibles = 0, habiles = 0, horas = 0, minutos = 0, fines = 0, disponibilidadMinutos = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String cargo_funcionario = null;
    String direccion_funcionario = null;
    informacion_usuario infor = null;
    ArrayList<motivo_permiso> listadoMotivos = null;
    ArrayList<permiso_horas> listadoAprobadas = null;
    ArrayList<permiso_horas> listadoAnuladas = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<PeriodoVaca> listadoVacas = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    boolean checkPendientes = true;
    ArrayList<ReportePermiso> listadoAuditoria = null;
    UsuarioIESS funcionario = null;
    ArrayList<MotivoAnulacion> motivos = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        id_x = Integer.parseInt(request.getParameter("per_hor_admin").toString());
        if (!(enlace.verificarUsuarioCumpleRol(id, "administrador") || enlace.verificarUsuarioCumpleRol(id, "encargado_permisos"))) {
            throw new Exception("Rol no habilitado");
        }
        funcionario = link.getUsuario(id_x);
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listadoMotivos = enlace.listadoMotivos();
        cargo_funcionario = link.consultarDenominacionUsuario(id_x);
        direccion_funcionario = link.consultarDireccionUsuario(id_x);
        infor = link.vacacionesUltimoPeriodoDisponibleUsuario(Integer.toString(id_x));
        listadoAprobadas = enlace.listadoPermisoHoraIESSUsuarioEstadoID(id_x, 2);
        listadoAnuladas = enlace.listadoPermisoHoraIESSUsuarioEstadoID(id_x, 4);
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoVacas = link.getPeriodosVacaUsuario(id_x);
        for (PeriodoVaca p : listadoVacas) {
            disponibilidadMinutos += p.getDiasHabDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp();
            dias_disponibles += p.getDiasDisp();
            habiles += p.getDiasHabDisp();
            horas += p.getHorasDisp();
            minutos += p.getMinDisp();
            fines += p.getDiasFinDisp();
        }
        checkPendientes = link.comprobarPendientes(listadoVacas);
        listadoAuditoria = link.getPermisos(id_x, "VACACIONES", false);
        motivos = enlace.listadoMotivosAnulacion();
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
        <title>Intranet Alcaldía - Permiso admin</title>
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
    </head>

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
                            <h1>Mis solicitudes de permiso - <%= funcionario.getNombres()%></h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevopermiso"> <i class="fas fa-plus"></i> Nueva solicitud</a>
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
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="anuladas-tab" data-toggle="tab" href="#anuladas" role="tab" aria-controls="anuladas" aria-selected="false"><i class="fas fa-exclamation-triangle"></i> Anuladas <%if (listadoAnuladas.size() != 0) {%><span class="badge badge-primary"><%= listadoAnuladas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="auditoria-tab" data-toggle="tab" href="#auditoria" role="tab" aria-controls="auditoria" aria-selected="false"><i class="fas fa-history"></i> Histórico <%if (listadoAuditoria.size() != 0) {%><span class="badge badge-primary"><%= listadoAuditoria.size()%></span><%}%></a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-x">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Motivo</th>
                                                        <th>Observación</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_horas aprobadas : listadoAprobadas) {
                                                            motivo_permiso mot = enlace.buscarMotivoId(aprobadas.getId_motivo());
                                                            AprobacionHoras elemen = enlace.obtenerAprobacionHorasID(aprobadas.getId_permiso());
                                                            usuario aprueba = enlace.buscar_usuarioID(elemen.getId_aprueba());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="active"><%= aprobadas.getId_permiso()%></a></td>
                                                        <td style="width: 10%"><%= aprobadas.getFecha()%></td>
                                                        <td style="width: 15%"><%= aprobadas.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= aprobadas.getTimestamp_fin()%></td>
                                                        <td style="width: 10%"><%= aprobadas.getId_tipo() == 1 ? aprobadas.getTiempo_solicita() : aprobadas.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 15%"><%= mot.getDescripcion()%></td>
                                                        <td style="width: 15%"><%= aprobadas.getObservacion()%></td>
                                                        <td style="width: 15%">
                                                            <%if (!aprobadas.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= aprobadas.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (aprobadas.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= aprobadas.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= aprobadas.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAnulacion" data-ipe="<%= aprobadas.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Anular"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="anuladas" role="tabpanel" aria-labelledby="anuladas-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-4">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Motivo</th>
                                                        <th>Observación</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_horas anul : listadoAnuladas) {
                                                            motivo_permiso mot = enlace.buscarMotivoId(anul.getId_motivo());
                                                            AnulacionSolicitud elemen = enlace.obtenerAnulacionSolicitudHoras(anul.getId_permiso());
                                                            usuario anula = enlace.buscar_usuarioID(elemen.getId_anula());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= anula.getApellido() + " " + anula.getNombre()%>" data-descri="<%= elemen.getMotivo()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="active"><%= anul.getId_permiso()%></a></td>
                                                        <td style="width: 10%"><%= anul.getFecha()%></td>
                                                        <td style="width: 15%"><%= anul.getTimestamp_inicio()%></td>
                                                        <td style="width: 15%"><%= anul.getTimestamp_fin()%></td>
                                                        <td style="width: 10%"><%= anul.getId_tipo() == 1 ? anul.getTiempo_solicita() : anul.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 15%"><%= mot.getDescripcion()%></td>
                                                        <td style="width: 15%"><%= anul.getObservacion()%></td>
                                                        <td style="width: 15%">
                                                            <%if (!anul.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= anul.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                                <%if (anul.getAsistencia() != null) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= anul.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                <%}%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_anulacion_h&id_permiso=<%= elemen.getId_solicitud()%>" class="btn btn-primary btn-sm active"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar justificativo"></i></a>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= anula.getApellido() + " " + anula.getNombre()%>" data-descri="<%= elemen.getMotivo()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= anul.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
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
                                    Solicitud de permiso
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <%if (link.consultarJefeUsuario(id_x).equals("DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO")) {%>
                            <h4 style="color: red">No existe jefe en la dirección que tiene asignada, acérquese a la Dirección de Administración de Talento Humano para solventar este inconveniente.</h4>
                            <%} else {%>
                            <form id="formhoras" action="administrar_permiso.control?accion=registro_horas_admin" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="txtadmin" id="txtadmin" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="txtidusuario" id="txtidusuario" value="<%= id_x%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="txtnombreusu" id="txtnombreusu" value="<%= funcionario.getNombres()%>">
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud *</label>
                                        <input type="text" class="form-control datepicker" placeholder="Ingrese fecha de solicitud" name="txtfechasolicitud" id="txtfechasolicitud" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtnombreservidor" id="txtnombreservidor" required readonly value="<%= funcionario.getNombres()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo *</label>
                                        <select class="form-control" onchange="seleccionMotivoPermisoHoras()" name="combomotivo" id="combomotivo" required>
                                            <option selected="" disabled="" value="">Seleccione motivo</option>
                                            <%for (motivo_permiso busq : listadoMotivos) {
                                                    if (busq.getId_motivo() != 11) {%>
                                            <option value="<%= busq.getId_motivo()%>" <%=busq.getId_motivo() == 1 && (infor == null || !checkPendientes || enlace.verificarPermisosVacaciones(id_x)) ? "disabled=" : ""%>><%= busq.getDescripcion()%></option>
                                            <%}
                                                }%>
                                        </select>
                                    </div>

                                    <div class="form-group col-md-4">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtcargoservidor" id="txtcargoservidor" required readonly value="<%= cargo_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ninguna información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtunidadservidor" id="txtunidadservidor" required readonly value="<%= direccion_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="txtmodalidadservidor" id="txtmodalidadservidor" required readonly value="<%= infor == null ? "NO ASIGNADA" : infor.getModalidad()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Tipo</label>
                                        <select class="form-control" onchange="seleccionTipoPermisoHoras()" name="combotipo" id="combotipo" required>
                                            <option disabled="" value="">Elija</option>
                                            <option selected value="1">Horas</option>
                                            <option value="2">Días</option>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias pendientes</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtdiaspendientes" id="txtdiaspendientes" required="" readonly value="<%= dias_disponibles%>" >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Disponibilidad en minutos</label>
                                        <input type="text" class="form-control" name="disponibilidadMinutos" id="disponibilidadMinutos" required="" readonly value="<%= disponibilidadMinutos%>" >
                                    </div>
                                    <div id="fecha_inicio" class="form-group col-md-2">
                                        <label>Desde *</label>
                                        <input type="text" class="form-control datepicker" onblur="hidGuardar()" placeholder="fecha inicio" name="txtinicio1" id="txtinicio1" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" id="hora_inicio">
                                        <label>Hora de inicio *</label>
                                        <input type="time" class="form-control" onchange="calculardiferencia()" placeholder="Hora inicio" name="txtinicio" id="txtinicio" required="" value="<%= enlace.hora_actual()%>">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" id="hora_fin">
                                        <label>Hora de fin *</label>
                                        <input type="time" class="form-control" onchange="calculardiferencia()" placeholder="Hora fin" name="txtfin" id="txtfin" required="">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4" id="tiempo_solicitado">
                                        <label>Tiempo solicitado</label>
                                        <input type="text" class="form-control" placeholder="Tiempo solicitado" name="txttiempo" id="txttiempo" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>                                    
                                    <div id="fecha_fin" class="form-group col-md-2" hidden="">
                                        <label>Hasta *</label>
                                        <input type="text" class="form-control datepicker" onblur="diaIngreso()" placeholder="Fecha fin" name="txtfin1" id="txtfin1" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="fecha_retorno" class="form-group col-md-2" hidden="">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="txtingreso" id="txtingreso" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="dia_solicitado" class="form-group col-md-2" hidden="">
                                        <label>Dias solicitados</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiassolicitados" id="txtdiassolicitados" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <label>Dias habiles</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiashabiles" id="txtdiashabiles" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <label>Dias no habiles</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiasnohabiles" id="txtdiasnohabiles" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Dias recargo</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiasrecargo" id="txtdiasrecargo" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>Dias descuento</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="txtdiasdescuento" id="txtdiasdescuento" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>   
                                    <div class="form-group col-md-2" hidden>
                                        <label>Horas</label>
                                        <input type="text" class="form-control" name="txthoras" id="txthoras" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <label>Minutos</label>
                                        <input type="text" class="form-control" name="txtminutos" id="txtminutos" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observaciones</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="areaobservacion" id="areaobservacion" maxlength="100"></textarea>
                                        <div class="invalid-feedback">
                                            No ha escrito ninguna observación
                                        </div>
                                    </div>
                                    <div id="divadju" class="form-group col-md-6" hidden>
                                        <label>Adjuntar <label id="lbladjunto"> </label> (tamaño máximo 2mb)</label>
                                        <input type="file" class="form-control" name="txtadjunto" id="txtadjunto" onchange="validateSize2()">
                                        <div class="invalid-feedback">
                                            Adjunto ningun archivo
                                        </div>
                                    </div>
                                    <div class="alert alert-info">
                                        <b>NOTA:</b> Todos los campos con (*) en este formulario son obligatorios, por favor complete correctamente el formulario
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" id="botonguardarsoli" value="Upload" class="btn btn-primary">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                            <%}%>
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalAnulacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de anulación
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formanulacion" action="administrar_permiso.control?accion=anular_hora" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtusu" id="txtusu" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" name="txtper" id="txtper">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de anulación</label>
                                        <select class="form-control" name="motivo" id="motivo" required>
                                            <%for (MotivoAnulacion mot : motivos) {%>
                                            <option <%= mot.getId() == 1 ? "selected" : ""%> value="<%=mot.getId()%>"><%=mot.getMotivo()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div id="divadju" class="form-group col-md-12">
                                        <label>Adjunto (obligatorio, PDF tamaño máximo 2MB)</label>
                                        <input type="file" class="form-control" name="archivo" id="archivo" onchange="validateFile()" required>
                                        <div class="invalid-feedback">
                                            No adjuntó ningún archivo
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button id="btnAnular" type="submit" value="Upload" class="btn btn-primary">Anular</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAnulacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de anulación
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
                                        <label>Funcionario que anula</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtanula" id="txtanula" required="" readonly >
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
                                            $('#modalDetalleAnulacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('rechaza');
                                                var descripcion = button.data('descri');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtanula').val(rechaza);
                                                modal.find('.modal-body #arearazon1').val(descripcion);
                                                modal.find('.modal-body #txtfecharechazo1').val(fecha);
                                            })

                                            $('#modalAnulacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtper').val(id_permiso);
                                            })

                                            $(document).ready(function () {
                                                $('#formhoras').submit(function (event) {
                                                    var btnGuardar = document.getElementById('botonguardarsoli');
                                                    btnGuardar.hidden = true;
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
                                                                title: 'Generando permiso',
                                                                text: '',
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
                                                                    title: "Registro completado",
                                                                    text: "",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
                                                                });
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
                                                                    location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                                location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
                                                            });
                                                        }
                                                    });
                                                    return false;
                                                });
                                            });

                                            $(document).ready(function () {
                                                $(document).ready(function () {
                                                    $('#formanulacion').submit(function (event) {
                                                        document.getElementById('btnAnular').hidden = true;
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
                                                                    title: 'Anulación en curso',
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
                                                                        title: 'Permiso anulado',
                                                                        text: response === 'x' ? 'Ingrese el nuevo permiso' : '',
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: 'Error',
                                                                        text: 'No se anuló la solicitud',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se anuló la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_horas_admin.jsp?per_hor_admin=" +<%= id_x%>;
                                                                });
                                                            }
                                                        });
                                                        return false;
                                                    });
                                                });
                                            });

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

                                            function seleccionMotivoPermisoHoras() {
                                                document.getElementById("botonguardarsoli").hidden = true;
                                                var id_motivo = $('#combomotivo').val();
                                                var x = document.getElementById("divadju");
                                                if (id_motivo != 1) {
                                                    x.hidden = false;
                                                    obtenerJustificanteMotivoID(parseInt(id_motivo));
                                                } else {
                                                    x.hidden = true;
                                                }
                                                seleccionTipoPermisoHoras();
                                            }

                                            function calculardiferencia() {
                                                var id_motivo = $('#combomotivo').val();
                                                if (id_motivo != null) {
                                                    var hora_inicio = $('#txtinicio').val();
                                                    var hora_final = $('#txtfin').val();
                                                    document.getElementById("botonguardarsoli").hidden = true;
                                                    if (hora_inicio == hora_final) {
                                                        iziToast.warning({
                                                            title: 'Rango de horas inválido',
                                                            message: 'La hora de inicio no puede ser igual a la hora de fin',
                                                            position: 'topRight',
                                                        });
                                                    } else if (hora_inicio > hora_final) {
                                                        iziToast.warning({
                                                            title: 'Rango de horas inválido',
                                                            message: 'La hora de inicio no puede ser mayor a la hora de fin',
                                                            position: 'topRight',
                                                        });
                                                    } else {
                                                        document.getElementById("botonguardarsoli").hidden = false;
                                                    }
                                                    // Expresión regular para comprobar formato
                                                    var formatohora = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/;
                                                    // Si algún valor no tiene formato correcto sale
                                                    if (!(hora_inicio.match(formatohora) && hora_final.match(formatohora))) {
                                                        return;
                                                    }
                                                    // Calcula los minutos de cada hora
                                                    var minutos_inicio = hora_inicio.split(':').reduce((p, c) => parseInt(p) * 60 + parseInt(c));
                                                    var minutos_final = hora_final.split(':').reduce((p, c) => parseInt(p) * 60 + parseInt(c));
                                                    // Si la hora final es anterior a la hora inicial sale
                                                    if (minutos_final < minutos_inicio)
                                                        return;
                                                    // Diferencia de minutos
                                                    var diferencia = minutos_final - minutos_inicio;
                                                    if( minutos_inicio <= 720 && minutos_final >= 840 ){
                                                        //Hora de salida menor o igual 12pm y retorno mayor o igual a 2pm, se debe restar 60 minutos al tiempo solicitado
                                                        diferencia -= 60;
                                                    }
                                                    // Cálculo de horas y minutos de la diferencia
                                                    var horas = Math.floor(diferencia / 60);
                                                    var minutos = diferencia % 60;
                                                    var disponibilidadMinutos = parseInt($('#disponibilidadMinutos').val());
                                                    if (id_motivo == 1 && disponibilidadMinutos < diferencia) {
                                                        iziToast.warning({
                                                            title: 'Disponibilidad insuficiente',
                                                            message: 'El tiempo solicitado supera su disponibilidad',
                                                            position: 'topRight',
                                                        });
                                                        document.getElementById("botonguardarsoli").hidden = true;
                                                    }
                                                    if ((horas == 7 && minutos == 0) || (horas < 7)) {
                                                        $('#txttiempo').val(horas + 'h y ' + (minutos < 10 ? '0' : '') + minutos + "m");
                                                        $('#txthoras').val(horas);
                                                        $('#txtminutos').val(minutos);
                                                        if( minutos_inicio <= 720 && minutos_final >= 840 ){
                                                            iziToast.info({
                                                                title: 'Reducción de tiempo solicitado',
                                                                message: 'Se restó 1 hora por concepto de almuerzo',
                                                                position: 'topRight',
                                                            });
                                                        }
                                                    } else {
                                                        iziToast.warning({
                                                            title: 'Tiempo excedido',
                                                            message: 'Se puede solicitar un máximo de 7 horas',
                                                            position: 'topRight',
                                                        });
                                                        document.getElementById("botonguardarsoli").hidden = true;
                                                    }
                                                } else {
                                                    Swal.fire({
                                                        title: "Seleccione el motivo por el que desea realizar su solicitud",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    })
                                                    document.getElementById("botonguardarsoli").hidden = true;
                                                }
                                            }

                                            function diaIngreso() {
                                                document.getElementById("botonguardarsoli").hidden = true;
                                                var id_motivo = $('#combomotivo').val();
                                                if (id_motivo != null) {
                                                    var inicio = $('#txtinicio1').val();
                                                    var fin = $('#txtfin1').val();
                                                    if (inicio > fin) {
                                                        Swal.fire({
                                                            title: "Fecha inválida",
                                                            text: "La fecha de inicio no puede ser mayor a la fecha de finalización",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                        document.getElementById("txtinicio1").value = fin;
                                                        document.getElementById("txtingreso").value = '';
                                                        document.getElementById("txtdiassolicitados").value = '';
                                                        document.getElementById("botonguardarsoli").hidden = true;
                                                    } else {
                                                        Swal.fire({
                                                            title: 'Calculando fecha de ingreso',
                                                            text: 'Por favor espere',
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
                                                } else {
                                                    Swal.fire({
                                                        title: "Sin motivo",
                                                        text: "Seleccione el motivo por el que desea realizar su solicitud",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                }
                                            }

                                            function diasSolicitadosRecargo() {
                                                var inicio = $('#txtinicio1').val();
                                                var fin = $('#txtfin1').val();
                                                var idusu = $('#txtidusuario').val();
                                                var id_motivo = $('#combomotivo').val();
                                                Swal.fire({
                                                    title: 'Calculando días solicitados',
                                                    text: 'Por favor espere',
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
                                                    idusu: idusu,
                                                    motivo: id_motivo
                                                }, function (responseText) {
                                                    if (responseText) {
                                                        var variables = responseText.split(",");
                                                        if (variables[3] == -1) {
                                                            Swal.fire({
                                                                title: "Días insuficientes",
                                                                text: "Los días solicitados sobrepasan sus días disponibles",
                                                                icon: "warning",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                            document.getElementById("txtdiassolicitados").value = "";
                                                            document.getElementById("txtdiasrecargo").value = "";
                                                            document.getElementById("botonguardarsoli").hidden = true;
                                                        } else if (parseInt(id_motivo) == 1 && parseInt(variables[0]) > 4) {
                                                            Swal.fire({
                                                                title: "Días excedidos",
                                                                text: "Para solicitar más de 4 días con cargo a vacaciones debe registrar una solicitud de vacaciones",
                                                                icon: "warning",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            });
                                                            document.getElementById("txtdiassolicitados").value = "";
                                                            document.getElementById("txtdiasrecargo").value = "";
                                                            document.getElementById("botonguardarsoli").hidden = true;
                                                        } else {
                                                            document.getElementById("txtdiassolicitados").value = variables[0];
                                                            document.getElementById("txtdiasnohabiles").value = variables[1];
                                                            document.getElementById("txtdiashabiles").value = variables[2];
                                                            document.getElementById("txtdiasrecargo").value = variables[3];
                                                            document.getElementById("txtdiasdescuento").value = variables[4];
                                                            document.getElementById("botonguardarsoli").hidden = false;
                                                            Swal.fire({
                                                                title: "Datos validados",
                                                                text: "",
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
                                                });
                                            }

                                            function alertarPend() {
                                                Swal.fire({
                                                    title: "Errores en sus vacaciones",
                                                    text: "Existen inconsistencias en sus registros de vacaciones. Acérquese a la Dirección de Administración de Talento Humano para solventar este inconveniente.",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                });
                                            }

                                            function alertarSolicitudes() {
                                                Swal.fire({
                                                    title: "Existencia de solicitudes pendientes o revisadas",
                                                    text: "Para generar nuevos permisos de vacaciones no deben existir solicitudes pendientes o revisadas.",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                });
                                            }

                                            function validateSize() {
                                                var input = document.getElementById("archivo");
                                                const fileSize = input.files[0].size / 1024 / 1024;
                                                if (fileSize > 2) {
                                                    Swal.fire({
                                                        title: "Mensaje",
                                                        text: "El archivo excede el tamaño máximo de 2 MB",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivo').val('');
                                                }
                                            }

                                            function validateSize2() {
                                                var input = document.getElementById("txtadjunto");
                                                const fileSize = input.files[0].size / 1024 / 1024;
                                                if (fileSize > 2) {
                                                    Swal.fire({
                                                        title: "Mensaje",
                                                        text: "El archivo excede el tamaño máximo de 2 MB",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#txtadjunto').val('');
                                                }
                                            }

                                            function mostrarInvalido() {
                                                Swal.fire({
                                                    title: "Permiso pendiente de validar",
                                                    text: "Los permisos de tipo calamidad/otros requieren de la validación previa de la Dirección de Administración de Talento Humano para ser generados.",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                });
                                            }

                                            $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('aprueba');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtaprueba').val(rechaza);
                                                modal.find('.modal-body #txtfechaaprueba').val(fecha);
                                            })

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

                                            function hidGuardar() {
                                                document.getElementById("botonguardarsoli").hidden = true;
                                            }

                                            function validateFile() {
                                                var input = document.getElementById("archivo");
                                                const fileSize = input.files[0].size / 1024 / 1024;
                                                var regex = new RegExp("(.*?)\.(pdf)$");
                                                if (!(regex.test(input.value.toLowerCase()))) {
                                                    Swal.fire({
                                                        title: "Formato incorrecto",
                                                        text: "El formato del archivo debe ser PDF",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivo').val('');
                                                } else if (fileSize > 2) {
                                                    Swal.fire({
                                                        title: "Mensaje",
                                                        text: "El archivo excede el tamaño máximo de 2 MB",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivo').val('');
                                                }
                                            }
    </script>
</body>
</html>