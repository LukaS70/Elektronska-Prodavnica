package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Button SearchBtn;
    private EditText imeModela;
    private ListView lista;
    private String izabraniProizvodjac;
    private String izabraniRam;
    private ArrayList<ModelTelefona> telefoni;
    private ArrayList<ModelTelefona> telefoniZaPrikaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Pretraga");
        telefoniZaPrikaz = new ArrayList<>();
        izabraniProizvodjac = "";
        izabraniRam = "";
        imeModela = findViewById(R.id.pretrazi_po_nazivu_modela);

        SearchBtn = findViewById(R.id.dugme_pretrazi);
        lista = findViewById(R.id.listViewSearch);

        telefoni = (ArrayList<ModelTelefona>)getIntent().getSerializableExtra("Telefoni");

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imeModelaTelefona = imeModela.getText().toString();
                telefoniZaPrikaz.clear();
                telefoniZaPrikaz = Kontroler.getInstanca().pretraziModele(telefoni,imeModelaTelefona,izabraniProizvodjac,izabraniRam);
                NoviAdapter adapter = new NoviAdapter();
                lista.setAdapter(adapter);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent= new Intent(SearchActivity.this,PrikazModelaTelefona.class);
                        intent.putExtra("Model telefona",telefoniZaPrikaz.get(i));
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.sve_ram:
                izabraniRam = "";
                item.setChecked(true);
                return true;

            case R.id.manje_od_4gb_ram:
                izabraniRam="4GB";
                item.setChecked(true);
                return true;

            case R.id.a4gb_ram:
                izabraniRam="4";
                item.setChecked(true);
                return true;

            case R.id.a6gb_ram:
                izabraniRam="6";
                item.setChecked(true);
                return true;

            case R.id.vise_od_6gb_ram:
                izabraniRam="6GB";
                item.setChecked(true);
                return true;

            case R.id.svi_proizvodjaci:
                izabraniProizvodjac = "";
                item.setChecked(true);
                return true;

            case R.id.samsung:
                izabraniProizvodjac = "Samsung";
                item.setChecked(true);
                return true;

            case R.id.google:
                izabraniProizvodjac = "Google";
                item.setChecked(true);
                return true;

            case R.id.xiaomi:
                izabraniProizvodjac = "Xiaomi";
                item.setChecked(true);
                return true;

            case R.id.huawei:
                izabraniProizvodjac = "Huawei";
                item.setChecked(true);
                return true;
            case R.id.nazad_dugme:
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class NoviAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return telefoniZaPrikaz.size();
        }

        @Override
        public Object getItem(int position) {
            return telefoniZaPrikaz.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        //definise izgled elementa liste
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            //inflater smesta layout u view
            view = getLayoutInflater().inflate(R.layout.prikaz_modela_telefona_za_listview,null);

            TextView txtNaziv = view.findViewById(R.id.naziv_modela_telefona);
            txtNaziv.setText(telefoniZaPrikaz.get(position).toString());
            TextView txtCena = view.findViewById(R.id.cena_modela_telefona);
            txtCena.setText(Double.toString(telefoniZaPrikaz.get(position).getCena())+"din");
            ImageView imgSlika = view.findViewById(R.id.slika_modela_telefona);
            //ubaci sliku preko url
//            new DownloadImageTask((ImageView) findViewById(R.id.slika_modela_telefona))
//                    .execute(modeliTelefona.get(position).getSlika().replace("http","https"));

            Picasso.get().load(telefoniZaPrikaz.get(position).getSlika()).fit().into(imgSlika);

            return view;
        }
    }
}
