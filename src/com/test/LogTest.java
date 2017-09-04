package com.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class LogTest {

    private static final Logger log = LogManager.getLogger(LogTest.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        LogTest processor;

        Singleton() {
            this.processor = new LogTest();
        }

        LogTest getProcessor() {
            return processor;
        }

    }

    public static LogTest getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void testLog() {
        log.error("测试日志输出！");
    }

}
