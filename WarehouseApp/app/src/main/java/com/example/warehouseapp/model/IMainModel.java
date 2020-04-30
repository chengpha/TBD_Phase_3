package com.example.warehouseapp.model;
import java.io.InputStream;
import java.util.List;

public interface IMainModel {
    List<Warehouse> getWarehouseList();
    void setSelectedWarehouse(Warehouse warehouse);
    Warehouse getSelectedWarehouse();
    void disableEnableFreight();
    String processInputFile(InputStream is, String path) throws Exception;
    String exportToJson();
    void saveCurrentState();
    void retrieveCurrentState();
}
