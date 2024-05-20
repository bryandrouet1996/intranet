/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.administrar_documento;
import enums.StatusEnum;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;

/**
 *
 * @author DR-PC
 */
public class conexion {

    private static Connection enlace;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
    //private static final String contrasena = "";
    private static final String url = "jdbc:mysql://192.168.120.16:3306/sys_intranet?allowMultiQueries=true"; //PRODUCCIÓN
//    private static final String url = "jdbc:mysql://192.168.130.17:3306/sys_intranet?allowMultiQueries=true"; //PRUEBA
    //private static final String url = "jdbc:mysql://localhost:3306/sys_intranet?allowMultiQueries=true"; //LOCAL
    private ResultSet rs;
    private PreparedStatement st;
    CallableStatement stmt = null;
    private static SecureRandom random = new SecureRandom();
    private BotSession sesion = null;
    private static final String SERVIDOR_MAIL = "smtp.gmail.com";
    private static final String REMITENTE_MAIL = "soporte.gadmce@gmail.com";
    private static final String PASS_MAIL = "xpeqltktacmibchn";
    private static final String FINAL_CONTENIDO = "\n\nEste es un correo automático, no lo respondas.\n\nAlcaldía Ciudadana de Esmeraldas - Dirección de Tecnologías de la Información";

    public conexion() {
        enlace = null;
        try {
            Class.forName(driver);
            enlace = DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("conexion | " + ex);
        }
        iniciarBot();
    }

    public java.sql.Date fechaActual() {
        java.util.Date fecha = new java.util.Date();
        java.sql.Date fecha_c = new java.sql.Date(fecha.getTime());
        return fecha_c;
    }

    public Timestamp fechaActualStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
    }

    public String hora_actual() {
        String hora = null;
        Calendar c = new GregorianCalendar();
        String horas = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String minutos = Integer.toString(c.get(Calendar.MINUTE));
        int min = Integer.parseInt(minutos);
        if (min < 10) {
            hora = horas + ":" + "0" + minutos;
        } else {
            hora = horas + ":" + minutos;
        }
        return hora;
    }

    public int diferenciaDias(java.sql.Date fecha_inicio, java.sql.Date fecha_final) {
        int dias = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date fechaInicial = null;
        Date fechaFinal = null;
        try {
            fechaInicial = dateFormat.parse(fecha_inicio.toString());
            fechaFinal = dateFormat.parse(fecha_final.toString());
            if (fecha_inicio.getMonth() != fecha_final.getMonth()) {
                dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
                return dias;
            }
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
        return dias + 1;
    }

    public int diferenciaRangoFecha(Date fecha_inicio, Date fecha_final) {
        int dias = 0;
        try {
            String sentencia;
            sentencia = "SELECT TIMESTAMPDIFF(DAY, '" + fecha_inicio + "','" + fecha_final + "') AS dias_transcurridos;";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                dias = rs.getInt("dias_transcurridos");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return dias + 1;
    }

    public String diferenciaHoras(String inicio, String fin) {
        Calendar c = Calendar.getInstance();
        try {
            Date dateInicio = new SimpleDateFormat().parse("19/05/2006 " + inicio + ":00");
            Date dateFinal = new SimpleDateFormat().parse("19/05/2006 " + fin + ":00");
            long milliseconds = dateFinal.getTime() - dateInicio.getTime();
            int seconds = (int) (milliseconds / 1000) % 60;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
            c.set(Calendar.SECOND, seconds);
            c.set(Calendar.MINUTE, minutes);
            c.set(Calendar.HOUR_OF_DAY, hours);
        } catch (ParseException ex) {
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "" + c.getTime().getHours() + ":" + c.getTime().getMinutes();
    }

    public String diferenciaTiempo(String inicio, String fin) {
        String tiempo = null;
        try {
            String sentencia;
            sentencia = "SELECT TIMEDIFF('" + inicio + "', '" + fin + "') as tiempo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tiempo = rs.getString("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return tiempo;
    }

    public String mesActualNombre() {
        Month mes = LocalDate.now().getMonth();
        String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        return nombre;
    }

    public String anioActual() {
        return "" + LocalDate.now().getYear();
    }

    public usuario buscar_usuarioID(int id) {
        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscar_usuarioID | " + ex);
        }
        return p;
    }

    public usuario getUsuario(int codigo) {
        usuario res = null;
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario WHERE codigo_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("codigo_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("correo"),
                        rs.getDate("fecha_nacimiento")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuario | " + ex);
        }
        return res;
    }

    public usuario buscar_usuarioCorreo(String correo) {

        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE correo= '" + correo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscar_usuarioCorreo | " + ex);
        }

        return p;
    }

    public boolean existeUsuario(String correo) {

        boolean confirmar = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE correo= '" + correo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                if (rs.getRow() > 0) {
                    confirmar = true;
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return confirmar;
    }

    public ArrayList<usuario> listadoMaximaAutoridad() {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad WHERE organizacion.nivel_hijo='D.02'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosDireccion() {
        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario ORDER BY apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoUsuariosDireccion | " + ex);
        }
        return listado;
    }

    public ArrayList<inventario> listadoInventario() {

        ArrayList<inventario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM inventario ORDER BY nombre DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                inventario p = new inventario();
                p.setId_inventario(rs.getInt("id_inventario"));
                p.setNombre(rs.getString("nombre"));
                p.setUsuariodominio(rs.getString("usuario_dominio"));
                p.setTipodispositivo(rs.getString("tipo_dispositivo"));
                p.setMacaddress(rs.getString("macaddress"));
                p.setMemoria(rs.getString("memoria"));
                p.setProcesador(rs.getString("procesador"));
                p.setDireccion_ip(rs.getString("direccion_ip"));
                p.setConexion_dhcp(rs.getString("conexion_dhcp"));
                p.setConexion_permanente(rs.getString("conexion_permanente"));
                p.setAntivirus(rs.getString("antivirus"));
                p.setCabildo(rs.getString("cabildo"));
                p.setSigame(rs.getString("sigame"));
                p.setOffice365(rs.getString("office365"));
                p.setArquitectura_so(rs.getString("arquitectura_so"));
                p.setCodigo_bodega(rs.getString("codigo_bodega"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setNombre_edificio(rs.getString("nombre_edificio"));
                p.setPiso(rs.getString("piso"));
                p.setUnidad_administrativa(rs.getString("unidad_administrativa"));
                p.setFuncionario(rs.getString("funcionario"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public inventario buscar_inventarioID(int id) {

        inventario p = new inventario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM inventario WHERE id_inventario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_inventario(rs.getInt("id_inventario"));
                p.setNombre(rs.getString("nombre"));
                p.setUsuariodominio(rs.getString("usuario_dominio"));
                p.setTipodispositivo(rs.getString("tipo_dispositivo"));
                p.setMacaddress(rs.getString("macaddress"));
                p.setMemoria(rs.getString("memoria"));
                p.setProcesador(rs.getString("procesador"));
                p.setDireccion_ip(rs.getString("direccion_ip"));
                p.setConexion_dhcp(rs.getString("conexion_dhcp"));
                p.setConexion_permanente(rs.getString("conexion_permanente"));
                p.setAntivirus(rs.getString("antivirus"));
                p.setCabildo(rs.getString("cabildo"));
                p.setSigame(rs.getString("sigame"));
                p.setOffice365(rs.getString("office365"));
                p.setArquitectura_so(rs.getString("arquitectura_so"));
                p.setCodigo_bodega(rs.getString("codigo_bodega"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setNombre_edificio(rs.getString("nombre_edificio"));
                p.setPiso(rs.getString("piso"));
                p.setUnidad_administrativa(rs.getString("unidad_administrativa"));
                p.setFuncionario(rs.getString("funcionario"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return p;
    }

    public usuario buscarUsuarioCedula(String cedula) {

        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE cedula= '" + cedula + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public usuario getUsuarioId(int id) {
        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario WHERE id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
                p.setCodigo_funcion(rs.getString("codigo_funcion"));
                p.setFuncion(rs.getString("funcion"));
                p.setFirma(rs.getString("firma"));
                p.setTipo_funcion(rs.getInt("tipo_funcion"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuarioId | " + ex);
        }
        return p;
    }

    public ArrayList<cargo> listadoCargos() {

        ArrayList<cargo> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM cargo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                cargo p = new cargo();
                p.setId_cargo(rs.getInt("id_cargo"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setDescripcion(rs.getString("descripcion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<tipo_equipo> listadoTipoEquipos() {
        ArrayList<tipo_equipo> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_equipo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_equipo p = new tipo_equipo();
                p.setId_tipo_equipo(rs.getInt("id_tipo_equipo"));
                p.setTipo(rs.getString("tipo"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<estado_conexion> listadoEstadoConexion() {
        ArrayList<estado_conexion> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM estado_conexion";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado_conexion p = new estado_conexion();
                p.setId_estado(rs.getInt("id_estado"));
                p.setDhcp(rs.getString("dhcp"));
                p.setPermanente(rs.getString("permanente"));
                p.setAntivirus(rs.getString("antivirus"));
                p.setCabildo(rs.getString("cabildo"));
                p.setSigame(rs.getString("sigame"));
                p.setOffice365(rs.getString("office365"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<arquitectura> listadoArquitecturaSo() {
        ArrayList<arquitectura> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM arquitectura";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                arquitectura p = new arquitectura();
                p.setId_arquitecturaso(rs.getInt("id_arquitecturaso"));
                p.setArquitectura_so(rs.getString("arquitectura_so"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<edificio> listadoEdificio() {
        ArrayList<edificio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM edificio";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                edificio p = new edificio();
                p.setId_edificio(rs.getInt("id_edificio"));
                p.setNombre_edificio(rs.getString("nombre_edificio"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<pisos> listadoPisos() {
        ArrayList<pisos> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM pisos";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                pisos p = new pisos();
                p.setId_piso(rs.getInt("id_piso"));
                p.setNombre_piso(rs.getString("nombre_piso"));

                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<organizacion> listadoUnidades() {

        ArrayList<organizacion> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM organizacion";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                organizacion p = new organizacion();
                p.setId_organizacion(rs.getInt("id_organizacion"));
                p.setNivel_padre(rs.getString("nivel_padre"));
                p.setNivel_hijo(rs.getString("nivel_hijo"));
                p.setNombre(rs.getString("nombre"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<tipo_viatico> listadoTiposViaticos() {

        ArrayList<tipo_viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_viatico";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_viatico p = new tipo_viatico();
                p.setId_viatico(rs.getInt("id_viatico"));
                p.setDescripcion(rs.getString("descripcion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public foto_usuario buscarFotoUsuarioID(int id) {

        foto_usuario foto = new foto_usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM foto_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                foto.setId_foto(rs.getInt("id_foto"));
                foto.setId_usuario(rs.getInt("id_usuario"));
                foto.setRuta(rs.getString("ruta"));
                foto.setNombre(rs.getString("nombre"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return foto;
    }

    public historia buscarFotoHistoriaID(int id) {

        historia elemento = new historia();
        try {
            String sentencia;
            sentencia = "SELECT *FROM historia WHERE id_historia= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_historia(rs.getInt("id_historia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setToken(rs.getString("token"));
                elemento.setFecha_subida(rs.getDate("fecha_subida"));
                elemento.setHora_subida(rs.getString("hora_subida"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public boolean accesoUsuario(int id) {

        boolean acceso = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permiso_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                acceso = rs.getBoolean("acceso");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return acceso;
    }

    public String tipoUsuario(int id) {

        String tipo = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_usuario INNER JOIN usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN rol ON rol.id_rol=tipo_usuario.id_rol WHERE usuario.id_usuario= '" + id + "' LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo = rs.getString("rol.descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tipo;
    }

    public boolean isTipoUsuarioID(int id_usuario, int id_rol) {
        int contador = 0;
        boolean confirmar = false;
        try {
            String sentencia;
            sentencia = "SELECT * FROM tipo_usuario WHERE id_usuario=" + id_usuario + " AND id_rol=" + id_rol;
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmar = true;
        }

        return confirmar;
    }

    public int tipoUsuarioID(int id) {

        int tipo = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_usuario INNER JOIN usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN rol ON rol.id_rol=tipo_usuario.id_rol WHERE usuario.id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo = rs.getInt("rol.id_rol");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tipo;
    }

    public boolean registroEstadoUsuario(estado_usuario ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO estado_usuario(id_usuario,fecha_acceso,hora_acceso) VALUES(?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setDate(2, ph.getFecha_acceso());
        st.setString(3, ph.getHora_acceso());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<destino> listadoPaisesCiudades() {

        ArrayList<destino> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM pais INNER JOIN ciudad ON ciudad.PaisCodigo=pais.PaisCodigo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                destino elemento = new destino();
                elemento.setId_destino(rs.getInt("ciudad.CiudadID"));
                elemento.setNombre_pais(rs.getString("pais.PaisNombre"));
                elemento.setNombre_ciudad(rs.getString("ciudad.CiudadNombre"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean actualizarUsuario(usuario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE usuario SET nombre=?,correo=? WHERE id_usuario= '" + id + "'");
            st.setString(1, elemento.getNombre());
            st.setString(2, elemento.getCorreo());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public String codigoDireccionUsuario(String codigo_unidad) {

        String codigo_direccion = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo WHERE usuario.codigo_unidad= '" + codigo_unidad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                codigo_direccion = rs.getString("organizacion.nivel_padre");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return codigo_direccion;
    }

    public usuario obtenerDirectorUsuario(String codigo_unidad) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE usuario.codigo_unidad= '" + codigo_unidad + "'AND tipo_usuario.id_rol=4";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                elemento.setFuncion(rs.getString("funcion"));
                elemento.setFirma(rs.getString("firma"));
                elemento.setTipo_funcion(rs.getInt("tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<usuario> listadoUsuarioUnidad(String codigo_funcion) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN permiso_usuario ON permiso_usuario.id_usuario=usuario.id_usuario WHERE codigo_funcion='" + codigo_funcion + "' AND permiso_usuario.acceso=1 ORDER BY apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setFuncion(rs.getString("usuario.funcion"));
                elemento.setFirma(rs.getString("usuario.firma"));
                elemento.setTipo_funcion(rs.getInt("usuario.tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public firma_viatico obtenerFirmaViaticoId(int id_viatico) {

        firma_viatico elemento = new firma_viatico();
        try {
            String sentencia;
            sentencia = "SELECT *FROM firma_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_firma(rs.getInt("id_firma"));
                elemento.setId_responsable(rs.getInt("id_responsable"));
                elemento.setId_autoridad(rs.getInt("id_autoridad"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public usuario obtenerFirmaResponsableViaticoId(int id_viatico) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN firma_viatico ON usuario.id_usuario=firma_viatico.id_responsable WHERE firma_viatico.id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                elemento.setFuncion(rs.getString("funcion"));
                elemento.setFirma(rs.getString("firma"));
                elemento.setTipo_funcion(rs.getInt("tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public usuario obtenerFirmaAutoridadViaticoId(int id_viatico) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN firma_viatico ON usuario.id_usuario=firma_viatico.id_autoridad WHERE firma_viatico.id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                elemento.setFuncion(rs.getString("funcion"));
                elemento.setFirma(rs.getString("firma"));
                elemento.setTipo_funcion(rs.getInt("tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public usuario obtenerFirmaResponsableInformeId(int id_informe) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN firma_informe ON usuario.id_usuario=firma_informe.id_responsable where firma_informe.id_informe='" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                elemento.setFuncion(rs.getString("funcion"));
                elemento.setFirma(rs.getString("firma"));
                elemento.setTipo_funcion(rs.getInt("tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public usuario obtenerFirmaJefeInformeId(int id_informe) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN firma_informe ON usuario.id_usuario=firma_informe.id_jefe where firma_informe.id_informe='" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                elemento.setFuncion(rs.getString("funcion"));
                elemento.setFirma(rs.getString("firma"));
                elemento.setTipo_funcion(rs.getInt("tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public documento_informe obtenerDocumentoInformeId(int id_informe) {

        documento_informe elemento = new documento_informe();
        try {
            String sentencia;
            sentencia = "SELECT *FROM documento_informe WHERE id_informe='" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_documento(rs.getInt("id_documento"));
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setRazon_social(rs.getString("razon_social"));
                elemento.setRuc(rs.getString("ruc"));
                elemento.setNumero(rs.getString("numero"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setTotal(rs.getDouble("total"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<documento_informe> ListadoDocumentoInformeId(int id_informe) {

        ArrayList<documento_informe> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM documento_informe WHERE id_informe='" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                documento_informe elemento = new documento_informe();
                elemento.setId_documento(rs.getInt("id_documento"));
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setRazon_social(rs.getString("razon_social"));
                elemento.setRuc(rs.getString("ruc"));
                elemento.setNumero(rs.getString("numero"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setTotal(rs.getDouble("total"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosDireccion(String codigo_unidad) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT distinct usuario.id_usuario,usuario.codigo_usuario,usuario.nombre,usuario.fecha_creacion,usuario.codigo_unidad,usuario.iniciales,usuario.cedula,usuario.codigo_cargo,usuario.apellido,usuario.correo,usuario.clave FROM usuario INNER JOIN permiso_usuario ON permiso_usuario.id_usuario = usuario.id_usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad OR organizacion.nivel_padre=usuario.codigo_unidad WHERE organizacion.nivel_padre='" + codigo_unidad + "'AND usuario.id_usuario!=2976 AND permiso_usuario.acceso=1 ORDER BY usuario.apellido DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("usuario.id_usuario"));
                p.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                p.setNombre(rs.getString("usuario.nombre"));
                p.setCedula(rs.getString("usuario.cedula"));
                p.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                p.setApellido(rs.getString("usuario.apellido"));
                p.setCorreo(rs.getString("usuario.correo"));
                p.setClave(rs.getString("usuario.clave"));
                p.setIniciales(rs.getString("usuario.iniciales"));
                p.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                p.setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                p.setFuncion(rs.getString("usuario.funcion"));
                p.setFirma(rs.getString("usuario.firma"));
                p.setTipo_funcion(rs.getInt("usuario.tipo_funcion"));
                p.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoServidoresDireccion(String codigo_unidad) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad WHERE organizacion.nivel_padre='" + codigo_unidad + "'OR organizacion.nivel_hijo='" + codigo_unidad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                p.setNombre(rs.getString("usuario.nombre"));
                p.setCedula(rs.getString("usuario.cedula"));
                p.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                p.setApellido(rs.getString("usuario.apellido"));
                p.setCorreo(rs.getString("usuario.correo"));
                p.setClave(rs.getString("usuario.clave"));
                p.setIniciales(rs.getString("usuario.iniciales"));
                p.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                p.setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                p.setFuncion(rs.getString("usuario.funcion"));
                p.setFirma(rs.getString("usuario.firma"));
                p.setTipo_funcion(rs.getInt("usuario.tipo_funcion"));
                p.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean ActualizarClaveUsuario(int id_usuario, String clave) throws SQLException {

        st = enlace.prepareStatement("UPDATE usuario SET clave=MD5(?) WHERE id_usuario= '" + id_usuario + "'");
        st.setString(1, clave);
        st.executeUpdate();
        st.close();

        return true;
    }

    public bot buscarBotID(int id) {

        bot elemento = new bot();
        try {
            String sentencia;
            sentencia = "SELECT *FROM bot WHERE id_bot= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_bot(rs.getInt("id_bot"));
                elemento.setAlias(rs.getString("alias"));
                elemento.setToken(rs.getString("token"));
                elemento.setEstado(rs.getBoolean("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public String buscarChatidUsuario(int id) {

        String chat_id = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM telegram_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                chat_id = rs.getString("chat_id");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return chat_id;
    }

    public boolean estadoTelegramUsuario(int id) {

        boolean estado = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM telegram_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getBoolean("estado");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public boolean estadoUsuario(int id) {

        boolean estado = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permiso_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getBoolean("acceso");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public String estadoPermisoHorasUsuarioID(int id) {

        String estado = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM estado_viatico WHERE id_estado= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public String webmailClaveUsuario(int id) {

        String clave = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM webmail_usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                clave = rs.getString("clave");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return clave;
    }

    public boolean actualizarTelegramUsuario(String chat_id, boolean estado, int id) {

        try {
            st = enlace.prepareStatement("UPDATE telegram_usuario SET chat_id=?, estado=? WHERE id_usuario= '" + id + "'");
            st.setString(1, chat_id);
            st.setBoolean(2, estado);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean deshabilitarUsuario(int id) {

        try {
            st = enlace.prepareStatement("UPDATE permiso_usuario SET acceso=? WHERE id_usuario= '" + id + "'");
            st.setBoolean(1, false);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean habilitarUsuario(int id) {

        try {
            st = enlace.prepareStatement("UPDATE permiso_usuario SET acceso=? WHERE id_usuario= '" + id + "'");
            st.setBoolean(1, true);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarUsuarioPerfil(usuario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE usuario SET correo=? WHERE id_usuario= '" + id + "'");
            st.setString(1, elemento.getCorreo());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean reseteoClaveUsuario(int id, String clave) {
        try {
            st = enlace.prepareStatement("UPDATE usuario SET clave=MD5(?) WHERE id_usuario=?");
            st.setString(1, clave);
            st.setInt(2, id);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println("reseteoClaveUsuario | " + ex);
        }
        return false;
    }

    public boolean actualizarUsuarioInformacion(usuario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE usuario SET nombre=?,apellido=?,correo=?,cedula=?,codigo_cargo=?,codigo_unidad=? WHERE id_usuario= '" + id + "'");
            st.setString(1, elemento.getNombre());
            st.setString(2, elemento.getApellido());
            st.setString(3, elemento.getCorreo());
            st.setString(4, elemento.getCedula());
            st.setString(5, elemento.getCodigo_cargo());
            st.setString(6, elemento.getCodigo_unidad());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarUsuarioInformacionUnidad(usuario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE usuario SET nombre=?,apellido=?,correo=?,cedula=?,codigo_unidad=? WHERE id_usuario= '" + id + "'");
            st.setString(1, elemento.getNombre());
            st.setString(2, elemento.getApellido());
            st.setString(3, elemento.getCorreo());
            st.setString(4, elemento.getCedula());
            st.setString(5, elemento.getCodigo_unidad());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarUsuarioInformacionCargo(usuario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE usuario SET nombre=?,apellido=?,correo=?,cedula=?,codigo_cargo=? WHERE id_usuario= '" + id + "'");
            st.setString(1, elemento.getNombre());
            st.setString(2, elemento.getApellido());
            st.setString(3, elemento.getCorreo());
            st.setString(4, elemento.getCedula());
            st.setString(5, elemento.getCodigo_cargo());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarFotoUsuario(InputStream foto, int id) {

        try {
            st = enlace.prepareStatement("UPDATE foto_usuario SET foto=? WHERE id_usuario= '" + id + "'");
            st.setBlob(1, foto);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarTClaveWebmail(String clave, int id) {

        try {
            st = enlace.prepareStatement("UPDATE webmail_usuario SET clave=? WHERE id_usuario= '" + id + "'");
            st.setString(1, clave);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public String encriptacionClave(String clave) {
        String md5 = "";
        try {
            if (!clave.equalsIgnoreCase("")) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.reset();
                md.update(clave.getBytes());
                byte bytes[] = md.digest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(0xff & bytes[i]);
                    if (hex.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(hex);
                }
                md5 = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            md5 = "Error inesperado";
        }
        return md5;
    }

    public boolean comprobacionUsuario(usuario elemento) {
        boolean confirmacion = false;
        usuario element = buscar_usuarioCorreo(elemento.getCorreo());
        if (element != null) {
            if (element.getClave().equals(encriptacionClave(elemento.getClave())) && element.getCorreo().equals(elemento.getCorreo())) {
                confirmacion = true;
            }
        } else {
            System.out.println("no existe");
        }
        return confirmacion;
    }

    public boolean notificacionTelegram(String mensaje, String chat_id, int id_bot) {
        boolean confirmacion = false;
        bot elemento = buscarBotID(id_bot);
        if (elemento.isEstado()) {
            elemento.sendMag(mensaje, chat_id);
            confirmacion = true;
        }
        return confirmacion;
    }

    public void iniciarBot() {
        ApiContextInitializer.init();
        TelegramBotsApi apiBot = new TelegramBotsApi();
        try {
            sesion = apiBot.registerBot(new bot());
            if (sesionActiva()) {
                detenerBot();
            }
        } catch (TelegramApiRequestException ex) {
        }
    }

    public void detenerBot() {
        try {
            if (sesion != null) {
                sesion.stop();
            }
        } catch (Exception e) {
        }
    }

    public boolean sesionActiva() {
        return sesion != null && sesion.isRunning();
    }

    public boolean registroViatico(viatico ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO viatico(id_tipo,id_usuario,descripcion_actividad,tipo_cuenta,numero_cuenta,nombre_banco) VALUES(?,?,?,?,?,?)");
        st.setInt(1, ph.getId_tipo());
        st.setInt(2, ph.getId_usuario());
        st.setString(3, ph.getDescripcion_actividad());
        st.setString(4, ph.getTipo_cuenta());
        st.setString(5, ph.getNumero_cuenta());
        st.setString(6, ph.getNombre_banco());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroParticipanteViatico(participacion_viatico ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO participacion_viatico(id_viatico,id_usuario) VALUES(?,?)");
        st.setInt(1, ph.getId_viatico());
        st.setInt(2, ph.getId_usuario());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroParticipanteInforme(participacion_informe ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO participacion_informe(id_informe,id_usuario) VALUES(?,?)");
        st.setInt(1, ph.getId_informe());
        st.setInt(2, ph.getId_usuario());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroRutaViatico(ruta_viatico ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO ruta_viatico(id_viatico,id_lugarpartida,id_lugarllegada,tipo_transporte,nombre_transporte,fecha_llegada,hora_llegada,fecha_salida,hora_salida) VALUES(?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_viatico());
        st.setInt(2, ph.getId_lugarpartida());
        st.setInt(3, ph.getId_lugarllegada());
        st.setString(4, ph.getTipo_transporte());
        st.setString(5, ph.getNombre_transporte());
        st.setDate(6, ph.getFecha_llegada());
        st.setString(7, ph.getHora_llegada());
        st.setDate(8, ph.getFecha_salida());
        st.setString(9, ph.getHora_salida());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroRutaInforme(ruta_informe ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO ruta_informe(id_informe,id_lugarpartida,id_lugarllegada,tipo_transporte,nombre_transporte,fecha_llegada,hora_llegada,fecha_salida,hora_salida) VALUES(?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_informe());
        st.setInt(2, ph.getId_lugarpartida());
        st.setInt(3, ph.getId_lugarllegada());
        st.setString(4, ph.getTipo_transporte());
        st.setString(5, ph.getNombre_transporte());
        st.setDate(6, ph.getFecha_llegada());
        st.setString(7, ph.getHora_llegada());
        st.setDate(8, ph.getFecha_salida());
        st.setString(9, ph.getHora_salida());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarRutaViatico(ruta_viatico elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_viatico SET id_lugarpartida=?,id_lugarllegada=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarpartida());
            st.setInt(2, elemento.getId_lugarllegada());
            st.setString(3, elemento.getTipo_transporte());
            st.setString(4, elemento.getNombre_transporte());
            st.setDate(5, elemento.getFecha_llegada());
            st.setString(6, elemento.getHora_llegada());
            st.setDate(7, elemento.getFecha_salida());
            st.setString(8, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaViaticoLugarPartida(ruta_viatico elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_viatico SET id_lugarpartida=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarpartida());
            st.setString(2, elemento.getTipo_transporte());
            st.setString(3, elemento.getNombre_transporte());
            st.setDate(4, elemento.getFecha_llegada());
            st.setString(5, elemento.getHora_llegada());
            st.setDate(6, elemento.getFecha_salida());
            st.setString(7, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaViaticoLugarLlegada(ruta_viatico elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_viatico SET id_lugarllegada=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarllegada());
            st.setString(2, elemento.getTipo_transporte());
            st.setString(3, elemento.getNombre_transporte());
            st.setDate(4, elemento.getFecha_llegada());
            st.setString(5, elemento.getHora_llegada());
            st.setDate(6, elemento.getFecha_salida());
            st.setString(7, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaViaticoInformacion(ruta_viatico elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_viatico SET tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setString(1, elemento.getTipo_transporte());
            st.setString(2, elemento.getNombre_transporte());
            st.setDate(3, elemento.getFecha_llegada());
            st.setString(4, elemento.getHora_llegada());
            st.setDate(5, elemento.getFecha_salida());
            st.setString(6, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaInforme(ruta_informe elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_informe SET id_lugarpartida=?,id_lugarllegada=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarpartida());
            st.setInt(2, elemento.getId_lugarllegada());
            st.setString(3, elemento.getTipo_transporte());
            st.setString(4, elemento.getNombre_transporte());
            st.setDate(5, elemento.getFecha_llegada());
            st.setString(6, elemento.getHora_llegada());
            st.setDate(7, elemento.getFecha_salida());
            st.setString(8, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaInformeLugarPartida(ruta_informe elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_informe SET id_lugarpartida=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarpartida());
            st.setString(2, elemento.getTipo_transporte());
            st.setString(3, elemento.getNombre_transporte());
            st.setDate(4, elemento.getFecha_llegada());
            st.setString(5, elemento.getHora_llegada());
            st.setDate(6, elemento.getFecha_salida());
            st.setString(7, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaInformeLugarLlegada(ruta_informe elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_informe SET id_lugarllegada=?,tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setInt(1, elemento.getId_lugarllegada());
            st.setString(2, elemento.getTipo_transporte());
            st.setString(3, elemento.getNombre_transporte());
            st.setDate(4, elemento.getFecha_llegada());
            st.setString(5, elemento.getHora_llegada());
            st.setDate(6, elemento.getFecha_salida());
            st.setString(7, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarRutaInformeInformacion(ruta_informe elemento, int id_ruta) {

        try {
            st = enlace.prepareStatement("UPDATE ruta_informe SET tipo_transporte=?,nombre_transporte=?,fecha_llegada=?,hora_llegada=?,fecha_salida=?,hora_salida=? WHERE id_ruta= '" + id_ruta + "'");
            st.setString(1, elemento.getTipo_transporte());
            st.setString(2, elemento.getNombre_transporte());
            st.setDate(3, elemento.getFecha_llegada());
            st.setString(4, elemento.getHora_llegada());
            st.setDate(5, elemento.getFecha_salida());
            st.setString(6, elemento.getHora_salida());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarViatico(viatico elemento, int id_viatico) {
        try {
            st = enlace.prepareStatement("UPDATE viatico SET id_tipo=?,descripcion_actividad=?,tipo_cuenta=?,numero_cuenta=?,nombre_banco=? WHERE id_viatico= '" + id_viatico + "'");
            st.setInt(1, elemento.getId_tipo());
            st.setString(2, elemento.getDescripcion_actividad());
            st.setString(3, elemento.getTipo_cuenta());
            st.setString(4, elemento.getNumero_cuenta());
            st.setString(5, elemento.getNombre_banco());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println("actualizarViatico | " + ex);
        }
        return false;
    }

    public boolean revisarViatico(int id, int idUsu, String motivo) {
        try {
            st = enlace.prepareStatement("UPDATE viatico SET id_estado=2 WHERE id_viatico=?; INSERT INTO revision_viatico(id_viatico, id_usuario, motivo) VALUES(?, ?, ?);");
            st.setInt(1, id);
            st.setInt(2, id);
            st.setInt(3, idUsu);
            st.setString(4, motivo);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("revisarViatico | " + ex);
        }
        return false;
    }

    public RevisionViatico getRevisionViatico(int id) {
        RevisionViatico r = new RevisionViatico();
        try {
            String sentencia = "SELECT * FROM revision_viatico WHERE id_viatico=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                r = new RevisionViatico(rs.getInt("id_revision"),
                        rs.getInt("id_viatico"),
                        rs.getInt("id_usuario"),
                        rs.getString("motivo"),
                        rs.getTimestamp("fecha"));
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRevisionViatico | " + ex);
        }
        return r;
    }

    public boolean aprobarViatico(int id, int idUsu, String motivo) {
        try {
            st = enlace.prepareStatement("UPDATE viatico SET id_estado=3 WHERE id_viatico=?; INSERT INTO aprobacion_viatico(id_viatico, id_usuario, motivo) VALUES(?, ?, ?);");
            st.setInt(1, id);
            st.setInt(2, id);
            st.setInt(3, idUsu);
            st.setString(4, motivo);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("aprobarViatico | " + ex);
        }
        return false;
    }

    public AprobacionViatico getAprobacionViatico(int id) {
        AprobacionViatico r = new AprobacionViatico();
        try {
            String sentencia = "SELECT * FROM aprobacion_viatico WHERE id_viatico=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                r = new AprobacionViatico(rs.getInt("id_aprobacion"),
                        rs.getInt("id_viatico"),
                        rs.getInt("id_usuario"),
                        rs.getString("motivo"),
                        rs.getTimestamp("fecha"));
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAprobacionViatico | " + ex);
        }
        return r;
    }

    public boolean rechazarViatico(int id, int idUsu, String motivo) {
        try {
            st = enlace.prepareStatement("UPDATE viatico SET id_estado=4 WHERE id_viatico=?; INSERT INTO rechazo_viatico(id_viatico, id_usuario, motivo) VALUES(?, ?, ?);");
            st.setInt(1, id);
            st.setInt(2, id);
            st.setInt(3, idUsu);
            st.setString(4, motivo);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("rechazarViatico | " + ex);
        }
        return false;
    }

    public RechazoViatico getRechazoViatico(int id) {
        RechazoViatico r = new RechazoViatico();
        try {
            String sentencia = "SELECT * FROM rechazo_viatico WHERE id_viatico=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                r = new RechazoViatico(rs.getInt("id_rechazo"),
                        rs.getInt("id_viatico"),
                        rs.getInt("id_usuario"),
                        rs.getString("motivo"),
                        rs.getTimestamp("fecha"));
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRechazoViatico | " + ex);
        }
        return r;
    }

    public viatico buscarViaticoNumeroSolicitud(String numero_solicitud) {
        viatico elemento = new viatico();
        try {
            String sentencia;
            sentencia = "SELECT * FROM viatico WHERE numero_solicitud= '" + numero_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscarViaticoNumeroSolicitud | " + ex);
        }
        return elemento;
    }

    public viatico buscarViaticoID(int id) {
        viatico elemento = new viatico();
        try {
            String sentencia;
            sentencia = "SELECT *FROM viatico WHERE id_viatico= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscarViaticoID | " + ex);
        }
        return elemento;
    }

    public ArrayList<rol_usuario> listadoRolesUsuarios() {

        ArrayList<rol_usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM rol";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                rol_usuario elemento = new rol_usuario();
                elemento.setId_rol(rs.getInt("id_rol"));
                elemento.setDescripcion(rs.getString("descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public String buscarTipoViaticoId(int id) {

        String tipo_viatico = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_viatico WHERE id_viatico= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_viatico = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tipo_viatico;
    }

    public String buscarCiudadID(int id) {

        String nombre_ciudad = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM ciudad WHERE CiudadId= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                nombre_ciudad = rs.getString("CiudadNombre");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return nombre_ciudad;
    }

    public tipo_viatico buscarTipoViatico(int id) {

        tipo_viatico elemento = new tipo_viatico();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_viatico WHERE id_viatico= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setDescripcion(rs.getString("descripcion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public String buscarEstadoViaticoId(int id) {

        String estado = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM estado_viatico WHERE id_estado= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public boolean registroFirmaViatico(firma_viatico ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO firma_viatico(id_viatico,id_responsable,id_autoridad) VALUES(?,?,?)");
        st.setInt(1, ph.getId_viatico());
        st.setInt(2, ph.getId_responsable());
        st.setInt(3, ph.getId_autoridad());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarFirmaViatico(int id_viatico, firma_viatico ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE firma_viatico SET id_responsable=?,id_autoridad=? WHERE id_viatico= '" + id_viatico + "'");
        st.setInt(1, ph.getId_responsable());
        st.setInt(2, ph.getId_autoridad());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroFirmaInforme(firma_informe ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO firma_informe(id_informe,id_responsable,id_jefe) VALUES(?,?,?)");
        st.setInt(1, ph.getId_informe());
        st.setInt(2, ph.getId_responsable());
        st.setInt(3, ph.getId_jefe());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroFotoVaciaUsuario(int id_usuario) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO foto_usuario(id_usuario) VALUES(?)");
        st.setInt(1, id_usuario);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroTipoUsuario(int id_usuario, int id_rol) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO tipo_usuario(id_usuario, id_rol) VALUES(?,?)");
        st.setInt(1, id_usuario);
        st.setInt(2, id_rol);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroPermisoUsuario(int id_usuario) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO permiso_usuario(id_usuario) VALUES(?)");
        st.setInt(1, id_usuario);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroSesionUsuario(int id_usuario) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO sesion(id_usuario) VALUES(?)");
        st.setInt(1, id_usuario);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroCalendarioGoogle(calendario_google ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO calendario_google(id_usuario,nombre) VALUES(?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getNombre());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroDocumentoInforme(documento_informe ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO documento_informe(id_informe,razon_social,ruc,numero,fecha,descripcion,total) VALUES(?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_informe());
        st.setString(2, ph.getRazon_social());
        st.setString(3, ph.getRuc());
        st.setString(4, ph.getNumero());
        st.setDate(5, ph.getFecha());
        st.setString(6, ph.getDescripcion());
        st.setDouble(7, ph.getTotal());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarDocumentoInforme(int id_documento, documento_informe ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE documento_informe SET razon_social=?,ruc=?,numero=?,fecha=?,descripcion=?,total=? WHERE id_documento= '" + id_documento + "'");
        st.setString(1, ph.getRazon_social());
        st.setString(2, ph.getRuc());
        st.setString(3, ph.getNumero());
        st.setDate(4, ph.getFecha());
        st.setString(5, ph.getDescripcion());
        st.setDouble(6, ph.getTotal());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarFirmaInforme(int id_informe, firma_informe ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE firma_informe SET id_responsable=?,id_jefe=? WHERE id_informe= '" + id_informe + "'");
        st.setInt(1, ph.getId_responsable());
        st.setInt(2, ph.getId_jefe());
        st.executeUpdate();
        st.close();

        return true;
    }

    public String buscarCargoUsuarioCodigo(String codigo) {

        String estado = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM cargo WHERE codigo_cargo= '" + codigo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            estado = "";
        }

        return estado;
    }

    public ArrayList<calendario_google> listadoCalendariosGoogleUsuario(int id_usuario) {

        ArrayList<calendario_google> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM calendario_google WHERE id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                calendario_google elemento = new calendario_google();
                elemento.setId_calendario(rs.getInt("id_calendario"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean eliminarCalendarioGoogleID(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM calendario_google WHERE id_calendario= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public ArrayList<viatico> listadoViaticosUsuario(int id_usuario) {

        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM viatico WHERE id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<viatico> listadoViaticosUsuarioEstaado(int id_usuario, int estado) {
        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM viatico WHERE id_usuario= '" + id_usuario + "'AND id_estado='" + estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoViaticosUsuarioEstaado | " + ex);
        }
        return listado;
    }

    public ArrayList<viatico> listadoViaticosUsuarioEstado(int id_usuario, int estado) {

        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM viatico INNER JOIN estado_viatico ON estado_viatico.id_estado=viatico.id_estado WHERE id_usuario= '" + id_usuario + "'AND estado_viatico.id_estado= '" + estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<participacion_viatico> listadoParticipantesViatico(int id_viatico) {

        ArrayList<participacion_viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM participacion_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                participacion_viatico elemento = new participacion_viatico();
                elemento.setId_participacion(rs.getInt("id_participacion"));
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<participacion_informe> listadoParticipantesInforme(int id_informe) {

        ArrayList<participacion_informe> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM participacion_informe WHERE id_informe= '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                participacion_informe elemento = new participacion_informe();
                elemento.setId_participacion(rs.getInt("id_participacion"));
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<ruta_viatico> listadoRutasViatico(int id_viatico) {

        ArrayList<ruta_viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM ruta_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta_viatico elemento = new ruta_viatico();
                elemento.setId_ruta(rs.getInt("id_ruta"));
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_lugarpartida(rs.getInt("id_lugarpartida"));
                elemento.setId_lugarllegada(rs.getInt("id_lugarllegada"));
                elemento.setFecha_llegada(rs.getDate("fecha_llegada"));
                elemento.setFecha_salida(rs.getDate("fecha_salida"));
                elemento.setHora_llegada(rs.getString("hora_llegada"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setNombre_transporte(rs.getString("nombre_transporte"));
                elemento.setTipo_transporte(rs.getString("tipo_transporte"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<ruta_informe> listadoRutasInforme(int id_informe) {

        ArrayList<ruta_informe> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM ruta_informe WHERE id_informe= '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta_informe elemento = new ruta_informe();
                elemento.setId_ruta(rs.getInt("id_ruta"));
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setId_lugarpartida(rs.getInt("id_lugarpartida"));
                elemento.setId_lugarllegada(rs.getInt("id_lugarllegada"));
                elemento.setFecha_llegada(rs.getDate("fecha_llegada"));
                elemento.setFecha_salida(rs.getDate("fecha_salida"));
                elemento.setHora_llegada(rs.getString("hora_llegada"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setNombre_transporte(rs.getString("nombre_transporte"));
                elemento.setTipo_transporte(rs.getString("tipo_transporte"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean actualizarInformeViatico(informe_viatico elemento, int id_informe) {

        try {
            st = enlace.prepareStatement("UPDATE informe_viatico SET descripcion=? WHERE id_informe= '" + id_informe + "'");
            st.setString(1, elemento.getDescripcion());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarEstadoViatico(int id_viatico, int id_estado) {

        try {
            st = enlace.prepareStatement("UPDATE viatico SET id_estado=? WHERE id_viatico= '" + id_viatico + "'");
            st.setInt(1, id_estado);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarParticipanteViatico(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM participacion_viatico WHERE id_usuario= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarParticipanteInforme(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM participacion_informe WHERE id_usuario= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarDocumentoInformeID(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM documento_informe WHERE id_documento= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarRutaViatico(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM ruta_viatico WHERE id_ruta= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarRutaInforme(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM ruta_informe WHERE id_ruta= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public static Connection getEnlace() {
        return enlace;
    }

    public boolean exiteParticipante(int id_viatico, int id_usuario) {

        boolean existe = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM participacion_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                participacion_viatico elemento = new participacion_viatico();
                elemento.setId_usuario(rs.getInt("id_usuario"));
                if (elemento.getId_usuario() == id_usuario) {
                    existe = true;
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return existe;
    }

    public boolean exiteParticipanteInforme(int id_informe, int id_usuario) {

        boolean existe = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM participacion_informe WHERE id_informe= '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                participacion_viatico elemento = new participacion_viatico();
                elemento.setId_usuario(rs.getInt("id_usuario"));
                if (elemento.getId_usuario() == id_usuario) {
                    existe = true;
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return existe;
    }

    public boolean existeInformeViatico(int id_viatico) {

        boolean existe = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM informe_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_viatico");
                if (id == id_viatico) {
                    existe = true;
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return existe;
    }

    public boolean rutasCompletasViaticos(int id_viatico) {

        int contador = 0;
        boolean existe = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM ruta_viatico WHERE id_viatico= '" + id_viatico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_viatico");
                if (id == id_viatico) {
                    contador++;
                    if (contador > 1) {
                        existe = true;
                    }
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return existe;
    }

    public boolean eliminarRuta(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM ruta_viatico WHERE id_ruta = '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean rutasCompletasInformes(int id_informe) {

        int contador = 0;
        boolean existe = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM ruta_informe WHERE id_informe= '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_informe");
                if (id == id_informe) {
                    contador++;
                    if (contador > 1) {
                        existe = true;
                    }
                }
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return existe;
    }

    public boolean registroInformeViatico(informe_viatico ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO informe_viatico(id_viatico, numero_solicitud,fecha,descripcion,id_estado) VALUES(?,?,?,?,?)");
        st.setInt(1, ph.getId_viatico());
        st.setString(2, ph.getNumero_solicitud());
        st.setDate(3, ph.getFecha());
        st.setString(4, ph.getDescripcion());
        st.setInt(5, ph.getId_estado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<informe_viatico> listadoInformeViaticosUsuarioEstado(int id_usuario, int estado) {

        ArrayList<informe_viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM informe_viatico INNER JOIN viatico ON viatico.id_viatico=informe_viatico.id_viatico INNER JOIN estado_viatico ON estado_viatico.id_estado=informe_viatico.id_estado WHERE viatico.id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                informe_viatico elemento = new informe_viatico();
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setNumero_solicitud(rs.getString("numero_solicitud"));
                elemento.setDescripcion(rs.getString("viatico.numero_solicitud"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setId_estado(rs.getInt("id_estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public informe_viatico InformeViaticosId(int id_informe) {

        informe_viatico elemento = new informe_viatico();
        try {
            String sentencia;
            sentencia = "SELECT *FROM informe_viatico WHERE id_informe= '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setNumero_solicitud(rs.getString("numero_solicitud"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setId_estado(rs.getInt("id_estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public int rutaRecienteViaticoId(int id) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_ruta) AS idmax FROM ruta_viatico WHERE id_viatico= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public informe_viatico buscaInformeViatico(String numero_solicitud) {

        informe_viatico elemento = new informe_viatico();
        try {
            String sentencia;
            sentencia = "SELECT *FROM informe_viatico WHERE numero_solicitud='" + numero_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_informe(rs.getInt("id_informe"));
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setNumero_solicitud(rs.getString("numero_solicitud"));
                elemento.setFecha(rs.getDate("fecha"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public int idViaticoInforme(int id_informe) {

        int id_viatico = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM informe_viatico WHERE id_informe '" + id_informe + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_viatico = rs.getInt("id_viatico");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_viatico;
    }

    public int numeroAccesosUsuario(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_estado) AS idmax FROM estado_usuario WHERE id_usuario= '" + id_usuario + "'AND MONTH(CURDATE())=MONTH(fecha_acceso) AND YEAR(CURDATE())=YEAR(fecha_acceso)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroAccesosIntranetRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_estado) AS idmax FROM estado_usuario WHERE id_usuario= '" + id_usuario + "'AND fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroAccesosIntranetRangoFechaDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(estado_usuario.id_estado) AS idmax FROM estado_usuario INNER JOIN usuario ON usuario.id_usuario=estado_usuario.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND estado_usuario.fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroAccesosIntranetRangoFechaDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(estado_usuario.id_estado) AS idmax FROM estado_usuario INNER JOIN usuario ON usuario.id_usuario=estado_usuario.id_usuario WHERE estado_usuario.fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroRegistroActividadesRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesRangoFechaDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "' AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesRangoFechaDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesEstadoRegistradasRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.estado=0 AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesEstadoAprobadasRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.estado=1 AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroRegistroActividadesCorregidasRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(comentarios_actividad.id_actividad) AS idmax FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroRegistroActividadesCorregidasRangoFechaDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(comentarios_actividad.id_actividad) AS idmax FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroRegistroActividadesCorregidasRangoFechaDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(comentarios_actividad.id_actividad) AS idmax FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesRegistradas(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad WHERE id_usuario= '" + id_usuario + "'AND  MONTH(CURDATE())=MONTH(fecha_actividad) AND YEAR(CURDATE())=YEAR(fecha_actividad)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroActividadesAprobadas(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_actividad) AS idmax FROM actividad WHERE id_usuario= '" + id_usuario + "'AND  MONTH(CURDATE())=MONTH(fecha_actividad) AND YEAR(CURDATE())=YEAR(fecha_actividad) AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int esprimeraSesion(int id_usuario) {

        int estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM sesion WHERE id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getInt("estado");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("esprimeraSesion | " + ex);
        }

        return estado;
    }

    public String tiempoEjecucionTarea(int id_actividad) {

        String tiempo = "";
        try {
            String sentencia;
            sentencia = "SELECT CONCAT( HOUR((TIMEDIFF(hora_inicio, hora_fin))), 'h y ', MINUTE((TIMEDIFF(hora_inicio, hora_fin))),'m') AS tiempo FROM actividad WHERE id_actividad= '" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tiempo = rs.getString("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tiempo;
    }

    public String tiempoPermisoHoras(String hora_inicio, String hora_fin) {

        String tiempo = "";
        try {
            String sentencia;
            sentencia = "SELECT CONCAT( HOUR((TIMEDIFF(" + hora_inicio + "," + hora_fin + "))), ',', MINUTE((TIMEDIFF(" + hora_inicio + "," + hora_fin + ")))) AS tiempo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tiempo = rs.getString("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tiempo;
    }

    public String tiempoEjecucionPermiso(String hora_inicio, String hora_fin) {

        String tiempo = "";
        try {
            String sentencia;
            sentencia = "SELECT CONCAT( HOUR((TIMEDIFF('" + hora_inicio + "','" + hora_fin + "'))), ',', MINUTE((TIMEDIFF('" + hora_inicio + "','" + hora_fin + "'))),'') AS tiempo";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tiempo = rs.getString("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return tiempo;
    }

    public int numeroPermisosHorasUsuario(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_permiso) AS idmax FROM permisohoras_usuario WHERE id_usuario= '" + id_usuario + "'AND  MONTH(CURDATE())=MONTH(fecha) AND YEAR(CURDATE())=YEAR(fecha)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int numeroPermisosVacacionesUsuario(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_permiso) AS idmax FROM permisovacaciones_usuario WHERE id_usuario= '" + id_usuario + "'AND  MONTH(CURDATE())=MONTH(fecha_solicitud) AND YEAR(CURDATE())=YEAR(fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int totalPermisosUsuario(int id_usuario) {
        return numeroPermisosHorasUsuario(id_usuario) + numeroPermisosVacacionesUsuario(id_usuario);
    }

    public int totalPermisosHorasDir(String codigoUnidad) {
        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM permisohoras_usuario INNER JOIN usuario ON permisohoras_usuario.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion='" + codigoUnidad + "' AND permisohoras_usuario.id_estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("totalPermisosHorasDir | " + ex);
        }
        return ruta;
    }

    public int totalPermisosVacaDir(String codigoUnidad) {
        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM permisovacaciones_usuario INNER JOIN usuario ON permisovacaciones_usuario.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion='" + codigoUnidad + "' AND permisovacaciones_usuario.estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("totalPermisosVacaDir | " + ex);
        }
        return ruta;
    }

    public String direccionPerteneceUsuario(String codigo_unidad) {

        String nombre = "";
        try {
            String sentencia;
            sentencia = "SELECT *FROM organizacion WHERE nivel_hijo= '" + codigo_unidad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                nombre = rs.getString("nombre");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return nombre;
    }

    public destino buscarDestinoId(int id_ruta) {

        destino elemento = new destino();
        try {
            String sentencia;
            sentencia = "SELECT *FROM pais INNER JOIN ciudad ON ciudad.PaisCodigo=pais.PaisCodigo INNER JOIN ruta_viatico ON ruta_viatico.id_lugarllegada=ciudad.CiudadID WHERE ruta_viatico.id_ruta= '" + id_ruta + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_destino(rs.getInt("ciudad.CiudadID"));
                elemento.setNombre_pais(rs.getString("pais.PaisNombre"));
                elemento.setNombre_ciudad(rs.getString("ciudad.CiudadNombre"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<estado_evento> listadoEstadosEvento() {

        ArrayList<estado_evento> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM estado_evento";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado_evento elemento = new estado_evento();
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setDescripcion(rs.getString("descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean registroEvento(evento ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO evento(id_usuario,titulo,color,inicio,hora_inicio,fin,hora_fin,fecha_creacion,id_estado) VALUES(?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getTitulo());
        st.setString(3, ph.getColor());
        st.setDate(4, ph.getInicio());
        st.setString(5, ph.getHora_inicio());
        st.setDate(6, ph.getFin());
        st.setString(7, ph.getHora_fin());
        st.setDate(8, ph.getFecha_creacion());
        st.setInt(9, ph.getId_estado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarEventoId(int id_evento, evento ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE evento SET titulo=?,color=?,hora_inicio=?,hora_fin=? WHERE id= '" + id_evento + "'");
        st.setString(1, ph.getTitulo());
        st.setString(2, ph.getColor());
        st.setString(3, ph.getHora_inicio());
        st.setString(4, ph.getHora_fin());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarEventoFecha(int id_evento, evento ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE evento SET inicio=?,fin=? WHERE id= '" + id_evento + "'");
        st.setDate(1, ph.getInicio());
        st.setDate(2, ph.getFin());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarFotoUsuario(int id_usuario, foto_usuario elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE foto_usuario SET ruta=? WHERE id_usuario= '" + id_usuario + "'");
        st.setString(1, elemento.getRuta());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean ActualizarFirmaUsuario(int id_usuario, String ruta) throws SQLException {

        st = enlace.prepareStatement("UPDATE usuario SET firma=? WHERE id_usuario= '" + id_usuario + "'");
        st.setString(1, ruta);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean eliminarEventoId(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM evento WHERE id= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
        }

        return false;
    }

    public ArrayList<evento> listadoEventosUsuario(int id_usuario) {

        ArrayList<evento> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM evento INNER JOIN usuario ON evento.id_usuario=usuario.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'OR evento.id_estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                evento elemento = new evento();
                elemento.setId_evento(rs.getInt("id"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTitulo(rs.getString("titulo"));
                elemento.setColor(rs.getString("color"));
                elemento.setInicio(rs.getDate("inicio"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFin(rs.getDate("fin"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<evento> listadoEventosDireccion(String codigo_funcion) {

        ArrayList<evento> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM evento INNER JOIN usuario ON evento.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                evento elemento = new evento();
                elemento.setId_evento(rs.getInt("id"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTitulo(rs.getString("titulo"));
                elemento.setColor(rs.getString("color"));
                elemento.setInicio(rs.getDate("inicio"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFin(rs.getDate("fin"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int usuarioPublicacionEvento(int id_evento) {

        int id_usuario = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM evento WHERE id= '" + id_evento + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_usuario = rs.getInt("id_usuario");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_usuario;
    }

    public String presentacionActiva() {

        String descripcion = "";
        try {
            String sentencia;
            sentencia = "SELECT descripcion FROM presentacion WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                descripcion = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("presentacionActiva | " + ex);
        }

        return descripcion;
    }

    public int registroPermisoHoras(permiso_horas ph) throws SQLException {
        usuario funcionario = getUsuario(ph.getCodigoUsu());
        conexion_oracle oracle = new conexion_oracle();
        String direccion = oracle.consultarDireccionUsuario(ph.getCodigoUsu());
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(direccion));
        st = enlace.prepareStatement("INSERT INTO permisohoras_usuario(" + (funcionario == null ? "codigo_usu" : "id_usuario") + ",hora_salida,hora_entrada,id_motivo,fecha,tiempo_solicita,horas,minutos,observacion,id_tipo,fecha_inicio,fecha_fin,fecha_ingreso,unidad,timestamp_inicio,timestamp_fin,denominacion,jefe,cargo_jefe,nombre_usu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, funcionario == null ? ph.getCodigoUsu() : funcionario.getId_usuario());
        st.setString(2, ph.getHora_salida());
        st.setString(3, ph.getHora_entrada());
        st.setInt(4, ph.getId_motivo());
        st.setDate(5, ph.getFecha());
        st.setString(6, ph.getTiempo_solicita());
        st.setInt(7, ph.getHoras());
        st.setInt(8, ph.getMinutos());
        st.setString(9, ph.getObservacion());
        st.setInt(10, ph.getId_tipo());
        st.setDate(11, ph.getFecha_inicio());
        st.setDate(12, ph.getFecha_fin());
        st.setDate(13, ph.getFecha_ingreso());
        st.setString(14, direccion);
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_" + ph.getHora_salida() + ":00");
            st.setTimestamp(15, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_" + ph.getHora_entrada() + ":00");
            st.setTimestamp(16, new Timestamp(utilDate.getTime()));
        } catch (Exception e) {
            st.setNull(15, Types.TIMESTAMP);
            st.setNull(16, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setString(17, oracle.consultarDenominacionUsuario(ph.getCodigoUsu()));
        st.setString(18, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(19, oracle.consultarDenominacionUsuario(codigoJefe));
        if (funcionario == null) {
            st.setString(20, ph.getNombreUsu());
        } else {
            st.setNull(20, Types.VARCHAR);
        }
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int registroPermisoHorasIESS(permiso_horas ph) throws SQLException {
        conexion_oracle oracle = new conexion_oracle();
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(ph.getDireccion()));
        st = enlace.prepareStatement("INSERT INTO permisohoras_usuario(codigo_usu,hora_salida,hora_entrada,id_motivo,fecha,tiempo_solicita,horas,minutos,observacion,id_tipo,fecha_inicio,fecha_fin,fecha_ingreso,unidad,timestamp_inicio,timestamp_fin,denominacion,jefe,cargo_jefe) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, ph.getCodigoUsu());
        st.setString(2, ph.getHora_salida());
        st.setString(3, ph.getHora_entrada());
        st.setInt(4, ph.getId_motivo());
        st.setDate(5, ph.getFecha());
        st.setString(6, ph.getTiempo_solicita());
        st.setInt(7, ph.getHoras());
        st.setInt(8, ph.getMinutos());
        st.setString(9, ph.getObservacion());
        st.setInt(10, ph.getId_tipo());
        st.setDate(11, ph.getFecha_inicio());
        st.setDate(12, ph.getFecha_fin());
        st.setDate(13, ph.getFecha_ingreso());
        st.setString(14, ph.getDireccion());
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_salida() + ":00");
            st.setTimestamp(15, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_entrada() + ":00");
            st.setTimestamp(16, new Timestamp(utilDate.getTime()));
        } catch (Exception e) {
            st.setNull(15, Types.TIMESTAMP);
            st.setNull(16, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setString(17, ph.getDenominacion());
        st.setString(18, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(19, oracle.consultarDenominacionUsuario(codigoJefe));
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int registroPermisoHorasECU(PermisoECU p) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO permiso_ecu(id_usu,descrip,inicio,fin,tiempo_soli,dias_habiles,dias_finde,unidad,denominacion,jefe,cargo_jefe,fecha_soli,id_motivo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, p.getIdUsu());
        st.setString(2, p.getDescripcion());
        st.setTimestamp(3, p.getInicio());
        st.setTimestamp(4, p.getFin());
        st.setString(5, p.getTiempoSoli());
        st.setInt(6, p.getDiasHabiles());
        st.setInt(7, p.getDiasFinde());
        st.setString(8, p.getUnidad());
        st.setString(9, p.getDenominacion());
        st.setString(10, p.getJefe());
        st.setString(11, p.getCargoJefe());
        st.setDate(12, p.getFechaSoli());
        st.setInt(13, p.getIdMotivo());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int registroPermisoManual(PermisoManual p) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO permiso_manual(id_admin,codigo_usu,fecha_inicio,fecha_fin,fecha_retorno,dias_habiles,fines_semana,hora_inicio,hora_fin,horas,minutos,observacion,denominacion,direccion,jefe,cargo_jefe) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, p.getAdmin());
        st.setInt(2, p.getCodUsu());
        st.setTimestamp(3, p.getFechaInicio());
        st.setTimestamp(4, p.getFechaFin());
        st.setTimestamp(5, p.getFechaRetorno());
        if (p.getDiasHabiles() == 0) {
            st.setNull(6, Types.INTEGER);
            st.setNull(7, Types.INTEGER);
            st.setString(8, p.getHoraInicio());
            st.setString(9, p.getHoraFin());
            st.setInt(10, p.getHoras());
            st.setInt(11, p.getMinutos());
        } else {
            st.setInt(6, p.getDiasHabiles());
            st.setInt(7, p.getFinesSemana());
            st.setNull(8, Types.VARCHAR);
            st.setNull(9, Types.VARCHAR);
            st.setNull(10, Types.INTEGER);
            st.setNull(11, Types.INTEGER);
        }
        st.setString(12, p.getObservacion());
        st.setString(13, p.getDenominacion());
        st.setString(14, p.getDireccion());
        st.setString(15, p.getJefe());
        st.setString(16, p.getCargoJefe());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean actualizarPermisoHoras(int id, permiso_horas ph) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET hora_salida=?, hora_entrada=?, id_motivo=?, fecha=?, tiempo_solicita=?, horas=?, minutos=?, observacion=?, id_tipo=?, fecha_inicio=?, fecha_fin=?, fecha_ingreso=?, timestamp_inicio=?, timestamp_fin=? WHERE id_permiso=?");
        st.setString(1, ph.getHora_salida());
        st.setString(2, ph.getHora_entrada());
        st.setInt(3, ph.getId_motivo());
        st.setDate(4, ph.getFecha());
        st.setString(5, ph.getTiempo_solicita());
        st.setInt(6, ph.getHoras());
        st.setInt(7, ph.getMinutos());
        st.setString(8, ph.getObservacion());
        st.setInt(9, ph.getId_tipo());
        st.setDate(10, ph.getFecha_inicio());
        st.setDate(11, ph.getFecha_fin());
        st.setDate(12, ph.getFecha_ingreso());
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_salida() + ":00");
            st.setTimestamp(13, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_entrada() + ":00");
            st.setTimestamp(14, new Timestamp(utilDate.getTime()));
        } catch (Exception e) {
            st.setNull(13, Types.TIMESTAMP);
            st.setNull(14, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setInt(15, id);
        st.executeUpdate();
        st.close();
        return true;
    }

    public int registroPermisoHorasVacaciones(permiso_horas ph) throws SQLException {
        usuario funcionario = getUsuario(ph.getCodigoUsu());
        int res = 0;
        conexion_oracle oracle = new conexion_oracle();
        String direccion = oracle.consultarDireccionUsuario(ph.getCodigoUsu());
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(direccion));
        st = enlace.prepareStatement("INSERT INTO permisohoras_usuario(" + (funcionario == null ? "codigo_usu" : "id_usuario") + ",hora_salida,hora_entrada,id_motivo,fecha,tiempo_solicita,horas,minutos,observacion,id_tipo,dias_habiles,dias_recargo,dias_descuento,fecha_inicio,fecha_fin,fecha_ingreso,unidad,dias_nohabiles,cierre,timestamp_inicio,timestamp_fin,denominacion,jefe,cargo_jefe,nombre_usu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, funcionario == null ? ph.getCodigoUsu() : funcionario.getId_usuario());
        st.setString(2, ph.getHora_salida());
        st.setString(3, ph.getHora_entrada());
        st.setInt(4, ph.getId_motivo());
        st.setDate(5, ph.getFecha());
        st.setString(6, ph.getTiempo_solicita());
        st.setInt(7, ph.getHoras());
        st.setInt(8, ph.getMinutos());
        st.setString(9, ph.getObservacion());
        st.setInt(10, ph.getId_tipo());
        st.setDouble(11, ph.getDias_habiles());
        st.setDouble(12, ph.getDias_recargo());
        st.setDouble(13, ph.getDias_descuento());
        st.setDate(14, ph.getFecha_inicio());
        st.setDate(15, ph.getFecha_fin());
        st.setDate(16, ph.getFecha_ingreso());
        st.setString(17, direccion);
        st.setDouble(18, ph.getDias_nohabiles());
        if (ph.getCierre().equals("")) {
            st.setNull(19, Types.CHAR);
        } else {
            st.setString(19, ph.getCierre());
        }
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_" + ph.getHora_salida() + ":00");
            st.setTimestamp(20, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_" + ph.getHora_entrada() + ":00");
            st.setTimestamp(21, new Timestamp(utilDate.getTime()));
        } catch (Exception e) {
            st.setNull(20, Types.TIMESTAMP);
            st.setNull(21, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setString(22, oracle.consultarDenominacionUsuario(ph.getCodigoUsu()));
        st.setString(23, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(24, oracle.consultarDenominacionUsuario(codigoJefe));
        if (funcionario == null) {
            st.setString(25, ph.getNombreUsu());
        } else {
            st.setNull(25, Types.VARCHAR);
        }
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean actualizarPermisoHorasVacaciones(int id, permiso_horas ph) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET id_usuario=?, hora_salida=?, hora_entrada=?, id_motivo=?, fecha=?, tiempo_solicita=?, horas=?, minutos=?, observacion=?, id_tipo=?, dias_habiles=?, dias_recargo=?, dias_descuento=?, fecha_inicio=?, fecha_fin=?, fecha_ingreso=?, dias_nohabiles=?, cierre=?, timestamp_inicio=?, timestamp_fin=? WHERE id_permiso=?");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getHora_salida());
        st.setString(3, ph.getHora_entrada());
        st.setInt(4, ph.getId_motivo());
        st.setDate(5, ph.getFecha());
        st.setString(6, ph.getTiempo_solicita());
        st.setInt(7, ph.getHoras());
        st.setInt(8, ph.getMinutos());
        st.setString(9, ph.getObservacion());
        st.setInt(10, ph.getId_tipo());
        st.setDouble(11, ph.getDias_habiles());
        st.setDouble(12, ph.getDias_recargo());
        st.setDouble(13, ph.getDias_descuento());
        st.setDate(14, ph.getFecha_inicio());
        st.setDate(15, ph.getFecha_fin());
        st.setDate(16, ph.getFecha_ingreso());
        st.setDouble(17, ph.getDias_nohabiles());
        if (ph.getCierre().equals("")) {
            st.setNull(18, Types.CHAR);
        } else {
            st.setString(18, ph.getCierre());
        }
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_salida() + ":00");
            st.setTimestamp(19, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha().toString() + "_" + ph.getHora_entrada() + ":00");
            st.setTimestamp(20, new Timestamp(utilDate.getTime()));
        } catch (Exception e) {
            st.setNull(19, Types.TIMESTAMP);
            st.setNull(20, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setInt(21, id);
        st.executeUpdate();
        st.close();
        return true;
    }

    public int registroPermisoHorasDias(permiso_horas ph) throws SQLException {
        usuario funcionario = getUsuario(ph.getCodigoUsu());
        conexion_oracle oracle = new conexion_oracle();
        String direccion = oracle.consultarDireccionUsuario(ph.getCodigoUsu());
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(direccion));
        st = enlace.prepareStatement("INSERT INTO permisohoras_usuario(" + (funcionario == null ? "codigo_usu" : "id_usuario") + ",fecha_inicio,fecha_fin,fecha_ingreso,id_motivo,fecha,dias_solicitados,dias_habiles,dias_nohabiles,dias_recargo,dias_pendientes,dias_descuento,observacion,id_tipo,unidad,timestamp_inicio,timestamp_fin,denominacion,jefe,cargo_jefe,nombre_usu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, funcionario == null ? ph.getCodigoUsu() : funcionario.getId_usuario());
        st.setDate(2, ph.getFecha_inicio());
        st.setDate(3, ph.getFecha_fin());
        st.setDate(4, ph.getFecha_ingreso());
        st.setInt(5, ph.getId_motivo());
        st.setDate(6, ph.getFecha());
        st.setInt(7, ph.getDias_solicitados());
        st.setDouble(8, ph.getDias_habiles());
        st.setDouble(9, ph.getDias_nohabiles());
        st.setDouble(10, ph.getDias_recargo());
        st.setDouble(11, ph.getDias_pendientes());
        st.setDouble(12, ph.getDias_descuento());
        st.setString(13, ph.getObservacion());
        st.setInt(14, ph.getId_tipo());
        st.setString(15, direccion);
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_08:00:00");
            st.setTimestamp(16, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_fin().toString() + "_17:00:00");
            st.setTimestamp(17, new Timestamp(utilDate.getTime()));
        } catch (ParseException e) {
            st.setNull(16, Types.TIMESTAMP);
            st.setNull(17, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setString(18, oracle.consultarDenominacionUsuario(ph.getCodigoUsu()));
        st.setString(19, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(20, oracle.consultarDenominacionUsuario(codigoJefe));
        if (funcionario == null) {
            st.setString(21, ph.getNombreUsu());
        } else {
            st.setNull(21, Types.VARCHAR);
        }
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int registroPermisoHorasDiasIESS(permiso_horas ph) throws SQLException {
        usuario funcionario = getUsuario(ph.getCodigoUsu());
        conexion_oracle oracle = new conexion_oracle();
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(ph.getDireccion()));
        st = enlace.prepareStatement("INSERT INTO permisohoras_usuario(" + (funcionario == null ? "codigo_usu" : "id_usuario") + ",fecha_inicio,fecha_fin,fecha_ingreso,id_motivo,fecha,dias_solicitados,dias_habiles,dias_nohabiles,dias_recargo,dias_pendientes,dias_descuento,observacion,id_tipo,unidad,timestamp_inicio,timestamp_fin,denominacion,jefe,cargo_jefe) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, funcionario == null ? ph.getCodigoUsu() : funcionario.getId_usuario());
        st.setDate(2, ph.getFecha_inicio());
        st.setDate(3, ph.getFecha_fin());
        st.setDate(4, ph.getFecha_ingreso());
        st.setInt(5, ph.getId_motivo());
        st.setDate(6, ph.getFecha());
        st.setInt(7, ph.getDias_solicitados());
        st.setDouble(8, ph.getDias_habiles());
        st.setDouble(9, ph.getDias_nohabiles());
        st.setDouble(10, ph.getDias_recargo());
        st.setDouble(11, ph.getDias_pendientes());
        st.setDouble(12, ph.getDias_descuento());
        st.setString(13, ph.getObservacion());
        st.setInt(14, ph.getId_tipo());
        st.setString(15, ph.getDireccion());
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_inicio().toString() + "_08:00:00");
            st.setTimestamp(16, new Timestamp(utilDate.getTime()));
            utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(ph.getFecha_fin().toString() + "_17:00:00");
            st.setTimestamp(17, new Timestamp(utilDate.getTime()));
        } catch (ParseException e) {
            st.setNull(16, Types.TIMESTAMP);
            st.setNull(17, Types.TIMESTAMP);
            System.out.println("parse utilDate | " + e);
        }
        st.setString(18, ph.getDenominacion());
        st.setString(19, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(20, oracle.consultarDenominacionUsuario(codigoJefe));
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean actualizarPermisoHorasDias(int id, permiso_horas ph) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET fecha_inicio=?, fecha_fin=?, fecha_ingreso=?, id_motivo=?, fecha=?, dias_solicitados=?, dias_habiles=?, dias_nohabiles=?, dias_recargo=?, dias_pendientes=?, dias_descuento=?, observacion=?, id_tipo=? WHERE id_permiso=?");
        st.setDate(1, ph.getFecha_inicio());
        st.setDate(2, ph.getFecha_fin());
        st.setDate(3, ph.getFecha_ingreso());
        st.setInt(4, ph.getId_motivo());
        st.setDate(5, ph.getFecha());
        st.setInt(6, ph.getDias_solicitados());
        st.setDouble(7, ph.getDias_habiles());
        st.setDouble(8, ph.getDias_nohabiles());
        st.setDouble(9, ph.getDias_recargo());
        st.setDouble(10, ph.getDias_pendientes());
        st.setDouble(11, ph.getDias_descuento());
        st.setString(12, ph.getObservacion());
        st.setInt(13, ph.getId_tipo());
        st.setInt(14, id);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean isAdministrador(int id_usuario) {
        int contador = 0;
        boolean confirmar = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON usuario.id_usuario = tipo_usuario.id_usuario WHERE usuario.id_usuario='" + id_usuario + "' AND tipo_usuario.id_rol = 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmar = true;
        }
        return confirmar;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraUsuarioEstadoID(int id_usuario, int id_estado) {
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisohoras_usuario WHERE id_usuario=? AND id_estado=?" + (id_estado > 1 ? " AND MONTH(fecha)=MONTH(CURDATE()) AND YEAR(fecha)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraUsuarioEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraIESSUsuarioEstadoID(int codUsu, int id_estado) {
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            usuario func = getUsuario(codUsu);
            String sentencia = func == null ? "SELECT * FROM permisohoras_usuario WHERE codigo_usu=? AND id_estado=?" : "SELECT * FROM permisohoras_usuario WHERE id_usuario=? AND id_estado=?";
            st = enlace.prepareStatement(sentencia + (id_estado > 1 ? " AND MONTH(fecha)=MONTH(CURDATE()) AND YEAR(fecha)=YEAR(CURDATE())" : ""));
            st.setInt(1, func == null ? codUsu : func.getId_usuario());
            st.setInt(2, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraIESSUsuarioEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraUsuarioEstadoMotivo(int idUsu, int idEstado, int idMotivo) {
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisohoras_usuario WHERE id_usuario=? AND id_estado=? AND id_motivo=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, idEstado);
            st.setInt(3, idMotivo);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraUsuarioEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraIESSUsuarioEstadoMotivo(int codUsu, int idEstado, int idMotivo) {
        usuario func = getUsuario(codUsu);
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario WHERE " + (func == null ? "codigo_usu" : "id_usuario") + "=? AND id_estado=? AND id_motivo=?";
            st = enlace.prepareStatement(sentencia + (idEstado > 1 ? " AND MONTH(fecha) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(fecha)=YEAR(CURDATE())" : ""));
            st.setInt(1, func == null ? codUsu : func.getId_usuario());
            st.setInt(2, idEstado);
            st.setInt(3, idMotivo);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraIESSUsuarioEstadoMotivo | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraIESSUsuarioEstadoMotivo(int codUsu, int idEstado, int idMotivo, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        usuario func = getUsuario(codUsu);
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario WHERE " + (func == null ? "codigo_usu" : "id_usuario") + "=? AND id_estado=? AND id_motivo=? AND fecha BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, func == null ? codUsu : func.getId_usuario());
            st.setInt(2, idEstado);
            st.setInt(3, idMotivo);
            st.setDate(4, fechaIni);
            st.setDate(5, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraIESSUsuarioEstadoMotivo(fechaIni, fechaFin) | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraEstadoMotivo(int idEstado, int idMotivo) {
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario WHERE id_estado=? AND id_motivo=? AND MONTH(fecha) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(fecha)=YEAR(CURDATE())";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idEstado);
            st.setInt(2, idMotivo);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraEstadoMotivo | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_horas> listadoPermisoHoraEstadoMotivo(int idEstado, int idMotivo, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<permiso_horas> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario WHERE id_estado=? AND id_motivo=? AND fecha BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idEstado);
            st.setInt(2, idMotivo);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_horas elemento = new permiso_horas();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHoraEstadoMotivo(fechaIni, fechaFin) | " + ex);
        }
        return listado;
    }

    public ArrayList<PermisoECU> listadoGeneralPermisoHorasECU(int id_estado) {
        ArrayList<PermisoECU> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permiso_ecu WHERE estado=?" + (id_estado > 0 ? " AND MONTH(fecha_soli)=MONTH(CURDATE()) AND YEAR(fecha_soli)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                PermisoECU elemento = new PermisoECU();
                elemento.setId(rs.getInt("id_permiso"));
                elemento.setIdUsu(rs.getInt("id_usu"));
                elemento.setIdMotivo(rs.getInt("id_motivo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setFechaSoli(rs.getDate("fecha_soli"));
                elemento.setDescripcion(rs.getString("descrip"));
                elemento.setInicio(rs.getTimestamp("inicio"));
                elemento.setFin(rs.getTimestamp("fin"));
                elemento.setTiempoSoli(rs.getString("tiempo_soli"));
                elemento.setDiasHabiles(rs.getInt("dias_habiles"));
                elemento.setDiasFinde(rs.getInt("dias_finde"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setConfirmacion(rs.getString("confirmacion"));
                elemento.setUnidad(rs.getString("unidad"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setCargoJefe(rs.getString("cargo_jefe"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoGeneralPermisoHorasECU | " + ex);
        }
        return listado;
    }

    public ArrayList<PermisoECU> listadoGeneralPermisoHorasECU(int id_estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<PermisoECU> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permiso_ecu WHERE estado=? AND fecha_soli BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                PermisoECU elemento = new PermisoECU();
                elemento.setId(rs.getInt("id_permiso"));
                elemento.setIdUsu(rs.getInt("id_usu"));
                elemento.setIdMotivo(rs.getInt("id_motivo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setFechaSoli(rs.getDate("fecha_soli"));
                elemento.setDescripcion(rs.getString("descrip"));
                elemento.setInicio(rs.getTimestamp("inicio"));
                elemento.setFin(rs.getTimestamp("fin"));
                elemento.setTiempoSoli(rs.getString("tiempo_soli"));
                elemento.setDiasHabiles(rs.getInt("dias_habiles"));
                elemento.setDiasFinde(rs.getInt("dias_finde"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setConfirmacion(rs.getString("confirmacion"));
                elemento.setUnidad(rs.getString("unidad"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setCargoJefe(rs.getString("cargo_jefe"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoGeneralPermisoHorasECU(id_estado, fechaIni, fechaFin) | " + ex);
        }
        return listado;
    }

    public ArrayList<PermisoECU> listadoPermisoHorasECU(int id_usuario, int id_estado) {
        ArrayList<PermisoECU> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permiso_ecu WHERE id_usu=? AND estado=?" + (id_estado > 0 ? " AND MONTH(fecha_soli)=MONTH(CURDATE()) AND YEAR(fecha_soli)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                PermisoECU elemento = new PermisoECU();
                elemento.setId(rs.getInt("id_permiso"));
                elemento.setIdUsu(rs.getInt("id_usu"));
                elemento.setIdMotivo(rs.getInt("id_motivo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setFechaSoli(rs.getDate("fecha_soli"));
                elemento.setDescripcion(rs.getString("descrip"));
                elemento.setInicio(rs.getTimestamp("inicio"));
                elemento.setFin(rs.getTimestamp("fin"));
                elemento.setTiempoSoli(rs.getString("tiempo_soli"));
                elemento.setDiasHabiles(rs.getInt("dias_habiles"));
                elemento.setDiasFinde(rs.getInt("dias_finde"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setConfirmacion(rs.getString("confirmacion"));
                elemento.setUnidad(rs.getString("unidad"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setCargoJefe(rs.getString("cargo_jefe"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoPermisoHorasECU | " + ex);
        }
        return listado;
    }

    public permiso_horas buscarPermisoHoras(int id_permiso) {
        permiso_horas elemento = new permiso_horas();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario WHERE id_permiso=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_permiso);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setHora_salida(rs.getString("hora_salida"));
                elemento.setHora_entrada(rs.getString("hora_entrada"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha(rs.getDate("fecha"));
                elemento.setTiempo_solicita(rs.getString("tiempo_solicita"));
                elemento.setHoras(rs.getInt("horas"));
                elemento.setMinutos(rs.getInt("minutos"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_pendientes(rs.getDouble("dias_pendientes"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setCierre(rs.getString("cierre"));
                elemento.setValido(rs.getBoolean("valido"));
                elemento.setAsistencia(rs.getString("asistencia"));
                elemento.setTimestamp_inicio(rs.getTimestamp("timestamp_inicio"));
                elemento.setTimestamp_fin(rs.getTimestamp("timestamp_fin"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setCargoJefe(rs.getString("cargo_jefe"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                elemento.setNombreUsu(rs.getString("nombre_usu"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscarPermisoHoras | " + ex);
        }
        return elemento;
    }

    public PermisoECU buscarPermisoHorasECU(int id_permiso) {
        PermisoECU elemento = new PermisoECU();
        try {
            String sentencia = "SELECT * FROM permiso_ecu WHERE id_permiso=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_permiso);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId(rs.getInt("id_permiso"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setCargoJefe(rs.getString("cargo_jefe"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscarPermisoHorasECU | " + ex);
        }
        return elemento;
    }

    public permiso_vacaciones buscarPermisoVacacion(int id_permiso) {
        permiso_vacaciones elemento = new permiso_vacaciones();
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisovacaciones_usuario WHERE id_permiso='" + id_permiso + "'ORDER BY id_permiso DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setFecha_labor(rs.getDate("fecha_labor"));
                elemento.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_pendientes(rs.getDouble("dias_pendientes"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setModalidad(rs.getString("modalidad"));
                elemento.setPeriodo(rs.getString("periodo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_registro"));
                elemento.setCodigoMotivo(rs.getInt("codigo_motivo"));
                elemento.setDireccion(rs.getString("unidad"));
                elemento.setJefe(rs.getString("jefe"));
                elemento.setDenominacion(rs.getString("denominacion"));
                elemento.setConsumo(rs.getString("consumo"));
                elemento.setYearsRestantes(rs.getString("per_rep"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                elemento.setNombreUsu(rs.getString("nombre_usu"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("buscarPermisoVacacion | " + ex);
        }

        return elemento;
    }

    public rol_pago buscarRolPagoUsuario(String codigo_usuario) {

        rol_pago elemento = new rol_pago();
        try {
            String sentencia;
            sentencia = "SELECT *FROM rol_pago WHERE codigo_usuario='" + codigo_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_rol(rs.getInt("id_rol"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setAnio(rs.getString("anio"));
                elemento.setMes(rs.getString("mes"));
                elemento.setIngreso(rs.getDouble("total_ingresos"));
                elemento.setDescuento(rs.getDouble("total_descuentos"));
                elemento.setLiquido_recibir(rs.getDouble("liquido_recibir"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<rol_pago> listadoRolesPagoUsuario(String codigo_usuario) {

        ArrayList<rol_pago> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM rol_pago WHERE codigo_usuario='" + codigo_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                rol_pago elemento = new rol_pago();
                elemento.setId_rol(rs.getInt("id_rol"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setAnio(rs.getString("anio"));
                elemento.setMes(rs.getString("mes"));
                elemento.setIngreso(rs.getDouble("total_ingresos"));
                elemento.setDescuento(rs.getDouble("total_descuentos"));
                elemento.setLiquido_recibir(rs.getDouble("liquido_recibir"));
                elemento.setLiquido_recibir(rs.getDouble("liquido_recibir"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<motivo_permiso> listadoMotivos() {

        ArrayList<motivo_permiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM motivo_permiso";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                motivo_permiso elemento = new motivo_permiso();
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setId_justificativo(rs.getInt("id_justificativo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public motivo_permiso buscarMotivoId(int id_motivo) {

        motivo_permiso elemento = new motivo_permiso();
        try {
            String sentencia;
            sentencia = "SELECT *FROM motivo_permiso WHERE id_motivo='" + id_motivo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setDescripcion(rs.getString("descripcion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public String obtenerJustificativoMotivoId(int id_motivo) {

        String justificativo = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM motivo_permiso INNER JOIN justificativo_permiso ON motivo_permiso.id_justificativo = justificativo_permiso.id_justificativo WHERE motivo_permiso.id_motivo='" + id_motivo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                justificativo = rs.getString("justificativo_permiso.descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return justificativo;
    }

    public boolean ActualizarPermisoHoras(int id_permiso, permiso_horas ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET hora_salida=?,hora_entrada=?,id_motivo=?,observacion=?,tiempo_solicita=? WHERE id_permiso= '" + id_permiso + "'");
        st.setString(1, ph.getHora_salida());
        st.setString(2, ph.getHora_entrada());
        st.setInt(3, ph.getId_motivo());
        st.setString(4, ph.getObservacion());
        st.setString(5, ph.getTiempo_solicita());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarEstadoSesion(int id_usuario) throws SQLException {

        st = enlace.prepareStatement("UPDATE sesion SET estado=? WHERE id_usuario= '" + id_usuario + "'");
        st.setInt(1, 1);
        st.executeUpdate();
        st.close();

        return true;
    }

    public int idUltimoRolPagoUsuario(String codigo_usuario) {

        int id_max = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_rol) AS idmax FROM rol_pago WHERE codigo_usuario='" + codigo_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_max = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_max;
    }

    public int registroPermisoVacaciones(permiso_vacaciones ph) throws SQLException {
        int res = 0;
        usuario funcionario = getUsuario(ph.getCodigoUsu());
        conexion_oracle oracle = new conexion_oracle();
        String direccion = oracle.consultarDireccionUsuario(ph.getCodigoUsu());
        int codigoJefe = oracle.consultarCodigoJefeDireccion(oracle.consultarCodigoDireccion(direccion));
        st = enlace.prepareStatement("INSERT INTO permisovacaciones_usuario(" + (funcionario == null ? "codigo_usu" : "id_usuario") + ",id_motivo,fecha_inicio,fecha_fin,fecha_ingreso,fecha_solicitud,fecha_labor,dias_solicitados,dias_pendientes,dias_habiles,dias_nohabiles,dias_recargo, dias_descuento,observacion,modalidad,periodo,codigo_motivo,denominacion,unidad,jefe,consumo,per_rep,cargo_jefe,nombre_usu) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, funcionario == null ? ph.getCodigoUsu() : funcionario.getId_usuario());
        st.setInt(2, ph.getId_motivo());
        st.setDate(3, ph.getFecha_inicio());
        st.setDate(4, ph.getFecha_fin());
        st.setDate(5, ph.getFecha_ingreso());
        st.setDate(6, ph.getFecha_solicitud());
        st.setDate(7, ph.getFecha_labor());
        st.setInt(8, ph.getDias_solicitados());
        st.setDouble(9, ph.getDias_pendientes());
        st.setDouble(10, ph.getDias_habiles());
        st.setDouble(11, ph.getDias_nohabiles());
        st.setDouble(12, ph.getDias_recargo());
        st.setDouble(13, ph.getDias_descuento());
        st.setString(14, ph.getObservacion());
        st.setString(15, ph.getModalidad());
        st.setString(16, ph.getPeriodo());
        st.setInt(17, ph.getCodigoMotivo());
        st.setString(18, oracle.consultarDenominacionUsuario(ph.getCodigoUsu()));
        st.setString(19, direccion);
        st.setString(20, oracle.consultarJefeUsuario(ph.getCodigoUsu()));
        st.setString(21, ph.getConsumo());
        st.setString(22, ph.getYearsRestantes());
        st.setString(23, oracle.consultarDenominacionUsuario(codigoJefe));
        if (funcionario == null) {
            st.setString(24, ph.getNombreUsu());
        } else {
            st.setNull(24, Types.VARCHAR);
        }
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean actualizarPermisoVacaciones(int id, permiso_vacaciones ph) throws SQLException {
        int codigo = consultarCodigoUsuario(ph.getId_usuario());
        conexion_oracle oracle = new conexion_oracle();
        st = enlace.prepareStatement("UPDATE permisovacaciones_usuario SET id_usuario=?, id_motivo=?, fecha_inicio=?, fecha_fin=?, fecha_ingreso=?, fecha_solicitud=?, fecha_labor=?, dias_solicitados=?, dias_pendientes=?, dias_habiles=?, dias_nohabiles=?, dias_recargo=?, dias_descuento=?,observacion=?, modalidad=?, periodo=?, codigo_motivo=?,denominacion=?, unidad=?, jefe=?, consumo=?, per_rep=? WHERE id_permiso=?");
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_motivo());
        st.setDate(3, ph.getFecha_inicio());
        st.setDate(4, ph.getFecha_fin());
        st.setDate(5, ph.getFecha_ingreso());
        st.setDate(6, ph.getFecha_solicitud());
        st.setDate(7, ph.getFecha_labor());
        st.setInt(8, ph.getDias_solicitados());
        st.setDouble(9, ph.getDias_pendientes());
        st.setDouble(10, ph.getDias_habiles());
        st.setDouble(11, ph.getDias_nohabiles());
        st.setDouble(12, ph.getDias_recargo());
        st.setDouble(13, ph.getDias_descuento());
        st.setString(14, ph.getObservacion());
        st.setString(15, ph.getModalidad());
        st.setString(16, ph.getPeriodo());
        st.setInt(17, ph.getCodigoMotivo());
        st.setString(18, oracle.consultarDenominacionUsuario(codigo));
        st.setString(19, oracle.consultarDireccionUsuario(codigo));
        st.setString(20, oracle.consultarJefeUsuario(codigo));
        st.setString(21, ph.getConsumo());
        st.setString(22, ph.getYearsRestantes());
        st.setInt(23, id);
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<motivo_vacaciones> listadoMotivosVacaciones() {

        ArrayList<motivo_vacaciones> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM motivo_permison WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                motivo_vacaciones p = new motivo_vacaciones();
                p.setId_motivo(rs.getInt("id_motivo"));
                p.setDescripcion(rs.getString("descripcion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int sumatoriaHabilesAprobadosUsuarioIDPeriodo(int id_usuario, String periodo) {
        int sumatoria = 0;
        try {
            String sentencia;
            sentencia = "SELECT SUM(dias_habiles) as total FROM permisovacaciones_usuario WHERE id_usuario='" + id_usuario + "'AND periodo='" + periodo + "' and estado=2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                sumatoria = rs.getInt("total");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return sumatoria;
    }

    public motivo_vacaciones obtenerMotivoID(int id_motivo) {

        motivo_vacaciones p = new motivo_vacaciones();
        try {
            String sentencia;
            sentencia = "SELECT *FROM motivo_permison WHERE id_motivo='" + id_motivo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_motivo(rs.getInt("id_motivo"));
                p.setDescripcion(rs.getString("descripcion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public ArrayList<permiso_vacaciones> listadoVacacionesUsuario(int id_usuario, int estado) {
        ArrayList<permiso_vacaciones> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permisovacaciones_usuario WHERE id_usuario=? AND estado=?" + (estado > 1 ? " AND MONTH(fecha_solicitud)=MONTH(CURDATE()) AND YEAR(fecha_solicitud)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_vacaciones elemento = new permiso_vacaciones();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setFecha_labor(rs.getDate("fecha_labor"));
                elemento.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                elemento.setDias_pendientes(rs.getDouble("dias_pendientes"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setModalidad(rs.getString("modalidad"));
                elemento.setPeriodo(rs.getString("periodo"));
                elemento.setEstado(rs.getInt("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesUsuario | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_vacaciones> listadoVacacionesUsuarioIESS(int codUsu, int estado) {
        ArrayList<permiso_vacaciones> listado = new ArrayList();
        try {
            usuario func = getUsuario(codUsu);
            String sentencia = func == null ? "SELECT * FROM permisovacaciones_usuario WHERE codigo_usu=? AND estado=?" : "SELECT * FROM permisovacaciones_usuario WHERE id_usuario=? AND estado=?";
            st = enlace.prepareStatement(sentencia + (estado > 1 ? " AND MONTH(fecha_solicitud)=MONTH(CURDATE()) AND YEAR(fecha_solicitud)=YEAR(CURDATE())" : ""));
            st.setInt(1, func == null ? codUsu : func.getId_usuario());
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_vacaciones elemento = new permiso_vacaciones();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setFecha_labor(rs.getDate("fecha_labor"));
                elemento.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                elemento.setDias_pendientes(rs.getDouble("dias_pendientes"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setModalidad(rs.getString("modalidad"));
                elemento.setPeriodo(rs.getString("periodo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setCodigoUsu(rs.getInt("codigo_usu"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesUsuarioIESS | " + ex);
        }
        return listado;
    }

    public ArrayList<permiso_vacaciones> listadoVacacionesUsuarioAnuladas(int id_usuario) {
        ArrayList<permiso_vacaciones> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisovacaciones_usuario WHERE id_usuario='" + id_usuario + "'AND estado=4";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                permiso_vacaciones elemento = new permiso_vacaciones();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setFecha_labor(rs.getDate("fecha_labor"));
                elemento.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                elemento.setDias_pendientes(rs.getDouble("dias_pendientes"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setModalidad(rs.getString("modalidad"));
                elemento.setPeriodo(rs.getString("periodo"));
                elemento.setEstado(rs.getInt("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesUsuarioAnuladas | " + ex);
        }
        return listado;
    }

    public permiso_vacaciones permisoVacacionesID(int id_permiso) {
        permiso_vacaciones elemento = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisovacaciones_usuario WHERE id_permiso='" + id_permiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento = new permiso_vacaciones();
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("id_motivo"));
                elemento.setFecha_inicio(rs.getDate("fecha_inicio"));
                elemento.setFecha_fin(rs.getDate("fecha_fin"));
                elemento.setFecha_ingreso(rs.getDate("fecha_ingreso"));
                elemento.setFecha_labor(rs.getDate("fecha_labor"));
                elemento.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                elemento.setDias_pendientes(rs.getInt("dias_pendientes"));
                elemento.setDias_solicitados(rs.getInt("dias_solicitados"));
                elemento.setDias_habiles(rs.getDouble("dias_habiles"));
                elemento.setDias_nohabiles(rs.getDouble("dias_nohabiles"));
                elemento.setDias_recargo(rs.getDouble("dias_recargo"));
                elemento.setDias_descuento(rs.getDouble("dias_descuento"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setModalidad(rs.getString("modalidad"));
                elemento.setPeriodo(rs.getString("periodo"));
                elemento.setEstado(rs.getInt("estado"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_registro"));
                elemento.setConsumo(rs.getString("consumo"));
                elemento.setYearsRestantes(rs.getString("per_rep"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodosRevisadas() {

        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE permisovacaciones_usuario.estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.periodo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodos(int estado) {
        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo WHERE permisovacaciones_usuario.estado=?" + (estado != 1 ? " AND MONTH(fecha_solicitud) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(fecha_solicitud)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
            //CONSULTA VACACIONES CON CODIGO USU
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo WHERE permisovacaciones_usuario.id_usuario IS NULL AND permisovacaciones_usuario.estado=?" + (estado != 1 ? " AND MONTH(permisovacaciones_usuario.fecha_solicitud) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(permisovacaciones_usuario.fecha_solicitud)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setCodigoUsu(rs.getInt("permisovacaciones_usuario.codigo_usu"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setCodigo_usuario(Integer.toString(rs.getInt("permisovacaciones_usuario.codigo_usu")));
                elemento.getFuncionario().setApellido(rs.getString("permisovacaciones_usuario.nombre_usu"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesTodos | " + ex);
        }
        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodos(int estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo WHERE permisovacaciones_usuario.estado=?" + (estado != 1 ? " AND fecha_solicitud BETWEEN ? AND ?" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            if (estado != 1) {
                st.setDate(2, fechaIni);
                st.setDate(3, fechaFin);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
            //CONSULTA VACACIONES CON CODIGO USU
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo WHERE permisovacaciones_usuario.id_usuario IS NULL AND permisovacaciones_usuario.estado=?" + (estado != 1 ? " AND permisovacaciones_usuario.fecha_solicitud BETWEEN ? AND ?" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            if (estado != 1) {
                st.setDate(2, fechaIni);
                st.setDate(3, fechaFin);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setCodigoUsu(rs.getInt("permisovacaciones_usuario.codigo_usu"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setCodigo_usuario(Integer.toString(rs.getInt("permisovacaciones_usuario.codigo_usu")));
                elemento.getFuncionario().setApellido(rs.getString("permisovacaciones_usuario.nombre_usu"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesTodos | " + ex);
        }
        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesDireccion(String codigo_funcion, int estado) {
        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE usuario.codigo_funcion=? AND permisovacaciones_usuario.estado=?" + (estado > 1 ? " AND MONTH(permisovacaciones_usuario.fecha_solicitud)=MONTH(CURDATE()) AND YEAR(permisovacaciones_usuario.fecha_solicitud)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo_funcion);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.periodo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesDireccion | " + ex);
        }
        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodosAprobadas() {

        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE permisovacaciones_usuario.estado=2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setDenominacion(rs.getString("permisovacaciones_usuario.denominacion"));
                elemento.getVacacion().setDireccion(rs.getString("permisovacaciones_usuario.unidad"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodosRechazadas() {

        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE permisovacaciones_usuario.estado=3";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<VacacionesGeneral> listadoVacacionesTodosAnuladas() {
        ArrayList<VacacionesGeneral> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM permisovacaciones_usuario INNER JOIN usuario ON usuario.id_usuario=permisovacaciones_usuario.id_usuario INNER JOIN motivo_permison ON permisovacaciones_usuario.id_motivo=motivo_permison.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE permisovacaciones_usuario.estado=4";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                VacacionesGeneral elemento = new VacacionesGeneral();
                elemento.getVacacion().setId_permiso(rs.getInt("permisovacaciones_usuario.id_permiso"));
                elemento.getVacacion().setId_usuario(rs.getInt("permisovacaciones_usuario.id_usuario"));
                elemento.getVacacion().setId_motivo(rs.getInt("permisovacaciones_usuario.id_motivo"));
                elemento.getVacacion().setFecha_inicio(rs.getDate("permisovacaciones_usuario.fecha_inicio"));
                elemento.getVacacion().setFecha_fin(rs.getDate("permisovacaciones_usuario.fecha_fin"));
                elemento.getVacacion().setFecha_ingreso(rs.getDate("permisovacaciones_usuario.fecha_ingreso"));
                elemento.getVacacion().setFecha_labor(rs.getDate("permisovacaciones_usuario.fecha_labor"));
                elemento.getVacacion().setFecha_solicitud(rs.getDate("permisovacaciones_usuario.fecha_solicitud"));
                elemento.getVacacion().setDias_pendientes(rs.getInt("permisovacaciones_usuario.dias_pendientes"));
                elemento.getVacacion().setDias_solicitados(rs.getInt("permisovacaciones_usuario.dias_solicitados"));
                elemento.getVacacion().setDias_habiles(rs.getDouble("permisovacaciones_usuario.dias_habiles"));
                elemento.getVacacion().setDias_nohabiles(rs.getDouble("permisovacaciones_usuario.dias_nohabiles"));
                elemento.getVacacion().setDias_recargo(rs.getDouble("permisovacaciones_usuario.dias_recargo"));
                elemento.getVacacion().setDias_descuento(rs.getDouble("permisovacaciones_usuario.dias_descuento"));
                elemento.getVacacion().setObservacion(rs.getString("permisovacaciones_usuario.observacion"));
                elemento.getVacacion().setModalidad(rs.getString("permisovacaciones_usuario.modalidad"));
                elemento.getVacacion().setPeriodo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getVacacion().setEstado(rs.getInt("permisovacaciones_usuario.estado"));
                elemento.getVacacion().setConsumo(rs.getString("permisovacaciones_usuario.consumo"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permison.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoVacacionesTodosAnuladas | " + ex);
        }

        return listado;
    }

    public ArrayList<HorasGeneral> listadoEnHorasDireccionEstadoID(String codigo_funcion, int id_estado) {
        ArrayList<HorasGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN usuario ON usuario.id_usuario=permisohoras_usuario.id_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo INNER JOIN organizacion ON usuario.codigo_funcion=organizacion.nivel_hijo INNER JOIN cargo ON usuario.codigo_cargo=cargo.codigo_cargo WHERE usuario.codigo_funcion=? AND permisohoras_usuario.id_estado=?" + (id_estado > 1 ? " AND MONTH(permisohoras_usuario.fecha)=MONTH(CURDATE()) AND YEAR(permisohoras_usuario.fecha)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo_funcion);
            st.setInt(2, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setId_usuario(rs.getInt("permisohoras_usuario.id_usuario"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setAsistencia(rs.getString("permisohoras_usuario.asistencia"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("organizacion.nombre"));
                elemento.setCargo(rs.getString("cargo.descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoEnHorasDireccionEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<HorasGeneral> listadoEnHorasTodosEstadoID(int id_estado) {
        ArrayList<HorasGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN usuario ON usuario.id_usuario=permisohoras_usuario.id_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_estado=" + id_estado + (id_estado != 1 ? " AND MONTH(fecha) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(fecha)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setId_usuario(rs.getInt("permisohoras_usuario.id_usuario"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setAsistencia(rs.getString("permisohoras_usuario.asistencia"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
            //CONSULTA POR CÓDIGO_USU
            sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_usuario IS NULL AND permisohoras_usuario.id_estado=?" + (id_estado != 1 ? " AND MONTH(permisohoras_usuario.fecha) IN (MONTH(CURDATE())-1, MONTH(CURDATE())) AND YEAR(permisohoras_usuario.fecha)=YEAR(CURDATE())" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setCodigoUsu(rs.getInt("permisohoras_usuario.codigo_usu"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setAsistencia(rs.getString("permisohoras_usuario.asistencia"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setCodigo_usuario(Integer.toString(rs.getInt("permisohoras_usuario.codigo_usu")));
                elemento.getFuncionario().setApellido(rs.getString("permisohoras_usuario.nombre_usu"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoEnHorasTodosEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<HorasGeneral> listadoEnHorasTodosEstadoID(int id_estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<HorasGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN usuario ON usuario.id_usuario=permisohoras_usuario.id_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_estado=?" + (id_estado != 1 ? " AND fecha BETWEEN ? AND ?" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            if (id_estado != 1) {
                st.setDate(2, fechaIni);
                st.setDate(3, fechaFin);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setId_usuario(rs.getInt("permisohoras_usuario.id_usuario"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setAsistencia(rs.getString("permisohoras_usuario.asistencia"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
            //CONSULTA POR CÓDIGO_USU
            sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_usuario IS NULL AND permisohoras_usuario.id_estado=?" + (id_estado != 1 ? " AND permisohoras_usuario.fecha BETWEEN ? AND ?" : "");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            if (id_estado != 1) {
                st.setDate(2, fechaIni);
                st.setDate(3, fechaFin);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setCodigoUsu(rs.getInt("permisohoras_usuario.codigo_usu"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setAsistencia(rs.getString("permisohoras_usuario.asistencia"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setCodigo_usuario(Integer.toString(rs.getInt("permisohoras_usuario.codigo_usu")));
                elemento.getFuncionario().setApellido(rs.getString("permisohoras_usuario.nombre_usu"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoEnHorasTodosEstadoID | " + ex);
        }
        return listado;
    }

    public ArrayList<HorasGeneral> listadoHorasOtros() {
        ArrayList<HorasGeneral> listado = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN usuario ON usuario.id_usuario=permisohoras_usuario.id_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_motivo IN (2,6) AND permisohoras_usuario.id_estado=0 AND permisohoras_usuario.valido IS NULL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setId_usuario(rs.getInt("permisohoras_usuario.id_usuario"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.getFuncionario().setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.getFuncionario().setNombre(rs.getString("usuario.nombre"));
                elemento.getFuncionario().setApellido(rs.getString("usuario.apellido"));
                elemento.getFuncionario().setCedula(rs.getString("usuario.cedula"));
                elemento.getFuncionario().setCorreo(rs.getString("usuario.correo"));
                elemento.getFuncionario().setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.getFuncionario().setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.getFuncionario().setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
            //CONSULTA POR CÓDIGO_USU
            sentencia = "SELECT * FROM permisohoras_usuario INNER JOIN motivo_permiso ON permisohoras_usuario.id_motivo=motivo_permiso.id_motivo WHERE permisohoras_usuario.id_usuario IS NULL AND permisohoras_usuario.id_motivo IN (2,6) AND permisohoras_usuario.id_estado=0 AND permisohoras_usuario.valido IS NULL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                HorasGeneral elemento = new HorasGeneral();
                elemento.getPermiso().setId_permiso(rs.getInt("permisohoras_usuario.id_permiso"));
                elemento.getPermiso().setCodigoUsu(rs.getInt("permisohoras_usuario.codigo_usu"));
                elemento.getPermiso().setHora_entrada(rs.getString("permisohoras_usuario.hora_entrada"));
                elemento.getPermiso().setHora_salida(rs.getString("permisohoras_usuario.hora_salida"));
                elemento.getPermiso().setFecha_inicio(rs.getDate("permisohoras_usuario.fecha_inicio"));
                elemento.getPermiso().setFecha_fin(rs.getDate("permisohoras_usuario.fecha_fin"));
                elemento.getPermiso().setFecha_ingreso(rs.getDate("permisohoras_usuario.fecha_ingreso"));
                elemento.getPermiso().setDias_solicitados(rs.getInt("permisohoras_usuario.dias_solicitados"));
                elemento.getPermiso().setId_motivo(rs.getInt("permisohoras_usuario.id_motivo"));
                elemento.getPermiso().setFecha(rs.getDate("permisohoras_usuario.fecha"));
                elemento.getPermiso().setTiempo_solicita(rs.getString("permisohoras_usuario.tiempo_solicita"));
                elemento.getPermiso().setHoras(rs.getInt("permisohoras_usuario.horas"));
                elemento.getPermiso().setMinutos(rs.getInt("permisohoras_usuario.minutos"));
                elemento.getPermiso().setObservacion(rs.getString("permisohoras_usuario.observacion"));
                elemento.getPermiso().setAdjunto(rs.getString("permisohoras_usuario.adjunto"));
                elemento.getPermiso().setId_tipo(rs.getInt("permisohoras_usuario.id_tipo"));
                elemento.getPermiso().setId_estado(rs.getInt("permisohoras_usuario.id_estado"));
                elemento.getPermiso().setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.getPermiso().setValido(rs.getBoolean("permisohoras_usuario.valido"));
                elemento.getPermiso().setTimestamp_inicio(rs.getTimestamp("permisohoras_usuario.timestamp_inicio"));
                elemento.getPermiso().setTimestamp_fin(rs.getTimestamp("permisohoras_usuario.timestamp_fin"));
                elemento.getFuncionario().setCodigo_usuario(Integer.toString(rs.getInt("permisohoras_usuario.codigo_usu")));
                elemento.getFuncionario().setApellido(rs.getString("permisohoras_usuario.nombre_usu"));
                elemento.setMotivo(rs.getString("motivo_permiso.descripcion"));
                elemento.setDireccion(rs.getString("permisohoras_usuario.unidad"));
                elemento.setCargo(rs.getString("permisohoras_usuario.denominacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoHorasOtros | " + ex);
        }
        return listado;
    }

    public ArrayList<viatico> listadoViaticosDireccion(String codigo_direccion) {

        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM viatico INNER JOIN usuario ON viatico.id_usuario=usuario.id_usuario\n"
                    + "INNER JOIN estado_viatico ON viatico.id_estado=estado_viatico.id_estado \n"
                    + "INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo\n"
                    + "WHERE organizacion.nivel_hijo= '" + codigo_direccion + "' OR organizacion.nivel_padre='" + codigo_direccion + "' AND estado_viatico.id_estado=1 OR estado_viatico.id_estado=2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("id_viatico"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("numero_cuenta"));
                elemento.setNombre_banco(rs.getString("nombre_banco"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setFecha(rs.getTimestamp("fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean enviarCorreo(String destinatario, String asunto, String cuerpo) {
        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
        boolean estado = false;
        String remitente = "soporte.alcaldia20@gmail.com";  //Para la dirección nomcuenta@gmail.com
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", "@sa123456");    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipients(Message.RecipientType.TO, destinatario);   //Se podrían añadir varios de la misma manera
            message.setSubject(asunto);
            message.setContent("<HTML>\n"
                    + "<HEAD><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
                    + "<TITLE>Alcaldía de Esmeraldas</TITLE>\n"
                    + "</HEAD>\n"
                    + "<BODY>\n"
                    + "<div> <table border=0 bordercolor=black align=center width=600 cellpadding=3 cellspacing=2 style=\"background-image: url(http://186.46.57.100:8084/servicios_ciudadanos/FRONT/img/correo.jpg);\" height=\"350\">\n"
                    + "<tr>\n"
                    + "<td>\n"
                    + "<font size=3 face=\"arial,verdana\">\n"
                    + "<br>\n"
                    + "" + cuerpo + "\n"
                    + "</font>\n"
                    + "</td>\n"
                    + "</tr>\n"
                    + "</table>"
                    + "                    </div>\n"
                    + "</BODY>\n"
                    + "</HTML>", "text/html");
            try ( Transport transport = session.getTransport("smtp")) {
                transport.connect("smtp.gmail.com", remitente, "@sa123456");
                transport.sendMessage(message, message.getAllRecipients());
            }
            estado = true;
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
        return estado;
    }

    public boolean enviarCorreoMod(final String destinatario, final String asunto, final String contenido) {
        boolean estado = false;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", SERVIDOR_MAIL);
        props.put("mail.smtp.user", REMITENTE_MAIL);
        props.put("mail.smtp.clave", PASS_MAIL);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(REMITENTE_MAIL));
            message.addRecipients(Message.RecipientType.TO, destinatario);
            message.setSubject(asunto);
            message.setText(contenido + FINAL_CONTENIDO);
            try ( Transport transport = session.getTransport("smtp")) {
                transport.connect(SERVIDOR_MAIL, REMITENTE_MAIL, PASS_MAIL);
                transport.sendMessage(message, message.getAllRecipients());
            }
            estado = true;
        } catch (MessagingException ex) {
            System.out.println("enviarCorreoMod | " + ex);
        }
        return estado;
    }

    public String generarToken() {
        long longToken = Math.abs(random.nextLong());
        String aleat = Long.toString(longToken, 25);
        return aleat;
    }

    public void ProcesaRoles(String id, String anio, String mes) throws Exception {
        String linea;
        String webservicelogin = "http://192.168.120.17/WebService4/apirest/login";
        URL url1 = new URL(webservicelogin);
        HttpURLConnection conexion1 = (HttpURLConnection) url1.openConnection();
        String msg = "{\"username\":\"intranet\",\"password\":\"123\"} ";
        JSONObject credentials = new JSONObject(msg);
        conexion1.setRequestMethod("POST");
        conexion1.setRequestProperty("Content-Type", "application/json");
        conexion1.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conexion1.getOutputStream());
        out.write(credentials.toString());
        out.flush();
        out.close();
        int responseCode = conexion1.getResponseCode();
        if (responseCode == 200) {
            String token = conexion1.getHeaderField("Authorization");
            String webservice = "http://192.168.120.17/WebService4/apirest/recursos/rolindividual/";
            URL url2 = new URL(webservice + id + "/" + anio + "/" + mes);
            HttpURLConnection conexion = (HttpURLConnection) url2.openConnection();
            conexion.setRequestProperty("Authorization", "Bearer " + token);
            conexion.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            while ((linea = rd.readLine()) != null) {
                JSONArray jsoncompleto = new JSONArray(linea);
                for (int i = 0; i < jsoncompleto.length(); i++) {
                    JSONObject json = (JSONObject) jsoncompleto.get(i);
                    ProcesaROL(json.getString("codigo_funcionario"), json.getString("anio"), json.getString("mes_numero"), json.getString("iddescripcion"));
                }
            }
            rd.close();
        }
    }

    public void ProcesarRolesPago(String id, String anio, String mes) throws SQLException {
        conexion_oracle link = new conexion_oracle();
        ArrayList<rol_individual> listado = link.rolePagoIndividual(id, anio, mes);
        for (rol_individual rol : listado) {
            ProcesarRolPago(rol.getCodigo_funcionario(), rol.getAnio(), rol.getMes_numero(), rol.getId_descripcion(), rol.getDescripcion(), rol.getMes_caracter());
        }
    }

    public void ProcesarRolPago(String id, String anio, String mes, String iddescripcion, String descripcion, String mes_caracter) throws SQLException {

        rol_pago elemento = new rol_pago();
        double total_ingresos = 0;
        double total_descuentos = 0;
        if (!existe(id, anio, mes, descripcion)) {
            st = enlace.prepareStatement("INSERT INTO rol_pago(codigo_usuario,anio,mes,mes_numero,descripcion) VALUES(?,?,?,?,?)");
            st.setString(1, id);
            st.setString(2, anio);
            st.setString(3, mes_caracter);
            st.setString(4, mes);
            st.setString(5, descripcion);
            st.executeUpdate();
            st.close();
            String sentencia;
            sentencia = "SELECT MAX(id_rol) as idmax FROM rol_pago WHERE codigo_usuario='" + id + "' and anio='" + anio + "' and mes_numero='" + mes + "' and descripcion='" + descripcion + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            int id_rol = 0;
            while (rs.next()) {
                id_rol = rs.getInt("idmax");
            }
            st.close();
            rs.close();
            conexion_oracle link = new conexion_oracle();
            ArrayList<rol_individual> listado = link.rolePagoIndividualTrabajador(id, anio, mes, iddescripcion);
            for (rol_individual rol : listado) {
                elemento.setIngreso(rol.getIngreso());
                elemento.setDescuento(rol.getEgreso());
                if (elemento.getIngreso() > 0) {
                    elemento.setDescripcion_ingreso(rol.getDescripcion_ingreso());
                    st = enlace.prepareStatement("INSERT INTO rol_ingresos(id_rol,ingreso,valor) VALUES(?,?,?)");
                    st.setInt(1, id_rol);
                    st.setString(2, elemento.getDescripcion_ingreso());
                    st.setDouble(3, elemento.getIngreso());
                    total_ingresos = total_ingresos + elemento.getIngreso();
                    st.executeUpdate();
                    st.close();
                } else {
                    elemento.setDescripcion_descuento(rol.getDescripcion_egreso());
                    st = enlace.prepareStatement("INSERT INTO rol_descuentos(id_rol,descuento,valor) VALUES(?,?,?)");
                    st.setInt(1, id_rol);
                    st.setString(2, elemento.getDescripcion_descuento());
                    st.setDouble(3, elemento.getDescuento());
                    total_descuentos = total_descuentos + elemento.getDescuento();
                    st.executeUpdate();
                    st.close();
                }
            }
            st = enlace.prepareStatement("UPDATE rol_pago SET total_ingresos=?, total_descuentos=?, liquido_recibir=? WHERE id_rol=" + id_rol + "");
            st.setDouble(1, limitarDecimales(total_ingresos));
            st.setDouble(2, limitarDecimales(total_descuentos));
            st.setDouble(3, limitarDecimales(total_ingresos - total_descuentos));
            st.executeUpdate();
            st.close();
        }

    }

    public void ProcesaROL(String id, String anio, String mes, String iddescripcion) throws Exception {
        rol_pago elemento = new rol_pago();
        String linea;
        String output = null;
        String webservicelogin = "http://192.168.120.17/WebService4/apirest/login";
        URL url1 = new URL(webservicelogin);
        HttpURLConnection conexion1 = (HttpURLConnection) url1.openConnection();
        String msg = "{\"username\":\"intranet\",\"password\":\"123\"} ";
        JSONObject credentials = new JSONObject(msg);
        conexion1.setRequestMethod("POST");
        conexion1.setRequestProperty("Content-Type", "application/json");
        conexion1.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(conexion1.getOutputStream());
        out.write(credentials.toString());
        out.flush();
        out.close();
        int responseCode = conexion1.getResponseCode();
        if (responseCode == 200) {
            String token = conexion1.getHeaderField("Authorization");
            String webservice = "http://192.168.120.17/WebService4/apirest/recursos/rolindividual/";
            URL url2 = new URL(webservice + id + "/" + anio + "/" + mes + "/" + iddescripcion);
            HttpURLConnection conexion = (HttpURLConnection) url2.openConnection();
            conexion.setRequestProperty("Authorization", "Bearer " + token);
            conexion.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            double total_ingresos = 0;
            double total_descuentos = 0;
            while ((linea = rd.readLine()) != null) {
                output = linea.replace("[", "").replace("]", "");
                JSONObject jsonrol = new JSONObject(output);
                String descripcion = jsonrol.getString("descripcion_rol");
                if (existe(id, anio, mes, descripcion) == false) {
                    st = enlace.prepareStatement("INSERT INTO rol_pago(codigo_usuario,anio,mes,mes_numero,descripcion) VALUES(?,?,?,?,?)");
                    st.setString(1, id);
                    st.setString(2, anio);
                    st.setString(3, jsonrol.getString("mes_caracter"));
                    st.setString(4, mes);
                    st.setString(5, descripcion);
                    st.executeUpdate();
                    st.close();
                    String sentencia;
                    sentencia = "SELECT id_rol FROM rol_pago WHERE codigo_usuario='" + id + "' and anio='" + anio + "' and mes_numero='" + mes + "' and descripcion='" + descripcion + "'";
                    st = enlace.prepareStatement(sentencia);
                    rs = st.executeQuery();
                    int id_rol = 0;
                    while (rs.next()) {
                        id_rol = rs.getInt("id_rol");
                    }
                    st.close();
                    rs.close();
                    JSONArray jsoncompleto = new JSONArray(linea);
                    for (int i = 0; i < jsoncompleto.length(); i++) {
                        JSONObject json = (JSONObject) jsoncompleto.get(i);
                        elemento.setIngreso(json.getDouble("ingreso"));
                        elemento.setDescuento(json.getDouble("descuento"));
                        if (json.getDouble("ingreso") > 0) {
                            elemento.setDescripcion_ingreso(json.getString("descripcion_ingreso"));
                            st = enlace.prepareStatement("INSERT INTO rol_ingresos(id_rol,ingreso,valor) VALUES(?,?,?)");
                            st.setInt(1, id_rol);
                            st.setString(2, elemento.getDescripcion_ingreso());
                            st.setDouble(3, elemento.getIngreso());
                            total_ingresos = total_ingresos + elemento.getIngreso();
                            st.executeUpdate();
                            st.close();
                        } else {
                            elemento.setDescripcion_descuento(json.getString("descripcion_descuento"));
                            st = enlace.prepareStatement("INSERT INTO rol_descuentos(id_rol,descuento,valor) VALUES(?,?,?)");
                            st.setInt(1, id_rol);
                            st.setString(2, elemento.getDescripcion_descuento());
                            st.setDouble(3, elemento.getDescuento());
                            total_descuentos = total_descuentos + elemento.getDescuento();
                            st.executeUpdate();
                            st.close();
                        }
                    }
                    st = enlace.prepareStatement("UPDATE rol_pago SET total_ingresos=?, total_descuentos=?, liquido_recibir=? WHERE id_rol=" + id_rol + "");
                    st.setDouble(1, limitarDecimales(total_ingresos));
                    st.setDouble(2, limitarDecimales(total_descuentos));
                    st.setDouble(3, limitarDecimales(total_ingresos - total_descuentos));
                    st.executeUpdate();
                    st.close();
                }
            }
            rd.close();
        }
    }

    public boolean existe(String id, String anio, String mes, String descripcion) throws SQLException {

        boolean respuesta = false;
        String sentencia;
        sentencia = "SELECT id_rol FROM rol_pago WHERE codigo_usuario='" + id + "' and anio='" + anio + "' and mes_numero='" + mes + "' and descripcion='" + descripcion + "'";
        st = enlace.prepareStatement(sentencia);
        rs = st.executeQuery();
        if (rs.first()) {
            respuesta = true;
        }
        st.close();
        rs.close();

        return respuesta;
    }

    public boolean ExisteRol(int codigo_usuario, int anio, String mes, String des) throws SQLException {
        boolean respuesta = false;
        String sentencia;
        sentencia = "SELECT * FROM rol_reporte WHERE codigo_funcionario=" + codigo_usuario + " and anio=" + anio + " and mes_numero=" + mes + " and REPLACE(descripcion,' ','_') ='" + des + "'";
        st = enlace.prepareStatement(sentencia);
        rs = st.executeQuery();
        if (rs.first()) {
            respuesta = true;
        }
        st.close();
        rs.close();
        return respuesta;
    }

    public boolean CreateRolReport(int codigo_usuario, int anio, String mes, String des, String nombres) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO rol_reporte(codigo_funcionario,anio,mes_numero,descripcion,nombres) VALUES(?,?,?,?,?)");
        st.setInt(1, codigo_usuario);
        st.setInt(2, anio);
        st.setString(3, mes);
        st.setString(4, des.replace("_", " "));
        st.setString(5, nombres);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroHistoria(historia ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO historia(id_usuario,descripcion,ruta,nombre,token,fecha_subida,hora_subida,id_tipo) VALUES(?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getDescripcion());
        st.setString(3, ph.getRuta());
        st.setString(4, ph.getNombre());
        st.setString(5, ph.getToken());
        st.setDate(6, ph.getFecha_subida());
        st.setString(7, ph.getHora_subida());
        st.setInt(8, ph.getId_tipo());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<historia> listadoHistoriasTipo(int id_tipo) {

        ArrayList<historia> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM historia WHERE id_tipo='" + id_tipo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                historia elemento = new historia();
                elemento.setId_historia(rs.getInt("id_historia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setToken(rs.getString("token"));
                elemento.setFecha_subida(rs.getDate("fecha_subida"));
                elemento.setHora_subida(rs.getString("hora_subida"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int diasNoHabiles(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {
        int contador = 0;
        int inicio = fecha_inicio.getDate();
        do {
            fecha_inicio.setDate(inicio);
            if (fecha_inicio.getDay() == 6 || fecha_inicio.getDay() == 0) {
                contador++;
            }
            if (diasMes((fecha_inicio.getMonth() + 1)) == inicio) {
                inicio = 1;
                fecha_inicio.setMonth((fecha_fin.getMonth() + 1));
                fecha_inicio.setDate(inicio);
            }
            inicio++;
        } while (fecha_inicio.before(fecha_fin));
        return contador;
    }

    public int diasHabilesRangoFecha(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int dias = 0;
        try {
            String sentencia;
            sentencia = "DATEDIFF('d';[fecha1];[fecha2])-DATEDIFF('ww';[fecha1];[fecha2];1)-DATEDIFF('ww';[fecha1];[fecha2];7) AS dias_habiles";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                dias = rs.getInt("dias_habiles");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return dias;
    }

    public int diasMes(int mes) {
        int cantidad = 0;
        if (mes >= 3 && mes <= 12 || mes == 1) {
            if (mes <= 7) {
                if (mes % 2 == 0) {
                    cantidad = 30;
                } else {
                    cantidad = 31;
                }
            } else {
                if (mes % 2 == 0) {
                    cantidad = 31;
                } else {
                    cantidad = 30;
                }
            }
        } else {
            if (esBisiesto()) {
                cantidad = 29;
            } else {
                cantidad = 28;
            }
        }
        return cantidad;
    }

    public boolean esBisiesto() {
        int anio = LocalDate.now().getYear();
        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public java.sql.Date diaIngreso(java.sql.Date fecha_fin) {
        java.sql.Date ingreso = fecha_fin;
        try {
            String sentencia = "SELECT IF(WEEKDAY(?) IN (4,5), ? + INTERVAL 7 - WEEKDAY(?) DAY, ? + INTERVAL 1 DAY) AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fecha_fin);
            st.setDate(2, fecha_fin);
            st.setDate(3, fecha_fin);
            st.setDate(4, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                ingreso = rs.getDate("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("diaIngreso | " + ex);
            return fecha_fin;
        }
        return ingreso;
    }

    public java.sql.Date diaIngresoCalendario(java.sql.Date fecha_fin) {
        java.sql.Date ingreso = fecha_fin;
        try {
            String sentencia = "SELECT ? + INTERVAL 1 DAY AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                ingreso = rs.getDate("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("diaIngresoCalendario | " + ex);
            return fecha_fin;
        }
        return ingreso;
    }

    public double limitarDecimales(double valor) {
        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
        separadoresPersonalizados.setDecimalSeparator('.');
        DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
        return Double.parseDouble(formato1.format(valor));
    }

    public int historiasRecientes() {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_historia) AS idmax FROM historia";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public java.sql.Date fechaNacimientoUsuario(int id_usuario) {
        java.sql.Date fecha_nacimiento = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                fecha_nacimiento = rs.getDate("fecha_nacimiento");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return fecha_nacimiento;
    }

    public boolean eliminarloginImagen(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM historia WHERE id_historia= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public int historiaMenor() {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MIN(id_historia) AS idmax FROM historia WHERE id_tipo=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int historiasMayor() {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_historia) AS idmax FROM historia WHERE id_tipo=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public void traspasoRutaViaticoInforme(int id_viatico, int id_informe) {
        ArrayList<ruta_viatico> listadoRutas = listadoRutasViatico(id_viatico);
        for (int paso = 0; paso < listadoRutas.size(); paso++) {
            ruta_informe elemento = new ruta_informe(id_informe, listadoRutas.get(paso).getId_lugarpartida(), listadoRutas.get(paso).getId_lugarllegada(), listadoRutas.get(paso).getTipo_transporte(), listadoRutas.get(paso).getNombre_transporte(), listadoRutas.get(paso).getFecha_salida(), listadoRutas.get(paso).getHora_salida(), listadoRutas.get(paso).getFecha_llegada(), listadoRutas.get(paso).getHora_llegada());
            try {
                registroRutaInforme(elemento);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    public void traspasoParticipantesViaticoInforme(int id_viatico, int id_informe) {
        ArrayList<participacion_viatico> listadoParticipante = listadoParticipantesViatico(id_viatico);
        for (int paso = 0; paso < listadoParticipante.size(); paso++) {
            participacion_informe elemento = new participacion_informe(id_informe, listadoParticipante.get(paso).getId_usuario());
            try {
                registroParticipanteInforme(elemento);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    public boolean registroAdjuntoPermisohora(adjunto_permisohora ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO adjunto_permiso(id_permiso,nombre,ruta) VALUES(?,?,?)");
        st.setInt(1, ph.getId_permiso());
        st.setString(2, ph.getNombre());
        st.setString(3, ph.getRuta());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroActividad(actividad ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO actividad(id_usuario,tarea,fecha_actividad,fecha_registro,hora_inicio,hora_fin,herramienta,herramienta_otro,observacion,requiriente,fecha_limite,indicador,avance,grado) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getTarea());
        st.setDate(3, ph.getFecha_actividad());
        st.setDate(4, ph.getFecha_registro());
        st.setString(5, ph.getHora_inicio());
        st.setString(6, ph.getHora_fin());
        st.setString(7, ph.getHerramienta());
        st.setString(8, ph.getHerramienta_otro());
        st.setString(9, ph.getObservacion());
        st.setString(10, ph.getRequiriente());
        st.setDate(11, ph.getFecha_limite());
        st.setString(12, ph.getIndicador());
        st.setString(13, ph.getAvance());
        st.setString(14, ph.getGrado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroUsuario(usuario ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO usuario(codigo_usuario,nombre,apellido,cedula,correo,clave,iniciales,codigo_cargo,codigo_unidad,fecha_nacimiento,fecha_creacion) VALUES(?,?,?,?,?,MD5(?),?,?,?,?,?)");
        st.setString(1, ph.getCodigo_usuario());
        st.setString(2, ph.getNombre());
        st.setString(3, ph.getApellido());
        st.setString(4, ph.getCedula());
        st.setString(5, ph.getCorreo());
        st.setString(6, ph.getClave());
        st.setString(7, ph.getIniciales());
        st.setString(8, ph.getCodigo_cargo());
        st.setString(9, ph.getCodigo_unidad());
        st.setDate(10, ph.getFecha_nacimiento());
        st.setDate(11, ph.getFecha_creacion());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean inicioSesion(inifin_sesion ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO inifin_sesion(usuario) VALUES(?)");
        st.setString(1, ph.getUsuario());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroEquipo(inventario ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO inventario(nombre, usuario_dominio, tipo_dispositivo, macaddress, memoria, procesador, direccion_ip, conexion_dhcp, conexion_permanente, antivirus, cabildo, sigame, office365, arquitectura_so, codigo_bodega, observaciones, nombre_edificio, piso, unidad_administrativa, funcionario) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        st.setString(1, ph.getNombre());
        st.setString(2, ph.getUsuariodominio());
        st.setString(3, ph.getTipodispositivo());
        st.setString(4, ph.getMacaddress());
        st.setString(5, ph.getMemoria());
        st.setString(6, ph.getProcesador());
        st.setString(7, ph.getDireccion_ip());
        st.setString(8, ph.getConexion_dhcp());
        st.setString(9, ph.getConexion_permanente());
        st.setString(10, ph.getAntivirus());
        st.setString(11, ph.getCabildo());
        st.setString(12, ph.getSigame());
        st.setString(13, ph.getOffice365());
        st.setString(14, ph.getArquitectura_so());
        st.setString(15, ph.getCodigo_bodega());
        st.setString(16, ph.getObservaciones());
        st.setString(17, ph.getNombre_edificio());
        st.setString(18, ph.getPiso());
        st.setString(19, ph.getUnidad_administrativa());
        st.setString(20, ph.getFuncionario());

        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarInventario(inventario elemento, int id) {

        try {
            st = enlace.prepareStatement("UPDATE inventario SET nombre=?, usuario_dominio=?, tipo_dispositivo=?, macaddress=?, memoria=?, procesador=?, direccion_ip=?, conexion_dhcp=?, conexion_permanente=?, antivirus=?, cabildo=?, sigame=?, office365=?, arquitectura_so=?, codigo_bodega=?, observaciones=?, nombre_edificio=?, piso=?, unidad_administrativa=?, funcionario=? WHERE id_inventario= '" + id + "'");
            st.setString(1, elemento.getNombre());
            st.setString(2, elemento.getUsuariodominio());
            st.setString(3, elemento.getTipodispositivo());
            st.setString(4, elemento.getMacaddress());
            st.setString(5, elemento.getMemoria());
            st.setString(6, elemento.getProcesador());
            st.setString(7, elemento.getDireccion_ip());
            st.setString(8, elemento.getConexion_dhcp());
            st.setString(9, elemento.getConexion_permanente());
            st.setString(10, elemento.getAntivirus());
            st.setString(11, elemento.getCabildo());
            st.setString(12, elemento.getSigame());
            st.setString(13, elemento.getOffice365());
            st.setString(14, elemento.getArquitectura_so());
            st.setString(15, elemento.getCodigo_bodega());
            st.setString(16, elemento.getObservaciones());
            st.setString(17, elemento.getNombre_edificio());
            st.setString(18, elemento.getPiso());
            st.setString(19, elemento.getUnidad_administrativa());
            st.setString(20, elemento.getFuncionario());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean eliminarEquipo(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM inventario WHERE id_inventario = '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean ActualizarActividad(int id_actividad, actividad ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE actividad SET tarea=?,fecha_actividad=?,hora_inicio=?,hora_fin=?,herramienta=?,herramienta_otro=?,observacion=?,requiriente=?,fecha_limite=?,indicador=?,avance=?,grado=? WHERE id_actividad= '" + id_actividad + "'");
        st.setString(1, ph.getTarea());
        st.setDate(2, ph.getFecha_actividad());
        st.setString(3, ph.getHora_inicio());
        st.setString(4, ph.getHora_fin());
        st.setString(5, ph.getHerramienta());
        st.setString(6, ph.getHerramienta_otro());
        st.setString(7, ph.getObservacion());
        st.setString(8, ph.getRequiriente());
        st.setDate(9, ph.getFecha_limite());
        st.setString(10, ph.getIndicador());
        st.setString(11, ph.getAvance());
        st.setString(12, ph.getGrado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean eliminarColaboradorActividad(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM colaborador_actividad WHERE id_usuario= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean esRangoCorrecto(Date fecha_actividad, int dias) {
        boolean confirmacion = false;
        try {
            String sentencia;
            sentencia = "SELECT '" + fecha_actividad + "'BETWEEN CURDATE() - INTERVAL '" + dias + "' DAY AND CURDATE() AS confirmar";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                confirmacion = rs.getBoolean("confirmar");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return confirmacion;
    }

    public boolean esRangoCorrectoIndividual(Date fecha_actividad, int cantidad, String formato) {
        boolean confirmacion = false;
        try {
            String sentencia;
            sentencia = "SELECT '" + fecha_actividad + "'BETWEEN CURDATE() - INTERVAL '" + cantidad + "' " + formato + " AND CURDATE() AS confirmar";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                confirmacion = rs.getBoolean("confirmar");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return confirmacion;
    }

    public boolean tienePermisoEdicionActividadesUsuarioID(int id_usuario) {
        int contador = 0;
        boolean confirmacion = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisoedicionactividad_usuario WHERE id_usuario ='" + id_usuario + "' AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmacion = true;
        }
        return confirmacion;
    }

    public int obtenerIntervaloDiasRegistroActividad() {
        int dias = 0;
        try {
            String sentencia;
            sentencia = "SELECT dias FROM intervalo_actividad WHERE id_intervalo=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                dias = rs.getInt("dias");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return dias;
    }

    public formato_intervalo obtenerIntervaloTiempoRegistroActividadesUsuarioID(int id_usuario) {
        formato_intervalo elemento = new formato_intervalo();
        try {
            String sentencia;
            sentencia = "SELECT *FROM formato_intervalo INNER JOIN permisoedicionactividad_usuario ON permisoedicionactividad_usuario.id_formato = formato_intervalo.id_formato  WHERE permisoedicionactividad_usuario.id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_formato(rs.getInt("formato_intervalo.id_formato"));
                elemento.setCantidad(rs.getInt("permisoedicionactividad_usuario.cantidad"));
                elemento.setDescripcion(rs.getString("formato_intervalo.descripcion"));
                elemento.setFormato(rs.getString("formato_intervalo.formato"));
                elemento.setFecha_creacion(rs.getTimestamp("formato_intervalo.fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("formato_intervalo.fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public actividad BuscarActividadID(int id_actividad) {

        actividad elemento = new actividad();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_actividad='" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public boolean eliminarActividad(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM actividad WHERE id_actividad= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarUsuarioId(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM usuario WHERE id_usuario= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public ArrayList<actividad> listadoActividadesRegistradasUsuarioId(int id_usuario) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND estado=0 ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasUsuarioId(int id_usuario) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND estado=1 ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasRangoFechaUsuarioId(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasRangoFechaCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON  usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasRangoFechaUsuarioId(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND estado=1 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasRangoFechaCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND estado=1 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesTodasRangoFechaUsuarioId(int id_usuario, Date fecha_inicio, Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesTodasRangoFechaRequirenteID(String nombre, Date fecha_inicio, Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE requiriente LIKE '%" + nombre + "%'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesTodasRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON  usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON  usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND actividad.estado=1 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON  usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividades(int id_usuario) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE fecha_actividad BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() AND id_usuario= '" + id_usuario + "' ORDER BY fecha_actividad DESC;";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividadesRequirenteNombre(String nombre) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE fecha_actividad BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE() AND requiriente LIKE '%" + nombre + "%' ORDER BY fecha_actividad DESC;";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasUsuarioId(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE id_usuario= '" + id_usuario + "AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean registroComentarioActividad(comentario_actividad ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO comentarios_actividad(id_actividad,id_usuario,descripcion,fecha_registro) VALUES(?,?,?,?)");
        st.setInt(1, ph.getId_actividad());
        st.setInt(2, ph.getId_usuario());
        st.setString(3, ph.getDescripcion());
        st.setDate(4, ph.getFecha_registro());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<comentario_actividad> listadoComentarioActividad(int id_actividad) {

        ArrayList<comentario_actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM comentarios_actividad WHERE id_actividad= '" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                comentario_actividad elemento = new comentario_actividad();
                elemento.setId_comentario(rs.getInt("id_comentario"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<comentario_actividad> listadoComentarioActividadRangoFechaID(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<comentario_actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                comentario_actividad elemento = new comentario_actividad();
                elemento.setId_comentario(rs.getInt("comentarios_actividad.id_comentario"));
                elemento.setId_usuario(rs.getInt("comentarios_actividad.id_usuario"));
                elemento.setId_actividad(rs.getInt("comentarios_actividad.id_actividad"));
                elemento.setDescripcion(rs.getString("comentarios_actividad.descripcion"));
                elemento.setFecha_registro(rs.getDate("comentarios_actividad.fecha_registro"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int cantidadComentariosCorregidosRangoIdUsuario(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int cantidad = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND comentarios_actividad.estado=1 AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return cantidad;
    }

    public int cantidadComentariosRangoIdUsuario(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int cantidad = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return cantidad;
    }

    public int cantidadComentariosNoCorregidosRangoIdUsuario(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int cantidad = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM comentarios_actividad INNER JOIN actividad ON actividad.id_actividad=comentarios_actividad.id_actividad INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.id_usuario= '" + id_usuario + "'AND comentarios_actividad.estado=0 AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return cantidad;
    }

    public comentario_actividad buscarComentarioActividadID(int id_actividad) {

        comentario_actividad elemento = new comentario_actividad();
        try {
            String sentencia;
            sentencia = "SELECT *FROM comentarios_actividad WHERE id_actividad= '" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_comentario(rs.getInt("id_comentario"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public boolean registroEvidenciaActividad(evidencia_actividad ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO evidencia_actividad(id_actividad,ruta,nombre) VALUES(?,?,?)");
        st.setInt(1, ph.getId_actividad());
        st.setString(2, ph.getRuta());
        st.setString(3, ph.getNombre());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<evidencia_actividad> listadoEvidenciaActividad(int id_actividad) {

        ArrayList<evidencia_actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM evidencia_actividad WHERE id_actividad='" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                evidencia_actividad elemento = new evidencia_actividad();
                elemento.setId_evidencia(rs.getInt("id_evidencia"));
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setNombre(rs.getString("nombre"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean eliminarEvidenciaActividad(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM evidencia_actividad WHERE id_evidencia= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public evidencia_actividad buscarEvidenciaActividadID(int id) {

        evidencia_actividad elemento = new evidencia_actividad();
        try {
            String sentencia;
            sentencia = "SELECT *FROM evidencia_actividad WHERE id_evidencia= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_evidencia(rs.getInt("id_evidencia"));
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setNombre(rs.getString("nombre"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public boolean aprobarActividad(int id_actividad) {

        try {
            st = enlace.prepareStatement("UPDATE actividad SET estado=? WHERE id_actividad= '" + id_actividad + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public boolean aprobarActividades(int id_usuario) {

        try {
            st = enlace.prepareStatement("UPDATE actividad SET estado=? WHERE id_usuario= '" + id_usuario + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public boolean aprobarActividadesDireccion(String codigo_unidad) {

        try {
            st = enlace.prepareStatement("UPDATE actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad SET estado=? WHERE actividad.estado=0 AND organizacion.nivel_padre='" + codigo_unidad + "' OR organizacion.nivel_hijo='" + codigo_unidad + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public boolean aprobarActividadesUsuario(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        try {
            st = enlace.prepareStatement("UPDATE actividad SET actividad.estado = ? WHERE estado=0 AND id_usuario= '" + id_usuario + "' AND actividad.id_actividad NOT IN (SELECT * FROM(SELECT DISTINCT actividad.id_actividad FROM actividad , comentarios_actividad  WHERE actividad.id_actividad = comentarios_actividad.id_actividad AND actividad.id_usuario= '" + id_usuario + "' AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ) AS t) AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public boolean aprobarActividadesDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        try {
            st = enlace.prepareStatement("UPDATE actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario SET actividad.estado = ? WHERE usuario.codigo_funcion= '" + codigo_funcion + "' AND actividad.id_actividad NOT IN (SELECT * FROM(SELECT DISTINCT actividad.id_actividad FROM actividad , comentarios_actividad WHERE actividad.id_actividad = comentarios_actividad.id_actividad AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ) AS t) AND actividad.estado=0 AND actividad.fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public int ActividadRecienteUsuario(int id_usuario) {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_actividad) AS idmax FROM actividad WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public int idMaxUsuario() {

        int ruta = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_usuario) AS idmax FROM usuario ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                ruta = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return ruta;
    }

    public String ObtenerFuncionUsuarioID(int id_usuario) {

        String funcion = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                funcion = rs.getString("funcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return funcion;
    }

    public String obtenerCodigoFuncionUsuario(int id_usuario) {

        String funcion = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                funcion = rs.getString("codigo_funcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return funcion;
    }

    public ArrayList<actividad> listadoActividadesDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON actividad.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("actividad.id_actividad"));
                elemento.setId_usuario(rs.getInt("actividad.id_usuario"));
                elemento.setTarea(rs.getString("actividad.tarea"));
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setHora_inicio(rs.getString("actividad.hora_inicio"));
                elemento.setHora_fin(rs.getString("actividad.hora_fin"));
                elemento.setFecha_registro(rs.getDate("actividad.fecha_registro"));
                elemento.setHerramienta(rs.getString("actividad.herramienta"));
                elemento.setHerramienta_otro(rs.getString("actividad.herramienta_otro"));
                elemento.setObservacion(rs.getString("actividad.observacion"));
                elemento.setRequiriente(rs.getString("actividad.requiriente"));
                elemento.setFecha_limite(rs.getDate("actividad.fecha_limite"));
                elemento.setIndicador(rs.getString("actividad.indicador"));
                elemento.setAvance(rs.getString("actividad.avance"));
                elemento.setGrado(rs.getString("actividad.grado"));
                elemento.setEstado(rs.getBoolean("actividad.estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON actividad.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "' AND actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("actividad.id_actividad"));
                elemento.setId_usuario(rs.getInt("actividad.id_usuario"));
                elemento.setTarea(rs.getString("actividad.tarea"));
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setHora_inicio(rs.getString("actividad.hora_inicio"));
                elemento.setHora_fin(rs.getString("actividad.hora_fin"));
                elemento.setFecha_registro(rs.getDate("actividad.fecha_registro"));
                elemento.setHerramienta(rs.getString("actividad.herramienta"));
                elemento.setHerramienta_otro(rs.getString("actividad.herramienta_otro"));
                elemento.setObservacion(rs.getString("actividad.observacion"));
                elemento.setRequiriente(rs.getString("actividad.requiriente"));
                elemento.setFecha_limite(rs.getDate("actividad.fecha_limite"));
                elemento.setIndicador(rs.getString("actividad.indicador"));
                elemento.setAvance(rs.getString("actividad.avance"));
                elemento.setGrado(rs.getString("actividad.grado"));
                elemento.setEstado(rs.getBoolean("actividad.estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON actividad.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "' AND actividad.estado=1 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("actividad.id_actividad"));
                elemento.setId_usuario(rs.getInt("actividad.id_usuario"));
                elemento.setTarea(rs.getString("actividad.tarea"));
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setHora_inicio(rs.getString("actividad.hora_inicio"));
                elemento.setHora_fin(rs.getString("actividad.hora_fin"));
                elemento.setFecha_registro(rs.getDate("actividad.fecha_registro"));
                elemento.setHerramienta(rs.getString("actividad.herramienta"));
                elemento.setHerramienta_otro(rs.getString("actividad.herramienta_otro"));
                elemento.setObservacion(rs.getString("actividad.observacion"));
                elemento.setRequiriente(rs.getString("actividad.requiriente"));
                elemento.setFecha_limite(rs.getDate("actividad.fecha_limite"));
                elemento.setIndicador(rs.getString("actividad.indicador"));
                elemento.setAvance(rs.getString("actividad.avance"));
                elemento.setGrado(rs.getString("actividad.grado"));
                elemento.setEstado(rs.getBoolean("actividad.estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int cantidadActividadesRegistradasDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "' AND actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesRegistradasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesCorregidasDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesCorregidasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesCorregidaUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesRegistradasUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND estado=0 ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesAprobadasUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND estado=1 ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesAprobadasDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesAprobadasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesDireccionCodigoFuncion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadActividadesDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as cantidad FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("cantidad");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public double porcentajeActividades(int cantidad, float total) {
        double porcentaje = 0;
        porcentaje = ((cantidad / total) * 100);
        return limitarDecimales(porcentaje);
    }

    public String iniciales(String cadena) {
        String nombre = cadena;
        int largo = nombre.length();
        String ini = " ";
        String xx = nombre.substring(0, 1);
        String x = "";
        String xxx = "";
        for (int i = 0; i < largo; i++) {
            x = nombre.substring(i, i + 1);
            if (x.equals(ini)) {
                xxx = nombre.substring(i + 1, i + 2);
                xx = xx + xxx;
            }
        }
        return xx;
    }

    public ArrayList<actividad> listadoActividadesDireccion(String codigo_unidad) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad WHERE fecha_actividad BETWEEN DATE_ADD(NOW(), INTERVAL -20 DAY) AND NOW() AND actividad.estado=0 AND organizacion.nivel_padre='" + codigo_unidad + "' OR organizacion.nivel_hijo='" + codigo_unidad + "'ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividadesDireccion(String codigo_unidad) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad WHERE organizacion.nivel_padre='" + codigo_unidad + "' OR organizacion.nivel_hijo='" + codigo_unidad + "'ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividadesInstitucion() {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividadesInstitucionAprobadas() {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoTodasActividadesInstitucionRegistradas() {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad WHERE estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesAprobadasDireccion(String codigo_unidad) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN organizacion ON organizacion.nivel_hijo=usuario.codigo_unidad WHERE actividad.estado=1 AND organizacion.nivel_padre='" + codigo_unidad + "' OR organizacion.nivel_hijo='" + codigo_unidad + "'ORDER BY fecha_actividad DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> listadoActividadesRegistradasUnidad(String codigo_unidad) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE actividad.estado=0 AND usuario.codigo_unidad='" + codigo_unidad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_actividad(rs.getInt("id_actividad"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setTarea(rs.getString("tarea"));
                elemento.setFecha_actividad(rs.getDate("fecha_actividad"));
                elemento.setHora_inicio(rs.getString("hora_inicio"));
                elemento.setHora_fin(rs.getString("hora_fin"));
                elemento.setFecha_registro(rs.getDate("fecha_registro"));
                elemento.setHerramienta(rs.getString("herramienta"));
                elemento.setHerramienta_otro(rs.getString("herramienta_otro"));
                elemento.setObservacion(rs.getString("observacion"));
                elemento.setRequiriente(rs.getString("requiriente"));
                elemento.setFecha_limite(rs.getDate("fecha_limite"));
                elemento.setIndicador(rs.getString("indicador"));
                elemento.setAvance(rs.getString("avance"));
                elemento.setGrado(rs.getString("grado"));
                elemento.setEstado(rs.getBoolean("estado"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public String[] separarElemento(String elemento) {
        String[] parts = elemento.split(",");
        return parts;
    }

    public boolean eliminarRolUsuarioID(int id_rol) {

        try {
            st = enlace.prepareStatement("DELETE FROM rol_pago WHERE id_rol= '" + id_rol + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean registroCalendario(calendario ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO estado_usuario(id_usuario,nombre,descripcion,) VALUES(?,?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getNombre());
        st.setString(3, ph.getDescripcion());
        st.setDate(4, ph.getFecha_creacion());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<calendario> listadoCalendarioUsuario(int id_usuario) {

        ArrayList<calendario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM calendario WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                calendario elemento = new calendario();
                elemento.setId_calendario(rs.getInt("id_calendario"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public String totalHorasActividadesRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        String horas = null;
        try {
            String sentencia;
            sentencia = "SELECT (SELECT DISTINCT CONCAT( (SELECT SUM(HOUR((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')+ CASE WHEN (SELECT SUM(MINUTE((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')>59 THEN (SELECT  TRUNCATE((SELECT SUM(MINUTE((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')/60 , 0)) ELSE 0 END , 'h y ', SUM(MINUTE((TIMEDIFF(hora_inicio, hora_fin))))% 60, 'm' ) FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "') AS total FROM actividad"
                    + " INNER JOIN usuario ON actividad.id_usuario= usuario.id_usuario AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "' WHERE actividad.id_usuario=" + id_usuario + "";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getString("total");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public String totalHorasActividadesRangoDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        String horas = null;
        try {
            String sentencia;
            sentencia = "SELECT (SELECT DISTINCT CONCAT( (SELECT SUM(HOUR((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')+ CASE WHEN (SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio,actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')>59 THEN (SELECT  TRUNCATE((SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')/60 , 0)) ELSE 0 END , 'h y ', SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin))))% 60, 'm' ) FROM actividad"
                    + " WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "') AS total FROM actividad"
                    + " INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion='" + codigo_funcion + "' AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getString("total");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public String totalHorasActividadesRangoDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        String horas = null;
        try {
            String sentencia;
            sentencia = "SELECT (SELECT DISTINCT CONCAT( (SELECT SUM(HOUR((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')+ CASE WHEN (SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio,actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')>59 THEN (SELECT  TRUNCATE((SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')/60 , 0)) ELSE 0 END , 'h y ', SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin))))% 60, 'm' ) FROM actividad"
                    + " WHERE actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "') AS total FROM actividad"
                    + " INNER JOIN usuario ON actividad.id_usuario=usuario.id_usuario WHERE actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getString("total");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public int totalHorasActividadesRangoUsuario(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int horas = 0;
        try {
            String sentencia;
            sentencia = "SELECT (SELECT DISTINCT CONCAT( (SELECT SUM(HOUR((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')+ CASE WHEN (SELECT SUM(MINUTE((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')>59 THEN (SELECT  TRUNCATE((SELECT SUM(MINUTE((TIMEDIFF(hora_inicio, hora_fin)))) AS actividad_tiempo FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "')/60 , 0)) ELSE 0 END) FROM actividad"
                    + " WHERE actividad.id_usuario='" + id_usuario + "'AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "') AS total FROM actividad"
                    + " INNER JOIN usuario ON actividad.id_usuario= usuario.id_usuario AND actividad.fecha_actividad BETWEEN'" + fecha_inicio + "'AND'" + fecha_fin + "' WHERE actividad.id_usuario=" + id_usuario + "";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getInt("total");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public ArrayList<usuario> listadoUsuariosDireccionTeletrabajo(String codigo_funcion) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE codigo_funcion='" + codigo_funcion + "' AND tipo_funcion=1 ORDER BY apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosDireccionCodigoFuncion(String codigo_funcion) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN permiso_usuario ON usuario.id_usuario = permiso_usuario.id_usuario WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND permiso_usuario.acceso=1 ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosActivos() {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN permiso_usuario ON usuario.id_usuario = permiso_usuario.id_usuario WHERE permiso_usuario.acceso=1 ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosActivosCodigoFuncion(String codigo_funcion) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON tipo_usuario.id_usuario=usuario.id_usuario INNER JOIN permiso_usuario ON usuario.id_usuario = permiso_usuario.id_usuario WHERE usuario.codigo_funcion='" + codigo_funcion + "'AND permiso_usuario.acceso=1 AND tipo_usuario.id_rol!=4 GROUP BY tipo_usuario.id_usuario ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoMaximaAutoridadDelegado() {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN permiso_usuario ON usuario.id_usuario = permiso_usuario.id_usuario WHERE usuario.funcion LIKE '%vicealcalde%' OR usuario.funcion LIKE '%alcalde%' AND permiso_usuario.acceso=1 ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosDireccionTeletrabajoSinDirector(String codigo_funcion) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON tipo_usuario.id_usuario=usuario.id_usuario WHERE usuario.codigo_funcion='" + codigo_funcion + "' AND tipo_usuario.id_rol!=4 AND usuario.tipo_funcion=1 GROUP BY tipo_usuario.id_usuario ORDER BY usuario.apellido DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosDireccionesTeletrabajoSinDirector() {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON tipo_usuario.id_usuario=usuario.id_usuario WHERE tipo_usuario.id_rol!=4 AND usuario.tipo_funcion=1 ORDER BY usuario.apellido DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuariosTeletrabajo() {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE tipo_funcion=1 ORDER BY apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public usuario directorDireccionCodigoFuncion(String codigo_funcion) {

        usuario elemento = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN tipo_usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE usuario.codigo_funcion='" + codigo_funcion + "' AND rol.descripcion='director'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<usuario> listadosDirectoresDireccionesUnidades() {
        final ArrayList<usuario> listado = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM usuario INNER JOIN tipo_usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN permiso_usuario on permiso_usuario.id_usuario=usuario.id_usuario where tipo_usuario.id_rol=4 AND permiso_usuario.acceso=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setCodigo_usuario(rs.getString("codigo_usuario"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setCedula(rs.getString("cedula"));
                elemento.setCodigo_cargo(rs.getString("codigo_cargo"));
                elemento.setApellido(rs.getString("apellido"));
                elemento.setCorreo(rs.getString("correo"));
                elemento.setClave(rs.getString("clave"));
                elemento.setIniciales(rs.getString("iniciales"));
                elemento.setCodigo_unidad(rs.getString("codigo_unidad"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int estadoComentarioActividad(int id_comentario) {

        int estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM comentarios_actividad WHERE id_comentario='" + id_comentario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getInt("estado");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public String buscarFirmaUsuarioID(int id_usuario) {

        String firma = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                firma = rs.getString("firma");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return firma;
    }

    public boolean reenviarActividadUsuario(int id_comentario) {

        try {
            st = enlace.prepareStatement("UPDATE comentarios_actividad SET estado = ? WHERE id_comentario= '" + id_comentario + "'");
            st.setBoolean(1, true);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return true;
    }

    public ArrayList<actividad> resumenActividadesDireccionRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesUsuarioIDRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesRegistradasUsuarioIDRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND actividad.estado=0 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesAprobadasUsuarioIDRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.id_usuario= '" + id_usuario + "'AND actividad.estado=1 AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesAprobadasDireccionRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=1 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesAprobadasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=1 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesRegistradasDireccionRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesRegistradasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesCorregidasDireccionRangoFecha(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesCorregidasDirecciones(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesCorregidasDirecciones(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesCorregidasUsuarioIDRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> cantidadActividadesCorregidasUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario INNER JOIN comentarios_actividad ON comentarios_actividad.id_actividad=actividad.id_actividad WHERE usuario.id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.fecha_actividad ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setFecha_actividad(rs.getDate("actividad.fecha_actividad"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuarioActividadesRegistradasRangoDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN actividad ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY usuario.id_usuario ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<usuario> listadoUsuarioActividadesAprobadasRangoDireccion(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN actividad ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=1 GROUP BY usuario.id_usuario ORDER BY usuario.apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesAprobadasDireccionRangoFechaFuncionarios(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=1 GROUP BY actividad.id_usuario ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_usuario(rs.getInt("actividad.id_usuario"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<actividad> resumenActividadesRegistradasDireccionRangoFechaFuncionarios(String codigo_funcion, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT actividad.fecha_actividad, COUNT(*) AS contador FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario WHERE usuario.codigo_funcion= '" + codigo_funcion + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "' AND actividad.estado=0 GROUP BY actividad.id_usuario ORDER BY actividad.fecha_actividad ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad elemento = new actividad();
                elemento.setId_usuario(rs.getInt("actividad.id_usuario"));
                elemento.setId_actividad(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<estado_usuario> resumenAccesosIntranetUsuarioIDRangoFecha(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        ArrayList<estado_usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT fecha_acceso,COUNT(*) AS contador FROM estado_usuario WHERE id_usuario= '" + id_usuario + "'AND fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'GROUP BY fecha_acceso ORDER BY fecha_acceso ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado_usuario elemento = new estado_usuario();
                elemento.setFecha_acceso(rs.getDate("fecha_acceso"));
                elemento.setId_estado(rs.getInt("contador"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int idPrimerAccesoUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MIN(id_estado) AS idmax,fecha_acceso,hora_acceso FROM estado_usuario WHERE id_usuario= '" + id_usuario + "'AND fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public int idUltimoAccesoUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_estado) AS idmax,fecha_acceso,hora_acceso FROM estado_usuario WHERE id_usuario= '" + id_usuario + "'AND fecha_acceso BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public int mayorRegistroActividadUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int horas = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(CONCAT(HOUR((TIMEDIFF(hora_inicio, hora_fin)))))AS tiempo FROM actividad WHERE id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getInt("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public int menorRegistroActividadUsuarioIDRango(int id_usuario, java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {

        int horas = 0;
        try {
            String sentencia;
            sentencia = "SELECT MIN(CONCAT(HOUR((TIMEDIFF(hora_inicio, hora_fin)))))AS tiempo FROM actividad WHERE id_usuario= '" + id_usuario + "'AND fecha_actividad BETWEEN '" + fecha_inicio + "'AND'" + fecha_fin + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                horas = rs.getInt("tiempo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return horas;
    }

    public estado_usuario buscarEstadoUsuarioID(int id_estado) {

        estado_usuario elemento = new estado_usuario();
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_estado) AS idmax,fecha_acceso,hora_acceso FROM estado_usuario WHERE id_estado= '" + id_estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_estado(rs.getInt("idmax"));
                elemento.setFecha_acceso(rs.getDate("fecha_acceso"));
                elemento.setHora_acceso(rs.getString("hora_acceso"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<String> listadoCodigoDireccionesTeletrabajo() {

        ArrayList<String> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT organizacion.nivel_padre FROM organizacion INNER JOIN usuario ON usuario.codigo_funcion=organizacion.nivel_padre WHERE usuario.tipo_funcion=1 GROUP BY organizacion.nivel_padre";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(rs.getString("organizacion.nivel_padre"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public double desviacionEstandarAccesosUsuario(ArrayList<estado_usuario> listado) {
        double sum = 0.0;
        double mean = 0.0;
        double num = 0.0;
        double numi = 0.0;
        double deno = 0.0;

        for (estado_usuario i : listado) {
            sum += i.getId_estado();
        }
        mean = sum / listado.size();

        for (estado_usuario i : listado) {
            numi = Math.pow(((double) i.getId_estado() - mean), 2);
            num += numi;
        }

        return Math.sqrt(num / listado.size());
    }

    public double promedioAccesosUsuario(ArrayList<estado_usuario> listado) {
        double sum = 0.0;
        double mean = 0.0;

        for (estado_usuario i : listado) {
            sum += i.getId_estado();
        }
        mean = sum / listado.size();

        return mean;
    }

    public double porcentajeSatisfaccionUsuarioSoporte(double promedio) {
        double resultado = 0;
        resultado = ((promedio * 100) / 7);
        return resultado;
    }

    public boolean exitesRegistroVacacionesPeriodoUsuario(int id_usuario, String periodo) {

        boolean confirmacion = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisovacaciones_usuario WHERE id_usuario='" + id_usuario + "'AND periodo='" + periodo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
            if (contador > 0) {
                confirmacion = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return confirmacion;
    }

    public int idMaximoPermisoVacacionesUsuarioID(int id_usuario, String periodo) {

        int dias = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_permiso) AS idmax FROM permisovacaciones_usuario WHERE id_usuario='" + id_usuario + "'AND periodo='" + periodo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                dias = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return dias;
    }

    public boolean eliminarPermisoVacacionID(int id) {

        try {
            st = enlace.prepareStatement("DELETE FROM permisovacaciones_usuario WHERE id_permiso= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean eliminarPermisoHorasID(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM permisohoras_usuario WHERE id_permiso= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean eliminarPermisoHorasECU(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM permiso_ecu WHERE id_permiso= '" + id + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean registroRechazoVacaciones(rechazo_vacaciones ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO rechazo_permisovacaciones(id_usuario,id_permiso,descripcion) VALUES(?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_permiso());
        st.setString(3, ph.getDescripcion());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroAnulacionVacaciones(AnulacionVacaciones a) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO anulacion_permisovacaciones(id_usuario,id_permiso,id_motivo) VALUES(?,?,?); UPDATE permisovacaciones_usuario SET reemplazado=0 WHERE id_permiso=?");
        st.setInt(1, a.getId_usuario());
        st.setInt(2, a.getId_permiso());
        st.setInt(3, a.getId_motivo());
        st.setInt(4, a.getId_permiso());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroRechazoHoras(rechazo_solicitud ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO rechazo_permisohoras(id_solicitud,id_rechaza,razon) VALUES(?,?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_rechaza());
        st.setString(3, ph.getRazon());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroRechazoHorasECU(rechazo_solicitud ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO rechazo_ecu(id_solicitud,id_rechaza,razon) VALUES(?,?,?);UPDATE permiso_ecu SET estado=2 WHERE id_permiso=?");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_rechaza());
        st.setString(3, ph.getRazon());
        st.setInt(4, ph.getId_solicitud());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroAnulacionHoras(AnulacionSolicitud a) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO anulacion_permisohoras(id_solicitud,id_anula,id_motivo) VALUES(?,?,?); " + (a.getId_motivo() == 1 ? "UPDATE permisohoras_usuario SET reemplazado=0 WHERE id_permiso=?" : ""));
        st.setInt(1, a.getId_solicitud());
        st.setInt(2, a.getId_anula());
        st.setInt(3, a.getId_motivo());
        if (a.getId_motivo() == 1) {
            st.setInt(4, a.getId_solicitud());
        }
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroAprobacionVacaciones(aprobacion_vacaciones ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO aprobacion_permisovacaciones(id_usuario,id_permiso) VALUES(?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_permiso());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroAprobacionSolicitud(AprobacionHoras ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO aprobacion_permisohoras(id_usuario,id_permiso) VALUES(?,?)");
        st.setInt(1, ph.getId_aprueba());
        st.setInt(2, ph.getId_solicitud());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroRevisionVacaciones(revision_vacaciones ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO revision_permisovacaciones(id_usuario,id_permiso) VALUES(?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_permiso());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean registroRevisionHoras(RevisionHoras r) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO revision_permisohoras(id_usuario,id_permiso) VALUES(?,?)");
        st.setInt(1, r.getId_usuario());
        st.setInt(2, r.getId_permiso());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarEstadoPermisoVacacionID(int id_estado, int id_solicitud) {
        try {
            st = enlace.prepareStatement("UPDATE permisovacaciones_usuario SET estado=? WHERE id_permiso=?");
            st.setInt(1, id_estado);
            st.setInt(2, id_solicitud);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println("actualizarEstadoPermisoVacacionID | " + ex);
        }
        return false;
    }

    public boolean actualizarEstadoPermisoHorasID(int id_estado, int id_solicitud) {
        try {
            st = enlace.prepareStatement("UPDATE permisohoras_usuario SET id_estado=? WHERE id_permiso= '" + id_solicitud + "'");
            st.setInt(1, id_estado);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean validarPermisoHorasID(int idPermiso) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET valido=1 WHERE id_permiso=?");
        st.setInt(1, idPermiso);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean validarPermisoHorasECU(int idPermiso, int idUsu, String path) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO revision_ecu(id_permiso, id_usuario) VALUES(?,?);UPDATE permiso_ecu SET estado=1, confirmacion=? WHERE id_permiso=?");
        st.setInt(1, idPermiso);
        st.setInt(2, idUsu);
        st.setString(3, path);
        st.setInt(4, idPermiso);
        st.executeUpdate();
        st.close();
        return true;
    }

    public int registroSolicitudSoporte(solicitud_soporte ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO solicitud_soporte(id_usuario,id_solicitante,id_sugerido,id_tipo,id_forma,incidente,referencia) VALUES(?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_solicitante());
        st.setInt(3, ph.getId_sugerido());
        st.setInt(4, ph.getId_tipo());
        st.setInt(5, ph.getId_forma());
        st.setString(6, ph.getIncidente());
        if (ph.getReferencia() != 0) {
            st.setInt(7, ph.getReferencia());
        } else {
            st.setNull(7, Types.INTEGER);
        }
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesSoporte(int id_usuario) {

        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud_soporte WHERE id_usuario= '" + id_usuario + "'ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean isAsignadoSolicitudSoporte(int id_solicitud) {

        boolean confirmacion = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM asignacion_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
            if (contador > 0) {
                confirmacion = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return confirmacion;
    }

    public boolean exiteSolicitudVacacionesPendienteUsuarioID(int id_usuario) {

        boolean confirmacion = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM permisovacaciones_usuario WHERE id_usuario= '" + id_usuario + "'AND estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
            if (contador > 0) {
                confirmacion = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return confirmacion;
    }

    public rechazo_vacaciones obtenerRechazoSolicitudVacaciones(int id_solicitud) {
        rechazo_vacaciones elemento = new rechazo_vacaciones();
        try {
            String sentencia;
            sentencia = "SELECT *FROM rechazo_permisovacaciones WHERE id_permiso= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_rechazo(rs.getInt("id_rechazo"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public AnulacionVacaciones obtenerAnulacionSolicitudVacaciones(int id_solicitud) {
        AnulacionVacaciones elemento = new AnulacionVacaciones();
        try {
            String sentencia;
            sentencia = "SELECT * FROM anulacion_permisovacaciones INNER JOIN motivo_anuvac ON anulacion_permisovacaciones.id_motivo=motivo_anuvac.id_motivo WHERE id_permiso=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_solicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_anulacion(rs.getInt("id_anulacion"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setId_motivo(rs.getInt("motivo_anuvac.id_motivo"));
                elemento.setMotivo(rs.getString("motivo"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerAnulacionSolicitudVacaciones | " + ex);
        }
        return elemento;
    }

    public rechazo_solicitud obtenerRechazoSolicitudHoras(int id_solicitud) {
        rechazo_solicitud elemento = new rechazo_solicitud();
        try {
            String sentencia;
            sentencia = "SELECT *FROM rechazo_permisohoras WHERE id_solicitud='" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_rechazo(rs.getInt("id_rechazo"));
                elemento.setId_solicitud(rs.getInt("id_solicitud"));
                elemento.setId_rechaza(rs.getInt("id_rechaza"));
                elemento.setRazon(rs.getString("razon"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public rechazo_solicitud obtenerRechazoSolicitudHorasECU(int id_solicitud) {
        rechazo_solicitud elemento = new rechazo_solicitud();
        try {
            String sentencia = "SELECT * FROM rechazo_ecu WHERE id_solicitud=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_solicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_rechazo(rs.getInt("id_rechazo"));
                elemento.setId_solicitud(rs.getInt("id_solicitud"));
                elemento.setId_rechaza(rs.getInt("id_rechaza"));
                elemento.setRazon(rs.getString("razon"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public AnulacionSolicitud obtenerAnulacionSolicitudHoras(int id_solicitud) {
        AnulacionSolicitud elemento = new AnulacionSolicitud();
        try {
            String sentencia;
            sentencia = "SELECT * FROM anulacion_permisohoras anula, motivo_anuvac moti WHERE anula.id_motivo=moti.id_motivo AND anula.id_solicitud=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_solicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_anulacion(rs.getInt("anula.id_anulacion"));
                elemento.setId_solicitud(rs.getInt("anula.id_solicitud"));
                elemento.setId_anula(rs.getInt("anula.id_anula"));
                elemento.setId_motivo(rs.getInt("anula.id_motivo"));
                elemento.setMotivo(rs.getString("moti.motivo"));
                elemento.setFecha_creacion(rs.getTimestamp("anula.fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerAnulacionSolicitudHoras | " + ex);
        }
        return elemento;
    }

    public usuario obtenerTecnicoAsignadoSolicitudSoporte(int id_solicitud) {

        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN asignacion_soporte ON usuario.id_usuario=asignacion_soporte.id_tecnico WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public usuario obtenerSolicitanteSoporte(int id_solicitud) {

        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario INNER JOIN solicitud_soporte ON usuario.id_usuario=solicitud_soporte.id_usuario WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public usuario getUsuarioSoporte(int id_solicitud) {
        usuario p = new usuario();
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario INNER JOIN solicitud_soporte ON usuario.id_usuario=solicitud_soporte.id_usuario WHERE id_solicitud=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_solicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setCodigo_usuario(rs.getString("codigo_usuario"));
                p.setNombre(rs.getString("nombre"));
                p.setCedula(rs.getString("cedula"));
                p.setCodigo_cargo(rs.getString("codigo_cargo"));
                p.setApellido(rs.getString("apellido"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
                p.setIniciales(rs.getString("iniciales"));
                p.setCodigo_unidad(rs.getString("codigo_unidad"));
                p.setFecha_creacion(rs.getDate("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerUsuarioSoporte | " + ex);
        }

        return p;
    }

    public ArrayList<solicitud_soporte> listadoMisSolicitudesSoporte(int id_usuario) {

        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud_soporte WHERE id_solicitante= '" + id_usuario + "'ORDER BY id_solicitud DESC ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesRecienteMes() {

        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte WHERE (YEAR(fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(fecha_creacion)= MONTH(CURRENT_DATE())) OR id_estado<>3 ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesUsuarioRecienteMes(int idTecnico) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE ((YEAR(fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(fecha_creacion)= MONTH(CURRENT_DATE())) OR id_estado<>3) AND asignacion_soporte.id_tecnico=? ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idTecnico);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("solicitud_soporte.id_solicitud"));
                p.setId_usuario(rs.getInt("solicitud_soporte.id_usuario"));
                p.setId_solicitante(rs.getInt("solicitud_soporte.id_solicitante"));
                p.setId_sugerido(rs.getInt("solicitud_soporte.id_sugerido"));
                p.setId_tipo(rs.getInt("solicitud_soporte.id_tipo"));
                p.setId_forma(rs.getInt("solicitud_soporte.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("solicitud_soporte.fecha_creacion"));
                p.setIncidente(rs.getString("solicitud_soporte.incidente"));
                p.setEstado(rs.getInt("solicitud_soporte.id_estado"));
                p.setAdjunto(rs.getString("solicitud_soporte.adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesUsuarioRecienteMes | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesRecientesUnidad(int idUnidad) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud INNER JOIN usuario ON asignacion_soporte.id_tecnico=usuario.id_usuario INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo WHERE ((YEAR(solicitud_soporte.fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(solicitud_soporte.fecha_creacion)= MONTH(CURRENT_DATE())) OR solicitud_soporte.id_estado<>3) AND organizacion.id_organizacion=? ORDER BY solicitud_soporte.id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUnidad);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("solicitud_soporte.id_solicitud"));
                p.setId_usuario(rs.getInt("solicitud_soporte.id_usuario"));
                p.setId_solicitante(rs.getInt("solicitud_soporte.id_solicitante"));
                p.setId_sugerido(rs.getInt("solicitud_soporte.id_sugerido"));
                p.setId_tipo(rs.getInt("solicitud_soporte.id_tipo"));
                p.setId_forma(rs.getInt("solicitud_soporte.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("solicitud_soporte.fecha_creacion"));
                p.setIncidente(rs.getString("solicitud_soporte.incidente"));
                p.setEstado(rs.getInt("solicitud_soporte.id_estado"));
                p.setAdjunto(rs.getString("solicitud_soporte.adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesRecientesUnidad | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesRecienteMes(int estado) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte WHERE YEAR(fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(fecha_creacion)= MONTH(CURRENT_DATE()) AND id_estado=? ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesRecienteMes | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesRecienteMesUnidad(int estado, int idUnidad) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud INNER JOIN usuario ON asignacion_soporte.id_tecnico=usuario.id_usuario INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo WHERE YEAR(solicitud_soporte.fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(solicitud_soporte.fecha_creacion)= MONTH(CURRENT_DATE()) AND solicitud_soporte.id_estado=? AND organizacion.id_organizacion=? ORDER BY solicitud_soporte.id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            st.setInt(2, idUnidad);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesRecienteMesUnidad | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesUsuarioEstadoRecienteMes(int idTecnico, int idEstado) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE (YEAR(fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(fecha_creacion)= MONTH(CURRENT_DATE())) AND asignacion_soporte.id_tecnico=? AND id_estado=? ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idTecnico);
            st.setInt(2, idEstado);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("solicitud_soporte.id_solicitud"));
                p.setId_usuario(rs.getInt("solicitud_soporte.id_usuario"));
                p.setId_solicitante(rs.getInt("solicitud_soporte.id_solicitante"));
                p.setId_sugerido(rs.getInt("solicitud_soporte.id_sugerido"));
                p.setId_tipo(rs.getInt("solicitud_soporte.id_tipo"));
                p.setId_forma(rs.getInt("solicitud_soporte.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("solicitud_soporte.fecha_creacion"));
                p.setIncidente(rs.getString("solicitud_soporte.incidente"));
                p.setEstado(rs.getInt("solicitud_soporte.id_estado"));
                p.setAdjunto(rs.getString("solicitud_soporte.adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesUsuarioEstadoRecienteMes | " + ex);
        }
        return listado;
    }

    public solicitud_soporte obtenerSolicitudSoporte(int id_solicitud) {

        solicitud_soporte p = new solicitud_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public rechazo_soporte getRechazoSoporte(int id_solicitud) {
        rechazo_soporte r = new rechazo_soporte();
        try {
            String sentencia;
            sentencia = "SELECT * FROM rechazo_soporte WHERE id_solicitud=? ORDER BY id_rechazo DESC LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_solicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                r = new rechazo_soporte(rs.getInt("id_rechazo"),
                        rs.getInt("id_solicitud"),
                        rs.getInt("id_tecnico"),
                        rs.getString("motivo"),
                        rs.getTimestamp("fecha_rechazo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRechazoSoporte | " + ex);
        }
        return r;
    }

    public AnulacionSoporte getAnulacionSoporte(int idSol) {
        AnulacionSoporte a = new AnulacionSoporte();
        try {
            String sentencia;
            sentencia = "SELECT * FROM anulacion_soporte WHERE id_solicitud=? ORDER BY id_anulacion DESC LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idSol);
            rs = st.executeQuery();
            while (rs.next()) {
                a = new AnulacionSoporte(rs.getInt("id_anulacion"),
                        rs.getInt("id_solicitud"),
                        rs.getInt("id_tecnico"),
                        rs.getString("motivo"),
                        rs.getTimestamp("fecha"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAnulacionSoporte | " + ex);
        }
        return a;
    }

    public DevolucionSoporte getDevolucionSoporteFuncionario(int idSol) {
        DevolucionSoporte a = new DevolucionSoporte();
        try {
            String sentencia;
            sentencia = "SELECT * FROM soporte_devolucion_funcionario WHERE ID_SOL=? ORDER BY ID_DEV DESC LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idSol);
            rs = st.executeQuery();
            while (rs.next()) {
                a = new DevolucionSoporte(rs.getInt("ID_DEV"),
                        rs.getInt("ID_SOL"),
                        rs.getString("MOTIVO_DEV"),
                        rs.getTimestamp("FECHA_DEV"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDevolucionSoporteFuncionario | " + ex);
        }
        return a;
    }

    public ArrayList<forma_soporte> listadoFormasSoporte() {

        ArrayList<forma_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM forma_soporte WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                forma_soporte p = new forma_soporte();
                p.setId_forma(rs.getInt("id_forma"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<tipo_soporte> listadoTiposSoporte() {

        ArrayList<tipo_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_soporte WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_soporte p = new tipo_soporte();
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<TipoSolucion> getTiposSolucion() {
        ArrayList<TipoSolucion> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM tipo_solucion WHERE estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new TipoSolucion(rs.getInt("id_tipo"),
                        rs.getString("descripcion"),
                        rs.getBoolean("estado"),
                        rs.getTimestamp("fecha_creacion")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTiposSolucion | " + ex);
        }
        return listado;
    }

    public tipo_soporte ObtenerTipoSoporteID(int id_tipo) {

        tipo_soporte p = new tipo_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_soporte WHERE id_tipo= '" + id_tipo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public forma_soporte ObtenerFormaSoporteID(int id_forma) {

        forma_soporte p = new forma_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM forma_soporte WHERE id_forma= '" + id_forma + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_forma(rs.getInt("id_forma"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public String obtenerEstadoSolicitudSoporteID(int id_solicitud) {

        String estado = null;
        try {
            String sentencia;
            sentencia = "SELECT *FROM estado_soporte INNER JOIN solicitud_soporte ON estado_soporte.id_estado=solicitud_soporte.id_estado WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estado = rs.getString("descripcion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return estado;
    }

    public boolean eliminarSolicitudSoporteID(int id_soporte) {
        try {
            st = enlace.prepareStatement("DELETE FROM solicitud_soporte WHERE id_solicitud= '" + id_soporte + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public boolean eliminarTramiteID(int id_tramite) {
        try {
            st = enlace.prepareStatement("DELETE FROM tramite WHERE id_tramite=?");
            st.setInt(1, id_tramite);
            boolean res = st.executeUpdate() > 0;
            st.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("eliminarTramiteID | " + ex);
        }
        return false;
    }

    public boolean cambiarEstadoTramite(int idTramite, int estado) {
        try {
            st = enlace.prepareStatement("UPDATE tramite SET id_estado=? WHERE id_tramite=?");
            st.setInt(1, estado);
            st.setInt(2, idTramite);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("cambiarEstadoTramite | " + ex);
        }
        return false;
    }

    public boolean cambiarAdjuntoTramite(int idTramite, int tipo, String numero, String asunto, String path) {
        try {
            st = enlace.prepareStatement("UPDATE tramite SET adjunto=?, tipo_tramite=?, numero_memo=?, asunto=?, devuelto=NULL WHERE id_tramite=?");
            st.setString(1, path);
            st.setInt(2, tipo);
            st.setString(3, numero);
            st.setString(4, asunto);
            st.setInt(5, idTramite);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("cambiarAdjuntoTramite | " + ex);
        }
        return false;
    }

    public boolean devolverTramite(int idTramite, String motivo) {
        try {
            st = enlace.prepareStatement("UPDATE tramite SET devuelto=? WHERE id_tramite=?");
            st.setString(1, motivo);
            st.setInt(2, idTramite);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("devolverTramite | " + ex);
        }
        return false;
    }

    public boolean actualizarSolicitudSoporteID(solicitud_soporte elemento, int id_solicitud) {

        try {
            st = enlace.prepareStatement("UPDATE solicitud_soporte SET id_estado=0,id_solicitante=?,id_sugerido=?,id_tipo=?,id_forma=?,fecha_creacion=NOW(),incidente=? WHERE id_solicitud= '" + id_solicitud + "'");
            st.setInt(1, elemento.getId_solicitante());
            st.setInt(2, elemento.getId_sugerido());
            st.setInt(3, elemento.getId_tipo());
            st.setInt(4, elemento.getId_forma());
            st.setString(5, elemento.getIncidente());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean actualizarEstadoSolicitudID(int id_estado, int id_solicitud) {

        try {
            st = enlace.prepareStatement("UPDATE solicitud_soporte SET id_estado=? WHERE id_solicitud= '" + id_solicitud + "'");
            st.setInt(1, id_estado);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public int obtenerCantidadSolicitudesEstadoID(int id_estado) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM solicitud_soporte WHERE id_estado= '" + id_estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int obtenerCantidadSolicitudesAtendidasTecnicoID(int id_tecnico) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM atencion_soporte WHERE id_tecnico = '" + id_tecnico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int getTotalSolicitudesAtendidasTecnico(int idTec) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM solicitud_soporte INNER JOIN atencion_soporte ON solicitud_soporte.id_solicitud=atencion_soporte.id_solicitud WHERE id_tecnico=? AND id_estado=3";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idTec);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return res;
    }

    public int obtenerCantidadSolicitudesRechazadasTecnicoID(int id_tecnico) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM rechazo_soporte WHERE id_tecnico = '" + id_tecnico + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int obtenerCantidadSolicitudesAsignadasUsuarioID(int id_usuario) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM asignacion_soporte WHERE id_tecnico= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int getTotalSolicitudesAsignadasTecnico(int idTec) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE id_tecnico=? AND id_estado=1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idTec);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTotalSolicitudesAsignadasTecnico | " + ex);
        }
        return res;
    }

    public int getTotalSolicitudesAtendiendoTecnico(int idTec) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE id_tecnico=? AND id_estado=2";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idTec);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTotalSolicitudesAtendiendoTecnico | " + ex);
        }
        return res;
    }

    public boolean registroAsignacionSoporteTecnicoID(asignacion_soporte ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO asignacion_soporte(id_solicitud,id_administrador,id_tecnico) VALUES(?,?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_administrador());
        st.setInt(3, ph.getId_tecnico());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesAsignadasUsuarioIDRecienteMes(int id_tecnico) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia = "SELECT *, (SELECT incidente FROM solicitud_soporte WHERE id_solicitud=sol_sop.referencia LIMIT 1) desc_ref,(SELECT observacion FROM diagnostico_soporte WHERE id_solicitud=sol_sop.referencia ORDER BY id_diagnostico DESC LIMIT 1) solucion_ref, (SELECT adjunto FROM diagnostico_soporte WHERE id_solicitud=sol_sop.referencia ORDER BY id_diagnostico DESC LIMIT 1) adjunto_ref FROM solicitud_soporte sol_sop INNER JOIN asignacion_soporte asi_sop ON sol_sop.id_solicitud=asi_sop.id_solicitud WHERE asi_sop.id_tecnico = ? AND ((YEAR(sol_sop.fecha_creacion) = YEAR(CURRENT_DATE()) AND MONTH(sol_sop.fecha_creacion)= MONTH(CURRENT_DATE())) OR id_estado<>3) ORDER BY sol_sop.id_estado ASC, sol_sop.id_solicitud ASC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tecnico);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("sol_sop.id_solicitud"));
                p.setId_usuario(rs.getInt("sol_sop.id_usuario"));
                p.setId_solicitante(rs.getInt("sol_sop.id_solicitante"));
                p.setId_sugerido(rs.getInt("sol_sop.id_sugerido"));
                p.setId_tipo(rs.getInt("sol_sop.id_tipo"));
                p.setId_forma(rs.getInt("sol_sop.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("sol_sop.fecha_creacion"));
                p.setIncidente(rs.getString("sol_sop.incidente"));
                p.setEstado(rs.getInt("sol_sop.id_estado"));
                p.setAdjunto(rs.getString("sol_sop.adjunto"));
                p.setReferencia(rs.getInt("sol_sop.referencia"));
                p.setDescripcionRef(rs.getString("desc_ref"));
                p.setSolucionRef(rs.getString("solucion_ref"));
                p.setAdjuntoRef(rs.getString("adjunto_ref"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public int totalSolicitudesEstadoUsuarioFecha(int id_tecnico, int estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS RES FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE asignacion_soporte.id_tecnico = ? AND solicitud_soporte.id_estado=? AND CAST(solicitud_soporte.fecha_creacion AS DATE) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tecnico);
            st.setInt(2, estado);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("totalSolicitudesEstadoUsuarioFecha | " + ex);
        }
        return res;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesFecha(java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte WHERE CAST(fecha_creacion AS DATE) BETWEEN ? AND ? ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesFecha | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesFechaUnidad(java.sql.Date fechaIni, java.sql.Date fechaFin, int idUnidad) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud INNER JOIN usuario ON asignacion_soporte.id_tecnico=usuario.id_usuario INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo WHERE CAST(solicitud_soporte.fecha_creacion AS DATE) BETWEEN ? AND ? AND organizacion.id_organizacion=? ORDER BY solicitud_soporte.id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            st.setInt(3, idUnidad);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesFecha | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesUsuarioFecha(int id_tecnico, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE asignacion_soporte.id_tecnico = ? AND CAST(solicitud_soporte.fecha_creacion AS DATE) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tecnico);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("solicitud_soporte.id_solicitud"));
                p.setId_usuario(rs.getInt("solicitud_soporte.id_usuario"));
                p.setId_solicitante(rs.getInt("solicitud_soporte.id_solicitante"));
                p.setId_sugerido(rs.getInt("solicitud_soporte.id_sugerido"));
                p.setId_tipo(rs.getInt("solicitud_soporte.id_tipo"));
                p.setId_forma(rs.getInt("solicitud_soporte.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("solicitud_soporte.fecha_creacion"));
                p.setIncidente(rs.getString("solicitud_soporte.incidente"));
                p.setEstado(rs.getInt("solicitud_soporte.id_estado"));
                p.setAdjunto(rs.getString("solicitud_soporte.adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesEstadoUsuarioFecha | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesFechaEstado(java.sql.Date fechaIni, java.sql.Date fechaFin, int idEstado) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte WHERE CAST(fecha_creacion AS DATE) BETWEEN ? AND ? AND solicitud_soporte.id_estado=? ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            st.setInt(3, idEstado);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesFechaEstado | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesFechaEstadoUnidad(java.sql.Date fechaIni, java.sql.Date fechaFin, int idEstado, int idUnidad) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud INNER JOIN usuario ON asignacion_soporte.id_tecnico=usuario.id_usuario INNER JOIN organizacion ON usuario.codigo_unidad=organizacion.nivel_hijo WHERE CAST(solicitud_soporte.fecha_creacion AS DATE) BETWEEN ? AND ? AND solicitud_soporte.id_estado=? AND organizacion.id_organizacion=? ORDER BY solicitud_soporte.id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            st.setInt(3, idEstado);
            st.setInt(4, idUnidad);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setId_sugerido(rs.getInt("id_sugerido"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setId_forma(rs.getInt("id_forma"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setIncidente(rs.getString("incidente"));
                p.setEstado(rs.getInt("id_estado"));
                p.setAdjunto(rs.getString("adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesFechaEstadoUnidad | " + ex);
        }
        return listado;
    }

    public ArrayList<solicitud_soporte> listadoSolicitudesUsuarioFechaEstado(int id_tecnico, java.sql.Date fechaIni, java.sql.Date fechaFin, int idEstado) {
        ArrayList<solicitud_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM solicitud_soporte INNER JOIN asignacion_soporte ON solicitud_soporte.id_solicitud=asignacion_soporte.id_solicitud WHERE asignacion_soporte.id_tecnico = ? AND CAST(solicitud_soporte.fecha_creacion AS DATE) BETWEEN ? AND ? AND solicitud_soporte.id_estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tecnico);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            st.setInt(4, idEstado);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_soporte p = new solicitud_soporte();
                p.setId_solicitud(rs.getInt("solicitud_soporte.id_solicitud"));
                p.setId_usuario(rs.getInt("solicitud_soporte.id_usuario"));
                p.setId_solicitante(rs.getInt("solicitud_soporte.id_solicitante"));
                p.setId_sugerido(rs.getInt("solicitud_soporte.id_sugerido"));
                p.setId_tipo(rs.getInt("solicitud_soporte.id_tipo"));
                p.setId_forma(rs.getInt("solicitud_soporte.id_forma"));
                p.setFecha_creacion(rs.getTimestamp("solicitud_soporte.fecha_creacion"));
                p.setIncidente(rs.getString("solicitud_soporte.incidente"));
                p.setEstado(rs.getInt("solicitud_soporte.id_estado"));
                p.setAdjunto(rs.getString("solicitud_soporte.adjunto"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSolicitudesUsuarioFechaEstado | " + ex);
        }
        return listado;
    }

    public asignacion_soporte obtenerAsignacionSoporteID(int id_solicitud) {

        asignacion_soporte p = new asignacion_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM asignacion_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_asignacion(rs.getInt("id_asignacion"));
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_tecnico(rs.getInt("id_tecnico"));
                p.setId_administrador(rs.getInt("id_administrador"));
                p.setFecha_asignacion(rs.getTimestamp("fecha_asignacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroAtencionSoporteTecnico(atencion_soporte ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO atencion_soporte(id_solicitud,id_tecnico) VALUES(?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_tecnico());
        st.executeUpdate();
        st.close();

        return true;
    }

    public atencion_soporte obtenerAtencionSoporteID(int id_solicitud) {

        atencion_soporte p = new atencion_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM atencion_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_atencion(rs.getInt("id_atencion"));
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_tecnico(rs.getInt("id_tecnico"));
                p.setFecha_atencion(rs.getTimestamp("fecha_atencion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroAtendidoSoporteTecnico(atendido_soporte ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO atendido_soporte(id_solicitud,id_tecnico) VALUES(?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_tecnico());
        st.executeUpdate();
        st.close();

        return true;
    }

    public atendido_soporte obtenerAtendidoSoporteID(int id_solicitud) {

        atendido_soporte p = new atendido_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM atendido_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_atendido(rs.getInt("id_atendido"));
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_tecnico(rs.getInt("id_tecnico"));
                p.setFecha_atendido(rs.getTimestamp("fecha_atendido"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroDiagnosticoSoporteTecnico(diagnostico_soporte diag) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO diagnostico_soporte(id_solicitud,id_tecnico,observacion,adjunto,tipo_solucion) VALUES(?,?,?,?,?)");
        st.setInt(1, diag.getId_solicitud());
        st.setInt(2, diag.getId_tecnico());
        st.setString(3, diag.getObservacion());
        if (diag.getAdjunto().equals("")) {
            st.setNull(4, Types.VARCHAR);
        } else {
            st.setString(4, diag.getAdjunto());
        }
        st.setInt(5, diag.getTipoSol());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean tieneDiagnosticoSolicitudSoporte(int id_solicitud) {

        boolean confirmacion = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM diagnostico_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
            if (contador > 0) {
                confirmacion = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return confirmacion;
    }

    public diagnostico_soporte obtenerDiagnosticoSoporteID(int id_solicitud) {

        diagnostico_soporte p = new diagnostico_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM diagnostico_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_diagnostico(rs.getInt("id_diagnostico"));
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_tecnico(rs.getInt("id_tecnico"));
                p.setObservacion(rs.getString("observacion"));
                p.setAdjunto(rs.getString("adjunto"));
                p.setFecha_diagnostico(rs.getTimestamp("fecha_diagnostico"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroCalificacionSoporteTecnico(calificacion_soporte ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO calificacion_soporte(id_solicitud,id_usuario,id_satisfaccion,observacion) VALUES(?,?,?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_usuario());
        st.setInt(3, ph.getId_satisfaccion());
        st.setString(4, ph.getObservacion());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean tieneCalificacionSolicitudSoporte(int id_solicitud) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM calificacion_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
            if (contador > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("tieneCalificacionSolicitudSoporte | " + ex);
        }
        return false;
    }

    public ArrayList<satisfaccion_soporte> listadoSatisfaccionSoporte() {

        ArrayList<satisfaccion_soporte> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM satisfaccion_soporte";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                satisfaccion_soporte p = new satisfaccion_soporte();
                p.setId_satisfaccion(rs.getInt("id_satisfaccion"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public satisfaccion_soporte obtenerSatisfaccionSolicitudSoporteID(int id_solicitud) {

        satisfaccion_soporte p = new satisfaccion_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM satisfaccion_soporte INNER JOIN calificacion_soporte ON calificacion_soporte.id_satisfaccion=satisfaccion_soporte.id_satisfaccion WHERE calificacion_soporte.id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_satisfaccion(rs.getInt("satisfaccion_soporte.id_satisfaccion"));
                p.setDescripcion(rs.getString("satisfaccion_soporte.descripcion"));
                p.setFecha_creacion(rs.getTimestamp("satisfaccion_soporte.fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public calificacion_soporte obtenerCalificacionSolicitudSoporteID(int id_solicitud) {

        calificacion_soporte p = new calificacion_soporte();
        try {
            String sentencia;
            sentencia = "SELECT *FROM calificacion_soporte WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_calificacion(rs.getInt("id_calificacion"));
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_satisfaccion(rs.getInt("id_satisfaccion"));
                p.setObservacion(rs.getString("observacion"));
                p.setFecha_calificacion(rs.getTimestamp("fecha_calificacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean eliminarAsignacionSoporteID(int id_solicitud) {

        try {
            st = enlace.prepareStatement("DELETE FROM asignacion_soporte WHERE id_solicitud= '" + id_solicitud + "'");
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean registroRechazoSoporteTecnico(rechazo_soporte ph) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO rechazo_soporte(id_solicitud,id_tecnico,motivo) VALUES(?,?,?)");
        st.setInt(1, ph.getId_solicitud());
        st.setInt(2, ph.getId_tecnico());
        st.setString(3, ph.getMotivo());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroAnulacionSoporte(AnulacionSoporte a) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO anulacion_soporte(id_solicitud,id_tecnico,motivo) VALUES(?,?,?)");
        st.setInt(1, a.getIdSol());
        st.setInt(2, a.getIdTec());
        st.setString(3, a.getMotivo());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registrarDevolucionFuncionarioSoporte(DevolucionSoporte a) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO soporte_devolucion_funcionario(ID_SOL,MOTIVO_DEV) VALUES(?,?)");
        st.setInt(1, a.getIdSoporte());
        st.setString(2, a.getMotivo());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroPlanOperativo(plan_operativo ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO plan_operativo(anio,id_usuario,presupuesto) VALUES(?,?,?)");
        st.setString(1, ph.getAnio());
        st.setInt(2, ph.getId_usuario());
        st.setDouble(3, ph.getPresupuesto());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<plan_operativo> listadoPlanesOperativos(int id_usuario) {

        ArrayList<plan_operativo> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM plan_operativo WHERE id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                plan_operativo p = new plan_operativo();
                p.setId_plan(rs.getInt("id_plan"));
                p.setAnio(rs.getString("anio"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getInt("estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public plan_operativo obtenerPlanOperativoID(int id_plan) {

        plan_operativo p = new plan_operativo();
        try {
            String sentencia;
            sentencia = "SELECT *FROM plan_operativo WHERE id_plan= '" + id_plan + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_plan(rs.getInt("id_plan"));
                p.setAnio(rs.getString("anio"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getInt("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public int idUltimoPlanUsuarioID(int id_usuario) {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_plan) AS idmax FROM plan_operativo WHERE id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public boolean registroActividadPlanOperativo(actividad_plan ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO actividad_plan(id_plan,actividad_descripcion,mes,id_tipo,indicador,meta,partida_presupuestaria,descripcion,id_orientador,programacion_trimestral,presupuesto,presupuesto_actual) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_plan());
        st.setString(2, ph.getActividad_descripcion());
        st.setString(3, ph.getMes());
        st.setInt(4, ph.getId_tipo());
        st.setString(5, ph.getIndicador());
        st.setString(6, ph.getMeta());
        st.setString(7, ph.getPartida_presupuestaria());
        st.setString(8, ph.getDescripcion());
        st.setInt(9, ph.getId_orientador());
        st.setString(10, ph.getProgramacion_trimestral());
        st.setDouble(11, ph.getPresupuesto());
        st.setDouble(12, ph.getPresupuesto_actual());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<actividad_plan> listadoActividadesPlanOperativo(int id_plan) {

        ArrayList<actividad_plan> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad_plan WHERE id_plan= '" + id_plan + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                actividad_plan p = new actividad_plan();
                p.setId_actividad(rs.getInt("id_actividad"));
                p.setId_plan(rs.getInt("id_plan"));
                p.setActividad_descripcion(rs.getString("actividad_descripcion"));
                p.setMes(rs.getString("mes"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setIndicador(rs.getString("indicador"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setMeta(rs.getString("meta"));
                p.setPartida_presupuestaria(rs.getString("partida_presupuestaria"));
                p.setId_orientador(rs.getInt("id_orientador"));
                p.setProgramacion_trimestral(rs.getString("programacion_trimestral"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setPresupuesto_actual(rs.getDouble("presupuesto_actual"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean exiteActividadPlanesOperativos(int id_plan) {

        boolean confirmar = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad_plan WHERE id_plan= '" + id_plan + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmar = true;
        }

        return confirmar;
    }

    public actividad_plan ObtenerActividadePlanOperativoID(int id_actividad) {

        actividad_plan p = new actividad_plan();
        try {
            String sentencia;
            sentencia = "SELECT *FROM actividad_plan WHERE id_actividad= '" + id_actividad + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_actividad(rs.getInt("id_actividad"));
                p.setId_plan(rs.getInt("id_plan"));
                p.setActividad_descripcion(rs.getString("actividad_descripcion"));
                p.setMes(rs.getString("mes"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setIndicador(rs.getString("indicador"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setMeta(rs.getString("meta"));
                p.setPartida_presupuestaria(rs.getString("partida_presupuestaria"));
                p.setId_orientador(rs.getInt("id_orientador"));
                p.setProgramacion_trimestral(rs.getString("programacion_trimestral"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setPresupuesto_actual(rs.getDouble("presupuesto_actual"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public ArrayList<tipo_actividad> listadoTipoActividades() {

        ArrayList<tipo_actividad> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_actividad";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_actividad p = new tipo_actividad();
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getBoolean("estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public tipo_actividad obtenerTipoActividadID(int id_tipo) {

        tipo_actividad p = new tipo_actividad();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_actividad WHERE id_tipo= '" + id_tipo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getBoolean("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public ArrayList<orientador_gasto> listadoOrientadorActividades() {

        ArrayList<orientador_gasto> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM orientador_gasto";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                orientador_gasto p = new orientador_gasto();
                p.setId_orientador(rs.getInt("id_orientador"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getBoolean("estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public orientador_gasto obtenerOrientadorActividadID(int id_tipo) {

        orientador_gasto p = new orientador_gasto();
        try {
            String sentencia;
            sentencia = "SELECT *FROM orientador_gasto WHERE id_orientador= '" + id_tipo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_orientador(rs.getInt("id_orientador"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getBoolean("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public int idUltimaActividadPlanID(int id_plan) {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_actividad) AS idmax FROM actividad_plan WHERE id_plan= '" + id_plan + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public ArrayList<asignacion_presupuesto> listadoAsignacionesSinPresupuesto() {

        ArrayList<asignacion_presupuesto> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM asignacion_presupuesto";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                asignacion_presupuesto p = new asignacion_presupuesto();
                p.setId_asignacion(rs.getInt("id_asignacion"));
                p.setId_organizacion(rs.getInt("id_organizacion"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_asignacion(rs.getTimestamp("fecha_asignacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                p.setAnio(rs.getInt("anio"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<resumen_actividades> listadoResumenActividades() {

        ArrayList<resumen_actividades> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT\n"
                    + "(SELECT MIN(actividad.fecha_actividad) FROM actividad WHERE actividad.id_usuario=usuario.id_usuario LIMIT 1) AS fecha_inicio,\n"
                    + "(SELECT MAX(actividad.fecha_actividad) FROM actividad WHERE actividad.id_usuario=usuario.id_usuario AND YEAR(actividad.fecha_actividad) = 2020 LIMIT 1) AS fecha_fin,\n"
                    + "CONCAT_WS(' ',usuario.`apellido`,usuario.`nombre`) AS nombre_funcionario,\n"
                    + "(SELECT organizacion.nombre FROM organizacion WHERE organizacion.nivel_hijo=usuario.codigo_funcion LIMIT 1) AS direccion,\n"
                    + "usuario.cedula AS cedula_funcionario,\n"
                    + "(SELECT  DISTINCT\n"
                    + "       CONCAT( (SELECT SUM(HOUR((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo\n"
                    + "FROM actividad\n"
                    + "WHERE actividad.id_usuario = usuario.id_usuario AND YEAR(actividad.fecha_actividad) = 2020) + \n"
                    + "        CASE WHEN (SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo\n"
                    + "FROM actividad\n"
                    + "WHERE actividad.id_usuario = usuario.id_usuario AND YEAR(actividad.fecha_actividad) = 2020) > 59 THEN (SELECT  TRUNCATE((SELECT SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin)))) AS actividad_tiempo\n"
                    + "FROM actividad\n"
                    + "WHERE actividad.id_usuario = usuario.id_usuario)/60 , 0)) ELSE 0 END , 'h y ', SUM(MINUTE((TIMEDIFF(actividad.hora_inicio, actividad.hora_fin))))% 60, 'm' )\n"
                    + "FROM actividad WHERE actividad.id_usuario = usuario.id_usuario AND YEAR(actividad.fecha_actividad) = 2020) AS total\n"
                    + "FROM actividad INNER JOIN usuario ON usuario.id_usuario=actividad.id_usuario \n"
                    + "GROUP BY usuario.id_usuario\n"
                    + "ORDER BY direccion DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_actividades p = new resumen_actividades();
                p.setFecha_inicio(rs.getDate("fecha_inicio"));
                p.setFecha_fin(rs.getDate("fecha_fin"));
                p.setNombre_funcionario(rs.getString("nombre_funcionario"));
                p.setDireccion(rs.getString("direccion"));
                p.setCedula(rs.getString("cedula_funcionario"));
                p.setTotal_horas(rs.getString("total"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<medio_acta> listadoMediosActa() {

        ArrayList<medio_acta> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM medio_acta";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                medio_acta elemento = new medio_acta();
                elemento.setId_medio(rs.getInt("id_medio"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean registrarAsistentesActaArray(int id_acta, String[] listado) {
        boolean confirmacion = false;
        for (int paso = 0; paso < listado.length; paso++) {
            int id_asistente = Integer.parseInt(listado[paso]);
            asistencia_acta elemento = new asistencia_acta(id_acta, id_asistente);
            try {
                registroAsistenciaActa(elemento);
                confirmacion = true;
            } catch (SQLException ex) {
                confirmacion = false;
            }
        }
        return confirmacion;
    }

    public boolean actualizarAsistentesActaArray(int id_acta, String[] listado) {
        boolean confirmacion = false;
        for (int paso = 0; paso < listado.length; paso++) {
            int id_asistente = Integer.parseInt(listado[paso]);
            asistencia_acta elemento = new asistencia_acta(id_acta, id_asistente);
            try {
                actualizarAsistenciaActa(id_acta, elemento);
                confirmacion = true;
            } catch (SQLException ex) {
                System.out.println(ex);
                confirmacion = false;
            }
        }
        return confirmacion;
    }

    public boolean registroActa(acta ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO acta(id_convocatoria,fecha_acta,fecha_convocatoria,asunto,lugar,id_medio,hora_inicio,hora_fin,orden_dia,desarrollo,estado) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        st.setInt(1, ph.getId_convocatoria());
        st.setDate(2, ph.getFecha_acta());
        st.setDate(3, ph.getFecha_convocatoria());
        st.setString(4, ph.getAsunto());
        st.setString(5, ph.getLugar());
        st.setInt(6, ph.getId_medio());
        st.setString(7, ph.getHora_inicio());
        st.setString(8, ph.getHora_fin());
        st.setString(9, ph.getOrden_dia());
        st.setString(10, ph.getDesarrollo());
        st.setBoolean(11, ph.isEstado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarActa(int id_acta, acta elemento) {

        try {
            st = enlace.prepareStatement("UPDATE acta SET fecha_acta=?,fecha_convocatoria=?,asunto=?,lugar=?,id_medio=?,hora_inicio=?,hora_fin=?,orden_dia=?,desarrollo=? WHERE id_acta= '" + id_acta + "'");
            st.setDate(1, elemento.getFecha_acta());
            st.setDate(2, elemento.getFecha_convocatoria());
            st.setString(3, elemento.getAsunto());
            st.setString(4, elemento.getLugar());
            st.setInt(5, elemento.getId_medio());
            st.setString(6, elemento.getHora_inicio());
            st.setString(7, elemento.getHora_fin());
            st.setString(8, elemento.getOrden_dia());
            st.setString(9, elemento.getDesarrollo());
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return false;
    }

    public boolean registroAsistenciaActa(asistencia_acta ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO asistencia_acta(id_acta,id_usuario) VALUES(?,?)");
        st.setInt(1, ph.getId_acta());
        st.setInt(2, ph.getId_usuario());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarAsistenciaActa(int id_acta, asistencia_acta ph) throws SQLException {

        st = enlace.prepareStatement("UPDATE asistencia_acta SET id_usuario=? WHERE id_acta= '" + id_acta + "'");
        st.setInt(1, ph.getId_usuario());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarConvocatoriaID(int id_convocatoria, convocatoria elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE convocatoria SET fecha_convocatoria=?,asunto=?,lugar=?,hora_inicio=?,hora_fin=?,orden_dia=?,id_medio=? WHERE id_convocatoria= '" + id_convocatoria + "'");
        st.setDate(1, elemento.getFecha_convocatoria());
        st.setString(2, elemento.getAsunto());
        st.setString(3, elemento.getLugar());
        st.setString(4, elemento.getHora_inicio());
        st.setString(5, elemento.getHora_fin());
        st.setString(6, elemento.getOrden_dia());
        st.setInt(7, elemento.getId_medio());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarEstadoConvocatoria(int id_convocatoria) throws SQLException {

        st = enlace.prepareStatement("UPDATE convocatoria SET estado=? WHERE id_convocatoria= '" + id_convocatoria + "'");
        st.setBoolean(1, true);
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<acta> listadoActas(int id_usuario) {

        ArrayList<acta> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM acta INNER JOIN convocatoria ON convocatoria.id_convocatoria = acta.id_convocatoria WHERE convocatoria.id_convocador='" + id_usuario + "' ORDER BY fecha_acta DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                acta p = new acta();
                p.setId_acta(rs.getInt("acta.id_acta"));
                p.setId_convocatoria(rs.getInt("acta.id_convocatoria"));
                p.setFecha_acta(rs.getDate("acta.fecha_acta"));
                p.setAsunto(rs.getString("acta.asunto"));
                p.setFecha_convocatoria(rs.getDate("acta.fecha_convocatoria"));
                p.setLugar(rs.getString("acta.lugar"));
                p.setId_medio(rs.getInt("acta.id_medio"));
                p.setHora_inicio(rs.getString("acta.hora_inicio"));
                p.setHora_fin(rs.getString("acta.hora_fin"));
                p.setOrden_dia(rs.getString("acta.orden_dia"));
                p.setDesarrollo(rs.getString("acta.desarrollo"));
                p.setEstado(rs.getBoolean("acta.estado"));
                p.setFecha_creacion(rs.getTimestamp("acta.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("acta.fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public acta buscarActaID(int id_acta) {

        acta p = new acta();
        try {
            String sentencia;
            sentencia = "SELECT *FROM acta WHERE id_acta= '" + id_acta + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_acta(rs.getInt("id_acta"));
                p.setFecha_acta(rs.getDate("fecha_acta"));
                p.setAsunto(rs.getString("asunto"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setLugar(rs.getString("lugar"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setDesarrollo(rs.getString("desarrollo"));
                p.setEstado(rs.getBoolean("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public ArrayList<usuario> listadoAsistenciaActa(int id_acta) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN asistencia_acta ON usuario.id_usuario=asistencia_acta.id_usuario WHERE asistencia_acta.id_acta= '" + id_acta + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("usuario.id_usuario"));
                p.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                p.setNombre(rs.getString("usuario.nombre"));
                p.setCedula(rs.getString("usuario.cedula"));
                p.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                p.setApellido(rs.getString("usuario.apellido"));
                p.setCorreo(rs.getString("usuario.correo"));
                p.setClave(rs.getString("usuario.clave"));
                p.setIniciales(rs.getString("usuario.iniciales"));
                p.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                p.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean esConvocadoUsuarioID(int id_convocatoria, int id_usuario) {

        int contador = 0;
        boolean confirmacion = false;
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocados WHERE convocados.id_convocatoria= '" + id_convocatoria + "'AND convocados.id_usuario= '" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmacion = true;
        }

        return confirmacion;
    }

    public int idMaxActa() {

        int idmax = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_acta) AS idmax FROM acta";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                idmax = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return idmax;
    }

    public int idMaxPermisoHoras() {

        int idmax = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_permiso) AS idmax FROM permisohoras_usuario";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                idmax = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return idmax;
    }

    public int idMaxViatico() {

        int idmax = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_viatico) AS idmax FROM viatico";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                idmax = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return idmax;
    }

    public ArrayList<compromiso_acta> listadoCompromisosActa(int id_acta) {

        ArrayList<compromiso_acta> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM compromiso_acta WHERE id_acta= '" + id_acta + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                compromiso_acta p = new compromiso_acta();
                p.setId_compromiso(rs.getInt("id_compromiso"));
                p.setId_acta(rs.getInt("id_acta"));
                p.setFecha_cumplimiento(rs.getDate("fecha_cumplimiento"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setId_responsable(rs.getInt("id_responsable"));
                p.setEstado(rs.getInt("estado"));
                p.setGrado(rs.getString("grado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public boolean registroCompromiso(compromiso_acta ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO compromiso_acta(id_acta,descripcion,id_responsable,fecha_cumplimiento) VALUES(?,?,?,?)");
        st.setInt(1, ph.getId_acta());
        st.setString(2, ph.getDescripcion());
        st.setInt(3, ph.getId_responsable());
        st.setDate(4, ph.getFecha_cumplimiento());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroConvocatoria(convocatoria ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO convocatoria(fecha_convocatoria,id_convocador,asunto,lugar,hora_inicio,hora_fin,orden_dia) VALUES(?,?,?,?,?,?,?)");
        st.setDate(1, ph.getFecha_convocatoria());
        st.setInt(2, ph.getId_convocador());
        st.setString(3, ph.getAsunto());
        st.setString(4, ph.getLugar());
        st.setString(5, ph.getHora_inicio());
        st.setString(6, ph.getHora_fin());
        st.setString(7, ph.getOrden_dia());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<convocatoria> listadoConvocatoriasVigentes(int id_usuario) {

        ArrayList<convocatoria> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocatoria WHERE id_convocador= '" + id_usuario + "' AND estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                convocatoria p = new convocatoria();
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<convocatoria> listadoConvocatoriasVigentesConvocados(int id_usuario) {

        ArrayList<convocatoria> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM convocatoria INNER JOIN convocados ON convocados.id_convocatoria=convocatoria.id_convocatoria WHERE convocados.id_usuario=" + id_usuario + " AND estado=0";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                convocatoria p = new convocatoria();
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<convocatoria> listadoConvocatoriasAnteriores(int id_usuario) {

        ArrayList<convocatoria> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocatoria WHERE id_convocador= '" + id_usuario + "' AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                convocatoria p = new convocatoria();
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<convocatoria> listadoConvocatoriasAnuladas(int id_usuario) {

        ArrayList<convocatoria> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocatoria WHERE id_convocador= '" + id_usuario + "' AND estado=2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                convocatoria p = new convocatoria();
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<convocatoria> listadoConvocatoriasIncumplidas(int id_usuario) {

        ArrayList<convocatoria> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocatoria WHERE id_convocador= '" + id_usuario + "' AND estado=3";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                convocatoria p = new convocatoria();
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public convocatoria buscarConvocatoriaID(int id_convocatoria) {

        convocatoria p = new convocatoria();
        try {
            String sentencia;
            sentencia = "SELECT *FROM convocatoria WHERE id_convocatoria= '" + id_convocatoria + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_convocatoria(rs.getInt("id_convocatoria"));
                p.setId_convocador(rs.getInt("id_convocador"));
                p.setFecha_convocatoria(rs.getDate("fecha_convocatoria"));
                p.setAsunto(rs.getString("asunto"));
                p.setLugar(rs.getString("lugar"));
                p.setHora_inicio(rs.getString("hora_inicio"));
                p.setHora_fin(rs.getString("hora_fin"));
                p.setOrden_dia(rs.getString("orden_dia"));
                p.setId_medio(rs.getInt("id_medio"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroConvocados(convocados ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO convocados(id_usuario,id_convocatoria) VALUES(?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setInt(2, ph.getId_convocatoria());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<usuario> listadoConvocadosIdConvocatoria(int id_convocatoria) {

        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN convocados ON convocados.id_usuario=usuario.id_usuario WHERE convocados.id_convocatoria= '" + id_convocatoria + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario p = new usuario();
                p.setId_usuario(rs.getInt("usuario.id_usuario"));
                p.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                p.setNombre(rs.getString("usuario.nombre"));
                p.setCedula(rs.getString("usuario.cedula"));
                p.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                p.setApellido(rs.getString("usuario.apellido"));
                p.setCorreo(rs.getString("usuario.correo"));
                p.setClave(rs.getString("usuario.clave"));
                p.setIniciales(rs.getString("usuario.iniciales"));
                p.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                p.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<compromiso_acta> listadoCompromisosUsuarioIdEstado(int id_usuario, int estado) {

        ArrayList<compromiso_acta> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM compromiso_acta WHERE id_responsable= '" + id_usuario + "' AND estado='" + estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                compromiso_acta p = new compromiso_acta();
                p.setId_compromiso(rs.getInt("id_compromiso"));
                p.setId_acta(rs.getInt("id_acta"));
                p.setFecha_cumplimiento(rs.getDate("fecha_cumplimiento"));
                p.setId_responsable(rs.getInt("id_responsable"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setGrado(rs.getString("grado"));
                p.setAccion(rs.getString("accion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getInt("estado"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public compromiso_acta obtenerCompromisoID(int id_compromiso) {

        compromiso_acta p = new compromiso_acta();
        try {
            String sentencia;
            sentencia = "SELECT *FROM compromiso_acta WHERE id_compromiso= '" + id_compromiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_compromiso(rs.getInt("id_compromiso"));
                p.setId_acta(rs.getInt("id_acta"));
                p.setFecha_cumplimiento(rs.getDate("fecha_cumplimiento"));
                p.setId_responsable(rs.getInt("id_responsable"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setGrado(rs.getString("grado"));
                p.setAccion(rs.getString("accion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setEstado(rs.getInt("estado"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean registroAnularConvocatoria(anular_convocatoria ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO anular_convocatoria(id_convocatoria,razon) VALUES(?,?)");
        st.setInt(1, ph.getId_convocatoria());
        st.setString(2, ph.getRazon());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarConvocatoriaEstado(int id_convocatoria, int estado) throws SQLException {

        st = enlace.prepareStatement("UPDATE convocatoria SET estado=? WHERE id_convocatoria= '" + id_convocatoria + "'");
        st.setInt(1, estado);
        st.executeUpdate();
        st.close();

        return true;
    }

    public String[] listadoCorreosConvocadosIdConvocatoria(int id_convocatoria) {

        ArrayList<String> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario INNER JOIN convocados ON convocados.id_usuario=usuario.id_usuario WHERE convocados.id_convocatoria= '" + id_convocatoria + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(rs.getString("usuario.correo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        String[] arr = new String[listado.size()];
        arr = listado.toArray(arr);

        return arr;
    }

    public int idUltimaConvocatoria() {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_convocatoria) AS idmax FROM convocatoria ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public boolean eliminarAsistentesIDActa(int id_acta) {

        boolean confirmacion = false;
        try {
            st = enlace.prepareStatement("DELETE FROM convocados WHERE id_acta= '" + id_acta + "'");
            confirmacion = true;
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
            confirmacion = false;
        }

        return confirmacion;
    }

    public boolean eliminarCompromisoID(int id_compromiso) {

        boolean confirmacion = false;
        try {
            st = enlace.prepareStatement("DELETE FROM compromiso_acta WHERE id_compromiso= '" + id_compromiso + "'");
            confirmacion = true;
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
            confirmacion = false;
        }

        return confirmacion;
    }

    public boolean eliminarConvocadosIDConvocatoria(int id_convocatoria) {

        boolean confirmacion = false;
        try {
            st = enlace.prepareStatement("DELETE FROM convocados WHERE id_convocatoria= '" + id_convocatoria + "'");
            confirmacion = true;
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
            confirmacion = false;
        }

        return confirmacion;
    }

    public boolean actualizarAdjuntoPermisoHorasID(int id_permiso, String ruta) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET adjunto=? WHERE id_permiso= '" + id_permiso + "'");
        st.setString(1, ruta);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarAdjuntoPermisoHorasECU(int id_permiso, String ruta) throws SQLException {
        st = enlace.prepareStatement("UPDATE permiso_ecu SET adjunto=? WHERE id_permiso=?");
        st.setString(1, ruta);
        st.setInt(2, id_permiso);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarAdjuntoPermisoManual(int id_permiso, String ruta) throws SQLException {
        st = enlace.prepareStatement("UPDATE permiso_manual SET adjunto=? WHERE id_permiso=?");
        st.setString(1, ruta);
        st.setInt(2, id_permiso);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean adjuntarAsistenciaPermisoHoras(int idPermiso, String ruta) throws SQLException {
        st = enlace.prepareStatement("UPDATE permisohoras_usuario SET asistencia=? WHERE id_permiso=?");
        st.setString(1, ruta);
        st.setInt(2, idPermiso);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registroSolicitudTinta(solicitud_tinta ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO solicitud_tinta(id_para,asunto,fecha_solicitud,detalle,id_solicitante) VALUES(?,?,?,?,?)");
        st.setInt(1, ph.getId_para());
        st.setString(2, ph.getAsunto());
        st.setDate(3, ph.getFecha_solicitud());
        st.setString(4, ph.getDetalle());
        st.setInt(5, ph.getId_solicitante());
        st.executeUpdate();
        st.close();

        return true;
    }

    public solicitud_tinta buscarSolicitudTintaID(int id_solicitud) {

        solicitud_tinta p = new solicitud_tinta();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud_tinta WHERE id_solicitud= '" + id_solicitud + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_para(rs.getInt("id_para"));
                p.setAsunto(rs.getString("asunto"));
                p.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                p.setDetalle(rs.getString("detalle"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setEstado(rs.getInt("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public ArrayList<solicitud_tinta> listadoSolicitudTintaID() {

        ArrayList<solicitud_tinta> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud_tinta";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud_tinta p = new solicitud_tinta();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setId_para(rs.getInt("id_para"));
                p.setAsunto(rs.getString("asunto"));
                p.setFecha_solicitud(rs.getDate("fecha_solicitud"));
                p.setDetalle(rs.getString("detalle"));
                p.setId_solicitante(rs.getInt("id_solicitante"));
                p.setEstado(rs.getInt("estado"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public int obtenerCantidadSolicitudesTintaEstadoID(int id_estado) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM solicitud_tinta WHERE estado= '" + id_estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public boolean actualizarCompromisoGradoAccion(int id_compromiso, compromiso_acta elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE compromiso_acta SET grado=?,accion=?,estado=? WHERE id_compromiso= '" + id_compromiso + "'");
        st.setString(1, elemento.getGrado());
        st.setString(2, elemento.getAccion());
        st.setInt(3, elemento.getEstado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean eliminarParticipacionViaticoID(int id_viatico) {

        boolean confirmacion = false;
        try {
            st = enlace.prepareStatement("DELETE FROM participacion_viatico WHERE id_viatico= '" + id_viatico + "'");
            confirmacion = true;
            return st.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.out.println(ex);
            confirmacion = false;
        }

        return confirmacion;
    }

    public boolean registroVerificableCompromiso(verificable_compromiso ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO verificable_compromiso(id_compromiso,nombre,ruta) VALUES(?,?,?)");
        st.setInt(1, ph.getId_compromiso());
        st.setString(2, ph.getNombre());
        st.setString(3, ph.getRuta());
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<verificable_compromiso> listadoVerificablesCompromiso(int id_compromiso) {

        ArrayList<verificable_compromiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM verificable_compromiso WHERE id_compromiso='" + id_compromiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                verificable_compromiso elemento = new verificable_compromiso();
                elemento.setId_verificable(rs.getInt("id_verificable"));
                elemento.setId_compromiso(rs.getInt("id_compromiso"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public verificable_compromiso obtenerVerificablesID(int id_verificable) {

        verificable_compromiso elemento = new verificable_compromiso();
        try {
            String sentencia;
            sentencia = "SELECT *FROM verificable_compromiso WHERE id_verificable='" + id_verificable + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_verificable(rs.getInt("id_verificable"));
                elemento.setId_compromiso(rs.getInt("id_compromiso"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setRuta(rs.getString("ruta"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public int cantidadTotalCompromisosActasEstado(int id_usuario, int id_estado) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM compromiso_acta INNER JOIN acta ON compromiso_acta.id_acta=acta.id_acta INNER JOIN convocatoria ON convocatoria.id_convocatoria = acta.id_convocatoria WHERE convocatoria.id_convocador='" + id_usuario + "' AND compromiso_acta.estado='" + id_estado + "'AND YEAR(CURDATE())=YEAR(compromiso_acta.fecha_creacion)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadTotalCompromisosActas(int id_usuario) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM compromiso_acta INNER JOIN acta ON compromiso_acta.id_acta=acta.id_acta INNER JOIN convocatoria ON convocatoria.id_convocatoria = acta.id_convocatoria WHERE convocatoria.id_convocador='" + id_usuario + "' AND YEAR(CURDATE())=YEAR(compromiso_acta.fecha_creacion)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int totalCompromisosMensuales(int id_usuario, int mes) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS contador FROM compromiso_acta INNER JOIN acta ON compromiso_acta.id_acta=acta.id_acta INNER JOIN convocatoria ON convocatoria.id_convocatoria = acta.id_convocatoria WHERE convocatoria.id_convocador='" + id_usuario + "'AND MONTH(compromiso_acta.fecha_creacion)='" + mes + "' AND YEAR(CURDATE())=YEAR(compromiso_acta.fecha_creacion)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadCompromisosActaID(int id_acta, int id_estado) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM compromiso_acta WHERE id_acta='" + id_acta + "' AND estado='" + id_estado + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public int cantidadCompromisosActasID(int id_acta) {

        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) as contador FROM compromiso_acta WHERE id_acta='" + id_acta + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = rs.getInt("contador");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return contador;
    }

    public boolean existeVerificableCompromiso(int id_compromiso) {

        boolean confirmar = false;
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT *FROM verificable_compromiso WHERE id_compromiso='" + id_compromiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador++;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        if (contador > 0) {
            confirmar = true;
        }

        return confirmar;
    }

    public ArrayList<TipoAsistencia> GetTiposAsistencias() {
        ArrayList<TipoAsistencia> asistencias = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT id_cat_asistencia,des_asistencia FROM cat_asistencia";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                TipoAsistencia a = new TipoAsistencia();
                a.setId(rs.getInt("id_cat_asistencia"));
                a.setDescripcion(rs.getString("des_asistencia"));
                asistencias.add(a);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return asistencias;
    }

    public TipoAsistencia GetTipoAsistencia(int id) {
        TipoAsistencia a = new TipoAsistencia();
        try {
            String sentencia;
            sentencia = "SELECT id_cat_asistencia,des_asistencia FROM cat_asistencia WHERE id_cat_asistencia=" + id;
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                a.setId(rs.getInt("id_cat_asistencia"));
                a.setDescripcion(rs.getString("des_asistencia"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return a;
    }

    public int GetTipoUltimaAsistenciaHoy(int id_usu) throws ParseException {
        LocalDate fechaActual = LocalDate.now();
        String fechaActualS = fechaActual.toString().split(" ")[0];
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT id_tipo FROM asistencia WHERE fecha_creacion LIKE '%" + fechaActualS + "%' AND id_usuario=" + id_usu + " ORDER BY id_asistencia DESC LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("id_tipo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return res;
    }

    private String GetHoraSalidaAlmuerzo(int id_usu) throws ParseException {
        LocalDate fechaActual = LocalDate.now();
        String fechaActualS = fechaActual.toString().split(" ")[0];
        String res = "";
        try {
            String sentencia;
            sentencia = "SELECT hora_creacion FROM asistencia WHERE fecha_creacion LIKE '%" + fechaActualS + "%' AND id_usuario=" + id_usu + " AND id_tipo=2 ORDER BY id_asistencia DESC LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("hora_creacion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return res;
    }

    public boolean RegistrarAsistencia(int id_usuario, int tipo) throws ParseException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date horaSalidaAlmuerzo = null;
            if (tipo == 3) {
                horaSalidaAlmuerzo = dateFormat.parse(GetHoraSalidaAlmuerzo(id_usuario));
            }
            st = enlace.prepareStatement("INSERT INTO asistencia(id_usuario,fecha_creacion,hora_creacion,id_estado,id_tipo) VALUES(?,NOW(),TIME(NOW()),?,?)");
            st.setInt(1, id_usuario);
            Date horaEntrada = dateFormat.parse("08:00:00");
            Date horaActual = dateFormat.parse(LocalTime.now().toString());
            int id_estado = 0;
            switch (tipo) {
                case 1:
                    if (horaActual.compareTo(horaEntrada) > 0) {
                        id_estado = 1;
                    }
                    break;
                case 3:
                    long differenceInMilliSeconds = Math.abs(horaSalidaAlmuerzo.getTime() - horaActual.getTime());
                    long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
                    long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
                    long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;
                    long totalSecs = differenceInHours * 60 * 60 + differenceInMinutes * 60 + differenceInSeconds;
                    if (totalSecs > 3600) {
                        id_estado = 1;
                    }
                    break;
                default:;
            }
            st.setInt(2, id_estado);
            st.setInt(3, tipo);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public ArrayList<asistencia> listadoAsistenciaEstadoUsuarioID(int id_usuario, int id_estado) {
        ArrayList<asistencia> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_asistencia WHERE id_usuario=? AND id_estado=? AND DATE(fecha_creacion) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                asistencia elemento = new asistencia();
                elemento.setId_asistencia(rs.getInt("id_asistencia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFuncionario(rs.getString("funcionario"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setHora_creacion(rs.getString("hora_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setTipo(rs.getString("tipo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAsistenciaEstadoUsuarioID(id_usuario, id_estado) | " + ex);
        }
        return listado;
    }

    public ArrayList<asistencia> listadoAsistenciaEstadoUsuarioID(int id_estado) {
        ArrayList<asistencia> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_asistencia WHERE id_estado=? AND DATE(fecha_creacion) BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            rs = st.executeQuery();
            while (rs.next()) {
                asistencia elemento = new asistencia();
                elemento.setId_asistencia(rs.getInt("id_asistencia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFuncionario(rs.getString("funcionario"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setHora_creacion(rs.getString("hora_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setTipo(rs.getString("tipo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAsistenciaEstadoUsuarioID(id_estado) | " + ex);
        }
        return listado;
    }

    public ArrayList<asistencia> listadoAsistenciaEstadoUsuarioIDRangoFecha(java.sql.Date fecha_inicio, java.sql.Date fecha_fin, int id_usuario, int id_estado) {
        ArrayList<asistencia> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_asistencia WHERE id_usuario=? AND id_estado=? AND DATE(fecha_creacion) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, id_estado);
            st.setDate(3, fecha_inicio);
            st.setDate(4, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                asistencia elemento = new asistencia();
                elemento.setId_asistencia(rs.getInt("id_asistencia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFuncionario(rs.getString("funcionario"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setHora_creacion(rs.getString("hora_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setTipo(rs.getString("tipo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, id_usuario, id_estado) | " + ex);
        }
        return listado;
    }

    public ArrayList<asistencia> listadoAsistenciaEstadoUsuarioIDRangoFecha(java.sql.Date fecha_inicio, java.sql.Date fecha_fin, int id_estado) {
        ArrayList<asistencia> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_asistencia WHERE id_estado=? AND DATE(fecha_creacion) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_estado);
            st.setDate(2, fecha_inicio);
            st.setDate(3, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                asistencia elemento = new asistencia();
                elemento.setId_asistencia(rs.getInt("id_asistencia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFuncionario(rs.getString("funcionario"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setHora_creacion(rs.getString("hora_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setTipo(rs.getString("tipo"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAsistenciaEstadoUsuarioIDRangoFecha(fecha_inicio, fecha_fin, id_estado) | " + ex);
        }
        return listado;
    }

    public asistencia obtenerAsistenciaId(int id_asistencia) {
        asistencia elemento = new asistencia();
        try {
            String sentencia = "SELECT * FROM v_asistencia WHERE id_asistencia=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_asistencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_asistencia(rs.getInt("id_asistencia"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFuncionario(rs.getString("funcionario"));
                elemento.setFecha_creacion(rs.getDate("fecha_creacion"));
                elemento.setHora_creacion(rs.getString("hora_creacion"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setTipo(rs.getString("tipo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerAsistenciaId | " + ex);
        }
        return elemento;
    }

    public aprobacion_vacaciones obtenerAprobacionVacacionesID(int id_permiso) {
        aprobacion_vacaciones elemento = new aprobacion_vacaciones();
        try {
            String sentencia;
            sentencia = "SELECT *FROM aprobacion_permisovacaciones WHERE id_permiso='" + id_permiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_aprobacion(rs.getInt("id_aprobacion"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public AprobacionHoras obtenerAprobacionHorasID(int id_permiso) {
        AprobacionHoras elemento = new AprobacionHoras();
        try {
            String sentencia;
            sentencia = "SELECT * FROM aprobacion_permisohoras WHERE id_permiso=" + id_permiso;
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_aprobacion(rs.getInt("id_aprobacion"));
                elemento.setId_solicitud(rs.getInt("id_permiso"));
                elemento.setId_aprueba(rs.getInt("id_usuario"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public revision_vacaciones obtenerRevisionVacacionesID(int id_permiso) {
        revision_vacaciones elemento = new revision_vacaciones();
        try {
            String sentencia;
            sentencia = "SELECT *FROM revision_permisovacaciones WHERE id_permiso='" + id_permiso + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_revision(rs.getInt("id_revision"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return elemento;
    }

    public RevisionHoras obtenerRevisionHorasID(int id_permiso) {
        RevisionHoras elemento = new RevisionHoras();
        try {
            String sentencia;
            sentencia = "SELECT * FROM revision_permisohoras WHERE id_permiso=" + id_permiso;
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_revision(rs.getInt("id_revision"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerRevisionHorasID | " + ex);
        }
        return elemento;
    }

    public RevisionHoras obtenerRevisionHorasECU(int id_permiso) {
        RevisionHoras elemento = new RevisionHoras();
        try {
            String sentencia = "SELECT * FROM revision_ecu WHERE id_permiso=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_permiso);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_revision(rs.getInt("id_revision"));
                elemento.setId_permiso(rs.getInt("id_permiso"));
                elemento.setId_usuario(rs.getInt("id_usuario"));
                elemento.setFecha_registro(rs.getTimestamp("fecha_registro"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerRevisionHorasECU | " + ex);
        }
        return elemento;
    }

    public ArrayList<banco> listadoBancos() {

        ArrayList<banco> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM banco ORDER BY nombre ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                banco p = new banco();
                p.setId_banco(rs.getInt("id_banco"));
                p.setNombre(rs.getString("nombre"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public banco obtenerBancoID(int id_banco) {
        banco p = new banco();
        try {
            String sentencia;
            sentencia = "SELECT *FROM banco WHERE id_banco='" + id_banco + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_banco(rs.getInt("id_banco"));
                p.setNombre(rs.getString("nombre"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public boolean enviarCorreoMasivo(String[] para, String mensaje, String asunto) {
        boolean enviado = false;
        try {
            String host = "smtp.gmail.com";
            String de = "reacsissoporte@gmail.com";
            String clave = "@Rs123456";
            Properties prop = System.getProperties();
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", host);
            prop.put("mail.smtp.user", de);
            prop.put("mail.smtp.password", clave);
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            Session sesion = Session.getDefaultInstance(prop, null);
            MimeMessage message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(de));
            InternetAddress[] direcciones = new InternetAddress[para.length];
            for (int i = 0; i < para.length; i++) {
                if (!para[i].equalsIgnoreCase("")) {
                    direcciones[i] = new InternetAddress(para[i]);
                }
            }
            for (int i = 0; i < direcciones.length; i++) {
                message.addRecipient(Message.RecipientType.TO, direcciones[i]);
            }
            message.setSubject(asunto);
            message.setContent("<HTML>\n"
                    + "<HEAD><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
                    + "<TITLE>Alcaldía de Esmerladas</TITLE>\n"
                    + "</HEAD>\n"
                    + "<BODY>\n"
                    + "<div> <table border=0 bordercolor=black align=center width=600 cellpadding=3 cellspacing=2 style=\"background-image: url(http://186.46.57.100:8085/intranet/assets/img/bgconv.jpg);\" height=\"350\">\n"
                    + "<tr>\n"
                    + "<td>\n"
                    + "<font size=3 face=\"arial,verdana\">\n"
                    + "<br>\n"
                    + "" + mensaje + "\n"
                    + "</font>\n"
                    + "</td>\n"
                    + "</tr>\n"
                    + "</table>"
                    + "                    </div>\n"
                    + "</BODY>\n"
                    + "</HTML>", "text/html");
            Transport transport = sesion.getTransport("smtp");
            transport.connect(host, de, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            enviado = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return enviado;
    }

    public ArrayList<modulo> listadoModulosTipoUsuarioID(int id_usuario) {
        ArrayList<modulo> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT DISTINCT(modulo.id_modulo),modulo.descripcion,modulo.ruta_enlace,modulo.icono,modulo.estado_clases,modulo.estado,modulo.blank,modulo.fecha_creacion,modulo.fecha_creacion FROM modulo INNER JOIN modulo_usuario ON modulo.id_modulo = modulo_usuario.id_modulo INNER JOIN tipo_usuario ON tipo_usuario.id_rol = modulo_usuario.id_rol WHERE tipo_usuario.id_usuario= '" + id_usuario + "' AND modulo.estado=1 ORDER BY modulo.orden ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                modulo p = new modulo();
                p.setId_modulo(rs.getInt("modulo.id_modulo"));
                p.setDescripcion(rs.getString("modulo.descripcion"));
                p.setRuta_enlace(rs.getString("modulo.ruta_enlace"));
                p.setIcono(rs.getString("modulo.icono"));
                p.setEstado_clases(rs.getString("modulo.estado_clases"));
                p.setEstado(rs.getBoolean("modulo.estado"));
                p.setBlank(rs.getBoolean("modulo.blank"));
                p.setFecha_creacion(rs.getTimestamp("modulo.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("modulo.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<componente> listadoComponentesTipoUsuarioID(int id_usuario, int id_modulo) {
        ArrayList<componente> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM componente INNER JOIN componente_usuario ON componente.id_componente = componente_usuario.id_componente INNER JOIN tipo_usuario ON tipo_usuario.id_rol = componente_usuario.id_rol WHERE tipo_usuario.id_usuario= '" + id_usuario + "' AND componente.id_modulo='" + id_modulo + "' AND componente.estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                componente p = new componente();
                p.setId_componente(rs.getInt("componente.id_componente"));
                p.setId_modulo(rs.getInt("componente.id_modulo"));
                p.setDescripcion(rs.getString("componente.descripcion"));
                p.setRuta_enlace(rs.getString("componente.ruta_enlace"));
                p.setEstado_clases(rs.getString("componente.estado_clases"));
                p.setIcono(rs.getString("componente.icono"));
                p.setEstado(rs.getBoolean("componente.estado"));
                p.setFecha_creacion(rs.getTimestamp("componente.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("componente.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public componente componenteModuloTipoUsuarioID(int id_usuario, int id_modulo) {
        componente p = new componente();
        try {
            String sentencia;
            sentencia = "SELECT *FROM componente INNER JOIN componente_usuario ON componente.id_componente = componente_usuario.id_componente INNER JOIN tipo_usuario ON tipo_usuario.id_rol = componente_usuario.id_rol WHERE tipo_usuario.id_usuario= '" + id_usuario + "' AND componente.id_modulo='" + id_modulo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_componente(rs.getInt("componente.id_componente"));
                p.setId_modulo(rs.getInt("componente.id_modulo"));
                p.setDescripcion(rs.getString("componente.descripcion"));
                p.setRuta_enlace(rs.getString("componente.ruta_enlace"));
                p.setEstado_clases(rs.getString("componente.estado_clases"));
                p.setEstado(rs.getBoolean("componente.estado"));
                p.setFecha_creacion(rs.getTimestamp("componente.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("componente.fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return p;
    }

    public componente obtenerComponenteDescripcion(String descripcion) {
        componente p = new componente();
        try {
            String sentencia;
            sentencia = "SELECT *FROM componente INNER JOIN componente_usuario ON componente.id_componente = componente_usuario.id_componente INNER JOIN tipo_usuario ON tipo_usuario.id_rol = componente_usuario.id_rol WHERE componente.descripcion= '" + descripcion + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_componente(rs.getInt("componente.id_componente"));
                p.setId_modulo(rs.getInt("componente.id_modulo"));
                p.setDescripcion(rs.getString("componente.descripcion"));
                p.setRuta_enlace(rs.getString("componente.ruta_enlace"));
                p.setEstado_clases(rs.getString("componente.estado_clases"));
                p.setEstado(rs.getBoolean("componente.estado"));
                p.setFecha_creacion(rs.getTimestamp("componente.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("componente.fecha_creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return p;
    }

    public ArrayList<subcomponente> listadoSubcomponentesTipoUsuarioID(int id_usuario, int id_componente) {
        ArrayList<subcomponente> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM subcomponente INNER JOIN subcomponente_usuario ON subcomponente.id_subcomponente = subcomponente_usuario.id_subcomponente INNER JOIN tipo_usuario ON tipo_usuario.id_rol = subcomponente_usuario.id_rol WHERE tipo_usuario.id_usuario= '" + id_usuario + "' AND subcomponente.id_componente='" + id_componente + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                subcomponente p = new subcomponente();
                p.setId_subcomponente(rs.getInt("subcomponente.id_subcomponente"));
                p.setId_componente(rs.getInt("subcomponente.id_componente"));
                p.setDescripcion(rs.getString("subcomponente.descripcion"));
                p.setRuta_enlace(rs.getString("subcomponente.ruta_enlace"));
                p.setEstado(rs.getBoolean("subcomponente.estado"));
                p.setFecha_creacion(rs.getTimestamp("subcomponente.fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("subcomponente.fecha_creacion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public int obtenerDiasHabilesRangoFecha(java.sql.Date fecha_inicio, java.sql.Date fecha_fin) {
        int dias = 0;
        try {
            String sentencia;
            sentencia = "SELECT contarDiasHabiles('" + fecha_inicio + "','" + fecha_fin + "') AS dias";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                dias = rs.getInt("dias");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return dias;
    }

    public ArrayList<almacenamiento> listadoAlmacenamientos() {

        ArrayList<almacenamiento> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM almacenamiento WHERE estado = 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                almacenamiento elemento = new almacenamiento();
                elemento.setId_almacenamiento(rs.getInt("id_almacenamiento"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setTipo_contenido(rs.getInt("tipo_contenido"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public almacenamiento obtenerAlmacenamientosID(int id_almacenamiento) {

        almacenamiento elemento = new almacenamiento();
        try {
            String sentencia;
            sentencia = "SELECT *FROM almacenamiento WHERE id_almacenamiento='" + id_almacenamiento + "' AND estado=1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_almacenamiento(rs.getInt("id_almacenamiento"));
                elemento.setNombre(rs.getString("nombre"));
                elemento.setTipo_contenido(rs.getInt("tipo_contenido"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public boolean registroAlmacenamiento(almacenamiento ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO almacenamiento(id_usuario, nombre, tipo_contenido) VALUES(?,?,?)");
        st.setInt(1, ph.getId_usuario());
        st.setString(2, ph.getNombre());
        st.setInt(3, ph.getTipo_contenido());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarAlmacenamientoID(int id_almacenamiento, almacenamiento elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE almacenamiento SET nombre=?, tipo_contenido=? WHERE id_almacenamiento= '" + id_almacenamiento + "'");
        st.setString(1, elemento.getNombre());
        st.setInt(2, elemento.getTipo_contenido());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean inhabilitarAlmacenamientoID(int id_almacenamiento) throws SQLException {

        st = enlace.prepareStatement("UPDATE almacenamiento SET estado=? WHERE id_almacenamiento= '" + id_almacenamiento + "'");
        st.setBoolean(1, false);
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean habilitarAlmacenamientoID(int id_almacenamiento) throws SQLException {

        st = enlace.prepareStatement("UPDATE almacenamiento SET estado=? WHERE id_almacenamiento= '" + id_almacenamiento + "'");
        st.setBoolean(1, true);
        st.executeUpdate();
        st.close();

        return true;
    }

    public ArrayList<tipo_contenido> listadoTipoContenidos() {

        ArrayList<tipo_contenido> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_contenido WHERE estado = 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_contenido elemento = new tipo_contenido();
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public tipo_contenido obtenerTipoContenidoID(int id_tipo) {

        tipo_contenido elemento = new tipo_contenido();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_contenido WHERE id_tipo='" + id_tipo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_tipo(rs.getInt("id_tipo"));
                elemento.setDescripcion(rs.getString("descripcion"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public int idUltimoRecurso() {

        int id_estado = 0;
        try {
            String sentencia;
            sentencia = "SELECT MAX(id_recurso) AS idmax FROM recurso ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                id_estado = rs.getInt("idmax");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return id_estado;
    }

    public boolean registroRecurso(recurso ph) throws SQLException {

        st = enlace.prepareStatement("INSERT INTO almacenamiento(id_almacenamiento, id_nombre, nombre, descripcion) VALUES(?,?,?,?)");
        st.setInt(1, ph.getId_almacenamiento());
        st.setInt(2, ph.getId_usuario());
        st.setString(3, ph.getNombre());
        st.setString(4, ph.getDescripcion());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarAdjuntoRecursoID(int id_recurso, recurso elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE almacenamiento SET ruta=? WHERE id_recurso= '" + id_recurso + "'");
        st.setString(1, elemento.getRuta());
        st.executeUpdate();
        st.close();

        return true;
    }

    public inventario buscar_registroID(int id) {

        inventario p = new inventario();
        try {
            String sentencia;
            sentencia = "SELECT *FROM inventario WHERE id_inventario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setNombre(rs.getString("nombre"));
                p.setUsuariodominio(rs.getString("usuario_dominio"));
                p.setTipodispositivo(rs.getString("tipo_dispositivo"));
                p.setMacaddress(rs.getString("macaddress"));
                p.setMemoria(rs.getString("memoria"));
                p.setProcesador(rs.getString("procesador"));
                p.setDireccion_ip(rs.getString("direccion_ip"));
                p.setConexion_dhcp(rs.getString("conexion_dhcp"));
                p.setConexion_permanente(rs.getString("conexion_permanente"));
                p.setAntivirus(rs.getString("antivirus"));
                p.setCabildo(rs.getString("cabildo"));
                p.setSigame(rs.getString("sigame"));
                p.setOffice365(rs.getString("office365"));
                p.setArquitectura_so(rs.getString("arquitectura_so"));
                p.setCodigo_bodega(rs.getString("codigo_bodega"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setNombre_edificio(rs.getString("nombre_edificio"));
                p.setPiso(rs.getString("piso"));
                p.setUnidad_administrativa(rs.getString("unidad_administrativa"));
                p.setFuncionario(rs.getString("funcionario"));

            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return p;
    }

    public int registroTramite(tramite ph) throws SQLException {
        String sql = "INSERT INTO tramite(numero_memo, fecha_elaboracion, hora_elaboracion, id_envia, id_para, asunto, tipo_tramite) VALUES('" + ph.getNumero_memorando() + "','" + ph.getFecha_elaboracion() + "','" + ph.getHora_elaboracion() + "'," + ph.getId_envia() + "," + ph.getId_para() + ",'" + ph.getAsunto() + "'," + ph.getTipo_tramite() + ")";
        Statement stmt = enlace.createStatement();
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        stmt.close();
        rs.close();
        return res;
    }

    public boolean actualizarTramiteAdjuntoID(int id_tramite, tramite elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE tramite SET numero_memo=?, fecha_elaboracion=?, hora_elaboracion=?, fecha_recepcion=?, id_envia=?, id_para=?, asunto=?, tipo_tramite=?, subtipo=?, id_estado=?, adjunto=? WHERE id_tramite= '" + id_tramite + "'");
        st.setString(1, elemento.getNumero_memorando());
        st.setDate(2, elemento.getFecha_elaboracion());
        st.setString(3, elemento.getHora_elaboracion());
        st.setTimestamp(4, elemento.getFecha_recepcion());
        st.setInt(5, elemento.getId_envia());
        st.setInt(6, elemento.getId_para());
        st.setString(7, elemento.getAsunto());
        st.setInt(8, elemento.getTipo_tramite());
        st.setInt(9, elemento.getSubtipo());
        st.setInt(10, elemento.getId_estado());
        st.setString(11, elemento.getAdjunto());
        st.executeUpdate();
        st.close();

        return true;
    }

    public boolean actualizarTramiteAID(int id_tramite, tramite elemento) throws SQLException {

        st = enlace.prepareStatement("UPDATE tramite SET numero_memo=?, fecha_elaboracion=?, hora_elaboracion=?, fecha_recepcion=?, id_envia=?, id_para=?, asunto=?, tipo_tramite=?, subtipo=?, id_estado=? WHERE id_tramite= '" + id_tramite + "'");
        st.setString(1, elemento.getNumero_memorando());
        st.setDate(2, elemento.getFecha_elaboracion());
        st.setString(3, elemento.getHora_elaboracion());
        st.setTimestamp(4, elemento.getFecha_recepcion());
        st.setInt(5, elemento.getId_envia());
        st.setInt(6, elemento.getId_para());
        st.setString(7, elemento.getAsunto());
        st.setInt(8, elemento.getTipo_tramite());
        st.setInt(9, elemento.getSubtipo());
        st.setInt(10, elemento.getId_estado());
        st.executeUpdate();
        st.close();

        return true;
    }

    public tramite obtenerTramiteID(int id_tramite) {

        tramite elemento = new tramite();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tramite WHERE id_tramite ='" + id_tramite + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return elemento;
    }

    public ArrayList<tramite> listadoTramitesUsuarioId(int id_usuario) {

        ArrayList<tramite> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tramite WHERE id_envia ='" + id_usuario + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite elemento = new tramite();
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return listado;
    }

    public ArrayList<tramite> getListadoTramitesSalida(int id_usuario, boolean devueltos) {
        ArrayList<tramite> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM tramite WHERE id_envia=? AND id_estado IN (" + (devueltos ? "2" : "0,1") + ")";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite elemento = new tramite();
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getListadoTramitesSalida(id_usuario, devueltos) | " + ex);
        }
        return listado;
    }

    public ArrayList<tramite> getListadoTramitesSalida(int id_usuario, boolean devueltos, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<tramite> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM tramite WHERE id_envia=? AND id_estado IN (" + (devueltos ? "2" : "0,1") + ") AND fecha_elaboracion BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite elemento = new tramite();
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getListadoTramitesSalida(id_usuario, devueltos, fechaIni, fechaFin) | " + ex);
        }
        return listado;
    }

    public ArrayList<tramite> getListadoTramitesEntrada(int idUsu, int estado) {
        ArrayList<tramite> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM tramite WHERE id_para=? AND id_estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite elemento = new tramite();
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getListadoTramitesEntrada(idUsu, estado) | " + ex);
        }
        return listado;
    }

    public ArrayList<tramite> getListadoTramitesEntrada(int idUsu, int estado, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<tramite> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM tramite WHERE id_para=? AND id_estado=? AND fecha_elaboracion BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, estado);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite elemento = new tramite();
                elemento.setId_tramite(rs.getInt("id_tramite"));
                elemento.setNumero_memorando(rs.getString("numero_memo"));
                elemento.setFecha_elaboracion(rs.getDate("fecha_elaboracion"));
                elemento.setHora_elaboracion(rs.getString("hora_elaboracion"));
                elemento.setFecha_recepcion(rs.getTimestamp("fecha_recepcion"));
                elemento.setId_envia(rs.getInt("id_envia"));
                elemento.setId_para(rs.getInt("id_para"));
                elemento.setAsunto(rs.getString("asunto"));
                elemento.setTipo_tramite(rs.getInt("tipo_tramite"));
                elemento.setSubtipo(rs.getInt("subtipo"));
                elemento.setId_estado(rs.getInt("id_estado"));
                elemento.setAdjunto(rs.getString("adjunto"));
                elemento.setDevuelto(rs.getString("devuelto"));
                elemento.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                elemento.setFecha_update(rs.getTimestamp("fecha_update"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getListadoTramitesEntrada(idUsu, estado, fechaIni, fechaFin) | " + ex);
        }
        return listado;
    }

    public boolean actualizarAdjuntoTramiteID(int id_tramite, tramite elemento) throws SQLException {
        st = enlace.prepareStatement("UPDATE tramite SET adjunto=? WHERE id_tramite= '" + id_tramite + "'");
        st.setString(1, elemento.getAdjunto());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarEstadoTramiteID(int id_tramite, int id_estado) throws SQLException {
        st = enlace.prepareStatement("UPDATE tramite SET id_estado=? WHERE id_tramite= '" + id_tramite + "'");
        st.setInt(1, id_estado);
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<tipo_tramite> listadoTipoTramite() {
        ArrayList<tipo_tramite> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM tipo_tramite WHERE estado = 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tipo_tramite p = new tipo_tramite();
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                p.setFecha_update(rs.getTimestamp("fecha_update"));
                p.setEstado(rs.getBoolean("estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public java.sql.Date restarDiaFecha(java.sql.Date fecha_retorno) {
        java.sql.Date fecha_fin = null;
        try {
            String sentencia;
            sentencia = "select DATE_SUB('" + fecha_retorno + "',INTERVAL 1 DAY) as fecha_fin";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                fecha_fin = rs.getDate("fecha_fin");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return fecha_fin;
    }

    public cargo getCargoByCodigo(String codigo) {
        cargo c = null;
        try {
            String sentencia = "SELECT * FROM cargo WHERE codigo_cargo=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                c = new cargo();
                c.setDescripcion(rs.getString("descripcion"));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("getCargoByCodigo | " + e);
        }
        return c;
    }

    public organizacion getUnidadByCodigo(String codigo) {
        organizacion o = null;
        try {
            String sentencia = "SELECT * FROM organizacion WHERE nivel_hijo=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                o = new organizacion();
                o.setNombre(rs.getString("nombre"));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("getUnidadByCodigo | " + e);
        }
        return o;
    }

    public ArrayList<Tutorial> getTutoriales() {
        ArrayList<Tutorial> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM tutorial";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                Tutorial elemento = new Tutorial();
                elemento.setId(rs.getInt("id_tutorial"));
                elemento.setVideo(rs.getString("ruta_video"));
                elemento.setPdf(rs.getString("ruta_pdf"));
                elemento.setDescripcion(rs.getString("descripcion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTutoriales | " + ex);
        }
        return listado;
    }

    public ArrayList<Tutorial> getManualesSigcal() {
        ArrayList<Tutorial> res = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM manual_sigcal";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                Tutorial elemento = new Tutorial();
                elemento.setId(rs.getInt("id"));
                elemento.setPdf(rs.getString("ruta"));
                elemento.setDescripcion(rs.getString("descripcion"));
                res.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getManualesSigcal | " + ex);
        }
        return res;
    }

    public Formulario getFormulario(int id) {
        Formulario res = new Formulario();
        try {
            String sentencia;
            sentencia = "SELECT * FROM formulario WHERE id_formulario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new Formulario(rs.getInt("id_formulario"),
                        rs.getString("ruta_doc"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFormulario | " + ex);
        }
        return res;
    }

    public ArrayList<Formulario> getFormularios() {
        ArrayList<Formulario> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM formulario WHERE direccion IS NULL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new Formulario(rs.getInt("id_formulario"),
                        rs.getString("ruta_doc"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFormularios | " + ex);
        }
        return listado;
    }

    public ArrayList<Formulario> getFormularios(int idDireccion) {
        ArrayList<Formulario> listado = new ArrayList<>();
        try {
            String sentencia;
            sentencia = "SELECT * FROM formulario WHERE direccion=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idDireccion);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new Formulario(rs.getInt("id_formulario"),
                        rs.getString("ruta_doc"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fecha")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFormularios | " + ex);
        }
        return listado;
    }

    public int consultarCodigoUsuario(int id_usuario) {
        int codigo = 0;
        try {
            String sentencia;
            sentencia = "SELECT codigo_usuario FROM usuario WHERE id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                codigo = rs.getInt("codigo_usuario");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("consultarCodigoUsuario | " + ex);
        }
        return codigo;
    }

    public int consultarIdUsuarioByCodigo(final int codigo) {
        int id = 0;
        try {
            String sentencia;
            sentencia = "SELECT id_usuario FROM usuario WHERE codigo_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id_usuario");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("consultarIdUsuarioByCodigo | " + ex);
        }
        return id;
    }

    public boolean existeSoporteSinCalificar(int idUsu) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT (SELECT COUNT(DISTINCT(id_solicitud)) FROM calificacion_soporte WHERE id_usuario=?)<(SELECT COUNT(*) FROM solicitud_soporte WHERE id_usuario=? AND id_estado=3) AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getBoolean("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("existeSoporteSinCalificar | " + ex);
        }
        return res;
    }

    public boolean existeAdjuntoSolicitud(int idSoli) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT * FROM diagnostico_soporte WHERE id_solicitud=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idSoli);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("adjunto") != null;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("existeAdjunto | " + ex);
        }
        return res;
    }

    public String getDireccionUsuarioId(int id) {
        String res = "DIRECCIÓN NO ASIGNADA";
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario INNER JOIN organizacion ON (IF(usuario.codigo_funcion='ninguno',usuario.codigo_unidad,usuario.codigo_funcion))=organizacion.nivel_hijo WHERE usuario.id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("organizacion.nombre");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDireccionUsuario | " + ex);
        }
        return res;
    }

    public int getIdDireccionUsuarioId(int id) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario INNER JOIN organizacion ON (IF(usuario.codigo_funcion='ninguno',usuario.codigo_unidad,usuario.codigo_funcion))=organizacion.nivel_hijo WHERE usuario.id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("organizacion.id_organizacion");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getIdDireccionUsuarioId | " + ex);
        }
        return res;
    }

    public int diferenciaHorasMysql(Timestamp fecha) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT HOUR(SEC_TO_TIME(TIMESTAMPDIFF(SECOND,?,CURRENT_TIMESTAMP))) AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setTimestamp(1, fecha);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("diferenciaHorasMysql | " + ex);
        }
        return res;
    }

    public tipo_tramite getTipoTramite(int id_tipo) {
        tipo_tramite t = new tipo_tramite();
        try {
            final String sentencia = "SELECT * FROM tipo_tramite WHERE id_tipo=? AND estado=1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tipo);
            rs = st.executeQuery();
            while (rs.next()) {
                t.setId_tipo(rs.getInt("id_tipo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setCodigo(rs.getString("codigo"));
                t.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                t.setFecha_update(rs.getTimestamp("fecha_update"));
                t.setEstado(rs.getBoolean("estado"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTipoTramite | " + ex);
        }
        return t;
    }

    public boolean auditarDINARDAP(int idUsu, String ip, String dato, int categoria, int idAuditoria, int idRegistro) {
        try {
            String sentencia = "INSERT INTO dinardap(ID_USU,IP_DIN,DATO_DIN,CAT_DIN,ID_AUDITORIA,ID_REGISTRO) VALUES(?,?,?,?,?,?)";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setString(2, ip);
            st.setString(3, dato);
            st.setInt(4, categoria);
            if (idAuditoria == 0) {
                st.setNull(5, Types.BIGINT);
                st.setNull(6, Types.BIGINT);
            } else {
                st.setInt(5, idAuditoria);
                st.setInt(6, idRegistro);
            }
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("auditarDINARDAP | " + ex);
        }
        return false;
    }

    public boolean verificarUsuarioCumpleRol(int idUsu, String rol) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT * FROM tipo_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE tipo_usuario.id_usuario=? AND rol.descripcion=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setString(2, rol);
            rs = st.executeQuery();
            while (rs.next()) {
                res = true;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("verificarUsuarioCumpleRol | " + ex);
        }
        return res;
    }

    public boolean verificarUsuarioCumpleRol(int idUsu, int rol) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT * FROM tipo_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE tipo_usuario.id_usuario=? AND rol.id_rol=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, rol);
            rs = st.executeQuery();
            while (rs.next()) {
                res = true;
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("verificarUsuarioCumpleRol | " + ex);
        }
        return res;
    }

    public ArrayList<usuario> listarUsuariosDINARDAP() {
        ArrayList<usuario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM usuario INNER JOIN tipo_usuario ON usuario.id_usuario=tipo_usuario.id_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE rol.descripcion IN ('cioce','admin_cioce') ORDER BY apellido ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("usuario.id_usuario"));
                elemento.setCodigo_usuario(rs.getString("usuario.codigo_usuario"));
                elemento.setNombre(rs.getString("usuario.nombre"));
                elemento.setCedula(rs.getString("usuario.cedula"));
                elemento.setCodigo_cargo(rs.getString("usuario.codigo_cargo"));
                elemento.setApellido(rs.getString("usuario.apellido"));
                elemento.setCorreo(rs.getString("usuario.correo"));
                elemento.setClave(rs.getString("usuario.clave"));
                elemento.setIniciales(rs.getString("usuario.iniciales"));
                elemento.setCodigo_unidad(rs.getString("usuario.codigo_unidad"));
                elemento.setCodigo_funcion(rs.getString("usuario.codigo_funcion"));
                elemento.setFuncion(rs.getString("usuario.funcion"));
                elemento.setFirma(rs.getString("usuario.firma"));
                elemento.setTipo_funcion(rs.getInt("usuario.tipo_funcion"));
                elemento.setFecha_creacion(rs.getDate("usuario.fecha_creacion"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listarUsuariosDINARDAP |" + ex);
        }
        return listado;
    }

    public ArrayList<AuditoriaDINARDAP> listarAuditoriaDINARDAP() {
        ArrayList<AuditoriaDINARDAP> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM dinardap WHERE (YEAR(FECHA_DIN) = YEAR(CURRENT_DATE()) AND MONTH(FECHA_DIN)= MONTH(CURRENT_DATE())) ORDER BY ID_DIN DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new AuditoriaDINARDAP(rs.getInt("id_din"),
                        rs.getInt("id_usu"),
                        rs.getString("ip_din"),
                        rs.getString("dato_din"),
                        rs.getInt("cat_din"),
                        rs.getTimestamp("fecha_din")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAuditoriaDINARDAP | " + ex);
        }
        return listado;
    }

    public ArrayList<AuditoriaDINARDAP> listarAuditoriaDINARDAPUsuario(int idUsu) {
        ArrayList<AuditoriaDINARDAP> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM dinardap WHERE (YEAR(FECHA_DIN) = YEAR(CURRENT_DATE()) AND MONTH(FECHA_DIN)= MONTH(CURRENT_DATE()) AND ID_USU=?) ORDER BY ID_DIN DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new AuditoriaDINARDAP(rs.getInt("id_din"),
                        rs.getInt("id_usu"),
                        rs.getString("ip_din"),
                        rs.getString("dato_din"),
                        rs.getInt("cat_din"),
                        rs.getTimestamp("fecha_din")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoAuditoriaDINARDAP | " + ex);
        }
        return listado;
    }

    public ArrayList<AuditoriaDINARDAP> listarAuditoriaDINARDAPFecha(java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<AuditoriaDINARDAP> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM dinardap WHERE CAST(FECHA_DIN AS DATE) BETWEEN ? AND ? ORDER BY ID_DIN DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new AuditoriaDINARDAP(rs.getInt("id_din"),
                        rs.getInt("id_usu"),
                        rs.getString("ip_din"),
                        rs.getString("dato_din"),
                        rs.getInt("cat_din"),
                        rs.getTimestamp("fecha_din")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listarAuditoriaDINARDAPFecha | " + ex);
        }
        return listado;
    }

    public ArrayList<AuditoriaDINARDAP> listarAuditoriaDINARDAPUsuarioFecha(int idUsu, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<AuditoriaDINARDAP> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM dinardap WHERE ID_USU=? AND CAST(FECHA_DIN AS DATE) BETWEEN ? AND ? ORDER BY ID_DIN DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new AuditoriaDINARDAP(rs.getInt("id_din"),
                        rs.getInt("id_usu"),
                        rs.getString("ip_din"),
                        rs.getString("dato_din"),
                        rs.getInt("cat_din"),
                        rs.getTimestamp("fecha_din")));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listarAuditoriaDINARDAPUsuarioFecha | " + ex);
        }
        return listado;
    }

    public String getDescripcionCategoriaDINARDAP(int id) {
        String res = "SIN DESCRIPCIÓN";
        try {
            String sentencia;
            sentencia = "SELECT * FROM dinardap_categoria WHERE ID_CAT=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("DESC_CAT");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDescripcionCategoriaDINARDAP | " + ex);
        }
        return res;
    }

    public ArrayList<Aplicacion> getAplicaciones() {
        ArrayList<Aplicacion> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_aplicaciones";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Aplicacion(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("codigo"),
                                rs.getString("version_app")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAplicaciones | " + ex);
        }
        return res;
    }

    public int registrarVersion(Version ver) throws SQLException {
        String sql = "INSERT INTO version(ID_USU, ID_APL, TIPO_VER, DESC_VER) VALUES(?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, ver.getIdUsu());
        st.setInt(2, ver.getIdApp());
        st.setInt(3, ver.getTipo());
        st.setString(4, ver.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public Aplicacion getAplicacion(int idApp) {
        Aplicacion res = new Aplicacion();
        try {
            String sentencia = "SELECT * FROM v_aplicaciones WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idApp);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new Aplicacion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("version_app")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAplicacion | " + ex);
        }
        return res;
    }

    public boolean actualizarAdjuntoVersion(int idVer, String adjunto) {
        try {
            st = enlace.prepareStatement("UPDATE version SET ADJ_VER=? WHERE ID_VER=?");
            st.setString(1, adjunto);
            st.setInt(2, idVer);
            return (st.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println("actualizarAdjuntoVersion | " + ex);
        }
        return false;
    }

    public Version getVersionById(int idVer) {
        Version res = new Version();
        try {
            String sentencia = "SELECT * FROM version WHERE ID_VER=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idVer);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new Version(rs.getInt("ID_VER"),
                        rs.getInt("ID_USU"),
                        rs.getInt("ID_APL"),
                        rs.getString("ETIQ_VER"),
                        rs.getInt("TIPO_VER"),
                        rs.getString("DESC_VER"),
                        rs.getString("ADJ_VER"),
                        rs.getTimestamp("FECHA_VER")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionById | " + ex);
        }
        return res;
    }

    public ArrayList<Version> getVersionesByUsu(int idUsu) {
        ArrayList<Version> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM version WHERE ID_USU=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Version(rs.getInt("ID_VER"),
                        rs.getInt("ID_USU"),
                        rs.getInt("ID_APL"),
                        rs.getString("ETIQ_VER"),
                        rs.getInt("TIPO_VER"),
                        rs.getString("DESC_VER"),
                        rs.getString("ADJ_VER"),
                        rs.getTimestamp("FECHA_VER")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesByUsu | " + ex);
        }
        return res;
    }

    public ArrayList<Version> getVersionesRecientes() {
        ArrayList<Version> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM version WHERE (YEAR(FECHA_VER) = YEAR(CURRENT_DATE()) AND MONTH(FECHA_VER)= MONTH(CURRENT_DATE())) ORDER BY ID_VER DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Version(rs.getInt("ID_VER"),
                        rs.getInt("ID_USU"),
                        rs.getInt("ID_APL"),
                        rs.getString("ETIQ_VER"),
                        rs.getInt("TIPO_VER"),
                        rs.getString("DESC_VER"),
                        rs.getString("ADJ_VER"),
                        rs.getTimestamp("FECHA_VER")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesRecientes | " + ex);
        }
        return res;
    }

    public ArrayList<Version> getVersionesByFechas(java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Version> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM version WHERE CAST(FECHA_VER AS DATE) BETWEEN ? AND ? ORDER BY ID_VER DESC";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Version(rs.getInt("ID_VER"),
                        rs.getInt("ID_USU"),
                        rs.getInt("ID_APL"),
                        rs.getString("ETIQ_VER"),
                        rs.getInt("TIPO_VER"),
                        rs.getString("DESC_VER"),
                        rs.getString("ADJ_VER"),
                        rs.getTimestamp("FECHA_VER")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesByFechas | " + ex);
        }
        return res;
    }

    public ArrayList<Version> getVersionesByUsuarioFechas(int idUsu, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<Version> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM version WHERE ID_USU=? AND CAST(FECHA_VER AS DATE) BETWEEN ? AND ? ORDER BY ID_VER DESC";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Version(rs.getInt("ID_VER"),
                        rs.getInt("ID_USU"),
                        rs.getInt("ID_APL"),
                        rs.getString("ETIQ_VER"),
                        rs.getInt("TIPO_VER"),
                        rs.getString("DESC_VER"),
                        rs.getString("ADJ_VER"),
                        rs.getTimestamp("FECHA_VER")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesByUsuarioFechas | " + ex);
        }
        return res;
    }

    public ArrayList<organizacion> getUnidadesByDir(String nivelPadre) {
        ArrayList<organizacion> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM organizacion WHERE nivel_padre=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, nivelPadre);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new organizacion(rs.getInt("id_organizacion"),
                        rs.getString("nivel_padre"),
                        rs.getString("nivel_hijo"),
                        rs.getString("nombre")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUnidadesByDir | " + ex);
        }
        return res;
    }

    public boolean actualizarDireccionVacacion(int id, String cargo, String direccion, String jefe, String cargoJefe) {
        boolean res = false;
        try {
            st = enlace.prepareStatement("UPDATE permisovacaciones_usuario SET denominacion=?, unidad=?, jefe=?, cargo_jefe=? WHERE id_permiso=?");
            st.setString(1, cargo);
            st.setString(2, direccion);
            st.setString(3, jefe);
            st.setString(4, cargoJefe);
            st.setInt(5, id);
            st.executeUpdate();
            res = true;
            st.close();
        } catch (SQLException ex) {
            System.out.println("actualizarDireccionVacacion | " + ex);
        }
        return res;
    }

    public boolean actualizarDireccionHoras(int id, String direccion) {
        conexion_oracle oracle = new conexion_oracle();
        String codDir = oracle.consultarCodigoDireccion(direccion);
        int codJefe = oracle.consultarCodigoJefeDireccion(codDir);
        UsuarioIESS jefe = oracle.getUsuario(codJefe);
        String cargoJefe = oracle.consultarDenominacionUsuario(codJefe);
        boolean res = false;
        try {
            st = enlace.prepareStatement("UPDATE permisohoras_usuario SET unidad=?, jefe=?, cargo_jefe=? WHERE id_permiso=?");
            st.setString(1, direccion);
            st.setString(2, jefe.getNombres());
            st.setString(3, cargoJefe);
            st.setInt(4, id);
            st.executeUpdate();
            res = true;
            st.close();
        } catch (SQLException ex) {
            System.out.println("actualizarDireccionHoras | " + ex);
        }
        return res;
    }

    public ArrayList<Rol> getRoles() {
        ArrayList<Rol> res = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM rol";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Rol(rs.getInt("ID_ROL"),
                        rs.getString("DESCRIPCION"),
                        rs.getTimestamp("FECHA_CREACION")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRoles | " + ex);
        }
        return res;
    }

    public Rol getRol(int id) {
        Rol res = new Rol();
        try {
            String sentencia;
            sentencia = "SELECT * FROM rol WHERE id_rol=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res.setId(rs.getInt("ID_ROL"));
                res.setDescripcion(rs.getString("DESCRIPCION"));
                res.setCreacion(rs.getTimestamp("FECHA_CREACION"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRol | " + ex);
        }
        return res;
    }

    public int registrarRol(Rol rol) throws SQLException {
        String sql = "INSERT INTO rol(DESCRIPCION) VALUES(?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, rol.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean eliminarRol(int id) {
        boolean res = true;
        String sql = "DELETE FROM rol WHERE id_rol=?";
        try {
            st = enlace.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            res = false;
            System.out.println("eliminarRol | " + e);
        }
        return res;
    }

    public boolean registroRolUsuario(int admin, int usu, int rol) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO tipo_usuario(id_usuario, id_rol, id_admin) VALUES(?,?,?)");
        st.setInt(1, usu);
        st.setInt(2, rol);
        st.setInt(3, admin);
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<modulo> getAllModulos() {
        ArrayList<modulo> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT id_modulo, descripcion FROM modulo WHERE estado=1 ORDER BY orden ASC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                modulo p = new modulo();
                p.setId_modulo(rs.getInt("id_modulo"));
                p.setDescripcion(rs.getString("descripcion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAllModulos | " + ex);
        }
        return listado;
    }

    public boolean verificarRolTieneModulo(int idRol, int idMod) {
        boolean res = false;
        try {
            String sentencia;
            sentencia = "SELECT DISTINCT(modulo.id_modulo) FROM modulo INNER JOIN modulo_usuario ON modulo.id_modulo = modulo_usuario.id_modulo WHERE modulo_usuario.id_rol=? AND modulo.id_modulo=? AND modulo.estado=1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idRol);
            st.setInt(2, idMod);
            rs = st.executeQuery();
            while (rs.next()) {
                res = true;
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("verificarRolTieneModulo | " + ex);
        }
        return res;
    }

    public boolean registrarModuloParaRol(int rol, int mod) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO modulo_usuario(id_rol, id_modulo) VALUES(?,?)");
        st.setInt(1, rol);
        st.setInt(2, mod);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean eliminarModulosNoElegidosParaRol(int rol, String[] modulos) throws SQLException {
        String sentencia = "DELETE FROM modulo_usuario WHERE id_rol=? AND id_modulo NOT IN (?";
        int nMod = 1;
        while (nMod <= modulos.length) {
            if (nMod > 1) {
                sentencia += ", ?";
            }
            nMod++;
        }
        sentencia += ")";
        st = enlace.prepareStatement(sentencia);
        st.setInt(1, rol);
        nMod = 2;
        for (String m : modulos) {
            st.setInt(nMod, Integer.parseInt(m));
            nMod++;
        }
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean eliminarRolesNoElegidosParaUsuario(int idAdmin, int idUsu, String[] roles) throws SQLException {
        ArrayList<Integer> rolesABorrar = new ArrayList();
        String sentencia = "SELECT tipo_usuario.id_rol AS RES FROM tipo_usuario INNER JOIN rol ON tipo_usuario.id_rol=rol.id_rol WHERE tipo_usuario.id_usuario=? AND tipo_usuario.id_rol NOT IN (?";
        int nRol = 1;
        while (nRol <= roles.length) {
            if (nRol > 1) {
                sentencia += ", ?";
            }
            nRol++;
        }
        sentencia += ")";
        st = enlace.prepareStatement(sentencia);
        st.setInt(1, idUsu);
        nRol = 2;
        for (String r : roles) {
            st.setInt(nRol, Integer.parseInt(r));
            nRol++;
        }
        rs = st.executeQuery();
        while (rs.next()) {
            rolesABorrar.add(rs.getInt("RES"));
        }
        st.close();
        rs.close();
        if (!rolesABorrar.isEmpty()) {
            sentencia = "";
            nRol = 1;
            while (nRol <= rolesABorrar.size()) {
                sentencia += "INSERT INTO rol_borrado(id_usu, id_rol, id_admin) VALUES(?, ?, ?);";
                nRol++;
            }
            st = enlace.prepareStatement(sentencia);
            nRol = 1;
            for (Integer r : rolesABorrar) {
                st.setInt(nRol, idUsu);
                st.setInt(nRol + 1, r);
                st.setInt(nRol + 2, idAdmin);
                nRol += 3;
            }
            st.executeUpdate();
            st.close();
        }
        sentencia = "DELETE FROM tipo_usuario WHERE id_usuario=? AND id_rol NOT IN (?";
        nRol = 1;
        while (nRol <= roles.length) {
            if (nRol > 1) {
                sentencia += ", ?";
            }
            nRol++;
        }
        sentencia += ")";
        st = enlace.prepareStatement(sentencia);
        st.setInt(1, idUsu);
        nRol = 2;
        for (String r : roles) {
            st.setInt(nRol, Integer.parseInt(r));
            nRol++;
        }
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean verificarPermisosVacaciones(int idUsu) {
        boolean res = false;
        try {
            String sentencia = "SELECT (\n"
                    + "SELECT COUNT(*) \n"
                    + "FROM permisohoras_usuario \n"
                    + "WHERE id_motivo=1 AND id_estado IN (0,1) AND id_usuario=?\n"
                    + ")+(\n"
                    + "SELECT COUNT(*) \n"
                    + "FROM permisovacaciones_usuario \n"
                    + "WHERE estado IN (0,1) AND id_usuario=?\n"
                    + ") AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                if (rs.getInt("RES") > 0) {
                    res = true;
                }
                break;
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            res = true;
            System.out.println("verificarPermisosVacaciones | " + ex);
        }
        return res;
    }

    public boolean eliminarViatico(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM viatico WHERE id_viatico=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarViatico | " + ex);
        }
        return false;
    }

    public ArrayList<viatico> listadoViaticosUsuarioResponsableEstado(int id_usuario, int estado) {
        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM viatico INNER JOIN firma_viatico ON viatico.id_viatico=firma_viatico.id_viatico WHERE firma_viatico.id_responsable=? AND viatico.id_estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("viatico.id_viatico"));
                elemento.setId_tipo(rs.getInt("viatico.id_tipo"));
                elemento.setId_usuario(rs.getInt("viatico.id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("viatico.descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("viatico.tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("viatico.numero_cuenta"));
                elemento.setNombre_banco(rs.getString("viatico.nombre_banco"));
                elemento.setId_estado(rs.getInt("viatico.id_estado"));
                elemento.setFecha(rs.getTimestamp("viatico.fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoViaticosUsuarioResponsableEstado | " + ex);
        }
        return listado;
    }

    public ArrayList<viatico> listadoViaticosUsuarioAutoridadEstado(int id_usuario, int estado) {
        ArrayList<viatico> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM viatico INNER JOIN firma_viatico ON viatico.id_viatico=firma_viatico.id_viatico WHERE firma_viatico.id_autoridad=? AND viatico.id_estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                viatico elemento = new viatico();
                elemento.setId_viatico(rs.getInt("viatico.id_viatico"));
                elemento.setId_tipo(rs.getInt("viatico.id_tipo"));
                elemento.setId_usuario(rs.getInt("viatico.id_usuario"));
                elemento.setDescripcion_actividad(rs.getString("viatico.descripcion_actividad"));
                elemento.setTipo_cuenta(rs.getString("viatico.tipo_cuenta"));
                elemento.setNumero_cuenta(rs.getString("viatico.numero_cuenta"));
                elemento.setNombre_banco(rs.getString("viatico.nombre_banco"));
                elemento.setId_estado(rs.getInt("viatico.id_estado"));
                elemento.setFecha(rs.getTimestamp("viatico.fecha"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoViaticosUsuarioAutoridadEstado | " + ex);
        }
        return listado;
    }

    public ArrayList<MotivoAnulacion> listadoMotivosAnulacion() {
        ArrayList<MotivoAnulacion> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM motivo_anuvac";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new MotivoAnulacion(
                        rs.getInt("id_motivo"),
                        rs.getString("motivo")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoMotivosAnulacion | " + ex);
        }
        return listado;
    }

    public int getVacacionPendiente(int idUsuario) {
        int res = 0;
        try {
            String sentencia = "SELECT vaca.id_permiso AS RES FROM permisovacaciones_usuario vaca, anulacion_permisovacaciones anula WHERE vaca.id_permiso=anula.id_permiso AND vaca.reemplazado=0 AND anula.id_motivo=1 AND anula.id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVacacionPendiente | " + ex);
        }
        return res;
    }

    public int getPermisoHorasPendiente(int idUsuario) {
        int res = 0;
        try {
            String sentencia = "SELECT per.id_permiso AS RES FROM permisohoras_usuario per, anulacion_permisohoras anula WHERE per.id_permiso=anula.id_solicitud AND per.reemplazado=0 AND anula.id_anula=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisoHorasPendiente | " + ex);
        }
        return res;
    }

    public boolean reemplazarVacacion(int id) {
        try {
            st = enlace.prepareStatement("UPDATE permisovacaciones_usuario SET reemplazado=1 WHERE id_permiso=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("reemplazarVacacion | " + ex);
        }
        return false;
    }

    public boolean reemplazarPermisoHoras(int id) {
        try {
            st = enlace.prepareStatement("UPDATE permisohoras_usuario SET reemplazado=1 WHERE id_permiso=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("reemplazarPermisoHoras | " + ex);
        }
        return false;
    }

    public int getDiferenciaDias(java.sql.Date fecha) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT DATEDIFF(NOW(), ?) AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fecha);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDiferenciaDias | " + ex);
        }
        return res;
    }

    public int getDiferenciaDiasHabiles(java.sql.Date fecha) {
        int res = 0;
        try {
            String sentencia;
            sentencia = "SELECT IF(CURDATE()>?, -1*contarDiasHabiles(?, CURDATE()), contarDiasHabiles(CURDATE(), ?)) AS RES";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fecha);
            st.setDate(2, fecha);
            st.setDate(3, fecha);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDiferenciaDiasHabiles | " + ex);
        }
        return res;
    }

    public ArrayList<ApruebaVacas> getApruebaVacas() {
        ArrayList<ApruebaVacas> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM aprueba_vacas";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ApruebaVacas(
                                rs.getInt("id_apvac"),
                                rs.getString("codigo_cargo"),
                                rs.getString("cargo"),
                                rs.getString("descrip"),
                                rs.getBoolean("estado")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getApruebaVacas | " + ex);
        }
        return listado;
    }

    public String getCodigoCargo(int id) {
        String res = "";
        try {
            String sentencia = "SELECT * FROM aprueba_vacas WHERE id_apvac=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("codigo_cargo");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCargo | " + ex);
        }
        return res;
    }

    public boolean setFirmaPerVac(int id) throws SQLException {
        conexion_oracle oracle = new conexion_oracle();
        String codigoCargo = getCodigoCargo(id);
        String cargo = oracle.getNombreCargo(codigoCargo);
        if (cargo.equals("")) {
            throw new SQLException("Error con el cargo");
        }
        String sentencia = "UPDATE aprueba_vacas SET estado=0;\n"
                + "UPDATE aprueba_vacas SET estado=1, descrip=? WHERE id_apvac=?";
        st = enlace.prepareStatement(sentencia);
        st.setString(1, cargo);
        st.setInt(2, id);
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<PermisoManual> getPermisosManuales(int estado) {
        ArrayList<PermisoManual> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permiso_manual WHERE estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new PermisoManual(
                                rs.getInt("id_permiso"),
                                rs.getInt("id_admin"),
                                rs.getInt("codigo_usu"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getInt("dias_habiles"),
                                rs.getInt("fines_semana"),
                                rs.getString("hora_inicio"),
                                rs.getString("hora_fin"),
                                rs.getInt("horas"),
                                rs.getInt("minutos"),
                                rs.getString("observacion"),
                                rs.getString("denominacion"),
                                rs.getString("direccion"),
                                rs.getString("jefe"),
                                rs.getString("cargo_jefe"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisosManuales(estado) | " + ex);
        }
        return listado;
    }

    public ArrayList<PermisoManual> getPermisosManuales(int estado, int codUsu) {
        ArrayList<PermisoManual> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM permiso_manual WHERE estado=? AND codigo_usu=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            st.setInt(2, codUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new PermisoManual(
                                rs.getInt("id_permiso"),
                                rs.getInt("id_admin"),
                                rs.getInt("codigo_usu"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getInt("dias_habiles"),
                                rs.getInt("fines_semana"),
                                rs.getString("hora_inicio"),
                                rs.getString("hora_fin"),
                                rs.getInt("horas"),
                                rs.getInt("minutos"),
                                rs.getString("observacion"),
                                rs.getString("denominacion"),
                                rs.getString("direccion"),
                                rs.getString("jefe"),
                                rs.getString("cargo_jefe"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisosManuales(estado,codUsu) | " + ex);
        }
        return listado;
    }

    public boolean registroAnulacionManual(AnulacionManual a) throws SQLException {
        st = enlace.prepareStatement("INSERT INTO anulacion_manual(id_permiso,id_anula,id_motivo) VALUES(?,?,?)");
        st.setInt(1, a.getIdPermiso());
        st.setInt(2, a.getIdAnula());
        st.setInt(3, a.getIdMotivo());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarEstadoPermisoManual(int estado, int idPermiso) {
        try {
            st = enlace.prepareStatement("UPDATE permiso_manual SET estado=? WHERE id_permiso=?");
            st.setInt(1, estado);
            st.setInt(2, idPermiso);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public AnulacionManual obtenerAnulacionManual(int id_permiso) {
        AnulacionManual elemento = new AnulacionManual();
        try {
            String sentencia = "SELECT * FROM anulacion_manual anula, motivo_anuvac moti WHERE anula.id_motivo=moti.id_motivo AND anula.id_permiso=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_permiso);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento = new AnulacionManual(
                        rs.getInt("anula.id_anulacion"),
                        rs.getInt("anula.id_permiso"),
                        rs.getInt("anula.id_anula"),
                        rs.getInt("anula.id_motivo"),
                        rs.getString("moti.motivo"),
                        rs.getTimestamp("anula.creacion")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("obtenerAnulacionManual | " + ex);
        }
        return elemento;
    }

    public boolean eliminarPermisoManual(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM permiso_manual WHERE id_permiso=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarPermisoManual | " + ex);
        }
        return false;
    }

    public boolean verificarUsuarioEncuesta(int idUsu) {
        boolean res = true;
//        try {
//            String sentencia = "SELECT * FROM encuestado WHERE id_usuario=?";
//            st = enlace.prepareStatement(sentencia);
//            st.setInt(1, idUsu);
//            rs = st.executeQuery();
//            while (rs.next()) {
//                res = true;
//            }
//            st.close();
//            rs.close();
//        } catch (Exception ex) {
//            System.out.println("verificarUsuarioEncuesta | " + ex);
//        }
        return res;
    }

    public String getEncuestaAbierta() {
        String res = "";
        try {
            String sentencia = "SELECT * FROM encuesta WHERE estado=1 LIMIT 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("enlace");
            }
            st.close();
            rs.close();
        } catch (Exception ex) {
            System.out.println("getEncuestaAbierta | " + ex);
        }
        return res;
    }

    public boolean registrarEncuestado(int idUsu) {
        try {
            String sentencia = "INSERT INTO encuestado(id_usuario) VALUES(?)";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e) {
            System.out.println("registrarEncuestado | " + e);
            return false;
        }
    }

    public int registrarCapacitacion(Capacitacion c) throws SQLException {
        String sql = "INSERT INTO capacitacion(id_usu, tema, enlace, descripcion) VALUES(?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, c.getIdUsuario());
        st.setString(2, c.getTema());
        st.setString(3, c.getEnlace());
        st.setString(4, c.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean cambiarEstadoCapacitacion(int idCap, int estado) {
        try {
            st = enlace.prepareStatement("UPDATE capacitacion SET estado=? WHERE id=?");
            st.setInt(1, estado);
            st.setInt(2, idCap);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("cambiarEstadoCapacitacion | " + ex);
        }
        return false;
    }

    public int registrarHorarioCapacitacion(int idCap, java.sql.Date fecha, String horaIni, String horaFin) throws SQLException {
        String sql = "INSERT INTO horario_cap(id_cap, fecha, hora_ini, hora_fin) VALUES(?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, idCap);
        st.setDate(2, fecha);
        st.setString(3, horaIni);
        st.setString(4, horaFin);
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public boolean actualizarAdjuntoCapacitacion(int idCap, String adjunto) {
        try {
            st = enlace.prepareStatement("UPDATE capacitacion SET adjunto=? WHERE id=?");
            st.setString(1, adjunto);
            st.setInt(2, idCap);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("actualizarAdjuntoCapacitacion | " + ex);
        }
        return false;
    }

    public boolean actualizarInformeCapacitacion(int idCap, String informe) {
        try {
            st = enlace.prepareStatement("UPDATE capacitacion SET informe=? WHERE id=?");
            st.setString(1, informe);
            st.setInt(2, idCap);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("actualizarInformeCapacitacion | " + ex);
        }
        return false;
    }

    public Capacitacion getCapacitacion(int id) {
        Capacitacion res = new Capacitacion();
        try {
            String sentencia = "SELECT * FROM v_capacitaciones WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new Capacitacion(
                        rs.getInt("id"),
                        rs.getInt("id_usu"),
                        rs.getInt("estado"),
                        rs.getString("usuario"),
                        rs.getDate("fecha_ini"),
                        rs.getDate("fecha_fin"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion"),
                        rs.getString("satisfaccion_desc"),
                        rs.getInt("satisfaccion_num"),
                        rs.getString("enlace")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitacion(id) | " + ex);
        }
        return res;
    }

    public Horario getHorario(int id, int idUsu) {
        Horario res = new Horario();
        try {
            String sentencia = "SELECT * FROM v_horarios_cap WHERE id_hor=? AND id_facilitador=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            st.setInt(2, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new Horario(
                        rs.getInt("id_hor"),
                        rs.getInt("id_cap"),
                        rs.getDate("fecha"),
                        rs.getString("hora_ini"),
                        rs.getString("hora_fin")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getHorario(id, idUsu) | " + ex);
        }
        return res;
    }

    public ArrayList<Capacitacion> getCapacitaciones(int idUsu, int estado) {
        ArrayList<Capacitacion> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_capacitaciones WHERE id_usu=? AND estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                Capacitacion c = new Capacitacion(
                        rs.getInt("id"),
                        rs.getInt("id_usu"),
                        rs.getInt("estado"),
                        rs.getString("usuario"),
                        rs.getDate("fecha_ini"),
                        rs.getDate("fecha_fin"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion"),
                        rs.getString("satisfaccion_desc"),
                        rs.getInt("satisfaccion_num"),
                        rs.getString("enlace")
                );
                c.setHorarios(rs.getInt("horarios"));
                res.add(c);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitaciones(idUsu, estado) | " + ex);
        }
        return res;
    }

    public ArrayList<Capacitacion> getCapacitaciones(int estado) {
        ArrayList<Capacitacion> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_capacitaciones WHERE estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                Capacitacion c = new Capacitacion(
                        rs.getInt("id"),
                        rs.getInt("id_usu"),
                        rs.getInt("estado"),
                        rs.getString("usuario"),
                        rs.getDate("fecha_ini"),
                        rs.getDate("fecha_fin"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion"),
                        rs.getString("satisfaccion_desc"),
                        rs.getInt("satisfaccion_num"),
                        rs.getString("enlace")
                );
                c.setHorarios(rs.getInt("horarios"));
                res.add(c);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitaciones(estado) | " + ex);
        }
        return res;
    }

    public ArrayList<Horario> getHorariosCapacitacion(int idCap) {
        ArrayList<Horario> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_horarios_cap WHERE id_cap=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idCap);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Horario(
                                rs.getInt("id_hor"),
                                rs.getInt("id_cap"),
                                rs.getDate("fecha"),
                                rs.getString("hora_ini"),
                                rs.getString("hora_fin")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getHorariosCapacitacion(idCap) | " + ex);
        }
        return res;
    }

    public ArrayList<InscritoCap> getInscritos(int idHorario) {
        ArrayList<InscritoCap> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_inscritos_cap WHERE id_hor=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idHorario);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new InscritoCap(
                                rs.getInt("id_inscrito"),
                                rs.getInt("id_inscripcion"),
                                rs.getString("inscrito"),
                                rs.getString("unidad_inscrito"),
                                rs.getInt("asistencia"),
                                rs.getInt("satisfaccion"),
                                rs.getString("satisfaccion_desc"),
                                rs.getDate("satisfaccion_fec")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getInscritos(idHorario) | " + ex);
        }
        return res;
    }

    public ArrayList<InscritoCap> getAsistentes(int idCap) {
        ArrayList<InscritoCap> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_inscritos_cap WHERE id_cap=? AND asistencia IS NOT NULL";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idCap);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new InscritoCap(
                                rs.getInt("id_inscrito"),
                                rs.getInt("id_inscripcion"),
                                rs.getString("inscrito"),
                                rs.getString("unidad_inscrito"),
                                rs.getInt("asistencia"),
                                rs.getInt("satisfaccion"),
                                rs.getString("satisfaccion_desc"),
                                rs.getDate("satisfaccion_fec")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAsistentes(idCap) | " + ex);
        }
        return res;
    }

    public ArrayList<Capacitacion> getCapacitacionesDisp(int idUsu) {
        ArrayList<Capacitacion> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_capacitaciones WHERE estado=1 AND id NOT IN(SELECT id_cap FROM v_inscritos_cap WHERE id_inscrito=?)";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                Capacitacion c = new Capacitacion(
                        rs.getInt("id"),
                        rs.getInt("id_usu"),
                        rs.getInt("estado"),
                        rs.getString("usuario"),
                        rs.getDate("fecha_ini"),
                        rs.getDate("fecha_fin"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion"),
                        rs.getString("satisfaccion_desc"),
                        rs.getInt("satisfaccion_num"),
                        rs.getString("enlace")
                );
                c.setHorarios(rs.getInt("horarios"));
                res.add(c);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitacionesDisp(idUsu) | " + ex);
        }
        return res;
    }

    public ArrayList<Capacitacion> getCapacitacionesVigentes(int idUsu) {
        ArrayList<Capacitacion> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_inscritos_cap WHERE id_inscrito=? AND estado=1";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                Capacitacion c = new Capacitacion(
                        rs.getInt("id_cap"),
                        rs.getInt("id_facilitador"),
                        rs.getInt("estado"),
                        rs.getString("facilitador"),
                        rs.getDate("fecha"),
                        rs.getDate("fecha"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion_cap"),
                        rs.getString("satisfaccion_cap_desc"),
                        rs.getInt("satisfaccion_cap_num"),
                        rs.getString("enlace")
                );
                c.setHoraIni(rs.getString("hora_ini"));
                c.setHoraFin(rs.getString("hora_fin"));
                res.add(c);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitacionesVigentes(idUsu) | " + ex);
        }
        return res;
    }

    public ArrayList<Capacitacion> getCapacitacionesAprobadas(int idUsu) {
        ArrayList<Capacitacion> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_inscritos_cap WHERE id_inscrito=? AND asistencia != 0";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                Capacitacion c = new Capacitacion(
                        rs.getInt("id_cap"),
                        rs.getInt("id_facilitador"),
                        rs.getInt("estado"),
                        rs.getString("facilitador"),
                        rs.getDate("fecha"),
                        rs.getDate("fecha"),
                        rs.getString("tema"),
                        rs.getString("descripcion"),
                        rs.getString("adjunto"),
                        rs.getString("informe"),
                        rs.getInt("inscritos"),
                        rs.getInt("asistentes"),
                        rs.getDouble("satisfaccion_cap"),
                        rs.getString("satisfaccion_cap_desc"),
                        rs.getInt("satisfaccion_cap_num"),
                        rs.getString("enlace")
                );
                c.setHoraIni(rs.getString("hora_ini"));
                c.setHoraFin(rs.getString("hora_fin"));
                c.setIdHorario(rs.getInt("id_hor"));
                c.setAsistencia(rs.getInt("asistencia"));
                c.setSatisfaccionAsistente(rs.getInt("satisfaccion"));
                c.setSatisfaccionAsistenteDesc(rs.getString("satisfaccion_desc"));
                c.setSatisfaccionAsistenteFec(rs.getTimestamp("satisfaccion_fec"));
                res.add(c);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getCapacitacionesAprobadas(idUsu) | " + ex);
        }
        return res;
    }

    public boolean eliminarCapacitacion(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM capacitacion WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarCapacitacion | " + ex);
        }
        return false;
    }

    public boolean eliminarHorarioCapacitacion(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM horario_cap WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarHorarioCapacitacion | " + ex);
        }
        return false;
    }

    public int inscribirCapacitacion(int idHor, int idUsu) throws SQLException {
        String sql = "INSERT INTO inscripcion_cap(id_hor, id_usu) VALUES(?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, idHor);
        st.setInt(2, idUsu);
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int calificarCapacitacion(int idAsistencia, int satisfaccion) throws SQLException {
        String sql = "INSERT INTO satisfaccion_cap(id_asi, id_sat_cat) VALUES(?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, idAsistencia);
        st.setInt(2, satisfaccion);
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public ArrayList<SatisfaccionCap> listadoSatisfaccionCap() {
        ArrayList<SatisfaccionCap> res = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM satisfaccion_cap_cat";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new SatisfaccionCap(
                                rs.getInt("id"),
                                rs.getString("descripcion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoSatisfaccionCap | " + ex);
        }
        return res;
    }

    public boolean registrarAsistencia(int idHor, ArrayList<InscritoCap> inscritos, ArrayList<InscritoCap> asistentes) throws SQLException {
        String sql = "";
        for (InscritoCap i : inscritos) {
            if (asistentes.contains(i)) {
                sql += "REPLACE INTO asistencia_cap(id_ins) VALUES(?);";
            } else {
                sql += "DELETE FROM asistencia_cap WHERE id_ins=?;";
            }
        }
        st = enlace.prepareStatement(sql);
        int num = 1;
        for (InscritoCap i : inscritos) {
            st.setInt(num++, i.getIdInscripcion());;
        }
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<TipoManual> getTiposManuales() {
        ArrayList<TipoManual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM tipo_manual";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new TipoManual(
                                rs.getInt("id"),
                                rs.getString("descripcion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTiposManuales | " + ex);
        }
        return res;
    }

    public ArrayList<Manual> getManuales() {
        ArrayList<Manual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_manuales";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Manual(
                                rs.getInt("id"),
                                rs.getInt("id_tipo"),
                                rs.getString("tipo"),
                                rs.getString("codigo"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getString("version")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getManuales | " + ex);
        }
        return res;
    }

    public int registrarManual(Manual m) throws SQLException {
        String sql = "INSERT INTO manual(id_tipo, titulo, descripcion) VALUES(?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, m.getIdTipo());
        st.setString(2, m.getTitulo());
        st.setString(3, m.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public int registrarVersionManual(VersionManual v) throws SQLException {
        String sql = "INSERT INTO version_manual(id_usuario, id_manual, titulo, descripcion) VALUES(?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, v.getIdUsuario());
        st.setInt(2, v.getIdManual());
        st.setString(3, v.getTitulo());
        st.setString(4, v.getDescripcion());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public ArrayList<VersionManual> getVersionesManuales() {
        ArrayList<VersionManual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_versiones_man_recientes";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new VersionManual(
                                rs.getInt("id"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario"),
                                rs.getInt("id_manual"),
                                rs.getString("manual"),
                                rs.getString("tipo_manual"),
                                rs.getString("version"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesManuales | " + ex);
        }
        return res;
    }

    public ArrayList<VersionManual> getVersionesManuales(int idUsu) {
        ArrayList<VersionManual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_versiones_man WHERE id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new VersionManual(
                                rs.getInt("id"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario"),
                                rs.getInt("id_manual"),
                                rs.getString("manual"),
                                rs.getString("tipo_manual"),
                                rs.getString("version"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesManuales(idUsu) | " + ex);
        }
        return res;
    }

    public ArrayList<VersionManual> getVersionesManuales(java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<VersionManual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_versiones_man WHERE DATE(creacion) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new VersionManual(
                                rs.getInt("id"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario"),
                                rs.getInt("id_manual"),
                                rs.getString("manual"),
                                rs.getString("tipo_manual"),
                                rs.getString("version"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesManuales(fechaIni, fechaFin) | " + ex);
        }
        return res;
    }

    public ArrayList<VersionManual> getVersionesManuales(int idUsu, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        ArrayList<VersionManual> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_versiones_man WHERE id_usuario=? AND DATE(creacion) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new VersionManual(
                                rs.getInt("id"),
                                rs.getInt("id_usuario"),
                                rs.getString("usuario"),
                                rs.getInt("id_manual"),
                                rs.getString("manual"),
                                rs.getString("tipo_manual"),
                                rs.getString("version"),
                                rs.getString("titulo"),
                                rs.getString("descripcion"),
                                rs.getString("adjunto"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getVersionesManuales(idUsu, fechaIni, fechaFin) | " + ex);
        }
        return res;
    }

    public boolean actualizarAdjuntoVersionManual(int idVer, String adjunto) {
        try {
            st = enlace.prepareStatement("UPDATE version_manual SET adjunto=? WHERE id=?");
            st.setString(1, adjunto);
            st.setInt(2, idVer);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("actualizarAdjuntoVersionManual | " + ex);
        }
        return false;
    }

    public boolean eliminarVersionManual(int id) {
        try {
            st = enlace.prepareStatement("DELETE FROM version_manual WHERE id=?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("eliminarVersionManual | " + ex);
        }
        return false;
    }

    public ArrayList<UsuarioConfig> getUsers() {
        ArrayList<UsuarioConfig> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_usuarios";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new UsuarioConfig(
                                rs.getInt("id"),
                                rs.getInt("codigo"),
                                rs.getString("cedula"),
                                rs.getString("nombres"),
                                rs.getString("correo"),
                                rs.getDate("fecha_nacimiento"),
                                rs.getString("cargo"),
                                rs.getString("codigo_direccion"),
                                rs.getString("direccion"),
                                rs.getBoolean("activo"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsers | " + ex);
        }
        return res;
    }

    public ArrayList<UsuarioConfig> getUsersDireccion(final UsuarioConfig usuario) {
        ArrayList<UsuarioConfig> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_usuarios WHERE activo AND id!=? AND codigo_direccion=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, usuario.getId());
            st.setString(2, usuario.getCodigo_direccion());
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new UsuarioConfig(
                                rs.getInt("id"),
                                rs.getInt("codigo"),
                                rs.getString("cedula"),
                                rs.getString("nombres"),
                                rs.getString("correo"),
                                rs.getDate("fecha_nacimiento"),
                                rs.getString("cargo"),
                                rs.getString("codigo_direccion"),
                                rs.getString("direccion"),
                                rs.getBoolean("activo"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsersDireccion(usuario) | " + ex);
        }
        return res;
    }

    public ArrayList<UsuarioConfig> getUsersDirectores(final UsuarioConfig usuario) {
        ArrayList<UsuarioConfig> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_usuarios_roles WHERE activo AND rol=4 AND cargo NOT LIKE '%ALCALDE%' AND cargo NOT LIKE '%CONCEJAL%' AND id!=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, usuario.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new UsuarioConfig(
                                rs.getInt("id"),
                                rs.getInt("codigo"),
                                rs.getString("cedula"),
                                rs.getString("nombres"),
                                rs.getString("correo"),
                                rs.getDate("fecha_nacimiento"),
                                rs.getString("cargo"),
                                rs.getString("codigo_direccion"),
                                rs.getString("direccion"),
                                rs.getBoolean("activo"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsersDirectores(usuario) | " + ex);
        }
        return res;
    }

    public UsuarioConfig getUser(final int id_usuario) {
        UsuarioConfig res = new UsuarioConfig();
        try {
            final String sentencia = "SELECT * FROM v_usuarios WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new UsuarioConfig(
                        rs.getInt("id"),
                        rs.getInt("codigo"),
                        rs.getString("cedula"),
                        rs.getString("tra_nom"),
                        rs.getString("correo"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("cargo"),
                        rs.getString("codigo_direccion"),
                        rs.getString("direccion"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creacion")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUser | " + ex);
        }
        return res;
    }

    public boolean ActualizarAdjuntoSoporte(int id_solicitud, String path) throws SQLException {
        st = enlace.prepareStatement("UPDATE solicitud_soporte SET adjunto=? WHERE id_solicitud=?");
        st.setString(1, path);
        st.setInt(2, id_solicitud);
        st.executeUpdate();
        st.close();
        return true;
    }

    public int registrarGastoPersonal(GastosPersonales g) throws SQLException {
        String sql = "INSERT INTO gasto_personal(id_usuario,c103,c104,c105,c106,c107,c108,c109,c110,c111,c112,c113) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, g.getUsuario().getId_usuario());
        st.setDouble(2, g.getC103());
        st.setDouble(3, g.getC104());
        st.setDouble(4, g.getC105());
        st.setDouble(5, g.getC106());
        st.setDouble(6, g.getC107());
        st.setDouble(7, g.getC108());
        st.setDouble(8, g.getC109());
        st.setDouble(9, g.getC110());
        st.setDouble(10, g.getC111());
        st.setDouble(11, g.getC112());
        st.setDouble(12, g.getC113());
        st.executeUpdate();
        rs = st.getGeneratedKeys();
        rs.next();
        int res = rs.getInt(1);
        st.close();
        rs.close();
        return res;
    }

    public void ActualizarGastoPersonal(GastosPersonales g) throws SQLException {
        st = enlace.prepareStatement("UPDATE gasto_personal SET estado=?,adjunto=?,c103=?,c104=?,c105=?,c106=?,c107=?,c108=?,c109=?,c110=?,c111=?,c112=?,c113=? WHERE id_gasto=?");
        st.setInt(1, g.getEstado());
        st.setString(2, g.getAdjunto());
        st.setDouble(3, g.getC103());
        st.setDouble(4, g.getC104());
        st.setDouble(5, g.getC105());
        st.setDouble(6, g.getC106());
        st.setDouble(7, g.getC107());
        st.setDouble(8, g.getC108());
        st.setDouble(9, g.getC109());
        st.setDouble(10, g.getC110());
        st.setDouble(11, g.getC111());
        st.setDouble(12, g.getC112());
        st.setDouble(13, g.getC113());
        st.setInt(14, g.getId());
        st.executeUpdate();
        st.close();
    }

    public void EliminarGastoPersonal(GastosPersonales g) throws SQLException {
        st = enlace.prepareStatement("DELETE FROM gasto_personal WHERE id_gasto=?");
        st.setInt(1, g.getId());
        st.executeUpdate();
        st.close();
    }

    public ArrayList<GastosPersonales> GetGastosPersonales(int idUsu, int estado) {
        ArrayList<GastosPersonales> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_gastos_personales WHERE id_usuario=? AND estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsu);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                GastosPersonales g = new GastosPersonales();
                g.setId(rs.getInt("id_gasto"));
                usuario u = new usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre_usuario"));
                g.setUsuario(u);
                g.setEstado(rs.getInt("estado"));
                g.setC103(rs.getDouble("c103"));
                g.setC104(rs.getDouble("c104"));
                g.setC105(rs.getDouble("c105"));
                g.setC106(rs.getDouble("c106"));
                g.setC107(rs.getDouble("c107"));
                g.setC108(rs.getDouble("c108"));
                g.setC109(rs.getDouble("c109"));
                g.setC110(rs.getDouble("c110"));
                g.setC111(rs.getDouble("c111"));
                g.setC112(rs.getDouble("c112"));
                g.setC113(rs.getDouble("c113"));
                g.setAdjunto(rs.getString("adjunto"));
                g.setCreacion(rs.getTimestamp("creacion"));
                if (estado == 1) {
                    usuario aprueba = new usuario();
                    aprueba.setNombre(rs.getString("aprueba"));
                    g.setAprobacion(new AprobacionGastosPersonales(
                            aprueba,
                            rs.getTimestamp("fecha_aprobacion")
                    ));
                } else if (estado == 2) {
                    usuario valida = new usuario();
                    valida.setNombre(rs.getString("valida"));
                    g.setValidacion(new ValidacionGastosPersonales(
                            valida,
                            rs.getTimestamp("fecha_validacion")
                    ));
                } else if (estado == 3) {
                    usuario rechaza = new usuario();
                    rechaza.setNombre(rs.getString("rechaza"));
                    g.setRechazo(new RechazoGastosPersonales(
                            rechaza,
                            rs.getString("motivo_rechazo"),
                            rs.getTimestamp("fecha_rechazo")
                    ));
                }
                res.add(g);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("GetGastosPersonales(idUsu, estado) | " + ex);
        }
        return res;
    }

    public ArrayList<GastosPersonales> GetGastosPersonales(int estado) {
        ArrayList<GastosPersonales> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_gastos_personales WHERE estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                GastosPersonales g = new GastosPersonales();
                g.setId(rs.getInt("id_gasto"));
                usuario u = new usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre_usuario"));
                g.setUsuario(u);
                g.setEstado(rs.getInt("estado"));
                g.setC103(rs.getDouble("c103"));
                g.setC104(rs.getDouble("c104"));
                g.setC105(rs.getDouble("c105"));
                g.setC106(rs.getDouble("c106"));
                g.setC107(rs.getDouble("c107"));
                g.setC108(rs.getDouble("c108"));
                g.setC109(rs.getDouble("c109"));
                g.setC110(rs.getDouble("c110"));
                g.setC111(rs.getDouble("c111"));
                g.setC112(rs.getDouble("c112"));
                g.setC113(rs.getDouble("c113"));
                g.setAdjunto(rs.getString("adjunto"));
                g.setCreacion(rs.getTimestamp("creacion"));
                if (estado == 1) {
                    usuario aprueba = new usuario();
                    aprueba.setNombre(rs.getString("aprueba"));
                    g.setAprobacion(new AprobacionGastosPersonales(
                            aprueba,
                            rs.getTimestamp("fecha_aprobacion")
                    ));
                } else if (estado == 2) {
                    usuario valida = new usuario();
                    valida.setNombre(rs.getString("valida"));
                    g.setValidacion(new ValidacionGastosPersonales(
                            valida,
                            rs.getTimestamp("fecha_validacion")
                    ));
                } else if (estado == 3) {
                    usuario rechaza = new usuario();
                    rechaza.setNombre(rs.getString("rechaza"));
                    g.setRechazo(new RechazoGastosPersonales(
                            rechaza,
                            rs.getString("motivo_rechazo"),
                            rs.getTimestamp("fecha_rechazo")
                    ));
                }
                res.add(g);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("GetGastosPersonales(estado) | " + ex);
        }
        return res;
    }

    public GastosPersonales GetGastoPersonal(int idGasto) {
        GastosPersonales res = new GastosPersonales();
        try {
            String sentencia = "SELECT * FROM v_gastos_personales WHERE id_gasto=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idGasto);
            rs = st.executeQuery();
            while (rs.next()) {
                res.setId(rs.getInt("id_gasto"));
                usuario u = new usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre_usuario"));
                u.setCedula(rs.getString("cedula_usuario"));
                res.setUsuario(u);
                res.setEstado(rs.getInt("estado"));
                res.setC103(rs.getDouble("c103"));
                res.setC104(rs.getDouble("c104"));
                res.setC105(rs.getDouble("c105"));
                res.setC106(rs.getDouble("c106"));
                res.setC107(rs.getDouble("c107"));
                res.setC108(rs.getDouble("c108"));
                res.setC109(rs.getDouble("c109"));
                res.setC110(rs.getDouble("c110"));
                res.setC111(rs.getDouble("c111"));
                res.setC112(rs.getDouble("c112"));
                res.setC113(rs.getDouble("c113"));
                res.setAdjunto(rs.getString("adjunto"));
                res.setCreacion(rs.getTimestamp("creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("GetGastoPersonal(idGasto) | " + ex);
        }
        return res;
    }

    public void aprobarGastoPersonal(AprobacionGastosPersonales a) throws SQLException {
        String sql = "INSERT INTO aprobacion_gasto(id_gasto,id_aprueba) VALUES(?,?); UPDATE gasto_personal SET estado=1 WHERE id_gasto=?;";
        st = enlace.prepareStatement(sql);
        st.setInt(1, a.getIdGasto());
        st.setInt(2, a.getAprueba().getId_usuario());
        st.setInt(3, a.getIdGasto());
        st.executeUpdate();
        st.close();
    }

    public void validarGastoPersonal(ValidacionGastosPersonales v) throws SQLException {
        String sql = "INSERT INTO validacion_gasto(id_gasto,id_valida) VALUES(?,?); UPDATE gasto_personal SET estado=2 WHERE id_gasto=?;";
        st = enlace.prepareStatement(sql);
        st.setInt(1, v.getIdGasto());
        st.setInt(2, v.getValida().getId_usuario());
        st.setInt(3, v.getIdGasto());
        st.executeUpdate();
        st.close();
    }

    public void rechazarGastoPersonal(RechazoGastosPersonales r) throws SQLException {
        String sql = "INSERT INTO rechazo_gasto(id_gasto,id_rechaza,motivo) VALUES(?,?,?); UPDATE gasto_personal SET estado=3 WHERE id_gasto=?;";
        st = enlace.prepareStatement(sql);
        st.setInt(1, r.getIdGasto());
        st.setInt(2, r.getRechaza().getId_usuario());
        st.setString(3, r.getMotivo());
        st.setInt(4, r.getIdGasto());
        st.executeUpdate();
        st.close();
    }

    public String getCodigoDireccionUsuario(int idUsuario) throws Exception {
        String res = "";
        final String sentencia = "SELECT IFNULL(o.codigo,'') AS res FROM organizacion o INNER JOIN usuario u ON o.nivel_hijo=u.codigo_unidad WHERE u.id_usuario=?";
        st = enlace.prepareStatement(sentencia);
        st.setInt(1, idUsuario);
        rs = st.executeQuery();
        while (rs.next()) {
            res = rs.getString("res");
        }
        st.close();
        rs.close();
        if (res.equals("")) {
            throw new Exception("Sin dirección asignada");
        }
        return res;
    }

    public ArrayList<TipoDocumento> listadoTipoDocumento() {
        ArrayList<TipoDocumento> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM tipo_documento WHERE visible";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                TipoDocumento p = new TipoDocumento();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCodigo(rs.getString("codigo"));
                res.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("listadoTipoDocumento " + ex);
        }
        return res;
    }

    public ArrayList<usuario> getOpcionesDestinatarioDocumento(final int id_usuario) {
        final ArrayList<usuario> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_opc_des_documento WHERE id!=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                usuario elemento = new usuario();
                elemento.setId_usuario(rs.getInt("id"));
                elemento.setCodigo_usuario(rs.getString("codigo"));
                elemento.setNombre(rs.getString("tra_nom"));
                elemento.setCodigo_unidad(rs.getString("direccion"));
                elemento.setCodigo_funcion(rs.getString("codigo_funcion"));
                res.add(elemento);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getOpcionesDestinatarioDocumento(id_usuario) | " + ex);
        }
        return res;
    }

    public TipoDocumento getTipoDocumento(final int id_tipo) {
        TipoDocumento res = new TipoDocumento();
        try {
            final String sentencia = "SELECT * FROM tipo_documento WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_tipo);
            rs = st.executeQuery();
            while (rs.next()) {
                res.setId(rs.getInt("id"));
                res.setNombre(rs.getString("nombre"));
                res.setCodigo(rs.getString("codigo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTipoDocumento | " + ex);
        }
        return res;
    }

    public String getNumeroDocumentoTemporal(String prefijo_codigo) {
        String res = "";
        try {
            final String sentencia = "SELECT codigo_temp AS res FROM v_documentos WHERE codigo_temp LIKE ? ORDER BY codigo_temp DESC LIMIT 1",
                    codigo_like = prefijo_codigo + "%" + administrar_documento.TEMPORAL;
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo_like);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("res");
                break;
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getNumeroDocumentoTemporal(prefijo_codigo) | " + e);
        }
        return res;
    }

    public String getNumeroDocumento(final String prefijo_codigo, final int tipo) {
        String res = "";
        try {
            final String sentencia = "SELECT codigo AS res FROM v_documentos WHERE codigo LIKE ? AND codigo NOT LIKE ? AND tipo=? ORDER BY codigo DESC LIMIT 1",
                    codigo_like = prefijo_codigo + "%",
                    codigo_not_like = "%" + administrar_documento.TEMPORAL;
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigo_like);
            st.setString(2, codigo_not_like);
            st.setInt(3, tipo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("res");
                break;
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getNumeroDocumento(prefijo_codigo,tipo) | " + e);
        }
        return res;
    }

    public int registrarDocumento(Documento d) throws Exception {
        try {
            final String sql = "INSERT INTO documento(de,tipo,tipo_circular,estado,codigo,codigo_temp,para,cargo_para,asunto,contenido,firmado,referencia) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, d.getDe());
            st.setInt(2, d.getTipo());
            if (d.getTipo_circular() != 0) {
                st.setInt(3, d.getTipo_circular());
            } else {
                st.setNull(3, Types.INTEGER);
            }
            st.setInt(4, d.getEstado());
            st.setString(5, d.getCodigo());
            if (!d.getCodigo_temp().equals("")) {
                st.setString(6, d.getCodigo_temp());
            } else {
                st.setNull(6, Types.VARCHAR);
            }
            if (!d.getPara().equals("")) {
                st.setString(7, d.getPara());
            } else {
                st.setNull(7, Types.VARCHAR);
            }
            if (!d.getPara_cargo().equals("")) {
                st.setString(8, d.getPara_cargo());
            } else {
                st.setNull(8, Types.VARCHAR);
            }
            st.setString(9, d.getAsunto());
            st.setString(10, d.getContenido());
            if (d.isFirmado()) {
                st.setBoolean(11, d.isFirmado());
            } else {
                st.setNull(11, Types.BOOLEAN);
            }
            if (d.getReferencia() != 0) {
                st.setInt(12, d.getReferencia());
            } else {
                st.setNull(12, Types.BIGINT);
            }
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            rs.next();
            final int res = rs.getInt(1);
            st.close();
            rs.close();
            return res;
        } catch (Exception e) {
            System.out.println("registrarDocumento | " + e);
            throw new Exception("Error al registrar documento");
        }
    }

    public void registrarDestinatarioDocumento(final DestinatarioDocumento d) throws Exception {
        try {
            final String sql = "INSERT INTO destinatario_documento(id_documento,id_usuario,tipo,estado) VALUES (?,?,?,?)";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getId_documento());
            st.setInt(2, d.getId_usuario());
            st.setInt(3, d.getTipo());
            st.setInt(4, d.getEstado());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("registrarDestinatarioDocumento | " + e);
            throw new Exception("Error al registrar destinatario");
        }
    }

    public void registrarDestinatariosDocumento(final Documento d) {
        try {
            String sql = "INSERT INTO destinatario_documento(id_documento,id_usuario,tipo,estado) VALUES";
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (!des.isActualizar()) {
                    sql += "(?,?,?,?),";
                }
            }
            sql = sql.substring(0, sql.length() - 1);
            st = enlace.prepareStatement(sql);
            int indice = 1;
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (!des.isActualizar()) {
                    st.setInt(indice, d.getId());
                    st.setInt(indice + 1, des.getId_usuario());
                    st.setInt(indice + 2, des.getTipo());
                    st.setInt(indice + 3, des.getEstado());
                    indice += 4;
                }
            }
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("registrarDestinatariosDocumento | " + e);
        }
    }

    public DestinatarioDocumento getDestinatarioDocumento(final int id_documento, final int id_usuario) {
        final DestinatarioDocumento res = new DestinatarioDocumento();
        try {
            final String sentencia = "SELECT * FROM v_destinatarios_documento WHERE id_documento=? AND id_usuario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_documento);
            st.setInt(2, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res.setId(rs.getInt("id"));
                res.setId_documento(rs.getInt("id_documento"));
                res.setId_usuario(rs.getInt("id_usuario"));
                res.setTipo(rs.getInt("tipo"));
                res.setEstado(rs.getInt("estado"));
                res.setLeido(rs.getTimestamp("leido"));
                res.setCreacion(rs.getTimestamp("creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDestinatarioDocumento(id_documento,id_usuario) | " + ex);
        }
        return res;
    }

    public ArrayList<DestinatarioDocumento> getDestinatariosDocumento(final int id_documento) {
        ArrayList<DestinatarioDocumento> res = new ArrayList<>();
        try {
            final String sentencia = "SELECT * FROM v_destinatarios_documento WHERE id_documento=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_documento);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new DestinatarioDocumento(
                                rs.getInt("id"),
                                rs.getInt("id_documento"),
                                rs.getInt("id_usuario"),
                                rs.getString("correo_usuario"),
                                rs.getInt("tipo"),
                                rs.getInt("estado"),
                                rs.getTimestamp("leido"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDestinatariosDocumento(id_documento) | " + ex);
        }
        return res;
    }

    public ArrayList<DestinatarioDocumento> getDestinatariosDocumento(final int id_documento, final int estado) {
        ArrayList<DestinatarioDocumento> res = new ArrayList<>();
        try {
            final String sentencia = "SELECT * FROM v_destinatarios_documento WHERE id_documento=? AND estado=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_documento);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new DestinatarioDocumento(
                                rs.getInt("id"),
                                rs.getInt("id_documento"),
                                rs.getInt("id_usuario"),
                                rs.getString("correo_usuario"),
                                rs.getInt("tipo"),
                                rs.getInt("estado"),
                                rs.getTimestamp("leido"),
                                rs.getTimestamp("creacion")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDestinatariosDocumento(id_documento, estado) | " + ex);
        }
        return res;
    }

    public void actualizarDestinatarioDocumento(final DestinatarioDocumento d) throws Exception {
        try {
            final String sql = "UPDATE destinatario_documento SET id_usuario=?, tipo=?, estado=?, leido=?, creacion=? WHERE id=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getId_usuario());
            st.setInt(2, d.getTipo());
            st.setInt(3, d.getEstado());
            if (d.getLeido() != null) {
                st.setTimestamp(4, d.getLeido());
            } else {
                st.setNull(4, Types.TIMESTAMP);
            }
            if (d.getCreacion() != null) {
                st.setTimestamp(5, d.getCreacion());
            } else {
                st.setNull(5, Types.TIMESTAMP);
            }
            st.setInt(6, d.getId());
            final boolean res = st.executeUpdate() != 1;
            st.close();
            if (res) {
                throw new Exception("No se actualizó la asignación del documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarDestinatarioDocumento | " + e);
            throw new Exception("Error al actualizar destinatario");
        }
    }

    public void actualizarDestinatariosDocumento(final Documento d) {
        try {
            String sql = "";
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (des.isActualizar()) {
                    sql += "UPDATE destinatario_documento SET id_usuario=?, tipo=?, estado=? WHERE id=?;";
                }
            }
            st = enlace.prepareStatement(sql);
            int indice = 1;
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (des.isActualizar()) {
                    st.setInt(indice, des.getId_usuario());
                    st.setInt(indice + 1, des.getTipo());
                    st.setInt(indice + 2, des.getEstado());
                    st.setInt(indice + 3, des.getId());
                    indice += 4;
                }
            }
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("actualizarDestinatariosDocumento | " + e);
        }
    }

    public void eliminarDestinatariosDocumento(final Documento d) {
        try {
            String sql = "";
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (des.isEliminar()) {
                    sql += "DELETE FROM destinatario_documento WHERE id=?;";
                }
            }
            st = enlace.prepareStatement(sql);
            int indice = 1;
            for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                if (des.isEliminar()) {
                    st.setInt(indice, des.getId());
                    indice++;
                }
            }
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            System.out.println("eliminarDestinatariosDocumento | " + e);
        }
    }

    public int registrarAnexoDocumento(AnexoDocumento a) throws Exception {
        try {
            final String sql = "INSERT INTO anexo_documento(id_documento,nombre) VALUES(?,?)";
            st = enlace.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, a.getIdDocumento());
            st.setString(2, a.getNombre());
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            rs.next();
            final int res = rs.getInt(1);
            st.close();
            rs.close();
            return res;
        } catch (Exception e) {
            System.out.println("registrarAnexoDocumento | " + e);
            throw new Exception("Error al registrar anexo del documento");
        }
    }

    public ArrayList<AnexoDocumento> getAnexosDocumento(final int id_documento) {
        ArrayList<AnexoDocumento> res = new ArrayList<>();
        try {
            final String sentencia = "SELECT * FROM anexo_documento WHERE id_documento=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_documento);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new AnexoDocumento(
                                rs.getInt("id"),
                                rs.getInt("id_documento"),
                                rs.getString("nombre"),
                                rs.getString("path")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getAnexosDocumento(id_documento) | " + ex);
        }
        return res;
    }

    public void actualizarAnexoDocumento(AnexoDocumento a) throws Exception {
        try {
            final String sql = "UPDATE anexo_documento SET path=? WHERE id=?";
            st = enlace.prepareStatement(sql);
            st.setString(1, a.getPath());
            st.setInt(2, a.getId());
            final boolean error = st.executeUpdate() != 1;
            st.close();
            if (error) {
                throw new Exception("No se actualizó el anexo");
            }
        } catch (SQLException e) {
            System.out.println("actualizarAnexoDocumento | " + e);
            throw new Exception("Error al actualizar anexo");
        }
    }

    public void eliminarAnexoDocumento(final int id) throws Exception {
        try {
            final String sql = "DELETE FROM anexo_documento WHERE id=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("eliminarAnexoDocumento | " + e);
            throw new Exception("Error al eliminar anexo");
        }
    }

    public void eliminarBorrador(final int id) throws Exception {
        try {
            final String sql = "DELETE FROM documento WHERE estado=0 AND id=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, id);
            final boolean error = st.executeUpdate() != 1;
            st.close();
            if (error) {
                throw new Exception("No se eliminó el borrador");
            }
        } catch (SQLException e) {
            System.out.println("eliminarBorrador | " + e);
            throw new Exception("Error al eliminar borrador");
        }
    }

    public void registrarAsignacionDocumento(AsignacionDocumento a) throws Exception {
        try {
            final String sql = "INSERT INTO asignacion_documento(id_documento,id_actual,id_nuevo,comentario) VALUES(?,?,?,?)";
            st = enlace.prepareStatement(sql);
            st.setInt(1, a.getIdDocumento());
            st.setInt(2, a.getActual());
            st.setInt(3, a.getNuevo());
            st.setString(4, a.getComentario());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("registrarAsignacionDocumento | " + e);
            throw new Exception("Error al registrar asignación documento");
        }
    }

    public Documento getDocumento(final int id) {
        Documento d = new Documento();
        final ArrayList<DestinatarioDocumento> destinatarios = getDestinatariosDocumento(id);
        final ArrayList<AnexoDocumento> anexos = getAnexosDocumento(id);
        try {
            final String sentencia = "SELECT * FROM v_documentos WHERE id=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setTipo(rs.getInt("tipo"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDe(rs.getInt("de"));
                d.setDeNombre(rs.getString("de_full"));
                d.setDestinatarios(rs.getString("destinatarios"));
                d.setIds_destinatarios(destinatarios);
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setCodigo_temp(rs.getString("codigo_temp"));
                d.setAsunto(rs.getString("asunto"));
                d.setContenido(rs.getString("contenido"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setPath(rs.getString("path"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setAnexos(anexos);
                d.setCreacion(rs.getTimestamp("creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumento(id) | " + ex);
        }
        return d;
    }

    public Documento getDocumento(final int id, final int de) {
        Documento d = new Documento();
        final ArrayList<DestinatarioDocumento> destinatarios = getDestinatariosDocumento(id);
        final ArrayList<AnexoDocumento> anexos = getAnexosDocumento(id);
        try {
            final String sentencia = "SELECT * FROM v_documentos WHERE id=? AND de=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id);
            st.setInt(2, de);
            rs = st.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setTipo(rs.getInt("tipo"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDe(rs.getInt("de"));
                d.setDeNombre(rs.getString("de_full"));
                d.setTipo_circular(rs.getInt("tipo_circular"));
                d.setDestinatarios(rs.getString("destinatarios"));
                d.setIds_destinatarios(destinatarios);
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setCodigo_temp(rs.getString("codigo_temp"));
                d.setPara(rs.getString("para"));
                d.setPara_cargo(rs.getString("cargo_para"));
                d.setAsunto(rs.getString("asunto"));
                d.setContenido(rs.getString("contenido"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setPath(rs.getString("path"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setAnexos(anexos);
                d.setCreacion(rs.getTimestamp("creacion"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumento(id, de) | " + ex);
        }
        return d;
    }

    public void actualizarDocumento(Documento d) throws Exception {
        try {
            final String sql = "UPDATE documento SET tipo=?,de=?,tipo_circular=?,estado=?,codigo=?,para=?,cargo_para=?,asunto=?,contenido=?,firmado=?,referencia=? WHERE estado=0 AND id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getTipo());
            st.setInt(2, d.getDe());
            if (d.getTipo_circular() != 0) {
                st.setInt(3, d.getTipo_circular());
            } else {
                st.setNull(3, Types.INTEGER);
            }
            st.setInt(4, d.getEstado());
            st.setString(5, d.getCodigo());
            if (!d.getPara().equals("")) {
                st.setString(6, d.getPara());
            } else {
                st.setNull(6, Types.VARCHAR);
            }
            if (!d.getPara_cargo().equals("")) {
                st.setString(7, d.getPara_cargo());
            } else {
                st.setNull(7, Types.VARCHAR);
            }
            st.setString(8, d.getAsunto());
            st.setString(9, d.getContenido());
            st.setBoolean(10, d.isFirmado());
            if (d.getReferencia() != 0) {
                st.setInt(11, d.getReferencia());
            } else {
                st.setNull(11, Types.INTEGER);
            }
            st.setInt(12, d.getId());
            st.setInt(13, d.getDe());
            final boolean error = st.executeUpdate() != 1;
            st.close();
            if (error) {
                throw new Exception("No se actualizó el documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarDocumento | " + e);
            throw new Exception("Error al actualizar documento (" + e + ")");
        }
    }

    public void actualizarEstadoDocumentoDe(Documento d) throws Exception {
        try {
            final String sql = "UPDATE documento SET estado=? WHERE id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getEstado());
            st.setInt(2, d.getId());
            st.setInt(3, d.getDe());
            final boolean res = st.executeUpdate() != 1;
            st.close();
            if (res) {
                throw new Exception("No se actualizó el documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarEstadoDocumentoDe | " + e);
            throw new Exception("Error al actualizar estado documento");
        }
    }

    public void actualizarEstadoDocumentoActual(Documento d) throws Exception {
        try {
            final String sql = "UPDATE documento SET estado=? WHERE id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getEstado());
            st.setInt(2, d.getId());
            st.setInt(3, d.getDe());
            final boolean res = st.executeUpdate() != 1;
            st.close();
            if (res) {
                throw new Exception("No se actualizó el documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarEstadoDocumentoActual | " + e);
            throw new Exception("Error al actualizar estado documento");
        }
    }

    public void actualizarFirmadoDocumento(Documento d) throws Exception {
        try {
            final String sql = "UPDATE documento SET firmado=? WHERE id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setBoolean(1, d.isFirmado());
            st.setInt(2, d.getId());
            st.setInt(3, d.getDe());
            final boolean error = st.executeUpdate() != 1;
            st.close();
            if (error) {
                throw new Exception("No se actualizó firmado documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarFirmadoDocumento | " + e);
            throw new Exception("Error al actualizar firmado documento");
        }
    }

    public void actualizarPathDocumento(Documento d) throws Exception {
        try {
            final String sql = "UPDATE documento SET estado=?,firmado=?,path=?,enviado=? WHERE id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, d.getEstado());
            st.setBoolean(2, d.isFirmado());
            st.setString(3, d.getPath());
            if (d.getEnviado() != null) {
                st.setTimestamp(4, d.getEnviado());
            } else {
                st.setNull(4, Types.TIMESTAMP);
            }
            st.setInt(5, d.getId());
            st.setInt(6, d.getDe());
            final boolean error = st.executeUpdate() != 1;
            st.close();
            if (error) {
                throw new Exception("No se actualizó el documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarPathDocumento | " + e);
            throw new Exception("Error al actualizar path documento");
        }
    }

    public void actualizarRemitenteDocumento(Documento d, final int nuevo_remitente) throws Exception {
        try {
            final String sql = "UPDATE documento SET de=? WHERE id=? AND de=?";
            st = enlace.prepareStatement(sql);
            st.setInt(1, nuevo_remitente);
            st.setInt(2, d.getId());
            st.setInt(3, d.getDe());
            final boolean res = st.executeUpdate() != 1;
            st.close();
            if (res) {
                throw new Exception("No se reasignó el documento");
            }
        } catch (SQLException e) {
            System.out.println("actualizarRemitenteDocumento | " + e);
            throw new Exception("Error al reasignar documento");
        }
    }

    public ArrayList<Documento> getDocumentosEnviados(final int idUsuario, final int estado) {
        ArrayList<Documento> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_documentos WHERE de=? AND estado=? " + (estado == 0 ? "" : "AND DATE(creacion) BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 MONTH) AND CURDATE()");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            rs = st.executeQuery();
            while (rs.next()) {
                Documento d = new Documento();
                d.setId(rs.getInt("id"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDestinatarios(rs.getString("destinatarios"));
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setAsunto(rs.getString("asunto"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setPath(rs.getString("path"));
                d.setCreacion(rs.getTimestamp("creacion"));
                d.setEnviado(rs.getTimestamp("enviado"));
                res.add(d);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumentosEnviados(idUsuario, estado) | " + ex);
        }
        for (Documento d : res) {
            d.setAnexos(getAnexosDocumento(d.getId()));
        }
        return res;
    }

    public ArrayList<Documento> getDocumentosEnviados(final int idUsuario, final int estado, java.sql.Date fecha_ini, java.sql.Date fecha_fin) {
        ArrayList<Documento> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_documentos WHERE de=? AND estado=? AND DATE(creacion) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, idUsuario);
            st.setInt(2, estado);
            st.setDate(3, fecha_ini);
            st.setDate(4, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                Documento d = new Documento();
                d.setId(rs.getInt("id"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDeNombre(rs.getString("de_full"));
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setAsunto(rs.getString("asunto"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setPath(rs.getString("path"));
                d.setCreacion(rs.getTimestamp("creacion"));
                d.setEnviado(rs.getTimestamp("enviado"));
                res.add(d);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumentosEnviados(idUsuario, estado, fecha_ini, fecha_fin) | " + ex);
        }
        for (Documento d : res) {
            d.setAnexos(getAnexosDocumento(d.getId()));
        }
        return res;
    }

    public ArrayList<Documento> getDocumentosRecibidos(final int id_usuario, final int estado_destinatario) {
        ArrayList<Documento> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_documentos_recibidos WHERE estado=2 AND id_destinatario=? AND estado_destinatario=? AND DATE(creacion_destinatario) BETWEEN DATE_ADD(CURDATE(), INTERVAL -3 MONTH) AND CURDATE()";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, estado_destinatario);
            rs = st.executeQuery();
            while (rs.next()) {
                final Documento d = new Documento();
                d.setId(rs.getInt("id"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDeNombre(rs.getString("de_full"));
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setAsunto(rs.getString("asunto"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setCreacion(rs.getTimestamp("creacion"));
                d.setEnviado(rs.getTimestamp("enviado"));
                final DestinatarioDocumento destinatario = new DestinatarioDocumento();
                destinatario.setTipo(rs.getInt("tipo_destinatario"));
                destinatario.setLeido(rs.getTimestamp("leido_destinatario"));
                d.setDestinatario(destinatario);
                res.add(d);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumentosRecibidos(id_usuario, estado_destinatario) | " + ex);
        }
        for (Documento d : res) {
            d.setAnexos(getAnexosDocumento(d.getId()));
        }
        return res;
    }

    public ArrayList<Documento> getDocumentosRecibidos(final int id_usuario, final int estado_destinatario, java.sql.Date fecha_ini, java.sql.Date fecha_fin) {
        ArrayList<Documento> res = new ArrayList();
        try {
            final String sentencia = "SELECT * FROM v_documentos_recibidos WHERE estado=2 AND id_destinatario=? AND estado_destinatario=? AND DATE(creacion_destinatario) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            st.setInt(2, estado_destinatario);
            st.setDate(3, fecha_ini);
            st.setDate(4, fecha_fin);
            rs = st.executeQuery();
            while (rs.next()) {
                final Documento d = new Documento();
                d.setId(rs.getInt("id"));
                d.setTipoDes(rs.getString("tipo_des"));
                d.setDeNombre(rs.getString("de_full"));
                d.setEstado(rs.getInt("estado"));
                d.setCodigo(rs.getString("codigo"));
                d.setAsunto(rs.getString("asunto"));
                d.setReferenciaCodigo(rs.getString("referencia_codigo"));
                d.setFirmado(rs.getBoolean("firmado"));
                d.setCreacion(rs.getTimestamp("creacion"));
                d.setEnviado(rs.getTimestamp("enviado"));
                final DestinatarioDocumento destinatario = new DestinatarioDocumento();
                destinatario.setTipo(rs.getInt("tipo_destinatario"));
                destinatario.setLeido(rs.getTimestamp("leido_destinatario"));
                d.setDestinatario(destinatario);
                res.add(d);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDocumentosRecibidos(id_usuario, estado_destinatario, fecha_ini, fecha_fin) | " + ex);
        }
        for (Documento d : res) {
            d.setAnexos(getAnexosDocumento(d.getId()));
        }
        return res;
    }

    public int getTotalNuevos(final int id_usuario) {
        int res = 0;
        try {
            final String sentencia = "SELECT COUNT(*) AS res FROM v_documentos_recibidos WHERE estado=2 AND estado_destinatario=1 AND leido_destinatario IS NULL AND id_destinatario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("res");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTotalNuevos | " + ex);
        }
        return res;
    }

    public int getTotalEnviadosMesActual(final int id_usuario) {
        int res = 0;
        try {
            final String sentencia = "SELECT COUNT(*) AS res FROM v_documentos WHERE estado=2 AND de=? AND DATE(creacion) BETWEEN DATE_ADD(CURDATE(), INTERVAL -30 DAY) AND CURDATE()";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("res");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTotalEnviadosMesActual | " + ex);
        }
        return res;
    }

    public int getTotalBorradores(final int id_usuario) {
        int res = 0;
        try {
            final String sentencia = "SELECT COUNT(*) AS res FROM v_documentos WHERE estado=0 AND de=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, id_usuario);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("res");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getTotalBorradores | " + ex);
        }
        return res;
    }

    public ArrayList<Direccion> getDirecciones() {
        final ArrayList<Direccion> res = new ArrayList<>();
        try {
            final String sentencia = "SELECT * FROM v_direcciones";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new Direccion(
                                rs.getString("nivel_hijo"),
                                rs.getString("nombre")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getDirecciones | " + ex);
        }
        return res;
    }

    public void auditarAcceso(AuditoriaAcceso a) {
        try {
            final String sql = "INSERT INTO auditoria_acceso(id_usuario,descripcion) VALUES(?,?)";
            st = enlace.prepareStatement(sql);
            st.setInt(1, a.getId_usuario());
            st.setString(2, a.getDescripcion());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("auditarAcceso | " + e);
        }
    }

    public boolean enviarCorreoHotmail(String destinatario, String mensaje) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(
                props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("soporte.gadmce2024@outlook.com", "@Sg123456");
            }
        }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("soporte.gadmce2024@outlook.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("GADMCE Memorando asignado");
            message.setText(mensaje + "." + "Se dispone con base en la Normativa Legal vigente, el estricto cumplimiento a las funciones asignadas y el tiempo de entrega establecido, de conformidad a la Ley Orgánica de Servicio Público, Art. 24.- Prohibiciones a las servidoras y los servidores públicos.- Prohíbase a las servidoras y los servidores públicos lo siguiente:\n"
                    + "c) Retardar o negar en forma injustificada el oportuno despacho de los asuntos o la prestación del servicio a que está obligado de acuerdo a las funciones de su cargo;\n"
                    + "Y de conformidad al Reglamento Interno de Administración de Talento Humano: \n"
                    + "Art. 28.- DEBERES.- Son deberes de los servidores del Gobierno Autónomo Descentralizado Municipal del Cantón Esmeraldas a más de los previstos en la Ley Orgánica del Servicio Público los siguientes:\n"
                    + "j) Entregar los trabajos asignados por el jefe inmediato en los tiempos establecidos, cumplir con todas las actividades planificadas y prestar servicios al usuario en los plazos legales determinados;\n"
                    + "Que, el incumplimiento e inobservancia a lo antes descrito será objeto de aplicación del Régimen Disciplinario dentro de la LOSEP Art. 41.- Responsabilidad administrativa.- La servidora o servidor público que incumpliere sus obligaciones o contraviniere las disposiciones de esta Ley, sus reglamentos, así como las leyes y normativa conexa, incurrirá en responsabilidad administrativa que será sancionada disciplinariamente, sin perjuicio de la acción civil o penal que pudiere originar el mismo hecho. \n"
                    + "Y dentro del Reglamento Interno de Administración de Talento Humano Art. 60.- FALTAS DISCIPLINARIAS.- Conforme a lo prescrito en la Ley Orgánica del Servicio Público y su Reglamento General, las faltas disciplinarias, de acuerdo a su incidencia pueden ser leves o graves; las que serán sancionadas de conformidad con el presente Reglamento.");

            Transport.send(message);

            System.out.println("ok");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public boolean enviarCorreoHotmailCaducado(String destinatario, String mensaje) {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(
                props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("soporte.gadmce2024@outlook.com", "@Sg123456");
            }
        }
        );
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("soporte.gadmce2024@outlook.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("GADMCE Memorando asignado");
            message.setText(mensaje + " ." + "La Dirección de Administración de Talento Humano del GADMCE, el incumplimiento a lo dispuesto al servidor, por lo que se solicita se aplique la normativa legal vigente, conforme lo establecido en la Ley Orgánica de Servicio Público, Art. 24.- Prohibiciones a las servidoras y los servidores públicos y el Reglamento Interno de Administración de Talento Humano, Art. 28 y demás normativa aplicable.");

            Transport.send(message);

            System.out.println("ok");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public boolean actualizarAdjuntoMemorandumID(int id_memorandum, Memorandum elemento) throws SQLException {
        st = enlace.prepareStatement("UPDATE memorandum SET adjunto=? WHERE id= '" + id_memorandum + "'");
        st.setString(1, elemento.getAdjunto());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean actualizarMemorandumNotificacion(int id_memorandum) throws SQLException {
        st = enlace.prepareStatement("UPDATE memorandum SET notificado=? WHERE id= '" + id_memorandum + "'");
        st.setInt(1, 1);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean completarInformeMemorandumID(int id_memorandum, Memorandum elemento) throws SQLException {
        st = enlace.prepareStatement("UPDATE memorandum SET adjunto_final=?,observacion_final=?,estado=?  WHERE id= '" + id_memorandum + "'");
        st.setString(1, elemento.getAdjunto_final());
        st.setString(2, elemento.getObservacion_final());
        st.setString(3, StatusEnum.COMPLETADO.getParam());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean CrearAlerta(Alerta alerta_info) {
        boolean response = false;
        try {
            String sql = "call CrearAlerta(?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, alerta_info.getId_usuario());
            stmt.setInt(2, alerta_info.getDias_notificacion());

            if (stmt.executeUpdate() > 0) {
                response = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
    }

    public Alerta ObtenerAlertaUsuarioID(int id_usuario) {
        Alerta alertaInfo = new Alerta();
        try {
            String sql = "call ObtenerAlertaUsuarioID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                alertaInfo.setId(rs.getInt("id"));
                alertaInfo.setId_usuario(rs.getInt("id_usuario"));
                alertaInfo.setDias_notificacion(rs.getInt("dias_notificacion"));
                alertaInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                alertaInfo.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return alertaInfo;
    }

    public boolean ActualizarAlerta(Alerta alerta_info) {
        boolean response = false;
        try {
            String sql = "call ActualizarAlerta(?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, alerta_info.getId_usuario());
            stmt.setInt(2, alerta_info.getDias_notificacion());

            if (stmt.executeUpdate() > 0) {
                response = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
    }

    public boolean CrearMemorandum(Memorandum memorandum_info) {
        boolean response = false;
        try {
            String sql = "call CrearMemorandum(?,?,?,?,?,?,?,?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, memorandum_info.getId_usuario());
            stmt.setInt(2, memorandum_info.getId_asignado());
            stmt.setString(3, memorandum_info.getActividad());
            stmt.setString(4, memorandum_info.getParticipantes());
            stmt.setDate(5, memorandum_info.getFecha_limite());
            stmt.setString(6, memorandum_info.getDocumento());
            stmt.setString(7, memorandum_info.getDescripcion());
            stmt.setString(8, memorandum_info.getObservacion());
            stmt.setString(9, memorandum_info.getResultado());

            if (stmt.executeUpdate() > 0) {
                response = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
    }

    public ArrayList<Memorandum> ObtenerListadoMemorandumsUsuarioIDEstado(int id_usuario, String estado) {
        ArrayList<Memorandum> list = new ArrayList();
        try {
            String sql = "call ObtenerListadoMemorandumsUsuarioIDEstado(?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            stmt.setString(2, estado);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Memorandum memorandumInfo = new Memorandum();
                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
                list.add(memorandumInfo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public ArrayList<Memorandum> ObtenerListadoMemorandumsAsignadorIDEstado(int id_usuario, String estado) {
        ArrayList<Memorandum> list = new ArrayList();
        try {
            String sql = "call ObtenerListadoMemorandumsAsignadorIDEstado(?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            stmt.setString(2, estado);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Memorandum memorandumInfo = new Memorandum();
                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setId_asignado(rs.getInt("id_asignado"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
                list.add(memorandumInfo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Memorandum ObtenerMemorandumRecienteUsuarioID(int id_usuario) {
        Memorandum memorandumInfo = new Memorandum();
        try {
            String sql = "call ObtenerMemorandumRecienteUsuarioID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            rs = stmt.executeQuery();

            while (rs.next()) {

                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return memorandumInfo;
    }

    public ArrayList<Memorandum> ObtenerMemorandumsPorCaducarUsuarioID(int id_usuario) {
        ArrayList<Memorandum> list = new ArrayList();
        try {
            String sql = "call ObtenerMemorandumsPorCaducarUsuarioID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Memorandum memorandumInfo = new Memorandum();
                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setId_asignado(rs.getInt("id_asignado"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
                list.add(memorandumInfo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public ArrayList<Memorandum> ObtenerMemorandumsPorCaducarAsignadorID(int id_usuario) {
        ArrayList<Memorandum> list = new ArrayList();
        try {
            String sql = "call ObtenerMemorandumsPorCaducarAsignadorID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_usuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Memorandum memorandumInfo = new Memorandum();
                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setId_asignado(rs.getInt("id_asignado"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
                list.add(memorandumInfo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public ArrayList<Memorandum> ObtenerMemorandumsCaducados() {
        ArrayList<Memorandum> list = new ArrayList();
        try {
            String sql = "call ObtenerMemorandumsCaducados()";
            stmt = enlace.prepareCall(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Memorandum memorandumInfo = new Memorandum();
                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setId_asignado(rs.getInt("id_asignado"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setDias_restantes(rs.getInt("dias_restantes"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
                list.add(memorandumInfo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Memorandum ObtenerMemorandumPorID(int id_memorandum) {
        Memorandum memorandumInfo = new Memorandum();
        try {
            String sql = "call ObtenerMemorandumPorID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_memorandum);
            rs = stmt.executeQuery();

            while (rs.next()) {

                memorandumInfo.setId(rs.getInt("id"));
                memorandumInfo.setId_usuario(rs.getInt("id_usuario"));
                memorandumInfo.setActividad(rs.getString("actividad"));
                memorandumInfo.setParticipantes(rs.getString("participantes"));
                memorandumInfo.setFecha_limite(rs.getDate("fecha_limite"));
                memorandumInfo.setAdjunto(rs.getString("adjunto"));
                memorandumInfo.setNotificado(rs.getInt("notificado"));
                memorandumInfo.setAdjunto_final(rs.getString("adjunto_final"));
                memorandumInfo.setObservacion_final(rs.getString("observacion_final"));
                memorandumInfo.setDocumento(rs.getString("documento"));
                memorandumInfo.setDescripcion(rs.getString("descripcion"));
                memorandumInfo.setObservacion(rs.getString("observacion"));
                memorandumInfo.setResultado(rs.getString("resultado"));
                memorandumInfo.setEstado(rs.getString("estado"));
                memorandumInfo.setFecha_creacion(rs.getTimestamp("fecha_creacion"));
                memorandumInfo.setFecha_update(rs.getTimestamp("fecha_update"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return memorandumInfo;
    }

    public boolean CompletarMemorandumID(int id_memorandum, String estado) {
        boolean response = false;
        try {
            String sql = "call CompletarMemorandumID(?,?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_memorandum);
            stmt.setString(2, estado);
            if (stmt.executeUpdate() > 0) {
                response = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
    }

    public boolean EliminarMemorandumID(int id_memorandum) {
        boolean response = false;
        try {
            String sql = "call EliminarMemorandumID(?)";
            stmt = enlace.prepareCall(sql);
            stmt.setInt(1, id_memorandum);
            if (stmt.executeUpdate() > 0) {
                response = true;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return response;
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
}
