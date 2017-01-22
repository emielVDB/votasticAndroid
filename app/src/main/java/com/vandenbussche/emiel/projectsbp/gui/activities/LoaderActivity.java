package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vandenbussche.emiel.projectsbp.DoneListener;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.VotasticApplication;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.FollowsCache;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.AUTHORITY;

public class LoaderActivity extends AppCompatActivity {
    boolean followsLoaded = false;
    boolean appSynced = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AuthHelper.isUserLoggedIn(this)) {
            VotasticApplication.initConnection(getApplicationContext());

            ContentResolver.setSyncAutomatically(AuthHelper.getAccount(this), AUTHORITY, true);
            FollowsCache.loadAllFollows(this, new DoneListener() {
                @Override
                public void done() {
                    followsLoaded = true;
                    if(followsLoaded) toHomeActivity(); //todo: &&appSynced
                }
            });

        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void toHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
