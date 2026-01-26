package com.springAiTemplate.Chatbot.ServiceImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.springAiTemplate.Chatbot.Services.ChatService;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService{

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public ChatServiceImpl(@Qualifier("getOllamaChatClient") ChatClient ollamaChatClient, VectorStore vectorStore){
        this.chatClient = ollamaChatClient;
        this.vectorStore = vectorStore;
    }
    @Override
    public String getChatResponse(String query) {
        return this.chatClient.prompt(query).call().content();
    }
    @Override
    public Flux<String> getStreamResponse(String query) {
        return this.chatClient.prompt(query).stream().content();
    }
    @Override
    public Flux<String> getRagChatbotResponse(String query, String conversationId) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
        .documentRetriever(VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.50)
                .vectorStore(vectorStore)
                .build())
        .build();

        Flux<String> answer = chatClient.prompt()
                .advisors(retrievalAugmentationAdvisor)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .system("the response generated should not exceed 100 tokens.")
                .user(query)
                .stream()
                .content();

        return answer;    
    }
}
