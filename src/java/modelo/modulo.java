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
public class modulo {
    private int id_modulo;
    private String descripcion;
    private String ruta_enlace;
    private String icono;
    private String estado_clases;
    private boolean estado;
    private int orden;
    private boolean blank;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public modulo() {
    }
    
    public modulo(String descripcion, String ruta_enlace, String icono, String estado_clases, boolean estado, int orden, boolean blank) {
        this.descripcion = descripcion;
        this.ruta_enlace = ruta_enlace;
        this.icono = icono;
        this.estado_clases = estado_clases;
        this.estado = estado;
        this.orden = orden;
        this.blank = blank;
    }

    public int getId_modulo() {
        return id_modulo;
    }

    public void setId_modulo(int id_modulo) {
        this.id_modulo = id_modulo;
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

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getEstado_clases() {
        return estado_clases;
    }

    public void setEstado_clases(String estado_clases) {
        this.estado_clases = estado_clases;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
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
        return "modulo{" + "id_modulo=" + id_modulo + ", descripcion=" + descripcion + ", ruta_enlace=" + ruta_enlace + ", icono=" + icono + ", estado_clases=" + estado_clases + ", estado=" + estado + ", orden=" + orden + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }
    
}
