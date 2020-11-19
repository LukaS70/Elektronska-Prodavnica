package com.example.elektronska_prodavnica.domen;

import java.io.Serializable;
import java.util.Date;

public class Ugovor implements Serializable {
    private int ugovorId;
    private Date datum;
    private int trajanjeUgovora;
    private Paket paket;
    private Korisnik korisnik;
    private ModelTelefona modelTelefona;

    public Ugovor(){

    }

    public Ugovor(int ugovorId, Date datum, int trajanjeUgovora, Paket paket, Korisnik korisnik, ModelTelefona modelTelefona){
        this.ugovorId = ugovorId;
        this.datum = datum;
        this.trajanjeUgovora = trajanjeUgovora;
        this.paket = paket;
        this.korisnik = korisnik;
        this.modelTelefona = modelTelefona;
    }

    public int getUgovorId() {
        return ugovorId;
    }

    public void setUgovorId(int ugovorId) {
        this.ugovorId = ugovorId;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getTrajanjeUgovora() {
        return trajanjeUgovora;
    }

    public void setTrajanjeUgovora(int trajanjeUgovora) {
        this.trajanjeUgovora = trajanjeUgovora;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
        this.paket = paket;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public ModelTelefona getModelTelefona() {
        return modelTelefona;
    }

    public void setModelTelefona(ModelTelefona modelTelefona) {
        this.modelTelefona = modelTelefona;
    }

}
