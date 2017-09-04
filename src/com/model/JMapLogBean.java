package com.model;

import com.dblog.TableCheckStepEnum;
import com.dblog.base.Log;
import com.dblog.bean.BaseLogBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapLogBean extends BaseLogBean {

    private static final Logger LOGGER = LogManager.getLogger("JMapLogBean");

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.DAY;
    }

    @Override
    public void logToFile() {
        LOGGER.error(buildSql());
    }

    private int num; // 序号
    private int instances; // 实例
    private int bytes; // 大小
    private String className; // 类名

    @Log(fieldType = "int", index = "0", logField = "num")
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Log(fieldType = "int", index = "0", logField = "instances")
    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @Log(fieldType = "int", index = "0", logField = "bytes")
    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getClassName() {
        return className;
    }

    @Log(fieldType = "varchar(50)", index = "0", logField = "className")
    public void setClassName(String className) {
        this.className = className;
    }

}
