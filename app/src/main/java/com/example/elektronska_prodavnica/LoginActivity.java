package com.example.elektronska_prodavnica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.Korisnik;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.example.elektronska_prodavnica.prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText InputKorisnickoIme;
    private EditText InputSifra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        loginButton = (Button)findViewById(R.id.login_btn);
        InputKorisnickoIme = (EditText)findViewById(R.id.korisnicko_ime_login);
        InputSifra = (EditText)findViewById(R.id.sifra_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String korisnickoIme = InputKorisnickoIme.getText().toString();
                String sifra = InputSifra.getText().toString();

                if(TextUtils.isEmpty(korisnickoIme)){
                    Toast.makeText(getApplicationContext(),"Morate uneti korisnicko ime",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(sifra)){
                    Toast.makeText(getApplicationContext(),"Morate uneti sifru",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    new UlogujSe().execute("http://10.0.2.2/flight/korisnik.json",korisnickoIme,sifra);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class UlogujSe extends AsyncTask<String, Void, Korisnik> {

        @Override
        protected Korisnik doInBackground(String... params) {
            Korisnik ulogovaniKorisnik = Kontroler.getInstanca().ulogujSe(params[0],params[1],params[2]);
            return ulogovaniKorisnik;
        }

        @Override
        protected void onPostExecute(Korisnik ulogovaniKorisnik) {
            super.onPostExecute(ulogovaniKorisnik);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(ulogovaniKorisnik==null) {
                Toast.makeText(getApplicationContext(), "Niste uneli validne podatke", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Toast.makeText(getApplicationContext(), "Uspesno prijavljivanje", Toast.LENGTH_SHORT).show();
                //sledeci activity
                Prevalent.getInstanca().setUlogovaniKorisnik(ulogovaniKorisnik);
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }
    }
}
