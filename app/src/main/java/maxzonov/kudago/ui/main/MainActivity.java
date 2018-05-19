package maxzonov.kudago.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import maxzonov.kudago.R;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.ui.adapter.EventAdapter;
import maxzonov.kudago.ui.details.DetailsActivity;
import maxzonov.kudago.utils.OnEventClickListener;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter MainPresenter mainPresenter;
    private CompositeDisposable compositeDisposable;
    private OnEventClickListener eventClickListener;

    @BindView(R.id.main_rv) RecyclerView recyclerView;
    @BindView(R.id.main_frame) FrameLayout frameLayout;
    @BindView(R.id.main_pb) ProgressBar progressBar;
    @BindView(R.id.main_tv_title) TextView tvTitle;
    @BindView(R.id.main_swipe_refresh) SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.main_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        showProgress(true);
        swipeRefresh.setOnRefreshListener(() -> mainPresenter.getData(compositeDisposable));

        compositeDisposable = new CompositeDisposable();

        mainPresenter.getData(compositeDisposable);

        eventClickListener = ((view, position) -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });
    }

    @Override
    public void showData(ArrayList<Event> events) {
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
            recyclerView.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finishSwipeRefresh() {
        swipeRefresh.setRefreshing(false);
    }
}