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

/**
 *
 * @author DR-PC
 */
public class ConexionVentanilla {

    private static Connection enlace = null;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
    private static final String url = "jdbc:mysql://192.168.120.16:3306/servicio_ciudadano?useUnicode=true&characterEncoding=UTF-8";//PRODUCCIÓN
    private ResultSet rs;
    private PreparedStatement st;

    public ConexionVentanilla() {
        try {
            Class.forName(driver);
            enlace = DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean existeFallecido(String cedula) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT * FROM fallecido WHERE CEDULA=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, cedula);
            rs = st.executeQuery();
            while (rs.next()) {
                res = true;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("existeFallecido | " + ex);
        }
        return res;
    }

    public boolean insertarFallecido(String cedula) {
        try {
            String sentencia = "INSERT INTO fallecido(CEDULA) VALUES(?)";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, cedula);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("insertarFallecido | " + ex);
        }
        return false;
    }

}
