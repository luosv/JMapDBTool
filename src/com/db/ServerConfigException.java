/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.db;

/**
 *
 * @author Administrator
 */
public class ServerConfigException extends RuntimeException
{
    public ServerConfigException()
    {
    }

    public ServerConfigException(String message)
    {
        super(message);
    }

    public ServerConfigException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ServerConfigException(Throwable cause)
    {
        super(cause);
    }
}