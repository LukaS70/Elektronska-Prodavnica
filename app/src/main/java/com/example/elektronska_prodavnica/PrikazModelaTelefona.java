package com.example.elektronska_prodavnica;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.domen.Paket;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.example.elektronska_prodavnica.prevalent.Cart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PrikazModelaTelefona extends AppCompatActivity {

    ArrayList<String> imenaPaketa;

    private Button dodajUKorpu;
    private Spinner s;
    private ModelTelefona mt;
    private Paket p;
    private String imePaketa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_modela_telefona);

        new PopuniSpiner().execute("http://10.0.2.2/flight/paket.json");

        dodajUKorpu = (Button)findViewById(R.id.dodaj_u_korpu);

        mt = (ModelTelefona)getIntent().getSerializableExtra("Model telefona");

        setTitle(mt.toString());
        TextView txtProizvodjac = findViewById(R.id.detalji_proizvodjac);
        txtProizvodjac.setText(mt.getProizvodjac());
        TextView txtNaziv = findViewById(R.id.detalji_naziv);
        txtNaziv.setText(mt.getNaziv());
        TextView txtMasa = findViewById(R.id.detalji_masa);
        txtMasa.setText("Masa: "+Double.toString(mt.getMasa()));
        TextView txtDimenzije = findViewById(R.id.detalji_dimenzije);
        txtDimenzije.setText("Dimenzije: "+mt.getDimenzije());
        TextView txtKamera = findViewById(R.id.detalji_kamera);
        txtKamera.setText("Kamera: "+mt.getKamera());
        TextView txtProcesor = findViewById(R.id.detalji_procesor);
        txtProcesor.setText("Procesor: "+mt.getProcesor());
        TextView txtBaterija = findViewById(R.id.detalji_baterija);
        txtBaterija.setText("Baterija: "+mt.getBaterija());
        TextView txtOperativniSistem = findViewById(R.id.detalji_operativni_sistem);
        txtOperativniSistem.setText("Operativni sistem: "+mt.getOperativniSistem());
        TextView txtMemorija = findViewById(R.id.detalji_memorija);
        txtMemorija.setText("Memorija: "+mt.getMemorija());
        TextView txtCena = findViewById(R.id.detalji_cena);
        txtCena.setText("Cena: "+Double.toString(mt.getCena())+"din");
        ImageView imgSlika = findViewById(R.id.detalji_slika);
        Picasso.get().load(mt.getSlika()).fit().into(imgSlika);

        dodajUKorpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imePaketa = s.getSelectedItem().toString();
                new NadjiPaket().execute("http://10.0.2.2/flight/paket.json");
            }
        });
    }


    private class PopuniSpiner extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> paketi = Kontroler.getInstanca().popuniSpiner(params[0]);
            return paketi;
        }

        @Override
        protected void onPostExecute(ArrayList<String> paketi) {
            super.onPostExecute(paketi);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(paketi==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ucitavanja paketa", Toast.LENGTH_SHORT).show();
                return;
            }else{
                imenaPaketa = paketi;
                s = (Spinner) findViewById(R.id.paket_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, imenaPaketa);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
                return;
            }
        }
    }

    private class NadjiPaket extends AsyncTask<String, Void, Paket> {

        @Override
        protected Paket doInBackground(String... params) {
            Paket paket = Kontroler.getInstanca().vratiPaket(params[0],imePaketa);
            return paket;
        }

        @Override
        protected void onPostExecute(Paket paket) {
            super.onPostExecute(paket);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(paket==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ucitavanja paketa", Toast.LENGTH_SHORT).show();
                return;
            }else{
                p = paket;

                Kontroler.getInstanca().ubaciUKorpu(mt,p);

                Toast.makeText(getApplicationContext(),"Telefon je dodat u korpu",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PrikazModelaTelefona.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nazad_dugme:
                Intent intent = new Intent(PrikazModelaTelefona.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
