package com.example.elektronska_prodavnica.servis;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elektronska_prodavnica.domen.Korisnik;
import com.example.elektronska_prodavnica.logika.JSONHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Komunikacija {


    public JSONObject ubaciKorisnika(String url, String podaci) {
        String poruka = "";
        String por2 = "";
        JSONObject korisnik = new JSONObject();
        try {
            korisnik= new JSONObject(podaci);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(korisnik.toString());
        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(podaci);
            wr.flush();
            wr.close();

            InputStream in = httpURLConnection.getInputStream();
//            InputStream in;
//            if (httpURLConnection.getResponseCode() / 100 == 2) {
//                in = httpURLConnection.getInputStream();
//            } else {
//
//                in = httpURLConnection.getErrorStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(in);
//                int inputStreamData = inputStreamReader.read();
//                while (inputStreamData != -1) {
//                    char current = (char) inputStreamData;
//                    inputStreamData = inputStreamReader.read();
//                    poruka += current;
//                }
//                Log.i("Error != 400", poruka);
//            }
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                poruka += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        System.out.println("Poruka: "+poruka);
        return korisnik;
    }

    public JSONArray vratiListu(String param) {

        JSONArray listaJson = new JSONArray();
        try {
            URL url = new URL(param);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String odg = "";

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                odg=sb.toString();
                listaJson = new JSONArray(odg);
            }
            br.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaJson;
    }


    public void ubaciUgovore(JSONArray ugovoriJSON,String url) {
        String poruka = "";
        for(int i = 0; i<ugovoriJSON.length();i++) {
            JSONObject ugovorJSON = new JSONObject();
            try {
                ugovorJSON = ugovoriJSON.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(ugovorJSON.toString());
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    poruka += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }
}
