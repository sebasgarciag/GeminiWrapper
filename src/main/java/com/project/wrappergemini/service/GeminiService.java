package com.project.wrappergemini.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.wrappergemini.dto.GeminiRequest;
import com.project.wrappergemini.dto.GeminiResponse;
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
    private static final int MAX_PROMPT_LENGTH = 30000; // Límite máximo de caracteres para el prompt
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.model}")
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
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Genera una respuesta de la API de Gemini
     * @param prompt La pregunta o texto de entrada
     * @return La respuesta generada por el modelo
     * @throws IllegalArgumentException si el prompt está vacío o excede el límite de caracteres
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

            // Validar longitud del prompt
            if (prompt.length() > MAX_PROMPT_LENGTH) {
                throw new IllegalArgumentException("La pregunta excede el límite máximo de " + MAX_PROMPT_LENGTH + " caracteres");
            }
            
            // Crear el cuerpo de la solicitud usando DTO
            GeminiRequest request = new GeminiRequest();
            GeminiRequest.Content content = new GeminiRequest.Content();
            GeminiRequest.Part part = new GeminiRequest.Part();
            part.setText(prompt);
            content.setParts(List.of(part));
            request.setContents(List.of(content));
            
            // Configurar parámetros de generación
            GeminiRequest.GenerationConfig config = new GeminiRequest.GenerationConfig();
            request.setGenerationConfig(config);
            
            // Crear solicitud HTTP
            String apiUrl = GEMINI_API_BASE_URL + modelName + ":generateContent";
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(request)))
                    .build();
            
            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            // Verificar código de respuesta
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la API de Gemini: " + response.statusCode() + " - " + response.body());
            }
            
            // Analizar respuesta usando DTO
            GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);
            String answer = geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
            
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