package com.heidari.document_service.repository;


import com.heidari.document_service.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing Tag entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds a Tag by its name.
     * Used to check if a tag already exists before creating a new one.
     *
     * @param name The name of the tag to search for.
     * @return An Optional containing the Tag if found, or empty if not.
     */
    Optional<Tag> findByName(String name);
}
