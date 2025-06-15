package com.tracker_cli.executor.utility;

import com.tracker_cli.model.*;


import java.time.LocalDate;
import java.util.*;

public interface CircularDependencyDetector {

    static List<Rule> createsDeadLockDependency(RuleDetail ruleDetail, List<Rule> ruleList) {
        Set<Rule> relativeRulesList = filterRelatives(ruleDetail.getFirstTaskHash(), ruleList);
        if(ruleDetail.getDate() == null) {
            relativeRulesList.addAll(filterRelatives(ruleDetail.getSecondTaskHash(), ruleList));
        }
        TaskDependencyGraph taskGraph = new TaskDependencyGraph(relativeRulesList);
        Stack<String> cycle = taskGraph.retrieveExistingCycle();
        if(cycle.isEmpty()) {
            return new ArrayList<>();
        }

        return convertCycleNodesToRules(retrieveCycle(cycle), relativeRulesList);
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

    private static List<String> retrieveCycle(Stack<String> cycle) {
        String cycleHead = cycle.pop();
        Stack<String> cycleBody = new Stack<>();
        cycleBody.add(cycleHead);
        while(!cycle.isEmpty()) {
            String cycleNode = cycle.pop();
            cycleBody.add(cycleNode);
            if(cycleNode.equals(cycleHead)) break;
        }
        List<String> result = new ArrayList<>();
        while(!cycleBody.isEmpty()) {
            result.add(cycleBody.pop());
        }
        return result;
    }

    private static List<Rule> convertCycleNodesToRules(List<String> cycle, Set<Rule> ruleSet) {
        List<Rule> rulesCycle = new ArrayList<>();
        for(int i=0; i< cycle.size()-1; i++) {
            String left_node = cycle.get(i);
            String right_node = cycle.get(i+1);
            boolean left_node_is_date = left_node.startsWith("date ");
            boolean right_node_is_date = right_node.startsWith("date ");
            if(left_node_is_date && right_node_is_date) continue;
            else if (left_node_is_date || right_node_is_date) {
                rulesCycle.add(retrieveTaskToDateRule(left_node, right_node, ruleSet, left_node_is_date));
            } else {
                rulesCycle.add(retrieveTaskToTaskRule(left_node, right_node, ruleSet));
            }
        }
        return rulesCycle;
    }

    private static Rule retrieveTaskToDateRule(String left_node, String right_node, Set<Rule> ruleSet, boolean dateIsLeft) {
        for(Rule rule : ruleSet) {
            if (rule instanceof TaskToTaskRule) continue;
            RuleEnum ruleEnum = dateIsLeft ? RuleEnum.AFTER : RuleEnum.BEFORE;
            if (rule.getRuleRelation() == ruleEnum) {
                if (dateIsLeft) {
                    if(rule.getFirstTaskHash().equals(getHashFromNode(right_node)) &&
                            rule.getFirstTaskStatus() == getTaskStatusFromNode(right_node) &&
                            ((TaskToDateRule)rule).getDate().equals(LocalDate.parse(left_node.substring(5))))
                    {
                        return rule;
                    }
                } else {
                    if(rule.getFirstTaskHash().equals(getHashFromNode(left_node)) &&
                            rule.getFirstTaskStatus() == getTaskStatusFromNode(left_node) &&
                            ((TaskToDateRule)rule).getDate().equals(LocalDate.parse(right_node.substring(5))))
                    {
                        return rule;
                    }
                }
            }
        }
        return null;
    }

    private static Rule retrieveTaskToTaskRule(String left_node, String right_node, Set<Rule> ruleSet) {
        for(Rule rule : ruleSet) {
            if(rule instanceof TaskToDateRule) continue;
            TaskToTaskRule taskToTaskRule = (TaskToTaskRule) rule;
            if (rule.getRuleRelation()==RuleEnum.BEFORE) {
                if (taskToTaskRule.getFirstTaskStatus() == getTaskStatusFromNode(left_node)
                        && taskToTaskRule.getFirstTaskHash().equals(getHashFromNode(left_node))
                        && taskToTaskRule.getSecondTaskStatus() == getTaskStatusFromNode(right_node)
                        && taskToTaskRule.getSecondTaskHash().equals(getHashFromNode(right_node)))
                {
                    return rule;
                }
            } else {
                if (taskToTaskRule.getFirstTaskStatus() == getTaskStatusFromNode(right_node)
                        && taskToTaskRule.getFirstTaskHash().equals(getHashFromNode(right_node))
                        && taskToTaskRule.getSecondTaskStatus() == getTaskStatusFromNode(left_node)
                        && taskToTaskRule.getSecondTaskHash().equals(getHashFromNode(left_node)))
                {
                    return rule;
                }
            }
        }
        return null;
    }

    private static String getHashFromNode(String node) {
        String[] strings = node.split(" ");
        return strings[1];
    }

    private static TaskStatusEnum getTaskStatusFromNode(String node) {
        String[] strings = node.split(" ");
        return switch (strings[0].toUpperCase()) {
            case "DONE" -> TaskStatusEnum.Done;
            case "STARTED" -> TaskStatusEnum.Started;
            case "INPROGRESS" -> TaskStatusEnum.InProgress;
            default -> null;
        };
    }
}
