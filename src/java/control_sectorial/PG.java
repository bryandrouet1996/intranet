/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_sectorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USUARIO
 */
public class PG {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://192.168.120.6/control_sectorial";//PRODUCCIÓN
    private static final String USER = "u_control_sectorial";//PRODUCCIÓN
    private static final String PASS = "@root1234";//PRODUCCIÓN
//    private static final String URL = "jdbc:postgresql://localhost/control_sectorial";//PRUEBA
//    private static final String USER = "postgres";//PRUEBA
//    private static final String PASS = "root1234";//PRUEBA
    private static Connection enlace = null;
    private ResultSet rs;
    private PreparedStatement st;

    public PG() {
        conectar();
    }

    private static void conectar() {
        try {
            if (enlace == null || !enlace.isValid(0)) {
                Class.forName(DRIVER);
                enlace = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("PG | " + ex);
            enlace = null;
        }
    }

    public static Connection getEnlace() {
        return enlace;
    }

    public static void cerrarConexion() {
        try {
            enlace.close();
        } catch (SQLException ex) {
            System.out.println("cerrarConexion | " + ex);
        }
    }

    public ArrayList<Parroquia> getParroquias() {
        final ArrayList<Parroquia> res = new ArrayList<>();
        try {
            final String query = "SELECT * FROM v_parroquias";
            st = enlace.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                final Parroquia p = new Parroquia();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                res.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getParroquias | " + ex);
        }
        return res;
    }

    public ArrayList<Sector> getSectores() {
        final ArrayList<Sector> res = new ArrayList<>();
        try {
            final String query = "SELECT * FROM v_sectores";
            st = enlace.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                final Sector s = new Sector();
                s.setId(rs.getInt("id"));
                s.setId_parroquia(rs.getInt("id_parroquia"));
                s.setNombre(rs.getString("nombre"));
                res.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSectores | " + ex);
        }
        return res;
    }

    public ArrayList<Barrio> getBarrios() {
        final ArrayList<Barrio> res = new ArrayList<>();
        try {
            final String query = "SELECT * FROM v_barrios";
            st = enlace.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                final Barrio b = new Barrio();
                b.setId(rs.getInt("id"));
                b.setId_sector(rs.getInt("id_sector"));
                b.setNombre(rs.getString("nombre"));
                res.add(b);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getBarrios | " + ex);
        }
        return res;
    }

    public int registrarFicha(final Ficha f) throws Exception {
        try {
            final String query = "INSERT INTO ficha(id_barrio,id_creador,nombre_creador,visita_alcalde, brigada_medica, brigada_veterinaria, olla_solidaria, minga_ciudadana, capacitacion, wifi) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            st = enlace.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, f.getId_barrio());
            st.setInt(2, f.getId_creador());
            st.setString(3, f.getNombre_creador());
            st.setBoolean(4, f.isVisita_alcalde());
            st.setBoolean(5, f.isBrigada_medica());
            st.setBoolean(6, f.isBrigada_veterinaria());
            st.setBoolean(7, f.isOlla_solidaria());
            st.setBoolean(8, f.isMinga_ciudadana());
            st.setBoolean(9, f.isCapacitacion());
            st.setBoolean(10, f.isWifi());
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            rs.next();
            final int res = rs.getInt(1);
            st.close();
            rs.close();
            return res;
        } catch (Exception e) {
            System.out.println("registrarFicha | " + e);
            throw new Exception("Error al registrar ficha");
        }
    }

    public Ficha getFicha(int id_barrio) {
        final Ficha f = new Ficha();
        try {
            final String query = "SELECT * FROM public.v_fichas_historial WHERE id_barrio=? LIMIT 1";
            st = enlace.prepareStatement(query);
            st.setInt(1, id_barrio);
            rs = st.executeQuery();
            while (rs.next()) {
                f.setId(rs.getInt("id"));
                f.setId_barrio(rs.getInt("id_barrio"));
                f.setId_creador(rs.getInt("id_creador"));
                final Barrio b = new Barrio();
                b.setId(rs.getInt("id_barrio"));
                b.setNombre(rs.getString("nombre_barrio"));
                f.setBarrio(b);
                f.setId_sector(rs.getInt("id_sector"));
                f.setId_parroquia(rs.getInt("id_parroquia"));
                f.setVisita_alcalde(rs.getBoolean("visita_alcalde"));
                f.setBrigada_medica(rs.getBoolean("brigada_medica"));
                f.setBrigada_veterinaria(rs.getBoolean("brigada_veterinaria"));
                f.setOlla_solidaria(rs.getBoolean("olla_solidaria"));
                f.setMinga_ciudadana(rs.getBoolean("minga_ciudadana"));
                f.setCapacitacion(rs.getBoolean("capacitacion"));
                f.setWifi(rs.getBoolean("wifi"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFicha(id_barrio) | " + ex);
        }
        return f;
    }

    public Ficha getFichaHistorial(int id) {
        final Ficha f = new Ficha();
        try {
            final String query = "SELECT * FROM public.v_fichas_historial WHERE id=? LIMIT 1";
            st = enlace.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                f.setId(rs.getInt("id"));
                f.setId_barrio(rs.getInt("id_barrio"));
                f.setId_creador(rs.getInt("id_creador"));
                final Barrio b = new Barrio();
                b.setId(rs.getInt("id_barrio"));
                b.setNombre(rs.getString("nombre_barrio"));
                f.setBarrio(b);
                f.setId_sector(rs.getInt("id_sector"));
                f.setId_parroquia(rs.getInt("id_parroquia"));
                f.setVisita_alcalde(rs.getBoolean("visita_alcalde"));
                f.setBrigada_medica(rs.getBoolean("brigada_medica"));
                f.setBrigada_veterinaria(rs.getBoolean("brigada_veterinaria"));
                f.setOlla_solidaria(rs.getBoolean("olla_solidaria"));
                f.setMinga_ciudadana(rs.getBoolean("minga_ciudadana"));
                f.setCapacitacion(rs.getBoolean("capacitacion"));
                f.setWifi(rs.getBoolean("wifi"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichaHistorial(id) | " + ex);
        }
        return f;
    }

    public ArrayList<Ficha> getFichasHistorial() {
        ArrayList<Ficha> res = new ArrayList<>();
        try {
            final String query = "SELECT * FROM public.v_fichas_historial";
            st = enlace.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                final Ficha f = new Ficha();
                f.setId(rs.getInt("id"));
                f.setId_barrio(rs.getInt("id_barrio"));
                f.setId_creador(rs.getInt("id_creador"));
                final Barrio b = new Barrio();
                b.setId(rs.getInt("id_barrio"));
                b.setNombre(rs.getString("nombre_barrio"));
                f.setBarrio(b);
                f.setId_sector(rs.getInt("id_sector"));
                final Sector s = new Sector();
                s.setId(rs.getInt("id_sector"));
                s.setNombre(rs.getString("nombre_sector"));
                f.setSector(s);
                f.setId_parroquia(rs.getInt("id_parroquia"));
                final Parroquia p = new Parroquia();
                p.setId(rs.getInt("id_parroquia"));
                p.setNombre(rs.getString("nombre_parroquia"));
                f.setParroquia(p);
                f.setVisita_alcalde(rs.getBoolean("visita_alcalde"));
                f.setBrigada_medica(rs.getBoolean("brigada_medica"));
                f.setBrigada_veterinaria(rs.getBoolean("brigada_veterinaria"));
                f.setOlla_solidaria(rs.getBoolean("olla_solidaria"));
                f.setMinga_ciudadana(rs.getBoolean("minga_ciudadana"));
                f.setCapacitacion(rs.getBoolean("capacitacion"));
                f.setWifi(rs.getBoolean("wifi"));
                f.setServicios_entregados(rs.getInt("servicios_entregados"));
                res.add(f);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichasHistorial | " + ex);
        }
        return res;
    }

    public ArrayList<Ficha> getFichasActualizadas() {
        ArrayList<Ficha> res = new ArrayList<>();
        try {
            final String query = "SELECT * FROM public.v_fichas_actualizadas";
            st = enlace.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                final Ficha f = new Ficha();
                f.setId(rs.getInt("id"));
                f.setId_barrio(rs.getInt("id_barrio"));
                f.setId_creador(rs.getInt("id_creador"));
                final Barrio b = new Barrio();
                b.setId(rs.getInt("id_barrio"));
                b.setNombre(rs.getString("nombre_barrio"));
                f.setBarrio(b);
                f.setId_sector(rs.getInt("id_sector"));
                final Sector s = new Sector();
                s.setId(rs.getInt("id_sector"));
                s.setNombre(rs.getString("nombre_sector"));
                f.setSector(s);
                f.setId_parroquia(rs.getInt("id_parroquia"));
                final Parroquia p = new Parroquia();
                p.setId(rs.getInt("id_parroquia"));
                p.setNombre(rs.getString("nombre_parroquia"));
                f.setParroquia(p);
                f.setVisita_alcalde(rs.getBoolean("visita_alcalde"));
                f.setBrigada_medica(rs.getBoolean("brigada_medica"));
                f.setBrigada_veterinaria(rs.getBoolean("brigada_veterinaria"));
                f.setOlla_solidaria(rs.getBoolean("olla_solidaria"));
                f.setMinga_ciudadana(rs.getBoolean("minga_ciudadana"));
                f.setCapacitacion(rs.getBoolean("capacitacion"));
                f.setWifi(rs.getBoolean("wifi"));
                f.setServicios_entregados(rs.getInt("servicios_entregados"));
                res.add(f);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichasActualizadas | " + ex);
        }
        return res;
    }
}
