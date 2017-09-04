package com.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class BaseDao{

	protected static Logger dblog = LogManager.getLogger("DBLOGCONSUMING");
	
	protected void info(Object info){
		dblog.info(info);
	}

	/**
	 * 获取指定时间到现在的时间数（毫秒）
	 * 
	 * @param time
	 * @return
	 */
	protected long getDurationToNow(long time) {
		return System.currentTimeMillis() - time;
	}
                
	/**
	 * 插入
	 * 
	 * @param bean
         * @param sqlName
	 * @return
	 */
	public abstract int insert(String sqlName, BaseBean bean);

	/**
	 * 更新
	 * 
	 * @param bean
         * @param sqlName
	 * @return
	 */
	public abstract int update(String sqlName, BaseBean bean);

	/**
	 * 删除
	 * 
	 * @param o
         * @param sqlName
	 * @return
	 */
	public abstract int delete(String sqlName, Object o);
        

}
