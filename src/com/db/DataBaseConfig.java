/**
 * @date 2014/7/27
 * @author ChenLong
 */
package com.db;

import java.util.Properties;

/**
 *
 * @author ChenLong
 */
public class DataBaseConfig
{
    private final String url;
    private final String username;
    private final String password;

    public DataBaseConfig(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        return properties;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
