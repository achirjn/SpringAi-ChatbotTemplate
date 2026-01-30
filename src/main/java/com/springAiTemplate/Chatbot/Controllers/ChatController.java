package com.springAiTemplate.Chatbot.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springAiTemplate.Chatbot.Services.ChatService;

import reactor.core.publisher.Flux;




@RestController
public class ChatController{

    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }
    
    @GetMapping("/chat/{q}")
    public ResponseEntity<?> chatMethod(@PathVariable("q") String query){
        return new ResponseEntity<>(this.chatService.getChatResponse(query), HttpStatus.OK);
    }

    @GetMapping("/stream-chat/{q}")
    public ResponseEntity<Flux<String>> streamMethod(@PathVariable("q") String query){
        return new ResponseEntity<>(this.chatService.getStreamResponse(query), HttpStatus.OK);
    }

    // @GetMapping("/rag-chat/{q}/{cid}")
    // public ResponseEntity<Flux<String>> ragChatbotMethod(@PathVariable("q") String query, @PathVariable("cid") String conversationId) {
    //     return new ResponseEntity<>(this.chatService.getRagChatbotResponse(query, conversationId), HttpStatus.OK);
    // }
    
}
