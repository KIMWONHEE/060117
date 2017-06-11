package com.example.kim_wonhee.a170601;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main4Activity extends AppCompatActivity {
    EditText editText, editText2;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        setTitle("Login");

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        textView = (TextView) findViewById(R.id.textView);

        editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void onMyClick(View v){
        if (v.getId() == R.id.button) {
            if (editText.getText().toString().equals("") || editText2.getText().toString().equals(""))
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            else
                thread.start();
        }
    }


    Handler handler = new Handler();
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                URL url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                String userid = editText.getText().toString();
                String password = editText2.getText().toString();

                String postData = "userid=" + URLEncoder.encode(userid) + "&password" + URLEncoder.encode(password);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream;

                if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
                final String result = loginResult(inputStream);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("FAIL"))
                            textView.setText("로그인이 실패했습니다.");
                        else
                            textView.setText(result + "님 로그인 성공");
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String loginResult(InputStream is) throws IOException {

            String str = "";
            Scanner s = new Scanner(is);
            while (s.hasNext())
                str += s.nextLine();
            s.close();
            return str;

        }
    };


}
