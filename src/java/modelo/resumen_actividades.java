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
public class resumen_actividades {
    private int id_resumen;
    private java.sql.Date fecha_inicio;
    private java.sql.Date fecha_fin;
    private String nombre_funcionario;
    private String direccion;
    private String cedula;
    private String total_horas;

    public resumen_actividades() {
    }

    public resumen_actividades(int id_resumen, Date fecha_inicio, Date fecha_fin, String nombre_funcionario, String direccion, String cedula, String total_horas) {
        this.id_resumen = id_resumen;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.nombre_funcionario = nombre_funcionario;
        this.direccion = direccion;
        this.cedula = cedula;
        this.total_horas = total_horas;
    }

    public int getId_resumen() {
        return id_resumen;
    }

    public void setId_resumen(int id_resumen) {
        this.id_resumen = id_resumen;
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

    public String getNombre_funcionario() {
        return nombre_funcionario;
    }

    public void setNombre_funcionario(String nombre_funcionario) {
        this.nombre_funcionario = nombre_funcionario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTotal_horas() {
        return total_horas;
    }

    public void setTotal_horas(String total_horas) {
        this.total_horas = total_horas;
    }

    @Override
    public String toString() {
        return "resumen_actividades{" + "id_resumen=" + id_resumen + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", nombre_funcionario=" + nombre_funcionario + ", direccion=" + direccion + ", cedula=" + cedula + ", total_horas=" + total_horas + '}';
    }
    
}
