package com.service;

import com.dblog.LogService;
import com.model.JMapLogBean;
import com.util.Symbol;
import com.util.TextFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapLogService {

    private static final Logger log = LogManager.getLogger(JMapLogService.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        JMapLogService processor;

        Singleton() {
            this.processor = new JMapLogService();
        }

        JMapLogService getProcessor() {
            return processor;
        }

    }

    public static JMapLogService getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 解析日志文件
     *
     * @throws IOException ex
     */
    public void readLogFile() throws IOException {
        File file = new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "jmaplog.txt");
        if (!file.exists()) {
            return;
        }
        List<String> list = TextFile.readLine(file);
        List<String> temp = new ArrayList<>();
        String[] str;
        String[] newStr;
        String[] ss;
        for (String line : list) {
            if (line == null || "".equals(line) || line.isEmpty()) {
                continue;
            }
            str = line.split(Symbol.BLANK_REG);
            for (String s : str) {
                if ("".equals(s)) {
                    continue;
                }
                temp.add(s);
            }
            if (temp.size() != 4) {
                temp.clear();
                continue;
            }
            newStr = temp.toArray(new String[1]);
            temp.clear();
            if (newStr.length != 4) {
                continue;
            }
            ss = newStr[0].split(Symbol.MAOHAO_REG);
            if (ss.length != 1) {
                continue;
            }
            newStr[0] = ss[0];
            log.error(newStr[0] + "  " + newStr[1] + "  " + newStr[2] + "  " + newStr[3]);
            //writeLogToDB(Integer.valueOf(newStr[0]), Integer.valueOf(newStr[1]), Integer.valueOf(newStr[2]), newStr[3]);
        }
        list.clear();
//        if (file.delete()) {
//            log.error("该轮文件解析完毕，删除成功！");
//        }
    }

    /**
     * 写数据库日志
     *
     * @param num       序号
     * @param instances 实例
     * @param bytes     大小
     * @param className 类名
     */
    private void writeLogToDB(int num, int instances, int bytes, String className) {

        JMapLogBean jMapLog = new JMapLogBean();

        jMapLog.setNum(num);
        jMapLog.setInstances(instances);
        jMapLog.setBytes(bytes);
        jMapLog.setClassName(className);

        LogService.getInstance().execute(jMapLog);

    }

    /**
     * 执行shell脚本
     */
    public void executeShell() {
        try {
            String shellPath = System.getProperty("user.dir") + File.separator + "shell" + File.separator + "jmap.sh";
            Process process = Runtime.getRuntime().exec(shellPath);
            process.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            log.error(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
