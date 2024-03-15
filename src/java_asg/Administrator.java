/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_asg;

import Interfaces.IdGenerator;

/**
 *
 * @author wjing
 */
public class Administrator extends User implements IdGenerator{
    private static int idCounter = 100000;
    
    //overloaded
    Administrator(){
    
    }
    Administrator(String id,String username,String password,String role){
        super(id,username,password,role);
    }
    
    //getter
    public static int getIDCounter(){
        return idCounter;
    }

    //setter
    public static void setIDCounter(int currentID){
        idCounter = currentID;
    }    
    
    //method
    public synchronized String generateID() {
        idCounter++; // Increment the counter
        return "AD" + idCounter;
    }
    
    public synchronized String previewNextID() {
        return "AD" + (idCounter + 1);
    }
    
     public boolean registerUser(String userID,String username,String password,String role){
         //check user ID exists
         for(User user: User.getUser()){
             if(user.getID() == userID){
                 return false;
             }
         }
         
         switch(role){
             case "SalesManager":
                 User newSM = new SalesManager(userID,username,password,role);
                 newSM.add(newSM);
                 break;
             case "PurchaseManager":
                 User newPM = new PurchaseManager(userID,username,password,role);
                 newPM.add(newPM);
                 break;
             case "Administrator":
                 User newAM = new Administrator(userID,username,password,role);
                 newAM.add(newAM);
                 break;
         }
         //serialization
         new User().save();
         return true;
     }
     
     
}
