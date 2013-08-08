package org.cobbzilla.pivotout.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import org.cobbzilla.pivotout.rule.RuleAction;
import org.cobbzilla.pivotout.rule.action.EmailAction;
import org.cobbzilla.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@XStreamAlias("action")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"data"})
public class ActionConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ActionConfiguration.class);

    private String type;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    private String config;
    public String getConfig() { return config; }
    public void setConfig(String config) { this.config = config; }

    private String data;
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public RuleAction buildAction(Properties defaultProperties) {
        RuleAction action;
        switch (type) {
            case "email":
                action = new EmailAction();
                break;
            case "log":
                action = new EmailAction();
                break;
            default:
                throw new IllegalArgumentException("unrecognized action type: "+type);
        }
        action.configure(PropertiesUtil.propsFromString(data, defaultProperties));
        return action;
    }

}
