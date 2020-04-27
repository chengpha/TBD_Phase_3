package com.example.warehouseapp.views;
import com.example.warehouseapp.model.Shipment;

public interface IDeleteShipmentView {
    void showDeleteShipmentClicked(Shipment shipment);
    void showShipmentDeleted(String shipmentId);
}
