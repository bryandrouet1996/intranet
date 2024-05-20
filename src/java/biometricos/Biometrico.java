package biometricos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class Biometrico {

    private static Connection enlace = null;
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String USER = "usuario_bio";
    private static final String PASSWORD = "@super_pass";
    private static final String PORT = "1433";
    private static final String SERVER_PORT = "192.168.120.24:" + PORT;//PRODUCCIÓN
//    private static final String SERVER_PORT = "127.0.0.1:" + PORT;//LOCAL
    private static final String DB = "biometricos";
    private static final String URL = "jdbc:sqlserver://" + SERVER_PORT + ";databaseName=" + DB + ";user=" + USER + ";password=" + PASSWORD;
    private ResultSet rs;
    private PreparedStatement st;

    public Biometrico() {
        abrirConexion();
    }

    private static void abrirConexion() {
        try {
            if (enlace == null || !enlace.isValid(0)) {
                Class.forName(DRIVER);
                enlace = DriverManager.getConnection(URL);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("abrirConexion | " + ex);
            enlace = null;
        }
    }

    private static void cerrarConexion() {
        try {
            enlace.close();
        } catch (SQLException ex) {
            System.out.println("cerrarConexion | " + ex);
        }
    }

    public ArrayList<Asistencia> getAsistencias() {
        abrirConexion();
        final ArrayList<Asistencia> res = new ArrayList();
        try {
            final String sql = "SELECT * FROM v_asistencias WHERE fecha=CAST(GETDATE() AS date) ORDER BY funcionario, creacion";
            st = enlace.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Asistencia(
                                rs.getString("id"),
                                rs.getString("codigo_funcionario"),
                                rs.getString("funcionario"),
                                rs.getString("cod_direccion_funcionario"),
                                rs.getString("direccion_funcionario"),
                                rs.getString("biometrico"),
                                rs.getString("fecha"),
                                rs.getString("hora").substring(0, 8)
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getAsistencias | " + e);
            return null;
        }
        cerrarConexion();
        return res;
    }

    public ArrayList<Asistencia> getAsistencias(final String codigo_usuario) {
        abrirConexion();
        final ArrayList<Asistencia> res = new ArrayList();
        try {
            final String sql = "SELECT * FROM v_asistencias WHERE codigo_funcionario=? AND fecha BETWEEN CAST(DATEADD(day,-30,GETDATE()) AS date) AND CAST(GETDATE()AS date) ORDER BY creacion";
            st = enlace.prepareStatement(sql);
            st.setString(1, codigo_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Asistencia(
                                rs.getString("id"),
                                rs.getString("codigo_funcionario"),
                                rs.getString("funcionario"),
                                rs.getString("cod_direccion_funcionario"),
                                rs.getString("direccion_funcionario"),
                                rs.getString("biometrico"),
                                rs.getString("fecha"),
                                rs.getString("hora").substring(0, 8)
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getAsistencias(codigo_usuario) | " + e);
            return null;
        }
        cerrarConexion();
        return res;
    }

    public ArrayList<Asistencia> getAsistencias(final Date fecha_inicio, final Date fecha_fin) {
        abrirConexion();
        final ArrayList<Asistencia> res = new ArrayList();
        try {
            final String sql = "SELECT * FROM v_asistencias WHERE fecha BETWEEN ? AND ? ORDER BY funcionario, creacion";
            st = enlace.prepareStatement(sql);
            st.setDate(1, fecha_inicio);
            st.setDate(2, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Asistencia(
                                rs.getString("id"),
                                rs.getString("codigo_funcionario"),
                                rs.getString("funcionario"),
                                rs.getString("cod_direccion_funcionario"),
                                rs.getString("direccion_funcionario"),
                                rs.getString("biometrico"),
                                rs.getString("fecha"),
                                rs.getString("hora").substring(0, 8)
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getAsistencias(fecha_inicio,fecha_fin) | " + e);
            return null;
        }
        cerrarConexion();
        return res;
    }

    public ArrayList<Asistencia> getAsistencias(final String codigo_usuario, final Date fecha_inicio, final Date fecha_fin) {
        abrirConexion();
        final ArrayList<Asistencia> res = new ArrayList();
        try {
            final String sql = "SELECT * FROM v_asistencias WHERE codigo_funcionario=? AND fecha BETWEEN ? AND ? ORDER BY creacion";
            st = enlace.prepareStatement(sql);
            st.setString(1, codigo_usuario);
            st.setDate(2, fecha_inicio);
            st.setDate(3, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Asistencia(
                                rs.getString("id"),
                                rs.getString("codigo_funcionario"),
                                rs.getString("funcionario"),
                                rs.getString("cod_direccion_funcionario"),
                                rs.getString("direccion_funcionario"),
                                rs.getString("biometrico"),
                                rs.getString("fecha"),
                                rs.getString("hora").substring(0, 8)
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getAsistencias(codigo_usuario,fecha_inicio,fecha_fin) | " + e);
            return null;
        }
        cerrarConexion();
        return res;
    }
}
