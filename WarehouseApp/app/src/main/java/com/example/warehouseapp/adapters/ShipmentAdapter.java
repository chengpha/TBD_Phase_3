package com.example.warehouseapp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.warehouseapp.R;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ShipmentAdapter extends BaseAdapter {
    private Warehouse warehouse;
    private Context context;

    public ShipmentAdapter(Context context, Warehouse warehouse){
        this.context = context;
        this.warehouse = warehouse;
    }
    @Override
    public int getCount() {
        return warehouse.getShipments().size();
    }

    @Override
    public Object getItem(int position) {
        ArrayList<Shipment> shipments = (ArrayList)warehouse.getShipments();
        return shipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.shipment_list_view, null);
        ArrayList<Shipment> shipments = (ArrayList)warehouse.getShipments();
        
        TextView shipment_id = view.findViewById(R.id.shipment_id);
        TextView shipment_method = view.findViewById(R.id.shipment_method);
        TextView weight = view.findViewById(R.id.weight);
        TextView receipt_date = view.findViewById(R.id.receipt_date);
        TextView date_added = view.findViewById(R.id.date_added);
        
        Shipment shipment = shipments.get(position);

        shipment_id.setText(shipment.getShipmentId());
        shipment_method.setText(shipment.getShipmentMethod());
        weight.setText(new Double(shipment.getWeight()).toString());
        receipt_date.setText(new Long(shipment.getReceiptDate()).toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        date_added.setText(formatter.format(shipment.getDateAdded()));

        return view;
    }
}
