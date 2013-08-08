package org.cobbzilla.pivotout.rule.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Base class for LabelAppliedTrigger and LabelRemovedTrigger. Allows us to keep their config props consistent.
 */
public abstract class LabelTriggerBase extends RuleTriggerBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelTriggerBase.class);

    public static final String PROP_MATCH_LABEL = "matchLabel";

    public String getMatchLabel() { return config.getProperty(PROP_MATCH_LABEL).trim(); }

    protected List<String> getMatchLabelArray() {
        return splitLabelList(PROP_MATCH_LABEL);
    }

    private Map<String, List<String>> labelArrays = new HashMap<>();
    protected List<String> splitLabelList(String propName) {
        List<String> labelArray = labelArrays.get(propName);
        if (labelArray == null) {
            labelArray = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(config.getProperty(propName), ",\t ");
            while (tokenizer.hasMoreTokens()) {
                labelArray.add(tokenizer.nextToken());
            }
            labelArrays.put(propName, labelArray);
        }
        return labelArray;
    }

}
