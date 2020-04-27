package com.example.warehouseapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.warehouseapp.R;
import com.example.warehouseapp.model.Warehouse;

import java.util.List;

public class WarehouseAdapter extends BaseAdapter {
    private List<Warehouse> warehouseList;
    private Context context;

    public WarehouseAdapter(Context context, List<Warehouse> warehouseList){
        this.context = context;
        this.warehouseList = warehouseList;
    }
    @Override
    public int getCount() {
        return warehouseList.size();
    }

    @Override
    public Object getItem(int position) {
        return warehouseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.warehouse_list_view, null);

        TextView warehouse_id = view.findViewById(R.id.warehouse_id);
        warehouse_id.setText(String.format("Shipments [Warehouse: %s]", warehouseList.get(position).toString()));

        ListView shipments = view.findViewById(R.id.shipment_list);
        ShipmentAdapter adapter = new ShipmentAdapter(context, warehouseList.get(position));
        shipments.setAdapter(adapter);

        return view;
    }
}
