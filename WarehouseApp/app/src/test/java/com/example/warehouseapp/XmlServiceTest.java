package com.example.warehouseapp;

import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.services.IFileService;
import com.example.warehouseapp.services.XmlService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlServiceTest {

    private String directoryPath;
    private IFileService xmlService;
    private File directoryFile;

    @Before
    public void initDataServiceTest() {

        // Directory used for test files
        directoryPath = System.getProperty("user.dir") + "/testData/";
        directoryFile = new File(directoryPath);
        if(!directoryFile.exists()) {
            directoryFile.mkdir();
        }
        // The files in the data directory must be removed prior to performing the tests.
        // Otherwise they might interfere with other tests.
        try {
            cleanDirectory(directoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        xmlService = new XmlService();
    }

    /**
     * Tests that an xml file is correctly processed and returns the correct list of shipments.
     * @throws Exception exception thrown when file doesn't contain correct content
     */
    @Test
    public void processInputFileWithString() throws Exception {
        List<Shipment> shipments;
        String content = "<Shipments>\n" +
                "\t<Warehouse id=\"485\" name=\"Warehouse 120\"> \n" +
                "\t\t<Shipment type=\"Air\" id=\"15dde\">\n" +
                "\t\t\t<Weight unit=\"kg\">20.6</Weight>\n" +
                "\t\t\t<ReceiptDate>1732239329</ReceiptDate>\n" +
                "\t\t</Shipment>\n" +
                "\t\t<Shipment type=\"Truck\" id=\"52523\">\n" +
                "\t\t\t<Weight unit=\"kg\">73</Weight>\n" +
                "\t\t\t<ReceiptDate>1732239329</ReceiptDate>\n" +
                "\t\t</Shipment>\n" +
                "\t</Warehouse>" +
                "</Shipments>";
        String file = directoryPath + "test.xml";
        try{
            FileUtils.writeStringToFile(new File(file), content, StandardCharsets.UTF_8);
        } catch(IOException e) {
            e.printStackTrace();
        }

        shipments = (List<Shipment>)xmlService.processInputFile(file);

        assertNotNull(shipments);
        assertEquals(2, shipments.size());
        assertEquals("15dde", shipments.get(0).getShipmentId());
        assertEquals("52523", shipments.get(1).getShipmentId());
    }

    /**
     * Tests that an xml file is correctly processed and returns the correct list of shipments.
     * @throws Exception exception thrown when file doesn't contain correct content
     */
    @Test
    public void processInputFileWithInputStream() throws Exception {
        List<Shipment> shipments;
        String content = "<Shipments>\n" +
                "\t<Warehouse id=\"485\" name=\"Warehouse 120\"> \n" +
                "\t\t<Shipment type=\"Air\" id=\"15dde\">\n" +
                "\t\t\t<Weight unit=\"kg\">20.6</Weight>\n" +
                "\t\t\t<ReceiptDate>1732239329</ReceiptDate>\n" +
                "\t\t</Shipment>\n" +
                "\t\t<Shipment type=\"Truck\" id=\"52523\">\n" +
                "\t\t\t<Weight unit=\"kg\">73</Weight>\n" +
                "\t\t\t<ReceiptDate>1732239329</ReceiptDate>\n" +
                "\t\t</Shipment>\n" +
                "\t</Warehouse>" +
                "</Shipments>";
        String filePath = directoryPath + "test.xml";
        try{
            FileUtils.writeStringToFile(new File(filePath), content, StandardCharsets.UTF_8);
        } catch(IOException e) {
            e.printStackTrace();
        }
        File newFile = new File(filePath);
        InputStream inputStream = openInputStream(newFile);

        shipments = (List<Shipment>)xmlService.processInputFile(inputStream);

        assertNotNull(shipments);
        assertEquals(2, shipments.size());
        assertEquals("15dde", shipments.get(0).getShipmentId());
        assertEquals("52523", shipments.get(1).getShipmentId());
    }

    /**
     * Clean the files up after the test.
     */
    @After
    public void FileCleanUp() {
        try {
            cleanDirectory(directoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}