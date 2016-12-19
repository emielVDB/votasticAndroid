package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNotificationsBinding;

/**
 * Created by emielPC on 10/11/16.
 */
public class NotificationsFragmentViewModel extends BaseObservable{
    Context context;
    FragmentNotificationsBinding binding;

    ObservableList<String> notificationList;

    public NotificationsFragmentViewModel(Context context, FragmentNotificationsBinding binding) {
        this.context = context;
        this.binding = binding;
        notificationList = new ObservableArrayList<>();
        binding.setNotificationItems(notificationList);
        notifyPropertyChanged(BR.notificationItems);
    }

    public void onPause(){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    public void onResume(){
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("votastic_notification"));
    }

    public void addItem(String message){
        notificationList.add(0, message);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            addItem(message);
        }
    };
}
