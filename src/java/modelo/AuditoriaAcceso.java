package modelo;

import java.sql.Timestamp;

public class AuditoriaAcceso {
    private int id_usuario;
    private String descripcion;
    private Timestamp creacion;

    public AuditoriaAcceso() {
    }

    public AuditoriaAcceso(int id_usuario, String descripcion) {
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }
    
    
}
