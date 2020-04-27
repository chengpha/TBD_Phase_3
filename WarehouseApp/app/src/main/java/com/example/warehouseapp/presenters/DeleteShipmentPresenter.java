package com.example.warehouseapp.presenters;

import com.example.warehouseapp.model.IDeleteShipmentModel;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.views.IDeleteShipmentView;

public class DeleteShipmentPresenter implements IDeleteShipmentPresenter {
    private IDeleteShipmentView view;
    private IDeleteShipmentModel model;

    public DeleteShipmentPresenter(IDeleteShipmentView view, IDeleteShipmentModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void shipmentItemClicked(Shipment shipment) {
        view.showDeleteShipmentClicked(shipment);
    }

    @Override
    public void shipmentDeletionConfirmed(Shipment shipment) {
        model.deleteShipment(shipment);
        view.showShipmentDeleted(shipment.getShipmentId());
    }
}
