package com.example.warehouseapp;

import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarehouseTest {

    @Test
    public void addShipmentFreightEnabled_Test()
    {
        Warehouse warehouse = new Warehouse("11111", "Test1");
        List<Shipment> shipments = new ArrayList<Shipment>();
        shipments.add(new Shipment(warehouse.getWarehouseId(), "Test1", "1234a", "air", 50, 1515354694451L));
        shipments.add(new Shipment(warehouse.getWarehouseId(), "Test1", "1234b", "truck", 155, 1515354694451L));
        warehouse.enableFreightReceipt();

        for(Shipment s : shipments)
        {
            warehouse.addShipment(s);
        }

        assertEquals(2, warehouse.getShipments().size());
        assertTrue(warehouse.getShipments().contains(shipments.get(0)));
        assertTrue(warehouse.getShipments().contains(shipments.get(1)));
    }

    /**
     *  Test to verify that shipments can't be added with freight receipt disabled
     */

    @Test
    public void addShipmentFreightDisabled_Test()
    {
        Warehouse warehouse = new Warehouse("11111", "Test1");
        List<Shipment> shipments = new ArrayList<Shipment>();
        shipments.add(new Shipment(warehouse.getWarehouseId(), "Test1", "1234a", "air", 50, 1515354694451L));
        shipments.add(new Shipment(warehouse.getWarehouseId(), "Test1", "1234b", "truck", 155, 1515354694451L));
        warehouse.disableFreightReceipt();

        for(Shipment s : shipments)
        {
            warehouse.addShipment(s);
        }

        assertEquals(0, warehouse.getShipments().size());
    }

    @Test
    public void getWarehouseId() {
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");
        assertEquals("12345", warehouse.getWarehouseId());
    }

    @Test
    public void getWarehouseName() {
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");
        assertEquals("Warehouse1", warehouse.getWarehouseName());
    }

    @Test
    public void enableFreightReceipt() {
        //Freight receipt is akready enabled, disable a
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");
        //Warehouse has freight receipt set to true to begin with. Disable first
        warehouse.disableFreightReceipt();
        assertFalse(warehouse.isFreightReceiptEnabled());
        //Re-enable freight receipt
        warehouse.enableFreightReceipt();
        assertTrue(warehouse.isFreightReceiptEnabled());
    }

    @Test
    public void disableFreightReceipt() {
        //Freight receipt is already enabled, disable and check if it's disabled.
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");
        assertTrue(warehouse.isFreightReceiptEnabled());
        warehouse.disableFreightReceipt();
        assertFalse(warehouse.isFreightReceiptEnabled());
    }

    @Test
    public void isFreightReceiptEnabled() {
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");
        //Freight receipt is already enabled. Check if it's true
        assertTrue(warehouse.isFreightReceiptEnabled());

    }

    @Test

    public void getShipments() {

    }

    @Test
    public void testToString() {
        Warehouse warehouse = new Warehouse("12345", "Warehouse1");

        assertEquals("12345", warehouse.toString());
    }
}