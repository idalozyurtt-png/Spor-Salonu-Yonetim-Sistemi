/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;
import java.util.List;
import model.Uye;
/**
 *
 * @author idalozyurt
 */
public interface IDosyaIslemleri {

    List<Uye> verileriYükle();
    void verileriKaydet( List<Uye> üyeler);
    void exportRapor(String rapor);

}
