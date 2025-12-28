package com.heidari.document_service.controller;

import com.heidari.document_service.dto.CreateDocumentRequest;
import com.heidari.document_service.model.Document;
import com.heidari.document_service.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    // Constructor Injection (Best Practice for required dependencies)
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Long> createDocument(@RequestBody CreateDocumentRequest request) {
        Long documentId = documentService.createDocument(request);
        return ResponseEntity.ok(documentId);
    }

    /**
     * Advanced Search Endpoint.
     * Usage: GET /documents/search?query=java&mode=title
     */
    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(
            @RequestParam String query,
            @RequestParam(defaultValue = "all") String mode) {

        return ResponseEntity.ok(documentService.searchDocuments(query, mode));
    }
}