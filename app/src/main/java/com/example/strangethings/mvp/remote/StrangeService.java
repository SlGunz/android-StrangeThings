package com.example.strangethings.mvp.remote;

import com.example.strangethings.mvp.Model;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class StrangeService {

    private final static String SERVICE_URL = "https://prnk.blob.core.windows.net/tmp/JSONSample.json";

    private static StrangeService strangeService;
    private OkHttpClient client;

    private List<Model.Thing> thingList;

    private StrangeService() {
        this.client = new OkHttpClient();
    }

    public static StrangeService instance() {
        if (strangeService == null) {
            strangeService = new StrangeService();
        }
        return strangeService;
    }

    public List<Model.Thing> things() {

        if (thingList == null) {
            thingList = requestThings();
        }
        return thingList;
    }

    private List<Model.Thing> requestThings() {
        Request request = new Request.Builder()
                .url(SERVICE_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonString = responseBody.string();
                return StrangeThings.instance().make(jsonString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

