package com.example.warehouseapp;
import android.os.Environment;
import java.io.File;

public class Constants {
    public static final String DATA_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "data" + File.separator;
    public static final String OUTPUT_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "output" + File.separator;
}
