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
public class informacion_usuario {
    private int id_informacion;
    private String codigo_funcionario;
    private Date fecha_ingreso;
    private String periodo;
    private int dias_habiles;
    private int disponibilidad;
    private int fines_semana;
    private String modalidad;
    private int disponibilidadHoras;
    private int disponibilidadMinutos;
    private int diasPendientes;
    private int horasPendientes;
    private int minutosPendientes;

    public informacion_usuario() {
    }

    public informacion_usuario(String codigo_funcionario, Date fecha_ingreso, String periodo, int dias_habiles, int disponibilidad, int fines_semana, String modalidad, int disponibilidadHoras, int disponibilidadMinutos, int diasPendientes, int horasPendientes, int minutosPendientes) {
        this.codigo_funcionario = codigo_funcionario;
        this.fecha_ingreso = fecha_ingreso;
        this.periodo = periodo;
        this.dias_habiles = dias_habiles;
        this.disponibilidad = disponibilidad;
        this.fines_semana = fines_semana;
        this.modalidad = modalidad;
        this.disponibilidadHoras = disponibilidadHoras;
        this.disponibilidadMinutos = disponibilidadMinutos;
        this.diasPendientes = diasPendientes;
        this.horasPendientes = horasPendientes;
        this.minutosPendientes = minutosPendientes;
    }     

    public int getId_informacion() {
        return id_informacion;
    }

    public void setId_informacion(int id_informacion) {
        this.id_informacion = id_informacion;
    }

    public String getCodigo_funcionario() {
        return codigo_funcionario;
    }

    public void setCodigo_funcionario(String codigo_funcionario) {
        this.codigo_funcionario = codigo_funcionario;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getDias_habiles() {
        return dias_habiles;
    }

    public void setDias_habiles(int dias_habiles) {
        this.dias_habiles = dias_habiles;
    }

    public int getFines_semana() {
        return fines_semana;
    }

    public void setFines_semana(int fines_semana) {
        this.fines_semana = fines_semana;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @Override
    public String toString() {
        return "informacion_usuario{" + "id_informacion=" + id_informacion + ", codigo_funcionario=" + codigo_funcionario + ", fecha_ingreso=" + fecha_ingreso + ", periodo=" + periodo + ", dias_habiles=" + dias_habiles + ", disponibilidad=" + disponibilidad + ", fines_semana=" + fines_semana + ", modalidad=" + modalidad + '}';
    }

    public int getDisponibilidadHoras() {
        return disponibilidadHoras;
    }

    public void setDisponibilidadHoras(int disponibilidadHoras) {
        this.disponibilidadHoras = disponibilidadHoras;
    }

    public int getDisponibilidadMinutos() {
        return disponibilidadMinutos;
    }

    public void setDisponibilidadMinutos(int disponibilidadMinutos) {
        this.disponibilidadMinutos = disponibilidadMinutos;
    }

    public int getDiasPendientes() {
        return diasPendientes;
    }

    public void setDiasPendientes(int diasPendientes) {
        this.diasPendientes = diasPendientes;
    }

    public int getHorasPendientes() {
        return horasPendientes;
    }

    public void setHorasPendientes(int horasPendientes) {
        this.horasPendientes = horasPendientes;
    }

    public int getMinutosPendientes() {
        return minutosPendientes;
    }

    public void setMinutosPendientes(int minutosPendientes) {
        this.minutosPendientes = minutosPendientes;
    }
    
    
}
