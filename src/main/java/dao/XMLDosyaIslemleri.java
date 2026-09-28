/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import model.PremiumUye;
import model.StandartUye;
import model.Uye;

/**
 *
 * @author idalozyurt
 */
public class XMLDosyaIslemleri implements IDosyaIslemleri {
    private static final String DOSYA_ADI = "üyeler.xml";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<Uye> verileriYükle() {
        List<Uye> uyeler = new ArrayList<>();
        
        try {
            File xmlFile = new File(DOSYA_ADI);
            if (!xmlFile.exists()) {
                return uyeler; 
            }
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("uye");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    int id = Integer.parseInt(element.getAttribute("id"));
                    String ad = element.getElementsByTagName("ad").item(0).getTextContent();
                    String soyad = element.getElementsByTagName("soyad").item(0).getTextContent();
                    String tur = element.getElementsByTagName("tur").item(0).getTextContent();
                    LocalDate baslangicTarihi = LocalDate.parse(
                        element.getElementsByTagName("baslangicTarihi").item(0).getTextContent(), 
                        formatter
                    );
                    
                    if (tur.equalsIgnoreCase("Premium")) {
                        double ekHizmetUcreti = 0.0;
                        NodeList ekHizmetList = element.getElementsByTagName("ekHizmetUcreti");
                        if (ekHizmetList.getLength() > 0) {
                            ekHizmetUcreti = Double.parseDouble(ekHizmetList.item(0).getTextContent());
                        }
                        uyeler.add(new PremiumUye(id, ad, soyad, tur, baslangicTarihi, ekHizmetUcreti));
                    } else {
                        uyeler.add(new StandartUye(id, ad, soyad, tur, baslangicTarihi));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("XML yükleme hatası: " + e.getMessage());
            e.printStackTrace();
        }
        
        return uyeler;
    }
@Override
public void verileriKaydet(List<Uye> uyeler) {
    try {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("uyeler");
        doc.appendChild(rootElement);
        
        for (Uye uye : uyeler) {
            Element uyeElement = doc.createElement("uye");
            uyeElement.setAttribute("id", String.valueOf(uye.getid()));
            
            Element adElement = doc.createElement("ad");
            adElement.appendChild(doc.createTextNode(uye.getad()));
            uyeElement.appendChild(adElement);
            
            Element soyadElement = doc.createElement("soyad");
            soyadElement.appendChild(doc.createTextNode(uye.getsoyad()));
            uyeElement.appendChild(soyadElement);
            
            Element turElement = doc.createElement("tur");
            turElement.appendChild(doc.createTextNode(uye.getuyelikturu()));
            uyeElement.appendChild(turElement);
            
            Element tarihElement = doc.createElement("baslangicTarihi");
            tarihElement.appendChild(doc.createTextNode(uye.baslangıctarihi().format(formatter)));
            uyeElement.appendChild(tarihElement);
            
            if (uye instanceof PremiumUye) {
                PremiumUye premiumUye = (PremiumUye) uye;
                Element ekUcretElement = doc.createElement("ekHizmetUcreti");
                ekUcretElement.appendChild(doc.createTextNode(String.valueOf(premiumUye.getekHizmetUcreti())));
                uyeElement.appendChild(ekUcretElement);
            }
            
            rootElement.appendChild(uyeElement);
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(DOSYA_ADI));
        
        transformer.transform(source, result);
        
    } catch (Exception e) {
        System.err.println("XML kaydetme hatası: " + e.getMessage());
        e.printStackTrace();
    }
}
    @Override
    public void exportRapor(String rapor) {
        try (FileWriter writer = new FileWriter("rapor_" + System.currentTimeMillis() + ".txt")) {
            writer.write(rapor);
            System.out.println("Rapor başarıyla kaydedildi.");
        } catch (IOException e) {
            System.err.println("Rapor kaydetme hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}