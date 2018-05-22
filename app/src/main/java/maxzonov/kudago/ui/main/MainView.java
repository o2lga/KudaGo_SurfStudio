package maxzonov.kudago.ui.main;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.place.PlaceDetail;

public interface MainView extends MvpView {
    void showData(ArrayList<Event> events);
    void showProgress(boolean toShow);
    void finishSwipeRefresh();
}
