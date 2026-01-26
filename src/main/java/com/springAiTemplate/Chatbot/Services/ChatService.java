package com.springAiTemplate.Chatbot.Services;

import reactor.core.publisher.Flux;

public interface ChatService {
    public String getChatResponse(String query);
    public Flux<String> getStreamResponse(String query);
    public Flux<String> getRagChatbotResponse(String query, String conversationId);
}
