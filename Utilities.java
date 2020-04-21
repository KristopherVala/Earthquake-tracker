package com.example.myapplication;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static final String LOG_TAG = Utilities.class.getSimpleName();

    public static List<String> fetchEarthquake(String request){
        URL url = createUrl(request);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG , "Error closing stream", e);
        }
        List <String> earthquakeInfo;

        earthquakeInfo = extractFeatureFromJson(jsonResponse);
        return earthquakeInfo;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }



    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    //read into the buffered stream

    private static List<String> extractFeatureFromJson(String earthquakes) {
        if (TextUtils.isEmpty(earthquakes)) {
            return null;
        }
        List<String> quakesTemp = new ArrayList();
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakes);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");

            if (featureArray.length() > 0) {
                for(int i = 0 ; i < featureArray.length() ; i++){
                    StringBuilder quakeers = new StringBuilder();
                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");
                    String time = properties.getString("time");
                    String location = properties.getString("title");
                    String url = properties.getString("url");
                    String magn = properties.getString("mag");

                    quakeers.append(time + "$");
                    quakeers.append(location + "$");
                    quakeers.append(url +"$");
                    quakeers.append(magn);

                    //add to string here
                    quakesTemp.add(quakeers.toString());
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        if(quakesTemp.size() != 0){
            return quakesTemp;
        }else
            return null;
    }


}
