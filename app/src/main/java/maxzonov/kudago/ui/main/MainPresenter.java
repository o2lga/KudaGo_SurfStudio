package maxzonov.kudago.ui.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import maxzonov.kudago.model.City;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.retrofit.EventApiService;
import maxzonov.kudago.retrofit.CityApiService;
import maxzonov.kudago.retrofit.RetrofitClient;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String FIRST_PAGE_TO_LOAD = "1";
    private static final String CITY_MOSCOW_NAME = "Москва";
    private static final String CITY_SAINT_PETERSBURG_NAME = "Санкт-Петербург";

    public void getData(CompositeDisposable compositeDisposable, String citySlug) {

        CityApiService cityApiService = RetrofitClient.getCityApiService();
        compositeDisposable.add(cityApiService.getCityJson()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCityDataResponse));

        EventApiService eventApiService = RetrofitClient.getApiService();
        compositeDisposable.add(eventApiService.getJson(citySlug, FIRST_PAGE_TO_LOAD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFirstEventsDataResponse, this::handleFirstEventsDataResponseError));
    }

    private void handleCityDataResponse(ArrayList<City> cities) {
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
        getViewState().persistCities(sortedCities);
    }

    private void handleFirstEventsDataResponse(ResponseData responseData) {

        getViewState().showProgress(false);
        getViewState().finishSwipeRefresh();
        getViewState().showData(responseData);
    }

    private void handleFirstEventsDataResponseError(Throwable error) {
        Log.d("myLog", "error");
    }

//    public void loadNextPage(CompositeDisposable compositeDisposable, String nextPageLink) {
//
//        EventApiService apiService = RetrofitClient.getApiService();
//        compositeDisposable.add(apiService.getJson(CITY_NAME, "2")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleFirstEventsDataResponse, this::handleFirstEventsDataResponseError));
//    }
}