<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.organizacion"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.resumen_actividades"%>
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
    int ind = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    java.sql.Date fecha_inicio = null;
    java.sql.Date fecha_fin = null;
    String dir = null;
    ArrayList<organizacion> listadoDirecciones = null;
    ArrayList<usuario> listadoFuncionariosDireccion = null;
    ArrayList<actividad> listadoResumen = null;
    ArrayList<actividad> listadoResumenAprobadas = null;
    ArrayList<actividad> listadoResumenRegistradas = null;
    ArrayList<actividad> listadoResumenCorregidas = null;
    ArrayList<resumen_actividades> listadoResumenAct = null;
    double porcentaje_aprobadas = 0;
    double porcentaje_registradas = 0;
    double porcentaje_corregidas = 0;
    int cantidad_registradas = 0;
    int cantidad_aprobadas = 0;
    int cantidad_corregidas = 0;
    String codigo_funcion = null;
    String tipo_usuario = null;
    String codigo_direccion = null;
    String funcion_usuario = null;
    String dire = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        ind = Integer.parseInt(request.getParameter("ind"));
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        //codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        if (request.getParameter("direccion") != null) {
            dire = request.getParameter("direccion");
        }
        listadoDirecciones = enlace.listadoUnidades();
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Monitoreo de actividades</title>
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
                        if (ind != 0) {
                            try {
                                fecha_inicio = Date.valueOf(request.getParameter("fecha_inicio"));
                                fecha_fin = Date.valueOf(request.getParameter("fecha_fin"));
                                dir = request.getParameter("direccion");
                                if (!dir.equalsIgnoreCase("todo")) {
                                    int cantidad_regis = enlace.cantidadActividadesRegistradasDireccionCodigoFuncion(dir, fecha_inicio, fecha_fin);
                                    cantidad_corregidas = enlace.cantidadActividadesCorregidasDireccionCodigoFuncion(dir, fecha_inicio, fecha_fin);
                                    cantidad_aprobadas = enlace.cantidadActividadesAprobadasDireccionCodigoFuncion(dir, fecha_inicio, fecha_fin);
                                    cantidad_registradas = cantidad_regis - cantidad_corregidas;
                                    float cantidad_total = enlace.cantidadActividadesDireccionCodigoFuncion(dir, fecha_inicio, fecha_fin);
                                    porcentaje_aprobadas = enlace.porcentajeActividades(cantidad_aprobadas, cantidad_total);
                                    porcentaje_registradas = enlace.porcentajeActividades(cantidad_registradas, cantidad_total);
                                    porcentaje_corregidas = enlace.porcentajeActividades(cantidad_corregidas, cantidad_total);
                                    listadoResumen = enlace.resumenActividadesDireccionRangoFecha(dir, fecha_inicio, fecha_fin);
                                    listadoResumenAprobadas = enlace.resumenActividadesAprobadasDireccionRangoFecha(dir, fecha_inicio, fecha_fin);
                                    listadoResumenRegistradas = enlace.resumenActividadesRegistradasDireccionRangoFecha(dir, fecha_inicio, fecha_fin);
                                    listadoResumenCorregidas = enlace.resumenActividadesCorregidasDireccionRangoFecha(dir, fecha_inicio, fecha_fin);
                                    listadoFuncionariosDireccion = enlace.listadoUsuariosDireccionTeletrabajoSinDirector(dir);
                                } else {
                                    int cantidad_regis = enlace.cantidadActividadesRegistradasDirecciones(fecha_inicio, fecha_fin);
                                    cantidad_corregidas = enlace.cantidadActividadesCorregidasDirecciones(fecha_inicio, fecha_fin);
                                    cantidad_aprobadas = enlace.cantidadActividadesAprobadasDirecciones(fecha_inicio, fecha_fin);
                                    cantidad_registradas = cantidad_regis - cantidad_corregidas;
                                    float cantidad_total = enlace.cantidadActividadesDirecciones(fecha_inicio, fecha_fin);
                                    porcentaje_aprobadas = enlace.porcentajeActividades(cantidad_aprobadas, cantidad_total);
                                    porcentaje_registradas = enlace.porcentajeActividades(cantidad_registradas, cantidad_total);
                                    porcentaje_corregidas = enlace.porcentajeActividades(cantidad_corregidas, cantidad_total);
                                    listadoResumen = enlace.resumenActividadesDirecciones(fecha_inicio, fecha_fin);
                                    listadoResumenAprobadas = enlace.resumenActividadesAprobadasDirecciones(fecha_inicio, fecha_fin);
                                    listadoResumenRegistradas = enlace.resumenActividadesRegistradasDirecciones(fecha_inicio, fecha_fin);
                                    listadoResumenCorregidas = enlace.resumenActividadesCorregidasDirecciones(fecha_inicio, fecha_fin);
                                    listadoFuncionariosDireccion = enlace.listadoUsuariosDireccionesTeletrabajoSinDirector();
                                }
                            } catch (Exception ex) {

                            }
                        }
                    %>
                    <section class="section">
                        <div class="section-header">
                            <%if (ind != 0) {%>
                            <%if (!dir.equalsIgnoreCase("todo")) {%>
                            <h1>Monitoreo de actividades <%= enlace.direccionPerteneceUsuario(dir)%></h1>
                            <%} else {%>
                            <h1>Monitoreo de actividades General</h1>
                            <%}%>
                            <%} else {%>
                            <h1>Monitoreo de actividades</h1>
                            <%}%>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <%if (ind != 0) {%>
                                        <div class="row">
                                            <div class="col-7">
                                                <div class="summary-chart active" data-tab-group="summary-tab" id="summary-chart">
                                                    <canvas id="canvasss" height="120"></canvas>
                                                </div>
                                                <div class="summary-item">
                                                    <div class="mb-4 mt-4">
                                                        <%if (!dir.equalsIgnoreCase("todo")) {%>
                                                        <div class="text-small float-right font-weight-bold text-muted"><button class="btn" type="button" href="javascript:;" onclick="mostrarDatosRegistradas('<%= fecha_inicio%>', '<%= fecha_fin%>', '<%= dir%>')" data-toggle="modal" data-target="#modalDatos"><%= cantidad_registradas%></button></div>
                                                            <%} else {%>
                                                        <div class="text-small float-right font-weight-bold text-muted"><button class="btn" type="button"><%= cantidad_registradas%></button></div>
                                                            <%}%>
                                                        <div class="font-weight-bold mb-1">Registradas (<%= porcentaje_registradas%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-danger" role="progressbar" data-width="<%= porcentaje_registradas%>%" aria-valuenow="" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <%if (!dir.equalsIgnoreCase("todo")) {%>
                                                        <div class="text-small float-right font-weight-bold text-muted"><button class="btn" type="button" href="javascript:;" onclick="mostrarDatosAprobadas('<%= fecha_inicio%>', '<%= fecha_fin%>', '<%= dir%>')" data-toggle="modal" data-target="#modalDatos"><%= cantidad_aprobadas%></button></div>
                                                            <%} else {%>
                                                        <div class="text-small float-right font-weight-bold text-muted"><button class="btn" type="button"><%= cantidad_aprobadas%></button></div>
                                                            <%}%>
                                                        <div class="font-weight-bold mb-1">Aprobadas (<%= porcentaje_aprobadas%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-info" role="progressbar" data-width="<%= porcentaje_aprobadas%>%" aria-valuenow="" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div class="mb-4 mt-4">
                                                        <div class="text-small float-right font-weight-bold text-muted"><button class="btn" type="button" href="javascript:;"><%= cantidad_corregidas%></button></div>
                                                        <div class="font-weight-bold mb-1">Corregidas (<%= porcentaje_corregidas%>%)</div>
                                                        <div class="progress" data-height="5">
                                                            <div class="progress-bar bg-warning" role="progressbar" data-width="<%= porcentaje_corregidas%>%" aria-valuenow="" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <%if (!dir.equalsIgnoreCase("todo")) {%>
                                                        <label><br>TOTAL DE ACTIVIDADES: <%= enlace.cantidadActividadesDireccionCodigoFuncion(dir, fecha_inicio, fecha_fin)%></br></label>
                                                            <%} else {%>
                                                        <label><br>TOTAL DE ACTIVIDADES: <%= enlace.cantidadActividadesDirecciones(fecha_inicio, fecha_fin)%></br></label>
                                                            <%}%>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-5">
                                                <div class="card-body">
                                                    <canvas id="myChart331" height="180"></canvas>
                                                </div>
                                            </div>
                                        </div>
                                        <%}%>
                                        <div class="card-body">
                                            <form action="administrar_actividad.control?accion=filtrar_general" method="post">
                                                <div class="row">
                                                    <%try {%>
                                                    <%if (ind != 0) {%>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control datepicker" name="txtini" id="txtini" value="<%= request.getParameter("fecha_inicio")%>" readonly=""/>
                                                    </div>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control datepicker" name="txtfin" id="txtfin" value="<%= request.getParameter("fecha_fin")%>" readonly=""/>
                                                    </div>
                                                    <div class="form-group col-md-5">
                                                        <select class="form-control select2" required name="combodireccion" id="combodireccion">
                                                            <option value="todo">Seleccione dirección</option>
                                                            <%for (organizacion direcc : listadoDirecciones) {%>
                                                            <%if (!direcc.getNivel_hijo().equalsIgnoreCase(dire)) {%>
                                                            <option value="<%= direcc.getNivel_hijo()%>"><%= direcc.getNombre()%></option>
                                                            <%} else {%>
                                                            <option value="<%= direcc.getNivel_hijo()%>" selected><%= direcc.getNombre()%></option>
                                                            <%}%>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <%} else {%>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtini" id="txtini" readonly/>
                                                    </div>
                                                    <div class="col-2">
                                                        <input type="text" class="form-control" name="txtfin" id="txtfin" readonly/>
                                                    </div>
                                                    <div class="form-group col-md-5">
                                                        <select class="form-control select2" required name="combodireccion" id="combodireccion">
                                                            <option value="todo">Seleccione dirección</option>
                                                            <%for (organizacion direcc : listadoDirecciones) {%>
                                                            <option value="<%= direcc.getNivel_hijo() %>"><%= direcc.getNombre() %></option>
                                                            <%}%>
                                                        </select>
                                                    </div>
                                                    <%}%>
                                                    <%} catch (Exception ex) {%>
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
                                            <br>
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-file-alt"></i> Registros actuales</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a hidden="" class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-chart-bar"></i>Resumen total</a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-1">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Funcionario</th>
                                                                    <th>Accesos a intranet</th>
                                                                    <th>Actividades totales</th>
                                                                    <th>Actividades con correcciones</th>
                                                                    <th>Tiempo total (tareas)</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%try {%>
                                                                <%
                                                                    if (ind != 0) {
                                                                        for (usuario busqueda : listadoFuncionariosDireccion) {
                                                                            int id_usuario = busqueda.getId_usuario();
                                                                            usuario elemento = enlace.buscar_usuarioID(id_usuario);
                                                                            String nombre_completo = elemento.getApellido() + " " + elemento.getNombre();
                                                                            int ingresos_intranet = enlace.numeroAccesosIntranetRangoFecha(id_usuario, fecha_inicio, fecha_fin);
                                                                            int numero_actividades = enlace.numeroRegistroActividadesRangoFecha(id_usuario, fecha_inicio, fecha_fin);
                                                                            int activades_corregidas = enlace.numeroRegistroActividadesCorregidasRangoFecha(id_usuario, fecha_inicio, fecha_fin);
                                                                            String horas = enlace.totalHorasActividadesRango(id_usuario, fecha_inicio, fecha_fin);
                                                                            float horas_nuevas = enlace.totalHorasActividadesRangoUsuario(id_usuario, fecha_inicio, fecha_fin);
                                                                            double promedio_horas = horas_nuevas / numero_actividades;
                                                                            if (horas == null) {
                                                                                horas = "sin registros";
                                                                            }
                                                                %>
                                                                <tr>
                                                                    <td><%= nombre_completo%></td>
                                                                    <td><a type="button" href="javascript:;" onclick="mostrarDatosAccesos('<%= fecha_inicio%>', '<%= fecha_fin%>', '<%= id_usuario%>')" data-toggle="modal" data-target="#modalAccesos"><%= ingresos_intranet%></a></td>
                                                                    <td><a type="button" href="javascript:;" onclick="mostrarDatosIndividual('<%= fecha_inicio%>', '<%= fecha_fin%>', '<%= id_usuario%>')" data-toggle="modal" data-target="#modalIndividual"><%= numero_actividades%></a></td>
                                                                    <td><a type="button" href="javascript:;" onclick="mostrarCorreccionesFuncionario('<%= fecha_inicio%>', '<%= fecha_fin%>', '<%= id_usuario%>')" data-toggle="modal" data-target="#modalCorrectivos"><%= activades_corregidas%></a></td>
                                                                    <td><a type="button" href="javascript:;" onclick="mostrarTiemposFuncionario('<%= promedio_horas%>', '<%= enlace.mayorRegistroActividadUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin)%>', '<%= enlace.menorRegistroActividadUsuarioIDRango(id_usuario, fecha_inicio, fecha_fin)%>')" data-toggle="modal" data-target="#modalHoras"><%= horas%></a></td>
                                                                </tr>
                                                                <%}%>
                                                                <%}%>
                                                                <%} catch (Exception ex) {%>
                                                                <%}%>
                                                            </tbody>
                                                            <%try {%>
                                                            <%if (ind != 0) {%>
                                                            <tfoot>
                                                                <tr>
                                                                    <td><b>TOTALES</b></td>
                                                                    <%if (!dir.equalsIgnoreCase("todo")) {%>
                                                                    <td><b><%= enlace.numeroAccesosIntranetRangoFechaDireccion(dir, fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.numeroActividadesRangoFechaDireccion(dir, fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.numeroRegistroActividadesCorregidasRangoFechaDireccion(dir, fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.totalHorasActividadesRangoDireccion(dir, fecha_inicio, fecha_fin)%></b></td>
                                                                    <%} else {%>
                                                                    <td><b><%= enlace.numeroAccesosIntranetRangoFechaDirecciones(fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.numeroActividadesRangoFechaDirecciones(fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.numeroRegistroActividadesCorregidasRangoFechaDirecciones(fecha_inicio, fecha_fin)%></b></td>
                                                                    <td><b><%= enlace.totalHorasActividadesRangoDirecciones(fecha_inicio, fecha_fin)%></b></td>
                                                                    <%}%>
                                                                </tr>
                                                            </tfoot>
                                                            <tfoot>
                                                                <tr>
                                                                    <td><b>Funcionario</b></td>
                                                                    <td><b>Accesos Intranet</b></td>
                                                                    <td><b>Actividades totales</b></td>
                                                                    <td><b>Actividades con correcciones</b></td>
                                                                    <td><b>Total horas (tareas)</b></td>
                                                                </tr>
                                                            </tfoot>
                                                            <%}%>
                                                            <%} catch (Exception ex) {%>
                                                            <%}%>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div hidden="" class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-resumen">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Apellidos y nombres</th>
                                                                    <th>Cédula de identidad</th>
                                                                    <th>Regimen laboral</th>
                                                                    <th>Dirección</th>
                                                                    <th>Unidad</th>
                                                                    <th>Desde</th>
                                                                    <th>Hasta</th>
                                                                    <th>Total horas laboradas</th>
                                                                    <th>Total dias</th>
                                                                    <th>Modalidad</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>

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
                                <form target="_blank" action="reporteActividad.control?accion=general" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
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
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modalDatos" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <form>
                                    <div class="form-row">
                                        <div class="form-group col-md-12" id="tabfunc">
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

                <div class="modal fade" id="modalIndividual" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Estadísticos funcionario 
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-row">
                                        <div class="form-group col-md-12" id="drawind">
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
                <div class="modal fade" id="modalAccesos" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Accesos funcionario
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-row">
                                        <div class="form-group col-md-12" id="intraaccess">
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
                <div class="modal fade" id="modalCorrectivos" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Actividades con correcciones 
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-row">
                                        <div class="form-group col-md-12" id="correctivostab">
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
                <div class="modal fade" id="modalHoras" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Tiempos de funcionario 
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <form>
                                    <div class="form-row">
                                        <div class="form-group col-md-12" id="horastab">
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
        <script src="assets/js/page/modules-chartjs.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>

        <script src="assets/modules/jquery-datatable/extensions/export/buttons.flash.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/buttons.html5.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/buttons.print.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/dataTables.buttons.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/jszip.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/pdfmake.min.js" type="text/javascript"></script>
        <script src="assets/modules/jquery-datatable/extensions/export/vfs_fonts.js" type="text/javascript"></script>
        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/funciones_actividad.js" type="text/javascript"></script>
        <script>
            var barChartData = {
            labels: [
            <%if (ind != 0) {%>
            <%for (actividad resumen : listadoResumen) {%>
            "<%= resumen.getFecha_actividad()%>",
            <%}%>
            <%}%>
            ],
                    datasets: [
                    {
                    label: 'Corregidas',
                            backgroundColor: "#ffa426",
                            yAxisID: "bar-y-axis",
                            data: [
            <%if (ind != 0) {%>
            <%for (actividad resumen : listadoResumenCorregidas) {%>
                            "<%= resumen.getId_actividad()%>",
            <%}%>
            <%}%>
                            ]
                    }, {
                    label: 'Aprobadas',
                            backgroundColor: "#3abaf4",
                            yAxisID: "bar-y-axis",
                            data: [
            <%if (ind != 0) {%>
            <%for (actividad resumen : listadoResumenAprobadas) {%>
                            "<%= resumen.getId_actividad()%>",
            <%}%>
            <%}%>
                            ]
                    }, {
                    label: 'Registradas',
                            backgroundColor: "#fc544b",
                            yAxisID: "bar-y-axis",
                            data: [
            <%if (ind != 0) {%>
            <%for (actividad resumen : listadoResumenRegistradas) {%>
                            "<%= resumen.getId_actividad()%>",
            <%}%>
            <%}%>
                            ]
                    }]
            };
            var ctx = document.getElementById("canvasss").getContext("2d");
            window.myBar = new Chart(ctx, {
            type: 'bar',
                    data: barChartData,
                    options: {
                    title: {
                    display: false,
                            text: "Chart.js Bar Chart - Stacked"
                    },
                            tooltips: {
                            mode: 'label'
                            },
                            responsive: true,
                            scales: {
                            xAxes: [{
                            stacked: true,
                                    scaleLabel: {
                                    display: true,
                                            labelString: 'Fecha de registro'
                                    },
                            }],
                                    yAxes: [{
                                    stacked: false,
                                            ticks: {
                                            beginAtZero: true,
                                                    min: 0,
                                                    stepSize: 5,
                                                    max: 50
                                            }, scaleLabel: {
                                    display: true,
                                            labelString: 'Cantidad de registros'
                                    },
                                    }, {
                                    id: "bar-y-axis",
                                            stacked: true,
                                            display: false, //optional
                                            ticks: {
                                            beginAtZero: true,
                                                    min: 0,
                                                    stepSize: 10,
                                                    max: 50
                                            },
                                            type: 'linear'
                                    }]
                            }
                    }
            });
        </script>
        <script>
                                                                        function mostrarDatosAprobadas(fecha_inicio, fecha_fin, cod_fu) {
                                                                        $("#tabfunc").load("actividades_funcionarios.jsp?tipo=1&fechi=" + fecha_inicio + "&fechf=" + fecha_fin + "&cf=" + cod_fu);
                                                                        }
                                                                        function mostrarDatosRegistradas(fecha_inicio, fecha_fin, cod_fu) {
                                                                        $("#tabfunc").load("actividades_funcionarios.jsp?tipo=0&fechi=" + fecha_inicio + "&fechf=" + fecha_fin + "&cf=" + cod_fu);
                                                                        }

                                                                        function mostrarDatosIndividual(fecha_inicio, fecha_fin, id_usuario) {
                                                                        $("#drawind").load("correcciones_funcionarios.jsp?fechi=" + fecha_inicio + "&fechf=" + fecha_fin + "&iu=" + id_usuario);
                                                                        }

                                                                        function mostrarDatosAccesos(fecha_inicio, fecha_fin, id_usuario) {
                                                                        $("#intraaccess").load("accesos_funcionario.jsp?fechi=" + fecha_inicio + "&fechf=" + fecha_fin + "&iu=" + id_usuario);
                                                                        }

                                                                        function mostrarCorreccionesFuncionario(fecha_inicio, fecha_fin, id_usuario) {
                                                                        $("#correctivostab").load("correctivos_funcionarios.jsp?fechi=" + fecha_inicio + "&fechf=" + fecha_fin + "&iu=" + id_usuario);
                                                                        }

                                                                        function mostrarTiemposFuncionario(promedio, hora_max, hora_min) {
                                                                        $("#horastab").load("tiempos_funcionario.jsp?prom=" + promedio + "&max=" + hora_max + "&min=" + hora_min);
                                                                        }
        </script>
        <script>
            var ctx = document.getElementById("myChart331").getContext('2d');
            var myChart = new Chart(ctx, {
            type: 'pie',
                    data: {
                    datasets: [{
                    data: [
            <%if (ind != 0) {%>
            <%= porcentaje_registradas%>,
            <%= porcentaje_aprobadas%>,
            <%= porcentaje_corregidas%>
            <%}%>
                    ],
                            backgroundColor: [
                                    '#fc544b',
                                    '#3abaf4',
                                    '#ffa426'
                            ],
                            label: 'Dataset 1'
                    }],
                            labels: [
                                    'Registradas',
                                    'Aprobadas',
                                    'Corregidas'
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
