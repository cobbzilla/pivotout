package org.cobbzilla.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class VelocityUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VelocityUtil.class);

    private static Map<String, Template> templates = new HashMap<>();

    public static Template getTemplate(String templateContents) throws ParseException {
        String templateName = MD5Util.md5hex(templateContents);
        Template template = templates.get(templateName);
        if (template == null) {
            RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
            StringReader reader = new StringReader(templateContents);
            SimpleNode node = runtimeServices.parse(reader, templateName);
            template = new Template();
            template.setRuntimeServices(runtimeServices);
            template.setData(node);
            template.initDocument();
            templates.put(templateName, template);
        }
        return template;
    }

    public static String merge(TemplateObject thing, String template) {
        VelocityContext vctx = new VelocityContext();
        vctx.put(thing.getTemplateObjectName(), thing);
        StringWriter out = new StringWriter();
        try {
            getTemplate(template).merge(vctx, out);
        } catch (ParseException e) {
            throw new IllegalStateException("merge: error merging thing ("+thing+") into template ("+template+"): "+e, e);
        }
        return out.toString();
    }
}
