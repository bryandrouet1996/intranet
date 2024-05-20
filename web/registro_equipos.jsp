<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : LT
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.pisos"%>
<%@page import="modelo.edificio"%>
<%@page import="modelo.arquitectura"%>
<%@page import="modelo.estado_conexion"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.rol_usuario"%>
<%@page import="modelo.organizacion"%>
<%@page import="modelo.tipo_equipo"%>
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
    String funcion_usuario = null;
    String codigo_direccion = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
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
        <title>Intranet Alcaldía - Registro de usuario</title>
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
                                <a href="#" class="dropdown-item has-icon">
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
                        ArrayList<tipo_equipo> listadoTipoEquipos = enlace.listadoTipoEquipos();
                        ArrayList<estado_conexion> listadoEstadoConexion = enlace.listadoEstadoConexion();
                        ArrayList<arquitectura> listadoArquitecturaSo = enlace.listadoArquitecturaSo();
                        ArrayList<edificio> listadoEdificio = enlace.listadoEdificio();
                        ArrayList<pisos> listadoPisos = enlace.listadoPisos();
                    %>
                    <section class="section">
                        <div class="section-header">
                            <h1>Registro de equipos</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <div class="form-row">
                                                <div class="form-group col-md-4">
                                                    <label>Nombre</label>
                                                    <input type="text" class="form-control" placeholder="Nombre equipo" name="txtnombre" id="txtnombre" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Usuario de dominio</label>
                                                    <input type="text" class="form-control" placeholder="Usuario" name="txtusuariodominio" id="txtusuariodominio" required>
                                                </div>
                                                <div class="form-group col-md-7">
                                                    <label>Tipo de dispositivo</label>
                                                    <select class="form-control select2" required name="combotipo" id="combotipo">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoTipoEquipos.size(); paso++) {%>
                                                        <option value="<%= listadoTipoEquipos.get(paso).getTipo()%>"><%= listadoTipoEquipos.get(paso).getTipo()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Mac address</label>
                                                    <input type="text" class="form-control" placeholder="mac address" name="txtmacaddress" id="txtmacaddress" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Memoria</label>
                                                    <input type="text" class="form-control" placeholder="RAM" name="txtmemoria" id="txtmemoria" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Procesador</label>
                                                    <input type="text" class="form-control" placeholder="procesador" name="txtprocesador" id="txtprocesador" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Dirección ip</label>
                                                    <input type="text" class="form-control" placeholder="IP" name="txtdireccion_ip" id="txtdireccion_ip" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Conexión DHCP</label>
                                                    <select class="form-control select2" required name="comboestado" id="comboestado">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getDhcp()%>"><%= listadoEstadoConexion.get(paso).getDhcp()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Conexión permanente</label>
                                                    <select class="form-control select2" required name="combopermanente" id="combopermanente">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getPermanente()%>"><%= listadoEstadoConexion.get(paso).getPermanente()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Antivirus</label>
                                                    <select class="form-control select2" required name="comboantivirus" id="comboantivirus">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getAntivirus()%>"><%= listadoEstadoConexion.get(paso).getAntivirus()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Cabildo</label>
                                                    <select class="form-control select2" required name="combocabildo" id="combocabildo">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getCabildo()%>"><%= listadoEstadoConexion.get(paso).getCabildo()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>SIG-AME</label>
                                                    <select class="form-control select2" required name="combosigame" id="combosigame">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getSigame()%>"><%= listadoEstadoConexion.get(paso).getSigame()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Office 365</label>
                                                    <select class="form-control select2" required name="combooffice" id="combooffice">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEstadoConexion.size(); paso++) {%>
                                                        <option value="<%= listadoEstadoConexion.get(paso).getOffice365()%>"><%= listadoEstadoConexion.get(paso).getOffice365()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Arquitectura S.O</label>
                                                    <select class="form-control select2" required name="comboarquitectura" id="comboarquitectura">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoArquitecturaSo.size(); paso++) {%>
                                                        <option value="<%= listadoArquitecturaSo.get(paso).getArquitectura_so()%>"><%= listadoArquitecturaSo.get(paso).getArquitectura_so()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Código de bodega</label>
                                                    <input type="text" class="form-control" placeholder="código" name="txtcodigo_bodega" id="txtcodigo_bodega" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Observaciones</label>
                                                    <input type="text" class="form-control" placeholder="obervacion" name="txtobservaciones" id="txtobservaciones" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Nombre de edificio</label>
                                                    <select class="form-control select2" required name="comboedificio" id="comboedificio">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoEdificio.size(); paso++) {%>
                                                        <option value="<%= listadoEdificio.get(paso).getNombre_edificio()%>"><%= listadoEdificio.get(paso).getNombre_edificio()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Piso</label>
                                                    <select class="form-control select2" required name="combopiso" id="combopiso">
                                                        <option>Seleccione</option>
                                                        <%for (int paso = 0; paso < listadoPisos.size(); paso++) {%>
                                                        <option value="<%= listadoPisos.get(paso).getNombre_piso()%>"><%= listadoPisos.get(paso).getNombre_piso()%>
                                                        </option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Unidad administrativa</label>
                                                    <input type="text" class="form-control" placeholder="unidad" name="txtunidad_administrativa" id="txtunidad_administrativa" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Funcionario</label>
                                                    <input type="text" class="form-control" placeholder="nombre" name="txtfuncionario" id="txtfuncionario" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-footer text-center">
                                            <button class="btn btn-primary" onclick="crearInventario()">Registrar equipo</button> 
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </section>
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
        
       <script> 
       function crearInventario(){
     var nombre = $('#txtnombre').val();
     var usuariodominio = $('#txtusuariodominio').val();
     var tipo = document.getElementById("combotipo").value;
     var macaddress = $('#txtmacaddress').val();
     var memoria = $('#txtmemoria').val();
     var procesador = $('#txtprocesador').val();
     var direccion_ip = $('#txtdireccion_ip').val();
     var estado = document.getElementById("comboestado").value;
     var permanente = document.getElementById("combopermanente").value;
     var antivirus = document.getElementById("comboantivirus").value;
     var cabildo = document.getElementById("combocabildo").value;
     var sigame = document.getElementById("combosigame").value;
     var office365 = document.getElementById("combooffice").value;
     var arquitectura_so = document.getElementById("comboarquitectura").value;
     var codigo_bodega = $('#txtcodigo_bodega').val();
     var observaciones = $('#txtobservaciones').val();
     var edificio = document.getElementById("comboedificio").value;
     var piso = document.getElementById("combopiso").value;
     var unidad_administrativa = $('#txtunidad_administrativa').val();
     var funcionario = $('#txtfuncionario').val();
     

    $.post('administrar_equipos.control?accion=registrar', {
        txtnombre: nombre,
        txtusuariodominio: usuariodominio,
        combotipo: tipo,
        txtmacaddress: macaddress,
        txtmemoria: memoria,
        txtprocesador: procesador,
        txtdireccion_ip: direccion_ip,
        comboestado: estado,
        combopermanente: permanente,
        comboantivirus: antivirus,
        combocabildo: cabildo,
        combosigame: sigame,
        combooffice: office365,
        comboarquitectura: arquitectura_so,
        txtcodigo_bodega: codigo_bodega,
        txtobservaciones: observaciones,
        comboedificio: edificio,
        combopiso: piso,
        txtunidad_administrativa: unidad_administrativa,
        txtfuncionario: funcionario
        

    }, function (responseText) {
        if (responseText) {
            swal("Mensaje", "Se creó registro de inventario con exito!!", {
                icon: "success",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                },
            }).then(function () {
                location.href = "administrar_inventario.jsp";
            });
        } else {
            swal("Mensaje", "Algo salio mal!!", {
                icon: "warning",
                buttons: {
                    confirm: {
                        className: 'btn btn-warning'
                    }
                },
            })
        }
    }, );
} 


</script>
    </body>
</html>
