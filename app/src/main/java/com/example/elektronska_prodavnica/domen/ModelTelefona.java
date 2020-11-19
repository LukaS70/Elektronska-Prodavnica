package com.example.elektronska_prodavnica.domen;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ModelTelefona implements Serializable {
    private int modelId;
    private String proizvodjac;
    private String naziv;
    private double masa;
    private String dimenzije;
    private String kamera;
    private String procesor;
    private String baterija;
    private String operativniSistem;
    private String memorija;
    private String slika;
    private double cena;

    public ModelTelefona(){

    }

    public ModelTelefona(int modelId,String proizvodjac,String naziv,double masa, String dimenzije, String kamera, String procesor, String baterija, String operativniSistem, String memorija, String slika, double cena){
        this.modelId = modelId;
        this.proizvodjac = proizvodjac;
        this.naziv = naziv;
        this.masa = masa;
        this.dimenzije = dimenzije;
        this.kamera = kamera;
        this.procesor = procesor;
        this.baterija = baterija;
        this.operativniSistem = operativniSistem;
        this.memorija = memorija;
        this.slika = slika;
        this.cena = cena;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getProizvodjac() {
        return proizvodjac;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getMasa() {
        return masa;
    }

    public void setMasa(double masa) {
        this.masa = masa;
    }

    public String getDimenzije() {
        return dimenzije;
    }

    public void setDimenzije(String dimenzije) {
        this.dimenzije = dimenzije;
    }

    public String getKamera() {
        return kamera;
    }

    public void setKamera(String kamera) {
        this.kamera = kamera;
    }

    public String getProcesor() {
        return procesor;
    }

    public void setProcesor(String procesor) {
        this.procesor = procesor;
    }

    public String getBaterija() {
        return baterija;
    }

    public void setBaterija(String baterija) {
        this.baterija = baterija;
    }

    public String getOperativniSistem() {
        return operativniSistem;
    }

    public void setOperativniSistem(String operativniSistem) {
        this.operativniSistem = operativniSistem;
    }

    public String getMemorija() {
        return memorija;
    }

    public void setMemorija(String memorija) {
        this.memorija = memorija;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    @NonNull
    @Override
    public String toString() {
        return proizvodjac+" "+naziv;
    }
}
