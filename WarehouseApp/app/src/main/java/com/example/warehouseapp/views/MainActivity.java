package com.example.warehouseapp.views;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.warehouseapp.Constants;
import com.example.warehouseapp.R;
import com.example.warehouseapp.model.IMainModel;
import com.example.warehouseapp.model.WarehouseApplication;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.presenters.IMainPresenter;
import com.example.warehouseapp.presenters.MainPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.stream.Stream;
import es.dmoral.toasty.Toasty;
import static com.example.warehouseapp.R.layout.spinner_drop_down_item;
import static com.example.warehouseapp.R.layout.spinner_item;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements IMainView {
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private final static int FILE_REQUEST_CODE = 1;
    private IMainPresenter mainPresenter;
    private IMainModel app;
    private Button btnDisableEnableFreight;
    private Button btnAddShipment;
    private TextView lblWarehouseId;
    private TextView lblWarehouseName;
    private Button btnDeleteShipment;
    private ArrayAdapter<Warehouse> spinnerAdapter;
    private Intent fileIntent;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
        /**
         * 'data' directory is needed to save the state of the program
         * 'output' directory is needed to save json export files
         */
        verifyRuntimeDirectoriesExist();
        app = (WarehouseApplication)getApplication();
        app.retrieveCurrentState();

        mainPresenter = new MainPresenter(app);
        mainPresenter.setView(this);

        lblWarehouseId = findViewById(R.id.lblWarehouseId);
        lblWarehouseName = findViewById(R.id.lblWarehouseName);

        Spinner spnSelectWarehouse = findViewById(R.id.spnSelectWarehouse);
        spinnerAdapter = new ArrayAdapter<>(this, spinner_item, mainPresenter.getWarehouseList());
        spinnerAdapter.notifyDataSetChanged();
        spinnerAdapter.setDropDownViewResource(spinner_drop_down_item);
        spnSelectWarehouse.setAdapter(spinnerAdapter);
        spnSelectWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainPresenter.setSelectedWarehouse((Warehouse)parent.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        Button btnChooseFile = findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(v -> {
            fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("*/*");
            startActivityForResult(fileIntent, FILE_REQUEST_CODE);
        });

        btnDisableEnableFreight = findViewById(R.id.btnDisableEnableFreight);
        btnDisableEnableFreight.setText("Disable");
        btnDisableEnableFreight.setOnClickListener(v-> mainPresenter.disableEnableFreightClicked());

        btnAddShipment = findViewById(R.id.btnAddShipment);
        btnAddShipment.setOnClickListener(v -> mainPresenter.addShipmentClicked());

        btnDeleteShipment = findViewById(R.id.btnDeleteShipment);
        btnDeleteShipment.setOnClickListener(v -> mainPresenter.deleteShipmentClicked());

        Button btnExportWarehouseShipments = findViewById(R.id.btnExportWarehouseShipments);
        btnExportWarehouseShipments.setOnClickListener(v -> mainPresenter.exportWarehouseShipmentsClicked());

        Button btnDisplayAllShipments = findViewById(R.id.btnDisplayAllShipments);
        btnDisplayAllShipments.setOnClickListener(v -> mainPresenter.displayAllShipmentsClicked());
    }

    @Override
    public void showFileProcessed(String msg) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("File upload report:")
                .setMessage(msg)
                .setNeutralButton("Ok", (dialog, which) -> dialog.cancel())
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showAddShipment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(String.format("Warehouse ID: %s", mainPresenter.getSelectedWarehouse().getWarehouseId()));
        View view = getLayoutInflater().inflate(R.layout.add_shipment, null);

        EditText shipmentId = view.findViewById(R.id.txtShipmentId);
        EditText shipmentMethod = view.findViewById(R.id.txtShipmentMethod);
        EditText weight = view.findViewById(R.id.txtWeight);
        EditText receiptDate = view.findViewById(R.id.txtReceiptDate);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddShipment = view.findViewById(R.id.btnAddShipment);

        alert.setView(view);
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        btnAddShipment.setOnClickListener(v -> {
            if(validateAndAddShipment(
                     shipmentId.getText().toString()
                    ,shipmentMethod.getText().toString()
                    ,weight.getText().toString()
                    ,receiptDate.getText().toString()))
                alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void showDeleteShipment() {
        Intent intent = new Intent(this, DeleteShipmentActivity.class);
        intent.putExtra("warehouse", mainPresenter.getSelectedWarehouse());
        startActivityForResult(intent, Constants.DELETE_SHIPMENT_CONST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //handle file picking
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {

            String path = data.getData().getPath();
            Uri fileUri = data.getData();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                mainPresenter.chooseFileClicked(is, path);
            } catch (Exception e) {
                Toasty.error(this, String.format("Bad file: %s", e.getMessage()), Toasty.LENGTH_LONG).show();
            }
            finally {
                try {
                    is.close();
                    spinnerAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //handle file deletion
        if (requestCode == Constants.DELETE_SHIPMENT_CONST && resultCode == RESULT_OK) {
            Warehouse warehouse;
            Serializable temp = data.getSerializableExtra("result");
            if (temp != null){
                warehouse = (Warehouse) temp;
                mainPresenter.getWarehouseList().forEach(w-> {
                    if(w.getWarehouseId().equals(warehouse.getWarehouseId())
                            && w.getShipments().size()!= warehouse.getShipments().size()){
                        w.getShipments().clear();
                        w.addShipments(warehouse.getShipments());
                        }
                    });
                }
            }
    }

    @Override
    public void showDisableEnableFreight() {
        String msg = String.format("Freight receipt has been %s for warehouse %s"
                ,!mainPresenter.getSelectedWarehouse().isFreightReceiptEnabled() ? "disabled" : "enabled"
                ,mainPresenter.getSelectedWarehouse().getWarehouseId());
        Button btnDisableEnableFreight = findViewById(R.id.btnDisableEnableFreight);
        btnDisableEnableFreight.setText(mainPresenter.getSelectedWarehouse().isFreightReceiptEnabled()?"Disable":"Enable");
        Toasty.custom(this, msg,null, getColor(R.color.colorAccent) ,getColor(R.color.colorBlack)
                ,Toasty.LENGTH_LONG,false,true).show();
    }

    @Override
    public void showExportToJson(String fileName) {
        String msg;
        if(!fileName.equals("")){
            msg = String.format("JSON extract %s has been generated for warehouse: %s", fileName, mainPresenter.getSelectedWarehouse());
            Toasty.custom(MainActivity.this, msg ,null, getColor(R.color.colorAccent), getColor(R.color.colorBlack)
                    ,Toasty.LENGTH_LONG,false,true).show();
        }else{
            msg = String.format("Failed to export shipments for warehouse: %s. Cannot access output directory.", mainPresenter.getSelectedWarehouse());
            Toasty.error(this, msg, Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void showDisplayAllShipments() {
        Intent intent = new Intent(this, DisplayAllShipmentsActivity.class);
        intent.putExtra("warehouse_list", (Serializable) mainPresenter.getWarehouseList());
        startActivityForResult(intent, Constants.DISPLAY_ALL_SHIPMENTS_CONST);
    }

    /**
     * Performs validation of shipment parameters. Adds shipment to selected warehouse.
     * @param shipmentIdText
     * @param shipmentMethodText
     * @param weightText
     * @param receiptDateText
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateAndAddShipment(String shipmentIdText,
                                           String shipmentMethodText,
                                           String weightText,
                                           String receiptDateText){

        if(shipmentIdText.trim().isEmpty() | shipmentMethodText.trim().isEmpty() | weightText.trim().isEmpty()| receiptDateText.trim().isEmpty()){
            Toasty.error(MainActivity.this, "Fill out all required fields", Toasty.LENGTH_LONG).show();
            return false;
        }
        /**
         * verify the right shipment method
         */
        if (Stream.of("AIR", "RAIL", "SHIP", "TRUCK").noneMatch(s -> shipmentMethodText.toUpperCase().trim().equals(s))
        ) {
            Toasty.error(MainActivity.this, "Shipment Method must be AIR, RAIL, SHIP, or TRUCK", Toasty.LENGTH_LONG).show();
            return false;
        }
        double weightDouble;
        long receiptDateLong;
        try{
            weightDouble = Double.parseDouble(weightText);
        }catch(NumberFormatException e){
            Toasty.error(MainActivity.this, "Weight must be a numeric value", Toasty.LENGTH_LONG).show();
            return false;
        }
        try{
            receiptDateLong = Long.parseLong(receiptDateText);
        } catch(NumberFormatException e){
            Toasty.error(MainActivity.this, "Receipt Date must be a numeric value", Toasty.LENGTH_LONG).show();
            return false;
        }

        /**
         * add shipment to selected warehouse
         */
        Warehouse selectedWarehouse = mainPresenter.getSelectedWarehouse();
        Shipment shipment = new Shipment(selectedWarehouse.getWarehouseId(),
                selectedWarehouse.getWarehouseName(),
                shipmentIdText,
                shipmentMethodText,
                weightDouble,
                receiptDateLong
        );

        shipment.setDateAdded();
        selectedWarehouse.addShipment(shipment);
        String msg = String.format("Shipment %s has been added to warehouse %s"
                ,shipment.getShipmentId()
                ,selectedWarehouse.getWarehouseId());
        Toasty.custom(MainActivity.this, msg ,null, getColor(R.color.colorAccent), getColor(R.color.colorBlack)
                ,Toasty.LENGTH_LONG,false,true).show();
        return true;
    }

    public void enableDisableControlsOnFreightReceiptChange(Warehouse w){
        if(w.isFreightReceiptEnabled()){
            btnDisableEnableFreight.setText("Disable");
            btnAddShipment.setEnabled(true);
            btnDeleteShipment.setEnabled(true);
            btnAddShipment.setText("Add");
            btnDeleteShipment.setText("Delete");
        }
        else{
            btnDisableEnableFreight.setText("Enable");
            btnAddShipment.setEnabled(false);
            btnDeleteShipment.setEnabled(false);
            btnAddShipment.setText("-");
            btnDeleteShipment.setText("-");
        }
    }

    /**
     * Updates the labels with the selected warehouse's name and ID
     * @param warehouse
     */
    @Override
    public void setSelectedWarehouse(Warehouse warehouse) {
        enableDisableControlsOnFreightReceiptChange(warehouse);

        lblWarehouseId.setText( mainPresenter.getSelectedWarehouse().toString());
        lblWarehouseName.setText(mainPresenter.getSelectedWarehouse().getWarehouseName() == null
                ? getString(R.string.n_a)
                : mainPresenter.getSelectedWarehouse().getWarehouseName());
    }

    /**
     * Verifies the runtime directories exist. They are needed for the app to function
     */
    private void verifyRuntimeDirectoriesExist() {
        File dataDirectory = new File(Constants.DATA_DIRECTORY);
        if(!(dataDirectory.exists() || dataDirectory.isDirectory()))
            dataDirectory.mkdir();
        dataDirectory = new File(Constants.OUTPUT_DIRECTORY);
        if(!(dataDirectory.exists() || dataDirectory.isDirectory()))
            dataDirectory.mkdir();
    }

    /**
     * save the changes made by the user on pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        app.saveCurrentState();
    }
}
