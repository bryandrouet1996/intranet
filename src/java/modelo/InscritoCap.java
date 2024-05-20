/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author Don Beta
 */
public class InscritoCap {
    private int id;
    private int idInscripcion;
    private String nombres;
    private String unidad;
    private int asistencia;
    private int satisfaccion;
    private String satisfaccionDesc;
    private Date satisfaccionFec;

    public InscritoCap() {
    }

    public InscritoCap(int id, int idInscripcion, String nombres, String unidad, int asistencia, int satisfaccion, String satisfaccionDesc, Date satisfaccionFec) {
        this.id = id;
        this.idInscripcion = idInscripcion;
        this.nombres = nombres;
        this.unidad = unidad;
        this.asistencia = asistencia;
        this.satisfaccion = satisfaccion;
        this.satisfaccionDesc = satisfaccionDesc;
        this.satisfaccionFec = satisfaccionFec;
    }
    
    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(int asistencia) {
        this.asistencia = asistencia;
    }

    public int getSatisfaccion() {
        return satisfaccion;
    }

    public void setSatisfaccion(int satisfaccion) {
        this.satisfaccion = satisfaccion;
    }

    public String getSatisfaccionDesc() {
        return satisfaccionDesc;
    }

    public void setSatisfaccionDesc(String satisfaccionDesc) {
        this.satisfaccionDesc = satisfaccionDesc;
    }

    public Date getSatisfaccionFec() {
        return satisfaccionFec;
    }

    public void setSatisfaccionFec(Date satisfaccionFec) {
        this.satisfaccionFec = satisfaccionFec;
    }

    
}
