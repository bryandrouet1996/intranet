/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function seleccionTipoPermisoHoras() {
    var id_tipo = parseInt($('#combotipo').val());
    var hora_inicio = document.getElementById("hora_inicio");
    var hora_fin = document.getElementById("hora_fin");
    var tiempo_solicitado = document.getElementById("tiempo_solicitado");
    var fecha_inicio = document.getElementById("fecha_inicio");
    var fecha_fin = document.getElementById("fecha_fin");
    var dia_solicitado = document.getElementById("dia_solicitado");
    var fecha_retorno = document.getElementById("fecha_retorno");
    if (id_tipo === 1) {
        hora_inicio.hidden = false;
        hora_fin.hidden = false;
        tiempo_solicitado.hidden = false;
        //fecha_inicio.hidden = true;
        fecha_fin.hidden = true;
        dia_solicitado.hidden = true;
        fecha_retorno.hidden = true;
        //colocada obligación
        $("#txtinicio").prop("required", true);
        $("#txtfin").prop("required", true);
        $("#txttiempo").prop("required", true);
        $("#txthoras").prop("required", true);
        $("#txtminutos").prop("required", true);
        //removida obligación
        $("#txtinicio1").removeAttr("required");
        $("#txtfin1").removeAttr("required");
        $("#txtingreso").removeAttr("required");
        $("#txtdiassolicitados").removeAttr("required");
        $("#txtdiashabiles").removeAttr("required");
        $("#txtdiasnohabiles").removeAttr("required");
        $("#txtdiasrecargo").removeAttr("required");
        $("#txtdiasdescuento").removeAttr("required");
    } else if (id_tipo === 2) {
        hora_inicio.hidden = true;
        hora_fin.hidden = true;
        tiempo_solicitado.hidden = true;
        fecha_inicio.hidden = false;
        fecha_fin.hidden = false;
        dia_solicitado.hidden = false;
        fecha_retorno.hidden = false;
        //colocada obligación
        $("#txtinicio1").prop("required", true);
        $("#txtfin1").prop("required", true);
        $("#txtingreso").prop("required", true);
        $("#txtdiassolicitados").prop("required", true);
        $("#txtdiashabiles").prop("required", true);
        $("#txtdiasnohabiles").prop("required", true);
        $("#txtdiasrecargo").prop("required", true);
        $("#txtdiasdescuento").prop("required", true);
        $("#txtinicio").removeAttr("required");
        $("#txtfin").removeAttr("required");
        $("#txttiempo").removeAttr("required");
        $("#txthoras").removeAttr("required");
        $("#txtminutos").removeAttr("required");
    }
}

function obtenerJustificanteMotivoID(id_motivo) {
    $.post('administrar_permiso.control?accion=justificante_motivo', {
        idmov: id_motivo
    }, function (responseText) {
        if (responseText) {
            var x = document.getElementById("lbladjunto");
            x.innerHTML = responseText;
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

$('#modaldetallepermiso').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var fecha_solicitud = button.data('fecsoli');
    var motivo = button.data('motivo');
    var hora_inicio = button.data('horai');
    var hora_fin = button.data('horaf');
    var tiempo = button.data('tiempo');
    var observacion = button.data('observacion');
    var modal = $(this);
    modal.find('.modal-body #txtfechasolicitud').val(fecha_solicitud);
    modal.find('.modal-body #combomotivo').val(motivo);
    modal.find('.modal-body #txtinicio').val(hora_inicio);
    modal.find('.modal-body #txtfin').val(hora_fin);
    modal.find('.modal-body #txttiempo').val(tiempo);
    modal.find('.modal-body #areaobservacion').val(observacion);
})

$('#modaldetallepermisodir').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var nombre = button.data('nombre');
    var cargo = button.data('cargo');
    var unidad = button.data('direccion');
    var modalidad = button.data('modalidad');
    var fecha_solicitud = button.data('fecsoli');
    var motivo = button.data('motivo');
    var hora_inicio = button.data('horai');
    var hora_fin = button.data('horaf');
    var tiempo = button.data('tiempo');
    var observacion = button.data('observacion');
    var modal = $(this);
    modal.find('.modal-body #txtnombreservidor').val(nombre);
    modal.find('.modal-body #txtcargoservidor').val(cargo);
    modal.find('.modal-body #txtunidadservidor').val(unidad);
    modal.find('.modal-body #txtmodalidadservidor').val(modalidad);
    modal.find('.modal-body #txtfechasolicitud').val(fecha_solicitud);
    modal.find('.modal-body #combomotivo').val(motivo);
    modal.find('.modal-body #txtinicio').val(hora_inicio);
    modal.find('.modal-body #txtfin').val(hora_fin);
    modal.find('.modal-body #txttiempo').val(tiempo);
    modal.find('.modal-body #areaobservacion').val(observacion);
})

$('#modalRechazo').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_permiso = button.data('ipe');
    var modal = $(this);
    modal.find('.modal-body #txtidper1').val(id_permiso);
})