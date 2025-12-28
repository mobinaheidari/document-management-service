package com.heidari.document_service.service;

import com.heidari.document_service.dto.CreateDocumentRequest;
import com.heidari.document_service.model.Document;
import com.heidari.document_service.model.Tag;
import com.heidari.document_service.repository.DocumentRepository;
import com.heidari.document_service.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for DocumentService.
 * <p>
 * Educational Note:
 * We use @ExtendWith(MockitoExtension.class) to enable Mockito support in JUnit 5.
 * This allows us to use @Mock to create dummy dependencies and @InjectMocks to inject them
 * into our service, ensuring we test the service logic in complete isolation from the database.
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock // Creates a mock implementation of the repository (no database connection)
    private DocumentRepository documentRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks // Creates an instance of DocumentService and injects the mocks above into it
    private DocumentService documentService;

    private CreateDocumentRequest request;

    @BeforeEach
    void setUp() {
        // Initialize test data before each test execution to ensure a clean state
        request = new CreateDocumentRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setTags(Arrays.asList("Java", "Spring"));
    }

    /**
     * Test Case: Verifies that a document is correctly saved and the ID is returned.
     * Focus: Stubbing repository responses and verifying interactions.
     */
    @Test
    void createDocument_ShouldSaveDocumentAndReturnId() {
        // --- Arrange (Prepare objects and define mock behavior) ---
        Document savedDoc = new Document();
        savedDoc.setId(1L);

        // Stubbing: When 'save' is called on the document repo, return the document with ID 1
        when(documentRepository.save(any(Document.class))).thenReturn(savedDoc);

        // Stubbing: Simulate that the tag does not exist in the DB (Optional.empty)
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Stubbing: When saving a new tag, return the same tag object
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArguments()[0]);

        // --- Act (Execute the method under test) ---
        Long resultId = documentService.createDocument(request);

        // --- Assert (Verify the results) ---
        assertEquals(1L, resultId, "The returned ID should match the mocked saved document ID");

        // Verification: Ensure that the repository's save method was called exactly once
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    /**
     * Test Case: Verifies that the search method delegates to the repository correctly.
     * Focus: Testing data flow and return values.
     */
    @Test
    void searchDocuments_ShouldCallFindAll() {
        // --- Arrange ---
        Document doc = new Document();
        doc.setTitle("Java");

        // Stubbing: Return a list containing one document when findAll is called
        when(documentRepository.findAll(any(Specification.class))).thenReturn(List.of(doc));

        // --- Act ---
        List<Document> result = documentService.searchDocuments("Java", "title");

        // --- Assert ---
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }
}