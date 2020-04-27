package com.example.warehouseapp.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.warehouseapp.R;
import com.example.warehouseapp.adapters.WarehouseAdapter;
import com.example.warehouseapp.model.Warehouse;

import java.util.List;

public class DisplayAllShipmentsActivity extends AppCompatActivity implements IDisplayAllShipmentView{
    public List<Warehouse> warehouse_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_shipments);

        warehouse_list = (List<Warehouse> ) getIntent().getSerializableExtra("warehouse_list");
        ListView warehouseList = findViewById(R.id.warehouse_list);
        WarehouseAdapter adapter = new WarehouseAdapter(this, warehouse_list);
        warehouseList.setAdapter(adapter);

    }
}
