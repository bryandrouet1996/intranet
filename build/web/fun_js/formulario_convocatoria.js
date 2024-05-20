/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('#modalAnularConvocatoria').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id_convocatoria = button.data('idconv');
    var modal = $(this);
    modal.find('.modal-body #txtidconvocatoria').val(id_convocatoria);
})