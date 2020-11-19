package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.EGLExt;
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

import com.example.elektronska_prodavnica.domen.Ugovor;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.example.elektronska_prodavnica.prevalent.Cart;
import com.example.elektronska_prodavnica.prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    ListView lista;
    ArrayList<Ugovor> ugovori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Korpa");

        notificationManager = NotificationManagerCompat.from(this);

        lista = findViewById(R.id.listViewCart);

        ugovori = Cart.getInstanca().getKorpa();

        postaviListu();
    }

    void postaviListu(){
        NoviAdapter adapter = new NoviAdapter();
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence options[] = new CharSequence[]{
                        "Obrisi",
                        "Odustani"
                };

                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Obrisi proizvod")
                        .setMessage("Da li ste sigurni da zelite da uklonite proizvod iz korpe?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Cart.getInstanca().getKorpa().remove(position);
                                Toast.makeText(getApplicationContext(),"Proizvod izbacen iz korpe",Toast.LENGTH_SHORT).show();
                                if(Cart.getInstanca().getKorpa().isEmpty()){
                                    Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    postaviListu();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.korpa_meni,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.potvrdi_porudzbinu:
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Potvrdi porudzbinu")
                        .setMessage("Da li zelite da potvrdite porudzbinu?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new UbaciUgovore().execute("http://10.0.2.2/flight/ugovor");
                                Toast.makeText(getApplicationContext(),"Porudzbina potvrdjena",Toast.LENGTH_SHORT).show();
                                notifikacijaPorudzbina(Prevalent.getInstanca().getUlogovaniKorisnik().getKorisnickoIme() +", Vasa porudzbina je primljena. Hvala na kupovini!");
//     ???                           Intent intent = new Intent(CartActivity.this,HomeActivity.class);
//        ???                        startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                return true;
            case R.id.nazad_na_home_sa_korpe:
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void notifikacijaPorudzbina(String poruka){

        Notification porudzbina = new NotificationCompat.Builder(this,NotificationChannels.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_done_black_24dp)
                .setContentTitle("Porudzbina primljena")
                .setContentText(poruka)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        System.out.println("U notifikacijaPoridzbina");
        System.out.println(porudzbina.toString());
        notificationManager.notify(2,porudzbina);
    }

    private class UbaciUgovore extends AsyncTask<String, Void, ArrayList<Ugovor>> {

        @Override
        protected ArrayList<Ugovor> doInBackground(String... params) {
            Kontroler.getInstanca().ubaciUgovore(ugovori,params[0]);
            return ugovori;
        }

        @Override
        protected void onPostExecute(ArrayList<Ugovor> ubaceniUgovori) {
            super.onPostExecute(ubaceniUgovori);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(ubaceniUgovori==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ubacivanja ugovora", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Toast.makeText(getApplicationContext(),"Porudzbina je uspesno poslata",Toast.LENGTH_SHORT).show();
                ugovori.clear();
                Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
    }

    public class NoviAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ugovori.size();
        }

        @Override
        public Object getItem(int position) {
            return ugovori.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        //definise izgled elementa liste
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            //inflater smesta layout u view
            view = getLayoutInflater().inflate(R.layout.prikaz_korpa_listview,null);

            TextView txtNazivTelefona = view.findViewById(R.id.naziv_telefona_ugovor);
            txtNazivTelefona.setText(ugovori.get(position).getModelTelefona().toString());
            TextView txtCenaTelefona = view.findViewById(R.id.cena_telefona_ugovor);
            txtCenaTelefona.setText(Double.toString(ugovori.get(position).getModelTelefona().getCena())+"din");
            TextView txtNazivPaketa = view.findViewById(R.id.naziv_paketa_ugovor);
            txtNazivPaketa.setText(ugovori.get(position).getPaket().getNazivPaketa());
            TextView txtCenaPaketa = view.findViewById(R.id.cena_paketa_ugovor);
            txtCenaPaketa.setText(Double.toString(ugovori.get(position).getPaket().getCena())+"din");
            TextView txtCenaUkupna = view.findViewById(R.id.cena_ukupna_ugovor);
            double cenaUkupna = ugovori.get(position).getModelTelefona().getCena()+ugovori.get(position).getPaket().getCena();
            txtCenaUkupna.setText(Double.toString(cenaUkupna)+"din");
            ImageView imgSlikaTelefona = view.findViewById(R.id.slika_telefona_korpa);
            Picasso.get().load(ugovori.get(position).getModelTelefona().getSlika()).fit().into(imgSlikaTelefona);
            ImageView imgSlikaPaketa = view.findViewById(R.id.slika_paketa_korpa);
            Picasso.get().load(ugovori.get(position).getPaket().getUrl()).fit().into(imgSlikaPaketa);



            return view;
        }
    }
}
