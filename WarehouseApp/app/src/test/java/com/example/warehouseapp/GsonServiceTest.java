package com.example.warehouseapp;

import com.example.warehouseapp.dtos.ShipmentsWrapper;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.services.GsonService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GsonServiceTest {
    private GsonService stu;
    private List<Shipment> shipments;
    private String directoryPath;
    private File directoryFile;

    @Before
    public void initGsonServiceTests(){

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

        stu = new GsonService();
        shipments = new ArrayList<>();
        shipments.add(new Shipment("1111", "Warehouse 120", "id_1111","air", 87, 134234234232L));
        shipments.add(new Shipment("2222", "Warehouse 121", "id_2222", "truck", 87, 1231231123123L));
    }

    /**
     * Tests that an json file is correctly processed and returns the correct list of shipments.
     * @throws Exception exception thrown when file doesn't contain correct content
     */
    @Test
    public void processInputFileWithString() throws Exception {
        // Arrange
        String content = "{\n" +
                "  \"warehouse_contents\": [\n" +
                "    {\n" +
                "      \"warehouse_id\": \"11111\",\n" +
                "      \"shipment_method\": \"air\",\n" +
                "      \"shipment_id\": \"48934j\",\n" +
                "      \"weight\": 84,\n" +
                "      \"receipt_date\": 1515354694451\n" +
                "    },\n" +
                "    {\n" +
                "      \"warehouse_id\": \"22222\",\n" +
                "      \"shipment_method\": \"truck\",\n" +
                "      \"shipment_id\": \"1adf4\",\n" +
                "      \"weight\": 354,\n" +
                "      \"receipt_date\": 1515354694451\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String filePath = directoryPath + "test.json";
        try {
            FileUtils.writeStringToFile(new File(filePath), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        shipments = (List<Shipment>) stu.processInputFile(filePath);

        // Assert
        assertNotNull(shipments);
        assertEquals(2, shipments.size());
        assertEquals("11111", shipments.get(0).getWarehouseId());
        assertEquals("22222", shipments.get(1).getWarehouseId());
    }

    /**
     * Tests that an json file is correctly processed and returns the correct list of shipments.
     * @throws Exception exception thrown when file doesn't contain correct content
     */
    @Test
    public void processInputFileWithInputStream() throws Exception {
        // Arrange
        String content = "{\n" +
                "  \"warehouse_contents\": [\n" +
                "    {\n" +
                "      \"warehouse_id\": \"11111\",\n" +
                "      \"shipment_method\": \"air\",\n" +
                "      \"shipment_id\": \"48934j\",\n" +
                "      \"weight\": 84,\n" +
                "      \"receipt_date\": 1515354694451\n" +
                "    },\n" +
                "    {\n" +
                "      \"warehouse_id\": \"22222\",\n" +
                "      \"shipment_method\": \"truck\",\n" +
                "      \"shipment_id\": \"1adf4\",\n" +
                "      \"weight\": 354,\n" +
                "      \"receipt_date\": 1515354694451\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String filePath = directoryPath + "test.json";
        try {
            FileUtils.writeStringToFile(new File(filePath), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File newFile = new File(filePath);
        InputStream inputStream = openInputStream(newFile);

        // Act
        shipments = (List<Shipment>) stu.processInputFile(inputStream);

        // Assert
        assertNotNull(shipments);
        assertEquals(2, shipments.size());
        assertEquals("11111", shipments.get(0).getWarehouseId());
        assertEquals("22222", shipments.get(1).getWarehouseId());
    }

    /**
     * Test to verify that shipments are correctly exported to a Json file.
     */
    @Test
    public void exportShipmentsToJsonFile() {
        // Arrange
        String location = directoryPath;
        String fileString = MessageFormat.format("{0}/{1}_{2}.json",
                location,
                "12345",
                new Date().getTime());
        File testFile = new File(fileString);

        // Act
        stu.exportShipmentsToJsonFile(location, fileString, new ShipmentsWrapper(shipments));

        // Assert
        assertTrue(testFile.exists());

        // Cleanup
        if(testFile.exists())
            testFile.delete();
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