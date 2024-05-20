/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function validacionCheckOtro() {
    element = document.getElementById("otros1");
    check = document.getElementById("sw8");
    if (check.checked) {
        element.style.display = 'block';
    } else {
        element.style.display = 'none';
    }
}

function validacionCheckPagina() {
    element = document.getElementById("paginas");
    check = document.getElementById("sw7");
    if (check.checked) {
        element.style.display = 'block';
    } else {
        element.style.display = 'none';
    }
}

function validacionNumber() {
    element = document.getElementById("comboavance");
    if (element.value > 100) {
        element.value = 100;
    } else if (element.value < 1) {
        element.value = 1;
    }
}

function validacionAnio(number) {
    element = document.getElementById("txtanio");
    if (element.value > number) {
        element.value = number;
    } else if (element.value < 1) {
        element.value = number;
    }
}

function validarhoras() {
    var txthorainicio = $('#txthorainicio').val();
    var txthorafin = $('#txthorafin').val();
    var horaInicio = moment(txthorainicio, 'h:mm');
    var horaFin = moment(txthorafin, 'h:mm');
    if (horaInicio > horaFin) {
        iziToast.error({
            title: 'Aviso',
            message: 'La hora de inicio no puede ser mayor a la hora de finalizacion',
            position: 'topRight',
        });
        document.getElementById("txthorainicio").value = txthorafin;
    }
}

function validarhoras() {
    var txthorainicio = $('#txthorainicio').val();
    var txthorafin = $('#txthorafin').val();
    var horaInicio = moment(txthorainicio, 'h:mm');
    var horaFin = moment(txthorafin, 'h:mm');
    if (horaInicio > horaFin) {
        iziToast.error({
            title: 'Aviso',
            message: 'La hora de inicio no puede ser mayor a la hora de finalizacion',
            position: 'topRight',
        });
    }
}

function obtenerValor(id_actividad) {
    var id_actividades = +id_actividad + ",";
    iziToast.error({
        title: 'Aviso',
        message: 'La hora de inicio no puede ser mayor a la hora de finalizacion' + id_actividades,
        position: 'topRight',
    });
}


