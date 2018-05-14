package maxzonov.kudago.ui.main;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

import maxzonov.kudago.model.main.Event;

public interface MainView extends MvpView {
    void showData(ArrayList<Event> events);
}
