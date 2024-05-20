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
public class item_tinta {
    private int id_item;
    private String descripcion;
    private int id_marca;
    private int cantidad;
    private int cantidad_minima;
    private int estado;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public item_tinta() {
    }

    public item_tinta(String descripcion, int id_marca, int cantidad, int cantidad_minima, int estado) {
        this.descripcion = descripcion;
        this.id_marca = id_marca;
        this.cantidad = cantidad;
        this.cantidad_minima = cantidad_minima;
        this.estado = estado;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad_minima() {
        return cantidad_minima;
    }

    public void setCantidad_minima(int cantidad_minima) {
        this.cantidad_minima = cantidad_minima;
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

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    @Override
    public String toString() {
        return "item_tinta{" + "id_item=" + id_item + ", descripcion=" + descripcion + ", id_marca=" + id_marca + ", cantidad=" + cantidad + ", cantidad_minima=" + cantidad_minima + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
