package com.example.warehouseapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.warehouseapp.R;
import com.example.warehouseapp.model.Warehouse;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class WarehouseAdapterTest {

    /**
     * Verifies that the correct number of warehouses are returned.
     */
    @Test
    public void getCount() {
        List<Warehouse> warehouses = new ArrayList<Warehouse>();
        warehouses.add(new Warehouse("12345", "abcde"));
        warehouses.add(new Warehouse("67890", "fghij"));
        WarehouseAdapter adapater = new WarehouseAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouses);

        int count = adapater.getCount();

        assertEquals(2, count);
    }

    /**
     * Verifies that the correct warehouse is returned.
     */
    @Test
    public void getItem() {
        List<Warehouse> warehouses = new ArrayList<Warehouse>();
        warehouses.add(new Warehouse("12345", "abcde"));
        warehouses.add(new Warehouse("67890", "fghij"));
        WarehouseAdapter adapater = new WarehouseAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouses);

        Warehouse warehouse = (Warehouse)adapater.getItem(1);

        assertNotNull(warehouse);
        assertEquals("67890", warehouse.getWarehouseId());
        assertEquals("fghij", warehouse.getWarehouseName());
    }

    /**
     * Verifies that the adapter returns views for the warehouses.
     */
    @Test
    public void getView() {
        List<Warehouse> warehouses = new ArrayList<Warehouse>();
        warehouses.add(new Warehouse("12345", "abcde"));
        warehouses.add(new Warehouse("67890", "fghij"));
        WarehouseAdapter adapter = new WarehouseAdapter(InstrumentationRegistry.getInstrumentation().getTargetContext(), warehouses);
        View view = adapter.getView(0, null, null);
        TextView warehouseId = view.findViewById(R.id.warehouse_id);

        assertNotNull(warehouseId);
    }
}