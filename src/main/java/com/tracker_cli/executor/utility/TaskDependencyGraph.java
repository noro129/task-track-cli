package com.tracker_cli.executor.utility;

import com.tracker_cli.model.*;

import java.time.LocalDate;
import java.util.*;

public class TaskDependencyGraph {
    private final Map<String, List<String>> taskGraph;

    public TaskDependencyGraph(Set<Rule> ruleSet, RuleDetail targetRule) {
        taskGraph = new HashMap<>();
        for(Rule rule : ruleSet) {
            String firstTaskKey = rule.getFirstTaskStatus().toString()+" "+ rule.getFirstTaskHash();
            if(!taskGraph.containsKey(firstTaskKey)) {
                taskGraph.put(firstTaskKey, new ArrayList<>());
            }
            if(rule instanceof TaskToTaskRule) {
                String secondTaskKey = ((TaskToTaskRule) rule).getSecondTaskStatus().toString()+" "+ ((TaskToTaskRule) rule).getSecondTaskHash();
                if(!taskGraph.containsKey(secondTaskKey)) {
                    taskGraph.put(secondTaskKey, new ArrayList<>());
                }
                switch (rule.getRuleRelation()) {
                    case RuleEnum.AFTER -> taskGraph.get(secondTaskKey).add(firstTaskKey);
                    case RuleEnum.BEFORE -> taskGraph.get(firstTaskKey).add(secondTaskKey);
                }
            } else if (rule instanceof TaskToDateRule) {
                String dateKey = "date " + ((TaskToDateRule) rule).getDate();
                if(!taskGraph.containsKey(dateKey)) {
                    taskGraph.put(dateKey, new ArrayList<>());
                }
                switch (rule.getRuleRelation()) {
                    case RuleEnum.AFTER -> taskGraph.get(dateKey).add(firstTaskKey);
                    case RuleEnum.BEFORE -> taskGraph.get(firstTaskKey).add(dateKey);
                }
            }
        }

        String firstTaskKey = targetRule.getFirstTaskStatus()+" "+targetRule.getFirstTaskHash().toUpperCase();
        if(!taskGraph.containsKey(firstTaskKey)) {
            taskGraph.put(firstTaskKey, new ArrayList<>());
        }
        if(targetRule.getDate() == null) {
            String secondTaskKey = targetRule.getSecondTaskStatus()+" "+targetRule.getSecondTaskHash().toUpperCase();
            if(!taskGraph.containsKey(secondTaskKey)) {
                taskGraph.put(secondTaskKey, new ArrayList<>());
            }
            switch (targetRule.getRuleRelation()) {
                case RuleEnum.AFTER -> taskGraph.get(secondTaskKey).add(firstTaskKey);
                case RuleEnum.BEFORE -> taskGraph.get(firstTaskKey).add(secondTaskKey);
            }
        } else {
            String dateKey = "date "+targetRule.getDate();
            if(!taskGraph.containsKey(dateKey)) {
                taskGraph.put(dateKey, new ArrayList<>());
            }
            switch (targetRule.getRuleRelation()) {
                case RuleEnum.AFTER -> taskGraph.get(dateKey).add(firstTaskKey);
                case RuleEnum.BEFORE -> taskGraph.get(firstTaskKey).add(dateKey);
            }
        }
        addNaturalDatesOrderingEdges();
    }

    public List<String> retrieveExistingCycle() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("task dependency graph: \n");
        for(String key : taskGraph.keySet()) {
            stringBuilder.append(key).append(" -> ").append(taskGraph.get(key)).append("\n");
        }
        return stringBuilder.toString();
    }

    private void addNaturalDatesOrderingEdges() {
        List<LocalDate> includedDates = new ArrayList<>();
        for(String key : taskGraph.keySet()) {
            if(key.startsWith("date ")) {
                includedDates.add(LocalDate.parse(key.substring(5)));
            }
        }
        Collections.sort(includedDates);

        for(int i =0; i<includedDates.size(); i++) {
            for(int j=i+1; j<includedDates.size(); j++) {
                taskGraph.get("date "+includedDates.get(i)).add("date "+includedDates.get(j));
            }
        }
    }

}
