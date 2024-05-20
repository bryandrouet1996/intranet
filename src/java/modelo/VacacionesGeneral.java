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
public class VacacionesGeneral {

    private permiso_vacaciones vacacion;
    private usuario funcionario;
    private String motivo;

    public VacacionesGeneral() {
        this.vacacion = new permiso_vacaciones();
        this.funcionario = new usuario();
        this.motivo = "";
    }

    public VacacionesGeneral(permiso_vacaciones vacacion, usuario funcionario, String motivo) {
        this.vacacion = vacacion;
        this.funcionario = funcionario;
        this.motivo = motivo;
    }

    public permiso_vacaciones getVacacion() {
        return vacacion;
    }

    public void setVacacion(permiso_vacaciones vacacion) {
        this.vacacion = vacacion;
    }

    public usuario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(usuario funcionario) {
        this.funcionario = funcionario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

}
