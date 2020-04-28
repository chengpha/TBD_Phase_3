package com.example.warehouseapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.warehouseapp.R;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShipmentAdapterTest {

    /**
     * Verifies that the adapter returns the correct size/number of shipments.
     */
    @Test
    public void getCount() {
        Warehouse warehouse = new Warehouse("12345", "abcde");
        warehouse.addShipment(new Shipment("12345", "abcde", "12346", "Air", 150, 1234567890));
        ShipmentAdapter adapter = new ShipmentAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouse);

        int count = adapter.getCount();

        assertEquals(1, count);
    }

    /**
     * Verifies that the adapter returns the correct item from the warehouse.
     */
    @Test
    public void getItem() {
        Warehouse warehouse = new Warehouse("12345", "abcde");
        warehouse.addShipment(new Shipment("12345", "abcde", "12346", "Air", 150, 1234567890));
        ShipmentAdapter adapter = new ShipmentAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouse);

        Shipment shipment = (Shipment)adapter.getItem(0);

        assertNotNull(shipment);
        assertEquals("12345", shipment.getWarehouseId());
        assertEquals("12346", shipment.getShipmentId());
        assertEquals("Air", shipment.getShipmentMethod());
    }

    /**
     * Verifies that the adapter returns views for the shipments.
     */
    @Test
    public void getView() {
        Warehouse warehouse = new Warehouse("12345", "abcde");
        warehouse.addShipment(new Shipment("12345", "abcde", "12346", "Air", 150, 1234567890));
        ShipmentAdapter adapter = new ShipmentAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouse);
        View view = adapter.getView(0, null, null);
        TextView shipmentId = view.findViewById(R.id.shipment_id);
        TextView shipmentMethod = view.findViewById(R.id.shipment_method);
        TextView weight = view.findViewById(R.id.weight);
        TextView receiptDate = view.findViewById(R.id.receipt_date);

        assertNotNull(shipmentId);
        assertNotNull(shipmentMethod);
        assertNotNull(weight);
        assertNotNull(receiptDate);
    }
}