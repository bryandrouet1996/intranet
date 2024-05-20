/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.conexion;
import modelo.evento;

/**
 *
 * @author Kevin Druet
 */
@WebServlet(name = "administrar_evento", urlPatterns = {"/administrar_evento.control"})
public class administrar_evento extends HttpServlet {

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
        if (accion.equalsIgnoreCase("registrar")) {
            int idUsuario = Integer.parseInt(request.getParameter("iu"));
            String titulo = request.getParameter("title");
            String color = request.getParameter("color");
            java.sql.Date inicio = Date.valueOf(request.getParameter("start"));
            java.sql.Date fin = Date.valueOf(request.getParameter("end"));
            int idEstado = Integer.parseInt(request.getParameter("estado"));
            String hora_inicio = request.getParameter("starthora");
            String hora_fin = request.getParameter("endhora");
            evento elemento = new evento(idUsuario, titulo, color, inicio, hora_inicio, fin, hora_fin,enlace.fechaActual(), idEstado);
            try {
                if (enlace.registroEvento(elemento)) {
                    if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp");
                    }
                } else {
                    if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp");
                }
            }
        } else if (accion.equalsIgnoreCase("modificar")) {
            int idUsuario = Integer.parseInt(request.getParameter("iu"));
            int id_evento = Integer.parseInt(request.getParameter("id"));
            String titulo = request.getParameter("title");
            String color = request.getParameter("color");
            String starthora = request.getParameter("starthora");
            String endhora = request.getParameter("endhora");
            String check = request.getParameter("checkdelete");
            evento elemento = new evento();
            elemento.setColor(color);
            elemento.setTitulo(titulo);
            elemento.setHora_inicio(starthora);
            elemento.setHora_fin(endhora);
            try {
                if (enlace.usuarioPublicacionEvento(id_evento) == idUsuario) {
                    if (check != null) {
                        if (enlace.eliminarEventoId(id_evento)) {
                            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                                response.sendRedirect("principal.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                                response.sendRedirect("principal_dir.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                                response.sendRedirect("principal_fun.jsp");
                            }
                        } else {
                            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                                response.sendRedirect("principal.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                                response.sendRedirect("principal_dir.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                                response.sendRedirect("principal_fun.jsp");
                            }
                        }
                    } else {
                        if (enlace.ActualizarEventoId(id_evento, elemento)) {
                            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                                response.sendRedirect("principal.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                                response.sendRedirect("principal_dir.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                                response.sendRedirect("principal_fun.jsp");
                            }
                        } else {
                            if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                                response.sendRedirect("principal.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                                response.sendRedirect("principal_dir.jsp");
                            } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                                response.sendRedirect("principal_fun.jsp");
                            }
                        }
                    }
                } else {
                    if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                        response.sendRedirect("principal.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                        response.sendRedirect("principal_dir.jsp");
                    } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                        response.sendRedirect("principal_fun.jsp");
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp");
                }
            }
        } else if (accion.equalsIgnoreCase("modificarfecha")) {
            int idUsuario = Integer.parseInt(request.getParameter("iu"));
            int id_evento = Integer.parseInt(request.getParameter("id"));
            java.sql.Date inicio = Date.valueOf(request.getParameter("start"));
            java.sql.Date fin = Date.valueOf(request.getParameter("end"));
            evento elemento = new evento();
            elemento.setInicio(inicio);
            elemento.setFin(fin);
            try {
                if (enlace.usuarioPublicacionEvento(id_evento) == idUsuario) {
                    if (enlace.ActualizarEventoFecha(id_evento, elemento)) {
                        if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp");
                        } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp");
                        } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp");
                        }
                    } else {
                        if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                            response.sendRedirect("principal.jsp");
                        } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                            response.sendRedirect("principal_dir.jsp");
                        } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                            response.sendRedirect("principal_fun.jsp");
                        }
                    }
                } else {

                }
            } catch (SQLException ex) {
                System.out.println(ex);
                if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("administrador")) {
                    response.sendRedirect("principal.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("director")) {
                    response.sendRedirect("principal_dir.jsp");
                } else if (enlace.tipoUsuario(idUsuario).equalsIgnoreCase("funcionario")) {
                    response.sendRedirect("principal_fun.jsp");
                }
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
