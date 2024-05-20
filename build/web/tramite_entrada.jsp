<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="java.sql.Date"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.tipo_tramite"%>
<%@page import="modelo.tramite"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
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
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<tramite> listadoNuevos = null, listadoLeidos = null;
    java.sql.Date fecha_inicio = null, fecha_fin = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        listadoNuevos = request.getParameter("txtini") == null ? enlace.getListadoTramitesEntrada(informacion.getId_usuario(), 0) : enlace.getListadoTramitesEntrada(informacion.getId_usuario(), 0, fecha_inicio, fecha_fin);
        listadoLeidos = request.getParameter("txtini") == null ? enlace.getListadoTramitesEntrada(informacion.getId_usuario(), 1) : enlace.getListadoTramitesEntrada(informacion.getId_usuario(), 1, fecha_inicio, fecha_fin);
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
        <title>Intranet Alcaldía - Listado de trámites recibidos</title>
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
                    <section class="section">
                        <div class="section-header">
                            <h1>Bandeja de entrada de trámites</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
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
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="true"><i class="fas fa-exchange-alt"></i> Nuevos <%if(listadoNuevos.size()!=0){%><span class="badge badge-primary"><%= listadoNuevos.size() %></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="leidos-tab" data-toggle="tab" href="#leidos" role="tab" aria-controls="leidos" aria-selected="false"><i class="fas fa-check"></i> Leídos <%if(listadoLeidos.size()!=0){%><span class="badge badge-primary"><%= listadoLeidos.size() %></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código</th>
                                                                    <th>Fecha Elaboración</th>
                                                                    <th>Núm. Memo/Oficio</th>
                                                                    <th>Tipo</th>
                                                                    <th>Remitente</th>
                                                                    <th>Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                               <%for(tramite t : listadoNuevos){
                                                                    usuario envia = enlace.buscar_usuarioID(t.getId_envia());
                                                                %>
                                                                <tr>
                                                                    <td><%= "TRA-" + t.getId_tramite() %></td>
                                                                    <td><%= t.getFecha_elaboracion() %></td>
                                                                    <td><%= t.getNumero_memorando() %></td>
                                                                    <td><%=enlace.getTipoTramite(t.getTipo_tramite()).getDescripcion()%></td>
                                                                    <td><%= envia.getNombre() %> <%= envia.getApellido() %></td>
                                                                    <td>
                                                                        <a href="javascript:" data-toggle="modal" data-target="#modalDetalleTramite" class="btn btn-primary btn-sm" data-fecha="<%= t.getFecha_elaboracion()%>" data-hora="<%= t.getHora_elaboracion()%>" data-numero="<%= t.getNumero_memorando()%>" data-tipo="<%= enlace.getTipoTramite(t.getTipo_tramite()).getDescripcion()%>" data-asun="<%= t.getAsunto()%>" data-hash="<%= t.getHash() != null ? t.getHash() : "No existe etiqueta de seguridad para el documento"%>" data-remi="<%= envia.getNombre() + " " + envia.getApellido()%>"><i class="fas fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a href="administrar_tramite.control?accion=descargar_documento&id_tramite=<%= t.getId_tramite() %>" target="_blank" class="btn btn-primary btn-sm" ><i class="fas fa-cloud-download-alt" data-toggle="tooltip" data-original-title="Descargar"></i></a>
                                                                        <a type="button" onclick="marcarLeido(<%= t.getId_tramite()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Marcar como leído"></i></a>
                                                                        <a href="javascript:" data-toggle="modal" data-target="#modalDevolver" class="btn btn-primary btn-sm" data-id="<%= t.getId_tramite()%>" ><i class="fas fa-times" data-toggle="tooltip" data-original-title="Devolver"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>                                                                
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="leidos" role="tabpanel" aria-labelledby="leidos-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xx">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código</th>
                                                                    <th>Fecha Elaboración</th>
                                                                    <th>Núm. Memo/Oficio</th>
                                                                    <th>Tipo</th>
                                                                    <th>Remitente</th>
                                                                    <th>Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                               <%for(tramite t : listadoLeidos){
                                                                    usuario envia = enlace.buscar_usuarioID(t.getId_envia());
                                                                %>
                                                                <tr>
                                                                    <td><%= "TRA-" + t.getId_tramite() %></td>
                                                                    <td><%= t.getFecha_elaboracion() %></td>
                                                                    <td><%= t.getNumero_memorando() %></td>
                                                                    <td><%=enlace.getTipoTramite(t.getTipo_tramite()).getDescripcion()%></td>
                                                                    <td><%= envia.getNombre() %> <%= envia.getApellido() %></td>
                                                                    <td>
                                                                        <a href="javascript:" data-original-title="Ver detalles" data-toggle="modal" data-target="#modalDetalleTramite" class="btn btn-primary btn-sm" data-fecha="<%= t.getFecha_elaboracion()%>" data-hora="<%= t.getHora_elaboracion()%>" data-numero="<%= t.getNumero_memorando()%>" data-tipo="<%= enlace.getTipoTramite(t.getTipo_tramite()).getDescripcion()%>" data-asun="<%= t.getAsunto()%>" data-hash="<%= t.getHash() != null ? t.getHash() : "No existe etiqueta de seguridad para el documento"%>" data-remi="<%= envia.getNombre() + " " + envia.getApellido()%>"><i class="fas fa-eye" ></i></a>
                                                                        <a href="administrar_tramite.control?accion=descargar_documento&id_tramite=<%= t.getId_tramite() %>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Descargar"><i class="fas fa-cloud-download-alt"></i></a>
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
                            </div>
                        </div>
                    </section>
                </div>
                <div class="modal fade" id="modalDetalleTramite" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Detalle de trámite
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
                                        <div class="form-group col-md-3">
                                            <label>Fecha de elaboración</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="fecha" id="fecha" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label>Hora</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="hora" id="hora" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-4">
                                            <label>Número de memo/oficio</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="numero" id="numero" required="" readonly="">
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label>Tipo de trámite</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="tipo" id="tipo" required="" readonly="">
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Asunto</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente la problemática." name="asun" id="asun" required="" readonly=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ninguna fecha
                                            </div>
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Remitente</label>
                                            <input type="text" class="form-control" placeholder="Detalle claramente la problemática." name="remi" id="remi" required="" readonly="">
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
                <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDevolver">
                    <div class="modal-dialog modal-lg" role="dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-center">
                                    <span class="fw-extrabold">
                                        Devolver trámite
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formdevolver" action="administrar_tramite.control?accion=devolver" method="post" enctype="multipart/form-data" class="needs-validation">
                                    <div class="form-row">
                                        <div class="form-group col-md-2" hidden="">
                                            <input type="text" class="form-control" name="id" id="id">
                                        </div>
                                        <div class="form-group col-md-12">
                                            <label>Motivo</label>
                                            <textarea type="text" class="form-control" placeholder="Detalle claramente el motivo de la devolución" name="motivo" id="motivo" required=""></textarea>
                                            <div class="invalid-feedback">
                                                No ha ingresado el motivo
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" value="Upload" class="btn btn-primary" id="btnDevolver">Devolver</button>
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
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        
        <script type="text/javascript">
            $("#table-x").dataTable({
                "ordering": true,
                "order": [ 0, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0,1] }
                ],"pageLength": 25,
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
                    ,{
                      extend: 'print',
                      text: 'Imprimir <i class="fas fa-print"></i>'
                    }
                  ],
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
              
            $("#table-xx").dataTable({
                "ordering": true,
                "order": [ 0, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0,1] }
                ],"pageLength": 25,
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
                    ,{
                      extend: 'print',
                      text: 'Imprimir <i class="fas fa-print"></i>'
                    }
                  ],
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
            
            $('#modalDetalleTramite').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var fecha = button.data('fecha');
                var hora = button.data('hora');
                var numero = button.data('numero');
                var tipo = button.data('tipo');
                var asun = button.data('asun');
                var hash = button.data('hash');
                var remi = button.data('remi');
                var modal = $(this);
                modal.find('.modal-body #fecha').val(fecha);
                modal.find('.modal-body #hora').val(hora);
                modal.find('.modal-body #numero').val(numero);
                modal.find('.modal-body #tipo').val(tipo);
                modal.find('.modal-body #asun').val(asun);
                modal.find('.modal-body #hash').val(hash);
                modal.find('.modal-body #remi').val(remi);
            });
            
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
                
               function marcarLeido(id_tramite) {
                    Swal.fire({
                        title: 'Confirmación',
                        text: "¿Desea marcar este trámite como leído?",
                        icon: 'warning',
                        buttonsStyling: false,
                        showCancelButton: true,
                        confirmButtonText: 'Sí, marcar',
                        cancelButtonText: 'No, cancelar',
                        customClass: {
                            confirmButton: 'btn btn-success',
                            cancelButton: 'btn btn-danger'
                        }
                    }).then((willDelete) => {
                        if (willDelete.isConfirmed) {
                            Swal.fire({
                                title: 'Marcando como leído',
                                text: 'Por favor espere',
                                timerProgressBar: true,
                                showConfirmButton: false,
                                allowOutsideClick: () => !Swal.isLoading(),
                                allowEscapeKey: () => !Swal.isLoading(),
                                didOpen: () => {
                                    Swal.showLoading();
                                }
                            })
                            $.post('administrar_tramite.control?accion=marcar_leido', {
                                id: id_tramite
                            }, function (responseText) {
                                if (responseText) {
                                    Swal.fire({
                                        title: "Marcado como leído",
                                        icon: "success",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    }).then(function () {
                                        location.href = "tramite_entrada.jsp";
                                    });
                                } else {
                                    Swal.fire({
                                        title: "Error al marcar",
                                        text: "No se marcó como leído",
                                        icon: "error",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    }).then(function () {
                                        location.href = "tramite_entrada.jsp";
                                    });
                                }
                            }, ).fail(function () {
                                Swal.fire({
                                    title: "Error crítico",
                                    text: "No se marcó como leído",
                                    icon: "error",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "tramite_entrada.jsp";
                                });
                            });
                        } else {
                            Swal.fire({
                                title: "Acción cancelada",
                                text: "Se canceló la acción",
                                icon: "warning",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            });
                        }
                    });
                }
                
                $('#modalDevolver').on('show.bs.modal', function (event) {
                    var button = $(event.relatedTarget);
                    var id = button.data('id');
                    var modal = $(this);
                    modal.find('.modal-body #id').val(id);
                })
                
                $(document).ready(function () {
                    $(document).ready(function () {
                        $('#formdevolver').submit(function (event) {
                            document.getElementById('btnDevolver').hidden = true;
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
                                        title: 'Devolviendo trámite',
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
                                            title: "Trámite devuelto",
                                            icon: "success",
                                            buttonsStyling: false,
                                            customClass: {
                                                confirmButton: 'btn btn-success'
                                            }
                                        }).then(function () {
                                            location.href = "tramite_entrada.jsp";
                                        });
                                    } else {
                                        Swal.fire({
                                            title: "Error",
                                            text: "No se devolvió el trámite",
                                            icon: "error",
                                            buttonsStyling: false,
                                            customClass: {
                                                confirmButton: 'btn btn-success'
                                            }
                                        }).then(function () {
                                            location.href = "tramite_entrada.jsp";
                                        });
                                    }
                                },
                                error: function () {
                                    Swal.fire({
                                        title: 'Error crítico',
                                        text: 'No se devolvió el trámite',
                                        icon: "error",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    }).then(function () {
                                        location.href = "tramite_entrada.jsp";
                                    });
                                }
                            });
                            return false;
                        });
                    });
                });
        </script>
        
    </body>
</html>
