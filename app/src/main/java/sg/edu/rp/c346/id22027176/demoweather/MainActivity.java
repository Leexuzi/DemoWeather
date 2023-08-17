package sg.edu.rp.c346.id22027176.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    ListView lvWeather;
    ArrayAdapter aaWeather;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWeather = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Weather> alWeather = new ArrayList<Weather>();
        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler() {
            String area;
            String forecast;
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArrItems = response.getJSONArray("items");
                    JSONObject firstObj = jsonArrItems.getJSONObject(0);
                    JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                    for(int i = 0; i < jsonArrForecasts.length(); i++) {
                        JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                        area = jsonObjForecast.getString("area");
                        forecast = jsonObjForecast.getString("forecast");
                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                        Log.d("we",weather.toString());
                    }
                }
                catch(JSONException e){
                }
                //POINT X – Code to display List View
                ArrayList dispForecasts = new ArrayList<>();
                for(int i=0; i<alWeather.size(); i++){
                    dispForecasts.add(alWeather.get(i).toString());
                }
                aaWeather = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, dispForecasts);
                lvWeather.setAdapter(aaWeather);
            }//end onSuccess
        });
    }//end onResume
}