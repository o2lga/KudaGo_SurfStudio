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
import maxzonov.kudago.retrofit.ApiService;
import maxzonov.kudago.retrofit.CityApiService;
import maxzonov.kudago.retrofit.RetrofitClient;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public void getData(CompositeDisposable compositeDisposable, String citySlug) {

        CityApiService cityApiService = RetrofitClient.getCityApiService();
        compositeDisposable.add(cityApiService.getCityJson()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCityDataResponse));

        ApiService apiService = RetrofitClient.getApiService();
        compositeDisposable.add(apiService.getJson(citySlug, "1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleDataResponse, this::handleDataResponseError));
    }

    private void handleCityDataResponse(ArrayList<City> cities) {
        ArrayList<City> sortedCities = new ArrayList<>();
        for (City city : cities) {
            switch (city.getName()) {
                case "Москва":
                    sortedCities.add(0, city);
                    break;
                case "Санкт-Петербург":
                    sortedCities.add(1, city);
                    break;
                default:
                    sortedCities.add(city);
                    break;
            }
        }
        getViewState().persistCities(sortedCities);
    }

    private void handleDataResponse(ResponseData responseData) {

        getViewState().showProgress(false);
        getViewState().finishSwipeRefresh();
        getViewState().showData(responseData);
    }

    private void handleDataResponseError(Throwable error) {
        Log.d("myLog", "error");
    }
}