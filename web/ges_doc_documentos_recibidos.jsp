<%@page import="modelo.Documento"%>
<%@page import="java.sql.Date"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.tipo_tramite"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.rol_usuario"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion mysql = new conexion();
    final int ID_MOD_GES_DOC = 35;
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<Documento> listadoNuevos = null,
            listadoArchivados = null;
    java.sql.Date fecha_inicio = null, fecha_fin = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = mysql.buscar_usuarioID(id);
        foto = mysql.buscarFotoUsuarioID(informacion.getId_usuario());
        listaComponente = mysql.listadoComponentesTipoUsuarioID(informacion.getId_usuario(), ID_MOD_GES_DOC);
        if (request.getParameter("txtini") != null && request.getParameter("txtfin") != null) {
            fecha_inicio = Date.valueOf(request.getParameter("txtini"));
            fecha_fin = Date.valueOf(request.getParameter("txtfin"));
        }
        listadoNuevos = request.getParameter("txtini") == null ? mysql.getDocumentosRecibidos(informacion.getId_usuario(), 1) : mysql.getDocumentosRecibidos(informacion.getId_usuario(), 1, fecha_inicio, fecha_fin);
        listadoArchivados = request.getParameter("txtini") == null ? mysql.getDocumentosRecibidos(informacion.getId_usuario(), 2) : mysql.getDocumentosRecibidos(informacion.getId_usuario(), 2, fecha_inicio, fecha_fin);
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
        <title>Intranet Alcaldía - Listado de documentos recibidos</title>
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
                        <li class="dropdown"><a href="#" data-toggle="dropdown" class="nav-link dropdown-toggle nav-link-lg nav-link-user">
                                <%if (foto.getNombre().equalsIgnoreCase("ninguno") && foto.getRuta().equalsIgnoreCase("ninguno")) {%>
                                <img alt="image" src="assets/img/avatar/avatar-1.png" class="rounded-circle mr-1">
                                <%} else {%>
                                <img src="imagen.control?id=<%= informacion.getId_usuario()%>" alt="..." class="avatar-img rounded-circle">
                                <%}%>
                                <div class="d-sm-none d-lg-inline-block"><%= informacion.getApellido()%> <%= informacion.getNombre()%></div></a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <a href="sesion.control?accion=configurar_cuenta&iu=<%=informacion.getId_usuario()%>" class="dropdown-item has-icon">
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
                                <a href="ges_doc_inicio.jsp" class="nav-link"><i class="fas fa-home"></i><span>Inicio</span></a>
                            </li>
                            <li class="menu-header">COMPONENTES</li>
                                <%for (componente c : listaComponente) {
                                %>
                            <li class="<%= c.getRuta_enlace().equals("documentos_recibidos") ? "active" : ""%>"><a class="nav-link" href="sesion.control?accion=<%= c.getRuta_enlace()%>&iu=<%= id%>"><i class="<%= c.getIcono()%>"></i> <span><%= c.getDescripcion()%></span></a></li>
                                <%}%>
                        </ul>
                    </aside>
                </div>
                <br>
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Documentos recibidos</h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a href="ges_doc_nuevo_documento.jsp" class="btn btn-primary"> <i class="fas fa-pen"></i> Redactar nuevo</a>   
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
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
                                                    <a class="nav-link active" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="true"><i class="fas fa-envelope-open-text"></i> Recibidos <%if (listadoNuevos.size() != 0) {%><span class="badge badge-primary"><%= listadoNuevos.size()%></span><%}%></a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" id="archivados-tab" data-toggle="tab" href="#archivados" role="tab" aria-controls="archivados"><i class="fas fa-folder"></i> Archivados <%if (listadoArchivados.size() != 0) {%><span class="badge badge-primary"><%= listadoArchivados.size()%></span><%}%></a>
                                                </li>
                                            </ul>
                                            <div class="tab-content" id="myTabContent">
                                                <div class="tab-pane fade show active" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%"></th>
                                                                    <th style="width: 20%">Remitente</th>
                                                                    <th style="width: 25%">Asunto</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 15%">N° documento</th>
                                                                    <th style="width: 15%">N° referencia</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Documento d : listadoNuevos) {%>
                                                                <tr style="font-weight: <%= d.getDestinatario().getLeido() == null ? "bold" : "normal"%>">
                                                                    <td>
                                                                        <%if (d.isFirmado()) {%>
                                                                        <a class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Firmado electrónicamente"><i style="color: white" class="fas fa-key"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                    <td><%= d.getDeNombre()%></td>
                                                                    <td><%= d.getAsunto()%></td>
                                                                    <td><%= d.getEnviado()%></td>
                                                                    <td><%= d.getCodigo()%></td>
                                                                    <td><%= d.getReferenciaCodigo()%></td>
                                                                    <td>
                                                                        <a href="administrar_documento.control?accion=ver_documento_recibido&id_documento=<%= d.getId()%>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Vista previa del documento"><i class="fas fa-file-alt"></i></a>
                                                                        <a href="administrar_documento.control?accion=descargar_documento_recibido&id_documento=<%= d.getId()%>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Descargar documento"><i class="fas fa-download"></i></a>
                                                                            <% if (d.getAnexos().size() > 0) {%>
                                                                        <a onclick="abrir_modal_anexos(<%= d.getId()%>)" type="button" data-toggle="modal" class="btn btn-primary btn-sm active"><i class="fa fa-folder-open" data-toggle="tooltip" data-original-title="Ver anexos"></i></a>
                                                                            <%}%>
                                                                        <a href="ges_doc_nuevo_documento.jsp?referencia=<%= d.getId()%>" target="_self" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Responder"><i class="fas fa-paper-plane"></i></a>
                                                                            <%if (d.getDestinatario().getTipo() == 1 || d.getDestinatario().getTipo() == 2 ||  d.getDestinatario().getTipo() == 3) {%>
                                                                        <a onclick="abrir_modal_reasignar(<%= d.getId()%>)" type="button" data-toggle="modal" class="btn btn-primary btn-sm active"><i class="fa fa-user-tag" data-toggle="tooltip" data-original-title="Reasignar"></i></a>
                                                                        <a onclick="abrir_modal_informar(<%= d.getId()%>)" type="button" data-toggle="modal" class="btn btn-primary btn-sm active"><i class="fa fa-bell" data-toggle="tooltip" data-original-title="Informar"></i></a>
                                                                            <%}%>
                                                                        <a onclick="archivar(<%= d.getId()%>)" type="button" class="btn btn-primary btn-sm active"><i class="fa fa-folder" data-toggle="tooltip" data-original-title="Archivar"></i></a>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade show" id="archivados" role="tabpanel" aria-labelledby="archivados-tab">
                                                    <div class="table-responsive">
                                                        <table class="table table-striped" id="table-xx">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%"></th>
                                                                    <th style="width: 20%">Remitente</th>
                                                                    <th style="width: 25%">Asunto</th>
                                                                    <th style="width: 10%">Fecha</th>
                                                                    <th style="width: 15%">N° documento</th>
                                                                    <th style="width: 15%">N° referencia</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Documento d : listadoArchivados) {%>
                                                                <tr>
                                                                    <td>
                                                                        <%if (d.isFirmado()) {%>
                                                                        <a class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Firmado electrónicamente"><i style="color: white" class="fas fa-key"></i></a>
                                                                            <%}%>
                                                                    </td>
                                                                    <td><%= d.getDeNombre()%></td>
                                                                    <td><%= d.getAsunto()%></td>
                                                                    <td><%= d.getEnviado()%></td>
                                                                    <td><%= d.getCodigo()%></td>
                                                                    <td><%= d.getReferenciaCodigo()%></td>
                                                                    <td>
                                                                        <a href="administrar_documento.control?accion=ver_documento_recibido&id_documento=<%= d.getId()%>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Vista previa del documento"><i class="fas fa-file-alt"></i></a>
                                                                        <a href="administrar_documento.control?accion=descargar_documento_recibido&id_documento=<%= d.getId()%>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Descargar documento"><i class="fas fa-download"></i></a>
                                                                            <% if (d.getAnexos().size() > 0) {%>
                                                                        <a onclick="abrir_modal_anexos(<%= d.getId()%>)" type="button" data-toggle="modal" class="btn btn-primary btn-sm active"><i class="fa fa-folder-open" data-toggle="tooltip" data-original-title="Ver anexos"></i></a>
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
                <div class="modal fade" role="dialog" aria-hidden="true" id="modal_anexos">
                    <div class="modal-dialog modal-lg" role="dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title text-center">
                                    <span class="fw-extrabold">
                                        Anexos
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div id="body_modal_anexos" class="modal-body">
                            </div>
                        </div>
                    </div>                
                </div>
                <div class="modal fade" id="modal_reasignar" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Reasignar a funcionario
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div id="body_modal_reasignar" class="modal-body">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="modal_informar" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Informar a funcionario
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div id="body_modal_informar" class="modal-body">
                            </div>
                        </div>
                    </div>
                </div>            
                <footer class="main-footer">
                    <div class="footer-center">
                        Copyright &copy; <%= LocalDate.now().getYear()%> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
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

        <script>
            const input_txtini = document.getElementById("txtini"),
                input_txtfin = document.getElementById("txtfin"),
                OPC_TABLA = {
                "ordering": true,
                "order": [3, 'desc'],
                "columnDefs": [
                    {"sortable": true, "targets": [1, 3, 4]}
                ],
                "pageLength": 10,
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
                    , {
                        extend: 'print',
                        text: 'Imprimir <i class="fas fa-print"></i>'
                    }
                ],
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
            }
            
            $("#table-x").dataTable(OPC_TABLA);
            
            $("#table-xx").dataTable(OPC_TABLA);

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
            })

            function abrir_modal_anexos(id_documento) {
                Swal.fire({
                    title: 'Cargando anexos',
                    text: 'Por favor espere',
                    timerProgressBar: true,
                    showConfirmButton: false,
                    allowOutsideClick: () => !Swal.isLoading(),
                    allowEscapeKey: () => !Swal.isLoading(),
                    didOpen: () => {
                        Swal.showLoading();
                    }
                })
                $("#body_modal_anexos").load("ges_doc_listado_anexos.jsp?id_documento=" + id_documento, function (response, status, xhr) {
                    if (status === "success") {
                        Swal.close()
                        $('#modal_anexos').modal('show')
                    } else {
                        console.log("Error: " + xhr.status + ": " + xhr.statusText)
                    }
                })
            }

            function abrir_modal_reasignar(id_documento) {
                Swal.fire({
                    title: 'Cargando destinatarios',
                    text: 'Por favor espere',
                    timerProgressBar: true,
                    showConfirmButton: false,
                    allowOutsideClick: () => !Swal.isLoading(),
                    allowEscapeKey: () => !Swal.isLoading(),
                    didOpen: () => {
                        Swal.showLoading();
                    }
                })
                $("#body_modal_reasignar").load("ges_doc_listado_reasignar_recibido.jsp?id_documento=" + id_documento, function (response, status, xhr) {
                    if (status === "success") {
                        Swal.close()
                        $('#modal_reasignar').modal('show')
                    } else {
                        console.log("Error: " + xhr.status + ": " + xhr.statusText)
                    }
                })
            }

            function abrir_modal_informar(id_documento) {
                Swal.fire({
                    title: 'Cargando destinatarios',
                    text: 'Por favor espere',
                    timerProgressBar: true,
                    showConfirmButton: false,
                    allowOutsideClick: () => !Swal.isLoading(),
                    allowEscapeKey: () => !Swal.isLoading(),
                    didOpen: () => {
                        Swal.showLoading();
                    }
                })
                $("#body_modal_informar").load("ges_doc_listado_informar_recibido.jsp?id_documento=" + id_documento, function (response, status, xhr) {
                    if (status === "success") {
                        Swal.close()
                        $('#modal_informar').modal('show')
                    } else {
                        console.log("Error: " + xhr.status + ": " + xhr.statusText)
                    }
                })
            }
            
            function archivar(id_documento){
                Swal.fire({
                    title: '¿Deseas archivar este documento?',
                    icon: 'warning',
                    buttonsStyling: false,
                    showCancelButton: true,
                    confirmButtonText: 'Sí, archivar',
                    cancelButtonText: 'No, cancelar',
                    customClass: {
                        confirmButton: 'btn btn-success',
                        cancelButton: 'btn btn-danger'
                    }
                }).then((action) => {
                    if (action.isConfirmed) {
                        const formData = new FormData()
                        formData.append('id_documento', id_documento)
                        $.ajax({
                            url: 'administrar_documento.control?accion=archivar_recibido',
                            type: 'POST',
                            data: formData,
                            contentType: false,
                            cache: false,
                            processData: false,
                            beforeSend: function () {
                                Swal.fire({
                                    title: 'Archivando documento',
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
                                const response_arr = response.split(';'),
                                        code = parseInt(response_arr[0]),
                                        mensaje = response_arr[1],
                                        nueva_pagina = response_arr[2]
                                if (code === 1) {
                                    Swal.fire({
                                        title: mensaje,
                                        icon: "success",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    }).then(function () {
                                        location.href = nueva_pagina
                                    })
                                } else if(isNaN(code)) {
                                    Swal.fire({
                                        title: mensaje,
                                        icon: "warning",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    })
                                } else {
                                    Swal.fire({
                                        title: mensaje,
                                        icon: "warning",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
                                    }).then(function () {
                                        location.href = nueva_pagina
                                    })
                                }
                            },
                            error: function () {
                                Swal.fire({
                                    title: 'Error crítico',
                                    text: 'Ocurrió un error al archivar',
                                    icon: "error",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                })
                            }
                        })
                    }
                })                
            }
            
            function get_fecha_inicio(){
                return moment().subtract(3, 'months').format('YYYY-MM-DD')
            }
            
            $(document).ready(function () {                
                if(<%= fecha_inicio == null %>){
                    input_txtini.value = get_fecha_inicio()
                }
            })
        </script>        
    </body>
</html>
