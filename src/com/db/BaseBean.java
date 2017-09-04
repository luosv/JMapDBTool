package com.db;

public class BaseBean
{
    // 条件
    private transient Object where;

    private long dealTime;

    public Object getWhere()
    {
        return where;
    }

    public void setWhere(Object where)
    {
        this.where = where;
    }

    public long getDealTime()
    {
        return dealTime;
    }

    public void setDealTime(long dealTime)
    {
        this.dealTime = dealTime;
    }

}
