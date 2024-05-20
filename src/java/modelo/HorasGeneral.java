/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Rayner
 */
public class HorasGeneral {
    private permiso_horas permiso;
    private usuario funcionario;
    private String motivo;
    private String direccion;
    private String cargo;

    public HorasGeneral() {
        this.permiso = new permiso_horas();
        this.funcionario = new usuario();
        this.motivo = "";
        this.direccion = "";
        this.cargo = "";
    }

    public HorasGeneral(permiso_horas permiso, usuario funcionario, String motivo, String direccion, String cargo) {
        this.permiso = permiso;
        this.funcionario = funcionario;
        this.motivo = motivo;
        this.direccion = direccion;
        this.cargo = cargo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public permiso_horas getPermiso() {
        return permiso;
    }

    public void setPermiso(permiso_horas permiso) {
        this.permiso = permiso;
    }

    public usuario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(usuario funcionario) {
        this.funcionario = funcionario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
}
