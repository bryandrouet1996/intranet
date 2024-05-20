<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.AprobacionViatico"%>
<%@page import="modelo.RechazoViatico"%>
<%@page import="modelo.RevisionViatico"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.ruta_viatico"%>
<%@page import="modelo.tipo_viatico"%>
<%@page import="modelo.viatico"%>
<%@page import="modelo.convocatoria"%>
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
    String funcion_usuario = null;
    usuario informacion = null;
    String codigo_direccion = null;
    String codigo_funcion = null;
    String tipo_usuario = null;
    ArrayList<viatico> listadoRegistados = null;
    ArrayList<viatico> listadoRevisados = null;
    ArrayList<viatico> listadoAprobados = null;
    ArrayList<viatico> listadoRechazados = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoRegistados = enlace.listadoViaticosUsuarioEstado(informacion.getId_usuario(), 1);
        listadoRevisados = enlace.listadoViaticosUsuarioEstado(informacion.getId_usuario(), 2);
        listadoAprobados = enlace.listadoViaticosUsuarioEstado(informacion.getId_usuario(), 3);
        listadoRechazados = enlace.listadoViaticosUsuarioEstado(informacion.getId_usuario(), 4);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Mis viaticos</title>
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
                            <h1>Viáticos</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="registro_viatico.jsp" class="btn btn-primary"> <i class="fas fa-plus"></i> Nueva solicitud</a>   
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Listado de solicitudes</h4>
                                        </div>
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-list"></i> Registradas <%if(listadoRegistados.size()!=0){%><span class="badge badge-primary"><%= listadoRegistados.size() %></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-check"></i> Revisadas <%if(listadoRevisados.size()!=0){%><span class="badge badge-primary"><%= listadoRevisados.size() %></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-check-double"></i> Aprobadas <%if(listadoAprobados.size()!=0){%><span class="badge badge-primary"><%= listadoAprobados.size() %></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="anul-tab" data-toggle="tab" href="#anul" role="tab" aria-controls="anul" aria-selected="false"><i class="fas fa-ban"></i> Rechazadas <%if(listadoRechazados.size()!=0){%><span class="badge badge-primary"><%= listadoRechazados.size() %></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-1">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código solicitud</th>
                                                                    <th>Fecha solicitud</th>
                                                                    <th>Tipo</th>
                                                                    <th>Descripción</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%if (!listadoRegistados.isEmpty()) {%>
                                                                <%for (viatico elem : listadoRegistados) {
                                                                    tipo_viatico el = enlace.buscarTipoViatico(elem.getId_tipo());
                                                                %>
                                                                <tr>
                                                                    <td>VT-<%= elem.getId_viatico() %></td>
                                                                    <td><%= elem.getFecha() %></td>
                                                                    <td><%= el.getDescripcion() %></td>
                                                                    <td><%= elem.getDescripcion_actividad() %></td>
                                                                    <td>
                                                                        <a href="registro_viatico.jsp?iv=<%= elem.getId_viatico() %>" type="button" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Editar" class="fas fa-edit"></i></a>
                                                                        <a href="reporte_viatico.control?tipo=licencia&id_viatico=<%= elem.getId_viatico() %>" target="_blank" type="button" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Imprimir" class="fas fa-print"></i></a>
                                                                        <a onclick="eliminarViatico(<%= elem.getId_viatico() %>)" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Eliminar" class="fa fa-times" style="color: white"></i></a>
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
                                                        <table class="table table-striped" id="table-2">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código solicitud</th>
                                                                    <th>Fecha solicitud</th>
                                                                    <th>Tipo</th>
                                                                    <th>Descripción</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%if (!listadoRevisados.isEmpty()) {%>
                                                                <%for (viatico elem : listadoRevisados) {                                                                    
                                                                    tipo_viatico el = enlace.buscarTipoViatico(elem.getId_tipo());
                                                                    RevisionViatico rev = enlace.getRevisionViatico(elem.getId_viatico());
                                                                    usuario solicitante = enlace.buscar_usuarioID(elem.getId_usuario()), revisado = enlace.buscar_usuarioID(rev.getIdUsuario());
                                                                %>
                                                                <tr>
                                                                     <td>VT-<%= elem.getId_viatico() %></td>
                                                                    <td><%= elem.getFecha() %></td>
                                                                    <td><%= el.getDescripcion() %></td>
                                                                    <td><%= elem.getDescripcion_actividad() %></td>
                                                                    <td>
                                                                        <a type="button" href="javascript:" data-toggle="modal" data-target="#modalRevision" data-id="<%= "VT-" + elem.getId_viatico() %>" data-fecha="<%= elem.getFecha() %>" data-fecharev="<%= rev.getFecha() %>" data-solicitante="<%= solicitante.getApellido() + " " + solicitante.getNombre() %>" data-revisado="<%= revisado.getApellido() + " " + revisado.getNombre() %>" data-motivo="<%= rev.getMotivo() %>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver revisión"></i></a>
                                                                        <a href="reporte_viatico.control?tipo=licencia&id_viatico=<%= elem.getId_viatico() %>" target="_blank" type="button" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Imprimir" class="fas fa-print"></i></a>
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
                                                        <table class="table table-striped" id="table-3">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código solicitud</th>
                                                                    <th>Fecha solicitud</th>
                                                                    <th>Tipo</th>
                                                                    <th>Descripción</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%if (!listadoAprobados.isEmpty()) {%>
                                                                <%for (viatico elem : listadoAprobados) {
                                                                    tipo_viatico el = enlace.buscarTipoViatico(elem.getId_tipo());
                                                                    AprobacionViatico ap = enlace.getAprobacionViatico(elem.getId_viatico());
                                                                    usuario solicitante = enlace.buscar_usuarioID(elem.getId_usuario()), aprobado = enlace.buscar_usuarioID(ap.getIdUsuario());
                                                                %>
                                                                <tr>
                                                                    <td>VT-<%= elem.getId_viatico() %></td>
                                                                    <td><%= elem.getFecha() %></td>
                                                                    <td><%= el.getDescripcion() %></td>
                                                                    <td><%= elem.getDescripcion_actividad() %></td>
                                                                    <td>
                                                                        <a type="button" href="javascript:" data-toggle="modal" data-target="#modalAprobacion" data-id="<%= "VT-" + elem.getId_viatico() %>" data-fecha="<%= elem.getFecha() %>" data-fechaap="<%= ap.getFecha() %>" data-solicitante="<%= solicitante.getApellido() + " " + solicitante.getNombre() %>" data-aprobado="<%= aprobado.getApellido() + " " + aprobado.getNombre() %>" data-motivo="<%= ap.getMotivo() %>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver aprobación"></i></a>
                                                                        <a href="reporte_viatico.control?tipo=licencia&id_viatico=<%= elem.getId_viatico() %>" target="_blank" type="button" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Imprimir" class="fas fa-print"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="anul" role="tabpanel" aria-labelledby="anul-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-4">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>Código solicitud</th>
                                                                    <th>Fecha solicitud</th>
                                                                    <th>Tipo</th>
                                                                    <th>Descripción</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%if (!listadoRechazados.isEmpty()) {%>
                                                                <%for (viatico elem : listadoRechazados) {
                                                                    tipo_viatico el = enlace.buscarTipoViatico(elem.getId_tipo());
                                                                    RechazoViatico rec = enlace.getRechazoViatico(elem.getId_viatico());
                                                                    usuario solicitante = enlace.buscar_usuarioID(elem.getId_usuario()), rechazado = enlace.buscar_usuarioID(rec.getIdUsuario());
                                                                %>
                                                                <tr>
                                                                    <td>VT-<%= elem.getId_viatico() %></td>
                                                                    <td><%= elem.getFecha() %></td>
                                                                    <td><%= el.getDescripcion() %></td>
                                                                    <td><%= elem.getDescripcion_actividad() %></td>
                                                                    <td>
                                                                        <a type="button" href="javascript:" data-toggle="modal" data-target="#modalRechazo" data-id="<%= "VT-" + elem.getId_viatico() %>" data-fecha="<%= elem.getFecha() %>" data-fecharec="<%= rec.getFecha() %>" data-solicitante="<%= solicitante.getApellido() + " " + solicitante.getNombre() %>" data-rechazado="<%= rechazado.getApellido() + " " + rechazado.getNombre() %>" data-motivo="<%= rec.getMotivo() %>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver rechazo"></i></a>
                                                                        <a href="reporte_viatico.control?tipo=licencia&id_viatico=<%= elem.getId_viatico() %>" target="_blank" type="button" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Imprimir" class="fas fa-print"></i></a>
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
                                                            
                <div class="modal fade" id="modalRevision" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label>Id viático</label>
                                        <input type="text" class="form-control" name="idmr" id="idmr" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de creación</label>
                                        <input type="text" class="form-control" name="fechamr" id="fechamr" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de revisión</label>
                                        <input type="text" class="form-control" name="fecharevmr" id="fecharevmr" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Solicitante</label>
                                        <input type="text" class="form-control" name="solicitantemr" id="solicitantemr" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Revisado por</label>
                                        <input type="text" class="form-control" name="revisadomr" id="revisadomr" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Motivo de revisión</label>
                                        <textarea type="text" class="form-control" name="motivomr" id="motivomr" readonly=""></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                                                            
                <div class="modal fade" id="modalAprobacion" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label>Id viático</label>
                                        <input type="text" class="form-control" name="idma" id="idma" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de creación</label>
                                        <input type="text" class="form-control" name="fechama" id="fechama" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de aprobación</label>
                                        <input type="text" class="form-control" name="fechaapma" id="fechaapma" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Solicitante</label>
                                        <input type="text" class="form-control" name="solicitantema" id="solicitantema" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Aprobado por</label>
                                        <input type="text" class="form-control" name="aprobadoma" id="aprobadoma" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Motivo de aprobación</label>
                                        <textarea type="text" class="form-control" name="motivoma" id="motivoma" readonly=""></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                                                            
                <div class="modal fade" id="modalRechazo" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <p class="small"></p>
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label>Id viático</label>
                                        <input type="text" class="form-control" name="idmrec" id="idmrec" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de creación</label>
                                        <input type="text" class="form-control" name="fechamrec" id="fechamrec" readonly="">
                                    </div>
                                    <div class="form-group col-md-5">
                                        <label>Fecha de rechazo</label>
                                        <input type="text" class="form-control" name="fecharecmrec" id="fecharecmrec" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Solicitante</label>
                                        <input type="text" class="form-control" name="solicitantemrec" id="solicitantemrec" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Rechazado por</label>
                                        <input type="text" class="form-control" name="rechazadomrec" id="rechazadomrec" readonly="">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Motivo de rechazo</label>
                                        <textarea type="text" class="form-control" name="motivomrec" id="motivomrec" readonly=""></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <footer class="main-footer">
                    <div class="footer-center">
                        Copyright &copy; 2019 <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal Cantón Esmeraldas - Dirección de Sistemas. All rights reserved.</a>
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
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-calendar.js"></script>
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        
        <script>
            function eliminarViatico(id) {
                swal({
                    title: '¿Desea eliminar este viático?',
                    text: "Una vez eliminado no se podrá recuperar",
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
                        $.post('administrar_viatico.control?accion=eliminar_viatico', {
                            id: id
                        }, function (responseText) {
                            if (responseText) {
                                swal("Viático eliminado", "El viático fue eliminado", {
                                    icon: "success",
                                    buttons: {
                                        confirm: {
                                            className: 'btn btn-success'
                                        }
                                    },
                                }).then(function () {
                                    location.href = "listado_viaticos.jsp";
                                });
                            } else {
                                swal("Error al eliminar", "El viático no fue eliminado", {
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
            
            $('#modalRevision').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                var fecha = button.data('fecha');
                var fechaRev = button.data('fecharev');
                var solicitante = button.data('solicitante');
                var revisado = button.data('revisado');
                var motivo = button.data('motivo');
                var modal = $(this);
                modal.find('.modal-body #idmr').val(id);
                modal.find('.modal-body #fechamr').val(fecha);
                modal.find('.modal-body #fecharevmr').val(fechaRev);
                modal.find('.modal-body #solicitantemr').val(solicitante);
                modal.find('.modal-body #revisadomr').val(revisado);
                modal.find('.modal-body #motivomr').val(motivo);
            })
            
            $('#modalAprobacion').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                var fecha = button.data('fecha');
                var fechaAp = button.data('fechaap');
                var solicitante = button.data('solicitante');
                var aprobado = button.data('aprobado');
                var motivo = button.data('motivo');
                var modal = $(this);
                modal.find('.modal-body #idma').val(id);
                modal.find('.modal-body #fechama').val(fecha);
                modal.find('.modal-body #fechaapma').val(fechaAp);
                modal.find('.modal-body #solicitantema').val(solicitante);
                modal.find('.modal-body #aprobadoma').val(aprobado);
                modal.find('.modal-body #motivoma').val(motivo);
            })
            
            $('#modalRechazo').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                var fecha = button.data('fecha');
                var fechaRec = button.data('fecharec');
                var solicitante = button.data('solicitante');
                var rechazado = button.data('rechazado');
                var motivo = button.data('motivo');
                var modal = $(this);
                modal.find('.modal-body #idmrec').val(id);
                modal.find('.modal-body #fechamrec').val(fecha);
                modal.find('.modal-body #fecharecmrec').val(fechaRec);
                modal.find('.modal-body #solicitantemrec').val(solicitante);
                modal.find('.modal-body #rechazadomrec').val(rechazado);
                modal.find('.modal-body #motivomrec').val(motivo);
            })
        </script>

    </body>
</html>
