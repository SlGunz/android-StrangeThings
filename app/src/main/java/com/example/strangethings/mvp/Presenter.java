package com.example.strangethings.mvp;

import android.os.AsyncTask;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.strangethings.mvp.remote.StrangeService;

import java.lang.ref.WeakReference;
import java.util.List;

@InjectViewState
public class Presenter extends MvpPresenter<View> {

    public void selectedElement(Model.Thing thing) {
        getViewState().showElement(thing);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        new ServerRequest(this).execute();
    }

    static class ServerRequest extends AsyncTask<Void, Void, List<Model.Thing>> {

        WeakReference<Presenter> presenterWeakReference;

        ServerRequest(Presenter presenter) {
            presenterWeakReference = new WeakReference<>(presenter);
        }

        @Override
        protected List<Model.Thing> doInBackground(Void... voids) {
            return StrangeService.instance()
                    .things();
        }

        @Override
        protected void onPostExecute(List<Model.Thing> things) {
            super.onPostExecute(things);

            Presenter presenter = presenterWeakReference.get();
            if (presenter != null) {
                presenter.getViewState()
                        .updateList(things);
            }
        }
    }
}
