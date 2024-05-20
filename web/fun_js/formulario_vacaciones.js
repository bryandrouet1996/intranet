/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('#modalRechazo').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_usuario = button.data('iu');
    var id_permiso = button.data('ipe');
    var modal = $(this);
    modal.find('.modal-body #txtiu1').val(id_usuario);
    modal.find('.modal-body #txtidper1').val(id_permiso);
})

function seleccionMotivoPermiso() {
    // Get the checkbox
    var id_motivo = $('#combomotivo').val();
    var x = document.getElementById("divadju");
    if (id_motivo !=1 && id_motivo !=2&& id_motivo != 0) {
        x.style.display = "block";
    }else{
        x.style.display = "none";
    }
}

function seleccionPeriodo() {
    var periodo = $('#txtperiodo').val();
    var codigo_usuario = $('#txtcodiusu').val();
    obtenerDisponiblidadPerido(periodo,codigo_usuario);
}

function obtenerDisponiblidadPerido(periodo,codigo_usuario) {
    $.post('administrar_permiso.control?accion=disponibilidad_periodo', {
        perio: periodo,
        cod_usu: codigo_usuario
    }, function (responseText) {
        if (responseText) {
            var x = document.getElementById("txtdiaspendientes");
            x.value = responseText;
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

