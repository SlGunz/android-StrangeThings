package com.example.strangethings.mvp;

import java.util.List;

public abstract class Model {

    public static final int TEXT_THING = 0;
    public static final int PICTURE_THING = 1;
    public static final int SELECTOR_THING = 2;
    public static final int SELECTOR_SUBTHING = 3;

    public interface Thing {
        int name();

        String info();
    }

    public interface TextThing extends Thing {
        String text();
    }

    public interface PictureThing extends TextThing {
        String url();
    }

    public interface SelectorThing extends Thing {
        int selectorId();

        List<Variant> variants();
    }

    public interface Variant extends Thing {
        int id();

        String text();
    }
}
