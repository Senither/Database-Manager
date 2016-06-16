package com.sendev.databasemanager.contracts;

import com.sendev.databasemanager.DatabaseManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DatabaseOutput
{
    private Logger logger = null;
    protected DatabaseManager dbm;

    public void setDatabaseManager(DatabaseManager dbm)
    {
        this.dbm = dbm;

        this.logger = dbm.plugin().getLogger();
    }

    /**
     * Determines if info messages should printed to the console.
     *
     * @return true if info messages should be printed to the console
     */
    public abstract boolean info();

    /**
     * If the logger output is currently enabled for the INFO message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     */
    public void info(String message)
    {
        if (info()) {
            log(Level.INFO, message);
        }
    }

    /**
     * If the logger output is currently enabled for the INFO message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     * @param parms   array of parameters to the message
     */
    public void info(String message, Object... parms)
    {
        if (info()) {
            log(Level.INFO, message, parms);
        }
    }

    /**
     * Determines if warning messages should printed to the console.
     *
     * @return true if warning messages should be printed to the console
     */
    public abstract boolean warning();

    /**
     * If the logger output is currently enabled for the WARNING message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     */
    public void warning(String message)
    {
        if (warning()) {
            log(Level.WARNING, message);
        }
    }

    /**
     * If the logger output is currently enabled for the WARNING message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     * @param parms   array of parameters to the message
     */
    public void warning(String message, Object... parms)
    {
        if (warning()) {
            log(Level.WARNING, message, parms);
        }
    }

    /**
     * Determines if error messages should printed to the console.
     *
     * @return true if error messages should be printed to the console
     */
    public abstract boolean error();

    /**
     * If the logger output is currently enabled for the SEVERE message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     */
    public void error(String message)
    {
        if (error()) {
            log(Level.SEVERE, message);
        }
    }

    /**
     * If the logger output is currently enabled for the SEVERE message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message the message to log to the console.
     * @param parms   array of parameters to the message
     */
    public void error(String message, Object... parms)
    {
        if (error()) {
            log(Level.SEVERE, message, parms);
        }
    }

    /**
     * If debugging is enabled in the database options, then the given
     * message is forwarded to all the registered output Handler objects.
     *
     * @see com.sendev.databasemanager.DatabaseOptions
     *
     * @param message the message to log to the console.
     */
    public void debug(String message)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message);
        }
    }

    /**
     * If debugging is enabled in the database options, then the given
     * message is forwarded to all the registered output Handler objects.
     *
     * @see com.sendev.databasemanager.DatabaseOptions
     *
     * @param message the message to log to the console.
     * @param parms   array of parameters to the message
     */
    public void debug(String message, Object... parms)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message, parms);
        }
    }

    /**
     * If the logger output is currently enabled for the EXCEPTION message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message   the message to log to the console.
     * @param exception the exception that are being thrown
     */
    public void exception(String message, Exception exception)
    {
        if (dbm.options().isDebugMessagesEnabled()) {
            logDebug(Level.INFO, message, exception);
        }
    }

    /**
     * If the logger output is currently enabled for the EXCEPTION message level then the
     * given message is forwarded to all the registered output Handler objects.
     *
     * @param message   the message to log to the console.
     * @param exception the exception that are being thrown
     * @param parms     array of parameters to the message
     */
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
