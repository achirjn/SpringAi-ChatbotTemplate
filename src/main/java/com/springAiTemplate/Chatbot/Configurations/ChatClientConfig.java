package com.springAiTemplate.Chatbot.Configurations;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatClient ollamaChatClient(ChatClient.Builder builder){
        return builder.defaultSystem(s -> s.text("You are a RAG chatbot for the company Investocks. Answer according to the rules ans regulations mentioned in the documents."))
                .build();
    }
}
