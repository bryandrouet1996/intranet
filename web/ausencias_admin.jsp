<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>
<%@page import="modelo.PermisoECU"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.revision_vacaciones"%>
<%@page import="modelo.aprobacion_vacaciones"%>
<%@page import="modelo.rechazo_vacaciones"%>
<%@page import="modelo.AnulacionVacaciones"%>
<%@page import="modelo.permiso_vacaciones"%>
<%@page import="modelo.VacacionesGeneral"%>
<%@page import="modelo.motivo_vacaciones"%>
<%@page import="modelo.UsuarioIESS"%>
<%@page import="modelo.ApruebaVacas"%>
<%@page import="modelo.MotivoAnulacion"%>
<%@page import="modelo.AnulacionSolicitud"%>
<%@page import="modelo.RevisionHoras"%>
<%@page import="modelo.AprobacionHoras"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.Direccion"%>
<%@page import="modelo.HorasGeneral"%>
<%@page import="modelo.rechazo_solicitud"%>
<%@page import="modelo.motivo_permiso"%>
<%@page import="modelo.permiso_horas"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="modelo.informacion_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    conexion_oracle link = new conexion_oracle();
    int id = 0, permiso_pendiente = 0, permiso_pendiente_vac = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    //ECU
    ArrayList<PermisoECU> listadoPendientesECU = null, listadoRevisadasECU = null, listadoRechazadasECU = null;
    //PERMISOS
    ArrayList<HorasGeneral> listadoRevisadas = null;
    ArrayList<HorasGeneral> listadoAprobadas = null;
    ArrayList<HorasGeneral> listadoRechazadas = null;
    ArrayList<HorasGeneral> listadoAnuladas = null;
    ArrayList<HorasGeneral> listadoOtros = null;
    ArrayList<motivo_permiso> listadoMotivos = null;
    java.sql.Date fecha_inicio = null, fecha_fin = null;
    //VACAS
    ArrayList<VacacionesGeneral> listadoRevisadasVac = null;
    ArrayList<VacacionesGeneral> listadoAprobadasVac = null;
    ArrayList<VacacionesGeneral> listadoRechazadasVac = null;
    ArrayList<VacacionesGeneral> listadoAnuladasVac = null;
    ArrayList<motivo_vacaciones> listadoMotivosVac = null;
    permiso_vacaciones vacacion_pendiente = null;
    //
    ArrayList<MotivoAnulacion> motivos = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<Direccion> listadoDirecciones = null;
    ArrayList<UsuarioIESS> listadoUsu = null;
    ArrayList<ApruebaVacas> listadoFirma = null;
    permiso_horas horas_pendiente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        if (!(enlace.verificarUsuarioCumpleRol(id, "administrador") || enlace.verificarUsuarioCumpleRol(id, "encargado_permisos"))) {
            throw new Exception("Rol no habilitado");
        }
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listadoMotivos = enlace.listadoMotivos();
        listadoMotivosVac = enlace.listadoMotivosVacaciones();
        listadoOtros = enlace.listadoHorasOtros();
        if (fecha_inicio != null) {
            //ECU
            listadoPendientesECU = enlace.listadoGeneralPermisoHorasECU(0, fecha_inicio, fecha_fin);
            listadoRevisadasECU = enlace.listadoGeneralPermisoHorasECU(1, fecha_inicio, fecha_fin);
            listadoRechazadasECU = enlace.listadoGeneralPermisoHorasECU(2, fecha_inicio, fecha_fin);
            //PERMISOS
            listadoRevisadas = enlace.listadoEnHorasTodosEstadoID(1, fecha_inicio, fecha_fin);
            listadoAprobadas = enlace.listadoEnHorasTodosEstadoID(2, fecha_inicio, fecha_fin);
            listadoRechazadas = enlace.listadoEnHorasTodosEstadoID(3, fecha_inicio, fecha_fin);
            listadoAnuladas = enlace.listadoEnHorasTodosEstadoID(4, fecha_inicio, fecha_fin);
            //VACAS            
            listadoRevisadasVac = enlace.listadoVacacionesTodos(1, fecha_inicio, fecha_fin);
            listadoAprobadasVac = enlace.listadoVacacionesTodos(2, fecha_inicio, fecha_fin);
            listadoRechazadasVac = enlace.listadoVacacionesTodos(3, fecha_inicio, fecha_fin);
            listadoAnuladasVac = enlace.listadoVacacionesTodos(4, fecha_inicio, fecha_fin);
        }else{
            //ECU
            listadoPendientesECU = enlace.listadoGeneralPermisoHorasECU(0);
            listadoRevisadasECU = enlace.listadoGeneralPermisoHorasECU(1);
            listadoRechazadasECU = enlace.listadoGeneralPermisoHorasECU(2);
            //PERMISOS
            listadoRevisadas = enlace.listadoEnHorasTodosEstadoID(1);
            listadoAprobadas = enlace.listadoEnHorasTodosEstadoID(2);
            listadoRechazadas = enlace.listadoEnHorasTodosEstadoID(3);
            listadoAnuladas = enlace.listadoEnHorasTodosEstadoID(4);
            //VACAS            
            listadoRevisadasVac = enlace.listadoVacacionesTodos(1);
            listadoAprobadasVac = enlace.listadoVacacionesTodos(2);
            listadoRechazadasVac = enlace.listadoVacacionesTodos(3);
            listadoAnuladasVac = enlace.listadoVacacionesTodos(4);
        }        
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoDirecciones = link.getDirecciones();
        listadoUsu = link.getUsuariosActivos();
        listadoFirma = enlace.getApruebaVacas();
        motivos = enlace.listadoMotivosAnulacion();
        permiso_pendiente = enlace.getPermisoHorasPendiente(id);
        horas_pendiente = enlace.buscarPermisoHoras(permiso_pendiente);        
        permiso_pendiente_vac = enlace.getVacacionPendiente(id);
        vacacion_pendiente = enlace.buscarPermisoVacacion(permiso_pendiente_vac);
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
        <title>Intranet Alcaldía - Administración de ausencias</title>
        <!-- General CSS Files -->
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/modules/fullcalendar/fullcalendar.min.css">
        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">
        <link rel="stylesheet" href="assets/modules/datatables/datatables.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/datatables/Select-1.2.4/css/select.bootstrap4.min.css">
        <link rel="stylesheet" href="assets/modules/jqvmap/dist/jqvmap.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons.min.css">
        <link rel="stylesheet" href="assets/modules/weather-icon/css/weather-icons-wind.min.css">
        <link rel="stylesheet" href="assets/modules/summernote/summernote-bs4.css">
        <link rel="stylesheet" href="assets/modules/izitoast/css/iziToast.min.css">
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

                <!-- Main Content -->
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Administración de ausencias</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#adminFirma"> <i class="fas fa-user"></i> Configurar firma</a>
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
                            <!-- Row Permisos -->
                            <div class="row">
                                <div class="col-md-12">
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
                                                    <div class="col-8">
                                                        <label>Acciones</label></br>
                                                        <a class="btn btn-primary daterange-btn icon-left btn-icon active"><i class="fas fa-calendar"></i> Elegir rango</a>
                                                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filtrar</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Permisos ECU</h4>
                                        </div>
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="otros-ecu-tab" data-toggle="tab" href="#otros-ecu" role="tab" aria-controls="otros-ecu" aria-selected="true"><i class="fas fa-exclamation-triangle"></i> Pendientes <%if (listadoPendientesECU.size() != 0) {%><span class="badge badge-primary"><%= listadoPendientesECU.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="home-ecu-tab" data-toggle="tab" href="#home-ecu" role="tab" aria-controls="home-ecu" aria-selected="false"><i class="fas fa-check"></i> Revisadas <%if (listadoRevisadasECU.size() != 0) {%><span class="badge badge-primary"><%= listadoRevisadasECU.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-ecu-tab" data-toggle="tab" href="#contact-ecu" role="tab" aria-controls="contact-ecu" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoRechazadasECU.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazadasECU.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="otros-ecu" role="tabpanel" aria-labelledby="otros-ecu-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-ecu-1">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (PermisoECU pen : listadoPendientesECU) {
                                                                        usuario fun = enlace.buscar_usuarioID(pen.getIdUsu());
                                                                        String nombre_usuario = fun.getApellido() + " " + fun.getNombre();
                                                                        motivo_permiso mot = enlace.buscarMotivoId(pen.getIdMotivo());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><%= pen.getId()%></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= pen.getInicio()%></td>
                                                                    <td style="width: 15%"><%= pen.getFin()%></td>
                                                                    <td style="width: 15%"><%= pen.getTiempoSoli()%></td>
                                                                    <td style="width: 15%"><%= mot.getDescripcion()%></td>
                                                                    <td style="width: 20%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetallePermisoECU" data-fecha_soli="<%= pen.getFechaSoli()%>" data-motivo="<%= mot.getDescripcion()%>" data-soli="<%= nombre_usuario + " - " + pen.getDenominacion()%>" data-unidad="<%= pen.getUnidad()%>" data-jefe="<%= pen.getJefe() + " - " + pen.getCargoJefe()%>" data-inicio="<%= pen.getInicio()%>" data-fin="<%= pen.getFin()%>" data-tiempo_soli="<%= pen.getTiempoSoli()%>" data-dias_habiles="<%= pen.getDiasHabiles()%>" data-dias_finde="<%= pen.getDiasFinde()%>" data-descrip="<%= pen.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <%if (pen.getAdjunto()!=null) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= pen.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                        <%}%>                                                                        
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalValidarECU" data-iu="<%= pen.getIdUsu()%>" data-ipe="<%= pen.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Validar"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazoECU" data-iu="<%= pen.getIdUsu()%>" data-ipe="<%= pen.getId()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="home-ecu" role="tabpanel" aria-labelledby="home-ecu-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-ecu-2">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (PermisoECU rev : listadoRevisadasECU) {
                                                                        usuario fun = enlace.buscar_usuarioID(rev.getIdUsu());
                                                                        String nombre_usuario = fun.getApellido() + " " + fun.getNombre();
                                                                        motivo_permiso mot = enlace.buscarMotivoId(rev.getIdMotivo());
                                                                        RevisionHoras elemen = enlace.obtenerRevisionHorasECU(rev.getId());
                                                                        usuario revisa = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="active"><%= rev.getId()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= rev.getInicio()%></td>
                                                                    <td style="width: 15%"><%= rev.getFin()%></td>
                                                                    <td style="width: 15%"><%= rev.getTiempoSoli()%></td>
                                                                    <td style="width: 15%"><%= mot.getDescripcion()%></td>
                                                                    <td style="width: 20%">
                                                                        <%if (rev.getConfirmacion()!=null) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= rev.getConfirmacion()%>" class="btn btn-primary btn-sm active"><i class="fas fa-file-download" data-toggle="tooltip" data-original-title="Descargar formulario"></i></a>
                                                                            <%}%>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <%if (rev.getAdjunto()!=null) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= rev.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                        <%}%>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="contact-ecu" role="tabpanel" aria-labelledby="contact-ecu-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-ecu-3">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (PermisoECU rec : listadoRechazadasECU) {
                                                                        usuario fun = enlace.buscar_usuarioID(rec.getIdUsu());
                                                                        String nombre_usuario = fun.getApellido() + " " + fun.getNombre();
                                                                        motivo_permiso mot = enlace.buscarMotivoId(rec.getIdMotivo());
                                                                        rechazo_solicitud elem = enlace.obtenerRechazoSolicitudHorasECU(rec.getId());
                                                                        usuario rechaza = enlace.buscar_usuarioID(elem.getId_rechaza());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getRazon()%>" data-fech="<%= elem.getFecha_creacion()%>" class="active"><%= rec.getId()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= rec.getInicio()%></td>
                                                                    <td style="width: 15%"><%= rec.getFin()%></td>
                                                                    <td style="width: 15%"><%= rec.getTiempoSoli()%></td>
                                                                    <td style="width: 15%"><%= mot.getDescripcion()%></td>
                                                                    <td style="width: 20%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getRazon()%>" data-fech="<%= elem.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <%if (rec.getAdjunto()!=null) {%>
                                                                        <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= rec.getAdjunto()%>" class="btn btn-primary btn-sm active"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
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
                                    <div class="card">
                                        <div class="card-header">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <h4>Permisos</h4>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#aprobdir"> <i class="fas fa-check"></i> Aprobación directa</a>
                                                    <!--<a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#actDir"> <i class="fas fa-pen"></i> Actualizar dirección</a>-->
                                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#adminInd"> <i class="fas fa-user"></i> Administración individual</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body">                                            
                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="otros-tab" data-toggle="tab" href="#otros" role="tab" aria-controls="otros" aria-selected="true"><i class="fas fa-exclamation-triangle"></i> Calamidad/Otros <%if (listadoOtros.size() != 0) {%><span class="badge badge-primary"><%= listadoOtros.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="false"><i class="fas fa-check"></i> Revisadas <%if (listadoRevisadas.size() != 0) {%><span class="badge badge-primary"><%= listadoRevisadas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadas.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoRechazadas.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazadas.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="anul-tab" data-toggle="tab" href="#anul" role="tab" aria-controls="anul" aria-selected="false"><i class="fas fa-exclamation-triangle"></i> Anuladas <%if (listadoAnuladas.size() != 0) {%><span class="badge badge-primary"><%= listadoAnuladas.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="otros" role="tabpanel" aria-labelledby="otros-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-4">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Adjunto(s)</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (HorasGeneral otro : listadoOtros) {
                                                                        permiso_horas permiso = otro.getPermiso();
                                                                        String nombre_usuario = otro.getFuncionario().getApellido() + " " + otro.getFuncionario().getNombre();
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="active"><%= permiso.getId_permiso()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getTimestamp_fin() : permiso.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                                    <td style="width: 10%"><%= otro.getMotivo()%></td>                                                        
                                                                    <td style="width: 10%">
                                                                        <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                    </td>
                                                                    <td style="width: 10%">
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                                            <%if (!permiso.isValido()) {%>
                                                                        <a type="button" onclick="validarSolicitudHoras(<%= permiso.getId_permiso()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Validar"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazo" data-iu="<%= permiso.getId_usuario()%>" data-ipe="<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-1">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Adjunto(s)</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (HorasGeneral revisadas : listadoRevisadas) {
                                                                        permiso_horas permiso = revisadas.getPermiso();
                                                                        String nombre_usuario = revisadas.getFuncionario().getApellido() + " " + revisadas.getFuncionario().getNombre();
                                                                        RevisionHoras elemen = enlace.obtenerRevisionHorasID(permiso.getId_permiso());
                                                                        usuario revisa = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="active"><%= permiso.getId_permiso()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getTimestamp_fin() : permiso.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                                    <td style="width: 10%"><%= revisadas.getMotivo()%></td>
                                                                    <td style="width: 10%">
                                                                        <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                            <%}%>
                                                                            <%if (permiso.getAsistencia() != null) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                    <td style="width: 10%">
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevision" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                            <%if (permiso.getId_motivo() == 4 && permiso.getAsistencia() == null) {%>
                                                                        <a type="button" onclick="alertarFaltaAsistencia()" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Aprobar"></i></a>
                                                                            <%} else {%>
                                                                        <a type="button" onclick="aprobarSolicitudHoras(<%= permiso.getId_permiso()%>,<%= informacion.getId_usuario()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Aprobar"></i></a>
                                                                            <%}%>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazo" data-iu="<%= permiso.getId_usuario()%>" data-ipe="<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-2">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Adjunto(s)</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (HorasGeneral aprobadas : listadoAprobadas) {
                                                                        permiso_horas permiso = aprobadas.getPermiso();
                                                                        usuario funcionario = aprobadas.getFuncionario();
                                                                        String nombre_usuario = permiso.getId_usuario() == 0 ? funcionario.getApellido() : funcionario.getApellido() + " " + funcionario.getNombre();
                                                                        AprobacionHoras elemen = enlace.obtenerAprobacionHorasID(permiso.getId_permiso());
                                                                        usuario aprueba = enlace.buscar_usuarioID(elemen.getId_aprueba());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="active"><%= permiso.getId_permiso()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getTimestamp_fin() : permiso.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                                    <td style="width: 10%"><%= aprobadas.getMotivo()%></td>                                                        
                                                                    <td style="width: 10%">
                                                                        <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                            <%}%>
                                                                            <%if (permiso.getAsistencia() != null) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                            <%}%>
                                                                    </td>   
                                                                    <td style="width: 10%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elemen.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAnulacion" data-ipe="<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Anular"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-3">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Adjunto(s)</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (HorasGeneral rechazadas : listadoRechazadas) {
                                                                        permiso_horas permiso = rechazadas.getPermiso();
                                                                        String nombre_usuario = rechazadas.getFuncionario().getApellido() + " " + rechazadas.getFuncionario().getNombre();
                                                                        rechazo_solicitud elem = enlace.obtenerRechazoSolicitudHoras(permiso.getId_permiso());
                                                                        usuario rechaza = enlace.buscar_usuarioID(elem.getId_rechaza());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getRazon()%>" data-fech="<%= elem.getFecha_creacion()%>" class="active"><%= permiso.getId_permiso()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_inicio()%></td>
                                                                    <td style="width: 15%"><%= permiso.getTimestamp_fin()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getTimestamp_fin() : permiso.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                                    <td style="width: 10%"><%= rechazadas.getMotivo()%></td>
                                                                    <td style="width: 10%">
                                                                        <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                            <%}%>
                                                                            <%if (permiso.getAsistencia() != null) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                    <td style="width: 10%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getRazon()%>" data-fech="<%= elem.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="anul" role="tabpanel" aria-labelledby="anul-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-5">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Motivo</th>
                                                                    <th>Adjunto(s)</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (HorasGeneral anul : listadoAnuladas) {
                                                                        permiso_horas permiso = anul.getPermiso();
                                                                        usuario funcionario = anul.getFuncionario();
                                                                        String nombre_usuario = permiso.getId_usuario() == 0 ? funcionario.getApellido() : funcionario.getApellido() + " " + funcionario.getNombre();
                                                                        AnulacionSolicitud elem = enlace.obtenerAnulacionSolicitudHoras(permiso.getId_permiso());
                                                                        usuario rechaza = enlace.buscar_usuarioID(elem.getId_anula());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getMotivo()%>" data-fech="<%= elem.getFecha_creacion()%>" class="active"><%= permiso.getId_permiso()%></a></td>
                                                                    <td style="width: 15%"><%= nombre_usuario%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getHora_salida() : permiso.getFecha_inicio()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getHora_entrada() : permiso.getFecha_fin()%></td>
                                                                    <td style="width: 15%"><%= permiso.getId_tipo() == 1 ? permiso.getTimestamp_fin() : permiso.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= permiso.getId_tipo() == 1 ? permiso.getTiempo_solicita() : permiso.getDias_solicitados() + " día(s)"%></td>
                                                                    <td style="width: 10%"><%= anul.getMotivo()%></td>                                                        
                                                                    <td style="width: 10%">
                                                                        <%if (!permiso.getAdjunto().equalsIgnoreCase("ninguno")) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=adjunto_horas&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                                            <%}%>
                                                                            <%if (permiso.getAsistencia() != null) {%>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_asistencia&id_permiso=<%= permiso.getId_permiso()%>" class="btn"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar asistencia a cita médica"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                    <td style="width: 10%">
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_anulacion_h&id_permiso=<%= elem.getId_solicitud()%>" class="btn btn-primary btn-sm active"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar justificativo"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacion" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-descri="<%= elem.getMotivo()%>" data-fech="<%= elem.getFecha_creacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=permiso_horas&ip=<%= permiso.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
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
                            <!-- Row Vacaciones -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <h4>Vacaciones</h4>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#aprobdirvac"> <i class="fas fa-check"></i> Aprobación directa</a>   
                                                    <!--<a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#actDirVac"> <i class="fas fa-pen"></i> Actualizar dirección</a>-->
                                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#adminIndVac"> <i class="fas fa-user"></i> Administración individual</a>   
                                                </div>                                                
                                            </div>
                                        </div>
                                        <div class="card-body">
                                            <ul class="nav nav-tabs" id="myTabVac" role="tablist">
                                                <li class="nav-item">
                                                    <a class="nav-link active" id="home-tab-vac" data-toggle="tab" href="#home-vac" role="tab" aria-controls="home-vac" aria-selected="true"><i class="fas fa-check"></i> Revisadas <%if (listadoRevisadasVac.size() != 0) {%><span class="badge badge-primary"><%= listadoRevisadasVac.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="profile-tab-vac" data-toggle="tab" href="#profile-vac" role="tab" aria-controls="profile-vac" aria-selected="false"><i class="fas fa-check-double"></i> Aprobadas <%if (listadoAprobadasVac.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobadasVac.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="contact-tab-vac" data-toggle="tab" href="#contact-vac" role="tab" aria-controls="contact-vac" aria-selected="false"><i class="fas fa-times"></i> Rechazadas <%if (listadoRechazadasVac.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazadasVac.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="anul-tab-vac" data-toggle="tab" href="#anul-vac" role="tab" aria-controls="anul-vac" aria-selected="false"><i class="fas fa-exclamation-triangle"></i> Anuladas <%if (listadoAnuladasVac.size() != 0) {%><span class="badge badge-primary"><%= listadoAnuladasVac.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="home-vac" role="tabpanel" aria-labelledby="home-tab-vac">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-2-vac">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Dirección</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (VacacionesGeneral revisadas : listadoRevisadasVac) {
                                                                        permiso_vacaciones vacacion = revisadas.getVacacion();
                                                                        String nombre_usuario = revisadas.getFuncionario().getApellido() + " " + revisadas.getFuncionario().getNombre();
                                                                        revision_vacaciones elemen = enlace.obtenerRevisionVacacionesID(vacacion.getId_permiso());
                                                                        usuario revisa = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevisionVac" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="active"><%= vacacion.getId_permiso()%></a></td>
                                                                    <td style="width: 25%"><%= nombre_usuario%></td>
                                                                    <td style="width: 25%"><%= vacacion.getDireccion()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_inicio()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_fin()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= (int) (vacacion.getDias_descuento()) + " día(s)"%></td>                                                        
                                                                    <td style="width: 10%">
                                                                        <%if (vacacion.getDias_solicitados() > 4) {%>
                                                                        <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= vacacion.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                            <%} else {%>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
                                                                            <%}%>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRevisionVac" data-revisa="<%= revisa.getApellido() + " " + revisa.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a type="button" onclick="aprobarSolicitudVacaciones(<%= vacacion.getId_permiso()%>,<%= informacion.getId_usuario()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check" data-toggle="tooltip" data-original-title="Aprobar"></i></a>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalRechazoVac" data-iu="<%= vacacion.getId_usuario()%>" data-ipe="<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Rechazar"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="profile-vac" role="tabpanel" aria-labelledby="profile-tab-vac">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-3-vac">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Dirección</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (VacacionesGeneral aprobadas : listadoAprobadasVac) {
                                                                        permiso_vacaciones vacacion = aprobadas.getVacacion();
                                                                        usuario funcionario = aprobadas.getFuncionario();
                                                                        String nombre_usuario = vacacion.getId_usuario() == 0 ? funcionario.getApellido() : funcionario.getApellido() + " " + funcionario.getNombre();
                                                                        aprobacion_vacaciones elem = enlace.obtenerAprobacionVacacionesID(vacacion.getId_permiso());
                                                                        usuario aprueba = enlace.buscar_usuarioID(elem.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacionVac" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elem.getFecha_registro()%>" class="active"><%= vacacion.getId_permiso()%></a></td>
                                                                    <td style="width: 25%"><%= nombre_usuario%></td>
                                                                    <td style="width: 25%"><%= vacacion.getDireccion()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_inicio()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_fin()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= (int) (vacacion.getDias_descuento()) + " día(s)"%></td>                                                      
                                                                    <td style="width: 10%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacionVac" data-aprueba="<%= aprueba.getApellido() + " " + aprueba.getNombre()%>" data-fech="<%= elem.getFecha_registro()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                            <%if (vacacion.getDias_solicitados() > 4) {%>
                                                                        <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= vacacion.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                            <%} else {%>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
                                                                            <%}%>
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalAnulacionVac" data-ipe="<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-times" data-toggle="tooltip" data-original-title="Anular"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="contact-vac" role="tabpanel" aria-labelledby="contact-tab-vac">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-4-vac">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Dirección</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (VacacionesGeneral rechazadas : listadoRechazadasVac) {
                                                                        permiso_vacaciones vacacion = rechazadas.getVacacion();
                                                                        String nombre_usuario = rechazadas.getFuncionario().getApellido() + " " + rechazadas.getFuncionario().getNombre();
                                                                        rechazo_vacaciones elemen = enlace.obtenerRechazoSolicitudVacaciones(vacacion.getId_permiso());
                                                                        usuario rechaza = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazoVac" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" data-descri="<%= elemen.getDescripcion()%>" class="active"><%= vacacion.getId_permiso()%></a></td>
                                                                    <td style="width: 25%"><%= nombre_usuario%></td>
                                                                    <td style="width: 25%"><%= vacacion.getDireccion()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_inicio()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_fin()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= (int) (vacacion.getDias_descuento()) + " día(s)"%></td>                                                        
                                                                    <td style="width: 10%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazoVac" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" data-descri="<%= elemen.getDescripcion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                            <%if (vacacion.getDias_solicitados() > 4) {%>
                                                                        <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= vacacion.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                            <%} else {%>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade" id="anul-vac" role="tabpanel" aria-labelledby="anul-tab-vac">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-5-vac">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th>ID</th>
                                                                    <th>Funcionario</th>
                                                                    <th>Dirección</th>
                                                                    <th>Inicio</th>
                                                                    <th>Fin</th>
                                                                    <th>Retorno</th>
                                                                    <th>Tiempo solicitado</th>
                                                                    <th>Acción</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (VacacionesGeneral anulada : listadoAnuladasVac) {
                                                                        permiso_vacaciones vacacion = anulada.getVacacion();
                                                                        usuario funcionario = anulada.getFuncionario();
                                                                        String nombre_usuario = vacacion.getId_usuario() == 0 ? funcionario.getApellido() : funcionario.getApellido() + " " + funcionario.getNombre();
                                                                        AnulacionVacaciones elemen = enlace.obtenerAnulacionSolicitudVacaciones(vacacion.getId_permiso());
                                                                        usuario rechaza = enlace.buscar_usuarioID(elemen.getId_usuario());
                                                                %>
                                                                <tr>
                                                                    <td style="width: 5%"><a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacionVac" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" data-descri="<%= elemen.getMotivo()%>" class="active"><%= vacacion.getId_permiso()%></a></td>
                                                                    <td style="width: 25%"><%= nombre_usuario%></td>
                                                                    <td style="width: 25%"><%= vacacion.getDireccion()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_inicio()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_fin()%></td>
                                                                    <td style="width: 10%"><%= vacacion.getFecha_ingreso()%></td>
                                                                    <td style="width: 5%"><%= (int) (vacacion.getDias_descuento()) + " día(s)"%></td>
                                                                    <td style="width: 10%">
                                                                        <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAnulacionVac" data-rechaza="<%= rechaza.getApellido() + " " + rechaza.getNombre()%>" data-fech="<%= elemen.getFecha_registro()%>" data-descri="<%= elemen.getMotivo()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                                        <a target="_blank" href="administrar_permiso.control?accion=descargar_anulacion_vaca&id_permiso=<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm active"><i class="fas fa-download" data-toggle="tooltip" data-original-title="Descargar justificativo"></i></a>
                                                                            <%if (vacacion.getDias_solicitados() > 4) {%>
                                                                        <a href="reporte_permiso.control?tipo=vacaciones&ip=<%= vacacion.getId_permiso()%>" target="_blank" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar solicitud"></i></a>
                                                                            <%} else {%>
                                                                        <a target="_blank" href="reporte_permiso.control?tipo=vacaciones_4&ip=<%= vacacion.getId_permiso()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar solicitud" class="fa fa-print"></i></a>
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
                            </div>
                        </div>
                    </section>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="aprobdir">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Aprobación directa
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
                                        <label>Id solicitud *</label>
                                        <input type="number" class="form-control" placeholder="Ingrese el ID de la solicitud y presione ENTER" name="idsoli" id="idsoli" onchange="checkPer()">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud *</label>
                                        <input type="text" class="form-control" placeholder="Fecha de solicitud" name="fechasoli" id="fechasoli" readonly="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Nombre del funcionario" name="nombreser" id="nombreser" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo *</label>
                                        <select class="form-control" name="moti" id="moti" required readonly>
                                            <option selected="" disabled="" value="">Seleccione motivo</option>
                                            <%for (motivo_permiso busq : listadoMotivos) {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Cargo del funcionario" name="cargoser" id="cargoser" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ninguna información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Unidad del funcionario" name="unidadser" id="unidadser" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Inicio *</label>
                                        <input type="text" class="form-control" placeholder="Inicio" name="ini" id="ini" readonly="">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fin *</label>
                                        <input type="text" class="form-control" placeholder="Fin" name="fin" id="fin" readonly="">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Tiempo solicitado</label>
                                        <input type="text" class="form-control" placeholder="Tiempo solicitado" name="tiempo" id="tiempo" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad del funcionario" name="modali" id="modali" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observaciones *</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="obs" id="obs" required="" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No ha escrito ninguna observación
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button hidden="" id="btnadj" type="button" class="btn btn-primary">Descargar adjunto</button>
                                    <button hidden="" id="btnasis" type="button" class="btn btn-primary">Descargar asistencia</button>
                                    <button hidden="" id="btnapr" type="button" onclick="aprobar()" class="btn btn-primary">Aprobar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="actDir">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Actualizar dirección
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
                                        <label>Id solicitud *</label>
                                        <input type="number" class="form-control" placeholder="Ingrese el ID de la solicitud y presione ENTER" name="idsoliad" id="idsoliad" onchange="checkPerAd()">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud *</label>
                                        <input type="text" class="form-control" placeholder="Fecha de solicitud" name="fechasoliad" id="fechasoliad" readonly="">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Nombre del funcionario" name="nombreserad" id="nombreserad" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo *</label>
                                        <select class="form-control" name="motiad" id="motiad" required readonly>
                                            <option selected="" disabled="" value="">Seleccione motivo</option>
                                            <%for (motivo_permiso busq : listadoMotivos) {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Cargo del funcionario" name="cargoserad" id="cargoserad" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ninguna información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Unidad del funcionario" name="unidadserad" id="unidadserad" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Inicio *</label>
                                        <input type="text" class="form-control" placeholder="Inicio" name="iniad" id="iniad" readonly="">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fin *</label>
                                        <input type="text" class="form-control" placeholder="Fin" name="finad" id="finad" readonly="">
                                        <div class="invalid-feedback">
                                            Sin información
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Tiempo solicitado</label>
                                        <input type="text" class="form-control" placeholder="Tiempo solicitado" name="tiempoad" id="tiempoad" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad del funcionario" name="modaliad" id="modaliad" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observaciones *</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="obsad" id="obsad" required="" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No ha escrito ninguna observación
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Dirección a cambiar</label>
                                        <select class="form-control" name="dirad" id="dirad" required>
                                            <%for (Direccion dir : listadoDirecciones) {%>
                                            <option value="<%= dir.getId()%>"><%= dir.getNombre()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button hidden="" id="btnAct" type="button" onclick="actualizar()" class="btn btn-primary">Actualizar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="adminInd">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Administración individual
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <div class="form-row">
                                <div class="form-group col-md-12">
                                    <label>Funcionario</label>
                                    <select style="width: 100%" class="form-control select2" name="listfun" id="listfun" required onchange="cambioFun()">
                                        <option <%= permiso_pendiente != 0 ? "" : "selected"%> disabled="" value="0">Seleccione</option>
                                        <%
                                            usuario func = enlace.getUsuarioId(horas_pendiente.getId_usuario());
                                            for (UsuarioIESS usu : listadoUsu) {%>
                                        <option value="<%= usu.getCodigo()%>" <%= permiso_pendiente != 0 ? ((func.getId_usuario() == 0 ? horas_pendiente.getCodigoUsu() : Integer.parseInt(func.getCodigo_usuario())) == usu.getCodigo() ? "selected" : "disabled") : ""%>><%= usu.getNombres()%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button <%= permiso_pendiente != 0 ? "" : "hidden"%> id="btnFun" type="button" onclick="administrar()" class="btn btn-primary">Administrar permisos</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="adminFirma">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Administración de firma
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <div class="form-row">
                                <div class="form-group col-md-12">
                                    <label>Cargo</label>
                                    <select style="width: 100%" class="form-control select2" name="listfirma" id="listfirma" required>
                                        <%for (ApruebaVacas ap : listadoFirma) {%>
                                        <option value="<%= ap.getId()%>" <%= ap.isEstado() ? "selected" : ""%>><%= ap.getCargo()%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="btnFirma" type="button" onclick="administrarFirma()" class="btn btn-primary">Configurar</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRechazo">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de rechazo
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formrechazo" action="administrar_permiso.control?accion=rechazar_hora" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtiu1" id="txtiu1" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtidper1" id="txtidper1">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de rechazo</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve la razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" class="btn btn-primary" id="btnRechazar">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
                                        
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalValidarECU">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Validar solicitud
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formvalidarecu" action="administrar_permiso.control?accion=validar_hora_ecu" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" class="form-control" name="iu" id="iu" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" class="form-control" name="ipe" id="ipe">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Formulario</label>
                                        <input type="file" class="form-control" name="archivo" id="archivo" onchange="validateFile()" required>
                                        <div class="invalid-feedback">
                                            No se ha cargado el formulario
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" class="btn btn-primary" id="btnValidarECU">Validar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
                                        
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRechazoECU">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de rechazo
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formrechazoecu" action="administrar_permiso.control?accion=rechazar_hora_ecu" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtiu1" id="txtiu1" value="<%= informacion.getId_usuario()%>">
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtidper1" id="txtidper1">
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de rechazo</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve la razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" class="btn btn-primary" id="btnRechazarECU">Guardar cambios</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
                                        
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalAnulacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de anulación
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formanulacion" action="administrar_permiso.control?accion=anular_hora" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtusu" id="txtusu" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" name="txtper" id="txtper">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de anulación</label>
                                        <select class="form-control" name="motivo" id="motivo" required>
                                            <%for (MotivoAnulacion mot : motivos) {%>
                                            <option <%= mot.getId() == 1 ? "selected" : ""%> value="<%=mot.getId()%>"><%=mot.getMotivo()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div id="divadju" class="form-group col-md-12">
                                        <label>Adjunto (obligatorio, PDF tamaño máximo 2MB)</label>
                                        <input type="file" class="form-control" name="archivo" id="archivo" onchange="validateFile()" required>
                                        <div class="invalid-feedback">
                                            No adjuntó ningún archivo
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button id="btnAnular" type="submit" value="Upload" class="btn btn-primary">Anular</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRechazo">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de rechazo
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
                                        <label>Funcionario que rechaza</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrechaza" id="txtrechaza" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharechazo1" id="txtfecharechazo1" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAnulacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de anulación
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
                                        <label>Funcionario que anula</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtanula" id="txtanula" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1" id="arearazon1" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharechazo1" id="txtfecharechazo1" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAprobacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de aprobación
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
                                        <label>Funcionario que aprueba</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtaprueba" id="txtaprueba" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfechaaprueba" id="txtfechaaprueba" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRevision">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de revisión
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
                                        <label>Funcionario que revisa</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrevisa" id="txtrevisa" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharevisa" id="txtfecharevisa" required="" readonly >
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
                                        
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetallePermisoECU">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de permiso
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
                                    <div class="form-group col-md-4">
                                        <label>Fecha solicitud</label>
                                        <input type="text" class="form-control" name="fecha_soli" id="fecha_soli" readonly>
                                    </div>
                                    <div class="form-group col-md-8">
                                        <label>Motivo solicitud</label>
                                        <input type="text" class="form-control" name="motivo" id="motivo" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Solicitante</label>
                                        <input type="text" class="form-control" name="soli" id="soli" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Dirección del solicitante</label>
                                        <input type="text" class="form-control" name="unidad" id="unidad" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Jefe del solicitante</label>
                                        <input type="text" class="form-control" name="jefe" id="jefe" readonly>
                                    </div>
                                    
                                    <div class="form-group col-md-6">
                                        <label>Inicio</label>
                                        <input type="text" class="form-control" name="inicio" id="inicio" readonly>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Fin</label>
                                        <input type="text" class="form-control" name="fin" id="fin" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Tiempo solicitado</label>
                                        <input type="text" class="form-control" name="tiempo_soli" id="tiempo_soli" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Días hábiles</label>
                                        <input type="text" class="form-control" name="dias_habiles" id="dias_habiles" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Días no hábiles</label>
                                        <input type="text" class="form-control" name="dias_finde" id="dias_finde" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observación</label>
                                        <textarea type="text" class="form-control" name="descrip" id="descrip" readonly=""></textarea>
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

            <!--VACAS-->
            <div class="modal fade"  role="dialog" aria-hidden="true" id="aprobdirvac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Aprobación directa
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
                                        <label>Id solicitud *</label>
                                        <input type="number" class="form-control" placeholder="Ingrese el ID de la solicitud y presione ENTER" name="idsolivac" id="idsolivac" onchange="checkvac()">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="fechasolivac" id="fechasolivac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="nombreservac" id="nombreservac" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo</label>
                                        <select class="form-control" name="motivac" id="motivac" required readonly>
                                            <option value="0">Seleccione motivo</option>
                                            <%for (motivo_vacaciones busq : listadoMotivosVac) {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha ing. labores</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="labvac" id="labvac" required="" readonly >
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="cargovac" id="cargovac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="unidadvac" id="unidadvac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="modvac" id="modvac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Desde</label>
                                        <input type="text" class="form-control"  placeholder="fecha inicio" name="inivac" id="inivac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Hasta</label>
                                        <input type="text" class="form-control" placeholder="Fecha finvac" name="finvac" id="finvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="retvac" id="retvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias solicitados</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="tiempovac" id="tiempovac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Periodo</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="pervac" id="pervac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Observaciones</label>
                                        <textarea type="text" class="form-control" placeholder="Observaciones" name="obsvac" id="obsvac" required="" readonly=""></textarea>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button hidden="" id="btnaprvac" type="button" onclick="aprobarVac()" class="btn btn-primary">Aprobar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="actDirVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Actualizar Dirección
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
                                        <label>Id solicitud *</label>
                                        <input type="number" class="form-control" placeholder="Ingrese el ID de la solicitud y presione ENTER" name="idsoliadvac" id="idsoliadvac" onchange="cargarActDirVac()">
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de solicitud</label>
                                        <input type="text" class="form-control" placeholder="Ingrese fecha de solicitud" name="fechasoliadvac" id="fechasoliadvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="nombreseradvac" id="nombreseradvac" required readonly>
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Motivo</label>
                                        <select class="form-control" name="motiadvac" id="motiadvac" required readonly>
                                            <option value="0">Seleccione motivo</option>
                                            <%for (motivo_vacaciones busq : listadoMotivosVac) {%>
                                            <option value="<%= busq.getId_motivo()%>"><%= busq.getDescripcion()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Cargo</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="cargoadvac" id="cargoadvac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado el cargovac
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Unidad a la que pertenece</label>
                                        <input type="text" class="form-control" placeholder="Ingrese nombre de funcionario" name="unidadadvac" id="unidadadvac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Modalidad</label>
                                        <input type="text" class="form-control" placeholder="Modalidad de funcionario" name="modadvac" id="modadvac" required readonly >
                                        <div class="invalid-feedback">
                                            No ha ingresado ningun nombre
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Desde</label>
                                        <input type="text" class="form-control"  placeholder="fecha inicio" name="iniadvac" id="iniadvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Hasta</label>
                                        <input type="text" class="form-control" placeholder="Fecha finvac" name="finadvac" id="finadvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Fecha de retorno</label>
                                        <input type="text" class="form-control" placeholder="Fecha de retorno" name="retadvac" id="retadvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Dias solicitados</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="tiempoadvac" id="tiempoadvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-8">
                                        <label>Periodo</label>
                                        <input type="text" class="form-control" placeholder="Dias solicitados" name="peradvac" id="peradvac" required="" readonly>
                                        <div class="invalid-feedback">
                                            No ha ninguna fecha
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Dirección a cambiar</label>
                                        <select class="form-control" name="diradvac" id="diradvac" required>
                                            <%for (Direccion dir : listadoDirecciones) {%>
                                            <option value="<%= dir.getId()%>"><%= dir.getNombre()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button hidden="" id="btnActVac" type="button" onclick="actualizarVac()" class="btn btn-primary">Actualizar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade"  role="dialog" aria-hidden="true" id="adminIndVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Administración individual
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <div class="form-row">
                                <div class="form-group col-md-12">
                                    <label>Funcionario</label>
                                    <select style="width: 100%" class="form-control select2" name="listfunvac" id="listfunvac" required onchange="cambioFunVac()">
                                        <option <%= permiso_pendiente_vac != 0 ? "" : "selected"%> disabled="" value="0">Seleccione</option>
                                        <%
                                            usuario funcVac = enlace.getUsuarioId(vacacion_pendiente.getId_usuario());
                                            for (UsuarioIESS usu : listadoUsu) {%>
                                        <option value="<%= usu.getCodigo()%>" <%= permiso_pendiente_vac != 0 ? ((funcVac.getId_usuario() == 0 ? vacacion_pendiente.getCodigoUsu() : Integer.parseInt(funcVac.getCodigo_usuario())) == usu.getCodigo() ? "selected" : "disabled") : ""%>><%= usu.getNombres()%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button <%= permiso_pendiente_vac != 0 ? "" : "hidden"%> id="btnFunVac" type="button" onclick="administrarVac()" class="btn btn-primary">Administrar permisos</button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>            

            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRechazoVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de rechazo
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formrechazoGVac" action="administrar_permiso.control?accion=rechazar_vacacion" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtiu11vac" id="txtiu11vac" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" placeholder="Dias pendientes" name="txtidper1vac" id="txtidper1vac">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de rechazo</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1vac" id="arearazon1vac" required=""></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" value="Upload" id="btnRechazarVac" class="btn btn-primary">Rechazar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalAnulacionVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registrar motivo de anulación
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formanulacionvac" action="administrar_permiso.control?accion=anular_vacacion" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id usuario</label>
                                        <input type="text" class="form-control" placeholder="id usuario" name="txtusuvac" id="txtusuvac" value="<%= informacion.getId_usuario()%>">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2" hidden="">
                                        <label>id permiso</label>
                                        <input type="text" class="form-control" name="txtpervac" id="txtpervac">
                                        <div class="invalid-feedback">
                                            ningun id
                                        </div>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón de anulación</label>
                                        <select class="form-control" name="motivovac" id="motivovac" required>
                                            <%for (MotivoAnulacion mot : motivos) {%>
                                            <option <%= mot.getId() == 1 ? "selected" : ""%> value="<%=mot.getId()%>"><%=mot.getMotivo()%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                    <div id="divadju" class="form-group col-md-12">
                                        <label>Adjunto (obligatorio, PDF tamaño máximo 2MB)</label>
                                        <input type="file" class="form-control" name="archivovac" id="archivovac" onchange="validateFileVac()" required>
                                        <div class="invalid-feedback">
                                            No adjuntó ningún archivo
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button id="btnAnularVac" type="submit" value="Upload" class="btn btn-primary">Anular</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRechazoVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de rechazo
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
                                        <label>Funcionario que rechaza</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrechazavac" id="txtrechazavac" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón</label>
                                        <textarea type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1vac" id="arearazon1vac" readonly></textarea>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharechazo1vac" id="txtfecharechazo1vac" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAnulacionVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de anulación
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
                                        <label>Funcionario que anula</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtanulavac" id="txtanulavac" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Razón</label>
                                        <input type="text" class="form-control" placeholder="Describa de manera breve razón por la que se rechaza la solicitud" name="arearazon1vac" id="arearazon1vac" readonly>
                                        <div class="invalid-feedback">
                                            No se ha detallado la razón
                                        </div>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharechazo1vac" id="txtfecharechazo1vac" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleAprobacionVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de aprobación
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
                                        <label>Funcionario que aprueba</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtapruebavac" id="txtapruebavac" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfechaapruebavac" id="txtfechaapruebavac" required="" readonly >
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleRevisionVac">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de revisión
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
                                        <label>Funcionario que revisa</label>
                                        <input type="text" class="form-control" placeholder="Funcionario que rechaza" name="txtrevisavac" id="txtrevisavac" required="" readonly >
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" placeholder="Fecha de registro" name="txtfecharevisavac" id="txtfecharevisavac" required="" readonly >
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

            <footer class="main-footer">
                <div class="footer-center">
                    Copyright &copy; <%= LocalDate.now().getYear()%> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
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
    <script src="assets/modules/bootstrap-daterangepicker/daterangepicker.js"></script>
    <script src="assets/modules/bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js"></script>
    <script src="assets/modules/bootstrap-timepicker/js/bootstrap-timepicker.min.js"></script>
    <script src="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
    <script src="assets/modules/select2/dist/js/select2.full.min.js"></script>
    <script src="assets/modules/jquery-selectric/jquery.selectric.min.js"></script>
    <script src="assets/modules/datatables/datatables.min.js"></script>
    <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
    <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/index-0.js"></script>
    <!-- JS Libraies -->
    <script src="assets/modules/fullcalendar/fullcalendar.min.js"></script>
    <script src="assets/modules/fullcalendar/locale/es.js"></script>
    <script src="assets/modules/izitoast/js/iziToast.min.js"></script>

    <!-- Page Specific JS File -->
    <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/forms-advanced-forms.js"></script>
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-toastr.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-calendar.js"></script>
    <!-- Template JS File -->

    <script src="assets/js/scripts.js"></script>
    <script src="assets/js/custom.js"></script>
    <script src="fun_js/formulario_horas.js" type="text/javascript"></script>
    <script src="fun_js/funciones_horas.js" type="text/javascript"></script>

    <script type="text/javascript">
                                            window.onload = function () {
                                                if (<%=permiso_pendiente%> !== 0) {
                                                    $('#adminInd').modal("show");
                                                    iziToast.warning({
                                                        title: 'Permiso con ID <%= permiso_pendiente%> anulado',
                                                        message: 'Ingrese el nuevo permiso para poder continuar',
                                                        position: 'topRight',
                                                    });
                                                } else if (<%=permiso_pendiente_vac%> !== 0) {
                                                    $('#adminIndVac').modal("show");
                                                    iziToast.warning({
                                                        title: 'Permiso con ID <%= permiso_pendiente_vac%> anulado',
                                                        message: 'Ingrese el nuevo permiso para poder continuar',
                                                        position: 'topRight',
                                                    });
                                                }
                                            }
                                            
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
                                            
                                            $('#modalValidarECU').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #ipe').val(id_permiso);
                                            })
                                            
                                            $('#modalRechazoECU').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtidper1').val(id_permiso);
                                            })

                                            function aprobarSolicitudHoras(id_solicitud, id_usuario) {
                                                Swal.fire({
                                                    title: 'Aprobación',
                                                    text: "¿Desea aprobar esta solicitud?",
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Sí, aprobar',
                                                    cancelButtonText: 'No, cancelar',
                                                    customClass: {
                                                        confirmButton: 'btn btn-success',
                                                        cancelButton: 'btn btn-danger'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        Swal.fire({
                                                            title: 'Aprobando solicitud',
                                                            text: 'Por favor espere',
                                                            timerProgressBar: true,
                                                            showConfirmButton: false,
                                                            allowOutsideClick: () => !Swal.isLoading(),
                                                            allowEscapeKey: () => !Swal.isLoading(),
                                                            didOpen: () => {
                                                                Swal.showLoading();
                                                            }
                                                        })
                                                        $.post('administrar_permiso.control?accion=aprobar_horas', {
                                                            ipe: id_solicitud,
                                                            iu: id_usuario
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                Swal.fire({
                                                                    title: "Solicitud aprobada",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error",
                                                                    text: "No se aprobó la solicitud",
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "No se aprobó la solicitud",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
                                                            });
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Acción cancelada",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            $('#modalAnulacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtper').val(id_permiso);
                                            })

                                            $('#adminInd').on('hidden.bs.modal', function (event) {
                                                if (<%= permiso_pendiente%> !== 0) {
                                                    $(this).modal("show");
                                                    iziToast.warning({
                                                        title: 'Permiso con ID <%= permiso_pendiente%> anulado',
                                                        message: 'Ingrese el nuevo permiso para poder continuar',
                                                        position: 'topRight',
                                                    });
                                                }
                                            })

                                            function alertarFaltaAsistencia() {
                                                Swal.fire({
                                                    title: "Asistencia a cita médica faltante",
                                                    text: "Para aprobar una solicitud de tipo cita médica el funcionario solicitante debe adjuntar la asistencia a la cita.",
                                                    icon: "error",
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                });
                                            }

                                            $(document).ready(function () {
                                                $(document).ready(function () {
                                                    $('#formrechazo').submit(function (event) {
                                                        document.getElementById('btnRechazar').hidden = true;
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
                                                                    title: 'Rechazando permiso',
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
                                                                        title: "Solicitud rechazada",
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: "Error",
                                                                        text: "No se rechazó la solicitud",
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se rechazó la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        });
                                                        return false;
                                                    });
                                                });
                                            });
                                            
                                            $(document).ready(function () {
                                                $(document).ready(function () {
                                                    $('#formvalidarecu').submit(function (event) {
                                                        document.getElementById('btnValidarECU').hidden = true;
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
                                                                    title: 'Validando permiso',
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
                                                                        title: "Solicitud validada",
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: "Error",
                                                                        text: "No se validó la solicitud",
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se validó la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        });
                                                        return false;
                                                    });
                                                });
                                            });
                                            
                                            $(document).ready(function () {
                                                $(document).ready(function () {
                                                    $('#formrechazoecu').submit(function (event) {
                                                        document.getElementById('btnRechazarECU').hidden = true;
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
                                                                    title: 'Rechazando permiso',
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
                                                                        title: "Solicitud rechazada",
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: "Error",
                                                                        text: "No se rechazó la solicitud",
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se rechazó la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        });
                                                        return false;
                                                    });
                                                });
                                            });

                                            $(document).ready(function () {
                                                $(document).ready(function () {
                                                    $('#formanulacion').submit(function (event) {
                                                        document.getElementById('btnAnular').hidden = true;
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
                                                                    title: 'Anulación en curso',
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
                                                                        title: 'Permiso anulado',
                                                                        text: response === 'x' ? 'Ingrese el nuevo permiso' : '',
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else {
                                                                    Swal.fire({
                                                                        title: 'Error',
                                                                        text: 'No se anuló la solicitud',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                }
                                                            },
                                                            error: function () {
                                                                Swal.fire({
                                                                    title: 'Error crítico',
                                                                    text: 'No se anuló la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        });
                                                        return false;
                                                    });
                                                });
                                            });

                                            $("#table-1").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'asc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-2").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-3").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-4").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'asc']],
                                                "columnDefs": [
                                                    {"sortable": true, "targets": [0, 1]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-5").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            function checkPer() {
                                                var btnApr = document.getElementById('btnapr');
                                                var btnAdj = document.getElementById('btnadj');
                                                var btnAsis = document.getElementById('btnasis');
                                                btnApr.hidden = true;
                                                btnAdj.hidden = true;
                                                btnAsis.hidden = true;
                                                Swal.fire({
                                                    title: 'Buscando permiso',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                var id = $('#idsoli').val();
                                                $.post('administrar_permiso.control?accion=consultar_permiso', {
                                                    id: id
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Permiso inválido",
                                                            text: "Sólo se pueden aprobar permisos pendientes o revisados",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    } else if (response === 'incompleto') {
                                                        alertarFaltaAsistencia();
                                                    } else if (response !== 'null') {
                                                        Swal.fire({
                                                            title: "Permiso cargado",
                                                            icon: "success",
                                                            showConfirmButton: false,
                                                            timer: 1000
                                                        });
                                                        var res = response.split('*');
                                                        $('#fechasoli').val(res[0]);
                                                        $('#nombreser').val(res[1]);
                                                        document.getElementById("moti").value = res[2];
                                                        $('#cargoser').val(res[3]);
                                                        $('#unidadser').val(res[4]);
                                                        $('#ini').val(res[5]);
                                                        $('#fin').val(res[6]);
                                                        $('#tiempo').val(res[7]);
                                                        $('#modali').val(res[8]);
                                                        $('#obs').val(res[9]);
                                                        btnApr.hidden = false;
                                                        if(res[10]!=='x'){
                                                            btnAdj.hidden = false;
                                                            btnAdj.onclick = function () {
                                                                window.open('administrar_permiso.control?accion=adjunto_horas&id_permiso='+id, '_blank');
                                                            };
                                                        }
                                                        if(res[11]!=='x'){
                                                            btnAsis.hidden = false;
                                                            btnAsis.onclick = function () {
                                                                window.open('administrar_permiso.control?accion=descargar_asistencia&id_permiso='+id, '_blank');
                                                            };
                                                        }
                                                    } else {
                                                        Swal.fire({
                                                            title: "Permiso inexistente",
                                                            text: "No se encontró la solicitud",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al buscar la solicitud",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function checkPerAd() {
                                                Swal.fire({
                                                    title: 'Buscando permiso',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                $('#btnAct').attr("hidden", true);
                                                var id = $('#idsoliad').val();
                                                $.post('administrar_permiso.control?accion=consultar_permiso', {
                                                    id: id
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Permiso inválido",
                                                            text: "Sólo se pueden aprobar permisos pendientes o revisados",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    } else if (response !== 'null') {
                                                        Swal.fire({
                                                            title: "Permiso cargado",
                                                            icon: "success",
                                                            showConfirmButton: false,
                                                            timer: 1000
                                                        });
                                                        var res = response.split('*');
                                                        $('#fechasoliad').val(res[0]);
                                                        $('#nombreserad').val(res[1]);
                                                        document.getElementById("motiad").value = res[2];
                                                        $('#cargoserad').val(res[3]);
                                                        $('#unidadserad').val(res[4]);
                                                        $('#iniad').val(res[5]);
                                                        $('#finad').val(res[6]);
                                                        $('#tiempoad').val(res[7]);
                                                        $('#modaliad').val(res[8]);
                                                        $('#obsad').val(res[9]);
                                                        $('#btnAct').removeAttr("hidden");
                                                    } else {
                                                        Swal.fire({
                                                            title: "Permiso inexistente",
                                                            text: "No se encontró la solicitud",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al buscar la solicitud",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function aprobar() {
                                                var btn = document.getElementById("btnapr");
                                                btn.hidden = true;
                                                Swal.fire({
                                                    title: 'Aprobando permiso',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                var id = $('#idsoli').val();
                                                var moti = $('#moti').val();
                                                $.post('administrar_permiso.control?accion=aprobdir', {
                                                    id: id,
                                                    moti: moti,
                                                    ap: <%=informacion.getId_usuario()%>
                                                }, function (response) {
                                                    if (response) {
                                                        Swal.fire({
                                                            title: "Solicitud aprobada",
                                                            text: "La solicitud fue aprobada",
                                                            icon: "success",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Error al aprobar",
                                                            text: "Ocurrió un error al aprobar",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al aprobar",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function actualizar() {
                                                var btn = document.getElementById("btnAct");
                                                btn.hidden = true;
                                                Swal.fire({
                                                    title: 'Actualizando',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                var id = $('#idsoliad').val();
                                                $.post('administrar_permiso.control?accion=actdirhoras', {
                                                    id: id,
                                                    idDir: $('#dirad').val()
                                                }, function (response) {
                                                    if (response) {
                                                        Swal.fire({
                                                            title: "Dirección actualizada",
                                                            icon: "success",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Error al actualizar",
                                                            text: "Ocurrió un error al intentar actualizar la dirección",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                        btn.hidden = false;
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al intentar actualizar la dirección",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function administrar() {
                                                location.href = "permiso_horas_admin.jsp?per_hor_admin=" + $('#listfun').val();
                                            }

                                            function administrarFirma() {
                                                var btnFirma = document.getElementById('btnFirma');
                                                btnFirma.hidden = true;
                                                var list = document.getElementById('listfirma');
                                                var fun = list[list.selectedIndex].text;
                                                Swal.fire({
                                                    title: 'Confirmación de firma',
                                                    text: '¿Desea asignar al/la ' + fun + ' para que firme las solicitudes de permisos y vacaciones?',
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Sí, asignar',
                                                    cancelButtonText: 'No, cancelar',
                                                    customClass: {
                                                        confirmButton: 'btn btn-success',
                                                        cancelButton: 'btn btn-danger'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        Swal.fire({
                                                            title: 'Configurando firma',
                                                            text: 'Por favor espere',
                                                            timerProgressBar: true,
                                                            showConfirmButton: false,
                                                            allowOutsideClick: () => !Swal.isLoading(),
                                                            allowEscapeKey: () => !Swal.isLoading(),
                                                            didOpen: () => {
                                                                Swal.showLoading();
                                                            }
                                                        })
                                                        $.post('administrar_permiso.control?accion=configurar_firma', {
                                                            id: list.value
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                Swal.fire({
                                                                    title: "Firma configurada",
                                                                    text: "Se asignó al/la " + fun + " para firmar las solicitudes de permisos y vacaciones",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error",
                                                                    text: "No se configuró la firma",
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "Ocurrió un error al configurar la firma",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
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
                                                        btnFirma.hidden = false;
                                                    }
                                                });
                                            }

                                            function validarSolicitudHoras(id_solicitud) {
                                                Swal.fire({
                                                    title: 'Confirmación',
                                                    text: "¿Desea validar esta solicitud?",
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    customClass: {
                                                        confirmButton: 'btn btn-success'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        $.post('administrar_permiso.control?accion=validar_horas', {
                                                            ipe: id_solicitud
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                Swal.fire({
                                                                    title: "Validado",
                                                                    text: "Se validó el permiso",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error al validar",
                                                                    text: "No se validó el permiso",
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                })
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "No se validó el permiso",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
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
                                            
                                            $('#modalDetallePermisoECU').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var fecha_soli = button.data('fecha_soli');
                                                var motivo = button.data('motivo');
                                                var soli = button.data('soli');
                                                var denominacion = button.data('denominacion');
                                                var unidad = button.data('unidad');
                                                var jefe = button.data('jefe');                                                
                                                var inicio = button.data('inicio');
                                                var fin = button.data('fin');
                                                var tiempo_soli = button.data('tiempo_soli');
                                                var dias_habiles = button.data('dias_habiles');
                                                var dias_finde = button.data('dias_finde');
                                                var descrip = button.data('descrip');
                                                var modal = $(this);
                                                modal.find('.modal-body #fecha_soli').val(fecha_soli);
                                                modal.find('.modal-body #motivo').val(motivo);
                                                modal.find('.modal-body #soli').val(soli);                                                
                                                modal.find('.modal-body #denominacion').val(denominacion);
                                                modal.find('.modal-body #unidad').val(unidad);
                                                modal.find('.modal-body #jefe').val(jefe);
                                                modal.find('.modal-body #inicio').val(inicio);
                                                modal.find('.modal-body #fin').val(fin);
                                                modal.find('.modal-body #tiempo_soli').val(tiempo_soli);                                                
                                                modal.find('.modal-body #dias_habiles').val(dias_habiles);
                                                modal.find('.modal-body #dias_finde').val(dias_finde);
                                                modal.find('.modal-body #descrip').val(descrip);
                                            })
                                            
                                            $('#modalDetalleRechazo').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('rechaza');
                                                var descripcion = button.data('descri');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtrechaza').val(rechaza);
                                                modal.find('.modal-body #arearazon1').val(descripcion);
                                                modal.find('.modal-body #txtfecharechazo1').val(fecha);
                                            })

                                            $('#modalDetalleAnulacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('rechaza');
                                                var descripcion = button.data('descri');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtanula').val(rechaza);
                                                modal.find('.modal-body #arearazon1').val(descripcion);
                                                modal.find('.modal-body #txtfecharechazo1').val(fecha);
                                            })

                                            $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('aprueba');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtaprueba').val(rechaza);
                                                modal.find('.modal-body #txtfechaaprueba').val(fecha);
                                            })

                                            $('#modalDetalleRevision').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var revisa = button.data('revisa');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtrevisa').val(revisa);
                                                modal.find('.modal-body #txtfecharevisa').val(fecha);
                                            })

                                            function cambioFun() {
                                                $('#btnFun').removeAttr('hidden')
                                            }

                                            function validateFile() {
                                                var input = document.getElementById("archivo");
                                                const fileSize = input.files[0].size / 1024 / 1024;
                                                var regex = new RegExp("(.*?)\.(pdf)$");
                                                if (!(regex.test(input.value.toLowerCase()))) {
                                                    Swal.fire({
                                                        title: "Formato incorrecto",
                                                        text: "El formato del archivo debe ser PDF",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivo').val('');
                                                } else if (fileSize > 2) {
                                                    Swal.fire({
                                                        title: "Mensaje",
                                                        text: "El archivo excede el tamaño máximo de 2 MB",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivo').val('');
                                                }
                                            }

                                            $('#modalRechazoVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_usuario = button.data('iu');
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtiu11vac').val(id_usuario);
                                                modal.find('.modal-body #txtidper1vac').val(id_permiso);
                                            })

                                            $('#modalAnulacionVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var id_permiso = button.data('ipe');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtpervac').val(id_permiso);
                                            })

                                            $('#adminIndVac').on('hidden.bs.modal', function (event) {
                                                if (<%= permiso_pendiente_vac%> !== 0) {
                                                    $(this).modal("show");
                                                    iziToast.warning({
                                                        title: 'Permiso con ID <%= permiso_pendiente_vac%> anulado',
                                                        message: 'Ingrese el nuevo permiso para poder continuar',
                                                        position: 'topRight',
                                                    });
                                                }
                                            })

                                            $(document).ready(function () {
                                                $('#formrechazoGVac').submit(function (event) {
                                                    document.getElementById('btnRechazarVac').hidden = true;
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
                                                                title: 'Rechazando permiso',
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
                                                                    title: "Solicitud rechazada",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error",
                                                                    text: "No se rechazó la solicitud",
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        },
                                                        error: function () {
                                                            Swal.fire({
                                                                title: 'Error crítico',
                                                                text: 'No se rechazó la solicitud',
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
                                                            });
                                                        }
                                                    });
                                                    return false;
                                                });
                                            });

                                            $(document).ready(function () {
                                                $('#formanulacionvac').submit(function (event) {
                                                    document.getElementById('btnAnularVac').hidden = true;
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
                                                                title: 'Anulación en curso',
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
                                                                    title: 'Permiso anulado',
                                                                    text: response === 'x' ? 'Ingrese el nuevo permiso' : '',
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: 'Error',
                                                                    text: 'No se anuló la solicitud',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        },
                                                        error: function () {
                                                            Swal.fire({
                                                                title: 'Error crítico',
                                                                text: 'No se anuló la solicitud',
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
                                                            });
                                                        }
                                                    });
                                                    return false;
                                                });
                                            });
                                            
                                            $("#table-ecu-1").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'asc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });
                                            
                                            $("#table-ecu-2").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-ecu-3").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-2-vac").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'asc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-3-vac").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-4-vac").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            $("#table-5-vac").dataTable({
                                                "ordering": true,
                                                "order": [[0, 'desc']],
                                                "columnDefs": [
                                                    {"sortable": false, "targets": [2, 3]}
                                                ], "pageLength": 10,
                                                language: {
                                                    "decimal": "",
                                                    "emptyTable": "No hay datos",
                                                    "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
                                                    "infoEmpty": "Mostrando 0 a 0 de 0 registros",
                                                    "infoFiltered": "(Filtro de _MAX_ total registros)",
                                                    "infoPostFix": "",
                                                    "thousands": ",",
                                                    "lengthMenu": "Mostrar _MENU_ registros",
                                                    "loadingRecords": "Cargando...",
                                                    "processing": "Procesando...",
                                                    "search": "Buscar:",
                                                    "zeroRecords": "No se encontraron coincidencias",
                                                    "paginate": {
                                                        "first": "Primero",
                                                        "last": "Ultimo",
                                                        "next": "Próximo",
                                                        "previous": "Anterior"
                                                    }
                                                }
                                            });

                                            function checkvac() {
                                                Swal.fire({
                                                    title: 'Buscando solicitud',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                $('#btnaprvac').attr("hidden", true);
                                                var id = $('#idsolivac').val();
                                                $.post('administrar_permiso.control?accion=consultar_vaca', {
                                                    id: id
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Solicitud inválida",
                                                            text: "Sólo se pueden aprobar vacaciones pendientes o revisadas",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    } else if (response !== 'null') {
                                                        Swal.fire({
                                                            title: "Solicitud cargada",
                                                            icon: "success",
                                                            showConfirmButton: false,
                                                            timer: 1000
                                                        });
                                                        var res = response.split('*');
                                                        $('#fechasolivac').val(res[0]);
                                                        $('#nombreservac').val(res[1]);
                                                        document.getElementById("motivac").value = res[2];
                                                        $('#labvac').val(res[3]);
                                                        $('#cargovac').val(res[4]);
                                                        $('#unidadvac').val(res[5]);
                                                        $('#modvac').val(res[6]);
                                                        $('#inivac').val(res[7]);
                                                        $('#finvac').val(res[8]);
                                                        $('#retvac').val(res[9]);
                                                        $('#tiempovac').val(res[10]);
                                                        $('#pervac').val(res[11] === 'null' ? 'NO DEFINIDO' : res[11]);
                                                        $('#obsvac').val(res[13]);
                                                        $('#btnaprvac').removeAttr("hidden");
                                                    } else {
                                                        Swal.fire({
                                                            title: "Permiso inexistente",
                                                            text: "No se encontró la solicitud",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al buscar la solicitud",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function cargarActDirVac() {
                                                Swal.fire({
                                                    title: 'Buscando solicitud',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                $('#btnActVac').attr("hidden", true);
                                                $('#cargoadvac').attr("readonly", true);
                                                var id = $('#idsoliadvac').val();
                                                $.post('administrar_permiso.control?accion=consultar_vaca', {
                                                    id: id
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Solicitud inválida",
                                                            text: "Sólo se pueden actualizar vacaciones pendientes o revisadas",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    } else if (response !== 'null') {
                                                        Swal.fire({
                                                            title: "Solicitud cargada",
                                                            icon: "success",
                                                            showConfirmButton: false,
                                                            timer: 1000
                                                        });
                                                        var res = response.split('*');
                                                        $('#fechasoliadvac').val(res[0]);
                                                        $('#nombreseradvac').val(res[1]);
                                                        document.getElementById("motiadvac").value = res[2];
                                                        $('#cargoadvac').val(res[4]);
                                                        $('#unidadadvac').val(res[5]);
                                                        $('#modadvac').val(res[6]);
                                                        $('#iniadvac').val(res[7]);
                                                        $('#finadvac').val(res[8]);
                                                        $('#retadvac').val(res[9]);
                                                        $('#tiempoadvac').val(res[10]);
                                                        $('#peradvac').val(res[11] === 'null' ? 'NO DEFINIDO' : res[11]);
                                                        $('#btnActVac').removeAttr("hidden");
                                                        $('#cargoadvac').removeAttr("readonly");
                                                    } else {
                                                        Swal.fire({
                                                            title: "Permiso inexistente",
                                                            text: "No se encontró la solicitud",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al buscar la solicitud",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function aprobarSolicitudVacaciones(id_solicitud, id_usuario) {
                                                Swal.fire({
                                                    title: '¿Desea aprobar la solicitud?',
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Sí, aprobar',
                                                    cancelButtonText: 'No, cancelar',
                                                    customClass: {
                                                        confirmButton: 'btn btn-success',
                                                        cancelButton: 'btn btn-danger'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        Swal.fire({
                                                            title: 'Aprobando solicitud',
                                                            text: 'Por favor espere',
                                                            timerProgressBar: true,
                                                            showConfirmButton: false,
                                                            allowOutsideClick: () => !Swal.isLoading(),
                                                            allowEscapeKey: () => !Swal.isLoading(),
                                                            didOpen: () => {
                                                                Swal.showLoading();
                                                            }
                                                        })
                                                        $.post('administrar_permiso.control?accion=aprobar_vacacion', {
                                                            ipe: id_solicitud,
                                                            iu: id_usuario
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                var respuesta = parseInt(responseText);
                                                                if (respuesta === 1) {
                                                                    Swal.fire({
                                                                        title: "Solicitud aprobada",
                                                                        icon: "success",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else if (respuesta === -1) {
                                                                    Swal.fire({
                                                                        title: "Error al aprobar",
                                                                        text: 'La solicitud no fue aprobada',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else if (respuesta === -2) {
                                                                    Swal.fire({
                                                                        title: "Error al aprobar",
                                                                        text: 'No se actualizó el estado de la solicitud',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                } else if (respuesta === -3) {
                                                                    Swal.fire({
                                                                        title: "Error al aprobar",
                                                                        text: 'No se registró la aprobación de la solicitud',
                                                                        icon: "error",
                                                                        buttonsStyling: false,
                                                                        customClass: {
                                                                            confirmButton: 'btn btn-success'
                                                                        }
                                                                    }).then(function () {
                                                                        location.href = "ausencias_admin.jsp";
                                                                    });
                                                                }
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error al aprobar",
                                                                    text: 'La solicitud no fue aprobada',
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "Ocurrió un error al aprobar",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
                                                            });
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Acción cancelada",
                                                            icon: "warning",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                    }
                                                });
                                            }

                                            function aprobarVac() {
                                                var btn = document.getElementById("btnaprvac");
                                                btn.hidden = true;
                                                Swal.fire({
                                                    title: 'Aprobando solicitud',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                var id = $('#idsolivac').val();
                                                var fecha = $('#fechasolivac').val();
                                                $.post('administrar_permiso.control?accion=aprobdirv', {
                                                    id: id,
                                                    ap: <%=informacion.getId_usuario()%>,
                                                    fecha: fecha
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Solicitud aprobada",
                                                            icon: "success",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Error al aprobar",
                                                            text: "Ocurrió un error al aprobar",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al aprobar",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            function actualizarVac() {
                                                var btn = document.getElementById("btnActVac");
                                                btn.hidden = true;
                                                Swal.fire({
                                                    title: 'Actualizando',
                                                    text: 'Por favor espere',
                                                    timerProgressBar: true,
                                                    showConfirmButton: false,
                                                    allowOutsideClick: () => !Swal.isLoading(),
                                                    allowEscapeKey: () => !Swal.isLoading(),
                                                    didOpen: () => {
                                                        Swal.showLoading();
                                                    }
                                                })
                                                var id = $('#idsoliadvac').val();
                                                $.post('administrar_permiso.control?accion=actdir', {
                                                    id: id,
                                                    idDir: $('#diradvac').val(),
                                                    cargovac: $('#cargoadvac').val()
                                                }, function (response) {
                                                    if (response === 'x') {
                                                        Swal.fire({
                                                            title: "Dirección actualizada",
                                                            icon: "success",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        }).then(function () {
                                                            location.href = "ausencias_admin.jsp";
                                                        });
                                                    } else {
                                                        Swal.fire({
                                                            title: "Error al actualizar",
                                                            text: "Ocurrió un error al intentar actualizar la dirección",
                                                            icon: "error",
                                                            buttonsStyling: false,
                                                            customClass: {
                                                                confirmButton: 'btn btn-success'
                                                            }
                                                        });
                                                        btn.hidden = false;
                                                    }
                                                }, ).fail(function () {
                                                    Swal.fire({
                                                        title: "Error crítico",
                                                        text: "Ocurrió un error al intentar actualizar la dirección",
                                                        icon: "error",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    }).then(function () {
                                                        location.href = "ausencias_admin.jsp";
                                                    });
                                                });
                                            }

                                            $('#modalDetalleRechazoVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('rechaza');
                                                var descripcion = button.data('descri');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtrechazavac').val(rechaza);
                                                modal.find('.modal-body #arearazon1vac').val(descripcion);
                                                modal.find('.modal-body #txtfecharechazo1vac').val(fecha);
                                            })

                                            $('#modalDetalleAnulacionVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('rechaza');
                                                var descripcion = button.data('descri');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtanulavac').val(rechaza);
                                                modal.find('.modal-body #arearazon1vac').val(descripcion);
                                                modal.find('.modal-body #txtfecharechazo1vac').val(fecha);
                                            })

                                            $('#modalDetalleAprobacionVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var rechaza = button.data('aprueba');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtapruebavac').val(rechaza);
                                                modal.find('.modal-body #txtfechaapruebavac').val(fecha);
                                            })

                                            $('#modalDetalleRevisionVac').on('show.bs.modal', function (event) {
                                                var button = $(event.relatedTarget);
                                                var revisa = button.data('revisa');
                                                var fecha = button.data('fech');
                                                var modal = $(this);
                                                modal.find('.modal-body #txtrevisavac').val(revisa);
                                                modal.find('.modal-body #txtfecharevisavac').val(fecha);
                                            })

                                            function administrarVac() {
                                                location.href = "permiso_vacaciones_admin.jsp?per_vaca_admin=" + $('#listfunvac').val();
                                            }

                                            function administrarFirmaVac() {
                                                var btnFirmaVac = document.getElementById('btnFirmaVac');
                                                btnFirmaVac.hidden = true;
                                                var list = document.getElementById('listfirmavac');
                                                var fun = list[list.selectedIndex].text;
                                                Swal.fire({
                                                    title: 'Confirmación de firma',
                                                    text: '¿Desea asignar al/la ' + fun + ' para que firme las solicitudes de permisos y vacaciones?',
                                                    icon: 'warning',
                                                    buttonsStyling: false,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Sí, asignar',
                                                    cancelButtonText: 'No, cancelar',
                                                    customClass: {
                                                        confirmButton: 'btn btn-success',
                                                        cancelButton: 'btn btn-danger'
                                                    }
                                                }).then((willDelete) => {
                                                    if (willDelete.isConfirmed) {
                                                        Swal.fire({
                                                            title: 'Configurando firma',
                                                            text: 'Por favor espere',
                                                            timerProgressBar: true,
                                                            showConfirmButton: false,
                                                            allowOutsideClick: () => !Swal.isLoading(),
                                                            allowEscapeKey: () => !Swal.isLoading(),
                                                            didOpen: () => {
                                                                Swal.showLoading();
                                                            }
                                                        })
                                                        $.post('administrar_permiso.control?accion=configurar_firma', {
                                                            id: list.value
                                                        }, function (responseText) {
                                                            if (responseText) {
                                                                Swal.fire({
                                                                    title: "Firma configurada",
                                                                    text: "Se asignó al/la " + fun + " para firmar las solicitudes de permisos y vacaciones",
                                                                    icon: "success",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            } else {
                                                                Swal.fire({
                                                                    title: "Error",
                                                                    text: "No se configuró la firma",
                                                                    icon: "error",
                                                                    buttonsStyling: false,
                                                                    customClass: {
                                                                        confirmButton: 'btn btn-success'
                                                                    }
                                                                }).then(function () {
                                                                    location.href = "ausencias_admin.jsp";
                                                                });
                                                            }
                                                        }, ).fail(function () {
                                                            Swal.fire({
                                                                title: "Error crítico",
                                                                text: "Ocurrió un error al configurar la firma",
                                                                icon: "error",
                                                                buttonsStyling: false,
                                                                customClass: {
                                                                    confirmButton: 'btn btn-success'
                                                                }
                                                            }).then(function () {
                                                                location.href = "ausencias_admin.jsp";
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
                                                        btnFirmaVac.hidden = false;
                                                    }
                                                });
                                            }

                                            function cambioFunVac() {
                                                $('#btnFunVac').removeAttr('hidden')
                                            }

                                            function validateFileVac() {
                                                var input = document.getElementById("archivovac");
                                                const fileSize = input.files[0].size / 1024 / 1024;
                                                var regex = new RegExp("(.*?)\.(pdf)$");
                                                if (!(regex.test(input.value.toLowerCase()))) {
                                                    Swal.fire({
                                                        title: "Formato incorrecto",
                                                        text: "El formato del archivo debe ser PDF",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivovac').val('');
                                                } else if (fileSize > 2) {
                                                    Swal.fire({
                                                        title: "Mensaje",
                                                        text: "El archivo excede el tamaño máximo de 2 MB",
                                                        icon: "warning",
                                                        buttonsStyling: false,
                                                        customClass: {
                                                            confirmButton: 'btn btn-success'
                                                        }
                                                    });
                                                    $('#archivovac').val('');
                                                }
                                            }
    </script>
</body>
</html>