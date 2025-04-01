package com.project.wrappergemini.controller;

import com.project.wrappergemini.dto.ConversationDTO;
import com.project.wrappergemini.model.Conversation;
import com.project.wrappergemini.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para el controlador GeminiController
 * 
 * Esta clase prueba la funcionalidad del controlador que maneja las peticiones
 * web relacionadas con la interacción del usuario con el modelo Gemini AI.
 * Se utilizan mocks para simular el comportamiento del servicio y del modelo de Spring.
 */
@ExtendWith(MockitoExtension.class)
class GeminiControllerTest {

    /**
     * Mock del servicio de Gemini para simular las interacciones con la API
     * y la base de datos sin realizar llamadas reales
     */
    @Mock
    private GeminiService geminiService;

    /**
     * Mock del modelo de Spring para verificar que los atributos
     * se añaden correctamente para las vistas
     */
    @Mock
    private Model model;

    /**
     * Instancia del controlador a probar, con los mocks inyectados automáticamente
     */
    @InjectMocks
    private GeminiController geminiController;

    /**
     * Prueba el método de página principal (homePage)
     * 
     * Verifica que:
     * 1. Se devuelve el nombre de vista correcto ("index")
     * 2. Se obtiene el historial de conversaciones del servicio
     * 3. Se añaden los atributos necesarios al modelo
     */
    @Test
    void testHomePage() {
        // Arrange - Preparar los datos y comportamiento esperado
        List<Conversation> conversations = Arrays.asList(
            new Conversation(1L, "Question 1", "Answer 1", LocalDateTime.now()),
            new Conversation(2L, "Question 2", "Answer 2", LocalDateTime.now())
        );
        when(geminiService.getConversationHistory()).thenReturn(conversations);

        // Act - Ejecutar el método a probar
        String viewName = geminiController.homePage(model);

        // Assert - Verificar los resultados
        assertEquals("index", viewName, "Debe devolver la vista 'index'");
        verify(model).addAttribute(eq("conversations"), any(List.class));
        verify(model).addAttribute(eq("conversation"), any(ConversationDTO.class));
        verify(geminiService).getConversationHistory();
    }

    /**
     * Prueba el método de envío de preguntas a Gemini (askGemini)
     * 
     * Verifica que:
     * 1. Se envía la pregunta al servicio para su procesamiento
     * 2. Se obtiene el historial actualizado después de procesar la pregunta
     * 3. Se añaden los atributos necesarios al modelo
     * 4. Se devuelve el nombre de vista correcto ("index")
     */
    @Test
    void testAskGemini() {
        // Arrange - Preparar los datos y comportamiento esperado
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setQuestion("Test question");
        
        List<Conversation> conversations = Arrays.asList(
            new Conversation(1L, "Question 1", "Answer 1", LocalDateTime.now())
        );
        when(geminiService.getConversationHistory()).thenReturn(conversations);

        // Act - Ejecutar el método a probar
        String viewName = geminiController.askGemini(conversationDTO, model);

        // Assert - Verificar los resultados
        assertEquals("index", viewName, "Debe devolver la vista 'index'");
        verify(geminiService).generateResponse("Test question");
        verify(geminiService).getConversationHistory();
        verify(model).addAttribute(eq("conversations"), any(List.class));
        verify(model).addAttribute(eq("conversation"), any(ConversationDTO.class));
    }
} 