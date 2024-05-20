<%@page import="control_sectorial.Barrio"%>
<%@page import="control_sectorial.Parroquia"%>
<%@page import="control_sectorial.Sector"%>
<%@page import="control_sectorial.Ficha"%>
<%@page import="control_sectorial.PG"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.tipo_tramite"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.foto_usuario"%>
<%@page import="modelo.actividad"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    conexion enlace = new conexion();
    PG postgres = new PG();
    int id = 0;
    usuario informacion = null;
    foto_usuario foto = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<Parroquia> listadoParroquias = null;
    ArrayList<Sector> listadoSectores = null;
    ArrayList<Barrio> listadoBarrios = null;
    String sectores = "", barrios = "";
    int id_barrio = 0;
    Ficha ficha_actual = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        listadoParroquias = postgres.getParroquias();
        listadoSectores = postgres.getSectores();
        for (Sector s : listadoSectores) {
            sectores += (s.stringify() + ",");
        }
        sectores = sectores.substring(0, sectores.length() - 1);
        listadoBarrios = postgres.getBarrios();
        for (Barrio b : listadoBarrios) {
            barrios += (b.stringify() + ",");
        }
        barrios = barrios.substring(0, barrios.length() - 1);
        if (request.getParameter("id_barrio") != null) {
            id_barrio = Integer.parseInt(request.getParameter("id_barrio"));
            ficha_actual = postgres.getFicha(id_barrio);
        }
    } catch (Exception e) {
        System.out.println("err | "+e);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Registro de documento</title>
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
    </head>

    <body id="todo">
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
                            <h6><a href="con_sec_fichas_registradas.jsp">Fichas registradas</a> > Ingresar ficha</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formNueva" action="administrar_ficha.control?accion=registrar" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-4">
                                                            <label>Parroquia *</label>
                                                            <select class="form-control" id="id_parroquia" <%= id_barrio != 0 ? "disabled" : ""%> onchange="cambioParroquia()" required>
                                                                <option value="" <%= id_barrio == 0 ? "selected" : ""%> disabled>Seleccione</option>
                                                                <%for (Parroquia p : listadoParroquias) {%>
                                                                <option value="<%= p.getId()%>"><%= p.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>Sector *</label>
                                                            <select class="form-control" id="id_sector" onchange="cambioSector()" disabled required>
                                                                <option value="">Seleccione</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>Barrio *</label>
                                                            <select class="form-control" id="id_barrio" onchange="cambioBarrio()" name="id_barrio" disabled required>
                                                                <option value="">Seleccione</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>Visita del alcalde *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="va_1" name="visita_alcalde" value="1" required>
                                                            <label for="va_1">Sí</label><br>
                                                            <input type="radio" id="va_0" name="visita_alcalde" value="0">
                                                            <label for="va_0">No</label><br>
                                                            <%} else {%>
                                                            <input type="radio" id="va_1" name="visita_alcalde" value="1" required <%= ficha_actual.isVisita_alcalde()? "checked" : ""%>>
                                                            <label for="va_1">Sí</label><br>
                                                            <input type="radio" id="va_0" name="visita_alcalde" value="0" <%= !ficha_actual.isVisita_alcalde() ? "checked" : ""%>>
                                                            <label for="va_0">No</label><br>
                                                            <%}%>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>Brigada médica *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="bm_1" name="brigada_medica" value="1" required>
                                                            <label for="bm_1">Sí</label><br>
                                                            <input type="radio" id="bm_0" name="brigada_medica" value="0">
                                                            <label for="bm_0">No</label><br>
                                                            <%} else {%>
                                                            <input type="radio" id="bm_1" name="brigada_medica" value="1" required <%= ficha_actual.isBrigada_medica()? "checked" : ""%>>
                                                            <label for="bm_1">Sí</label><br>
                                                            <input type="radio" id="bm_0" name="brigada_medica" value="0" <%= !ficha_actual.isBrigada_medica() ? "checked" : ""%>>
                                                            <label for="bm_0">No</label><br>
                                                            <%}%>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>Brigada veterinaria *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="bv_1" name="brigada_veterinaria" value="1" required>
                                                            <label for="bv_1">Sí</label><br>
                                                            <input type="radio" id="bv_0" name="brigada_veterinaria" value="0">
                                                            <label for="bv_0">No</label><br>
                                                            <%} else {%>                                                            
                                                            <input type="radio" id="bv_1" name="brigada_veterinaria" value="1" required <%= ficha_actual.isBrigada_veterinaria()? "checked" : ""%>>
                                                            <label for="bv_1">Sí</label><br>
                                                            <input type="radio" id="bv_0" name="brigada_veterinaria" value="0" <%= !ficha_actual.isBrigada_veterinaria() ? "checked" : ""%>>
                                                            <label for="bv_0">No</label><br>
                                                            <%}%>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>Olla solidaria *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="os_1" name="olla_solidaria" value="1" required>
                                                            <label for="os_1">Sí</label><br>
                                                            <input type="radio" id="os_0" name="olla_solidaria" value="0">
                                                            <label for="os_0">No</label><br>
                                                            <%} else {%>
                                                            <input type="radio" id="os_1" name="olla_solidaria" value="1" required <%= ficha_actual.isOlla_solidaria()? "checked" : ""%>>
                                                            <label for="os_1">Sí</label><br>
                                                            <input type="radio" id="os_0" name="olla_solidaria" value="0" <%= !ficha_actual.isOlla_solidaria() ? "checked" : ""%>>
                                                            <label for="os_0">No</label><br>
                                                            <%}%>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>Minga ciudadana *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="mc_1" name="minga_ciudadana" value="1" required>
                                                            <label for="mc_1">Sí</label><br>
                                                            <input type="radio" id="mc_0" name="minga_ciudadana" value="0">
                                                            <label for="mc_0">No</label><br>
                                                            <%} else {%>
                                                            <input type="radio" id="mc_1" name="minga_ciudadana" value="1" required <%= ficha_actual.isMinga_ciudadana()? "checked" : ""%>>
                                                            <label for="mc_1">Sí</label><br>
                                                            <input type="radio" id="mc_0" name="minga_ciudadana" value="0" <%= !ficha_actual.isMinga_ciudadana() ? "checked" : ""%>>
                                                            <label for="mc_0">No</label><br>
                                                            <%}%>                                                            
                                                        </div>                                                        
                                                        <div class="form-group col-md-3">
                                                            <p>Capacitación *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="ca_1" name="capacitacion" value="1" required>
                                                            <label for="ca_1">Sí</label><br>
                                                            <input type="radio" id="ca_0" name="capacitacion" value="0">
                                                            <label for="ca_0">No</label><br>
                                                            <%} else {%>
                                                            <input type="radio" id="ca_1" name="capacitacion" value="1" required <%= ficha_actual.isCapacitacion()? "checked" : ""%>>
                                                            <label for="ca_1">Sí</label><br>
                                                            <input type="radio" id="ca_0" name="capacitacion" value="0" <%= !ficha_actual.isCapacitacion() ? "checked" : ""%>>
                                                            <label for="ca_0">No</label><br>
                                                            <%}%>                                                            
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <p>WiFi *</p>
                                                            <%if (ficha_actual == null) {%>
                                                            <input type="radio" id="wi_1" name="wifi" value="1" required>
                                                            <label for="wi_1">Sí</label><br>
                                                            <input type="radio" id="wi_0" name="wifi" value="0">
                                                            <label for="wi_0">No</label><br>
                                                            <%} else {%>                                                            
                                                            <input type="radio" id="wi_1" name="wifi" value="1" required <%= ficha_actual.isWifi()? "checked" : ""%>>
                                                            <label for="wi_1">Sí</label><br>
                                                            <input type="radio" id="wi_0" name="wifi" value="0" <%= !ficha_actual.isWifi() ? "checked" : ""%>>
                                                            <label for="wi_0">No</label><br>
                                                            <%}%>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card-footer text-right">
                                                    <button id="btnRegistrar" class="btn btn-primary btn-block btn-lg" type="submit"><i class="fas fa-save"></i> Registrar</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
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
        <script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-toastr.js"></script>

        <script type="text/javascript">
            const sectores = [<%= sectores%>],
                    barrios = [<%= barrios%>]
            const comboParroquia = document.getElementById('id_parroquia'),
                    comboSectores = document.getElementById('id_sector'),
                    comboBarrios = document.getElementById('id_barrio')
            
            const va_1 = document.getElementById('va_1'),
                    va_0 = document.getElementById('va_0'),
                    bm_1 = document.getElementById('bm_1'),
                    bm_0 = document.getElementById('bm_0'),
                    bv_1 = document.getElementById('bv_1'),
                    bv_0 = document.getElementById('bv_0'),
                    os_1 = document.getElementById('os_1'),
                    os_0 = document.getElementById('os_0'),
                    mc_1 = document.getElementById('mc_1'),
                    mc_0 = document.getElementById('mc_0'),
                    ca_1 = document.getElementById('ca_1'),
                    ca_0 = document.getElementById('ca_0'),
                    wi_1 = document.getElementById('wi_1'),
                    wi_0 = document.getElementById('wi_0')
                        
            function limpiarChecks(){
                va_1.checked = false
                va_0.checked = false
                bm_1.checked = false
                bm_0.checked = false
                bv_1.checked = false
                bv_0.checked = false
                os_1.checked = false
                os_0.checked = false
                mc_1.checked = false
                mc_0.checked = false
                ca_1.checked = false
                ca_0.checked = false
                wi_1.checked = false
                wi_0.checked = false
            }
            
            function cleanCombo(combo){
                combo.length = 0
                const op = new Option('Seleccione', null)
                op.disabled = true
                op.selected = true
                combo.add(op)
            }
            
            function agregarOps(combo, arr, disabled = false){
                cleanCombo(combo)
                for(item of arr){
                    combo.add(new Option(item.nombre, item.id))
                }
                combo.disabled = disabled
            }

            function cambioParroquia() {
                const id_parroquia = parseInt(comboParroquia.value)                
                agregarOps(comboSectores, sectores.filter(i => i.id_parroquia == id_parroquia))
                cleanCombo(comboBarrios)
            }

            function cambioSector() {
                const id_sector = parseInt(comboSectores.value)
                comboBarrios.length = 0
                const op = new Option('Seleccione', null)
                op.disabled = true
                op.selected = true
                comboBarrios.add(op)
                agregarOps(comboBarrios, barrios.filter(i => i.id_sector == id_sector))
            }

            function cambioBarrio(){
                const id_barrio = parseInt(comboBarrios.value)
                if(isNaN(id_barrio))
                    return
                Swal.fire({
                    title: 'Verificando barrio',
                    text: 'Por favor espere',
                    timerProgressBar: true,
                    showConfirmButton: false,
                    allowOutsideClick: () => !Swal.isLoading(),
                    allowEscapeKey: () => !Swal.isLoading(),
                    didOpen: () => {
                        Swal.showLoading();
                    }
                })
                $.post('administrar_ficha.control?accion=buscar_ficha', {
                    id_barrio: id_barrio
                }, function (response) {
                    const ficha = JSON.parse(response)
                    if (ficha.id) {
                        Swal.fire({
                            title: "Ficha encontrada",
                            text: "Proceda con la actualización de la ficha del barrio",
                            icon: "success",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        })                        
                        if(ficha.visita_alcalde){
                            va_1.checked = true
                            va_0.checked = false
                        } else {
                            va_1.checked = false
                            va_0.checked = true
                        }
                        if(ficha.brigada_medica){
                            bm_1.checked = true
                            bm_0.checked = false
                        } else {
                            bm_1.checked = false
                            bm_0.checked = true
                        }
                        if(ficha.brigada_veterinaria){
                            bv_1.checked = true
                            bv_0.checked = false
                        } else {
                            bv_1.checked = false
                            bv_0.checked = true
                        }
                        if(ficha.olla_solidaria){
                            os_1.checked = true
                            os_0.checked = false
                        } else {
                            os_1.checked = false
                            os_0.checked = true
                        }
                        if(ficha.minga_ciudadana){
                            mc_1.checked = true
                            mc_0.checked = false
                        } else {
                            mc_1.checked = false
                            mc_0.checked = true
                        }
                        if(ficha.capacitacion){
                            ca_1.checked = true
                            ca_0.checked = false
                        } else {
                            ca_1.checked = false
                            ca_0.checked = true
                        }
                        if(ficha.wifi){
                            wi_1.checked = true
                            wi_0.checked = false
                        } else {
                            wi_1.checked = false
                            wi_0.checked = true
                        }
                    } else {
                        Swal.fire({
                            title: "Barrio sin ficha",
                            text: "Proceda con el registro de la ficha del barrio",
                            icon: "success",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        })
                        limpiarChecks()
                    }
                }).fail(function () {
                    Swal.fire({
                        title: "Error crítico",
                        text: "No se verificó el barrio",
                        icon: "error",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    })
                })
            }

            $(document).ready(function () {
                const id_barrio = <%= id_barrio%>
                if(id_barrio !== 0){
                    const barrio = barrios.find(b=>b.id==id_barrio)
                    if(barrio){
                        const id_sector = barrio.id_sector,
                                sector = sectores.find(s=>s.id==id_sector)
                        if(sector){
                            const id_parroquia = sector.id_parroquia
                            comboParroquia.value = id_parroquia
                            agregarOps(comboSectores, sectores.filter(s=>s.id_parroquia==id_parroquia), true)
                            comboSectores.value = id_sector
                            agregarOps(comboBarrios, barrios.filter(b=>b.id_sector==id_sector), true)
                            comboBarrios.value = id_barrio
                        }
                    }
                }
                
                $('#formNueva').submit(function (event) {
                    const btn = document.getElementById('btnRegistrar')
                    btn.disabled = true;
                    comboBarrios.disabled = false
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
                                title: 'Registrando ficha',
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
                            if (!isNaN(parseInt(response))) {
                                Swal.fire({
                                    title: "Ficha registrada",
                                    icon: "success",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    location.href = "con_sec_fichas_registradas.jsp";
                                })
                            } else {
                                Swal.fire({
                                    title: "Error",
                                    text: response,
                                    icon: "error",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                }).then(function () {
                                    btn.disabled = false
                                    comboBarrios.disabled = true
                                })
                            }
                        },
                        error: function () {
                            Swal.fire({
                                title: 'Error crítico',
                                text: "No se registró la ficha",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                btn.disabled = false;
                            });
                        }
                    });
                    return false;
                })
            })
        </script>
    </body>
</html>
