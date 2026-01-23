package com.springAiTemplate.Chatbot.Controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    @Qualifier("ollamaChatClient")
    private ChatClient chatClient;
    
    @GetMapping("/chat/{q}")
    public ResponseEntity<?> chatMethod(@PathVariable("q") String query){
        String response =  this.chatClient.prompt(query).call().content();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
