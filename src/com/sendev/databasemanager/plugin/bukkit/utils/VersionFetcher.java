package com.sendev.databasemanager.plugin.bukkit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VersionFetcher
{
    private static final String VERSION_URL = "http://javadoc.sen-dev.com/DatabaseManager.txt";
    private static final Map<String, String> properties = new HashMap<>();

    private static String VERSION = null;

    static {
        properties.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        properties.put("Cache-Control", "no-cache");
        properties.put("Pragma", "no-cache");
        properties.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        properties.put("Accept-Encoding", "gzip, deflate, sdch");
        properties.put("Accept-Language", "en-US,en;q=0.8,en-GB;q=0.6,da;q=0.4");
    }

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

        return setAndReturnVersion(readConnectionStream(connection.getInputStream()));
    }

    private static HttpURLConnection createConnection() throws IOException
    {
        URL url = new URL(VERSION_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");

        properties.keySet().stream().forEach(( property ) -> {
            con.setRequestProperty(property, properties.get(property));
        });

        return con;
    }

    private static String readConnectionStream(InputStream stream) throws IOException
    {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString().trim();
        }
    }

    private static String setAndReturnVersion(String version)
    {
        VERSION = version;

        return VERSION;
    }
}
