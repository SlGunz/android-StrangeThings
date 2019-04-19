package com.example.strangethings.mvp.remote;

import android.util.Log;
import com.example.strangethings.mvp.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StrangeThings {

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

    private static String sTextThingInfo = "Text: %s";
    private static String sPictureThingInfo = "Picture text: %s";
    private static String sSelectorFormatString = "Selector ID: %d, variants count: %d";
    private static String sVariantFormatString = "Variant ID: %d, text: %s";

    /**
     * Sets an output format string. It shows when you select an item.
     *
     * @param format format string. Default value: "Text: %s"
     */
    public static void textThingInfoOutput(String format) {
        sTextThingInfo = format;
    }

    /**
     * Sets an output format string. It shows when you select an item.
     *
     * @param format format string. Default value: "Picture text: %s"
     */
    public static void pictureThingInfoOutput(String format) {
        sPictureThingInfo = format;
    }

    /**
     * Sets an output format string. It shows when you select an item.
     *
     * @param format format string. Default value: "Selector ID: %d, variants count: %d"
     */
    public static void selectorThingInfoOutput(String format) {
        sSelectorFormatString = format;
    }

    /**
     * Sets an output format string. It shows when you select an item.
     *
     * @param format format string. Default value: "Variant ID: %d, text: %s"
     */
    public static void variantThingInfoOutput(String format) {
        sVariantFormatString = format;
    }


    List<Model.Thing> make(String jsonString) throws JSONException {

        List<Model.Thing> things = new ArrayList<>();

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

        TextThing(String text) {
            mText = text;
        }

        TextThing(TextThing textThing) {
            mText = textThing.mText;
        }

        @Override
        public int name() {
            return Model.TEXT_THING;
        }

        @Override
        public String info() {
            return String.format(Locale.getDefault(), sTextThingInfo, mText);
        }

        @Override
        public String text() {
            return mText;
        }
    }

    private static class PictureThing implements Model.PictureThing {

        private String mUrl;
        private String mText;

        PictureThing(String url, String text) {
            mUrl = url;
            mText = text;
        }

        PictureThing(PictureThing pictureThing) {
            mUrl = pictureThing.mUrl;
            mText = pictureThing.mText;
        }

        @Override
        public int name() {
            return Model.PICTURE_THING;
        }

        @Override
        public String info() {
            return String.format(Locale.getDefault(), sPictureThingInfo, mText);
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

        SelectorThing(int id, List<Model.Variant> variants) {
            mSelectorId = id;
            mVariants = variants;
        }

        SelectorThing(SelectorThing selectorThing) {
            mSelectorId = selectorThing.mSelectorId;
            mVariants = new ArrayList<>(selectorThing.mVariants);
        }

        @Override
        public int name() {
            return Model.SELECTOR_THING;
        }

        @Override
        public String info() {
            return String.format(Locale.getDefault(), sSelectorFormatString, mSelectorId, variants().size());
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
            return Model.SELECTOR_SUBTHING;
        }

        @Override
        public String info() {
            return String.format(Locale.getDefault(), sVariantFormatString, mId, mText);
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
