<%-- 
    Document   : index
    Created on : 22/01/2020, 10:13:36
    Author     : Kevin Druet
--%>

<%@page import="java.time.LocalDate"%>
<%@page import="modelo.periodo_vacaciones"%>
<%@page import="modelo.conexion_oracle"%>
<%@page import="modelo.usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.historia"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    conexion enlace = new conexion();
    ArrayList<historia> listadologin = enlace.listadoHistoriasTipo(1);
    HttpSession sesion = request.getSession();
    int id = 1;
    try {
        id = Integer.parseInt(sesion.getAttribute("usuario").toString());
    } catch (Exception e) {

    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <!-- End Google Tag Manager -->
        <title>Intranet Alcaldia - Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>
        <!-- External CSS libraries -->
        <link type="text/css" rel="stylesheet" href="ses/css/bootstrap.min.css">
        <link rel="stylesheet" href="ses/font-awesome/css/font-awesome.min.css">
        <link type="text/css" rel="stylesheet" href="ses/fonts/flaticon/font/flaticon.css">
        <link rel="stylesheet" href="assets/modules/izitoast/css/iziToast.min.css">
        <!-- Template CSS -->
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.js"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker.css" rel="stylesheet"/>

        <!-- Favicon icon -->
        <link rel="shortcut icon" href="assets/img/ic.ico" type="image/x-icon" >

        <!-- Google fonts -->
        <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800%7CPoppins:400,500,700,800,900%7CRoboto:100,300,400,400i,500,700">
        <!-- Custom Stylesheet -->
        <link type="text/css" rel="stylesheet" href="ses/css/style.css">
        <link rel="stylesheet" type="text/css" id="style_sheet" href="ses/css/skins/default.css">

    </head>
    <body id="top">
        <!-- Login 3 start -->
        <div class="login-3 tab-box">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-4 col-md-12 col-pad-0 form-section">
                        <div class="login-inner-form">
                            <div class="details">
                                <a href="index.jsp">
                                    <img src="ses/img/logo.png" alt="logo" width="210px">
                                </a>
                                <form action="sesion.control?accion=normal" method="POST">
                                    <div class="form-group">
                                        <input type="email" name="txtcorreo" class="input-text" placeholder="Correo institucional" required>
                                    </div>
                                    <div class="form-group">
                                        <input type="password" name="txtclave" class="input-text" placeholder="Contraseña" required>
                                    </div>
                                    <div class="form-group">
                                        <a href="recuperar_clave.jsp?step=0&m"><h6>¿Olvidó su contraseña?</h6></a>
                                    </div>
                                    <div class="form-group">
                                        <button type="submit" class="btn-md btn-theme btn-block">Acceder</button>
                                    </div>
                                    <% if (id == 0) {%>
                                    <div>
                                        <script>
                                            window.onload = function () {
                                                errorSesion();
                                            }
                                        </script>
                                    </div>
                                    <%
                                            sesion.removeAttribute("usuario");
                                            sesion.invalidate();
                                        }
                                    %>
                                    <% if (id == 2) {%>
                                    <div>
                                        <script>
                                            window.onload = function () {
                                                noexisteSesion();
                                            }
                                        </script>
                                    </div>
                                    <%
                                            sesion.removeAttribute("usuario");
                                            sesion.invalidate();
                                        }
                                    %>
                                    <div class="container social-box">
                                        <ul class="list-unstyled list-inline text-center">
                                            <li class="list-inline-item">
                                                <a href="https://www.facebook.com/VickoVillacis/" target="_blank" class="btn-floating btn-fb mx-1">
                                                    <i class="fa fa-facebook-f"> </i>
                                                </a>
                                            </li>
                                            <!--<li class="list-inline-item">
                                                <a href="https://twitter.com/LuciaSosaR" target="_blank" class="btn-floating btn-tw mx-1">
                                                    <i class="fa fa-twitter"> </i>
                                                </a>
                                            </li>-->
                                            <li class="list-inline-item">
                                                <a href="https://www.youtube.com/user/GADMEsmeraldas" target="_blank" class="btn-floating btn-dribbble mx-1">
                                                    <i class="fa fa-youtube-play"> </i>
                                                </a>
                                            </li>
                                            <!--<li class="list-inline-item">
                                                <a href="https://www.instagram.com/alcaldiadeesmeraldas/" target="_blank" class="btn-floating btn-dribbble mx-1">
                                                    <i class="fa fa-instagram"> </i>
                                                </a>
                                            </li>-->
                                        </ul>
                                    </div>
                                    <div class="form-group">
                                        <p>Copyright © <%= LocalDate.now().getYear() %></p><h6><a target="_blank" href="https://www.esmeraldas.gob.ec/">GAD Municipal del Cantón Esmeraldas<br>Dirección de Tecnologías de la Información</a></h6>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div id="bg-img" class="col-lg-8 col-md-12col-pad-0 bg-img none-992">
                    </div>
                </div>
            </div>
            <!-- Login 3 end -->

            <!-- External JS libraries -->
            <script src="ses/js/jquery-2.2.0.min.js"></script>
            <script src="ses/js/popper.min.js"></script>
            <script src="ses/js/bootstrap.min.js"></script>
            <%if (enlace.presentacionActiva().equals("navidad")) {%>
            <script type="text/javascript">
                                            var speed = 30; // A menor numero más rápido
                                            var flakes = 150; // Numero de Copos de Nieve
                                            var flake_image = "assets/img/qq.png"; // URL de la imagen de nieve
                                            var swide, shigh;
                                            var dx = new Array();
                                            var xp = new Array();
                                            var yp = new Array();
                                            var am = new Array();
                                            var sty = new Array();
                                            window.onload = function () {
                                                if (document.getElementById) {
                                                    var k, f, b;
                                                    b = document.createElement("div");
                                                    b.style.position = "absolute";
                                                    b.setAttribute("id", "bod");
                                                    document.body.appendChild(b);
                                                    set_scroll();
                                                    set_width();
                                                    for (var i = 0; i < flakes; i++) {
                                                        dx[i] = 0;
                                                        am[i] = Math.random() * 20;
                                                        xp[i] = am[i] + Math.random() * (swide - 2 * am[i] - 25);
                                                        yp[i] = Math.random() * shigh;
                                                        sty[i] = 0.75 + 1.25 * Math.random();
                                                        f = document.createElement("div");
                                                        f.style.position = "absolute";
                                                        f.setAttribute("id", "flk" + i);
                                                        f.style.zIndex = i;
                                                        f.style.top = yp[i] + "px";
                                                        f.style.left = xp[i] + "px";
                                                        k = document.createElement("img");
                                                        k.src = flake_image;
                                                        f.appendChild(k);
                                                        b.appendChild(f);
                                                    }
                                                    setInterval("winter_snow()", speed);
                                                }
                                            }
                                            window.onresize = set_width;
                                            function set_width() {
                                                if (document.documentElement && document.documentElement.clientWidth) {
                                                    swide = document.documentElement.clientWidth;
                                                    shigh = document.documentElement.clientHeight;
                                                } else if (typeof (self.innerHeight) == "number") {
                                                    swide = self.innerWidth;
                                                    shigh = self.innerHeight;
                                                } else if (document.body.clientWidth) {
                                                    swide = document.body.clientWidth;
                                                    shigh = document.body.clientHeight;
                                                } else {
                                                    swide = 800;
                                                    shigh = 600
                                                }
                                            }
                                            window.onscroll = set_scroll;
                                            function set_scroll() {
                                                var sleft, sdown;
                                                if (typeof (self.pageYOffset) == "number") {
                                                    sdown = self.pageYOffset;
                                                    sleft = self.pageXOffset;
                                                } else if (document.body.scrollTop || document.body.scrollLeft) {
                                                    sdown = document.body.scrollTop;
                                                    sleft = document.body.scrollLeft;
                                                } else if (document.documentElement && (document.documentElement.scrollTop || document.documentElement.scrollLeft)) {
                                                    sleft = document.documentElement.scrollLeft;
                                                    sdown = document.documentElement.scrollTop;
                                                } else {
                                                    sdown = 0;
                                                    sleft = 0;
                                                }
                                                document.getElementById("bod").style.top = sdown + "px";
                                                document.getElementById("bod").style.left = sleft + "px";
                                            }
                                            function winter_snow() {
                                                for (var i = 0; i < flakes; i++) {
                                                    yp[i] += sty[i];
                                                    if (yp[i] > shigh - 30) {
                                                        xp[i] = am[i] + Math.random() * (swide - 2 * am[i] - 25);
                                                        yp[i] = 0;
                                                        sty[i] = 0.75 + 1.25 * Math.random();
                                                    }
                                                    dx[i] += 0.02 + Math.random() / 10;
                                                    document.getElementById("flk" + i).style.top = yp[i] + "px";
                                                    document.getElementById("flk" + i).style.left = (xp[i] + am[i] * Math.sin(dx[i])) + "px";
                                                }
                                            }
            </script>
            <%}%>
            <script type="text/javascript">
                (function () {
                    var i =<%= enlace.historiaMenor()%>;
                    var cambiar = setInterval(function () {
                        $('#bg-img').animate({
                            opacity: 1
                        }, 'slow', function () {
                            $(this).css({'background-image': 'url("historias.control?accion=login&id=' + i + '")'})
                                    .animate({opacity: 1});
                            i++;
                            if (i == <%= enlace.historiasMayor() + 1%>) {
                                i = <%= enlace.historiaMenor()%>
                            }
                            ;
                        });
                    }, 3000);

                })();
            </script>
            <script src="assets/js/plugin/jquery-ui-1.12.1.custom/jquery-ui.min.js"></script>
            <script src="assets/js/plugin/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js"></script>
            <!-- jQuery Scrollbar -->
            <script src="assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>
            <script src="assets/modules/jquery.min.js"></script>
            <script src="assets/modules/popper.js"></script>
            <script src="assets/modules/tooltip.js"></script>
            <script src="assets/modules/bootstrap/js/bootstrap.min.js"></script>
            <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
            <script src="assets/modules/moment.min.js"></script>
            <script src="assets/js/stisla.js"></script>

            <!-- JS Libraies -->
            <script src="assets/modules/izitoast/js/iziToast.min.js"></script>

            <!-- Page Specific JS File -->
            <script src="assets/js/page/modules-toastr.js"></script>

            <!-- Template JS File -->
            <script src="assets/js/scripts.js"></script>
            <script src="assets/js/custom.js"></script>
            <script src="fun_js/sesion_validacion.js" type="text/javascript"></script>
            <!-- Custom JS Script -->
    </body>
</html>
