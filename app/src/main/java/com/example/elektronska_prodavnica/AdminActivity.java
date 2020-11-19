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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.Ugovor;
import com.example.elektronska_prodavnica.logika.Kontroler;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    ListView lista;
    ArrayList<Ugovor> ucitaniUgovori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Ugovori");

        lista = findViewById(R.id.listViewUgovori);

        new DajListuUgovora().execute("http://10.0.2.2/flight/ugovor.json");
    }

    private class DajListuUgovora extends AsyncTask<String, Void, ArrayList<Ugovor>> {

        @Override
        protected ArrayList<Ugovor> doInBackground(String... params) {
            ArrayList<Ugovor> ugovori = Kontroler.getInstanca().vratiSveUgovore(params[0]);
            return ugovori;
        }

        @Override
        protected void onPostExecute(ArrayList<Ugovor> ugovori) {
            super.onPostExecute(ugovori);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(ugovori==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ucitavanja ugovora", Toast.LENGTH_SHORT).show();
                return;
            }else{
                ucitaniUgovori = ugovori;
                NoviAdapter adapter = new NoviAdapter();
                lista.setAdapter(adapter);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent= new Intent(AdminActivity.this,PrikazUgovora.class);
                        intent.putExtra("Ugovor",ucitaniUgovori.get(i));
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
            return ucitaniUgovori.size();
        }

        @Override
        public Object getItem(int position) {
            return ucitaniUgovori.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        //definise izgled elementa liste
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            //inflater smesta layout u view
            view = getLayoutInflater().inflate(R.layout.prikaz_ugovora_za_listview,null);

            TextView txtImeTelefona = view.findViewById(R.id.ugovor_telefon);
            txtImeTelefona.setText(ucitaniUgovori.get(position).getModelTelefona().toString());
            TextView txtImeKorisnika = view.findViewById(R.id.ugovor_korisnik);
            txtImeKorisnika.setText(ucitaniUgovori.get(position).getKorisnik().toString());
            TextView txtImePaketa = view.findViewById(R.id.ugovor_paket);
            txtImePaketa.setText(ucitaniUgovori.get(position).getPaket().getNazivPaketa());

            return view;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ubaci_telefon:
                Intent intent= new Intent(AdminActivity.this,AdminDodajTelefonActivity.class);
                startActivity(intent);
               return true;
            case R.id.ubaci_paket:
                Intent intent2 = new Intent(AdminActivity.this,AdminDodajPaketActivity.class);
                startActivity(intent2);
               return true;
            case R.id.nazad_na_home_sa_admin:
                Intent intent3 = new Intent(AdminActivity.this,HomeActivity.class);
                startActivity(intent3);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
