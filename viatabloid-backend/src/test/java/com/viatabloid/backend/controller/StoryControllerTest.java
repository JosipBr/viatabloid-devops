package com.viatabloid.backend.controller;

import com.viatabloid.backend.model.Story;
import com.viatabloid.backend.repository.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("StoryController Unit Tests")
class StoryControllerTest {

    @Mock // Creates a mock instance of StoryRepository
    private StoryRepository storyRepository;

    @InjectMocks // Injects the mocked StoryRepository into StoryController
    private StoryController storyController;

    private Story story1;
    private Story story2;
    private UUID story1Id;
    private UUID story2Id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks before each test

        story1Id = UUID.randomUUID();
        story1 = new Story(story1Id, "Title One", "Content One", "Department A");

        story2Id = UUID.randomUUID();
        story2 = new Story(story2Id, "Title Two", "Content Two", "Department B");
    }

    @Test
    @DisplayName("getAllStories: Should return a list of all stories")
    void getAllStories_ShouldReturnListOfStories() {
        // Arrange
        List<Story> allStories = Arrays.asList(story1, story2);
        when(storyRepository.findAll()).thenReturn(allStories);

        // Act
        List<Story> result = storyController.getAllStories();

        // Assert
        assertNotNull(result, "Result list should not be null");
        assertEquals(2, result.size(), "Result list should contain 2 stories");
        assertTrue(result.contains(story1), "Result should contain story1");
        assertTrue(result.contains(story2), "Result should contain story2");
        verify(storyRepository, times(1)).findAll(); // Verify findAll was called once
    }

    @Test
    @DisplayName("getStoryById: Should return OK with story when found")
    void getStoryById_ShouldReturnOkWithStory_WhenFound() {
        // Arrange
        when(storyRepository.findById(story1Id)).thenReturn(Optional.of(story1));

        // Act
        ResponseEntity<Story> response = storyController.getStoryById(story1Id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(story1, response.getBody(), "Returned story should match the expected story");
        verify(storyRepository, times(1)).findById(story1Id); // Verify findById was called once
    }

    @Test
    @DisplayName("getStoryById: Should return NOT_FOUND when story not found")
    void getStoryById_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(storyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Story> response = storyController.getStoryById(nonExistentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(storyRepository, times(1)).findById(nonExistentId); // Verify findById was called once
    }

    @Test
    @DisplayName("createStory: Should return CREATED with the saved story")
    void createStory_ShouldReturnCreatedWithSavedStory() {
        // Arrange
        Story newStory = new Story(null, "New Title", "New Content", "New Dept");
        Story savedStory = new Story(UUID.randomUUID(), "New Title", "New Content", "New Dept"); // Simulates saved story with generated ID

        when(storyRepository.save(any(Story.class))).thenReturn(savedStory);

        // Act
        ResponseEntity<Story> response = storyController.createStory(newStory);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status should be CREATED");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(savedStory.getId(), response.getBody().getId(), "Saved story ID should be the generated ID");
        assertEquals(savedStory.getTitle(), response.getBody().getTitle(), "Saved story title should match");
        verify(storyRepository, times(1)).save(newStory); // Verify save was called once
    }

    @Test
    @DisplayName("updateStory: Should return OK with updated story when found")
    void updateStory_ShouldReturnOkWithUpdatedStory_WhenFound() {
        // Arrange
        Story updatedDetails = new Story(null, "Updated Title", "Updated Content", "Updated Dept"); // ID is ignored

        when(storyRepository.findById(story1Id)).thenReturn(Optional.of(story1));
        when(storyRepository.save(any(Story.class))).thenReturn(story1); // story1 will have updated fields

        // Act
        ResponseEntity<Story> response = storyController.updateStory(story1Id, updatedDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(story1Id, response.getBody().getId(), "Updated story ID should match");
        assertEquals(updatedDetails.getTitle(), response.getBody().getTitle(), "Title should be updated");
        assertEquals(updatedDetails.getContent(), response.getBody().getContent(), "Content should be updated");
        assertEquals(updatedDetails.getDepartment(), response.getBody().getDepartment(), "Department should be updated");

        verify(storyRepository, times(1)).findById(story1Id);
        verify(storyRepository, times(1)).save(story1); // Verifies save was called with the modified existing story
    }

    @Test
    @DisplayName("updateStory: Should return NOT_FOUND when story to update is not found")
    void updateStory_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        Story updatedDetails = new Story(null, "Title", "Content", "Dept");

        when(storyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Story> response = storyController.updateStory(nonExistentId, updatedDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be NOT_FOUND");
        assertNull(response.getBody(), "Response body should be null");
        verify(storyRepository, times(1)).findById(nonExistentId);
        verify(storyRepository, never()).save(any(Story.class)); // Save should not be called
    }

    @Test
    @DisplayName("deleteStory: Should return NO_CONTENT when story is deleted successfully")
    void deleteStory_ShouldReturnNoContent_WhenDeletedSuccessfully() {
        // Arrange
        doNothing().when(storyRepository).deleteById(story1Id); // Configure mock to do nothing on delete

        // Act
        ResponseEntity<HttpStatus> response = storyController.deleteStory(story1Id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status should be NO_CONTENT");
        verify(storyRepository, times(1)).deleteById(story1Id); // Verify deleteById was called once
    }

    @Test
    @DisplayName("deleteStory: Should return INTERNAL_SERVER_ERROR when delete fails")
    void deleteStory_ShouldReturnInternalServerError_WhenDeleteFails() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(storyRepository).deleteById(story1Id); // Simulate DB error

        // Act
        ResponseEntity<HttpStatus> response = storyController.deleteStory(story1Id);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Status should be INTERNAL_SERVER_ERROR");
        verify(storyRepository, times(1)).deleteById(story1Id); // Verify deleteById was called once
    }
}