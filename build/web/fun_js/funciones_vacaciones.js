/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function diaIngreso() {
    iziToast.info({
        title: 'Validando',
        message: 'Se están procesando los datos de sus vacaciones',
        position: 'topRight',
    });
    document.getElementById("botonguardar").hidden = true;
    var inicio = $('#txtinicio').val();
    var fin = $('#txtfin').val();
    if (inicio > fin) {
        swal("Aviso", "   La fecha de inicio no puede ser mayor a la fecha de finalización!!", {
            icon: "error",
            buttons: {
                confirm: {
                    className: 'btn btn-danger'
                }
            },
        });
        document.getElementById("txtinicio").value = fin;
        document.getElementById("txtingreso").value = "";
        document.getElementById("txtdiassolicitados").value = "";
        document.getElementById("txtdiasrecargo").value = "";
        document.getElementById("botonguardar").hidden = true;
    } else {
        $.post('administrar_permiso.control?accion=fecha_ingreso', {
            txtinicio: inicio,
            txtfin: fin
        }, function (responseText) {
            if (responseText) {
                document.getElementById("txtingreso").value = responseText;
                diasSolicitadosRecargo();
            }
        }, );
    }
}

function diasSolicitadosRecargo() {
    var inicio = $('#txtinicio').val();
    var fin = $('#txtfin').val();
    var idusu = $('#txtidusuario').val();
    var motivo = $('#combomotivo').val();
    $.post('administrar_permiso.control?accion=dias_solicitados', {
        txtinicio: inicio,
        txtfin: fin,
        idusu: idusu,
        motivo: motivo
    }, function (responseText) {
        if (responseText) {
            var variables = responseText.split(",");
            if(variables[3]==-1){
                iziToast.error({
                    title: 'Aviso',
                    message: 'Los días solicitados sobrepasan sus días disponibles',
                    position: 'topRight',
                });
                document.getElementById("txtdiassolicitados").value = "";
                document.getElementById("txtdiasrecargo").value = "";
                document.getElementById("botonguardar").hidden = true;
            }else{
                document.getElementById("txtdiassolicitados").value = variables[0];
                document.getElementById("txtdiasnohabiles").value = variables[1];
                document.getElementById("txtdiashabiles").value = variables[2];
                document.getElementById("txtdiasrecargo").value = variables[3];
                document.getElementById("txtdiasdescuento").value = variables[4];
                document.getElementById("botonguardar").hidden = false;
                iziToast.success({
                    title: 'Validado',
                    message: 'Los datos de sus vacaciones fueron validados',
                    position: 'topRight',
                });
            }
        }
    }, );
}

$(document).ready(function () {
    $('#formvacaciones').submit(function (event) {
        iziToast.info({
            title: 'Registrando',
            message: 'Se está procesando la solicitud',
            position: 'topRight',
        });
        var btn = document.getElementById("botonguardar");
        btn.hidden = true;
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
                    var respuesta = parseInt(response);
                    if (respuesta === 1) {
                        swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                            icon: "success",
                            buttons: {
                                confirm: {
                                    className: 'btn btn-success'
                                }
                            },
                        }).then(function () {
                            location.href = "permiso_vacaciones.jsp";
                        });
                    } else if (respuesta === -1) {
                        iziToast.error({
                            title: 'Aviso',
                            message: 'Existió un error al intentar realizar esta acción',
                            position: 'topRight',
                        });
                        btn.hidden = false;
                    } else if (respuesta === -2) {
                        iziToast.error({
                            title: 'Aviso',
                            message: 'Por favor complete el formulario',
                            position: 'topRight',
                        });
                        btn.hidden = false;
                    }
                } else {
                    iziToast.error({
                        title: 'Aviso',
                        message: 'Existió un error al intentar realizar esta acción',
                        position: 'topRight',
                    });
                    btn.hidden = false;
                }
            }
        });
        return false;
    });
});

$(document).ready(function () {
    $('#formrechazo').submit(function (event) {
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
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "vacaciones_direccion.jsp";
                    });
                    $('#modalrechazo').modal('hide');
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
    $('#formrechazoG').submit(function (event) {
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
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "vacaciones_general.jsp";
                    });
                    $('#modalrechazo').modal('hide');
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

function eliminarPermiso(id_permiso) {
    swal({
        title: '¿Desea eliminar este registro?',
        text: "Una vez eliminado no se podra recuperar",
        type: 'warning',
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
            $.post('administrar_permiso.control?accion=eliminar_vacacion', {
                ipe: id_permiso
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha eliminado registro con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "permiso_vacaciones.jsp";
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

function aprobarSolicitudVacaciones(id_solicitud, id_usuario) {
    swal({
        title: '¿Desea aprobar esta solicitud?',
        text: "",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo aprobarla',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_permiso.control?accion=aprobar_vacacion', {
                ipe: id_solicitud,
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    var respuesta = parseInt(responseText);
                    if (respuesta===1){
                        swal("Mensaje", "Se ha aprobó registro con exito!!", {
                            icon: "success",
                            buttons: {
                                confirm: {
                                    className: 'btn btn-success'
                                }
                            },
                        }).then(function () {
                            location.href = "vacaciones_general.jsp";
                        });
                    }else if(respuesta===-1){
                        swal("Mensaje", "No se registro información en base oracle!!", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    })
                    }else if(respuesta===-2){
                        swal("Mensaje", "No se realizo actualización de estado de solicitud!!", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    })
                    }else if(respuesta===-3){
                        swal("Mensaje", "No se realizo registro de aprobación de solicitud!!", {
                        icon: "warning",
                        buttons: {
                            confirm: {
                                className: 'btn btn-warning'
                            }
                        },
                    })
                    }
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
            swal("Se cancelo la acción", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function revisarSolicitudVacaciones(id_solicitud, id_usuario) {
    swal({
        title: '¿Desea revisar esta solicitud?',
        text: "",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo revisarla',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_permiso.control?accion=revisar_vacacion', {
                ipe: id_solicitud,
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha revisó registro con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "vacaciones_direccion.jsp";
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
            swal("Se cancelo la acción", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}


