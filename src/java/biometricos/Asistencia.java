/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricos;

/**
 *
 * @author USUARIO
 */
public class Asistencia {

    private String id;
    private String codigo_funcionario;
    private String funcionario;
    private String cod_direccion_funcionario;
    private String direccion_funcionario;
    private String biometrico;
    private String fecha;
    private String hora;

    public Asistencia() {
    }

    public Asistencia(String id, String codigo_funcionario, String funcionario, String cod_direccion_funcionario, String direccion_funcionario, String biometrico, String fecha, String hora) {
        this.id = id;
        this.codigo_funcionario = codigo_funcionario;
        this.funcionario = funcionario;
        this.cod_direccion_funcionario = cod_direccion_funcionario;
        this.direccion_funcionario = direccion_funcionario;
        this.biometrico = biometrico;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo_funcionario() {
        return codigo_funcionario;
    }

    public void setCodigo_funcionario(String codigo_funcionario) {
        this.codigo_funcionario = codigo_funcionario;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getCod_direccion_funcionario() {
        return cod_direccion_funcionario;
    }

    public void setCod_direccion_funcionario(String cod_direccion_funcionario) {
        this.cod_direccion_funcionario = cod_direccion_funcionario;
    }

    public String getDireccion_funcionario() {
        return direccion_funcionario;
    }

    public void setDireccion_funcionario(String direccion_funcionario) {
        this.direccion_funcionario = direccion_funcionario;
    }

    public String getBiometrico() {
        return biometrico;
    }

    public void setBiometrico(String biometrico) {
        this.biometrico = biometrico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    
}
