package org.cobbzilla.pivotout.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.io.input.TeeInputStream;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.rule.Rule;
import org.cobbzilla.pivotout.service.EventProcessingService;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XStreamAlias("pivotout-config")
public class PivotoutConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(PivotoutConfiguration.class);

    private static final Class[] XSTREAM_CLASSES = {PivotoutConfiguration.class, TriggerConfiguration.class, ActionConfiguration.class};

    @XStreamAsAttribute
    private String token;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    @XStreamAsAttribute
    private String projects;
    public String getProjects() { return projects; }
    public void setProjects(String projects) { this.projects = projects; }

    @XStreamImplicit(itemFieldName="config")
    private List<TemplateConfiguration> configs = new ArrayList<>();
    public List<TemplateConfiguration> getConfigs() { return configs; }
    public void setConfigs(List<TemplateConfiguration> configs) { this.configs = configs; }

    public Map<String, TemplateConfiguration> getTemplateConfigurationMap() {
        Map<String, TemplateConfiguration> map = new HashMap<>();
        if (configs != null && configs.size() > 0) {
            for (TemplateConfiguration config : configs) {
                map.put(config.getName(), config);
            }
        }
        return map;
    }

    @XStreamAlias("rules")
    private List<RuleConfiguration> ruleConfigurations = new ArrayList<>();
    public List<RuleConfiguration> getRuleConfigurations() { return ruleConfigurations; }
    public void setRuleConfigurations(List<RuleConfiguration> ruleConfigurations) { this.ruleConfigurations = ruleConfigurations; }

    private String configurationXml;
    /** @return the XML that was used to create this configuration */
    public String getConfigurationXml() { return configurationXml; }

    public static PivotoutConfiguration buildConfig (InputStream in) {
        XStream xStream = new XStream();
        xStream.processAnnotations(XSTREAM_CLASSES);
        final ByteArrayOutputStream branch = new ByteArrayOutputStream();
        final PivotoutConfiguration configuration = (PivotoutConfiguration) xStream.fromXML(new TeeInputStream(in, branch));
        configuration.configurationXml = branch.toString();
        return configuration;
    }

    public void configure(EventProcessingService eventProcessingService) {

        PivotalApiService apiService = new PivotalApiService();
        apiService.setToken(token);

        for (RuleConfiguration configuration : ruleConfigurations) {
            final Rule rule = configuration.buildRule(apiService, getTemplateConfigurationMap());
            eventProcessingService.addRule(rule);
        }

        if (projects != null && projects.length() > 0) {
            for (String projectId : projects.split(", ")) {

                projectId = projectId.trim();
                if (projectId.length() == 0) continue;

                List<Story> stories;
                try {
                    LOG.info("requesting stories for project="+projectId);
                    stories = apiService.findStoriesByProject(projectId);
                    LOG.info("found "+stories.size()+" stories for project="+projectId);
                    for (Story story : stories) {
                        eventProcessingService.initializeStory(story);
                    }
                    LOG.info("successfully loaded "+stories.size()+" stories for project="+projectId);

                } catch (IOException e) {
                    LOG.error("Error loading stories for project: "+projectId+": "+e, e);
                }
            }
        }
    }
}
