package com.springAiTemplate.Chatbot.Helper;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.stereotype.Component;

@Component
public class CustomChatMemory {
    private static final int MAX_TURNS = 5; // 3 user + 3 assistant
    private final Deque<String> history = new ArrayDeque<>();

    public synchronized void addUser(String msg) {
        history.addLast("User: " + msg);
        trim();
    }

    public synchronized void addAssistant(String msg) {
        history.addLast("Assistant: " + msg);
        trim();
    }

    public synchronized String getHistory() {
        return String.join("\n", history);
    }

    private void trim() {
        while (history.size() > MAX_TURNS * 2) {
            history.pollFirst();
        }
    }
}
