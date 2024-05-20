<%@page import="modelo.DestinatarioDocumento"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.Documento"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    HttpSession sesion = request.getSession();
    conexion mysql = new conexion();
    int id_usuario = 0,
            id_documento = 0;
    usuario usu = null;
    ArrayList<usuario> listadoDestinatarios = null;
    Documento doc = null;
    try {
        id_usuario = Integer.parseInt(sesion.getAttribute("user").toString());
        usu = mysql.buscar_usuarioID(id_usuario);
        if (request.getParameter("id_documento") != null) {
            id_documento = Integer.parseInt(request.getParameter("id_documento"));
            doc = mysql.getDocumento(id_documento);
            listadoDestinatarios = mysql.listadoUsuarioUnidad(usu.getCodigo_funcion());
            for (usuario d : listadoDestinatarios) {
                if (id_usuario == d.getId_usuario() || doc.getDe() == d.getId_usuario()) {
                    listadoDestinatarios.remove(d);
                    break;
                }
            }
            for(DestinatarioDocumento dd : doc.getIds_destinatarios()){
                for(usuario d : listadoDestinatarios){
                    if(dd.getId_usuario() == d.getId_usuario()){
                        listadoDestinatarios.remove(d);
                        break;
                    }
                }
            }
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
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">

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

        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
    </head>
    <body>
        <%if(listadoDestinatarios.size() > 0){%>
        <div class="table-responsive">
            <table class="table table-striped" id="table-x-informar">
                <thead>                                 
                    <tr>
                        <th>Funcionario</th>
                        <th></th>
                        <th></th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%for(usuario u : listadoDestinatarios){%>
                    <tr>
                        <td><%= u.getApellido() + " " + u.getNombre()%></td>
                        <td></td>
                        <td></td>
                        <td>
                           <a type="button" onclick="informar_recibido(<%= u.getId_usuario()%>)" class="btn btn-primary btn-sm active"><i class="fas fa-check-circle" data-toggle="tooltip" data-original-title="Informar"></i></a>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>
        <%}else{%>
        <p>No hay registros</p>
        <%}%>        
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
        <script src="assets/modules/jquery-ui/jquery-ui.min.js"></script>
        <script src="assets/modules/fullcalendar/locale/es.js"></script>
        <script src="assets/modules/datatables/datatables.min.js"></script>
        <script src="assets/modules/datatables/DataTables-1.10.16/js/dataTables.bootstrap4.min.js"></script>
        <script src="assets/modules/datatables/Select-1.2.4/js/dataTables.select.min.js"></script>
        <script src="assets/js/page/modules-datatables.js"></script>
        <!-- Page Specific JS File -->
        <script src="assets/js/page/forms-advanced-forms.js"></script>

        <!-- Template JS File -->
        <script src="assets/js/scripts.js"></script>
        <script src="assets/js/custom.js"></script>
    </body>
    
    <script type="text/javascript">
        $("#table-x-informar").dataTable({
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
          })
          
          function informar_recibido(nuevo_destinatario){
                Swal.fire({
                    title: "¿Deseas informar este documento?",
                    icon: 'warning',
                    buttonsStyling: false,
                    showCancelButton: true,
                    confirmButtonText: 'Sí',
                    cancelButtonText: 'No, cancelar',
                    customClass: {
                        confirmButton: 'btn btn-success',
                        cancelButton: 'btn btn-danger'
                    }
                }).then((result) => {
                    if(result.isConfirmed){
                        Swal.fire({
                            title: 'Informando',
                            text: 'Por favor espere',
                            timerProgressBar: true,
                            showConfirmButton: false,
                            allowOutsideClick: () => !Swal.isLoading(),
                            allowEscapeKey: () => !Swal.isLoading(),
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        })
                        $.post('administrar_documento.control?accion=informar_recibido', {
                            id_documento: <%= id_documento%>,
                            nuevo_destinatario: nuevo_destinatario
                        }, function (response) {
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
                                });
                            } else if(isNaN(code)) {
                                Swal.fire({
                                    title: mensaje,
                                    icon: "warning",
                                    buttonsStyling: false,
                                    customClass: {
                                        confirmButton: 'btn btn-success'
                                    }
                                })
                            }else{
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
                        }, ).fail(function () {
                            Swal.fire({
                                title: "Error crítico",
                                text: "No se informó",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            })
                        })
                    }                    
                })
            }
    </script>
</html>
