/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.db.bean;

/**
 *
 * @author Administrator
 */
public class ConfigDbBean
{

    private int serverId;//服务器ID
    private String db_game_url;
    private String db_game_data_url;
    private String db_log_info_url;
    private String serverIdList;
    private String openTime;
    private String heartHttp_host;
    private String errorlog_http;
    private String privatekey;
    private int serverType;
    private String name;
    private int port;
    private String platfrom;
    private int lsId;
    private String db_game_username;
    private String db_game_password;
    private String db_game_data_username;
    private String db_game_data_password;
    private String lang;
    private String db_public_data_url;
    private int backgrand_port;
    private String rechargeUrl;//充值验证地址
    private String publicIp;//公共服的连接IP
    private int publicPort;//公共服的连接端口
    private String gameServerIp;//游戏服对外的IP
    private int showServerId;//对玩家显示的游戏服ID
    private String db_public_data_username;//公共数据库连接用户名
    private String db_public_data_password;//公共数据库连接密码
    private int IsHave843;//是否支持843的监听
    
    private String funcellArea = "China";
    private String funcellchannelID = "null";
    private int funcellGameId = 187;
    private String loginHttpUrl="";//登录公告人数的接口

    public int getServerId()
    {
        return serverId;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public String getDb_game_url()
    {
        return db_game_url;
    }

    public void setDb_game_url(String db_game_url)
    {
        this.db_game_url = db_game_url;
    }

    public String getDb_game_data_url()
    {
        return db_game_data_url;
    }

    public void setDb_game_data_url(String db_game_data_url)
    {
        this.db_game_data_url = db_game_data_url;
    }

    public String getDb_log_info_url()
    {
        return db_log_info_url;
    }

    public void setDb_log_info_url(String db_log_info_url)
    {
        this.db_log_info_url = db_log_info_url;
    }

    public String getServerIdList()
    {
        return serverIdList;
    }

    public void setServerIdList(String serverIdList)
    {
        this.serverIdList = serverIdList;
    }

    public String getOpenTime()
    {
        return openTime;
    }

    public void setOpenTime(String openTime)
    {
        this.openTime = openTime;
    }

    public String getHeartHttp_host()
    {
        return heartHttp_host;
    }

    public void setHeartHttp_host(String heartHttp_host)
    {
        this.heartHttp_host = heartHttp_host;
    }

    public String getErrorlog_http()
    {
        return errorlog_http;
    }

    public void setErrorlog_http(String errorlog_http)
    {
        this.errorlog_http = errorlog_http;
    }

    public String getPrivatekey()
    {
        return privatekey;
    }

    public void setPrivatekey(String privatekey)
    {
        this.privatekey = privatekey;
    }

    public int getServerType()
    {
        return serverType;
    }

    public void setServerType(int serverType)
    {
        this.serverType = serverType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getPlatfrom()
    {
        return platfrom;
    }

    public void setPlatfrom(String platfrom)
    {
        this.platfrom = platfrom;
    }

    public int getLsId()
    {
        return lsId;
    }

    public void setLsId(int lsId)
    {
        this.lsId = lsId;
    }

    public String getDb_game_username()
    {
        return db_game_username;
    }

    public void setDb_game_username(String db_game_username)
    {
        this.db_game_username = db_game_username;
    }

    public String getDb_game_password()
    {
        return db_game_password;
    }

    public void setDb_game_password(String db_game_password)
    {
        this.db_game_password = db_game_password;
    }

    public String getDb_game_data_username()
    {
        return db_game_data_username;
    }

    public void setDb_game_data_username(String db_game_data_username)
    {
        this.db_game_data_username = db_game_data_username;
    }

    public String getDb_game_data_password()
    {
        return db_game_data_password;
    }

    public void setDb_game_data_password(String db_game_data_password)
    {
        this.db_game_data_password = db_game_data_password;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getDb_public_data_url()
    {
        return db_public_data_url;
    }

    public void setDb_public_data_url(String db_public_data_url)
    {
        this.db_public_data_url = db_public_data_url;
    }
    
    public String getRechargeUrl()
    {
        return rechargeUrl;
    }

    public void setRechargeUrl(String rechargeUrl)
    {
        this.rechargeUrl = rechargeUrl;
    }

    public String getPublicIp()
    {
        return publicIp;
    }

    public void setPublicIp(String publicIp)
    {
        this.publicIp = publicIp;
    }

    public int getPublicPort()
    {
        return publicPort;
    }

    public void setPublicPort(int publicPort)
    {
        this.publicPort = publicPort;
    }

    public String getGameServerIp()
    {
        return gameServerIp;
    }

    public void setGameServerIp(String gameServerIp)
    {
        this.gameServerIp = gameServerIp;
    }

    public int getShowServerId()
    {
        return showServerId;
    }

    public void setShowServerId(int showServerId)
    {
        this.showServerId = showServerId;
    }

    public String getDb_public_data_username()
    {
        return db_public_data_username;
    }

    public void setDb_public_data_username(String db_public_data_username)
    {
        this.db_public_data_username = db_public_data_username;
    }

    public String getDb_public_data_password()
    {
        return db_public_data_password;
    }

    public void setDb_public_data_password(String db_public_data_password)
    {
        this.db_public_data_password = db_public_data_password;
    }

    public int getBackgrand_port()
    {
        return backgrand_port;
    }

    public void setBackgrand_port(int backgrand_port)
    {
        this.backgrand_port = backgrand_port;
    }

    public int getIsHave843()
    {
        return IsHave843;
    }

    public void setIsHave843(int IsHave843)
    {
        this.IsHave843 = IsHave843;
    }

    public String getFuncellArea()
    {
        return funcellArea;
    }

    public void setFuncellArea(String funcellArea)
    {
        this.funcellArea = funcellArea;
    }

    public String getFuncellchannelID()
    {
        return funcellchannelID;
    }

    public void setFuncellchannelID(String funcellchannelID)
    {
        this.funcellchannelID = funcellchannelID;
    }

    public int getFuncellGameId()
    {
        return funcellGameId;
    }

    public void setFuncellGameId(int funcellGameId)
    {
        this.funcellGameId = funcellGameId;
    }

    public String getLoginHttpUrl()
    {
        return loginHttpUrl;
    }

    public void setLoginHttpUrl(String loginHttpUrl)
    {
        this.loginHttpUrl = loginHttpUrl;
    }
 
}
