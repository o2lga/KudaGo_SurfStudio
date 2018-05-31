package maxzonov.kudago.ui.city;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import maxzonov.kudago.R;
import maxzonov.kudago.ui.adapter.CityAdapter;
import maxzonov.kudago.utils.OnCityClickListener;

public class CityActivity extends AppCompatActivity {

    private static final int EXIT_RESULT = 100;
    private static final int CITY_RESULT = 101;

    private static final String INTENT_CITIES_ARRAY_ID = "cities";
    private static final String INTENT_CITY_ID = "city";

    @BindView(R.id.city_toolbar) Toolbar toolbar;
    @BindView(R.id.rv_city) RecyclerView rvCity;

    private Unbinder unbinder;

    private ArrayList<String> cities = new ArrayList<>();
    private CityAdapter cityAdapter;

    private OnCityClickListener cityClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        cities = getIntent().getStringArrayListExtra(INTENT_CITIES_ARRAY_ID);

        cityClickListener = ((view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(INTENT_CITY_ID, position);
            setResult(CITY_RESULT, intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCity.setLayoutManager(layoutManager);
        cityAdapter = new CityAdapter(this, cities, cityClickListener);
        rvCity.setAdapter(cityAdapter);
    }

    @OnClick(R.id.iv_city_toolbar_exit)
    void onExitClicked() {
        setResult(EXIT_RESULT);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
