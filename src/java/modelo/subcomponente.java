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
public class subcomponente {
    private int id_subcomponente;
    private int id_componente;
    private String descripcion;
    private String ruta_enlace;
    private boolean estado;
    private int orden;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public subcomponente() {
    }

    public subcomponente(int id_componente, String descripcion, String ruta_enlace, boolean estado, int orden) {
        this.id_componente = id_componente;
        this.descripcion = descripcion;
        this.ruta_enlace = ruta_enlace;
        this.estado = estado;
        this.orden = orden;
    }

    public int getId_subcomponente() {
        return id_subcomponente;
    }

    public void setId_subcomponente(int id_subcomponente) {
        this.id_subcomponente = id_subcomponente;
    }

    public int getId_componente() {
        return id_componente;
    }

    public void setId_componente(int id_componente) {
        this.id_componente = id_componente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta_enlace() {
        return ruta_enlace;
    }

    public void setRuta_enlace(String ruta_enlace) {
        this.ruta_enlace = ruta_enlace;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }
    

    @Override
    public String toString() {
        return "subcomponente{" + "id_subcomponente=" + id_subcomponente + ", id_componente=" + id_componente + ", descripcion=" + descripcion + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
