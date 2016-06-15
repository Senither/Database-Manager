package com.sendev.databasemanager.plugin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionFetcher
{
    private static final String VERSION_URL = "http://javadoc.sen-dev.com/DatabaseManager/version";
    private static final String USER_AGENT = "Mozilla/5.0";

    private static String VERSION = null;

    /**
     * Gets the latest version retrieved by the version fetcher.
     *
     * @return either (1) the latest version retrieved by the version fetcher
     *         or (2) <code>NULL</code> if nothing has been retrieved yet
     */
    public static final String getVersion()
    {
        return VERSION;
    }

    /**
     * Fetches the latest version of DBM.
     *
     * @return the latest version of DBM
     *
     * @throws IOException if the HTTP connection times out, an unknown error
     *                     occurs, or the connection returns an invalid response.
     */
    public static final String fetch() throws IOException
    {
        HttpURLConnection connection = createConnection();

        return setAndReturnVersion(readConnectionStream(connection.getInputStream()).trim());
    }

    private static HttpURLConnection createConnection() throws IOException
    {
        URL url = new URL(VERSION_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        return con;
    }

    private static String readConnectionStream(InputStream stream) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }

    private static String setAndReturnVersion(String version)
    {
        VERSION = version;

        return VERSION;
    }
}
