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
public class PremiumUye extends Uye {
    private double ekHizmetUcreti;
        
    public PremiumUye(int id, String ad, String soyad, String uyelikturu, LocalDate baslangıctarihi, double ekHizmetUcreti) {
        super(id, ad, soyad, uyelikturu, baslangıctarihi);
        this.ekHizmetUcreti = ekHizmetUcreti;
    }
    
    public double getekHizmetUcreti() {
        return ekHizmetUcreti;
    }
    
    public void setekHizmetUcreti(double ekHizmetUcreti) {
        this.ekHizmetUcreti = ekHizmetUcreti;
    }

    @Override
    public double aylikUcret() {
        return 3000.0 + ekHizmetUcreti;
    }

    @Override
    public String özetBilgi() {
        return String.format("Premium Üye: %s %s - Aylık Ücret: %.2f TL (Ek Hizmet: %.2f TL)", 
            getad(), getsoyad(), aylikUcret(), ekHizmetUcreti);
    }

    @Override
    public String raporBilgisi() {
        return özetBilgi();
    }
}