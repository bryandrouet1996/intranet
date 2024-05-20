<%-- 
    Document   : registro_memorandum.jsp
    Created on : 27/04/2024, 15:16:57
    Author     : Bryan Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.tipo_tramite"%>
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
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    ArrayList<usuario> listadoActivos = null;
    int id_memorandum = 0;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        if (request.getParameter("idmemorandum") != null) {
            id_memorandum = Integer.parseInt(request.getParameter("idmemorandum"));
        }
        informacion = enlace.buscar_usuarioID(id);
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
        if(!enlace.isTipoUsuarioID(id, 82)){
            listadoActivos = enlace.listadoUsuariosActivos();
        }else{
            listadoActivos = enlace.listadosDirectoresDireccionesUnidades();
        }
        
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
        <title>Intranet Alcaldía - Registro de memorandum</title>
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
                                <%if (mod.isBlank()) {%>
                            <li><a class="nav-link" target="_blank" href="sesion.control?accion=<%= mod.getRuta_enlace()%>&iu=<%= id%>"><i class="<%= mod.getIcono()%>"></i> <span><%= mod.getDescripcion()%></span></a></li>
                                <%} else if (listaComponente.size() > 1) {%>
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
                            <h6><a href="listado_memorandums.jsp">Listado memorandums</a> > Ingresar nuevo memorandum</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <%if (id_memorandum == 0) {%>
                                            <form class="needs-validation" id="formRegistroMemorandum" action="memorandum.ct?opcion=registro" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-grouponvocatoria col-md-2" hidden>
                                                            <label>Id memorandum</label>
                                                            <input type="text" class="form-control" name="txtmemorandum" id="txtmemorandum" value="0" required>
                                                        </div>
                                                        <div class="form-group col-md-2" hidden>
                                                            <label>Id envia</label>
                                                            <input type="text" class="form-control" name="txtidusuario" id="txtidusuario" value="<%= informacion.getId_usuario()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Departamento Solicitante</label>
                                                            <input type="text" class="form-control" name="txtactividad" id="txtactividad" placeholder="Ingrese actividad" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Director Solicitante </label>
                                                            <input type="text" class="form-control" name="txtparticipante" id="txtparticipante" placeholder="Ingrese participantes" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Asignación</label>
                                                            <select class="form-control select2" name="comboasignacion" id="comboasignacion" required >
                                                                <option value="">Seleccione funcionario</option>
                                                                <%for (usuario func : listadoActivos) {%>
                                                                <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha</label>
                                                            <input type="text" class="form-control datepicker" name="txtfecha" id="txtfecha" required>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Documento</label>
                                                            <input type="text" class="form-control" name="txtdocumento" id="txtdocumento" placeholder="Ingrese documento" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Adjunto</label>
                                                            <input type="file" class="form-control" name="txtadjunto" id="txtadjunto" required onchange="validateSize()">
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Descripción</label>
                                                            <textarea class="form-control" name="txtdescripcion" id="txtdescripcion" placeholder="Ingrese descripción"></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Observación</label>
                                                            <textarea class="form-control" name="txtobservacion" id="txtobservacion"  placeholder="Ingrese observación"></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Resultado</label>
                                                            <textarea class="form-control" name="txtresultados" id="txtresultados"  placeholder="Ingrese resultados"></textarea> 
                                                        </div>

                                                    </div>
                                                </div>
                                                <div class="card-footer text-right">
                                                    <button class="btn btn-primary btn-block btn-lg" type="submit"><i class="fas fa-save"></i> Registrar</button>
                                                </div>
                                            </form>
                                            <%} else {%>
                                            <form class="needs-validation" id="formconvocatoria" action="administrar_tramite.control?accion=actualizar_tramite" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-grouponvocatoria col-md-2" hidden>
                                                            <label>Id tramite</label>
                                                            <input type="text" class="form-control" name="txtidtra" id="txtidtra" value="<%= id_memorandum%>" required>
                                                        </div>
                                                        <div class="form-group col-md-2" hidden>
                                                            <label>Id envia</label>
                                                            <input type="text" class="form-control" name="txtiusu" id="txtiusu" value="<%= informacion.getId_usuario()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <label>*Fecha de elaboración</label>
                                                            <input type="text" class="form-control datepicker" name="txtfechaelaboracion" id="txtfechaelaboracion" required>
                                                        </div>
                                                        <div class="form-group col-md-2">
                                                            <label>*Hora de elaboración</label>
                                                            <input type="time" class="form-control" name="txthoraelaboracion" id="txthoraelaboracion" value="<%= enlace.hora_actual()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-4">
                                                            <label>*Número de memo / oficio</label>
                                                            <input type="text" class="form-control" name="txtnumeromemo" id="txtnumeromemo" placeholder="Ingrese número de memo / oficio" required>
                                                        </div>
                                                        <div class="form-group col-md-3">
                                                            <label>*Tipo de trámite</label>
                                                            <select class="form-control" id="combotipo" name="combotipo" required="">
                                                                <option value="">Seleccione</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>*Asunto</label>
                                                            <textarea class="form-control" rows="5" name="areaasunto" id="areaasunto" required placeholder="Detalle el asunto del trámite a registrar para que el destinatario tenga una vista previa de la documentación."></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Destinatario</label>
                                                            <select class="form-control select2" name="combodestinatario" id="combodestinatario" required >
                                                                <option value="">Seleccione funcionario</option>
                                                                <%for (usuario func : listadoActivos) {%>
                                                                <option value="<%= func.getId_usuario()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <label>*Documento</label>
                                                            <input type="file" class="form-control" name="txtadjunto" id="txtadjunto" placeholder="Ingrese número de memo / oficio" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="card-footer text-right">
                                                    <button class="btn btn-primary btn-block btn-lg" type="submit"><i class="fas fa-save"></i> Registrar</button>
                                                </div>
                                            </form>
                                            <%}%>
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
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>
        <script src="assets/js/page/modules-sweetalert.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/modules-toastr.js"></script>
        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>

        <script type="text/javascript">
            function validateSize() {
                var input = document.getElementById("txtadjunto");
                const fileSize = input.files[0].size / 1024 / 1024;
                if (fileSize > 2) {
                    swal("Mensaje",
                            "El archivo excede el tamaño máximo de 2 MB",
                            "warning"
                            );
                    $('#txtadjunto').val('');
                }
            }
        </script>
        <script src="fun_js/MemorandumJS.js" type="text/javascript"></script>
    </body>
</html>
