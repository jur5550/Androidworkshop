package com.example.jurvanzonneveld.helloworld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    public void sendMessage(View view){
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText2);
//        String message = editText.getText().toString();
//        AsyncJsonActivity getjson = new AsyncJsonActivity();
//
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
//}
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = " com.example.helloworld.MESSAGE";
    Button btnHit;
    TextView txtJson;
    ProgressDialog pd;
    public String apiUrl = "http://api.tvmaze.com/search/shows?q=girls";
//    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
//    // that is used to execute network ops.
//    private NetworkFragment mNetworkFragment;
//
//    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
//    // downloads with consecutive button clicks.
//    private boolean mDownloading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHit = (Button) findViewById(R.id.button);
        txtJson = (TextView) findViewById(R.id.textView2);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute(apiUrl);
            }
        });
    }
    private class JsonTask extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.show();

        }
        protected String doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine())!= null){
                    buffer.append(line+"\n");
                    Log.d("Response: ", " > "+ line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(pd.isShowing()){
                pd.dismiss();
            }
            JSONArray mJsonArray = null;
            try {
                mJsonArray = new JSONArray(result);

                //hier pakt hij de eerste resultaat, is alleen gedaan om te zien of we er objecten van kunnen maken.
                // wil je gewoon het hele resultaat weer geven vervang de setText met
                //txtJson.setText(result);
                JSONObject shows = mJsonArray.getJSONObject(0);
                txtJson.setText(shows.getString("show"));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }
}