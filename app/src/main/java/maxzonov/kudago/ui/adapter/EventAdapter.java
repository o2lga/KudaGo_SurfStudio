package maxzonov.kudago.ui.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import maxzonov.kudago.R;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.date.Date;
import maxzonov.kudago.model.main.place.Place;
import maxzonov.kudago.utils.Utility;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;
    private static final int ID_LOCATION = 0;
    private static final int ID_DATE = 1;
    private static final int ID_PRICE = 2;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_item_view, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        Place place = event.getPlace();
        ArrayList<Date> dates = event.getDates();

        checkInfoModelAndShow(holder, dates, place);

        // Get first image for item
        String imageUrl = "";
        try {
            imageUrl = event.getImages().get(0).getImageUrl();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (holder.ivPhoto != null) {
            Picasso.get().load(imageUrl).into(holder.ivPhoto);
        }

        holder.tvTitle.setText(event.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvDetails.setText(Html.fromHtml(event.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvDetails.setText(Html.fromHtml(event.getDescription()));
        }

        showInfoLayout(holder, ID_PRICE, event.getPrice());

    }

    @Override
    public int getItemCount() {
        if (events == null) {
            return 0;
        }
        return events.size();
    }

    private void checkInfoModelAndShow(EventViewHolder holder, ArrayList<Date> dates, Place place) {
        if (place != null)
            showInfoLayout(holder, ID_LOCATION, place.getId());
        else
            holder.layoutLocation.setVisibility(View.GONE);

        int datesSize = dates.size();

        String firstDate;
        String lastDate;
        if (datesSize > 1) {
            firstDate = dates.get(0).getDateStart();
            lastDate = dates.get(datesSize - 1).getDateEnd();
            String formatted = Utility.convertDatesPeriod(Long.parseLong(firstDate), Long.parseLong(lastDate));
            showInfoLayout(holder, ID_DATE, formatted);
        } else if (datesSize == 1) {
            String formatted = Utility.convertDatesPeriod(Long.parseLong(dates.get(0).getDateStart()), Long.parseLong(dates.get(0).getDateEnd()));
            showInfoLayout(holder, ID_DATE, formatted);
        }
    }

    private void showInfoLayout(EventViewHolder holder, int infoId, String infoContent) {
        switch (infoId) {
            case ID_LOCATION:
                if (!infoContent.equals(""))
                    holder.tvInfoLocation.setText(infoContent);
                else
                    holder.layoutLocation.setVisibility(View.GONE);
                break;
            case ID_DATE:
                if (!infoContent.equals(""))
                    holder.tvInfoDate.setText(infoContent);
                else
                    holder.layoutDate.setVisibility(View.GONE);
                break;
            case ID_PRICE:
                if (!infoContent.equals(""))
                    holder.tvInfoPrice.setText(infoContent);
                else
                    holder.layoutPrice.setVisibility(View.GONE);
                break;
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_item_tv_title) TextView tvTitle;
        @BindView(R.id.main_item_tv_details) TextView tvDetails;
        @BindView(R.id.main_item_iv) ImageView ivPhoto;
        @BindView(R.id.main_item_tv_info_location) TextView tvInfoLocation;
        @BindView(R.id.main_item_tv_info_date) TextView tvInfoDate;
        @BindView(R.id.main_item_tv_info_price) TextView tvInfoPrice;

        @BindView(R.id.main_item_layout_location) LinearLayout layoutLocation;
        @BindView(R.id.main_item_layout_date) LinearLayout layoutDate;
        @BindView(R.id.main_item_layout_price) LinearLayout layoutPrice;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}