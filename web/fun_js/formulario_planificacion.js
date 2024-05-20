/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function seleccionTipoActividad(id_tipo) {
    // Get the checkbox
    var x = document.getElementById("presu");
    var y = document.getElementById("presua");
    if (id_tipo != 2) {
        x.style.display = "block";
        y.style.display = "block";
    } else {
        x.style.display = "none";
        y.style.display = "none";
    }

}

function sumatoriaProgramacion() {
    var x = document.getElementById("rgbutton");
    var pro1 = parseInt($('#txtpro1').val());
    var pro2 = parseInt($('#txtpro2').val());
    var pro3 = parseInt($('#txtpro3').val());
    var pro4 = parseInt($('#txtpro4').val());
    var total = pro1 + pro2 + pro3 + pro4;
    if (total>100) {
        iziToast.error({
            title: 'Aviso',
            message: 'Programación trimestral supera el 100%',
            position: 'topRight',
        });
        x.style.display = "none";
    }else if(total<=100){
        x.style.display = "block";
    }
}

function diferenciaPresupuesto(presupuesto_actual) {
    var x = document.getElementById("rgbutton");
    var pro1 = parseFloat(presupuesto_actual);
    var pro2 = parseFloat($('#txtpresupuesto').val());
    var total = pro1-pro2;
    if (pro2>pro1) {
        iziToast.error({
            title: 'Aviso',
            message: 'Esta sobrepasando el presupuesto asignado',
            position: 'topRight',
        });
        x.style.display = "none";
    }else{
        document.getElementById("txtpresupuestoac").value = total;
        x.style.display = "block";
    }
}
