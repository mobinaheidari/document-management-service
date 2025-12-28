package com.heidari.document_service.service;

import com.heidari.document_service.dto.CreateDocumentRequest;
import com.heidari.document_service.model.Document;
import com.heidari.document_service.model.Tag;
import com.heidari.document_service.repository.DocumentRepository;
import com.heidari.document_service.repository.DocumentSpecification;
import com.heidari.document_service.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TagRepository tagRepository;

    /*
     * Educational Note:
     * As of Spring 4.3+, classes with a single constructor use "Implicit Constructor Injection".
     * The @Autowired annotation is optional. Using 'final' fields promotes immutability and testability.
     */
    public DocumentService(DocumentRepository documentRepository, TagRepository tagRepository) {
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Creates a new document and handles tag associations (find existing or create new).
     * Educational Note on @Transactional:
     * This annotation guarantees Atomicity. If the document save fails, any changes made to Tags
     * within this method will be rolled back, preventing orphaned data or inconsistencies.
     *
     * @param request The DTO containing document details.
     * @return The ID of the persisted document.
     */
    @Transactional
    public Long createDocument(CreateDocumentRequest request) {

        List<Tag> finalTags = new ArrayList<>();

        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {

                // Logic: Retrieve existing tag by name OR create a new one if it doesn't exist.
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));

                // Prevent duplicates if the input list contains the same tag twice (e.g. ["Java", "Java"])
                if (!finalTags.contains(tag)) {
                    finalTags.add(tag);
                }
            }
        }

        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setTags(finalTags);

        Document savedDoc = documentRepository.save(document);

        return savedDoc.getId();
    }

    /**
     * Performs advanced search based on query and mode.
     * @param query The text to search for.
     * @param mode The search scope (title, content, tag, all).
     * @return List of matching documents.
     */
    public List<Document> searchDocuments(String query, String mode) {
        Specification<Document> spec = DocumentSpecification.searchByMode(query, mode);
        return documentRepository.findAll(spec);
    }
}