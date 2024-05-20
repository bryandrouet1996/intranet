<%-- 
    Document   : registro_actividad.jsp
    Created on : 27/04/2020, 15:16:57
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
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
    usuario directorDir = null;
    ArrayList<usuario> listadoFuncionariosDireccion = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        listadoFuncionariosDireccion = enlace.listadoUsuariosActivos();
        directorDir = enlace.directorDireccionCodigoFuncion(codigo_funcion);
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
        <title>Intranet Alcaldía - Registro de actividad</title>
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
                        <div class="section-header">
                            <h6><a href="sesion.control?accion=teletrabajo&iu=<%=informacion.getId_usuario()%>">Registro de actividades</a> | Ingresar nueva actividad</h6>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12 col-md-6 col-lg-12">
                                    <div class="card">
                                        <%
                                            actividad act = null;
                                            int iact = iact = Integer.parseInt(request.getParameter("idact"));
                                            if (iact != 0) {
                                                act = enlace.BuscarActividadID(iact);
                                            }
                                        %>
                                        <div class="card-body">
                                            <%if (act == null) {%>
                                            <form class="needs-validation" novalidate="">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha (aaaa/mm/dd)</label>
                                                            <input type="text" class="form-control datepicker" name="txtfechaactividad" id="txtfechaactividad" required>
                                                        </div>
                                                        <div class="form-group col-md-10">
                                                            <label>*Tarea/Actividad</label>
                                                            <input type="text" class="form-control" name="txttarea" id="txttarea" required="" placeholder="Ingrese una breve descripción de lo realizado">
                                                        </div>
                                                        <div class="form-row col-8">
                                                            <div class="form-group col-md-3">
                                                                <label>*Hora de inicio</label>
                                                                <input type="time" class="form-control" name="txthorainicio" id="txthorainicio" required value="<%= enlace.hora_actual()%>">
                                                            </div>
                                                            <div class="form-group col-md-3">
                                                                <label>*Hora de fin</label>
                                                                <input type="time" class="form-control" onchange="validarhoras()" name="txthorafin" id="txthorafin" required>
                                                            </div>
                                                            <div class="form-group col-md-6">
                                                                <label>*Persona requirente/Usuario final</label>
                                                                <select class="form-control select2" required name="txtrequiriente" id="txtrequiriente">
                                                                    <option>Seleccione</option>
                                                                    <%for (usuario func : listadoFuncionariosDireccion) {%>
                                                                    <option value="<%= func.getApellido()%> <%= func.getNombre()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                    <%}%>
                                                                </select>
                                                            </div>
                                                            <div class="form-group col-md-12">
                                                                <label>*Indicador</label>
                                                                <input type="text" class="form-control" placeholder="realizado / planificado" name="txtindicador" id="txtindicador" value="realizado / planificado" required>
                                                            </div>

                                                            <div class="form-group col-md-4">
                                                                <label>*Grado de consecución</label>
                                                                <select type="text" class="form-control form-control-sm" name="combogrado" id="combogrado" required>
                                                                    <option value="">Seleccione</option>
                                                                    <option value="0%">0%</option>
                                                                    <option value="50%">50%</option>
                                                                    <option value="100%" selected>100%</option>
                                                                </select>
                                                            </div>
                                                            <div class="form-group col-md-4">
                                                                <label>*Fecha límite (aaaa/mm/dd)</label>
                                                                <input type="text" class="form-control datepicker" name="txtfechalimite" id="txtfechalimite" required>
                                                            </div>
                                                            <div class="form-group col-md-4">
                                                                <label>*Avance</label>
                                                                <input type="number" onchange="validacionNumber()" class="form-control" min="0" max="100" placeholder="20" name="comboavance" id="comboavance" value="100" required>
                                                            </div>
                                                            <div class="form-group col-md-12">
                                                                <label>*Observación/Avance</label>
                                                                <textarea class="form-control" rows="5" name="areaobservacion" id="areaobservacion" required placeholder="Debe realizar alguna aclaración más extensa de la actividad realizada, o cualquier aspecto que considere importante resaltar en relación misma."></textarea> 
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-4 border">
                                                            <label class="form-label">*Herramientas utilizadas (Seleccione)</label>
                                                            <div class="selectgroup selectgroup-pills">
                                                                <label class="">
                                                                    <input type="checkbox" name="sw1" id="sw1" value="Programa de edición">
                                                                    <span class="">Programa de edición(audio,video,foto,etc)</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw10" id="sw10" value="Presencial">
                                                                    <span>Presencial</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw2" id="sw2" value="Ofimática">
                                                                    <span>Ofimática</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw3" id="sw3" value="Whatsapp/Teléfono">
                                                                    <span>Whatsapp/Teléfono</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw4" id="sw4" value="Correo electrónico">
                                                                    <span>Correo electrónico</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw5" id="sw5" value="Redes sociales">
                                                                    <span>Redes sociales</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw6" id="sw6" value="ERP Cabildo | SIGAME">
                                                                    <span>ERP Cabildo | SIGAME</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw9" id="sw9" value="Video conferencia">
                                                                    <span>Video Conferencia</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw7" id="sw7" value="Páginas WEB">
                                                                    <span>Páginas WEB</span>
                                                                </label>
                                                                <br>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw8" onchange="validacionCheckOtro()" id="sw8" value="Otros">
                                                                    <span>Otros</span>
                                                                    <input type="text" name="otros1" id="otros1" class="form-control" style="display: none">
                                                                </label>
                                            <%} else {%>
                                            <%
                                                String nueva = "";
                                                String[] herramientas = enlace.separarElemento(act.getHerramienta());
                                                String[] todas = enlace.separarElemento("Programa de edición,Presencial,Ofimática,Whatsapp/Teléfono,Correo electrónico,Redes sociales,ERP Cabildo | SIGAME,Video conferencia,Páginas WEB");
                                                for(int paso=0;paso<todas.length;paso++){
                                                    for(int step=0;step<herramientas.length;step++){
                                                        if(todas[paso].equals(herramientas[step])){
                                                            todas[paso]="";
                                                        }
                                                    }
                                                }
                                            %>
                                            <form class="needs-validation" novalidate="">
                                                <div class="card-body">
                                                    <div class="form-row">
                                                        <div class="form-group col-md-2">
                                                            <label>*Fecha (aaaa/mm/dd)</label>
                                                            <input type="text" class="form-control datepicker" name="txtfechaactividad" id="txtfechaactividad" value="<%= act.getFecha_actividad()%>" required>
                                                        </div>
                                                        <div class="form-group col-md-10">
                                                            <label>*Tarea/Actividad</label>
                                                            <input type="text" class="form-control" name="txttarea" id="txttarea" required="" placeholder="Ingrese una breve descripción de lo realizado" value='<%= act.getTarea()%>'>
                                                        </div>
                                                        <div class="form-row col-8">
                                                            <div class="form-group col-md-3">
                                                                <label>*Hora de inicio</label>
                                                                <input type="time" class="form-control" name="txthorainicio" id="txthorainicio" required value="<%= act.getHora_inicio()%>">
                                                            </div>
                                                            <div class="form-group col-md-3">
                                                                <label>*Hora de fin</label>
                                                                <input type="time" class="form-control" onchange="validarhoras()" name="txthorafin" id="txthorafin" required value="<%= act.getHora_fin()%>">
                                                            </div>
                                                            <div class="form-group col-md-6">
                                                                <label>*Persona requirente/Usuario final</label>
                                                                <select class="form-control select2" required name="txtrequiriente" id="txtrequiriente">
                                                                    <option value="<%=act.getRequiriente()%>"><%= act.getRequiriente()%></option>
                                                                    <%for (usuario func : listadoFuncionariosDireccion) {%>
                                                                    <option value="<%= func.getApellido()%> <%= func.getNombre()%>"><%= func.getApellido()%> <%= func.getNombre()%></option>
                                                                    <%}%>
                                                                </select>
                                                            </div>
                                                            <div class="form-group col-md-12">
                                                                <label>*Indicador</label>
                                                                <input type="text" class="form-control" placeholder="realizado / planificado" name="txtindicador" id="txtindicador" required value="<%= act.getIndicador()%>">
                                                            </div>
                                                            <div class="form-group col-md-4">
                                                                <label>*Grado de consecución</label>
                                                                <select type="text" class="form-control form-control-sm" name="combogrado" id="combogrado" required>
                                                                    <option value="">Seleccione</option>
                                                                    <% if (act.getGrado().equalsIgnoreCase("0%")) {%>
                                                                    <option value="0%" selected>0%</option>
                                                                    <option value="50%">50%</option>
                                                                    <option value="100%">100%</option>
                                                                    <%} else if (act.getGrado().equalsIgnoreCase("50%")) {%>
                                                                    <option value="0%">0%</option>
                                                                    <option value="50%" selected>50%</option>
                                                                    <option value="100%">100%</option>
                                                                    <%} else if (act.getGrado().equalsIgnoreCase("100%")) {%>
                                                                    <option value="0%">0%</option>
                                                                    <option value="50%">50%</option>
                                                                    <option value="100%" selected>100%</option>
                                                                    <%}%>
                                                                </select>
                                                            </div>
                                                            <div class="form-group col-md-4">
                                                                <label>*Fecha límite (aaaa/mm/dd)</label>
                                                                <input type="text" class="form-control datepicker" name="txtfechalimite" id="txtfechalimite" required value="<%= act.getFecha_limite()%>">
                                                            </div>
                                                            <div class="form-group col-md-4">
                                                                <label>*Avance</label>
                                                                <input type="number" class="form-control" onblur="validacionNumber()" min="0" max="100" placeholder="20" name="comboavance" id="comboavance" value="<%= act.getAvance()%>" required>
                                                            </div>
                                                            <div class="form-group col-md-12">
                                                                <label>*Observación/Avance</label>
                                                                <textarea class="form-control" rows="5" name="areaobservacion" id="areaobservacion" required placeholder="En caso de requerirlo aqui puede realizar alguna aclaración más extensa de la actividad realizada, o cualquier aspecto que considere importante resaltar en relación misma."><%= act.getObservacion()%></textarea> 
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-4 border">
                                                            <label class="form-label">*Herramientas utilizadas (Seleccione)</label>
                                                            <div class="selectgroup selectgroup-pills">
                                                                <%for (int paso = 0; paso < herramientas.length; paso++) {%>
                                                                <%if (herramientas[paso].equalsIgnoreCase("Programa de edición")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw1" id="sw1" value="Programa de edición" checked>
                                                                    <span>Programa de edición(audio,video,foto,etc)</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Presencial")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw10" id="sw10" value="Presencial" checked>
                                                                    <span>Presencial</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Ofimática")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw2" id="sw2" value="Ofimática" checked>
                                                                    <span>Ofimática</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Whatsapp/Teléfono")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw3" id="sw3" value="Whatsapp/Teléfono" checked>
                                                                    <span>Whatsapp/Teléfono</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Correo electrónico")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw4" id="sw4" value="Correo electrónico" checked>
                                                                    <span>Correo electrónico</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Redes sociales")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw5" id="sw5" value="Redes sociales" checked>
                                                                    <span>Redes sociales</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("ERP Cabildo | SIGAME")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw6" id="sw6" value="ERP Cabildo | SIGAME" checked>
                                                                    <span>ERP Cabildo | SIGAME</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Video conferencia")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw9" id="sw9" value="Video conferencia" checked>
                                                                    <span>Video conferencia</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (herramientas[paso].equalsIgnoreCase("Páginas WEB")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw7" id="sw7" value="Páginas WEB" checked>
                                                                    <span>Páginas WEB</span>
                                                                </label>
                                                                <br>
                                                                <%}%>
                                                                <%}%>
                                                                <%for (int paso = 0; paso < todas.length; paso++) {%>
                                                                <%if (todas[paso].equalsIgnoreCase("Programa de edición")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw1" id="sw1" value="Programa de edición">
                                                                    <span>Programa de edición(audio,video,foto,etc)</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Presencial")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw10" id="sw10" value="Presencial">
                                                                    <span>Presencial</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Ofimática")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw2" id="sw2" value="Ofimática">
                                                                    <span>Ofimática</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Whatsapp/Teléfono")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw3" id="sw3" value="Whatsapp/Teléfono">
                                                                    <span>Whatsapp/Teléfono</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Correo electrónico")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw4" id="sw4" value="Correo electrónico">
                                                                    <span>Correo electrónico</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Redes sociales")) {%>
                                                                <label>
                                                                    <input type="checkbox" name="sw5" id="sw5" value="Redes sociales">
                                                                    <span>Redes sociales</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("ERP Cabildo | SIGAME")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw6" id="sw6" value="ERP Cabildo | SIGAME">
                                                                    <span>ERP Cabildo | SIGAME</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Video conferencia")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw9" id="sw9" value="Video conferencia">
                                                                    <span>Video conferencia</span>
                                                                </label>
                                                                <br>
                                                                <%} else if (todas[paso].equalsIgnoreCase("Páginas WEB")) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw7" id="sw7" value="Páginas WEB">
                                                                    <span>Páginas WEB</span>
                                                                </label>
                                                                <br>
                                                                <%}%>
                                                                <%}%>
                                                                <%if (act.getHerramienta_otro().length() != 0) {%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw8" onchange="validacionCheckOtro()" id="sw8" value="Otros" checked>
                                                                    <span>Otros</span>
                                                                    <input type="text" name="otros1" id="otros1" class="form-control" value="<%= act.getHerramienta_otro()%>">
                                                                </label>
                                                                <%}else{%>
                                                                <label class="selectgroup-item">
                                                                    <input type="checkbox" name="sw8" onchange="validacionCheckOtro()" id="sw8" value="Otros">
                                                                    <span>Otros</span>
                                                                    <input type="text" name="otros1" id="otros1" class="form-control" style="display: none">
                                                                </label>
                                                                <%}%>
                                                                <%}%>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-9">
                                    <div class="card">
                                        <div class="card-header">
                                            <div class="d-flex align-items-center">
                                                <button class="btn btn-primary" onclick="crearActividad(<%= informacion.getId_usuario()%>)" data-toggle="modal" data-target="#modaladjunto"> <i class="fas fa-paperclip"></i> Añadir adjunto</button>
                                                <b>Añadir archivo de evidencia(opcional): imagen, audio, video u otro formato de máximo 2mb</b>
                                            </div>
                                        </div>
                                        <%
                                            ArrayList<evidencia_actividad> listadoEvidencia = new ArrayList();
                                            if (iact != 0) {
                                                listadoEvidencia = enlace.listadoEvidenciaActividad(iact);
                                            } else {
                                                listadoEvidencia = enlace.listadoEvidenciaActividad(iact);
                                            }
                                        %>
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-3">
                                                    <thead>
                                                        <tr>
                                                            <th hidden>#</th>
                                                            <th>Nombre</th>
                                                            <th hidden>Tamaño</th>
                                                            <th>Acción</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%if (listadoEvidencia.size() != 0) {%>
                                                        <%for (int paso = 0; paso < listadoEvidencia.size(); paso++) {%>
                                                        <tr>
                                                            <td hidden></td>
                                                            <td>
                                                                <%if (act != null) {%>
                                                                <%= listadoEvidencia.get(paso).getNombre()%>
                                                                <%}%>
                                                            </td>
                                                            <td hidden></td>
                                                            <td>
                                                                <%if (act != null) {%>
                                                                <a href="administrar_actividad.control?accion=descargar_evidencia&id_evi=<%= listadoEvidencia.get(paso).getId_evidencia()%>" class="btn btn-primary btn-sm active" data-toggle="tooltip" title="Descargar"><i class="fa fa-download"></i></a>
                                                                <a onclick="eliminarEvidenciaActividad(<%= listadoEvidencia.get(paso).getId_evidencia()%>)" class="btn btn-primary btn-sm active" data-toggle="tooltip" title="Quitar"><i class="fa fa-times"></i></a>
                                                                    <%} else {%>
                                                                <button onclick="" data-toggle="tooltip" title="" class="btn btn-info btn-twitter btn-lg btn-link" data-original-title="Quitar">
                                                                    <i class="fa fa-user-times"></i>
                                                                </button>
                                                                <%}%>
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
                                <div class="col-3">
                                    <div class="card-body">
                                        <div class="card-body">
                                            <br>
                                            <br>
                                            <br>
                                            <%if (act != null) {%>
                                            <input hidden type="text" class="form-control" name="iact" id="iact" value="<%= act.getId_actividad()%>">
                                            <%} else {%>
                                            <input hidden type="text" class="form-control" name="iact" id="iact" value="0">
                                            <%}%>
                                            <div class="d-flex">
                                                <%if (act != null) {%>
                                                <a id="imprimir" target="_blank" href="reporteActividad.control?accion=actividad&iact=<%= act.getId_actividad()%>" class="btn btn-primary"> <i class="fas fa-print"></i> Imprimir registro</a>
                                                <%} else {%>
                                                <a id="imprimir" target="_blank" href="reporteActividad.control?accion=actividad_registro&iu=<%= informacion.getId_usuario()%>" class="btn btn-primary disabled"> <i class="fas fa-print"></i> Imprimir registro</a>
                                                <%}%>
                                            </div>
                                            <br>
                                            <br>
                                            <div class="d-flex">
                                                <button type="submit" class="btn btn-primary" onclick="crearActividad1(<%= informacion.getId_usuario()%>)"> <i class="fas fa-save"></i> Guardar registro</button>
                                            </div>
                                            <br>
                                            <br>
                                            <br>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
                <div class="modal fade" id="modaladjunto" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Añadir adjunto
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formevidencia" action="administrar_evidencia.control" method="post" enctype="multipart/form-data" class="needs-validation" novalidate="">
                                    <div class="form-row">
                                        <div class="col-md-12">
                                            <div class="form-group" hidden>
                                                <label for="txtnombre">Nombre</label>
                                                <%if (iact != 0) {%>
                                                <input type="text" class="form-control" id="txtidact" name="txtidact" placeholder="Nombre para adjunto" required="" value="<%= act.getId_actividad()%>">
                                                <%} else {%>
                                                <input type="text" class="form-control" id="txtidact" name="txtidact" placeholder="Nombre para adjunto" required="">
                                                <%}%>
                                                <div class="invalid-feedback">
                                                    Ingrese un nombre para el adjunto.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label for="txtnombre">Nombre</label>
                                                <input type="text" class="form-control" id="txtnombre" name="txtnombre" placeholder="Nombre para adjunto" required="">
                                                <div class="invalid-feedback">
                                                    Ingrese un nombre para el adjunto.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label for="txtapellido">Documento</label>
                                                <input type="file" class="form-control" id="txtadjunto" name="txtadjunto" required="">
                                                <div class="invalid-feedback">
                                                    Ingrese un archivo valido.
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" value="Upload" class="btn btn-primary">Añadir</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
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
        <script src="fun_js/funciones_actividad.js" type="text/javascript"></script>
        <script src="fun_js/validacion.js" type="text/javascript"></script>
    </body>
</html>
