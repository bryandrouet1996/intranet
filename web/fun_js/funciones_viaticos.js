/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formviatico').submit(function (event) {
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
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se guardaron los cambios realizados',
                        position: 'topRight',
                    });
                    document.getElementById("txtviatico").value = response;
                    document.getElementById("txtidviatico").value = response;
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

$(document).ready(function () {
    $('#formtransporte').submit(function (event) {
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
                    swal("Mensaje", "Se registro destino con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = response;
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

});

$(document).ready(function () {
    $('#formtransporte1').submit(function (event) {
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
                    $("#table-2").load(" #table-2");
                    $("#botonfinalizar").load(" #botonfinalizar");
                    $('#modalTransporte').modal('hide');
                    swal("Mensaje", "Se registro destino con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    })

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

function finalizarRegistroViatico() {
    var id_viatico = $('#txtidviatico').val();
    swal({
        title: '¡Se realizo registro de manera exitosa!',
        text: "¿Desea generar el documento de su registro?",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, en otro momento',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo generarlo',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            window.open("reporte_viatico.control?tipo=licencia&id_viatico=" + id_viatico, "_blank");
            location.href = "listado_viaticos.jsp";
        } else {
            swal("No se genero documento", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            }).then(function () {
                location.href = "listado_viaticos.jsp";
            });
        }
    });
}