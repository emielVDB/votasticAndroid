package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.models.Poll;

import java.util.List;

/**
 * Created by Stijn on 2/10/2016.
 */
public class NotificationsAdaptar extends RecyclerView.Adapter<NotificationsAdaptar.Viewholder>{
    private ObservableList<String> notificationList = null;
    private int maxPos = 0;
    private NewNotificationsNeededListener newNotificationsNeededListener;

    private Context context;

    public NotificationsAdaptar(ObservableList<String> notificationList, Context context, NewNotificationsNeededListener newNotificationsNeededListener) {
        this.notificationList = notificationList;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
        this.newNotificationsNeededListener = newNotificationsNeededListener;
        notificationList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> strings) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<String> strings, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<String> strings, int i, int i1) {
                notifyItemRangeInserted(i, i1);
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> strings, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> strings, int i, int i1) {
                notifyItemRangeRemoved(i, i1);
            }
        });
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        NotificationsAdaptar.Viewholder vh = new NotificationsAdaptar.Viewholder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        String notification= notificationList.get(position);
        holder.lblMessage.setText(notification);

        if(newNotificationsNeededListener != null) {
            if (position >= this.notificationList.size() - 1 && maxPos < position) {
                maxPos = position;
                newNotificationsNeededListener.getNewNotiffications();
            }
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView lblMessage;

        public Viewholder(View view) {
            super(view);
            lblMessage = (TextView)view.findViewById(R.id.lblNotificationMessage);
        }

    }


    public interface NewNotificationsNeededListener {
        public void getNewNotiffications();
    }
}
