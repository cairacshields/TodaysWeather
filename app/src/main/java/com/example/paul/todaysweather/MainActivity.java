package com.example.paul.todaysweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Connection.HttpManager;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    final static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    final static String API_ID = "&appid=46823a6d3e5a5aea39bd72cf8a706516";
    ImageView search;
    EditText userCity;
    TextView city, temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (ImageView)findViewById(R.id.search);
        userCity =(EditText)findViewById(R.id.userCity);
        city = (TextView)findViewById(R.id.city);
        temp =(TextView)findViewById(R.id.temp);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserCity = userCity.getText().toString().toLowerCase();

                final String COMPLETE_URL = BASE_URL+UserCity+API_ID;

                requestData(COMPLETE_URL);

                userCity.setText("");

            }
        });
    }

    public void requestData(String s){
        FetchData task = new FetchData();
        task.execute(s);
    }

    public class FetchData extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... strings) {

            return HttpManager.getData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject obj = new JSONObject(s);
                JSONObject myObj = obj.getJSONObject("main");

                String cityName = obj.getString("name");
                Double cityTemp = myObj.getDouble("temp");

                Double far = 1.8 * (cityTemp - 273) +32;

                String myfar = String.valueOf(Math.round(far));
                temp.setText(myfar + "°");
                city.setText(cityName.toUpperCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
