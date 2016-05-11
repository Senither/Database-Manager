package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.DatabaseManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DatabaseOutput
{
    private Logger logger = null;
    protected DatabaseManager dbm;
    private String channelName = "Unknown";

    public void setDatabaseManager(DatabaseManager dbm)
    {
        this.dbm = dbm;

        this.logger = dbm.plugin().getLogger();

        setChannelName(this.dbm.plugin().getDescription().getName());
    }

    private void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public abstract boolean info();

    public void info(String message)
    {
        if (info()) {
            log(Level.INFO, message);
        }
    }

    public void info(String message, Object... parms)
    {
        if (info()) {
            log(Level.INFO, message, parms);
        }
    }

    public abstract boolean warning();

    public void warning(String message)
    {
        if (warning()) {
            log(Level.WARNING, message);
        }
    }

    public void warning(String message, Object... parms)
    {
        if (warning()) {
            log(Level.WARNING, message, parms);
        }
    }

    public abstract boolean error();

    public void error(String message)
    {
        if (error()) {
            log(Level.SEVERE, message);
        }
    }

    public void error(String message, Object... parms)
    {
        if (error()) {
            log(Level.SEVERE, message, parms);
        }
    }

    public void debug(String message)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message);
        }
    }

    public void debug(String message, Object... parms)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message, parms);
        }
    }

    public void exception(String message, Exception exception)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message, exception);
        }
    }

    public void exception(String message, Exception exception, Object... parms)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message, exception, parms);
        }
    }

    private void log(Level level, String message, Object... parms)
    {
        logger.log(level, "[DBM] {0}", String.format(message, parms));
    }

    private void logDebug(Level level, String message, Object... parms)
    {
        logger.log(level, "[DBM::DEBUG] {0}", String.format(message, parms));
    }

    private void logDebug(Level level, String message, Exception exception, Object... parms)
    {
        logger.log(level, "[DBM::DEBUG] {0} {1}", new Object[]{String.format(message, parms), exception});
    }
}
