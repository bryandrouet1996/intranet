/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cerrar(){
    location.href = 'helpdesk.jsp';
}

function formularioRegistro(id_usuario) {
    $('#registroSolicitudSistema').modal('show');
    $("#formSoporte").load("registro_soporte.jsp?iu=" + id_usuario);
}

function formularioEditar(id_soporte) {
    $('#editarSolicitudSistema').modal('show');
    $("#formEditarSoporte").load("editar_soporte.jsp?id_sop=" + id_soporte);
}

$('#modalDetalleSolicitud').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var incidente = button.data('incidente');
    var forma = button.data('forma');
    var tecnico_sugerido = button.data('sugerido');
    var tecnico_registra = button.data('registra');
    var modal = $(this);
    modal.find('.modal-body #areaincidente').val(incidente);
    modal.find('.modal-body #txtforms').val(forma);
    modal.find('.modal-body #txtsugerido').val(tecnico_sugerido);
    modal.find('.modal-body #txtregistra').val(tecnico_registra);
})

$('#modalDetalleSolicitudReferencia').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var descripcion = button.data('descripcion');
    var diagnostico = button.data('diagnostico');
    var adjunto = button.data('adjunto');
    var modal = $(this);
    modal.find('.modal-body #descripcion').val(descripcion);
    modal.find('.modal-body #diagnostico').val(diagnostico);
    if (adjunto != '') {
        modal.find('.modal-body #adjunto').attr('href', adjunto);
        modal.find('.modal-body #adjunto').attr('hidden', false);
        modal.find('.modal-body #sinadjunto').attr('hidden', true);
    }else{
        modal.find('.modal-body #adjunto').attr('hidden', true);
        modal.find('.modal-body #sinadjunto').attr('hidden', false);
    }
})

$('#modalDetalleAsignacion').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var incidente = button.data('incidente');
    var tecnico_asignado = button.data('asignado');
    var fecha_asignado = button.data('fecha');
    var modal = $(this);
    modal.find('.modal-body #areaincidente12').val(incidente);
    modal.find('.modal-body #txtfechaasignacion12').val(fecha_asignado);
    modal.find('.modal-body #areaasignacion12').val(tecnico_asignado);
})

$('#modalDetalleAceptacion').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var incidente = button.data('incidente');
    var tecnico_aceptado = button.data('asignado');
    var fecha_aceptado = button.data('fecha');
    var modal = $(this);
    modal.find('.modal-body #areaincidente121').val(incidente);
    modal.find('.modal-body #txtfechaaceptacion12').val(fecha_aceptado);
    modal.find('.modal-body #areaaceptacion12').val(tecnico_aceptado);
})

$('#modalDetalleAtendido').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var incidente = button.data('incidente');
    var tecnico_aceptado = button.data('asignado');
    var fecha_aceptado = button.data('fecha');
    var modal = $(this);
    modal.find('.modal-body #areaincidente121').val(incidente);
    modal.find('.modal-body #txtfechaaceptacion12').val(fecha_aceptado);
    modal.find('.modal-body #areaaceptacion12').val(tecnico_aceptado);
})

$('#modalDetalleCalificacion').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var incidente = button.data('incidente');
    var tecnico_aceptado = button.data('asignado');
    var fecha_aceptado = button.data('fecha');
    var modal = $(this);
    modal.find('.modal-body #areaincidente121').val(incidente);
    modal.find('.modal-body #txtfechaaceptacion12').val(fecha_aceptado);
    modal.find('.modal-body #areaaceptacion12').val(tecnico_aceptado);
})

$('#modalAsignacion').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var codigo = button.data('codigo');
    var modal = $(this);
    modal.find('.modal-body #idsoli').val(codigo);
})

$('#modalDiagnosticoSolicitud').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var codigo = button.data('cod');
    var modal = $(this);
    modal.find('.modal-body #txtidsolicitud1').val(codigo);
})

$('#modalDetalleDiagnostico').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var diagnostico = button.data('diagnostico');
    var fecha = button.data('fecha');
    var modal = $(this);
    modal.find('.modal-body #areadiagnostico1').val(diagnostico);
    modal.find('.modal-body #txtfechadiagnostico').val(fecha);
})

$('#modalCalificarSolicitud').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var solu = button.data('solu');
    var modal = $(this);
    modal.find('.modal-body #txtidsolu').val(solu);
})

$('#modalDetalleCalificacionSolicitud').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var comentario = button.data('comentario');
    var fecha = button.data('fecha');
    var calificacion = button.data('calificacion');
    var modal = $(this);
    modal.find('.modal-body #areacomentario22').val(comentario);
    modal.find('.modal-body #txtcalificacion1').val(calificacion);
    modal.find('.modal-body #txtfechacalificacion1').val(fecha);
})

