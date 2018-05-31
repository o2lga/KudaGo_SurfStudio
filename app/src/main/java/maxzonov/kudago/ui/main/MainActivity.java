package maxzonov.kudago.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import maxzonov.kudago.model.City;
import maxzonov.kudago.ui.city.CityActivity;
import maxzonov.kudago.R;
import maxzonov.kudago.model.ResponseData;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.place.Place;
import maxzonov.kudago.ui.adapter.EventAdapter;
import maxzonov.kudago.ui.details.DetailsActivity;
import maxzonov.kudago.utils.OnEventClickListener;
import maxzonov.kudago.utils.Utility;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter MainPresenter mainPresenter;

    private static final String INTENT_IMAGES_URLS_ARRAY_ID = "images";
    private static final String INTENT_PLACE_ID = "place";
    private static final String INTENT_COORDINATES_ID = "coords";
    private static final String INTENT_DESCRIPTION_ID = "descr";
    private static final String INTENT_FULL_DESCRIPTION_ID = "full_descr";
    private static final String INTENT_TITLE_ID = "title";
    private static final String INTENT_DATE_ID = "date";
    private static final String INTENT_PRICE_ID = "price";

    private static final String INTENT_CITIES_ARRAY_ID = "cities";
    private static final String INTENT_CITY_ID = "city";

    private static final String CITY_MOSCOW_SLUG = "msk";

    private static final int CITY_REQUEST = 101;

    private CompositeDisposable compositeDisposable;
    private OnEventClickListener eventClickListener;
    private ArrayList<Event> events = new ArrayList<>();
    private ResponseData responseData = new ResponseData();

    private String currentCity = "msk";
    private ArrayList<City> cities = new ArrayList<>();

    @BindView(R.id.coordinator_layout_main) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_rv) RecyclerView recyclerView;
    @BindView(R.id.main_pb) ProgressBar progressBar;
    @BindView(R.id.main_swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.main_toolbar) Toolbar toolbar;

    @BindView(R.id.main_layout_no_internet) LinearLayout layoutNoInternet;
    @BindView(R.id.main_layout_content) LinearLayout layoutContent;

    @BindView(R.id.main_toolbar_tv_city) TextView tvToolbarCity;

    private LinearLayoutManager layoutManager;
    private EventAdapter eventAdapter;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        unbinder = ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        showProgress(true);
        recyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(events, eventClickListener);
        recyclerView.setAdapter(eventAdapter);

        if (Utility.isNetworkAvailable(this)) {
            layoutNoInternet.setVisibility(View.GONE);
            mainPresenter.getData(compositeDisposable, CITY_MOSCOW_SLUG);
        } else {
            handleInternetError();
        }

        eventClickListener = ((view, position) -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

            Event event = events.get(position);

            ArrayList<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < event.getImages().size(); i++) {
                imageUrls.add(event.getImages().get(i).getImageUrl());
            }

            Place place = event.getPlace();
            ArrayList<String> coords = new ArrayList<>();
            if (place != null) {
                coords.add(place.getCoords().getLatitude());
                coords.add(place.getCoords().getLongitude());
                intent.putExtra(INTENT_PLACE_ID, event.getPlace().getTitle());
                intent.putStringArrayListExtra(INTENT_COORDINATES_ID, coords);
            } else {
                intent.putExtra(INTENT_PLACE_ID, "");
            }

            intent.putExtra(INTENT_TITLE_ID, event.getTitle());
            intent.putExtra(INTENT_DESCRIPTION_ID, event.getDescription());
            intent.putExtra(INTENT_FULL_DESCRIPTION_ID, event.getFullDescription());
            intent.putExtra(INTENT_PRICE_ID, event.getPrice());
            intent.putStringArrayListExtra(INTENT_IMAGES_URLS_ARRAY_ID, imageUrls);
            startActivity(intent);
        });

        swipeRefresh.setOnRefreshListener(() -> {
            if (Utility.isNetworkAvailable(this)) {
                layoutNoInternet.setVisibility(View.GONE);
                mainPresenter.getData(compositeDisposable, currentCity);
            } else {
                swipeRefresh.setRefreshing(false);
                handleInternetError();
            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private void handleInternetError() {
        progressBar.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        layoutNoInternet.setVisibility(View.VISIBLE);

        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                getResources().getString(R.string.main_snackbar_no_internet),
                Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CITY_REQUEST) {
            events.clear();
            showProgress(true);

            int cityId = data.getIntExtra(INTENT_CITY_ID, 0);
            String cityName = cities.get(cityId).getName();
            String citySlug = cities.get(cityId).getSlug();
            currentCity = citySlug;

            tvToolbarCity.setText(cityName);
            mainPresenter.getData(compositeDisposable, citySlug);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.main_toolbar_city_layout)
    void onToolbarClicked() {
        ArrayList<String> stringsCity = new ArrayList<>();
        int citySize = cities.size();
        for (int i = 0; i < citySize; i++) {
            City itemCity = cities.get(i);
            stringsCity.add(itemCity.getName());
        }
        Intent intent = new Intent(MainActivity.this, CityActivity.class);
        intent.putStringArrayListExtra(INTENT_CITIES_ARRAY_ID, stringsCity);
        startActivityForResult(intent, CITY_REQUEST);
    }

    @Override
    public void showData(ResponseData responseData) {
        this.events = responseData.getEvents();
        this.responseData = responseData;
//        eventAdapter.addToAdapterArray(responseData.getEvents());
        eventAdapter = new EventAdapter(responseData.getEvents(), eventClickListener);
        recyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void showProgress(boolean toShow) {
        if (toShow) {
            progressBar.setVisibility(View.VISIBLE);
            layoutContent.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finishSwipeRefresh() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void persistCities(ArrayList<City> cities) {
        this.cities = cities;
    }
}