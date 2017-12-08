package com.xxibuttons.test.goqr;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Helps comparing data contained in QR.
 *
 * If a QR is generated and it contains information (a string, a URL,...), we can send again this url to read-qr-code
 * API command and check if generated image contains this information
 */
public class GoQrDatacomparatorHelper {

    boolean compareDataContained(final String url, final String data) throws IOException {
        GoQRRESTRequestHelper helper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.READ_QR,
                url
        );
        String response = helper.readQRCode();
        JSONObject jsonObject = new JSONArray(response).getJSONObject(0);
        JSONArray a = jsonObject.getJSONArray("symbol");
        String dataReturned = a.getJSONObject(0).getString("data");
        return dataReturned.compareTo(data) == 0;
    }
}
