package com.project.wrappergemini.controller;

import com.project.wrappergemini.model.Conversation;
import com.project.wrappergemini.service.GeminiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestionar las interacciones con la interfaz de usuario
 * Maneja las solicitudes HTTP y coordina con el servicio
 */
@Controller
public class GeminiController {

    private final GeminiService geminiService;

    /**
     * Constructor del controlador
     * @param geminiService Servicio de integración con Gemini AI
     */
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * Método para mostrar la página principal
     * @param model Modelo para transferir datos a la vista
     * @return Nombre de la plantilla a renderizar
     */
    @GetMapping("/")
    public String homePage(Model model) {
        List<Conversation> conversations = geminiService.getConversationHistory();
        model.addAttribute("conversations", conversations);
        model.addAttribute("conversation", new Conversation());
        return "index";
    }

    /**
     * Método para procesar preguntas al modelo Gemini
     * @param conversation Objeto que contiene la pregunta del usuario
     * @param model Modelo para transferir datos a la vista
     * @return Nombre de la plantilla a renderizar
     */
    @PostMapping("/ask")
    public String askGemini(@ModelAttribute("conversation") Conversation conversation, Model model) {
        String question = conversation.getQuestion();
        geminiService.generateResponse(question);
        
        List<Conversation> conversations = geminiService.getConversationHistory();
        model.addAttribute("conversations", conversations);
        model.addAttribute("conversation", new Conversation());
        return "index";
    }
} 