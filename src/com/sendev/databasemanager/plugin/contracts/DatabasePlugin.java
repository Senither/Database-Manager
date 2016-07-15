package com.sendev.databasemanager.plugin.contracts;

import java.util.logging.Logger;

import com.sendev.databasemanager.exceptions.InvalidPluginException;

public interface DatabasePlugin
{

    public void parse(Object plugin) throws InvalidPluginException;

    public String getName();

    public String getMain();

    public Logger getLogger();
}
