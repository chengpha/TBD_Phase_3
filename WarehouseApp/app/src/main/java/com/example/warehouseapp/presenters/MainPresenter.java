package com.example.warehouseapp.presenters;

import com.example.warehouseapp.model.IMainModel;
import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.views.IMainView;

import java.util.List;

public class MainPresenter implements IMainPresenter {
    private IMainView view;
    private IMainModel model;

    public MainPresenter(IMainModel model) {
        this.model = model;
    }

    public void setView(IMainView view) {
        this.view = view;
    }

    @Override
    public List<Warehouse> getWarehouseList() {
        return model.getWarehouseList();
    }

    @Override
    public Warehouse getSelectedWarehouse() {
        return model.getSelectedWarehouse();
    }

    @Override
    public void chooseFileClicked() {

    }

    @Override
    public void setSelectedWarehouse(Warehouse warehouse) {
        if(warehouse != null){
            model.setSelectedWarehouse(warehouse);
            view.setSelectedWarehouse(warehouse);
        }
    }

    @Override
    public void disableEnableFreightClicked() {
        if(model.getSelectedWarehouse() == null)
            return;
        model.disableEnableFreight();
        view.enableDisableControlsOnFreightReceiptChange(model.getSelectedWarehouse());
        view.showDisableEnableFreight();
    }

    public void addShipmentClicked(){
        if(model.getSelectedWarehouse() == null)
            return;
        view.showAddShipment();
    }

    @Override
    public void deleteShipmentClicked() {
        if(model.getSelectedWarehouse() == null)
            return;
        view.showDeleteShipment();
    }

    @Override
    public void exportWarehouseShipmentsClicked() {
        if(model.getSelectedWarehouse() == null)
            return;
        view.showExportToJson(model.exportToJson());
    }

    @Override
    public void displayAllShipmentsClicked() {
        if(model.getSelectedWarehouse() == null)
            return;
        view.showDisplayAllShipments();
    }

    @Override
    public void saveCurrentState() {
        model.saveCurrentState();
    }
}
