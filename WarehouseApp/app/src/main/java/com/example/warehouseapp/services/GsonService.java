package com.example.warehouseapp.services;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.warehouseapp.dtos.ShipmentsWrapper;
import com.example.warehouseapp.model.Shipment;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import static java.nio.file.Files.exists;


/**
 * Gson controller implements Gson library to perform JSON read/write file operations across the application.
 */
public class GsonService implements IFileService {
    /**
        Visitor design pattern is used in this class.

        This method below isn't used in the android app. It comes from the library
        that was used by the desktop application. Instead, the overload below it is used.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Collection<Shipment> processInputFile(String file) throws Exception{
        Collection<Shipment> list;
        try (Reader reader = new FileReader(file)) {
            list = new Gson().fromJson(reader, ShipmentsWrapper.class).getShipmentList();
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Collection<Shipment> processInputFile(InputStream file) throws Exception{
        Collection<Shipment> list;
        try (Reader reader = new InputStreamReader(file)) {
            list = new Gson().fromJson(reader, ShipmentsWrapper.class).getShipmentList();
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String exportShipmentsToJsonFile(String location, String fileString, Object o){
        if (exists(Paths.get(location))) {
            try {
                Files.write(Paths.get(fileString), new Gson().toJson(o).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileString;
        } else {
            return "";
        }
    }
}
