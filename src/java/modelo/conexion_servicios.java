/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
 * @author Kevin Druet
 */
public class conexion_servicios {

    private static Connection enlace;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
    //?useUnicode=true&characterEncoding=UTF-8
    private static final String url = "jdbc:mysql://192.168.120.16:3306/servicio_ciudadano";
    private ResultSet rs;
    private PreparedStatement st;
    private static SecureRandom random = new SecureRandom();

    public conexion_servicios() {
        enlace = null;
        try {
            Class.forName(driver);
            enlace = DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }

    }

    public ArrayList<solicitud> listadoSolicitudTodas() {
        ArrayList<solicitud> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM solicitud ORDER BY id_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud p = new solicitud();
                p.setId_solicitud(rs.getInt("id_solicitud"));
                p.setCod_solicitud(rs.getString("cod_solicitud"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_servicio(rs.getInt("id_servicio"));
                p.setFecha_solicitud(rs.getTimestamp("fecha_solicitud"));
                p.setEstado(rs.getInt("estado"));
                p.setObservacion(rs.getString("observacion"));
                p.setPaso(rs.getInt("paso"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<estados_solicitud> listadoEstadosSolicitud() {
        ArrayList<estados_solicitud> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *FROM estados_solicitud ORDER BY id_estados_solicitud DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                estados_solicitud p = new estados_solicitud();
                p.setId_estados_solicitud(rs.getInt("id_estados_solicitud"));
                p.setEstados(rs.getString("estado"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public int contadorSolicitudesEstado(int estado) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS cantidad FROM solicitud WHERE estado= '" + estado + "'";
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

    public int contadorSolicitudesEnTramite() {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS cantidad FROM solicitud WHERE estado!=0 AND estado!=1 AND estado!=2 AND estado!=400";
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

    public int contadorSolicitudes() {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(*) AS cantidad FROM solicitud";
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

    public double porcentajeSolicitudesEstados(int estado) {
        double porcentaje = 0;
        float numero_solicitudes = contadorSolicitudesEstado(estado);
        int total_solicitudes = contadorSolicitudes();
        porcentaje = ((numero_solicitudes / total_solicitudes) * 100);
        return limitarDecimales(porcentaje);
    }

    public double porcentajeSolicitudesEnTramite() {
        double porcentaje = 0;
        float numero_solicitudes = contadorSolicitudesEnTramite();
        int total_solicitudes = contadorSolicitudes();
        porcentaje = ((numero_solicitudes / total_solicitudes) * 100);
        return limitarDecimales(porcentaje);
    }

    public int totalSolicitudesMensualesEstado(int mes, int estado) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_solicitud) AS contador FROM solicitud WHERE MONTH(fecha_solicitud)='" + mes + "'AND YEAR(CURDATE())=YEAR(fecha_solicitud) AND estado='" + estado + "'";
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

    public int totalSolicitudesMensualesEnTramite(int mes) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_solicitud) AS contador FROM solicitud WHERE MONTH(fecha_solicitud)='" + mes + "'AND YEAR(CURDATE())=YEAR(fecha_solicitud) AND estado!=0 AND estado!=1 AND estado!=2 AND estado!=400";
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

    public String mesNombre(int id_mes) {
        String mes = null;
        if (id_mes == 1) {
            mes = "enero";
        } else if (id_mes == 2) {
            mes = "febrero";
        } else if (id_mes == 3) {
            mes = "marzo";
        } else if (id_mes == 4) {
            mes = "abril";
        } else if (id_mes == 5) {
            mes = "mayo";
        } else if (id_mes == 6) {
            mes = "junio";
        } else if (id_mes == 7) {
            mes = "julio";
        } else if (id_mes == 8) {
            mes = "agosto";
        } else if (id_mes == 9) {
            mes = "septiembre";
        } else if (id_mes == 10) {
            mes = "octubre";
        } else if (id_mes == 11) {
            mes = "noviembre";
        } else if (id_mes == 12) {
            mes = "diciembre";
        }
        return mes;
    }

    public int totalSolicitudesMensuales(int mes) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT COUNT(id_solicitud) AS contador FROM solicitud WHERE MONTH(fecha_solicitud)='" + mes + "'AND YEAR(CURDATE())=YEAR(fecha_solicitud)";
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

    public ArrayList<tramite_funcionario> cantidadTramitesFuncionario() {
        ArrayList<tramite_funcionario> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT tramite_funcionario.id_usuario as iu, usuario.nombres AS nombre,usuario.apellidos AS apellido, COUNT(*) AS contador from tramite_funcionario INNER JOIN usuario ON usuario.id_usuario=tramite_funcionario.id_usuario GROUP BY usuario.id_usuario ORDER BY contador DESC";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                tramite_funcionario p = new tramite_funcionario();
                p.setId_usuario(rs.getInt("iu"));
                p.setCantidad(rs.getInt("contador"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<organizacion> listadoDirecciones() {
        ArrayList<organizacion> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT *from rol where id_rol!=1 and id_rol!=14 and id_rol!=15 and id_rol!=3";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                organizacion p = new organizacion();
                p.setId_organizacion(rs.getInt("id_rol"));
                p.setNombre(rs.getString("descripcion"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<resumen_servicio> listadoResumenDireccionAnio(String anio, int direccion) {
        ArrayList<resumen_servicio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "'\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_servicio p = new resumen_servicio();
                p.setId_resumen(rs.getInt("numero_mes"));
                p.setNombre_mes(rs.getString("nombre_mes"));
                p.setCantidad(rs.getInt("cantidad_solicitudes"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<resumen_servicio> listadoSolicitudesFinalizadasResumenDireccionAnio(String anio, int direccion) {
        ArrayList<resumen_servicio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=2\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_servicio p = new resumen_servicio();
                p.setId_resumen(rs.getInt("numero_mes"));
                p.setNombre_mes(rs.getString("nombre_mes"));
                p.setCantidad(rs.getInt("cantidad_solicitudes"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public int cantidadSolicitudesFinalizadasResumenDireccionAnio(String anio, int direccion) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=2\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = contador + rs.getInt("cantidad_solicitudes");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return contador;
    }

    public int cantidadSolicitudesAnuladasResumenDireccionAnio(String anio, int direccion) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=400\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = contador + rs.getInt("cantidad_solicitudes");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return contador;
    }

    public ArrayList<resumen_servicio> listadoSolicitudesEnTramiteResumenDireccionAnio(String anio, int direccion) {
        ArrayList<resumen_servicio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado!=0 AND solicitud.estado!=1 AND solicitud.estado!=2 AND solicitud.estado!=400\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_servicio p = new resumen_servicio();
                p.setId_resumen(rs.getInt("numero_mes"));
                p.setNombre_mes(rs.getString("nombre_mes"));
                p.setCantidad(rs.getInt("cantidad_solicitudes"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<resumen_servicio> listadoSolicitudesPendientesDireccionAnio(String anio, int direccion) {
        ArrayList<resumen_servicio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=0\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_servicio p = new resumen_servicio();
                p.setId_resumen(rs.getInt("numero_mes"));
                p.setNombre_mes(rs.getString("nombre_mes"));
                p.setCantidad(rs.getInt("cantidad_solicitudes"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public ArrayList<resumen_servicio> listadoSolicitudesAnuladasDireccionAnio(String anio, int direccion) {
        ArrayList<resumen_servicio> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=400\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                resumen_servicio p = new resumen_servicio();
                p.setId_resumen(rs.getInt("numero_mes"));
                p.setNombre_mes(rs.getString("nombre_mes"));
                p.setCantidad(rs.getInt("cantidad_solicitudes"));
                listado.add(p);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return listado;
    }

    public int cantidadSolicitudesEnTramiteResumenDireccionAnio(String anio, int direccion) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado!=0 AND solicitud.estado!=1 AND solicitud.estado!=2 AND solicitud.estado!=400\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = contador + rs.getInt("cantidad_solicitudes");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return contador;
    }

    public int cantidadSolicitudesNuevasDireccionAnio(String anio, int direccion) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=0\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = contador + rs.getInt("cantidad_solicitudes");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return contador;
    }

    public int cantidadSolicitudesAnuladasDireccionAnio(String anio, int direccion) {
        int contador = 0;
        try {
            String sentencia;
            sentencia = "SELECT  \n"
                    + "MONTH(solicitud.fecha_solicitud) AS numero_mes,\n"
                    + "MONTHNAME(solicitud.fecha_solicitud) AS nombre_mes,\n"
                    + "COUNT(*) AS cantidad_solicitudes FROM solicitud \n"
                    + "INNER JOIN servicio ON servicio.id_servicio = solicitud.id_servicio \n"
                    + "INNER JOIN permisos_servicios ON servicio.id_servicio = permisos_servicios.id_servicio\n"
                    + "WHERE YEAR(solicitud.fecha_solicitud)='" + anio + "' AND permisos_servicios.id_rol ='" + direccion + "' AND solicitud.estado=400\n"
                    + "GROUP BY MONTH (solicitud.fecha_solicitud)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                contador = contador + rs.getInt("cantidad_solicitudes");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return contador;
    }

    public double limitarDecimales(double valor) {
        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
        separadoresPersonalizados.setDecimalSeparator('.');
        DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
        return Double.parseDouble(formato1.format(valor));
    }

    public usuario_servicio buscar_usuarioID(int id) {
        usuario_servicio p = new usuario_servicio();
        try {
            String sentencia;
            sentencia = "SELECT *FROM usuario WHERE id_usuario= '" + id + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));
                p.setCedula(rs.getString("cedula"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));
                p.setClave(rs.getString("clave"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return p;
    }

    public ArrayList<String> listadoCorreoUsuariosTramiteNoFinalizados() {
        ArrayList<String> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT usuario.correo FROM usuario \n"
                    + "INNER JOIN solicitud ON usuario.id_usuario=solicitud.id_usuario \n"
                    + "INNER JOIN estados_solicitud ON solicitud.id_rol=estados_solicitud.id_estados_solicitud \n"
                    + "WHERE estados_solicitud.id_estados_solicitud!=3 GROUP BY usuario.id_usuario";
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
        return listado;
    }

    public ArrayList<String> listadoCorreoFuncionarios() {
        ArrayList<String> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT usuario.correo FROM usuario \n"
                    + "INNER JOIN tipo_usuario ON tipo_usuario.id_usuario=usuario.id_usuario\n"
                    + "WHERE tipo_usuario.id_rol!=3";
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
                    + "<div> <table border=0 bordercolor=black align=center width=600 cellpadding=1 cellspacing=1 style=\"background-image: url(http://186.46.57.100:8084/servicios_ciudadanos/FRONT/img/correoo.jpg);\" height=\"350\">\n"
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

            try (Transport transport = session.getTransport("smtp")) {
                transport.connect("smtp.gmail.com", remitente, "@sa123456");
                transport.sendMessage(message, message.getAllRecipients());
            }
            estado = true;
        } catch (MessagingException ex) {
            System.out.println(ex);
        }
        return estado;
    }
}

//conexion_servicios enl=new conexion_servicios();
//        ArrayList<String>listado=enl.listadoCorreoFuncionarios();
//        for(String correo:listado){
//            enl.enviarCorreo(correo, "Notificación por servicios", " ");
//        }
