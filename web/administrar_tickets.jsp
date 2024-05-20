<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.organizacion"%>
<%@page import="modelo.DevolucionSoporte"%>
<%@page import="modelo.AnulacionSoporte"%>
<%@page import="modelo.rechazo_soporte"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.calificacion_soporte"%>
<%@page import="modelo.atendido_soporte"%>
<%@page import="modelo.diagnostico_soporte"%>
<%@page import="modelo.atencion_soporte"%>
<%@page import="modelo.asignacion_soporte"%>
<%@page import="modelo.satisfaccion_soporte"%>
<%@page import="modelo.forma_soporte"%>
<%@page import="modelo.tipo_soporte"%>
<%@page import="modelo.solicitud_soporte"%>
<%@page import="modelo.rol_pago"%>
<%@page import="modelo.rol_usuario"%>
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
    conexion_oracle oracle = new conexion_oracle();
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String codigo_direccion = null;
    String funcion_usuario = null;
    String tipo_usuario = null;
    String codigo_funcion = null;
    ArrayList<solicitud_soporte> listadoSoportesSolicitados = null;
    ArrayList<usuario> listadoTecnicos = null;
    ArrayList<organizacion> unidades = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    int numEstado = -1;
    int tecnico = -1;
    int unidad = -1;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        if(request.getParameter("txtestado")!= null){
            numEstado = Integer.parseInt(request.getParameter("txtestado").toString());
        }
        if(request.getParameter("tecnico")!= null){
            tecnico = Integer.parseInt(request.getParameter("tecnico").toString());
        }
        if(request.getParameter("unidad")!= null){
            unidad = Integer.parseInt(request.getParameter("unidad").toString());
        }
        listadoSoportesSolicitados = request.getParameter("txtini") == null ? (numEstado == -1 ? (tecnico == -1 ? (unidad == -1 ? enlace.listadoSolicitudesRecienteMes() : enlace.listadoSolicitudesRecientesUnidad(unidad)) : enlace.listadoSolicitudesUsuarioRecienteMes(tecnico)) : (tecnico == -1 ? (unidad == -1 ? enlace.listadoSolicitudesRecienteMes(numEstado) : enlace.listadoSolicitudesRecienteMesUnidad(numEstado, unidad)) : enlace.listadoSolicitudesUsuarioEstadoRecienteMes(tecnico, numEstado))) : (numEstado == -1 ? (tecnico == -1 ? (unidad == -1 ? enlace.listadoSolicitudesFecha(fecha_inicio, fecha_fin) : enlace.listadoSolicitudesFechaUnidad(fecha_inicio, fecha_fin, unidad)) : enlace.listadoSolicitudesUsuarioFecha(tecnico, fecha_inicio, fecha_fin)) : (tecnico == -1 ? (unidad == -1 ? enlace.listadoSolicitudesFechaEstado(fecha_inicio, fecha_fin, numEstado) : enlace.listadoSolicitudesFechaEstadoUnidad(fecha_inicio, fecha_fin, numEstado, unidad)) : enlace.listadoSolicitudesUsuarioFechaEstado(tecnico, fecha_inicio, fecha_fin, numEstado)));
        codigo_direccion = informacion.getCodigo_unidad();
        listadoTecnicos = enlace.listadoUsuarioUnidad("D.02.15");
        unidades = enlace.getUnidadesByDir("D.02.15");
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Administración de solicitudes de soporte</title>
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
                    <%
                    %>
                    <section class="section">
                        <div class="section-header">
                            <h1>Administración de solicitudes de soporte</h1>
                        </div>
                        <div class="section-body">        
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="card card-hero">
                                        <div class="card-header" style="background: #00793e">
                                            <div class="card-icon">
                                                <i class="fas fa-bell text-light"></i>
                                            </div>
                                            <h4><%= enlace.obtenerCantidadSolicitudesEstadoID(0)%></h4>
                                            <div class="card-description"><b>Nuevos</b></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card card-hero">
                                        <div class="card-header" style="background: #00793e">
                                            <div class="card-icon">
                                                <i class="fas fa-rocket text-light"></i>
                                            </div>
                                            <h4><%= enlace.obtenerCantidadSolicitudesEstadoID(1)%></h4>
                                            <div class="card-description"><b>Asignados</b></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card card-hero">
                                        <div class="card-header" style="background: #00793e">
                                            <div class="card-icon">
                                                <i class="fas fa-cogs text-light"></i>
                                            </div>
                                            <h4><%= enlace.obtenerCantidadSolicitudesEstadoID(2)%></h4>
                                            <div class="card-description"><b>Atendiendo</b></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card card-hero">
                                        <div class="card-header" style="background: #00793e">
                                            <div class="card-icon">
                                                <i class="fas fa-check-circle text-light"></i>
                                            </div>
                                            <h4><%= enlace.obtenerCantidadSolicitudesEstadoID(3)%></h4>
                                            <div class="card-description"><b>Atendidos</b></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form action="">
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
                                                    <div class="col-2">
                                                        <label>Estado</label>
                                                        <select class="form-control select2" name="txtestado" id="txtestado">
                                                            <option <%=numEstado==-1?"selected":""%> value="-1">Todos</option>
                                                            <option <%=numEstado==0?"selected":""%> value="0">Nuevos</option>
                                                            <option <%=numEstado==1?"selected":""%> value="1">Asignados</option>
                                                            <option <%=numEstado==2?"selected":""%> value="2">Atendiendo</option>
                                                            <option <%=numEstado==3?"selected":""%> value="3">Atendidos</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-3">
                                                        <label>Unidad</label>
                                                        <select class="form-control select2" name="unidad" id="unidad" required="">
                                                            <option value="-1" <%=unidad==-1?"selected":""%>>Todas</option>
                                                            <%for (organizacion u : unidades) {%>
                                                            <option value="<%= u.getId_organizacion()%>" <%=unidad==u.getId_organizacion()?"selected":""%>><%= u.getNombre()%></option>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <div class="col-3">
                                                        <label>Técnico</label>
                                                        <select class="form-control select2" name="tecnico" id="tecnico" required="">
                                                            <option value="-1" <%=tecnico==-1?"selected":""%>>Todos</option>
                                                            <%for (usuario f : listadoTecnicos) {%>
                                                            <option value="<%= f.getId_usuario()%>" <%=tecnico==f.getId_usuario()?"selected":""%>><%= f.getApellido()%> <%= f.getNombre()%></option>
                                                            <%}%>
                                                        </select>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
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
                                        <div class="card-header">
                                            <h4>Solicitudes recientes</h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-x">
                                                    <thead>                                 
                                                        <tr>
                                                            <th style="width: 5%">Código</th>
                                                            <th style="width: 10%">Fecha</th>
                                                            <th style="width: 10%">Tipo de soporte</th>
                                                            <th style="width: 20%">Funcionario Solicitante</th>
                                                            <th style="width: 20%">Dirección Solicitante</th>
                                                            <th style="width: 20%">Técnico asignado</th>
                                                            <th style="width: 5%">Calificación</th>
                                                            <th style="width: 5%">Estado</th>
                                                            <th style="width: 5%">Acciones</th>
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
                                                                usuario asi_usu = enlace.buscar_usuarioID(asig.getId_tecnico());
                                                                atencion_soporte aten_sopor = enlace.obtenerAtencionSoporteID(soli.getId_solicitud());
                                                                diagnostico_soporte diagnos = enlace.obtenerDiagnosticoSoporteID(soli.getId_solicitud());
                                                                atendido_soporte atendido_sop = enlace.obtenerAtendidoSoporteID(soli.getId_solicitud());
                                                                calificacion_soporte cal_sop = enlace.obtenerCalificacionSolicitudSoporteID(soli.getId_solicitud());
                                                                String direccion = enlace.getDireccionUsuarioId(soli.getId_solicitante());
                                                        %>
                                                        <tr>
                                                            <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleSolicitud" data-incidente="<%= soli.getIncidente()%>" data-forma="<%= fp.getDescripcion()%>" data-sugerido="<%= sugerido.getApellido()%> <%= sugerido.getNombre()%>" data-registra="<%= registra.getApellido()%> <%= registra.getNombre()%>"><%= "SP-" + soli.getId_solicitud()%></a></td>
                                                            <td><%= soli.getFecha_creacion()%></td>
                                                            <td><%= tp.getDescripcion()%></td>
                                                            <td><%= func.getApellido()%> <%= func.getNombre()%></td>
                                                            <td><%= !direccion.equals("DIRECCIÓN NO ASIGNADA") ? direccion : oracle.consultarDireccionUsuario(enlace.consultarCodigoUsuario(soli.getId_solicitante()))%></td>
                                                            <td>
                                                                <%if (enlace.isAsignadoSolicitudSoporte(soli.getId_solicitud())) {
                                                                        usuario tec_asig = enlace.obtenerTecnicoAsignadoSolicitudSoporte(soli.getId_solicitud());
                                                                %>
                                                                <%= tec_asig.getApellido()%> <%= tec_asig.getNombre()%> 
                                                                <%} else {%>
                                                                <%= "¡No ha sido asignado aún!"%>
                                                                <%}%>
                                                            </td>
                                                            <td>
                                                                <%if (enlace.tieneCalificacionSolicitudSoporte(soli.getId_solicitud())) {
                                                                    satisfaccion_soporte satis=enlace.obtenerSatisfaccionSolicitudSoporteID(soli.getId_solicitud());
                                                                %>
                                                                    <%if(satis.getId_satisfaccion()==1){%>
                                                                    <a class="badge badge-danger text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==2){%>
                                                                    <a class="badge badge-warning text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==3){%>
                                                                    <a class="badge badge-warning text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==4){%>
                                                                    <a class="badge badge-secondary text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==5){%>
                                                                    <a class="badge badge-primary text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==6){%>
                                                                    <a class="badge badge-primary text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}else if(satis.getId_satisfaccion()==7){%>
                                                                    <a class="badge badge-success text-white" href="javascript:" data-toggle="modal" data-target="#modalDetalleCalificacion" data-incidente="<%= cal_sop.getObservacion() %>" data-fecha="<%= cal_sop.getFecha_calificacion() %>" data-asignado="<%= func.getApellido()%> <%= func.getNombre()%>"><%= satis.getDescripcion()%></a>
                                                                    <%}%>
                                                                <%} else {%>
                                                                <%= "¡Sin calificar!"%>
                                                                <%}%>
                                                            </td>
                                                            <td>
                                                                <%if (soli.getEstado() == 0) {%>
                                                                <a class="badge badge-warning text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 1) {%>
                                                                <a class="badge <%=enlace.diferenciaHorasMysql(soli.getFecha_creacion())<24 ? "badge-primary" : "badge-danger"%> text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 2) {%>
                                                                <a class="badge badge-info text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 3) {%>
                                                                <a class="badge badge-success text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 4) {%>
                                                                <a class="badge badge-danger text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 5) {%>
                                                                <a class="badge badge-dark text-white"><%= estado%></a>
                                                                <%} else if (soli.getEstado() == 69 || soli.getEstado() == 70) {%>
                                                                <a class="badge badge-danger text-white"><%= estado%></a>
                                                                <%}%>
                                                            </td>
                                                            <td>
                                                                <%if(soli.getAdjunto() != null){%>
                                                                <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%=soli.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar adjunto solicitud"></i></a>
                                                                <%}%>
                                                                <%if (soli.getEstado() == 0) {%>
                                                                <a type="button" href="javascript:" data-toggle="modal" data-target="#modalAsignacion" data-codigo="<%= soli.getId_solicitud() %>" class="btn btn-primary btn-sm active"><i class="fas fa-user-tag" data-toggle="tooltip" data-original-title="Asignar"></i></a>
                                                                <a type="button" href="javascript:" data-toggle="modal" data-target="#modalAnulacion" data-cod="<%= soli.getId_solicitud() %>" class="btn btn-primary btn-sm active"><i class="fas fa-redo-alt" data-toggle="tooltip" data-original-title="Devolver"></i></a>
                                                                <%} else if(soli.getEstado()==5) {
                                                                    rechazo_soporte r = enlace.getRechazoSoporte(soli.getId_solicitud());
                                                                    usuario t = enlace.buscar_usuarioID(r.getId_tecnico());%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalAsignacion" data-codigo="<%= soli.getId_solicitud() %>" class="btn btn-primary btn-sm active"><i class="fas fa-user-tag" data-toggle="tooltip" data-original-title="Asignar"></i></a>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleRechazo" data-motivo="<%= r.getMotivo()%>" data-fecha="<%= r.getFecha_rechazo() %>" data-tecnico="<%= t.getNombre() + " " + t.getApellido()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver rechazo"></i></a>
                                                                <%}else if(soli.getEstado()==1) {%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleAsignacion" data-incidente="<%= soli.getIncidente()%>" data-fecha="<%= asig.getFecha_asignacion() %>" data-asignado="<%= asi_usu.getApellido()%> <%= asi_usu.getNombre()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalReasignacion" data-codigo="<%= soli.getId_solicitud() %>" class="btn btn-primary btn-sm active"><i class="fas fa-user-tag" data-toggle="tooltip" data-original-title="Reasignar"></i></a>
                                                                <%}else if(soli.getEstado()==2) {%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleAceptacion" data-incidente="<%= soli.getIncidente()%>" data-fecha="<%= aten_sopor.getFecha_atencion() %>" data-asignado="<%= asi_usu.getApellido()%> <%= asi_usu.getNombre()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                <%}else if(soli.getEstado()==3) {%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleAtendido" data-incidente="<%= diagnos.getObservacion() %>" data-fecha="<%= atendido_sop.getFecha_atendido() %>" data-asignado="<%= asi_usu.getApellido()%> <%= asi_usu.getNombre()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                <%} else if(soli.getEstado()==69) {
                                                                    AnulacionSoporte a = enlace.getAnulacionSoporte(soli.getId_solicitud());
                                                                    usuario t = enlace.buscar_usuarioID(a.getIdTec());%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleAnulacion" data-motivo="<%= a.getMotivo()%>" data-fecha="<%= a.getFecha() %>" data-tecnico="<%= t.getNombre() + " " + t.getApellido()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver devolución"></i></a>
                                                                <%} else if(soli.getEstado()==70) {
                                                                    DevolucionSoporte d = enlace.getDevolucionSoporteFuncionario(soli.getId_solicitud());%>
                                                                    <a type="button" href="javascript:" data-toggle="modal" data-target="#modalDetalleDevolucion" data-motivo="<%= d.getMotivo()%>" data-fecha="<%= d.getFecha() %>" data-tecnico="<%= func.getNombre() + " " + func.getApellido()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver devolución"></i></a>
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
                <div class="modal fade" id="modalDetalleAsignacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de asignación
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
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaincidente12" id="areaincidente12" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de asignación</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfechaasignacion12" id="txtfechaasignacion12" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Técnico asignado</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaasignacion12" id="areaasignacion12" required="" readonly="">
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
                <div class="modal fade" id="modalDetalleRechazo" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
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
                                            <label>Descripción de motivo</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="arearechazo" id="arearechazo" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de rechazo</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="fecharechazo" id="fecharechazo" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Técnico asignado</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="tecnicorechazo" id="tecnicorechazo" required="" readonly="">
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
                <div class="modal fade" id="modalDetalleAnulacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de devolución
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
                                            <label>Descripción de motivo</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaanu" id="areaanu" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de devolución</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="fechaanu" id="fechaanu" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Técnico que devolvió</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="tecnicoanu" id="tecnicoanu" required="" readonly="">
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
                <div class="modal fade" id="modalAnulacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formAnulacion" action="administrar_ticket.control?accion=anular_solicitud" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden="">
                                            <label>id solicitud</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtidsolicitudanula" id="txtidsolicitudanula" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden="">
                                            <label>id tecnico</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtidtecnicoanula" id="txtidtecnicoanula" required="" readonly="" value="<%= informacion.getId_usuario() %>">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>                                        
                                        <div class="form-group col-md-12">
                                            <label>Motivo</label>
                                            <textarea type="text" class="form-control" placeholder="Describa el motivo de la devolución" name="areaanula" id="areaanula" required=""></textarea>
                                            <div class="invalid-feedback">
                                                No ingresó el diagnóstico
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
                <div class="modal fade" id="modalDetalleAceptacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de Atención
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
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaincidente121" id="areaincidente121" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de aceptación</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfechaaceptacion12" id="txtfechaaceptacion12" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Técnico atendiendo</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaaceptacion12" id="areaaceptacion12" required="" readonly="">
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
                <div class="modal fade" id="modalDetalleAtendido" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de Atención
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
                                            <label>Diagnóstico</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaincidente121" id="areaincidente121" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de atención</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfechaaceptacion12" id="txtfechaaceptacion12" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Técnico atendió</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaaceptacion12" id="areaaceptacion12" required="" readonly="">
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
                <div class="modal fade" id="modalDetalleCalificacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de Calificación
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
                                            <label>Descripción calificación</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaincidente121" id="areaincidente121" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de calificación</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfechaaceptacion12" id="txtfechaaceptacion12" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Funcionario atendido</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaaceptacion12" id="areaaceptacion12" required="" readonly="">
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
                <div class="modal fade" id="modalAsignacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Asigne técnico
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <div class="form-row">
                                    <div class="form-group col-md-6" hidden="">
                                            <label>id administrador</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="idadmin" id="idadmin" required="" value="<%= informacion.getId_usuario() %>">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                            <div class="form-group col-md-6" hidden="">
                                            <label>id solicitud</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="idsoli" id="idsoli" required="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-striped" id="table-3">
                                        <thead>                                 
                                            <tr>
                                                <th>Funcionario</th>
                                                <th></th>
                                                <th></th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%for(usuario tec:listadoTecnicos){
                                                usuario elm=enlace.buscar_usuarioID(tec.getId_usuario());
                                            %>
                                            <tr>
                                                <td><%= elm.getApellido() %> <%= elm.getNombre() %></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                   <a type="button" onclick="asignarSoporteTecnicoID(<%= elm.getId_usuario() %>)" class="btn btn-primary btn-sm active"><i class="fas fa-check-circle" data-toggle="tooltip" data-original-title="Elegir"></i></a>
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
                <div class="modal fade" id="modalReasignacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Reasignación de técnico
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <div class="form-row">
                                    <div class="form-group col-md-6" hidden="">
                                            <label>id administrador</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="idadminrea" id="idadminrea" required="" value="<%= informacion.getId_usuario() %>">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                            <div class="form-group col-md-6" hidden="">
                                            <label>id solicitud</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="idsolirea" id="idsolirea" required="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-striped" id="table-4">
                                        <thead>                                 
                                            <tr>
                                                <th>Funcionario</th>
                                                <th></th>
                                                <th></th>
                                                <th>Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%for(usuario tec:listadoTecnicos){
                                                usuario elm=enlace.buscar_usuarioID(tec.getId_usuario());
                                            %>
                                            <tr>
                                                <td><%= elm.getApellido() %> <%= elm.getNombre() %></td>
                                                <td></td>
                                                <td></td>
                                                <td>
                                                   <a type="button" onclick="reasignarSoporteTecnicoID(<%= elm.getId_usuario() %>)" class="btn btn-primary btn-sm active"><i class="fas fa-check-circle" data-toggle="tooltip" data-original-title="Elegir"></i></a>
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
                <div class="modal fade" id="modalDetalleDevolucion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de devolución
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
                                            <label>Descripción de motivo</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="areaanu" id="areaanu" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de devolución</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="fechaanu" id="fechaanu" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Funcionario que devolvió</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="tecnicoanu" id="tecnicoanu" required="" readonly="">
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
        <script type="text/javascript">
            $(document).ready(function () {
                $('#formAnulacion').submit(function (event) {
                    event.preventDefault();
                    $.ajax({
                        url: $(this).attr('action'),
                        type: $(this).attr('method'),
                        data: new FormData(this),
                        contentType: false,
                        cache: false,
                        processData: false,
                        success: function (response) {
                            if (response) {
                                swal("Mensaje", "Se devolvió el soporte", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                }).then(function () {
                                    location.href = "administrar_tickets.jsp";
                                });
                            } else {
                                iziToast.error({
                                    title: 'Aviso',
                                    message: 'Existió un error al devolver',
                                    position: 'topRight',
                                });
                            }
                        }
                    });
                    return false;
                });
            })
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
            $('#modalReasignacion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var codigo = button.data('codigo');
                var modal = $(this);
                modal.find('.modal-body #idsolirea').val(codigo);
            })
            $('#modalDetalleRechazo').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var motivo = button.data('motivo');
                var fecha = button.data('fecha');
                var tecnico = button.data('tecnico');
                var modal = $(this);
                modal.find('.modal-body #arearechazo').val(motivo);
                modal.find('.modal-body #fecharechazo').val(fecha);
                modal.find('.modal-body #tecnicorechazo').val(tecnico);
            })
            $('#modalAnulacion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var codigo = button.data('cod');
                var modal = $(this);
                modal.find('.modal-body #txtidsolicitudanula').val(codigo);
            })
            $('#modalDetalleAnulacion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var motivo = button.data('motivo');
                var fecha = button.data('fecha');
                var tecnico = button.data('tecnico');
                var modal = $(this);
                modal.find('.modal-body #areaanu').val(motivo);
                modal.find('.modal-body #fechaanu').val(fecha);
                modal.find('.modal-body #tecnicoanu').val(tecnico);
            })
            $("#table-x").dataTable({
                "ordering": false,
                "order": [ 0, 'desc' ],
                "columnDefs": [
                  { "sortable": false, "targets": [0,4] }
                ],"pageLength": 25,
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
            function reasignarSoporteTecnicoID(id_tecnico) {
                var id_solicitud = $('#idsolirea').val();
                var id_administrador = $('#idadminrea').val();
                swal({
                    title: 'Confirmar reasignación',
                    text: "¿Desea reasignar a este técnico?",
                    type: 'warning',
                    buttons: {
                        cancel: {
                            visible: true,
                            text: 'No, cancelar',
                            className: 'btn btn-danger'
                        },
                        confirm: {
                            text: 'Sí, deseo reasignarlo',
                            className: 'btn btn-success'
                        }
                    }
                }).then((willDelete) => {
                    if (willDelete) {
                        $.post('administrar_ticket.control?accion=reasignar_solicitud', {
                            isop: id_solicitud,
                            idad: id_administrador,
                            idtec: id_tecnico
                        }, function (responseText) {
                            if (responseText) {
                                $('#modalReasignacion').modal('hide');
                                swal("Mensaje", "El técnico fue reasignado", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                }).then(function () {
                                    location.href = "administrar_tickets.jsp";
                                });
                            } else {
                                swal("Mensaje", "Algo salió mal", {
                                    icon: "warning",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-warning'
                                        }
                                    },
                                })
                            }
                        }, );
                    } else {
                        swal("El técnico no fue reasignado", {
                            buttons: {
                                confirm: {
                                    className: 'btn btn-success'
                                }
                            }
                        });
                    }
                });
            }
            $('#modalDetalleDevolucion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var motivo = button.data('motivo');
                var fecha = button.data('fecha');
                var tecnico = button.data('tecnico');
                var modal = $(this);
                modal.find('.modal-body #areaanu').val(motivo);
                modal.find('.modal-body #fechaanu').val(fecha);
                modal.find('.modal-body #tecnicoanu').val(tecnico);
            })
        </script>
    </body>
</html>
