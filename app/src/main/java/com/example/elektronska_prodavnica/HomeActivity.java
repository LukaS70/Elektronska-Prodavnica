package com.example.elektronska_prodavnica;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.example.elektronska_prodavnica.prevalent.Cart;
import com.example.elektronska_prodavnica.prevalent.Prevalent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView lista;
    ArrayList<ModelTelefona> modeliTelefona;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        modeliTelefona = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Telefoni");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Cart.getInstanca().getKorpa().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Korpa je prazna",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView ulogovaniKorisnikTextView = headerView.findViewById(R.id.ulogovani_korisnicko_ime);

        adminDugme();

        if(Prevalent.getInstanca().getUlogovaniKorisnik()!=null) {
            ulogovaniKorisnikTextView.setText(Prevalent.getInstanca().getUlogovaniKorisnik().toString());
        }else{
            ulogovaniKorisnikTextView.setText("Prijavite se da bi izvrsili kupovinu!");
        }

        lista = findViewById(R.id.listView1);

        new DajListuTelefona().execute("http://10.0.2.2/flight/model_telefona.json");

    }



    private void adminDugme(){
        if(Prevalent.getInstanca().getUlogovaniKorisnik().getAdmin() == 1){
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_admin).setVisible(true);
        }
    }

    private class DajListuTelefona extends AsyncTask<String, Void, ArrayList<ModelTelefona>> {

        @Override
        protected ArrayList<ModelTelefona> doInBackground(String... params) {
            ArrayList<ModelTelefona> telefoni = Kontroler.getInstanca().vratiSveTelefone(params[0]);
            return telefoni;
        }

        @Override
        protected void onPostExecute(ArrayList<ModelTelefona> telefoni) {
            super.onPostExecute(telefoni);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(telefoni==null) {
                Toast.makeText(getApplicationContext(), "Greska prilikom ucitavanja telefona", Toast.LENGTH_SHORT).show();
                return;
            }else{
                modeliTelefona = telefoni;
                NoviAdapter adapter = new NoviAdapter();
                lista.setAdapter(adapter);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent= new Intent(HomeActivity.this,PrikazModelaTelefona.class);
                        intent.putExtra("Model telefona",modeliTelefona.get(i));
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
            return modeliTelefona.size();
        }

        @Override
        public Object getItem(int position) {
            return modeliTelefona.get(position);
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
            txtNaziv.setText(modeliTelefona.get(position).toString());
            TextView txtCena = view.findViewById(R.id.cena_modela_telefona);
            txtCena.setText(Double.toString(modeliTelefona.get(position).getCena())+"din");
            ImageView imgSlika = view.findViewById(R.id.slika_modela_telefona);
            //ubaci sliku preko url
//            new DownloadImageTask((ImageView) findViewById(R.id.slika_modela_telefona))
//                    .execute(modeliTelefona.get(position).getSlika().replace("http","https"));

            Picasso.get().load(modeliTelefona.get(position).getSlika()).fit().into(imgSlika);


            return view;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            //super.onBackPressed();
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Logout")
                    .setMessage("Da li zelite da napustite aplikaciju?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

  @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if(id == R.id.action_settings){
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id == R.id.nav_paketi){
            Intent intent = new Intent(HomeActivity.this,PaketiActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_pretrazi){
            Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("Telefoni",modeliTelefona);
            startActivity(intent);
        }else if(id == R.id.nav_korpa){
            if(Cart.getInstanca().getKorpa().isEmpty()){
                Toast.makeText(getApplicationContext(),"Korpa je prazna",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        }else if(id == R.id.nav_logout){
            Prevalent.getInstanca().setUlogovaniKorisnik(null);
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else if(id == R.id.nav_admin){
            Intent intent = new Intent(HomeActivity.this,AdminActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_lokacija){
            Intent intent = new Intent(HomeActivity.this,LokacijaActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
