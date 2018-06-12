package maxzonov.kudago.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
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
import com.bumptech.glide.Glide;

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
import maxzonov.kudago.utils.OnRetryLoadingClickListener;
import maxzonov.kudago.utils.SharedPreferenceManager;
import maxzonov.kudago.utils.Utility;

public class MainActivity extends MvpAppCompatActivity implements MainView, OnRetryLoadingClickListener {

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

    private static final int CITY_REQUEST = 101;

    private CompositeDisposable compositeDisposable;
    private OnEventClickListener eventClickListener;
    private ArrayList<Event> events = new ArrayList<>();

    private String currentCitySlug = "msk";
    private String currentCityName = "Москва";
    private ArrayList<City> cities = new ArrayList<>();

    @BindView(R.id.coordinator_layout_main) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_nested_scroll) NestedScrollView nestedScrollView;
    @BindView(R.id.main_rv) RecyclerView recyclerView;
    @BindView(R.id.main_pb) ProgressBar progressBar;
    @BindView(R.id.main_swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.main_toolbar) Toolbar toolbar;

    @BindView(R.id.main_layout_no_internet) LinearLayout layoutNoInternet;
    @BindView(R.id.main_layout_content) LinearLayout layoutContent;

    @BindView(R.id.main_toolbar_tv_city) TextView tvToolbarCity;

    private EventAdapter eventAdapter;

    private SharedPreferenceManager prefsManager;

    private int pageCounter = 1;
    private boolean isLoading = false;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        unbinder = ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        prefsManager = new SharedPreferenceManager(this);
        currentCitySlug = prefsManager.readFromPrefs("current_city_slug");
        currentCityName = prefsManager.readFromPrefs("current_city_name");
        tvToolbarCity.setText(currentCityName);

        initRecyclerView();

        initScrollViewListener();

        if (Utility.isNetworkAvailable(this)) {
            showLoadingProgress(true);
            layoutNoInternet.setVisibility(View.GONE);
            mainPresenter.getData(compositeDisposable, currentCitySlug);
            pageCounter++;
        } else {
            swipeRefresh.setRefreshing(false);
            handleInternetError();
        }

        initSwipeToRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CITY_REQUEST) {
            events.clear();
            showLoadingProgress(true);

            int cityId = data.getIntExtra(INTENT_CITY_ID, 0);
            currentCityName = cities.get(cityId).getName();
            String citySlug = cities.get(cityId).getSlug();
            currentCitySlug = citySlug;
            prefsManager.writeToPrefs("current_city_slug", citySlug);
            prefsManager.writeToPrefs("current_city_name", currentCityName);

            tvToolbarCity.setText(currentCityName);

            if (Utility.isNetworkAvailable(this)) {
                mainPresenter.getData(compositeDisposable, citySlug);
            } else {
                handleInternetError();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_city_slug", currentCitySlug);
        outState.putString("current_city_name", currentCityName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        compositeDisposable.dispose();
        Glide.get(this).clearMemory();
    }

    @OnClick(R.id.main_toolbar_city_layout)
    void onToolbarClicked() {
        ArrayList<String> stringsCity = new ArrayList<>(fillCitiesArray());

        if (Utility.isNetworkAvailable(this)) {
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
            intent.putStringArrayListExtra(INTENT_CITIES_ARRAY_ID, stringsCity);
            startActivityForResult(intent, CITY_REQUEST);
        }
    }

    @Override
    public void showData(ResponseData responseData) {
        this.events = responseData.getEvents();
        eventAdapter.clear();
        eventAdapter.addData(responseData.getEvents());
        nestedScrollView.scrollTo(0, 0);
        eventAdapter.addLoadingItem();
    }

    @Override
    public void showLoadingProgress(boolean toShow) {
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

    @Override
    public void showAdditionalData(ResponseData responseData) {
        events.addAll(responseData.getEvents());
        eventAdapter.removeLoadingItem();
        isLoading = false;
        eventAdapter.addData(responseData.getEvents());
        eventAdapter.addLoadingItem();
    }

    @Override
    public void handleInternetError() {
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
    public void showPaginationError() {
        eventAdapter.showRetry(true);
    }

    private ArrayList<String> fillImagesArray(Event event) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < event.getImages().size(); i++) {
            imageUrls.add(event.getImages().get(i).getImageUrl());
        }
        return imageUrls;
    }

    @Override
    public void retryPageLoad() {
        if (Utility.isNetworkAvailable(this)) {
            mainPresenter.loadNextPage(compositeDisposable, String.valueOf(pageCounter++), currentCitySlug);
        } else {
            showPaginationError();
        }
    }

    private void initRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initEventClickListener();
        eventAdapter = new EventAdapter(this, eventClickListener);
        recyclerView.setAdapter(eventAdapter);
    }

    private void initEventClickListener() {
        eventClickListener = ((view, position, date) -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

            Event event = events.get(position);

            ArrayList<String> imageUrls = new ArrayList<>(fillImagesArray(event));

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
            intent.putExtra(INTENT_DATE_ID, date);
            intent.putStringArrayListExtra(INTENT_IMAGES_URLS_ARRAY_ID, imageUrls);
            startActivity(intent);
        });
    }

    private void initScrollViewListener() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                    if(v.getChildAt(v.getChildCount() - 1) != null) {
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            if (!isLoading) {

                                if (Utility.isNetworkAvailable(this)) {
                                    isLoading = true;
                                    mainPresenter.loadNextPage(compositeDisposable, String.valueOf(pageCounter++), currentCitySlug);
                                } else {
                                    showPaginationError();
                                }
                            }
                        }
                    }
                });
    }

    private void initSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            pageCounter = 1;
            if (Utility.isNetworkAvailable(this)) {
                layoutNoInternet.setVisibility(View.GONE);
                mainPresenter.getData(compositeDisposable, currentCitySlug);
                pageCounter++;
            } else {
                swipeRefresh.setRefreshing(false);
                handleInternetError();
            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    private ArrayList<String> fillCitiesArray() {
        ArrayList<String> stringsCity = new ArrayList<>();
        int citySize = cities.size();
        for (int i = 0; i < citySize; i++) {
            City itemCity = cities.get(i);
            stringsCity.add(itemCity.getName());
        }

        return stringsCity;
    }
}