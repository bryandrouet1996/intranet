<%-- 
    Document   : actividades.jsp
    Created on : 27/04/2020, 15:16:32
    Author     : Kevin Druet
--%>

<%@page import="modelo.forma_soporte"%>
<%@page import="modelo.tipo_soporte"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    conexion enlace = new conexion();
    int id_usuario = 0;
    String codigo_funcion = null;
    usuario informacion = null;
    ArrayList<usuario> listadoFuncionarios = null;
    ArrayList<tipo_soporte> listadoTipoSoportes = null;
    ArrayList<forma_soporte> listadoFormaSoportes = null;
    ArrayList<usuario> listadoTecnicos = null;
    try {
        id_usuario = Integer.parseInt(request.getParameter("iu"));
        informacion = enlace.buscar_usuarioID(id_usuario);
        listadoFuncionarios = enlace.listadoUsuariosDireccion();
        codigo_funcion = enlace.obtenerCodigoFuncionUsuario(id_usuario);
        listadoTipoSoportes = enlace.listadoTiposSoporte();
        listadoFormaSoportes = enlace.listadoFormasSoporte();
        listadoTecnicos = enlace.listadoUsuarioUnidad("D.02.15");
    } catch (Exception e) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">

        <!-- CSS Libraries -->
        <link rel="stylesheet" href="assets/modules/bootstrap-daterangepicker/daterangepicker.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-colorpicker/dist/css/bootstrap-colorpicker.min.css">
        <link rel="stylesheet" href="assets/modules/select2/dist/css/select2.min.css">
        <link rel="stylesheet" href="assets/modules/jquery-selectric/selectric.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-timepicker/css/bootstrap-timepicker.min.css">
        <link rel="stylesheet" href="assets/modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css">

        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    </head>
    <body>
        <p class="small"></p>
        <form id="formsoporte" action="administrar_ticket.control?accion=registro_ticket" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-2" hidden>
                    <div class="form-group">
                        <input type="text" class="form-control" id="txtidusu" name="txtidusu" value="<%= informacion.getId_usuario()%>" required="">
                    </div>
                </div>
                <div class="form-group col-md-12">
                    <label>Funcionario solicitante*</label>
                    <select class="form-control select2" name="combosolicitante" id="combosolicitante" required="" readonly> 
                        <option>Seleccione</option>
                        <%for (usuario funcionario : listadoFuncionarios) {%>
                            <%if(funcionario.getId_usuario()==informacion.getId_usuario()){%>
                                <option selected value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}else{%>
                                <option value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                            <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-3">
                    <label>Forma solicitud*</label>
                    <select class="form-control select2" name="comboforma" id="comboforma" required="">
                        <option>Seleccione</option>
                        <%for (forma_soporte forma : listadoFormaSoportes) {%>
                        <%if(forma.getId_forma()==4){%>
                        <option selected value="<%= forma.getId_forma()%>"><%= forma.getDescripcion()%></option>
                        <%}else{%>
                        <option value="<%= forma.getId_forma()%>"><%= forma.getDescripcion()%></option>
                        <%}%>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-6">
                    <label>Técnico sugerido*</label>
                    <select class="form-control select2" name="combosugerido" id="combosugerido" required="">
                        <option>Seleccione</option>
                        <%for (usuario funcionario : listadoTecnicos) {%>
                        <option value="<%= funcionario.getId_usuario()%>"><%= funcionario.getApellido()%> <%= funcionario.getNombre()%></option>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-3">
                    <label>Tipo de solicitud*</label>
                    <select class="form-control select2" name="combotipo" id="combotipo" required="">
                        <option>Seleccione</option>
                        <%for (tipo_soporte tipo : listadoTipoSoportes) {%>
                        <option value="<%= tipo.getId_tipo()%>"><%= tipo.getDescripcion()%></option>
                        <%}%>
                    </select>
                </div>
                <div class="form-group col-md-12">
                    <div class="form-group">
                        <label for="areadescripcion">Descripción de problemática*</label>
                        <textarea type="text" class="form-control" id="areadescripcion" name="areadescripcion" placeholder="Describa la problemática reportada por el usuario." required></textarea>
                    </div>
                </div>
                <div class="form-group col-md-12">
                    <label>Adjuntar archivo (tamaño máximo 2 MB)</label>
                    <input type="file" class="form-control" name="adjunto" id="adjunto" onchange="validateSize()">
                    <div class="invalid-feedback">
                        No adjuntó ningún archivo
                    </div>
                </div>
                    <div class="form-group col-md-12" hidden="">
                    <label>Soporte de referencia</label>
                    <input type="number" class="form-control" name="referencia" id="referencia" placeholder="Ingrese el número del soporte de referencia" onchange="verificarRef()">
                </div>
            </div>
            <div class="alert alert-info">
                <b>NOTA:</b> Todos los campos de este formulario son obligatorios (*).
            </div>
            <div class="modal-footer">
                <button id="btnRegistrar" onclick="registrar()" type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Registrar</button>
                <button onclick="cerrar()" type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
            </div>
        </form>
        <script src="assets/modules/jquery.min.js"></script>
        <script src="assets/modules/popper.js"></script>
        <script src="assets/modules/tooltip.js"></script>
        <script src="assets/modules/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
        <script src="assets/modules/moment.min.js"></script>
        <script src="assets/js/stisla.js"></script>

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
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>
        <script src="fun_js/funciones_soporte.js" type="text/javascript"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
        
        <script type="text/javascript">
            function registrar(){
                var btn = document.getElementById('btnRegistrar');
                btn.hidden = true;
            }
            
            function validateSize() {
                var input = document.getElementById("adjunto");
                const fileSize = input.files[0].size / 1024 / 1024;
                if (fileSize > 2) {
                    swal("Mensaje", "El archivo excede el tamaño máximo de 2 MB", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    });
                    $('#adjunto').val('');
                }
            }
            
            function verificarRef(){                
                var input = document.getElementById('referencia'), ref = input.value;
                ref = Number.parseInt(ref);
                if(Number.isNaN(ref)){
                    swal("Soporte de referencia inválido", "El número de soporte ingresado es inválido", {
                        icon: "error",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    });
                    input.value = '';
                }else{
                    $.post('administrar_ticket.control?accion=solicitud_referencia', {
                        idSol: ref
                    }, function (responseText) {
                        var res = parseInt(responseText);
                        if (res == 0) {
                            swal("Soporte de referencia no atendido", "El soporte de referencia ingresado debe estar atendido", {
                                icon: "error",
                                buttons: {
                                    confirm: {
                                        className: 'btn btn-success'
                                    }
                                },
                            });
                            input.value = '';
                        } else if (res == -1) {
                            swal("Soporte de referencia inexistente", "El soporte de referencia ingresado no existe", {
                                icon: "error",
                                buttons: {
                                    confirm: {
                                        className: 'btn btn-success'
                                    }
                                },
                            });
                            input.value = '';
                        }
                    });
                }
            }
        </script>
    </body>
</html>
