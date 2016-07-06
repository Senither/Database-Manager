package com.sendev.databasemanager.plugin.contracts;

import com.sendev.databasemanager.exceptions.InvalidPluginException;
import java.util.logging.Logger;

public interface DatabasePlugin
{

    public void parse(Object plugin) throws InvalidPluginException;

    public String getName();

    public String getMain();

    public Logger getLogger();
}
