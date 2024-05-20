<%@page import="sinarp.Ciudadano"%>
<%@page import="sinarp.Cedula"%>
<%@page import="sinarp.SINARP"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.ConexionVentanilla"%>
<%@page import="java.net.InetAddress"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="java.sql.Date"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
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
    ConexionVentanilla ventanilla = new ConexionVentanilla();
    SINARP sinarp = new SINARP();
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    Ciudadano rcd = null, msp = null;
    ArrayList<Cedula> rc = null;
    String ced = "", ape1 = "", ape2 = "", nom1 = "", nom2 = "";
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if (!(enlace.verificarUsuarioCumpleRol(id, "admin_cioce") || enlace.verificarUsuarioCumpleRol(id, "cioce"))) {
            throw new Exception("Rol no habilitado");
        }
        ced = request.getParameter("txtced") != null ? request.getParameter("txtced").toString() : "";
        ape1 = request.getParameter("txtape1") != null ? request.getParameter("txtape1").toString().toLowerCase().replace('ñ', 'n') : "";
        ape2 = request.getParameter("txtape2") != null ? request.getParameter("txtape2").toString().toLowerCase().replace('ñ', 'n') : "";
        nom1 = request.getParameter("txtnom1") != null ? request.getParameter("txtnom1").toString().toLowerCase().replace('ñ', 'n') : "";
        nom2 = request.getParameter("txtnom2") != null ? request.getParameter("txtnom2").toString().toLowerCase().replace('ñ', 'n') : "";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || "".equals(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                ip = ipAddress;
            }
        }
        if (ced != "") {
            rcd = sinarp.getRCD(ced, true);
            if (rcd != null) {
                enlace.auditarDINARDAP(id, ip, ced, 4, rcd.getIdAuditoria(), rcd.getIdRegistro());
            }
            if (rcd.getFechaDefuncion() != null) {
                if (!ventanilla.existeFallecido(ced)) {
                    ventanilla.insertarFallecido(ced);
                }
            }
            msp = sinarp.getMSP(ced, true);
            if (msp != null) {
                enlace.auditarDINARDAP(id, ip, ced, 1, msp.getIdAuditoria(), msp.getIdRegistro());
            }
        } else if ((ape1 != "" && ape2 != "") || (ape1 != "" && nom1 != "")) {
            enlace.auditarDINARDAP(id, ip, ape1 + "-" + ape2 + "-" + nom1 + "-" + nom2, 2, 0, 0);
            rc = sinarp.getRC(ape1, ape2, nom1, nom2);
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
        <title>Intranet Alcaldía - SINARP</title>
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
        <link rel="stylesheet" href="assets/css/components.css"></head>

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
                    <%
                    %>
                    <section class="section">
                        <div class="section-header">
                            <h1>SINARP</h1>
                        </div>
                        <div class="section-body">        
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form action="">
                                                <div class="row">
                                                    <div class="col-2">
                                                        <select class="form-control select2" name="combopar" id="combopar" required="" onchange="cambioParametro()">
                                                            <option selected value="ced">Cédula</option>
                                                            <option value="nom">Apellidos y nombres</option>
                                                        </select>
                                                    </div>
                                                    <div id="divtxtced" class="col-2">
                                                        <input type="text" class="form-control" onchange="cambioCedula()" id="txtced" name="txtced" placeholder="Ingrese la cédula" maxlength="10"/>
                                                    </div>
                                                    <div hidden="" id="divtxtape1" class="col-2">
                                                        <input type="text" class="form-control" id="txtape1" name="txtape1" placeholder="Primer apellido"/>
                                                    </div>
                                                    <div hidden="" id="divtxtape2" class="col-2">
                                                        <input type="text" class="form-control" name="txtape2" placeholder="Segundo apellido"/>
                                                    </div>
                                                    <div hidden="" id="divtxtnom1" class="col-2">
                                                        <input type="text" class="form-control" name="txtnom1" placeholder="Primer nombre"/>
                                                    </div>
                                                    <div hidden="" id="divtxtnom2" class="col-2">
                                                        <input type="text" class="form-control" name="txtnom2" placeholder="Segundo nombre"/>
                                                    </div>
                                                    <div class="col-2">
                                                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Consultar</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <%if (rcd != null) {%>
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Registro Civil</h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="form-row">
                                                <div class="form-group col-md-6">
                                                    <label>Nombres</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Nombre funcionario" name="nombreRCD" id="nombreRCD" value="<%= rcd.getNombres()%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Cédula</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Cédula funcionario" name="cedulaRCD" id="cedulaRCD" value="<%= ced%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Fecha de nacimiento</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="naciRCD" id="naciRCD" value="<%= rcd.getFechaNacimiento()%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Fecha de expedición de cédula</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="fechaRCD" id="fechaRCD" value="<%= rcd.getFechaExpedicion()%>" required>
                                                </div> 
                                                <div class="form-group col-md-6">
                                                    <label>Estado civil</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="civilRCD" id="civilRCD" value="<%= rcd.getEstadoCivil()%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Fallecido</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="falleRCD" id="falleRCD" value="<%= rcd.getFechaDefuncion() == null ? "No" : "Sí"%>" required>
                                                </div>
                                                <%if (rcd.getFechaDefuncion() != null) {%>
                                                <div class="form-group col-md-6">
                                                    <label>Fecha de defunción</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="defunRCD" id="defunRCD" value="<%= rcd.getFechaDefuncion()%>" required>
                                                </div>
                                                <%}%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%if (msp != null) {%>
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Ministerio de Salud Pública</h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="form-row">
                                                <div class="form-group col-md-6">
                                                    <label>¿Tiene discapacidad?</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Nombre funcionario" name="tieneMSP" id="tieneMSP" value="<%= msp.getCodigoCONADIS() == null ? "No" : "Sí"%>" required>
                                                </div>
                                                <%if (msp.getCodigoCONADIS() != null) {%>
                                                <div class="form-group col-md-6">
                                                    <label>Código CONADIS</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Cédula funcionario" name="codigoMSP" id="codigoMSP" value="<%= msp.getCodigoCONADIS()%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Grado discapacidad</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Cédula funcionario" name="gradMSP" id="gradMSP" value="<%= msp.getGrado()%>" required>
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label>Porcentaje discapacidad</label>
                                                    <input disabled="" type="text" class="form-control" placeholder="Correo funcionario" name="porcMSP" id="porcMSP" value="<%= msp.getPorcentaje()%>" required>
                                                </div>
                                                <%}%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%}%>
                                <%} else if (rc != null) {%>
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Búsqueda por nombres <%if (rc.size() != 0) {%><span class="badge badge-primary"><%= rc.size()%></span><%}%></h4>
                                        </div>
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-x">
                                                    <thead>                                 
                                                        <tr>
                                                            <th style="width: 50">Cédula</th>
                                                            <th style="width: 50">Fecha de nacimiento</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            for (int i = 0; i < rc.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td><a href="dinardap.jsp?txtced=<%=rc.get(i).getDato()%>"><%=rc.get(i).getDato()%></a></td>
                                                            <td><%=rc.get(i).getFecha()%></td>
                                                        </tr>
                                                        <%}%>                                                                     
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%}%>
                            </div>
                        </div>
                    </section>
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
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-sweetalert.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/formulario_soporte.js" type="text/javascript"></script>
        <script src="fun_js/funciones_soporte.js" type="text/javascript"></script>
        <script type="text/javascript">
                                                            $("#table-x").dataTable({
                                                                "ordering": false,
                                                                "order": [0, 'desc'],
                                                                "columnDefs": [
                                                                    {"sortable": false, "targets": [0, 1]}
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

                                                            function validarCedula(campo) {
                                                                var ced = document.getElementById(campo).value;
                                                                var total = 0;
                                                                var longitud = ced.length;
                                                                var longcheck = longitud - 1;
                                                                if (ced !== "" && longitud === 10) {
                                                                    var i;
                                                                    for (i = 0; i < longcheck; i++) {
                                                                        if (i % 2 === 0) {
                                                                            var aux = ced.charAt(i) * 2;
                                                                            if (aux > 9)
                                                                                aux -= 9;
                                                                            total += aux;
                                                                        } else {
                                                                            total += parseInt(ced.charAt(i));
                                                                        }
                                                                    }
                                                                    total = total % 10 ? 10 - total % 10 : 0;
                                                                    if (!(parseInt(ced.charAt(longitud - 1)) === total)) {
                                                                        swal({
                                                                            title: "Cédula",
                                                                            text: "Cédula inválida",
                                                                            icon: "error",
                                                                            buttons: {
                                                                                confirm: {
                                                                                    text: 'Aceptar',
                                                                                    className: 'btn btn-danger'
                                                                                }
                                                                            }
                                                                        });
                                                                        document.getElementById(campo).value = '';
                                                                        return false;
                                                                    } else {
                                                                        return true;
                                                                    }
                                                                } else {
                                                                    swal({
                                                                        title: "Cédula",
                                                                        text: "Cédula inválida",
                                                                        icon: "error",
                                                                        buttons: {
                                                                            confirm: {
                                                                                text: 'Aceptar',
                                                                                className: 'btn btn-danger'
                                                                            }
                                                                        }
                                                                    });
                                                                    document.getElementById(campo).value = '';
                                                                    return false;
                                                                }
                                                            }

                                                            function cambioCedula() {
                                                                if (!validarCedula('txtced')) {
                                                                    document.getElementById("txtced").value = '';
                                                                }
                                                            }

                                                            function cambioParametro() {
                                                                var parametro = document.getElementById("combopar").value;
                                                                var ced = document.getElementById("divtxtced");
                                                                var ape1 = document.getElementById("divtxtape1");
                                                                var ape2 = document.getElementById("divtxtape2");
                                                                var nom1 = document.getElementById("divtxtnom1");
                                                                var nom2 = document.getElementById("divtxtnom2");
                                                                if (parametro == 'ced') {
                                                                    ced.hidden = false;
                                                                    ape1.hidden = true;
                                                                    ape2.hidden = true;
                                                                    nom1.hidden = true;
                                                                    nom2.hidden = true;
                                                                } else {
                                                                    ced.hidden = true;
                                                                    ape1.hidden = false;
                                                                    ape2.hidden = false;
                                                                    nom1.hidden = false;
                                                                    nom2.hidden = false;
                                                                }
                                                            }
        </script>
    </body>
</html>
