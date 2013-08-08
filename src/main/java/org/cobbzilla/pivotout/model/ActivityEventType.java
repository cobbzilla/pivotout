package org.cobbzilla.pivotout.model;

public enum ActivityEventType {

    note_create, story_create, story_update, story_delete, multi_story_delete, add_story_label, remove_story_label;

    public boolean isDelete() {
        return this == story_delete || this == multi_story_delete;
    }
}
