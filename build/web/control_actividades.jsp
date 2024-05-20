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
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    ArrayList<actividad> listadoTodasActividades = null;
    ArrayList<actividad> listadoActividadesRegistradas = null;
    ArrayList<actividad> listadoActividadesRevisadas = null;
    ArrayList<usuario> listadoFuncionariosDireccion = null;
    String codigo_funcion = null;
    foto_usuario foto = null;
    String tipo_usuario = null;
    String codigo_direccion = null;
    String funcion_usuario = null;
    int iu = 0;
    int op = 0;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        iu = Integer.parseInt(request.getParameter("iu"));
        op = Integer.parseInt(request.getParameter("op"));
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoFuncionariosDireccion = enlace.listadoUsuariosDireccionTeletrabajo(codigo_funcion);
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Control de actividades</title>
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
                    <%

                        if (iu != 0) {
                            fecha_inicio = Date.valueOf(request.getParameter("fecha_inicio"));
                            fecha_fin = Date.valueOf(request.getParameter("fecha_fin"));
                            listadoTodasActividades = enlace.listadoActividadesTodasRangoFechaUsuarioId(iu, fecha_inicio, fecha_fin);
                            listadoActividadesRegistradas = enlace.listadoActividadesRegistradasRangoFechaUsuarioId(iu, fecha_inicio, fecha_fin);
                            listadoActividadesRevisadas = enlace.listadoActividadesAprobadasRangoFechaUsuarioId(iu, fecha_inicio, fecha_fin);
                        } else if (iu == 0 & op != 0) {
                            fecha_inicio = Date.valueOf(request.getParameter("fecha_inicio"));
                            fecha_fin = Date.valueOf(request.getParameter("fecha_fin"));
                            listadoTodasActividades = enlace.listadoActividadesTodasRangoFecha(codigo_funcion, fecha_inicio, fecha_fin);
                            listadoActividadesRegistradas = enlace.listadoActividadesRegistradasRangoFecha(codigo_funcion, fecha_inicio, fecha_fin);
                            listadoActividadesRevisadas = enlace.listadoActividadesAprobadasRangoFecha(codigo_funcion, fecha_inicio, fecha_fin);
                        }
                    %>
                    <section class="section">
                        <div class="section-header">
                            <h1>Control de actividades | Mi equipo de trabajo</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form action="administrar_actividad.control?accion=filtrar" method="post">
                                                <div class="row">
                                                    <%if (iu != 0) {%>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtini" id="txtini" value="<%= request.getParameter("fecha_inicio")%>" readonly=""/>
                                                    </div>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtfin" id="txtfin" value="<%= request.getParameter("fecha_fin")%>" readonly=""/>
                                                    </div>
                                                    <div class="form-group col-md-5">
                                                        <select class="form-control select2" required name="combofuncionario" id="combofuncionario">
                                                            <option value="0">Seleccione funcionario</option>
                                                            <%for (usuario busq : listadoFuncionariosDireccion) {
                                                                    String nombre_completo = busq.getApellido() + " " + busq.getNombre();
                                                            %>
                                                            <%if (iu != busq.getId_usuario()) {%>
                                                            <option value="<%= busq.getId_usuario()%>"><%= nombre_completo%></option>
                                                            <%} else {%>
                                                            <option selected value="<%= busq.getId_usuario()%>"><%= nombre_completo%></option>
                                                            <%}%>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <%} else if (iu == 0 && op == 0) {%>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtini" id="txtini" readonly=""/>
                                                    </div>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtfin" id="txtfin" readonly=""/>
                                                    </div>
                                                    <div class="form-group col-md-5">
                                                        <select class="form-control select2" required name="combofuncionario" id="combofuncionario">
                                                            <option value="0">Seleccione funcionario</option>
                                                            <%for (usuario busq : listadoFuncionariosDireccion) {
                                                                    String nombre_completo = busq.getApellido() + " " + busq.getNombre();
                                                            %>
                                                            <option value="<%= busq.getId_usuario()%>"><%= nombre_completo%></option>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <%} else if (iu == 0 && op == 1) {%>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtini" id="txtini" value="<%= request.getParameter("fecha_inicio")%>" readonly=""/>
                                                    </div>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtfin" id="txtfin" value="<%= request.getParameter("fecha_fin")%>" readonly=""/>
                                                    </div>
                                                    <div class="form-group col-md-5">
                                                        <select class="form-control select2" required name="combofuncionario" id="combofuncionario">
                                                            <option value="0">Seleccione funcionario</option>
                                                            <%for (usuario busq : listadoFuncionariosDireccion) {
                                                                    String nombre_completo = busq.getApellido() + " " + busq.getNombre();
                                                            %>
                                                            <%if (iu != busq.getId_usuario()) {%>
                                                            <option value="<%= busq.getId_usuario()%>"><%= nombre_completo%></option>
                                                            <%} else {%>
                                                            <option selected value="<%= busq.getId_usuario()%>"><%= nombre_completo%></option>
                                                            <%}%>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <%}%>
                                                    <div class="form-group">
                                                        <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango
                                                        </a>
                                                    </div>
                                                    <div class="col-1">
                                                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                                    </div>

                                                </div>
                                            </form>
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-check"></i> Registradas</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-check-double"></i> Revisadas</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-bars"></i> Todas</a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <%if (iu != 0) {%>
                                                    <a hidden="" type="button" href="administrar_actividad.control?accion=aprobar_actual&iu=<%= iu%>&fec_in=<%= fecha_inicio%>&fec_fn=<%= fecha_fin%>" class="btn btn-primary active"> <i class="fas fa-check"></i> Aprobar actuales</a>   
                                                    <%} else if (iu == 0 && op == 1) {%>
                                                    <a hidden="" type="button" href="administrar_actividad.control?accion=aprobar_actuales&cf=<%= codigo_funcion%>&fec_in=<%= fecha_inicio%>&fec_fn=<%= fecha_fin%>" class="btn btn-primary active"> <i class="fas fa-check"></i> Aprobar actuales</a>   
                                                    <%}%>
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-1">
                                                            <thead>
                                                                <tr>
                                                                    <th>Fecha</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Tiempo de ejecución</th>
                                                                    <th>Descripción de tarea</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (iu != 0) {
                                                                        for (actividad activi : listadoActividadesRegistradas) {
                                                                            int id_actividad = activi.getId_actividad();
                                                                            String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                            int id_usuario = activi.getId_usuario();
                                                                            usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                            String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                            comentario_actividad comentario = enlace.buscarComentarioActividadID(id_actividad);
                                                                %>
                                                                <tr>
                                                                    <td><%= activi.getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= activi.getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalDetalle" class="btn btn-primary btn-sm active" data-fecha="<%= activi.getFecha_actividad()%>" data-tarea="<%= activi.getTarea()%>" data-horai="<%= activi.getHora_inicio()%>" data-horaf="<%= activi.getHora_fin()%>" data-requirente="<%= activi.getRequiriente()%>" data-avance="<%= activi.getAvance()%>" data-herramienta="<%= activi.getHerramienta()%><%= activi.getHerramienta_otro()%>" data-observacion="<%= activi.getObservacion()%>" data-idact="<%= id_actividad%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                            <%if (comentario.getDescripcion() != null) {%>
                                                                            <%if (enlace.estadoComentarioActividad(comentario.getId_comentario()) == 0) {%>
                                                                        <a data-toggle="modal" data-target="#modalComentario" class="btn btn-danger btn-sm active" data-usu="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" data-fec="<%= comentario.getFecha_registro()%>" data-com="<%= comentario.getDescripcion()%>" ><i data-toggle="tooltip" data-original-title="Ver comentario" class="fas fa-comment"></i></a>
                                                                            <%} else {%>   
                                                                        <a data-toggle="modal" data-target="#modalComentario" class="btn btn-warning btn-sm active" data-usu="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" data-fec="<%= comentario.getFecha_registro()%>" data-com="<%= comentario.getDescripcion()%>" ><i data-toggle="tooltip" data-original-title="Ver comentario" class="fas fa-comment"></i></a>
                                                                            <%}%>
                                                                            <%}%>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <%}
                                                                } else if (iu == 0 && op == 1) {
                                                                    for (actividad activi : listadoActividadesRegistradas) {
                                                                        int id_actividad = activi.getId_actividad();
                                                                        String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                        int id_usuario = activi.getId_usuario();
                                                                        usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                        String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                        comentario_actividad comentario = enlace.buscarComentarioActividadID(id_actividad);
                                                                %>
                                                                <tr>
                                                                    <td><%= activi.getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= activi.getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalDetalle" class="btn btn-primary btn-sm active" data-fecha="<%= activi.getFecha_actividad()%>" data-tarea="<%= activi.getTarea()%>" data-horai="<%= activi.getHora_inicio()%>" data-horaf="<%= activi.getHora_fin()%>" data-requirente="<%= activi.getRequiriente()%>" data-avance="<%= activi.getAvance()%>" data-herramienta="<%= activi.getHerramienta()%><%= activi.getHerramienta_otro()%>" data-observacion="<%= activi.getObservacion()%>" data-idact="<%= id_actividad%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                            <%if (comentario.getDescripcion() != null) {%>
                                                                            <%if (enlace.estadoComentarioActividad(comentario.getId_comentario()) == 0) {%>
                                                                        <a data-toggle="modal" data-target="#modalComentario" class="btn btn-danger btn-sm active" data-usu="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" data-fec="<%= comentario.getFecha_registro()%>" data-com="<%= comentario.getDescripcion()%>" ><i data-toggle="tooltip" data-original-title="Ver comentario" class="fas fa-comment"></i></a>
                                                                            <%} else {%>   
                                                                        <a data-toggle="modal" data-target="#modalComentario" class="btn btn-warning btn-sm active" data-usu="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" data-fec="<%= comentario.getFecha_registro()%>" data-com="<%= comentario.getDescripcion()%>" ><i data-toggle="tooltip" data-original-title="Ver comentario" class="fas fa-comment"></i></a>
                                                                            <%}%>
                                                                            <%}%>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
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
                                                                    <th>Funcionario</th>
                                                                    <th>Tiempo de ejecución</th>
                                                                    <th>Descripción de tarea</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (iu != 0) {
                                                                        for (int paso = 0; paso < listadoActividadesRevisadas.size(); paso++) {
                                                                            int id_actividad = listadoActividadesRevisadas.get(paso).getId_actividad();
                                                                            String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                            int id_usuario = listadoActividadesRevisadas.get(paso).getId_usuario();
                                                                            usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                            String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                %>
                                                                <tr>
                                                                    <td><%= listadoActividadesRevisadas.get(paso).getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= listadoActividadesRevisadas.get(paso).getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalVer" class="btn btn-primary btn-sm active" data-fecha="<%= listadoActividadesRevisadas.get(paso).getFecha_actividad()%>" data-tarea="<%= listadoActividadesRevisadas.get(paso).getTarea()%>" data-horai="<%= listadoActividadesRevisadas.get(paso).getHora_inicio()%>" data-horaf="<%= listadoActividadesRevisadas.get(paso).getHora_fin()%>" data-requirente="<%= listadoActividadesRevisadas.get(paso).getRequiriente()%>" data-avance="<%= listadoActividadesRevisadas.get(paso).getAvance()%>" data-herramienta="<%= listadoActividadesRevisadas.get(paso).getHerramienta()%>" data-observacion="<%= listadoActividadesRevisadas.get(paso).getObservacion()%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}
                                                                } else if (iu == 0 && op == 1) {
                                                                    for (int paso = 0; paso < listadoActividadesRevisadas.size(); paso++) {
                                                                        int id_actividad = listadoActividadesRevisadas.get(paso).getId_actividad();
                                                                        String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                        int id_usuario = listadoActividadesRevisadas.get(paso).getId_usuario();
                                                                        usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                        String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                %>
                                                                <tr>
                                                                    <td><%= listadoActividadesRevisadas.get(paso).getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= listadoActividadesRevisadas.get(paso).getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalVer" class="btn btn-primary btn-sm active" data-fecha="<%= listadoActividadesRevisadas.get(paso).getFecha_actividad()%>" data-tarea="<%= listadoActividadesRevisadas.get(paso).getTarea()%>" data-horai="<%= listadoActividadesRevisadas.get(paso).getHora_inicio()%>" data-horaf="<%= listadoActividadesRevisadas.get(paso).getHora_fin()%>" data-requirente="<%= listadoActividadesRevisadas.get(paso).getRequiriente()%>" data-avance="<%= listadoActividadesRevisadas.get(paso).getAvance()%>" data-herramienta="<%= listadoActividadesRevisadas.get(paso).getHerramienta()%>" data-observacion="<%= listadoActividadesRevisadas.get(paso).getObservacion()%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
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
                                                                    <th>Funcionario</th>
                                                                    <th>Tiempo de ejecución</th>
                                                                    <th>Descripción de tarea</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (iu != 0) {
                                                                        for (int paso = 0; paso < listadoTodasActividades.size(); paso++) {
                                                                            int id_actividad = listadoTodasActividades.get(paso).getId_actividad();
                                                                            String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                            int id_usuario = listadoTodasActividades.get(paso).getId_usuario();
                                                                            usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                            String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                %>
                                                                <tr>
                                                                    <td><%= listadoTodasActividades.get(paso).getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= listadoTodasActividades.get(paso).getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalVer" class="btn btn-primary btn-sm active" data-fecha="<%= listadoTodasActividades.get(paso).getFecha_actividad()%>" data-tarea="<%= listadoTodasActividades.get(paso).getTarea()%>" data-horai="<%= listadoTodasActividades.get(paso).getHora_inicio()%>" data-horaf="<%= listadoTodasActividades.get(paso).getHora_fin()%>" data-requirente="<%= listadoTodasActividades.get(paso).getRequiriente()%>" data-avance="<%= listadoTodasActividades.get(paso).getAvance()%>" data-herramienta="<%= listadoTodasActividades.get(paso).getHerramienta()%>" data-observacion="<%= listadoTodasActividades.get(paso).getObservacion()%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}
                                                                } else if (iu == 0 && op == 1) {
                                                                    for (int paso = 0; paso < listadoTodasActividades.size(); paso++) {
                                                                        int id_actividad = listadoTodasActividades.get(paso).getId_actividad();
                                                                        String tiempo_ejecucion = enlace.tiempoEjecucionTarea(id_actividad);
                                                                        int id_usuario = listadoTodasActividades.get(paso).getId_usuario();
                                                                        usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                        String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                %>
                                                                <tr>
                                                                    <td><%= listadoTodasActividades.get(paso).getFecha_actividad()%></td>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><%= tiempo_ejecucion%></td>
                                                                    <td><%= listadoTodasActividades.get(paso).getTarea()%></td>
                                                                    <td>
                                                                        <a data-toggle="modal" data-target="#modalVer" class="btn btn-primary btn-sm active" data-fecha="<%= listadoTodasActividades.get(paso).getFecha_actividad()%>" data-tarea="<%= listadoTodasActividades.get(paso).getTarea()%>" data-horai="<%= listadoTodasActividades.get(paso).getHora_inicio()%>" data-horaf="<%= listadoTodasActividades.get(paso).getHora_fin()%>" data-requirente="<%= listadoTodasActividades.get(paso).getRequiriente()%>" data-avance="<%= listadoTodasActividades.get(paso).getAvance()%>" data-herramienta="<%= listadoTodasActividades.get(paso).getHerramienta()%>" data-observacion="<%= listadoTodasActividades.get(paso).getObservacion()%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver informacion"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
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
                <div class="modal fade" id="modaltodas" tabindex="-1" role="dialog" aria-hidden="true">
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
                                <form target="_blank" action="reporteActividad.control?accion=actividades" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden>
                                            <label>Id usuario</label>
                                            <input type="text" class="form-control" name="txtidusuario2" id="txtidusuario2" required="" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de inicio</label>
                                            <input type="text" class="form-control datepicker" name="txtin2" id="txtin2" required="">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Fecha de finalización</label>
                                            <input type="text" class="form-control datepicker" name="txtfi2" id="txtfi2" required="">
                                            <div class="invalid-feedback">
                                                What's your name?
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
                <div class="modal fade" id="modalDetalle" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalles de actividad
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form action="" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-2">
                                            <label>Fecha</label>
                                            <input type="text" class="form-control" name="txtfecha" id="txtfecha" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Tarea/Actividad</label>
                                            <input type="text" class="form-control" name="txttarea" id="txttarea" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Hora de inicio</label>
                                            <input type="text" class="form-control" name="txthorainicio" id="txthorainicio" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Hora de fin</label>
                                            <input type="text" class="form-control" name="txthorafin" id="txthorafin" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-4">
                                            <label>Persona requirente/Usuario final</label>
                                            <input type="text" class="form-control" name="txtrequirente" id="txtrequirente" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Avance</label>
                                            <input type="text" class="form-control" name="txtavance" id="txtavance" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Herramientas</label>
                                            <textarea type="text" class="form-control" name="txtherramienta" id="txtherramienta" required="" readonly></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>

                                        <div class="form-group col-md-6">
                                            <label>Observación</label>
                                            <textarea type="text" class="form-control" name="txtobservacion" id="txtobservacion" required="" readonly></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtidusuario" id="txtidusuario" required="" placeholder="Escriba aqui su comentario" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtidu" id="txtidu" required="" placeholder="Escriba aqui su comentario" value="<%= iu%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtop11" id="txtop11" required="" placeholder="Escriba aqui su comentario" value="<%= op%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <%if (iu != 0) {%>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtinic" id="txtinic" required="" placeholder="Escriba aqui su comentario" value="<%= request.getParameter("fecha_inicio")%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtfinf" id="txtfinf" required="" placeholder="Escriba aqui su comentario" value="<%= request.getParameter("fecha_fin")%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <%} else if (iu == 0 && op == 1) {%>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtinic" id="txtinic" required="" placeholder="Escriba aqui su comentario" value="<%= request.getParameter("fecha_inicio")%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtfinf" id="txtfinf" required="" placeholder="Escriba aqui su comentario" value="<%= request.getParameter("fecha_fin")%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <%}%>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtidactividad" id="txtidactividad" required="" placeholder="Escriba aqui su comentario">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Corrección</label>
                                            <textarea type="text" class="form-control" name="txtcomentario" id="txtcomentario" required="" placeholder="Escriba aqui su corrección"></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12" id="tabadjuntos" hidden>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" onclick="cargarDatos()" class="btn btn-info">Ver adjuntos</button>
                                        <button hidden="" type="button" onclick="aprobarActividad()" class="btn btn-success">Aprobar</button>
                                        <button type="button" onclick="comentarActividad()" class="btn btn-primary">Corregir</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modalVer" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalles de actividad
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form action="" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-2">
                                            <label>Fecha</label>
                                            <input type="text" class="form-control" name="txtfecha1" id="txtfecha1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Tarea/Actividad</label>
                                            <input type="text" class="form-control" name="txttarea1" id="txttarea1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Hora de inicio</label>
                                            <input type="text" class="form-control" name="txthorainicio1" id="txthorainicio1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Hora de fin</label>
                                            <input type="text" class="form-control" name="txthorafin1" id="txthorafin1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-4">
                                            <label>Persona requirente/Usuario final</label>
                                            <input type="text" class="form-control" name="txtrequirente1" id="txtrequirente1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Avance</label>
                                            <input type="text" class="form-control" name="txtavance1" id="txtavance1" required="" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label>Herramientas</label>
                                            <textarea type="text" class="form-control" name="txtherramienta1" id="txtherramienta1" required="" readonly></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>

                                        <div class="form-group col-md-12">
                                            <label>Observación</label>
                                            <textarea type="text" class="form-control" name="txtobservacion1" id="txtobservacion1" required="" readonly></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtidusuario1" id="txtidusuario1" required="" placeholder="Escriba aqui su comentario" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-6" hidden>
                                            <input type="text" class="form-control" name="txtidactividad1" id="txtidactividad1" required="" placeholder="Escriba aqui su comentario">
                                            <div class="invalid-feedback">
                                                What's your name?
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

                <div class="modal fade" id="modalComentario" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Comentario
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form action="" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-12">
                                            <textarea type="text" class="form-control" name="txtcomentario1" id="txtcomentario1" required="" placeholder="Escriba aqui su comentario" readonly></textarea>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-8">
                                            <label>Comentado por</label>
                                            <input type="text" class="form-control" name="txtusuariocom1" id="txtusuariocom1" required="" placeholder="Escriba aqui su comentario" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-4">
                                            <label>Fecha</label>
                                            <input type="text" class="form-control" name="txtfechareg1" id="txtfechareg1" required="" placeholder="Escriba aqui su comentario" readonly>
                                            <div class="invalid-feedback">
                                                What's your name?
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
        <script src="fun_js/funciones_actividad.js" type="text/javascript"></script>
        <script src="fun_js/validacion.js" type="text/javascript"></script>
        <script type="text/javascript">
            function cargarDatos() {
                var id_actividad = $('#txtidactividad').val();
                document.getElementById("tabadjuntos").hidden = false;
                $("#tabadjuntos").load("adjuntos_actividad.jsp?id_actividad=" + id_actividad);
                $('#modalDetalle').on('hidden', function () {
                    $(this).data('modalDetalle').$element.removeData();
                })
            }
        </script>
        <script type="text/javascript">
            /* <![CDATA[ */
            (function () {
                var bsModal = null;
                $("[data-toggle=modal]").click(function (e) {
                    e.preventDefault();
                    var trgId = $(this).attr('data-target');
                    if (bsModal == null)
                        bsModal = $(trgId).modal;
                    $.fn.bsModal = bsModal;
                    $(trgId + " .modal-body").load($(this).attr("href"));
                    $(trgId).bsModal('show');
                });
            })();
            /* <![CDATA[ */
        </script>
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
    </body>
</html>
