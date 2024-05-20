/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function errorSesion(){
   iziToast.error({
    title: 'Aviso',
    message: 'Existe un error en el usuario o contraseña ingresados',
    position: 'topRight'
   });
}

function noexisteSesion(){
   iziToast.info({
    title: 'Aviso',
    message: 'El usuario ingresado no existe o se encuentra inhabilitado',
    position: 'topRight'
   });
}
