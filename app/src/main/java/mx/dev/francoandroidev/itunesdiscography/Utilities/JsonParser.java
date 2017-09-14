package mx.dev.francoandroidev.itunesdiscography.Utilities;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import mx.dev.francoandroidev.itunesdiscography.MainActivity;

/**
 * Created by franco on 13/09/17.
 */

public final class JsonParser {
    private static final String TAG = JsonParser.class.getName();

    public static JSONObject getJSON(int requestType) {
        String jsonString = "";
        String requestedUrl = "";
        if(requestType == MainActivity.REQUEST_CATEGORY) {
            requestedUrl = "https://itunes.apple.com/WebObjects/MZStoreServices.woa/ws/genres?id=34";//"http://itunes.apple.com/search?term=genre&media=music&limit=50";
        }
        else if (requestType == MainActivity.REQUEST_ARTIST){
            //Not implemented yet
        }
        else {
            //Not implemented yet
        }

        //Get the json from specified URL
            try {
                URL url = new URL(requestedUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                jsonString = getJsonString(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        JSONObject jsonObj = null;
        JSONObject jsonObject = null;
        JSONObject jsonObject1 = null;

        try {
            jsonObj = new JSONObject(jsonString);
            jsonObject = new JSONObject(jsonObj.getString("34"));
            jsonObject1 = new JSONObject(jsonObject.getString("subgenres"));
            //Log.d("jsonArrayCategories",         jsonObject1.length()  +"");
            //jsonArrayCategories = new JSONArray(jsonObject.getString("subgenres"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject1;
    }

    private static String getJsonString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
