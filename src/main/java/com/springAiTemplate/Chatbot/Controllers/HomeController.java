package com.springAiTemplate.Chatbot.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springAiTemplate.Chatbot.Helper.DocumentIngestor;





@RestController
public class HomeController {
    private final DocumentIngestor ingestor;

    public HomeController(DocumentIngestor ingestor){
        this.ingestor = ingestor;
    }

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ingest-text")
    public void uploadPolicyText(@RequestParam("policyText") String policyText) {
        ingestor.ingest(List.of(policyText));
    }
    
    @PostMapping("ingest-file")
    public void uploadPolicyDoc(@RequestParam("file") MultipartFile file) {
        try{
            Path tempFile = Files.createTempFile("upload-", ".txt");
            file.transferTo(tempFile.toFile());
            Resource resource = new FileSystemResource(tempFile);
            ingestor.ingestResources(List.of(resource));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
