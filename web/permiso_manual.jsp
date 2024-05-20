<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.UsuarioIESS"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.AnulacionManual"%>
<%@page import="modelo.MotivoAnulacion"%>
<%@page import="modelo.PermisoManual"%>
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
    UsuarioIESS funcionario = null;
    foto_usuario foto = null;
    String cargo_funcionario = null;
    String direccion_funcionario = null;
    ArrayList<PermisoManual> listadoAprobadas = new ArrayList<>(), listadoAnuladas = new ArrayList<>();
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<UsuarioIESS> listadoUsu = null;
    ArrayList<MotivoAnulacion> motivos = null;
    ArrayList<PeriodoVaca> listadoVacas = null;
    informacion_usuario infor = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        if (request.getParameter("per_hor_admin") != null) {
            id_x = Integer.parseInt(request.getParameter("per_hor_admin").toString());
        }
        if (!(enlace.verificarUsuarioCumpleRol(id, "administrador") || enlace.verificarUsuarioCumpleRol(id, "encargado_permisos"))) {
            throw new Exception("Rol no habilitado");
        }
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        motivos = enlace.listadoMotivosAnulacion();
        if (id_x != 0) {
            funcionario = link.getUsuario(id_x);
            cargo_funcionario = link.consultarDenominacionUsuario(id_x);
            direccion_funcionario = link.consultarDireccionUsuario(id_x);
            listadoAprobadas = enlace.getPermisosManuales(0, id_x);
            listadoAnuladas = enlace.getPermisosManuales(4, id_x);
            listadoVacas = link.getPeriodosVacaUsuario(id_x);
            for (PeriodoVaca p : listadoVacas) {
                disponibilidadMinutos += p.getDiasHabDisp() * 8 * 60 + p.getHorasDisp() * 60 + p.getMinDisp();
                dias_disponibles += p.getDiasDisp();
                habiles += p.getDiasHabDisp();
                horas += p.getHorasDisp();
                minutos += p.getMinDisp();
                fines += p.getDiasFinDisp();
            }
            infor = link.vacacionesUltimoPeriodoDisponibleUsuario(Integer.toString(id_x));
        } else {
            listadoAprobadas = enlace.getPermisosManuales(0);
            listadoAnuladas = enlace.getPermisosManuales(4);
        }
        listadoUsu = link.getUsuariosActivos();
    } catch (Exception e) {
        System.out.println("ex |" + e);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Liquidación de vacaciones</title>
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
        <link rel="stylesheet" href="assets/css/components.css"></head>

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
                            <h1>Liquidación de vacaciones<%= id_x != 0 ? " - " + funcionario.getNombres() : ""%></h1>
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
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="anul-tab" data-toggle="tab" href="#anul" role="tab" aria-controls="anul" aria-selected="false"><i class="fas fa-exclamation-triangle"></i> Anuladas <%if (listadoAnuladas.size() != 0) {%><span class="badge badge-primary"><%= listadoAnuladas.size()%></span><%}%></a>
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
                                                        <th>Funcionario</th>
                                                        <th>Dirección</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Retorno</th>                                                        
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (PermisoManual a : listadoAprobadas) {
                                                            usuario func = enlace.buscar_usuarioID(a.getCodUsu());
                                                            UsuarioIESS iess = func.getId_usuario() == 0 ? link.getUsuario(a.getCodUsu()) : null;
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= a.getId()%></td>
                                                        <td style="width: 10%"><%= a.getCreacion()%></td>
                                                        <td style="width: 15%"><%= iess == null ? (func.getApellido() + " " + func.getNombre()) : iess.getNombres()%></td>
                                                        <td style="width: 30%"><%= a.getDireccion()%></td>
                                                        <td style="width: 10%"><%= a.getFechaInicio()%></td>
                                                        <td style="width: 10%"><%= a.getFechaFin()%></td>
                                                        <td style="width: 10%"><%= a.getFechaRetorno()%></td>
                                                        <td style="width: 5%">
                                                            <%if (a.getAdjunto() != null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 5%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAnulacion" data-ipe="<%= a.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Anular"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="anul" role="tabpanel" aria-labelledby="anul-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-xx">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Funcionario</th>
                                                        <th>Dirección</th>
                                                        <th>Inicio</th>
                                                        <th>Fin</th>
                                                        <th>Retorno</th>                                                        
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (PermisoManual a : listadoAnuladas) {
                                                            AnulacionManual elem = enlace.obtenerAnulacionManual(a.getId());
                                                            usuario func = enlace.buscar_usuarioID(a.getCodUsu());
                                                            UsuarioIESS iess = func.getId_usuario() == 0 ? link.getUsuario(a.getCodUsu()) : null;
                                                            usuario anula = enlace.buscar_usuarioID(a.getAdmin());
                                                    %>
                                                    <tr>
                                                        <td style="width: 5%"><%= a.getId()%></td>
                                                        <td style="width: 10%"><%= a.getCreacion()%></td>
                                                        <td style="width: 15%"><%= iess == null ? (func.getApellido() + " " + func.getNombre()) : iess.getNombres()%></td>
                                                        <td style="width: 30%"><%= a.getDireccion()%></td>
                                                        <td style="width: 10%"><%= a.getFechaInicio()%></td>
                                                        <td style="width: 10%"><%= a.getFechaFin()%></td>
                                                        <td style="width: 10%"><%= a.getFechaRetorno()%></td>
                                                        <td style="width: 5%">
                                                            <%if (a.getAdjunto() != null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                <%}%>
                                                        </td>
                                                        <td style="width: 5%">
                                                            <a target="_blank" href="administrar_permiso.control?accion=descargar_anulacion_m&id_permiso=<%= a.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar justificativo"></i></a>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= anula.getApellido() + " " + anula.getNombre()%>" data-descri="<%= elem.getMotivo()%>" data-fech="<%= elem.getCreacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
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
                            <form id="formmanual" action="administrar_permiso.control?accion=registro_manual" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="idadmin" id="idadmin" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="codusu" id="codusu" value="<%= id_x%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="jefe" id="jefe" value="<%= link.consultarJefeUsuario(id_x)%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden>
                                        <input type="text" class="form-control" name="cargojefe" id="cargojefe" value="<%=link.consultarDenominacionUsuario(link.consultarCodigoJefeDireccion(link.consultarCodigoDireccion(direccion_funcionario)))%>">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de solicitud *</label>
                                        <input type="text" class="form-control datepicker" placeholder="Ingrese fecha de solicitud" name="txtfechasolicitud" id="txtfechasolicitud" required="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-7">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtnombreservidor" id="txtnombreservidor" required readonly value="<%= funcionario.getNombres()%>">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtcargoservidor" id="txtcargoservidor" required readonly value="<%= cargo_funcionario%>">
                                        <div class="invalid-feedback">
                                            No ha ninguna información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="txtunidadservidor" id="txtunidadservidor" required readonly value="<%= direccion_funcionario%>">
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
                                    <div id="fecha_inicio" class="form-group col-md-2" hidden="">
                                        <label>Desde *</label>
                                        <input type="text" class="form-control datepicker" placeholder="fecha inicio" name="txtinicio1" id="txtinicio1" onblur="setFocusHasta()" required="">
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
                                    <div class="form-group col-md-1">
                                        <label>Horas</label>
                                        <input type="text" class="form-control" name="txthoras" id="txthoras" required readonly onchange="validarTiempo('H')">
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-1">
                                        <label>Minutos</label>
                                        <input type="text" class="form-control" name="txtminutos" id="txtminutos" required readonly onchange="validarTiempo('M')">
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
                                        <label>Adjuntar (tamaño máximo 2MB)</label>
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
                        </div>
                    </div>
                </div>
            </div>            
            <%}%>
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
                            <form id="formanulacion" action="administrar_permiso.control?accion=anular_manual" method="post" enctype="multipart/form-data" class="needs-validation">
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
                                        <%for (UsuarioIESS usu : listadoUsu) {%>
                                        <option value="<%= usu.getCodigo()%>"><%= usu.getNombres()%></option>
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
                                    $('#modalAnulacion').on('show.bs.modal', function (event) {
                                        var button = $(event.relatedTarget);
                                        var id_permiso = button.data('ipe');
                                        var modal = $(this);
                                        modal.find('.modal-body #txtper').val(id_permiso);
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

                                    $("#table-xx").dataTable({
                                        "ordering": true,
                                        "order": [[0, 'desc']],
                                        "columnDefs": [
                                            {"sortable": false, "targets": [2, 3]}
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
                                                                text: '',
                                                                icon: "success",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                                location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                            location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
                                                        });
                                                    }
                                                });
                                                return false;
                                            });
                                        });
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

                                    function validarTiempo(tipo) {
                                        var horas = document.getElementById("txthoras");
                                        var minutos = document.getElementById("txtminutos");
                                        var nHoras = parseInt(horas.value);
                                        var nMinutos = parseInt(minutos.value);
                                        var minDisp = <%=disponibilidadMinutos%>;
                                        var diasHab = parseInt(document.getElementById("combotipo").value) === 1 ? 0 : parseInt(document.getElementById("txtdiashabiles").value);
                                        if (Number.isNaN(parseInt(horas.value)) || Number.isNaN(parseInt(minutos.value))) {
                                            iziToast.error({
                                                title: 'Tiempo inválido',
                                                message: 'Ingrese números',
                                                position: 'topRight',
                                            });
                                        } else if (nHoras + nMinutos === 0) {
                                            iziToast.error({
                                                title: 'Aviso',
                                                message: 'La hora de inicio no puede ser igual a la hora de fin',
                                                position: 'topRight',
                                            });
                                        } else if (nHoras > 7 || nMinutos > 59) {
                                            iziToast.error({
                                                title: 'Tiempo inválido',
                                                message: 'Ingrese un máximo de 7 horas y 59 minutos',
                                                position: 'topRight',
                                            });
                                            horas.value = 0;
                                            minutos.value = 0;
                                        } else {
                                            var minRest = minDisp - diasHab * 8 * 60 - nHoras * 60 - nMinutos;
                                            console.log('minRest ' + minRest)
                                            if (minRest < 0) {
                                                iziToast.error({
                                                    title: 'Disponibilidad insuficiente',
                                                    message: 'Está solicitando más tiempo del disponible',
                                                    position: 'topRight',
                                                });
                                                horas.value = 0;
                                                minutos.value = 0;
                                            } else {
                                                iziToast.success({
                                                    title: 'Hora válida',
                                                    message: 'El rango de horas es válido',
                                                    position: 'topRight',
                                                });
                                                document.getElementById("botonguardarsoli").hidden = false;
                                            }
                                        }
                                        console.log('tipo ' + tipo)
                                        if (document.getElementById("botonguardarsoli").hidden) {
                                            if (tipo) {
                                                if (tipo.toUpperCase() === 'H') {
                                                    horas.value = 0;
                                                } else {
                                                    minutos.value = 0;
                                                }
                                            } else {
                                                horas.value = 0;
                                                minutos.value = 0;
                                            }
                                        }
                                    }

                                    function seleccionTipoPermisoHoras() {
                                        var id_tipo = parseInt($('#combotipo').val());
                                        var hora_inicio = document.getElementById("hora_inicio");
                                        var hora_fin = document.getElementById("hora_fin");
                                        var tiempo_solicitado = document.getElementById("tiempo_solicitado");
                                        var fecha_inicio = document.getElementById("fecha_inicio");
                                        var fecha_fin = document.getElementById("fecha_fin");
                                        var dia_solicitado = document.getElementById("dia_solicitado");
                                        var fecha_retorno = document.getElementById("fecha_retorno");
                                        var horas = document.getElementById("txthoras");
                                        var minutos = document.getElementById("txtminutos");
                                        horas.value = 0;
                                        minutos.value = 0;
                                        document.getElementById("botonguardarsoli").hidden = true;
                                        if (id_tipo === 1) {
                                            hora_inicio.hidden = false;
                                            hora_fin.hidden = false;
                                            tiempo_solicitado.hidden = false;
                                            fecha_inicio.hidden = true;
                                            fecha_fin.hidden = true;
                                            dia_solicitado.hidden = true;
                                            fecha_retorno.hidden = true;
                                            horas.setAttribute('readonly', true);
                                            minutos.setAttribute('readonly', true);
                                            $("#txtinicio").prop("required", true);
                                            $("#txtfin").prop("required", true);
                                            $("#txttiempo").prop("required", true);
                                            $("#txthoras").prop("required", true);
                                            $("#txtminutos").prop("required", true);
                                            $("#txtinicio1").removeAttr("required");
                                            $("#txtfin1").removeAttr("required");
                                            $("#txtingreso").removeAttr("required");
                                            $("#txtdiassolicitados").removeAttr("required");
                                            $("#txtdiashabiles").removeAttr("required");
                                            $("#txtdiasnohabiles").removeAttr("required");
                                            $("#txtdiasrecargo").removeAttr("required");
                                            $("#txtdiasdescuento").removeAttr("required");
                                        } else if (id_tipo === 2) {
                                            hora_inicio.hidden = true;
                                            hora_fin.hidden = true;
                                            tiempo_solicitado.hidden = true;
                                            fecha_inicio.hidden = false;
                                            fecha_fin.hidden = false;
                                            dia_solicitado.hidden = false;
                                            fecha_retorno.hidden = false;
                                            horas.removeAttribute('readonly');
                                            minutos.removeAttribute('readonly');
                                            $("#txtinicio1").prop("required", true);
                                            $("#txtfin1").prop("required", true);
                                            $("#txtingreso").prop("required", true);
                                            $("#txtdiassolicitados").prop("required", true);
                                            $("#txtdiashabiles").prop("required", true);
                                            $("#txtdiasnohabiles").prop("required", true);
                                            $("#txtdiasrecargo").prop("required", true);
                                            $("#txtdiasdescuento").prop("required", true);
                                            $("#txtinicio").removeAttr("required");
                                            $("#txtfin").removeAttr("required");
                                            $("#txttiempo").removeAttr("required");
                                            $("#txthoras").removeAttr("required");
                                            $("#txtminutos").removeAttr("required");
                                        }
                                    }

                                    $(document).ready(function () {
                                        $('#formmanual').submit(function (event) {
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
                                                        title: 'Procesando',
                                                        text: 'Se está generando el permiso',
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
                                                            location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                            location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
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
                                                        location.href = "permiso_manual.jsp?per_hor_admin=" +<%= id_x%>;
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
                                    function calculardiferencia() {
                                        var hora_inicio = $('#txtinicio').val();
                                        var hora_final = $('#txtfin').val();
                                        document.getElementById("botonguardarsoli").hidden = true;
                                        if (hora_inicio > hora_final) {
                                            iziToast.error({
                                                title: 'Aviso',
                                                message: 'La hora de inicio no puede ser mayor a la hora de fin',
                                                position: 'topRight',
                                            });
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
                                        // Cálculo de horas y minutos de la diferencia
                                        var horas = Math.floor(diferencia / 60);
                                        var minutos = diferencia % 60;
                                        $('#txttiempo').val(horas + 'h y ' + (minutos < 10 ? '0' : '') + minutos + "m");
                                        $('#txthoras').val(horas);
                                        $('#txtminutos').val(minutos);
                                        validarTiempo();
                                    }

                                    function diaIngreso() {
                                        var inicio = $('#txtinicio1').val();
                                        var fin = $('#txtfin1').val();
                                        document.getElementById('txthoras').value = 0;
                                        document.getElementById('txtminutos').value = 0;
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
                                    }

                                    function diasSolicitadosRecargo() {
                                        var inicio = $('#txtinicio1').val();
                                        var fin = $('#txtfin1').val();
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
                                        $.post('administrar_permiso.control?accion=dias_solicitados_manual', {
                                            txtinicio: inicio,
                                            txtfin: fin,
                                            codusu: <%=id_x%>
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

                                    function setFocusHasta() {
                                        document.getElementById("txtfin1").focus();
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

                                    $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                        var button = $(event.relatedTarget);
                                        var rechaza = button.data('aprueba');
                                        var fecha = button.data('fech');
                                        var modal = $(this);
                                        modal.find('.modal-body #txtaprueba').val(rechaza);
                                        modal.find('.modal-body #txtfechaaprueba').val(fecha);
                                    })

                                    function cambioFun() {
                                        $('#btnFun').removeAttr('hidden')
                                    }

                                    function administrar() {
                                        location.href = "permiso_manual.jsp?per_hor_admin=" + $('#listfun').val();
                                    }
    </script>
</body>
</html>