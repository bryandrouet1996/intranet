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
public class permiso_horas {
    private int id_permiso;
    private int id_usuario;
    private String hora_entrada;
    private String hora_salida;
    private int id_motivo;
    private Date fecha;
    private String tiempo_solicita;
    private int horas;
    private int minutos;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Date fecha_ingreso;
    private int dias_solicitados;
    private double dias_pendientes;
    private double dias_habiles;
    private double dias_nohabiles;
    private double dias_recargo;
    private double dias_descuento;
    private int id_tipo;
    private String observacion;
    private String adjunto;
    private int id_estado;
    private Timestamp fecha_creacion;
    private String direccion;
    private String cierre;
    private boolean valido;
    private String asistencia;
    private Timestamp timestamp_inicio;
    private Timestamp timestamp_fin;
    private String denominacion;
    private String jefe;
    private String cargoJefe;
    private int codigoUsu;
    private String nombreUsu;

    public permiso_horas() {
    }

    public permiso_horas(int id_usuario, String hora_entrada, String hora_salida, int id_motivo, Date fecha, String tiempo_solicita, int horas, int minutos, String observacion, int id_tipo) {
        this.id_usuario = id_usuario;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.id_motivo = id_motivo;
        this.fecha = fecha;
        this.tiempo_solicita = tiempo_solicita;
        this.horas = horas;
        this.minutos = minutos;
        this.observacion = observacion;
        this.id_tipo = id_tipo;
    }
    
    public permiso_horas(String hora_entrada, String hora_salida, int id_motivo, Date fecha, String tiempo_solicita, int horas, int minutos, String observacion, int id_tipo) {
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.id_motivo = id_motivo;
        this.fecha = fecha;
        this.tiempo_solicita = tiempo_solicita;
        this.horas = horas;
        this.minutos = minutos;
        this.observacion = observacion;
        this.id_tipo = id_tipo;
    }

    public permiso_horas(int id_usuario, int id_motivo, Date fecha, Date fecha_inicio, Date fecha_fin, Date fecha_ingreso, int dias_solicitados, double dias_pendientes, double dias_habiles, double dias_nohabiles, double dias_recargo, double dias_descuento, String observacion, int id_tipo) {
        this.id_usuario = id_usuario;
        this.id_motivo = id_motivo;
        this.fecha = fecha;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_ingreso = fecha_ingreso;
        this.dias_solicitados = dias_solicitados;
        this.dias_pendientes = dias_pendientes;
        this.dias_habiles = dias_habiles;
        this.dias_nohabiles = dias_nohabiles;
        this.dias_recargo = dias_recargo;
        this.dias_descuento = dias_descuento;
        this.observacion = observacion;
        this.id_tipo = id_tipo;
    }
    
    public permiso_horas(int id_motivo, Date fecha, Date fecha_inicio, Date fecha_fin, Date fecha_ingreso, int dias_solicitados, double dias_pendientes, double dias_habiles, double dias_nohabiles, double dias_recargo, double dias_descuento, String observacion, int id_tipo) {
        this.id_motivo = id_motivo;
        this.fecha = fecha;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.fecha_ingreso = fecha_ingreso;
        this.dias_solicitados = dias_solicitados;
        this.dias_pendientes = dias_pendientes;
        this.dias_habiles = dias_habiles;
        this.dias_nohabiles = dias_nohabiles;
        this.dias_recargo = dias_recargo;
        this.dias_descuento = dias_descuento;
        this.observacion = observacion;
        this.id_tipo = id_tipo;
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

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTiempo_solicita() {
        return tiempo_solicita;
    }

    public void setTiempo_solicita(String tiempo_solicita) {
        this.tiempo_solicita = tiempo_solicita;
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

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }
    
    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
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

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }
    
    public String toString1() {
        return "permiso_horas{" + "id_permiso=" + id_permiso + ", id_usuario=" + id_usuario + ", hora_entrada=" + hora_entrada + ", hora_salida=" + hora_salida + ", id_motivo=" + id_motivo + ", fecha=" + fecha + ", tiempo_solicita=" + tiempo_solicita + ", horas=" + horas + ", minutos=" + minutos + ", observacion=" + observacion + ", adjunto=" + adjunto + ", id_tipo=" + id_tipo  + ", id_estado=" + id_estado + '}';
    }

    @Override
    public String toString() {
        return "permiso_horas{" + "id_permiso=" + id_permiso + ", id_usuario=" + id_usuario + ", id_motivo=" + id_motivo + ", fecha=" + fecha + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", fecha_ingreso=" + fecha_ingreso + ", dias_solicitados=" + dias_solicitados + ", dias_pendientes=" + dias_pendientes + ", dias_habiles=" + dias_habiles + ", dias_nohabiles=" + dias_nohabiles + ", dias_recargo=" + dias_recargo+ ", id_tipo=" + id_tipo + ", dias_descuento=" + dias_descuento + ", observacion=" + observacion + ", adjunto=" + adjunto + ", id_estado=" + id_estado + '}';
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCierre() {
        return cierre;
    }

    public void setCierre(String cierre) {
        this.cierre = cierre;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public String getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(String asistencia) {
        this.asistencia = asistencia;
    }

    public Timestamp getTimestamp_inicio() {
        return timestamp_inicio;
    }

    public void setTimestamp_inicio(Timestamp timestamp_inicio) {
        this.timestamp_inicio = timestamp_inicio;
    }

    public Timestamp getTimestamp_fin() {
        return timestamp_fin;
    }

    public void setTimestamp_fin(Timestamp timestamp_fin) {
        this.timestamp_fin = timestamp_fin;
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
