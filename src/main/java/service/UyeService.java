/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IDosyaIslemleri;
import dao.XMLDosyaIslemleri;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Uye;

/**
 *
 * @author idalozyurt
 */
public class UyeService {
    private List<Uye> uyeler;
    public IDosyaIslemleri dosyaIslemleri;
    private int sonid; 
    
    
     public UyeService() {
        this.dosyaIslemleri = new XMLDosyaIslemleri();
        this.uyeler = dosyaIslemleri.verileriYükle();
        this.sonid = uyeler.isEmpty() ? 0 : 
            uyeler.stream().mapToInt(Uye::getid).max().getAsInt();
    }
     
      public void uyeEkle(Uye uye) {
        uye.setid(++sonid);
        uyeler.add(uye);
        dosyaIslemleri.verileriKaydet(uyeler);
    }
      
    public void uyesil(int id){
        uyeler.removeIf(uye -> uye.getid()==id);
        
        dosyaIslemleri.verileriKaydet(uyeler);
    }  
    
    public void uyegüncelle(Uye günceluye){
        for(int i=0;i<uyeler.size();i++){
            if(uyeler.get(i).getid()==günceluye.getid()){
                uyeler.set(i, günceluye);
                break;
            }
        }
         dosyaIslemleri.verileriKaydet(uyeler);
    }
    
    public List<Uye> tümüyelerigetir(){
        return new ArrayList<>(uyeler);
    }
    public List<Uye> uyeAra(String kriter) {
    List<Uye> sonuç = new ArrayList<>();
    
    if (kriter == null || kriter.trim().isEmpty()) {
        return sonuç;
    }
    
    String aranan = kriter.trim().toLowerCase();
    
    for (Uye uye : uyeler) {
        String tamIsim = (uye.getad() + " " + uye.getsoyad()).toLowerCase();
        String ad = uye.getad().toLowerCase();
        String soyad = uye.getsoyad().toLowerCase();
        
        if (tamIsim.contains(aranan) || 
            ad.contains(aranan) || 
            soyad.contains(aranan)) {
            sonuç.add(uye);
        }
    }
    
    return sonuç;
}

    public List<Uye> uyeleriSirala() {
    List<Uye> siraliliste = new ArrayList<>(uyeler);
    
    siraliliste.sort(Comparator.comparing(Uye::getad)
                               .thenComparing(Uye::getsoyad));
    
    return siraliliste;
}
    public double toplamAylıkGelir(){
        return uyeler.stream().mapToDouble(Uye::aylikUcret).sum();
    }
    
       
    public String raporBilgisi() {
           StringBuilder rapor = new StringBuilder();
        
        rapor.append("=== ÜYE RAPORU ===\n");
        rapor.append("Toplam Üye Sayısı: ").append(uyeler.size()).append("\n");
        rapor.append("Toplam Aylık Getiri: ").append(String.format("%.2f", toplamAylıkGelir())).append(" TL\n\n");
        

        for (Uye uye : uyeler) {
            rapor.append(uye.raporBilgisi()).append("\n");
        }
        
        return rapor.toString();
    }

}
