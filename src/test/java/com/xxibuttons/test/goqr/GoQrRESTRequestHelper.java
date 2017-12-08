package com.xxibuttons.test.goqr;


import com.xxibuttons.test.utils.RESTRequestHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helps test purgomalum API
 */
class GoQrRESTRequestHelper extends RESTRequestHelper {

    private static final String DEFAULT_SCHEME = "https";
    private static final String BASE_URL = "api.qrserver.com";
    private static final String API_VERSION = "v1";
    private String data = "";
    private Map<String, String> optionalParameters;

    GoQrRESTRequestHelper() {
        super(BASE_URL + "/" + API_VERSION + "/");
        optionalParameters = new HashMap<>();
    }

    public void setData(String data) {
        this.data = "data=" + data;
    }

    public String readQRCode(String url) throws UnsupportedEncodingException {
        url = URLEncoder.encode(url, "UTF-8");
        return makeRequestURIAndProcess(SCHEME.HTTPS, METHOD.GET, "read-qr-code", url);
    }
    public String createQRCode(){
        return makeRequestURIAndProcess(SCHEME.HTTPS, METHOD.GET, "create-qr-code", data);
    }
}
