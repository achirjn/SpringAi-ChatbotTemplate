package com.springAiTemplate.Chatbot.Helper;

import java.io.IOException;
import java.io.InputStream;
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

    // public void ingest(List<String> texts) {
    //     List<Document> documents = texts.stream()
    //             .map(text -> new Document(text))
    //             .toList();
    //     TextSplitter splitter;
    //     splitter = new TokenTextSplitter(
    //             200,
    //             30,
    //             0,
    //             0,
    //             true
    //     );
    //     List<Document> chunks = splitter.split(documents);
    //     vectorStore.add(chunks);
    // }

    public void ingestResources(List<Resource> resources) {
        List<Document> documents = new ArrayList<>();

    for (Resource resource : resources) {
        try (InputStream is = resource.getInputStream()) {

            String content = new String(is.readAllBytes());

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
