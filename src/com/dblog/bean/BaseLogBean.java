package com.dblog.bean;

import com.dblog.TableCheckStepEnum;
import com.dblog.base.Log;
import com.dblog.base.MetaData;
import com.util.BeanUtil;
import com.util.TimeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luosv on 2017/9/4 0004.
 */
public abstract class BaseLogBean {

    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LogManager.getLogger(BaseLogBean.class);
    //字段DDL信息 用于建表
    private static final ConcurrentHashMap<Class<? extends BaseLogBean>, HashSet<MetaData>> metadataset = new ConcurrentHashMap<>();
    //添加索引信息
    private static final ConcurrentHashMap<Class<? extends BaseLogBean>, HashSet<MetaData>> metadatasetindex = new ConcurrentHashMap<>();
    private long times = TimeUtils.Time();
    private static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat YYYY_MM = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");

    @SuppressWarnings("unchecked")
    public BaseLogBean() {
        //初始化日志字段信息
        if (!metadataset.containsKey(getClass())) {
            HashSet<MetaData> metadata = new HashSet<>();
            HashSet<MetaData> metadataindex = new HashSet<>();
            HashMap<String, String> testmap = new HashMap<>();
            Method[] declaredMethods = getClass().getMethods();
            for (Method method : declaredMethods) {
                Log annotation = method.getAnnotation(Log.class);
                if (annotation != null) {
                    metadata.add(new MetaData(annotation.logField(), annotation.fieldType()));
                    String indexstr = testmap.get(annotation.index());
                    if (null != indexstr) {
                        testmap.put(annotation.index(), indexstr + "," + annotation.logField());
                    } else if (!annotation.index().equals("0")) {                  //索引字段为0时不设置
                        testmap.put(annotation.index(), annotation.logField());
                    } else {
                    }
                }
            }
            Iterator iter = testmap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) iter.next();
                metadataindex.add(new MetaData(entry.getValue(), entry.getKey(), 0));
            }
            metadataset.putIfAbsent(getClass(), metadata);
            metadatasetindex.putIfAbsent(getClass(), metadataindex);
        }
    }

    public String buildSql() {
        if (logger.isDebugEnabled()) {
            logger.debug("buildSql(BaseLogBean) - start");
        }
        //构建表名
        String tableName = buildTableName(times);
        String fields = "(";
        String values = "(";
        //构建插入语句主体
        HashMap<String, Object> param = getParam();
        for (String key : param.keySet()) {
            fields += key + ",";
            values += dealValue(param.get(key)) + ",";
        }
        if (fields.length() > 1) {
            fields = fields.substring(0, fields.length() - 1);
        }
        if (values.length() > 1) {
            values = values.substring(0, values.length() - 1);
        }
        fields += ")";
        values += ")";
        //构建插入语句
        String buffer = "insert into `" + tableName + "` " + fields + "values" + values;
        if (logger.isDebugEnabled()) {
            logger.debug("buildSql(BaseLogBean) - end");
        }
        return buffer;
    }

    private String dealValue(Object object) {
        if (object instanceof Date) {
            return "'" + YYYY_MM_DD_HH_MM_SS.format(object) + "'";
        }
        if (object instanceof List) {
            //暂不作集合类支持
        }
        if (object instanceof String) {
            //TODO 防注入处理
//			object=
        }
        return object == null ? "''" : "'" + object.toString() + "'";
    }

    /**
     * 构建表名加日期滚动
     *
     * @param time
     * @return
     */
    public String buildTableName(long time) {
        String tablename = getTableName();
        switch (getRollingStep()) {
            case DAY:
                tablename += YYYY_MM_DD.format(new Date(time));
                break;
            case MONTH:
                tablename += YYYY_MM.format(new Date(time));
                break;
            case YEAR:
                tablename += YYYY.format(new Date(time));
                break;
            case WEEK:
                tablename += YYYY.format(new Date(time)) + "_" + TimeUtils.getWeekOfYear(time);
                break;
//		case UNROLL:
        }
        return tablename;
    }

    private HashMap<String, Object> getParam() {
        HashMap<String, Object> param = new HashMap<>();
        Method[] declaredMethods = getClass().getMethods();
        for (Method method : declaredMethods) {
            Log annotation = method.getAnnotation(Log.class);
            if (annotation != null) {
                try {
                    param.put(annotation.logField(), BeanUtil.invokeMethod(this, method));
                } catch (IllegalArgumentException e) {
                    logger.error("getParam()", e);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    logger.error(ex, ex);
                }
            }
        }
        return param;
    }

    /**
     * 建表语句
     *
     * @param time
     * @return
     */
    public String buildCreateTableSql(long time) {
        StringBuilder DDL = new StringBuilder();
        DDL.append("CREATE TABLE IF NOT EXISTS `").append(buildTableName(time)).append("` (\n`id` int(11) NOT NULL AUTO_INCREMENT,\n");
        for (MetaData metaData : getMetadata()) {
            DDL.append(metaData).append(",\n");
        }
        DDL.append("PRIMARY KEY (`id`),\n");
        // 添加索引
        for (MetaData metaData : getMetadataIndex()) {
            DDL.append("KEY ").append(metaData).append(",\n");
        }
        //删除最后一个逗号
        DDL.deleteCharAt(DDL.length() - 2);

        DDL.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ");

        return DDL.toString();
    }

    /**
     * 表名
     *
     * @return
     */
    private String getTableName() {
        return getClass().getSimpleName();
    }

    /**
     * 日志多长时间建一次表
     *
     * @return
     */
    public abstract TableCheckStepEnum getRollingStep();

    public HashSet<MetaData> getMetadata() {
        return metadataset.get(getClass());
    }

    public HashSet<MetaData> getMetadataIndex() {
        return metadatasetindex.get(getClass());
    }

    /**
     * 日志产生时间
     *
     * @return
     */
    @Log(logField = "time", fieldType = "bigint", index = "1")
    public long getTimes() {
        return times / 1000;
    }

    public void setTimes(long time) {
        this.times = time;
    }

    public abstract void logToFile();

}
