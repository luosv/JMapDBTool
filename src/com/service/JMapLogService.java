package com.service;

import com.dblog.LogService;
import com.model.JMapLogBean;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapLogService {

    private void writeJMapLog(int num, int instances, int bytes, String className) {

        JMapLogBean jMapLog = new JMapLogBean();

        jMapLog.setNum(num);
        jMapLog.setInstances(instances);
        jMapLog.setBytes(bytes);
        jMapLog.setClassName(className);

        LogService.getInstance().execute(jMapLog);

    }

}
