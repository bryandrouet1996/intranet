/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class ReportePermiso {

    private String funcionario;
    private int periodo;
    private int dias;
    private int horas;
    private int minutos;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Timestamp fechaRetorno;
    private String descripcion;
    private String regimen;
    private String departamento;

    public ReportePermiso() {
    }

    public ReportePermiso(String funcionario, int periodo, int dias, int horas, int minutos, Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaRetorno, String descripcion, String regimen, String departamento) {
        this.funcionario = funcionario;
        this.periodo = periodo;
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRetorno = fechaRetorno;
        this.descripcion = descripcion;
        this.regimen = regimen;
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Timestamp getFechaRetorno() {
        return fechaRetorno;
    }

    public void setFechaRetorno(Timestamp fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

}
