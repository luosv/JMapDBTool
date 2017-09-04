package com.dblog.task;

import com.dblog.bean.BaseLogBean;

public class FileLogTask implements Runnable {
    BaseLogBean log;

    public FileLogTask(BaseLogBean log) {
        this.log = log;
    }

    @Override
    public void run() {
        log.logToFile();

    }
}
