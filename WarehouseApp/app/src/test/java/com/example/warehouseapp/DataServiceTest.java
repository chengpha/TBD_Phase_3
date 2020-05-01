package com.example.warehouseapp;

import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.services.DataService;

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.junit.Assert.*;

public class DataServiceTest {

    private DataService dataService;
    private String directoryPath;
    private List<Warehouse> warehouseList;
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
        dataService = new DataService();
        //Arrange some data
        warehouseList = new ArrayList<Warehouse>() {
            {
                add(new Warehouse("12513", "Warehouse 12513"));
                add(new Warehouse("15566", "Warehouse 15566"));
            }
        };
    }

    /**
     *   Test the ability of DataService to save and retrieve the warehouse data
     */
    @Test
    public void saveAndRetrieveCurrentState_Test() {
        // Act: save the contents of the array first, then retrieve it.
        dataService.saveCurrentState(warehouseList, directoryPath);
        List<Warehouse> stu = dataService.retrieveCurrentState(directoryPath);

        // Assert the results
        assertNotNull(stu);
        assertEquals(warehouseList.size(), stu.size());
        assertEquals("12513", stu.get(0).getWarehouseId());
        assertEquals("15566", stu.get(1).getWarehouseId());
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