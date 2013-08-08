package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.cobbzilla.util.TemplateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@XStreamAlias("story")
public class Story implements TemplateObject {

    public static final String PIVOTAL_STORY_URL_PREFIX = "https://www.pivotaltracker.com/story/show/";

    private static final Logger LOG = LoggerFactory.getLogger(Story.class);

    @Override public String getTemplateObjectName() { return Story.class.getSimpleName().toLowerCase(); }

    private long id;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    private String url;
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getStoryUrl () {
        return PIVOTAL_STORY_URL_PREFIX + id;
    }

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean getHasName() { return name != null && name.length() > 0; }

    public String getMostRecentName() {
        if (relatedActivities == null) return getName();
        for (Activity activity : relatedActivities) {
            if (activity.getStory().getHasName()) return activity.getStory().getName();
        }
        return getName();
    }

    @XStreamAlias("owned_by")
    private String ownedBy;
    public String getOwnedBy() { return ownedBy; }
    public void setOwnedBy(String ownedBy) { this.ownedBy = ownedBy; }

    public boolean getHasOwner () { return ownedBy != null && ownedBy.length() > 0; }
    public boolean getWasOwnerRemoved() { return ownedBy != null && ownedBy.length() == 0; }

    public String getMostRecentOwner() {
        if (relatedActivities == null) return getOwnedBy();
        for (Activity activity : relatedActivities) {
            final String owner = activity.getStory().getOwnedBy();
            if (owner != null && owner.length() > 0) return owner;
        }
        return getOwnedBy();
    }

    @XStreamAlias("requested_by")
    private String requestedBy;
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }

    private int estimate;
    public int getEstimate() { return estimate; }
    public void setEstimate(int estimate) { this.estimate = estimate; }

    @XStreamAlias("story_type")
    private String storyType;
    public String getStoryType() { return storyType; }
    public void setStoryType(String storyType) { this.storyType = storyType; }

    public StoryType getType () { return StoryType.valueOf(storyType); }

    private List<Note> notes;
    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }

    @XStreamAlias("accepted_at")
    private String acceptedAt;
    public String getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(String acceptedAt) { this.acceptedAt = acceptedAt; }

    @XStreamAlias("current_state")
    private String currentState;
    public String getCurrentState() { return currentState; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }

    public String getMostRecentState() {
        if (relatedActivities == null) return getCurrentState();
        for (Activity activity : relatedActivities) {
            final String state = activity.getStory().getCurrentState();
            if (state != null && state.length() > 0) return state;
        }
        return StoryState.unscheduled.name();
    }

    private String labels;
    public String getLabels() { return labels; }
    public void setLabels(String labels) { this.labels = labels; }

    public boolean getHasLabels() { return labels != null; }

    public List<String> getLabelsList () {

        if (labels == null || labels.length() == 0) return Collections.emptyList();

        List<String> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(getLabels(), ",");
        while (st.hasMoreTokens()) {
            final String token = st.nextToken().trim();
            if (token != null && token.length() > 0) list.add(token);
        }

        return list;
    }

    public boolean getHasLabel (String label) { return getLabelsList().contains(label); }

    public boolean getHasLabelMatch(Pattern pattern) {
        for (String label : getLabelsList()) {
            if (pattern.matcher(label).matches()) return true;
        }
        return false;
    }


    public StoryState getState () { return StoryState.valueOf(currentState.toLowerCase()); }

    private List<Activity> relatedActivities = new ArrayList<>();
    public List<Activity> getRelatedActivities() { return relatedActivities; }
    public void setRelatedActivities(List<Activity> relatedActivities) { this.relatedActivities = relatedActivities; }

    public List<Activity> getRelatedPastActivities() {
        if (relatedActivities.size() <= 1) return Collections.emptyList();
        return relatedActivities.subList(1, relatedActivities.size());
    }

    public void addRelatedActivity(Activity activity) {
        if (activity == null) {
            final String msg = "addRelatedActivity: cannot add null activity";
            LOG.warn(msg);
            throw new NullPointerException(msg);
        }
        if (relatedActivities == null) {
            final String msg = "addRelatedActivity: how odd, relatedActivities is null. initializing it";
            relatedActivities = new ArrayList<>();
        }
        relatedActivities.add(0, activity);
    }

    public Activity getCurrentActivity() {
        if (relatedActivities == null || relatedActivities.size() == 0) return null;
        return relatedActivities.get(0);
    }

    public Activity getPreviousActivity() {
        if (relatedActivities == null || relatedActivities.size() < 2) return null;
        return relatedActivities.get(1);
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", notes=" + notes +
                ", acceptedAt='" + acceptedAt + '\'' +
                ", currentState='" + currentState + '\'' +
                ", relatedActivities=" + ((relatedActivities == null) ? 0 : relatedActivities.size()) +
                '}';
    }

    public String getProjectId() {
        if (relatedActivities == null || relatedActivities.size() == 0) return null;
        return getCurrentActivity().getProjectId();
    }

    public List<String> findPreviousLabelSet() {
        for (Activity activity : getRelatedPastActivities()) {
            if (activity.getStory().getHasLabels()) return activity.getStory().getLabelsList();
        }
        return Collections.emptyList();
    }

}
