package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.accounts.Account;
import android.animation.LayoutTransition;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentResolver;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPollBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ContentNewPollBinding;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewPollActivityViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewsFragmentViewModel;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.AUTHORITY;

public class NewPollActivity extends AppCompatActivity {
    ActivityNewPollBinding binding;
    NewPollActivityViewModel newPollActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_poll);
        newPollActivityViewModel = new NewPollActivityViewModel(binding, this);
        binding.setPoll(new PollRequest());

        LinearLayout parentLayout = binding.content.parentLayout;
        LayoutTransition layoutTransition = parentLayout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
