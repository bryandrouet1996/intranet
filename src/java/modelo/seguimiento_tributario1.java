/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Luis Torres F
 */
public class seguimiento_tributario1 {
    private String descripcion;
    private int cantidad;
    private float total;

    public seguimiento_tributario1(String descripcion, int cantidad, float total) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.total = total;
    }

    public seguimiento_tributario1() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
