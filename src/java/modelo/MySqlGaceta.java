/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author DR-PC
 */
public class MySqlGaceta {

    private static Connection enlace;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
//    private static final String url = "jdbc:mysql://192.168.130.17:3306/gaceta_tributaria?allowMultiQueries=true";//PRUEBA
    private static final String url = "jdbc:mysql://192.168.120.16:3306/gaceta_tributaria?allowMultiQueries=true";//PRODUCCIÓN
    private ResultSet rs;
    private PreparedStatement st;

    public MySqlGaceta() {
        enlace = null;
        try {
            Class.forName(driver);
            enlace = DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("conexion | " + ex);
        }
    }

    public static Connection getEnlace() {
        return enlace;
    }

    public void setAutoCommit(final boolean commit) {
        try {
            enlace.setAutoCommit(commit);
        } catch (SQLException e) {
            System.out.println("setAutoCommit | " + e);
        }
    }

    public void confirmCommit() {
        try {
            enlace.commit();
            setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("confirmCommit | " + e);
        }
    }

    public void rollback() {
        try {
            enlace.rollback();
            setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("rollback | " + e);
        }
    }

    public ArrayList<Notificacion> getNotificaciones() {
        ArrayList<Notificacion> res = new ArrayList<>();
        try {
            final String sentencia = "SELECT * FROM notificacion";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Notificacion(
                                rs.getInt("id"),
                                rs.getString("tipo_doc"),
                                rs.getString("numero_doc"),
                                rs.getInt("ciu"),
                                rs.getString("identificacion"),
                                rs.getString("razon_social"),
                                rs.getString("identificacion_representante"),
                                rs.getString("nombre_representante"),
                                rs.getFloat("valor"),
                                rs.getString("fecha")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getNotificaciones | " + ex);
        }
        return res;
    }

    public void registrarNotificacion(Notificacion n) throws Exception {
        try {
            final String sql = "INSERT INTO notificacion(tipo_doc,numero_doc,ciu,identificacion,razon_social,identificacion_representante,nombre_representante,valor,fecha) VALUES(?,?,?,?,?,?,?,?,?)";
            st = enlace.prepareStatement(sql);
            st.setString(1, n.getTipoDocumento());
            st.setString(2, n.getNumeroDocumento());
            st.setInt(3, n.getCiu());
            st.setString(4, n.getIdentificacion());
            st.setString(5, n.getRazonSocial());
            if (n.getIdentificacionRepresentante().equals("")) {
                st.setNull(6, Types.VARCHAR);
            } else {
                st.setString(6, n.getIdentificacionRepresentante());
            }
            if (n.getNombreRepresentante().equals("")) {
                st.setNull(7, Types.VARCHAR);
            } else {
                st.setString(7, n.getNombreRepresentante());
            }
            st.setFloat(8, n.getValor());
            if (n.getFecha().equals("")) {
                st.setNull(9, Types.VARCHAR);
            } else {
                st.setString(9, n.getFecha());
            }
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("registrarNotificacion | " + e);
            throw new Exception("Error al registrar notificacion");
        }
    }
}
