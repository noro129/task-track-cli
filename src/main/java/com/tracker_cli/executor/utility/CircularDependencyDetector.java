package com.tracker_cli.executor.utility;

import com.tracker_cli.model.Rule;
import com.tracker_cli.model.RuleDetail;
import com.tracker_cli.model.TaskToTaskRule;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface CircularDependencyDetector {

    static boolean createsDeadLockDependency(RuleDetail ruleDetail, List<Rule> ruleList) {
        Set<Rule> relativeRulesList = filterRelatives(ruleDetail.getFirstTaskHash(), ruleList);
        if(ruleDetail.getDate() == null) {
            relativeRulesList.addAll(filterRelatives(ruleDetail.getSecondTaskHash(), ruleList));
        }
        System.out.println("INFO: related rules are "+relativeRulesList);
        return false;
    }

    private static Set<Rule> filterRelatives(String hash, List<Rule> ruleList) {
        if(hash==null) return new HashSet<>();
        Set<Rule> relatedRules = new HashSet<>();
        Set<String> hashes = new HashSet<>(Collections.singleton(hash));
        boolean changedInSize = true;
        int size;

        while(changedInSize) {
            size = relatedRules.size();
            for(Rule rule : ruleList) {
                if(hashes.contains(rule.getFirstTaskHash())) {
                    relatedRules.add(rule);
                    if(rule instanceof TaskToTaskRule) {
                        hashes.add(((TaskToTaskRule) rule).getSecondTaskHash());
                    }
                } else if (rule instanceof TaskToTaskRule) {
                    if(hashes.contains(((TaskToTaskRule) rule).getSecondTaskHash())) {
                        relatedRules.add(rule);
                        hashes.add(rule.getFirstTaskHash());
                    }
                }
            }
            changedInSize = relatedRules.size() != size;
        }
        return relatedRules;
    }
}
