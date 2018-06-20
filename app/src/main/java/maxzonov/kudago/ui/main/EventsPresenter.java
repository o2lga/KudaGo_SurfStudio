package maxzonov.kudago.ui.main;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import maxzonov.kudago.model.City;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.retrofit.EventApiService;
import maxzonov.kudago.retrofit.CityApiService;
import maxzonov.kudago.retrofit.RetrofitClient;
import maxzonov.kudago.utils.Utility;

@InjectViewState
public class EventsPresenter extends MvpPresenter<EventsView> {

    private static final String FIRST_PAGE_TO_LOAD = "1";
    private static final String CITY_MOSCOW_NAME = "Москва";
    private static final String CITY_SAINT_PETERSBURG_NAME = "Санкт-Петербург";

    public void getData(Context context, CompositeDisposable compositeDisposable, String citySlug) {

        if (Utility.isNetworkAvailable(context)) {
            CityApiService cityApiService = RetrofitClient.getCityApiService();
            compositeDisposable.add(cityApiService.getCityJson()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleCityDataResponse));

            EventApiService eventApiService = RetrofitClient.getApiService();
            compositeDisposable.add(eventApiService.getJson(citySlug, FIRST_PAGE_TO_LOAD)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleFirstEventsDataResponse, this::handleFirstEventsDataResponseError));
        } else {
            getViewState().handleInternetError();
        }
    }

    private void handleCityDataResponse(ArrayList<City> cities) {
        ArrayList<City> sortedCities = new ArrayList<>(sortCitiesArray(cities));
        getViewState().persistCities(sortedCities);
    }

    private void handleFirstEventsDataResponse(ResponseData responseData) {
        getViewState().showLoadingProgress(false);
        getViewState().finishSwipeRefresh();
        getViewState().showData(responseData);
    }

    private void handleFirstEventsDataResponseError(Throwable error) {
        Log.e("myLog", error.getMessage());
        getViewState().handleInternetError();
    }

    public void loadNextPage(Context context, CompositeDisposable compositeDisposable, String pageToLoad, String cityName) {

        if (Utility.isNetworkAvailable(context)) {
            EventApiService apiService = RetrofitClient.getApiService();
            compositeDisposable.add(apiService.getJson(cityName, pageToLoad)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleNextPageEventsDataResponse, this::handleNextPageEventsDataResponseError));
        } else {
            getViewState().showPaginationError();
        }
    }

    private void handleNextPageEventsDataResponse(ResponseData responseData) {
        getViewState().showAdditionalData(responseData);
    }

    private void handleNextPageEventsDataResponseError(Throwable error) {
        Log.e("myLog", error.getMessage());
        getViewState().showPaginationError();
    }

    private ArrayList<City> sortCitiesArray(ArrayList<City> cities) {
        ArrayList<City> sortedCities = new ArrayList<>();
        for (City city : cities) {
            switch (city.getName()) {
                case CITY_MOSCOW_NAME:
                    sortedCities.add(0, city);
                    break;
                case CITY_SAINT_PETERSBURG_NAME:
                    sortedCities.add(1, city);
                    break;
                default:
                    sortedCities.add(city);
                    break;
            }
        }
        return sortedCities;
    }
}