package org.cobbzilla.pivotout.model;

public enum StoryState {

    unscheduled, unstarted, started, finished, delivered, rejected, accepted;

    public static boolean isActive (String state) {

        if (state == null || state.length() == 0) return false;

        switch (StoryState.valueOf(state)) {
            case unscheduled:
            case unstarted:
            case started:
            case rejected:
                return true;
            default:
                return false;
        }
    }

}
