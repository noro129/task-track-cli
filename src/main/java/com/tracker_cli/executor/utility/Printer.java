package com.tracker_cli.executor.utility;

import com.tracker_cli.model.Rule;
import com.tracker_cli.model.Task;
import com.tracker_cli.model.TaskToDateRule;
import com.tracker_cli.model.TaskToTaskRule;

import java.util.ArrayList;
import java.util.List;

public interface Printer {

    static void printTasks(List<Task> taskList) {
        if(taskList == null) return;
        printInWidthSize("Id", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task", 50 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Status", 20 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Date", 20 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Time", 20 ,true, true, '_');
        for(Task task : taskList) {
            printInWidthSize(task.getId(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(task.getTaskName(), 50 ,false, false);
            System.out.print("|");
            printInWidthSize(task.getTaskStatus().toString(), 20 ,false, true);
            System.out.print("|");
            printInWidthSize(task.getTaskDate().toString(), 20 ,false, true);
            System.out.print("|");
            printInWidthSize(task.getTaskTime().toString(), 20 ,true,true);

        }

        System.out.println(taskList.size()+" entries.\n");
    }

    private static void printInWidthSize(String word, int size, boolean newLine, boolean inMiddle) {
        printInWidthSize(word, size, newLine, inMiddle, ' ');
    }

    private static void printInWidthSize(String word, int size, boolean newLine, boolean inMiddle, char filler) {
        int nbSpaces = size - word.length();
        if(inMiddle) {
            for(int i=0; i<nbSpaces/2;i++) System.out.print(filler);
            System.out.print(word);
            for(int i=0; i< nbSpaces - nbSpaces/2;i++) System.out.print(filler);
        }
        else {
            System.out.print(word);
            for(int i=0; i<nbSpaces;i++) System.out.print(filler);
        }

        if(newLine) System.out.println();
    }

    static void printRules(List<Rule> ruleList) {
        List<TaskToTaskRule> taskToTaskRuleList = new ArrayList<>();
        List<TaskToDateRule> taskToDateRuleList = new ArrayList<>();
        ruleList.forEach(rule -> {
            if (rule instanceof TaskToTaskRule) {
                taskToTaskRuleList.add((TaskToTaskRule) rule);
            } else {
                taskToDateRuleList.add((TaskToDateRule) rule);
            }
        });

        printTaskToDateRule(taskToDateRuleList);
        System.out.println(taskToDateRuleList.size()+" entries.\n");
        printTaskToTaskRule(taskToTaskRuleList);
        System.out.println(taskToTaskRuleList.size()+" entries.\n");
    }

    private static void printTaskToDateRule(List<TaskToDateRule> ruleList) {
        printInWidthSize("Rule Id", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Status", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Hash", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Rule", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Date", 30 ,true, true, '_');
        for(TaskToDateRule rule : ruleList) {
            printInWidthSize(rule.getId(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getFirstTaskStatus().toString(), 30 ,false, true);
            System.out.print("|");
            printInWidthSize(rule.getFirstTaskHash(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getRuleRelation().toString(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getDate().toString(), 30 ,true, true);

        }
    }

    private static void printTaskToTaskRule(List<TaskToTaskRule> ruleList) {
        printInWidthSize("Rule Id", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Status", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Hash", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Rule", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Status", 30 ,false, true, '_');
        System.out.print("|");
        printInWidthSize("Task Hash", 30 ,true, true, '_');
        for(TaskToTaskRule rule : ruleList) {
            printInWidthSize(rule.getId(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getFirstTaskStatus().toString(), 30 ,false, true);
            System.out.print("|");
            printInWidthSize(rule.getFirstTaskHash(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getRuleRelation().toString(), 30 ,false, false);
            System.out.print("|");
            printInWidthSize(rule.getSecondTaskStatus().toString(), 30 ,false, true);
            System.out.print("|");
            printInWidthSize(rule.getSecondTaskHash(), 30 ,true, true);
        }
    }
}
