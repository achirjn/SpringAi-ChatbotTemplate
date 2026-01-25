package com.springAiTemplate.Chatbot.ServiceImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.springAiTemplate.Chatbot.Services.ChatService;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService{

    private final ChatClient chatClient;

    public ChatServiceImpl(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient){
        this.chatClient = ollamaChatClient;
    }
    @Override
    public String getChatResponse(String query) {
        return this.chatClient.prompt(query).call().content();
    }
    @Override
    public Flux<String> getStreamResponse(String query) {
        return this.chatClient.prompt(query).stream().content();
    }

}
