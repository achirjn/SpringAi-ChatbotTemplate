package com.springAiTemplate.Chatbot.Helper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DocumentIngestor {
    private final VectorStore vectorStore;

    public DocumentIngestor(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingest(List<String> texts) {
        List<Document> documents = texts.stream()
                .map(text -> new Document(text))
                .toList();
        TextSplitter splitter = new TokenTextSplitter(
                200,
                30,
                0,
                0,
                true
        );
        List<Document> chunks = splitter.split(documents);
        vectorStore.add(chunks);
    }

    public void ingestResources(List<Resource> resources) {
        List<Document> documents = new ArrayList<>();
        for (Resource resource : resources) {
            try {
                String content = Files.readString(resource.getFile().toPath());
                Document document = new Document(
                        content,
                        Map.of("source", resource.getFilename())
                );
                documents.add(document);
            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to read resource: " + resource.getFilename(), e
                );
            }
        }
        TextSplitter splitter = new TokenTextSplitter(
                200,
                30,
                0,
                0,
                true
        );
        List<Document> chunks = splitter.split(documents);
        vectorStore.add(chunks);
    }
}
