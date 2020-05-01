package com.example.warehouseapp;

import com.example.warehouseapp.model.DeleteShipmentModel;
import com.example.warehouseapp.model.IDeleteShipmentModel;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class DeleteShipmentModelTest extends TestCase {
    @Test
    public void testDeleteOneShipment() {
        Warehouse warehouse = new Warehouse("11111", "Test1");
        Shipment shipment1 = new Shipment("11111", "Test1", "45678s", "Ground", 203.82, 14062021);
        Shipment shipment2 = new Shipment("11111", "Test1", "12345s", "Ground", 203.82, 14062021);
        warehouse.addShipment(shipment1);
        warehouse.addShipment(shipment2);
        IDeleteShipmentModel shipmentModel = new DeleteShipmentModel(warehouse);

        shipmentModel.deleteShipment(((ArrayList<Shipment>) shipmentModel.getShipments()).get(1));

        // Since they are reference types, both assertions should be true.
        assertEquals(1, shipmentModel.getShipments().size());
        assertEquals(1, warehouse.getShipments().size());
        assertEquals("45678s", ((ArrayList<Shipment>) shipmentModel.getShipments()).get(0).getShipmentId());
        assertEquals("45678s", ((ArrayList<Shipment>) warehouse.getShipments()).get(0).getShipmentId());
    }

    @Test

    public void testDeleteAllShipments() {
        Warehouse warehouse = new Warehouse("11111", "Test1");
        Shipment shipment1 = new Shipment("11111", "Test1", "45678s", "Ground", 203.82, 14062021);
        Shipment shipment2 = new Shipment("11111", "Test1", "12345s", "Ground", 203.82, 14062021);
        warehouse.addShipment(shipment1);
        warehouse.addShipment(shipment2);
        IDeleteShipmentModel shipmentModel = new DeleteShipmentModel(warehouse);

        for(int i = shipmentModel.getShipments().size() - 1; i >= 0; i--) {
            shipmentModel.deleteShipment(((ArrayList<Shipment>) shipmentModel.getShipments()).get(i));
        }

        // Since they are reference types, both assertions should be true.
        assertEquals(0, shipmentModel.getShipments().size());
        assertEquals(0, warehouse.getShipments().size());

    }
}

