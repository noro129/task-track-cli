package com.tracker_cli.executor.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tracker_cli.action.target.CleanActionTarget;
import com.tracker_cli.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
            System.err.println(e.getMessage());
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
            rules = listRules();

        } catch (IOException e) {
            System.err.println("ERROR: could not read rules data file: "+RULES_DATA_FILE.getAbsolutePath());
            System.err.println(e.getMessage());
            return false;
        }

        if(!taskExists(ruleDetail.getFirstTaskHash())) {
            System.err.println("ERROR: task with id "+ruleDetail.getFirstTaskHash()+" does not exist");
            return false;
        }

        AtomicBoolean overrideRule = new AtomicBoolean(false);

        Rule rule;
        if(ruleDetail.getDate() != null) {
            rule = new TaskToDateRule(Long.toHexString(System.currentTimeMillis()).toUpperCase(),
                    ruleDetail.getFirstTaskStatus(),
                    ruleDetail.getFirstTaskHash(),
                    ruleDetail.getRuleRelation(),
                    ruleDetail.getDate());
            rules.forEach(rule1 -> {
                if(rule1 instanceof TaskToDateRule) {
                    if(Objects.equals(rule1.getFirstTaskHash(), rule.getFirstTaskHash()) && rule1.getRuleRelation()==rule.getRuleRelation() && rule1.getFirstTaskStatus()==rule.getFirstTaskStatus()) {
                        System.out.println("INFO: similar rule already exists.");
                        System.out.println("\texisting rule: "+rule1.toString());
                        System.out.println("\tnew rule     : "+ruleDetail.toString());
                        overrideRule.set(true);
                        ((TaskToDateRule) rule1).setDate(ruleDetail.getDate());
                    }
                }
            });
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
            rules.forEach(rule1 -> {
                if(rule1 instanceof TaskToTaskRule) {
                    if (rule1.getFirstTaskStatus()==ruleDetail.getFirstTaskStatus() &&
                    rule1.getFirstTaskHash().equalsIgnoreCase(ruleDetail.getFirstTaskHash()) &&
                    rule1.getRuleRelation() == ruleDetail.getRuleRelation() &&
                    ((TaskToTaskRule) rule1).getSecondTaskHash().equalsIgnoreCase(ruleDetail.getSecondTaskHash()) &&
                    ((TaskToTaskRule) rule1).getSecondTaskStatus() == ruleDetail.getSecondTaskStatus()) {
                        overrideRule.set(true);
                        System.out.println("INFO: rule already exists.");
                        System.out.println("\texisting rule: "+rule1.toString());
                    }
                }
            });
            if(overrideRule.get()) return true;
        }


        if(!overrideRule.get()) rules.add(rule);

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
            System.err.println(e.getMessage());
            return null;
        }
        return taskList.stream().filter(task -> include.contains(task.getTaskStatus())).toList();
    }

    static List<Rule> listRules(String type, String taskHash) {
        List<Rule> ruleList;
        try {
            ruleList = listRules();
        } catch (IOException e) {
            System.err.println("ERROR: could not read rules data file: "+RULES_DATA_FILE.getAbsolutePath());
            System.err.println(e.getMessage());
            return null;
        }
        return ruleList.stream().filter(rule -> {
            boolean condition = true;
            if(type!=null) {
                condition = rule.getClass() == matchTypeToRule(type);
            }
            if(taskHash!=null && !taskHash.isEmpty()) {
                if(rule instanceof TaskToTaskRule) condition = condition && (((TaskToTaskRule) rule).getSecondTaskHash().equalsIgnoreCase(taskHash) || rule.getId().equalsIgnoreCase(taskHash));
                else condition = condition && rule.getId().equalsIgnoreCase(taskHash);
            }
            return condition;
        }).toList();
    }

    static boolean cleanTasks(boolean showFirst, CleanActionTarget cleanAction) {
        TaskStatusEnum taskStatus = CleanStatusMapper.getTaskStatusFromCleanTarget(cleanAction);
        if (showFirst) {
            if(taskStatus==null) {
                Printer.printTasks(listTasks(new HashSet<>(List.of(TaskStatusEnum.values()))));
                deleteRulesByTask(listTasks(new HashSet<>(List.of(TaskStatusEnum.values()))), true);
            }
            else {
                Printer.printTasks(listTasks(new HashSet<>(Collections.singleton(taskStatus))));
                deleteRulesByTask(listTasks(new HashSet<>(Collections.singleton(taskStatus))), true);
            }
        } else {
            List<Task> taskList;
            try {
                taskList = listTasks();
            } catch (IOException e) {
                System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
                System.err.println(e.getMessage());
                return false;
            }

            if(!deleteRulesByTask(taskList.stream().filter(task -> task.getTaskStatus()==taskStatus).toList(), false)) return false;
            taskList = taskList.stream().filter(task -> task.getTaskStatus()!=taskStatus).toList();

            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(TASKS_DATA_FILE, taskList);
            } catch (IOException e) {
                System.err.println("ERROR: could not remove "+cleanAction.toString()+" tasks.");
                return false;
            }
            System.out.println("INFO: '"+cleanAction.toString()+"' tasks deleted successfully.");
        }
        return true;
    }

    static boolean updateTask(String taskHash, String newTaskMessage, TaskStatusEnum taskStatus) {
        List<Task> taskList;
        AtomicBoolean taskFound = new AtomicBoolean(false);
        try {
            taskList = listTasks();
        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
            System.err.println(e.getMessage());
            return false;
        }
        taskList.forEach(task -> {
            if(task.getId().equalsIgnoreCase(taskHash)){
                if(newTaskMessage!=null) task.setTaskName(newTaskMessage);
                if(taskStatus!=null) task.setTaskStatus(taskStatus);
                taskFound.set(true);
            }
        });
        if(taskFound.get()) {
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(TASKS_DATA_FILE, taskList);
            } catch (IOException e) {
                System.err.println("ERROR: error while saving the task "+taskHash+" with the new modification.");
                return false;
            }
        } else {
            System.err.println("ERROR: task with id "+taskHash+" does not exist");
        }
        return taskFound.get();
    }

    static boolean removeTask(String hash, boolean safeCheck) {
        List<Task> taskList;
        AtomicReference<Task> taskToRemove = new AtomicReference<>(null);


        try {
            taskList = listTasks();
        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file: "+TASKS_DATA_FILE.getAbsolutePath());
            System.err.println(e.getMessage());
            return false;
        }

        taskList = taskList.stream().filter(task -> {
            if(task.getId().equals(hash.toUpperCase())) {
                taskToRemove.set(task);
            }
            return !task.getId().equals(hash.toUpperCase());
        }).toList();

        if(safeCheck) {
            System.out.println("INFO: task selected to be deleted.");
            Printer.printTasks(Collections.singletonList(taskToRemove.get()));
            deleteRulesByTask(Collections.singletonList(taskToRemove.get()), true);
            return true;
        }
        if(taskToRemove.get() == null) {
            System.out.println("INFO: task with hash "+hash+" does not exist.");
            return true;
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(TASKS_DATA_FILE, taskList);
        } catch (IOException e) {
            System.err.println("ERROR: could not delete task with hash: "+hash);
            return false;
        }
        deleteRulesByTask(Collections.singletonList(taskToRemove.get()), false);
        return true;
    }

    static boolean removeRule(String hash) {
        List<Rule> ruleList;

        try {
            ruleList = listRules();
        } catch (IOException e) {
            System.err.println("ERROR: could not read rules data file: "+RULES_DATA_FILE.getAbsolutePath());
            System.err.println(e.getMessage());
            return false;
        }

        ruleList = ruleList.stream().filter(rule -> !rule.getId().equals(hash.toUpperCase())).toList();

        try {
            RuleList ruleListWrapper = new RuleList();
            ruleListWrapper.setRuleList(ruleList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(RULES_DATA_FILE, ruleListWrapper);
        } catch (IOException e) {
            System.err.println("ERROR: could not delete rule with hash: "+hash);
            return false;
        }

        return true;
    }

    private static Class<? extends Rule> matchTypeToRule(String type) {
        return switch (type.toLowerCase()) {
            case "tasktotask" -> TaskToTaskRule.class;
            case "tasktodate" -> TaskToDateRule.class;
            default -> Rule.class;
        };
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

    //TODO implement circular dependency check method later
    private static boolean createsDeadLockDependency(RuleDetail ruleDetail) {
        return false;
    }

    private static List<Task> listTasks() throws IOException {
        if(TASKS_DATA_FILE.exists()) return mapper.readValue(TASKS_DATA_FILE, new TypeReference<List<Task>>(){});
        else return new ArrayList<>();
    }

    private static List<Rule> listRules() throws IOException {
        if(RULES_DATA_FILE.exists()) return mapper.readValue(RULES_DATA_FILE, RuleList.class).getRuleList();
        else return new ArrayList<>();
    }

    private static boolean deleteRulesByTask(List<Task> taskList, boolean showFirst) {
        if(taskList == null) return true;
        Set<String> taskHashes = new HashSet<>();
        for(Task task : taskList) taskHashes.add(task.getId());
        List<Rule> ruleList;
        try {
            ruleList = listRules();
        } catch (IOException e) {
            return false;
        }
        if(showFirst) {
            System.out.println("INFO: listing rules selected to be deleted.");
            Printer.printRules(ruleList.stream().filter(rule -> {
                if (rule instanceof TaskToDateRule) return taskHashes.contains(rule.getFirstTaskHash());
                else {
                    return taskHashes.contains(rule.getFirstTaskHash()) || taskHashes.contains(((TaskToTaskRule) rule).getSecondTaskHash());
                }
            }).toList());
            return true;
        }

        ruleList = ruleList.stream().filter(rule -> {
            if (rule instanceof TaskToDateRule) return !taskHashes.contains(rule.getFirstTaskHash());
            else {
                return !taskHashes.contains(rule.getFirstTaskHash()) && !taskHashes.contains(((TaskToTaskRule) rule).getSecondTaskHash());
            }
        }).toList();

        try {
            RuleList ruleListWrapper = new RuleList();
            ruleListWrapper.setRuleList(ruleList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(RULES_DATA_FILE, ruleListWrapper);
        } catch (IOException e) {
            System.err.println("ERROR: could not remove associated rules");
            return false;
        }

        return true;
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