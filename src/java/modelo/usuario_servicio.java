/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Kevin Druet
 */
public class usuario_servicio {
     private int id_usuario;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String telefono;
    private String correo;
    private String clave;

    public usuario_servicio() {
    }

    public usuario_servicio(String correo, String clave) {
        this.correo = correo;
        this.clave = clave;
    }

    public usuario_servicio(String nombres, String correo, String clave) {
        this.nombres = nombres;
        this.correo = correo;
        this.clave = clave;
    }

    public usuario_servicio(int id_usuario, String nombres, String apellidos, String cedula, String telefono, String correo, String clave) {
        this.id_usuario = id_usuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.clave = clave;
    }

    public usuario_servicio(String nombres, String apellidos, String cedula, String telefono, String correo, String clave) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.clave = clave;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
