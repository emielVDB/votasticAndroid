package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.AUTHORITY;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(AuthHelper.isUserLoggedIn(this)) {
            //todo: start and wait to finish of syncing

            ContentResolver.setSyncAutomatically(AuthHelper.getAccount(this), AUTHORITY, true);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
