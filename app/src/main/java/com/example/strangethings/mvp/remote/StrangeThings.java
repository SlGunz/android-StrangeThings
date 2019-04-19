package com.example.strangethings.mvp.remote;

import android.util.Log;
import com.example.strangethings.mvp.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Things {

    private static final String TAG = "Things";

    public static final int TEXT_THING = 0;
    public static final int PICTURE_THING = 1;
    public static final int SELECTOR_THING = 2;
    private static final int VARIANT_THING = 3;

    private static final String TEXT_ITEM = "hz";
    private static final String PICTURE_ITEM = "picture";
    private static final String SELECTOR_ITEM = "selector";

    private TextThing textThing;
    private PictureThing pictureThing;
    private SelectorThing selectorThing;

    private List<Integer> views;

    public Things(String jsonString) throws JSONException {

        views = new ArrayList<>();

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
                    views.add(TEXT_THING);
                    break;
                case PICTURE_ITEM:
                    views.add(PICTURE_THING);
                    break;
                case SELECTOR_ITEM:
                    views.add(SELECTOR_THING);
                    break;
            }
        }
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
        int selectedId = data.getInt("selectorId");

        JSONArray items = data.getJSONArray("variants");
        List<Model.Variant> variants = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            int id = item.getInt("id");
            String text = item.getString("text");
            Variant variant = new Variant(id, text);
            variants.add(variant);
        }
        return new SelectorThing(selectedId, variants);
    }

    private static class TextThing implements Model.TextThing {

        private String mText;

        public TextThing(String text) {
            mText = text;
        }

        @Override
        public int name() {
            return TEXT_THING;
        }

        @Override
        public String info() {
            return "Text: " + mText;
        }

        @Override
        public String text() {
            return mText;
        }
    }

    private static class PictureThing implements Model.PictureThing {

        private String mUrl;
        private String mText;

        public PictureThing(String url, String text) {
            mUrl = url;
            mText = text;
        }

        @Override
        public int name() {
            return PICTURE_THING;
        }

        @Override
        public String info() {
            return "Picture text:" + mText;
        }

        @Override
        public String text() {
            return mText;
        }

        @Override
        public String url() {
            return mUrl;
        }
    }

    private static class SelectorThing implements Model.SelectorThing {

        private int mSelectorId;
        private List<Model.Variant> mVariants;

        public SelectorThing(int id, List<Model.Variant> variants) {
            mSelectorId = id;
            mVariants = variants;
        }

        @Override
        public int name() {
            return SELECTOR_THING;
        }

        @Override
        public String info() {
            return String.format(Locale.getDefault(),
                    "Selector ID: %d, variants count: %d", mSelectorId, variants().size());
        }

        @Override
        public int selectorId() {
            return mSelectorId;
        }

        @Override
        public List<Model.Variant> variants() {
            return mVariants;
        }
    }

    private static class Variant implements Model.Variant {

        private int mId;
        private String mText;

        public Variant(int id, String text) {
            mId = id;
            mText = text;
        }

        @Override
        public int name() {
            return VARIANT_THING;
        }

        @Override
        public String info() {
            return null;
        }

        @Override
        public int id() {
            return mId;
        }

        @Override
        public String text() {
            return mText;
        }
    }
}
