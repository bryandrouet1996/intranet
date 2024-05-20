<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.calendario_google"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    conexion_oracle oracle = new conexion_oracle();
    int id = 0;
    usuario informacion = null;
    String funcion_usuario = null;
    String codigo_funcion = null;
    String tipo_usuario = null;
    String codigo_direccion = null;
    foto_usuario foto = null;
    String firma=null;
    ArrayList<calendario_google> listadoCalendario=null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        firma=enlace.buscarFirmaUsuarioID(informacion.getId_usuario());
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoCalendario=enlace.listadoCalendariosGoogleUsuario(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Configuración de cuenta</title>
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
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">
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
                    <%
                        String[] nombre = informacion.getNombre().split(" ");
                    %>
                    <section class="section">
                        <div class="section-header">
                            <h1>Configuración de cuenta</h1>
                        </div>
                        <div class="section-body">
                            <h2 class="section-title">Hola, <%= nombre[0]%></h2>
                            <p class="section-lead">
                                Aquí puede cambiar la información de su cuenta institucional.
                            </p>
                            <div class="row">
                                <div class="col-12 col-md-12 col-lg-5">
                                    <div class="card author-box card-primary">
                                        <div class="card-header">
                                            <div class="author-box-name">
                                                <a href="#"><%= informacion.getApellido()%> <%= informacion.getNombre()%></a>
                                                <div class="author-box-job">
                                                    <%= oracle.consultarUnidadUsuario(Integer.parseInt(informacion.getCodigo_usuario()))%>
                                                </div>
                                                <div class="author-box-job">
                                                    <%= oracle.consultarDenominacionUsuario(Integer.parseInt(informacion.getCodigo_usuario()))%>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body">
                                            <div class="author-box-left" id="foto_us">
                                                <%if (foto.getNombre().equalsIgnoreCase("ninguno") && foto.getRuta().equalsIgnoreCase("ninguno")) {%>
                                                <img alt="image" src="assets/img/avatar/avatar-1.png" class="rounded-circle author-box-picture">
                                                <%} else {%>
                                                <img alt="image" src="imagen.control?id=<%= informacion.getId_usuario()%>" class="rounded-circle author-box-picture">
                                                <%}%>
                                                <div class="clearfix"></div>
                                                <a class="btn btn-primary mt-3 active" data-toggle="modal" data-target="#modalcontrasena"><i class="fa fa-unlock"></i> Cambiar contraseña</a>
                                            </div>
                                            <div class="author-box-details">
                                                <div class="author-box-description">
                                                    <%= oracle.consultarDireccionUsuario(Integer.parseInt(informacion.getCodigo_usuario()))%>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-12 col-lg-7">
                                    <div class="card">
                                        <form id="formfoto" action="administrar_usuario.control?accion=foto_usuario" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                            <div class="card-header">
                                                <h4>Mis datos</h4>
                                            </div>
                                            <div class="card-body">
                                                <div class="row">    
                                                    <div class="form-group col-md-6 col-12" hidden>
                                                        <label>Nombres completos</label>
                                                        <input type="text" class="form-control" id="iu" name="iu" value="<%= informacion.getId_usuario()%>" required="" readonly>
                                                        <div class="invalid-feedback">
                                                            Please fill in the first name
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6 col-12">
                                                        <label>Nombres completos</label>
                                                        <input type="text" class="form-control" value="<%= informacion.getApellido()%> <%= informacion.getNombre()%>" required="" readonly>
                                                        <div class="invalid-feedback">
                                                            Please fill in the first name
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6 col-12">
                                                        <label>Correo institucional</label>
                                                        <input type="email" class="form-control" value="<%= informacion.getCorreo()%>" required="" readonly>
                                                        <div class="invalid-feedback">
                                                            Please fill in the email
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6 col-12">
                                                        <label>Foto</label>
                                                        <input type="file" class="form-control" id="txtfoto" name="txtfoto" required="">
                                                        <div class="invalid-feedback">
                                                            Please fill in the email
                                                        </div>
                                                        <a href="#" class="btn btn-icon icon-left active"> 
                                                            <span class="btn-label just-icon">Tamaño de imagen (max:2mb)</span>                                          
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card-footer text-right">
                                                <button class="btn btn-primary">Guardar cambios</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="col-12 col-md-12 col-lg-6">
                                    <div class="card">
                                        <form method="post" class="needs-validation" novalidate="">
                                            <div class="card-header">
                                                <h4>Configuración de telegram</h4>
                                                    <a class="btn btn-icon icon-left active" id="alertTelegram"> 
                                                    <span data-toggle="tooltip" data-original-title="Ayuda"><i class="fas fa-question-circle"></i></span>
                                                </a>
                                            </div>
                                            <div class="card-body">
                                                <div class="row">                               
                                                    <div class="form-group col-md-12 col-12">
                                                        <label class="custom-switch mt-2">
                                                            <span class="custom-switch-description">ChatID </span>
                                                            <%if (enlace.estadoTelegramUsuario(informacion.getId_usuario())) {%>
                                                            <input type="checkbox" name="swichtelegram" id="swichtelegram" class="custom-switch-input" checked>
                                                            <%} else {%>
                                                            <input type="checkbox" name="swichtelegram" id="swichtelegram" class="custom-switch-input">
                                                            <%}%>
                                                            <span class="custom-switch-indicator"></span>
                                                        </label>
                                                        <br>
                                                        <br>
                                                        <input type="text" name="txtchat" id="txtchat" class="form-control" value="<%= enlace.buscarChatidUsuario(informacion.getId_usuario())%>" required="">
                                                    </div>
                                                    <a href="#" class="btn btn-icon icon-left active"> 
                                                        <span class="btn-label just-icon"><i class="fas fa-play"></i> ¿Como obtengo mi ChatID?</span>                                          
                                                    </a>
                                                </div>
                                            </div>
                                            <div class="card-footer text-right">
                                                <button class="btn btn-primary" type="button" onclick="cambiarChadID(<%= informacion.getId_usuario()%>)">Guardar cambios</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="col-12 col-md-12 col-lg-6">
                                    <div class="card">
                                        <form method="post" class="needs-validation" novalidate="">
                                            <div class="card-header">
                                                <h4>Configuración de Webmail</h4>
                                                <a class="btn btn-icon icon-left active" id="alertWebmail"> 
                                                    <span class="btn-label just-icon" data-toggle="tooltip" data-original-title="Ayuda"><i class="fas fa-question-circle"></i></span>
                                                </a>
                                            </div>
                                            <div class="card-body">
                                                <div class="row">                               
                                                    <div class="form-group col-md-12 col-12">
                                                        <label>Contraseña de Webmail</label>
                                                        <input type="password" class="form-control" name="txtclavemail" id="txtclavemail" value="<%= enlace.webmailClaveUsuario(informacion.getId_usuario())%>" required="">
                                                        <div class="invalid-feedback">
                                                            Please fill in the first name
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card-footer text-right">
                                                <button type="button" onclick="cambiarClaveMail(<%= informacion.getId_usuario()%>)" class="btn btn-primary">Guardar cambios</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="col-12 col-md-12 col-lg-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Firma</h4>
                                            Se recomienda subir captura de su firma con tinta azul y trazada en papel blanco.
                                            <a class="btn btn-icon icon-left active" id="alertFirma"> 
                                                <span class="btn-label just-icon" data-toggle="tooltip" data-original-title="Ayuda"><i class="fas fa-question-circle"></i></span>
                                            </a>
                                        </div>
                                        <div class="card-body">
                                            <table class="table table-sm" id="tablafirma">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Imagen</th>
                                                        <th scope="col">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td>
                                                            <%if(firma.equalsIgnoreCase("ninguno")){%>
                                                            <img alt="Sin imagen" src="assets/img/vacio.jpg" class="rounded-circle" width="35" data-toggle="tooltip" title="Firma digital">
                                                            <%}else{%>
                                                            <img alt="image" src="firma.control?iu=<%= informacion.getId_usuario() %>" class="rounded-circle" width="35" data-toggle="tooltip" title="Firma digital">
                                                            <%}%>
                                                        </td>
                                                        <td>
                                                            <a class="btn active btn-sm btn-primary" data-toggle="modal" data-target="#modalfirma"><i data-toggle="tooltip" data-original-title="Actualizar firma" class="fas fa-pen-alt"></i></a>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-md-12 col-lg-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Calendarios Google</h4>
                                            <a href="javascript:" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalCalendario"><i class="fas fa-plus"></i> Añadir nuevo</a>
                                            <a class="btn btn-icon icon-left active" id="alertCalendario"> 
                                                <span class="btn-label just-icon" data-toggle="tooltip" data-original-title="Ayuda"><i class="fas fa-question-circle"></i></span>
                                            </a>
                                        </div>
                                        <div class="card-body">
                                            <table class="table table-sm" id="tablacalendario">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Nombre</th>
                                                        <th scope="col">Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                   <%if(!listadoCalendario.isEmpty()){%>
                                                    <%for(calendario_google elem:listadoCalendario){%>
                                                    <tr>
                                                        <td>
                                                            <%= elem.getNombre() %>
                                                        </td>
                                                        <td>
                                                            <a type="button" onclick="eliminarCalendario(<%= elem.getId_calendario() %>) " class="btn active btn-sm btn-primary"><i data-toggle="tooltip" data-original-title="Quitar" class="fas fa-trash"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    <%}else{%>
                                                    <tr>
                                                        
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </section>
                </div>
                <div class="modal fade" id="modalcontrasena" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-sm" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Cambio de contraseña
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form target="_blank" action="" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden>
                                            <label>Id usuario</label>
                                            <input type="text" class="form-control" name="txtidusuario" id="txtidusuario" required="" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Contraseña actual</label>
                                            <input type="password" class="form-control" placeholder="Ingrese su contraseña actual" name="txtcontraactual" id="txtcontraactual" required="">
                                            <div class="invalid-feedback">
                                                No ha ingresado ninguna información
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Nueva contraseña</label>
                                            <input type="password" class="form-control" placeholder="Ingrese su nueva contraseña" name="txtnuevacontra" id="txtnuevacontra" required="">
                                            <div class="invalid-feedback">
                                                No ha ingresado ninguna información
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Repita nueva contraseña</label>
                                            <input type="password" class="form-control" placeholder="Repita su nueva contraseña" name="txtrepitacontra" id="txtrepitacontra" required="">
                                            <div class="invalid-feedback">
                                                No ha ingresado ninguna información
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" onclick="cambiarContrasenaUsuario()" data-dismiss="modal" class="btn btn-primary">Guardar cambios</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modalfirma" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Actualizar firma digital
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formfirma" action="administrar_usuario.control?accion=firma_usuario" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden>
                                            <label>Id usuario</label>
                                            <input type="text" class="form-control" name="txtidusuario11" id="txtidusuario" required="" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Firma</label>
                                            <input type="file" class="form-control" placeholder="Ingrese su contraseña actual" name="txtfirma" id="txtfirma" required="">
                                            <div class="invalid-feedback">
                                                No ha ingresado ninguna información
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Guardar cambios</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modalCalendario" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Añadir calendario
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form target="_blank" action="" class="needs-validation" novalidate="" method="post">
                                    <div class="form-row">
                                        <div class="form-group col-md-6" hidden>
                                            <label>Id usuario</label>
                                            <input type="text" class="form-control" name="txtidusuario11" id="txtidusuario11" required="" value="<%= informacion.getId_usuario()%>">
                                            <div class="invalid-feedback">
                                                What's your name?
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Nombre</label>
                                            <input type="text" class="form-control" placeholder="Ingrese nombre de calendario" name="txtcalendario" id="txtcalendario" required="">
                                            <div class="invalid-feedback">
                                                No ha ingresado ninguna información
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" onclick="registroCalendario()" data-dismiss="modal" class="btn btn-primary">Guardar cambios</button>
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
        <script src="assets/modules/datatables/datatables.min.js"></script>
        <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
        <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/index-0.js"></script>
        <script src="assets/js/page/modules-datatables.js"></script>
        <!-- JS Libraies -->
        <script src="assets/modules/fullcalendar/fullcalendar.min.js"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
        <script src="assets/modules/izitoast/js/iziToast.min.js"></script>
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-calendar.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/funciones_usuario.js" type="text/javascript"></script>
    </body>
</html>