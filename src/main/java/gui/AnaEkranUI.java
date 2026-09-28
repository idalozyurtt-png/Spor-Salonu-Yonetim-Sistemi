/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import service.UyeService;
import model.Uye;
import model.PremiumUye;
import model.StandartUye;

/**
 *
 * @author idalozyurt
 */
public class AnaEkranUI extends JFrame {
    private UyeService uyeService;
    private JTable uyeTablo;
    private DefaultTableModel tableModel;
    private JTextField aramaField;

    public AnaEkranUI() {
        uyeService = new UyeService();
        initializeUI();
        uyeleriTabloyaYukle();
    }

    private void initializeUI() {
        setTitle("Üye Yönetim Sistemi");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel aramaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aramaPanel.add(new JLabel("Ara:"));
        aramaField = new JTextField(20);
        aramaPanel.add(aramaField);
        
        JButton araButton = new JButton("Ara");
        araButton.addActionListener(e -> aramaYap());
        aramaPanel.add(araButton);
        
        JButton temizleButton = new JButton("Temizle");
        temizleButton.addActionListener(e -> {
            aramaField.setText("");
            uyeleriTabloyaYukle();
        });
        aramaPanel.add(temizleButton);

        String[] columns = {"ID", "Ad", "Soyad", "Üyelik Türü", "Başlangıç Tarihi", "Aylık Ücret"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        uyeTablo = new JTable(tableModel);
        uyeTablo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(uyeTablo);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton ekleButton = new JButton("Yeni Üye");
        ekleButton.addActionListener(e -> yeniUyeEkle());
        JButton duzenleButton = new JButton("Düzenle");
        duzenleButton.addActionListener(e -> uyeDuzenle());
        JButton silButton = new JButton("Sil");
        silButton.addActionListener(e -> uyeSil());
        JButton yenileButton = new JButton("Yenile");
        yenileButton.addActionListener(e -> uyeleriTabloyaYukle());
        JButton raporButton = new JButton("Rapor Oluştur");
        raporButton.addActionListener(e -> raporOlustur());
        JButton siralamaButton = new JButton("Sırala (A-Z)");
        siralamaButton.addActionListener(e -> uyeleriSirala());
        
        buttonPanel.add(ekleButton);
        buttonPanel.add(duzenleButton);
        buttonPanel.add(silButton);
        buttonPanel.add(yenileButton);
        buttonPanel.add(raporButton);
        buttonPanel.add(siralamaButton);
        
        mainPanel.add(aramaPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void uyeleriTabloyaYukle() {
        try {
            tableModel.setRowCount(0);
            List<Uye> uyeler = uyeService.tümüyelerigetir();
            
            for (Uye uye : uyeler) {
                Object[] row = new Object[6];
                row[0] = uye.getid();
                row[1] = uye.getad();
                row[2] = uye.getsoyad();
                row[3] = uye.getuyelikturu();
                row[4] = uye.baslangıctarihi().toString();
                row[5] = String.format("%.2f TL", uye.aylikUcret());
                
                tableModel.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Üyeler yüklenirken hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void aramaYap() {
    try {
        String aramaKriteri = aramaField.getText().trim();
        
        if (aramaKriteri.isEmpty()) {
            uyeleriTabloyaYukle();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Uye> sonuclar = uyeService.uyeAra(aramaKriteri);
        
        for (Uye uye : sonuclar) {
            Object[] row = new Object[6];
            row[0] = uye.getid();
            row[1] = uye.getad();
            row[2] = uye.getsoyad();
            row[3] = uye.getuyelikturu();
            row[4] = uye.baslangıctarihi().toString();
            row[5] = String.format("%.2f TL", uye.aylikUcret());
            
            tableModel.addRow(row);
        }
        
        if (sonuclar.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "'" + aramaKriteri + "' için sonuç bulunamadı.",
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Arama sırasında hata: " + e.getMessage(), 
            "Hata", JOptionPane.ERROR_MESSAGE);
    }
}

    private void uyeleriSirala() {
        try {
            tableModel.setRowCount(0);
            List<Uye> siraliListe = uyeService.uyeleriSirala();
            
            for (Uye uye : siraliListe) {
                Object[] row = new Object[6];
                row[0] = uye.getid();
                row[1] = uye.getad();
                row[2] = uye.getsoyad();
                row[3] = uye.getuyelikturu();
                row[4] = uye.baslangıctarihi().toString();
                row[5] = String.format("%.2f TL", uye.aylikUcret());
                
                tableModel.addRow(row);
            }
            
            JOptionPane.showMessageDialog(this, "Üyeler başarıyla sıralandı.", 
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sıralama sırasında hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void yeniUyeEkle() {
        try {
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            
            JTextField adField = new JTextField();
            JTextField soyadField = new JTextField();
            JComboBox<String> turCombo = new JComboBox<>(new String[]{"Standart", "Premium"});
            JTextField tarihField = new JTextField(LocalDate.now().toString());
            JTextField ekUcretField = new JTextField("0.0");
            ekUcretField.setEnabled(false);
            
            turCombo.addActionListener(e -> {
                ekUcretField.setEnabled("Premium".equals(turCombo.getSelectedItem()));
            });
            
            panel.add(new JLabel("Ad:"));
            panel.add(adField);
            panel.add(new JLabel("Soyad:"));
            panel.add(soyadField);
            panel.add(new JLabel("Üyelik Türü:"));
            panel.add(turCombo);
            panel.add(new JLabel("Başlangıç Tarihi (YYYY-AA-GG):"));
            panel.add(tarihField);
            panel.add(new JLabel("Ek Hizmet Ücreti:"));
            panel.add(ekUcretField);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Yeni Üye Ekle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                
                String ad = adField.getText().trim();
                String soyad = soyadField.getText().trim();
                
                if (ad.isEmpty() || soyad.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ad ve soyad alanları boş olamaz!", 
                        "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LocalDate tarih;
                try {
                    tarih = LocalDate.parse(tarihField.getText().trim());
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Geçersiz tarih formatı! YYYY-AA-GG formatında girin.", 
                        "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double ekUcret = 0.0;
                String tur = (String) turCombo.getSelectedItem();
                
                if ("Premium".equals(tur)) {
                    try {
                        ekUcret = Double.parseDouble(ekUcretField.getText().trim());
                        if (ekUcret < 0) {
                            JOptionPane.showMessageDialog(this, "Ek ücret negatif olamaz!", 
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Geçersiz ek ücret formatı!", 
                            "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                Uye yeniUye;
                if ("Premium".equals(tur)) {
                    yeniUye = new PremiumUye(0, ad, soyad, tur, tarih, ekUcret);
                } else {
                    yeniUye = new StandartUye(0, ad, soyad, tur, tarih);
                }
                
                // Servise ekle
                uyeService.uyeEkle(yeniUye);
                
                uyeleriTabloyaYukle();
                
                JOptionPane.showMessageDialog(this, "Üye başarıyla eklendi.", 
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Üye eklerken hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uyeDuzenle() {
        try {
            int seciliSatir = uyeTablo.getSelectedRow();
            if (seciliSatir < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen düzenlemek için bir üye seçin.", 
                    "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int uyeId = (int) tableModel.getValueAt(seciliSatir, 0);
            String ad = (String) tableModel.getValueAt(seciliSatir, 1);
            String soyad = (String) tableModel.getValueAt(seciliSatir, 2);
            String tur = (String) tableModel.getValueAt(seciliSatir, 3);
            String tarihStr = (String) tableModel.getValueAt(seciliSatir, 4);
           
            double ekUcret = 0.0;
            List<Uye> uyeler = uyeService.tümüyelerigetir();
            for (Uye u : uyeler) {
                if (u.getid() == uyeId && u instanceof PremiumUye) {
                    ekUcret = ((PremiumUye) u).getekHizmetUcreti();
                    break;
                }
            }
            
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            
            JTextField adField = new JTextField(ad);
            JTextField soyadField = new JTextField(soyad);
            JComboBox<String> turCombo = new JComboBox<>(new String[]{"Standart", "Premium"});
            turCombo.setSelectedItem(tur);
            JTextField tarihField = new JTextField(tarihStr);
            JTextField ekUcretField = new JTextField(String.valueOf(ekUcret));
            ekUcretField.setEnabled("Premium".equals(tur));
            
            turCombo.addActionListener(e -> {
                ekUcretField.setEnabled("Premium".equals(turCombo.getSelectedItem()));
            });
            
            panel.add(new JLabel("Ad:"));
            panel.add(adField);
            panel.add(new JLabel("Soyad:"));
            panel.add(soyadField);
            panel.add(new JLabel("Üyelik Türü:"));
            panel.add(turCombo);
            panel.add(new JLabel("Başlangıç Tarihi (YYYY-AA-GG):"));
            panel.add(tarihField);
            panel.add(new JLabel("Ek Hizmet Ücreti:"));
            panel.add(ekUcretField);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Üye Düzenle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                
                String yeniAd = adField.getText().trim();
                String yeniSoyad = soyadField.getText().trim();
                
                if (yeniAd.isEmpty() || yeniSoyad.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ad ve soyad alanları boş olamaz!", 
                        "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LocalDate tarih;
                try {
                    tarih = LocalDate.parse(tarihField.getText().trim());
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Geçersiz tarih formatı! YYYY-AA-GG formatında girin.", 
                        "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double yeniEkUcret = 0.0;
                String yeniTur = (String) turCombo.getSelectedItem();
                
                if ("Premium".equals(yeniTur)) {
                    try {
                        yeniEkUcret = Double.parseDouble(ekUcretField.getText().trim());
                        if (yeniEkUcret < 0) {
                            JOptionPane.showMessageDialog(this, "Ek ücret negatif olamaz!", 
                                "Hata", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Geçersiz ek ücret formatı!", 
                            "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                Uye guncelUye;
                if ("Premium".equals(yeniTur)) {
                    guncelUye = new PremiumUye(uyeId, yeniAd, yeniSoyad, yeniTur, tarih, yeniEkUcret);
                } else {
                    guncelUye = new StandartUye(uyeId, yeniAd, yeniSoyad, yeniTur, tarih);
                }
                
                uyeService.uyegüncelle(guncelUye);
                uyeleriTabloyaYukle();
                
                JOptionPane.showMessageDialog(this, "Üye başarıyla güncellendi.", 
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Üye düzenlerken hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uyeSil() {
        try {
            int seciliSatir = uyeTablo.getSelectedRow();
            if (seciliSatir < 0) {
                JOptionPane.showMessageDialog(this, "Lütfen silmek için bir üye seçin.", 
                    "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int uyeId = (int) tableModel.getValueAt(seciliSatir, 0);
            String ad = (String) tableModel.getValueAt(seciliSatir, 1);
            String soyad = (String) tableModel.getValueAt(seciliSatir, 2);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                ad + " " + soyad + " isimli üyeyi silmek istediğinize emin misiniz?", 
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                uyeService.uyesil(uyeId);
                uyeleriTabloyaYukle();
                JOptionPane.showMessageDialog(this, "Üye başarıyla silindi.", 
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Üye silerken hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void raporOlustur() {
        try {
            String rapor = uyeService.raporBilgisi();
            
            JTextArea textArea = new JTextArea(rapor);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                "Üye Raporu", JOptionPane.INFORMATION_MESSAGE);
            
            uyeService.dosyaIslemleri.exportRapor(rapor);
            JOptionPane.showMessageDialog(this, "Rapor başarıyla kaydedildi.", 
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Rapor oluştururken hata: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}