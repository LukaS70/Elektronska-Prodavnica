package com.example.elektronska_prodavnica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.Korisnik;
import com.example.elektronska_prodavnica.logika.JSONHandler;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.example.elektronska_prodavnica.prevalent.Prevalent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private Button signupButton;
    private EditText InputIme;
    private EditText InputPrezime;
    private EditText InputKorisnickoIme;
    private EditText InputEmail;
    private EditText InputTelefon;
    private EditText InputAdresa;
    private EditText InputSifra;
    private JSONObject postData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Registracija");

        signupButton = (Button)findViewById(R.id.signup_btn);
        InputIme = (EditText)findViewById(R.id.ime_input);
        InputPrezime = (EditText)findViewById(R.id.prezime_input);
        InputKorisnickoIme = (EditText)findViewById(R.id.korisnicko_ime_input);
        InputEmail = (EditText)findViewById(R.id.email_input);
        InputTelefon = (EditText)findViewById(R.id.telefon_input);
        InputAdresa = (EditText)findViewById(R.id.adresa_input);
        InputSifra = (EditText)findViewById(R.id.sifra_input);

        notificationManager = NotificationManagerCompat.from(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = InputIme.getText().toString();
                String prezime = InputPrezime.getText().toString();
                String korisnickoIme = InputKorisnickoIme.getText().toString();
                String email = InputEmail.getText().toString();
                String telefon = InputTelefon.getText().toString();
                String adresa = InputAdresa.getText().toString();
                String sifra = InputSifra.getText().toString();

                Korisnik k = new Korisnik(-1,ime,prezime,korisnickoIme,sifra,email,telefon,adresa,0);

                if(TextUtils.isEmpty(ime)){
                    Toast.makeText(getApplicationContext(),"Morate uneti ime",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(prezime)){
                    Toast.makeText(getApplicationContext(),"Morate uneti prezime",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(korisnickoIme)){
                    Toast.makeText(getApplicationContext(),"Morate uneti korisnicko ime",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Morate uneti email",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(telefon)){
                    Toast.makeText(getApplicationContext(),"Morate uneti telefon",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(adresa)){
                    Toast.makeText(getApplicationContext(),"Morate uneti adresu",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(sifra)){
                    Toast.makeText(getApplicationContext(),"Morate uneti sifru",Toast.LENGTH_SHORT).show();
                    return;
                }

                postData = JSONHandler.getInstanca().napraviKorisnika(k);

                new ProveriDaLiPostoji().execute("http://10.0.2.2/flight/korisnik.json");

        }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class ProveriDaLiPostoji extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray korisnici = Kontroler.getInstanca().vratiSveKorisnike(params[0]);
            return korisnici;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(Kontroler.getInstanca().daLiPostoji(result,InputKorisnickoIme.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Korisnicko ime zauzeto", Toast.LENGTH_SHORT).show();

                return;
            }else{
                if(postData != null) {
                    new PosaljiPodatkeOKorisniku().execute("http://10.0.2.2/flight/korisnik", postData.toString());
                }
            }
        }
    }

    private class PosaljiPodatkeOKorisniku extends AsyncTask<String, Void, Korisnik> {

        @Override
        protected Korisnik doInBackground(String... params) {
            Korisnik korisnik = Kontroler.getInstanca().registrujKorisnika(params[0], params[1]);
            return korisnik;
        }

        @Override
        protected void onPostExecute(Korisnik registrovaniKorisnik) {
            super.onPostExecute(registrovaniKorisnik);
            if(registrovaniKorisnik != null) {
                Toast.makeText(getApplicationContext(), "Uspesno ste se registrovali", Toast.LENGTH_SHORT).show();
                notifikacijaRegistracija(registrovaniKorisnik.toString()+ ", dobrodosli u MobileShop!");
                Log.e("TAG", registrovaniKorisnik.toString());//uneti korisnik
                System.out.println(registrovaniKorisnik.getKorisnikId()+registrovaniKorisnik.getKorisnickoIme());
                //na novi activity
                Prevalent.getInstanca().setUlogovaniKorisnik(registrovaniKorisnik);
                Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void notifikacijaRegistracija(String poruka) {
        Notification registacija = new NotificationCompat.Builder(this, NotificationChannels.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .setContentTitle("Dobrosdosli")
                .setContentText(poruka)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        notificationManager.notify(1, registacija);
    }
}
