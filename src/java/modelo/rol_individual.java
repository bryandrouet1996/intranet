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
public class rol_individual {
    private String codigo_funcionario;
    private String anio;
    private String mes_caracter;
    private String mes_numero;
    private String id_descripcion;
    private String descripcion;
    private String descripcion_ingreso;
    private double ingreso;
    private String descripcion_egreso;
    private double egreso;

    public rol_individual() {
    }

    public rol_individual(String codigo_funcionario, String anio, String mes_caracter, String mes_numero, String id_descripcion, String descripcion) {
        this.codigo_funcionario = codigo_funcionario;
        this.anio = anio;
        this.mes_caracter = mes_caracter;
        this.mes_numero = mes_numero;
        this.id_descripcion = id_descripcion;
        this.descripcion = descripcion;
    }

    public String getCodigo_funcionario() {
        return codigo_funcionario;
    }

    public void setCodigo_funcionario(String codigo_funcionario) {
        this.codigo_funcionario = codigo_funcionario;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes_caracter() {
        return mes_caracter;
    }

    public void setMes_caracter(String mes_caracter) {
        this.mes_caracter = mes_caracter;
    }

    public String getMes_numero() {
        return mes_numero;
    }

    public void setMes_numero(String mes_numero) {
        this.mes_numero = mes_numero;
    }

    public String getId_descripcion() {
        return id_descripcion;
    }

    public void setId_descripcion(String id_descripcion) {
        this.id_descripcion = id_descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion_ingreso() {
        return descripcion_ingreso;
    }

    public void setDescripcion_ingreso(String descripcion_ingreso) {
        this.descripcion_ingreso = descripcion_ingreso;
    }

    public double getIngreso() {
        return ingreso;
    }

    public void setIngreso(double ingreso) {
        this.ingreso = ingreso;
    }

    public String getDescripcion_egreso() {
        return descripcion_egreso;
    }

    public void setDescripcion_egreso(String descripcion_egreso) {
        this.descripcion_egreso = descripcion_egreso;
    }

    public double getEgreso() {
        return egreso;
    }

    public void setEgreso(double egreso) {
        this.egreso = egreso;
    }

    @Override
    public String toString() {
        return "rol_individual{" + "codigo_funcionario=" + codigo_funcionario + ", anio=" + anio + ", mes_caracter=" + mes_caracter + ", mes_numero=" + mes_numero + ", id_descripcion=" + id_descripcion + ", descripcion=" + descripcion + ", descripcion_ingreso=" + descripcion_ingreso + ", ingreso=" + ingreso + ", descripcion_egreso=" + descripcion_egreso + ", egreso=" + egreso + '}';
    }
    
}
