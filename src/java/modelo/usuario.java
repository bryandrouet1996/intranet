/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author usuario
 */
public class usuario {

    private int id_usuario;
    private String codigo_usuario;
    private String nombre;
    private String apellido;
    private String cedula;
    private String correo;
    private String clave;
    private String iniciales;
    private String codigo_cargo;
    private String codigo_unidad;
    private String codigo_funcion;
    private String funcion;
    private String firma;
    private int tipo_funcion;
    private java.sql.Date fecha_nacimiento;
    private java.sql.Date fecha_creacion;

    public usuario() {
    }

    public usuario(String correo, String clave) {
        this.correo = correo;
        this.clave = clave;
    }

    public usuario(String codigo_usuario, String nombre, String apellido, String cedula, String correo, String clave, String iniciales, String codigo_cargo, String codigo_unidad, java.sql.Date fecha_nacimiento, java.sql.Date fecha_creacion) {
        this.codigo_usuario = codigo_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.correo = correo;
        this.clave = clave;
        this.iniciales = iniciales;
        this.codigo_cargo = codigo_cargo;
        this.codigo_unidad = codigo_unidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_creacion = fecha_creacion;
    }

    public usuario(int id_usuario, String codigo_usuario, String nombre, String apellido, String cedula, String correo, Date fecha_nacimiento) {
        this.id_usuario = id_usuario;
        this.codigo_usuario = codigo_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getCodigo_usuario() {
        return codigo_usuario;
    }

    public void setCodigo_usuario(String codigo_usuario) {
        this.codigo_usuario = codigo_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public String getIniciales() {
        return iniciales;
    }

    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }

    public String getCodigo_cargo() {
        return codigo_cargo;
    }

    public void setCodigo_cargo(String codigo_cargo) {
        this.codigo_cargo = codigo_cargo;
    }

    public String getCodigo_unidad() {
        return codigo_unidad;
    }

    public void setCodigo_unidad(String codigo_unidad) {
        this.codigo_unidad = codigo_unidad;
    }

    public String getCodigo_funcion() {
        return codigo_funcion;
    }

    public void setCodigo_funcion(String codigo_funcion) {
        this.codigo_funcion = codigo_funcion;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public int getTipo_funcion() {
        return tipo_funcion;
    }

    public void setTipo_funcion(int tipo_funcion) {
        this.tipo_funcion = tipo_funcion;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public java.sql.Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(java.sql.Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String stringify() {
        return "{id: " + id_usuario + ",codigo_funcion: '" + codigo_funcion + "',codigo_unidad: '" + codigo_unidad + "',nombre: '" + nombre + "'}";
    }

    @Override
    public String toString() {
        return "usuario{" + "id_usuario=" + id_usuario + ", codigo_usuario=" + codigo_usuario + ", nombre=" + nombre + ", apellido=" + apellido + ", cedula=" + cedula + ", correo=" + correo + ", clave=" + clave + ", iniciales=" + iniciales + ", codigo_cargo=" + codigo_cargo + ", codigo_unidad=" + codigo_unidad + ", fecha_nacimiento=" + fecha_nacimiento + ", fecha_creacion=" + fecha_creacion + '}';
    }
}
