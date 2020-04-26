package com.example.myapplication;

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

    public RestApiClient(Activity mainAct) {
//        url = address;
//        host = hostAddr;
//        key = keyVal;
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
    protected String doInBackground(String... urls) {
        // we are only interested in the first url, multi url calls are not supported
        client = new OkHttpClient();
        Response resp = null;
        Request request = new Request.Builder()
            .url("https://covid-193.p.rapidapi.com/statistics?country=turkey")
            .get()
            .addHeader("x-rapidapi-host", "covid-193.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "c63fd2820dmsh78d8229f8c575dap1f9f83jsnc3071c1d492c")
            .build();
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
