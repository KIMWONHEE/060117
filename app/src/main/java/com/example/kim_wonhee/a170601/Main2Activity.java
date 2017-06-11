package com.example.kim_wonhee.a170601;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main2Activity extends AppCompatActivity {

    String urlstr = "";
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        editText.setText("http://www.google.com");
    }

    public void onMyClick(View v) {
        if (v.getId() == R.id.button) {
            mythread.start();
        }
    }

    Handler myHandler = new Handler();
    Thread mythread = new Thread() {
        @Override
        public void run() {
            try {

                urlstr = editText.getText().toString();
                URL url = new URL(urlstr);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final String data = readData(urlConnection.getInputStream());

                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data);
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    String readData(InputStream is) {
        String data = "";

        Scanner s = new Scanner(is);
        while (s.hasNext())
            data += s.nextLine() + "\n";
        s.close();
        return data;
    }

}
