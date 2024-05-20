/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class UsuarioConfig {
    private int id;
    private int codigo;
    private String cedula;
    private String nombres;
    private String correo;
    private Date fechaNac;
    private String cargo;
    private String direccion;
    private String codigo_direccion;
    private boolean activo;
    private Timestamp creacion;

    public UsuarioConfig() {
    }

    public UsuarioConfig(int id, int codigo, String cedula, String nombres, String correo, Date fechaNac, String cargo, String codigo_direccion, String direccion, boolean activo, Timestamp creacion) {
        this.id = id;
        this.codigo = codigo;
        this.cedula = cedula;
        this.nombres = nombres;
        this.correo = correo;
        this.fechaNac = fechaNac;
        this.cargo = cargo;
        this.codigo_direccion = codigo_direccion;
        this.direccion = direccion;
        this.activo = activo;
        this.creacion = creacion;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodigo_direccion() {
        return codigo_direccion;
    }

    public void setCodigo_direccion(String codigo_direccion) {
        this.codigo_direccion = codigo_direccion;
    }

    
}
