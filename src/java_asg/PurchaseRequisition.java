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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
/**
 *
 * @author wjing
 */
public class PurchaseRequisition implements IdGenerator,Manageable<PurchaseRequisition>,FileAccess,Serializable{
    private String prID; //purchase requisition ID
    private Item item;
    private int requiredQuantity;
    private Date requiredDate;
    private String smID; // Sales Manager ID
    private double totalPrice;
    private String status; //Expired (current date exceed required date), Rejected (rejected by purchase manager), 
                           //Approved (sales manager approved), Canceled (purchase order is deleted),
                           //Convert to PO (purchase manager generated Purchase Order)
    private static ArrayList<PurchaseRequisition> prList = new ArrayList<>();
    private static int idCounter = 100000;
    
    PurchaseRequisition(){
    
    }
    PurchaseRequisition(Item item,int reqQtt,Date reqDate,String smID,String status){
        this.prID = generateID(); 
        this.item = item;
        this.requiredQuantity = reqQtt;
        this.requiredDate = reqDate;
        this.smID = smID;
        this.totalPrice = item.getPrice() * reqQtt;
        this.status = status;
    }
    
    //get set method
    public String getPRID(){
        return this.prID;
    }
    
    public Item getPRItem(){
        return this.item;
    }
    
    public int getRequiredQuantity(){
        return this.requiredQuantity;
    }
    
    public Date getRequiredDate(){
        return this.requiredDate;
    }
    
    public String getPRSMID(){
        return this.smID;
    }
    
    public double getTotalPrice(){
        return this.totalPrice;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public static ArrayList<PurchaseRequisition> getPR(){
        return prList;
    }
    
    public void setPRID(String prID){
        this.prID = prID;
    }
    
    public void setPRItem(Item item){
        this.item = item;
    }
    
    public void setRequiredQuantity(int reqQtt){
        this.requiredQuantity = reqQtt;
    }
    
    public void setRequiredDate(Date reqDate){
        this.requiredDate = reqDate;
    }
    
    public void setPRSMID(String smID){
        this.smID = smID;
    }
    
    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    //method
    @Override
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "PR" + idCounter;
    }
    
    public synchronized String previewNextID() {
        return "PR" + (idCounter + 1);
    }
    
    @Override
    public void add(PurchaseRequisition pr){
        prList.add(pr);
        pr.save();
    }
    
    @Override
    public void delete(String prID){
        prList.removeIf(pr -> pr.prID.equals(prID));
        new PurchaseRequisition().save();
    }
    
    @Override
    public PurchaseRequisition getByID(String prID){
        for(PurchaseRequisition pr:prList){
            if(pr.getPRID().equals(prID)){
                return pr;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean edit(String prID,String editedData,int column){
        PurchaseRequisition pr = new PurchaseRequisition().getByID(prID);
        editedData = editedData.replaceAll("//", "");
        editedData = editedData.replaceAll("[^A-Za-z0-9/]","");
        switch(column){
            case 1:
                Item itemEdit = new Item().getByID(editedData);
                if(itemEdit != null){
                    pr.setPRItem(itemEdit);
                }
                else{
                    return false;
                }
                break;
            case 3:
                try {
                    int qtt = Integer.parseInt(editedData);
                    if(qtt <= 0){
                        return false;
                    }
                    pr.setRequiredQuantity(qtt);
                    pr.setTotalPrice(pr.getRequiredQuantity() * pr.getPRItem().getPrice());
                    break;
                } catch (NumberFormatException e) {
                    return false;
                }
            case 4:
                //current date
                LocalDate currentDateLocal = LocalDate.now();
                Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                //date format
                String strDate = editedData;
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(strDate);
                    //required date cannot before current date
                    if (date.before(currentDate)) {
                        return false;
                    }
                    pr.setRequiredDate(date);
                    PurchaseRequisition.prStatusUpdate();
                    break;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
        }
        pr.save();
        return true;
    }
      
    public static void prStatusUpdate(){
        //set expired pr
        LocalDate currentDateLocal = LocalDate.now();
        Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (PurchaseRequisition pr : PurchaseRequisition.getPR()) {
            if (pr.getRequiredDate().before(currentDate)) {
                pr.setStatus("Expired");
            }
            else if(pr.getRequiredDate().after(currentDate) && pr.getStatus().equals("Expired")){
                pr.setStatus("Approved");
            }
        }
        new PurchaseRequisition().save();
    }
    
    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\prData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(prList);
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
            FileInputStream fIN = new FileInputStream("src\\Database\\prData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            prList = (ArrayList<PurchaseRequisition>) oIN.readObject();
            idCounter = (int) oIN.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
