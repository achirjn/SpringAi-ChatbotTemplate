package com.springAiTemplate.Chatbot.Controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/chat")
public class ChatController{

    private final ChatClient chatClient;

    public ChatController(@Qualifier("ollamaChatClient") ChatClient ollamaChatClient){
        this.chatClient = ollamaChatClient;
    }
    
    @GetMapping("/{q}")
    public ResponseEntity<?> chatMethod(@PathVariable("q") String query){
        String response =  this.chatClient.prompt(query).call().content();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
