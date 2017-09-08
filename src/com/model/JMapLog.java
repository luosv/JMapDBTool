package com.model;

/**
 * Created by luosv on 2017/9/6 0006.
 */
public class JMapLog {

    private int num;
    private int instances;
    private int bytes;
    private String className;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "JMapLog [num=" + num + ", instances=" + instances + ", bytes=" + bytes + ", className" + className + "]";
    }
}
