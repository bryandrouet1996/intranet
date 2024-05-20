<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="modelo.SatisfaccionCap"%>
<%@page import="modelo.Horario"%>
<%@page import="modelo.Capacitacion"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
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
    ArrayList<SatisfaccionCap> listadoSatisfaccionCap = new ArrayList<>();
    ArrayList<Capacitacion> capasDisp = new ArrayList<>(), capasVig = new ArrayList<>(), capasAp = new ArrayList<>();
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoSatisfaccionCap = enlace.listadoSatisfaccionCap();
        capasDisp = enlace.getCapacitacionesDisp(id);
        capasVig = enlace.getCapacitacionesVigentes(id);
        capasAp = enlace.getCapacitacionesAprobadas(id);      
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
        <title>Intranet Alcaldía - Capacitaciones</title>
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
                            <h1>Capacitaciones ofrecidas por la Dirección de Tecnologías de la Información</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Listado de capacitaciones</h4>
                                        </div>
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true"><i class="fas fa-info-circle"></i> Disponibles <%if (capasDisp.size() != 0) {%><span class="badge badge-primary"><%= capasDisp.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false"><i class="fas fa-clock"></i> Vigentes <%if (capasVig.size() != 0) {%><span class="badge badge-primary"><%= capasVig.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false"><i class="fas fa-check"></i> Aprobadas <%if (capasAp.size() != 0) {%><span class="badge badge-primary"><%= capasAp.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 25%">Facilitador</th>
                                                                    <th style="width: 10%">Primera fecha</th>
                                                                    <th style="width: 10%">Última fecha</th>
                                                                    <th style="width: 10%">Horarios</th>
                                                                    <th style="width: 30%">Tema</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Capacitacion c : capasDisp) {%>
                                                                <tr>
                                                                    <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><%= c.getId() %></a></td>
                                                                    <td><%= c.getUsuario()%></td>
                                                                    <td><%= c.getFechaIni()%></td>
                                                                    <td><%= c.getFechaFin()%></td>
                                                                    <td><%= c.getHorarios()%></td>
                                                                    <td><%= c.getTema()%></td>
                                                                    <td>
                                                                        <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%=c.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar temario"></i></a>
                                                                        <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#horariosCap<%=c.getId()%>"><i class="fa fa-clock" data-toggle="tooltip" data-original-title="Ver horarios"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xx">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 25%">Facilitador</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Hora inicio</th>
                                                                    <th style="width: 10%">Hora fin</th>
                                                                    <th style="width: 30%">Tema</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Capacitacion c : capasVig) {%>
                                                                <tr>
                                                                    <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><%= c.getId() %></a></td>
                                                                    <td><%= c.getUsuario()%></td>
                                                                    <td><%= c.getFechaIni()%></td>
                                                                    <td><%= c.getHoraIni()%></td>
                                                                    <td><%= c.getHoraFin()%></td>
                                                                    <td><%= c.getTema()%></td>
                                                                    <td>
                                                                        <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%=c.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar temario"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xxx">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 25%">Facilitador</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 10%">Hora inicio</th>
                                                                    <th style="width: 10%">Hora fin</th>
                                                                    <th style="width: 30%">Tema</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Capacitacion c : capasAp) {%>
                                                                <tr>
                                                                    <td><a href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><%= c.getId() %></a></td>
                                                                    <td><%= c.getUsuario()%></td>
                                                                    <td><%= c.getFechaIni()%></td>
                                                                    <td><%= c.getHoraIni()%></td>
                                                                    <td><%= c.getHoraFin()%></td>
                                                                    <td><%= c.getTema()%></td>
                                                                    <td>
                                                                        <%if(c.getSatisfaccionAsistente()==0){%>
                                                                            <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#calificarCap" data-asis="<%= c.getAsistencia()%>" data-fecha="<%= c.getFechaIni() %>" data-tema="<%= c.getTema()%>" data-fac="<%= c.getUsuario()%>" data-hora_ini="<%= c.getHoraIni()%>" data-hora_fin="<%= c.getHoraFin()%>"><i class="fa fa-thumbs-up" data-toggle="tooltip" data-original-title="Evaluar satisfacción"></i></a>
                                                                        <%}else{%>
                                                                            <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#modalDetalleSatisfaccionCap" data-usuario="<%= c.getUsuario() %>" data-fecha="<%= c.getFechaIni() %>" data-tema="<%= c.getTema()%>" data-sat="<%= c.getSatisfaccionAsistenteDesc()%>" data-sat_fec="<%= c.getSatisfaccionAsistenteFec()%>"><i class="fa fa-thumbs-up" data-toggle="tooltip" data-original-title="Ver satisfacción"></i></a>
                                                                        <%}%>
                                                                        <%if(c.getSatisfaccionAsistente()==0){%>
                                                                            <a onclick="alertarCert()" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar certificado"></i></a>
                                                                        <%}else{%>
                                                                            <a target="_blank" href="administrar_cap.control?accion=certificado&asi=<%=c.getAsistencia()%>" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar certificado"></i></a>
                                                                        <%}%>
                                                                        <a class="btn btn-primary btn-sm active" href="javascript:" data-toggle="modal" data-target="#modalDetalleCap" data-usuario="<%= c.getUsuario() %>" data-tema="<%= c.getTema()%>" data-enlace="<%= c.getEnlace()%>" data-desc="<%= c.getDescripcion() %>"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>                                                                        
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%=c.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fa fa-file-download" data-toggle="tooltip" data-original-title="Descargar temario"></i></a>
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
                    <div class="modal fade" id="modalDetalleCap" role="dialog">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <span class="fw-extrabold">
                                            Detalle de capacitación
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
                                                <label>Facilitador</label>
                                                <input type="text" class="form-control" name="usuario" id="usuario" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-12">
                                                <label>Tema</label>
                                                <input type="text" class="form-control" name="tema" id="tema" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-12">
                                                <label>Enlace</label><br>
                                                <a target="_blank" href="" id="enlaceCap" class="btn btn-primary">Clic aquí para abrir el enlace de la capacitación</a>
                                            </div>
                                            <div class="form-group col-md-12">
                                                <label>Descripción</label>
                                                <textarea type="text" class="form-control" name="desc" id="desc" required="" readonly=""></textarea>
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
                    <div class="modal fade" id="modalDetalleSatisfaccionCap" role="dialog">
                        <div class="modal-dialog modal-lg" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">
                                        <span class="fw-extrabold">
                                            Detalle de satisfacción
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
                                            <div class="form-group col-md-8">
                                                <label>Facilitador</label>
                                                <input type="text" class="form-control" name="usuario" id="usuario" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-4">
                                                <label>Fecha</label>
                                                <input type="text" class="form-control" name="fecha" id="fecha" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-12">
                                                <label>Tema</label>
                                                <input type="text" class="form-control" name="tema" id="tema" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label>Nivel de satisfacción registrado</label>
                                                <input type="text" class="form-control" name="sat" id="sat" required="" readonly="">
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label>Fecha registro satisfacción</label>
                                                <input type="text" class="form-control" name="sat_fec" id="sat_fec" required="" readonly="">
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
                    <%for(Capacitacion c : capasDisp){%>
                        <div class="modal fade" id="horariosCap<%=c.getId()%>" role="dialog">
                            <div class="modal-dialog modal-lg" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">
                                            <span class="fw-extrabold">
                                                Horarios disponibles
                                            </span>
                                        </h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-c<%=c.getId()%>">
                                                <thead>                                 
                                                    <tr>
                                                        <th>Fecha</th>
                                                        <th>Hora inicio</th>
                                                        <th>Hora fin</th>
                                                        <th>Acciones</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for(Horario h : enlace.getHorariosCapacitacion(c.getId())){
                                                    %>
                                                    <tr>
                                                        <td><%= h.getFecha() %></td>
                                                        <td><%= h.getHoraIni()%></td>
                                                        <td><%= h.getHoraFin()%></td>
                                                        <td>
                                                            <a type="button" onclick="inscribirHorario(<%= h.getId() %>, '<%= h.getHoraIni() + " - " + h.getHoraFin() + " a realizarse el " + h.getFecha() %>', '<%= c.getTema()%>')" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Inscribirme"></i></a>
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
                    <%}%>
                    <div class="modal fade" id="calificarCap" role="dialog">
                            <div class="modal-dialog modal-lg" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">
                                            <span class="fw-extrabold">
                                                Evaluar satisfacción
                                            </span>
                                        </h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <form id="formCalificar" action="administrar_cap.control?accion=calificar" method="post" enctype="multipart/form-data" class="needs-validation">
                                            <div class="form-row">
                                                <div class="form-group col-md-2" hidden>
                                                    <input type="text" class="form-control" id="asis" name="asis" readonly="">
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <label>Tema</label>
                                                    <input type="text" class="form-control" id="tema" name="tema" readonly="">
                                                </div>
                                                <div class="form-group col-md-12">
                                                    <label>Facilitador</label>
                                                    <input type="text" class="form-control" id="fac" name="fac" readonly="">
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Fecha de realización</label>
                                                    <input type="text" class="form-control" id="fecha" name="fecha" readonly="">
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Hora de inicio</label>
                                                    <input type="text" class="form-control" id="hora_ini" name="hora_ini" readonly="">
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Hora de fin</label>
                                                    <input type="text" class="form-control" id="hora_fin" name="hora_fin" readonly="">
                                                </div>
                                                <div class="form-group col-md-4">
                                                    <label>Nivel de satisfacción</label>
                                                    <select class="form-control" name="sat" id="sat" required="">
                                                        <option value="" selected="" disabled="">Seleccione</option>
                                                        <%for (SatisfaccionCap s : listadoSatisfaccionCap) {%>
                                                        <option value="<%= s.getId()%>"><%= s.getDescripcion()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="submit" id="btnCalificar" class="btn btn-primary">Calificar</button>
                                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                            </div>
                                        </form>
                                    </div>
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
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script type="text/javascript">
            <%for(Capacitacion c : capasDisp){%>
                    $("#table-c<%=c.getId()%>").dataTable({
                        "ordering": false,
                        "order": [ 1, 'desc' ],
                        "columnDefs": [
                          { "sortable": true, "targets": [0] }
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
            <%}%>
                
            $(document).ready(function () {
                $('#formCalificar').submit(function (event) {
                    var btn = document.getElementById("btnCalificar");
                    btn.hidden = true;
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
                                title: 'Registrando satisfacción',
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
                                    title: 'Satisfacción registrada',
                                    icon: 'success',
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "capacitacion_disp.jsp";
                                });
                            } else {
                                Swal.fire({
                                    title: 'Error',
                                    text: 'No se pudo completar el registro',
                                    icon: 'error',
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "capacitacion_disp.jsp";
                                });
                            }
                        },
                        error: function () {
                            Swal.fire({
                                title: 'Error crítico',
                                text: 'No se pudo completar el registro',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "capacitacion_disp.jsp";
                            });
                        }
                    });
                    return false;
                });
            });
            
            $("#table-x").dataTable({
                "ordering": false,
                "order": [ 1, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0] }
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
              
            $("#table-xx").dataTable({
                "ordering": false,
                "order": [ 1, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0] }
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
              
            $("#table-xxx").dataTable({
                "ordering": true,
                "order": [ 0, 'desc' ],
                "columnDefs": [
                  { "sortable": true, "targets": [0, 2] }
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
              
            $('#modalDetalleCap').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var usuario = button.data('usuario');
                var tema = button.data('tema');
                var enlace = button.data('enlace');
                var desc = button.data('desc');
                var modal = $(this);
                modal.find('.modal-body #usuario').val(usuario);
                modal.find('.modal-body #tema').val(tema);
                document.getElementById('enlaceCap').href = enlace;
                modal.find('.modal-body #desc').val(desc);
            })
            
            $('#calificarCap').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var asis = button.data('asis');                
                var tema = button.data('tema');
                var fac = button.data('fac');
                var fecha = button.data('fecha');
                var hora_ini = button.data('hora_ini');
                var hora_fin = button.data('hora_fin');
                var modal = $(this);
                modal.find('.modal-body #asis').val(asis);
                modal.find('.modal-body #tema').val(tema);
                modal.find('.modal-body #fac').val(fac);
                modal.find('.modal-body #fecha').val(fecha);
                modal.find('.modal-body #hora_ini').val(hora_ini);
                modal.find('.modal-body #hora_fin').val(hora_fin);
            })
            
            $('#modalDetalleSatisfaccionCap').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var usuario = button.data('usuario');
                var fecha = button.data('fecha');
                var tema = button.data('tema');
                var sat = button.data('sat');
                var sat_fec = button.data('sat_fec');
                var modal = $(this);
                modal.find('.modal-body #usuario').val(usuario);
                modal.find('.modal-body #fecha').val(fecha);
                modal.find('.modal-body #tema').val(tema);
                modal.find('.modal-body #sat').val(sat);
                modal.find('.modal-body #sat_fec').val(sat_fec);
            })
            
            function inscribirHorario(id_hor, horario, tema) {
                Swal.fire({
                    title: '¿Desea inscribirse en este horario?',
                    text: "Una vez inscrito no se podrá desvincular",
                    icon: 'warning',
                    buttonsStyling: false,
                    showCancelButton: true,
                    confirmButtonText: 'Sí, inscribirme',
                    cancelButtonText: 'No, cancelar',
                    customClass: {
                        confirmButton: 'btn btn-success',
                        cancelButton: 'btn btn-danger'
                    }
                }).then((willDelete) => {
                    if (willDelete.isConfirmed) {
                        Swal.fire({
                            title: 'Efectuando registro',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                        $.post('administrar_cap.control?accion=inscribir', {
                            id_hor: id_hor,
                            id_usu: <%=id%>,
                            horario: horario,
                            tema: tema
                        }, function (responseText) {
                            if (responseText) {
                                Swal.fire({
                                    title: "Registro completado",
                                    icon: "success",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "capacitacion_disp.jsp";
                                });
                            } else {
                                Swal.fire({
                                    title: "Error",
                                    text: "No se completó el registro",
                                    icon: "warning",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "capacitacion_disp.jsp";
                                });
                            }
                        }, ).fail(function () {
                            Swal.fire({
                                title: "Error crítico",
                                text: "No se completó el registro",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "capacitacion_disp.jsp";
                            });
                        });
                    } else {
                        Swal.fire({
                            title: "Acción cancelada",
                            text: "No se inscribió en la capacitación",
                            icon: "warning",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        })
                    }
                });
            }
            
            function alertarCert(){
                Swal.fire({
                    title: "Evaluación de satisfacción pendiente",
                    text: "Para poder generar el certificado de asistencia primero debe evaluar el nivel de satisfacción de la capacitación",
                    icon: "info",
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success'
                    }
                })
            }
        </script>
    </body>
</html>
