package maxzonov.kudago.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import maxzonov.kudago.R;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.ui.adapter.EventAdapter;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter MainPresenter mainPresenter;
    private CompositeDisposable compositeDisposable;

    @BindView(R.id.main_rv) RecyclerView recyclerView;
    @BindView(R.id.main_frame) FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        mainPresenter.getData(compositeDisposable);
    }

    @Override
    public void showData(ArrayList<Event> events) {
        LinearLayoutManager layoutManagerCategory =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerCategory);
        EventAdapter eventAdapter = new EventAdapter(events);
        recyclerView.setAdapter(eventAdapter);

    }
}