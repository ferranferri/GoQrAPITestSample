package com.xxibuttons.test.goqr;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Although test doesn't have lots of logic it is better to test utility methods that composes urls, concat strings, etc.
 */
public class TestUtilityClasses {

    private final static String  DEFAULT_DATA = "HELLO_WORLD";

    GoQRRESTRequestHelper requestHelper;

    @BeforeMethod
    public void setUp() throws Exception {
        requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                DEFAULT_DATA);
    }

    @Test
    public void testGoQrRESTRequestHelper() throws Exception {

        Assert.assertEquals(requestHelper.getFinal_url(), "http://api.qrserver.com/v1/create-qr-code/?data=" + DEFAULT_DATA);
    }

    @Test
    public void testGoQrRESTRequestHelperWithHTTPScheme() throws Exception {
        requestHelper.setScheme(GoQRRESTRequestHelper.SCHEME.HTTP);
        Assert.assertEquals(requestHelper.getFinal_url(), "http://api.qrserver.com/v1/create-qr-code/?data=" + DEFAULT_DATA);
    }

    @Test
    public void testGoQrTestHelperWithNotDefaultData() throws Exception {
        requestHelper.setData("different_data");
        Assert.assertEquals(requestHelper.getFinal_url(), "http://api.qrserver.com/v1/create-qr-code/?data=different_data");
    }

    @Test
    public void testGoQrTestHelperWithOptionalParams() throws Exception {
        requestHelper.addOptionalParam("param1", "valueOfParam1");
        Assert.assertEquals(requestHelper.getFinal_url(), "http://api.qrserver.com/v1/create-qr-code/?data=" + DEFAULT_DATA + "&param1=valueOfParam1");
    }

    @Test
    public void testGoQrTestHelperWithMoreOptionalParams() throws Exception {
        requestHelper.addOptionalParam("param1", "valueOfParam1");
        requestHelper.addOptionalParam("param2", "valueOfParam2");
        Assert.assertEquals(
                requestHelper.getFinal_url(),
                "http://api.qrserver.com/v1/create-qr-code/?data=" + DEFAULT_DATA + "&param1=valueOfParam1&param2=valueOfParam2");
    }

    @Test
    public void testReadQRCodeURLBuilding() throws Exception {
        String expected = "http://api.qrserver.com/v1/read-qr-code/?fileurl=https%3A%2F%2Fapi.qrserver.com%2Fv1%2Fcreate-qr-code%2F%3Fdata%3Ddifferent_data&outputformat=json";
        GoQRRESTRequestHelper helper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.READ_QR,
                "https://api.qrserver.com/v1/create-qr-code/?data=different_data"
                );
        Assert.assertEquals(helper.getFinal_url(), expected);
    }

    @Test
    public void testGoQrDatacomparatorHelper() throws Exception {
        String urlCreateQR = "https://api.qrserver.com/v1/create-qr-code/?data=different_data";
        GoQrDatacomparatorHelper comparator = new GoQrDatacomparatorHelper();
        Assert.assertTrue(comparator.compareDataContained(urlCreateQR, "different_data"));
    }
}
