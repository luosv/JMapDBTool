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
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class JMapLogService {

    private static final Logger LOGGER = LogManager.getLogger(JMapLogService.class);

    private static ResourceBundle bundle = PropertyResourceBundle.getBundle("db");
    private static Connection connection = getConnection();

    /**
     * 执行shell脚本
     */
    public static boolean executeShell() {
        boolean isSuccess = true;
        try {
            String shellPath = System.getProperty("user.dir") + File.separator + "shell" + File.separator + "j_map.sh";
            Process process = Runtime.getRuntime().exec(shellPath);
            process.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            LOGGER.error(result);
            isSuccess = false;
        } catch (Exception e) {
            LOGGER.error("j_map.sh 脚本执行出错了！！！");
        }
        return isSuccess;
    }

    /**
     * 解析日志文件
     */
    public static List<JMapLog> readLogFile() {
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
//        if (file.delete()) {
//            LOGGER.error("j_map.log 解析完删除中...");
//        }
        return jMapLogList;
    }

    /**
     * 创建数据表
     */
    public static String createTable() {
        String tabName = "jmaplog" + DateUtil.getStringAllDate();
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
            return null;
        }
        LOGGER.error("create tabName:" + tabName + " end!");
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
            LOGGER.error("insertData running...");
            statement.executeBatch();
            connection.commit();
            statement.close();
            LOGGER.error("insertData end...");
        } catch (SQLException e) {
            LOGGER.error("写数据库日志时出错了！！！");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        }
        return conn;
    }

}
