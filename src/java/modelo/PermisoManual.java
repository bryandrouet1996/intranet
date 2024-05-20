/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Rayner
 */
public class PermisoManual {
    private int id;
    private int admin;
    private int codUsu;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Timestamp fechaRetorno;
    private int diasHabiles;
    private int finesSemana;
    private String horaInicio;
    private String horaFin;
    private int horas;
    private int minutos;
    private String observacion;
    private String denominacion;
    private String direccion;
    private String jefe;
    private String cargoJefe;
    private String adjunto;
    private Timestamp creacion;

    public PermisoManual() {
    }

    public PermisoManual(int admin, int codUsu, Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaRetorno, int diasHabiles, int finesSemana, String horaInicio, String horaFin, int horas, int minutos, String observacion, String denominacion, String direccion, String jefe, String cargoJefe) {
        this.admin = admin;
        this.codUsu = codUsu;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRetorno = fechaRetorno;
        this.diasHabiles = diasHabiles;
        this.finesSemana = finesSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horas = horas;
        this.minutos = minutos;
        this.observacion = observacion;
        this.denominacion = denominacion;
        this.direccion = direccion;
        this.jefe = jefe;
        this.cargoJefe = cargoJefe;
    }

    public PermisoManual(int id, int admin, int codUsu, Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaRetorno, int diasHabiles, int finesSemana, String horaInicio, String horaFin, int horas, int minutos, String observacion, String denominacion, String direccion, String jefe, String cargoJefe, String adjunto, Timestamp creacion) {
        this.id = id;
        this.admin = admin;
        this.codUsu = codUsu;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaRetorno = fechaRetorno;
        this.diasHabiles = diasHabiles;
        this.finesSemana = finesSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horas = horas;
        this.minutos = minutos;
        this.observacion = observacion;
        this.denominacion = denominacion;
        this.direccion = direccion;
        this.jefe = jefe;
        this.cargoJefe = cargoJefe;
        this.adjunto = adjunto;
        this.creacion = creacion;
    }  
       
    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getCodUsu() {
        return codUsu;
    }

    public void setCodUsu(int codUsu) {
        this.codUsu = codUsu;
    }

    public int getDiasHabiles() {
        return diasHabiles;
    }

    public void setDiasHabiles(int diasHabiles) {
        this.diasHabiles = diasHabiles;
    }

    public int getFinesSemana() {
        return finesSemana;
    }

    public void setFinesSemana(int finesSemana) {
        this.finesSemana = finesSemana;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getCargoJefe() {
        return cargoJefe;
    }

    public void setCargoJefe(String cargoJefe) {
        this.cargoJefe = cargoJefe;
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

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    

    
}
