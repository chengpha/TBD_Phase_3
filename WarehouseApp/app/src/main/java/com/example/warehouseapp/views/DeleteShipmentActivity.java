package com.example.warehouseapp.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.warehouseapp.R;
import com.example.warehouseapp.adapters.ShipmentAdapter;
import com.example.warehouseapp.model.DeleteShipmentModel;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.presenters.DeleteShipmentPresenter;
import com.example.warehouseapp.presenters.IDeleteShipmentPresenter;
import es.dmoral.toasty.Toasty;


public class DeleteShipmentActivity extends AppCompatActivity implements IDeleteShipmentView {

    private  ShipmentAdapter adapter;
    private IDeleteShipmentPresenter deleteShipmentPresenter;
    private Warehouse warehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_shipment);
        warehouse = (Warehouse) getIntent().getSerializableExtra("warehouse");
        deleteShipmentPresenter = new DeleteShipmentPresenter(this, new DeleteShipmentModel(warehouse));
        TextView warehouseId = findViewById(R.id.txtDeleteShipment_WarehouseId);
        warehouseId.setText(String.format("Delete shipments [Warehouse: %s]", warehouse.getWarehouseId()));
        ListView shipmentList = findViewById(R.id.shipment_list);
        adapter = new ShipmentAdapter(this, warehouse);
        shipmentList.setAdapter(adapter);

        shipmentList.setOnItemClickListener((parent, view, position, id) -> {
            deleteShipmentPresenter.shipmentItemClicked((Shipment)shipmentList.getItemAtPosition(position));
        });
    }

    @Override
    public void showDeleteShipmentClicked(Shipment shipment) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format("Are you sure you want to delete Shipment: %s?", shipment.getShipmentId()))
                    .setCancelable(false)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        deleteShipmentPresenter.shipmentDeletionConfirmed(shipment);
                        dialog.cancel();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showShipmentDeleted(String shipmentId) {
        adapter.notifyDataSetChanged();
        Toasty.custom(this, String.format("Shipment %s has been deleted", shipmentId),null, getColor(R.color.colorAccent) ,getColor(R.color.colorBlack)
                ,Toasty.LENGTH_LONG,false,true).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("result", warehouse);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
