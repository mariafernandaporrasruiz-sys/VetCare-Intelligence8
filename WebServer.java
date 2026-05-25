import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class WebServer {

    private ArrayList<String> expedientes = new ArrayList<String>();
    private ArrayList<String> consultasIA = new ArrayList<String>();

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8085), 0);

            server.createContext("/", this::showHome);
            server.createContext("/generate", this::generateAdvice);
            server.createContext("/ask", this::askAI);
            server.createContext("/history", this::showHistory);

            server.start();

            System.out.println("VetCare Intelligence esta funcionando.");
            System.out.println("Abre en Chrome: http://localhost:8085");

        } catch (Exception e) {
            System.out.println("Error del servidor: " + e.getMessage());
        }
    }

    private String styles() {
        return "<style>"
                + "*{box-sizing:border-box;}"
                + "body{margin:0;font-family:Segoe UI,Arial,sans-serif;background:#f4f8fb;color:#25364a;}"
                + ".hero{background:linear-gradient(135deg,#0f766e,#22c1c3);color:white;padding:45px 25px 82px;text-align:center;}"
                + ".hero h1{margin:0;font-size:45px;letter-spacing:.4px;}"
                + ".hero p{max-width:820px;margin:12px auto 0;font-size:17px;opacity:.96;}"
                + ".nav{margin-top:24px;display:flex;justify-content:center;gap:12px;flex-wrap:wrap;}"
                + ".nav a{background:white;color:#0f766e;text-decoration:none;padding:11px 20px;border-radius:999px;font-weight:800;box-shadow:0 8px 18px rgba(0,0,0,.14);}"
                + ".nav a:hover{background:#ecfeff;}"
                + ".container{width:92%;max-width:1180px;margin:-45px auto 45px;}"
                + ".layout{display:grid;grid-template-columns:1.35fr .75fr;gap:26px;align-items:start;}"
                + ".card{background:white;border-radius:26px;padding:30px;box-shadow:0 18px 45px rgba(15,118,110,.15);margin-bottom:26px;}"
                + ".card h2{margin-top:0;color:#0f766e;font-size:25px;}"
                + ".info{background:linear-gradient(135deg,#ecfeff,#f8ffff);border:1px solid #c7f9f4;border-left:7px solid #14b8a6;padding:17px;border-radius:18px;line-height:1.55;margin-bottom:20px;}"
                + ".section{font-size:20px;color:#0f766e;font-weight:900;margin-top:24px;margin-bottom:14px;border-bottom:2px solid #e7f5f4;padding-bottom:9px;}"
                + ".formgrid{display:grid;grid-template-columns:1fr 1fr;gap:17px;}"
                + ".full{grid-column:1/3;}"
                + "label{display:block;font-weight:800;margin-bottom:7px;color:#334155;}"
                + "input,textarea,select{width:100%;padding:13px;border-radius:15px;border:1px solid #cbd5e1;background:#fbfeff;font-size:15px;}"
                + "input:focus,textarea:focus,select:focus{outline:none;border-color:#14b8a6;box-shadow:0 0 0 4px rgba(20,184,166,.14);}"
                + "textarea{min-height:100px;resize:vertical;}"
                + "button{width:100%;padding:15px;margin-top:24px;border:0;border-radius:17px;background:linear-gradient(135deg,#0f766e,#14b8a6);color:white;font-size:17px;font-weight:900;cursor:pointer;box-shadow:0 12px 25px rgba(20,184,166,.28);}"
                + "button:hover{background:#0f766e;transform:translateY(-1px);}"
                + ".feature{background:#f8ffff;border:1px solid #d9f5f2;padding:17px;border-radius:18px;margin-bottom:14px;}"
                + ".feature strong{display:block;color:#0f766e;margin-bottom:6px;}"
                + ".result{background:#f0fdfa;border-left:8px solid #14b8a6;padding:24px;border-radius:20px;white-space:pre-line;line-height:1.65;font-size:15.5px;}"
                + ".tag{display:inline-block;background:#ccfbf1;color:#0f766e;padding:7px 13px;border-radius:999px;font-weight:900;font-size:13px;margin-bottom:12px;}"
                + ".item{background:#f8ffff;border:1px solid #d9f5f2;padding:18px;border-radius:18px;margin-bottom:15px;white-space:pre-line;line-height:1.55;}"
                + ".summary{display:grid;grid-template-columns:repeat(4,1fr);gap:15px;margin-bottom:22px;}"
                + ".box{background:#f8ffff;border:1px solid #d9f5f2;border-radius:18px;padding:16px;}"
                + ".box strong{display:block;color:#0f766e;margin-bottom:5px;}"
                + ".mini{font-size:13px;color:#64748b;text-align:center;margin-top:14px;}"
                + ".dashboard{display:grid;grid-template-columns:repeat(3,1fr);gap:15px;margin-bottom:24px;}"
                + ".dash{background:#ecfeff;border:1px solid #c7f9f4;border-radius:18px;padding:18px;text-align:center;}"
                + ".dash strong{display:block;font-size:28px;color:#0f766e;}"
                + ".dash span{color:#64748b;font-weight:700;}"
                + "@media(max-width:900px){.layout{grid-template-columns:1fr}.summary{grid-template-columns:1fr 1fr}.dashboard{grid-template-columns:1fr;}}"
                + "@media(max-width:650px){.formgrid{grid-template-columns:1fr}.full{grid-column:1}.summary{grid-template-columns:1fr}.hero h1{font-size:31px}.container{margin-top:-35px}}"
                + "</style>";
    }

    private void showHome(HttpExchange exchange) throws IOException {
        String html = "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head><meta charset='UTF-8'><title>VetCare Intelligence</title>" + styles() + "</head>"
                + "<body>"

                + "<div class='hero'>"
                + "<h1>VetCare Intelligence</h1>"
                + "<p>Expediente clinico inteligente para mascotas, vacunas, controles veterinarios y recomendaciones con IA.</p>"
                + "<div class='nav'>"
                + "<a href='/'>Panel principal</a>"
                + "<a href='/history'>Expedientes clinicos</a>"
                + "</div>"
                + "</div>"

                + "<div class='container'>"

                + "<div class='dashboard'>"
                + "<div class='dash'><strong>" + expedientes.size() + "</strong><span>Expedientes</span></div>"
                + "<div class='dash'><strong>" + consultasIA.size() + "</strong><span>Consultas IA</span></div>"
                + "<div class='dash'><strong>IA</strong><span>Gemini activo</span></div>"
                + "</div>"

                + "<div class='layout'>"

                + "<div class='card'>"
                + "<h2>Nuevo expediente clinico</h2>"
                + "<div class='info'>Registra los datos de la mascota, diagnostico, sintomas, vacunas y proximo control. El sistema genera un informe inteligente y guarda el expediente durante la sesion.</div>"

                + "<form method='POST' action='/generate'>"

                + "<div class='section'>Responsable</div>"
                + "<div class='formgrid'>"
                + "<div class='full'><label>Nombre del responsable</label><input type='text' name='ownerName' value='Maria Fernanda Porras Ruiz' required></div>"
                + "</div>"

                + "<div class='section'>Paciente</div>"
                + "<div class='formgrid'>"
                + "<div><label>Nombre de la mascota</label><input type='text' name='petName' value='Luna' required></div>"
                + "<div><label>Especie</label><input type='text' name='species' value='Perro' required></div>"
                + "<div><label>Raza</label><input type='text' name='breed' value='Golden Retriever' required></div>"
                + "<div><label>Edad</label><input type='number' name='age' value='3' required></div>"
                + "<div><label>Peso aproximado</label><input type='text' name='weight' value='18 kg'></div>"
                + "<div><label>Estado general</label><select name='generalStatus'><option>Estable</option><option>En observacion</option><option>Requiere seguimiento</option><option>Control pendiente</option></select></div>"
                + "</div>"

                + "<div class='section'>Vacunas y controles</div>"
                + "<div class='formgrid'>"
                + "<div><label>Vacuna registrada</label><input type='text' name='vaccineName' value='Vacuna contra la rabia' required></div>"
                + "<div><label>Fecha de aplicacion</label><input type='date' name='applicationDate' value='2026-05-20' required></div>"
                + "<div><label>Proxima vacunacion</label><input type='date' name='nextDueDate' value='2027-05-20' required></div>"
                + "<div><label>Proximo control veterinario</label><input type='date' name='nextControlDate' value='2026-06-25'></div>"
                + "</div>"

                + "<div class='section'>Informacion medica</div>"
                + "<div class='formgrid'>"
                + "<div class='full'><label>Diagnostico</label><input type='text' name='diagnosis' value='Condicion saludable' required></div>"
                + "<div class='full'><label>Sintomas observados</label><textarea name='symptoms'>No presenta sintomas importantes al momento del registro.</textarea></div>"
                + "<div class='full'><label>Tratamiento o indicaciones</label><textarea name='treatment' required>Continuar con vacunacion regular y controles veterinarios.</textarea></div>"
                + "<div class='full'><label>Observaciones medicas</label><textarea name='observations'>Se recomienda mantener seguimiento preventivo y revisar el proximo control.</textarea></div>"
                + "</div>"

                + "<button type='submit'>Crear expediente e informe IA</button>"
                + "</form>"
                + "</div>"

                + "<div>"
                + "<div class='card'>"
                + "<h2>Asistente IA</h2>"
                + "<div class='feature'><strong>Expediente por paciente</strong>Guarda datos clinicos, vacunas, diagnostico y proximo control.</div>"
                + "<div class='feature'><strong>Gemini API</strong>Genera respuestas dinamicas segun la informacion registrada.</div>"
                + "<div class='feature'><strong>Control de dominio</strong>Responde solamente preguntas relacionadas con mascotas.</div>"
                + "</div>"

                + "<div class='card'>"
                + "<h2>Consulta rapida</h2>"
                + "<form method='POST' action='/ask'>"
                + "<label>Pregunta para la IA</label>"
                + "<textarea name='question' required>Que cuidados necesita una mascota con vacunas pendientes?</textarea>"
                + "<button type='submit'>Consultar IA</button>"
                + "</form>"
                + "<div class='mini'>La consulta se guardara en el historial de la sesion.</div>"
                + "</div>"
                + "</div>"

                + "</div>"
                + "</div>"

                + "</body></html>";

        sendResponse(exchange, html);
    }

    private void generateAdvice(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, "Solicitud invalida.");
            return;
        }

        Map<String, String> data = readForm(exchange);

        String ownerName = data.get("ownerName");
        String petName = data.get("petName");
        String species = data.get("species");
        String breed = data.get("breed");
        int age = Integer.parseInt(data.get("age"));
        String weight = data.get("weight");
        String generalStatus = data.get("generalStatus");
        String vaccineName = data.get("vaccineName");
        String applicationDate = data.get("applicationDate");
        String nextDueDate = data.get("nextDueDate");
        String nextControlDate = data.get("nextControlDate");
        String diagnosis = data.get("diagnosis");
        String symptoms = data.get("symptoms");
        String treatment = data.get("treatment");
        String observations = data.get("observations");

        Owner owner = new Owner(1, ownerName, "N/A", "N/A");
        Pet pet = new Pet(1, petName, species, breed, age, owner.getId());
        Vaccine vaccine = new Vaccine(1, pet.getId(), vaccineName, applicationDate, nextDueDate);

        String fullTreatment = treatment
                + "\nSintomas observados: " + symptoms
                + "\nPeso aproximado: " + weight
                + "\nEstado general: " + generalStatus
                + "\nProximo control veterinario: " + nextControlDate
                + "\nObservaciones medicas: " + observations
                + "\nImportante: responder sin Markdown, sin asteriscos y en texto plano.";

        MedicalRecord record = new MedicalRecord(1, pet.getId(), "Expediente clinico registrado desde la web.", diagnosis, fullTreatment, "2026-05-25");

        AIService aiService = new AIService();
        String recommendation = cleanMarkdown(aiService.generatePetCareAdvice(pet, vaccine, record));

        String expediente = "EXPEDIENTE CLINICO\n\n"
                + "Responsable: " + ownerName + "\n"
                + "Paciente: " + petName + "\n"
                + "Especie: " + species + "\n"
                + "Raza: " + breed + "\n"
                + "Edad: " + age + " años\n"
                + "Peso: " + weight + "\n"
                + "Estado general: " + generalStatus + "\n\n"
                + "VACUNAS Y CONTROLES\n"
                + "Vacuna registrada: " + vaccineName + "\n"
                + "Fecha de aplicacion: " + applicationDate + "\n"
                + "Proxima vacuna: " + nextDueDate + "\n"
                + "Proximo control: " + nextControlDate + "\n\n"
                + "INFORMACION MEDICA\n"
                + "Diagnostico: " + diagnosis + "\n"
                + "Sintomas: " + symptoms + "\n"
                + "Tratamiento: " + treatment + "\n"
                + "Observaciones: " + observations + "\n\n"
                + "INFORME IA\n"
                + recommendation;

        expedientes.add(expediente);

        String html = "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head><meta charset='UTF-8'><title>Informe inteligente</title>" + styles() + "</head>"
                + "<body>"
                + "<div class='hero'>"
                + "<h1>Informe inteligente</h1>"
                + "<p>Analisis generado con base en el expediente clinico registrado.</p>"
                + "<div class='nav'><a href='/'>Nuevo expediente</a><a href='/history'>Ver expedientes</a></div>"
                + "</div>"

                + "<div class='container'>"
                + "<div class='card'>"
                + "<div class='summary'>"
                + "<div class='box'><strong>Paciente</strong>" + escapeHtml(petName) + "</div>"
                + "<div class='box'><strong>Especie</strong>" + escapeHtml(species) + "</div>"
                + "<div class='box'><strong>Proxima vacuna</strong>" + escapeHtml(nextDueDate) + "</div>"
                + "<div class='box'><strong>Proximo control</strong>" + escapeHtml(nextControlDate) + "</div>"
                + "</div>"
                + "<span class='tag'>Expediente guardado</span>"
                + "<div class='result'>" + escapeHtml(recommendation) + "</div>"
                + "</div>"
                + "</div>"

                + "</body></html>";

        sendResponse(exchange, html);
    }

    private void askAI(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, "Solicitud invalida.");
            return;
        }

        Map<String, String> data = readForm(exchange);
        String question = data.get("question") + "\nResponde sin Markdown, sin asteriscos, sin simbolos ** y en texto plano.";

        AIService aiService = new AIService();
        String answer = cleanMarkdown(aiService.answerPetQuestion(question));

        String item = "Pregunta: " + data.get("question") + "\n\nRespuesta IA:\n" + answer;
        consultasIA.add(item);

        String html = "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head><meta charset='UTF-8'><title>Respuesta IA</title>" + styles() + "</head>"
                + "<body>"
                + "<div class='hero'>"
                + "<h1>Respuesta del asistente</h1>"
                + "<p>Consulta procesada por IA especializada en mascotas.</p>"
                + "<div class='nav'><a href='/'>Panel principal</a><a href='/history'>Expedientes</a></div>"
                + "</div>"
                + "<div class='container'>"
                + "<div class='card'>"
                + "<span class='tag'>Consulta procesada</span>"
                + "<div class='result'>" + escapeHtml(item) + "</div>"
                + "</div>"
                + "</div>"
                + "</body></html>";

        sendResponse(exchange, html);
    }

    private void showHistory(HttpExchange exchange) throws IOException {
        String expedienteHtml = "";
        String consultasHtml = "";

        if (expedientes.isEmpty()) {
            expedienteHtml = "<div class='item'>Todavia no hay expedientes clinicos guardados.</div>";
        } else {
            for (int i = 0; i < expedientes.size(); i++) {
                expedienteHtml += "<div class='item'><span class='tag'>Expediente " + (i + 1) + "</span><br>"
                        + escapeHtml(expedientes.get(i)) + "</div>";
            }
        }

        if (consultasIA.isEmpty()) {
            consultasHtml = "<div class='item'>Todavia no hay consultas realizadas a la IA.</div>";
        } else {
            for (int i = 0; i < consultasIA.size(); i++) {
                consultasHtml += "<div class='item'><span class='tag'>Consulta " + (i + 1) + "</span><br>"
                        + escapeHtml(consultasIA.get(i)) + "</div>";
            }
        }

        String html = "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head><meta charset='UTF-8'><title>Expedientes clinicos</title>" + styles() + "</head>"
                + "<body>"
                + "<div class='hero'>"
                + "<h1>Expedientes clinicos</h1>"
                + "<p>Pacientes registrados, vacunas pendientes, proximos controles e informes generados con IA.</p>"
                + "<div class='nav'><a href='/'>Panel principal</a><a href='/history'>Actualizar</a></div>"
                + "</div>"
                + "<div class='container'>"
                + "<div class='dashboard'>"
                + "<div class='dash'><strong>" + expedientes.size() + "</strong><span>Expedientes</span></div>"
                + "<div class='dash'><strong>" + consultasIA.size() + "</strong><span>Consultas IA</span></div>"
                + "<div class='dash'><strong>Activo</strong><span>Historial de sesion</span></div>"
                + "</div>"
                + "<div class='card'><h2>Expedientes de pacientes</h2>" + expedienteHtml + "</div>"
                + "<div class='card'><h2>Consultas realizadas a la IA</h2>" + consultasHtml + "</div>"
                + "</div>"
                + "</body></html>";

        sendResponse(exchange, html);
    }

    private Map<String, String> readForm(HttpExchange exchange) throws IOException {
        String formData = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return parseFormData(formData);
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> data = new HashMap<String, String>();
        String[] pairs = formData.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);

            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                data.put(key, value);
            }
        }

        return data;
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream output = exchange.getResponseBody();
        output.write(bytes);
        output.close();
    }

    private String cleanMarkdown(String text) {
        if (text == null) {
            return "";
        }

        text = text.replace("***", "");
        text = text.replace("**", "");
        text = text.replace("*", "");
        text = text.replace("###", "");
        text = text.replace("##", "");
        text = text.replace("#", "");
        text = text.replace("`", "");
        text = text.replace("---", "");
        text = text.replace("___", "");

        return text.trim();
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        text = cleanMarkdown(text);

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
