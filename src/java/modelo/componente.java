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
public class componente {
    private int id_componente;
    private int id_modulo;
    private String descripcion;
    private String ruta_enlace;
    private String estado_clases;
    private String icono;
    private boolean estado;
    private int orden;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public componente() {
    }

    public componente(int id_modulo, String descripcion, String ruta_enlace, String estado_clases, String icono, boolean estado, int orden) {
        this.id_modulo = id_modulo;
        this.descripcion = descripcion;
        this.ruta_enlace = ruta_enlace;
        this.estado_clases = estado_clases;
        this.icono = icono;
        this.estado = estado;
        this.orden = orden;
    }

    public int getId_componente() {
        return id_componente;
    }

    public void setId_componente(int id_componente) {
        this.id_componente = id_componente;
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

    public String getEstado_clases() {
        return estado_clases;
    }

    public void setEstado_clases(String estado_clases) {
        this.estado_clases = estado_clases;
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
        return "componente{" + "id_componente=" + id_componente + ", id_modulo=" + id_modulo + ", descripcion=" + descripcion + ", ruta_enlace=" + ruta_enlace + ", estado_clases=" + estado_clases + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
    
}
