/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function crearPrueba(){
    alert('holaaaaaaaaa');
}


function cambiarContrasenaUsuario() {
    var contrasena_actual = $('#txtcontraactual').val();
    var contrasena_nueva = $('#txtnuevacontra').val();
    var repetir_nueva = $('#txtrepitacontra').val();
    var id_usuario = $('#txtidusuario').val();
    $.post('administrar_usuario.control?accion=cambioClave', {
        idUsuario: id_usuario,
        txtcontraactual: contrasena_actual,
        txtnuevacontra: contrasena_nueva,
        txtrepitacontra: repetir_nueva
    }, function (responseText) {
        if (responseText) {
            iziToast.success({
                title: 'Aviso',
                message: 'Se guardaron los cambios realizados',
                position: 'topRight',
            });
            document.getElementById("txtcontraactual").value = "";
            document.getElementById("txtnuevacontra").value = "";
            document.getElementById("txtrepitacontra").value = "";
        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    }, );
}

function registroCalendario() {
    var calendario = $('#txtcalendario').val();
    var id_usuario = $('#txtidusuario11').val();
    $.post('administrar_usuario.control?accion=registro_calendario', {
        idUsuario: id_usuario,
        txtcalendario: calendario
    }, function (responseText) {
        if (responseText) {
            $("#tablacalendario").load(" #tablacalendario");
            iziToast.success({
                title: 'Aviso',
                message: 'Se guardaron los cambios realizados',
                position: 'topRight',
            });
            document.getElementById("txtcalendario").value = "";
        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    }, );
}

function cambiarClaveMail(id_usuario) {
    var clave_mail = $('#txtclavemail').val();
    $.post('administrar_usuario.control?accion=modificarWebmail', {
        idUsuario: id_usuario,
        txtclavemail: clave_mail
    }, function (responseText) {
        if (responseText) {
            iziToast.success({
                title: 'Aviso',
                message: 'Se guardaron los cambios realizados',
                position: 'topRight',
            });
        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    }, );
}

function eliminarCalendario(id_calendario) {
    swal({
        title: '¿Desea eliminar este calendario?',
        text: "Al eliminar el calendario no tendrá acceso a los eventos del mismo",
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
            $.post('administrar_usuario.control?accion=eliminar_calendario', {
                ic: id_calendario
            }, function (responseText) {
                if (responseText) {
                    $("#tablacalendario").load(" #tablacalendario");
                    swal("Mensaje", "El calendario seleccionado ha sido eliminado!!", {
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
            swal("El calendario no ha sido eliminado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function cambiarChadID(id_usuario) {
    var swit = $('#swichtelegram').val();
    var chat_id = $('#txtchat').val();
    $.post('administrar_usuario.control?accion=modificarTelegram', {
        idUsuario: id_usuario,
        swichtelegram: swit,
        txtchat: chat_id
    }, function (responseText) {
        if (responseText) {
            iziToast.success({
                title: 'Aviso',
                message: 'Se guardaron los cambios realizados',
                position: 'topRight',
            });
        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    }, );
}

$(document).ready(function () {
    $('#formfoto').submit(function (event) {
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
                    $("#foto_us").load(" #foto_us");
                    swal("Mensaje", "Se cambio la foto de su cuenta con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'
                            }
                        }
                    }).then(function () {
                        location.href = "configurar_cuenta.jsp";
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
            }
        });
        return false;
    });

});

function bloquearUsuario(id_usuario) {
    swal({
        title: 'Desea bloquear este usuario?',
        text: "Al bloquear el usuario este no podra hacer uso de su cuenta",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo bloquear',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_usuario.control?accion=bloqueo', {
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha bloqueado al usuario con exito!!", {
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
            swal("El usuario no ha sido bloqueado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function desbloquearUsuario(id_usuario) {
    swal({
        title: 'Desea desbloquear este usuario?',
        text: "Al desbloquear el usuario este podra hacer uso de su cuenta",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo desbloquear',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_usuario.control?accion=desbloqueo', {
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha desbloqueado al usuario con exito!!", {
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
            swal("El usuario no ha sido desbloqueado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function reseteoUsuario(id_usuario) {
    swal({
        title: 'Desea resetear la clave de este usuario?',
        text: "La clave regresara a la por defecto",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo resetear',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_usuario.control?accion=reseteo', {
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se ha reseteado la clave del usuario!!", {
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
            swal("No se reseteo la clave del usuario", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function eliminarUsuario(id_usuario) {
    swal({
        title: 'Desea eliminar este usuario?',
        text: "Al eliminar el usuario este no podra hacer uso de su cuenta",
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
            $.post('administrar_usuario.control?accion=eliminar', {
                iu: id_usuario
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "El usuario seleccionado ha sido eliminado!!", {
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
            swal("El usuario no ha sido eliminado", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function modificarUsuario(id_usuario) {
    var nombre = $('#txtnombre').val();
    var apellido = $('#txtapellido').val();
    var cedula = $('#txtcedula').val();
    var correo = $('#txtcorreo').val();
    var unidad = document.getElementById("combounidad").value;
    var cargo = document.getElementById("combocargo").value;
    var tipo_usuario = document.getElementById("combotipo").value;
    $.post('administrar_usuario.control?accion=modificarinfo', {
        iu: id_usuario,
        txtnombre: nombre,
        txtapellido: apellido,
        txtcedula: cedula,
        txtcorreo: correo,
        combounidad: unidad,
        combocargo: cargo,
        combotipo: tipo_usuario
    }, function (responseText) {
        if (responseText) {
            swal("Mensaje", "Se actualizo la información del usuario!!", {
                icon: "success",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                },
            }).then(function () {
                location.href = "administrar_usuarios.jsp";
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
}

function crearUsuario() {
    var nombre = $('#txtnombre').val();
    var apellido = $('#txtapellido').val();
    var cedula = $('#txtcedula').val();
    var correo = $('#txtcorreo').val();
    var unidad = document.getElementById("combounidad").value;
    var cargo = document.getElementById("combocargo").value;
    var tipo_usuario = document.getElementById("combotipo").value;
    var fecha_nacimiento = $('#txtfechanacimiento').val();
    $.post('administrar_usuario.control?accion=registrar', {
        txtnombre: nombre,
        txtapellido: apellido,
        txtcedula: cedula,
        txtcorreo: correo,
        combounidad: unidad,
        combocargo: cargo,
        combotipo: tipo_usuario,
        txtfechanacimiento: fecha_nacimiento
    }, function (responseText) {
        var response = parseInt(responseText);
        if (response === 1) {
            swal("Usuario creado", "Se creó la cuenta de usuario", {
                icon: "success",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                },
            }).then(function () {
                location.href = "administrar_usuarios.jsp";
            });
        } else if (response === -1) {
            swal("Usuario inexistente", "No se encontró el usuario en ERP-CABILDO", {
                icon: "error",
                buttons: {
                    confirm: {
                        className: 'btn btn-warning'
                    }
                },
            })
        } else if (response === -2) {
            swal("Usuario existente", "Ya existe un usuario registrado con la cédula ingresada", {
                icon: "warning",
                buttons: {
                    confirm: {
                        className: 'btn btn-warning'
                    }
                },
            })
        } else if (response === -3) {
            swal("Error", "Ocurrió un error al registrar", {
                icon: "error",
                buttons: {
                    confirm: {
                        className: 'btn btn-warning'
                    }
                },
            })
        }
    }, );
}



$(document).ready(function () {
    $('#formfirma').submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr('action'),
            type: $(this).attr('method'),
            data: new FormData(this),
            contentType: false,
            cache: false,
            processData: false,
            success: function (response) {
                $('#modalfirma').modal('hide');
                $("#tablafirma").load(" #tablafirma");
                swal("Mensaje", "Se cambio la firma de su cuenta con exito!!", {
                    icon: "success",
                    buttons: {
                        confirm: {
                            className: 'btn btn-success'
                        }
                    }
                }).then(function () {
                    location.href = "configurar_cuenta.jsp";
                });
            }
        });
        return false;
    });

});

     
