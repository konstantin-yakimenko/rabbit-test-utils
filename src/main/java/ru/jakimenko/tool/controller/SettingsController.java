package ru.jakimenko.tool.controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import ru.jakimenko.tool.task.RabbitGenTask;
import ru.jakimenko.tool.task.RabbitTask;
import ru.jakimenko.tool.task.Task;
import ru.jakimenko.tool.task.TaskManager;
import ru.jakimenko.tool.util.IoCContainerReader;

/**
 *
 * @author kyyakime
 */
@ManagedBean
@ApplicationScoped
public class SettingsController {
    
    private final TaskManager taskManager;
    private final List<RabbitTask> rabbitTasks = new ArrayList<>();
    private final List<RabbitGenTask> rabbitGenTasks = new ArrayList<>();
    
    public SettingsController() {
        taskManager = IoCContainerReader.getTaskManager();

        for (Task task: taskManager.getTasks()) {

            if (task instanceof RabbitTask) {
                rabbitTasks.add((RabbitTask)task);
            }
            if (task instanceof RabbitGenTask) {
                rabbitGenTasks.add((RabbitGenTask)task);
            }
        }        
        
    }

    public List<RabbitTask> getRabbitTasks() {
        return rabbitTasks;
    }

    public List<RabbitGenTask> getRabbitGenTasks() {
        return rabbitGenTasks;
    }
    

}
