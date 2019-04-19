package com.example.strangethings.mvp;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

public interface View extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void showElement(Model.Thing thing);

    void updateList(List<Model.Thing> things);
}
