package com.example.warehouseapp.model;

import java.util.ArrayList;
import java.util.Collection;

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
        shipments.remove(shipment);
    }

    @Override
    public Collection<Shipment> getShipments() {
        return warehouse.getShipments();
    }
}
