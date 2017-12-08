package com.xxibuttons.test.goqr;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestGoQRCreateQR {

    private static final String DEFAULT_DATA = "This is testing data";
    private static final Logger logger = Logger.getLogger("TESTING");

    @BeforeClass
    public void setUp() throws Exception {
        logger.addHandler(new FileHandler("TestingLogs.txt"));
        logger.setLevel(Level.ALL);
    }

    @DataProvider
    public static Object[][] listOfBGColors() {
        return new Object[][]{
                {"255-0-0", "SAMPLE_BGRED.png"},
                {"f00", "SAMPLE_BGRED.png"},
                {"FF0000", "SAMPLE_BGRED.png"},
                {"556B2F", "SAMPLE_BGOLIVE.png"}
        };
    }

    @DataProvider
    public static Object[][] listOfFGColors() {
        return new Object[][]{
                {"255-0-0", "SAMPLE_FGRED.png"},
                {"f00", "SAMPLE_FGRED.png"},
                {"FF0000", "SAMPLE_FGRED.png"},
                {"556B2F", "SAMPLE_FGOLIVE.png"}
        };
    }

    // use this data provider for regression testing and suspicious combinations
    @DataProvider
    public static Object[][] parameterTable() {
        return new Object[][] {
                {"this is a sentence", 240, "png", "ISO-8859-1", "ISO-8859-1", "f00", "", 3, "M" },
                {"this is a sentence", 280, "jpg", "UTF-8", "ISO-8859-1", "", "f00", 3, "Q" },
                //{"this is a sentence", 280, "jpg", "UTF-8", "ISO-8859-1", "f00", "f00", 3, "Q" }, // cannot be read, because is red over red
        };
    }

    @DataProvider
    public static Object[][] dataToSendVariations() {
        return new Object[][]{
                {"HelloWorld"},
                {"Hello World"},
                {"https://goolge.com/"},
                {"S/N:23154353453"},
                {"{\"json_data\": {\"x\":3, \"name\":\"Ferran\"}"}
        };
    }

    @DataProvider
    public static Object[][] listOfSizes() {
        return new Object[][]{
                {10, "png"},
                {100, "gif"},
                {200, "png"},
                {200, "jpeg"},
                {1000, "jpg"},
                //{5000, 5000, "svg"}, //Buffered image doesn't work correctly with SVG, always returns null pointer
                //{5000, 5000, "eps"}  //Buffered image doesn't work correctly with EPS, always returns null pointer
        };
    }

    /**
     * Test default values for simple calls.
     * Note: Seems to be an error in API documentation. Default size is 250x250 and not
     * 200x200 as documentation says.
     * @param data The data to send
     * @throws Exception If something is wrong
     */
    @Test(dataProvider = "dataToSendVariations")
    public void testDefaultHttpCallReturnsQRCode(String data) throws Exception {
        GoQRRESTRequestHelper requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                data
        );
        BufferedImage bi = requestHelper.createQRCode();
        Assert.assertNotNull(bi, "Image returned is null");
        Assert.assertEquals(bi.getWidth(), 250, "Default width must be 250");
        Assert.assertEquals(bi.getHeight(), 250, "Default height must be 250");
        Assert.assertTrue(new GoQrDatacomparatorHelper().compareDataContained(requestHelper.getFinal_url(),data));
    }

    /**
     * Test QR with different sizes
     * @param qrSize Size of QR side
     * @param format Format of image generated
     * @throws Exception
     */
    @Test(dataProvider = "listOfSizes")
    public void testQRCanGetDifferentSizes(int qrSize, String format) throws Exception {
        GoQRRESTRequestHelper requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                DEFAULT_DATA
        );
        String size = qrSize + "x" + qrSize;
        requestHelper.addOptionalParam("size", size);
        requestHelper.addOptionalParam("format", format);
        BufferedImage bi = requestHelper.createQRCode();
        Assert.assertEquals(bi.getWidth(), qrSize);
    }

    /**
     * Test QR with FG color
     * @param color The QR foreground color
     * @param fileSample Sample file to compare with
     * @throws Exception
     */
    @Test(dataProvider = "listOfFGColors")
    public void testQRCanGetDifferentFGColors(String color, String fileSample) throws Exception {
        GoQRRESTRequestHelper requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                DEFAULT_DATA
        );
        requestHelper.addOptionalParam("color", color);
        BufferedImage bi = requestHelper.createQRCode();
        fileSample = "src/test/resources/com/xxibuttons/test/goqr/samples/" + fileSample;
        Assert.assertTrue(new GoQrDatacomparatorHelper().bufferedImagesEqual(bi, fileSample));
        Assert.assertTrue(new GoQrDatacomparatorHelper().compareDataContained(requestHelper.getFinal_url(),DEFAULT_DATA));
    }

    /**
     * Test QR with BG color
     * @param color The QR background color
     * @param fileSample Sample file to compare with
     * @throws Exception
     */
    @Test(dataProvider = "listOfBGColors")
    public void testQRCanGetDifferentBGColors(String color, String fileSample) throws Exception {
        GoQRRESTRequestHelper requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                DEFAULT_DATA
        );
        requestHelper.addOptionalParam("bgcolor", color);
        BufferedImage bi = requestHelper.createQRCode();
        fileSample = "src/test/resources/com/xxibuttons/test/goqr/samples/" + fileSample;
        Assert.assertTrue(new GoQrDatacomparatorHelper().bufferedImagesEqual(bi, fileSample));
        Assert.assertTrue(new GoQrDatacomparatorHelper().compareDataContained(requestHelper.getFinal_url(),DEFAULT_DATA));
    }

    /**
     * Used to make combinations of parameters
     * @param data data to test
     * @param size size of QR
     * @param format Format
     * @param src_encoding source encoding
     * @param target_encoding target encoding
     * @param color FG color
     * @param bgcolor BG color
     * @param margin Margin
     * @param ECC Quality
     * @throws Exception
     */
    @Test(dataProvider = "parameterTable")
    public void testCreateQRCombinationOfParameters(
            String data,
            int size,
            String format,
            String src_encoding,
            String target_encoding,
            String color,
            String bgcolor,
            int margin,
            String ECC
    ) throws Exception {
        GoQRRESTRequestHelper requestHelper = new GoQRRESTRequestHelper(
                GoQRRESTRequestHelper.METHOD.GET,
                GoQRRESTRequestHelper.API_COMMAND.CREATE_QR,
                data
        );
        if (size != 0) {
            requestHelper.addOptionalParam("size", size + "x" + size);
        }
        if (!format.isEmpty()) {
            requestHelper.addOptionalParam("format", format);
        }
        if (!src_encoding.isEmpty()) {
            requestHelper.addOptionalParam("charset-source", src_encoding);
        }
        if (!target_encoding.isEmpty()) {
            requestHelper.addOptionalParam("charset-target", target_encoding);
        }
        if (!color.isEmpty()) {
            requestHelper.addOptionalParam("color", color);
        }
        if (!bgcolor.isEmpty()) {
            requestHelper.addOptionalParam("bgcolor", bgcolor);
        }
        if (margin > 0) {
            requestHelper.addOptionalParam("margin", "" + margin);
        }
        if (!ECC.isEmpty()) {
            requestHelper.addOptionalParam("ecc", ECC);
        }
        requestHelper.createQRCode();
        Assert.assertTrue(new GoQrDatacomparatorHelper().compareDataContained(requestHelper.getFinal_url(),data));
    }
}
