/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('#modalEditarAlmacenamiento').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_almacenamiento = button.data('id');
    var nombre = button.data('nombre');
    var id_tipo = button.data('tipo');
    var modal = $(this);
    modal.find('.modal-body #txtial').val(id_almacenamiento);
    modal.find('.modal-body #txtnombre').val(nombre);
    modal.find('.modal-body #combotipo').val(id_tipo);
})


