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
public class StandartUye extends Uye {

    public StandartUye(int id, String ad, String soyad, String uyelikturu, LocalDate baslangıctarihi) {
        super(id, ad, soyad, uyelikturu, baslangıctarihi);
        
    }

    @Override
    public double aylikUcret() {
        return 3000.0;
    }

    @Override
    public String özetBilgi() {
        return String.format("Standart üye: %s %s - Aylık ücret: %.2f TL", getad(),getsoyad(),aylikUcret());
    }

    @Override
    public String raporBilgisi() {
      return özetBilgi();
    }
    
}
