package com.springAiTemplate.Chatbot.Configurations;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatClient getOllamaChatClient(OllamaChatModel chatModel){
        // MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        ChatClient chatClient = ChatClient.builder(chatModel)
            // .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .defaultOptions(ChatOptions.builder()
                        .maxTokens(200)
                        .temperature(0.0)
                        .build())
            .build();
        return chatClient;
    }
}
