package com.example.warehouseapp.presenters;

import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.views.IMainView;

import java.io.InputStream;
import java.util.List;

public interface IMainPresenter {
    void setView(IMainView mainView);
    List<Warehouse> getWarehouseList();
    Warehouse getSelectedWarehouse();
    void chooseFileClicked(InputStream is, String path) throws Exception;
    void setSelectedWarehouse(Warehouse warehouse);
    void disableEnableFreightClicked();
    void addShipmentClicked();
    void deleteShipmentClicked();
    void exportWarehouseShipmentsClicked();
    void displayAllShipmentsClicked();
}
