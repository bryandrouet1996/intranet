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
public class rol_pago {
    private int id_rol;
    private String codigo_usuario;
    private String anio;
    private String mes;
    private String mes_numero;
    private String iddescripcion;
    private String descripcion_rol;
    private String nombre_funcionario;
    private String descripcion_ingreso;
    private Double ingreso;
    private String descripcion_descuento;
    private Double descuento;
    private Double liquido_recibir;

    public rol_pago() {
    }

    public rol_pago(String codigo_usuario, String anio, String mes, String mes_numero, String iddescripcion, String descripcion_rol, String nombre_funcionario, String descripcion_ingreso, Double ingreso, String descripcion_descuento, Double descuento, Double liquido_recibir) {
        this.codigo_usuario = codigo_usuario;
        this.anio = anio;
        this.mes = mes;
        this.mes_numero = mes_numero;
        this.iddescripcion = iddescripcion;
        this.descripcion_rol = descripcion_rol;
        this.nombre_funcionario = nombre_funcionario;
        this.descripcion_ingreso = descripcion_ingreso;
        this.ingreso = ingreso;
        this.descripcion_descuento = descripcion_descuento;
        this.descuento = descuento;
        this.liquido_recibir = liquido_recibir;
    }

    

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getCodigo_usuario() {
        return codigo_usuario;
    }

    public void setCodigo_usuario(String codigo_usuario) {
        this.codigo_usuario = codigo_usuario;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes_numero() {
        return mes_numero;
    }

    public void setMes_numero(String mes_numero) {
        this.mes_numero = mes_numero;
    }

    public String getIddescripcion() {
        return iddescripcion;
    }

    public void setIddescripcion(String iddescripcion) {
        this.iddescripcion = iddescripcion;
    }

    public String getDescripcion_rol() {
        return descripcion_rol;
    }

    public void setDescripcion_rol(String descripcion_rol) {
        this.descripcion_rol = descripcion_rol;
    }

    public String getNombre_funcionario() {
        return nombre_funcionario;
    }

    public void setNombre_funcionario(String nombre_funcionario) {
        this.nombre_funcionario = nombre_funcionario;
    }

    public String getDescripcion_ingreso() {
        return descripcion_ingreso;
    }

    public void setDescripcion_ingreso(String descripcion_ingreso) {
        this.descripcion_ingreso = descripcion_ingreso;
    }

    public Double getIngreso() {
        return ingreso;
    }

    public void setIngreso(Double ingreso) {
        this.ingreso = ingreso;
    }

    public String getDescripcion_descuento() {
        return descripcion_descuento;
    }

    public void setDescripcion_descuento(String descripcion_descuento) {
        this.descripcion_descuento = descripcion_descuento;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getLiquido_recibir() {
        return liquido_recibir;
    }

    public void setLiquido_recibir(Double liquido_recibir) {
        this.liquido_recibir = liquido_recibir;
    }

    @Override
    public String toString() {
        return "rol_pago{" + "id_rol=" + id_rol + ", codigo_usuario=" + codigo_usuario + ", anio=" + anio + ", mes=" + mes + ", mes_numero=" + mes_numero + ", iddescripcion=" + iddescripcion + ", descripcion_rol=" + descripcion_rol + ", nombre_funcionario=" + nombre_funcionario + ", descripcion_ingreso=" + descripcion_ingreso + ", ingreso=" + ingreso + ", descripcion_descuento=" + descripcion_descuento + ", descuento=" + descuento + ", liquido_recibir=" + liquido_recibir + '}';
    }

    
}
