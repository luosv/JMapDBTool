package com.dblog.task;

import com.dblog.bean.BaseLogBean;
import com.util.TimeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;


/**
 */
public class DBLogTask implements Runnable {

    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(DBLogTask.class);
    public final static AtomicInteger count = new AtomicInteger();
    private DataSource ds;
    private BaseLogBean bean;

    public DBLogTask(BaseLogBean bean, DataSource ds) {
        this.ds = ds;
        this.bean = bean;
    }

    @Override
    public void run() {
        String buildSql = "";
        String buildCreateTableSql = "";
        Connection connection = null;
        try {
            buildCreateTableSql = bean.buildCreateTableSql(TimeUtils.Time());
            buildSql = bean.buildSql();
            connection = ds.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.execute(buildCreateTableSql);
            createStatement.executeUpdate(buildSql);

            if (logger.isDebugEnabled()) {
                logger.debug(buildSql);
            }
            count.getAndIncrement();
        } catch (SQLException e) {
            logger.error(e, e);
            bean.logToFile();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e, e);
                }
            }
        }
    }

    public BaseLogBean getBean() {
        return bean;
    }

    public void setBean(BaseLogBean bean) {
        this.bean = bean;
    }

}
