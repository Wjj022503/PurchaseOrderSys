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
public class Supplier implements IdGenerator,Manageable<Supplier>,FileAccess,Serializable{
    private String supplierID;
    private String supplierName;
    private ArrayList<Item> suppliedItemList = new ArrayList<>();
    private static ArrayList<Supplier> supplierList = new ArrayList<>();
    private static int idCounter = 100000;
    
    //constructor
    Supplier(){
    
    }
    
    Supplier(String supplierName,ArrayList<Item> suppliedItemList){
        this.supplierID = generateID();
        this.supplierName = supplierName;
        this.suppliedItemList = new ArrayList<>(suppliedItemList);
    }
    
    //get set method
    public String getSupplierID(){
        return this.supplierID;
    }
    
    public String getSupplierName(){
        return this.supplierName;
    }
    
    public ArrayList<Item> getSuppliedItem(){
        return this.suppliedItemList;
    }
    
    public static ArrayList<Supplier> getSupplier(){
        return supplierList;
    }
    
    public void setSupplierID(String supplierID){
        this.supplierID = supplierID;
    }
    
    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }
    
    //method
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "SP" + idCounter;
    }
    
    public synchronized String previewNextID() {
        return "SP" + (idCounter + 1);
    }    
    
    public void add(Supplier supplier){
        supplierList.add(supplier);
        //Serialization
        supplier.save();
        
    }
    
    public void delete(String supplierID){
        supplierList.removeIf(supplier -> supplier.getSupplierID().equals(supplierID));
        //Serialization
        new Supplier().save();
    }
    
    public void addSuppliedItem(Item item){
        this.suppliedItemList.add(item);
        //Serialization
        new Supplier().save();
        new Item().save();
    }
    
    public void deleteSuppliedItem(String itemID){
        this.suppliedItemList.removeIf(items -> items.getItemID().equals(itemID));
        //Serialization
        new Supplier().save();
        new Item().save();
    }
    
    public Supplier getByID(String supID){
        for(Supplier sup:supplierList){
            if(sup.getSupplierID().equals(supID)){
                return sup;
            }
        }
        
        return null;
    }
    
    public boolean edit(String spID,String editedData,int column){
        editedData = editedData.replaceAll("[^A-Za-z0-9]","");
        Supplier sp = new Supplier().getByID(spID);
        sp.setSupplierName(editedData);
        sp.save();
        return true;
    }
    
    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\supplierData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(supplierList);
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
            FileInputStream fIN = new FileInputStream("src\\Database\\supplierData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            supplierList = (ArrayList<Supplier>) oIN.readObject();
            idCounter = (int) oIN.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }  
}
