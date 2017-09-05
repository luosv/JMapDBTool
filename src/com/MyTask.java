package com;

import com.service.JMapLogService;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by luosv on 2017/9/5 0005.
 */
public class MyTask extends TimerTask {

    @Override
    public void run() {
        try {
            // 执行shell脚本
            JMapLogService.getInstance().executeShell();
            // 解析日志文件并记录数据库日志
            JMapLogService.getInstance().readLogFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
