package maxzonov.kudago.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import maxzonov.kudago.R;
import maxzonov.kudago.utils.OnCityClickListener;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<String> cities;
    private OnCityClickListener cityClickListener;
    private Activity activity;

    public CityAdapter(Activity activity, List<String> items, OnCityClickListener cityClickListener) {
        this.activity = activity;
        this.cities = items;
        this.cityClickListener = cityClickListener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cities_item_view, parent, false);
        return new CityViewHolder(itemView, cityClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String city = cities.get(position);
        holder.tvCity.setText(city);
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0;
        }
        return cities.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_city) TextView tvCity;
        @BindView(R.id.iv_city_checked) ImageView imageView;
        private OnCityClickListener cityClickListener;

        CityViewHolder(View itemView, OnCityClickListener cityClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.cityClickListener = cityClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            imageView.setVisibility(View.VISIBLE);
            cityClickListener.onCityClick(view, getAdapterPosition());
            activity.finish();
        }
    }
}