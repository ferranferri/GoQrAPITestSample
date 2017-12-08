package com.xxibuttons.test.utils;

import org.junit.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Helper to make REST calls
 */
public class RESTRequestHelper {


    //private final String BASE_URL;
    private static final Logger logger = Logger.getLogger(RESTRequestHelper.class.getName());

    public RESTRequestHelper() {
        logger.setLevel(Level.ALL);
    }


    /**
     * Opens a connection, and process the response from URLDecoder
     * @param url The Url to call
     * @return The response as String
     */
    protected BufferedImage makeRequestURLAndGetImage(URL url){

        try {
            return ImageIO.read(url);
        } catch (IOException ioe) {
            throw new RuntimeException("Something went really bad!");
        }

    }

    protected String makeRequestURLAndProcess(URL url, String method) throws IOException {

        logger.fine("Request url: " + url.toString() );
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            Assert.assertEquals(200, connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String response = br.readLine();
            connection.disconnect();

            return URLDecoder.decode(response, "UTF-8");
        } catch (IOException ioe) {
            throw new RuntimeException("Something went really bad!. Connections returned code " + connection.getResponseCode());
        }

    }
}
