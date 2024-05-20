/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#formhoras').submit(function (event) {
        iziToast.info({
            title: 'Registrando',
            message: 'Se está procesando la solicitud',
            position: 'topRight',
        });
        var btn = document.getElementById("botonguardarsoli");
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
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "permiso_horas.jsp";
                    });
                } else {
                    iziToast.error({
                        title: 'Aviso',
                        message: 'Existio un error al intentar realizar esta acción',
                        position: 'topRight',
                    });
                    btn.hidden = false;
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
            $.post('administrar_permiso.control?accion=eliminar_horas', {
                id_permiso: id_permiso
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
                        location.href = "permiso_horas.jsp";
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

function aprobarSolicitudHoras(id_solicitud, id_usuario) {
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
            $.post('administrar_permiso.control?accion=aprobar_horas', {
                ipe: id_solicitud,
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha aprobó registro con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "horas_general.jsp";
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

function revisarSolicitudHoras(id_solicitud, id_usuario) {
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
                text: 'Sí, deseo revisarla',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_permiso.control?accion=revisar_horas', {
                ipe: id_solicitud,
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Revisión exitosa", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "horas_direccion.jsp";
                    });
                } else {
                    swal("Mensaje", "Ocurrió un error", {
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
            swal("Se canceló la acción", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}