package ru.jakimenko.tool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kyyakime
 */
public abstract class ProcessThread {

    private static final Logger LOG = LogManager.getLogger();

    protected Thread thready;
    protected boolean stopped;
    protected boolean running;

    protected ExecuterDispatcher execDispatcher;
    protected List<File> files;

    protected int allCount;
    protected int endCount;
    protected int successCount;
    protected int failCount;

    public ProcessThread() {
        running = false;
    }

    protected abstract void onExecute() throws InterruptedException;
    public abstract void showResult();

    private void done() {
        execDispatcher.getListExecuter().clear();
        execDispatcher = null;
        for (File file : files) {
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception ex) {
                    LOG.error(ex);
                }
            }
        }
        files.clear();
        files = null;
    }

    public void start() {
        stopped = false;
        allCount = 0;
        endCount = 0;
        successCount = 0;
        failCount = 0;
        execDispatcher = new ExecuterDispatcher();
        files = new ArrayList();

        thready = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    onExecute();
                } catch (InterruptedException ex) {
                }
            }
        });

        thready.start();
        running = true;
    }

    public void cancel() {
        stopped = true;
        execDispatcher.stop();
        if (thready.isAlive()) {
            thready.interrupt();
        }
        done();
        running = false;
    }

    public void complite() {
        done();
        running = false;
    }

    public Integer getProgress() {
        if (allCount > 0) {
            return (int)Math.round(100. * endCount / allCount);
        } else {
            return 0;
        }
    }

    protected void waitFinished() {
        while (execDispatcher.active() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                break;
            }
        }
        endCount = allCount;
    }

    public boolean isRunning() {
        return running;
    }
    
    public int getSuccessCount() {
        return successCount;
    }

    public int getFailCount() {
        return failCount;
    }
    
}
