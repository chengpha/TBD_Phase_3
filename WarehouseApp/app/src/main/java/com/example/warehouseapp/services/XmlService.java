package com.example.warehouseapp.services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.warehouseapp.model.Shipment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;


public class XmlService implements IFileService {
    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     Visitor design pattern is used in this class.

     This method below isn't used in the android app. It comes from the library
     that was used by the desktop application. Instead, the overload below it is used.
     */
    public Collection<Shipment> processInputFile(String file) throws Exception {
        Collection<Shipment> list = new ArrayList<>();
        try {
            // turns the xml into usable data docs
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File xmlFile = new File(file);
            Document doc = dBuilder.parse(xmlFile.toURI().toString());
            doc.getDocumentElement().normalize();

            // Gets all the shipment lists and puts them into a nodelist
            NodeList shipmentNodeList = doc.getElementsByTagName("Shipment");
            if (shipmentNodeList.getLength() == 0)
                    throw new Exception();

            // Loop through each shipment in the nodelist
            for (int index = 0; index < shipmentNodeList.getLength(); index++) {
                Node shipment = shipmentNodeList.item(index);

                if (shipment.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) shipment;

                    // Gets all the values needed
                    String warehouseID = ((Element) (shipment.getParentNode())).getAttribute("id"); // checks the shipment node parent, aka warehouse, and finds its ID
                    String warehouseName = ((Element) (shipment.getParentNode())).getAttribute("name"); // checks the shipment node parent, aka warehouse, and finds its name
                    String shipmentID =  eElement.getAttribute("id"); // grabs the id attribute
                    String shipmentMethod = eElement.getAttribute("type"); // grabs the type attribute, aka air, rail, truck, etc.
                    Double weight = Double.parseDouble(doc.getElementsByTagName("Weight").item(index).getTextContent()); // gets the weight by index
                    Long receiptDate = Long.parseLong(doc.getElementsByTagName("ReceiptDate").item(index).getTextContent()); // gets the receipt date by index

                    // Creates a shipment and adds it to the list
                    Shipment s = new Shipment(warehouseID, warehouseName, shipmentID, shipmentMethod, weight, receiptDate);
                    s.setDateAdded();
                    list.add(s);
                }
            }
        } catch(Exception e) {
            throw e;
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Collection<Shipment> processInputFile(InputStream is) throws Exception {
        Collection<Shipment> list = new ArrayList<>();
        try {
            // turns the xml into usable data docs
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            // Gets all the shipment lists and puts them into a nodelist
            NodeList shipmentNodeList = doc.getElementsByTagName("Shipment");
            if (shipmentNodeList.getLength() == 0)
                throw new Exception();

            // Loop through each shipment in the nodelist
            for (int index = 0; index < shipmentNodeList.getLength(); index++) {
                Node shipment = shipmentNodeList.item(index);

                if (shipment.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) shipment;

                    // Gets all the values needed
                    String warehouseID = ((Element) (shipment.getParentNode())).getAttribute("id"); // checks the shipment node parent, aka warehouse, and finds its ID
                    String warehouseName = ((Element) (shipment.getParentNode())).getAttribute("name"); // checks the shipment node parent, aka warehouse, and finds its name
                    String shipmentID =  eElement.getAttribute("id"); // grabs the id attribute
                    String shipmentMethod = eElement.getAttribute("type"); // grabs the type attribute, aka air, rail, truck, etc.
                    Double weight = Double.parseDouble(doc.getElementsByTagName("Weight").item(index).getTextContent()); // gets the weight by index
                    Long receiptDate = Long.parseLong(doc.getElementsByTagName("ReceiptDate").item(index).getTextContent()); // gets the receipt date by index

                    // Creates a shipment and adds it to the list
                    Shipment s = new Shipment(warehouseID, warehouseName, shipmentID, shipmentMethod, weight, receiptDate);
                    s.setDateAdded();
                    list.add(s);
                }
            }
        } catch(Exception e) {
            throw e;
        }
        return list;
    }
}
