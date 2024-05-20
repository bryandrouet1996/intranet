/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.documento_informe;
import modelo.firma_informe;
import modelo.informe_viatico;
import modelo.participacion_informe;
import modelo.ruta_informe;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_informeViatico", urlPatterns = {"/administrar_informeViatico.control"})
public class administrar_informeViatico extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        conexion enlace = new conexion();
        String accion = request.getParameter("accion");
        PrintWriter out = response.getWriter();
        if (accion.equalsIgnoreCase("registrar")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int idViatico = Integer.parseInt(request.getParameter("vt"));
            String descripcion = request.getParameter("areadescripcion");
            String numero_solicitud = request.getParameter("txtnumerosolicitud");
            java.sql.Date fecha_solicitud = Date.valueOf(request.getParameter("txtfechasolicitud"));
            int id_responsable = Integer.parseInt(request.getParameter("comboresponsable"));
            int id_jefe = Integer.parseInt(request.getParameter("combojefe"));
            informe_viatico elemento = new informe_viatico(idViatico, numero_solicitud, descripcion, fecha_solicitud, 1);
            try {
                if (enlace.registroInformeViatico(elemento)) {
                    informe_viatico busca = enlace.buscaInformeViatico(numero_solicitud);
                    enlace.traspasoRutaViaticoInforme(idViatico, busca.getId_informe());
                    enlace.traspasoParticipantesViaticoInforme(idViatico, busca.getId_informe());
                    firma_informe eleme = new firma_informe(busca.getId_informe(), id_responsable, id_jefe);
                    if (enlace.registroFirmaInforme(eleme)) {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        }
                    } else {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                        }
                    }
                } else {
                    informe_viatico busca = enlace.buscaInformeViatico(numero_solicitud);
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                informe_viatico busca = enlace.buscaInformeViatico(numero_solicitud);
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=7&vt=" + idViatico + "&ii=" + busca.getId_informe());
                }
            }
        } else if (accion.equalsIgnoreCase("modificar")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            String descripcion = request.getParameter("areadescripcion");
            int id_responsable = Integer.parseInt(request.getParameter("comboresponsable"));
            int id_jefe = Integer.parseInt(request.getParameter("combojefe"));
            informe_viatico elemento = new informe_viatico();
            elemento.setDescripcion(descripcion);
            if (enlace.actualizarInformeViatico(elemento, id_informe)) {
                firma_informe eleme = new firma_informe();
                eleme.setId_jefe(id_jefe);
                eleme.setId_responsable(id_responsable);
                try {
                    if (enlace.ActualizarFirmaInforme(id_informe, eleme)) {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                        }
                    } else {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            } else {
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_servidor")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            int id_servidor = Integer.parseInt(request.getParameter("serv"));
            participacion_informe elemento = new participacion_informe(id_informe, id_servidor);
            try {
                if (!enlace.exiteParticipanteInforme(id_informe, id_servidor)) {
                    if (enlace.registroParticipanteInforme(elemento)) {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                        }
                    } else {
                        if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                        } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                        }
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            }
        } else if (accion.equalsIgnoreCase("eliminar_participante")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            int id_usuario = Integer.parseInt(request.getParameter("us"));
            if (enlace.eliminarParticipanteInforme(id_usuario)) {
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            } else {
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_transporte")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            String tipo_transporte = request.getParameter("combotipotransporte");
            String nombre_transporte = request.getParameter("txtnombretransporte");
            int lugar_partida = Integer.parseInt(request.getParameter("combolugarpartida"));
            java.sql.Date fecha_salida = Date.valueOf(request.getParameter("txtfechasalida"));
            String hora_salida = request.getParameter("txthorasalida");
            int lugar_destino = Integer.parseInt(request.getParameter("combolugardestino"));
            java.sql.Date fecha_llegada = Date.valueOf(request.getParameter("txtfechallegada"));
            String hora_llegada = request.getParameter("txthorallegada");
            ruta_informe elemento = new ruta_informe(id_informe, lugar_partida, lugar_destino, tipo_transporte, nombre_transporte, fecha_salida, hora_salida, fecha_llegada, hora_llegada);
            try {
                if (enlace.registroRutaInforme(elemento)) {
                    enlace.actualizarEstadoViatico(id_informe, 1);
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            }
        } else if (accion.equalsIgnoreCase("modificar_transporte")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            int id_ruta = Integer.parseInt(request.getParameter("txtid"));
            String tipo_transporte = request.getParameter("combotipotransporte");
            String nombre_transporte = request.getParameter("txtnombretransporte");
            String lugar_partida = null;
            java.sql.Date fecha_salida = Date.valueOf(request.getParameter("txtfechasalida"));
            String hora_salida = request.getParameter("txthorasalida");
            String lugar_destino = null;
            java.sql.Date fecha_llegada = Date.valueOf(request.getParameter("txtfechallegada"));
            String hora_llegada = request.getParameter("txthorallegada");
            int partida = 0;
            int destino = 0;
            if (!request.getParameter("combolugarpartida").equalsIgnoreCase("")) {
                lugar_partida=request.getParameter("combolugarpartida");
                partida = Integer.parseInt(lugar_partida);
            }
            if (!request.getParameter("combolugardestino").equalsIgnoreCase("")) {
                lugar_destino=request.getParameter("combolugardestino");
                destino = Integer.parseInt(lugar_destino);
            }
            ruta_informe elemento = new ruta_informe(id_informe, partida, destino, tipo_transporte, nombre_transporte, fecha_salida, hora_salida, fecha_llegada, hora_llegada);
            if (elemento.getId_lugarllegada() != 0 && elemento.getId_lugarpartida() != 0) {
                if (enlace.actualizarRutaInforme(elemento, id_ruta)) {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            } else if (elemento.getId_lugarllegada()==0 && elemento.getId_lugarpartida()!=0) {
                if (enlace.actualizarRutaInformeLugarPartida(elemento, id_ruta)) {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            } else if (elemento.getId_lugarpartida()==0 && elemento.getId_lugarllegada()!=0) {
                if (enlace.actualizarRutaInformeLugarLlegada(elemento, id_ruta)) {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            }else if(elemento.getId_lugarllegada() == 0 && elemento.getId_lugarpartida() == 0){
                if (enlace.actualizarRutaInformeInformacion(elemento, id_ruta)) {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                } else {
                    if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                    } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                    }
                }
            }
        } else if (accion.equalsIgnoreCase("eliminar_ruta")) {
            int id_us = Integer.parseInt(request.getParameter("idUsuario"));
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            int id_ruta = Integer.parseInt(request.getParameter("rut"));
            if (enlace.eliminarRutaInforme(id_ruta)) {
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            } else {
                if (enlace.tipoUsuario(id_us).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp?step=13&ii=" + id_informe);
                } else if (enlace.tipoUsuario(id_us).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp?step=13&ii=" + id_informe);
                }
            }
        } else if (accion.equalsIgnoreCase("registro_documento")) {
            int id_informe = Integer.parseInt(request.getParameter("ii"));
            String razon_social = request.getParameter("txtrazonsocial");
            String ruc = request.getParameter("txtruc");
            String numero = request.getParameter("txtnumero");
            java.sql.Date fecha = Date.valueOf(request.getParameter("txtfecha"));
            String descripcion = request.getParameter("areadescripcion");
            double total = Double.parseDouble(request.getParameter("txttotal"));
            documento_informe elemento = new documento_informe(id_informe, razon_social, ruc, numero, fecha, descripcion, total);
            System.out.println(elemento);
            try {
                if (enlace.registroDocumentoInforme(elemento)) {
                    out.print("ok");
                } else {
                   
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("modificar_documento")) {
            int id = Integer.parseInt(request.getParameter("doc"));
            String razon_social = request.getParameter("txtrazonsocial");
            String ruc = request.getParameter("txtruc");
            String numero = request.getParameter("txtnumero");
            java.sql.Date fecha = Date.valueOf(request.getParameter("txtfecha"));
            String descripcion = request.getParameter("areadescripcion");
            double total = Double.parseDouble(request.getParameter("txttotal"));
            documento_informe elemento = new documento_informe();
            elemento.setRazon_social(razon_social);
            elemento.setFecha(fecha);
            elemento.setNumero(numero);
            elemento.setDescripcion(descripcion);
            elemento.setTotal(total);
            elemento.setRuc(ruc);
            try {
                if (enlace.ActualizarDocumentoInforme(id, elemento)) {
                    out.print("ok");
                } else {
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } else if (accion.equalsIgnoreCase("eliminar_documento")) {
            int id_doc = Integer.parseInt(request.getParameter("doc"));
            if (enlace.eliminarDocumentoInformeID(id_doc)) {
               out.print("ok");
            } else {
                
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
