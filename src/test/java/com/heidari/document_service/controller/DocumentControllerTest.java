package com.heidari.document_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heidari.document_service.dto.CreateDocumentRequest;
import com.heidari.document_service.model.Document;
import com.heidari.document_service.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for the Web Layer (Controller).
 * <p>
 * Educational Note:
 * We use @WebMvcTest(DocumentController.class) to create a "Slice Test".
 * Instead of loading the entire application context (Database, Service, etc.),
 * it only loads the beans required for the web layer (Controller, Advice, Filters).
 * This makes the test faster and focused solely on HTTP request/response logic.
 */
@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests without starting a real Tomcat server

    @MockitoBean// Replaces the real DocumentService bean in the Spring Context with a Mockito mock
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper; // Utility to serialize objects into JSON strings

    /**
     * Test Case: Verifies that a valid POST request creates a document.
     * Checks: HTTP Status 200, Service invocation, and Response Body.
     */
    @Test
    void createDocument_ShouldReturnStatusOk_AndId() throws Exception {
        // --- Arrange ---
        CreateDocumentRequest request = new CreateDocumentRequest();
        request.setTitle("New Doc");

        // Stubbing: Simulate the service returning ID 100 when called
        when(documentService.createDocument(any(CreateDocumentRequest.class))).thenReturn(100L);

        // --- Act & Assert ---
        // We perform a POST request and chain expectations (assertions)
        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convert object to JSON
                .andExpect(status().isOk())           // Verify HTTP 200 OK
                .andExpect(content().string("100"));  // Verify response body matches expected ID
    }

    /**
     * Test Case: Verifies the search endpoint and JSON structure.
     * usage of 'JsonPath' to inspect specific fields in the response.
     */
    @Test
    void searchDocuments_ShouldReturnList() throws Exception {
        // --- Arrange ---
        Document doc = new Document();
        doc.setTitle("Search Result");

        // Stubbing: Return a list containing one document for any string inputs
        when(documentService.searchDocuments(anyString(), anyString()))
                .thenReturn(List.of(doc));

        // --- Act & Assert ---
        mockMvc.perform(get("/documents/search")
                        .param("query", "Java")
                        .param("mode", "title"))
                .andExpect(status().isOk())
                // JsonPath syntax allows navigating the JSON structure (like XPath for XML)
                // $ represents the root array, [0] is the first element
                .andExpect(jsonPath("$[0].title").value("Search Result"));
    }
}