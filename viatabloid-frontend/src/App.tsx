import React, { useState, useEffect } from "react";
import "./App.css";
import axios from "axios"; // Import axios

interface Story {
  id: string; // Will be UUID from backend, represented as string
  title: string;
  content: string;
  department: string;
}

const API_BASE_URL = "http://localhost:8080/api/stories"; // Your Spring Boot backend URL

function App() {
  const [stories, setStories] = useState<Story[]>([]);
  const [newStoryTitle, setNewStoryTitle] = useState<string>("");
  const [newStoryContent, setNewStoryContent] = useState<string>("");
  const [newStoryDepartment, setNewStoryDepartment] =
    useState<string>("Engineering");
  const [editingStory, setEditingStory] = useState<Story | null>(null); // State for story being edited

  // Fetch stories on component mount
  useEffect(() => {
    fetchStories();
  }, []);

  const fetchStories = async () => {
    try {
      const response = await axios.get<Story[]>(API_BASE_URL);
      setStories(response.data);
    } catch (error) {
      console.error("Error fetching stories:", error);
    }
  };

  const addStory = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newStoryTitle.trim() === "" || newStoryContent.trim() === "") {
      alert("Please fill in both title and content for the story.");
      return;
    }

    const newStoryData = {
      title: newStoryTitle,
      content: newStoryContent,
      department: newStoryDepartment,
    };

    try {
      await axios.post(API_BASE_URL, newStoryData);
      setNewStoryTitle("");
      setNewStoryContent("");
      setNewStoryDepartment("Engineering");
      fetchStories(); // Refresh the list of stories
    } catch (error) {
      console.error("Error adding story:", error);
      alert("Failed to add story.");
    }
  };

  const deleteStory = async (id: string) => {
    if (window.confirm("Are you sure you want to delete this story?")) {
      try {
        await axios.delete(`${API_BASE_URL}/${id}`);
        fetchStories(); // Refresh the list of stories
      } catch (error) {
        console.error("Error deleting story:", error);
        alert("Failed to delete story.");
      }
    }
  };

  const startEditingStory = (story: Story) => {
    setEditingStory(story);
    setNewStoryTitle(story.title); // Populate form for editing
    setNewStoryContent(story.content);
    setNewStoryDepartment(story.department);
  };

  const cancelEditing = () => {
    setEditingStory(null);
    setNewStoryTitle("");
    setNewStoryContent("");
    setNewStoryDepartment("Engineering");
  };

  const updateStory = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingStory) return;

    const updatedStoryData = {
      id: editingStory.id, // ID must be included for PUT request
      title: newStoryTitle,
      content: newStoryContent,
      department: newStoryDepartment,
    };

    try {
      await axios.put(`${API_BASE_URL}/${editingStory.id}`, updatedStoryData);
      cancelEditing(); // Clear editing state
      fetchStories(); // Refresh the list of stories
    } catch (error) {
      console.error("Error updating story:", error);
      alert("Failed to update story.");
    }
  };

  // Group stories by department for display
  const storiesByDepartment: { [key: string]: Story[] } = stories.reduce(
    (acc, story) => {
      if (!acc[story.department]) {
        acc[story.department] = [];
      }
      acc[story.department].push(story);
      return acc;
    },
    {} as { [key: string]: Story[] }
  );

  return (
    <div className="App">
      <h1>VIA Tabloid</h1>

      <div className="story-form-container">
        <h2>{editingStory ? "Edit Story" : "Add New Story"}</h2>
        <form onSubmit={editingStory ? updateStory : addStory}>
          <div>
            <label htmlFor="storyTitle">Title:</label>
            <input
              type="text"
              id="storyTitle"
              value={newStoryTitle}
              onChange={(e) => setNewStoryTitle(e.target.value)}
              required
            />
          </div>
          <div>
            <label htmlFor="storyContent">Content:</label>
            <textarea
              id="storyContent"
              value={newStoryContent}
              onChange={(e) => setNewStoryContent(e.target.value)}
              rows={4}
              required
            ></textarea>
          </div>
          <div>
            <label htmlFor="storyDepartment">Department:</label>
            <select
              id="storyDepartment"
              value={newStoryDepartment}
              onChange={(e) => setNewStoryDepartment(e.target.value)}
            >
              <option value="Engineering">Engineering</option>
              <option value="Design">Design</option>
              <option value="Business">Business</option>
              {/* Add more departments as needed */}
            </select>
          </div>
          <button type="submit">
            {editingStory ? "Update Story" : "Add Story"}
          </button>
          {editingStory && (
            <button type="button" onClick={cancelEditing}>
              Cancel Edit
            </button>
          )}
        </form>
      </div>

      <div className="departments-container">
        {Object.keys(storiesByDepartment).map((department) => (
          <div key={department} className="department-section">
            <h2>{department} Department Stories</h2>
            <div className="story-list">
              {storiesByDepartment[department].length > 0 ? (
                storiesByDepartment[department].map((story) => (
                  <div key={story.id} className="story-card">
                    <h3>{story.title}</h3>
                    <p>{story.content}</p>
                    <p className="story-department">
                      Department: {story.department}
                    </p>
                    <div className="story-actions">
                      <button onClick={() => startEditingStory(story)}>
                        Update
                      </button>
                      <button onClick={() => deleteStory(story.id)}>
                        Delete
                      </button>
                    </div>
                  </div>
                ))
              ) : (
                <p>No stories yet for {department} department.</p>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default App;
