package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.adapters.NotificationsAdaptar;
import com.vandenbussche.emiel.projectsbp.database.FollowsAccess;
import com.vandenbussche.emiel.projectsbp.database.FollowsCache;
import com.vandenbussche.emiel.projectsbp.database.NotificationsAccess;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNotificationsBinding;
import com.vandenbussche.emiel.projectsbp.models.Follow;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by emielPC on 10/11/16.
 */
public class NotificationsFragmentViewModel extends BaseObservable implements NotificationsAdaptar.NewNotificationsNeededListener{
    Context context;
    FragmentNotificationsBinding binding;

    ObservableList<String> notificationList;

    long currentMaxId = 99999999;

    public NotificationsFragmentViewModel(Context context, FragmentNotificationsBinding binding) {
        this.context = context;
        this.binding = binding;
        notificationList = new ObservableArrayList<>();
        binding.setNotificationItems(notificationList);
        notifyPropertyChanged(BR.notificationItems);
        binding.setListener(this);
        notifyPropertyChanged(BR.listener);
    }

    public void onPause(){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    public void onResume(){
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("votastic_notification"));
        getNewNotiffications();
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

    @Override
    public void getNewNotiffications() {
        //use the currentMaxId
        NotificationsAccess.getFive(context, "_id", currentMaxId+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ContentValues>>() {
                    @Override
                    public void call(final List<ContentValues> notifications) {
                        List<String> inputNotiList = new ArrayList<String>();
                        for (ContentValues notificationItem : notifications) {
                            if(notificationItem.getAsLong("_id") < currentMaxId){
                                currentMaxId = notificationItem.getAsLong("_id");
                            }
                            inputNotiList.add( notificationItem.getAsString("message"));
                        }
                        notificationList.addAll(inputNotiList);

                    }
                });
    }
}
