package com.xxibuttons.test.goqr;

import org.testng.annotations.Test;

public class TestGoQRCreateQR {
    @Test
    public void testSimpleHttpCallReturnsQRCode() throws Exception {
        GoQrRESTRequestHelper goQrRESTRequestHelper = new GoQrRESTRequestHelper();
        goQrRESTRequestHelper.setData("helloWorld");
        String response = goQrRESTRequestHelper.makeRequest();
        int A =0;
    }
}
