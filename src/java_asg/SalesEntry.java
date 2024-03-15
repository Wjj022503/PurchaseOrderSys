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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
/**
 *
 * @author wjing
 */
public class SalesEntry implements IdGenerator,Manageable<SalesEntry>,FileAccess,Serializable{
    private String salesID;
    private Item item;
    private int quantitySold;
    private Date salesDate; // can also use LocalDate from java.time if you prefer
    private double totalAmount;
    private static ArrayList<SalesEntry> salesList = new ArrayList<>();
    private static int idCounter = 100000;
    //constructor
    SalesEntry(){
    
    }
    
    SalesEntry(Item item, int quantitySold, Date salesDate) {
        this.salesID = generateID();
        this.item = item;
        this.quantitySold = quantitySold;
        this.salesDate = salesDate;
        this.totalAmount = item.getPrice() * quantitySold;
    }

    //get set method
    public String getSalesID(){
        return this.salesID;
    }
    
    public Item getItem(){
        return this.item;
    }
    
    public int getQuantitySold(){
        return this.quantitySold;
    }
    
    public Date getSalesDate(){
        return this.salesDate;
    }
    
    public double getTotalAmount(){
        return this.totalAmount; 
    }
    
    public static ArrayList<SalesEntry> getSE(){
        return salesList;
    }
    
    public void setSalesID(String salesID){
        this.salesID = salesID;
    }
    
    public void setItem(Item item){
        this.item = item;
    }
    
    public void setQuantitySold(int qttSold){
        this.quantitySold = qttSold;
    }
    
    public void setSalesDate(Date salesDate){
        this.salesDate = salesDate;
    }
    
    public void setTotalAmount(double totalAmount){
        this.totalAmount = totalAmount;
    }
    
    //method
    @Override
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "SE" + idCounter;
    }
    
    @Override
    public synchronized String previewNextID() {
        return "SE" + (idCounter + 1);
    }
    
    @Override
    public void add(SalesEntry sales){
        salesList.add(sales);
        sales.save();
    }
    
    @Override
    public void delete(String salesID){
        salesList.removeIf(sales -> sales.salesID.equals(salesID));
        new SalesEntry().save();
    }
    
    @Override
    public SalesEntry getByID(String seID){
        for(SalesEntry se:salesList){
            if(se.getSalesID().equals(seID)){
                return se;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean edit(String seID,String editedData,int column){
        SalesEntry se = new SalesEntry().getByID(seID);
        editedData = editedData.replaceAll("//", "");
        editedData = editedData.replaceAll("[^A-Za-z0-9/]","");
        switch(column){
            case 1:
                Item itemEdit = new Item().getByID(editedData);
                if(itemEdit != null){
                    se.setItem(itemEdit);
                }
                else{
                    return false;
                }
                break;
            case 3:
                LocalDate currentDateLocal = LocalDate.now();
                Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                String strDate = editedData; // Your date in dd/MM/yyyy format
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(strDate);
                    if(date.after(currentDate)){
                        return false;
                    }
                    se.setSalesDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case 4:
                try {
                    int qtt = Integer.parseInt(editedData);
                    if(qtt <= 0){
                        return false;
                    }
                    int new_stock = 0;
                    new_stock = se.getItem().getStock() + se.getQuantitySold() - qtt;
                    se.setQuantitySold(qtt);
                    se.getItem().setStock(new_stock);
                    se.setTotalAmount(se.getItem().getPrice() * se.getQuantitySold());
                    break;

                } catch (NumberFormatException e) {
                    return false;
                }
        }
        se.save();
        return true;
    }
    
    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\salesData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(salesList);
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
            FileInputStream fIN = new FileInputStream("src\\Database\\salesData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            salesList = (ArrayList<SalesEntry>) oIN.readObject();
            idCounter = (int) oIN.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
