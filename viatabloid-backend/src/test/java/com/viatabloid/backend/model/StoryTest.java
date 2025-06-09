package com.viatabloid.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Story Unit Tests")
class StoryTest {

    private UUID testId;
    private Story testStory;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        // Using AllArgsConstructor
        testStory = new Story(testId, "Test Title", "Test Content", "Test Department");
    }

    @Test
    @DisplayName("Constructor and Getters: Should correctly initialize and retrieve story properties")
    void constructorAndGetters_ShouldInitializeAndRetrieveProperties() {
        // Arrange & Act (done in @BeforeEach for testStory)
        // Assert
        assertEquals(testId, testStory.getId(), "ID should match the initialized ID");
        assertEquals("Test Title", testStory.getTitle(), "Title should match the initialized title");
        assertEquals("Test Content", testStory.getContent(), "Content should match the initialized content");
        assertEquals("Test Department", testStory.getDepartment(), "Department should match the initialized department");
    }

    @Test
    @DisplayName("NoArgsConstructor: Should create a Story with null fields")
    void noArgsConstructor_ShouldCreateStoryWithNullFields() {
        // Act
        Story emptyStory = new Story();

        // Assert
        assertNull(emptyStory.getId(), "ID should be null for no-arg constructor");
        assertNull(emptyStory.getTitle(), "Title should be null for no-arg constructor");
        assertNull(emptyStory.getContent(), "Content should be null for no-arg constructor");
        assertNull(emptyStory.getDepartment(), "Department should be null for no-arg constructor");
    }


    @Test
    @DisplayName("Setters: Should update story properties correctly")
    void setters_ShouldUpdateProperties() {
        // Arrange
        String newTitle = "Updated Title";
        String newContent = "Updated Content";
        String newDepartment = "New Department";

        // Act
        testStory.setTitle(newTitle);
        testStory.setContent(newContent);
        testStory.setDepartment(newDepartment);

        // Assert
        assertEquals(newTitle, testStory.getTitle(), "Title should be updated by setter");
        assertEquals(newContent, testStory.getContent(), "Content should be updated by setter");
        assertEquals(newDepartment, testStory.getDepartment(), "Department should be updated by setter");
    }

    @Test
    @DisplayName("Equals: Should return true for stories with the same ID")
    void equals_ShouldReturnTrue_ForSameId() {
        // Arrange
        Story anotherStoryWithSameId = new Story(testId, "Another Title", "Another Content", "Another Dept");

        // Act & Assert
        assertTrue(testStory.equals(anotherStoryWithSameId), "Stories with the same ID should be equal");
        assertTrue(testStory.equals(testStory), "Story should be equal to itself");
    }

    @Test
    @DisplayName("Equals: Should return false for stories with different IDs")
    void equals_ShouldReturnFalse_ForDifferentIds() {
        // Arrange
        Story differentStory = new Story(UUID.randomUUID(), "Different Title", "Different Content", "Different Dept");

        // Act & Assert
        assertFalse(testStory.equals(differentStory), "Stories with different IDs should not be equal");
        assertFalse(testStory.equals(null), "Story should not be equal to null");
        assertFalse(testStory.equals(new Object()), "Story should not be equal to a different object type");
    }

    @Test
    @DisplayName("HashCode: Should be consistent for equal objects")
    void hashCode_ShouldBeConsistent_ForEqualObjects() {
        // Arrange
        Story anotherStoryWithSameId = new Story(testId, "Another Title", "Another Content", "Another Dept");

        // Act & Assert
        assertEquals(testStory.hashCode(), anotherStoryWithSameId.hashCode(), "Hash codes should be equal for equal objects");
        assertEquals(testStory.hashCode(), testStory.hashCode(), "Hash code should be consistent with itself");
    }

    @Test
    @DisplayName("ToString: Should contain relevant story information")
    void toString_ShouldContainRelevantInformation() {
        // Act
        String storyString = testStory.toString();

        // Assert
        assertTrue(storyString.contains(testId.toString()), "ToString should contain story ID");
        assertTrue(storyString.contains("Test Title"), "ToString should contain title");
        assertTrue(storyString.contains("Test Content"), "ToString should contain content");
        assertTrue(storyString.contains("Test Department"), "ToString should contain department");
        assertTrue(storyString.startsWith("Story("), "ToString should start with class name and parenthesis"); // Lombok's toString
        assertTrue(storyString.endsWith(")"), "ToString should end with parenthesis"); // Lombok's toString
    }
}