package com.miw.dit.presentation.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miw.dit.R;
import com.miw.dit.model.Attendee;
import com.miw.dit.model.Event;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dani on 10/2/15.
 */
public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private EventsAdapterListener callback;

    private List<Event> events;
    private boolean full;
    private int lastPosition;
    private Context context;
    private String userId;

    public EventsAdapter(Context context, EventsAdapterListener callback, List<Event> events, String userId) {
        this.context = context;
        this.events = events;
        lastPosition = -1;
        full = false;
        this.userId = userId;
        this.callback = callback;
    }

    public void setFull() {
        this.full = true;
        notifyItemRemoved(events.size());
    }

    public void addEvents(List<Event> events) {
        int position = this.events.size();
        this.events.addAll(events);
        notifyItemRangeInserted(position, events.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(this.getClass().getSimpleName(), "onCreateViewHolder viewType { " + viewType + " }");
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_event, parent, false);
            return new ViewEventHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress_bar_indeterminate, parent, false);
            ViewFooterHolder viewFooterHolder = new ViewFooterHolder(view);
            viewFooterHolder.setIsRecyclable(false);
            return viewFooterHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(this.getClass().getSimpleName(), "onCreateViewHolder position { " + position + " }");

        if (holder instanceof ViewEventHolder) {
            ViewEventHolder viewEventHolder = (ViewEventHolder) holder;
            Event event = events.get(position);
            viewEventHolder.setEvent(event, userId);
            setAnimation(viewEventHolder.getCardView(), position);
        } else if (holder instanceof ViewFooterHolder) {
            Event event = events.get(position - 1);
            callback.onBindLoadingEvent(event);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == events.size())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int size = events.size();
        if (full)
            return size;
        return size + 1;

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void removeEvent(Event event) {
        int index = events.indexOf(event);
        notifyItemRemoved(index);
        events.remove(index);
    }

    public interface EventsAdapterListener {
        public void onBindLoadingEvent(Event lastEvent);

        public void onButtonDetailsClick(Event event);

        public void onButtonDeleteClick(Event event, Button button);

        public void onButtonAttendClick(Event event, Button button);

        public void onButtonDropOutClick(Event event, Button button);
    }

    public static class ViewFooterHolder extends RecyclerView.ViewHolder {

        public ViewFooterHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewEventHolder extends RecyclerView.ViewHolder {

        private Context context;

        private Event event;

        private CardView cardView;
        private TextView textTitle;
        private TextView textAddress;
        private TextView textTime;
        private ImageView imageProfile;
        private ImageView imageEvent;
        private Button buttonDetails;
        private Button buttonPrimary;

        public ViewEventHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cardView = (CardView) itemView.findViewById(R.id.card_view_event);
            textTitle = (TextView) itemView.findViewById(R.id.text_event_title);
            textAddress = (TextView) itemView.findViewById(R.id.text_event_address);
            textTime = (TextView) itemView.findViewById(R.id.text_event_time);
            imageProfile = (ImageView) itemView.findViewById(R.id.image_event_profile);
            imageEvent = (ImageView) itemView.findViewById(R.id.image_event);
            buttonDetails = (Button) itemView.findViewById(R.id.button_event_details);
            buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onButtonDetailsClick(event);
                }
            });
            buttonPrimary = (Button) itemView.findViewById(R.id.button_event_primary);
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonAttendClickListener();
                }
            });
        }

        private void onButtonDeleteClickListener() {
            callback.onButtonDeleteClick(event, buttonPrimary);
        }

        private void onButtonAttendClickListener() {
            callback.onButtonAttendClick(event, buttonPrimary);
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonDropOutClickListener();
                }
            });
        }

        private void onButtonDropOutClickListener() {
            callback.onButtonDropOutClick(event, buttonPrimary);
            buttonPrimary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonAttendClickListener();
                }
            });
        }

        public void setEvent(Event event, String userId) {
            this.event = event;
            textTitle.setText(event.getTitle());
            textAddress.setText(event.getAddress());
            textTime.setText(parseTime(event.getTime()));
            Picasso.with(context).load(event.getHeaderImage()).into(imageEvent);
            Picasso.with(context).load(event.getProfileImage()).into(imageProfile);
            if (event.getUserId().equals(userId)) {
                buttonPrimary.setText("delete");
                buttonPrimary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonDeleteClickListener();
                    }
                });
            } else {
                for (Attendee attendee : event.getAttendees()) {
                    if (attendee.getUserId().equals(userId)) {
                        buttonPrimary.setText("drop out");
                        buttonPrimary.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onButtonDropOutClickListener();
                            }
                        });
                        break;
                    }
                }
            }
        }


        public CardView getCardView() {
            return cardView;
        }

        private String parseTime(long time) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);
            String dateStr = dateFormat.format(calendar.getTime());
            java.text.DateFormat timeFormat = DateFormat.getTimeFormat(context);
            String timeStr = timeFormat.format(calendar.getTime());
            return dateStr + " " + timeStr;
        }
    }

}
