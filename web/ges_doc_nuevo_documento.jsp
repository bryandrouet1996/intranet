<%@page import="modelo.Direccion"%>
<%@page import="modelo.AnexoDocumento"%>
<%@page import="modelo.DestinatarioDocumento"%>
<%@page import="modelo.TipoDocumento"%>
<%@page import="modelo.Documento"%>
<%@page import="java.sql.Date"%>
<%@page import="java.time.LocalDate"%>
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
    ArrayList<Direccion> listadoDirecciones = null;
    ArrayList<usuario> listadoDestinatarios = null;
    ArrayList<TipoDocumento> listadoTipos = null;
    int referencia = 0,
            id_documento = 0,
            tipo_documento = 0,
            tipo_circular = 0;
    Documento doc = new Documento();
    String arr_destinatarios = "",
            arr_destinatarios_finales = "";
    try {
        id = Integer.parseInt(sesion.getAttribute("user").toString());
        informacion = mysql.buscar_usuarioID(id);
        foto = mysql.buscarFotoUsuarioID(informacion.getId_usuario());
        listaComponente = mysql.listadoComponentesTipoUsuarioID(informacion.getId_usuario(), ID_MOD_GES_DOC);
        listadoDirecciones = mysql.getDirecciones();
        listadoDestinatarios = mysql.getOpcionesDestinatarioDocumento(id);
        for (usuario u : listadoDestinatarios) {
            arr_destinatarios += (u.stringify() + ",");
        }
        if(arr_destinatarios.length()>0){
            arr_destinatarios = arr_destinatarios.substring(0, arr_destinatarios.length() - 1);
        }
        listadoTipos = mysql.listadoTipoDocumento();
        for(TipoDocumento t:listadoTipos){
            if(t.getId() == 2 && !mysql.verificarUsuarioCumpleRol(id, "administrador")){
                listadoTipos.remove(t);
                break;
            }
        }
        if (request.getParameter("referencia") != null) {
            referencia = Integer.parseInt(request.getParameter("referencia"));
        }
        if (request.getParameter("id_documento") != null) {
            id_documento = Integer.parseInt(request.getParameter("id_documento"));
            doc = mysql.getDocumento(id_documento, informacion.getId_usuario());
            if(doc.getEstado() != 0){
                doc = new Documento();                
            }else{
                tipo_documento = doc.getTipo();
                tipo_circular = doc.getTipo_circular();
            }
        }
    } catch (Exception e) {
    System.out.println("e "+e);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <title>Intranet Alcaldía - Nuevo documento</title>
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
                            <li class="<%= c.getRuta_enlace().equals("nuevo_documento") ? "active" : ""%>"><a class="nav-link" href="sesion.control?accion=<%= c.getRuta_enlace()%>&iu=<%= id%>"><i class="<%= c.getIcono()%>"></i> <span><%= c.getDescripcion()%></span></a></li>
                            <%}%>
                        </ul>
                    </aside>
                </div>
                <br>
                <div class="main-content">
                    <section class="section">
                        <div class="section-header">
                            <h1>Nuevo documento</h1>
                        </div>
                        <div class="section-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <form class="needs-validation" id="formNuevo" action="administrar_documento.control?accion=registrar" method="post" enctype="multipart/form-data">
                                                <div class="card-body">
                                                    <div class="row">
                                                        <input hidden name="id_documento" id="id_documento" value="<%= id_documento %>">
                                                        <input hidden name="referencia" id="referencia" value="<%= referencia %>">
                                                        <input hidden name="es_borrador" id="es_borrador" value="0">
                                                        <input hidden name="destinatarios" id="destinatarios">
                                                        <div class="form-group col-md-2">
                                                            <label>Tipo de documento *</label>
                                                            <select class="form-control" id="tipo" name="tipo" required onchange="cambio_tipo()">
                                                                <%for (TipoDocumento tipo : listadoTipos) {%>
                                                                <option value="<%= tipo.getId()%>" <%= doc.getId() != 0 && doc.getTipo() == tipo.getId() ? "selected" : ""%>><%= tipo.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-10" id="div_tipo_circular">
                                                            <label>Tipo de circular *</label>
                                                            <select class="form-control select2" name="tipo_circular" id="tipo_circular">
                                                                <option value="1" <%= tipo_circular <= 1 ? "selected" : ""%>>Interna</option>
                                                                <option value="2" <%= tipo_circular == 2 ? "selected" : ""%>>Para directores</option>
                                                                <option value="3" <%= tipo_circular == 3 ? "selected" : ""%>>General</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-5" id="div_direccion">
                                                            <label>Dirección </label>
                                                            <select class="form-control select2" name="direccion" id="direccion" onchange="cambio_direccion()">
                                                                <option value="" selected disabled>Seleccione dirección</option>
                                                                <%for (Direccion dir : listadoDirecciones) {
                                                                %>
                                                                <option value="<%= dir.getId()%>"><%= dir.getNombre()%></option>
                                                                <%}%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-5" id="div_destinatarios_disp">
                                                            <label>Destinatario(s) disponibles</label>
                                                            <select class="form-control select2" multiple="multiple" name="destinatarios_disp" id="destinatarios_disp" disabled onchange="cambio_destinatario_disp()">
                                                                <option value="" disabled>Seleccione funcionario(s)</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-12" id="div_destinatario">
                                                            <label>Destinatario(s) elegido(s)*</label>
                                                            <select class="form-control select2" multiple="multiple" name="destinatario" id="destinatario" required onchange="cambio_destinatarios_elegidos()">
                                                                <%for(DestinatarioDocumento des : doc.getIds_destinatarios()){
                                                                    for(usuario func : listadoDestinatarios){
                                                                        if(des.getId_usuario() == func.getId_usuario()){
                                                                        final String texto_des = (des.getTipo() == 1 ? "PARA: " : "CC: ") + func.getNombre() + " - " + func.getCodigo_unidad();
                                                                        arr_destinatarios_finales += "{id: " + des.getId_usuario() + ",tipo: " + des.getTipo() + ", nombre: '" + texto_des + "'},";
                                                                        %>
                                                                        <option value="<%= func.getId_usuario()%>" selected><%= texto_des%></option>
                                                                        <%break;}
                                                                    }
                                                                }
                                                                if(arr_destinatarios_finales.length()>0){
                                                                    arr_destinatarios_finales = arr_destinatarios_finales.substring(0,arr_destinatarios_finales.length()-1);
                                                                }%>
                                                            </select>
                                                        </div>
                                                        <div class="form-group col-md-5" id="div_para">
                                                            <label>Destinatario *</label>
                                                            <input type="text" class="form-control" name="para" id="para" placeholder="Ingrese el nombre del destinatario" value="<%= doc.getPara() == null ? "" : doc.getPara()%>">
                                                        </div>
                                                        <div class="form-group col-md-5" id="div_cargo_para">
                                                            <label>Cargo destinatario *</label>
                                                            <input type="text" class="form-control" name="cargo_para" id="cargo_para" placeholder="Ingrese el cargo del destinatario" value="<%= doc.getPara_cargo() == null ? "" : doc.getPara_cargo()%>">
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Asunto *</label>
                                                            <textarea style="width: 100%" rows="2" name="asunto" id="asunto" required placeholder="Detalle el asunto del documento"><%= doc.getId() != 0 ? doc.getAsunto() : ""%></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Contenido *</label>
                                                            <textarea style="width: 100%" rows="13" name="contenido" id="contenido" required placeholder="Detalle el contenido del documento."><%= doc.getId() != 0 ? doc.getContenido() : ""%></textarea> 
                                                        </div>
                                                        <div class="form-group col-md-12">
                                                            <label>Anexos</label>
                                                            <input multiple type="file" class="form-control" name="anexos" id="anexos" onchange="validar_anexos('anexos')">
                                                        </div>
                                                        <%if(doc.getAnexos().size() > 0){%>
                                                        <div class="form-group col-md-12" id="div_anexos">
                                                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                                <li class="nav-item">
                                                                    <a class="nav-link active" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="true"><i class="fas fa-file"></i> Anexos cargados <%if(doc.getAnexos().size()!=0){%><span id="span_anexos" class="badge badge-primary"><%= doc.getAnexos().size() %></span><%}%></a>
                                                                </li>
                                                            </ul>
                                                            <div class="tab-content" id="myTabContent">
                                                                <div class="tab-pane fade show active" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                                    <div class="table-responsive">
                                                                        <table class="table table-striped" id="table-x">
                                                                            <thead>                                 
                                                                                <tr>
                                                                                    <th style="width: 10%">N°</th>
                                                                                    <th style="width: 70%">Nombre</th>
                                                                                    <th style="width: 20%">Acciones</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <%int i = 1; for(AnexoDocumento a : doc.getAnexos()){%>
                                                                                <tr>
                                                                                    <td><%= i%></td>
                                                                                    <td><%= a.getNombre()%></td>
                                                                                    <td>
                                                                                        <a href="descargar_archivo.control?accion=descargar_archivo&ruta=<%= a.getPath()%>" target="_blank" class="btn btn-primary btn-sm" data-toggle="tooltip" data-original-title="Descargar documento"><i class="fas fa-download"></i></a>
                                                                                        <a onclick="eliminar_anexo(<%= i%>,<%= a.getId()%>,<%= a.getIdDocumento()%>)" class="btn btn-primary btn-sm active"><i class="fa fa-times" data-toggle="tooltip" data-original-title="Eliminar"></i></a>
                                                                                    </td>
                                                                                </tr>
                                                                                <%}%>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                </div>                                                
                                                            </div>
                                                        </div>
                                                        <%}%>
                                                        <div class="form-group col-md-12">
                                                            <p>¿Deseas firmar electrónicamente el documento?</p>
                                                            <input type="radio" id="fi_1" name="firma_opc" value="1" required checked onchange="cambioFirma()">
                                                            <label for="fi_1">Sí</label><br>
                                                            <input type="radio" id="fi_0" name="firma_opc" value="0" onchange="cambioFirma()">
                                                            <label for="fi_0">No</label><br>
                                                        </div>
                                                            <div class="form-group col-md-6" id="div_firma">
                                                            <label>Firma</label>
                                                            <input type="file" class="form-control" name="firma" id="firma" onchange="validar_extensiones('firma',['p12','pfx'])" accept=".p12, .pfx" required>
                                                        </div>
                                                        <div class="form-group col-md-6" id="div_clave_firma">
                                                            <label>Clave firma</label>
                                                            <input type="password" class="form-control" name="clave_firma" id="clave_firma" maxlength="30" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-1"></div>
                                                    <div class="col-4">
                                                        <button id="btnBorrador" class="btn btn-primary btn-block btn-lg" type="button" onclick="submitForm(1)"><i class="fas fa-save"></i> Guardar como borrador</button>
                                                    </div>
                                                    <div class="col-2"></div>
                                                    <div class="col-4">
                                                        <button id="btnRegistrar" class="btn btn-primary btn-block btn-lg" type="button" onclick="submitForm(0)"><i class="fas fa-key"></i> Firmar y enviar</button>
                                                    </div>
                                                    <div class="col-1"></div>
                                                </div>
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

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        
        <script type="text/javascript">
            const FORM_ID = 'formNuevo',
                    HTML_BTN_ENVIAR_FE = `<i class="fas fa-key"></i> Firmar y enviar`,
                    HTML_BTN_ENVIAR_E = `<i class="fas fa-paper-plane"></i> Enviar`,
                    input_tipo = document.getElementById('tipo'),
                    div_tipo_circular = document.getElementById('div_tipo_circular'),
                    input_tipo_circular = document.getElementById('tipo_circular'),
                    div_direccion = document.getElementById('div_direccion'),
                    div_destinatarios_disp = document.getElementById('div_destinatarios_disp'),
                    div_destinatario = document.getElementById('div_destinatario'),
                    div_para = document.getElementById('div_para'),
                    div_cargo_para = document.getElementById('div_cargo_para'),
                    input_para = document.getElementById('para'),
                    input_cargo_para = document.getElementById('cargo_para'),
                    input_destinatario = document.getElementById('destinatario'),
                    input_destinatarios = document.getElementById('destinatarios'),
                    input_asunto = document.getElementById('asunto'),
                    input_contenido = document.getElementById('contenido'),
                    fi_1 = document.getElementById('fi_1'),
                    fi_0 = document.getElementById('fi_0'),
                    div_firma = document.getElementById('div_firma'),
                    input_firma = document.getElementById('firma'),
                    div_clave_firma = document.getElementById('div_clave_firma'),
                    input_clave_firma = document.getElementById('clave_firma'),
                    btn_borrador = document.getElementById('btnBorrador'),
                    btn_registrar = document.getElementById('btnRegistrar'),
                    input_es_borrador = document.getElementById('es_borrador'),
                    div_anexos = document.getElementById('div_anexos'),
                    span_anexos = document.getElementById('span_anexos'),
                    tabla_x = document.getElementById('table-x'),
                    tipos_dest = {1: "Para", 2: "Con Copia"},
                    tipos_dest_acr = {1: "PARA", 2: "CC"}
                
            const OPC_TABLA = {
                "ordering": false,
                "pageLength": 10,
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
              }
              
            $("#table-x").dataTable(OPC_TABLA)
                
            function check_fields(es_borrador){
                const tipo_documento = parseInt(input_tipo.value)
                if(tipo_documento === 3){//SI ES OFICIO
                    if(input_para.value === '' || input_cargo_para.value === ''){
                        return false
                    }
                }else if(tipo_documento !== 2){//SI NO ES CIRCULAR
                    if(input_destinatario.value === ''){
                        return false
                    }
                }
                if(input_asunto.value === '' || input_contenido.value === ''){
                    return false
                }
                if(!es_borrador && fi_1.checked){
                    if(input_firma.files[0] === null || input_clave_firma.value === ''){
                        return false
                    }
                }
                return true                
            }
            
            function submitForm(es_borrador){
                input_es_borrador.value = es_borrador
                const form_valido = check_fields(es_borrador)
                if(!form_valido){
                    Swal.fire({
                        title: "Formulario incompleto",
                        icon: "warning",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    })
                    return
                }
                let destinatarios = '';
                for(var d of arr_destinatarios_finales){
                    destinatarios += d.id + ':' + d.tipo + ';'
                }
                input_destinatarios.value = destinatarios.substring(0,destinatarios.length-1)
                const tipo_circular = div_tipo_circular.hidden ? 0 : input_tipo_circular.value
                input_tipo_circular.value = tipo_circular
                if(es_borrador || fi_0.checked){
                    $('#'+FORM_ID).submit()
                }else{
                    const formData = new FormData()
                    formData.append('firma', input_firma.files[0])
                    formData.append('clave_firma', input_clave_firma.value)
                    $.ajax({
                        url: 'administrar_documento.control?accion=verificar_firma',
                        type: 'POST',
                        data: formData,
                        contentType: false,
                        cache: false,
                        processData: false,
                        beforeSend: function () {
                            Swal.fire({
                                title: 'Verificando firma',
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
                            const code = parseInt(response)
                            if (code === 1) {
                                $('#'+FORM_ID).submit()
                            } else {
                                Swal.fire({
                                    title: 'Ocurrió un error al verificar la firma, ¿desea enviar de todos modos?',
                                    icon: 'warning',
                                    buttonsStyling: false,
                                    showCancelButton: true,
                                    confirmButtonText: 'Sí, gestionar',
                                    cancelButtonText: 'No, cancelar',
                                    customClass: {
                                        confirmButton: 'btn btn-success',
                                        cancelButton: 'btn btn-danger'
                                    }
                                }).then((action) => {
                                    if (action.isConfirmed) {
                                        $('#'+FORM_ID).submit()
                                    }else{
                                        Swal.fire({
                                            title: "Revise la contraseña ingresada",
                                            icon: "warning",
                                            buttonsStyling: false,
                                            customClass: {
                                                confirmButton: 'btn btn-success'
                                            }
                                        })
                                    }
                                })
                            }
                        },
                        error: function () {
                            Swal.fire({
                                title: 'Error crítico',
                                text: 'Ocurrió un error al firmar',
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            })
                        }
                    })
                }
            }
            
            function eliminar_anexo(id_fila, id_anexo, id_documento){
                Swal.fire({
                    title: '¿Deseas eliminar el anexo?',
                    icon: 'warning',
                    buttonsStyling: false,
                    showCancelButton: true,
                    confirmButtonText: 'Sí, eliminar',
                    cancelButtonText: 'No, cancelar',
                    customClass: {
                        confirmButton: 'btn btn-success',
                        cancelButton: 'btn btn-danger'
                    }
                }).then((action) => {
                    if (action.isConfirmed) {
                        const formData = new FormData()
                        formData.append('id_anexo', id_anexo)
                        formData.append('id_documento', id_documento)
                        $.ajax({
                            url: 'administrar_documento.control?accion=eliminar_anexo',
                            type: 'POST',
                            data: formData,
                            contentType: false,
                            cache: false,
                            processData: false,
                            beforeSend: function () {                       
                                Swal.fire({
                                    title: 'Eliminando anexo',
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
                                    const anexos_actuales = parseInt(span_anexos.textContent)-1
                                    if(anexos_actuales === 0)
                                        div_anexos.hidden = true
                                    span_anexos.textContent = anexos_actuales
                                    tabla_x.deleteRow(id_fila)
                                    Swal.fire({
                                        title: mensaje,
                                        icon: "success",
                                        buttonsStyling: false,
                                        customClass: {
                                            confirmButton: 'btn btn-success'
                                        }
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
                                    })
                                }
                            },
                            error: function () {
                                Swal.fire({
                                    title: 'Error crítico',
                                    text: "No se eliminó el anexo",
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
                
            $(document).ready(function () {
                div_tipo_circular.hidden = true
                div_direccion.hidden = true
                div_destinatarios_disp.hidden = true
                div_destinatario.hidden = true
                div_para.hidden = true
                div_cargo_para.hidden = true
                const tipo_documento = <%= tipo_documento %>
                if(tipo_documento === 2){
                    div_tipo_circular.hidden = false
                }else if(tipo_documento === 3){
                    div_para.hidden = false
                    div_cargo_para.hidden = false                    
                }else{                    
                    div_direccion.hidden = false
                    div_destinatario.hidden = false
                    div_destinatarios_disp.hidden = false
                }                
                $('#'+FORM_ID).submit(function (event) {
                    event.preventDefault();
                    btn_borrador.disabled = true
                    btn_registrar.disabled = true
                    $.ajax({
                        url: $(this).attr('action'),
                        type: $(this).attr('method'),
                        data: new FormData(this),
                        contentType: false,
                        cache: false,
                        processData: false,
                        beforeSend: function () {                       
                            Swal.fire({
                                title: 'Registrando documento',
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
                                }).then(function () {
                                    btn_borrador.disabled = false
                                    btn_registrar.disabled = false
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
                                text: "No se registró el documento",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                btn_borrador.disabled = false
                                btn_registrar.disabled = false
                            })
                        }
                    })
                })
            })
            
            function cambioFirma(){                
                if(fi_1.checked){
                    div_firma.hidden = false
                    input_firma.required = true
                    div_clave_firma.hidden = false
                    input_clave_firma.required = true
                    btn_registrar.innerHTML = HTML_BTN_ENVIAR_FE
                }else{
                    div_firma.hidden = true
                    input_firma.value = ''
                    input_firma.required = false
                    div_clave_firma.hidden = true
                    input_clave_firma.value = ''
                    input_clave_firma.required = false
                    btn_registrar.innerHTML = HTML_BTN_ENVIAR_E
                }
            }
            
            function validar_nombre_anexo(nombre){
                if(tabla_x){
                    const filas = tabla_x.rows
                    for(fila of filas){
                        if(nombre === fila.cells[1].textContent)
                            return false
                    }
                    return true
                }
                return true
            }
            
            function validate_size(input_id) {
                const MAX_SIZE = 2 //In MB
                const input = document.getElementById(input_id);
                for (a of input.files) {
                    const fileSize = a.size / 1024 / 1024;
                    if (fileSize > MAX_SIZE) {
                        Swal.fire({
                            title: "Tamaño inválido",
                            text: "El archivo " + a.name + " excede el tamaño máximo de " + MAX_SIZE + " MB",
                            icon: "warning",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        })
                        input.value = null
                        break
                    }
                }
            }

            function validar_anexos(input_id) {
                const input = document.getElementById(input_id);
                for (a of input.files) {
                    if(!validar_nombre_anexo(a.name)){
                        Swal.fire({
                            title: "Nombre duplicado",
                            text: "El archivo " + a.name + " ya fue agregado como anexo",
                            icon: "warning",
                            buttonsStyling: false,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        })
                        input.value = null
                        return
                    }
                    validate_size(input_id)
                }
            }
            
            function validar_extensiones(input_id, extensiones){
                const input = document.getElementById(input_id),
                        ext_str = extensiones.reduce((acu, nue)=> acu+=nue+', ','').slice(0,-2),
                        ext_reg_str = extensiones.reduce((acu, nue)=> acu+=nue+'|','').slice(0,-1),
                        regex = new RegExp("(.*?)\.("+ext_reg_str+")$"),
                        name = input.value.toLowerCase()
                if (!(regex.test(name))) {
                    Swal.fire({
                        title: "Archivo inválido",
                        text: "El formato del archivo debe ser: "+ext_str,
                        icon: "warning",
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    })
                    input.value = null
                }else{
                    validar_anexos(input_id)
                }
            }
            
            const arr_destinatarios=[<%= arr_destinatarios%>],
                    combo_direcciones = document.getElementById('direccion'),
                    combo_destinatarios_disp = document.getElementById('destinatarios_disp')
            let arr_destinatarios_finales = [<%= arr_destinatarios_finales%>]
            
            function cleanCombo(combo, seleccione = true){
                combo.length = 0
                if(seleccione){
                    const op = new Option('Seleccione', null)
                    op.disabled = true
                    op.selected = true
                    combo.add(op)
                }
            }
            
            function agregarOps(combo, arr, disabled = false){
                cleanCombo(combo, false)
                for(item of arr){
                    combo.add(new Option(item.nombre, item.id))
                }
                combo.disabled = disabled
            }
            
            function cambio_tipo(){
                const tipo = parseInt(input_tipo.value)
                div_tipo_circular.hidden = true
                div_direccion.hidden = true
                div_destinatario.hidden = true
                div_destinatarios_disp.hidden = true
                input_para.value = ''
                div_para.hidden = true
                input_cargo_para.value = ''
                div_cargo_para.hidden = true
                if(tipo === 2){
                    div_tipo_circular.hidden = false
                }else if(tipo === 3){
                    div_para.hidden = false
                    div_cargo_para.hidden = false                    
                }else{
                    div_direccion.hidden = false
                    div_destinatario.hidden = false
                    div_destinatarios_disp.hidden = false                    
                }
            }
            
            function cambio_direccion() {
                const id_direccion = combo_direcciones.value
                agregarOps(combo_destinatarios_disp, arr_destinatarios.filter(i => i.codigo_funcion == id_direccion))
            }
            
            async function cambio_destinatario_disp(){
                const destinatarios_elegidos = []
                for(var opt of input_destinatario.options){
                    destinatarios_elegidos.push({id: opt.value, nombre: opt.text})
                }
                let nuevo_destinatario
                for(var opt of combo_destinatarios_disp.options){
                    if(opt.selected){
                        const input_tipo_des = await Swal.fire({
                            title: "Tipo de destinatario",                            
                            buttonsStyling: false,
                            showCancelButton: true,
                            confirmButtonText: 'Agregar',
                            cancelButtonText: 'Cancelar',
                            customClass: {
                                confirmButton: 'btn btn-success',
                                cancelButton: 'btn btn-danger'
                            },
                            input: "select",
                            inputOptions: tipos_dest
                        }), tipo_des = input_tipo_des.value
                        if(tipo_des){
                            nuevo_destinatario = {id: opt.value, tipo: parseInt(tipo_des), nombre: tipos_dest_acr[tipo_des] + ': ' + opt.text + ' - ' + combo_direcciones.selectedOptions[0].text}
                        }
                        break
                    }
                }
                const destinatarios_disp = []
                for(var opt of combo_destinatarios_disp.options){
                    destinatarios_disp.push({id: opt.value, nombre: opt.text})
                }
                $("#destinatarios_disp").empty()
                agregarOps(combo_destinatarios_disp, destinatarios_disp)
                if(nuevo_destinatario){
                    if(!destinatarios_elegidos.find(i=>i.id===nuevo_destinatario.id)){
                        destinatarios_elegidos.push(nuevo_destinatario)
                        arr_destinatarios_finales.push(nuevo_destinatario)
                        arr_destinatarios_finales = arr_destinatarios_finales.sort((a,b)=>{return a.tipo-b.tipo})
                    }
                }
                agregarOps(input_destinatario, destinatarios_elegidos)
                for(var opt of input_destinatario.options){
                    opt.selected = true
                }
            }
            
            function cambio_destinatarios_elegidos(){
                const destinatarios = []
                for(var opt of input_destinatario.options){
                    if(opt.selected){
                        destinatarios.push({id: opt.value, nombre: opt.text})                        
                    }else{
                        arr_destinatarios_finales = arr_destinatarios_finales.filter(i=>opt.value!==i.id.toString())
                    }
                }
                $("#destinatario").empty()
                agregarOps(input_destinatario, destinatarios)
                for(var opt of input_destinatario.options){
                    opt.selected = true
                }
            }
        </script>
        
    </body>
</html>
