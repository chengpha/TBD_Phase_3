package com.example.warehouseapp;

import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class DeleteShipmentModelTest extends TestCase {

private ArrayList<Shipment> shipmentsArray;

    public void testAddShipment(){
        Warehouse warehouse = new Warehouse("11111", "Test1");
        shipmentsArray.add(new Shipment("31313", "Warehouse0", "31313t", "Ground", 787.34, 30062021));
        shipmentsArray.add(new Shipment("12345", "Warehouse2", "45678s", "Ground", 203.82, 14062021));

        for (Shipment s : shipmentsArray)
        {
            warehouse.addShipment(s);
        }
        assertEquals("31313t", shipmentsArray.get(0).getShipmentId());
    }

    public void testDeleteShipment() {
        Warehouse warehouse = new Warehouse("11111", "Test1");
        ArrayList<Shipment> shipments = (ArrayList<Shipment>)warehouse.getShipments();
        shipmentsArray.remove(new Shipment("12345", "Warehouse2", "45678s", "Ground", 203.82, 14062021));

        for (Shipment s : shipmentsArray)
        {
            warehouse.deleteShipment(s);
        }
        assertEquals("45678s", shipmentsArray.remove(0).getShipmentId());
    }
}

