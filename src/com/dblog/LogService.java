package com.dblog;

import com.alibaba.druid.pool.DruidDataSource;
import com.db.config.ServerConfig;
import com.dblog.base.MetaData;
import com.dblog.bean.BaseLogBean;
import com.dblog.task.DBLogTask;
import com.dblog.task.FileLogTask;
import com.util.ClassUtil;
import com.util.DBUtils;
import com.util.TableCompar;
import com.util.TimeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public class LogService {

    public static final Logger logger = LogManager.getLogger(LogService.class);
    //public static final LogService instance = new LogService();
    public static final String poolName = "logdbpool";
    public static volatile boolean isFile = false; //数据库连接异常时，是否存文件

    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        LogService manager;

        Singleton() {
            this.manager = new LogService();
        }

        LogService getProcessor() {
            return manager;
        }
    }

    //LogService
    public static LogService getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private DruidDataSource ds;
    private ThreadPoolExecutor dbexecutor;
    private ThreadPoolExecutor fileexecutor;
    private static final int dbthreads = 20;
    private static final int filethreads = 10;
    private BlockingQueue<Runnable> dbqueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Runnable> filequeue = new LinkedBlockingQueue<>();
    private final static AtomicInteger count = new AtomicInteger();
    private final static AtomicInteger count1 = new AtomicInteger();
    private final static AtomicLong lostCount = new AtomicLong();

    private LogService() {
        logger.info("初始化日志数据库服务");
        try {
//            ds = new ComboPooledDataSource();
//            ds.setDriverClass(DbServerConfig.getLogDrivers());
//            ds.setJdbcUrl(DbServerConfig.getLogUrl());
//            ds.setPassword(DbServerConfig.getLogPassword());
//            ds.setUser(DbServerConfig.getLogUser());
//            ds.setInitialPoolSize(10);
//            ds.setAcquireIncrement(10);
//            ds.setMinPoolSize(10);
//            ds.setMaxPoolSize(30);
//            ds.setMaxIdleTime(60000);
//            ds.setCheckoutTimeout(2000);
//            ds.setIdleConnectionTestPeriod(60 * 10);
//            ds.setPreferredTestQuery(DbServerConfig.getLogValidationquery());
            ds = new DruidDataSource();
            ds.setDriverClassName(ServerConfig.getLogDrivers());
            ds.setUrl(ServerConfig.getLogUrl());
            ds.setUsername(ServerConfig.getLogUser());
            ds.setPassword(ServerConfig.getLogPassword());
            ds.setPoolPreparedStatements(true);//打开游标缓存
            ds.setMaxWait(60000);
            ds.setValidationQuery(ServerConfig.getLogValidationquery());
            ds.setMinIdle(10);
            ds.setMaxActive(30);
            ds.setMaxOpenPreparedStatements(20);
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);
            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeout(1800);//30分中的连接不用则关闭
            ds.setLogAbandoned(true);//记录删除日志

            logger.error("启动日志数据库连接池完毕" + ServerConfig.getLogUrl());
            sleep(200);//停留一下，确定日志连接池连接成功
            checkTable();
            dbexecutor = new ThreadPoolExecutor(10, dbthreads, 0l, TimeUnit.MILLISECONDS, dbqueue);
            fileexecutor = new ThreadPoolExecutor(5, filethreads, 0l, TimeUnit.MILLISECONDS, filequeue);
            logger.info("启动日志线程池完毕");
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
        logger.info("初始化日志数据库服务结束");
    }

    public final void checkTable() {
        Set<Class<BaseLogBean>> subClasses = ClassUtil.getSubClasses("com.game", BaseLogBean.class);
        Connection connection = null;
        try {
            connection = ds.getConnection();
            List<String> tableName = DBUtils.getTableName(connection);
            long currentTimeMillis = TimeUtils.Time();
            for (Class<BaseLogBean> cls : subClasses) {
                boolean ischou = Modifier.isAbstract(cls.getModifiers());
                if (ischou) {
                    logger.error(cls + "是抽象类， 不能进行实例===============================");
                    continue;
                }
                try {
                    BaseLogBean bean = cls.newInstance();
                    String buildTableName = bean.buildTableName(currentTimeMillis);
                    logger.error("检测查表" + buildTableName);
                    if (tableName.contains(buildTableName.toLowerCase())) {
                        List<ColumnInfo> columnDefine = DBUtils.getColumnDefine(connection, buildTableName);
                        Iterator<ColumnInfo> iterator = columnDefine.iterator();
                        while (iterator.hasNext()) {
                            ColumnInfo next = iterator.next();
                            if (next.getName().equalsIgnoreCase("id")) {
                                iterator.remove();
                            }

                        }

                        HashMap<String, ColumnInfo> dbmatedata = new HashMap<>();
                        for (ColumnInfo columnInfo : columnDefine) {
                            dbmatedata.put(columnInfo.getName(), columnInfo);
                        }
                        //存在表  对比结构
                        List<ColumnInfo> codeDefine = new ArrayList<>();
                        HashSet<MetaData> metaDataSet = bean.getMetadata();
                        for (MetaData md : metaDataSet) {
                            codeDefine.add(md.toColumnInfo());
                        }
                        List<String> compartor = TableCompar.getInstance().compartor(buildTableName, codeDefine, columnDefine);
                        if (compartor.size() > 0) {
                            Statement createStatement = connection.createStatement();
                            for (String string : compartor) {
                                logger.info("检查到变更" + string);
                                createStatement.addBatch(string);
                            }
                            createStatement.executeBatch();
                        }
                    } else {
                        //不存在表 略过
                    }
                    logger.error(buildTableName + "检查结束");
                } catch (Exception e) {
                    logger.error(cls.getName() + "," + e, e);
                }
            }
        } catch (SQLException e1) {
            logger.error(e1, e1);
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

    public void execute(BaseLogBean bean) {
        int dbsize = dbexecutor.getQueue().size();
        int filesize = fileexecutor.getQueue().size();
        int file = 0;
        long lostcount = 0;
        if (dbsize <= 8 * 10000) {
            //写数据库 十万
            file = count.get();
            dbexecutor.submit(new DBLogTask(bean, ds));
        } else if (filesize <= 2 * 10000) {
            //写文件2万
            file = count.getAndIncrement();
            fileexecutor.submit(new FileLogTask(bean));
            if (file != 0 && file % 100 == 0) {
                logger.info("executor(BaseLogBean) - filelogcount" + lostcount);
            }
        } else {
            //队列太长 丢掉
            lostcount = lostCount.getAndIncrement();
            logger.error("自启动开始共有" + lostcount + "条日志丢失");
            if (lostcount != 0 && lostcount % 1000 == 0) {
                logger.info("executor(BaseLogBean) - lostlogcount" + lostcount);
            }
        }

    }

    public void executeDDL(String ddl) {
        try {
            Connection connection = ds.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.execute(ddl);
            if (logger.isDebugEnabled()) {
                logger.info(ddl);
            }
        } catch (SQLException e) {
            logger.error(e + ":" + ddl);
            isFile = true;
        }

    }

    /**
     * 日志系统关闭 系统shutdown的时候调用这里
     */
    public void shutdown() {
        logger.info("正在关闭日志系统");
        List<Runnable> fileshutdownNow = fileexecutor.shutdownNow();
        List<Runnable> dbshutdownNow = dbexecutor.shutdownNow();
        if (fileshutdownNow.size() > 0) {
            logger.info("正在保存剩余日志队列中的数据,长度" + fileshutdownNow.size());
            for (int i = 0; i < fileshutdownNow.size(); i++) {
                Runnable runnable = fileshutdownNow.get(i);
                runnable.run();
                logger.info("保存文件日志队列第" + fileshutdownNow.size() + "条完成");
            }
        }
        if (dbshutdownNow.size() > 0) {
            for (int i = 0; i < dbshutdownNow.size(); i++) {
                Runnable task = dbshutdownNow.get(i);
                task.run();
                logger.info("保存数据库日志队列第" + dbshutdownNow.size() + "条完成");
            }
        }

        ds.close();
        logger.info("日志服务己关闭");
    }

}
