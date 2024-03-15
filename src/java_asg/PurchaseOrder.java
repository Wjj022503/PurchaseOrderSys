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
public class PurchaseOrder implements IdGenerator,Manageable<PurchaseOrder>,FileAccess,Serializable{
    private String poID;
    private PurchaseRequisition pr;
    private String pmID;
    private static ArrayList<PurchaseOrder> poList = new ArrayList<>();
    private static int idCounter = 100000;
    
    //constructor
    PurchaseOrder(){
    
    }
    PurchaseOrder(PurchaseRequisition pr,String pmID){
        this.poID = generateID();
        this.pr = pr;
        this.pmID = pmID;
    }
    
    //get set method
    public String getPOID(){
        return this.poID;
    }
    
    public PurchaseRequisition getPOPR(){
        return this.pr;
    }
    
    public String getPMID(){
        return this.pmID;
    }
    
    public static ArrayList<PurchaseOrder> getPO(){
        return poList;
    }
    
    public void setPOID(String poID){
        this.poID = poID;
    }
    
    public void setPOPR(PurchaseRequisition pr){
        this.pr = pr;
    }
    
    public void setPMID(String pmID){
        this.pmID = pmID;
    }
    
    //method
    @Override
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "PO" + idCounter;
    }
    
    @Override
    public synchronized String previewNextID() {
        return "PO" + (idCounter + 1);
    }
    
    @Override
    public void add(PurchaseOrder po){
        poList.add(po);
        po.save();
    }
    @Override
    public void delete(String poID){
        poList.removeIf(po -> po.poID.equals(poID));
        new PurchaseOrder().save();
    }

    @Override
    public PurchaseOrder getByID(String poID){
        for(PurchaseOrder po:poList){
            if(po.getPOID().equals(poID)){
                return po;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean edit(String poID,String editedData,int column){
        editedData = editedData.replaceAll("//", "");
        editedData = editedData.replaceAll("[^A-Za-z0-9/]","");
        PurchaseOrder po = new PurchaseOrder().getByID(poID);
        switch(column){
            case 3:
                Item item = new Item().getByID(editedData);
                if(item != null){
                    po.getPOPR().setPRItem(item);
                    po.getPOPR().setTotalPrice(po.getPOPR().getRequiredQuantity() * po.getPOPR().getPRItem().getPrice());
                    break;
                }
                else{
                    return false;
                }                
            case 4:
                LocalDate currentDateLocal = LocalDate.now();
                Date currentDate = Date.from(currentDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
                String strDate = editedData; // Your date in dd/MM/yyyy format
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(strDate);
                    if(date.before(currentDate)){
                        return false;
                    }
                    po.getPOPR().setRequiredDate(date);
                    PurchaseRequisition.prStatusUpdate();
                    break;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            case 5:
                try {
                    int qtt = Integer.parseInt(editedData);
                    po.getPOPR().setRequiredQuantity(qtt);
                    po.getPOPR().setTotalPrice(po.getPOPR().getRequiredQuantity() * po.getPOPR().getPRItem().getPrice());
                    break;
                } catch (NumberFormatException e) {
                    return false;
                }
        }
        po.save();
        return true;
    }

    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\poData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(poList);
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
            FileInputStream fIN = new FileInputStream("src\\Database\\poData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            poList = (ArrayList<PurchaseOrder>) oIN.readObject();
            idCounter = (int) oIN.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
