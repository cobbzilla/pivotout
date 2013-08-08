package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XStreamAlias("note")
public class Note {

    private static final Logger LOG = LoggerFactory.getLogger(Note.class);

    private long id;
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    private String text;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

}
