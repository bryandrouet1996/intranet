<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.Version"%>
<%@page import="modelo.Aplicacion"%>
<%@page import="modelo.motivo_permiso"%>
<%@page import="modelo.DevolucionSoporte"%>
<%@page import="modelo.AnulacionSoporte"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.satisfaccion_soporte"%>
<%@page import="modelo.atendido_soporte"%>
<%@page import="modelo.diagnostico_soporte"%>
<%@page import="modelo.atencion_soporte"%>
<%@page import="modelo.asignacion_soporte"%>
<%@page import="modelo.calificacion_soporte"%>
<%@page import="modelo.satisfaccion_soporte"%>
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
    ArrayList<Version> versiones = new ArrayList<>();
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<Aplicacion> aplicaciones = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        aplicaciones = enlace.getAplicaciones();
        versiones = enlace.getVersionesByUsu(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (!enlace.verificarUsuarioCumpleRol(id, "versionador")) {
            throw new Exception("Rol no habilitado");
        }
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
        <title>Intranet Alcaldía - Versionador</title>
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
                    <section class="section">
                        <div class="section-header">
                            <h1>Versionador</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a type="button" onclick="formularioRegistro()" class="btn btn-primary active" > <i class="fas fa-plus"></i> Crear versión</a>
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Listado de mis versiones <%if (versiones.size() != 0) {%><span class="badge badge-primary"><%= versiones.size()%></span><%}%></h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-x">
                                                    <thead>                                 
                                                        <tr>
                                                            <th>Código</th>
                                                            <th>Fecha</th>
                                                            <th>Autor</th>
                                                            <th>Aplicación</th>
                                                            <th>Tipo de cambio</th>
                                                            <th>Acciones</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%for (Version ver : versiones) {
                                                                usuario autor = enlace.buscar_usuarioID(ver.getIdUsu());
                                                                Aplicacion app = enlace.getAplicacion(ver.getIdApp());
                                                        %>
                                                        <tr>
                                                            <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleVersion" data-observacion="<%= ver.getDescripcion() %>" data-app="<%= app.getNombre() %>" data-cod="<%= ver.getEtiqueta() %>" data-tipo="<%= ver.getTipo() == 1 ? "Menor" : "Mayor" %>" data-autor="<%= autor.getApellido() %> <%= autor.getNombre() %>" data-fecha="<%= ver.getFecha() %>"><%= app.getCodigo() + " v." + ver.getEtiqueta() %></a></td>
                                                            <td><%= ver.getFecha()%></td>
                                                            <td><%= autor.getApellido()%> <%= autor.getNombre()%></td>
                                                            <td><%= app.getNombre()%></td>
                                                            <td>
                                                                <%if(ver.getTipo()==1){%>
                                                                    <a class="badge badge-primary text-white">Menor</a>
                                                                <%}else{%>
                                                                    <a class="badge badge-info text-white">Mayor</a>
                                                                <%}%>
                                                            </td>
                                                            <td>
                                                                <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#modalDetalleVersion" data-observacion="<%= ver.getDescripcion() %>" data-app="<%= app.getNombre() %>" data-cod="<%= ver.getEtiqueta() %>" data-tipo="<%= ver.getTipo() == 1 ? "Menor" : "Mayor" %>" data-autor="<%= autor.getApellido() %> <%= autor.getNombre() %>" data-fecha="<%= ver.getFecha() %>"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                <%if(ver.getAdjunto()!=null){%>
                                                                    <a target="_blank" href="descargar_archivo.control?accion=descargar_version&id_ver=<%=ver.getId()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar"></i></a>
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
                    <div class="modal fade"  role="dialog" aria-hidden="true" id="modalNuevaVersion">
                        <div class="modal-dialog modal-lg" role="dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title text-center">
                                        <span class="fw-extrabold">
                                            Registro de versión
                                        </span>
                                    </h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <p class="small"></p>
                                    <form id="formVersion" action="administrar_app.control?accion=registrar_version" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                            <div class="form-row">
                                                <div class="form-group col-md-2" hidden>
                                                    <label>Id Usuario</label>
                                                    <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="idUsu" id="idUsu" value="<%= informacion.getId_usuario()%>">
                                                    <div class="invalid-feedback">
                                                        No ha ninguna fecha
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-8">
                                                    <label>Aplicación</label>
                                                    <select class="form-control" name="cmbApp" id="cmbApp" required>
                                                        <option selected="" disabled="" value="">Seleccione aplicación</option>
                                                        <%for (Aplicacion app : aplicaciones) {%>
                                                        <option value="<%= app.getId()%>"><%= app.getNombre() + " (" + app.getVersion() + ")"%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Tipo de cambio</label>
                                                    <select class="form-control" name="cmbTipo" id="cmbTipo" required>
                                                        <option selected value="1">Menor</option>
                                                        <option value="2">Mayor</option>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <label>Descripción *</label>
                                                    <textarea type="text" class="form-control" placeholder="Descripción" name="txtdes" id="txtdes" required=""></textarea>
                                                    <div class="invalid-feedback">
                                                        No ha escrito ninguna descripción
                                                    </div>
                                                </div>
                                                <div id="divadju" class="form-group col-md-12">
                                                    <label>Adjunto <label id="lbladjunto"> </label> (opcional, tamaño máximo 20MB)</label>
                                                    <input type="file" class="form-control" name="archivo" id="archivo" onchange="validateSize()">
                                                    <div class="invalid-feedback">
                                                        No adjuntó ningún archivo
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="submit" id="botonguardarsoli" value="Upload" class="btn btn-primary">Guardar</button>
                                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                            </div>
                                        </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="modalDetalleVersion" role="dialog">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <span class="fw-extrabold">
                                            Detalle de versión
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
                                                <label>Descripción del cambio</label>
                                                <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtobservacion" id="txtobservacion" required="" readonly=""></textarea>
                                                <div class="invalid-feedback">
                                                    No ha ninguna fecha
                                                </div>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label>Aplicación</label>
                                                <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtapp" id="txtapp" required="" readonly="">
                                                <div class="invalid-feedback">
                                                    No ha ninguna fecha
                                                </div>
                                            </div>
                                            <div class="form-group col-md-3">
                                                <label>Código</label>
                                                <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtcod" id="txtcod" required="" readonly="">
                                                <div class="invalid-feedback">
                                                    No ha ninguna fecha
                                                </div>
                                            </div>
                                            <div class="form-group col-md-3">
                                                <label>Tipo de cambio</label>
                                                <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txttipo" id="txttipo" required="" readonly="">
                                                <div class="invalid-feedback">
                                                    No ha ninguna fecha
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group col-md-6">
                                                <label>Autor</label>
                                                <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtautor" id="txtautor" required="" readonly="">
                                                <div class="invalid-feedback">
                                                    No ha ninguna fecha
                                                </div>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label>Fecha</label>
                                                <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="txtfecha" id="txtfecha" required="" readonly="">
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
        <script src="assets/modules/fullcalendar/locale/es.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#formVersion').submit(function (event) {
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
                                swal("Versión nueva", "Se registró la versión", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                }).then(function () {
                                    location.href = "versionador.jsp";
                                });
                            } else {
                                iziToast.error({
                                    title: 'Aviso',
                                    message: 'Existió un error al registrar la versión',
                                    position: 'topRight',
                                });
                            }
                        }
                    });
                    return false;
                });
            })
            
            function formularioRegistro() {
                $('#modalNuevaVersion').modal('show');
            }
            
            $("#table-x").dataTable({
                "ordering": true,
                "order": [ 1, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0,5] }
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
              
            $('#modalDetalleVersion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var observacion = button.data('observacion');
                var app = button.data('app');
                var codigo = button.data('cod');
                var tipo = button.data('tipo');
                var autor = button.data('autor');
                var fecha = button.data('fecha');
                var modal = $(this);
                modal.find('.modal-body #txtobservacion').val(observacion);
                modal.find('.modal-body #txtapp').val(app);
                modal.find('.modal-body #txtcod').val(codigo);
                modal.find('.modal-body #txttipo').val(tipo);
                modal.find('.modal-body #txtautor').val(autor);
                modal.find('.modal-body #txtfecha').val(fecha);
            })
            
            function validateSize() {
                var input = document.getElementById("archivo");
                const fileSize = input.files[0].size / 1024 / 1024;
                if (fileSize > 20) {
                    swal("Mensaje", "El archivo excede el tamaño máximo de 20 MB", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    });
                    $('#archivo').val('');
                }
            }
        </script>
    </body>
</html>
