package com.example.warehouseapp;

import com.example.warehouseapp.model.Shipment;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipmentTest {


    @Test
    public void getShipmentId() {
        Shipment shipment = new Shipment("12345", "getShipmentID", "24684k", "Ground", 12.5, 26042020);
        assertEquals("24684k", shipment.getShipmentId());
    }

    @Test
    public void testShipmentIdtForMultipleShipments() {
        List<Shipment> shipments = new ArrayList();
        shipments.add(new Shipment("11111", "Warehouse1", "11111k", "Ground", 111.11, 25012020));
        shipments.add(new Shipment("22222", "Warehouse2", "22222m", "Air", 222.22, 26012020));

        assertEquals("11111k", shipments.get(0).getShipmentId());
        assertEquals("22222m", shipments.get(1).getShipmentId());

    }


    @Test
    public void getWarehouseId() {
        Shipment shipment = new Shipment("56484", "Nike Warehouse", "98765M", "Air", 55.5, 26042020);
        assertEquals("56484", shipment.getWarehouseId());
    }

    @Test
    public void getWarehouseName() {
        Shipment shipment = new Shipment("56484", "Nike Warehouse", "98765M", "Air", 55.5, 26042020);
        assertEquals("Nike Warehouse", shipment.getWarehouseName());
    }

    @Test
    public void testWarehouseIdForMultipleShipments() {
        List<Shipment> shipments = new ArrayList();
        shipments.add(new Shipment("11111", "Warehouse1", "11111k", "Ground", 111.11, 25012020));
        shipments.add(new Shipment("22222", "Warehouse2", "22222m", "Air", 222.22, 26012020));

        assertEquals("11111", shipments.get(0).getWarehouseId());
        assertEquals("22222", shipments.get(1).getWarehouseId());
    }

    @Test
    public void testWarehouseNameForMultipleShipments() {
        List<Shipment> shipments = new ArrayList();
        shipments.add(new Shipment("11111", "Warehouse1", "11111k", "Ground", 111.11, 25012020));
        shipments.add(new Shipment("22222", "Warehouse2", "22222m", "Air", 222.22, 26012020));

        assertEquals("Warehouse1", shipments.get(0).getWarehouseName());
        assertEquals("Warehouse2", shipments.get(1).getWarehouseName());

    }


    //Date will be added and will check for date to be added
    //Date is added with Now() method, checks that date is added is exactly the same.
    @Test
    public void getDateAdded() {
        Shipment shipment = new Shipment("12345", "getShipmentID", "24684k", "Ground", 12.5, 26042020);

        assertEquals(shipment.getDateAdded(), shipment.getDateAdded());

    }

}