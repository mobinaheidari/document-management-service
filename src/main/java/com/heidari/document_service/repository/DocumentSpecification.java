package com.heidari.document_service.repository;

import com.heidari.document_service.model.Document;
import com.heidari.document_service.model.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Defines dynamic query logic for searching documents.
 * <p>
 * Educational Note:
 * This class implements the "Specification Pattern" using the JPA Criteria API.
 * Instead of hardcoding SQL strings (which are error-prone and hard to modify),
 * we verify types at compile-time and build the "WHERE" clause dynamically based on user input.
 * <p>
 * Key Concepts:
 * 1. Root: Represents the entity we are querying (FROM Document).
 * 2. CriteriaBuilder: A factory to create logic conditions (LIKE, EQUAL, OR, AND).
 * 3. Predicate: Represents the actual condition (WHERE title LIKE ...).
 */
public class DocumentSpecification {

    /**
     * Generates a Specification based on the search query and the search mode.
     *
     * @param queryText The text to search for (e.g., "Java").
     * @param mode      The scope of search: "title", "content", "tag", or "all".
     * @return A Specification object acting as the WHERE clause.
     */
    public static Specification<Document> searchByMode(String queryText, String mode) {
        return (root, query, cb) -> {

            // Safety Check: If query is empty, return null (no filtering applied)
            if (!StringUtils.hasText(queryText)) {
                return null;
            }

            // Pattern for partial matching: "%text%" (Contains logic)
            String searchPattern = "%" + queryText.toLowerCase() + "%";
            Join<Document, Tag> tagJoin;

            // Default to "all" if mode is null
            String searchMode = (mode != null) ? mode.toLowerCase() : "all";

            switch (searchMode) {
                case "title":
                    // WHERE LOWER(title) LIKE '%...%'
                    return cb.like(cb.lower(root.get("title")), searchPattern);

                case "content":
                    // WHERE LOWER(content) LIKE '%...%'
                    return cb.like(cb.lower(root.get("content")), searchPattern);

                case "tag":
                    /*
                     * Logic: INNER JOIN
                     * We only want documents that definitely have this specific tag.
                     * If a document has no tags, it is excluded immediately.
                     */
                    tagJoin = root.join("tags", JoinType.INNER);
                    query.distinct(true); // Avoid duplicate results if a doc has multiple matching tags
                    return cb.like(cb.lower(tagJoin.get("name")), searchPattern);

                case "all":
                default:
                    /*
                     * Critical Logic: LEFT JOIN vs INNER JOIN
                     * We use LEFT JOIN here because we want to search in Title OR Content OR Tags.
                     * * Why?
                     * If we used INNER JOIN, a document with a matching Title but NO tags would be
                     * hidden/excluded from the results. LEFT JOIN allows looking at tags
                     * while still keeping documents that have no tags.
                     */
                    tagJoin = root.join("tags", JoinType.LEFT);

                    // Create 3 independent conditions
                    Predicate titleMatch = cb.like(cb.lower(root.get("title")), searchPattern);
                    Predicate contentMatch = cb.like(cb.lower(root.get("content")), searchPattern);
                    Predicate tagMatch = cb.like(cb.lower(tagJoin.get("name")), searchPattern);

                    // Apply DISTINCT to prevent "Cartesian Product" duplicates in the result list
                    query.distinct(true);

                    // Combine with logical OR: (title LIKE ...) OR (content LIKE ...) OR (tag LIKE ...)
                    return cb.or(titleMatch, contentMatch, tagMatch);
            }
        };
    }
}