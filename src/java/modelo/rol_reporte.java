/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Rayner
 */
public class rol_reporte {
    private int codigo_funcionario;
    private int anio;
    private String mes_numero;
    private String descripcion;
    private String nombres;

    public rol_reporte() {
    }   

    public rol_reporte(int codigo_funcionario, int anio, String mes_numero, String descripcion, String nombres) {
        this.codigo_funcionario = codigo_funcionario;
        this.anio = anio;
        this.mes_numero = mes_numero;
        this.descripcion = descripcion;
        this.nombres = nombres;
    }

    public int getCodigo_funcionario() {
        return codigo_funcionario;
    }

    public void setCodigo_funcionario(int codigo_funcionario) {
        this.codigo_funcionario = codigo_funcionario;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMes_numero() {
        return mes_numero;
    }

    public void setMes_numero(String mes_numero) {
        this.mes_numero = mes_numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    
}
