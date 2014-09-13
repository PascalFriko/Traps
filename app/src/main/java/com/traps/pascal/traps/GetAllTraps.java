package com.traps.pascal.traps;

import android.os.Message;
import android.util.JsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pascal on 13.09.2014.
 */
public class GetAllTraps {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static JsonReader reader = null;

    public GetAllTraps() {

    }

    public List getJSONfromURL(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = (httpClient.execute(httpGet));
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        List<List> messages = new ArrayList<List>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                messages.add(readMessage(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List readMessage (JsonReader reader) throws IOException {

        int trap_ID = -1;
        double longitude = -1;
        double latitude = -1;
        int allowed_speed = -1;
        String date_of_entry = null;
        String address = null;
        String city = null;
        String country = null;
        int direction = -1;

        List data = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("trap_ID")){
                trap_ID = reader.nextInt();
                data.add(trap_ID);
            } else if (name.equals("longitude")) {
                longitude = reader.nextDouble();
                data.add(longitude);
            } else if (name.equals("latitude")) {
                latitude = reader.nextDouble();
                data.add(latitude);
            } else if (name.equals("allowed_speed")) {
                allowed_speed = reader.nextInt();
                data.add(allowed_speed);
            } else if (name.equals("date_of_entry")) {
                date_of_entry = reader.nextString();
                data.add(date_of_entry);
            } else if (name.equals("address")) {
                address = reader.nextString();
                data.add(address);
            } else if (name.equals("city")) {
                city = reader.nextString();
                data.add(city);
            } else if (name.equals("country")) {
                country = reader.nextString();
                data.add(country);
            } else if (name.equals("direction")) {
                direction = reader.nextInt();
                data.add(direction);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return data;
    }

}
