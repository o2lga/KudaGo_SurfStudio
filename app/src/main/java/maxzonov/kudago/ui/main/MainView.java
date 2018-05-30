package maxzonov.kudago.ui.main;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

import maxzonov.kudago.model.City;
import maxzonov.kudago.model.ResponseData;

public interface MainView extends MvpView {
    void showData(ResponseData responseData);
    void showProgress(boolean toShow);
    void finishSwipeRefresh();
    void persistCities(ArrayList<City> cities);
}
