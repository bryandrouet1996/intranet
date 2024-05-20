/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formconvocatoria').submit(function (event) {
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
                            window.open("reporte_acta.control?tipo=reporte_convocatoria&iconv=" + response, "_blank");
                            location.href = "listado_convocatorias.jsp";
                        } else {
                            swal("No se genero documento", {
                                buttons: {
                                    confirm: {
                                        className: 'btn btn-success'
                                    }
                                }
                            }).then(function () {
                                location.href = "listado_convocatorias.jsp";
                            });
                        }
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
    $('#formacta').submit(function (event) {
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
                        message: 'Guardando......',
                        position: 'topRight',
                    });
                    iact = document.getElementById("txtidacta");
                    idact = document.getElementById("txtacta");
                    iact.value = response.toString();
                    idact.value = response.toString();
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
    $('#formcompromiso1').submit(function (event) {
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
                    $('#modalCompromiso').modal('hide');
                    swal("Mensaje", "Se registro compromiso con exito!!", {
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

$(document).ready(function () {
    $('#formcompromiso').submit(function (event) {
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
                    swal("Mensaje", "Se registro compromiso con exito!!", {
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

function crearActa(id_convocatoria) {
    fecha_1 = document.getElementById("txtfechaacta");
    fecha_2 = document.getElementById("txtfechaconvocatoria");
    asistente = document.getElementById("comboasistentes");
    var iact = $('#txtidacta').val();
    var txtfechaacta = fecha_1.value;
    var txtfechaconvocatoria = fecha_2.value;
    var txtasunto = $('#txtasunto').val();
    var combomedio = $('#combomedio').val();
    var txthorainicio = $('#txthorainicio').val();
    var txthorafin = $('#txthorafin').val();
    var txtlugar = $('#txtlugar').val();
    var areaorden = $('#areaobservacion').val();
    var areadesarrollo = $('#areadesarrollo').val();
    $.post('administrar_acta.control?accion=finalizar_acta', {
        iact: iact,
        ic: id_convocatoria,
        txtfechaacta: txtfechaacta,
        txtfechaconvocatoria: txtfechaconvocatoria,
        txtasunto: txtasunto,
        combomedio: combomedio,
        txthorainicio: txthorainicio,
        txthorafin: txthorafin,
        txtlugar: txtlugar,
        areaorden: areaorden,
        areadesarrollo: areadesarrollo
    }, function (responseText) {
        if (responseText) {
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
                    window.open("reporte_acta.control?tipo=reporte_acta&iact=" + responseText, "_blank");
                    location.href = "listado_actas.jsp";
                } else {
                    swal("No se genero documento", {
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        }
                    }).then(function () {
                        location.href = "listado_actas.jsp";
                    });
                }
            });

        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    });

}

function editarConvocatoria(id_convocatoria) {
    swal({
        title: '¿Desea editar convocatoria?',
        text: "Usted esta a punto de modificar el contenido de la convocatoria que ya fué notificada, esta acción \n\
        implica que se notificará a los participantes otra vez con los cambios realizados, en caso que la \n\
        notificación haya sido realizada fisicamente deberá ser nuevamente impresa para ser reemplazada.",
        icon: "warning",
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, editar de todas formas',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            location.href = "registro_convocatoria.jsp?ic=" + id_convocatoria;
        } else {
            swal("Se cancelo la edición de convocatoria", {
                icon: "info",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function anularConvocatoria() {
    var iconv = $('#txtidconvocatoria').val();
    var txtrazon = $('#txtrazon').val();
    swal({
        title: '¿Desea anular convocatoria?',
        text: "Una vez anulada la convocatoria esta será notificada a los participantes y no podrá ser modificada.",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo anular',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_acta.control?accion=anular_convocatoria', {
                iconv: iconv,
                txtrazon: txtrazon
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se anulo convocatoria con éxito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "listado_convocatorias.jsp";
                    });
                } else {
                    swal("Mensaje", "Algo salio mal!!", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    })
                }
            }, );
        } else {
            swal("La convocatoria no fue anulada", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function eliminarCompromisoActa(id_compromiso) {
    swal({
        title: '¿Desea eliminar este registro?',
        text: "Una vez eliminado no se podra recuperar",
        icon: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo eliminar',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_acta.control?accion=eliminar_compromiso', {
                icomp: id_compromiso
            }, function (responseText) {
                if (responseText) {
                    $("#table-2").load(" #table-2");
                    swal("Mensaje", "Se ha eliminado registro con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    })
                } else {
                    swal("Mensaje", "Algo salio mal!!", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    })
                }
            }, );
        } else {
            swal("Su registro no fue eliminado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}


function notificacionFuncionarios() {
    iziToast.success({
        title: 'Aviso',
        message: 'Por favor espere estamos notificando a los funcionarios',
        position: 'topRight',
    });
}