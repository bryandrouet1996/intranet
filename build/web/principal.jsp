<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.calendario_google"%>
<%@page import="modelo.conexion_servicios"%>
<%@page import="modelo.evento"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.estado_evento"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    int id = 0;
    usuario informacion = null;
    String codigo_direccion = null;
    String funcion_usuario = null;
    foto_usuario foto = null;
    String codigo_funcion = null;
    ArrayList<estado_evento> listadoEstadosEvento = null;
    ArrayList<evento> listadoEventosUsuario = null;
    ArrayList<evento> listadoEventosDireccion = null;
    ArrayList<calendario_google> listadoCalendario = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("usuario_ad").toString());
        informacion = enlace.buscar_usuarioID(id);
        codigo_direccion = informacion.getCodigo_unidad();
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listadoEstadosEvento = enlace.listadoEstadosEvento();
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoEventosUsuario = enlace.listadoEventosUsuario(informacion.getId_usuario());
        listadoEventosDireccion = enlace.listadoEventosDireccion(codigo_funcion);
        listadoCalendario = enlace.listadoCalendariosGoogleUsuario(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Principal</title>
        <!-- General CSS Files -->
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/modules/fullcalendar/fullcalendar.min.css">
        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/jqvmap/dist/jqvmap.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons-wind.min.css">
        <link rel="stylesheet" href="assets/modules/summernote/summernote-bs4.css">
        <link rel="stylesheet" href="assets/modules/izitoast/css/iziToast.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    </head>

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
                                <img src="imagen.control?id=<%= informacion.getId_usuario()%>" alt="..." class="rounded-circle mr-1">
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
                            <a href="principal.jsp">
                                <img src="assets/img/aaa.png" height="50px">
                            </a>
                        </div>
                        <div class="sidebar-brand sidebar-brand-sm">
                            <a href="principal.jsp">DS</a>
                        </div>
                        <ul class="sidebar-menu">
                            <li class="dropdown active">
                                <a href="principal.jsp" class="nav-link"><i class="fas fa-home"></i><span>Principal</span></a>
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

                <!-- Main Content -->
                <div class="main-content">
                    <section class="section">
                        <br>
                        <div class="row">
                            <img class="img-fluid" src="assets/img/banner.jpg">
                            <div class="col-lg-3 col-md-6 col-sm-6 col-12">
                                <div class="card card-statistic-1">
                                    <div class="card-icon">
                                        <img src="assets/img/iniciosdesesion.png" height="75px">
                                    </div>
                                    <div class="card-wrap">
                                        <div class="card-header">
                                            <h4>Accesos a Intranet</h4>
                                        </div>
                                        <div class="card-body">
                                            <%= enlace.numeroAccesosUsuario(informacion.getId_usuario())%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--<div class="col-lg-3 col-md-6 col-sm-6 col-12">
                                <div class="card card-statistic-1">
                                    <div class="card-icon">
                                        <img src="assets/img/faltasdelmes.png" height="75px">
                                    </div>
                                    <div class="card-wrap">
                                        <div class="card-header">
                                            <h4>Faltas del mes</h4>
                                        </div>
                                        <div class="card-body">
                                            0
                                        </div>
                                    </div>
                                </div>
                            </div>-->
                            <div class="col-lg-3 col-md-6 col-sm-6 col-12">
                                <div class="card card-statistic-1">
                                    <div class="card-icon">
                                        <img src="assets/img/permisos.png" height="75px">
                                    </div>
                                    <div class="card-wrap">
                                        <div class="card-header">
                                            <h4>Permisos</h4>
                                        </div>
                                        <div class="card-body">
                                            <%= enlace.totalPermisosUsuario(informacion.getId_usuario())%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 col-sm-6 col-12">
                                <a href="sesion.control?accion=manuales&iu=<%= id %>"> 
                                    <div class="card card-statistic-1">
                                        <div class="card-icon">
                                            <img src="assets/img/manual.png" height="75px">
                                        </div>
                                        <div class="card-wrap">
                                            <div class="card-header">
                                                <h4>Manuales<br>de usuario</h4>
                                            </div>
                                        </div>
                                    </div> 
                                </a>
                            </div>
                                    <br><br>        
                            <div class="col-lg-3 col-md-6 col-sm-6 col-12">
                                <a href="sesion.control?accion=formularios&iu=<%= id %>"> 
                                    <div class="card card-statistic-1">
                                        <div class="card-icon">
                                            <img src="assets/img/formularios.png" height="75px">
                                        </div>
                                        <div class="card-wrap">
                                            <div class="card-header">
                                                <h4>Formularios</h4>
                                            </div>
                                        </div>
                                    </div> 
                                </a>
                            </div>
                        </div>
                    </section>
                    <div class="modal fade" id="ModalAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <form class="form-horizontal" method="POST" action="administrar_evento.control?accion=registrar&iu=<%= informacion.getId_usuario()%>">
                                    <div class="modal-header">
                                        <h5 class="modal-title">
                                            <span class="fw-extrabold">
                                                Añadir Evento
                                            </span>
                                        </h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label for="title" class="col-sm-2 control-label">Título</label>
                                                    <input type="text" name="title" class="form-control" id="title" placeholder="Título" required>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="start" class="col-sm-6 control-label">Fecha inicio</label>
                                                    <input type="text" name="start" class="form-control datepicker" id="start">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="starthora" class="col-sm-6 control-label">Hora inicio</label>
                                                    <input type="time" name="starthora" class="form-control" id="starthora" value="<%= enlace.hora_actual()%>">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="end" class="col-sm-6 control-label">Fecha final</label>
                                                    <input type="text" name="end" class="form-control datepicker" id="end">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="endhora" class="col-sm-6 control-label">Hora final</label>
                                                    <input type="time" name="endhora" class="form-control" id="endhora">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="estado" class="col-sm-2 control-label">Estado</label>
                                                    <select name="estado" class="form-control" id="estado" required>
                                                        <option value="">Seleccionar</option>
                                                        <%for (int paso = 0; paso < listadoEstadosEvento.size(); paso++) {%>
                                                        <option value="<%= listadoEstadosEvento.get(paso).getId_estado()%>"><%= listadoEstadosEvento.get(paso).getDescripcion()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="color" class="col-sm-2 control-label">Color</label>
                                                    <select name="color" class="form-control" id="color" required>
                                                        <option value="">Seleccionar</option>
                                                        <option style="color:#0071c5;" value="#0071c5">&#9724; Azul oscuro</option>
                                                        <option style="color:#40E0D0;" value="#40E0D0">&#9724; Turquesa</option>
                                                        <option style="color:#008000;" value="#008000">&#9724; Verde</option>						  
                                                        <option style="color:#FFD700;" value="#FFD700">&#9724; Amarillo</option>
                                                        <option style="color:#FF8C00;" value="#FF8C00">&#9724; Naranja</option>
                                                        <option style="color:#FF0000;" value="#FF0000">&#9724; Rojo</option>
                                                        <option style="color:#000;" value="#000">&#9724; Negro</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Guardar</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="ModalEdit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <form class="form-horizontal" method="POST" action="administrar_evento.control?accion=modificar&iu=<%= informacion.getId_usuario()%>">
                                    <div class="modal-header">
                                        <h5 class="modal-title">
                                            <span class="fw-extrabold">
                                                Editar Evento
                                            </span>
                                        </h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label for="title" class="col-sm-2 control-label">Titulo</label>
                                                    <input type="text" name="title" class="form-control" id="title" placeholder="Titulo">
                                                </div>
                                            </div>
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label for="color" class="col-sm-2 control-label">Color</label>
                                                    <select name="color" class="form-control" id="color">
                                                        <option value="">Seleccionar</option>
                                                        <option style="color:#0071c5;" value="#0071c5">&#9724; Azul oscuro</option>
                                                        <option style="color:#40E0D0;" value="#40E0D0">&#9724; Turquesa</option>
                                                        <option style="color:#008000;" value="#008000">&#9724; Verde</option>						  
                                                        <option style="color:#FFD700;" value="#FFD700">&#9724; Amarillo</option>
                                                        <option style="color:#FF8C00;" value="#FF8C00">&#9724; Naranja</option>
                                                        <option style="color:#FF0000;" value="#FF0000">&#9724; Rojo</option>
                                                        <option style="color:#000;" value="#000">&#9724; Negro</option>

                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="title" class="col-sm-6 control-label">Hora inicio</label>
                                                    <input type="time" name="starthora" class="form-control" id="starthora" placeholder="Titulo">
                                                </div>
                                            </div>
                                            <div class="col-sm-6">
                                                <div class="form-group">
                                                    <label for="title" class="col-sm-6 control-label">Hora fin</label>
                                                    <input type="time" name="endhora" class="form-control" id="endhora" placeholder="Titulo">
                                                </div>
                                            </div>
                                            <div class="col-sm-12">
                                                <div class="form-group">
                                                    <label for="title" class="col-sm-6 control-label">Creado por</label>
                                                    <input type="text" name="creador" class="form-control" id="creador" placeholder="Titulo" readonly>
                                                </div>
                                            </div>
                                            <div class="col-sm-offset-2 col-sm-12">
                                                <div class="form-group"> 
                                                    <div class="checkbox">
                                                        <label class="text-danger"><input type="checkbox"  name="checkdelete"> Eliminar Evento</label>
                                                    </div>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" class="form-control" id="id">
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Guardar</button>
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

        <!-- JS Libraies -->
        <script src="assets/modules/simple-weather/jquery.simpleWeather.min.js"></script>
        <script src="assets/modules/chart.min.js"></script>
        <script src="assets/modules/jqvmap/dist/jquery.vmap.min.js"></script>
        <script src="assets/modules/jqvmap/dist/maps/jquery.vmap.world.js"></script>
        <script src="assets/modules/summernote/summernote-bs4.js"></script>
        <script src="assets/modules/chocolat/dist/js/jquery.chocolat.min.js"></script>
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <script src="assets/modules/bootstrap-daterangepicker/daterangepicker.js"></script>
        <script src="assets/modules/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
        <script src="assets/modules/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
        <script src="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/index-0.js"></script>
        <!-- JS Libraies -->
        <script src="assets/modules/fullcalendar/fullcalendar.min.js"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
        <script src="assets/modules/fullcalendar/gcal.min.js" type="text/javascript"></script>
        <script>
            $(document).ready(function () {
                var date = new Date();
                var yyyy = date.getFullYear().toString();
                var mm = (date.getMonth() + 1).toString().length == 1 ? "0" + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString();
                var dd = (date.getDate()).toString().length == 1 ? "0" + (date.getDate()).toString() : (date.getDate()).toString();
                $("#myEvent").fullCalendar({
                    height: 'auto',
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'month,agendaWeek,agendaDay,listWeek'
                    },
                    defaultDate: yyyy + "-" + mm + "-" + dd,
                    editable: true,
                    eventLimit: true, // allow "more" link when too many events
                    selectable: true,
                    selectHelper: true,
                    textColor: '#FFF',
                    select: function (start, end) {

                        $('#ModalAdd #start').val(moment(start).format('YYYY-MM-DD'));
                        $('#ModalAdd #end').val(moment(end).format('YYYY-MM-DD'));
                        $('#ModalAdd').modal('show');
                    },
                    eventRender: function (event, element) {
                        element.bind('dblclick', function () {
                            $('#ModalEdit #id').val(event.id);
                            $('#ModalEdit #title').val(event.title);
                            $('#ModalEdit #color').val(event.color);
                            $('#ModalEdit #starthora').val(event.starthora);
                            $('#ModalEdit #endhora').val(event.endhora);
                            $('#ModalEdit #creador').val(event.creador);
                            $('#ModalEdit').modal('show');
                        });
                    },
                    eventDrop: function (event, delta, revertFunc) { // si changement de position

                        edit(event);

                    },
                    eventResize: function (event, dayDelta, minuteDelta, revertFunc) { // si changement de longueur

                        edit(event);

                    },
                    events: [
            <% for (evento buscado:listadoEventosUsuario) {
                    int id_usuarioEvento = buscado.getId_usuario();
                    usuario creadorEvento = enlace.buscar_usuarioID(id_usuarioEvento);
                    String usuarioCreador = creadorEvento.getApellido() + " " + creadorEvento.getNombre();
            %>
                        {
                            id: "<%= buscado.getId_evento()%>",
                            title: "<%= buscado.getTitulo()%>",
                            start: "<%= buscado.getInicio()%>",
                            end: "<%= buscado.getFin()%>",
                            color: "<%= buscado.getColor()%>",
                            starthora: "<%= buscado.getHora_inicio()%>",
                            endhora: "<%= buscado.getHora_fin()%>",
                            estado: "<%= buscado.getId_estado()%>",
                            creador: "<%= usuarioCreador%>",
                            textColor: '#fff',
                        },
            <%}%>
            <% for (evento buscado:listadoEventosDireccion) {
                    int id_usuarioEvento = buscado.getId_usuario();
                    usuario creadorEvento = enlace.buscar_usuarioID(id_usuarioEvento);
                    String usuarioCreador = creadorEvento.getApellido() + " " + creadorEvento.getNombre();
            %>
                        {
                            id: "<%= buscado.getId_evento()%>",
                            title: "<%= buscado.getTitulo()%>",
                            start: "<%= buscado.getInicio()%>",
                            end: "<%= buscado.getFin()%>",
                            color: "<%= buscado.getColor()%>",
                            starthora: "<%= buscado.getHora_inicio()%>",
                            endhora: "<%= buscado.getHora_fin()%>",
                            estado: "<%= buscado.getId_estado()%>",
                            creador: "<%= usuarioCreador%>",
                            textColor: '#fff',
                        },
            <%}%>
                    ]
                    ,
                    googleCalendarApiKey: 'AIzaSyDZEfvaKjkcPn4fOKXGFJzbCfpfBIQLexU',
                    eventSources: [
                            {
                            googleCalendarId: 'es.ec#holiday@group.v.calendar.google.com'
                            },
                            <%
                                if(!listadoCalendario.isEmpty()){
                                for(calendario_google em:listadoCalendario){
                            %>
                                {
                            googleCalendarId: '<%= em.getNombre() %>'
                            },
                            <%}}%>
                    ]
                });

                function edit(event) {
                    start = event.start.format('YYYY-MM-DD');
                    if (event.end) {
                        end = event.end.format('YYYY-MM-DD');
                    } else {
                        end = start;
                    }

                    id = event.id;

                    Event = [];
                    Event[0] = id;
                    Event[1] = start;
                    Event[2] = end;

                    $.ajax({
                        url: 'administrar_evento.control?accion=modificarfecha&iu=<%= informacion.getId_usuario()%>',
                        type: "POST",
                        data: {id: id, start: start, end: end},
                        success: function (rep) {
                            if (rep == 'OK') {
                                swal("Mensaje", "Ocurrio algo con la acción que intento hacer!", {
                                    icon: "error",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-danger'
                                        }
                                    },
                                });
                            } else {
                                swal("Mensaje", "Se ha modificado su evento!", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                });
                            }
                        }
                    });
                }
            });
        </script>
        <!-- Page Specific JS File -->

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
    </body>
</html>