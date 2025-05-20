package com.tracker_cli.executor.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.tracker_cli.model.Task;
import com.tracker_cli.model.TaskDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public interface DataOperator {
    String DATA_LOCATION = System.getProperty("user.home")+"\\.tasktrack\\data";
    File TASKS_DATA_FILE = new File(DATA_LOCATION+"\\tasks.json");
    File RULES_DATA_FILE = new File(DATA_LOCATION+"\\rules.json");



    static boolean addTask(TaskDetail taskDetail){
        if(!TASKS_DATA_FILE.exists()) createDataLocation();
        ObjectMapper mapper = new ObjectMapper();
        List<Task> tasks;
        try {
            tasks = mapper.readValue(TASKS_DATA_FILE, new TypeReference<List<Task>>(){});
        } catch (IOException e) {
            System.err.println("ERROR: could not find tasks data file. "+TASKS_DATA_FILE.getAbsolutePath());
            return false;
        }
        Task task = new Task(intToHex(tasks.size()+1000), taskDetail.getTaskName(), taskDetail.getTaskStatus(),LocalDate.now(), LocalTime.now());
        return true;
    }

    static boolean addRule(){
        if(!RULES_DATA_FILE.exists()) createDataLocation();
        return true;
    }


    static void createDataLocation() {
        (new File(DATA_LOCATION)).getParentFile().mkdirs();
    }

    //TODO Edit later
    private static String intToHex(int in) {
        return ""+in;
    }
}
