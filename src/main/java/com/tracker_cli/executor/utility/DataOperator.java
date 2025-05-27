package com.tracker_cli.executor.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tracker_cli.action.target.ListActionTarget;
import com.tracker_cli.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataOperator {
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .findAndRegisterModules();

    String DATA_LOCATION = System.getProperty("user.home")+"\\.tasktrack\\data";
    File TASKS_DATA_FILE = new File(DATA_LOCATION+"\\tasks.json");
    File RULES_DATA_FILE = new File(DATA_LOCATION+"\\rules.json");



    static boolean addTask(TaskDetail taskDetail){
        if(!TASKS_DATA_FILE.exists()) createDataLocation();

        List<Task> tasks;
        try {
            tasks = listTasks();
        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
            System.out.println(e.getMessage());
            return false;
        }

        Task task = new Task(Long.toHexString(System.currentTimeMillis()).toUpperCase(), taskDetail.getTaskName(), taskDetail.getTaskStatus(),LocalDate.now(), LocalTime.now());
        tasks.add(task);

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(TASKS_DATA_FILE, tasks);
        } catch (IOException e) {
            System.err.println("ERROR: could not add task "+task);
            return false;
        }
        return true;
    }

    static boolean addRule(RuleDetail ruleDetail){
        if(!RULES_DATA_FILE.exists()) createDataLocation();

        List<Rule> rules;
        try {
            if(RULES_DATA_FILE.exists()) rules = mapper.readValue(RULES_DATA_FILE, RuleList.class).getRuleList();
            else rules = new ArrayList<>();

        } catch (IOException e) {
            System.err.println("ERROR: could not read rules data file: "+RULES_DATA_FILE.getAbsolutePath());
            System.out.println(e.getMessage());
            return false;
        }

        if(!taskExists(ruleDetail.getFirstTaskHash())) {
            System.err.println("ERROR: task with id "+ruleDetail.getFirstTaskHash()+" does not exist");
            return false;
        }


        Rule rule;
        if(ruleDetail.getDate() != null) {
            rule = new TaskToDateRule(Long.toHexString(System.currentTimeMillis()).toUpperCase(),
                    ruleDetail.getFirstTaskStatus(),
                    ruleDetail.getFirstTaskHash(),
                    ruleDetail.getRuleRelation(),
                    ruleDetail.getDate());
        } else {
            if(!taskExists(ruleDetail.getSecondTaskHash())) {
                System.err.println("ERROR: task with id "+ruleDetail.getSecondTaskHash()+" does not exist");
                return false;
            }
            rule = new TaskToTaskRule(Long.toHexString(System.currentTimeMillis()).toUpperCase(),
                    ruleDetail.getFirstTaskStatus(),
                    ruleDetail.getFirstTaskHash(),
                    ruleDetail.getRuleRelation(),
                    ruleDetail.getSecondTaskStatus(),
                    ruleDetail.getSecondTaskHash());
        }

        rules.add(rule);

        try {
            RuleList ruleList = new RuleList();
            ruleList.setRuleList(rules);
            mapper.writerWithDefaultPrettyPrinter().writeValue(RULES_DATA_FILE, ruleList);
        } catch (IOException e) {
            System.err.println("ERROR: could not add rule: "+rule);
            return false;
        }
        return true;
    }

    static List<Task> listTasks(Set<TaskStatusEnum> include) {
        List<Task> taskList;
        try {
            taskList = listTasks();
        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
            System.out.println(e.getMessage());
            return null;
        }
        return taskList.stream().filter(task -> include.contains(task.getTaskStatus())).toList();
    }

    private static void createDataLocation() {
        (new File(DATA_LOCATION)).mkdirs();
    }

    private static boolean taskExists(String taskId) {
        List<Task> taskList;
        try {
            taskList = listTasks();
        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
            return false;
        }
        for(Task task : taskList) {
            if(task.getId().equalsIgnoreCase(taskId)) return true;
        }
        return false;
    }

    private static boolean createsDeadLockDependency(RuleDetail ruleDetail) {
        return false;
    }

    private static List<Task> listTasks() throws IOException {
        if(TASKS_DATA_FILE.exists()) return mapper.readValue(TASKS_DATA_FILE, new TypeReference<List<Task>>(){});
        else return new ArrayList<>();
    }


}

class RuleList {
    private List<Rule> ruleList;

    public RuleList() {
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }
}