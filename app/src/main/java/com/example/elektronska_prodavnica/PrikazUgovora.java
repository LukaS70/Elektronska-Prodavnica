package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elektronska_prodavnica.domen.Ugovor;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PrikazUgovora extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_ugovora);

        Ugovor u = (Ugovor) getIntent().getSerializableExtra("Ugovor");
        setTitle("Ugovor id: "+u.getUgovorId());

        TextView txtNazivTelefona = findViewById(R.id.detalji_ugovor_naziv_telefona);
        txtNazivTelefona.setText(u.getModelTelefona().toString());
        TextView txtNazivPaketa = findViewById(R.id.detalji_ugovor_naziv_paket);
        txtNazivPaketa.setText(u.getPaket().getNazivPaketa());
        TextView txtDatum = findViewById(R.id.detalji_ugovor_datum);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(u.getDatum());
        txtDatum.setText("Datum: "+ date);
        TextView txtTrajanje = findViewById(R.id.detalji_ugovor_trajanje);
        txtTrajanje.setText("Trajanje: "+Integer.toString(u.getTrajanjeUgovora())+" meseci");
        TextView txtKorisnik = findViewById(R.id.detalji_ugovor_korisnik);
        txtKorisnik.setText("Korisnik: "+u.getKorisnik().toString());
        ImageView imgSlikaTelefona = findViewById(R.id.detalji_ugovor_slika_telefona);
        Picasso.get().load(u.getModelTelefona().getSlika()).fit().into(imgSlikaTelefona);
        ImageView imgSlikaPaketa = findViewById(R.id.detalji_ugovor_slika_paket);
        Picasso.get().load(u.getPaket().getUrl()).fit().into(imgSlikaPaketa);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nazad_dugme:
                Intent intent = new Intent(PrikazUgovora.this, AdminActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
