package maxzonov.kudago.ui.main;

import android.content.Intent;
import android.os.Bundle;
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
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import maxzonov.kudago.R;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.place.Place;
import maxzonov.kudago.model.main.place.PlaceDetail;
import maxzonov.kudago.ui.adapter.EventAdapter;
import maxzonov.kudago.ui.details.DetailsActivity;
import maxzonov.kudago.utils.OnEventClickListener;
import maxzonov.kudago.utils.Utility;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter MainPresenter mainPresenter;
    private CompositeDisposable compositeDisposable;
    private OnEventClickListener eventClickListener;
    private ArrayList<Event> events = new ArrayList<>();

    @BindView(R.id.main_rv) RecyclerView recyclerView;
    @BindView(R.id.main_pb) ProgressBar progressBar;
    @BindView(R.id.main_tv_title) TextView tvTitle;
    @BindView(R.id.main_swipe_refresh) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.main_toolbar) Toolbar toolbar;

    @BindView(R.id.main_layout_no_internet) LinearLayout layoutNoInternet;
    @BindView(R.id.main_layout_content) LinearLayout layoutContent;

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

        if (Utility.isNetworkAvailable(this)) {
            mainPresenter.getData(compositeDisposable);
        } else {
            progressBar.setVisibility(View.GONE);
            layoutContent.setVisibility(View.GONE);
            layoutNoInternet.setVisibility(View.VISIBLE);
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
                intent.putExtra("place", event.getPlace().getTitle());
                intent.putStringArrayListExtra("coords", coords);
            } else {
                intent.putExtra("place", "");
            }

            intent.putExtra("title", event.getTitle());
            intent.putExtra("descr", event.getDescription());
            intent.putExtra("full_descr", event.getFullDescription());
            intent.putExtra("price", event.getPrice());
            intent.putStringArrayListExtra("images", imageUrls);
            startActivity(intent);
        });

        swipeRefresh.setOnRefreshListener(() -> mainPresenter.getData(compositeDisposable));
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showData(ArrayList<Event> events) {
        this.events = events;

        LinearLayoutManager layoutManagerCategory =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerCategory);
        EventAdapter eventAdapter = new EventAdapter(events, eventClickListener);
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
}