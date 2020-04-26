package com.example.warehouseapp.model;


import java.util.List;

public interface IModel {

    List<Warehouse> getWarehouseList();
    void setSelectedWarehouse(Warehouse warehouse);
    Warehouse getSelectedWarehouse();
    void disableEnableFreight();
    String processInputFile(String file) throws Exception;
    String exportToJson();
    void saveCurrentState();
    List<Warehouse> retrieveCurrentState();
    String printShipmentsForWarehouse(Warehouse w);
    String printAllWarehousesWithShipments();
    String warehouseShipmentsString(Warehouse w);
    String prettyJsonFormat(String temp);
}
