package com.example.strangethings.mvp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Model {

    public static final int TEXT_THING = 0;
    public static final int PICTURE_THING = 1;
    public static final int SELECTOR_THING = 2;
    public static final int SELECTOR_SUBTHING = 3;


    public interface Thing {
        int name();

        String info();
    }

    /**
     * TextThing - class represents Text item model.
     */
    public static class TextThing implements Thing {

        private String mText;

        public TextThing(String text) {
            mText = text;
        }

        public TextThing(TextThing textThing) {
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

        public String text() {
            return mText;
        }
    }

    /**
     * PictureThing - class represents item model with picture and title.
     */
    public static class PictureThing implements Thing {

        private String mUrl;
        private String mText;

        public PictureThing(String url, String text) {
            mUrl = url;
            mText = text;
        }

        public PictureThing(PictureThing pictureThing) {
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

        public String text() {
            return mText;
        }

        public String url() {
            return mUrl;
        }
    }

    /**
     * SelectorThing - class represents item model with VariantId and sub-items.
     */
    public static class SelectorThing implements Thing {

        private int mSelectorId;
        private List<Model.Variant> mVariants;

        public SelectorThing(int id, List<Model.Variant> variants) {
            mSelectorId = id;
            mVariants = variants;
        }

        public SelectorThing(SelectorThing selectorThing) {
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

        public int selectorId() {
            return mSelectorId;
        }

        public List<Model.Variant> variants() {
            return mVariants;
        }
    }

    /**
     * Variant - sub-item of {@link SelectorThing} class
     */
    public static class Variant implements Thing {

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

        public int id() {
            return mId;
        }

        public String text() {
            return mText;
        }
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
}
