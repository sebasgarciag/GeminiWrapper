package com.project.wrappergemini.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.wrappergemini.model.Conversation;
import com.project.wrappergemini.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Servicio para interactuar con la API de Gemini
 * Maneja la comunicación con la API y almacena las conversaciones
 */
@Service
public class GeminiService {

    private static final String GEMINI_API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.model.name}")
    private String modelName;

    private final ConversationRepository conversationRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    /**
     * Constructor del servicio
     * @param conversationRepository Repositorio para acceso a datos
     */
    public GeminiService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Genera una respuesta de la API de Gemini
     * @param prompt La pregunta o texto de entrada
     * @return La respuesta generada por el modelo
     */
    public String generateResponse(String prompt) {
        try {
            // Validar que la API key esté presente
            if (!StringUtils.hasText(apiKey)) {
                throw new IllegalStateException("API key no configurada. Por favor configure la variable de entorno GEMINI_API_KEY");
            }
            
            // Validar que tenemos un prompt válido
            if (!StringUtils.hasText(prompt)) {
                throw new IllegalArgumentException("La pregunta no puede estar vacía");
            }
            
            // Crear el cuerpo de la solicitud
            ObjectNode requestBody = objectMapper.createObjectNode();
            ArrayNode contents = objectMapper.createArrayNode();
            ObjectNode content = objectMapper.createObjectNode();
            ArrayNode parts = objectMapper.createArrayNode();
            ObjectNode part = objectMapper.createObjectNode();
            
            part.put("text", prompt);
            parts.add(part);
            content.set("parts", parts);
            contents.add(content);
            requestBody.set("contents", contents);
            
            // Añadir configuración de generación
            ObjectNode generationConfig = objectMapper.createObjectNode();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("topP", 0.95);
            generationConfig.put("topK", 40);
            requestBody.set("generationConfig", generationConfig);
            
            // Crear solicitud HTTP con nombre de modelo dinámico
            String apiUrl = GEMINI_API_BASE_URL + modelName + ":generateContent";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
            
            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Verificar código de respuesta
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la API de Gemini: " + response.statusCode() + " - " + response.body());
            }
            
            // Analizar respuesta
            JsonNode responseBody = objectMapper.readTree(response.body());
            String answer = responseBody
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText("No se pudo obtener respuesta de Gemini");
            
            // Guardar conversación en la base de datos
            Conversation conversation = new Conversation();
            conversation.setQuestion(prompt);
            conversation.setAnswer(answer);
            conversationRepository.save(conversation);
            
            return answer;
        } catch (Exception e) {
            throw new RuntimeException("Error comunicándose con la API de Gemini: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el historial de conversaciones ordenado por fecha
     * @return Lista de conversaciones ordenadas cronológicamente
     */
    public List<Conversation> getConversationHistory() {
        return conversationRepository.findAllByOrderByTimestampAsc();
    }
} 