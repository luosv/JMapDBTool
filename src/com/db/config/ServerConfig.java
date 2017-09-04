package com.db.config;

import com.db.DataBaseConfig;
import com.db.ServerConfigException;
import com.db.bean.ConfigDbBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ServerConfig配置类
 */
public class ServerConfig {

    private final static Logger logger = LogManager.getLogger(ServerConfig.class);

    private DataBaseConfig loginDBConfig = null; // 登录数据库配置
    private DataBaseConfig gameDBConfig = null; // game数据库配置
    private DataBaseConfig gameDataDBConfig = null; // game_data数据库配置
    private DataBaseConfig publicDBConfig = null; // 激活码数据库配置
    private DataBaseConfig configDBConfig = null; // 运维配置数据库

    private static String verifyUrl = "https://auth.funcell123.com/verify"; //登录验证地址
    private static int httpPort = 8999;                   //登录服务器http端口
    private static int isNeedVerify = 1;                   //是否需要导平台验证,0不需要,1需要
    private static int productId = 185;//产品ＩＤ，在平台定义的值

    private static int serverPort = 8000;
    private static String serverName = "";
    private static String serverPlatform = "";
    private static int serverId = 0;
    private static int LsId = 0;

    private static String logdrivers = "com.mysql.jdbc.Driver";    //数据库连接驱动
    private static String logurl = "jdbc:mysql://192.168.10.146:3306/test?autoReconnect=true&characterEncoding=UTF-8";    //登陆地址
    private static String loguser = "root";   //用户名
    private static String logpassword = "xujian";  //密码
    private static String logvalidationquery = "select 1"; //验证连接是否有效

    private static String openTime = "2015-01-01 00:00:00";//开服时间
    private static String privateKey = "r9h$t!1*63^2rjet6hdj2";

    private static String tokenSecretKey = "";  //解密access_token的秘钥
    private static String httpHeartUrl = "";
    private static String httpParams = "";

    private static int backPort = 7000;

    private static final List<Integer> serverIdList = new ArrayList<>();

    private static String rechargeVerifyUrl = "https://payment.funcell123.com/confirm"; //充值验证地址

    private static String errorLogUrl = "";//错误日志报告地址

    private boolean loaded = false;
    private final static ServerConfig instance;

    private static int serverType = 0;//服务器类型：0表示测试服，1表示正式游戏服，2 登录服 3表示跨服 4战斗服
    private static String langType = "cn";//语言包设置

    private static String publicIp = "";
    private static int publicPort = 0;

    private static int isCrossCountry = 0;//是否支持跨国的战斗服

    private static String gameServerIp = "127.0.0.1";
    private static int showServerId = 0;//用于对玩家显示的服务器ID值

    private static boolean have843 = false;

    private static String funcellArea = "China";
    private static String funcellchannelID = "null";
    private static int funcellGameId = 185;
    private static String loginHttpUrl = "192.168.1.38:9999";

    static {
        instance = new ServerConfig();
    }

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException {
        if (loaded) {
            logger.warn("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "db-login-data":
                        loginDBConfig = getDBConf(child);
                        break;
                    case "db-game":
                        gameDBConfig = getDBConf(child);
                        break;
                    case "config-db":
                        configDBConfig = getDBConf(child);
                        break;
                    case "db-game-data":
                        gameDataDBConfig = getDBConf(child);
                        break;
                    case "db-public-data":
                        publicDBConfig = getDBConf(child);
                        break;
                    case "login-data":
                        setData(child);
                        break;
                    case "db-log-info":
                        setLogData(child);
                        break;
                    case "serverIdList":
                        setServerIdList(child);
                        break;
                    case "openTime":
                        setData(child);
                        break;
                    case "heartHttp":
                        setHttpHeartData(child);
                        break;
                    case "backgrand":
                        setBackgrandPort(child);
                        break;
                    case "privatekey":
                        setData(child);
                        break;
                    case "tokenSecretKey":
                        setData(child);
                        break;
                    case "serverId":
                        setData(child);
                        break;
                    case "recharge":
                        setData(child);
                        break;
                    case "serverType":
                        setData(child);
                        break;
                    case "talkingtada":
                        setData(child);
                        break;
                    case "reyun":
                        setData(child);
                        break;
                    case "serverInfo":
                        setServerInfo(child);
                        break;
                    case "errorlog":
                        setErrorlogURL(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        loaded = true;
    }

    public boolean loadConfigDB(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "config-db":
                        configDBConfig = getDBConf(child);
                        break;
                    case "serverId":
                        if (!setData(child)) {
                            return false;
                        }
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        return true;

    }

    public void setGameServerConfigInfo(ConfigDbBean bean) {

        if (loaded) {
            logger.warn("has loaded ServerConfig,setServerConfigInfo---->ConfigDbBean.getServerId()== [" + bean.getServerId() + "]");
            return;
        }

        //db-game、db-game-data、db-public-data
        gameDBConfig = new DataBaseConfig(url(bean.getDb_game_url()), bean.getDb_game_username(), bean.getDb_game_password());
        String dbName = bean.getDb_game_data_username().length() > 2 ? bean.getDb_game_data_username() : bean.getDb_game_username();
        String dbPass = bean.getDb_game_data_password().length() > 2 ? bean.getDb_game_data_password() : bean.getDb_game_password();
        gameDataDBConfig = new DataBaseConfig(url(bean.getDb_game_data_url()), dbName, dbPass);
        dbName = bean.getDb_public_data_username().length() > 2 ? bean.getDb_public_data_username() : dbName;
        dbPass = bean.getDb_public_data_password().length() > 2 ? bean.getDb_public_data_password() : dbPass;
        publicDBConfig = new DataBaseConfig(url(bean.getDb_public_data_url()), dbName, dbPass);

        //db-log-info
        logurl = url(bean.getDb_log_info_url());
        loguser = bean.getDb_game_username();
        logpassword = bean.getDb_game_password();

        //serverIdList
        setServerIdListByStr(bean.getServerIdList());

        //openTime
        openTime = bean.getOpenTime();

        //heartHttp
        httpHeartUrl = bean.getHeartHttp_host();

        //errorlog
        errorLogUrl = bean.getErrorlog_http();

        //privatekey
        privateKey = bean.getPrivatekey();

        //serverType
        serverType = bean.getServerType();

        //serverInfo
        serverPort = bean.getPort();
        serverName = bean.getName();
        serverId = bean.getServerId();
        serverPlatform = bean.getPlatfrom();
        LsId = bean.getLsId();
        //语言包
        langType = bean.getLang();

        //充值地址
        rechargeVerifyUrl = bean.getRechargeUrl();
        loaded = true;
        publicIp = bean.getPublicIp();
        publicPort = bean.getPublicPort();
        gameServerIp = bean.getGameServerIp();
        showServerId = bean.getShowServerId();
        //backgrand
        backPort = bean.getBackgrand_port();
        have843 = bean.getIsHave843() > 0;
        funcellArea = bean.getFuncellArea();
        funcellGameId = bean.getFuncellGameId();
        loginHttpUrl = bean.getLoginHttpUrl();
    }

    private String url(String uu) {
        return "jdbc:mysql://" + uu + "?autoReconnect=true&characterEncoding=UTF-8";
    }

    private DataBaseConfig getDBConf(Node node) {
        String url = null, username = null, password = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "url":
                    url = attValue;
                    break;
                case "username":
                    username = attValue;
                    break;
                case "password":
                    password = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new ServerConfigException();
        }
        return new DataBaseConfig(url, username, password);
    }

    private boolean setData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "verifyUrl":
                    verifyUrl = attValue;
                    break;
                case "httpPort":
                    httpPort = Integer.parseInt(attValue);
                    break;
                case "isNeedVerify"://是否需要导平台验证,0不需要，1需要
                    isNeedVerify = Integer.parseInt(attValue);
                    break;
                case "productId":
                    productId = Integer.parseInt(attValue);
                    break;
                case "openTime":
                    openTime = attValue;
                    break;
                case "privatekey":
                    privateKey = attValue;
                    break;
                case "sectetKey":
                    tokenSecretKey = attValue;
                    break;
                case "rechargeVerifyUrl":
                    rechargeVerifyUrl = attValue;
                    break;
                case "serverType"://0表示测试服，1表示正式游戏服，2表示跨服
                    serverType = Integer.parseInt(attValue);
                    break;
                case "serverId": {
                    int id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    serverId = id;
                }
                break;
                case "lsId": {
                    int id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    LsId = id;
                }
                break;
                case "isCrossCountry": {
                    int id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    isCrossCountry = id;
                }
                break;
                case "language": {
                    langType = attValue;
                }
                break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        return true;
    }

    private void setLogData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "drivers":
                    logdrivers = attValue;
                    break;
                case "url":
                    logurl = attValue;
                    break;
                case "username":
                    loguser = attValue;
                    break;
                case "password":
                    logpassword = attValue;
                    break;
                case "validationquery":
                    logvalidationquery = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    //设置合过的服务器id列表
    private void setServerIdList(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "serverIdList":
                    setServerIdListByStr(attValue);
                    break;
                default:
                    logger.warn("setServerIdList unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setServerIdListByStr(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            String d = tokenizer.nextToken();
            i = Integer.valueOf(d);//将字符串转换为整型
            serverIdList.add(i);
        }
    }

    //判断是否是正确的服务器id
    public static boolean isRightServerId(int serverId) {
        return serverIdList.contains(serverId);
    }

    public DataBaseConfig getConfigDBConfig() {
//        checkLoaded();
        return configDBConfig;
    }

    public DataBaseConfig getLoginDBConfig() {
        checkLoaded();
        return loginDBConfig;
    }

    public DataBaseConfig getGameDBConfig() {
        checkLoaded();
        return gameDBConfig;
    }

    public DataBaseConfig getGameDataDBConfig() {
        checkLoaded();
        return gameDataDBConfig;
    }

    public DataBaseConfig getPublicDBConfig() {
        checkLoaded();
        return publicDBConfig;
    }

    //获取登录验证地址
    public static String getLoginVerifyUrl() {
        return verifyUrl;
    }

    //获取登录http端口
    public static int getLoginHtttpPort() {
        return httpPort;
    }

    //是否需要登录验证
    public static boolean isNeedVerify() {
        return isNeedVerify == 1;
    }

    //获取驱动
    public static String getLogDrivers() {
        return logdrivers;
    }

    //获取URL
    public static String getLogUrl() {
        return logurl;
    }

    //获取用户名
    public static String getLogUser() {
        return loguser;
    }

    //获取LOG密码
    public static String getLogPassword() {
        return logpassword;
    }

    //获取验证信息
    public static String getLogValidationquery() {
        return logvalidationquery;
    }

    //获取合服过的服务器id列表
    public static List<Integer> getServerIdList() {
        return serverIdList;
    }

    //获取开服时间
    public static String getServerOpenTime() {
        return openTime;
    }

    //获取私密key
    public static String getPrivateKey() {
        return privateKey;
    }

    //获取充值验证地址
    public static String getRechargeVerifyUrl() {
        return rechargeVerifyUrl;
    }

    private void checkLoaded() {
        if (!loaded) {
            throw new ServerConfigException();
        }
    }

    private void setHttpHeartData(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "host":
                    httpHeartUrl = attValue;
                    break;
                case "params":
                    httpParams = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setServerInfo(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName.toLowerCase()) {
                case "port":
                    serverPort = Integer.parseInt(attValue);
                    break;
                case "servername":
                    serverName = attValue;
                    break;
                case "serverid":
                    serverId = Integer.parseInt(attValue);
                    break;
                case "platfrom":
                    serverPlatform = attValue;
                    break;
                case "lsid":
                    LsId = Integer.parseInt(attValue);
                    break;
                case "showServerId":
                    showServerId = Integer.parseInt(attValue);
                    break;
                case "have843":
                    have843 = Integer.parseInt(attValue) > 0;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setBackgrandPort(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "port":
                    backPort = Integer.parseInt(attValue);
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setErrorlogURL(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "http":
                    errorLogUrl = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    public static String GetHttpHeart() {
        return httpHeartUrl;
    }

    public static String GetHttpParams() {
        return httpParams;
    }

    public static int getBackPort() {
        return backPort;
    }

    public static boolean isTestServer() {
        return serverType == 0;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServerPlatform() {
        return serverPlatform;
    }

    public static int getServerId() {
        return serverId;
    }

    public static int getLsId() {
        return LsId;
    }

    public static String getLangType() {
        return langType;
    }

    public static String getErrorLogUrl() {
        return errorLogUrl;
    }

    public static void setVerifyUrl(String verifyUrl) {
        ServerConfig.verifyUrl = verifyUrl;
    }

    public static void setHttpPort(int httpPort) {
        ServerConfig.httpPort = httpPort;
    }

    public static void setIsNeedVerify(int isNeedVerify) {
        ServerConfig.isNeedVerify = isNeedVerify;
    }

    public static void setServerPort(int serverPort) {
        ServerConfig.serverPort = serverPort;
    }

    public static void setServerName(String serverName) {
        ServerConfig.serverName = serverName;
    }

    public static void setServerPlatform(String serverPlatform) {
        ServerConfig.serverPlatform = serverPlatform;
    }

    public static void setServerId(int serverId) {
        ServerConfig.serverId = serverId;
    }

    public static void setLsId(int LsId) {
        ServerConfig.LsId = LsId;
    }

    public static void setLogdrivers(String logdrivers) {
        ServerConfig.logdrivers = logdrivers;
    }

    public static void setLogurl(String logurl) {
        ServerConfig.logurl = logurl;
    }

    public static void setLoguser(String loguser) {
        ServerConfig.loguser = loguser;
    }

    public static void setLogpassword(String logpassword) {
        ServerConfig.logpassword = logpassword;
    }

    public static void setLogvalidationquery(String logvalidationquery) {
        ServerConfig.logvalidationquery = logvalidationquery;
    }

    public static String getTokenSecretKey() {
        return tokenSecretKey;
    }

    public static void setTokenSecretKey(String tokenSecretKey) {
        ServerConfig.tokenSecretKey = tokenSecretKey;
    }

    public static void setOpenTime(String openTime) {
        ServerConfig.openTime = openTime;
    }

    public static void setPrivateKey(String privateKey) {
        ServerConfig.privateKey = privateKey;
    }

    public static void setHttpHeartUrl(String httpHeartUrl) {
        ServerConfig.httpHeartUrl = httpHeartUrl;
    }

    public static void setHttpParams(String httpParams) {
        ServerConfig.httpParams = httpParams;
    }

    public static void setBackPort(int backPort) {
        ServerConfig.backPort = backPort;
    }

    public static void setRechargeVerifyUrl(String rechargeVerifyUrl) {
        ServerConfig.rechargeVerifyUrl = rechargeVerifyUrl;
    }

    public static void setErrorLogUrl(String errorLogUrl) {
        ServerConfig.errorLogUrl = errorLogUrl;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public static void setServerType(int serverType) {
        ServerConfig.serverType = serverType;
    }

    public static int GetServerType() {
        return ServerConfig.serverType;
    }

    public static void setLangType(String langType) {
        ServerConfig.langType = langType;
    }

    public static String getPublicIp() {
        return publicIp;
    }

    public static int getPublicPort() {
        return publicPort;
    }

    public static String getGameServerIp() {
        return gameServerIp;
    }

    public static int getIsCrossCountry() {
        return isCrossCountry;
    }

    public static int getShowServerId() {
        return showServerId;
    }

    public static boolean isHave843() {
        return have843;
    }

    public static String getFuncellArea() {
        return funcellArea;
    }

    public static String getFuncellchannelID() {
        return funcellchannelID;
    }

    public static int getFuncellGameId() {
        return funcellGameId;
    }

    public static void setFuncellArea(String funcellArea) {
        ServerConfig.funcellArea = funcellArea;
    }

    public static void setFuncellGameId(int funcellGameId) {
        ServerConfig.funcellGameId = funcellGameId;
    }

    public static String getLoginHttpUrl() {
        return loginHttpUrl;
    }

    public static int getProductId() {
        return productId;
    }

}
