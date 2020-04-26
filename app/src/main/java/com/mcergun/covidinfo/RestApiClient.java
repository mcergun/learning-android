package com.mcergun.covidinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestApiClient extends AsyncTask<String, Void, String> {

    protected String url;
    protected String host;
    protected String key;
    protected ProgressDialog dialog;
    protected OkHttpClient client = new OkHttpClient();
    protected OnCompletedListener onCompleted = null;

    public RestApiClient(Activity mainAct, String addr, String hostAddr, String keyVal) {
        url = addr;
        host = hostAddr;
        key = keyVal;
        dialog = new ProgressDialog(mainAct);
    }

    public void updateHeader(String hostAddr, String keyVal) {
        host = hostAddr;
        key = keyVal;
    }

    public void updateAddress(String address) {
        url = address;
    }

    public void addOnCompletedListener(OnCompletedListener onComp) {
        onCompleted = onComp;
    }

    @Override
    protected String doInBackground(String... params) {
        // 1st parameter -> function
        // n [0-x]
        // 2nth parameter -> queryKey
        // 2n + 1th parameter -> queryVal
        if (params.length < 1) {
            throw new IllegalArgumentException("No function parameter is supplied!");
        }
        client = new OkHttpClient();
        Response resp = null;
        StringBuilder urlStr = new StringBuilder(url);
        urlStr.append('/');
        urlStr.append(params[0]);
        int paramsCount = (params.length - 1) / 2;
        for (int i = 0; i < paramsCount; i++) {
            if (i == 0) {
                urlStr.append('?');
            } else {
                urlStr.append('&');
            }
            urlStr.append(params[i * 2 + 1]);
            urlStr.append('=');
            urlStr.append(params[i * 2 + 2]);
        }
        Request request = new Request.Builder().url(urlStr.toString()).
                get().
                addHeader("x-rapidapi-host", host).
                addHeader("x-rapidapi-key", key).
                build();
        try {
            Log.d("deneme", "1");
            resp = client.newCall(request).execute();
            Log.d("deneme", "2");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            return resp.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPreExecute() {
        dialog.setMessage("Loading ...");
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                RestApiClient.this.cancel(true);
            }
        });
    }

    protected void onProgressUpdate(Void v) {
    }

    protected void onPostExecute(String result) {
        onCompleted.onComplete(result);
        dialog.dismiss();
    }
}
