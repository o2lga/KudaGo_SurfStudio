package maxzonov.kudago.ui.details;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import maxzonov.kudago.R;

public class DetailsActivity extends MvpAppCompatActivity implements DetailsView {

    @BindView(R.id.details_title) TextView tvTitle;
    @BindView(R.id.details_subtitle) TextView tvSubTitle;
    @BindView(R.id.details_full_descr) TextView tvFullDescr;
    @BindView(R.id.details_tv_info_price) TextView tvPrice;
    @BindView(R.id.details_tv_info_location) TextView tvLocation;
    @BindView(R.id.details_tv_info_date) TextView tvDate;

    @BindView(R.id.details_layout_location) LinearLayout layoutLocation;
    @BindView(R.id.details_layout_date) LinearLayout layoutDate;
    @BindView(R.id.details_layout_price) LinearLayout layoutPrice;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    }
}
