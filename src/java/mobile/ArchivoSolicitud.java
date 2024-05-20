/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

/**
 *
 * @author USUARIO
 */
public class ArchivoSolicitud {

    private int id;
    private int id_denuncia;
    private String path;

    public ArchivoSolicitud(String path) {
        this.path = path;
    }

    public ArchivoSolicitud(int id_denuncia, String path) {
        this.id_denuncia = id_denuncia;
        this.path = path;
    }

    public ArchivoSolicitud(int id, int id_denuncia, String path) {
        this.id = id;
        this.id_denuncia = id_denuncia;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_denuncia() {
        return id_denuncia;
    }

    public void setId_denuncia(int id_denuncia) {
        this.id_denuncia = id_denuncia;
    }

}
