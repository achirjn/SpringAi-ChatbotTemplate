package com.springAiTemplate.Chatbot.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;





@RestController
public class HomeController {
    // private final DocumentIngestor ingestor;

    // public HomeController(DocumentIngestor ingestor){
    //     this.ingestor = ingestor;
    // }

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @PostMapping("/ingest-text")
    // public void uploadPolicyText(@RequestParam("policyText") String policyText) {
    //     ingestor.ingest(List.of(policyText));
    // }
    
    // @PostMapping("ingest-file")
    // public void uploadPolicyDoc(@RequestParam("file") MultipartFile file) {
    //     try{
    //         Path tempFile = Files.createTempFile("upload-", ".txt");
    //         file.transferTo(tempFile.toFile());
    //         Resource resource = new FileSystemResource(tempFile);
    //         ingestor.ingestResources(List.of(resource));
    //     }
    //     catch(Exception e){
    //         e.printStackTrace();
    //     }
    // }
    
    
}
