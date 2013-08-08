package org.cobbzilla.pivotout.config;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import org.cobbzilla.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@XStreamConverter(value=ToAttributedValueConverter.class, strings={"data"})
public class TemplateConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateConfiguration.class);

    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    private String data;
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Properties getProperties () {
        return PropertiesUtil.propsFromString(data);
    }
}
