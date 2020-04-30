package com.example.warehouseapp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.warehouseapp.Constants;
import com.example.warehouseapp.R;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WarehouseAdapter extends BaseAdapter {
    private List<IWarehouseAdapterItem> adapterList;
    private Context context;

    public WarehouseAdapter(Context context, List<Warehouse> warehouseList){
        this.context = context;
        adapterList = new ArrayList<>();
        for (Warehouse w : warehouseList) {
            adapterList.add(w);
            for (Shipment s : w.getShipments()) {
                adapterList.add(s);
            }
        }
    }
    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return adapterList.get(position).objectIdentifier();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(getItemId(position) == Constants.WAREHOUSE_ID){
            view = LayoutInflater.from(context).inflate(R.layout.warehouse_list_view, null);

            TextView warehouse_id = view.findViewById(R.id.warehouse_id);
            warehouse_id.setText(String.format("Shipments [Warehouse: %s]", getItem(position).toString()));
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.shipment_list_view, null);
            TextView shipment_id = view.findViewById(R.id.shipment_id);
            TextView shipment_method = view.findViewById(R.id.shipment_method);
            TextView weight = view.findViewById(R.id.weight);
            TextView receipt_date = view.findViewById(R.id.receipt_date);
            TextView date_added = view.findViewById(R.id.date_added);

            Shipment shipment = (Shipment) getItem(position);

            shipment_id.setText(shipment.getShipmentId());
            shipment_method.setText(shipment.getShipmentMethod());
            weight.setText(new Double(shipment.getWeight()).toString());
            receipt_date.setText(new Long(shipment.getReceiptDate()).toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            date_added.setText(shipment.getDateAdded().format(formatter));
        }
        return view;
    }
}
