<%@page import="modelo.AnexoDocumento"%>
<%@page import="modelo.Documento"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    HttpSession sesion = request.getSession();
    conexion mysql = new conexion();
    int id_usuario = 0,
            id_documento = 0;
    Documento doc = new Documento();
    try {
        id_usuario = Integer.parseInt(sesion.getAttribute("user").toString());
        if (request.getParameter("id_documento") != null) {
            id_documento = Integer.parseInt(request.getParameter("id_documento"));
            doc = mysql.getDocumento(id_documento, id_usuario);
            if (doc.getId() == 0) {
                throw new Exception("Documento inexistente");
            }
        }
    } catch (Exception e) {
        System.out.println("ges_doc_subir | "+e);
        throw new Exception("Error al subir documento");
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
        <form id="formSubirDoc" action="administrar_documento.control?accion=subir_documento" method="post" enctype="multipart/form-data" class="needs-validation">
            <div class="form-row">
                <input hidden name="m_sub_doc_id" id="m_sub_doc_id" value="<%= id_documento%>">  
                <div class="form-group col-md-12">
                    <label>Documento (tamaño máximo 2MB)</label>
                    <input type="file" class="form-control" name="m_sub_doc_adjunto" id="m_sub_doc_adjunto" onchange="validar_extensiones('m_sub_doc_adjunto',['pdf'])" accept=".pdf" required>
                    <div class="invalid-feedback">
                        No adjuntó ningún archivo
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" id="m_sub_doc_btn" class="btn btn-primary">Subir y enviar</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>
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
        
        <script>
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
                    validate_size(input_id)
                }
            }
            
            $(document).ready(function () {
                const btn_subir = document.getElementById('m_sub_doc_btn')
                $('#formSubirDoc').submit(function (event) {
                    btn_subir.disabled = true
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
                                title: 'Subiendo documento y enviando',
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
                                    btn_subir.disabled = false
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
                                text: "No se subió el documento",
                                icon: "error",
                                buttonsStyling: false,
                                customClass: {
                                    confirmButton: 'btn btn-success'
                                }
                            }).then(function () {
                                btn_subir.disabled = false
                            })
                        }
                    });
                    return false;
                })
            })
        </script>
    </body>
</html>
