package com.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

/**
 * Created by luosv on 2017/9/8 0008.
 */
public class OptionConfigure {

    private static final Logger LOGGER = LogManager.getLogger(OptionConfigure.class);

    private static final String DEFAULT_CONFIG_PATH = "config/db.xml";

    private String classname;
    private String url;
    private String username;
    private String password;
    private String database;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public boolean hasConfig() {
        File xmlFile = new File(DEFAULT_CONFIG_PATH);
        return xmlFile.exists();
    }

    public void initFromXml() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(DEFAULT_CONFIG_PATH);
        Document doc;
        try {
            doc = builder.parse(xmlFile);
        } catch (Exception e) {
            LOGGER.error("cannot find config file", e);
            return;
        }
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        Node node = children.item(1);
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "classname":
                    classname = attValue;
                    break;
                case "url":
                    url = attValue;
                    break;
                case "username":
                    username = attValue;
                    break;
                case "password":
                    password = attValue;
                    break;
                case "database":
                    database = attValue;
                    break;
            }
        }
    }

//    public static void main(String[] args) throws ParserConfigurationException {
//        OptionConfigure configure = new OptionConfigure();
//        configure.initFromXml();
//        LOGGER.error(configure.getClassname());
//        LOGGER.error(configure.getUrl());
//        LOGGER.error(configure.getUsername());
//        LOGGER.error(configure.getPassword());
//        LOGGER.error(configure.getDatabase());
//    }

}
