package ru.jakimenko.tool.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author kyyakime
 */
public class ExecuterDispatcher {

    List<Future<?>> listExecuter = new ArrayList<>();
    private final boolean isRemoveDone;

    public ExecuterDispatcher() {
        isRemoveDone = true;
    }

    public ExecuterDispatcher(boolean isRemoveDone) {
        this.isRemoveDone = isRemoveDone;
    }

    public List<Future<?>> getListExecuter() {
        return listExecuter;
    }
            

    /**
     * Добавить задачу в список
     *
     * @param future
     */
    public void put(Future<?> future) {
        synchronized (listExecuter) {
            listExecuter.add(future);
        }
    }

    /**
     * Получить каличество активных задач
     *
     * @return
     */
    public int active() {
        int count = 0;
        synchronized (listExecuter) {
            Iterator it = listExecuter.iterator();
            while (it.hasNext()) {
                Future<?> future = (Future<?>) it.next();
                if (!future.isDone() && !future.isCancelled()) {
                    count++;
                } else if (isRemoveDone) {
                    it.remove();
                }
            }
        }
        return count;
    }

    public void stop() {
        synchronized (listExecuter) {
            Iterator it = listExecuter.iterator();
            while (it.hasNext()) {
                Future<?> future = (Future<?>) it.next();
                if (!future.isCancelled() && !future.isDone()) {
                    future.cancel(true);
                } else if (isRemoveDone) {
                    it.remove();
                }
            }
        }
    }
}
