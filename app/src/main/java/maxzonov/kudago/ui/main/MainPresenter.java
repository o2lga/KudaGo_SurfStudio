package maxzonov.kudago.ui.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.retrofit.ApiService;
import maxzonov.kudago.retrofit.RetrofitClient;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String CITY_NAME = "msk";

    public void getData(CompositeDisposable compositeDisposable) {
        ApiService apiService = RetrofitClient.getApiService();
        compositeDisposable.add(apiService.getJson(CITY_NAME)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleResponseError));
    }

    private void handleResponse(ResponseData responseData) {

        getViewState().showProgress(false);
        getViewState().finishSwipeRefresh();
        getViewState().showData(responseData.getEvents());
    }

    private void handleResponseError(Throwable error) {

    }
}