package com.xxibuttons.test.utils;

import org.junit.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Logger;


/**
 * Helper to make REST calls
 */
public class RESTRequestHelper {


    private final String BASE_URL;
    private static final Logger logger = Logger.getLogger(RESTRequestHelper.class.getName());
    public RESTRequestHelper(final String baseURL) {
        BASE_URL = baseURL;
    }

    public enum SCHEME {
        HTTP("http"),
        HTTPS("https");

        private String scheme;

        SCHEME(String scheme) {
            this.scheme = scheme;
        }
        public String getValue() {
            return this.scheme;
        }
    }

    public enum METHOD {
        GET("GET"),
        POST("POST");

        private String method;

        METHOD(String method) {
            this.method = method;
        }
        public String getValue() {
            return this.method;
        }
    }

    protected String makeRequestURIAndProcess(SCHEME scheme, METHOD method, String function, String command){
        try {

            String request = scheme.getValue() + "://" + BASE_URL + function + "/?" + command;
            logger.fine("Url: " + request);
            URL url = new URL(request);
            return makeRequestURLAndProcess(url, method);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL");
        }

    }

    /**
     * Opens a connection, and process the response from URLDecoder
     * @param url The Url to call
     * @param method
     * @return The response as String
     */
    private String makeRequestURLAndProcess(URL url, METHOD method){

        try {
            BufferedImage bi = ImageIO.read(url);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestMethod(method.getValue());
//
//            Assert.assertEquals(200, connection.getResponseCode());
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    (connection.getInputStream())));
//
//            String response = br.readLine();
//            connection.disconnect();
//
//            return URLDecoder.decode(response, "UTF-8");
            return bi.toString();
        } catch (IOException ioe) {
            throw new RuntimeException("Something went really bad!");
        }

    }
}
