/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_asg;
import Interfaces.FileAccess;
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
public class User implements Manageable<User>,FileAccess,Serializable{
    private String id;
    private String username;
    private String password;
    public enum Roles{
        ADMINISTRATOR,
        SALESMANAGER,
        PURCHASEMANAGER,
    }
    private Roles role;
    private static ArrayList<User> userList = new ArrayList<>();
    
    //constructor
    User(){
    
    }
    User(String id,String username,String password,String role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = Roles.valueOf(role.toUpperCase());
    } 
    
    //get set method
    public String getID(){
        return this.id;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public Roles getRole(){
        return this.role;
    }
    
    public static ArrayList<User> getUser(){
        return userList;
    }
    
    public void setID(String id){
        this.id = id;
    }
    
    public void setUername(String username){
        this.username = username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public void setRole(String role){
        this.role = Roles.valueOf(role);
    }
    
    //method    
    public static User login(String username, String password) {
        username = username.replaceAll("[^A-Za-z0-9.!@#$%^&*]", "");
        password = password.replaceAll("[^A-Za-z0-9.!@#$%^&*]", "");
        for (User user : userList) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
   }
    
    //method
    @Override
    public void add(User user){
        userList.add(user);
        //Serialization
        user.save();
    }
    
    @Override
    public void delete(String id){
        userList.removeIf(user -> user.getID().equals(id));
        //Serialization
        new User().save();
    }
    
    @Override
    public User getByID(String userID){
        for(User user:userList){
            if(user.getID().equals(userID)){
                return user;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean edit(String userID,String editedData,int column){
        User userBefore = new User().getByID(userID);
        switch(column){
            case 1:
                editedData = editedData.replaceAll("[^A-Za-z0-9]","");
                userBefore.setUername(editedData);
                break;
            case 2:
                editedData = editedData.replaceAll("[^A-Za-z0-9.!@#$%^&*]",""); 
                userBefore.setPassword(editedData);
                break;
            case 3:
                editedData = editedData.replaceAll("[^A-Za-z]","");
                User editedUser = new User();
                if(editedData.toUpperCase().equals("SALESMANAGER")){
                    editedUser = new SalesManager(userBefore.getID(),userBefore.getUsername(),userBefore.getPassword(),"SalesManager");
                    userBefore.add(editedUser);
                    editedUser.delete(userBefore.getID());            
                    break;
                }
                else if(editedData.toUpperCase().equals("PURCHASEMANAGER")){
                    editedUser = new PurchaseManager(userBefore.getID(),userBefore.getUsername(),userBefore.getPassword(),"PurchaseManager");
                    userBefore.add(editedUser);
                    editedUser.delete(userBefore.getID());                
                    break;
                }
                else if(editedData.toUpperCase().equals("ADMINISTRATOR")){
                    editedUser = new Administrator(userBefore.getID(),userBefore.getUsername(),userBefore.getPassword(),"Administrator");
                    userBefore.add(editedUser);
                    editedUser.delete(userBefore.getID());                    
                    break;                
                }
                else{
                    return false;
                }
        }
        new User().save();
        return true;
    }
    
    @Override
    public boolean save(){
        try{
            FileOutputStream fOut = new FileOutputStream("src\\Database\\userData.txt");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(userList);
            oOut.writeObject(SalesManager.getIDCounter());
            oOut.writeObject(PurchaseManager.getIDCounter());
            oOut.writeObject(Administrator.getIDCounter());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean load(){
        try{
            FileInputStream fIN = new FileInputStream("src\\Database\\userData.txt");
            ObjectInputStream oIN = new ObjectInputStream(fIN);
            userList = (ArrayList<User>) oIN.readObject();
            SalesManager.setIDCounter((int) oIN.readObject());
            PurchaseManager.setIDCounter((int) oIN.readObject());
            Administrator.setIDCounter((int) oIN.readObject());
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
