package modelo;

import java.sql.Timestamp;

public class DestinatarioDocumento {

    private int id;
    private int id_documento;
    private int id_usuario;
    private String correo_usuario;
    private int tipo = 1;
    private int estado = 1;
    private Timestamp leido;
    private Timestamp creacion;
    private boolean actualizar = false;
    private boolean eliminar = false;

    public DestinatarioDocumento() {
    }

    public DestinatarioDocumento(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public DestinatarioDocumento(int id_usuario, int tipo) {
        this.id_usuario = id_usuario;
        this.tipo = tipo;
    }

    public DestinatarioDocumento(int id_documento, int id_usuario, int tipo) {
        this.id_documento = id_documento;
        this.id_usuario = id_usuario;
        this.tipo = tipo;
    }

    public DestinatarioDocumento(int id, int id_documento, int id_usuario, String correo_usuario, int tipo, int estado, Timestamp leido, Timestamp creacion) {
        this.id = id;
        this.id_documento = id_documento;
        this.id_usuario = id_usuario;
        this.correo_usuario = correo_usuario;
        this.tipo = tipo;
        this.estado = estado;
        this.leido = leido;
        this.creacion = creacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public boolean isActualizar() {
        return actualizar;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }

    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    public Timestamp getLeido() {
        return leido;
    }

    public void setLeido(Timestamp leido) {
        this.leido = leido;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

}
