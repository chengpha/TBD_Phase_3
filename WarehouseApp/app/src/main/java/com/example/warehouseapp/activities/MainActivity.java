package com.example.warehouseapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.warehouseapp.Constants;
import com.example.warehouseapp.R;
import com.example.warehouseapp.WarehouseApplication;
import com.example.warehouseapp.model.Shipment;
import com.example.warehouseapp.model.Warehouse;
import com.example.warehouseapp.presenters.IMainPresenter;
import com.example.warehouseapp.presenters.MainPresenter;
import com.example.warehouseapp.views.IMainView;

import java.io.File;
import java.util.stream.Stream;

import es.dmoral.toasty.Toasty;

import static android.R.layout.simple_spinner_dropdown_item;
import static com.example.warehouseapp.R.layout.spinner_item;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements IMainView {
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private IMainPresenter mainPresenter;
    private WarehouseApplication app;
    private Button btnDisableEnableFreight;
    private Button btnAddShipment;
    private TextView lblWarehouseId;
    private TextView lblWarehouseName;
    private Button btnDeleteShipment;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 'data' directory is needed to save the state of the program
         * 'output' directory is needed to save json export files
         */
        verifyRuntimeDirectoriesExist();
        app = (WarehouseApplication)getApplication();

        mainPresenter = new MainPresenter(app);
        mainPresenter.setView(this);

        lblWarehouseId = findViewById(R.id.lblWarehouseId);
        lblWarehouseName = findViewById(R.id.lblWarehouseName);

        Button btnChooseFile = findViewById(R.id.btnChooseFile);
        btnChooseFile.setOnClickListener(a -> {
            Toasty.error(this, "Not implemented", Toasty.LENGTH_SHORT).show();
        });


        Spinner spnSelectWarehouse = findViewById(R.id.spnSelectWarehouse);
        ArrayAdapter<Warehouse> adapter = new ArrayAdapter<>(this, spinner_item, mainPresenter.getWarehouseList());
        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        spnSelectWarehouse.setAdapter(adapter);
        spnSelectWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainPresenter.setSelectedWarehouse((Warehouse)parent.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        btnDisableEnableFreight = findViewById(R.id.btnDisableEnableFreight);
        btnDisableEnableFreight.setText("Disable");
        btnDisableEnableFreight.setOnClickListener(v-> mainPresenter.disableEnableFreightClicked());

        btnAddShipment = findViewById(R.id.btnAddShipment);
        btnAddShipment.setOnClickListener(v -> mainPresenter.addShipmentClicked());

        btnDeleteShipment = findViewById(R.id.btnDeleteShipment);
        btnDeleteShipment.setOnClickListener(v -> {
            mainPresenter.deleteShipmentClicked();
            Toasty.error(this, "Not implemented", Toasty.LENGTH_SHORT).show();
        });

        Button btnExportWarehouseShipments = findViewById(R.id.btnExportWarehouseShipments);
        btnExportWarehouseShipments.setOnClickListener(v -> mainPresenter.exportWarehouseShipmentsClicked());

        Button btnDisplayAllShipments = findViewById(R.id.btnDisplayAllShipments);
        btnDisplayAllShipments.setOnClickListener(v -> {
            mainPresenter.displayAllShipmentsClicked();
            Toasty.error(this, "Not implemented", Toasty.LENGTH_SHORT).show();
        });

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }


    /**
     * Indicates when the user has responded to a permission request
     * @param requestCode The request code
     * @param permissions The permissions requested
     * @param grantResults The result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toasty.error(this, "Permission required, closing application",
                            Toasty.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
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
        }
        else{
            btnDisableEnableFreight.setText("Enable");
            btnAddShipment.setEnabled(false);
            btnDeleteShipment.setEnabled(false);
        }
    }

    @Override
    public void setSelectedWarehouse(Warehouse warehouse) {
        enableDisableControlsOnFreightReceiptChange(warehouse);

        lblWarehouseId.setText( mainPresenter.getSelectedWarehouse().toString());
        lblWarehouseName.setText(mainPresenter.getSelectedWarehouse().getWarehouseName().equals("")
                ? getString(R.string.n_a)
                : mainPresenter.getSelectedWarehouse().getWarehouseName());
    }

    private void verifyRuntimeDirectoriesExist() {
        File dataDirectory = new File(Constants.DATA_DIRECTORY);
        if(!(dataDirectory.exists() || dataDirectory.isDirectory()))
            dataDirectory.mkdir();
        dataDirectory = new File(Constants.OUTPUT_DIRECTORY);
        if(!(dataDirectory.exists() || dataDirectory.isDirectory()))
            dataDirectory.mkdir();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.saveCurrentState();
    }
}
