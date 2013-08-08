package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("activity")
public class Activity {

    private static final Logger LOG = LoggerFactory.getLogger(Activity.class);

    private long id;
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @XStreamAlias("occurred_at")
    private String occurredAt;
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt (String occurredAt) { this.occurredAt = occurredAt; }

    @XStreamAlias("event_type")
    private String eventType;
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public ActivityEventType getActivityEventType () {
        return ActivityEventType.valueOf(eventType.toLowerCase());
    }

    @XStreamAlias("project_id")
    private String projectId;
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public boolean getHasProjectId () { return projectId != null && projectId.length() > 0; }


    private String author;
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @XStreamAlias("stories")
    private List<Story> stories;
    public List<Story> getStories() { return stories; }
    public void setStories(List<Story> stories) { this.stories = stories; }

    public Story getStory () {
        return (stories != null && !stories.isEmpty()) ? stories.get(0) : null;
    }
    public void setStory(Story story) {
        if (stories == null) stories = new ArrayList<>();
        stories.add(story);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", occurredAt='" + occurredAt + '\'' +
                ", eventType='" + eventType + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", stories=" + stories +
                ", project=" + projectId +
                '}';
    }

}
