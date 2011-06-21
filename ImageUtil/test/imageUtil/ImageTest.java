/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imageUtil;

import java.io.File;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author James
 */
public class ImageTest {
    private Image testImage;

    public ImageTest() {        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private Image loadTestImage() {
        if (testImage == null) {
            try {
                testImage = ImageLoader.fromFile("./test/sailboat.jpg");
            }
            catch (IOException ioe) {
                fail("IOException");
            }
        }
        return testImage;
    }

    /**
     * Test of getSourceType method, of class Image.
     */
    @Test
    public void testGetSourceType() {
        System.out.println("getSourceType");
        Image instance = loadTestImage();
        ImageType expResult = ImageType.JPG;
        ImageType result = instance.getSourceType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWidth method, of class Image.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        Image instance = loadTestImage();
        int expResult = 600;
        int result = instance.getWidth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeight method, of class Image.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Image instance = loadTestImage();
        int expResult = 450;
        int result = instance.getHeight();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAspectRatio method, of class Image.
     */
    @Test
    public void testGetAspectRatio() {
        System.out.println("getAspectRatio");
        Image instance = loadTestImage();
        double expResult = 1.3333333333;
        double result = instance.getAspectRatio();
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of writeToFile method, of class Image.
     */
    @Test
    public void testWriteToFile_File() throws Exception {
        System.out.println("writeToFile1");
        File file = new File("./test/test");
        Image instance = loadTestImage();
        File expResult = new File("./test/test.jpg");
        File result = instance.writeToFile(file);
        assertEquals(expResult, result);
        removeFile(result);

        System.out.println("writeToFile2");
        file = new File("./test/test.gif");
        expResult = new File("./test/test.gif");
        result = instance.writeToFile(file);
        assertEquals(expResult, result);
        removeFile(result);

        System.out.println("writeToFile3");
        file = new File("./test/test.xyz");
        expResult = new File("./test/test.xyz.jpg");
        result = instance.writeToFile(file);
        assertEquals(expResult, result);
        removeFile(result);
    }

    private void removeFile(File result) {
        if (result != null && result.exists())
            result.delete();
    }

    /**
     * Test of writeToFile method, of class Image.
     */
    /*
    @Test
    public void testWriteToFile_File_String() throws Exception {
        System.out.println("writeToFile");
        File file = null;
        String type = "";
        Image instance = null;
        instance.writeToFile(file, type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

}