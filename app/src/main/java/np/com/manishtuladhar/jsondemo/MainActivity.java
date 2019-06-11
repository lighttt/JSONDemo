package np.com.manishtuladhar.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String city ="Kathmandu";
    String app_id = "04402e294a942dab68a991dc735cf2e4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+app_id);
    }

/*
   ----------------- Downloading the JSON Data --------------------------
*/
    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1)
                {

                    char current = (char)data;

                    result += current;

                    data = reader.read();
                }

                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        // on post execute helps to update label on UI thread which doinbackground cannot
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try
            {

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Website content", weatherInfo);

                JSONArray array = new JSONArray(weatherInfo);

                for(int i = 0; i<array.length(); i++)
                {

                    JSONObject jsonPart = array.getJSONObject(i);

                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
