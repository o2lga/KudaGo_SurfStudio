package maxzonov.kudago.ui.main;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

import maxzonov.kudago.model.City;
import maxzonov.kudago.model.ResponseData;

public interface EventsView extends MvpView {
    void showData(ResponseData responseData);
    void showLoadingProgress(boolean toShow);
    void finishSwipeRefresh();
    void persistCities(ArrayList<City> cities);
    void showAdditionalData(ResponseData responseData);
    void handleInternetError();
    void showPaginationError();
}
