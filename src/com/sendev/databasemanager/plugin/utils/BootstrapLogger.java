package com.sendev.databasemanager.plugin.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BootstrapLogger
{
    public static void logBootMessageTo(Logger logger, String version)
    {
        String latestVersion = version;

        try {
            latestVersion = VersionFetcher.fetch();
        } catch (IOException ex) {
            logger.info("Failed to make a version check with SenDevelopment, the site might be down.");
        }

        logger.log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        logger.log(Level.INFO, "Plugin: Database Manager v{0}", version);
        logger.log(Level.INFO, "Author: Alexis Tan (Senither) ");
        logger.log(Level.INFO, "Site: http://sen-dev.com/");
        logger.log(Level.INFO, "Wiki: https://bitbucket.org/Senither/database-manager");

        if (!version.equals(latestVersion)) {
            logger.log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            logger.log(Level.INFO, "There is a new version of DBM avaliable!");
            logger.log(Level.INFO, "Version avaliable: v{0}", latestVersion);
            logger.log(Level.INFO, "Current version:   v{0}", version);
        }

        logger.log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }
}
