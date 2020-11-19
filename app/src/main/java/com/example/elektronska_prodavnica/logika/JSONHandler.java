package com.example.elektronska_prodavnica.logika;

import com.example.elektronska_prodavnica.domen.Korisnik;
import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.domen.Paket;
import com.example.elektronska_prodavnica.domen.Ugovor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JSONHandler {
    private static JSONHandler instanca;


    private JSONHandler() {

    }

    public static JSONHandler getInstanca() {
        if (instanca == null) {
            instanca = new JSONHandler();
        }
        return instanca;
    }

    public static ArrayList<Korisnik> napraviListuKorisnika(JSONArray korisniciJson) {
        ArrayList<Korisnik> korisnici = new ArrayList<>();
        for(int i=0;i<korisniciJson.length();i++){
            Korisnik k = new Korisnik();
            try {
                JSONObject korisnikJSON = korisniciJson.getJSONObject(i);
                if(korisnikJSON.has("korisnik_id")){
                    k.setKorisnikId(korisnikJSON.getInt("korisnik_id"));
                }
                if(korisnikJSON.has("ime")){
                    k.setIme(korisnikJSON.getString("ime"));
                }
                if(korisnikJSON.has("prezime")){
                    k.setPrezime(korisnikJSON.getString("prezime"));
                }
                if(korisnikJSON.has("korisnicko_ime")){
                    k.setKorisnickoIme(korisnikJSON.getString("korisnicko_ime"));
                }
                if(korisnikJSON.has("sifra")){
                    k.setSifra(korisnikJSON.getString("sifra"));
                }
                if(korisnikJSON.has("email")){
                    k.setEmail(korisnikJSON.getString("email"));
                }
                if(korisnikJSON.has("kontakt_telefon")){
                    k.setTelefon(korisnikJSON.getString("kontakt_telefon"));
                }
                if(korisnikJSON.has("adresa")){
                    k.setAdresa(korisnikJSON.getString("adresa"));
                }
                if(korisnikJSON.has("admin")){
                    k.setAdmin(korisnikJSON.getInt("admin"));
                }
                korisnici.add(k);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(Korisnik k : korisnici){
            System.out.println(k.getKorisnickoIme());
        }
        return korisnici;
    }

    public JSONObject napraviKorisnika(Korisnik k) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("ime", k.getIme());
            postData.put("prezime", k.getPrezime());
            postData.put("korisnicko_ime", k.getKorisnickoIme());
            postData.put("sifra", k.getSifra());
            postData.put("email", k.getEmail());
            postData.put("kontakt_telefon", k.getTelefon());
            postData.put("adresa", k.getAdresa());
            postData.put("admin", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }


    public Korisnik napraviObjekatKorisnika(JSONObject korisnikJSONBezId) {
        Korisnik k = new Korisnik();
        try {
            if(korisnikJSONBezId.has("korisnik_id")){
                k.setKorisnikId(korisnikJSONBezId.getInt("korisnik_id"));
            }
            if(korisnikJSONBezId.has("ime")){
                k.setIme(korisnikJSONBezId.getString("ime"));
            }
            if(korisnikJSONBezId.has("prezime")){
                k.setPrezime(korisnikJSONBezId.getString("prezime"));
            }
            if(korisnikJSONBezId.has("korisnicko_ime")){
                k.setKorisnickoIme(korisnikJSONBezId.getString("korisnicko_ime"));
            }
            if(korisnikJSONBezId.has("sifra")){
                k.setSifra(korisnikJSONBezId.getString("sifra"));
            }
            if(korisnikJSONBezId.has("email")){
                k.setEmail(korisnikJSONBezId.getString("email"));
            }
            if(korisnikJSONBezId.has("kontakt_telefon")){
                k.setTelefon(korisnikJSONBezId.getString("kontakt_telefon"));
            }
            if(korisnikJSONBezId.has("adresa")){
                k.setAdresa(korisnikJSONBezId.getString("adresa"));
            }
            if(korisnikJSONBezId.has("admin")){
                k.setAdmin(korisnikJSONBezId.getInt("admin"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return k;
    }

    public ArrayList<ModelTelefona> napraviListuTelefona(JSONArray telefoniJSON) {
        ArrayList<ModelTelefona> telefoni = new ArrayList<>();
        for(int i=0;i<telefoniJSON.length();i++){
            ModelTelefona mt = new ModelTelefona();
            try {
                JSONObject modelTelefonaJSON = telefoniJSON.getJSONObject(i);
                if(modelTelefonaJSON.has("model_id")){
                    mt.setModelId(modelTelefonaJSON.getInt("model_id"));
                }
                if(modelTelefonaJSON.has("proizvodjac")){
                    mt.setProizvodjac(modelTelefonaJSON.getString("proizvodjac"));
                }
                if(modelTelefonaJSON.has("naziv")){
                    mt.setNaziv(modelTelefonaJSON.getString("naziv"));
                }
                if(modelTelefonaJSON.has("masa")){
                    mt.setMasa(modelTelefonaJSON.getDouble("masa"));
                }
                if(modelTelefonaJSON.has("dimenzije")){
                    mt.setDimenzije(modelTelefonaJSON.getString("dimenzije"));
                }
                if(modelTelefonaJSON.has("kamera")){
                    mt.setKamera(modelTelefonaJSON.getString("kamera"));
                }
                if(modelTelefonaJSON.has("procesor")){
                    mt.setProcesor(modelTelefonaJSON.getString("procesor"));
                }
                if(modelTelefonaJSON.has("baterija")){
                    mt.setBaterija(modelTelefonaJSON.getString("baterija"));
                }
                if(modelTelefonaJSON.has("oprativni_sistem")){
                    mt.setOperativniSistem(modelTelefonaJSON.getString("oprativni_sistem"));
                }
                if(modelTelefonaJSON.has("memorija")){
                    mt.setMemorija(modelTelefonaJSON.getString("memorija"));
                }
                if(modelTelefonaJSON.has("slika")){
                    mt.setSlika(modelTelefonaJSON.getString("slika"));
                }
                if(modelTelefonaJSON.has("cena")){
                    mt.setCena(modelTelefonaJSON.getDouble("cena"));
                }
                telefoni.add(mt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(ModelTelefona mt : telefoni){
            System.out.println(mt.toString());
            System.out.println(mt.getBaterija());
            System.out.println(mt.getCena());
            System.out.println(mt.getDimenzije());
            System.out.println(mt.getKamera());
            System.out.println(mt.getMasa());
            System.out.println(mt.getMemorija());
            System.out.println(mt.getModelId());
        }
        return telefoni;
    }

    public ArrayList<Paket> napraviListuPaketa(JSONArray paketiJSON) {
        ArrayList<Paket> paketi = new ArrayList<>();
        for(int i=0;i<paketiJSON.length();i++){
            Paket p = new Paket();
            try {
                JSONObject paketJSON = paketiJSON.getJSONObject(i);
                if(paketJSON.has("paket_id")){
                    p.setPaketId(paketJSON.getInt("paket_id"));
                }
                if(paketJSON.has("naziv_paketa")){
                    p.setNazivPaketa(paketJSON.getString("naziv_paketa"));
                }
                if(paketJSON.has("broj_minuta")){
                    p.setBrojMinuta(paketJSON.getInt("broj_minuta"));
                }
                if(paketJSON.has("broj_sms")){
                    p.setBrojSMS(paketJSON.getInt("broj_sms"));
                }
                if(paketJSON.has("broj_mb")){
                    p.setBrojMB(paketJSON.getInt("broj_mb"));
                }
                if(paketJSON.has("cena")){
                    p.setCena(paketJSON.getDouble("cena"));
                }
                if(paketJSON.has("url")){
                    p.setUrl(paketJSON.getString("url"));
                }
                paketi.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(Paket p : paketi){
            System.out.println(p.getPaketId());
            System.out.println(p.getNazivPaketa());
            System.out.println(p.getBrojMinuta());
            System.out.println(p.getBrojSMS());
            System.out.println(p.getBrojMB());
            System.out.println(p.getCena());
            System.out.println(p.getUrl());
        }
        return paketi;
    }

    public JSONArray napraviListuUgovoraZaUbacivanje(ArrayList<Ugovor> ugovori) {
        JSONArray ugovoriJSON = new JSONArray();

        for (Ugovor u : ugovori) {

            JSONObject uJSON = new JSONObject();
            try {
                uJSON.put("datum", new Date(u.getDatum().getTime()));
                uJSON.put("trajanje_ugovora", u.getTrajanjeUgovora());
                uJSON.put("paket_id", u.getPaket().getPaketId());
                uJSON.put("korisnik_id", u.getKorisnik().getKorisnikId());
                uJSON.put("model_id", u.getModelTelefona().getModelId());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ugovoriJSON.put(uJSON);
        }
        System.out.println("Za ubac: "+ugovoriJSON.toString());
        return ugovoriJSON;
    }

    public ArrayList<Ugovor> napraviListuUgovora(JSONArray ugovoriJSON) {
        ArrayList<Ugovor> ugovori = new ArrayList<>();
        for(int i=0;i<ugovoriJSON.length();i++){
            Ugovor u = new Ugovor();
            Korisnik k = new Korisnik();
            ModelTelefona mt = new ModelTelefona();
            Paket p = new Paket();
            try {
                JSONObject ugovorJSON = ugovoriJSON.getJSONObject(i);
                if(ugovorJSON.has("paket_id")){
                    p.setPaketId(ugovorJSON.getInt("paket_id"));
                }
                if(ugovorJSON.has("naziv_paketa")){
                    p.setNazivPaketa(ugovorJSON.getString("naziv_paketa"));
                }
                if(ugovorJSON.has("broj_minuta")){
                    p.setBrojMinuta(ugovorJSON.getInt("broj_minuta"));
                }
                if(ugovorJSON.has("broj_sms")){
                    p.setBrojSMS(ugovorJSON.getInt("broj_sms"));
                }
                if(ugovorJSON.has("broj_mb")){
                    p.setBrojMB(ugovorJSON.getInt("broj_mb"));
                }
                if(ugovorJSON.has("url")){
                    p.setUrl(ugovorJSON.getString("url"));
                }

                if(ugovorJSON.has("model_id")){
                    mt.setModelId(ugovorJSON.getInt("model_id"));
                }
                if(ugovorJSON.has("proizvodjac")){
                    mt.setProizvodjac(ugovorJSON.getString("proizvodjac"));
                }
                if(ugovorJSON.has("naziv")){
                    mt.setNaziv(ugovorJSON.getString("naziv"));
                }
                if(ugovorJSON.has("masa")){
                    mt.setMasa(ugovorJSON.getDouble("masa"));
                }
                if(ugovorJSON.has("dimenzije")){
                    mt.setDimenzije(ugovorJSON.getString("dimenzije"));
                }
                if(ugovorJSON.has("kamera")){
                    mt.setKamera(ugovorJSON.getString("kamera"));
                }
                if(ugovorJSON.has("procesor")){
                    mt.setProcesor(ugovorJSON.getString("procesor"));
                }
                if(ugovorJSON.has("baterija")){
                    mt.setBaterija(ugovorJSON.getString("baterija"));
                }
                if(ugovorJSON.has("oprativni_sistem")){
                    mt.setOperativniSistem(ugovorJSON.getString("oprativni_sistem"));
                }
                if(ugovorJSON.has("memorija")){
                    mt.setMemorija(ugovorJSON.getString("memorija"));
                }
                if(ugovorJSON.has("slika")){
                    mt.setSlika(ugovorJSON.getString("slika"));
                }
                if(ugovorJSON.has("cena")){
                    mt.setCena(ugovorJSON.getDouble("cena"));
                }

                if(ugovorJSON.has("korisnik_id")){
                    k.setKorisnikId(ugovorJSON.getInt("korisnik_id"));
                }
                if(ugovorJSON.has("ime")){
                    k.setIme(ugovorJSON.getString("ime"));
                }
                if(ugovorJSON.has("prezime")){
                    k.setPrezime(ugovorJSON.getString("prezime"));
                }
                if(ugovorJSON.has("korisnicko_ime")){
                    k.setKorisnickoIme(ugovorJSON.getString("korisnicko_ime"));
                }
                if(ugovorJSON.has("sifra")){
                    k.setSifra(ugovorJSON.getString("sifra"));
                }
                if(ugovorJSON.has("email")){
                    k.setEmail(ugovorJSON.getString("email"));
                }
                if(ugovorJSON.has("kontakt_telefon")){
                    k.setTelefon(ugovorJSON.getString("kontakt_telefon"));
                }
                if(ugovorJSON.has("adresa")){
                    k.setAdresa(ugovorJSON.getString("adresa"));
                }
                if(ugovorJSON.has("admin")){
                    k.setAdmin(ugovorJSON.getInt("admin"));
                }

                if(ugovorJSON.has("ugovor_id")){
                    u.setUgovorId(ugovorJSON.getInt("ugovor_id"));
                }
                if(ugovorJSON.has("trajanje_ugovora")){
                    u.setTrajanjeUgovora(ugovorJSON.getInt("trajanje_ugovora"));
                }
                if(ugovorJSON.has("datum")){
                    String date = ugovorJSON.getString("datum");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date datum = sdf1.parse(date);
                    u.setDatum(datum);
                }

                u.setKorisnik(k);
                u.setModelTelefona(mt);
                u.setPaket(p);
                ugovori.add(u);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(Ugovor u : ugovori){
            System.out.println(u.getUgovorId());
            System.out.println(u.getModelTelefona().toString());
            System.out.println(u.getPaket().getNazivPaketa());
            System.out.println(u.getKorisnik().toString());
        }
        return ugovori;
    }

    public JSONObject napraviModelTelefona(ModelTelefona mt) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("proizvodjac", mt.getProizvodjac());
            postData.put("naziv", mt.getNaziv());
            postData.put("masa", mt.getMasa());
            postData.put("dimenzije", mt.getDimenzije());
            postData.put("kamera", mt.getKamera());
            postData.put("procesor", mt.getProcesor());
            postData.put("baterija", mt.getBaterija());
            postData.put("oprativni_sistem", mt.getOperativniSistem());
            postData.put("memorija", mt.getMemorija());
            postData.put("slika", mt.getSlika());
            postData.put("cena", mt.getCena());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public ModelTelefona napraviObjekatTelefona(JSONObject telefonJSONBezId) {
        ModelTelefona mt = new ModelTelefona();
        try {
            if(telefonJSONBezId.has("model_id")){
                mt.setModelId(telefonJSONBezId.getInt("model_id"));
            }
            if(telefonJSONBezId.has("proizvodjac")){
                mt.setProizvodjac(telefonJSONBezId.getString("proizvodjac"));
            }
            if(telefonJSONBezId.has("naziv")){
                mt.setNaziv(telefonJSONBezId.getString("naziv"));
            }
            if(telefonJSONBezId.has("masa")){
                mt.setMasa(telefonJSONBezId.getDouble("masa"));
            }
            if(telefonJSONBezId.has("dimenzije")){
                mt.setDimenzije(telefonJSONBezId.getString("dimenzije"));
            }
            if(telefonJSONBezId.has("kamera")){
                mt.setKamera(telefonJSONBezId.getString("kamera"));
            }
            if(telefonJSONBezId.has("procesor")){
                mt.setProcesor(telefonJSONBezId.getString("procesor"));
            }
            if(telefonJSONBezId.has("baterija")){
                mt.setBaterija(telefonJSONBezId.getString("baterija"));
            }
            if(telefonJSONBezId.has("oprativni_sistem")){
                mt.setOperativniSistem(telefonJSONBezId.getString("oprativni_sistem"));
            }
            if(telefonJSONBezId.has("memorija")){
                mt.setMemorija(telefonJSONBezId.getString("memorija"));
            }
            if(telefonJSONBezId.has("slika")){
                mt.setSlika(telefonJSONBezId.getString("slika"));
            }
            if(telefonJSONBezId.has("cena")){
                mt.setCena(telefonJSONBezId.getDouble("cena"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mt;
    }

    public JSONObject napraviPaket(Paket p) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("naziv_paketa", p.getNazivPaketa());
            postData.put("broj_minuta", p.getBrojMinuta());
            postData.put("broj_sms", p.getBrojSMS());
            postData.put("broj_mb", p.getBrojMB());
            postData.put("cena", p.getCena());
            postData.put("url", p.getUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public Paket napraviObjekatPaketa(JSONObject paketJSONBezId) {
        Paket p = new Paket();
        try {
            if(paketJSONBezId.has("paket_id")){
                p.setPaketId(paketJSONBezId.getInt("paket_id"));
            }
            if(paketJSONBezId.has("naziv_paketa")){
                p.setNazivPaketa(paketJSONBezId.getString("naziv_paketa"));
            }
            if(paketJSONBezId.has("broj_minuta")){
                p.setBrojMinuta(paketJSONBezId.getInt("broj_minuta"));
            }
            if(paketJSONBezId.has("broj_sms")){
                p.setBrojSMS(paketJSONBezId.getInt("broj_sms"));
            }
            if(paketJSONBezId.has("broj_mb")){
                p.setBrojMB(paketJSONBezId.getInt("broj_mb"));
            }
            if(paketJSONBezId.has("cena")){
                p.setCena(paketJSONBezId.getDouble("cena"));
            }
            if(paketJSONBezId.has("url")){
                p.setUrl(paketJSONBezId.getString("url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }
}
