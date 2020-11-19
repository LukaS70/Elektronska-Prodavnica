package com.example.elektronska_prodavnica.prevalent;

import com.example.elektronska_prodavnica.domen.Korisnik;

public class Prevalent {
    private static Prevalent instanca;
    private Korisnik ulogovaniKorisnik;

    private Prevalent() {
        ulogovaniKorisnik = new Korisnik();
    }

    public static Prevalent getInstanca() {
        if(instanca == null){
            instanca = new Prevalent();
        }
        return instanca;
    }

    public Korisnik getUlogovaniKorisnik() {
        return ulogovaniKorisnik;
    }

    public void setUlogovaniKorisnik(Korisnik ulogovaniKorisnik) {
        this.ulogovaniKorisnik = ulogovaniKorisnik;
    }
}
