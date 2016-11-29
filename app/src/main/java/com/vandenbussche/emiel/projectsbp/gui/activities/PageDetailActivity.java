package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.database.Contract;
import com.vandenbussche.emiel.projectsbp.database.PollsAccess;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ContentPageDetailBinding;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;
import com.vandenbussche.emiel.projectsbp.viewmodel.PageDetailActivityViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPollsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class PageDetailActivity extends AppCompatActivity {
    PageDetailActivityViewModel pageDetailActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPageDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_page_detail);
        binding.content.pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.content.pollsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        pageDetailActivityViewModel = new PageDetailActivityViewModel(binding, this);

        if(getIntent() != null){
            setTitle(getIntent().getStringExtra("title"));
            pageDetailActivityViewModel.loadPolls(getIntent().getStringExtra("_id"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
