package com.heidari.document_service.repository;

import com.heidari.document_service.model.Document;
import com.heidari.document_service.model.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // This annotation loads an embedded DB (H2) and configures JPA strictly for testing
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void searchByMode_ShouldFilterByTitle() {
        // Arrange: Insert real data into the in-memory DB
        createDoc("Java Basics", "Content 1", "Coding");
        createDoc("Python Intro", "Content 2", "Scripting");

        // Act: Run the specification
        Specification<Document> spec = DocumentSpecification.searchByMode("Java", "title");
        List<Document> results = documentRepository.findAll(spec);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Java Basics");
    }

    @Test
    void searchByMode_ShouldFilterByTag() {
        // Arrange
        createDoc("Doc A", "Content A", "Spring");
        createDoc("Doc B", "Content B", "Hibernate");

        // Act
        Specification<Document> spec = DocumentSpecification.searchByMode("Spring", "tag");
        List<Document> results = documentRepository.findAll(spec);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTags().get(0).getName()).isEqualTo("Spring");
    }

    // Helper method to create data easily
    private void createDoc(String title, String content, String tagName) {
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);

        Document doc = new Document();
        doc.setTitle(title);
        doc.setContent(content);
        doc.setTags(List.of(tag));
        documentRepository.save(doc);
    }
}