package com.example.strangethings.mvp.remote;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.strangethings.mvp.Model.*;

class StrangeThings {

    private static final String TAG = "StrangeThings";

    private static StrangeThings strangeThings;

    private static final String TEXT_ITEM = "hz";
    private static final String PICTURE_ITEM = "picture";
    private static final String SELECTOR_ITEM = "selector";

    private TextThing textThing;
    private PictureThing pictureThing;
    private SelectorThing selectorThing;

    private StrangeThings() {
    }

    static StrangeThings instance() {
        if (strangeThings == null) {
            strangeThings = new StrangeThings();
        }
        return strangeThings;
    }

    List<Thing> make(String jsonString) throws JSONException {

        List<Thing> things = new ArrayList<>();

        JSONObject jsonBody = new JSONObject(jsonString);
        JSONArray items = jsonBody.getJSONArray("data");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String name = item.getString("name").toLowerCase();

            switch (name) {
                case TEXT_ITEM: {
                    textThing = textThing(item);
                    break;
                }
                case PICTURE_ITEM: {
                    pictureThing = pictureThing(item);
                    break;
                }
                case SELECTOR_ITEM: {
                    selectorThing = selectorThing(item);
                    break;
                }
                default:
                    Log.i(TAG, "Parsing error: unknown object name: " + name);
                    break;
            }
        }

        items = jsonBody.getJSONArray("view");
        for (int i = 0; i < items.length(); i++) {
            String name = items.getString(i).toLowerCase();
            switch (name) {
                case TEXT_ITEM:
                    things.add(new TextThing(textThing));
                    break;
                case PICTURE_ITEM:
                    things.add(new PictureThing(pictureThing));
                    break;
                case SELECTOR_ITEM:
                    things.add(new SelectorThing(selectorThing));
                    break;
            }
        }

        return things;
    }

    private TextThing textThing(JSONObject object) throws JSONException {
        JSONObject data = object.getJSONObject("data");
        String text = data.getString("text");
        return new TextThing(text);
    }

    private PictureThing pictureThing(JSONObject object) throws JSONException {
        JSONObject data = object.getJSONObject("data");
        String text = data.getString("text");
        String url = data.getString("url");
        return new PictureThing(url, text);
    }

    private SelectorThing selectorThing(JSONObject object) throws JSONException {
        JSONObject data = object.getJSONObject("data");
        int selectedId = data.getInt("selectedId");

        JSONArray items = data.getJSONArray("variants");
        List<Variant> variants = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            int id = item.getInt("id");
            String text = item.getString("text");
            Variant variant = new Variant(id, text);
            variants.add(variant);
        }
        return new SelectorThing(selectedId, variants);
    }
}
