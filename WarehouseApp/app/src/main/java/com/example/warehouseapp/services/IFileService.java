package com.example.warehouseapp.services;
import com.example.warehouseapp.model.Shipment;

import java.util.Collection;

public interface IFileService {
    Collection<Shipment> processInputFile(String file) throws Exception;
}
