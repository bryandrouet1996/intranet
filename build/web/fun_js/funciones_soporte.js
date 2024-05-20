/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formsoporte').submit(function (event) {
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
                    //$("#table-3").load(" #table-3");
                    $('#registroSolicitudSistema').modal('hide');
                    swal("Mensaje", "Se ha realizado registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "helpdesk.jsp";
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

    $('#formEditarSoportes').submit(function (event) {
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
                    //$("#table-3").load(" #table-3");
                    $('#editarSolicitudSistema').modal('hide');
                    swal("Mensaje", "Se actualizo registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "helpdesk.jsp";
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
    
    $('#formDiagnosticoSoporte').submit(function (event) {
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
                    $('#modalDiagnosticoSolicitud').modal('hide');
                    swal("Mensaje", "Se registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "mis_soportes.jsp";
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
    
    $('#formCalificarSoporte').submit(function (event) {
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
                    $('#modalCalificarSolicitud').modal('hide');
                    swal("Mensaje", "Se registro de manera exitosa!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "helpdesk.jsp";
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

function eliminarSolicitudSoporte(id_solicitud) {
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
            $.post('administrar_ticket.control?accion=eliminar_solicitud', {
                isop: id_solicitud
            }, function (responseText) {
                if (responseText) {
                    $("#table-1").load(" #table-1");
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

function asignarSoporteTecnicoID(id_tecnico) {
    var id_solicitud = $('#idsoli').val();
    var id_administrador = $('#idadmin').val();
    swal({
        title: 'Confirmar asignación',
        text: "¿Desea asignar a este técnico?",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Sí, deseo asignarlo',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_ticket.control?accion=asignar_solicitud', {
                isop: id_solicitud,
                idad: id_administrador,
                idtec: id_tecnico
            }, function (responseText) {
                if (responseText) {
                    $('#modalAsignacion').modal('hide');
                    swal("Mensaje", "Se asigno técnico con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "administrar_tickets.jsp";
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
            swal("El técnico no fue asignado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function aceptarSoporteTecnicoID(id_solicitud, id_tecnico) {
    swal({
        title: '¿Desea aceptar este soporte?',
        text: "Una vez aceptado ya no se podrá rechazar",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo aceptarlo',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_ticket.control?accion=atender_solicitud', {
                isop: id_solicitud,
                idtec: id_tecnico
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se acepto soporte con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "mis_soportes.jsp";
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
            swal("Se cancelo a acción a realizar", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function cerrarSoporteTecnicoID(id_solicitud, id_tecnico) {
    swal({
        title: '¿Desea finalizar este soporte?',
        text: "",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo finalizarlo',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_ticket.control?accion=cerrar_solicitud', {
                isop: id_solicitud,
                idtec: id_tecnico
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se finalizó soporte con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "mis_soportes.jsp";
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
            swal("Se cancelo a acción a realizar", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function rechazoSoporteTecnicoID(id_solicitud, id_tecnico) {
    swal({
        title: '¿Desea rechazar este soporte?',
        text: "Una vez se hecho esto no estara visible en su bandeja",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo rechazarlo',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_ticket.control?accion=rechazar_solicitud', {
                isop: id_solicitud,
                idtec: id_tecnico
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se rechazo soporte con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        },
                    }).then(function () {
                        location.href = "mis_soportes.jsp";
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
            swal("Se cancelo a acción a realizar", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}


