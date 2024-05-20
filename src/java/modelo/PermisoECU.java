/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class PermisoECU {

    private int id;
    private int idUsu;
    private int idMotivo;
    private int estado;
    private Date fechaSoli;
    private String descripcion;
    private Timestamp inicio;
    private Timestamp fin;
    private String tiempoSoli;
    private int diasHabiles;
    private int diasFinde;
    private String adjunto;
    private String confirmacion;
    private String unidad;
    private String denominacion;
    private String jefe;
    private String cargoJefe;

    public PermisoECU() {
    }

    public PermisoECU(int idUsu, int idMotivo, Date fechaSoli, String descripcion, String unidad, String denominacion, String jefe, String cargoJefe) {
        this.idUsu = idUsu;
        this.idMotivo = idMotivo;
        this.fechaSoli = fechaSoli;
        this.descripcion = descripcion;
        this.unidad = unidad;
        this.denominacion = denominacion;
        this.jefe = jefe;
        this.cargoJefe = cargoJefe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(int idUsu) {
        this.idUsu = idUsu;
    }    
    
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getInicio() {
        return inicio;
    }

    public void setInicio(Timestamp inicio) {
        this.inicio = inicio;
    }

    public Timestamp getFin() {
        return fin;
    }

    public void setFin(Timestamp fin) {
        this.fin = fin;
    }

    public String getTiempoSoli() {
        return tiempoSoli;
    }

    public void setTiempoSoli(String tiempoSoli) {
        this.tiempoSoli = tiempoSoli;
    }

    public int getDiasHabiles() {
        return diasHabiles;
    }

    public void setDiasHabiles(int diasHabiles) {
        this.diasHabiles = diasHabiles;
    }

    public int getDiasFinde() {
        return diasFinde;
    }

    public void setDiasFinde(int diasFinde) {
        this.diasFinde = diasFinde;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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

    public Date getFechaSoli() {
        return fechaSoli;
    }

    public void setFechaSoli(Date fechaSoli) {
        this.fechaSoli = fechaSoli;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

}
