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
 * @author Kevin Druet
 */
public class permiso_vacaciones {
    private int id_permiso;
    private int id_usuario;
    private int id_motivo;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Date fecha_ingreso;
    private Date fecha_labor;
    private Date fecha_solicitud;
    private int dias_solicitados;
    private double dias_pendientes;
    private double dias_habiles;
    private double dias_nohabiles;
    private double dias_recargo;
    private double dias_descuento;
    private String observacion;
    private String modalidad;
    private String periodo;
    private int estado;
    private Timestamp fecha_creacion;
    private int codigoMotivo;
    private String direccion;
    private String jefe;
    private String denominacion;
    private String consumo;
    private String yearsRestantes;
    private int codigoUsu;
    private String nombreUsu;
    
    public permiso_vacaciones() {
    }

    public permiso_vacaciones(int id_usuario, int id_motivo, Date fecha_inicio, Date fecha_fin, Date fecha_ingreso, Date fecha_labor, Date fecha_solicitud, int dias_solicitados, double dias_pendientes, double dias_habiles, double dias_nohabiles, double dias_recargo, double dias_descuento, String observacion, String modalidad, String periodo, String consumo, String yearsRestantes) {
        this.id_usuario = id_usuario;
        this.id_motivo = id_motivo;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_ingreso = fecha_ingreso;
        this.fecha_labor = fecha_labor;
        this.fecha_solicitud = fecha_solicitud;
        this.dias_solicitados = dias_solicitados;
        this.dias_pendientes = dias_pendientes;
        this.dias_habiles = dias_habiles;
        this.dias_nohabiles = dias_nohabiles;
        this.dias_recargo = dias_recargo;
        this.dias_descuento = dias_descuento;
        this.observacion = observacion;
        this.modalidad = modalidad;
        this.periodo = periodo;
        this.consumo = consumo;
        this.yearsRestantes = yearsRestantes;
    }

    public int getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(int id_permiso) {
        this.id_permiso = id_permiso;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(Date fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public int getDias_solicitados() {
        return dias_solicitados;
    }

    public void setDias_solicitados(int dias_solicitados) {
        this.dias_solicitados = dias_solicitados;
    }

    public double getDias_pendientes() {
        return dias_pendientes;
    }

    public void setDias_pendientes(double dias_pendientes) {
        this.dias_pendientes = dias_pendientes;
    }

    public double getDias_habiles() {
        return dias_habiles;
    }

    public void setDias_habiles(double dias_habiles) {
        this.dias_habiles = dias_habiles;
    }

    public double getDias_nohabiles() {
        return dias_nohabiles;
    }

    public void setDias_nohabiles(double dias_nohabiles) {
        this.dias_nohabiles = dias_nohabiles;
    }

    public double getDias_recargo() {
        return dias_recargo;
    }

    public void setDias_recargo(double dias_recargo) {
        this.dias_recargo = dias_recargo;
    }

    public double getDias_descuento() {
        return dias_descuento;
    }

    public void setDias_descuento(double dias_descuento) {
        this.dias_descuento = dias_descuento;
    }
    
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Date getFecha_labor() {
        return fecha_labor;
    }

    public void setFecha_labor(Date fecha_labor) {
        this.fecha_labor = fecha_labor;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }   
    
    
    @Override
    public String toString() {
        return "permiso_vacaciones{" + "id_permiso=" + id_permiso + ", id_usuario=" + id_usuario + ", id_motivo=" + id_motivo + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", fecha_ingreso=" + fecha_ingreso + ", fecha_labor=" + fecha_labor + ", fecha_solicitud=" + fecha_solicitud + ", dias_solicitados=" + dias_solicitados + ", dias_pendientes=" + dias_pendientes + ", dias_habiles=" + dias_habiles + ", dias_nohabiles=" + dias_nohabiles + ", dias_recargo=" + dias_recargo + ", observacion=" + observacion + ", modalidad=" + modalidad + ", periodo=" + periodo + ", estado=" + estado + '}';
    }

    public int getCodigoMotivo() {
        return codigoMotivo;
    }

    public void setCodigoMotivo(int codigoMotivo) {
        this.codigoMotivo = codigoMotivo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }

    public String getYearsRestantes() {
        return yearsRestantes;
    }

    public void setYearsRestantes(String yearsRestantes) {
        this.yearsRestantes = yearsRestantes;
    }

    public int getCodigoUsu() {
        return codigoUsu;
    }

    public void setCodigoUsu(int codigoUsu) {
        this.codigoUsu = codigoUsu;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

   
}