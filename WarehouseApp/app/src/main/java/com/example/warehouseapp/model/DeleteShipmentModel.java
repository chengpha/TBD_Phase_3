package com.example.warehouseapp.model;

import java.util.ArrayList;

/**
 * model used to remove individual shipments from warehouses
 */
public class DeleteShipmentModel implements IDeleteShipmentModel {
    private Warehouse warehouse;

    public DeleteShipmentModel(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    @Override
    public void deleteShipment(Shipment shipment) {
        ArrayList<Shipment> shipments = (ArrayList<Shipment>)warehouse.getShipments();
        for (Shipment s : shipments) {
            if(s.getShipmentId().equals(shipment.getShipmentId())){
                shipments.remove(s);
                break;
            }
        }
    }
}
