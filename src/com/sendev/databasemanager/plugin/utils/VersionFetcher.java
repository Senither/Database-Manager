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

    public static final String getVersion()
    {
        return VERSION;
    }

    public static final String fetch() throws IOException
    {
        HttpURLConnection connection = createConnection();

        VERSION = readConnectionStream(connection.getInputStream()).trim();

        return VERSION;
    }

    private static HttpURLConnection createConnection() throws IOException
    {
        URL url = new URL(VERSION_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
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
}
