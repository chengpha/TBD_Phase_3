package com.example.warehouseapp.services;

public class FileServiceFactory {
    public IFileService getFileService(String type){
        switch (type){
            case "xml":
                return new XmlService();
            case "json":
                return new GsonService();
            default:
                return null;
        }
    }
}
