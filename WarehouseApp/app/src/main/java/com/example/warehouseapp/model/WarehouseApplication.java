package com.example.warehouseapp.model;

import android.app.Application;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.warehouseapp.Constants;
import com.example.warehouseapp.dtos.ShipmentsWrapper;
import com.example.warehouseapp.services.DataService;
import com.example.warehouseapp.services.FileServiceFactory;
import com.example.warehouseapp.services.GsonService;
import com.example.warehouseapp.services.IFileService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WarehouseApplication extends Application implements IMainModel {
    private List<Warehouse> warehouseList = new ArrayList<>();
    private Warehouse selectedWarehouse;
    private DataService dataService = new DataService();
    private GsonService gsonService = new GsonService();

    public WarehouseApplication(){
    }
    @Override
    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    @Override
    public void setSelectedWarehouse(Warehouse warehouse) {
        selectedWarehouse = warehouse;
    }

    @Override
    public Warehouse getSelectedWarehouse() {
        return selectedWarehouse;
    }

    @Override
    public void disableEnableFreight(){
        if (selectedWarehouse.isFreightReceiptEnabled()) {
            selectedWarehouse.disableFreightReceipt();
        } else {
            selectedWarehouse.enableFreightReceipt();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String processInputFile(InputStream is, String path) throws Exception {
        Collection<Shipment> shipmentList = new ArrayList<>();
        String msg = "";

        File f = new File(path);
        String ext = getFileExtension(f.getName());

        /**
         * use FileServiceFactory to decide what service to use to process the incoming file
         */
        FileServiceFactory fileServiceFactory = new FileServiceFactory();
        IFileService fileService = fileServiceFactory.getFileService(ext);
        shipmentList.addAll(fileService.processInputFile(is));
        /**
         *  Create warehouses if they do not exist; add shipments to warehouses;
         *  Duplicate shipments are not allowed;
         */
        for (final Shipment s : shipmentList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                s.setDateAdded();
            }
            Warehouse warehouse;
            if (warehouseList.stream().noneMatch(w -> w.getWarehouseId().equals(s.getWarehouseId()))) {
                warehouse = new Warehouse(s.getWarehouseId(), s.getWarehouseName());
                warehouseList.add(warehouse);
            } else
                warehouse = warehouseList
                        .stream()
                        .filter(w -> w.getWarehouseId().equals(s.getWarehouseId()))
                        .findFirst()
                        .get();

            /**
             * if the freight receipt in the warehouse is disabled, do not add any shipments
             */
            if(!warehouse.isFreightReceiptEnabled()){
                msg += String.format("Freight receipt is disabled for warehouse %s.Shipment %s won't be added.%n",
                        warehouse.getWarehouseId(),
                        s.getShipmentId());
                continue;
            }

            if(warehouse.addShipment(s))
                msg += String.format("Shipment %s has been added to warehouse %s.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
            else
                msg += String.format(
                        "Duplicate shipment ID: %s for warehouse: %s. Shipment won't be added.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
        }

        return  String.format("File %s has been imported.\n%s", path, msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String exportToJson() {
        String location = Constants.OUTPUT_DIRECTORY;
        String fileString = MessageFormat.format("{0}/{1}_{2}.json",
                location,
                getSelectedWarehouse().getWarehouseId(),
                new Date().getTime());
        return gsonService.exportShipmentsToJsonFile(location, fileString, new ShipmentsWrapper(getSelectedWarehouse().getShipments()));
    }

    @Override
    public void saveCurrentState() {
        dataService.saveCurrentState(warehouseList, Constants.DATA_DIRECTORY);
    }

    @Override
    public void retrieveCurrentState() {
        this.warehouseList.addAll(dataService.retrieveCurrentState(Constants.DATA_DIRECTORY));
    }

    public String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
