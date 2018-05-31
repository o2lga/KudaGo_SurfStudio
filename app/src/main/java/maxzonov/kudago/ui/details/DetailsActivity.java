package maxzonov.kudago.ui.details;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import maxzonov.kudago.R;
import maxzonov.kudago.ui.adapter.ViewPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

public class DetailsActivity extends MvpAppCompatActivity implements DetailsView, OnMapReadyCallback {

    @InjectPresenter DetailsPresenter detailsPresenter;

    private static final String INTENT_IMAGES_URLS_ARRAY_ID = "images";
    private static final String INTENT_PLACE_ID = "place";
    private static final String INTENT_COORDINATES_ID = "coords";
    private static final String INTENT_DESCRIPTION_ID = "descr";
    private static final String INTENT_FULL_DESCRIPTION_ID = "full_descr";
    private static final String INTENT_TITLE_ID = "title";
    private static final String INTENT_DATE_ID = "date";
    private static final String INTENT_PRICE_ID = "price";

    @BindView(R.id.details_title) TextView tvTitle;
    @BindView(R.id.details_subtitle) TextView tvSubTitle;
    @BindView(R.id.details_full_descr) TextView tvFullDescr;
    @BindView(R.id.details_tv_info_price) TextView tvPrice;
    @BindView(R.id.details_tv_info_location) TextView tvLocation;
    @BindView(R.id.details_tv_info_date) TextView tvDate;

    @BindView(R.id.details_layout_location) LinearLayout layoutLocation;
    @BindView(R.id.details_layout_date) LinearLayout layoutDate;
    @BindView(R.id.details_layout_price) LinearLayout layoutPrice;

    @BindView(R.id.details_map_view) MapView mapView;

    private Double latitude, longitude;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        unbinder = ButterKnife.bind(this);

        ViewPager viewPager = findViewById(R.id.details_viewpager);

        ArrayList<String> strings = new ArrayList<>(getIntent().getStringArrayListExtra(INTENT_IMAGES_URLS_ARRAY_ID));

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, strings);
        CircleIndicator indicator = findViewById(R.id.circle_indicator);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        if (!getIntent().getStringExtra(INTENT_PLACE_ID).equals("")) {
            latitude = Double.parseDouble(getIntent().getStringArrayListExtra(INTENT_COORDINATES_ID).get(0));
            longitude = Double.parseDouble(getIntent().getStringArrayListExtra(INTENT_COORDINATES_ID).get(1));
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        } else {
            mapView.setVisibility(View.GONE);
        }

        tvTitle.setText(getIntent().getStringExtra(INTENT_TITLE_ID));
        setTextWithHtml(tvSubTitle, getIntent().getStringExtra(INTENT_DESCRIPTION_ID));
        setTextWithHtml(tvFullDescr, getIntent().getStringExtra(INTENT_FULL_DESCRIPTION_ID));
        emptyInfoHandling(getIntent().getStringExtra(INTENT_PLACE_ID), layoutLocation, tvLocation);
        emptyInfoHandling(getIntent().getStringExtra(INTENT_DATE_ID), layoutDate, tvDate);
        emptyInfoHandling(getIntent().getStringExtra(INTENT_PRICE_ID), layoutPrice, tvPrice);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                .position(new LatLng(latitude, longitude)));

        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(16)
                        .build()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.details_toolbar_btn_back)
    void onBackClick() {
        onBackPressed();
    }

    private void emptyInfoHandling(String info, LinearLayout layout, TextView textView) {
        if (info == null || info.equals(""))
            layout.setVisibility(View.GONE);
        else
            layout.setVisibility(View.VISIBLE);
            textView.setText(info);
    }

    private void setTextWithHtml(TextView textView, String content) {

        if (content != null && content.length() > 0 ){
            if (content.endsWith("\n")) {
                content = content.substring(0, content.length() - 1);
            }
            content = content.replaceAll("\\<.*?\\>", "");
        } else {
            textView.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            textView.setText(Html.fromHtml(content).toString());
        }
    }
}
