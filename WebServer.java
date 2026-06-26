import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    private String activeVeterinarian = "";
    private String activeSpecialty = "";

    private final HashMap<String, String> records = new HashMap<>();
    private final HashMap<String, ArrayList<String>> visits = new HashMap<>();
    private final HashMap<String, String> appointments = new HashMap<>();
    private final ArrayList<String> aiHistory = new ArrayList<>();

    public void start() {
        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(8085), 0
            );

            server.createContext("/", this::showLogin);
            server.createContext("/login", this::login);
            server.createContext("/logout", this::logout);

            server.createContext("/dashboard", this::showDashboard);
            server.createContext("/patients", this::showPatients);
            server.createContext("/new-visit", this::showNewVisit);
            server.createContext("/appointments", this::showAppointments);
            server.createContext("/hospitalized", this::showHospitalized);
            server.createContext("/assistant", this::showAssistant);
            server.createContext("/history", this::showHistory);

            server.createContext("/search", this::searchRecord);
            server.createContext("/generate", this::generateAdvice);
            server.createContext("/schedule", this::scheduleAppointment);
            server.createContext("/ask", this::askAI);

            server.start();

            System.out.println("VetCare Intelligence está funcionando.");
            System.out.println("Abre: http://localhost:8085");

        } catch (Exception e) {
            System.out.println(
                    "Error del servidor: " + e.getMessage()
            );
        }
    }

    private String styles() {
        return """
            <style>
                :root {
                    --green: #3b9185;
                    --dark: #25464d;
                    --mint: #e9f8f4;
                    --sky: #edf7ff;
                    --cream: #fff8e8;
                    --ink: #29414b;
                    --muted: #72868f;
                    --line: #dce9ec;
                    --shadow: 0 14px 34px rgba(41,65,75,.10);
                }

                * {
                    box-sizing: border-box;
                }

                body {
                    margin: 0;
                    font-family: "Segoe UI", Arial, sans-serif;
                    background: #f5f9fa;
                    color: var(--ink);
                }

                a {
                    text-decoration: none;
                    color: inherit;
                }

                input,
                select,
                textarea {
                    width: 100%;
                    padding: 12px 13px;
                    border: 1px solid #cfdee2;
                    border-radius: 13px;
                    background: #fcfeff;
                    color: var(--ink);
                    font: inherit;
                }

                textarea {
                    min-height: 92px;
                    resize: vertical;
                }

                input:focus,
                select:focus,
                textarea:focus {
                    outline: none;
                    border-color: #67bcaf;
                    box-shadow: 0 0 0 4px rgba(103,188,175,.15);
                }

                button,
                .button {
                    display: inline-block;
                    border: 0;
                    border-radius: 13px;
                    padding: 12px 17px;
                    background: var(--green);
                    color: white;
                    font-weight: 800;
                    cursor: pointer;
                    font: inherit;
                }

                button:hover,
                .button:hover {
                    background: #286d65;
                }

                .login-page {
                    min-height: 100vh;
                    display: grid;
                    grid-template-columns: 1.05fr .95fr;
                    background: linear-gradient(
                        135deg,
                        #effbf8,
                        #eef7ff,
                        #fff8e9
                    );
                }

                .login-visual {
                    padding: 58px;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    background: linear-gradient(
                        145deg,
                        #e3f8f2,
                        #f2f8ff 58%,
                        #fff8e8
                    );
                }

                .login-visual h1 {
                    margin: 0;
                    max-width: 630px;
                    font-size: 52px;
                    line-height: 1.04;
                    color: #286d65;
                }

                .login-visual p {
                    max-width: 620px;
                    font-size: 18px;
                    line-height: 1.6;
                    color: #58717b;
                }

                .animal-row {
                    display: grid;
                    grid-template-columns: repeat(4, 1fr);
                    gap: 14px;
                    margin-top: 30px;
                    max-width: 650px;
                }

                .animal-card {
                    background: rgba(255,255,255,.78);
                    border: 1px solid white;
                    border-radius: 24px;
                    padding: 14px;
                    text-align: center;
                    box-shadow: 0 12px 24px rgba(46,99,91,.08);
                }

                .animal-card svg {
                    width: 78px;
                    height: 78px;
                }

                .animal-card b {
                    display: block;
                    color: #286d65;
                }

                .login-panel {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    padding: 36px;
                }

                .login-card {
                    width: 100%;
                    max-width: 650px;
                    background: white;
                    border: 1px solid var(--line);
                    border-radius: 30px;
                    padding: 34px;
                    box-shadow: var(--shadow);
                }

                .login-card h2 {
                    margin: 0;
                    color: #286d65;
                    font-size: 32px;
                }

                .sub {
                    margin: 8px 0 26px;
                    color: var(--muted);
                }

                .profile-grid {
                    display: grid;
                    gap: 14px;
                }

                .profile-card {
                    display: grid;
                    grid-template-columns: 60px 1fr;
                    gap: 14px;
                    align-items: center;
                    padding: 16px;
                    border: 1px solid var(--line);
                    border-radius: 22px;
                    background: #fcfeff;
                }

                .avatar {
                    width: 60px;
                    height: 60px;
                    display: grid;
                    place-items: center;
                    border-radius: 18px;
                    background: var(--mint);
                    color: #286d65;
                    font-weight: 900;
                }

                .profile-card h3 {
                    margin: 0 0 3px;
                    color: #286d65;
                }

                .profile-card p {
                    margin: 0 0 10px;
                    color: var(--muted);
                    font-size: 14px;
                }

                .profile-card form {
                    display: grid;
                    grid-template-columns: 1fr auto;
                    gap: 9px;
                }

                .error {
                    background: #fff0f0;
                    border: 1px solid #f2caca;
                    color: #a44747;
                    border-radius: 14px;
                    padding: 12px;
                    margin-bottom: 16px;
                    font-weight: 700;
                }

                .app-shell {
                    min-height: 100vh;
                    display: grid;
                    grid-template-columns: 250px 1fr;
                }

                .sidebar {
                    position: sticky;
                    top: 0;
                    height: 100vh;
                    padding: 26px 18px;
                    background: var(--dark);
                    color: #eaf7f5;
                    display: flex;
                    flex-direction: column;
                }

                .brand {
                    font-size: 25px;
                    font-weight: 900;
                    margin: 0 10px 24px;
                }

                .brand small {
                    display: block;
                    color: #add0ca;
                    font-size: 12px;
                    font-weight: 600;
                }

                .nav-link {
                    padding: 12px 13px;
                    border-radius: 13px;
                    margin-bottom: 6px;
                    color: #dcefeb;
                    font-weight: 700;
                }

                .nav-link:hover {
                    background: rgba(255,255,255,.09);
                }

                .vet-card {
                    margin-top: auto;
                    padding: 15px;
                    border: 1px solid rgba(255,255,255,.12);
                    background: rgba(255,255,255,.06);
                    border-radius: 18px;
                }

                .vet-card strong {
                    display: block;
                }

                .vet-card span {
                    display: block;
                    color: #aed0ca;
                    font-size: 12px;
                    line-height: 1.4;
                    margin-top: 4px;
                }

                .logout {
                    display: inline-block;
                    margin-top: 10px;
                    color: #ffcaca;
                    font-weight: 800;
                }

                .main {
                    min-width: 0;
                    padding: 28px;
                }

                .page-hero {
                    display: grid;
                    grid-template-columns: 1fr 300px;
                    align-items: center;
                    gap: 24px;
                    padding: 30px;
                    border-radius: 30px;
                    border: 1px solid var(--line);
                    background: linear-gradient(
                        135deg,
                        #e2f8f2,
                        #eef7ff 62%,
                        #fff7df
                    );
                }

                .page-hero h1 {
                    margin: 0 0 8px;
                    color: #286d65;
                    font-size: 38px;
                }

                .page-hero p {
                    margin: 0;
                    color: #58717c;
                    line-height: 1.6;
                }

                .hero-animals {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 8px;
                }

                .hero-animals svg {
                    width: 125px;
                    height: 115px;
                }

                .stats {
                    display: grid;
                    grid-template-columns: repeat(4, 1fr);
                    gap: 15px;
                    margin: 22px 0;
                }

                .stat {
                    background: white;
                    border: 1px solid var(--line);
                    border-radius: 21px;
                    padding: 19px;
                    box-shadow: 0 8px 20px rgba(41,65,75,.06);
                }

                .stat strong {
                    display: block;
                    font-size: 28px;
                    color: var(--green);
                }

                .stat span {
                    color: var(--muted);
                    font-weight: 700;
                }

                .module-grid {
                    display: grid;
                    grid-template-columns: repeat(3, 1fr);
                    gap: 16px;
                    margin-bottom: 22px;
                }

                .module-card {
                    background: white;
                    border: 1px solid var(--line);
                    border-radius: 24px;
                    padding: 20px;
                    box-shadow: 0 8px 20px rgba(41,65,75,.06);
                    min-height: 180px;
                }

                .module-card h3 {
                    margin: 8px 0 6px;
                    color: #286d65;
                }

                .module-card p {
                    margin: 0 0 16px;
                    color: var(--muted);
                    line-height: 1.45;
                }

                .mini-animal,
                .mini-animal svg {
                    width: 64px;
                    height: 64px;
                }

                .content-grid {
                    display: grid;
                    grid-template-columns: 1.25fr .75fr;
                    gap: 20px;
                    align-items: start;
                }

                .card {
                    background: white;
                    border: 1px solid var(--line);
                    border-radius: 25px;
                    padding: 24px;
                    box-shadow: var(--shadow);
                    margin-bottom: 20px;
                }

                .card h2 {
                    margin: 0 0 8px;
                    color: #286d65;
                }

                .card-sub {
                    margin: 0 0 20px;
                    color: var(--muted);
                    line-height: 1.5;
                }

                .section-title {
                    margin: 22px 0 13px;
                    padding: 12px 14px;
                    border-radius: 14px;
                    background: var(--mint);
                    color: #286d65;
                    font-weight: 900;
                }

                .form-grid {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 14px;
                }

                .full {
                    grid-column: 1 / -1;
                }

                label {
                    display: block;
                    margin-bottom: 6px;
                    color: #4e6771;
                    font-size: 13px;
                    font-weight: 800;
                }

                .hidden {
                    display: none;
                }

                .soft-box {
                    padding: 15px;
                    border-radius: 16px;
                    background: var(--sky);
                    border: 1px solid #dcebf4;
                }

                .record {
                    padding: 18px;
                    border: 1px solid var(--line);
                    border-radius: 20px;
                    background: #fcfeff;
                    margin-bottom: 14px;
                }

                .record h3 {
                    margin: 0 0 8px;
                    color: #286d65;
                }

                .record p {
                    margin: 5px 0;
                    color: #526b76;
                }

                .tag {
                    display: inline-block;
                    padding: 6px 11px;
                    border-radius: 999px;
                    margin: 0 6px 8px 0;
                    font-size: 12px;
                    font-weight: 900;
                }

                .normal {
                    background: #e4f6ec;
                    color: #23704a;
                }

                .follow {
                    background: #e8f0ff;
                    color: #2d55a1;
                }

                .important {
                    background: #fff0df;
                    color: #ad641d;
                }

                .urgent {
                    background: #ffe7e7;
                    color: #a64343;
                }

                .neutral {
                    background: #edf3f5;
                    color: #536c77;
                }

                .visit {
                    padding: 14px;
                    border-radius: 16px;
                    border: 1px solid var(--line);
                    background: #f8fbfc;
                    margin-top: 10px;
                    color: #526b76;
                    line-height: 1.55;
                }

                .result {
                    padding: 20px;
                    border-radius: 18px;
                    border: 1px solid #d8eee7;
                    border-left: 6px solid var(--green);
                    background: #f3fcf9;
                    white-space: pre-line;
                    line-height: 1.65;
                }

                .empty {
                    padding: 26px;
                    text-align: center;
                    color: var(--muted);
                    border: 1px dashed #cfdfe3;
                    border-radius: 18px;
                    background: #fbfdfe;
                }

                @media(max-width: 1050px) {
                    .login-page {
                        grid-template-columns: 1fr;
                    }

                    .login-visual {
                        display: none;
                    }

                    .app-shell {
                        grid-template-columns: 1fr;
                    }

                    .sidebar {
                        height: auto;
                        position: static;
                    }

                    .content-grid {
                        grid-template-columns: 1fr;
                    }

                    .stats {
                        grid-template-columns: 1fr 1fr;
                    }

                    .module-grid {
                        grid-template-columns: 1fr 1fr;
                    }
                }

                @media(max-width: 700px) {
                    .main {
                        padding: 15px;
                    }

                    .page-hero {
                        grid-template-columns: 1fr;
                        padding: 22px;
                    }

                    .hero-animals {
                        display: none;
                    }

                    .stats,
                    .module-grid,
                    .form-grid {
                        grid-template-columns: 1fr;
                    }

                    .profile-card {
                        grid-template-columns: 1fr;
                    }

                    .profile-card form {
                        grid-template-columns: 1fr;
                    }
                }
            </style>
        """;
    }

    private String animalSvg(String type) {
        if ("cat".equals(type)) {
            return """
                <svg viewBox="0 0 120 120"
                     xmlns="http://www.w3.org/2000/svg">

                    <circle cx="60" cy="62"
                            r="38"
                            fill="#a9c7d8"/>

                    <path d="M31 39 22 13l28 18z
                             M89 39l9-26-28 18z"
                          fill="#809ead"/>

                    <circle cx="47" cy="58"
                            r="4"
                            fill="#334b55"/>

                    <circle cx="73" cy="58"
                            r="4"
                            fill="#334b55"/>

                    <path d="m60 70-7 6h14z"
                          fill="#e59a9a"/>
                </svg>
            """;
        }

        if ("rabbit".equals(type)) {
            return """
                <svg viewBox="0 0 120 120"
                     xmlns="http://www.w3.org/2000/svg">

                    <ellipse cx="45" cy="27"
                             rx="11" ry="27"
                             fill="#d7c3e8"/>

                    <ellipse cx="75" cy="27"
                             rx="11" ry="27"
                             fill="#d7c3e8"/>

                    <circle cx="60" cy="68"
                            r="37"
                            fill="#e9def2"/>

                    <circle cx="47" cy="62"
                            r="4"
                            fill="#334b55"/>

                    <circle cx="73" cy="62"
                            r="4"
                            fill="#334b55"/>

                    <ellipse cx="60" cy="75"
                             rx="7" ry="5"
                             fill="#e8a9b5"/>
                </svg>
            """;
        }

        if ("bird".equals(type)) {
            return """
                <svg viewBox="0 0 120 120"
                     xmlns="http://www.w3.org/2000/svg">

                    <circle cx="57" cy="63"
                            r="36"
                            fill="#f6cf71"/>

                    <path d="M89 62l21 8-21 8z"
                          fill="#e89b55"/>

                    <circle cx="48" cy="55"
                            r="5"
                            fill="#334b55"/>

                    <path d="M25 66c12-22 25-20
                             31-7-11 1-19 8-24 18z"
                          fill="#e8b94f"/>
                </svg>
            """;
        }

        return """
            <svg viewBox="0 0 120 120"
                 xmlns="http://www.w3.org/2000/svg">

                <circle cx="60" cy="62"
                        r="38"
                        fill="#f2c987"/>

                <path d="M30 40 13 20l8 34z
                         M90 40l17-20-8 34z"
                      fill="#d79c4a"/>

                <circle cx="47" cy="58"
                        r="4"
                        fill="#334b55"/>

                <circle cx="73" cy="58"
                        r="4"
                        fill="#334b55"/>

                <ellipse cx="60" cy="73"
                         rx="9" ry="6"
                         fill="#334b55"/>
            </svg>
        """;
    }

    private void showLogin(HttpExchange exchange)
            throws IOException {

        sendResponse(exchange, loginPage(""));
    }

    private String loginPage(String message) {
        String notice = message == null
                || message.isBlank()
                ? ""
                : "<div class='error'>"
                + escapeHtml(message)
                + "</div>";

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport"
                      content="width=device-width, initial-scale=1">

                <title>VetCare Intelligence</title>
        """
                + styles()
                + """
            </head>

            <body>
                <div class="login-page">

                    <section class="login-visual">

                        <h1>
                            Cuidado veterinario más claro,
                            cercano y organizado
                        </h1>

                        <p>
                            Gestiona pacientes, citas,
                            hospitalización y seguimiento
                            clínico desde una plataforma
                            amigable.
                        </p>

                        <div class="animal-row">

                            <div class="animal-card">
        """
                + animalSvg("dog")
                + """
                                <b>Perros</b>
                            </div>

                            <div class="animal-card">
        """
                + animalSvg("cat")
                + """
                                <b>Gatos</b>
                            </div>

                            <div class="animal-card">
        """
                + animalSvg("rabbit")
                + """
                                <b>Conejos</b>
                            </div>

                            <div class="animal-card">
        """
                + animalSvg("bird")
                + """
                                <b>Aves</b>
                            </div>
                        </div>
                    </section>

                    <section class="login-panel">

                        <div class="login-card">

                            <h2>VetCare Intelligence</h2>

                            <p class="sub">
                                Selecciona el perfil veterinario
                                para ingresar.
                            </p>
        """
                + notice
                + "<div class='profile-grid'>"

                + profileForm(
                    "CM",
                    "Dr. Carlos Mora",
                    "Medicina general",
                    "carlos"
                )

                + profileForm(
                    "AR",
                    "Dra. Ana Rodriguez",
                    "Vacunación y prevención",
                    "ana"
                )

                + profileForm(
                    "SC",
                    "Dra. Sofia Castro",
                    "Hospitalización y seguimiento",
                    "sofia"
                )

                + """
                            </div>
                        </div>
                    </section>
                </div>
            </body>
            </html>
        """;
    }

    private String profileForm(
            String initials,
            String name,
            String specialty,
            String id
    ) {
        return "<div class='profile-card'>"

                + "<div class='avatar'>"
                + initials
                + "</div>"

                + "<div>"

                + "<h3>"
                + name
                + "</h3>"

                + "<p>"
                + specialty
                + "</p>"

                + "<form method='POST' action='/login'>"

                + "<input type='hidden' "
                + "name='vetId' value='"
                + id
                + "'>"

                + "<input type='password' "
                + "name='password' "
                + "placeholder='Clave de acceso' "
                + "required>"

                + "<button type='submit'>"
                + "Ingresar"
                + "</button>"

                + "</form>"
                + "</div>"
                + "</div>";
    }

    private void login(HttpExchange exchange)
            throws IOException {

        Map<String, String> data =
                readForm(exchange);

        String vetId =
                value(data.get("vetId"), "");

        String password =
                value(data.get("password"), "");

        if ("carlos".equals(vetId)
                && "1234".equals(password)) {

            activeVeterinarian = "Dr. Carlos Mora";
            activeSpecialty = "Medicina general";

            showDashboard(exchange);
            return;
        }

        if ("ana".equals(vetId)
                && "2222".equals(password)) {

            activeVeterinarian = "Dra. Ana Rodriguez";
            activeSpecialty =
                    "Vacunación y medicina preventiva";

            showDashboard(exchange);
            return;
        }

        if ("sofia".equals(vetId)
                && "3333".equals(password)) {

            activeVeterinarian = "Dra. Sofia Castro";
            activeSpecialty =
                    "Hospitalización y seguimiento clínico";

            showDashboard(exchange);
            return;
        }

        sendResponse(
            exchange,
            loginPage(
                "Clave incorrecta. Intenta nuevamente."
            )
        );
    }

    private void logout(HttpExchange exchange)
            throws IOException {

        activeVeterinarian = "";
        activeSpecialty = "";

        sendResponse(
            exchange,
            loginPage("Sesión cerrada correctamente.")
        );
    }

    private boolean isLoggedIn() {
        return activeVeterinarian != null
                && !activeVeterinarian.isBlank();
    }

    private String sidebar() {
        return "<aside class='sidebar'>"

                + "<div class='brand'>"
                + "VetCare"
                + "<small>Intelligence</small>"
                + "</div>"

                + "<a class='nav-link' "
                + "href='/dashboard'>Inicio</a>"

                + "<a class='nav-link' "
                + "href='/patients'>Pacientes</a>"

                + "<a class='nav-link' "
                + "href='/new-visit'>Nueva visita</a>"

                + "<a class='nav-link' "
                + "href='/appointments'>Citas</a>"

                + "<a class='nav-link' "
                + "href='/hospitalized'>Hospitalizados</a>"

                + "<a class='nav-link' "
                + "href='/assistant'>Asistente IA</a>"

                + "<a class='nav-link' "
                + "href='/history'>Historial</a>"

                + "<div class='vet-card'>"

                + "<strong>"
                + escapeHtml(activeVeterinarian)
                + "</strong>"

                + "<span>"
                + escapeHtml(activeSpecialty)
                + "</span>"

                + "<a class='logout' "
                + "href='/logout'>Cerrar sesión</a>"

                + "</div>"
                + "</aside>";
    }

    private String layout(
            String title,
            String description,
            String content
    ) {
        return "<!DOCTYPE html>"

                + "<html lang='es'>"

                + "<head>"

                + "<meta charset='UTF-8'>"

                + "<meta name='viewport' "
                + "content='width=device-width,initial-scale=1'>"

                + "<title>"
                + escapeHtml(title)
                + "</title>"

                + styles()

                + "</head>"

                + "<body>"

                + "<div class='app-shell'>"

                + sidebar()

                + "<main class='main'>"

                + "<section class='page-hero'>"

                + "<div>"

                + "<h1>"
                + escapeHtml(title)
                + "</h1>"

                + "<p>"
                + escapeHtml(description)
                + "</p>"

                + "</div>"

                + "<div class='hero-animals'>"
                + animalSvg("dog")
                + animalSvg("cat")
                + "</div>"

                + "</section>"

                + "<div style='margin-top:22px'>"

                + content

                + "</div>"

                + "</main>"

                + "</div>"

                + "</body>"

                + "</html>";
    }

    private void showDashboard(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        String content =
                "<section class='stats'>"

                + "<div class='stat'>"
                + "<strong>"
                + records.size()
                + "</strong>"
                + "<span>Pacientes</span>"
                + "</div>"

                + "<div class='stat'>"
                + "<strong>"
                + appointments.size()
                + "</strong>"
                + "<span>Citas</span>"
                + "</div>"

                + "<div class='stat'>"
                + "<strong>"
                + countHospitalized()
                + "</strong>"
                + "<span>Hospitalizados</span>"
                + "</div>"

                + "<div class='stat'>"
                + "<strong>"
                + aiHistory.size()
                + "</strong>"
                + "<span>Consultas IA</span>"
                + "</div>"

                + "</section>"

                + "<section class='module-grid'>"

                + moduleCard(
                    "dog",
                    "Pacientes",
                    "Busca expedientes por cédula y mascota.",
                    "/patients"
                )

                + moduleCard(
                    "cat",
                    "Nueva visita",
                    "Registra una consulta sin duplicar "
                    + "el expediente.",
                    "/new-visit"
                )

                + moduleCard(
                    "bird",
                    "Citas",
                    "Agenda y actualiza el estado de cada cita.",
                    "/appointments"
                )

                + moduleCard(
                    "rabbit",
                    "Hospitalizados",
                    "Consulta pacientes internados "
                    + "o en observación.",
                    "/hospitalized"
                )

                + moduleCard(
                    "cat",
                    "Asistente IA",
                    "Realiza preguntas veterinarias a Gemini.",
                    "/assistant"
                )

                + moduleCard(
                    "dog",
                    "Historial",
                    "Revisa visitas, citas y consultas.",
                    "/history"
                )

                + "</section>";

        sendResponse(
            exchange,
            layout(
                "Panel principal",
                "Bienvenido, "
                + activeVeterinarian
                + ". Selecciona el módulo que necesitas.",
                content
            )
        );
    }

    private String moduleCard(
            String animal,
            String title,
            String text,
            String link
    ) {
        return "<a class='module-card' "
                + "href='"
                + link
                + "'>"

                + "<div class='mini-animal'>"
                + animalSvg(animal)
                + "</div>"

                + "<h3>"
                + title
                + "</h3>"

                + "<p>"
                + text
                + "</p>"

                + "<span class='button'>"
                + "Abrir módulo"
                + "</span>"

                + "</a>";
    }

    private void showPatients(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        StringBuilder allRecords =
                new StringBuilder();

        for (String record : records.values()) {
            allRecords.append(record);
        }

        String recordsHtml =
                allRecords.length() == 0

                ? "<div class='empty'>"
                + "Todavía no hay pacientes registrados."
                + "</div>"

                : allRecords.toString();

        String content =
                "<div class='content-grid'>"

                + "<div class='card'>"

                + "<h2>Buscar expediente</h2>"

                + "<p class='card-sub'>"
                + "Utiliza la cédula del responsable "
                + "y el nombre de la mascota."
                + "</p>"

                + "<form method='POST' action='/search'>"

                + "<div class='form-grid'>"

                + "<div>"
                + "<label>Cédula</label>"
                + "<input name='searchOwnerId' required>"
                + "</div>"

                + "<div>"
                + "<label>Mascota</label>"
                + "<input name='searchPet' required>"
                + "</div>"

                + "</div>"

                + "<button type='submit'>"
                + "Buscar paciente"
                + "</button>"

                + "</form>"
                + "</div>"

                + "<div class='card'>"

                + "<h2>Acción rápida</h2>"

                + "<p class='card-sub'>"
                + "Registra una nueva visita "
                + "o crea el expediente."
                + "</p>"

                + "<a class='button' "
                + "href='/new-visit'>Nueva visita</a>"

                + "</div>"
                + "</div>"

                + "<div class='card'>"

                + "<h2>Pacientes registrados</h2>"

                + recordsHtml

                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Pacientes",
                "Consulta expedientes existentes "
                + "y evita registros duplicados.",
                content
            )
        );
    }

    private void showNewVisit(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        sendResponse(
            exchange,
            layout(
                "Nueva visita",
                "Completa la información por secciones "
                + "para mantener el expediente ordenado.",
                newVisitForm()
            )
        );
    }

    private String newVisitForm() {
        return """
            <div class="card">

                <form method="POST"
                      action="/generate">

                    <div class="section-title">
                        1. Responsable
                    </div>

                    <div class="form-grid">

                        <div>
                            <label>Cédula</label>
                            <input name="ownerId" required>
                        </div>

                        <div>
                            <label>Nombre completo</label>
                            <input name="ownerName" required>
                        </div>

                        <div>
                            <label>Teléfono</label>
                            <input name="ownerPhone">
                        </div>

                        <div>
                            <label>Correo</label>
                            <input name="ownerEmail">
                        </div>
                    </div>

                    <div class="section-title">
                        2. Mascota
                    </div>

                    <div class="form-grid">

                        <div>
                            <label>Nombre</label>
                            <input name="petName" required>
                        </div>

                        <div>
                            <label>Especie</label>

                            <select name="species">
                                <option>Perro</option>
                                <option>Gato</option>
                                <option>Conejo</option>
                                <option>Ave</option>
                                <option>Otra</option>
                            </select>
                        </div>

                        <div>
                            <label>Raza</label>
                            <input name="breed">
                        </div>

                        <div>
                            <label>Edad</label>

                            <input type="number"
                                   name="age"
                                   min="0"
                                   required>
                        </div>

                        <div>
                            <label>Peso</label>
                            <input name="weight">
                        </div>

                        <div>
                            <label>Estado</label>

                            <select name="patientStatus">
                                <option>Activo</option>
                                <option>En seguimiento</option>
                                <option>Recuperado</option>
                                <option>Pendiente de control</option>
                                <option>Referido</option>
                                <option>Inactivo</option>
                            </select>
                        </div>
                    </div>

                    <div class="section-title">
                        3. Consulta
                    </div>

                    <div class="form-grid">

                        <div>
                            <label>Tipo de consulta</label>

                            <select name="consultationType">
                                <option>
                                    Consulta por enfermedad
                                </option>

                                <option>Vacunación</option>
                                <option>Revisión general</option>
                                <option>Control de seguimiento</option>
                                <option>Emergencia</option>
                                <option>Control preventivo</option>
                            </select>
                        </div>

                        <div>
                            <label>Prioridad</label>

                            <select name="priority">
                                <option>Normal</option>
                                <option>Seguimiento</option>
                                <option>Importante</option>
                                <option>Urgente</option>
                            </select>
                        </div>

                        <div class="full">
                            <label>Diagnóstico</label>
                            <input name="diagnosis" required>
                        </div>

                        <div class="full">
                            <label>Síntomas</label>
                            <textarea name="symptoms"></textarea>
                        </div>

                        <div class="full">
                            <label>Tratamiento</label>

                            <textarea
                                name="treatment"
                                required>
                            </textarea>
                        </div>

                        <div>
                            <label>Medicamento</label>
                            <input name="medicineName">
                        </div>

                        <div>
                            <label>Dosis</label>
                            <input name="medicineDose">
                        </div>

                        <div>
                            <label>Frecuencia</label>
                            <input name="medicineFrequency">
                        </div>

                        <div>
                            <label>Duración</label>
                            <input name="medicineDuration">
                        </div>
                    </div>

                    <div class="section-title">
                        4. Referencia y hospitalización
                    </div>

                    <div class="form-grid">

                        <div>
                            <label>Referir expediente</label>

                            <select name="referralStatus">
                                <option>No</option>
                                <option>Sí</option>
                            </select>
                        </div>

                        <div>
                            <label>Veterinario destino</label>

                            <select name="referralVet">
                                <option>Sin referencia</option>

                                <option>
                                    Dr. Carlos Mora -
                                    Medicina general
                                </option>

                                <option>
                                    Dra. Ana Rodriguez -
                                    Vacunación y prevención
                                </option>

                                <option>
                                    Dra. Sofia Castro -
                                    Hospitalización y seguimiento
                                </option>
                            </select>
                        </div>

                        <div class="full">
                            <label>Motivo de referencia</label>

                            <textarea name="referralReason">
No aplica.
                            </textarea>
                        </div>

                        <div>
                            <label>Hospitalización</label>

                            <select
                                id="hospitalizationStatus"
                                name="hospitalizationStatus"
                                onchange="toggleHospitalization()">

                                <option>No hospitalizado</option>
                                <option>Hospitalizado</option>
                                <option>Internado</option>
                                <option>En observación</option>
                            </select>
                        </div>

                        <div>
                            <label>Evolución</label>

                            <select name="patientEvolution">
                                <option>Estable</option>
                                <option>Mejorando</option>
                                <option>Igual</option>
                                <option>Empeorando</option>
                                <option>Requiere nueva revisión</option>
                            </select>
                        </div>

                        <div
                            id="hospitalBox"
                            class="full hidden soft-box">

                            <div class="form-grid">

                                <div>
                                    <label>Área</label>

                                    <select name="hospitalArea">
                                        <option>No aplica</option>
                                        <option>Observación</option>

                                        <option>
                                            Hospitalización general
                                        </option>

                                        <option>Aislamiento</option>
                                        <option>Recuperación</option>
                                    </select>
                                </div>

                                <div>
                                    <label>
                                        Fecha estimada de salida
                                    </label>

                                    <input
                                        type="date"
                                        name="dischargeDate">
                                </div>

                                <div>
                                    <label>Alta médica</label>

                                    <select
                                        id="medicalDischarge"
                                        name="medicalDischarge"
                                        onchange="toggleDischarge()">

                                        <option>No</option>
                                        <option>Sí</option>
                                    </select>
                                </div>

                                <div
                                    id="dischargeNotesBox"
                                    class="hidden">

                                    <label>
                                        Indicaciones de salida
                                    </label>

                                    <textarea
                                        name="dischargeNotes">
                                    </textarea>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="section-title">
                        5. Vacunas y seguimiento
                    </div>

                    <div class="form-grid">

                        <div>
                            <label>Vacuna</label>

                            <input
                                name="vaccineName"
                                placeholder="Opcional">
                        </div>

                        <div>
                            <label>Fecha de aplicación</label>

                            <input
                                type="date"
                                name="applicationDate">
                        </div>

                        <div>
                            <label>Próxima vacunación</label>

                            <input
                                type="date"
                                name="nextDueDate">
                        </div>

                        <div>
                            <label>Próximo control</label>

                            <input
                                type="date"
                                name="nextControlDate">
                        </div>

                        <div class="full">
                            <label>Observaciones</label>

                            <textarea
                                name="observations">
                            </textarea>
                        </div>
                    </div>

                    <button type="submit">
                        Guardar visita y generar informe
                    </button>
                </form>
            </div>

            <script>
                function toggleHospitalization() {
                    const status =
                        document.getElementById(
                            'hospitalizationStatus'
                        ).value;

                    const visible =
                        status === 'Hospitalizado'
                        || status === 'Internado'
                        || status === 'En observación';

                    document.getElementById(
                        'hospitalBox'
                    ).classList.toggle(
                        'hidden',
                        !visible
                    );
                }

                function toggleDischarge() {
                    const status =
                        document.getElementById(
                            'medicalDischarge'
                        ).value;

                    document.getElementById(
                        'dischargeNotesBox'
                    ).classList.toggle(
                        'hidden',
                        status !== 'Sí'
                    );
                }
            </script>
        """;
    }

    private void showAppointments(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        StringBuilder allAppointments =
                new StringBuilder();

        for (String appointment : appointments.values()) {
            allAppointments.append(appointment);
        }

        String appointmentList =
                allAppointments.length() == 0

                ? "<div class='empty'>"
                + "Todavía no hay citas registradas."
                + "</div>"

                : allAppointments.toString();

        String content =
                "<div class='content-grid'>"

                + "<div class='card'>"

                + "<h2>Agendar cita</h2>"

                + appointmentForm()

                + "</div>"

                + "<div class='card'>"

                + "<h2>Agenda</h2>"

                + "<p class='card-sub'>"
                + "Consulta las citas registradas "
                + "durante la sesión."
                + "</p>"

                + appointmentList

                + "</div>"
                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Citas",
                "Agenda, actualiza y revisa "
                + "las citas de los pacientes.",
                content
            )
        );
    }

    private String appointmentForm() {
        return """
            <form method="POST"
                  action="/schedule">

                <div class="form-grid">

                    <div>
                        <label>Cédula</label>

                        <input
                            name="appointmentOwnerId"
                            required>
                    </div>

                    <div>
                        <label>Responsable</label>

                        <input
                            name="appointmentOwnerName"
                            required>
                    </div>

                    <div>
                        <label>Mascota</label>

                        <input
                            name="appointmentPet"
                            required>
                    </div>

                    <div>
                        <label>Tipo de cita</label>

                        <select name="appointmentType">
                            <option>
                                Consulta por enfermedad
                            </option>

                            <option>Vacunación</option>
                            <option>Revisión general</option>
                            <option>Control de seguimiento</option>
                            <option>Emergencia</option>
                        </select>
                    </div>

                    <div>
                        <label>Fecha</label>

                        <input
                            type="date"
                            name="appointmentDate"
                            required>
                    </div>

                    <div>
                        <label>Hora</label>

                        <select name="appointmentTime">
                            <option>8:00 AM</option>
                            <option>9:00 AM</option>
                            <option>10:00 AM</option>
                            <option>11:00 AM</option>
                            <option>1:00 PM</option>
                            <option>2:00 PM</option>
                            <option>3:00 PM</option>
                        </select>
                    </div>

                    <div>
                        <label>Estado</label>

                        <select name="appointmentStatus">
                            <option>Programada</option>
                            <option>Asistió</option>
                            <option>No asistió</option>
                            <option>Reprogramada</option>
                            <option>Cancelada</option>
                            <option>En seguimiento</option>
                        </select>
                    </div>

                    <div class="full">
                        <label>Motivo o resultado</label>

                        <textarea
                            name="appointmentReason">
                        </textarea>
                    </div>
                </div>

                <button type="submit">
                    Guardar cita
                </button>
            </form>
        """;
    }

    private void showHospitalized(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        StringBuilder hospitalized =
                new StringBuilder();

        for (String record : records.values()) {
            if (record.contains("Hospitalizado")
                    || record.contains("Internado")
                    || record.contains("En observación")) {

                hospitalized.append(record);
            }
        }

        String content =
                hospitalized.length() == 0

                ? "<div class='card'>"
                + "<div class='empty'>"
                + "No hay pacientes hospitalizados "
                + "durante esta sesión."
                + "</div>"
                + "</div>"

                : "<div class='card'>"
                + "<h2>Pacientes hospitalizados</h2>"
                + hospitalized
                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Hospitalizados",
                "Revisa pacientes internados, "
                + "en observación o seguimiento.",
                content
            )
        );
    }

    private void showAssistant(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        String content =
                "<div class='content-grid'>"

                + "<div class='card'>"

                + "<h2>Asistente veterinario</h2>"

                + "<p class='card-sub'>"
                + "Realiza preguntas relacionadas "
                + "con mascotas, vacunas y cuidados."
                + "</p>"

                + "<form method='POST' action='/ask'>"

                + "<label>Pregunta</label>"

                + "<textarea "
                + "name='question' "
                + "required>"
                + "</textarea>"

                + "<button type='submit'>"
                + "Consultar Gemini"
                + "</button>"

                + "</form>"
                + "</div>"

                + "<div class='card'>"

                + "<h2>Uso responsable</h2>"

                + "<p class='card-sub'>"
                + "La respuesta es informativa "
                + "y no sustituye una valoración veterinaria."
                + "</p>"

                + "<div class='mini-animal'>"
                + animalSvg("rabbit")
                + "</div>"

                + "</div>"
                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Asistente IA",
                "Obtén orientación general "
                + "sobre el cuidado de mascotas.",
                content
            )
        );
    }

    private void generateAdvice(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        Map<String, String> data =
                readForm(exchange);

        String ownerId =
                value(data.get("ownerId"), "No registrada");

        String ownerName =
                value(data.get("ownerName"), "No registrado");

        String ownerPhone =
                value(data.get("ownerPhone"), "No registrado");

        String ownerEmail =
                value(data.get("ownerEmail"), "No registrado");

        String petName =
                value(data.get("petName"), "No registrado");

        String species =
                value(data.get("species"), "No registrada");

        String breed =
                value(data.get("breed"), "No registrada");

        int age =
                parseInt(data.get("age"));

        String weight =
                value(data.get("weight"), "No registrado");

        String patientStatus =
                value(data.get("patientStatus"), "Activo");

        String consultationType =
                value(
                    data.get("consultationType"),
                    "Consulta general"
                );

        String priority =
                value(data.get("priority"), "Normal");

        String diagnosis =
                value(data.get("diagnosis"), "No registrado");

        String symptoms =
                value(data.get("symptoms"), "No registrados");

        String treatment =
                value(data.get("treatment"), "Sin tratamiento");

        String medicineName =
                value(data.get("medicineName"), "No indicado");

        String medicineDose =
                value(data.get("medicineDose"), "No indicada");

        String medicineFrequency =
                value(
                    data.get("medicineFrequency"),
                    "No indicada"
                );

        String medicineDuration =
                value(
                    data.get("medicineDuration"),
                    "No indicada"
                );

        String referralStatus =
                value(data.get("referralStatus"), "No");

        String referralVet =
                value(
                    data.get("referralVet"),
                    "Sin referencia"
                );

        String referralReason =
                value(
                    data.get("referralReason"),
                    "No aplica"
                );

        String hospitalization =
                value(
                    data.get("hospitalizationStatus"),
                    "No hospitalizado"
                );

        String hospitalArea =
                value(
                    data.get("hospitalArea"),
                    "No aplica"
                );

        String dischargeDate =
                value(
                    data.get("dischargeDate"),
                    "No aplica"
                );

        String medicalDischarge =
                value(
                    data.get("medicalDischarge"),
                    "No"
                );

        String dischargeNotes =
                value(
                    data.get("dischargeNotes"),
                    "No aplica"
                );

        String evolution =
                value(
                    data.get("patientEvolution"),
                    "Estable"
                );

        String vaccineName =
                value(
                    data.get("vaccineName"),
                    "No aplica"
                );

        String applicationDate =
                value(
                    data.get("applicationDate"),
                    "No registrada"
                );

        String nextDueDate =
                value(
                    data.get("nextDueDate"),
                    "No programada"
                );

        String nextControl =
                value(
                    data.get("nextControlDate"),
                    "No programado"
                );

        String observations =
                value(
                    data.get("observations"),
                    "Sin observaciones"
                );

        String suggested =
                suggestVeterinarian(
                    consultationType,
                    hospitalization,
                    priority
                );

        String assigned =
                activeVeterinarian;

        String referredBy =
                "No aplica";

        if ("Sí".equalsIgnoreCase(referralStatus)
                && !"Sin referencia".equalsIgnoreCase(
                    referralVet
                )) {

            assigned =
                    cleanVetName(referralVet);

            referredBy =
                    activeVeterinarian;
        }

        String key =
                buildKey(ownerId, petName);

        boolean existed =
                records.containsKey(key);

        Owner owner =
                new Owner(
                    1,
                    ownerName,
                    ownerPhone,
                    ownerEmail
                );

        Pet pet =
                new Pet(
                    1,
                    petName,
                    species,
                    breed,
                    age,
                    owner.getId()
                );

        Vaccine vaccine =
                new Vaccine(
                    1,
                    pet.getId(),
                    vaccineName,
                    applicationDate,
                    nextDueDate
                );

        String details =
                treatment

                + "\nCédula: "
                + ownerId

                + "\nTipo: "
                + consultationType

                + "\nEstado: "
                + patientStatus

                + "\nPrioridad: "
                + priority

                + "\nHospitalización: "
                + hospitalization

                + "\nÁrea: "
                + hospitalArea

                + "\nSalida: "
                + dischargeDate

                + "\nAlta: "
                + medicalDischarge

                + "\nIndicaciones: "
                + dischargeNotes

                + "\nEvolución: "
                + evolution

                + "\nSíntomas: "
                + symptoms

                + "\nPeso: "
                + weight

                + "\nMedicamento: "
                + medicineName

                + "\nDosis: "
                + medicineDose

                + "\nFrecuencia: "
                + medicineFrequency

                + "\nDuración: "
                + medicineDuration

                + "\nPróximo control: "
                + nextControl

                + "\nObservaciones: "
                + observations

                + "\nAtendido por: "
                + activeVeterinarian

                + "\nSugerido: "
                + suggested

                + "\nAsignado: "
                + assigned

                + "\nReferido por: "
                + referredBy

                + "\nMotivo: "
                + referralReason

                + "\nResponder sin Markdown.";

        MedicalRecord medicalRecord =
                new MedicalRecord(
                    1,
                    pet.getId(),
                    "Visita clínica registrada desde la web.",
                    diagnosis,
                    details,
                    "2026-06-15"
                );

        String recommendation =
                cleanMarkdown(
                    new AIService()
                        .generatePetCareAdvice(
                            pet,
                            vaccine,
                            medicalRecord
                        )
                );

        String recordCard =
                "<div class='record'>"

                + "<span class='tag "
                + priorityClass(priority)
                + "'>"
                + escapeHtml(priority)
                + "</span>"

                + "<span class='tag neutral'>"
                + escapeHtml(patientStatus)
                + "</span>"

                + "<h3>"
                + escapeHtml(petName)
                + "</h3>"

                + "<p><b>Especie:</b> "
                + escapeHtml(species)
                + "</p>"

                + "<p><b>Cédula:</b> "
                + escapeHtml(ownerId)
                + "</p>"

                + "<p><b>Responsable:</b> "
                + escapeHtml(ownerName)
                + "</p>"

                + "<p><b>Diagnóstico:</b> "
                + escapeHtml(diagnosis)
                + "</p>"

                + "<p><b>Asignado a:</b> "
                + escapeHtml(assigned)
                + "</p>"

                + "<p><b>Hospitalización:</b> "
                + escapeHtml(hospitalization)
                + "</p>"

                + "<p><b>Próximo control:</b> "
                + escapeHtml(nextControl)
                + "</p>"

                + "</div>";

        records.put(key, recordCard);

        visits.putIfAbsent(
            key,
            new ArrayList<>()
        );

        String visit =
                "<div class='visit'>"

                + "<b>"
                + escapeHtml(consultationType)
                + "</b><br>"

                + "Atendido por: "
                + escapeHtml(activeVeterinarian)
                + "<br>"

                + "Asignado a: "
                + escapeHtml(assigned)
                + "<br>"

                + "Referido por: "
                + escapeHtml(referredBy)
                + "<br>"

                + "Diagnóstico: "
                + escapeHtml(diagnosis)
                + "<br>"

                + "Evolución: "
                + escapeHtml(evolution)
                + "<br>"

                + "Medicamento: "
                + escapeHtml(medicineName)
                + "<br>"

                + "Próximo control: "
                + escapeHtml(nextControl)

                + "</div>";

        visits.get(key).add(visit);

        String message =
                existed

                ? "El expediente fue actualizado "
                + "y se agregó una nueva visita."

                : "El expediente fue creado correctamente.";

        String content =
                "<div class='card'>"

                + "<h2>"
                + escapeHtml(message)
                + "</h2>"

                + "<div class='record'>"

                + "<h3>"
                + escapeHtml(petName)
                + "</h3>"

                + "<p><b>Responsable:</b> "
                + escapeHtml(ownerName)
                + "</p>"

                + "<p><b>Veterinario asignado:</b> "
                + escapeHtml(assigned)
                + "</p>"

                + "<p><b>Sugerencia:</b> "
                + escapeHtml(suggested)
                + "</p>"

                + "</div>"

                + "<div class='result'>"
                + escapeHtml(recommendation)
                + "</div>"

                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Informe inteligente",
                "La visita fue procesada "
                + "y guardada en el expediente.",
                content
            )
        );
    }

    private void searchRecord(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        Map<String, String> data =
                readForm(exchange);

        String key =
                buildKey(
                    value(
                        data.get("searchOwnerId"),
                        ""
                    ),
                    value(
                        data.get("searchPet"),
                        ""
                    )
                );

        String content;

        if (records.containsKey(key)) {

            content =
                    "<div class='card'>"

                    + "<h2>Expediente encontrado</h2>"

                    + records.get(key)

                    + "<h2>Visitas registradas</h2>"

                    + visitsHtml(key)

                    + "</div>";

        } else {

            content =
                    "<div class='card'>"

                    + "<div class='empty'>"
                    + "No se encontró un expediente "
                    + "con esos datos."
                    + "</div>"

                    + "</div>";
        }

        sendResponse(
            exchange,
            layout(
                "Resultado de búsqueda",
                "Consulta el expediente "
                + "y sus visitas registradas.",
                content
            )
        );
    }

    private void scheduleAppointment(
            HttpExchange exchange
    ) throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        Map<String, String> data =
                readForm(exchange);

        String ownerId =
                value(
                    data.get("appointmentOwnerId"),
                    "No registrada"
                );

        String ownerName =
                value(
                    data.get("appointmentOwnerName"),
                    "No registrado"
                );

        String petName =
                value(
                    data.get("appointmentPet"),
                    "No registrado"
                );

        String type =
                value(
                    data.get("appointmentType"),
                    "Consulta general"
                );

        String date =
                value(
                    data.get("appointmentDate"),
                    "Sin fecha"
                );

        String time =
                value(
                    data.get("appointmentTime"),
                    "Sin hora"
                );

        String status =
                value(
                    data.get("appointmentStatus"),
                    "Programada"
                );

        String reason =
                value(
                    data.get("appointmentReason"),
                    "Sin motivo"
                );

        String key =
                buildKey(ownerId, petName)
                + "-"
                + date
                + "-"
                + time;

        String card =
                "<div class='record'>"

                + "<span class='tag "
                + appointmentClass(status)
                + "'>"
                + escapeHtml(status)
                + "</span>"

                + "<h3>"
                + escapeHtml(petName)
                + "</h3>"

                + "<p><b>Cédula:</b> "
                + escapeHtml(ownerId)
                + "</p>"

                + "<p><b>Responsable:</b> "
                + escapeHtml(ownerName)
                + "</p>"

                + "<p><b>Veterinario:</b> "
                + escapeHtml(activeVeterinarian)
                + "</p>"

                + "<p><b>Tipo:</b> "
                + escapeHtml(type)
                + "</p>"

                + "<p><b>Fecha:</b> "
                + escapeHtml(date)
                + "</p>"

                + "<p><b>Hora:</b> "
                + escapeHtml(time)
                + "</p>"

                + "<p><b>Motivo:</b> "
                + escapeHtml(reason)
                + "</p>"

                + "</div>";

        appointments.put(key, card);

        sendResponse(
            exchange,
            layout(
                "Cita registrada",
                "La cita fue guardada correctamente.",
                "<div class='card'>"
                + card
                + "</div>"
            )
        );
    }

    private void askAI(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        Map<String, String> data =
                readForm(exchange);

        String question =
                value(data.get("question"), "");

        String answer =
                cleanMarkdown(
                    new AIService()
                        .answerPetQuestion(
                            question
                            + "\nResponde en texto plano, "
                            + "sin Markdown."
                        )
                );

        String item =
                "<div class='record'>"

                + "<h3>Consulta IA</h3>"

                + "<p><b>Veterinario:</b> "
                + escapeHtml(activeVeterinarian)
                + "</p>"

                + "<p><b>Pregunta:</b> "
                + escapeHtml(question)
                + "</p>"

                + "<div class='result'>"
                + escapeHtml(answer)
                + "</div>"

                + "</div>";

        aiHistory.add(item);

        sendResponse(
            exchange,
            layout(
                "Respuesta del asistente",
                "Consulta procesada por Gemini.",
                "<div class='card'>"
                + item
                + "</div>"
            )
        );
    }

    private void showHistory(HttpExchange exchange)
            throws IOException {

        if (!isLoggedIn()) {
            sendResponse(exchange, loginPage(""));
            return;
        }

        StringBuilder recordHtml =
                new StringBuilder();

        for (String key : records.keySet()) {
            recordHtml.append(records.get(key));
            recordHtml.append("<h3>Visitas</h3>");
            recordHtml.append(visitsHtml(key));
        }

        StringBuilder appointmentHtml =
                new StringBuilder();

        for (String appointment : appointments.values()) {
            appointmentHtml.append(appointment);
        }

        StringBuilder aiHtml =
                new StringBuilder();

        for (String item : aiHistory) {
            aiHtml.append(item);
        }

        String content =
                "<div class='card'>"

                + "<h2>Expedientes</h2>"

                + (
                    recordHtml.length() == 0

                    ? "<div class='empty'>"
                    + "No hay expedientes."
                    + "</div>"

                    : recordHtml
                )

                + "</div>"

                + "<div class='card'>"

                + "<h2>Citas</h2>"

                + (
                    appointmentHtml.length() == 0

                    ? "<div class='empty'>"
                    + "No hay citas."
                    + "</div>"

                    : appointmentHtml
                )

                + "</div>"

                + "<div class='card'>"

                + "<h2>Consultas IA</h2>"

                + (
                    aiHtml.length() == 0

                    ? "<div class='empty'>"
                    + "No hay consultas."
                    + "</div>"

                    : aiHtml
                )

                + "</div>";

        sendResponse(
            exchange,
            layout(
                "Historial",
                "Consulta la información registrada "
                + "durante la sesión.",
                content
            )
        );
    }

    private int countHospitalized() {
        int count = 0;

        for (String record : records.values()) {
            if (record.contains("Hospitalizado")
                    || record.contains("Internado")
                    || record.contains("En observación")) {

                count++;
            }
        }

        return count;
    }

    private String visitsHtml(String key) {
        ArrayList<String> list =
                visits.get(key);

        if (list == null || list.isEmpty()) {
            return "<div class='empty'>"
                    + "No hay visitas registradas."
                    + "</div>";
        }

        StringBuilder builder =
                new StringBuilder();

        for (String visit : list) {
            builder.append(visit);
        }

        return builder.toString();
    }

    private Map<String, String> readForm(
            HttpExchange exchange
    ) throws IOException {

        String formData =
                new String(
                    exchange
                        .getRequestBody()
                        .readAllBytes(),
                    StandardCharsets.UTF_8
                );

        Map<String, String> data =
                new HashMap<>();

        if (formData.isBlank()) {
            return data;
        }

        for (String pair : formData.split("&")) {
            String[] keyValue =
                    pair.split("=", 2);

            if (keyValue.length == 2) {
                String key =
                        URLDecoder.decode(
                            keyValue[0],
                            StandardCharsets.UTF_8
                        );

                String value =
                        URLDecoder.decode(
                            keyValue[1],
                            StandardCharsets.UTF_8
                        );

                data.put(key, value);
            }
        }

        return data;
    }

    private void sendResponse(
            HttpExchange exchange,
            String response
    ) throws IOException {

        exchange
            .getResponseHeaders()
            .set(
                "Content-Type",
                "text/html; charset=UTF-8"
            );

        byte[] bytes =
                response.getBytes(
                    StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
            200,
            bytes.length
        );

        try (
            OutputStream output =
                exchange.getResponseBody()
        ) {
            output.write(bytes);
        }
    }

    private String buildKey(
            String ownerId,
            String petName
    ) {
        return value(ownerId, "")
                .trim()
                .toLowerCase()
                .replace(" ", "")

                + "-"

                + value(petName, "")
                .trim()
                .toLowerCase();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(
                value == null
                ? "0"
                : value.trim()
            );

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String value(
            String text,
            String defaultValue
    ) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }

        return text.trim();
    }

    private String cleanMarkdown(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("***", "")
                .replace("**", "")
                .replace("*", "")
                .replace("###", "")
                .replace("##", "")
                .replace("#", "")
                .replace("`", "")
                .replace("---", "")
                .replace("___", "")
                .trim();
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        return cleanMarkdown(text)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private String priorityClass(String priority) {
        String text =
                value(priority, "Normal")
                .toLowerCase();

        if (text.contains("urgente")) {
            return "urgent";
        }

        if (text.contains("importante")) {
            return "important";
        }

        if (text.contains("seguimiento")) {
            return "follow";
        }

        return "normal";
    }

    private String appointmentClass(String status) {
        String text =
                value(status, "Programada")
                .toLowerCase();

        if (text.contains("asistió")
                || text.contains("asistio")) {

            return "normal";
        }

        if (text.contains("seguimiento")
                || text.contains("reprogramada")) {

            return "follow";
        }

        if (text.contains("cancelada")
                || text.contains("no asistió")
                || text.contains("no asistio")) {

            return "important";
        }

        return "neutral";
    }

    private String suggestVeterinarian(
            String consultation,
            String hospitalization,
            String priority
    ) {
        String consultationText =
                value(consultation, "")
                .toLowerCase();

        String hospitalizationText =
                value(hospitalization, "")
                .toLowerCase();

        String priorityText =
                value(priority, "")
                .toLowerCase();

        if (hospitalizationText.contains("hospitalizado")
                || hospitalizationText.contains("internado")
                || hospitalizationText.contains("observación")
                || hospitalizationText.contains("observacion")
                || priorityText.contains("urgente")
                || priorityText.contains("importante")) {

            return "Dra. Sofia Castro - "
                    + "Hospitalización y seguimiento clínico";
        }

        if (consultationText.contains("vacun")
                || consultationText.contains("preventivo")
                || consultationText.contains("revisión")
                || consultationText.contains("revision")) {

            return "Dra. Ana Rodriguez - "
                    + "Vacunación y medicina preventiva";
        }

        return "Dr. Carlos Mora - Medicina general";
    }

    private String cleanVetName(String option) {
        String text =
                value(option, "");

        if (text.contains("Carlos Mora")) {
            return "Dr. Carlos Mora";
        }

        if (text.contains("Ana Rodriguez")) {
            return "Dra. Ana Rodriguez";
        }

        if (text.contains("Sofia Castro")) {
            return "Dra. Sofia Castro";
        }

        return text;
    }
}