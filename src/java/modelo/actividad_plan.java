/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Kevin Druet
 */
public class actividad_plan {
    private int id_actividad;
    private int id_plan;
    private String actividad_descripcion;
    private String mes;
    private int id_tipo;
    private String indicador;
    private String meta;
    private String partida_presupuestaria;
    private String descripcion;
    private int id_orientador;
    private String programacion_trimestral;
    private double presupuesto;
    private double presupuesto_actual;
    private Timestamp fecha_creacion;
            
    public actividad_plan() {
    }

    public actividad_plan(int id_plan, String actividad_descripcion, String mes, int id_tipo, String indicador, String meta, String partida_presupuestaria, String descripcion, int id_orientador, String programacion_trimestral, double presupuesto, double presupuesto_actual) {
        this.id_plan = id_plan;
        this.actividad_descripcion = actividad_descripcion;
        this.mes = mes;
        this.id_tipo = id_tipo;
        this.indicador = indicador;
        this.meta = meta;
        this.partida_presupuestaria = partida_presupuestaria;
        this.descripcion = descripcion;
        this.id_orientador = id_orientador;
        this.programacion_trimestral = programacion_trimestral;
        this.presupuesto = presupuesto;
        this.presupuesto_actual = presupuesto_actual;
    }


    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }
    

    public String getActividad_descripcion() {
        return actividad_descripcion;
    }

    public void setActividad_descripcion(String actividad_descripcion) {
        this.actividad_descripcion = actividad_descripcion;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getPartida_presupuestaria() {
        return partida_presupuestaria;
    }

    public void setPartida_presupuestaria(String partida_presupuestaria) {
        this.partida_presupuestaria = partida_presupuestaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_orientador() {
        return id_orientador;
    }

    public void setId_orientador(int id_orientador) {
        this.id_orientador = id_orientador;
    }

    public String getProgramacion_trimestral() {
        return programacion_trimestral;
    }

    public void setProgramacion_trimestral(String programacion_trimestral) {
        this.programacion_trimestral = programacion_trimestral;
    }
    
    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getPresupuesto_actual() {
        return presupuesto_actual;
    }

    public void setPresupuesto_actual(double presupuesto_actual) {
        this.presupuesto_actual = presupuesto_actual;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "actividad_plan{" + "id_actividad=" + id_actividad + ", id_plan=" + id_plan + ", actividad_descripcion=" + actividad_descripcion + ", mes=" + mes + ", id_tipo=" + id_tipo + ", indicador=" + indicador + ", meta=" + meta + ", partida_presupuestaria=" + partida_presupuestaria + ", descripcion=" + descripcion + ", id_orientador=" + id_orientador + ", programacion_trimestral=" + programacion_trimestral + ", presupuesto=" + presupuesto + ", presupuesto_actual=" + presupuesto_actual + ", fecha_creacion=" + fecha_creacion + '}';
    }

}
