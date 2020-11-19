package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elektronska_prodavnica.domen.Paket;
import com.squareup.picasso.Picasso;

public class PrikazPaketa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_paketa);

        Paket p = (Paket)getIntent().getSerializableExtra("Paket");
        setTitle(p.getNazivPaketa());

        TextView txtNaziv = findViewById(R.id.detalji_naziv_paket);
        txtNaziv.setText(p.getNazivPaketa());
        TextView txtBrojMinuta = findViewById(R.id.detalji_broj_minuta);
        txtBrojMinuta.setText("Broj minuta: "+Integer.toString(p.getBrojMinuta()));
        TextView txtBrojSMS = findViewById(R.id.detalji_broj_sms);
        txtBrojSMS.setText("Broj poruka: "+Integer.toString(p.getBrojSMS()));
        TextView txtBrojMB = findViewById(R.id.detalji_broj_mb);
        txtBrojMB.setText("Broj MB: "+Integer.toString(p.getBrojMB()));
        TextView txtCena = findViewById(R.id.detalji_cena_paket);
        txtCena.setText("Cena: "+Double.toString(p.getCena())+"din");
        ImageView imgSlika = findViewById(R.id.detalji_slika_paket);
        Picasso.get().load(p.getUrl()).fit().into(imgSlika);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nazad_dugme:
                Intent intent = new Intent(PrikazPaketa.this, PaketiActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
