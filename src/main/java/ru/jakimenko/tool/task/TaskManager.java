package ru.jakimenko.tool.task;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 *
 * @author kyyakime
 */
public class TaskManager {

    private final Map<String, Task> tasks = new LinkedHashMap<>();

    private final TaskScheduler taskScheduler;

    public TaskManager(List<Task> tasks, TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        for (Task task : tasks) {
            addTask(task);
        }
    }

    public final void addTask(Task task) {
        tasks.put(task.getId(), task);
        taskScheduler.schedule(task, new CronTrigger(task.getCron()));
    }

    public List<Task> getTasks() {
        List<Task> taskList = new LinkedList<>();
        for (Task task : tasks.values()) {
            taskList.add(task);
        }
        return taskList;
    }
}
