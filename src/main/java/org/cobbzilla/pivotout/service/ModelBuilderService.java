package org.cobbzilla.pivotout.service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.cobbzilla.pivotout.model.*;
import org.cobbzilla.util.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class ModelBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(ModelBuilderService.class);

    private static final Class[] XSTREAM_CLASSES = { Activity.class, Story.class, Token.class, Activities.class, Stories.class };

    private XStream xStream;

    /**
     * see: http://stackoverflow.com/questions/4409717/java-xstream-ignore-tag-that-doesnt-exist-in-xml
     * @return an XStream parser that will ignore elements in XML that have no corresponding object field
     */
    public XStream getXStream() {
        if (xStream == null) {
            xStream = new XStream() {
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return new MapperWrapper(next) {
                        public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                            try {
                                return definedIn != Object.class || realClass(fieldName) != null;
                            } catch(CannotResolveClassException cnrce) {
                                return false;
                            }
                        }
                    };
                }
            };
            xStream.processAnnotations(XSTREAM_CLASSES);
        }
        return xStream;
    }

    public Activity buildActivity (String xml) {
        return (Activity) getXStream().fromXML(xml);
    }

    public Activity buildActivity (InputStream in) {
        in = StreamUtil.log(in);
        return (Activity) getXStream().fromXML(in);
    }

    public List<Activity> buildActivityList (InputStream in) {
        return ((Activities) getXStream().fromXML(in)).getActivities();
    }

    public Token buildToken(InputStream in) {
        return (Token) getXStream().fromXML(in);
    }

    public List<Story> buildStoryList(InputStream in) {
        LOG.info("parsing story list=====>");
        in = StreamUtil.log(in);
        return ((Stories) getXStream().fromXML(in)).getStoryList();
    }
}
