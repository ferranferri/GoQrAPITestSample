package com.xxibuttons.test.goqr;


import com.xxibuttons.test.utils.RESTRequestHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helps test GoQR API
 */
class GoQRRESTRequestHelper extends RESTRequestHelper {

    private static final Logger logger = Logger.getLogger("TESTING");
    private static final String BASE_URL = "api.qrserver.com";
    private static final String API_VERSION = "v1";
    private API_COMMAND api_command;
    private METHOD method;
    private SCHEME scheme;

    private String data;
    private Map<String, String> optionalParameters;

    GoQRRESTRequestHelper(final METHOD method, final API_COMMAND api_command, String data) {
        super();
        this.scheme = SCHEME.HTTP;
        this.data = data;
        this.api_command = api_command;
        this.method = method;
        optionalParameters = new HashMap<>();
    }

    public void addOptionalParam(String paramName, String value) {
        optionalParameters.put(paramName, value);
    }

    enum API_COMMAND {
        CREATE_QR("create-qr-code"),
        READ_QR("read-qr-code");


        private final String commnad;

        API_COMMAND(String command) {
            this.commnad = command;
        }

        public String getValue() {
            return commnad;
        }
    }
    enum SCHEME {
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
    enum METHOD {
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

    public void setScheme(SCHEME scheme) {
        this.scheme = scheme;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String readQRCode() throws IOException {
        method = METHOD.GET;
        api_command = API_COMMAND.READ_QR;
        logger.fine("Read QR from URL: " + getFinal_url());
        return makeRequestURLAndProcess(new URL(getFinal_url()), method.getValue());

    }
    public BufferedImage createQRCode() throws UnsupportedEncodingException, MalformedURLException {
        logger.fine("Create QR from URL: " + getFinal_url());
        return makeRequestURLAndGetImage(new URL(getFinal_url()));
    }

    public String getFinal_url() throws UnsupportedEncodingException {
        return (api_command == API_COMMAND.CREATE_QR?this.urlComposerCreateQR():this.urlComposerReadQR());
    }

    private String urlComposerCreateQR() throws UnsupportedEncodingException {

        String s = scheme.getValue() + "://" + BASE_URL + "/" + API_VERSION + "/" + api_command.getValue() + "/?data=" + URLEncoder.encode(data, "UTF-8");
        StringBuilder builder = new StringBuilder();
        for (String key : optionalParameters.keySet()) {
            builder.append("&").append(key).append("=").append(optionalParameters.get(key));
        }
        return s + builder.toString();
    }

    private String urlComposerReadQR() throws UnsupportedEncodingException {
        return scheme.getValue() + "://" + BASE_URL + "/" + API_VERSION + "/" +
                api_command.getValue() + "/?fileurl=" + URLEncoder.encode(data, "UTF-8") + "&outputformat=json";
    }
}
