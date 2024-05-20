/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Don Beta
 */
public class UsuarioIESS {
    private int codigo;
    private String nombres;
    private String regimen;
    private String cedula;

    public UsuarioIESS() {
    }   

    public UsuarioIESS(int codigo, String nombres, String regimen) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.regimen = regimen;
    }

    public UsuarioIESS(int codigo, String nombres, String regimen, String cedula) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.regimen = regimen;
        this.cedula = cedula;
    }       
       
    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    
}
