package com.viatabloid.backend.model;

import jakarta.persistence.*;
import lombok.Data; // From Lombok
import lombok.NoArgsConstructor; // From Lombok
import lombok.AllArgsConstructor; // From Lombok

import java.util.UUID;

@Entity // Marks this class as a JPA entity
@Table(name = "stories") // Maps this entity to the "stories" table
@Data // Generates getters, setters, toString, equals, hashCode (Lombok)
@NoArgsConstructor // Generates a no-argument constructor (Lombok)
@AllArgsConstructor // Generates a constructor with all fields (Lombok)
public class Story {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.AUTO) // Generates UUID automatically
    private UUID id;

    @Column(nullable = false) // Maps to a column, cannot be null
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT") // Maps to a TEXT column, cannot be null
    private String content;

    @Column(nullable = false) // Maps to a column, cannot be null
    private String department;
}