package com.project.wrappergemini.repository;

import com.project.wrappergemini.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceso a datos de las conversaciones
 * Proporciona métodos para persistir y recuperar conversaciones de la base de datos
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    
    /**
     * Encuentra todas las conversaciones ordenadas por fecha ascendente (más antiguas primero)
     * @return Lista de conversaciones ordenadas cronológicamente
     */
    List<Conversation> findAllByOrderByTimestampAsc();
} 