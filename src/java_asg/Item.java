/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_asg;
import Interfaces.FileAccess;
import Interfaces.IdGenerator;
import Interfaces.Manageable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.Serializable;
/**
 *
 * @author wjing
 */
public class Item implements IdGenerator,Manageable<Item>,FileAccess,Serializable{
    private String itemID;
    private String itemName;
    private double price;
    private Supplier supplier;
    private int stock;
    private static ArrayList<Item> itemList = new ArrayList<>();
    private static int idCounter = 100000;

    
    Item(){
    
    }
    Item(String itemName,double price,Supplier supplier,int stock){
        this.itemID = generateID();
        this.itemName = itemName;
        this.price = price;
        this.supplier = supplier;
        this.stock = stock;
    }
    
    //get set method
    public String getItemID(){
        return this.itemID;
    }
    
    public String getItemName(){
        return this.itemName;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    public Supplier getSupplier(){
        return this.supplier;
    }
    
    public int getStock(){
        return this.stock;
    }
    
    public static ArrayList<Item> getItems(){
        return itemList;
    }
    
    public void setItemID(String itemID){
        this.itemID = itemID;
    }
    
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    
    public void setPrice(double price){
        this.price = price;
    }
    
    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
    }
    
    public void setStock(int stock){
        this.stock = stock;
    }
    
    //method
    @Override
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "IT" + idCounter;
    }
    
    @Override
    public synchronized String previewNextID() {
        return "IT" + (idCounter + 1);
    }
    
    @Override
    public void add(Item item){
        itemList.add(item);
        //Serialization
        item.save();
    }
    
    @Override
    public void delete(String itemID){
        itemList.removeIf(item -> item.getItemID().equals(itemID));
        //Serialization
        new Item().save();
    }
    
    @Override
    public Item getByID(String itemID){
        for(Item item:itemList){
            if(item.getItemID().equals(itemID)){
                return item;
            }
        } 
        return null;
    }
    
    @Override
    public boolean edit(String itemID,String editedData,int column){
        editedData = editedData.replaceAll("[^A-Za-z0-9.]","");
        Item item = new Item().getByID(itemID);
        switch(column){
            case 1:
                item.setItemName(editedData);
                break;
            case 2:
                item.setPrice(Double.parseDouble(editedData));
                break;
            case 3:
                item.getSupplier().deleteSuppliedItem(itemID);
                Supplier sp = new Supplier().getByID(editedData);
                if(sp != null){
                    item.setSupplier(sp);
                    sp.getSuppliedItem().add(item);
                    break;
                }
                else{
                    return false;
                }
            case 4:
                item.setStock(Integer.parseInt(editedData));
                break;
        }
        item.save();
        return true;
    }

    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\itemData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(itemList);
            oOut.writeObject(idCounter);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean load(){
        try{
            FileInputStream fIN = new FileInputStream("src\\Database\\itemData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            itemList = (ArrayList<Item>) oIN.readObject();
            idCounter = (int)oIN.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
