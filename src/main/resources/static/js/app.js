/**
 * Script principal para manejar la interacción del usuario con la interfaz de chat
 * Incluye funcionalidades para formato de texto, scroll automático, modo oscuro y envío de mensajes
 */
document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const chatContainer = document.getElementById('chatContainer');
    const form = document.getElementById('questionForm');
    const typingIndicator = document.getElementById('typingIndicator');
    const themeSwitch = document.getElementById('themeSwitch');
    const styleSwitch = document.getElementById('styleSwitch');
    const textarea = document.getElementById('question');
    const icon = themeSwitch.querySelector('i');
    const dbModal = document.getElementById('dbModal');
    
    /**
     * Función para formatear texto con Markdown básico
     * Convierte los asteriscos dobles en texto en negrita
     */
    function formatText() {
        const formattedElements = document.querySelectorAll('.formatted-text');
        formattedElements.forEach(element => {
            const originalText = element.getAttribute('data-text');
            if (originalText) {
                // Reemplazar **texto** con <strong>texto</strong>
                const formattedText = originalText.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
                element.innerHTML = formattedText;
            }
        });
    }
    
    // Aplicar formato al cargar la página
    formatText();
    
    /**
     * Función para desplazarse al final del contenedor de chat
     * Utiliza scrollTop para un desplazamiento instantáneo
     */
    function scrollToBottom() {
        // Uso directo de scrollTop para un scroll instantáneo
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }
    
    // Ejecutar la función de scroll al cargar la página
    scrollToBottom();
    
    // Asegurar que el scroll también se ejecute cuando las imágenes y otros recursos se carguen
    window.addEventListener('load', function() {
        setTimeout(scrollToBottom, 100);
        formatText(); // Asegurarse de que el formato se aplique después de cargar todo
    });
    
    /**
     * Alternador de tema visual (cyberpunk/profesional)
     * Cambia entre los estilos visuales y guarda la preferencia
     */
    styleSwitch.addEventListener('click', function() {
        document.body.classList.toggle('professional-theme');
        
        if (document.body.classList.contains('professional-theme')) {
            styleSwitch.querySelector('i').classList.remove('fa-palette');
            styleSwitch.querySelector('i').classList.add('fa-pen-fancy');
            localStorage.setItem('theme', 'professional');
        } else {
            styleSwitch.querySelector('i').classList.remove('fa-pen-fancy');
            styleSwitch.querySelector('i').classList.add('fa-palette');
            localStorage.setItem('theme', 'cyberpunk');
        }
    });
    
    // Verificar si hay una preferencia guardada de tema
    if (localStorage.getItem('theme') === 'professional') {
        document.body.classList.add('professional-theme');
        styleSwitch.querySelector('i').classList.remove('fa-palette');
        styleSwitch.querySelector('i').classList.add('fa-pen-fancy');
    }
    
    /**
     * Funcionalidad para expandir celdas de la tabla en el modal de base de datos
     * Permite ver el contenido completo al hacer clic en una celda
     */
    if (dbModal) {
        dbModal.addEventListener('shown.bs.modal', function() {
            const tableCells = dbModal.querySelectorAll('td');
            tableCells.forEach(cell => {
                cell.addEventListener('click', function() {
                    this.classList.toggle('expanded');
                    if (this.classList.contains('expanded')) {
                        this.style.whiteSpace = 'normal';
                        this.style.maxWidth = 'none';
                    } else {
                        this.style.whiteSpace = 'nowrap';
                        this.style.maxWidth = '300px';
                    }
                });
            });
        });
    }
    
    /**
     * Manejo del envío del formulario con indicador de carga
     * Crea mensajes temporales mientras se procesa la respuesta
     */
    form.addEventListener('submit', function(e) {
        const questionInput = document.getElementById('question');
        if (questionInput.value.trim() !== '') {
            // Mostrar indicador de escritura cuando se envía el formulario
            typingIndicator.style.display = 'block';
            
            // Crear un mensaje temporal del usuario hasta que la página se recargue
            const tempUserMessage = document.createElement('div');
            tempUserMessage.className = 'question new-message';
            tempUserMessage.innerHTML = `
                <div class="message-header">
                    <div class="message-avatar user-avatar text-white">
                        <i class="fas fa-user"></i>
                    </div>
                    <div class="message-content">
                        <strong>Tú</strong>
                    </div>
                </div>
                <p>${questionInput.value}</p>
                <div class="timestamp">${new Date().toLocaleString()}</div>
            `;
            chatContainer.appendChild(tempUserMessage);
            
            // Crear el espacio para la respuesta de Gemini
            const tempGeminiMessage = document.createElement('div');
            tempGeminiMessage.className = 'answer new-message';
            tempGeminiMessage.innerHTML = `
                <div class="message-header">
                    <div class="message-avatar ai-avatar text-white">
                        <i class="fas fa-robot"></i>
                    </div>
                    <div class="message-content">
                        <strong>Gemini</strong>
                    </div>
                </div>
                <p style="white-space: pre-line;">Procesando respuesta...</p>
            `;
            chatContainer.appendChild(tempGeminiMessage);
            
            // Incrementar contador de mensajes en la insignia
            const countBadge = document.getElementById('conversationCount');
            if (countBadge) {
                const currentCount = parseInt(countBadge.textContent);
                if (!isNaN(currentCount)) {
                    countBadge.textContent = (currentCount + 1) + ' mensajes';
                }
            }
            
            // Desplazarse hasta el final
            scrollToBottom();
        }
    });
    
    /**
     * Alternar modo oscuro
     * Cambia las variables CSS y guarda la preferencia en localStorage
     */
    themeSwitch.addEventListener('click', function() {
        document.body.classList.toggle('dark-mode');
        
        if (document.body.classList.contains('dark-mode')) {
            document.documentElement.style.setProperty('--light-bg', '#1a1a1a');
            document.documentElement.style.setProperty('--text-color', '#f0f0f0');
            icon.classList.remove('fa-moon');
            icon.classList.add('fa-sun');
            localStorage.setItem('darkMode', 'enabled');
        } else {
            document.documentElement.style.setProperty('--light-bg', '#f8f9fa');
            document.documentElement.style.setProperty('--text-color', '#333');
            icon.classList.remove('fa-sun');
            icon.classList.add('fa-moon');
            localStorage.setItem('darkMode', 'disabled');
        }
    });
    
    // Verificar si hay una preferencia guardada de modo oscuro
    if (localStorage.getItem('darkMode') === 'enabled') {
        document.body.classList.add('dark-mode');
        document.documentElement.style.setProperty('--light-bg', '#1a1a1a');
        document.documentElement.style.setProperty('--text-color', '#f0f0f0');
        icon.classList.remove('fa-moon');
        icon.classList.add('fa-sun');
    }
    
    /**
     * Auto-ajuste de altura del textarea
     * Ajusta dinámicamente la altura según el contenido
     */
    textarea.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
    
    /**
     * Soporte para Ctrl+Enter para enviar el texto
     * Permite enviar el mensaje sin hacer clic en el botón
     */
    textarea.addEventListener('keydown', function(e) {
        if (e.ctrlKey && e.key === 'Enter') {
            form.dispatchEvent(new Event('submit', { cancelable: true }));
            form.submit();
        }
    });
    
    // Añadir tooltips a los botones
    const sendButton = document.querySelector('.btn-primary');
    if (sendButton) {
        sendButton.setAttribute('title', 'Enviar mensaje (Ctrl+Enter)');
    }
}); 