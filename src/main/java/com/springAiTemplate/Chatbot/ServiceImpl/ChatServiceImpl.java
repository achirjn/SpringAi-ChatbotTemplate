package com.springAiTemplate.Chatbot.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.springAiTemplate.Chatbot.Helper.CustomChatMemory;
import com.springAiTemplate.Chatbot.Services.ChatService;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final CustomChatMemory customChatMemory;

    public ChatServiceImpl(@Qualifier("getOllamaChatClient") ChatClient ollamaChatClient, VectorStore vectorStore, CustomChatMemory customChatMemory) {
        this.chatClient = ollamaChatClient;
        this.vectorStore = vectorStore;
        this.customChatMemory = customChatMemory;
    }

    @Override
    public String getChatResponse(String query) {
        return this.chatClient.prompt(query).call().content();
    }

    @Override
    public Flux<String> getStreamResponse(String query) {
        return this.chatClient.prompt(query)
                .stream()
                .content();
    }

    // @Override
    // public Flux<String> getRagChatbotResponse(String query) {
    //     // Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
    //     // .documentRetriever(VectorStoreDocumentRetriever.builder()
    //     // .similarityThreshold(0.75)
    //     // .topK(1)
    //     // .vectorStore(vectorStore)
    //     // .build())
    //     // .build();
    //     long t0 = System.currentTimeMillis();
    //     System.out.println("RAG start");
    //     List<Document> docs;
    //     docs = vectorStore.similaritySearch(
    //             SearchRequest.builder()
    //                     .query(query)
    //                     .topK(2)
    //                     .similarityThreshold(0.6)
    //                     .build());
    //     long t1 = System.currentTimeMillis();
    //     System.out.println("Vector search took: " + (t1 - t0) + " ms");
    //     System.out.println("Docs retrieved: " + docs.size());
    //     if (docs.isEmpty()) {
    //         return Flux.just("I don't know.");
    //     }
    //     long t2 = System.currentTimeMillis();
    //     System.out.println("Starting LLM");
    //     PromptTemplate customPromptTemplate = PromptTemplate.builder()
    //             .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
    //             .template(
    //                     """
    //                                      User query:
    //                                      <query>

    //                                      Context information is below.

    //                             ---------------------
    //                             <question_answer_context>
    //                             ---------------------

    //                             You MUST follow these rules:

    //                             1. You are FORBIDDEN from using any knowledge outside the provided context.
    //                                      2. If the context does NOT contain the answer, you MUST reply with exactly: "I don't know."
    //                                      3. Do NOT guess.
    //                             4. Avoid statements like "Based on the context..." or "The provided information...".
    //                                      5. Frame your response using maximum 100 tokens.
    //                                      """)
    //             .build();
    //     var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
    //             .searchRequest(SearchRequest.builder()
    //                     .similarityThreshold(0.6)
    //                     .topK(2)
    //                     .build())
    //             .promptTemplate(customPromptTemplate)
    //             .build();

    //     Flux<String> answer = chatClient.prompt(query)
    //             .advisors(qaAdvisor)
    //             // .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
    //             .stream()
    //             .content()
    //             .doOnCancel(() -> {
    //                 System.out.println("Client disconnected, stopping stream...");
    //             })
    //             .onErrorResume(e -> {
    //                 System.out.println("Streaming error: " + e.getMessage());
    //                 return Flux.empty();
    //             })
    //             .doOnComplete(() -> {
    //                 long t3 = System.currentTimeMillis();
    //                 System.out.println("LLM stream done in: " + (t3 - t2) + " ms");
    //             });

    //     return answer;
    // }

    @Override
    public Flux<String> getRagChatbotResponse(String query) {

        long t0 = System.currentTimeMillis();
    System.out.println("RAG start");

    // 1. Vector search
    List<Document> docs = vectorStore.similaritySearch(
            SearchRequest.builder()
                    .query(query)
                    .topK(2)
                    .similarityThreshold(0.6)
                    .build()
    );

    long t1 = System.currentTimeMillis();
    System.out.println("Vector search took: " + (t1 - t0) + " ms");
    System.out.println("Docs retrieved: " + docs.size());

    // Optional hard short-circuit (recommended)
    if (docs.isEmpty()) {
        customChatMemory.addUser(query);
        customChatMemory.addAssistant("I don't know.");
        return Flux.just("I don't know.");
    }

    // 2. Build context
    String context = docs.stream()
            .map(Document::getText)
            .collect(Collectors.joining("\n\n"));

    String memory = customChatMemory.getHistory();

    long t2 = System.currentTimeMillis();
    System.out.println("Starting LLM");

    // Buffer to collect assistant response
    StringBuilder assistantBuffer = new StringBuilder();

    return chatClient.prompt()
            .system("""
                    You MUST answer using ONLY the provided context or the chat history.
                    If the answer is not present, reply exactly with: "I don't have the relevant information."
                    Do NOT use outside knowledge.
                    You MAY respond to simple greetings using chat history.
                    Maximum 100 tokens.
                    """)
            .user("""
                    Context:
                    ----------------
                    """ + context + """
                    ----------------

                    Chat History:
                    ----------------
                    """ + memory + """
                    ----------------

                    Question:
                    """ + query)
            .stream()
            .content()
            .doOnNext(token -> assistantBuffer.append(token))
            .doOnComplete(() -> {
                // ✅ WRITE TO MEMORY HERE (ONLY ON SUCCESS)
                customChatMemory.addUser(query);
                customChatMemory.addAssistant(assistantBuffer.toString());

                long t3 = System.currentTimeMillis();
                System.out.println("LLM stream done in: " + (t3 - t2) + " ms");
            })
            .onErrorResume(e -> {
                System.out.println("Streaming error: " + e.getMessage());
                return Flux.empty();
            });
        }
}
