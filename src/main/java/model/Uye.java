/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author idalozyurt
 */
public abstract class Uye implements IRaporlanabilir{

   
    private int id;
    private String ad;
    private String soyad;
    private String uyelikturu;
    private final LocalDate baslangıctarihi;
    
    
    public Uye(int id, String ad, String soyad, String uyelikturu, LocalDate baslangıctarihi){
        this.id=id;
        this.ad=ad;
        this.soyad=soyad;
        this.uyelikturu=uyelikturu;
        this.baslangıctarihi=baslangıctarihi;
    }
    
    public int getid(){
        return id;
    }
    public String getad(){
        return ad;
    }
     public String getsoyad(){
        return soyad;
    }
     public String getuyelikturu(){
        return uyelikturu;
    }
    public LocalDate baslangıctarihi(){
        return  baslangıctarihi;
    }
    
    public void setid(int id){
        this.id=id;
    }
    public void setad(String ad){
        this.ad=ad;
    }
    public void setsoyad(String soyad){
        this.soyad=soyad;
    }
    public void setUyelikturu(String uyelikturu){
        this.uyelikturu=uyelikturu;
    }
    
    
    public abstract double aylikUcret();
    public abstract String özetBilgi();
    
    
    
    
}
