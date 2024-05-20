/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('#modalDetalle').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var fecha = button.data('fecha');
    var tarea = button.data('tarea');
    var hora_inicio = button.data('horai');
    var hora_fin = button.data('horaf');
    var requirente = button.data('requirente');
    var avance = button.data('avance');
    var herramienta = button.data('herramienta');
    var observacion = button.data('observacion');
    var id_actividad = button.data('idact');
    var modal = $(this);
    modal.find('.modal-body #txtfecha').val(fecha);
    modal.find('.modal-body #txttarea').val(tarea);
    modal.find('.modal-body #txthorainicio').val(hora_inicio);
    modal.find('.modal-body #txthorafin').val(hora_fin);
    modal.find('.modal-body #txtrequirente').val(requirente);
    modal.find('.modal-body #txtavance').val(avance);
    modal.find('.modal-body #txtherramienta').val(herramienta);
    modal.find('.modal-body #txtobservacion').val(observacion);
    modal.find('.modal-body #txtidactividad').val(id_actividad);
})

$('#modalVer').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var fecha = button.data('fecha');
    var tarea = button.data('tarea');
    var hora_inicio = button.data('horai');
    var hora_fin = button.data('horaf');
    var requirente = button.data('requirente');
    var avance = button.data('avance');
    var herramienta = button.data('herramienta');
    var observacion = button.data('observacion');
    var id_actividad = button.data('idact');
    var modal = $(this);
    modal.find('.modal-body #txtfecha1').val(fecha);
    modal.find('.modal-body #txttarea1').val(tarea);
    modal.find('.modal-body #txthorainicio1').val(hora_inicio);
    modal.find('.modal-body #txthorafin1').val(hora_fin);
    modal.find('.modal-body #txtrequirente1').val(requirente);
    modal.find('.modal-body #txtavance1').val(avance);
    modal.find('.modal-body #txtherramienta1').val(herramienta);
    modal.find('.modal-body #txtobservacion1').val(observacion);
    modal.find('.modal-body #txtidactividad1').val(id_actividad);
})

$('#modalObservacion').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_usuario = button.data('usu');
    var id_actividad = button.data('act');
    var modal = $(this);
    modal.find('.modal-body #txtidusuario').val(id_usuario);
    modal.find('.modal-body #txtidactividad').val(id_actividad);
})

$('#modalComentario').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var usuario = button.data('usu');
    var fecha = button.data('fec');
     var comentario = button.data('com');
    var modal = $(this);
    modal.find('.modal-body #txtcomentario1').val(comentario);
    modal.find('.modal-body #txtusuariocom1').val(usuario);
    modal.find('.modal-body #txtfechareg1').val(fecha);
})

