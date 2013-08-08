package org.cobbzilla.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties propsFromString (String propsString) {
        return propsFromString(propsString, null);
    }

    public static Properties propsFromString (String propsString, Properties defaults) {
        Properties props = new Properties(defaults);
        if (propsString == null) return props;
        try (InputStream in = new ByteArrayInputStream(propsString.getBytes())) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException("error reading input string: "+e, e);
        }
        return props;
    }

}
