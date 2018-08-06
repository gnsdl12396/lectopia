package com.lectopia.tsop.movieapplication;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvGenre;
    private TextView tvActors;
    private TextView tvPlot;
    private TextView tvReleased;
    private TextView tvDirector;
    private ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvActors = findViewById(R.id.tvActors);
        tvDirector = findViewById(R.id.tvDirector);
        tvGenre = findViewById(R.id.tvGenre);
        tvPlot = findViewById(R.id.tvPlot);
        tvReleased = findViewById(R.id.tvRelease);
        etTitle = findViewById(R.id.etTitle);
        ivPoster = findViewById(R.id.imgPoster);

        Button btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTitle.getText() != null && !etTitle.getText().toString().isEmpty()) {
                    //MySearchTask
                    new MySearchTask().execute(etTitle.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "타이틀을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class MySearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject result = new JSONObject(s);
                tvActors.setText(result.getString("Actors"));
                tvDirector.setText(result.getString("Director"));
                tvGenre.setText(result.getString("Genre"));
                tvPlot.setText(result.getString("Plot"));
                tvReleased.setText(result.getString("Released"));
                String imgUrl = result.getString("Poster");
                new ImageTask().execute(imgUrl);
            } catch(Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {
            String title = strings[0];
            String params = "apikey=e383c2a5&t=" + title;
            String urlStr = "http://www.omdbapi.com/?" + params;
            StringBuilder sb = null;
            try {
//                byte[] data = params.getBytes("UTF-8");
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.getOutputStream().write(params.getBytes("UTF-8"));

                sb = new StringBuilder();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    cancel(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    class ImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ivPoster.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String src = strings[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(src);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream input = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                cancel(true);
            }
            return bitmap;
        }
    }
}
