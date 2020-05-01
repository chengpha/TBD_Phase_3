package com.example.warehouseapp.model;

import java.util.Collection;

public interface IDeleteShipmentModel {
    void deleteShipment(Shipment shipment);
    Collection<Shipment> getShipments();
}
