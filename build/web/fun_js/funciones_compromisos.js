/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formularioVerificable').submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr('action'),
            type: $(this).attr('method'),
            data: new FormData(this),
            contentType: false,
            cache: false,
            processData: false,
            success: function (response) {
                if (response) {
                    $('#modalCumplimiento').modal('hide');
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "listado_compromisos.jsp";
                    });
                } else {
                    iziToast.error({
                        title: 'Aviso',
                        message: 'Existio un error al intentar realizar esta acción',
                        position: 'topRight',
                    });
                }
            }
        });
        return false;
    });

    $('#formularioArchivo').submit(function (event) {
        var id_compromiso = $('#txtidcompromiso').val();
        event.preventDefault();
        $.ajax({
            url: $(this).attr('action'),
            type: $(this).attr('method'),
            data: new FormData(this),
            contentType: false,
            cache: false,
            processData: false,
            success: function (response) {
                if (response) {
                    $('#modalVerificable').modal('hide');
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    })
                    var x = document.getElementById("botoncumplimiento");
                    $("#tablaverificable").load("verificable_compromiso.jsp?id_compromiso=" + id_compromiso);
                    $.post('administrar_acta.control?accion=existe_verificable', {
                        idcomp: id_compromiso
                    }, function (response) {
                        if (response) {
                            var resp = parseInt(response);
                            if (resp === 1) {
                                x.hidden = false;
                            } else if (resp === 0) {
                                x.hidden = true;
                            }
                        }
                    }, );
                    $("#tablaverificable").load("verificable_compromiso.jsp?id_compromiso=" + id_compromiso);
                } else {
                    iziToast.error({
                        title: 'Aviso',
                        message: 'Existio un error al intentar realizar esta acción',
                        position: 'topRight',
                    });
                }
            }
        });
        return false;
    });
});