/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author DR-PC
 */
public class MySql {

    private static final String REMITENTE = "soporte.gadmce@gmail.com";
    private static final String CONTRA = "uprbxywwzrpmhvpl";
    private static final String SERVIDOR = "smtp.gmail.com";
    private static Connection enlace;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
//    private static final String url = "jdbc:mysql://192.168.130.17:3306/buzon?allowMultiQueries=true";//PRUEBA
    private static final String url = "jdbc:mysql://192.168.120.16:3306/buzon?allowMultiQueries=true";//PRODUCCIÓN
    private ResultSet rs;
    private PreparedStatement st;
    private static SecureRandom random = new SecureRandom();

    public MySql() {
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

    public boolean enviarCorreoMod(String destinatario, String asunto, String cuerpo) {
        boolean estado = false;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", SERVIDOR);
        props.put("mail.smtp.user", REMITENTE);
        props.put("mail.smtp.clave", CONTRA);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(REMITENTE));
            message.addRecipients(Message.RecipientType.TO, destinatario);
            message.setSubject(asunto);
            message.setText(cuerpo);
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(SERVIDOR, REMITENTE, CONTRA);
                transport.sendMessage(message, message.getAllRecipients());
            }
            estado = true;
        } catch (MessagingException ex) {
            System.out.println("enviarCorreo | " + ex);
        }
        return estado;
    }

    public ArrayList<EstadoSolicitud> getEstados() {
        ArrayList<EstadoSolicitud> res = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM estado";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new EstadoSolicitud(rs.getInt("id"),
                                rs.getString("nombre"))
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getEstados | " + ex);
        }
        return res;
    }

    public ArrayList<CategoriaSolicitud> getCategorias() {
        ArrayList<CategoriaSolicitud> res = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM categoria";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new CategoriaSolicitud(rs.getInt("id"),
                                rs.getString("nombre"))
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCategorias | " + ex);
        }
        return res;
    }

    public ArrayList<SubcategoriaSolicitud> getSubcategorias() {
        ArrayList<SubcategoriaSolicitud> res = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM subcategoria";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new SubcategoriaSolicitud(rs.getInt("id"),
                                rs.getInt("id_categoria"),
                                rs.getString("nombre"))
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSubcategorias | " + ex);
        }
        return res;
    }

    public Solicitud getSolicitud(int id) {
        Solicitud s = new Solicitud();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSolicitud | " + ex);
        }
        return s;
    }

    public ArrayList<Solicitud> getSolicitudes(int estado) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE estado=? ORDER BY creacion " + (estado == 1 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSolicitudes(estado) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getSolicitudes(int estado, int tipo) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE estado=? AND tipo=? ORDER BY creacion " + (estado == 1 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            st.setInt(2, tipo);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSolicitudes(estado,tipo) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getSolicitudes(int estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE estado=? AND DATE(creacion) BETWEEN ? AND ? ORDER BY creacion " + (estado == 1 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSolicitudes(estado,fechaIni,fechaFin) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getSolicitudes(int estado, int tipo, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE estado=? AND tipo=? AND DATE(creacion) BETWEEN ? AND ? ORDER BY creacion " + (estado == 1 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            st.setInt(2, tipo);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getSolicitudes(estado,tipo,fechaIni,fechaFin) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getMisSolicitudes(int idUsuario, int estado) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE gestiona=? AND estado=? ORDER BY creacion " + (estado < 3 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getMisSolicitudes(idUsuario,estado) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getMisSolicitudes(int idUsuario, int estado, int tipo) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE gestiona=? AND estado=? AND tipo=? ORDER BY creacion " + (estado < 3 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            st.setInt(3, tipo);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getMisSolicitudes(idUsuario,estado,tipo) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getMisSolicitudes(int idUsuario, int estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE gestiona=? AND estado=? AND DATE(creacion) BETWEEN ? AND ? ORDER BY creacion " + (estado < 3 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getMisSolicitudes(idUsuario,estado,fechaIni,fechaFin) | " + ex);
        }
        return solicitudes;
    }

    public ArrayList<Solicitud> getMisSolicitudes(int idUsuario, int estado, int tipo, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM v_solicitudes WHERE gestiona=? AND estado=? AND tipo=? AND DATE(creacion) BETWEEN ? AND ? ORDER BY creacion " + (estado < 3 ? "ASC" : "DESC");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            st.setInt(3, tipo);
            st.setDate(4, fechaIni);
            st.setDate(5, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud(
                        rs.getInt("id"),
                        rs.getInt("tipo"),
                        rs.getString("tipo_des"),
                        rs.getInt("estado"),
                        rs.getString("estado_des"),
                        rs.getInt("categoria"),
                        rs.getString("categoria_des"),
                        rs.getInt("subcategoria"),
                        rs.getString("subcategoria_des"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("celular"),
                        rs.getString("direccion"),
                        rs.getString("descripcion"),
                        rs.getString("observacion"),
                        rs.getTimestamp("creacion"),
                        rs.getTimestamp("cierre"),
                        rs.getInt("gestiona")
                );
                String archivos = rs.getString("archivos");
                if (archivos != null) {
                    String[] files = archivos.split(",");
                    for (String f : files) {
                        s.getArchivos().add(new ArchivoSolicitud(f));
                    }
                }
                solicitudes.add(s);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getMisSolicitudes(idUsuario,estado,tipo,fechaIni,fechaFin) | " + ex);
        }
        return solicitudes;
    }

    public int registrarSolicitud(Solicitud s) throws SQLException {
        String sql = "INSERT INTO solicitud(tipo,id_subcategoria,cedula,nombre,correo,celular,direccion,descripcion) VALUES(?,?,?,?,?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, s.getTipo());
        st.setInt(2, s.getSubcategoria().getId());
        st.setString(3, s.getCedula());
        st.setString(4, s.getNombre());
        st.setString(5, s.getCorreo());
        st.setString(6, s.getCelular());
        st.setString(7, s.getDireccion());
        st.setString(8, s.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        rs.close();
        st.close();
        s = getSolicitud(res);
        enviarCorreoMod(s.getCorreo(), s.getTipoDes() + " #" + s.getId() + " REGISTRADA", "Su " + s.getTipoDes().toLowerCase() + " fue registrada en el Buzón Ciudadano.");
        return res;
    }

    public boolean eliminarSolicitud(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM solicitud WHERE id=?;DELETE FROM archivo WHERE id_solicitud=?;");
            st.setInt(1, id);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarSolicitud | " + ex);
        }
        return false;
    }

    public int registrarArchivo(ArchivoSolicitud a) throws SQLException {
        String sql = "INSERT INTO archivo(id_solicitud,path) VALUES(?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, a.getId_denuncia());
        st.setString(2, a.getPath());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        rs.close();
        st.close();
        return res;
    }

    public void gestionarSolicitud(int idUsuario, Solicitud s) throws SQLException {
        st = enlace.prepareStatement("UPDATE solicitud SET estado=2,gestiona=? WHERE id=? AND gestiona IS NULL");
        st.setInt(1, idUsuario);
        st.setInt(2, s.getId());
        st.executeUpdate();
        st.close();
        s = getSolicitud(s.getId());
        enviarCorreoMod(s.getCorreo(), s.getTipoDes() + " #" + s.getId() + " EN REVISIÓN", "Su " + s.getTipoDes().toLowerCase() + " está siendo revisada.");
    }

    public void cancelarGestionSolicitud(int idUsuario, Solicitud s) throws SQLException {
        st = enlace.prepareStatement("UPDATE solicitud SET estado=1,gestiona=NULL WHERE gestiona=? AND id=?");
        st.setInt(1, idUsuario);
        st.setInt(2, s.getId());
        st.executeUpdate();
        st.close();
    }

    public void observarSolicitud(Solicitud s) throws SQLException {
        st = enlace.prepareStatement("UPDATE solicitud SET estado=?,observacion=?,cierre=NOW() WHERE id=?");
        st.setInt(1, s.getEstado().getId());
        st.setString(2, s.getObservacion());
        st.setInt(3, s.getId());
        st.executeUpdate();
        st.close();
        s = getSolicitud(s.getId());
        enviarCorreoMod(s.getCorreo(), s.getTipoDes() + " #" + s.getId() + " " + s.getEstado().getNombre(), "Su " + s.getTipoDes().toLowerCase() + " fue " + s.getEstado().getNombre().toLowerCase() + " con la siguiente observación: " + s.getObservacion());
    }
}
