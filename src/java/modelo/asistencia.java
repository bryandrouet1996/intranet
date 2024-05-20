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
public class asistencia {
    private int id_asistencia;
    private int id_usuario;
    private String funcionario;
    private Date fecha_creacion;
    private String hora_creacion;
    private int id_estado;
    private int id_tipo;
    private String tipo;    

    public asistencia() {
    }

    public asistencia(int id_asistencia, int id_usuario, Date fecha_creacion, String hora_creacion, int id_estado, int id_tipo) {
        this.id_asistencia = id_asistencia;
        this.id_usuario = id_usuario;
        this.fecha_creacion = fecha_creacion;
        this.hora_creacion = hora_creacion;
        this.id_estado = id_estado;
        this.id_tipo = id_tipo;
    }   
    
    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getHora_creacion() {
        return hora_creacion;
    }

    public void setHora_creacion(String hora_creacion) {
        this.hora_creacion = hora_creacion;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }
    
    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
}
