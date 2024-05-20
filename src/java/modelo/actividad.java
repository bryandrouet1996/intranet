/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author Kevin Druet
 */
public class actividad {
    private int id_actividad;
    private int id_usuario;
    private String tarea;
    private java.sql.Date fecha_actividad;
    private java.sql.Date fecha_registro;
    private String hora_inicio;
    private String hora_fin;
    private String herramienta;
    private String herramienta_otro;
    private String observacion;
    private String requiriente;
    private java.sql.Date fecha_limite;
    private String indicador;
    private String grado;
    private String avance;
    private boolean estado;
    
    public actividad() {
    }

    public actividad(int id_usuario, String tarea, Date fecha_actividad, Date fecha_registro, String hora_inicio, String hora_fin, String herramienta, String herramienta_otro, String observacion, String requiriente, Date fecha_limite, String indicador, String grado, String avance) {
        this.id_usuario = id_usuario;
        this.tarea = tarea;
        this.fecha_actividad = fecha_actividad;
        this.fecha_registro = fecha_registro;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.herramienta = herramienta;
        this.herramienta_otro = herramienta_otro;
        this.observacion = observacion;
        this.requiriente = requiriente;
        this.fecha_limite = fecha_limite;
        this.indicador = indicador;
        this.grado = grado;
        this.avance = avance;
    }

    

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public Date getFecha_actividad() {
        return fecha_actividad;
    }

    public void setFecha_actividad(Date fecha_actividad) {
        this.fecha_actividad = fecha_actividad;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(String herramienta) {
        this.herramienta = herramienta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRequiriente() {
        return requiriente;
    }

    public void setRequiriente(String requiriente) {
        this.requiriente = requiriente;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getAvance() {
        return avance;
    }

    public void setAvance(String avance) {
        this.avance = avance;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHerramienta_otro() {
        return herramienta_otro;
    }

    public void setHerramienta_otro(String herramienta_otro) {
        this.herramienta_otro = herramienta_otro;
    }

    @Override
    public String toString() {
        return "actividad{" + "id_actividad=" + id_actividad + ", id_usuario=" + id_usuario + ", tarea=" + tarea + ", fecha_actividad=" + fecha_actividad + ", fecha_registro=" + fecha_registro + ", hora_inicio=" + hora_inicio + ", hora_fin=" + hora_fin + ", herramienta=" + herramienta + ", herramienta_otro=" + herramienta_otro + ", observacion=" + observacion + ", requiriente=" + requiriente + ", fecha_limite=" + fecha_limite + ", indicador=" + indicador + ", grado=" + grado + ", avance=" + avance + ", estado=" + estado + '}';
    }
    
    
   
}
