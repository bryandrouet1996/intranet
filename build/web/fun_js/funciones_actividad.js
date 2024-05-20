/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function crearActividad(id_usuario) {
    fecha1 = document.getElementById("txtfechaactividad");
    fecha2 = document.getElementById("txtfechalimite");

    var iact1 = $('#iact').val();

    var iu1 = id_usuario;
    var txtfechaactividad1 = fecha1.value;
    var txtfechalimite1 = fecha2.value;
    var txthorainicio1 = $('#txthorainicio').val();
    var txthorafin1 = $('#txthorafin').val();
    var txtindicador1 = $('#txtindicador').val();
    var comboavance1 = $('#comboavance').val();
    var combogrado1 = $('#combogrado').val();
    var txttarea1 = $('#txttarea').val();
    var txtrequiriente1 = $('#txtrequiriente').val();
    var areaobservacion1 = $('#areaobservacion').val();
    var areaherramienta1 = $('#areaherramienta').val();

    if ($('#sw1').prop('checked')) {

        var sw11 = $('#sw1').val();
    }
    if ($('#sw2').prop('checked')) {

        var sw21 = $('#sw2').val();

    }
    if ($('#sw3').prop('checked')) {

        var sw31 = $('#sw3').val();

    }
    if ($('#sw4').prop('checked')) {

        var sw41 = $('#sw4').val();

    }
    if ($('#sw5').prop('checked')) {
        var sw51 = $('#sw5').val();

    }
    if ($('#sw6').prop('checked')) {
        var sw61 = $('#sw6').val();

    }
    if ($('#sw7').prop('checked')) {
        var sw71 = $('#sw7').val();

    }
    if ($('#sw9').prop('checked')) {
        var sw91 = $('#sw9').val();

    }
    if ($('#sw10').prop('checked')) {
        var sw101 = $('#sw10').val();

    }
    if ($('#sw8').prop('checked')) {
        var sw81 = document.getElementById("otros1").value;
    }
    $.post('administrar_actividad.control?accion=registro', {
        iact: iact1,
        iu: iu1,
        txtfechaactividad: txtfechaactividad1,
        txtfechalimite: txtfechalimite1,
        txthorainicio: txthorainicio1,
        txthorafin: txthorafin1,
        txtindicador: txtindicador1,
        comboavance: comboavance1,
        combogrado: combogrado1,
        txttarea: txttarea1,
        txtrequiriente: txtrequiriente1,
        areaobservacion: areaobservacion1,
        areaherramienta: areaherramienta1,
        sw1: sw11,
        sw2: sw21,
        sw3: sw31,
        sw4: sw41,
        sw5: sw51,
        sw6: sw61,
        sw7: sw71,
        sw8: sw81,
        sw9: sw91,
        sw10: sw101
    }, function (responseText) {
        if (responseText) {
            var respuesta = parseInt(responseText);
            if (respuesta > 0) {
                iziToast.success({
                    title: 'Aviso',
                    message: 'Se guardaron los cambios realizados',
                    position: 'topRight',
                });
                iact = document.getElementById("iact");
                idact = document.getElementById("txtidact");
                iact.value = responseText.toString();
                idact.value = responseText.toString();
            } else if (respuesta === -1) {
                iziToast.warning({
                    title: 'Aviso',
                    message: 'La actividad que intenta registrar ya no se encuentra en el intervalo de fecha permitido',
                    position: 'topRight',
                });
            } else if (respuesta === -2) {
                iziToast.error({
                    title: 'Aviso',
                    message: 'No es posible registrar actividad, por favor verifique que este realizando un registro correcto',
                    position: 'topRight',
                });
            }
        }else{
            iziToast.error({
                    title: 'Aviso',
                    message: 'No es posible registrar actividad, por favor verifique que este realizando un registro correcto',
                    position: 'topRight',
                });
        }
    });

}

function crearActividad1(id_usuario) {
    fecha1 = document.getElementById("txtfechaactividad");
    fecha2 = document.getElementById("txtfechalimite");

    var iact1 = $('#iact').val();

    var iu1 = id_usuario;
    var txtfechaactividad1 = fecha1.value;
    var txtfechalimite1 = fecha2.value;
    var txthorainicio1 = $('#txthorainicio').val();
    var txthorafin1 = $('#txthorafin').val();
    var txtindicador1 = $('#txtindicador').val();
    var comboavance1 = $('#comboavance').val();
    var combogrado1 = $('#combogrado').val();
    var txttarea1 = $('#txttarea').val();
    var txtrequiriente1 = $('#txtrequiriente').val();
    var areaobservacion1 = $('#areaobservacion').val();
    var areaherramienta1 = $('#areaherramienta').val();

    if ($('#sw1').prop('checked')) {

        var sw11 = $('#sw1').val();
    }
    if ($('#sw2').prop('checked')) {

        var sw21 = $('#sw2').val();

    }
    if ($('#sw3').prop('checked')) {

        var sw31 = $('#sw3').val();

    }
    if ($('#sw4').prop('checked')) {

        var sw41 = $('#sw4').val();

    }
    if ($('#sw5').prop('checked')) {
        var sw51 = $('#sw5').val();

    }
    if ($('#sw6').prop('checked')) {
        var sw61 = $('#sw6').val();

    }
    if ($('#sw7').prop('checked')) {
        var sw71 = $('#sw7').val();

    }
    if ($('#sw8').prop('checked')) {

        var sw81 = document.getElementById("otros1").value;
    }

    if ($('#sw9').prop('checked')) {
        var sw91 = $('#sw9').val();
    }

    if ($('#sw10').prop('checked')) {
        var sw101 = $('#sw10').val();

    }
    $.post('administrar_actividad.control?accion=registro', {
        iact: iact1,
        iu: iu1,
        txtfechaactividad: txtfechaactividad1,
        txtfechalimite: txtfechalimite1,
        txthorainicio: txthorainicio1,
        txthorafin: txthorafin1,
        txtindicador: txtindicador1,
        comboavance: comboavance1,
        combogrado: combogrado1,
        txttarea: txttarea1,
        txtrequiriente: txtrequiriente1,
        areaobservacion: areaobservacion1,
        areaherramienta: areaherramienta1,
        sw1: sw11,
        sw2: sw21,
        sw3: sw31,
        sw4: sw41,
        sw5: sw51,
        sw6: sw61,
        sw7: sw71,
        sw8: sw81,
        sw9: sw91,
        sw10: sw101
    }, function (responseText) {
        if (responseText) {
            var respuesta = parseInt(responseText);
            if (respuesta > 0) {
                iziToast.success({
                    title: 'Aviso',
                    message: 'Se guardaron los cambios realizados',
                    position: 'topRight',
                });
                iact = document.getElementById("iact");
                idact = document.getElementById("txtidact");
                iact.value = responseText.toString();
                idact.value = responseText.toString();
                swal({
                    title: 'Registro exitoso,¿Desea registrar una nueva actividad?',
                    type: 'warning',
                    buttons: {
                        cancel: {
                            visible: true,
                            text: 'No, cancelar',
                            className: 'btn btn-danger'
                        },
                        confirm: {
                            text: 'Si, deseo registrar',
                            className: 'btn btn-success'
                        }
                    }
                }).then((willDelete) => {
                    if (willDelete) {
                        swal("Empiece su nuevo registro", {
                            buttons: {
                                confirm: {
                                    className: 'btn btn-success'
                                }
                            }
                        });
                        document.getElementById("txttarea").value = "";
                        document.getElementById("txtrequiriente").value = "";
                        document.getElementById("txtindicador").value = "";
                        document.getElementById("areaobservacion").value = "";
                        document.getElementById("iact").value = "0";
                        location.href = "registro_actividad.jsp?idact=0";
                    } else {
                        location.href = "listado_actividades.jsp";
                    }
                });
            } else if (respuesta === -1) {
                iziToast.warning({
                    title: 'Aviso',
                    message: 'La actividad que intenta registrar ya no se encuentra en el intervalo de fecha permitido',
                    position: 'topRight',
                });
            } else if (respuesta === -2) {
                iziToast.error({
                    title: 'Aviso',
                    message: 'No es posible registrar actividad, por favor verifique que este realizando un registro correcto',
                    position: 'topRight',
                });
            }
        }else{
            iziToast.error({
                    title: 'Aviso',
                    message: 'No es posible registrar actividad, por favor verifique que este realizando un registro correcto',
                    position: 'topRight',
                });
        }
    });

}

function eliminarActividad(id_actividad) {
    swal({
        title: 'Desea eliminar este registro?',
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
            $.post('administrar_actividad.control?accion=eliminar_actividad', {
                iact: id_actividad
            }, function (responseText) {
                if (responseText) {
                    $("#table-1").load(" #table-1");
                    $("#table-3").load(" #table-3");
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

$(document).ready(function () {
    $('#formevidencia').submit(function (event) {
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
                    $("#table-3").load(" #table-3");
                    swal("Mensaje", "Se adjunto su documento con exito!!", {
                        icon: "success",
                        buttons: {
                            confirm: {
                                className: 'btn btn-success'

                            }
                        }
                    }).then(function () {
                        var iact1 = $('#iact').val();
                        location.href = "registro_actividad.jsp?idact=" + iact1;

                    });
                } else {
                    swal("Mensaje", "Algo salio mal, verifique que este realizando un registro correcto!!", {
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

function eliminarEvidenciaActividad(id_evidencia) {
    $.post('administrar_actividad.control?accion=eliminar_evidencia', {
        iev: id_evidencia
    }, function (responseText) {
        if (responseText) {
            $("#table-3").load(" #table-3");
            swal("Mensaje", "Se ha eliminado adjunto con exito!!", {
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
}

function comentarActividad() {
    var id_usuario = $('#txtidusuario').val();
    var id_actividad = $('#txtidactividad').val();
    var comentario = $('#txtcomentario').val();
    var idu = $('#txtidu').val();
    var fecin = $('#txtinic').val();
    var fecfi = $('#txtfinf').val();
    var op = $('#txtop11').val();
    $.post('administrar_actividad.control?accion=registro_comentario', {
        iact: id_actividad,
        icom: id_usuario,
        descrip: comentario
    }, function (responseText) {
        if (responseText) {
            $("#table-1").load(" #table-1");
            swal("Mensaje", "Se comento la actividad seleccionada, el funcionario recibira un correo en este momento.", {
                icon: "success",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            }).then(function () {
                if (op == 0) {
                    location.href = "control_actividades.jsp?iu=" + idu + "&fecha_inicio=" + fecin + "&fecha_fin=" + fecfi + "&op=0";
                } else if (op == 1) {
                    location.href = "control_actividades.jsp?iu=0&fecha_inicio=" + fecin + "&fecha_fin=" + fecfi + "&op=1";
                }
            });
            $('#modalDetalle').modal('hide');
        } else {
            iziToast.error({
                title: 'Aviso',
                message: 'Existio un error al intentar guardar los cambios',
                position: 'topRight',
            });
        }
    }, );
}

function aprobarActividad() {
    var id_actividad = $('#txtidactividad').val();
    var idu = $('#txtidu').val();
    var fecin = $('#txtinic').val();
    var fecfi = $('#txtfinf').val();
    var op = $('#txtop11').val();
    $.post('administrar_actividad.control?accion=aprobar_actividad', {
        iact: id_actividad
    }, function (responseText) {
        if (responseText) {
            $("#table-1").load(" #table-1");
            swal("Mensaje", "Se aprobo actividad con exito!!", {
                icon: "success",
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                },
            }).then(function () {
                if (op == 0) {
                    location.href = "control_actividades.jsp?iu=" + idu + "&fecha_inicio=" + fecin + "&fecha_fin=" + fecfi + "&op=0";
                } else if (op == 1) {
                    location.href = "control_actividades.jsp?iu=0&fecha_inicio=" + fecin + "&fecha_fin=" + fecfi + "&op=1";
                }
            });
            $('#modalDetalle').modal('hide');
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

function aprobarActividadesDireccion(codigo_unidad) {
    swal({
        title: '¿Desea aprobar todas las actividades?',
        text: "Se aprobaran todas las activides de su dirección",
        type: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo aprobar',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_actividad.control?accion=aprobar_todo', {
                cunid: codigo_unidad
            }, function (responseText) {
                if (responseText) {
                    swal("Mensaje", "Se aprobo con exito!!", {
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
            swal("No se aprobaron los registros", {
                buttons: {
                    confirm: {
                        className: 'btn btn-success'
                    }
                }
            });
        }
    });
}

function generarReporteActividades(id_usuario) {
    var inicio = $('#txtini').val();
    var fin = $('#txtfin').val();
    window.open("reporteActividad.control?accion=actividades&txtidusuario2=" + id_usuario + "&txtin2=" + inicio + "&txtfi2=" + fin, "_blank");
}
