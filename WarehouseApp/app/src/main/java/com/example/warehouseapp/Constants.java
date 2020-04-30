package com.example.warehouseapp;
import android.os.Environment;
import java.io.File;

public class Constants {
    public static final String DATA_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "data" + File.separator;
    public static final String OUTPUT_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "output" + File.separator;
    public static final int DELETE_SHIPMENT_CONST = 3;
    public static final int DISPLAY_ALL_SHIPMENTS_CONST = 2;
    public static final int WAREHOUSE_ID = 1;
    public static final int SHIPMENT_ID = 2;
}
