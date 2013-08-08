package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@XStreamAlias("activities")
public class Activities {

    private static final Logger LOG = LoggerFactory.getLogger(Activities.class);

    @XStreamImplicit(itemFieldName="activity")
    private List<Activity> activities;
    public List<Activity> getActivities() { return activities; }
    public void setActivities(List<Activity> activities) { this.activities = activities; }

}
