<%-- 
    Document   : registro_actividad.jsp
    Created on : 27/04/2020, 15:16:57
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.participacion_viatico"%>
<%@page import="modelo.firma_viatico"%>
<%@page import="modelo.viatico"%>
<%@page import="modelo.ruta_viatico"%>
<%@page import="modelo.destino"%>
<%@page import="modelo.tipo_viatico"%>
<%@page import="modelo.banco"%>
<%@page import="modelo.compromiso_acta"%>
<%@page import="modelo.acta"%>
<%@page import="modelo.medio_acta"%>
<%@page import="modelo.convocatoria"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.actividad"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.evidencia_actividad"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    String codigo_funcion = null;
    String direccion_funcionario = null;
    int id_viatico = 0;
    ArrayList<banco> listadoBancos = null;
    ArrayList<tipo_viatico> listadoTipoViaticos = null;
    ArrayList<usuario> listadoUsuariosActivos = null;
    ArrayList<usuario> listadoMaximaAutoridades = null;
    ArrayList<usuario> listadoUsuariosUnidad = null;
    ArrayList<destino> listadoDestinos = null;
    usuario directorUnidad = null;
    ArrayList<ruta_viatico> listadoRutaViatico = null;
    ArrayList<participacion_viatico> listadoParticipantes = null;
    viatico solicitud = null;
    firma_viatico firma = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        direccion_funcionario = enlace.direccionPerteneceUsuario(codigo_funcion);
        listadoBancos = enlace.listadoBancos();
        listadoTipoViaticos = enlace.listadoTiposViaticos();
        listadoUsuariosActivos = enlace.listadoUsuariosActivos();
        listadoMaximaAutoridades = enlace.listadoMaximaAutoridadDelegado();
        listadoUsuariosUnidad = enlace.listadoUsuarioUnidad(codigo_funcion);
        directorUnidad = enlace.obtenerDirectorUsuario(codigo_funcion);
        listadoDestinos = enlace.listadoPaisesCiudades();
        if (request.getParameter("iv") != null) {
            id_viatico = Integer.parseInt(request.getParameter("iv"));
            listadoRutaViatico = enlace.listadoRutasViatico(id_viatico);
            solicitud = enlace.buscarViaticoID(id_viatico);
            firma = enlace.obtenerFirmaViaticoId(id_viatico);
            listadoParticipantes = enlace.listadoParticipantesViatico(id_viatico);
        }
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
        <title>Intranet Alcaldía - Registro de viático</title>
        <!-- General CSS Files -->
        <link rel="stylesheet" href="assets/modules/choices.js/choices.min.css" />
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/modules/fullcalendar/fullcalendar.min.css">
        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/multi-select.css">
        <link rel="stylesheet" href="assets/bootstrap-multiselect.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
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

    <body id="todo">
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
                        <%if (id_viatico == 0) {%>
                        <div class="section-header">
                            <h6><a href="listado_viaticos.jsp">Listado de viáticos</a> > Ingresar nueva solicitud</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formviatico" action="administrar_viatico.control?accion=viatico" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-12">
                                                            <h4>Información de la solicitud</h4>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>Nombre del servidor</label>
                                                            <input type="text" class="form-control" name="txtnombre" id="txtnombre" placeholder="Nombre del servidor" value="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" readonly="" required>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>Nombre de unidad del servidor</label>
                                                            <input type="text" class="form-control" name="txtunidad" id="txtunidad" placeholder="Nombre de unidad del servidor" value="<%= direccion_funcionario%>" required readonly="">
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Tipo de solicitud</label>
                                                            <select class="form-control " name="combotipoviatico" id="combotipoviatico" required>
                                                                <option disabled="" value="">Seleccione</option>
                                                                <%for (tipo_viatico tipovia : listadoTipoViaticos) {%>
                                                                <option value="<%= tipovia.getId_viatico()%>"><%= tipovia.getDescripcion()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Descripción de actividades a realizarse</label>
                                                            <textarea class="form-control" rows="5" name="areadescripcion" id="areadescripcion" required placeholder="Descripción de actividades a realizarse"></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12" hidden="">
                                                            <label>*Id viatico</label>
                                                            <input type="text" class="form-control"  name="txtidviatico" id="txtidviatico" value="0" required>
                                                        </div>
                                                        <div class="form-group col-md-2" hidden>
                                                            <label>Id usu</label>
                                                            <input type="text" class="form-control" name="txtiusu" id="txtiusu" value="<%= informacion.getId_usuario()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Servidores que integran el servicio institucional</label>
                                                            <select class="form-control select2" name="comboasistentes" id="comboasistentes" multiple="multiple" required >
                                                                <%for (usuario participa : listadoUsuariosActivos) {%>
                                                                <option value="<%= participa.getId_usuario()%>"><%= participa.getApellido()%> <%= participa.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Datos para la transferencia</h4>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Tipo de cuenta</label>
                                                            <select class="form-control " name="combotipocuenta" id="combotipocuenta" required>
                                                                <option value="">Seleccione</option>
                                                                <option value="Cuenta de ahorro">Cuenta de ahorro</option>
                                                                <option value="Cuenta corriente">Cuenta corriente</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Número de cuenta</label>
                                                            <input type="text" class="form-control" name="txtnumerocuenta" id="txtnumerocuenta" placeholder="Número de cuenta" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Nombre de entidad bancaria</label>
                                                            <select class="form-control select2" name="txtnombrebanco" id="txtnombrebanco" placeholder="Seleccione entidad bancaria" required >
                                                                <option value="">Seleccione entidad bancaria</option>
                                                                <%for (banco bancos : listadoBancos) {%>
                                                                <option value="<%= bancos.getNombre()%>"><%= bancos.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Transporte</h4>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <div class="card-header-action text-right mb-4">
                                                                <button type="submit" data-toggle="modal" data-target="#modalTransporte" class="btn btn-primary"><i class="fas fa-plus"></i> Añadir ruta</button>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table class="table table-striped" id="table-2">
                                                                    <thead>                                 
                                                                        <tr>
                                                                            <th>Tipo transporte</th>
                                                                            <th>Nombre de transporte</th>
                                                                            <th>Ruta</th>
                                                                            <th>Fecha salida</th>
                                                                            <th>Hora salida</th>
                                                                            <th>Acciones</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <%if (listadoRutaViatico != null) {%>
                                                                        <%for (ruta_viatico ruta : listadoRutaViatico) {
                                                                                String partida = enlace.buscarCiudadID(ruta.getId_lugarpartida());
                                                                                String llegada = enlace.buscarCiudadID(ruta.getId_lugarllegada());
                                                                                String rut = partida + "-" + llegada;
                                                                        %>
                                                                        <tr><%= ruta.getTipo_transporte()%></tr>
                                                                        <tr><%= ruta.getNombre_transporte()%></tr>
                                                                        <tr><%= rut%></tr>
                                                                        <tr><%= ruta.getFecha_salida()%></tr>
                                                                        <tr<%= ruta.getHora_salida()%></tr>
                                                                        <tr></tr>
                                                                        <%}%>
                                                                        <%}%>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Autorización</h4>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Autoridad nominadora o su delegado</label>
                                                            <select class="form-control" name="comboautoridad" id="comboautoridad" required>
                                                                <option disabled="" value="">Seleccione</option>
                                                                <%for (usuario autoridades : listadoMaximaAutoridades) {
                                                                        String funcion_autoridad = enlace.ObtenerFuncionUsuarioID(autoridades.getId_usuario());
                                                                %>
                                                                <%if (funcion_autoridad.equalsIgnoreCase("alcalde")) {%>
                                                                <option selected value="<%= autoridades.getId_usuario()%>"><%= autoridades.getApellido()%> <%= autoridades.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= autoridades.getId_usuario()%>"><%= autoridades.getApellido()%> <%= autoridades.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Representante de la unidad solicitante</label>
                                                            <select class="form-control " name="comboresponsable" id="comboresponsable" required>
                                                                <option disabled="" value="">Seleccione</option>
                                                                <%for (usuario func : listadoUsuariosUnidad) {%>
                                                                <%if (func.getId_usuario() == directorUnidad.getId_usuario()) {%>
                                                                <option selected="" value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>                                             
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%} else {%>
                        <div class="section-header">
                            <h6><a href="listado_viaticos.jsp">Listado de viáticos</a> > Actualizar solicitud</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formviatico" action="administrar_viatico.control?accion=viatico" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-12">
                                                            <h4>Información de la solicitud</h4>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>Nombre del servidor</label>
                                                            <input type="text" class="form-control" name="txtnombre" id="txtnombre" placeholder="Nombre del servidor" value="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" readonly="" required>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>Nombre de unidad del servidor</label>
                                                            <input type="text" class="form-control" name="txtunidad" id="txtunidad" placeholder="Nombre de unidad del servidor" value="<%= direccion_funcionario%>" required readonly="">
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Tipo de solicitud</label>
                                                            <select class="form-control " name="combotipoviatico" id="combotipoviatico" required>
                                                                <option value="">Seleccione</option>
                                                                <%for (tipo_viatico tipovia : listadoTipoViaticos) {%>
                                                                <%if (tipovia.getId_viatico() == solicitud.getId_tipo()) {%>
                                                                <option selected="" value="<%= tipovia.getId_viatico()%>"><%= tipovia.getDescripcion()%></option>
                                                                <%} else {%>
                                                                <option value="<%= tipovia.getId_viatico()%>"><%= tipovia.getDescripcion()%></option>                                                               
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Descripción de actividades a realizarse</label>
                                                            <textarea class="form-control" rows="5" name="areadescripcion" id="areadescripcion" required placeholder="Descripción de actividades a realizarse"><%= solicitud.getDescripcion_actividad()%></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12" hidden="">
                                                            <label>*Id viatico</label>
                                                            <input type="text" class="form-control"  name="txtidviatico" id="txtidviatico" value="<%= id_viatico%>" required>
                                                        </div>
                                                        <div class="form-group col-md-2" hidden>
                                                            <label>Id usu</label>
                                                            <input type="text" class="form-control" name="txtiusu" id="txtiusu" value="<%= informacion.getId_usuario()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Servidores que integran el servicio institucional</label>
                                                            <select class="form-control select2" name="comboasistentes" id="comboasistentes" multiple="multiple" required >
                                                                <%for (usuario participa : listadoUsuariosActivos) {%>
                                                                <%for (participacion_viatico part : listadoParticipantes) {%>
                                                                <%if (part.getId_usuario() == participa.getId_usuario()) {%>
                                                                <option selected value="<%= participa.getId_usuario()%>"><%= participa.getApellido()%> <%= participa.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= participa.getId_usuario()%>"><%= participa.getApellido()%> <%= participa.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Datos para transferencia</h4>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Tipo de cuenta</label>
                                                            <select class="form-control " name="combotipocuenta" id="combotipocuenta" required>
                                                                <option value="">Seleccione</option>
                                                                <%if (solicitud.getTipo_cuenta().equalsIgnoreCase("Cuenta de ahorro")) {%>
                                                                <option selected="" value="Cuenta de ahorro">Cuenta de ahorro</option>
                                                                <option value="Cuenta corriente">Cuenta corriente</option>
                                                                <%} else if (solicitud.getTipo_cuenta().equalsIgnoreCase("Cuenta corriente")) {%>
                                                                <option value="Cuenta de ahorro">Cuenta de ahorro</option>
                                                                <option selected="" value="Cuenta corriente">Cuenta corriente</option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Número de cuenta</label>
                                                            <input type="text" class="form-control" name="txtnumerocuenta" id="txtnumerocuenta" value="<%= solicitud.getNumero_cuenta()%>" placeholder="Número de cuenta" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Nombre de entidad bancaria</label>
                                                            <select class="form-control select2" name="txtnombrebanco" id="txtnombrebanco" placeholder="Seleccione entidad bancaria" required >
                                                                <option value="">Seleccione entidad bancaria</option>
                                                                <%for (banco bancos : listadoBancos) {%>
                                                                <%if (solicitud.getNombre_banco().equalsIgnoreCase(bancos.getNombre())) {%>
                                                                <option selected="" value="<%= bancos.getNombre()%>"><%= bancos.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= bancos.getNombre()%>"><%= bancos.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Transporte</h4>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <div class="card-header-action text-right mb-4">
                                                                <button type="submit" data-toggle="modal" data-target="#modalTransporte" class="btn btn-primary"><i class="fas fa-plus"></i> Añadir ruta</button>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table class="table table-striped" id="table-2">
                                                                    <thead>                                 
                                                                        <tr>
                                                                            <th>Tipo transporte</th>
                                                                            <th>Nombre de transporte</th>
                                                                            <th>Ruta</th>
                                                                            <th>Fecha salida</th>
                                                                            <th>Hora salida</th>
                                                                            <th>Acciones</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <%if (listadoRutaViatico != null) {%>
                                                                        <%for (ruta_viatico ruta : listadoRutaViatico) {
                                                                                String partida = enlace.buscarCiudadID(ruta.getId_lugarpartida());
                                                                                String llegada = enlace.buscarCiudadID(ruta.getId_lugarllegada());
                                                                                String rut = partida + "-" + llegada;
                                                                        %>
                                                                        <tr>
                                                                            <td><%= ruta.getTipo_transporte()%></td>
                                                                            <td><%= ruta.getNombre_transporte()%></td>
                                                                            <td><%= rut%></td>
                                                                            <td><%= ruta.getFecha_salida()%></td>
                                                                            <td><%= ruta.getHora_salida()%></td>
                                                                            <td>
                                                                                <a href="javascript:" type="button" onclick="eliminarRutaViatico(<%= ruta.getId_ruta()%>)" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Quitar"><i class="fas fa-times"></i></a>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                                        <%}%>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <h4>Autorización</h4>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Autoridad nominadora o su delegado</label>
                                                            <select class="form-control" name="comboautoridad" id="comboautoridad" required>
                                                                <option disabled="" value="">Seleccione</option>
                                                                <%for (usuario autoridades : listadoMaximaAutoridades) {
                                                                %>
                                                                <%if (firma.getId_autoridad() == autoridades.getId_usuario()) {%>
                                                                <option selected value="<%= autoridades.getId_usuario()%>"><%= autoridades.getApellido()%> <%= autoridades.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= autoridades.getId_usuario()%>"><%= autoridades.getApellido()%> <%= autoridades.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Representante de la unidad solicitante</label>
                                                            <select class="form-control " name="comboresponsable" id="comboresponsable" required>
                                                                <option disabled="" value="">Seleccione</option>
                                                                <%for (usuario func : listadoUsuariosUnidad) {%>
                                                                <%if (firma.getId_responsable() == func.getId_usuario()) {%>
                                                                <option selected="" value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%} else {%>
                                                                <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="botonfinalizar" class="card-footer text-right">
                                                    <%if(listadoRutaViatico.size()>=2){%>
                                                    <button class="btn btn-primary btn-block btn-lg" type="button" onclick="finalizarRegistroViatico()"><i class="fas fa-save"></i> Finalizar registro</button>
                                                    <%}else{%>
                                                    <button class="btn btn-primary btn-block btn-lg" type="button" onclick="alertarRutas()"><i class="fas fa-save"></i> Finalizar registro</button>
                                                    <%}%>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%}%>
                    </section>
                    <div class="modal fade" id="modalTransporte" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <span class="fw-extrabold">
                                            Añadir ruta
                                        </span>
                                    </h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <p class="small"></p>
                                    <%if (id_viatico == 0) {%>
                                    <form id="formtransporte" action="administrar_viatico.control?accion=registro_transporte" method="post" enctype="multipart/form-data">
                                        <%} else {%>
                                        <form id="formtransporte1" action="administrar_viatico.control?accion=registro_transporte" method="post" enctype="multipart/form-data">
                                            <%}%>
                                            <div class="form-row">
                                                <%if (id_viatico == 0) {%>
                                                <div class="form-group col-md-3" hidden="">
                                                    <label>Id viatico</label>
                                                    <input type="text" class="form-control" name="txtviatico" id="txtviatico" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <%} else {%>
                                                <div class="form-group col-md-3" hidden="">
                                                    <label>Id viatico</label>
                                                    <input type="text" class="form-control" name="txtviatico" id="txviatico" value="<%= id_viatico%>" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <%}%>
                                                <div class="form-group col-md-6">
                                                    <label>*Tipo de transporte</label>
                                                    <select class="form-control " name="combotipotransporte" id="combotipotransporte" required>
                                                        <option value="">Seleccione</option>
                                                        <option value="Aéreo">Aéreo</option>
                                                        <option value="Marítimo">Marítimo</option>
                                                        <option value="Terrestre">Terrestre</option>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>*Nombre de transporte</label>
                                                    <input type="text" class="form-control" name="txtnombretransporte" id="txtnombretransporte" placeholder="Nombre de la compañía de transporte" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Lugar de partida</label><br>
                                                    <select class="selectpicker show-menu-arrow row" id="combolugarpartida" name="combolugarpartida" data-style="form-control" data-live-search="true" title="Elija ciudad de partida">
                                                        <%for (destino lugar : listadoDestinos) {%>
                                                        <option value="<%= lugar.getId_destino()%>"><%= lugar.getNombre_pais() + "," + lugar.getNombre_ciudad()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Fecha de salida</label>
                                                    <input type="text" class="form-control datepicker" name="txtfechasalida" id="txtfechasalida" value="" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Hora de salida</label>
                                                    <input type="time" class="form-control" name="txthorasalida" id="txthorasalida" value="<%= enlace.hora_actual()%>" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Lugar de llegada</label><br>
                                                    <select class="selectpicker show-menu-arrow row" id="combolugardestino" name="combolugardestino" data-style="form-control" data-live-search="true" title="Elija ciudad de llegada">
                                                        <%for (destino lugar : listadoDestinos) {%>
                                                        <option value="<%= lugar.getId_destino()%>"><%= lugar.getNombre_pais() + "," + lugar.getNombre_ciudad()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Fecha de llegada</label>
                                                    <input type="text" class="form-control datepicker" name="txtfechallegada" id="txtfechallegada" value="" required>
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>*Hora de llegada</label>
                                                    <input type="time" class="form-control" name="txthorallegada" id="txthorallegada" value="<%= enlace.hora_actual()%>" required>
                                                </div>

                                            </div>
                                            <div class="modal-footer">
                                                <button type="submit" class="btn btn-primary">Registrar</button>
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
        <script src="assets/jquery.multi-select.js"></script>
        <script src="assets/bootstrap-multiselect.js"></script>
        <script type="text/javascript">
            $('.selectpicker').selectpicker({
                style: 'btn-default'
            });
        </script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>
        <script src="assets/select2-custom.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
        <!-- (Optional) Latest compiled and minified JavaScript translation files -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/i18n/defaults-*.min.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-calendar.js"></script>
        <!-- JS Libraies -->
        <script src="assets/modules/izitoast/js/iziToast.min.js"></script>
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/funciones_viaticos.js" type="text/javascript"></script>
        
        <script>
            function eliminarRutaViatico(id_ruta) {                
                swal({
                    title: '¿Desea eliminar esta ruta?',
                    text: "Al eliminar la ruta no podrá recuperarla",
                    type: 'warning',
                    buttons: {
                        cancel: {
                            visible: true,
                            text: 'No, cancelar',
                            className: 'btn btn-danger'
                        },
                        confirm: {
                            text: 'Sí, eliminar',
                            className: 'btn btn-success'
                        }
                    }
                }).then((willDelete) => {
                    if (willDelete) {
                        $.post('administrar_viatico.control?accion=eliminar_ruta', {
                            iu: id_ruta
                        }, function (responseText) {
                            if (responseText) {
                                swal("Ruta eliminada", "La ruta fue eliminada", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                }).then(function () {
                                    location.href = "registro_viatico.jsp?iv="+<%= id_viatico %>;
                                });
                            } else {
                                swal("Error al eliminar", "La ruta no fue eliminada", {
                                    icon: "warning",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-warning'
                                        }
                                    },
                                });
                            }
                        }, );
                    } else {
                        swal("Se canceló la acción", {
                            buttons: {
                                confirm: {
                                    className: 'btn btn-success'
                                }
                            }
                        });
                    }
                });
            }
            
            function alertarRutas() {
                swal("Rutas de transporte incompletas", "Debe registrar al menos 2 rutas de transporte para finalizar el registro del viático.", {
                    icon: "error",
                    buttons: {
                        confirm: {
                            className: 'btn btn-success'
                        }
                    }
                });
            }
        </script>
    </body>
</html>
