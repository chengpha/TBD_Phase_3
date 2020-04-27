package com.example.warehouseapp.presenters;

import com.example.warehouseapp.model.Shipment;

public interface IDeleteShipmentPresenter {
    void shipmentItemClicked(Shipment shipment);
    void shipmentDeletionConfirmed(Shipment shipment);
}
