/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author Luis Torres F
 */
public class inifin_sesion {

    
    public inifin_sesion() {
        
    }

    public inifin_sesion(int id_registro, String usuario, String area, String cargo, Date fecha, Date horaini, Date horafin, String ipequipo) {
        this.id_registro = id_registro;
        this.usuario = usuario;
        this.area = area;
        this.cargo = cargo;
        this.fecha = fecha;
        this.horaini = horaini;
        this.horafin = horafin;
        this.ipequipo = ipequipo;
    }
    
    private int id_registro;
    private String usuario;
    private String area;
    private String cargo;
    private java.sql.Date fecha;
    private java.sql.Date horaini;
    private java.sql.Date horafin;
    private String ipequipo;

    public int getId_registro() {
        return id_registro;
    }

    public void setId_registro(int id_registro) {
        this.id_registro = id_registro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraini() {
        return horaini;
    }

    public void setHoraini(Date horaini) {
        this.horaini = horaini;
    }

    public Date getHorafin() {
        return horafin;
    }

    public void setHorafin(Date horafin) {
        this.horafin = horafin;
    }

    public String getIpequipo() {
        return ipequipo;
    }

    public void setIpequipo(String ipequipo) {
        this.ipequipo = ipequipo;
    }
    

}
