package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.rule.Rule;
import org.cobbzilla.pivotout.rule.RuleAction;
import org.cobbzilla.pivotout.rule.RuleTrigger;
import org.cobbzilla.pivotout.service.EventProcessingService;
import org.cobbzilla.pivotout.service.ModelBuilderService;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerTestBase.class);

    private ModelBuilderService modelBuilderService = new ModelBuilderService();
    private EventProcessingService eventProcessingService;

    protected PivotalApiService apiService = new PivotalApiService();

    @Before
    public void initEPService () { eventProcessingService = new EventProcessingService(); }

    protected MockAction addRule(RuleTrigger trigger) {
        Rule rule = new Rule();
        final MockAction action = new MockAction();
        rule.setAction(action);
        rule.setTrigger(trigger);

        eventProcessingService.addRule(rule);
        return action;
    }

    protected void process(String xml) {
        Activity activity = modelBuilderService.buildActivity(xml);
        eventProcessingService.processEvent(activity);
    }

    class MockAction implements RuleAction {

        public boolean performCalled = false;

        @Override
        public void configure(Properties props) {}

        @Override
        public Object perform(Story story) {
            performCalled = true;
            return new Object();
        }
    }

}
