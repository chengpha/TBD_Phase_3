package com.example.warehouseapp.views;
import com.example.warehouseapp.model.Warehouse;

public interface IMainView {
    void showFileProcessed(String msg);
    void showAddShipment();
    void showDeleteShipment();
    void showDisableEnableFreight();
    void enableDisableControlsOnFreightReceiptChange(Warehouse warehouse);
    void setSelectedWarehouse(Warehouse warehouse);
    void showExportToJson(String fileName);
    void showDisplayAllShipments();
}
