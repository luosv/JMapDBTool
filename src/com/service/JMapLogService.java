package com.service;

import com.db.OptionConfigure;
import com.model.JMapLog;
import com.util.DateUtil;
import com.util.Symbol;
import com.util.TextFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapLogService {

    private static final Logger LOGGER = LogManager.getLogger(JMapLogService.class);

    private static final String RUNNING_SHELL_FILE = "j_map.sh";
    private static final String SHELL_FILE_DIR = System.getProperty("user.dir") + File.separator + "shell";
    private static Connection connection = getConnection();

    /**
     * 执行shell脚本
     */
    public static boolean executeShell() {
        LOGGER.error("Begin to execute a shell script...");
        boolean isSuccess = true;
        ProcessBuilder pb = new ProcessBuilder("./" + RUNNING_SHELL_FILE);
        pb.directory(new File(SHELL_FILE_DIR));
        int runningStatus = 0;
        String s;
        try {
            Process p = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                LOGGER.error(s);
            }
            while ((s = stdError.readLine()) != null) {
                LOGGER.error(s);
            }
            try {
                runningStatus = p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (runningStatus == 0) {
            isSuccess = false;
            LOGGER.error("Output log successful!");
        }
        return isSuccess;
    }

    /**
     * 解析日志文件
     */
    public static List<JMapLog> readLogFile() {
        LOGGER.error("Start parsing the j_map.log...");
        File file = new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "j_map.log");
        if (!file.exists()) {
            LOGGER.error("j_map.log 不存在！！！");
            return null;
        }
        List<JMapLog> jMapLogList = new ArrayList<>();
        List<String> list;
        try {
            list = TextFile.readLine(file);
        } catch (IOException e) {
            LOGGER.error("j_map.log 日志文件读取出错了！！！");
            e.printStackTrace();
            return null;
        }
        List<String> temp = new ArrayList<>();
        String[] str, newStr, ss;
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
            JMapLog jMapLog = new JMapLog();
            jMapLog.setNum(Integer.valueOf(newStr[0]));
            jMapLog.setInstances(Integer.valueOf(newStr[1]));
            jMapLog.setBytes(Integer.valueOf(newStr[2]));
            jMapLog.setClassName(newStr[3]);
            jMapLogList.add(jMapLog);
        }
        list.clear();
        LOGGER.error("The j_map.log parsing is complete!");
        if (file.delete()) {
            LOGGER.error("The j_map.log delete completed!");
        }
        return jMapLogList;
    }

    /**
     * 创建数据表
     */
    public static String createTable() {
        String tabName = "jmaplog" + DateUtil.getStringAllDate();
        LOGGER.error("Create table: " + tabName + "...");
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE " + tabName +
                    "(num INTEGER not NULL, " +
                    " instances INTEGER, " +
                    " bytes INTEGER, " +
                    " className VARCHAR(255), " +
                    " PRIMARY KEY ( num ))";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            LOGGER.error("创建数据表时出错了！！！");
            e.printStackTrace();
            return null;
        }
        LOGGER.error("Create table successful!");
        return tabName;
    }

    /**
     * 写数据库日志
     *
     * @param tabName     表名
     * @param jMapLogList 数据集合
     */
    public static void insertData(String tabName, List<JMapLog> jMapLogList) {
        if (tabName == null || jMapLogList == null) {
            return;
        }
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tabName + " VALUES (?, ?, ?, ?)");
            for (JMapLog jMapLog : jMapLogList) {
                statement.setInt(1, jMapLog.getNum());
                statement.setInt(2, jMapLog.getInstances());
                statement.setInt(3, jMapLog.getBytes());
                statement.setString(4, jMapLog.getClassName());
                statement.addBatch();
            }
            LOGGER.error("InsertData running...");
            statement.executeBatch();
            connection.commit();
            statement.close();
            LOGGER.error("InsertData successful!");
            LOGGER.error("Waiting...");
        } catch (SQLException e) {
            LOGGER.error("写数据库日志时出错了！！！");
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接的函数
     *
     * @return con
     */
    private static Connection getConnection() {
        OptionConfigure configure = new OptionConfigure();
        if (configure.hasConfig()) {
            try {
                configure.initFromXml();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        Connection conn = null;
        try {
            Class.forName(configure.getClassname());
            conn = DriverManager.getConnection(configure.getUrl(), configure.getUsername()
                    , configure.getPassword());
        } catch (Exception e) {
            LOGGER.error("数据库连接失败" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

}
