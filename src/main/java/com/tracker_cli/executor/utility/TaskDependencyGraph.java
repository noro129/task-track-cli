package com.tracker_cli.executor.utility;

import com.tracker_cli.model.*;

import java.time.LocalDate;
import java.util.*;

public class TaskDependencyGraph {
    private final Map<String, List<String>> taskGraph;

    public TaskDependencyGraph(Set<Rule> ruleSet) {
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
        addNaturalDatesOrderingEdges();
    }

    public Stack<String> retrieveExistingCycle() {
        Stack<String> result = new Stack<>();
        Set<String> visited = new HashSet<>();
        for(String node : taskGraph.keySet()){
            if(!visited.contains(node)) {
                visited.add(node);
                result.add(node);
                if(dfs(getNodeChildren(node), visited, result, new HashSet<>(result))) break;
                result.pop();
            }
        }
        return result;
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

    private boolean dfs(List<String> nodes, Set<String> visited, Stack<String> path, Set<String> pathNodes){
        for (String node : nodes) {
            if(pathNodes.contains(node)) {
                path.add(node);
                return true;
            }
            if(!visited.contains(node)) {
                path.add(node);
                visited.add(node);
                pathNodes.add(node);
                if(dfs(getNodeChildren(node), visited, path, pathNodes)) return true;
                else {
                    path.pop();
                    pathNodes.remove(node);
                }
            }
        }
        return false;
    }

    private List<String> getNodeChildren(String node) {
        if(node.startsWith("date ") || node.startsWith("Done ")) return taskGraph.getOrDefault(node, new ArrayList<>());
        else {
            List<String> result = taskGraph.getOrDefault(node, new ArrayList<>());
            if (node.startsWith("Started ")) {
                if(taskGraph.containsKey("Inprogress "+node.substring(8))) result.add("Inprogress "+node.substring(8));
                if(taskGraph.containsKey("Done "+node.substring(8))) result.add("Done "+node.substring(8));
            }
            if(node.startsWith("Inprogress ")) {
                if(taskGraph.containsKey("Done "+node.substring(11))) result.add("Done "+node.substring(11));
            }
            return result;
        }
    }

}
