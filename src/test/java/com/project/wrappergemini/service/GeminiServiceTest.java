package com.project.wrappergemini.service;

import com.project.wrappergemini.model.Conversation;
import com.project.wrappergemini.repository.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para GeminiService
 * 
 * Esta clase prueba la funcionalidad del servicio que interactúa con la API de Gemini
 * y gestiona el almacenamiento de conversaciones. Utiliza mocks para simular
 * las respuestas HTTP y el comportamiento del repositorio sin realizar llamadas reales.
 */
@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    /**
     * Mock del repositorio para simular el acceso a la base de datos
     * sin realizar operaciones reales
     */
    @Mock
    private ConversationRepository conversationRepository;

    /**
     * Mock del cliente HTTP para simular las llamadas a la API de Gemini
     * sin realizar peticiones reales
     */
    @Mock
    private HttpClient httpClient;

    /**
     * Mock de la respuesta HTTP para controlar lo que devuelve la API simulada
     */
    @Mock
    private HttpResponse<String> httpResponse;

    /**
     * Instancia del servicio a probar, con los mocks inyectados automáticamente
     */
    @InjectMocks
    private GeminiService geminiService;

    /**
     * Configuración inicial para cada prueba
     * 
     * Establece valores simulados para las propiedades que normalmente
     * se configuran en application.properties o variables de entorno
     */
    @BeforeEach
    void setUp() {
        // Inyectamos valores de prueba en los campos privados del servicio
        ReflectionTestUtils.setField(geminiService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(geminiService, "modelName", "test-model");
        ReflectionTestUtils.setField(geminiService, "httpClient", httpClient);
    }

    /**
     * Prueba la generación de respuestas con entrada válida
     * 
     * Verifica que:
     * 1. Se realiza correctamente la llamada HTTP a la API de Gemini
     * 2. Se procesa correctamente la respuesta JSON
     * 3. Se guarda la conversación en la base de datos
     * 4. Se devuelve el texto de respuesta esperado
     */
    @Test
    void testGenerateResponse_ValidInput() throws Exception {
        // Arrange - Preparar los datos y comportamiento esperado
        String prompt = "Test question";
        // JSON simulado que reproduce el formato de respuesta de la API de Gemini
        String mockResponse = "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"Test answer\"}]}}]}";
        
        // Configurar el mock de respuesta HTTP
        when(httpResponse.body()).thenReturn(mockResponse);
        when(httpResponse.statusCode()).thenReturn(200); // Código 200 = OK
        
        // Configurar el mock del cliente HTTP
        when(httpClient.send(any(HttpRequest.class), Mockito.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpResponse);
            
        // Configurar el mock del repositorio
        when(conversationRepository.save(any(Conversation.class)))
            .thenReturn(new Conversation(1L, prompt, "Test answer", LocalDateTime.now()));

        // Act - Ejecutar el método a probar
        String response = geminiService.generateResponse(prompt);

        // Assert - Verificar los resultados
        assertNotNull(response, "La respuesta no debería ser nula");
        assertEquals("Test answer", response, "Debería devolver el texto extraído de la respuesta JSON");
        
        // Verificar que se guardó la conversación
        verify(conversationRepository).save(any(Conversation.class));
        
        // Verificar que se realizó la llamada HTTP
        verify(httpClient).send(any(HttpRequest.class), Mockito.<HttpResponse.BodyHandler<String>>any());
    }

    /**
     * Prueba el comportamiento con entrada vacía
     * 
     * Verifica que se lanza una excepción cuando el prompt está vacío,
     * lo que indica validación de entrada
     */
    @Test
    void testGenerateResponse_EmptyInput() {
        // Act & Assert - Ejecutar y verificar
        assertThrows(RuntimeException.class, () -> {
            geminiService.generateResponse("");
        }, "Debería lanzar una excepción con entrada vacía");
        
        // Verificar que no se intentó guardar nada en el repositorio
        verify(conversationRepository, never()).save(any());
    }

    /**
     * Prueba el comportamiento con entrada nula
     * 
     * Verifica que se lanza una excepción cuando el prompt es null,
     * lo que indica validación de entrada
     */
    @Test
    void testGenerateResponse_NullInput() {
        // Act & Assert - Ejecutar y verificar
        assertThrows(RuntimeException.class, () -> {
            geminiService.generateResponse(null);
        }, "Debería lanzar una excepción con entrada nula");
        
        // Verificar que no se intentó guardar nada en el repositorio
        verify(conversationRepository, never()).save(any());
    }

    /**
     * Prueba la obtención del historial de conversaciones
     * 
     * Verifica que:
     * 1. Se llama al método correcto del repositorio
     * 2. Se devuelve la lista de conversaciones sin modificar
     */
    @Test
    void testGetConversationHistory() {
        // Arrange - Preparar los datos y comportamiento esperado
        List<Conversation> expectedConversations = Arrays.asList(
            new Conversation(1L, "Question 1", "Answer 1", LocalDateTime.now()),
            new Conversation(2L, "Question 2", "Answer 2", LocalDateTime.now())
        );
        when(conversationRepository.findAllByOrderByTimestampAsc())
            .thenReturn(expectedConversations);

        // Act - Ejecutar el método a probar
        List<Conversation> actualConversations = geminiService.getConversationHistory();

        // Assert - Verificar los resultados
        assertEquals(expectedConversations, actualConversations, 
            "Debería devolver exactamente la misma lista del repositorio");
        assertEquals(2, actualConversations.size(), 
            "Debería contener el número correcto de conversaciones");
            
        verify(conversationRepository).findAllByOrderByTimestampAsc();
    }
} 