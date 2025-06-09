package com.viatabloid.backend.repository;

import com.viatabloid.backend.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository // Marks this interface as a Spring repository component
public interface StoryRepository extends JpaRepository<Story, UUID> {
    // JpaRepository provides basic CRUD operations for Story entity with UUID as ID type.
    // You can add custom query methods here if needed, e.g., findByDepartment(String department);
}