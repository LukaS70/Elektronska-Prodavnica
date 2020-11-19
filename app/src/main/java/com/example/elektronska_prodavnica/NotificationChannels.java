package com.example.elektronska_prodavnica;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

public class NotificationChannels extends Application {
    public static final String CHANNEL_1_ID="Registracija";
    public static final String CHANNEL_2_ID="Porudzbina";
    public static final String CHANNEL_3_ID="Ubacivanje";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel registracija = new NotificationChannel(CHANNEL_1_ID, "registracija", NotificationManager.IMPORTANCE_LOW);
            registracija.enableLights(true);
            registracija.setLightColor(Color.BLUE);

            //jos
            registracija.setDescription("za registraciju");

            NotificationChannel porudzbina = new NotificationChannel(CHANNEL_2_ID, "porudzbina", NotificationManager.IMPORTANCE_HIGH);
            porudzbina.enableLights(true);
            porudzbina.setLightColor(Color.WHITE);
            registracija.enableVibration(true);
            //jos
            porudzbina.setDescription("za porudzbinu");

            NotificationChannel ubacivanje = new NotificationChannel(CHANNEL_3_ID, "ubacivanje", NotificationManager.IMPORTANCE_DEFAULT);
            porudzbina.enableLights(true);
            porudzbina.setLightColor(Color.YELLOW);
            registracija.enableVibration(true);
            //jos
            porudzbina.setDescription("za ubacivanje");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(registracija);
            manager.createNotificationChannel(porudzbina);
            manager.createNotificationChannel(ubacivanje);
        }
    }
}
