package com.example.strangethings.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.strangethings.R;
import com.example.strangethings.mvp.Model;
import com.example.strangethings.mvp.Presenter;
import com.example.strangethings.mvp.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements View {

    @InjectPresenter
    Presenter presenter;

    ThingsAdapter thingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model.textThingInfoOutput(getResources().getString(R.string.textThingFormatString));
        Model.pictureThingInfoOutput(getResources().getString(R.string.pictureThingFormatString));
        Model.selectorThingInfoOutput(getResources().getString(R.string.selectorThingFormatString));
        Model.variantThingInfoOutput(getResources().getString(R.string.variantThingFormatString));

        thingsAdapter = new ThingsAdapter(new ArrayList<>());
        thingsAdapter.notificator(presenter::selectedElement);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(thingsAdapter);
    }

    @Override
    public void showElement(Model.Thing thing) {
        Toast.makeText(this, thing.info(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateList(List<Model.Thing> things) {
        if (things != null) {
            thingsAdapter.updateList(things);
        }
    }
}
