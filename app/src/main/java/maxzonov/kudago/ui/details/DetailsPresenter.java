package maxzonov.kudago.ui.details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.model.detail.DetailEvent;
import maxzonov.kudago.retrofit.ApiService;
import maxzonov.kudago.retrofit.DetailsApiService;
import maxzonov.kudago.retrofit.RetrofitClient;
import maxzonov.kudago.ui.main.MainView;

@InjectViewState
public class DetailsPresenter extends MvpPresenter<DetailsView> {
}
