package com.example.converterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Convert extends AppCompatActivity {
    private EditText currency;
    private Button button3;
    private TextView answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        currency=findViewById(R.id.currency);
        button3=findViewById(R.id.button3);
        answer=findViewById(R.id.answer);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currency.getText().toString().isEmpty()){
                    Toast.makeText(Convert.this, "Empty field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String currency1 = currency.getText().toString();
                    String url = "https://api.coingecko.com/api/v3/simple/price?ids="+ currency1+ "&vs_currencies=USD";
                    new GetURLData().execute(url);
                }
            }
        });
    }
    private class GetURLData extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            super.onPreExecute();
            answer.setText("Waiting...");
        }
        @Override
        protected String doInBackground(String... strings){
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try{
                URL url = new URL(strings[0]);
                connection=(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line=reader.readLine()) != null){
                    buffer.append(line).append("\n");
                    return buffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection != null){
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);


            try {
                JSONObject jsonObject = new JSONObject(result);
                answer.setText("Coin price = "+jsonObject.getJSONObject(currency.getText().toString()).getDouble("usd")+"$");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}