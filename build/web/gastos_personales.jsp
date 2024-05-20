<%@page import="modelo.GastosPersonales"%>
<%@page import="java.time.LocalDate"%>
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
    conexion_oracle oracle = new conexion_oracle();
    int id = 0, cod = 0;
    double ingresos = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<GastosPersonales> listadoPendientes = new ArrayList<>(), listadoAprobados = new ArrayList<>(), listadoValidados = new ArrayList<>(), listadoRechazados = new ArrayList<>();        
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoPendientes = enlace.GetGastosPersonales(informacion.getId_usuario(), 0);
        listadoAprobados = enlace.GetGastosPersonales(informacion.getId_usuario(), 1);
        listadoValidados = enlace.GetGastosPersonales(informacion.getId_usuario(), 2);
        listadoRechazados = enlace.GetGastosPersonales(informacion.getId_usuario(), 3);
        try {
            cod = Integer.parseInt(informacion.getCodigo_usuario());
        } catch (Exception e) {}
        ingresos = oracle.getIngresos(cod);
    } catch (Exception e) {
        System.out.println("ex | "+e);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Gastos personales</title>
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
                            <h1>Formulario de Proyección de Gastos Personales</h1>
                            <%if(listadoPendientes.size() + listadoAprobados.size() + listadoValidados.size() == 0){%>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="javascript:" class="btn btn-primary" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalRegistro"> <i class="fas fa-plus"></i> Nueva solicitud</a>
                                </div>
                            </div>
                            <%}%>
                        </div>                        
                        <div class="card">
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><i class="fas fa-bars"></i> Pendientes <%if (listadoPendientes.size() != 0) {%><span class="badge badge-primary"><%= listadoPendientes.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="apro-tab" data-toggle="tab" href="#apro" role="tab" aria-controls="apro" aria-selected="false"><i class="fas fa-check"></i> Aprobados <%if (listadoAprobados.size() != 0) {%><span class="badge badge-primary"><%= listadoAprobados.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="vali-tab" data-toggle="tab" href="#vali" role="tab" aria-controls="vali" aria-selected="false"><i class="fas fa-check-double"></i> Validados <%if (listadoValidados.size() != 0) {%><span class="badge badge-primary"><%= listadoValidados.size()%></span><%}%></a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="reject-tab" data-toggle="tab" href="#reject" role="tab" aria-controls="reject" aria-selected="false"><i class="fas fa-times"></i> Rechazados <%if (listadoRechazados.size() != 0) {%><span class="badge badge-primary"><%= listadoRechazados.size()%></span><%}%></a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-x">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Total ingresos</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (GastosPersonales g : listadoPendientes) {%>
                                                    <tr>
                                                        <td style="width: 5%"><%= g.getId()%></td>
                                                        <td style="width: 15%"><%= g.getCreacion()%></td>
                                                        <td style="width: 15%"><%= g.getC105()%></td>
                                                        <td style="width: 10%">
                                                            <%if (g.getAdjunto()!=null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= g.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                            <%}%>
                                                        </td>
                                                        <td style="width: 20%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalle" data-fecha="<%= g.getCreacion()%>" data-usuario="<%= g.getUsuario().getNombre()%>" data-c103="<%= g.getC103()%>" data-c104="<%= g.getC104()%>" data-c105="<%= g.getC105()%>" data-c106="<%= g.getC106()%>" data-c107="<%= g.getC107()%>" data-c108="<%= g.getC108()%>" data-c109="<%= g.getC109()%>" data-c110="<%= g.getC110()%>" data-c111="<%= g.getC111()%>" data-c112="<%= g.getC112()%>" data-c113="<%= g.getC113()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver detalles"></i></a>
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalUpdate" data-id="<%= g.getId()%>" data-c103="<%= g.getC103()%>" data-c104="<%= g.getC104()%>" data-c105="<%= g.getC105()%>" data-c106="<%= g.getC106()%>" data-c107="<%= g.getC107()%>" data-c108="<%= g.getC108()%>" data-c109="<%= g.getC109()%>" data-c110="<%= g.getC110()%>" data-c111="<%= g.getC111()%>" data-c112="<%= g.getC112()%>" data-c113="<%= g.getC113()%>" class="btn btn-primary btn-sm active"><i class="fa fa-pen" data-toggle="tooltip" data-original-title="Actualizar"></i></a>
                                                            <a target="_blank" href="administrar_gastos.control?accion=generar_excel&id_gasto=<%= g.getId()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                            <a href="javascript:" type="button" onclick="eliminarFormulario(<%= g.getId()%>)"  class="btn btn-primary btn-sm active"><i class="fa fa-times" data-toggle="tooltip" data-original-title="Eliminar"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="apro" role="tabpanel" aria-labelledby="apro-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-2">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Total ingresos</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (GastosPersonales g : listadoAprobados) {%>
                                                    <tr>
                                                        <td style="width: 5%"><%= g.getId()%></td>
                                                        <td style="width: 15%"><%= g.getCreacion()%></td>
                                                        <td style="width: 15%"><%= g.getC105()%></td>
                                                        <td style="width: 10%">
                                                            <%if (g.getAdjunto()!=null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= g.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                            <%}%>
                                                        </td>
                                                        <td style="width: 20%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleAprobacion" data-aprueba="<%= g.getAprobacion().getAprueba().getNombre()%>" data-fecha="<%= g.getAprobacion().getCreacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver aprobación"></i></a>
                                                            <a target="_blank" href="administrar_gastos.control?accion=generar_excel&id_gasto=<%= g.getId()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="vali" role="tabpanel" aria-labelledby="vali-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-3">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Total ingresos</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (GastosPersonales g : listadoValidados) {%>
                                                    <tr>
                                                        <td style="width: 5%"><%= g.getId()%></td>
                                                        <td style="width: 15%"><%= g.getCreacion()%></td>
                                                        <td style="width: 15%"><%= g.getC105()%></td>
                                                        <td style="width: 10%">
                                                            <%if (g.getAdjunto()!=null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= g.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                            <%}%>
                                                        </td>
                                                        <td style="width: 20%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleValidacion" data-valida="<%= g.getValidacion().getValida().getNombre()%>" data-fecha="<%= g.getValidacion().getCreacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver validación"></i></a>
                                                            <a target="_blank" href="administrar_gastos.control?accion=generar_excel&id_gasto=<%= g.getId()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="reject" role="tabpanel" aria-labelledby="reject-tab">
                                        <div class="table-responsive">
                                            <table class="table table-striped" id="table-4">
                                                <thead>                                 
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Fecha</th>
                                                        <th>Total ingresos</th>
                                                        <th>Adjunto</th>
                                                        <th>Acción</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%for (GastosPersonales g : listadoRechazados) {%>
                                                    <tr>
                                                        <td style="width: 5%"><%= g.getId()%></td>
                                                        <td style="width: 15%"><%= g.getCreacion()%></td>
                                                        <td style="width: 15%"><%= g.getC105()%></td>
                                                        <td style="width: 10%">
                                                            <%if (g.getAdjunto()!=null) {%>
                                                            <a target="_blank" href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= g.getAdjunto()%>" class="btn"><i class="fas fa-paperclip" data-toggle="tooltip" data-original-title="Descargar adjunto"></i></a>
                                                            <%}%>
                                                        </td>
                                                        <td style="width: 20%">
                                                            <a href="javascript:" type="button" data-toggle="modal" data-target="#modalDetalleRechazo" data-rechaza="<%= g.getRechazo().getRechaza().getNombre()%>" data-motivo="<%= g.getRechazo().getMotivo()%>" data-fecha="<%= g.getRechazo().getCreacion()%>" class="btn btn-primary btn-sm active"><i class="fa fa-eye" data-toggle="tooltip" data-original-title="Ver rechazo"></i></a>
                                                            <a target="_blank" href="administrar_gastos.control?accion=generar_excel&id_gasto=<%= g.getId()%>" class="btn btn-primary btn-sm"><i data-toggle="tooltip" data-original-title="Generar documento" class="fa fa-print"></i></a>
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
                    </section>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalRegistro">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Registro de Formulario de Proyección de Gastos Personales
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formRegistro" action="administrar_gastos.control?accion=registro_formulario" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <span style="font-weight: bold">
                                        Ingresos proyectados
                                    </span>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con este empleador (sólo se considera la remuneración mensual unificada)</label>
                                        <input type="number" step="0.01" class="form-control" placeholder="Ingrese el valor" name="c103" id="c103" value="<%= ingresos%>" min="<%= ingresos%>" required onchange="calcular105('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con otros empleadores (en caso de haberlos)</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c104" id="c104" required onchange="cambioC104('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div id="divadj" class="form-group col-md-6" hidden="">
                                        <label>Adjuntar certificado (extensión pdf, tamaño máximo 2 MB)</label>
                                        <input type="file" class="form-control" name="adjunto" id="adjunto" onchange="validateSize('')">
                                        <div class="invalid-feedback">
                                            No adjuntó certificado
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Total de ingresos proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="c105" id="c105" value="<%= ingresos%>" readonly required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <span style="font-weight: bold">
                                        Gastos proyectados
                                    </span>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vivienda</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c106" id="c106" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de educación, arte y cultura</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c107" id="c107" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de salud</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c108" id="c108" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vestimenta</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c109" id="c109" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de alimentación</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c110" id="c110" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de turismo</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="c111" id="c111" required onchange="calcular112('')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Total de gastos proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="c112" id="c112" readonly required>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Rebaja de Impuesto a la Renta por gastos personales proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="c113" id="c113" readonly required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" id="btnGuardar" class="btn btn-primary">Guardar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalUpdate">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Actualización de Formulario de Proyección de Gastos Personales
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form id="formUpdate" action="administrar_gastos.control?accion=actualizar_formulario" method="post" enctype="multipart/form-data" class="needs-validation">
                                <div class="form-row">
                                    <span style="font-weight: bold">
                                        Ingresos proyectados
                                    </span>
                                </div>
                                <div class="form-row">
                                    <input class="form-control" id="uid" hidden>
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con este empleador (sólo se considera la remuneración mensual unificada)</label>
                                        <input type="number" step="0.01" class="form-control" placeholder="Ingrese el valor" name="uc103" id="uc103" required onchange="calcular105('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con otros empleadores (en caso de haberlos)</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc104" id="uc104" required onchange="cambioC104('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div id="udivadj" class="form-group col-md-6" hidden>
                                        <label>Adjuntar certificado (extensión pdf, tamaño máximo 2 MB)</label>
                                        <input type="file" class="form-control" name="uadjunto" id="uadjunto" onchange="validateSize('u')">
                                        <div class="invalid-feedback">
                                            No adjuntó certificado
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Total de ingresos proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="uc105" id="uc105" readonly required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <span style="font-weight: bold">
                                        Gastos proyectados
                                    </span>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vivienda</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc106" id="uc106" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de educación, arte y cultura</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc107" id="uc107" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de salud</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc108" id="uc108" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vestimenta</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc109" id="uc109" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de alimentación</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc110" id="uc110" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de turismo</label>
                                        <input type="number" step="0.01" min="0" value="0" class="form-control" placeholder="Ingrese el valor" name="uc111" id="uc111" required onchange="calcular112('u')">
                                        <div class="invalid-feedback">
                                            Valor inválido
                                        </div>  
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Total de gastos proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="uc112" id="uc112" readonly required>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Rebaja de Impuesto a la Renta por gastos personales proyectados</label>
                                        <input type="number" step="0.01" class="form-control" name="uc113" id="uc113" readonly required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" id="ubtnGuardar" class="btn btn-primary">Guardar</button>
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalle">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de formulario
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
                                        <label>Fecha de solicitud</label>
                                        <input type="text" class="form-control" name="fecha" id="fecha" readonly>
                                    </div>
                                    <div class="form-group col-md-8">
                                        <label>Apellidos y nombres del funcionario</label>
                                        <input type="text" class="form-control" name="usuario" id="usuario" readonly>
                                    </div>                                    
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con este empleador (sólo se considera la remuneración mensual unificada)</label>
                                        <input class="form-control" name="c103" id="c103" readonly>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Total de ingresos con otros empleadores (en caso de haberlos)</label>
                                        <input class="form-control" name="c104" id="c104" readonly>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Total de ingresos proyectados</label>
                                        <input class="form-control" name="c105" id="c105" readonly>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <span style="font-weight: bold">
                                        Gastos proyectados
                                    </span>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vivienda</label>
                                        <input class="form-control" name="c106" id="c106" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de educación, arte y cultura</label>
                                        <input class="form-control" name="c107" id="c107" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de salud</label>
                                        <input class="form-control" name="c108" id="c108" readonly>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label>Gastos de vestimenta</label>
                                        <input class="form-control" name="c109" id="c109" readonly> 
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de alimentación</label>
                                        <input class="form-control" name="c110" id="c110" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Gastos de turismo</label>
                                        <input class="form-control" name="c111" id="c111" readonly>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Total de gastos proyectados</label>
                                        <input class="form-control" name="c112" id="c112" readonly>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>Rebaja de Impuesto a la Renta por gastos personales proyectados</label>
                                        <input class="form-control" name="c113" id="c113" readonly>
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
                            <form class="needs-validation" novalidate>
                                <div class="form-row">
                                    <div class="form-group col-md-12">
                                        <label>Funcionario que rechaza</label>
                                        <input type="text" class="form-control" id="rechaza" readonly>
                                    </div>
                                    <div class="form-group col-md-12">
                                        <label>Motivo</label>
                                        <textarea type="text" class="form-control" id="motivo" readonly></textarea>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" id="fecha" readonly>
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
                            <form class="needs-validation" novalidate>
                                <div class="form-row">
                                    <div class="form-group col-md-12">
                                        <label>Funcionario que aprueba</label>
                                        <input type="text" class="form-control" id="aprueba" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" id="fecha" readonly>
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
            <div class="modal fade"  role="dialog" aria-hidden="true" id="modalDetalleValidacion">
                <div class="modal-dialog modal-lg" role="dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-center">
                                <span class="fw-extrabold">
                                    Detalle de validación
                                </span>
                            </h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p class="small"></p>
                            <form class="needs-validation" novalidate>
                                <div class="form-row">
                                    <div class="form-group col-md-12">
                                        <label>Funcionario que valida</label>
                                        <input type="text" class="form-control" id="valida" readonly>
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label>Fecha</label>
                                        <input type="text" class="form-control" id="fecha" readonly>
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
    <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <!-- Page Specific JS File -->
    <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>
    <script src="assets/js/page/modules-datatables.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/forms-advanced-forms.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-toastr.js"></script>
    <!-- Page Specific JS File -->
    <script src="assets/js/page/modules-calendar.js"></script>
    <!-- Template JS File -->
    <script src="assets/js/scripts.js"></script>
    <script type="text/javascript">
        $("#table-x").dataTable({
            "ordering": false,
            "order": [[0, 'asc']],
            "columnDefs": [
                {"sortable": false, "targets": [0]}
            ], "pageLength": 25,
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
        
        $('#modalDetalle').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #fecha').val(button.data('fecha'));
            modal.find('.modal-body #usuario').val(button.data('usuario'));
            modal.find('.modal-body #c103').val(button.data('c103'));
            modal.find('.modal-body #c104').val(button.data('c104'));
            modal.find('.modal-body #c105').val(button.data('c105'));
            modal.find('.modal-body #c106').val(button.data('c106'));
            modal.find('.modal-body #c107').val(button.data('c107'));
            modal.find('.modal-body #c108').val(button.data('c108'));
            modal.find('.modal-body #c109').val(button.data('c109'));
            modal.find('.modal-body #c110').val(button.data('c110'));
            modal.find('.modal-body #c111').val(button.data('c111'));
            modal.find('.modal-body #c112').val(button.data('c112'));
            modal.find('.modal-body #c113').val(button.data('c113'));
        })
        
        $('#modalUpdate').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #uid').val(button.data('id'));
            modal.find('.modal-body #uc103').val(button.data('c103'));
            modal.find('.modal-body #uc104').val(button.data('c104'));
            if(parseFloat(button.data('c104')) != 0){
                modal.find('.modal-body #udivadj').prop('hidden',false);
                modal.find('.modal-body #udivadj').prop('required', true);
            }
            modal.find('.modal-body #uc105').val(button.data('c105'));
            modal.find('.modal-body #uc106').val(button.data('c106'));
            modal.find('.modal-body #uc107').val(button.data('c107'));
            modal.find('.modal-body #uc108').val(button.data('c108'));
            modal.find('.modal-body #uc109').val(button.data('c109'));
            modal.find('.modal-body #uc110').val(button.data('c110'));
            modal.find('.modal-body #uc111').val(button.data('c111'));
            modal.find('.modal-body #uc112').val(button.data('c112'));
            modal.find('.modal-body #uc113').val(button.data('c113'));
        })
        
        $(document).ready(function () {
            $('#formRegistro').submit(function (event) {
                var btn = document.getElementById("btnGuardar");
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
                            title: 'Registrando formulario',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                    },
                    success: function (responseText) {
                        var response = parseInt(responseText);
                        if (response === 1) {
                            Swal.fire({
                                title: 'Registro exitoso',
                                icon: 'success',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -1) {
                            Swal.fire({
                                title: 'Valor inválido',
                                text: 'El total de ingresos con este empleador debe ser mayor a sus otros ingresos',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -2) {
                            Swal.fire({
                                title: 'Total de gastos inválido',
                                text: 'El total de gastos proyectados no debe superar 7 canastas básicas',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
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
                            location.href = "gastos_personales.jsp";
                        });
                    }
                });
                return false;
            });
        });
        
        $(document).ready(function () {
            $('#formUpdate').submit(function (event) {
                var btn = document.getElementById("ubtnGuardar");
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
                            title: 'Actualizando formulario',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                    },
                    success: function (responseText) {
                        var response = parseInt(responseText);
                        if (response === 1) {
                            Swal.fire({
                                title: 'Actualización exitosa',
                                icon: 'success',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === 0) {
                            Swal.fire({
                                title: 'Error al actualizar',
                                text: 'Ocurrió un error al actualizar',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -1) {
                            Swal.fire({
                                title: 'Total de ingresos inválido',
                                text: 'El total de ingresos debe ser distinto a lo ya registrado para actualizar',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -2) {
                            Swal.fire({
                                title: 'Valor inválido',
                                text: 'El total de ingresos con este empleador debe ser mayor a sus otros ingresos',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -3) {
                            Swal.fire({
                                title: 'Total de gastos inválido',
                                text: 'El total de gastos proyectados no debe superar 7 canastas básicas',
                                icon: 'error',
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        }
                    },
                    error: function () {
                        Swal.fire({
                            title: 'Error crítico',
                            text: 'No se pudo completar la actualización',
                            icon: 'error',
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "gastos_personales.jsp";
                        });
                    }
                });
                return false;
            });
        });
        
        function calcular105(prefix){
            var inputC103 = document.getElementById(prefix+'c103'), inputC104 = document.getElementById(prefix+'c104'), inputC105 = document.getElementById(prefix+'c105');
            var c103 = parseFloat(inputC103.value), c104 = parseFloat(inputC104.value), c105;
            if(isNaN(c103)){
                c103 = 0;
                inputC103.value = c103;
            }else{
                if(c103 < <%= ingresos%>){
                    Swal.fire({
                        title: "Valor inválido",
                        text: "El total de ingresos con este empleador debe ser mayor o igual a "+<%= ingresos%>,
                        icon: "warning",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    });
                    c103 = <%= ingresos%>;
                    inputC103.value = c103;
                }
            }
            if(isNaN(c104)){
                c104 = 0;
                inputC104.value = c104;
            }
            if(c103 >= c104){
                c105 = c103 + c104;
                inputC105.value = c105;                
            }else{
                Swal.fire({
                    title: "Valor inválido",
                    text: "El total de ingresos con este empleador debe ser mayor a sus otros ingresos",
                    icon: "warning",
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success'
                    }
                });
                inputC103.value = '';
            }            
        }
        
        function cambioC104(prefix){
            var input = document.getElementById(prefix+'c104'), totalOtros = parseFloat(input.value);
            var divadj = document.getElementById(prefix+'divadj'), adjunto = document.getElementById(prefix+'adjunto');
            if(isNaN(totalOtros) || totalOtros < 0){
                Swal.fire({
                    title: "Valor inválido",
                    text: "El total de ingresos con otros empleadores es inválido",
                    icon: "warning",
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-success'
                    }
                });
                input.value = 0;
            }else{
                if(totalOtros == 0){
                    divadj.hidden = true;
                    adjunto.required = false;
                }else{
                    divadj.hidden = false;
                    adjunto.required = true;
                }
            }
            calcular105(prefix);
        }
        
        var CANASTA_BASICA = 763.44, FRACCION_BASICA = 11722, CB7 = CANASTA_BASICA * 7;
        
        function calcularC113(prefix){
            var C105 = document.getElementById(prefix+'c105').value, C112 = document.getElementById(prefix+'c112').value, L = C112 < CB7 ? C112 : CB7;
            var r, inputC113 = document.getElementById(prefix+'c113');
            if(C105 <= 2.13 * FRACCION_BASICA){
                r = L * .2;
            }else{
                r = L * .1;
            }
            inputC113.value = r;
        }
        
        function calcular112(prefix){
            var btn = document.getElementById(prefix+"btnGuardar");
            btn.hidden = true;
            var inputC106 = document.getElementById(prefix+'c106'), inputC107 = document.getElementById(prefix+'c107'), inputC108 = document.getElementById(prefix+'c108'), inputC109 = document.getElementById(prefix+'c109'), inputC110 = document.getElementById(prefix+'c110'), inputC111 = document.getElementById(prefix+'c111'), inputC112 = document.getElementById(prefix+'c112');
            var c106 = parseFloat(inputC106.value), c107 = parseFloat(inputC107.value), c108 = parseFloat(inputC108.value), c109 = parseFloat(inputC109.value), c110 = parseFloat(inputC110.value), c111 = parseFloat(inputC111.value);
            if(isNaN(c106)){
                c106 = 0;
                inputC106.value = c106;
            }
            if(isNaN(c107)){
                c107 = 0;
                inputC107.value = c107;
            }
            if(isNaN(c108)){
                c108 = 0;
                inputC108.value = c108;
            }
            if(isNaN(c109)){
                c109 = 0;
                inputC109.value = c109;
            }
            if(isNaN(c110)){
                c110 = 0;
                inputC110.value = c110;
            }
            if(isNaN(c111)){
                c111 = 0;
                inputC111.value = c111;
            }
            var c112 = c106 + c107 + c108 + c109 + c110 + c111;
            if(c112 > CB7){
                swal("Total de gastos inválido", "El total de gastos proyectados no debe superar 7 canastas básicas: "+CB7, {
                    icon: "warning",
                    buttons: {
                        confirm: {
                            className: 'btn btn-warning'
                        }
                    },
                });
                c112 = '';
                btn.hidden = true;
            }else{
                btn.hidden = false;
            }
            inputC112.value = c112;
            calcularC113(prefix);            
        }

        function eliminarFormulario(id) {
            Swal.fire({
                title: '¿Desea eliminar este formulario?',
                icon: 'warning',
                buttonsStyling: false,
                showCancelButton: true,
                confirmButtonText: 'Sí',
                cancelButtonText: 'No',
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                }
            }).then((willDelete) => {
                if (willDelete.isConfirmed) {
                    Swal.fire({
                        title: 'Eliminando formulario',
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: () => !Swal.isLoading(),
                        allowEscapeKey: () => !Swal.isLoading(),
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    })
                    $.post('administrar_gastos.control?accion=eliminar_formulario', {
                        id_gasto: id
                    }, function (responseText) {
                        var response = parseInt(responseText);
                        if (response === 1) {
                            Swal.fire({
                                title: "Formulario eliminado",
                                icon: "success",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === 0) {
                            Swal.fire({
                                title: "Error al eliminar",
                                text: "Ocurrió un error al eliminar",
                                icon: "warning",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        } else if(response === -1) {
                            Swal.fire({
                                title: "Formulario inválido",
                                text: "Sólo se pueden eliminar formularios pendientes",
                                icon: "warning",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                location.href = "gastos_personales.jsp";
                            });
                        }
                    }, ).fail(function () {
                        Swal.fire({
                            title: "Error crítico",
                            text: "No se eliminó el formulario",
                            icon: "error",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        }).then(function () {
                            location.href = "gastos_personales.jsp";
                        });
                    });
                } else {
                    Swal.fire({
                        title: "Acción cancelada",
                        text: "No se eliminó el formulario",
                        icon: "info",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    })
                }
            });
        }
        
        function validateSize(prefix) {
            var input = document.getElementById(prefix+"adjunto");
            const fileSize = input.files[0].size / 1024 / 1024;
            const ext = input.value.toLowerCase();
            var regex = new RegExp("(.*?)\.(pdf)$");
            if (!(regex.test(ext))) {
                swal("Formato incorrecto", "El formato del archivo debe ser .pdf", {
                    icon: "warning",
                    buttons: {
                        confirm: {
                            className: 'btn btn-warning'
                        }
                    },
                });
                input.value = '';
            } else if (fileSize > 2){
                swal("Tamaño excedido", "El archivo excede el tamaño máximo de 2 MB", {
                    icon: "warning",
                    buttons: {
                        confirm: {
                            className: 'btn btn-warning'
                        }
                    },
                });
                input.value = '';
            }
        }

        $('#modalDetalleAprobacion').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #aprueba').val(button.data('aprueba'));
            modal.find('.modal-body #fecha').val(button.data('fecha'));
        })
        
        $('#modalDetalleValidacion').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #valida').val(button.data('valida'));
            modal.find('.modal-body #fecha').val(button.data('fecha'));
        })

        $('#modalDetalleRechazo').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);
            var modal = $(this);
            modal.find('.modal-body #rechaza').val(button.data('rechaza'));
            modal.find('.modal-body #motivo').val(button.data('motivo'));
            modal.find('.modal-body #fecha').val(button.data('fecha'));
        })        
    </script>
</body>
</html>