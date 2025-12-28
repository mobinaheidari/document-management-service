package com.heidari.document_service.repository;


import com.heidari.document_service.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for Document entity.
 * * Educational Notes:
 * 1. By extending 'JpaRepository', we inherit standard CRUD operations (save, findById, delete, etc.)
 * without writing any implementation code. This replaces the legacy DAO pattern (e.g., DocumentDAOImpl).
 * * 2. By extending 'JpaSpecificationExecutor', we enable the Criteria API support.
 * This allows passing a 'Specification' object to 'findAll()' for dynamic, programmatic query generation,
 * which is cleaner and safer than string-based JPQL for complex filters.
 */
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    // No explicit methods needed. Spring Data JPA generates implementation proxies at runtime.
}