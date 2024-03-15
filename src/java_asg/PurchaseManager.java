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
public class PurchaseManager extends User implements IdGenerator{
    private static int idCounter = 100000;
    
    PurchaseManager(){
    
    }
    PurchaseManager(String id,String username, String password,String role){
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
        return "PM" + idCounter;
    }
    
    public synchronized String previewNextID() {
        return "PM" + (idCounter + 1);
    }
}
