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

import com.example.elektronska_prodavnica.domen.Paket;
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

public class AdminDodajPaketActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private NotificationManagerCompat notificationManager;
    String currentPhotoPath;

    private Button dodajPaketButton;
    private EditText InputNazivPaketa;
    private EditText InputBrojMinuta;
    private EditText InputBrojSMS;
    private EditText InputBrojMB;
    private EditText InputCenaPaketa;
    private EditText InputUrl;
    private JSONObject postDataPaket;
    private Paket p;
    private Button dodajSliku;
    private ImageView slikaJpg;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dodaj_paket);

        setTitle("Ubacivanje paketa");

        dodajSliku = (Button)findViewById(R.id.dodaj_sliku_paketa_btn);
        slikaJpg = (ImageView)findViewById(R.id.slika_paketa_jpg_input);
        dodajPaketButton = (Button)findViewById(R.id.dodaj_paket_btn);
        InputNazivPaketa = (EditText)findViewById(R.id.naziv_paketa_input);
        InputBrojMinuta = (EditText)findViewById(R.id.broj_minuta_input);
        InputBrojSMS = (EditText)findViewById(R.id.broj_sms_input);
        InputBrojMB = (EditText)findViewById(R.id.broj_mb_input);
        InputCenaPaketa = (EditText)findViewById(R.id.cena_paketa_input);
        InputUrl = (EditText)findViewById(R.id.slika_paketa_input);

        storageReference = FirebaseStorage.getInstance().getReference();

        notificationManager = NotificationManagerCompat.from(this);

        dodajPaketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = InputUrl.getText().toString();
                String nazivPaketa = InputNazivPaketa.getText().toString();
                int minuti = 0;
                if(!InputBrojMinuta.getText().toString().isEmpty()) {
                    minuti = Integer.parseInt(InputBrojMinuta.getText().toString());
                }
                int sms = 0;
                if(!InputBrojSMS.getText().toString().isEmpty()) {
                    sms = Integer.parseInt(InputBrojSMS.getText().toString());
                }
                int mb = 0;
                if(!InputBrojMB.getText().toString().isEmpty()) {
                    mb = Integer.parseInt(InputBrojMB.getText().toString());
                }
                double cena = 0;
                if(!InputCenaPaketa.getText().toString().isEmpty()) {
                    cena = Double.parseDouble(InputCenaPaketa.getText().toString());
                }


                if(TextUtils.isEmpty(nazivPaketa)){
                    Toast.makeText(getApplicationContext(),"Morate uneti naziv",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(InputBrojMinuta.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti broj minuta",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(InputBrojSMS.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti broj SMS",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(InputBrojMB.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti broj MB",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(InputCenaPaketa.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Morate uneti cenu",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(url)){
                    Toast.makeText(getApplicationContext(),"Morate uneti sliku",Toast.LENGTH_SHORT).show();
                    return;
                }

                p = new Paket(-1,nazivPaketa,minuti,sms,mb,cena,url);
                postDataPaket = JSONHandler.getInstanca().napraviPaket(p);

                new ProveriDaLiPostoji().execute("http://10.0.2.2/flight/paket.json");


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
                        InputUrl.setText(uri.toString());
                    }
                });
                Toast.makeText(AdminDodajPaketActivity.this,"Uspesno Upload-ovana slika",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminDodajPaketActivity.this,"Greska prilikom Upload-a",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile()throws IOException {
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

    private class ProveriDaLiPostoji extends AsyncTask<String, Void, ArrayList<Paket>> {

        @Override
        protected ArrayList<Paket> doInBackground(String... params) {
            ArrayList<Paket> paketi = Kontroler.getInstanca().daLiPostojiPaket(params[0],p);
            return paketi;
        }

        @Override
        protected void onPostExecute(ArrayList<Paket> paketi) {
            super.onPostExecute(paketi);
            //Toast.makeText(getApplicationContext(),"Uspesno ste se registrovali",Toast.LENGTH_SHORT).show();
            if(!paketi.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Paket vec postoji", Toast.LENGTH_SHORT).show();
                return;
            }else{
                if(postDataPaket != null) {
                    new PosaljiPodatkeOPaketu().execute("http://10.0.2.2/flight/paket", postDataPaket.toString());
                }
            }
        }
    }

    private class PosaljiPodatkeOPaketu extends AsyncTask<String, Void, Paket> {

        @Override
        protected Paket doInBackground(String... params) {
            Paket paket = Kontroler.getInstanca().ubaciPaket(params[0], params[1]);
            return paket;
        }

        @Override
        protected void onPostExecute(Paket ubacenPaket) {
            super.onPostExecute(ubacenPaket);
            if(ubacenPaket != null) {
                Toast.makeText(getApplicationContext(), "Uspesno ubacen paket", Toast.LENGTH_SHORT).show();
                notifikacijaUbacenPaket("Uspesno ste ubacili paket: "+ubacenPaket.toString());
                Log.e("TAG", ubacenPaket.toString());//uneti korisnik
                Intent intent = new Intent(AdminDodajPaketActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void notifikacijaUbacenPaket(String poruka) {
        Notification ubacivanjePaketa = new NotificationCompat.Builder(this, NotificationChannels.CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_paketi)
                .setContentTitle("Ubacivanje uspesno")
                .setContentText(poruka)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(3, ubacivanjePaketa);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nazad_dugme:
                Intent intent = new Intent(AdminDodajPaketActivity.this,AdminActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
