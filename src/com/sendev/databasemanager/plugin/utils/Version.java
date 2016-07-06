package com.sendev.databasemanager.plugin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Version
{
    private static final String VERSION_URL = "http://javadoc.sen-dev.com/DatabaseManager.txt";
    private static final Map<String, String> PROPERTIES = new HashMap<>();
    private static final List<String> VERSIONS = new ArrayList<>();

    private static String VERSION = null;

    static {
        PROPERTIES.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        PROPERTIES.put("Cache-Control", "no-cache");
        PROPERTIES.put("Pragma", "no-cache");
        PROPERTIES.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        PROPERTIES.put("Accept-Encoding", "gzip, deflate, sdch");
        PROPERTIES.put("Accept-Language", "en-US,en;q=0.8,en-GB;q=0.6,da;q=0.4");
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
     * Checks to see if the provided version is the latest public release of DBM.
     *
     * @param version The version to compare with
     *
     * @return <code>TRUE</code> if the provided version the latest release.
     */
    public static boolean isLatestRelease(String version)
    {
        return (VERSION == null) ? false : VERSION.equalsIgnoreCase(version);
    }

    /**
     * Gets the difference between the provided version and the latest release.
     *
     * @param version The version to compare with
     *
     * @return the difference between the provided version and the latest release.
     */
    public static int getVersionDiff(String version)
    {
        if (version == null || VERSIONS.isEmpty()) {
            return -1;
        }

        if (!VERSIONS.contains(version)) {
            return VERSIONS.size();
        }

        for (int diff = 0; diff < VERSIONS.size(); diff++) {
            if (VERSIONS.get(diff).equalsIgnoreCase(version)) {
                return diff;
            }
        }

        return VERSIONS.size();
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

    /**
     * Gets a list of all the versions avaliable in the {@link #VERSIONS} array.
     * <p>
     * <strong>Note:</strong> When this method is called a new copy of the versions array
     * will be created, adding, removing or modifying items in the returned array will
     * not affect the versions array within the class.
     *
     * @return The list of versions in the versions array.
     */
    public static List<String> getVersions()
    {
        return new ArrayList<>(VERSIONS);
    }

    /**
     * Creates a new HTTP connection to the {@link #VERSION_URL}.
     *
     * @return the HTTP connection to the url.
     *
     * @throws IOException if an I/O exception occurs.
     */
    private static HttpURLConnection createConnection() throws IOException
    {
        URL url = new URL(VERSION_URL);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");

        PROPERTIES.keySet().stream().forEach(( property ) -> {
            con.setRequestProperty(property, PROPERTIES.get(property));
        });

        return con;
    }

    /**
     * Reads the connections input stream and writes it to the {@link #VERSIONS} array.
     *
     * @param stream The stream that should be read from
     *
     * @return The list of versions that was read from the input stream.
     *
     * @throws IOException f an I/O exception occurs.
     */
    private static List<String> readConnectionStream(InputStream stream) throws IOException
    {
        VERSIONS.clear();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String line;

            while ((line = in.readLine()) != null) {
                VERSIONS.add(line.trim());
            }

            return VERSIONS;
        }
    }

    /**
     * Sets the first index of the provided array to the latest release and returns tie value.
     *
     * @param versions The list of versions that should be used
     *
     * @return The most recent release of DBM.
     */
    private static String setAndReturnVersion(List<String> versions)
    {
        VERSION = versions.get(0);

        return VERSION;
    }
}
