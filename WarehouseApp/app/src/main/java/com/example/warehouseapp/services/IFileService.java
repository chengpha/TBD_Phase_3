package com.example.warehouseapp.services;
import com.example.warehouseapp.model.Shipment;

import java.io.InputStream;
import java.util.Collection;

public interface IFileService {
    Collection<Shipment> processInputFile(String file) throws Exception;
    Collection<Shipment> processInputFile(InputStream is) throws Exception;
}
