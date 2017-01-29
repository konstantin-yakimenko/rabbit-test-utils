package ru.jakimenko.tool.task;

import java.util.UUID;

/**
 *
 * @author kyyakime
 */
public abstract class Task implements Runnable, Cloneable {
    
    private final String id;

    private final String name;
    private final String cron;
    private boolean active;
    
    public Task(String name, String cron, boolean autoStart) {
        this.id = UUID.randomUUID().toString();
        this.cron = cron;
        this.name = name;
        this.active = autoStart;        
    }
    
    @Override
    public abstract void run();
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCron() {
        return cron;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
