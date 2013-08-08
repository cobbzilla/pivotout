package org.cobbzilla.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(StreamUtil.class);


    public static InputStream log(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading stream: "+e, e);
        }
        LOG.info(out.toString());
        return new ByteArrayInputStream(out.toByteArray());
    }
}
