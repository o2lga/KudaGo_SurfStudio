package maxzonov.kudago.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import maxzonov.kudago.R;
import maxzonov.kudago.model.main.Event;
import maxzonov.kudago.model.main.date.Date;
import maxzonov.kudago.model.main.place.Place;
import maxzonov.kudago.utils.GlideApp;
import maxzonov.kudago.utils.OnEventClickListener;
import maxzonov.kudago.utils.Utility;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Event> events;
    private OnEventClickListener eventClickListener;

    private static final int DEFAULT_LOADING = 0;
    private static final int PAGINATION_LOADING = 1;

    private static final int ID_DATE = 10;
    private static final int ID_PRICE = 11;

    private boolean isLoadingAdded = false;

    public EventAdapter(Context context, List<Event> events, OnEventClickListener clickListener) {
        this.context = context;
        this.events = events;
        this.eventClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case DEFAULT_LOADING:
                View itemView = inflater.inflate(R.layout.events_item_view, parent, false);
                viewHolder = new EventViewHolder(itemView, eventClickListener);
                break;

            case PAGINATION_LOADING:
                View viewPagination = inflater.inflate(R.layout.progress_item_view, parent, false);
                viewHolder = new PaginationViewHolder(viewPagination);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {

            case DEFAULT_LOADING:

                final EventViewHolder eventViewHolder = (EventViewHolder) holder;

                Event event = events.get(position);
                ArrayList<Date> dates = event.getDates();

                formatAndShowDate(eventViewHolder, dates);

                // Get first image for item
                String imageUrl = "";
                try {
                    imageUrl = event.getImages().get(0).getImageUrl();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (eventViewHolder.ivPhoto != null) {
//                    Picasso.get().load(imageUrl).fit().centerCrop().into(eventViewHolder.ivPhoto);
                    GlideApp.with(context).load(imageUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE).fitCenter().into(eventViewHolder.ivPhoto);
                }

                eventViewHolder.tvTitle.setText(event.getTitle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    eventViewHolder.tvDetails.setText(Html.fromHtml(event.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    eventViewHolder.tvDetails.setText(Html.fromHtml(event.getDescription()));
                }

                showInfoLayout(eventViewHolder, ID_PRICE, event.getPrice());
                showLocation(eventViewHolder, event.getPlace());
                break;

            case PAGINATION_LOADING:
                final PaginationViewHolder paginationViewHolder = (PaginationViewHolder) holder;

                paginationViewHolder.progressBar.setVisibility(View.VISIBLE);

                break;
        }

    }

    @Override
    public int getItemCount() {
        if (events == null) {
            return 0;
        }
        return events.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DEFAULT_LOADING;
        } else {
            return (position == events.size() - 1 && isLoadingAdded) ? PAGINATION_LOADING : DEFAULT_LOADING;
        }
    }

    private void formatAndShowDate(EventViewHolder holder, ArrayList<Date> dates) {

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

    private void showLocation(EventViewHolder holder, Place place) {
        if (place != null) {
            holder.tvInfoLocation.setText(place.getTitle());
        } else {
            holder.layoutLocation.setVisibility(View.GONE);
        }
    }

    public void addToAdapterArray(ArrayList<Event> resultEvents) {
        events.addAll(resultEvents);
        notifyItemInserted(events.size() - 1);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_item_tv_title) TextView tvTitle;
        @BindView(R.id.main_item_tv_details) TextView tvDetails;
        @BindView(R.id.main_item_iv) ImageView ivPhoto;
        @BindView(R.id.main_item_tv_info_location) TextView tvInfoLocation;
        @BindView(R.id.main_item_tv_info_date) TextView tvInfoDate;
        @BindView(R.id.main_item_tv_info_price) TextView tvInfoPrice;

        @BindView(R.id.main_item_layout_location) LinearLayout layoutLocation;
        @BindView(R.id.main_item_layout_date) LinearLayout layoutDate;
        @BindView(R.id.main_item_layout_price) LinearLayout layoutPrice;

        private OnEventClickListener clickListener;

        public EventViewHolder(View itemView, OnEventClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onEventClick(view, getAdapterPosition());
        }
    }

    public class PaginationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pagination_pb) ProgressBar progressBar;
        @BindView(R.id.pagination_layout) ConstraintLayout layout;

        public PaginationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}