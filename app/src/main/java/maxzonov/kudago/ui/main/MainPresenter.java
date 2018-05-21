package maxzonov.kudago.ui.main;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.place.Place;
import maxzonov.kudago.model.main.place.PlaceDetail;
import maxzonov.kudago.retrofit.ApiService;
import maxzonov.kudago.retrofit.PlaceApiService;
import maxzonov.kudago.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Response;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String CITY_NAME = "msk";
    private Vector<PlaceDetail> placeDetails = new Vector<>();

    public void getData(CompositeDisposable compositeDisposable) {
        ApiService apiService = RetrofitClient.getApiService();
        compositeDisposable.add(apiService.getJson(CITY_NAME)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleResponseError));
    }

    private void handleResponse(ResponseData responseData) {

        ArrayList<Event> events = responseData.getEvents();
        PlaceApiService placeApiService = RetrofitClient.getPlaceApiService();
        int eventsSize = events.size();

        for (int i = 0; i < eventsSize; i++) {
            Place place = responseData.getEvents().get(i).getPlace();
            if (place != null) {
                Call<PlaceDetail> call = placeApiService.getPlaceJson(place.getId());

                Thread thread = new Thread(() -> {
                    try {
                        Response<PlaceDetail> response = call.execute();
                        if (response.isSuccessful()) {
                            placeDetails.add(response.body());
                        } else {
                            Log.d("myLog", String.valueOf(response.message()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                placeDetails.add(null);
            }
        }

        ArrayList<PlaceDetail> details = new ArrayList<>(placeDetails);
        getViewState().showProgress(false);
        getViewState().finishSwipeRefresh();
        getViewState().showData(responseData.getEvents(), details);
    }

    private void handleResponseError(Throwable error) {

    }
}