<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.subcomponente"%>
<%@page import="modelo.componente"%>
<%@page import="modelo.modulo"%>
<%@page import="modelo.orientador_gasto"%>
<%@page import="modelo.tipo_actividad"%>
<%@page import="modelo.actividad_plan"%>
<%@page import="modelo.plan_operativo"%>
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
    int id = 0;
    String funcion_usuario = null;
    usuario informacion = null;
    String codigo_direccion = null;
    String codigo_funcion = null;
    String tipo_usuario = null;
    foto_usuario foto = null;
    plan_operativo plan_n = null;
    int id_plan = 0;
    actividad_plan acti_rec = null;
    ArrayList<modulo> listaModulos = null;
    ArrayList<componente> listaComponente = null;
    ArrayList<subcomponente> listaSubcomponente = null;
    try {
        if (request.getParameter("ipa") != null) {
            id_plan = Integer.parseInt(request.getParameter("ipa"));
        }
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = enlace.buscar_usuarioID(id);
        funcion_usuario = enlace.ObtenerFuncionUsuarioID(informacion.getId_usuario());
        codigo_direccion = informacion.getCodigo_unidad();
        tipo_usuario = enlace.tipoUsuario(informacion.getId_usuario());
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(informacion.getId_usuario());
        foto = enlace.buscarFotoUsuarioID(informacion.getId_usuario());
        plan_n = enlace.obtenerPlanOperativoID(id_plan);
        listaModulos = enlace.listadoModulosTipoUsuarioID(informacion.getId_usuario());
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
        <title>Intranet Alcaldía - Asignación de presupuesto</title>
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
                            <h1>Plan operativo PL-<%= id_plan%>(Presupuesto $<%= plan_n.getPresupuesto()%>)</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header">
                                            <h4>Listado de asignaciones</h4> <a class="btn btn-icon icon-left active" id="alertPoa"> 
                                                <span data-toggle="tooltip" data-original-title="Información"><i class="fas fa-question-circle"></i></span>
                                            </a>
                                        </div>
                                        <div class="card-body">
                                            <div class="table-responsive">
                                                <table class="table table-striped" id="table-1">
                                                    <thead>                                 
                                                        <tr>
                                                            <th>Fecha</th>
                                                            <th>Dirección / Unidad</th>
                                                            <th>Actividades</th>
                                                            <th>Presupuesto</th>
                                                            <th>Estado</th>
                                                            <th>Acciones</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%if (!listadoActividadesPlan.isEmpty()) {%>
                                                        <%for (actividad_plan acti : listadoActividadesPlan) {
                                                            tipo_actividad tiac = enlace.obtenerTipoActividadID(acti.getId_tipo());
                                                            orientador_gasto org = enlace.obtenerOrientadorActividadID(acti.getId_orientador());
                                                        %>
                                                        <tr>
                                                            <td><%= "ACT-" + acti.getId_actividad()%></td>
                                                            <td><%= acti.getActividad_descripcion() %></td>
                                                            <td><%= tiac.getDescripcion() %></td>
                                                            <td><%= acti.getIndicador() %></td>
                                                            <td><%= acti.getMeta() %></td>
                                                            <td><%= acti.getPartida_presupuestaria() %></td>
                                                            <td><%= acti.getDescripcion() %></td>
                                                            <td><%= acti.getPresupuesto() %></td>
                                                            <td width="100">
                                                                <a class="btn btn-primary btn-sm active"><i data-toggle="tooltip" data-original-title="Ver plan" class="fa fa-eye"></i></a>
                                                                <a class="btn btn-primary btn-sm active"><i data-toggle="tooltip" data-original-title="Editar" class="fa fa-edit"></i></a>
                                                                <a class="btn btn-primary btn-sm active"><i class="fa fa-times" data-toggle="tooltip" data-original-title="Eliminar"></i></a>
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
                            </div>
                    </section>
                </div>
                <div class="modal fade" id="modalRegistroActividad" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">
                                    <span class="fw-extrabold">
                                        Nueva actividad
                                    </span>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p class="small"></p>
                                <form id="formNuevaActividad" action="administrar_plan.control?accion=registrar_actividad" enctype="multipart/form-data" class="needs-validation" novalidate="" method="post" >
                                    <div class="form-row">
                                        <div class="col-md-12" hidden>
                                            <div class="form-group">
                                                <label for="txtusuario">id plan</label>
                                                <input type="text" class="form-control" id="txtipa" name="txtipa" placeholder="actividad" value="<%= plan_n.getId_plan()%>" readonly>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label for="txtactividad">Actividad *</label>
                                                <input type="text" class="form-control" id="txtactividad" name="txtactividad" placeholder="Actividad" required="">
                                            </div>
                                        </div>
                                        <div class="form-row col-10">
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label for="combotipo">Tipo de actividad *</label>
                                                    <select type="text" class="form-control" id="combotipo" name="combotipo" required="">
                                                        <option value="">Seleccione</option>
                                                        <%for (tipo_actividad tip : listadoTipoActividades) {%>
                                                        <option value="<%= tip.getId_tipo()%>" onclick="seleccionTipoActividad(<%= tip.getId_tipo()%>)"><%= tip.getDescripcion()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-8">
                                                <div class="form-group">
                                                    <label for="txtindicador">Indicador *</label>
                                                    <input type="text" class="form-control" id="txtindicador" name="txtindicador" placeholder="Indicador" required="">
                                                </div>
                                            </div>
                                            <div class="col-md-7">
                                                <div class="form-group">
                                                    <label for="txtmeta">Programación trimestral *</label>
                                                    <div class="form-row col-md-12">
                                                        <div class="col-md-3">
                                                            <input onchange="sumatoriaProgramacion()" type="number" min="0" max="100" class="form-control" id="txtpro1" name="txtpro1" placeholder="I" value="0"  required="">
                                                        </div>
                                                        <div class="col-md-3">
                                                            <input onchange="sumatoriaProgramacion()" type="number" min="0" max="100" class="form-control" id="txtpro2" name="txtpro2" placeholder="II" value="0" required="">
                                                        </div>
                                                        <div class="col-md-3">
                                                            <input onchange="sumatoriaProgramacion()" type="number" min="0" max="100" class="form-control" id="txtpro3" name="txtpro3" placeholder="III" value="0" required="">
                                                        </div>
                                                        <div class="col-md-3">
                                                            <input onchange="sumatoriaProgramacion()" type="number" min="0" max="100" class="form-control" id="txtpro4" name="txtpro4" placeholder="IV" value="0" required="">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="txtmeta">Meta *</label>
                                                    <input type="text" class="form-control" id="txtmeta" name="txtmeta" placeholder="Meta" required="">
                                                </div>
                                            </div> 
                                            <div class="col-md-7">
                                                <div class="form-group">
                                                    <label for="txtpartida">Partida presupuestaria *</label>
                                                    <input type="text" class="form-control" id="txtpartida" name="txtpartida" placeholder="Partida presupuestaria" required="">
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="txtdescripcion">Descripción *</label>
                                                    <input type="text" class="form-control" id="txtdescripcion" name="txtdescripcion" placeholder="Descripción" required="">
                                                </div>
                                            </div>
                                            <div class="col-md-5">
                                                <div class="form-group">
                                                    <label for="comboorientador">Orientador de gasto *</label>
                                                    <select type="text" class="form-control" id="comboorientador" name="comboorientador" required="">
                                                        <option value="">Seleccione</option>
                                                        <%for (orientador_gasto orient : listadoOrientadorActividades) {%>
                                                        <option value="<%= orient.getId_orientador()%>"><%= orient.getDescripcion()%></option>
                                                        <%}%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-3" id="presu" style="display: none">
                                                <div class="form-group">
                                                    <label for="txtpresupuesto">Presupuesto *</label>
                                                    <%if(!enlace.exiteActividadPlanesOperativos(plan_n.getId_plan())){%>
                                                    <input onchange="diferenciaPresupuesto(<%= plan_n.getPresupuesto() %>)" type="number" min="0" class="form-control" id="txtpresupuesto" name="txtpresupuesto" placeholder="Descripción" value="0">
                                                    <%}else{%>
                                                    <input onchange="diferenciaPresupuesto(<%= acti_rec.getPresupuesto_actual() %>)" type="number" min="0" class="form-control" id="txtpresupuesto" name="txtpresupuesto" placeholder="Descripción" value="0">
                                                    <%}%>
                                                </div>
                                            </div>
                                            <div class="col-md-3" id="presua" style="display: none">
                                                <div class="form-group">
                                                    <label for="txtpresupuestoac">Presupuesto actual *</label>
                                                    <%if(!enlace.exiteActividadPlanesOperativos(plan_n.getId_plan())){%>
                                                        <input  type="text" min="0" class="form-control" id="txtpresupuestoac" name="txtpresupuestoac" placeholder="Descripción" value="<%= plan_n.getPresupuesto() %>" readonly="">
                                                    <%}else{%>
                                                    <input type="text" min="0" class="form-control" id="txtpresupuestoac" name="txtpresupuestoac" placeholder="Descripción" value="<%= acti_rec.getPresupuesto_actual() %>" readonly="">
                                                    <%}%>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2 border">
                                            <label class="form-label">*Meses (Seleccione)</label>
                                            <div class="selectgroup selectgroup-pills">
                                                <label class="">
                                                    <input type="checkbox" name="sw1" id="sw1" value="Enero">
                                                    <span>Enero</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw2" id="sw2" value="Febrero">
                                                    <span>Febrero</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw3" id="sw3" value="Marzo">
                                                    <span>Marzo</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw4" id="sw4" value="Abril">
                                                    <span>Abril</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw5" id="sw5" value="Mayo">
                                                    <span>Mayo</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw6" id="sw6" value="Junio">
                                                    <span>Junio</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw7" id="sw7" value="Julio">
                                                    <span>Julio</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw8" id="sw8" value="Agosto">
                                                    <span>Agosto</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw9" id="sw9" value="Septiembre">
                                                    <span>Septiembre</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw10" id="sw10" value="Octubre">
                                                    <span>Octubre</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw11" id="sw11" value="Noviembre">
                                                    <span>Noviembre</span>
                                                </label>
                                                <br>
                                                <label class="selectgroup-item">
                                                    <input type="checkbox" name="sw12" id="sw12" value="Diciembre">
                                                    <span>Diciembre</span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info">
                                        <b>NOTA:</b> Todos los campos de este formulario son obligatorios (*).
                                    </div>
                                    <div class="modal-footer">
                                        <button id="rgbutton" type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Registrar</button>
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <footer class="main-footer">
                    <div class="footer-center">
                        Copyright &copy; 2019 <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal Cantón Esmeraldas - Dirección de Sistemas. All rights reserved.</a>
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
        <script src="assets/modules/sweetalert/sweetalert.min.js"></script>

        <!-- Page Specific JS File -->
        <script src="assets/modules/izitoast/js/iziToast.min.js"></script>
        <script src="assets/js/page/modules-sweetalert.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        <script src="fun_js/funciones_plan.js" type="text/javascript"></script>
        <script src="fun_js/formulario_planificacion.js" type="text/javascript"></script>
        <script src="fun_js/validacion.js" type="text/javascript"></script>

    </body>
</html>
