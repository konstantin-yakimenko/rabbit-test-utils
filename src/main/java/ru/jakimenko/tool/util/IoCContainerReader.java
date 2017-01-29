package ru.jakimenko.tool.util;

import java.util.Map;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.jakimenko.tool.handler.FixHandler;
import ru.jakimenko.tool.task.TaskManager;

/**
 *
 * @author kyyakime
 */
public class IoCContainerReader {

    private static <T> T getManagedBean(final String name, final Class<T> clazz) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (T) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, name);
    }

    public static ThreadPoolTaskExecutor getTaskExecutor() {
        return getManagedBean("taskExecutor", ThreadPoolTaskExecutor.class);
    }

    public static ThreadPoolTaskExecutor getProcessExecutor() {
        return getManagedBean("processExecutor", ThreadPoolTaskExecutor.class);
    }
    
    public static Integer getPeriodUpdateListVerifications() {
        return getManagedBean("periodUpdateListVerifications", Integer.class);
    }

    public static Integer getNumberPartCountByFixAndClose() {
        return getManagedBean("numberPartCountByFixAndClose", Integer.class);
    }

    public static String getPathTemp() {
        return getManagedBean("pathTemp", String.class);
    }

    public static TaskManager getTaskManager() {
        return getManagedBean("taskManager", TaskManager.class);
    }
//
//    public static ParametersManager getParametersManager() {
//        return getManagedBean("parametersManager", ParametersManager.class);
//    }
    
    public static FixHandler getFixHandler() {
        return getManagedBean("fixHandler", FixHandler.class);
    }
    
}
