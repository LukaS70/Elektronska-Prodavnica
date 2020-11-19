package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.domen.Paket;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PaketiActivity extends AppCompatActivity {

    ListView lista;
    ArrayList<Paket> paketiUcitani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paketi);
        setTitle("Paketi");

        lista = findViewById(R.id.listViewPaketi);

        new DajListuPaketa().execute("http://10.0.2.2/flight/paket.json");
    }

    private class DajListuPaketa extends AsyncTask<String, Void, ArrayList<Paket>> {

        @Override
        protected ArrayList<Paket> doInBackground(String... params) {
            ArrayList<Paket> paketi = Kontroler.getInstanca().vratiSvePakete(params[0]);
            return paketi;
        }

        @Override
        protected void onPostExecute(ArrayList<Paket> paketi) {
            super.onPostExecute(paketi);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(paketi==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ucitavanja paketa", Toast.LENGTH_SHORT).show();
                return;
            }else{
                paketiUcitani = paketi;
                NoviAdapter adapter = new NoviAdapter();
                lista.setAdapter(adapter);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent= new Intent(PaketiActivity.this,PrikazPaketa.class);
                        intent.putExtra("Paket",paketiUcitani.get(i));
                        startActivity(intent);
                    }
                });
                return;
            }
        }
    }

    public class NoviAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return paketiUcitani.size();
        }

        @Override
        public Object getItem(int position) {
            return paketiUcitani.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        //definise izgled elementa liste
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            //inflater smesta layout u view
            view = getLayoutInflater().inflate(R.layout.prikaz_paketa_za_listview,null);

            TextView txtNaziv = view.findViewById(R.id.naziv_paketa);
            txtNaziv.setText(paketiUcitani.get(position).getNazivPaketa());
            TextView txtCena = view.findViewById(R.id.cena_paketa);
            txtCena.setText(Double.toString(paketiUcitani.get(position).getCena())+"din");
            ImageView imgSlika = view.findViewById(R.id.slika_paketa);
            //ubaci sliku preko url
//            new DownloadImageTask((ImageView) findViewById(R.id.slika_modela_telefona))
//                    .execute(modeliTelefona.get(position).getSlika().replace("http","https"));

            Picasso.get().load(paketiUcitani.get(position).getUrl()).fit().into(imgSlika);

            return view;
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
                Intent intent = new Intent(PaketiActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
