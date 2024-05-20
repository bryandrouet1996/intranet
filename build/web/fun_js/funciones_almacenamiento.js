/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#formNuevoAlmacenamiento').submit(function (event) {
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
                    $('#modalNuevoAlmacenamiento').modal('hide');
                    location.href = "recurso_desarrollo.jsp";
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se ha registrado almacenamiento con exito!!',
                        position: 'topRight',
                    });
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'Ocurrió algo mientras se realizaba la acción',
                        position: 'topRight',
                    });
                }
            }
        });
        return false;
    });

    $('#formEditarAlmacenamiento').submit(function (event) {
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
                    $('#modalEditarAlmacenamiento').modal('hide');
                    location.href = "recurso_desarrollo.jsp";
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se ha actualizado informacion de almacenamiento con exito!!',
                        position: 'topRight',
                    });
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'Ocurrió algo mientras se realizaba la acción',
                        position: 'topRight',
                    });
                }
            }
        });
        return false;
    });
    
    $('#formNuevoRecurso').submit(function (event) {
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
                    $('#modalNuevoRecurso').modal('hide');
                    location.href = "listado_recursos.jsp";
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se ha registrado almacenamiento con exito!!',
                        position: 'topRight',
                    });
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'Ocurrió algo mientras se realizaba la acción',
                        position: 'topRight',
                    });
                }
            }
        });
        return false;
    });
});

function inhabilitarAlmacenamiento(id_almacenamiento) {
    swal({
        title: '¿Desea inhabilitar este almacenamiento?',
        text: "",
        icon: 'warning',
        buttons: {
            cancel: {
                visible: true,
                text: 'No, cancelar',
                className: 'btn btn-danger'
            },
            confirm: {
                text: 'Si, deseo inhabilitar',
                className: 'btn btn-success'
            }
        }
    }).then((willDelete) => {
        if (willDelete) {
            $.post('administrar_almacenamiento.control?accion=inhabilitar_almacenamiento', {
                txtial: id_almacenamiento
            }, function (responseText) {
                if (responseText) {
                    $("#seccion_almacenamientos").load(" #seccion_almacenamientos");
                    location.href = "recurso_desarrollo.jsp";
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se inhabilito almacen con exito',
                        position: 'topRight',
                    });
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'Ocurrió algo mientras se realizaba la acción',
                        position: 'topRight',
                    });
                }
            }, );
        } else {
            swal({
                title: 'No se realizo la acción',
                text: "",
                icon: 'info',
            })
        }
    });
}
