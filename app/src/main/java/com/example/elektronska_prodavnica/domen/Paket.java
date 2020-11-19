package com.example.elektronska_prodavnica.domen;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Paket implements Serializable {
    private int paketId;
    private String nazivPaketa;
    private int brojMinuta;
    private int brojSMS;
    private int brojMB;
    private double cena;
    private String url;

    public Paket(){

    }

    public Paket(int paketId, String nazivPaketa, int brojMinuta, int brojSMS, int brojMB, double cena, String url){
        this.paketId = paketId;
        this.nazivPaketa = nazivPaketa;
        this.brojMinuta = brojMinuta;
        this.brojSMS = brojSMS;
        this.brojMB = brojMB;
        this.cena = cena;
        this.url = url;
    }

    public int getPaketId() {
        return paketId;
    }

    public void setPaketId(int paketId) {
        this.paketId = paketId;
    }

    public String getNazivPaketa() {
        return nazivPaketa;
    }

    public void setNazivPaketa(String nazivPaketa) {
        this.nazivPaketa = nazivPaketa;
    }

    public int getBrojMinuta() {
        return brojMinuta;
    }

    public void setBrojMinuta(int brojMinuta) {
        this.brojMinuta = brojMinuta;
    }

    public int getBrojSMS() {
        return brojSMS;
    }

    public void setBrojSMS(int brojSMS) {
        this.brojSMS = brojSMS;
    }

    public int getBrojMB() {
        return brojMB;
    }

    public void setBrojMB(int brojMB) {
        this.brojMB = brojMB;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return nazivPaketa;
    }
}
