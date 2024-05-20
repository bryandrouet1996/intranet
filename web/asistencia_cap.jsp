<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.InscritoCap"%>
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
    int id = 0, id_hor = 0, id_cap = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    Horario hor = new Horario();
    Capacitacion cap = new Capacitacion();
    ArrayList<InscritoCap> inscritos = new ArrayList<>();
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());        
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (!(enlace.verificarUsuarioCumpleRol(id, "administrador") || enlace.verificarUsuarioCumpleRol(id, "capacitacion"))) {
            throw new Exception("Rol no habilitado");
        }
        if(request.getParameter("id_hor") != null ){
            id_hor = Integer.parseInt(request.getParameter("id_hor"));
            hor = enlace.getHorario(id_hor, id);
            if(hor.getId() == 0){
                throw new Exception("Horario inexistente o sin acceso");
            }
            cap = enlace.getCapacitacion(hor.getIdCap());
            inscritos = enlace.getInscritos(id_hor);
        }else{
            id_cap = Integer.parseInt(request.getParameter("id_cap"));
            cap = enlace.getCapacitacion(id_cap);
            inscritos = enlace.getAsistentes(id_cap);
        }        
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
        <title>Intranet Alcaldía - Listado de asistencia</title>
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
                            <h1>Listado de asistencia - <%= cap.getTema() + (id_hor == 0 ? (" (Nivel de satisfacción: " + cap.getSatisfaccionDesc() + ")") : "") %> <%= id_hor != 0 ? " (Horario: " + hor.getHoraIni() + " - " + hor.getHoraFin() + " " + hor.getFecha() + ")" : "" %></h1>
                            <div class="section-header-breadcrumb">
                                <div class="flex-column activities">
                                    <a target="_self" href="<%= id_hor == 0 ? "capacitacion_admin" : "capacitacion" %>.jsp" class="btn btn-primary" type="button" class="btn btn-primary"> <i class="fas fa-list"></i> Volver a capacitaciones</a>
                                </div>
                            </div>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4><%=id_hor != 0 ? "Inscritos" : "Asistentes" %> <%if (inscritos.size() != 0) {%><span class="badge badge-primary"><%= inscritos.size()%></span><%}%></h4>
                                        </div>
                                        <div class="card-body">
                                            <form id="formAsis" action="administrar_cap.control?accion=asistencia" method="post" enctype="multipart/form-data" class="needs-validation">
                                                <div class="form-group col-md-2" hidden>
                                                    <input type="text" class="form-control" id="hor" name="hor" value="<%= hor.getId()%>">
                                                </div>
                                                <div class="table-responsive">
                                                    <%if(id_hor != 0){%>
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 35%">Funcionario</th>
                                                                    <th style="width: 30%">Unidad</th>                                                      
                                                                    <th style="width: 30%">Asistencia</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (InscritoCap i : inscritos) {%>
                                                                <tr>
                                                                    <td><%= i.getId()%></td>
                                                                    <td><%= i.getNombres()%></td>
                                                                    <td><%= i.getUnidad()%></td>
                                                                    <td>
                                                                        <select name="asi<%= i.getId()%>" id="asi<%= i.getId()%>" required="">
                                                                            <option value="1" <%= i.getAsistencia() != 0 ? "selected" : "" %>>Sí</option>
                                                                            <option value="0" <%= i.getAsistencia() == 0 ? "selected" : "" %>>No</option>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    <%}else{%>
                                                        <table class="table table-striped" id="table-x">
                                                            <thead>                                 
                                                                <tr>
                                                                    <th style="width: 5%">Código</th>
                                                                    <th style="width: 30%">Funcionario</th>
                                                                    <th style="width: 35%">Unidad</th>                                                      
                                                                    <th style="width: 20%">Nivel de satisfacción</th>
                                                                    <th style="width: 10%">Acciones</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (InscritoCap i : inscritos) {%>
                                                                <tr>
                                                                    <td><%= i.getId()%></td>
                                                                    <td><%= i.getNombres()%></td>
                                                                    <td><%= i.getUnidad()%></td>
                                                                    <td><%= i.getSatisfaccionDesc()%></td>
                                                                    <td>
                                                                        <%if(i.getSatisfaccion()== 0){%>
                                                                            <a onclick="alertarCert()" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar certificado"></i></a>
                                                                        <%}else{%>
                                                                            <a target="_blank" href="administrar_cap.control?accion=certificado&asi=<%=i.getAsistencia()%>" class="btn btn-primary btn-sm active"><i class="fa fa-print" data-toggle="tooltip" data-original-title="Generar certificado"></i></a>
                                                                        <%}%>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                            </tbody>
                                                        </table>
                                                    <%}%>
                                                </div>
                                                <%if(id_hor != 0 && inscritos.size() > 0){%>
                                                    <button type="submit" id="btnAsis" class="btn btn-primary">Guardar</button>
                                                <%}%>
                                            </form>                                            
                                        </div>
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
            $(document).ready(function () {
                $('#formAsis').submit(function (event) {
                    var btn = document.getElementById("btnAsis");
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
                                title: 'Registrando asistencia',
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
                                    title: 'Asistencia registrada',
                                    icon: 'success',
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "asistencia_cap.jsp?id_hor=<%= id_hor%>";
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
                                    location.href = "asistencia_cap.jsp?id_hor=<%= id_hor%>";
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
                                location.href = "asistencia_cap.jsp?id_hor=<%= id_hor%>";
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
                },
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
                ]
              });
              
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
