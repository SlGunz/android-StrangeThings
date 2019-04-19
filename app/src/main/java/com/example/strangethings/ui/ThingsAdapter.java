package com.example.strangethings.ui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.strangethings.R;
import com.example.strangethings.mvp.Model;

import java.util.List;

public class ThingsAdapter extends RecyclerView.Adapter<ThingsAdapter.ThingViewHolder> {

    ///////////////////////////////////////////////////////////////////////////
    // Adapter
    ///////////////////////////////////////////////////////////////////////////

    public interface Notification {
        void show(Model.Thing thing);
    }

    private List<Model.Thing> thingsList;
    private Notification notification;
    private Context context;

    public ThingsAdapter(List<Model.Thing> items) {
        thingsList = items;
    }

    public void notificator(Notification notification) {
        this.notification = notification;
    }

    public void updateList(@NonNull List<Model.Thing> newThingList) {
        thingsList.clear();
        thingsList.addAll(newThingList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext().getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewHolder;
        switch (viewType) {
            case Model.TEXT_THING:
                viewHolder = inflater.inflate(R.layout.list_item_text_thing, viewGroup, false);
                return new TextViewHolder(viewHolder, notification);
            case Model.PICTURE_THING:
                viewHolder = inflater.inflate(R.layout.list_item_picture_thing, viewGroup, false);
                return new PictureViewHolder(viewHolder, notification);
            case Model.SELECTOR_THING:
                viewHolder = inflater.inflate(R.layout.list_item_selector_thing, viewGroup, false);
                return new SelectorViewHolder(viewHolder, notification);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ThingViewHolder viewHolder, int i) {
        viewHolder.bind(context, thingsList.get(i));
    }

    @Override
    public int getItemCount() {
        return thingsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return thingsList.get(position).name();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolders
    ///////////////////////////////////////////////////////////////////////////

    abstract static class ThingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Notification clickNotification;
        Model.Thing thing;

        ThingViewHolder(@NonNull View viewHolder, Notification notification) {
            super(viewHolder);
            viewHolder.setOnClickListener(this);
            clickNotification = notification;
        }

        @Override
        public void onClick(View v) {
            clickNotification.show(thing);
        }

        @CallSuper
        void bind(Context context, Model.Thing t) {
            thing = t;
        }
    }

    static class TextViewHolder extends ThingViewHolder {

        private TextView textView;

        TextViewHolder(@NonNull View viewHolder, Notification notification) {
            super(viewHolder, notification);
            textView = viewHolder.findViewById(R.id.textView);
        }

        @Override
        void bind(Context context, Model.Thing t) {
            super.bind(context, t);
            textView.setText(((Model.TextThing)t).text());
        }
    }

    static class PictureViewHolder extends ThingViewHolder {

        ImageView imageView;
        TextView textView;

        PictureViewHolder(@NonNull View viewHolder, Notification notification) {
            super(viewHolder, notification);
            imageView = viewHolder.findViewById(R.id.imageView);
            textView = viewHolder.findViewById(R.id.textView);
        }

        @Override
        void bind(Context context, Model.Thing t) {
            super.bind(context, t);
            Model.PictureThing thing = (Model.PictureThing) t;
            Glide.with(context).load(thing.url()).into(imageView);
            textView.setText(thing.text());
        }
    }

    static class SelectorViewHolder extends ThingViewHolder {

        SelectorView selectorView;

        SelectorViewHolder(@NonNull View viewHolder, Notification notification) {
            super(viewHolder, notification);
            selectorView = viewHolder.findViewById(R.id.selectorView);
        }

        void bind(Context context, Model.Thing t) {
            super.bind(context, t);
            Model.SelectorThing thing = (Model.SelectorThing) t;
            selectorView.setVariants(context, thing.variants(), clickNotification);
        }
    }
}
