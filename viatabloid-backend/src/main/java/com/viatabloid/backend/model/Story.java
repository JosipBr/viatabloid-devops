package com.viatabloid.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode; // <-- ADD THIS IMPORT
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Add this annotation and configure it to ONLY include the 'id' field
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include // <-- Mark 'id' to be included in equals/hashCode
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String department;
}