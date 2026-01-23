package com.springAiTemplate.Chatbot.Configurations;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatClient ollamaChatClient(OllamaChatModel chatModel){
        return ChatClient.create(chatModel);
    }
}
