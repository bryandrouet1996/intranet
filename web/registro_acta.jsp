<%-- 
    Document   : registro_actividad.jsp
    Created on : 27/04/2020, 15:16:57
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
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
    String funcion_usuario = null;
    String codigo_direccion = null;
    String codigo_funcion = null;
    int id_convocatoria = 0;
    convocatoria conv = null;
    ArrayList<usuario> listadoDirectores = null;
    ArrayList<usuario> listadoConvocados = null;
    ArrayList<usuario> listadoAsistenciaActa = null;
    ArrayList<medio_acta> listadoMedios = null;
    ArrayList<compromiso_acta> listadoCompromisos = null;
    int id_acta = 0;
    acta act= null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        if (request.getParameter("iact") != null) {
            id_acta = Integer.parseInt(request.getParameter("iact"));
            act = enlace.buscarActaID(id_acta);
            listadoAsistenciaActa = enlace.listadoAsistenciaActa(id_acta);
            listadoCompromisos = enlace.listadoCompromisosActa(id_acta);
        }
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoDirectores = enlace.listadosDirectoresDireccionesUnidades();
        id_convocatoria = Integer.parseInt(request.getParameter("ic"));
        conv = enlace.buscarConvocatoriaID(id_convocatoria);
        listadoMedios = enlace.listadoMediosActa();
        listadoConvocados = enlace.listadoConvocadosIdConvocatoria(id_convocatoria);
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
        <title>Intranet Alcaldía - Registro de convocatoria</title>
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
                        <%if (id_acta == 0) {%>
                        <div class="section-header">
                            <h6><a href="listado_actas.jsp">Listado actas</a> > Ingresar nueva acta</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formacta" action="administrar_acta.control?accion=registro_acta" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-2" hidden="">
                                                            <label>*id convocatoria</label>
                                                            <input type="text" class="form-control" name="txtconvoca" id="txtconvoca" value="<%= conv.getId_convocatoria() %>" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha de acta</label>
                                                            <input type="text" class="form-control datepicker" name="txtfechaacta" id="txtfechaacta" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha convocatoria</label>
                                                            <input type="text" class="form-control " name="txtfechaconvocatoria" id="txtfechaconvocatoria" value="<%= conv.getFecha_convocatoria()%>" required readonly="">
                                                        </div>
                                                        <div class="form-group col-md-5">
                                                            <label>*Asunto</label>
                                                            <input type="text" class="form-control" name="txtasunto" id="txtasunto" required="" placeholder="Ingrese una breve descripción" value="<%= conv.getAsunto()%>">
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <label>*Medio</label>
                                                            <select class="form-control" name="combomedio" id="combomedio" required="">
                                                                <%for (medio_acta med : listadoMedios) {%>
                                                                <%if (conv.getId_medio() == med.getId_medio()) {%>
                                                                <option selected="" value="<%= med.getId_medio()%>"><%= med.getDescripcion()%></option>
                                                                <%} else {%>
                                                                <option value="<%= med.getId_medio()%>"><%= med.getDescripcion()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Hora de inicio</label>
                                                            <input type="time" class="form-control" name="txthorainicio" id="txthorainicio" required value="<%= conv.getHora_inicio()%>">
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Hora de fin</label>
                                                            <input type="time" class="form-control" onchange="validarhoras()" name="txthorafin" id="txthorafin" value="<%= conv.getHora_fin()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-8">
                                                            <label>*Lugar</label>
                                                            <input type="text" class="form-control"  name="txtlugar" id="txtlugar" value="<%= conv.getLugar()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Orden del día</label>
                                                            <textarea class="form-control" rows="5" name="areaobservacion" id="areaobservacion" required placeholder="Detalle el orden del día de la reunión a realizar"><%= conv.getOrden_dia()%></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Desarrollo</label>
                                                            <textarea class="form-control" rows="5" name="areadesarrollo" id="areadesarrollo" required placeholder="Detalle el desarrollo de la reunión"><%= conv.getOrden_dia()%></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12" hidden="">
                                                            <label>*Id acta</label>
                                                            <input type="text" class="form-control"  name="txtidacta" id="txtidacta" value="0" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Asistentes</label>
                                                            <select class="form-control select2" name="comboasistentes" id="comboasistentes" multiple="multiple" required >
                                                                <%for (usuario func : listadoConvocados) {%>
                                                                <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <div class="card-header-action text-right mb-4">
                                                                <button type="submit" data-toggle="modal" data-target="#modalCompromiso" class="btn btn-primary"><i class="fas fa-plus"></i> Añadir compromiso</button>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table class="table table-striped" id="table-2">
                                                                    <thead>                                 
                                                                        <tr>
                                                                            <th>Fecha cumplimiento</th>
                                                                            <th>Funcionario</th>
                                                                            <th>Compromiso</th>
                                                                            <th>Acciones</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>

                                                                    </tbody>
                                                                </table>
                                                            </div>
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
                            <h6><a href="listado_actas.jsp">Listado actas</a> > Actualizar acta</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formacta" action="administrar_acta.control?accion=registro_acta" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-2" hidden="">
                                                            <label>*id convocatoria</label>
                                                            <input type="text" class="form-control" name="txtconvoca" id="txtconvoca" value="<%= conv.getId_convocatoria() %>" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha de acta</label>
                                                            <input type="text" class="form-control datepicker" name="txtfechaacta" id="txtfechaacta" value="<%= act.getFecha_acta() %>" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha convocatoria</label>
                                                            <input type="text" class="form-control " name="txtfechaconvocatoria" id="txtfechaconvocatoria" value="<%= act.getFecha_convocatoria() %>" required readonly="">
                                                        </div>
                                                        <div class="form-group col-md-5">
                                                            <label>*Asunto</label>
                                                            <input type="text" class="form-control" name="txtasunto" id="txtasunto" required="" placeholder="Ingrese una breve descripción" value="<%= act.getAsunto() %>">
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <label>*Medio</label>
                                                            <select class="form-control" name="combomedio" id="combomedio" required="">
                                                                <%for (medio_acta med : listadoMedios) {%>
                                                                <%if (act.getId_medio() == med.getId_medio()) {%>
                                                                <option selected="" value="<%= med.getId_medio()%>"><%= med.getDescripcion()%></option>
                                                                <%} else {%>
                                                                <option value="<%= med.getId_medio()%>"><%= med.getDescripcion()%></option>
                                                                <%}%>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Hora de inicio</label>
                                                            <input type="time" class="form-control" name="txthorainicio" id="txthorainicio" required value="<%= act.getHora_inicio() %>">
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Hora de fin</label>
                                                            <input type="time" class="form-control" onchange="validarhoras()" name="txthorafin" id="txthorafin" value="<%= act.getHora_fin() %>" required>
                                                        </div>
                                                        <div class="form-group col-md-8">
                                                            <label>*Lugar</label>
                                                            <input type="text" class="form-control"  name="txtlugar" id="txtlugar" value="<%= act.getLugar() %>" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Orden del día</label>
                                                            <textarea class="form-control" rows="5" name="areaobservacion" id="areaobservacion" required placeholder="Detalle el orden del día de la reunión a realizar"><%= act.getOrden_dia() %></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Desarrollo</label>
                                                            <textarea class="form-control" rows="5" name="areadesarrollo" id="areadesarrollo" required placeholder="Detalle el desarrollo de la reunión"><%= act.getDesarrollo() %></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12" hidden="">
                                                            <label>*Id acta</label>
                                                            <input type="text" class="form-control"  name="txtidacta" id="txtidacta" value="<%= id_acta %>" required>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Asistentes</label>
                                                            <select class="form-control select2" name="comboasistentes" id="comboasistentes" multiple="multiple" required >
                                                                <%for (usuario func : listadoAsistenciaActa) {%>
                                                                <option selected value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <div class="card-header-action text-right mb-4">
                                                                <button type="submit" data-toggle="modal" data-target="#modalCompromiso" class="btn btn-primary"><i class="fas fa-plus"></i> Añadir compromiso</button>
                                                            </div>
                                                            <div class="table-responsive">
                                                                <table class="table table-striped" id="table-2">
                                                                    <thead>                                 
                                                                        <tr>
                                                                            <th>Fecha cumplimiento</th>
                                                                            <th>Funcionario</th>
                                                                            <th>Compromiso</th>
                                                                            <th>Acciones</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <%for(compromiso_acta comp:listadoCompromisos){
                                                                            usuario elemento=enlace.buscar_usuarioID(comp.getId_responsable());
                                                                        %>
                                                                        <tr>
                                                                            <td><%= comp.getFecha_cumplimiento() %></td>
                                                                            <td><%= elemento.getApellido() %> <%= elemento.getNombre() %></td>
                                                                            <td><%= comp.getDescripcion() %></td>
                                                                            <td>
                                                                                <a href="javascript:" onclick="eliminarCompromisoActa(<%= comp.getId_compromiso() %>)" type="button" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Quitar"><i class="fas fa-times"></i></a>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card-footer text-right">
                                                    <button class="btn btn-primary btn-block btn-lg" type="button" onclick="crearActa(<%= conv.getId_convocatoria() %>)"><i class="fas fa-save"></i> Finalizar registro</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%}%>
                    </section>
                    <div class="modal fade" id="modalCompromiso" tabindex="-1" role="dialog" aria-hidden="true">
                            <div class="modal-dialog modal-lg" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">
                                            <span class="fw-extrabold">
                                                Añadir compromiso
                                            </span>
                                        </h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <p class="small"></p>
                                        <%if (id_acta == 0) {%>
                                        <form id="formcompromiso" action="administrar_acta.control?accion=registro_compromiso" method="post" enctype="multipart/form-data">
                                            <%}else{%>
                                            <form id="formcompromiso1" action="administrar_acta.control?accion=registro_compromiso" method="post" enctype="multipart/form-data">
                                            <%}%>
                                            <div class="form-row">
                                                <%if (id_acta == 0) {%>
                                                <div class="form-group col-md-3" hidden="">
                                                    <label>Id acta</label>
                                                    <input type="text" class="form-control" name="txtacta" id="txtacta" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <%}else{%>
                                                <div class="form-group col-md-3" hidden="">
                                                    <label>Id acta</label>
                                                    <input type="text" class="form-control" name="txtacta" id="txtacta" value="<%= id_acta %>" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <%}%>
                                                <div class="form-group col-md-3" hidden="">
                                                    <label>Id conv</label>
                                                    <input type="text" class="form-control" name="txtconv" id="txtconv" value="<%= conv.getId_convocatoria() %>" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-3">
                                                    <label>Fecha cumplimiento</label>
                                                    <input type="text" class="form-control datepicker" name="txtfechacumplimiento" id="txtfechacumplimiento" required="">
                                                    <div class="invalid-feedback">
                                                        What's your name?
                                                    </div>
                                                </div>
                                                <div class="form-group col-md-9">
                                                    <label>*Asistentes</label>
                                                    <select class="form-control" name="comboresponsable" id="comboresponsable" required >
                                                        <option value="">Seleccione</option>
                                                        <%for (usuario func : listadoConvocados) {%>
                                                        <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <label>Compromiso</label>
                                                    <textarea type="text" class="form-control" name="areacompromiso" id="areacompromiso" placeholder="Detalle compromiso de participante" required=""></textarea>
                                                    <div class="invalid-feedback">
                                                        Ingrese información correcta
                                                    </div>
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
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>
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
        <script src="fun_js/formulario_convocatoria.js" type="text/javascript"></script>
        <script src="fun_js/funciones_convocatoria.js" type="text/javascript"></script>
        <script src="fun_js/validacion.js" type="text/javascript"></script>
    </body>
</html>
