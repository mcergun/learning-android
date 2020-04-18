package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonDownloader extends AsyncTask<String, String, Void> {
    public String jsonPath;
    public String jsonData;
    private ProgressDialog progressDialog;
    Activity act;

    JsonDownloader(Activity ac, String url) {
        act = ac;
        progressDialog = new ProgressDialog(act);
        jsonPath = url;
    }

    protected void onPreExecute() {
        progressDialog.setMessage("Downloading JSON from " + jsonPath + " ...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                JsonDownloader.this.cancel(true);
            }
        });
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            URL url = new URL(jsonPath);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setUseCaches(false);
            huc.setAllowUserInteraction(false);
            huc.setConnectTimeout(1000);
            huc.setReadTimeout(1000);
            huc.connect();
            Log.d("deneme", "1");
            int code = huc.getResponseCode();
            Log.d("deneme", "2, code = " + code);
            if (code != 200)
            {
                throw new IOException("Invalid response from server: " + code);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
//            Log.d("deneme", sb.toString());

            br.close();
            huc.disconnect();
            jsonData = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void v) {
        progressDialog.dismiss();
        ((MainActivity) act).updateCovidData(jsonData);
    }
}
