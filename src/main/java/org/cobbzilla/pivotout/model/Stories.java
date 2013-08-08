package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("stories")
public class Stories {

    private static final Logger LOG = LoggerFactory.getLogger(Stories.class);

    @XStreamImplicit(itemFieldName="story")
    public List<Story> storyList = new ArrayList<>();
    public List<Story> getStoryList() { return storyList; }
    public void setStoryList(List<Story> storyList) { this.storyList = storyList; }

}
