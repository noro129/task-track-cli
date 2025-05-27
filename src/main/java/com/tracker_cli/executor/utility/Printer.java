package com.tracker_cli.executor.utility;

import com.tracker_cli.model.Task;

import java.util.List;

public interface Printer {

    static void printTasks(List<Task> taskList) {
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
}
