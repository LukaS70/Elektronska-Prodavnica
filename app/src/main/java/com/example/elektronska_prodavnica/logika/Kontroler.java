package com.example.elektronska_prodavnica.logika;

import android.widget.EditText;
import android.widget.Toast;

import com.example.elektronska_prodavnica.SignupActivity;
import com.example.elektronska_prodavnica.domen.Korisnik;
import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.domen.Paket;
import com.example.elektronska_prodavnica.domen.Ugovor;
import com.example.elektronska_prodavnica.prevalent.Cart;
import com.example.elektronska_prodavnica.prevalent.Prevalent;
import com.example.elektronska_prodavnica.servis.Komunikacija;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kontroler {
    private static Kontroler instanca;
    private Komunikacija kom;

    private Kontroler() {
        kom = new Komunikacija();
    }

    public static Kontroler getInstanca() {
        if(instanca == null){
            instanca = new Kontroler();
        }
        return instanca;
    }


    public Korisnik registrujKorisnika(String url, String podaci) {
        JSONObject korisnikJSONBezId = kom.ubaciKorisnika(url,podaci);
        Korisnik registrovaniKorisnik = JSONHandler.getInstanca().napraviObjekatKorisnika(korisnikJSONBezId);
        JSONArray korisniciJSON = kom.vratiListu("http://10.0.2.2/flight/korisnik.json");
        ArrayList<Korisnik> korisnici = JSONHandler.napraviListuKorisnika(korisniciJSON);
        for(Korisnik k:korisnici){
            if(registrovaniKorisnik.getKorisnickoIme().equals(k.getKorisnickoIme())){
                registrovaniKorisnik.setKorisnikId(k.getKorisnikId());
            }
        }
        return registrovaniKorisnik;
    }

    public JSONArray vratiSveKorisnike(String param) {
        return kom.vratiListu(param);
    }

    public boolean daLiPostoji(JSONArray korisniciJSON, String korisnickoIme){
        ArrayList<Korisnik> korisnici = JSONHandler.napraviListuKorisnika(korisniciJSON);
        for (Korisnik k : korisnici){
            if(k.getKorisnickoIme().equals(korisnickoIme)){
                return true;
            }
        }
        return false;
    }

    public Korisnik ulogujSe(String url, String korisnickoIme, String sifra) {
        JSONArray korisniciJSON = kom.vratiListu(url);
        ArrayList<Korisnik> korisnici = JSONHandler.napraviListuKorisnika(korisniciJSON);
        for (Korisnik k : korisnici){
            if(k.getKorisnickoIme().equals(korisnickoIme) && k.getSifra().equals(sifra)){
                return k;
            }
        }
        return null;
    }

    public ArrayList<ModelTelefona> vratiSveTelefone(String url) {
        JSONArray telefoniJSON = kom.vratiListu(url);
        ArrayList<ModelTelefona> telefoni = JSONHandler.getInstanca().napraviListuTelefona(telefoniJSON);
        return telefoni;
    }

    public ArrayList<Paket> vratiSvePakete(String url) {
        JSONArray paketiJSON = kom.vratiListu(url);
        ArrayList<Paket> paketi = JSONHandler.getInstanca().napraviListuPaketa(paketiJSON);
        return paketi;
    }

    public ArrayList<String> popuniSpiner(String param) {
        ArrayList<Paket> paketi = vratiSvePakete(param);
        ArrayList<String> imenaPaketa = new ArrayList<>();
        for (Paket p : paketi){
            imenaPaketa.add(p.getNazivPaketa());
        }
        return imenaPaketa;
    }

    public Paket vratiPaket(String url,String imePaketa) {
        ArrayList<Paket> paketi = vratiSvePakete(url);
        for(Paket p : paketi){
            if(imePaketa.equals(p.getNazivPaketa())){
                return p;
            }
        }
        return null;
    }

    public void ubaciUKorpu(ModelTelefona mt, Paket p) {

        Ugovor u = new Ugovor(-1,new Date(),24,p, Prevalent.getInstanca().getUlogovaniKorisnik(),mt);
        Cart.getInstanca().getKorpa().add(u);
        System.out.println(Cart.getInstanca().getKorpa().get(0).getDatum());
        System.out.println(Cart.getInstanca().getKorpa().get(0).getKorisnik().toString());
        System.out.println(Cart.getInstanca().getKorpa().get(0).getModelTelefona().toString());
        System.out.println(Cart.getInstanca().getKorpa().get(0).getPaket().getNazivPaketa());
    }

    public void ubaciUgovore(ArrayList<Ugovor> ugovori,String url) {
        JSONArray ugovoriJSON = JSONHandler.getInstanca().napraviListuUgovoraZaUbacivanje(ugovori);
        kom.ubaciUgovore(ugovoriJSON,url);

    }

    public ArrayList<ModelTelefona> pretraziModele(ArrayList<ModelTelefona> telefoni,String imeModela, String izabraniProizvodjac, String izabraniRam) {
        ArrayList<ModelTelefona> telefoniZaPrikaz = new ArrayList<>();
        ArrayList<ModelTelefona> telefoniZaImeModela = new ArrayList<>();
        ArrayList<ModelTelefona> telefoniZaProizvodjaca = new ArrayList<>();
        ArrayList<ModelTelefona> telefoniZaRam = new ArrayList<>();
        System.out.println(imeModela);
        System.out.println(izabraniProizvodjac);
        System.out.println(izabraniRam);

        for(ModelTelefona telefon : telefoni){
            if(!imeModela.isEmpty()){
                if(telefon.toString().toLowerCase().contains(imeModela.toLowerCase())){
                    telefoniZaImeModela.add(telefon);
                }
            }
            if(!izabraniProizvodjac.isEmpty()){
                if(telefon.getProizvodjac().equals(izabraniProizvodjac)){
                    telefoniZaProizvodjaca.add(telefon);
                }
            }
            if(!izabraniRam.isEmpty()){
                String[] n = telefon.getMemorija().split(""); //array of strings
                StringBuffer f = new StringBuffer(); // buffer to store numbers
                String s = telefon.getMemorija();
                Matcher matcher = Pattern.compile("\\d+").matcher(s);
                matcher.find();
                int ram = Integer.valueOf(matcher.group());
                System.out.println(ram);
                switch (izabraniRam){
                case "4GB":
                    if(ram < 4){
                        telefoniZaRam.add(telefon);
                    }
                    break;
                case "4":
                    if(ram == 4){
                        telefoniZaRam.add(telefon);
                    }
                    break;
                case "6":
                    if(ram == 6){
                        telefoniZaRam.add(telefon);
                    }
                    break;
                case "6GB":
                    if(ram > 6){
                        telefoniZaRam.add(telefon);
                    }
                    break;
               }
            }
        }

//        System.out.println(telefoniZaImeModela.get(0).toString());
//        System.out.println(telefoniZaProizvodjaca.get(0).toString());
//        System.out.println(telefoniZaRam.get(0).toString());

        if(!imeModela.isEmpty() && !izabraniProizvodjac.isEmpty() && !izabraniRam.isEmpty()){
            for (ModelTelefona t1 : telefoniZaImeModela){
                for(ModelTelefona t2: telefoniZaProizvodjaca){
                    for(ModelTelefona t3 : telefoniZaRam){
                        if (t1.getModelId() == t2.getModelId() && t1.getModelId()==t3.getModelId()){
                            telefoniZaPrikaz.add(t1);
                        }
                    }
                }
            }
            return telefoniZaPrikaz;
        }
        if(!imeModela.isEmpty() && !izabraniProizvodjac.isEmpty() && izabraniRam.isEmpty()){
            for (ModelTelefona t1 : telefoniZaImeModela){
                for(ModelTelefona t2: telefoniZaProizvodjaca){
                    if (t1.getModelId() == t2.getModelId()){
                        telefoniZaPrikaz.add(t1);
                    }
                }
            }
            return telefoniZaPrikaz;
        }
        if(!imeModela.isEmpty() && izabraniProizvodjac.isEmpty() && !izabraniRam.isEmpty()){
            for (ModelTelefona t1 : telefoniZaImeModela){
                for(ModelTelefona t3 : telefoniZaRam){
                    if (t1.getModelId() == t3.getModelId()){
                        telefoniZaPrikaz.add(t1);
                    }
                }
            }
            return telefoniZaPrikaz;
        }
        if(imeModela.isEmpty() && !izabraniProizvodjac.isEmpty() && !izabraniRam.isEmpty()){
            for(ModelTelefona t2: telefoniZaProizvodjaca){
                for(ModelTelefona t3 : telefoniZaRam){
                    if (t2.getModelId() == t3.getModelId()){
                        telefoniZaPrikaz.add(t2);
                    }
                }
            }
            return telefoniZaPrikaz;
        }
        if(!imeModela.isEmpty() && izabraniProizvodjac.isEmpty() && izabraniRam.isEmpty()){
            telefoniZaPrikaz = telefoniZaImeModela;
            return telefoniZaPrikaz;
        }
        if(imeModela.isEmpty() && !izabraniProizvodjac.isEmpty() && izabraniRam.isEmpty()){
            telefoniZaPrikaz = telefoniZaProizvodjaca;
            return telefoniZaPrikaz;
        }
        if(imeModela.isEmpty() && izabraniProizvodjac.isEmpty() && !izabraniRam.isEmpty()){
            telefoniZaPrikaz = telefoniZaRam;
            return telefoniZaPrikaz;
        }
        return  telefoniZaPrikaz;
    }

    public ArrayList<Ugovor> vratiSveUgovore(String url) {
        JSONArray ugovoriJSON = kom.vratiListu(url);
        ArrayList<Ugovor> ugovori = JSONHandler.getInstanca().napraviListuUgovora(ugovoriJSON);
        return ugovori;
    }

    public ArrayList<ModelTelefona> daLiPostojiTelefon(String param, ModelTelefona mt) {
        ArrayList<ModelTelefona> telefoni = vratiSveTelefone(param);
        ArrayList<ModelTelefona> isti = new ArrayList<>();
        for(ModelTelefona t : telefoni){
            if(t.getNaziv().equals(mt.getNaziv())){
                isti.add(t);
            }
        }
        return isti;
    }

    public ModelTelefona ubaciTelefon(String url, String podaci) {
        JSONObject telefonJSONBezId = kom.ubaciKorisnika(url,podaci);//ubaviKorisnika == ubaci za svaki tip
        ModelTelefona ubacenTelefon = JSONHandler.getInstanca().napraviObjekatTelefona(telefonJSONBezId);
        JSONArray telefoniJSON = kom.vratiListu("http://10.0.2.2/flight/model_telefona.json");
        ArrayList<ModelTelefona> telefoni = JSONHandler.getInstanca().napraviListuTelefona(telefoniJSON);
        for(ModelTelefona t : telefoni){
            if(ubacenTelefon.getNaziv().equals(t.getNaziv())){
                ubacenTelefon.setModelId(t.getModelId());
            }
        }
        return ubacenTelefon;
    }

    public ArrayList<Paket> daLiPostojiPaket(String param, Paket p) {
        ArrayList<Paket> paketi = vratiSvePakete(param);
        ArrayList<Paket> isti = new ArrayList<>();
        for(Paket paket : paketi){
            if(paket.getNazivPaketa().equals(p.getNazivPaketa())){
                isti.add(paket);
            }
        }
        return isti;
    }

    public Paket ubaciPaket(String url, String podaci) {
        JSONObject paketJSONBezId = kom.ubaciKorisnika(url,podaci);//ubaviKorisnika == ubaci za svaki tip
        Paket ubacenPaket = JSONHandler.getInstanca().napraviObjekatPaketa(paketJSONBezId);
        JSONArray paketiJSON = kom.vratiListu("http://10.0.2.2/flight/paket.json");
        ArrayList<Paket> paketi = JSONHandler.getInstanca().napraviListuPaketa(paketiJSON);
        for(Paket p : paketi){
            if(ubacenPaket.getNazivPaketa().equals(p.getNazivPaketa())){
                ubacenPaket.setPaketId(p.getPaketId());
            }
        }
        return ubacenPaket;
    }
}
