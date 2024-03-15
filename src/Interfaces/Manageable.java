/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

/**
 *
 * @author wjing
 */
public interface Manageable<T> {

    // Abstract methods
    public abstract void add(T entity);
    public abstract void delete(String id);
    public abstract T getByID(String id);
    public abstract boolean edit(String ID,String editedData,int column);
}
