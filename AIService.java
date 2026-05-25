import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIService {

    public String generatePetCareAdvice(Pet pet, Vaccine vaccine, MedicalRecord medicalRecord) {
        String prompt =
                "Eres un asistente veterinario informativo dentro de un sistema llamado Smart PetCare Assistant. "
                + "Tu función es analizar datos de mascotas y generar recomendaciones básicas de cuidado. "
                + "No des diagnósticos definitivos. No reemplaces a un veterinario profesional. "
                + "Responde en español, de forma clara, ordenada y útil. "
                + "Usa subtítulos cortos. "
                + "No respondas temas que no sean de mascotas.\n\n"
                + "DATOS DE LA MASCOTA:\n"
                + "Nombre: " + pet.getName() + "\n"
                + "Especie: " + pet.getSpecies() + "\n"
                + "Raza: " + pet.getBreed() + "\n"
                + "Edad: " + pet.getAge() + " años\n"
                + "Vacuna registrada: " + vaccine.getVaccineName() + "\n"
                + "Fecha de aplicación: " + vaccine.getApplicationDate() + "\n"
                + "Próxima vacunación: " + vaccine.getNextDueDate() + "\n"
                + "Diagnóstico registrado: " + medicalRecord.getDiagnosis() + "\n"
                + "Tratamiento indicado: " + medicalRecord.getTreatment() + "\n\n"
                + "Genera:\n"
                + "1. Resumen del caso.\n"
                + "2. Recomendación de cuidado.\n"
                + "3. Señales de alerta generales.\n"
                + "4. Próximo paso recomendado.\n"
                + "5. Nota de que no sustituye una consulta veterinaria.";

        return askGemini(prompt);
    }

    public String answerPetQuestion(String question) {
        if (!isPetQuestionAllowed(question)) {
            return "Lo siento, este asistente solo responde preguntas relacionadas con mascotas, vacunas, citas veterinarias, diagnósticos, tratamientos y cuidados básicos.";
        }

        String prompt =
                "Eres un asistente veterinario informativo. "
                + "Responde únicamente preguntas sobre mascotas, vacunas, alimentación, higiene, salud general, citas veterinarias, diagnósticos registrados y tratamientos. "
                + "No respondas preguntas fuera del área de mascotas. "
                + "No des diagnósticos definitivos ni medicamentos específicos. "
                + "No reemplaces la consulta con un veterinario. "
                + "Responde en español, claro y útil.\n\n"
                + "Pregunta del usuario:\n"
                + question;

        return askGemini(prompt);
    }

    private boolean isPetQuestionAllowed(String question) {
        String q = question.toLowerCase();

        return q.contains("mascota") ||
               q.contains("perro") ||
               q.contains("gato") ||
               q.contains("conejo") ||
               q.contains("hamster") ||
               q.contains("ave") ||
               q.contains("cachorro") ||
               q.contains("vacuna") ||
               q.contains("vacunas") ||
               q.contains("rabia") ||
               q.contains("veterinario") ||
               q.contains("veterinaria") ||
               q.contains("cita") ||
               q.contains("consulta") ||
               q.contains("diagnostico") ||
               q.contains("diagnóstico") ||
               q.contains("tratamiento") ||
               q.contains("comida") ||
               q.contains("alimento") ||
               q.contains("alimentacion") ||
               q.contains("alimentación") ||
               q.contains("salud") ||
               q.contains("cuidado") ||
               q.contains("baño") ||
               q.contains("bano") ||
               q.contains("piel") ||
               q.contains("pelo") ||
               q.contains("ojos") ||
               q.contains("oreja") ||
               q.contains("orejas") ||
               q.contains("dientes") ||
               q.contains("boca");
    }

    private String askGemini(String prompt) {
        try {
            String apiKey = System.getenv("GEMINI_API_KEY");

            if (apiKey == null || apiKey.trim().isEmpty()) {
                return "No se encontró la API Key de Gemini. Configura la variable GEMINI_API_KEY antes de ejecutar el sistema.";
            }

            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            String jsonBody =
                    "{"
                    + "\"contents\":["
                    + "{"
                    + "\"parts\":["
                    + "{"
                    + "\"text\":\"" + escapeJson(prompt) + "\""
                    + "}"
                    + "]"
                    + "}"
                    + "]"
                    + "}";

            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
            outputStream.close();

            int statusCode = connection.getResponseCode();

            BufferedReader reader;

            if (statusCode >= 200 && statusCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            if (statusCode < 200 || statusCode >= 300) {
                return "Error al conectar con Gemini. Código: " + statusCode + "\n" + response.toString();
            }

            return extractTextFromGeminiResponse(response.toString());

        } catch (Exception e) {
            return "Error usando Gemini: " + e.getMessage();
        }
    }

    private String extractTextFromGeminiResponse(String json) {
        String marker = "\"text\":";
        int index = json.indexOf(marker);

        if (index == -1) {
            return "No se pudo leer la respuesta de Gemini.\nRespuesta recibida:\n" + json;
        }

        int start = json.indexOf("\"", index + marker.length());

        if (start == -1) {
            return "No se pudo interpretar la respuesta de Gemini.";
        }

        int end = start + 1;
        boolean escaped = false;

        while (end < json.length()) {
            char c = json.charAt(end);

            if (c == '\\' && !escaped) {
                escaped = true;
            } else if (c == '"' && !escaped) {
                break;
            } else {
                escaped = false;
            }

            end++;
        }

        String text = json.substring(start + 1, end);

        return text.replace("\\n", "\n")
                   .replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\u003c", "<")
                   .replace("\\u003e", ">");
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}