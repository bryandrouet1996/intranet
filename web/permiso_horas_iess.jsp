<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.ReportePermiso"%>
<%@page import="modelo.UsuarioIESS"%>
<%@page import="modelo.MotivoAnulacion"%>
<%@page import="modelo.AnulacionSolicitud"%>
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
    int dias_disponibles = 0, habiles = 0, horas = 0, minutos = 0, fines = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String cargo_funcionario = null;
    String direccion_funcionario = null;
    informacion_usuario infor = null;
    ArrayList<motivo_permiso> listadoMotivos = null;
    ArrayList<permiso_horas> listadoAprobadas = new ArrayList<>(), listadoAnuladas = new ArrayList<>();
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<PeriodoVaca> listadoVacas = new ArrayList<>();
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<UsuarioIESS> listadoUsu = null;
    ArrayList<MotivoAnulacion> motivos = null;
    UsuarioIESS usu = new UsuarioIESS();
    ArrayList<ReportePermiso> listadoAuditoria = null;
    java.sql.Date fecha_inicio = null, fecha_fin = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        if (request.getParameter("per_hor_admin") != null) {
            id_x = Integer.parseInt(request.getParameter("per_hor_admin").toString());
        }
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        if (id_x != 0) {
            usu = link.getUsuario(id_x);
            cargo_funcionario = link.consultarDenominacionUsuario(id_x);
            direccion_funcionario = link.consultarDireccionUsuario(id_x);
            listadoMotivos = enlace.listadoMotivos();
            infor = link.vacacionesUltimoPeriodoDisponibleUsuario(Integer.toString(id_x));
            if (fecha_inicio != null) {
                listadoAprobadas = enlace.listadoPermisoHoraIESSUsuarioEstadoMotivo(id_x, 2, 11, fecha_inicio, fecha_fin);
                listadoAnuladas = enlace.listadoPermisoHoraIESSUsuarioEstadoMotivo(id_x, 4, 11, fecha_inicio, fecha_fin);
            } else {
                listadoAprobadas = enlace.listadoPermisoHoraIESSUsuarioEstadoMotivo(id_x, 2, 11);
                listadoAnuladas = enlace.listadoPermisoHoraIESSUsuarioEstadoMotivo(id_x, 4, 11);
            }
            listadoVacas = link.getPeriodosVacaUsuario(id_x);
            for (PeriodoVaca p : listadoVacas) {
                dias_disponibles += p.getDiasDisp();
                habiles += p.getDiasHabDisp();
                horas += p.getHorasDisp();
                minutos += p.getMinDisp();
                fines += p.getDiasFinDisp();
            }
            listadoAuditoria = link.getPermisos(id_x, "NOTIFICACIONES CORREO IESS", true);
        } else {
            if (fecha_inicio != null) {
                listadoAprobadas = enlace.listadoPermisoHoraEstadoMotivo(2, 11, fecha_inicio, fecha_fin);
                listadoAnuladas = enlace.listadoPermisoHoraEstadoMotivo(4, 11, fecha_inicio, fecha_fin);
            } else {
                listadoAprobadas = enlace.listadoPermisoHoraEstadoMotivo(2, 11);
                listadoAnuladas = enlace.listadoPermisoHoraEstadoMotivo(4, 11);
            }
        }
        listadoUsu = link.getUsuariosActivosIESS();
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
        <title>Intranet Alcaldía - Permiso notificación IESS</title>
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
                            <h1>Solicitudes notificación correo IESS<%= id_x != 0 ? " - " + usu.getNombres() + " (" + usu.getRegimen() + ")" : ""%></h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#adminInd"> <i class="fas fa-user"></i> Administración individual</a>
                                </div>
                                <%if (id_x != 0) {%>
                                <div class="flex-column activities">
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevopermiso"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                </div>
                                <%}%>
                            </div>
                        </div>
                        <%if (id_x != 0) {%>
                        <div class="alert alert-light alert-has-icon col-12">
                            <div class="alert-icon">
                                <i class="fas fa-lightbulb"></i>
                            </div>
                            <div class="alert-body">
                                <div class="alert-title">Total de días disponibles</div>
                                Para poder visualizar el detalle de la totalidad de sus días disponibles haga <a href="javascript:" data-toggle="modal" data-target="#modalDetalleDisponibilidad" class="active badge badge-info">click aquí</a>
                            </div>
                        </div>
                        <%}%>
                        <div class="card">
                            <div class="card-body">
                                <form action="">
                                    <input name="per_hor_admin" value="<%= id_x%>" hidden>
                                    <div class="row">
                                        <div class="col-2">
                                            <label>Fecha inicio</label>
                                            <%if (fecha_inicio != null) {%>
                                            <input type="text" class="form-control datepicker" name="txtini" id="txtini" value="<%= fecha_inicio%>"/>
                                            <%} else {%>
                                            <input type="text" class="form-control datepicker" name="txtini" id="txtini"/>
                                            <%}%>
                                        </div>
                                        <div class="col-2">
                                            <label>Fecha fin</label>
                                            <%if (fecha_fin != null) {%>
                                            <input type="text" class="form-control datepicker" name="txtfin" id="txtfin" value="<%= fecha_fin%>"/>
                                            <%} else {%>
                                            <input type="text" class="form-control datepicker" name="txtfin" id="txtfin"/>
                                            <%}%>
                                        </div>
                                        <div class="col-8">
                                            <label>Acciones</label></br>
                                            <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango</a>
                                            <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="anul-tab" data-toggle="tab" href="#anul" role="tab" aria-controls="anul" aria-selected="false"><i class="fas fa-exclamation-triangle"></i> Anuladas <%if (listadoAnuladas.size() != 0) {%><span class="badge badge-primary"><%= listadoAnuladas.size()%></span><%}%></a>
                                    </li>
                                    <%if (id_x != 0) {%>
                                    <li class="nav-item">
                                        <a class="nav-link" id="auditoria-tab" data-toggle="tab" href="#auditoria" role="tab" aria-controls="auditoria" aria-selected="false"><i class="fas fa-history"></i> Histórico <%if (listadoAuditoria.size() != 0) {%><span class="badge badge-primary"><%= listadoAuditoria.size()%></span><%}%></a>
                                    </li>
                                    <%}%>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xx">
                                                <thead>                                 
                                                    <tr>
                                                        <th style="width: 5%">ID</th>
                                                        <th style="width: 10%">Código</th>
                                                        <th style="width: 20%">Funcionario</th>
                                                        <th style="width: 10%">Cédula</th>
                                                        <th style="width: 20%">Dirección</th>
                                                        <th style="width: 10%">Inicio</th>
                                                        <th style="width: 10%">Fin</th>
                                                        <th style="width: 5%">Tiempo solicitado</th>
                                                        <th style="width: 10%">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_horas aprobadas : listadoAprobadas) {
                                                            AprobacionHoras elemen = enlace.obtenerAprobacionHorasID(aprobadas.getId_permiso());
                                                            usuario func = aprobadas.getId_usuario() == 0 ? null : enlace.buscar_usuarioID(aprobadas.getId_usuario());
                                                            UsuarioIESS func2 = func == null ? link.getUsuario(aprobadas.getCodigoUsu()) : null;
                                                            usuario aprueba = enlace.buscar_usuarioID(elemen.getId_aprueba());
                                                    %>
                                                    <tr>
                                                        <td><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="active"><%= aprobadas.getId_permiso()%></a></td>
                                                        <td><%= func == null ? func2.getCodigo(): func.getCodigo_usuario()%></td>
                                                        <td><%= func == null ? func2.getNombres() : func.getApellido() + " " + func.getNombre()%></td>
                                                        <td><%= func == null ? func2.getCedula(): func.getCedula()%></td>
                                                        <td><%= aprobadas.getDireccion()%></td>
                                                        <td><%= aprobadas.getId_tipo() == 1 ? aprobadas.getHora_salida() : aprobadas.getFecha_inicio()%></td>
                                                        <td><%= aprobadas.getId_tipo() == 1 ? aprobadas.getHora_entrada() : aprobadas.getFecha_fin()%></td>
                                                        <td><%= aprobadas.getId_tipo() == 1 ? aprobadas.getTiempo_solicita() : aprobadas.getDias_solicitados() + " día(s)"%></td>
                                                        <td>
                                                            <%if (!aprobadas.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= aprobadas.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAnulacion" data-ipe="<%= aprobadas.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Anular"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="anul" role="tabpanel" aria-labelledby="anul-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xxx">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Código</th>
                                                        <th>Funcionario</th>
                                                        <th>Dirección</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Tiempo solicitado</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (permiso_horas anulada : listadoAnuladas) {
                                                            AnulacionSolicitud elemen = enlace.obtenerAnulacionSolicitudHoras(anulada.getId_permiso());
                                                            usuario func = anulada.getId_usuario() == 0 ? null : enlace.buscar_usuarioID(anulada.getId_usuario());
                                                            UsuarioIESS func2 = func == null ? link.getUsuario(anulada.getCodigoUsu()) : null;
                                                            usuario anula = enlace.buscar_usuarioID(elemen.getId_anula());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= anula.getApellido() + " " + anula.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" data-descri="<%= elemen.getMotivo()%>" class="active"><%= anulada.getId_permiso()%></a></td>
                                                        <td style="width: 10%"><%= func == null ? func2.getCodigo(): func.getCodigo_usuario()%></td>
                                                        <td style="width: 25%"><%= func == null ? func2.getNombres() : func.getApellido() + " " + func.getNombre()%></td>
                                                        <td style="width: 25%"><%= anulada.getDireccion()%></td>
                                                        <td style="width: 10%"><%= anulada.getId_tipo() == 1 ? anulada.getHora_salida() : anulada.getFecha_inicio()%></td>
                                                        <td style="width: 10%"><%= anulada.getId_tipo() == 1 ? anulada.getHora_entrada() : anulada.getFecha_fin()%></td>
                                                        <td style="width: 5%"><%= anulada.getId_tipo() == 1 ? anulada.getTiempo_solicita() : anulada.getDias_solicitados() + " día(s)"%></td>
                                                        <td style="width: 10%">
                                                            <%if (!anulada.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                            <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= anulada.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= anula.getApellido() + " " + anula.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" data-descri="<%= elemen.getMotivo()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <%if (id_x != 0) {%>
                                    <div class="tab-pane fade" id="auditoria" role="tabpanel" aria-labelledby="auditoria-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-x">
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
                                    <%}%>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
            <%if (id_x != 0) {%>
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
                            <h4 style="color: red">No existe jefe en la dirección que tiene asignada, acérquese a la Dirección de Administración del Talento Humano para solventar este inconveniente.</h4>
                            <%} else {%>
                            <form id="formhoras" action="administrar_permiso.control?accion=registro_horas_iess" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="txtadmin" id="txtadmin" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <label>Id Usuario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="txtidusuario" id="txtidusuario" value="<%= id_x%>">
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
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtnombreservidor" id="txtnombreservidor" required readonly value="<%= usu.getNombres()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo *</label>
                                        <select class="form-control" name="combomotivo" id="combomotivo" required>
                                            <%for (motivo_permiso busq : listadoMotivos) {
                                                    if (busq.getId_motivo() == 11) {%>
                                            <option selected="" value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
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
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="txtmodalidadservidor" id="txtmodalidadservidor" required readonly value="<%= usu.getRegimen()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Tipo</label>
                                        <select class="form-control" onchange="seleccionTipoPermisoHoras()" name="combotipo" id="combotipo" required>
                                            <option selected value="2">Días</option>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias pendientes</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtdiaspendientes" id="txtdiaspendientes" required="" readonly value="<%= dias_disponibles%>" >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="fecha_inicio" class="form-group col-md-2">
                                        <label>Desde *</label>
                                        <input type="text" class="form-control datepicker" onblur="hidGuardar()" placeholder="fecha inicio" name="txtinicio1" id="txtinicio1" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="fecha_fin" class="form-group col-md-2">
                                        <label>Hasta *</label>
                                        <input type="text" class="form-control datepicker" onblur="diaIngreso()" placeholder="Fecha fin" name="txtfin1" id="txtfin1" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="fecha_retorno" class="form-group col-md-2">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="txtingreso" id="txtingreso" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div id="dia_solicitado" class="form-group col-md-2">
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
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="areaobservacion" id="areaobservacion"></textarea>
                                        <div class="invalid-feedback">
                                            No ha escrito ninguna observación
                                        </div>
                                    </div>
                                    <div id="divadju" class="form-group col-md-6">
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
            <%}%>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="adminInd">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Administración individual
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <div class="form-row">
                                <div class="form-group col-md-12">
                                    <label>Funcionario</label>
                                    <select style="width: 100%" class="form-control select2" name="listfun" id="listfun" required onchange="cambioFun()">
                                        <option selected disabled value="0">Seleccione</option>
                                        <%for (UsuarioIESS usuarioIESS : listadoUsu) {%>
                                        <option value="<%= usuarioIESS.getCodigo()%>"><%= usuarioIESS.getNombres() + " - " + usuarioIESS.getRegimen()%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button hidden id="btnFun" type="button" onclick="administrar()" class="btn btn-primary">Administrar permisos</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                            </div>
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
                                        <input type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" readonly>
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

                                            $(document).ready(function () {
                                                $('#formhoras').submit(function (event) {
                                                    var btn = document.getElementById("botonguardarsoli");
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
                                                                title: 'Registrando permiso',
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
                                                                    title: 'Registro exitoso',
                                                                    text: '',
                                                                    icon: 'success',
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                                    location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                                location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
                                                            });
                                                        }
                                                    });
                                                    return false;
                                                });
                                            });

                                            $("#table-x").dataTable({
                                                "ordering": false,
                                                "order": [[0, 'asc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [0]}
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
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
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
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
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
                                                        title: "Tamaño excedido",
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

                                            function diaIngreso() {
                                                document.getElementById("botonguardarsoli").hidden = true;
                                                var inicio = $('#txtinicio1').val();
                                                var fin = $('#txtfin1').val();
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
                                                    document.getElementById("txtinicio1").value = fin;
                                                    document.getElementById("txtingreso").value = '';
                                                    document.getElementById("txtdiassolicitados").value = '';
                                                    document.getElementById("botonguardarsoli").hidden = true;
                                                } else {
                                                    Swal.fire({
                                                        title: 'Calculando fecha de ingreso',
                                                        text: '',
                                                        timerProgressBar: true,
                                                        showConfirmButton: false,
                                                        allowOutsideClick: () => !Swal.isLoading(),
                                                        allowEscapeKey: () => !Swal.isLoading(),
                                                        didOpen: () => {
                                                            Swal.showLoading();
                                                        }
                                                    })
                                                    $.post('administrar_permiso.control?accion=fecha_ingreso_calendario', {
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
                                                    ;
                                                }
                                            }

                                            function diasSolicitadosRecargo() {
                                                var inicio = $('#txtinicio1').val();
                                                var fin = $('#txtfin1').val();
                                                var idusu = $('#txtidusuario').val();
                                                var id_motivo = $('#combomotivo').val();
                                                Swal.fire({
                                                    title: 'Calculando días solicitados',
                                                    text: '',
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
                                                            document.getElementById("botonguardar").hidden = true;
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

                                            function alertarSolicitudes() {
                                                Swal.fire({
                                                    title: "Existencia de solicitudes pendientes o revisadas",
                                                    text: "Para generar nuevos permisos de vacaciones no deben existir solicitudes pendientes o revisadas",
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
                                                        title: "Tamaño excedido",
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
                                                        title: "Tamaño excedido",
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
                                                                    title: 'Anulando permiso',
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
                                                                        title: "Permiso anulado",
                                                                        text: response === 'x' ? 'Ingrese el nuevo permiso' : '',
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: "Error",
                                                                        text: 'No se anuló la solicitud',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se pudo completar la anulación',
                                                                    icon: 'error',
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "permiso_horas_iess.jsp?per_hor_admin=" +<%= id_x%>;
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

                                            $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('aprueba');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtaprueba').val(rechaza);
                                                modal.find('.modal-body #txtfechaaprueba').val(fecha);
                                            })

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

                                            function cambioFun() {
                                                $('#btnFun').removeAttr('hidden')
                                            }

                                            function administrar() {
                                                location.href = "permiso_horas_iess.jsp?per_hor_admin=" + $('#listfun').val();
                                            }

                                            function hidGuardar() {
                                                document.getElementById("botonguardarsoli").hidden = true;
                                            }
    </script>
</body>
</html>