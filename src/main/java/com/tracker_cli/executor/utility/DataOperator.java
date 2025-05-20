package com.tracker_cli.executor.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tracker_cli.model.Task;
import com.tracker_cli.model.TaskDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface DataOperator {
    String DATA_LOCATION = System.getProperty("user.home")+"\\.tasktrack\\data";
    File TASKS_DATA_FILE = new File(DATA_LOCATION+"\\tasks.json");
    File RULES_DATA_FILE = new File(DATA_LOCATION+"\\rules.json");



    static boolean addTask(TaskDetail taskDetail){
        if(!TASKS_DATA_FILE.exists()) createDataLocation();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Task> tasks;
        try {
            if(TASKS_DATA_FILE.exists()) tasks = mapper.readValue(TASKS_DATA_FILE, new TypeReference<List<Task>>(){});
            else tasks = new ArrayList<>();

        } catch (IOException e) {
            System.err.println("ERROR: could not read tasks data file. "+TASKS_DATA_FILE.getAbsolutePath());
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

    static boolean addRule(){
        if(!RULES_DATA_FILE.exists()) createDataLocation();
        return true;
    }

    static void createDataLocation() {
        (new File(DATA_LOCATION)).mkdirs();
    }
}
