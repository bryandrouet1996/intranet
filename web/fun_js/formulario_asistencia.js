/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function mostrarUbicacion(id_asistencia) {
    $("#form_ubicacion").load("ubicacion_asistencia.jsp?id_asistencia=" + id_asistencia + "");
}

