package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPollDetailBinding;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.viewmodel.PageDetailActivityViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.PollDetailActivityViewModel;

public class PollDetailActivity extends AppCompatActivity {
    PollDetailActivityViewModel pollDetailActivityViewModel;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPollDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_poll_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.content.reactionsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.content.reactionsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());

        if(getIntent() != null){
            setTitle("Poll -live-");
            Poll poll = (Poll)(getIntent().getSerializableExtra("poll"));
            pollDetailActivityViewModel = new PollDetailActivityViewModel(binding, this, poll, linearLayoutManager);
        }

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
