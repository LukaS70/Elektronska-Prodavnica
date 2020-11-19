package com.example.elektronska_prodavnica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.elektronska_prodavnica.domen.ModelTelefona;
import com.example.elektronska_prodavnica.logika.JSONHandler;
import com.example.elektronska_prodavnica.logika.Kontroler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminDodajTelefonActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private NotificationManagerCompat notificationManager;
    String currentPhotoPath;


    private Button dodajTelefonButton;
    private EditText InputNaziv;
    private EditText InputProizvodjac;
    private EditText InputMasa;
    private EditText InputDimenzije;
    private EditText InputKamera;
    private EditText InputProcesor;
    private EditText InputBaterija;
    private EditText InputOperativniSistem;
    private EditText InputMemorija;
    private EditText InputSlika;
    private EditText InputCena;
    private JSONObject postData;
    private ModelTelefona mt;
    private Button dodajSliku;
    private ImageView slikaJpg;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dodaj_telefon);

        setTitle("Ubacivanje telefona");

        dodajSliku = (Button)findViewById(R.id.dodaj_sliku_btn);
        slikaJpg = (ImageView)findViewById(R.id.slika_jpg_input);
        dodajTelefonButton = (Button)findViewById(R.id.dodaj_telefon_btn);
        InputNaziv = (EditText)findViewById(R.id.naziv_input);
        InputProizvodjac = (EditText)findViewById(R.id.proizvodjac_input);
        InputMasa = (EditText)findViewById(R.id.masa_input);
        InputDimenzije = (EditText)findViewById(R.id.dimenzije_input);
        InputKamera = (EditText)findViewById(R.id.kamera_input);
        InputProcesor = (EditText)findViewById(R.id.procesor_input);
        InputBaterija = (EditText)findViewById(R.id.baterija_input);
        InputOperativniSistem = (EditText)findViewById(R.id.operativni_sistem_input);
        InputMemorija = (EditText)findViewById(R.id.memorija_input);
        InputSlika = (EditText)findViewById(R.id.slika_input);
        InputCena = (EditText)findViewById(R.id.cena_input);

        storageReference = FirebaseStorage.getInstance().getReference();

        notificationManager = NotificationManagerCompat.from(this);

        dodajTelefonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proizvodjac = InputProizvodjac.getText().toString();
                String naziv = InputNaziv.getText().toString();
                int masa = 0;
                if(!InputMasa.getText().toString().isEmpty()) {
                    masa = Integer.parseInt(InputMasa.getText().toString());
                }
                String dimenzije = InputDimenzije.getText().toString();
                String kamera = InputKamera.getText().toString();
                String procesor = InputProcesor.getText().toString();
                String baterija = InputBaterija.getText().toString();
                String operativniSistem = InputOperativniSistem.getText().toString();
                String memorija = InputMemorija.getText().toString();
                String slika = InputSlika.getText().toString();
                double cena = 0;
                if(!InputCena.getText().toString().isEmpty()) {
                    cena = Double.parseDouble(InputCena.getText().toString());
                }


                if(TextUtils.isEmpty(proizvodjac)){
                    Toast.makeText(getApplicationContext(),"Morate uneti proizvodjaca",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(naziv)){
                    Toast.makeText(getApplicationContext(),"Morate uneti naziv",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(InputMasa.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti korisnicko masu",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(dimenzije)){
                    Toast.makeText(getApplicationContext(),"Morate uneti dimenzije",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(kamera)){
                    Toast.makeText(getApplicationContext(),"Morate uneti kameru",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(procesor)){
                    Toast.makeText(getApplicationContext(),"Morate uneti procesor",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(baterija)){
                    Toast.makeText(getApplicationContext(),"Morate uneti bateriju",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(operativniSistem)){
                    Toast.makeText(getApplicationContext(),"Morate uneti operativni sistem",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(memorija)){
                    Toast.makeText(getApplicationContext(),"Morate uneti memoriju",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(slika)){
                    Toast.makeText(getApplicationContext(),"Morate uneti sliku",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(InputCena.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti cenu",Toast.LENGTH_SHORT).show();
                    return;
                }

                mt = new ModelTelefona(-1,proizvodjac,naziv,masa,dimenzije,kamera,procesor,baterija,operativniSistem,memorija,slika,cena);

                postData = JSONHandler.getInstanca().napraviModelTelefona(mt);

                new ProveriDaLiPostoji().execute("http://10.0.2.2/flight/model_telefona.json");
            }
        });

        dodajSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

    }

    private void askCameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(this,"Aplikaciji je neophodan pristup kameri.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CAMERA_REQUEST_CODE){

            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                Log.d("Tag","Absolute Url of image is "+ Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(),contentUri);
            }

        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri){
        final StorageReference image = storageReference.child("images/"+name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(slikaJpg);
                        InputSlika.setText(uri.toString());
                    }
                });
                Toast.makeText(AdminDodajTelefonActivity.this,"Uspesno Upload-ovana slika",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminDodajTelefonActivity.this,"Greska prilikom Upload-a",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile()throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private class ProveriDaLiPostoji extends AsyncTask<String, Void, ArrayList<ModelTelefona>> {

        @Override
        protected ArrayList<ModelTelefona> doInBackground(String... params) {
            ArrayList<ModelTelefona> telefoni = Kontroler.getInstanca().daLiPostojiTelefon(params[0],mt);
            return telefoni;
        }

        @Override
        protected void onPostExecute(ArrayList<ModelTelefona> telefoni) {
            super.onPostExecute(telefoni);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(!telefoni.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Telefon vec postoji", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(postData != null) {
                    new PosaljiPodatkeOTelefonu().execute("http://10.0.2.2/flight/model_telefona", postData.toString());
                }
            }
        }
    }

    private class PosaljiPodatkeOTelefonu extends AsyncTask<String, Void, ModelTelefona> {

        @Override
        protected ModelTelefona doInBackground(String... params) {
            ModelTelefona telefon = Kontroler.getInstanca().ubaciTelefon(params[0], params[1]);
            return telefon;
        }

        @Override
        protected void onPostExecute(ModelTelefona ubacenTelefon) {
            super.onPostExecute(ubacenTelefon);
            if(ubacenTelefon != null) {
                Toast.makeText(getApplicationContext(), "Uspesno ubacen telefon", Toast.LENGTH_SHORT).show();
                notifikacijaUbacenTelefon("Uspesno ste ubacili telefon: "+ubacenTelefon.toString());
                Log.e("TAG", ubacenTelefon.toString());//uneti korisnik
                Intent intent = new Intent(AdminDodajTelefonActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void notifikacijaUbacenTelefon(String poruka) {
        Notification ubacivanjeTelefona = new NotificationCompat.Builder(this, NotificationChannels.CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_telefon)
                .setContentTitle("Ubacivanje uspesno")
                .setContentText(poruka)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(3, ubacivanjeTelefona);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nazad_dugme:
                Intent intent = new Intent(AdminDodajTelefonActivity.this,AdminActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

