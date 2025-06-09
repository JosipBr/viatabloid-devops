// src/App.test.tsx
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, beforeEach } from "vitest";
import App from "./App";
import axios from "axios";
import { vi, Mock } from "vitest";

interface Story {
  id: string;
  title: string;
  content: string;
  department: string;
}

vi.mock("axios");

describe("App Component", () => {
  const mockStories: Story[] = [
    { id: "1", title: "Story A", content: "Content A", department: "News" },
    { id: "2", title: "Story B", content: "Content B", department: "Sports" },
    { id: "3", title: "Story C", content: "Content C", department: "News" },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
    (axios.get as Mock).mockResolvedValue({ data: mockStories });
  });

  it("renders the main heading and the form fields", async () => {
    render(<App />);

    expect(
      screen.getByRole("heading", { name: /VIA Tabloid/i })
    ).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: /Add New Story/i })
    ).toBeInTheDocument();

    expect(screen.getByLabelText(/Title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Content/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Department/i)).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /Add Story/i })
    ).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText("Story A")).toBeInTheDocument();
    });
  });

  it("displays stories fetched from the API", async () => {
    render(<App />);

    await waitFor(() => {
      // For Story A (News)
      const storyACard = screen
        .getByText("Story A")
        .closest(".story-card") as HTMLElement; // <-- FIX HERE
      expect(storyACard).toBeInTheDocument();
      expect(within(storyACard).getByText("Content A")).toBeInTheDocument();
      expect(
        within(storyACard).getByText("Department: News")
      ).toBeInTheDocument();

      // For Story B (Sports)
      const storyBCard = screen
        .getByText("Story B")
        .closest(".story-card") as HTMLElement; // <-- FIX HERE
      expect(storyBCard).toBeInTheDocument();
      expect(within(storyBCard).getByText("Content B")).toBeInTheDocument();
      expect(
        within(storyBCard).getByText("Department: Sports")
      ).toBeInTheDocument();

      // For Story C (News)
      const storyCCard = screen
        .getByText("Story C")
        .closest(".story-card") as HTMLElement; // <-- FIX HERE
      expect(storyCCard).toBeInTheDocument();
      expect(within(storyCCard).getByText("Content C")).toBeInTheDocument();
      expect(
        within(storyCCard).getByText("Department: News")
      ).toBeInTheDocument();
    });
  });

  it("allows adding a new story and displays it", async () => {
    const user = userEvent.setup();
    const newStoryData = {
      title: "New Story",
      content: "New Content",
      department: "Engineering",
    };
    const postedStory = { id: "4", ...newStoryData };

    (axios.post as Mock).mockResolvedValue({ data: postedStory });
    (axios.get as Mock).mockResolvedValueOnce({ data: mockStories });
    (axios.get as Mock).mockResolvedValueOnce({
      data: [...mockStories, postedStory],
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText("Story A")).toBeInTheDocument();
    });

    await user.type(screen.getByLabelText(/Title/i), newStoryData.title);
    await user.type(screen.getByLabelText(/Content/i), newStoryData.content);
    await user.selectOptions(
      screen.getByLabelText(/Department/i),
      newStoryData.department
    );

    await user.click(screen.getByRole("button", { name: /Add Story/i }));

    await waitFor(() => {
      expect(axios.post).toHaveBeenCalledTimes(1);
      expect(axios.post).toHaveBeenCalledWith(
        expect.stringContaining("/api/stories"),
        newStoryData
      );
    });

    await waitFor(() => {
      expect(screen.getByText(newStoryData.title)).toBeInTheDocument();
      expect(
        screen.getByText(`Department: ${newStoryData.department}`)
      ).toBeInTheDocument();
    });
  });
});
