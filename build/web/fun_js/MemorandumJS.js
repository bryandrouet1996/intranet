/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $('#formRegistroMemorandumCoordinador').submit(function (event) {
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
                    var opcion = parseInt(response);
                    if (opcion === 1) {
                        iziToast.success({
                            title: 'Aviso',
                            message: 'Se ha realizado el registro de manera exitosa y funcionario ha sido notificado vía correo',
                            position: 'topRight',
                        });
                        setTimeout(() => {
                            location.href = "memoradums_coordinador.jsp";
                        }, 2000);
                    }
                    if (opcion === -1) {
                        iziToast.warning({
                            title: 'Aviso',
                            message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                            position: 'topRight',
                        });
                    }
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                        position: 'topRight',
                    })
                }
            },

        });
        return false;
    });
});

$(document).ready(function () {
    $('#formRegistroMemorandum').submit(function (event) {
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
                    var opcion = parseInt(response);
                    if (opcion === 1) {
                        iziToast.success({
                            title: 'Aviso',
                            message: 'Se ha realizado el registro de manera exitosa y funcionario ha sido notificado vía correo',
                            position: 'topRight',
                        });
                        setTimeout(() => {
                            location.href = "memoradums_direccion.jsp";
                        }, 2000);
                    }
                    if (opcion === -1) {
                        iziToast.warning({
                            title: 'Aviso',
                            message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                            position: 'topRight',
                        });
                    }
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                        position: 'topRight',
                    })
                }
            },

        });
        return false;
    });
});

$(document).ready(function () {
    $('#formCompletarDirector').submit(function (event) {
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
                    var opcion = parseInt(response);
                    if (opcion === 1) {
                        iziToast.success({
                            title: 'Aviso',
                            message: 'Se ha realizado el registro de manera exitosa!',
                            position: 'topRight',
                        });
                        setTimeout(() => {
                            location.href = "memoradums_direccion.jsp";
                        }, 2000);
                    }
                    if (opcion === -1) {
                        iziToast.warning({
                            title: 'Aviso',
                            message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                            position: 'topRight',
                        });
                    }
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                        position: 'topRight',
                    })
                }
            },

        });
        return false;
    });
});

$(document).ready(function () {
    $('#formCompletar').submit(function (event) {
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
                    var opcion = parseInt(response);
                    if (opcion === 1) {
                        iziToast.success({
                            title: 'Aviso',
                            message: 'Se ha realizado el registro de manera exitosa!',
                            position: 'topRight',
                        });
                        setTimeout(() => {
                            location.href = "listado_memorandums.jsp";
                        }, 2000);
                    }
                    if (opcion === -1) {
                        iziToast.warning({
                            title: 'Aviso',
                            message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                            position: 'topRight',
                        });
                    }
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se pudo realizar la acción solicitada, intente nuevamente',
                        position: 'topRight',
                    })
                }
            },

        });
        return false;
    });
});


function CompletarMemorandumID(id_memorandum) {
    swal({
        title: '¿Ya se ha completado esta actividad?',
        icon: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, se ha completado',
                className: 'btn btn-primary'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('memorandum.ct?opcion=completar', {
                txtidmemorandum: id_memorandum
            }, function (response) {
                if (response) {
                    $("#myTabContent").load(" #myTabContent");
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se ha realizado el cambio de estado del memorandum a completado',
                        position: 'topRight',
                    });
                    setTimeout(() => {
                        location.href = "listado_memorandums.jsp";
                    }, 2000);
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se realizo la acción solicitada, intente nuevamente por favor',
                        position: 'topRight',
                    })
                }
            }, );
        } else {
            swal("No se realizo la acción", {
                icon: 'info',
                buttons: {
                    confirm: {
                        className: 'btn btn-primary'
                    }
                }
            });
        }
    });
}

function EliminarMemorandumID(id_memorandum) {
    swal({
        title: '¿Desea eliminar el memorandum seleccionado?',
        icon: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, eliminar',
                className: 'btn btn-primary'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('memorandum.ct?opcion=eliminar', {
                txtidmemorandum: id_memorandum
            }, function (response) {
                if (response) {
                    $("#myTabContent").load(" #myTabContent");
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se ha realizado acción de manera exitosa.',
                        position: 'topRight',
                    });
                    setTimeout(() => {
                        location.href = "memoradums_direccion.jsp";
                    }, 2000);
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se realizo la acción solicitada, intente nuevamente por favor',
                        position: 'topRight',
                    })
                }
            }, );
        } else {
            swal("No se realizo la acción", {
                icon: 'info',
                buttons: {
                    confirm: {
                        className: 'btn btn-primary'
                    }
                }
            });
        }
    });
}

$('#modalCompletar').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var idusu = button.data('memorandum');
    var modal = $(this);
    modal.find('.modal-body #txtmemorandum').val(idusu);
})


