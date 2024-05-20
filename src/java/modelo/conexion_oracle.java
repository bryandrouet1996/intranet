/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Kevin Druet
 */
public class conexion_oracle {

    private static Connection enlace = null;
    private static final String driver = "oracle.jdbc.driver.OracleDriver";
    private static final String usuario = "INTRANET";//PRODUCCIÓN
    private static final String contrasena = "INTRANET";//PRODUCCIÓN
    private static final String url = "jdbc:oracle:thin:@192.168.120.15:1521:bdesme";//PRODUCCIÓN
//    private static final String usuario = "PRUEBA";//PRUEBA
//    private static final String contrasena = "1234";//PRUEBA
//    private static final String url = "jdbc:oracle:thin:@192.168.120.13:1521:bdesme";//PRUEBA
    private PreparedStatement st;
    private ResultSet rs;

    public conexion_oracle() {
        //enlace = null;
    }

    public void abrir_conexion() {
        try {
            if (enlace == null || !enlace.isValid(0)) {
                Class.forName(driver);
                enlace = DriverManager.getConnection(url, usuario, contrasena);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("abrir_conexion | " + ex);
            enlace = null;
        }
    }

    public void cierre_conexion() {
        try {
            enlace.close();
        } catch (SQLException ex) {
            System.out.println("cierre_conexion | " + ex);
        }
    }

    public ArrayList<informacion_usuario> listadoVacacionesDisponibleUsuario(String codigo_usuario) {
        abrir_conexion();
        ArrayList<informacion_usuario> listado = new ArrayList();
        String sentencia = "";
        try {
            sentencia = "SELECT * FROM v_nomprv01_disp_1 WHERE CODIGO_FUNCIONARIO=" + codigo_usuario + "";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new informacion_usuario(
                                rs.getString("CODIGO_FUNCIONARIO"),
                                rs.getDate("FECHA_INGRESO"),
                                rs.getString("PERIODO"),
                                rs.getInt("DIAS_HABILES"),
                                rs.getInt("DISPONIBILIDAD"),
                                rs.getInt("FINES_SEMANA"),
                                rs.getString("MODALIDAD"),
                                rs.getInt("DISPONIBILIDAD_HORAS"),
                                rs.getInt("DISPONIBILIDAD_MINUTOS"),
                                rs.getInt("DIAS_PENDIENTES"),
                                rs.getInt("HORAS_PENDIENTES"),
                                rs.getInt("MINUTOS_PENDIENTES")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoVacacionesDisponibleUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<PeriodoVaca> getPeriodosVacaUsuario(int cod) {
        abrir_conexion();
        ArrayList<PeriodoVaca> res = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "SELECT * FROM v_nomprv01_disp_1 WHERE CODIGO_FUNCIONARIO=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, cod);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(
                        new PeriodoVaca(
                                rs.getInt("CODIGO_FUNCIONARIO"),
                                rs.getString("PERIODO"),
                                rs.getString("MODALIDAD"),
                                rs.getInt("DIAS_HABILES"),
                                rs.getInt("FINES_SEMANA"),
                                rs.getInt("DISPONIBILIDAD"),
                                rs.getInt("DISPONIBILIDAD_HORAS"),
                                rs.getInt("DISPONIBILIDAD_MINUTOS"),
                                rs.getInt("DIAS_HABILES_DISPONIBLES"),
                                rs.getInt("DIAS_PENDIENTES"),
                                rs.getInt("HORAS_PENDIENTES"),
                                rs.getInt("MINUTOS_PENDIENTES"),
                                rs.getInt("DIAS_NHABILES_DISPONIBLES"),
                                rs.getDate("FECHA_PRIMREG")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getPeriodosVacaUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public informacion_usuario vacacionesUltimoPeriodoDisponibleUsuario(String codigo_usuario) {
        abrir_conexion();
        informacion_usuario elemento = null;
        String sentencia = "";
        try {
            sentencia = "SELECT * FROM v_nomprv01_disp_1 WHERE CODIGO_FUNCIONARIO=" + codigo_usuario + "";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                elemento = new informacion_usuario();
                elemento.setCodigo_funcionario(rs.getString("CODIGO_FUNCIONARIO"));
                elemento.setFecha_ingreso(rs.getDate("FECHA_INGRESO"));
                elemento.setPeriodo(rs.getString("CODIGO_PERIODO"));
                elemento.setDias_habiles(rs.getInt("DIAS_HABILES"));
                elemento.setFines_semana(rs.getInt("FINES_SEMANA"));
                elemento.setDisponibilidad(rs.getInt("DISPONIBILIDAD"));
                elemento.setModalidad(rs.getString("MODALIDAD"));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("vacacionesUltimoPeriodoDisponibleUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return elemento;
    }

    public String vacacionesTomadas(Date fecha_ini, Date fecha_fin, String codigo_usuario) {
        abrir_conexion();
        String datos = "";
        String sentencia = "";
        try {
            sentencia = "select COD, nvl(NUM_DIAS,to_number(((trunc(to_date(to_char(nomca01ffin,'rrrr-mm-dd'),'rrrr-mm-dd'))) - (trunc(to_date(to_char(nomca01fini,'rrrr-mm-dd'),'rrrr-mm-dd')))))) num_dias, nomca01fini,nomca01ffin, DESCRIPCION from v_vacaciones_tom\n"
                    + "where (trunc(to_date(to_char(nomca01fini,'rrrr-mm-dd'),'rrrr-mm-dd')) >= to_date('" + fecha_ini + "','rrrr-mm-dd')\n"
                    + "and trunc(to_date(to_char(nomca01fini,'rrrr-mm-dd'),'rrrr-mm-dd')) <= to_date('" + fecha_fin + "','rrrr-mm-dd'))\n"
                    + " and nvl(NUM_DIAS,to_number(((trunc(to_date(to_char(nomca01ffin,'rrrr-mm-dd'),'rrrr-mm-dd'))) - (trunc(to_date(to_char(nomca01fini,'rrrr-mm-dd'),'rrrr-mm-dd')))))) > 0\n"
                    + "and cod ='" + codigo_usuario + "'";

            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                datos = datos + rs.getString("DESCRIPCION") + " desde " + rs.getDate("nomca01fini") + " hasta " + rs.getDate("nomca01ffin") + " ===> " + rs.getString("NUM_DIAS") + " días " + "\n";
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("vacacionesTomadas | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return datos;
    }

    public int obtenerDisponibilidadUsuarioPeriodo(String periodo, String codigo_usuario) {
        abrir_conexion();
        int disponibilidad = 0;
        String sentencia = "";
        try {
            sentencia = "SELECT * FROM v_nomprv01_disp WHERE CODIGO_FUNCIONARIO='" + codigo_usuario + "'AND PERIODO='" + periodo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                disponibilidad = rs.getInt("DISPONIBILIDAD");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("obtenerDisponibilidadUsuarioPeriodo | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return disponibilidad;
    }

    public ArrayList<rol_individual> rolePagoIndividual(String codigo_usuario, String anio, String mes) {
        abrir_conexion();
        ArrayList<rol_individual> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "SELECT DISTINCT codigo_funcionario,anio,mes_caracter,mes_numero,trol02codi,"
                    + " descripcion_rol FROM v_rol_individual_1"
                    + " WHERE codigo_funcionario= '" + codigo_usuario + "' and anio= '" + anio + "' and mes_numero= '" + mes + "' ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                rol_individual elemento = new rol_individual();
                elemento.setCodigo_funcionario(rs.getString("codigo_funcionario"));
                elemento.setAnio(rs.getString("anio"));
                elemento.setMes_caracter(rs.getString("mes_caracter"));
                elemento.setMes_numero(rs.getString("mes_numero"));
                elemento.setId_descripcion(rs.getString("trol02codi"));
                elemento.setDescripcion(rs.getString("descripcion_rol"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("rolePagoIndividual | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public rol_reporte GetRolReport(int codigo_usuario, int anio, String mes, String des) {
        abrir_conexion();
        rol_reporte elemento = null;
        String sentencia = "";
        try {
            sentencia = "SELECT codigo_funcionario,nombre_funcionario,anio,mes_numero,descripcion_rol"
                    + " FROM v_rol_individual"
                    + " WHERE codigo_funcionario= " + codigo_usuario
                    + " and anio=" + anio
                    + " and mes_numero=" + mes
                    + " AND REPLACE(descripcion_rol,' ','_') ='" + des + "'"
                    + " group by codigo_funcionario,nombre_funcionario,anio,mes_numero,descripcion_rol";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            System.out.println("lectura");
            while (rs.next()) {
                System.out.println("while");
                elemento = new rol_reporte(rs.getInt("codigo_funcionario"),
                        rs.getInt("anio"),
                        rs.getString("mes_numero"),
                        rs.getString("descripcion_rol"),
                        rs.getString("nombre_funcionario"));
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("GetRolReport | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return elemento;
    }

    public ArrayList<rol_individual> rolePagoIndividualTrabajador(String codigo_usuario, String anio, String mes, String descripcion) {
        abrir_conexion();
        ArrayList<rol_individual> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "SELECT codigo_funcionario,anio,mes_caracter,mes_numero,trol02codi,"
                    + " descripcion_rol,nombre_funcionario,descripcion_ingreso,ingreso,"
                    + " descripcion_descuento, descuento FROM v_rol_individual"
                    + " WHERE codigo_funcionario= '" + codigo_usuario + "' and anio= '" + anio + "' and mes_numero= '" + mes + "' and trol02codi= '" + descripcion + "' ";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                rol_individual elemento = new rol_individual();
                elemento.setCodigo_funcionario(rs.getString("codigo_funcionario"));
                elemento.setAnio(rs.getString("anio"));
                elemento.setMes_caracter(rs.getString("mes_caracter"));
                elemento.setMes_numero(rs.getString("mes_numero"));
                elemento.setId_descripcion(rs.getString("trol02codi"));
                elemento.setDescripcion(rs.getString("descripcion_rol"));
                elemento.setDescripcion_ingreso(rs.getString("descripcion_ingreso"));
                elemento.setIngreso(rs.getDouble("ingreso"));
                elemento.setDescripcion_egreso(rs.getString("descripcion_descuento"));
                elemento.setEgreso(rs.getDouble("descuento"));
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("rolePagoIndividualTrabajador | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<periodo_vacaciones> listadoPeriodos(String codigo_funcionario) {
        abrir_conexion();
        ArrayList<periodo_vacaciones> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select a.periodo, a.dias from v_totdiasdisp a where nom01codi = '" + codigo_funcionario + "' union "
                    + "SELECT  TO_CHAR(SYSDATE,'YYYY')-LEVEL+1, TOTVACACIONES_PER('" + codigo_funcionario + "', TO_CHAR(SYSDATE,'YYYY')-LEVEL+1) dias FROM dual\n"
                    + "CONNECT BY LEVEL <= (select (to_char(sysdate,'rrrr')-b.periodo)\n"
                    + "from v_totdiasdisp b \n"
                    + "where b.nom01codi = '" + codigo_funcionario + "')";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                periodo_vacaciones elemento = new periodo_vacaciones();
                elemento.setDias_disponibles(rs.getInt("dias"));
                elemento.setAnio(rs.getInt("periodo"));
                System.out.println(elemento);
                listado.add(elemento);
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoPeriodos | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public boolean registroSolicitudVacaciones(permiso_vacaciones elemento) throws SQLException {
        boolean res = false;
        conexion mysql = new conexion();
        usuario funcionario = elemento.getId_usuario() == 0 ? null : mysql.buscar_usuarioID(elemento.getId_usuario());
        aprobacion_vacaciones aprobacion = mysql.obtenerAprobacionVacacionesID(elemento.getId_permiso());
        revision_vacaciones revision = mysql.obtenerRevisionVacacionesID(elemento.getId_permiso());
        int codigo_aprueba = mysql.consultarCodigoUsuario(revision.getId_usuario());
        int codigo_autoriza = consultarCodigoJefeUsuario(funcionario == null ? elemento.getCodigoUsu() : Integer.parseInt(funcionario.getCodigo_usuario()));
        informacion_usuario informacion = vacacionesUltimoPeriodoDisponibleUsuario(funcionario == null ? Integer.toString(elemento.getCodigoUsu()) : funcionario.getCodigo_usuario());
        abrir_conexion();
        try {
            st = enlace.prepareStatement("INSERT INTO nomca021(id_permiso,dias_habiles,dias_nohabiles,dias_recargo,dias_descuento,fecha_autorizacion,fecha_aprobacion,nomprv01codi,codigo_funcionario,codigo_funcionarioap,codigo_funcionarioau,fecha_salida,fecha_fin,fecha_retorno,dias_solicitados,id_motivo,observacion,fecha_creacion,id_usuario,tipo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'V')");
            st.setInt(1, elemento.getId_permiso());
            st.setDouble(2, elemento.getDias_habiles());
            int dif = (int) (elemento.getDias_recargo() - elemento.getDias_nohabiles());
            st.setDouble(3, dif < 0 ? 0 : elemento.getDias_nohabiles());
            st.setDouble(4, dif < 0 ? elemento.getDias_recargo() : dif);
            st.setDouble(5, elemento.getDias_descuento());
            st.setTimestamp(6, revision.getFecha_registro());
            st.setTimestamp(7, aprobacion.getFecha_registro());
            st.setInt(8, informacion == null ? 0 : Integer.parseInt(informacion.getPeriodo()));
            st.setInt(9, funcionario == null ? elemento.getCodigoUsu() : Integer.parseInt(funcionario.getCodigo_usuario()));
            st.setInt(10, codigo_aprueba);
            st.setInt(11, codigo_autoriza);
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(elemento.getFecha_inicio().toString() + "_08:00:00");
                st.setTimestamp(12, new Timestamp(utilDate.getTime()));
                utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(elemento.getFecha_fin().toString() + "_17:00:00");
                st.setTimestamp(13, new Timestamp(utilDate.getTime()));
                utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(elemento.getFecha_ingreso().toString() + "_08:00:00");
                st.setTimestamp(14, new Timestamp(utilDate.getTime()));
            } catch (Exception e) {
                st.setNull(12, Types.TIMESTAMP);
                st.setNull(13, Types.TIMESTAMP);
                st.setNull(14, Types.TIMESTAMP);
                System.out.println("parse utilDate | " + e);
            }
            st.setInt(15, elemento.getDias_solicitados());
            st.setInt(16, elemento.getCodigoMotivo());
            st.setString(17, elemento.getObservacion());
            st.setTimestamp(18, elemento.getFecha_creacion());
            st.setInt(19, elemento.getId_usuario());
            st.executeUpdate();
            st.close();
            res = true;
        } catch (NumberFormatException | SQLException e) {
            System.out.println("registroSolicitudVacaciones | " + e);
        }
        cierre_conexion();
        return res;
    }

    public boolean registroSolicitudHoras(permiso_horas elemento) throws SQLException {
        boolean res = false;
        conexion mysql = new conexion();
        usuario funcionario = elemento.getId_usuario() == 0 ? null : mysql.buscar_usuarioID(elemento.getId_usuario());
        RevisionHoras revision = mysql.obtenerRevisionHorasID(elemento.getId_permiso());
        AprobacionHoras aprobacion = mysql.obtenerAprobacionHorasID(elemento.getId_permiso());
        int codigo_aprueba = mysql.consultarCodigoUsuario(revision.getId_usuario());
        int codigo_autoriza = consultarCodigoJefeUsuario(funcionario == null ? elemento.getCodigoUsu() : Integer.parseInt(funcionario.getCodigo_usuario()));
        informacion_usuario informacion = vacacionesUltimoPeriodoDisponibleUsuario(funcionario == null ? Integer.toString(elemento.getCodigoUsu()) : funcionario.getCodigo_usuario());
        abrir_conexion();
        try {
            st = enlace.prepareStatement("INSERT INTO nomca021(id_permiso,dias_habiles,dias_nohabiles,dias_recargo,dias_descuento,fecha_autorizacion,fecha_aprobacion,nomprv01codi,codigo_funcionario,codigo_funcionarioap,codigo_funcionarioau,fecha_salida,fecha_fin,fecha_retorno,dias_solicitados,id_motivo,observacion,fecha_creacion,id_usuario,horas,minutos,tipo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'H')");
            st.setInt(1, elemento.getId_permiso());
            st.setDouble(2, elemento.getDias_habiles());
            st.setDouble(3, elemento.getDias_nohabiles());
            st.setDouble(4, elemento.getDias_recargo());
            st.setDouble(5, elemento.getDias_descuento());
            st.setTimestamp(6, revision.getFecha_registro());
            st.setTimestamp(7, aprobacion.getFecha_creacion());
            st.setInt(8, informacion == null ? 0 : Integer.parseInt(informacion.getPeriodo()));
            st.setInt(9, funcionario == null ? elemento.getCodigoUsu() : Integer.parseInt(funcionario.getCodigo_usuario()));
            st.setInt(10, codigo_aprueba);
            st.setInt(11, codigo_autoriza);
            st.setTimestamp(12, elemento.getTimestamp_inicio());
            st.setTimestamp(13, elemento.getTimestamp_fin());
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(elemento.getFecha_ingreso() + "_08:00:00");
                st.setTimestamp(14, elemento.getId_tipo() == 1 ? elemento.getTimestamp_fin() : new Timestamp(utilDate.getTime()));
            } catch (ParseException e) {
                st.setNull(14, Types.TIMESTAMP);
                System.out.println("parse utilDate | " + e);
            }
            st.setInt(15, elemento.getDias_solicitados());
            switch (elemento.getId_motivo()) {
                case 1:
                    st.setInt(16, 6829);
                    break;
                case 2:
                    st.setInt(16, 6823);
                    break;
                case 3:
                    st.setInt(16, 6822);
                    break;
                case 4:
                    st.setInt(16, 75401);
                    break;
                case 5:
                    st.setInt(16, 6828);
                    break;
                case 6:
                    st.setInt(16, 6825);
                    break;
                case 7:
                    st.setInt(16, 6826);
                    break;
                case 8:
                    st.setInt(16, 6827);
                    break;
                case 9:
                    st.setInt(16, 6879);
                    break;
                case 10:
                    st.setInt(16, 61586);
                    break;
                case 11:
                    st.setInt(16, 73665);
                    break;
                default:
                    st.setInt(16, 6829);
                    break;
            }
            st.setString(17, elemento.getObservacion());
            st.setTimestamp(18, elemento.getFecha_creacion());
            st.setInt(19, elemento.getId_usuario());
            st.setInt(20, elemento.getHoras());
            st.setInt(21, elemento.getMinutos());
            st.executeUpdate();
            st.close();
            res = true;
        } catch (NumberFormatException | SQLException e) {
            System.out.println("registroSolicitudHoras | " + e);
        }
        cierre_conexion();
        return res;
    }

    public boolean registroSolicitudManual(PermisoManual p) throws SQLException {
        boolean res = false;
        conexion mysql = new conexion();
        int codigo_aprueba = Integer.parseInt(mysql.buscar_usuarioID(p.getAdmin()).getCodigo_usuario());
        int codigo_autoriza = consultarCodigoJefeUsuario(p.getCodUsu());
        informacion_usuario informacion = vacacionesUltimoPeriodoDisponibleUsuario(Integer.toString(p.getCodUsu()));
        abrir_conexion();
        try {
            st = enlace.prepareStatement("INSERT INTO nomca021(id_permiso,dias_habiles,dias_nohabiles,dias_recargo,dias_descuento,fecha_autorizacion,fecha_aprobacion,nomprv01codi,codigo_funcionario,codigo_funcionarioap,codigo_funcionarioau,fecha_salida,fecha_fin,fecha_retorno,dias_solicitados,id_motivo,observacion,fecha_creacion,id_usuario,horas,minutos,tipo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'M')");
            st.setInt(1, p.getId());
            st.setDouble(2, p.getDiasHabiles());
            st.setDouble(3, p.getFinesSemana());
            st.setDouble(4, 0);
            st.setDouble(5, p.getDiasHabiles() + p.getFinesSemana());
            st.setTimestamp(6, p.getCreacion());
            st.setTimestamp(7, p.getCreacion());
            st.setInt(8, informacion == null ? 0 : Integer.parseInt(informacion.getPeriodo()));
            st.setInt(9, p.getCodUsu());
            st.setInt(10, codigo_aprueba);
            st.setInt(11, codigo_autoriza);
            st.setTimestamp(12, p.getFechaInicio());
            st.setTimestamp(13, p.getFechaFin());
            st.setTimestamp(14, p.getFechaRetorno());
            st.setInt(15, p.getDiasHabiles() + p.getFinesSemana());
            st.setInt(16, 6824);
            st.setString(17, p.getObservacion());
            st.setTimestamp(18, p.getCreacion());
            st.setInt(19, p.getAdmin());
            st.setInt(20, p.getHoras());
            st.setInt(21, p.getMinutos());
            st.executeUpdate();
            st.close();
            res = true;
        } catch (NumberFormatException | SQLException e) {
            System.out.println("registroSolicitudManual | " + e);
        }
        cierre_conexion();
        return res;
    }

    public String consultarDenominacionUsuario(int codigo) {
        abrir_conexion();
        String res = "DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO";
        String sentencia = "";
        try {
            sentencia = "SELECT NOM_CARGOEMP('" + codigo + "') AS RES FROM DUAL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarDenominacionUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public String consultarDireccionUsuario(int codigo) {
        abrir_conexion();
        String res = "DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO";
        String sentencia = "";
        try {
            sentencia = "SELECT NOM_DIREMP('" + codigo + "') AS RES FROM nom01 WHERE nom01codi='" + codigo + "'";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarDireccionUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public String consultarUnidadUsuario(int codigo) {
        abrir_conexion();
        String res = "DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO";
        String sentencia = "";
        try {
            sentencia = "SELECT NOM_UNIDADEMP('" + codigo + "') AS RES FROM DUAL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarUnidadUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public String consultarJefeUsuario(int codigo) {
        abrir_conexion();
        String res = "DATO DESACTUALIZADO, ACÉRQUESE A TALENTO HUMANO";
        String sentencia = "";
        try {
            sentencia = "SELECT NOM_JEFEUNIDADEMP('" + codigo + "') AS RES FROM DUAL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                if (!rs.getString("RES").equals("Error")) {
                    res = rs.getString("RES");
                }
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarJefeUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public int consultarCodigoJefeUsuario(int codigo) {
        abrir_conexion();
        int res = 0;
        String sentencia = "";
        try {
            sentencia = "SELECT NOM_CODJEFEUNIDADEMP('" + codigo + "') AS RES FROM DUAL";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarCodigoJefeUsuario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public ArrayList<seguimiento_tributario> getEmisionesRecaudadas(int year) {
        abrir_conexion();
        ArrayList<seguimiento_tributario> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, pk_uti.find_impuesto(emi01seri), count(emi01codi) cantidad,sum((emi01vtot + nvl(emi01inte,0) + nvl(emi01reca,0) + nvl(emi01coa,0))-nvl(emi01desc,0)) valor_total, emi01anio,\n"
                    + "       case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Recaudado'\n"
                    + "       end estado\n"
                    + "from emi01\n"
                    + "where emi01esta in ('R','J')\n"
                    + "and emi01anio = ?\n"
                    + "group by emi01seri, case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Recaudado'\n"
                    + "       end, emi01anio\n"
                    + "order by 5,2";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, year);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new seguimiento_tributario(rs.getInt("emi01seri"),
                        rs.getString("pk_uti.find_impuesto(emi01seri)"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getEmisionesRecaudadas | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ComponenteImpuesto> getComponentesEmisionesRecaudadas(int year, int idImpuesto) {
        System.out.println("idImpuesto " + idImpuesto);
        abrir_conexion();
        ArrayList<ComponenteImpuesto> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select a.emi01seri, pk_uti.find_impuesto(a.emi01seri) impuesto,  \n"
                    + "       b.emi04codi, (select emi04desd \n"
                    + "                     from emi04 \n"
                    + "                     where emi04.emi04codi = b.emi04codi) componente\n"
                    + "from emi01 a, emi02 b\n"
                    + "where a.emi01esta in ('R','J')\n"
                    + "and a.emi01anio = ?\n"
                    + "and a.emi01codi = b.emi01codi\n"
                    + "and a.emi01seri = ?\n"
                    + "group by a.emi01seri, b.emi04codi";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, year);
            st.setInt(2, idImpuesto);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ComponenteImpuesto(rs.getInt("b.emi04codi"),
                                rs.getInt("a.emi01seri"),
                                rs.getString("impuesto")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("getComponentesEmisionesRecaudadas | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<seguimiento_tributario> listadoEmisiones(int y) {
        abrir_conexion();
        ArrayList<seguimiento_tributario> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, pk_uti.find_impuesto(emi01seri), count(emi01codi) cantidad,\n"
                    + "       sum((emi01vtot + nvl(emi01inte,0) + nvl(emi01reca,0) + nvl(emi01coa,0))-nvl(emi01desc,0)) valor_total, emi01anio,\n"
                    + "       case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Abono'\n"
                    + "       end estado, emi01esta\n"
                    + "from emi01\n"
                    + "where emi01esta = 'E'\n"
                    + "and emi01anio = '" + y + "'\n"
                    + "group by emi01seri, emi01esta, emi01anio\n"
                    + "order by 5,2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new seguimiento_tributario(rs.getInt("emi01seri"),
                        rs.getString("pk_uti.find_impuesto(emi01seri)"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoEmisiones | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<seguimiento_tributario> listadoEmisiones1(int y) {
        abrir_conexion();
        ArrayList<seguimiento_tributario> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, pk_uti.find_impuesto(emi01seri), count(emi01codi) cantidad,\n"
                    + "       sum((emi01vtot + nvl(emi01inte,0) + nvl(emi01reca,0) + nvl(emi01coa,0))-nvl(emi01desc,0)) valor_total, emi01anio,\n"
                    + "       case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Abono'\n"
                    + "       end estado, emi01esta\n"
                    + "from emi01\n"
                    + "where emi01esta in ('R','J')\n"
                    + "and emi01anio = '" + y + "'\n"
                    + "group by emi01seri, emi01esta, emi01anio\n"
                    + "order by 5,2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new seguimiento_tributario(rs.getInt("emi01seri"),
                        rs.getString("pk_uti.find_impuesto(emi01seri)"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoEmisiones1 | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<seguimiento_tributario> listadoEmisiones2(int y) {
        abrir_conexion();
        ArrayList<seguimiento_tributario> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, pk_uti.find_impuesto(emi01seri), count(emi01codi) cantidad,\n"
                    + "       sum((emi01vtot + nvl(emi01inte,0) + nvl(emi01reca,0) + nvl(emi01coa,0))-nvl(emi01desc,0)) valor_total, emi01anio,\n"
                    + "       case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Abono'\n"
                    + "       end estado, emi01esta\n"
                    + "from emi01\n"
                    + "where emi01esta in ('B')\n"
                    + "and emi01anio = '" + y + "'\n"
                    + "group by emi01seri, emi01esta, emi01anio\n"
                    + "order by 5,2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new seguimiento_tributario(rs.getInt("emi01seri"),
                        rs.getString("pk_uti.find_impuesto(emi01seri)"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoEmisiones2 | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<seguimiento_tributario> listadoEmisiones3(int y) {
        abrir_conexion();
        ArrayList<seguimiento_tributario> listado = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, pk_uti.find_impuesto(emi01seri), count(emi01codi) cantidad,\n"
                    + "       sum((emi01vtot + nvl(emi01inte,0) + nvl(emi01reca,0) + nvl(emi01coa,0))-nvl(emi01desc,0)) valor_total, emi01anio,\n"
                    + "       case emi01esta\n"
                    + "           when 'R' then 'Recaudado'\n"
                    + "           when 'B' then 'Baja'\n"
                    + "           when 'E' then 'Pendiente'\n"
                    + "           when 'J' then 'Recaudado'\n"
                    + "           when 'A' then 'Abono'\n"
                    + "           when 'RE' then 'Recaudado'\n"
                    + "       end estado, emi01esta\n"
                    + "from ncr02\n"
                    + "where id_ncr01 in (select id from ncr01 where estado = 'AP')\n"
                    + "and emi01anio = '" + y + "'\n"
                    + "group by emi01seri, emi01esta, emi01anio\n"
                    + "order by 5,2";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new seguimiento_tributario(rs.getInt("emi01seri"),
                        rs.getString("pk_uti.find_impuesto(emi01seri)"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoEmisiones3 | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<Desglose> listadoEmisionesConceptoMes(int idConcepto, int year) {
        abrir_conexion();
        ArrayList<Desglose> res = new ArrayList<>();
        String sentencia = "";
        try {
            sentencia = "select emi01seri, impuesto, cantidad, valor_total, emi01anio, estado, mes_numero, mes \n"
                    + "from v_seguimiento_tributario\n"
                    + "where emi01anio = " + year + "\n"
                    + "and EMI01SERI = " + idConcepto + "";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Desglose(rs.getString("mes"),
                        rs.getString("estado"),
                        rs.getInt("cantidad"),
                        rs.getFloat("valor_total"))
                );
            }
            st.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("listadoEmisionesConceptoMes | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }

    public ArrayList<Direccion> getDirecciones() {
        abrir_conexion();
        ArrayList<Direccion> res = new ArrayList<>();
        try {
            String sentencia = "select ltrim(rtrim(descripcion)) descripcion, seg04codi from v_departamentos1\n"
                    + "where seg04codi in (select seg04codi\n"
                    + "from v_departamentos1\n"
                    + "having length(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(seg04codi,1,''),2,''),3,''),4,''),5,''),6,''),7,''),8,''),9,''),0,'')) = 2\n"
                    + "group by seg04codi)";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res.add(new Direccion(rs.getString("SEG04CODI"),
                        rs.getString("DESCRIPCION")));
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("getDirecciones | " + e);
        }
        cierre_conexion();
        return res;
    }

    public String consultarCodigoDireccion(String nombre) {
        ArrayList<Direccion> direcciones = getDirecciones();
        for (Direccion dir : direcciones) {
            if (dir.getNombre().equals(nombre)) {
                return dir.getId();
            }
        }
        return "0";
    }

    public String consultarDireccion(String codigo) {
        ArrayList<Direccion> direcciones = getDirecciones();
        for (Direccion dir : direcciones) {
            if (dir.getId().equals(codigo)) {
                return dir.getNombre();
            }
        }
        return "";
    }

    public String consultarJefeDireccion(String codigo) {
        abrir_conexion();
        String res = "SIN JEFE";
        try {
            String sentencia = "select f_nomjefedir('" + codigo + "') AS RES from dual";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarDireccionUsuario | " + e);
        }
        cierre_conexion();
        return res;
    }

    public int consultarCodigoJefeDireccion(String codigo) {
        abrir_conexion();
        int res = 0;
        try {
            String sentencia = "select f_codjefedir('" + codigo + "') AS RES from dual";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("consultarCodigoJefeDireccion | " + e);
        }
        cierre_conexion();
        return res;
    }

    public boolean comprobarPendientes(ArrayList<PeriodoVaca> periodos) {
        for (PeriodoVaca p : periodos) {
            if (p.getDiasPend() == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean anularPermiso(int id, String tipo) throws SQLException {
        boolean res = false;
        abrir_conexion();
        try {
            st = enlace.prepareStatement("{CALL p_anulapermiso(?,?)}");
            st.setInt(1, id);
            st.setString(2, tipo);
            st.executeUpdate();
            st.close();
            res = true;
        } catch (NumberFormatException | SQLException e) {
            System.out.println("anularPermiso | " + e);
        }
        cierre_conexion();
        return res;
    }

    public ArrayList<ReportePermiso> getPermisos() {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE TO_CHAR(fecha_inicio, 'rrrr') = TO_CHAR(SYSDATE, 'rrrr') AND (TO_CHAR(fecha_inicio, 'mm') = TO_CHAR(SYSDATE, 'mm') OR TO_CHAR(fecha_fin, 'mm') = TO_CHAR(SYSDATE, 'mm'))";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisosDireccion(String direccion) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE cod_direccion=? AND TO_CHAR(fecha_inicio, 'rrrr') = TO_CHAR(SYSDATE, 'rrrr') AND (TO_CHAR(fecha_inicio, 'mm') = TO_CHAR(SYSDATE, 'mm') OR TO_CHAR(fecha_fin, 'mm') = TO_CHAR(SYSDATE, 'mm'))";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, direccion);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisosDireccion | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisos(int codigoUsu) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE codigo_funcionario=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigoUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos(codigoUsu) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisosDireccion(String direccion, int codigoUsu) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE cod_direccion=? AND codigo_funcionario=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, direccion);
            st.setInt(2, codigoUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisosDireccion(codigoUsu) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisos(int codigoUsu, String motivo, boolean incluye) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_detaus1 WHERE codigo_funcionario=? AND " + (incluye ? "descripcion=?" : "descripcion NOT LIKE ?");
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigoUsu);
            st.setString(2, motivo);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos(codigoUsu) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisos(java.sql.Date fechaIni, java.sql.Date fechaFin) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE ("
                    + "(fecha_inicio between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr') or fecha_fin between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr')) or "
                    + "(to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin and to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin)"
                    + ")";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            st.setDate(5, fechaIni);
            st.setDate(6, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos(fechaIni, fechaFin) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisosDireccion(String direccion, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "SELECT * FROM v_detaus1 WHERE cod_direccion=? AND ("
                    + "(fecha_inicio between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr') or fecha_fin between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr')) or "
                    + "(to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin and to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin)"
                    + ")";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, direccion);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            st.setDate(4, fechaIni);
            st.setDate(5, fechaFin);
            st.setDate(6, fechaIni);
            st.setDate(7, fechaFin);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisosDireccion(fechaIni, fechaFin) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisos(int codigoUsu, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_detaus1 WHERE ("
                    + "(fecha_inicio between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr') or fecha_fin between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr')) or "
                    + "(to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin and to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin)"
                    + ") AND codigo_funcionario=?";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, fechaIni);
            st.setDate(2, fechaFin);
            st.setDate(3, fechaIni);
            st.setDate(4, fechaFin);
            st.setDate(5, fechaIni);
            st.setDate(6, fechaFin);
            st.setInt(7, codigoUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos(codigoUsu, fechaIni, fechaFin) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<ReportePermiso> getPermisosDireccion(String direccion, int codigoUsu, java.sql.Date fechaIni, java.sql.Date fechaFin) {
        abrir_conexion();
        ArrayList<ReportePermiso> listado = new ArrayList();
        try {
            String sentencia = "SELECT * FROM v_detaus1 WHERE cod_direccion=? AND ("
                    + "(fecha_inicio between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr') or fecha_fin between to_date(?,'dd-mm-rrrr') and to_date(?,'dd-mm-rrrr')) or "
                    + "(to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin and to_date(?,'dd-mm-rrrr') between fecha_inicio and fecha_fin)"
                    + ") AND codigo_funcionario=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, direccion);
            st.setDate(2, fechaIni);
            st.setDate(3, fechaFin);
            st.setDate(4, fechaIni);
            st.setDate(5, fechaFin);
            st.setDate(6, fechaIni);
            st.setDate(7, fechaFin);
            st.setInt(8, codigoUsu);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new ReportePermiso(
                                rs.getString("funcionario"),
                                rs.getInt("periodo"),
                                rs.getInt("num_dias"),
                                rs.getInt("num_horas"),
                                rs.getInt("num_minutos"),
                                rs.getTimestamp("fecha_inicio"),
                                rs.getTimestamp("fecha_fin"),
                                rs.getTimestamp("fecha_retorno"),
                                rs.getString("descripcion"),
                                rs.getString("regimen"),
                                rs.getString("departamento")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getPermisos(codigoUsu, fechaIni, fechaFin) | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public String getRegimen(int codigo) {
        abrir_conexion();
        String res = "NO DEFINIDO";
        try {
            String sentencia = "select pk_nomtitulos.FD_REGIMEN(pk_nomtitulos.F_REGACTIVO(nom01codi)) regimen from nom01 where nom01codi=?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("regimen");
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getRegimen(codigo) | " + ex);
        }
        cierre_conexion();
        return res;
    }

    public ArrayList<UsuarioIESS> getUsuarios() {
        abrir_conexion();
        ArrayList<UsuarioIESS> listado = new ArrayList();
        try {
            String sentencia;
            sentencia = "select codigo_empleado codigo, nombre_empleado nombres, regimen from v_empleados";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(new UsuarioIESS(
                        rs.getInt("codigo"),
                        rs.getString("nombres"),
                        rs.getString("regimen")
                ));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuarios | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public usuario getUsuarioActivo(String cedula){
        abrir_conexion();
        usuario res = null;
        try {
            String sentencia = "select codigo_empleado codigo from v_empleados where estado_empleado=1 and cedula_empleado=?";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, cedula);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new usuario();
                res.setCodigo_usuario(rs.getString("codigo"));
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuarioActivo(cedula) | " + ex);
        }
        cierre_conexion();
        return res;
    }
    
    public ArrayList<UsuarioIESS> getUsuariosActivos() {
        abrir_conexion();
        ArrayList<UsuarioIESS> listado = new ArrayList();
        try {
            String sentencia = "select codigo_empleado codigo, nombre_empleado nombres, regimen from v_empleados where estado_empleado = 1";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new UsuarioIESS(
                                rs.getInt("codigo"),
                                rs.getString("nombres"),
                                rs.getString("regimen")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuariosActivos | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public ArrayList<UsuarioIESS> getUsuariosActivosIESS() {
        abrir_conexion();
        ArrayList<UsuarioIESS> listado = new ArrayList();
        try {
            String sentencia = "select codigo_empleado codigo, nombre_empleado nombres, regimen from v_empleados where estado_empleado = 1 and (regimen like '%INDEFINIDOS%' or regimen like '%TRABAJADORES%')";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new UsuarioIESS(
                                rs.getInt("codigo"),
                                rs.getString("nombres"),
                                rs.getString("regimen")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuariosActivosIESS | " + ex);
        }
        cierre_conexion();
        return listado;
    }
    
    public ArrayList<UsuarioIESS> getUsuariosActivosBiometrico() {
        abrir_conexion();
        ArrayList<UsuarioIESS> listado = new ArrayList();
        try {
            String sentencia = "select codigo_empleado codigo, nombre_empleado nombres, regimen from v_empleados where estado_empleado = 1 and regimen not like '%JUBIL%' order by 2 asc";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                listado.add(
                        new UsuarioIESS(
                                rs.getInt("codigo"),
                                rs.getString("nombres"),
                                rs.getString("regimen")
                        )
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuariosActivosBiometrico | " + ex);
        }
        cierre_conexion();
        return listado;
    }

    public UsuarioIESS getUsuario(int codigo) {
        abrir_conexion();
        UsuarioIESS res = new UsuarioIESS();
        try {
            String sentencia = "select codigo_empleado codigo, nombre_empleado nombres, regimen, cedula_empleado cedula from v_empleados where codigo_empleado = ?";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = new UsuarioIESS(
                        rs.getInt("codigo"),
                        rs.getString("nombres"),
                        rs.getString("regimen"),
                        rs.getString("cedula")
                );
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getUsuario | " + ex);
        }
        cierre_conexion();
        return res;
    }

    public Date getFechaIngreso(int codigo) {
        ArrayList<PeriodoVaca> vacas = getPeriodosVacaUsuario(codigo);
        Date res = null;
        if (vacas.size() > 0) {
            abrir_conexion();
            try {
                String sentencia = "select NOM_FECREGINI(?,?) AS RES from dual";
                st = enlace.prepareStatement(sentencia);
                st.setInt(1, codigo);
                st.setInt(2, Integer.parseInt(vacas.get(0).getPeriodo().split("-")[0]));
                rs = st.executeQuery();
                while (rs.next()) {
                    res = rs.getDate("RES");
                }
                st.close();
                rs.close();
            } catch (SQLException e) {
                System.out.println("getFechaIngreso | " + e);
            }
            cierre_conexion();
        }
        return res;
    }

    public String getNombreCargo(String codigoCargo) {
        abrir_conexion();
        String res = "";
        try {
            String sentencia = "select F_EMPLEADOCAR(?) AS RES from dual";
            st = enlace.prepareStatement(sentencia);
            st.setString(1, codigoCargo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("getNombreCargo | " + e);
        }
        cierre_conexion();
        return res;
    }

    public boolean verificarHorario(int codigo) {
        abrir_conexion();
        boolean res = true;
        String sentencia = "";
        try {
            sentencia = "SELECT f_horario(?) AS RES FROM DUAL";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getInt("RES") == 1;
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("verificarHorario | " + sentencia + " | " + e);
        }
        cierre_conexion();
        return res;
    }
    
    public double getIngresos(int codigo) {
        abrir_conexion();
        double res = 0;
        try {
            String sentencia = "SELECT F_NOM_GPSRI(?) AS RES FROM dual";
            st = enlace.prepareStatement(sentencia);
            st.setInt(1, codigo);
            rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getDouble("RES");
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("getIngresos | " + e);
        }
        cierre_conexion();
        return res;
    }
}
