/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author Rayner
 */
public class PeriodoVaca {

    private int id;
    private int codFunc;
    private String periodo;
    private String modalidad;
    private int habiles;
    private int noHabiles;
    private int diasDisp;
    private int horasDisp;
    private int minDisp;
    private int diasHabDisp;
    private int diasPend;
    private int horasPend;
    private int minPend;
    private int diasFinDisp;
    private Date fechaIngreso;

    public PeriodoVaca() {
    }

    public PeriodoVaca(int codFunc, String periodo, String modalidad, int habiles, int noHabiles, int diasDisp, int horasDisp, int minDisp, int diasHabDisp, int diasPend, int horasPend, int minPend, int diasFinDisp, Date fechaIngreso) {
        this.codFunc = codFunc;
        this.periodo = periodo;
        this.modalidad = modalidad;
        this.habiles = habiles;
        this.noHabiles = noHabiles;
        this.diasDisp = diasDisp;
        this.horasDisp = horasDisp;
        this.minDisp = minDisp;
        this.diasHabDisp = diasHabDisp;
        this.diasPend = diasPend;
        this.horasPend = horasPend;
        this.minPend = minPend;
        this.diasFinDisp = diasFinDisp;
        this.fechaIngreso = fechaIngreso;
    }

    public int getMinPend() {
        return minPend;
    }

    public void setMinPend(int minPend) {
        this.minPend = minPend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodFunc() {
        return codFunc;
    }

    public void setCodFunc(int codFunc) {
        this.codFunc = codFunc;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getHabiles() {
        return habiles;
    }

    public void setHabiles(int habiles) {
        this.habiles = habiles;
    }

    public int getNoHabiles() {
        return noHabiles;
    }

    public void setNoHabiles(int noHabiles) {
        this.noHabiles = noHabiles;
    }

    public int getDiasDisp() {
        return diasDisp;
    }

    public void setDiasDisp(int diasDisp) {
        this.diasDisp = diasDisp;
    }

    public int getHorasDisp() {
        return horasDisp;
    }

    public void setHorasDisp(int horasDisp) {
        this.horasDisp = horasDisp;
    }

    public int getMinDisp() {
        return minDisp;
    }

    public void setMinDisp(int minDisp) {
        this.minDisp = minDisp;
    }

    public int getDiasPend() {
        return diasPend;
    }

    public void setDiasPend(int diasPend) {
        this.diasPend = diasPend;
    }

    public int getHorasPend() {
        return horasPend;
    }

    public void setHorasPend(int horasPend) {
        this.horasPend = horasPend;
    }

    public int getDiasHabDisp() {
        return diasHabDisp;
    }

    public void setDiasHabDisp(int diasHabDisp) {
        this.diasHabDisp = diasHabDisp;
    }

    public int getDiasFinDisp() {
        return diasFinDisp;
    }

    public void setDiasFinDisp(int diasFinDisp) {
        this.diasFinDisp = diasFinDisp;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

}
