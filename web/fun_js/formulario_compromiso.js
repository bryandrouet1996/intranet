/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('#modalCumplimiento').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_compromiso = button.data('id');
    var fecha_cumplimiento = button.data('fecha');
    var descripcion = button.data('descripcion');
    var modal = $(this);
    modal.find('.modal-body #txtidcompromiso').val(id_compromiso);
    modal.find('.modal-body #txtcompromiso').val(descripcion);
    modal.find('.modal-body #txtfechacumplimiento').val(fecha_cumplimiento);
})

function cargarDatos(id_compromiso) {
    var x = document.getElementById("botoncumplimiento");
    $("#tablaverificable").load("verificable_compromiso.jsp?id_compromiso=" + id_compromiso);
    $.post('administrar_acta.control?accion=existe_verificable', {
        idcomp: id_compromiso
    }, function (response) {
        if (response) {
           var resp = parseInt(response);
           if(resp===1){
               x.hidden = false ;
           }else if(resp===0){
               x.hidden = true;
           }
        }
    }, );
}

function cargarDetalleCompromiso(id_compromiso) {
    $("#tabladetalleverificable").load("verificable_compromiso.jsp?id_compromiso=" + id_compromiso);
}

function cargarDetallesCompromisosActas(id_acta) {
    $("#tabladetallecompromiso").load("compromisos_actas.jsp?id_acta=" +id_acta);
}

function mostrarModal() {
    var id_compromiso = $('#txtidcompromiso').val();
    $('#modalVerificable').on('show.bs.modal', function (event) {
        var modal = $(this);
        modal.find('.modal-body #txtidcompromiso1').val(id_compromiso);
    })
    $('#modalVerificable').modal('show');
}


