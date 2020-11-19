package com.example.elektronska_prodavnica.prevalent;

import com.example.elektronska_prodavnica.domen.Ugovor;

import java.util.ArrayList;

public class Cart {
    private static Cart instanca;
    private ArrayList<Ugovor> korpa;

    private Cart() {
        korpa = new ArrayList<>();
    }

    public static Cart getInstanca() {
        if(instanca == null){
            instanca = new Cart();
        }
        return instanca;
    }

    public ArrayList<Ugovor> getKorpa() {
        return korpa;
    }

    public void setKorpa(ArrayList<Ugovor> korpa) {
        this.korpa = korpa;
    }
}
