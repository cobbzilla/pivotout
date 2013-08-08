package org.cobbzilla.pivotout.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XStreamAlias("token")
public class Token {

    private static final Logger LOG = LoggerFactory.getLogger(Token.class);

    private String guid;
    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    private String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

}
