<%-- 
    Document   : index
    Created on : 16/07/2019, 9:43:23
    Author     : usuario
--%>
<%@page import="java.time.LocalDate"%>
<%@page import="modelo.usuario"%>
<%@page import="modelo.conexion"%>
<%
    conexion enlace = new conexion();
    String mensaje = request.getParameter("m");
    int step = Integer.parseInt(request.getParameter("step"));
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Intranet Alcaldia - Primera sesión</title>
        <meta content='width=device-width, initial-scale=1.0, shrink-to-fit=no' name='viewport' />
        <link rel="icon" href="assets/img/ic.ico" type="image/x-icon"/>

        <!-- Fonts and icons -->
        <!-- CSS Files -->
        <link rel="stylesheet" href="assets/modules/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/modules/fontawesome/css/all.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/components.css">

    </head>

    <body class="bg-gradient-primary">
        <div class="container">
            <!-- Outer Row -->
            <%if (step == 0) {%>
            <%
                int id_usuario = Integer.parseInt(request.getParameter("iu"));
            %>
            <div class="row justify-content-center">

                <div class="col-xl-12 col-lg-12 col-md-9">
                    <br>
                    <div class="card o-hidden border-0 shadow-lg my-5">
                        <div class="card-body p-0">
                            <!-- Nested Row within Card Body -->
                            <div class="row">
                                <div class="col-lg-6 d-none d-lg-block">
                                    <img src="assets/img/logoi.jpg" height="477" width="1110">
                                </div>
                                <div class="col-lg-6">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="h4 text-gray-900 mb-4"><b>Por ser su primer inicio de sesión por favor cambie su contraseña</b></h1>
                                        </div>
                                        <form class="user" action="administrar_sesion.control?accion=mensaje&iu=<%= id_usuario%>" method="post">
                                            <%if (mensaje.equalsIgnoreCase("us")) {%>
                                            <div role="alert" aria-live="assertive" aria-atomic="true" class="toast" data-autohide="false">
                                                <div class="toast-header">
                                                    <svg class=" rounded mr-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg"
                                                         preserveAspectRatio="xMidYMid slice" focusable="false" role="img">
                                                    <rect fill="#ee3923" width="100%" height="100%" /></svg>
                                                    <strong class="mr-auto">Aviso</strong>
                                                    <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="toast-body">
                                                    Su bot no se encuentra activo use su correo electrónico por favor
                                                </div>
                                            </div>
                                            <%} else if (mensaje.equalsIgnoreCase("ctr")) {%>
                                            <div role="alert" aria-live="assertive" aria-atomic="true" class="toast" data-autohide="false">
                                                <div class="toast-header">
                                                    <svg class=" rounded mr-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg"
                                                         preserveAspectRatio="xMidYMid slice" focusable="false" role="img">
                                                    <rect fill="#ee3923" width="100%" height="100%" /></svg>
                                                    <strong class="mr-auto">Aviso</strong>
                                                    <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="toast-body">
                                                    La contraseña actual es incorrecta
                                                </div>
                                            </div>
                                            <%} else if (mensaje.equalsIgnoreCase("cti")) {%>
                                            <div role="alert" aria-live="assertive" aria-atomic="true" class="toast" data-autohide="false">
                                                <div class="toast-header">
                                                    <svg class=" rounded mr-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg"
                                                         preserveAspectRatio="xMidYMid slice" focusable="false" role="img">
                                                    <rect fill="#03b6ff" width="100%" height="100%" /></svg>
                                                    <strong class="mr-auto">Aviso</strong>
                                                    <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                </div>
                                                <div class="toast-body">
                                                    Las claves ingresadas no son iguales.
                                                </div>
                                            </div>
                                            <%}%>
                                            <div class="form-group">
                                                <input type="password" class="form-control form-control-user" name="txtclaveactual" id="txtclaveactual" aria-describedby="emailHelp" placeholder="Clave actual" required>
                                            </div>
                                            <div class="form-group">
                                                <input type="password" class="form-control form-control-user" name="txtclavenueva" id="txtclavenueva" aria-describedby="emailHelp" placeholder="Nueva clave" required>
                                            </div>
                                            <div class="form-group">
                                                <input type="password" class="form-control form-control-user" name="txtclaverepetir" id="txtclaverepetir" aria-describedby="emailHelp" placeholder="Repita nueva clave" required>
                                            </div>
                                            <button  id="botoncontinuar" type="submit" class="btn btn-primary btn-block">
                                                Continuar
                                            </button>
                                        </form>
                                        <hr>
                                        <div class="text-center">
                                            <a href="index.jsp">Iniciar sesión</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%}%>
            <!-- Footer -->
            <footer class="page-footer font-small special-color-dark pt-4">
                <div class="container">
                    <ul class="list-unstyled list-inline text-center">
                        <li class="list-inline-item">
                            <a href="https://www.facebook.com/LuciaSosaAlcaldesa" target="_blank" class="btn-floating btn-fb mx-1">
                                <i class="fab fa-facebook-f"> </i>
                            </a>
                        </li>
                        <li class="list-inline-item">
                            <a href="https://twitter.com/LuciaSosaR" target="_blank" class="btn-floating btn-tw mx-1">
                                <i class="fab fa-twitter"> </i>
                            </a>
                        </li>
                        <li class="list-inline-item">
                            <a href="https://www.youtube.com/user/GADMEsmeraldas" target="_blank" class="btn-floating btn-dribbble mx-1">
                                <i class="fab fa-youtube"> </i>
                            </a>
                        </li>
                    </ul>
                </div>
                <hr>
                <div class="footer-copyright text-center py-3">Copyright &copy; <%= LocalDate.now().getYear() %> <div class="bullet"></div><a target="_blank" href="http://www.esmeraldas.gob.ec/"> GAD Municipal del Cantón Esmeraldas - Dirección de Tecnologías de la Información</a>
                </div>
            </footer>
        </div>
        <script src="assets/modules/jquery.min.js"></script>
        <script src="assets/modules/popper.js"></script>
        <script src="assets/modules/tooltip.js"></script>
        <script src="assets/modules/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/modules/nicescroll/jquery.nicescroll.min.js"></script>
        <script src="assets/modules/moment.min.js"></script>
        <script src="assets/js/stisla.js"></script>
        <script src="js/validacion.js" type="text/javascript"></script>
        <%if (enlace.presentacionActiva()
                    .equals("navidad")) {%>
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
        <script>
            $('.toast').toast('show');
        </script>
    </body>

</html>

