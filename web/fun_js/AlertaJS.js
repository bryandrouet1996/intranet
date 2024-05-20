/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $('#formConfigurarAlerta').submit(function (event) {
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
                    $("#modalConfiguracionAlerta .close").click();
                    iziToast.success({
                        title: 'Aviso',
                        message: 'Se realizo registro de manera exitosa',
                        position: 'topRight',
                    });
                } else {
                    iziToast.warning({
                        title: 'Aviso',
                        message: 'No se realizo la acción solicitada, verifique su registro',
                        position: 'topRight',
                    })
                }
            },

        });
        return false;
    });
});
