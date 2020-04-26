package com.example.warehouseapp;

import android.app.Application;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.warehouseapp.dtos.ShipmentsWrapper;
import com.example.warehouseapp.model.IModel;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.services.DataService;
import com.example.warehouseapp.services.FileServiceFactory;
import com.example.warehouseapp.services.GsonService;
import com.example.warehouseapp.services.IFileService;
import com.example.warehouseapp.services.XmlService;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WarehouseApplication extends Application implements IModel {
    private List<Warehouse> warehouseList;
    private Warehouse selectedWarehouse;
    private DataService dataService;
    private XmlService xmlService;
    private GsonService gsonService;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public WarehouseApplication(){
        this.dataService = new DataService();
        this.xmlService = new XmlService();
        this.gsonService = new GsonService();
        this.warehouseList = retrieveCurrentState();

        /**
         * Uncomment the section below to load some warehouses into the application
         */
/*        Warehouse warehouse0 = new Warehouse("11111", "");
        Warehouse warehouse1 = new Warehouse("22222", "Warehouse N2");
        Warehouse warehouse2 = new Warehouse("33333", "Warehouse N3");
        List<Shipment> shipments = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse0.getWarehouseId(), "Shipment 1", "1234a", "air", 50, 1515354694451L));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse0.getWarehouseId(), "Shipment 2", "1234b", "truck", 155, 1515354694451L));
        }

        for(Shipment s : shipments)
            warehouse0.addShipment(s);
        warehouseList.add(warehouse0);

        shipments = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse1.getWarehouseId(), "Shipment 1", "1234a", "air", 50, 1515354694451L));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse1.getWarehouseId(), "Shipment 2", "1234b", "truck", 155, 1515354694451L));
        }

        for(Shipment s : shipments)
            warehouse1.addShipment(s);
        warehouseList.add(warehouse1);

        shipments = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse2.getWarehouseId(), "Shipment 1", "1234a", "air", 50, 1515354694451L));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shipments.add(new Shipment(warehouse2.getWarehouseId(), "Shipment 2", "1234b", "truck", 155, 1515354694451L));
        }

        for(Shipment s : shipments)
            warehouse2.addShipment(s);
        warehouseList.add(warehouse2);*/
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
    public String processInputFile(String file) throws Exception {
        Collection<Shipment> shipmentList = new ArrayList<>();
        String msg = "";

        File f = new File(file);
        String ext = getFileExtension(f.getName());

        /**
         * use FileServiceFactory to decide what service to use to process the incoming file
         */
        FileServiceFactory fileServiceFactory = new FileServiceFactory();
        IFileService fileService = fileServiceFactory.getFileService(ext);
        shipmentList.addAll(fileService.processInputFile(file));
        /**
         *  Create warehouses if they do not exist; add shipments to warehouses;
         *  Duplicate shipments are not allowed;
         */
        for (final Shipment s : shipmentList) {
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
                msg += String.format("Shipment %s has been added to warehouse %s%n.",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
            else
                msg += String.format(
                        "Duplicate shipment ID: %s for warehouse: %s. Shipment won't be added.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
        }

        return  String.format("%s has been imported.\n%s", file, msg);
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
    public List<Warehouse> retrieveCurrentState() {
        return dataService.retrieveCurrentState(Constants.DATA_DIRECTORY);
    }

    @Override
    public String printShipmentsForWarehouse(Warehouse w) {
        return null;
    }

    @Override
    public String printAllWarehousesWithShipments() {
        return null;
    }

    @Override
    public String warehouseShipmentsString(Warehouse w) {
        return null;
    }

    @Override
    public String prettyJsonFormat(String temp) {
        return null;
    }

    public String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
