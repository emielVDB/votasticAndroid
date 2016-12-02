package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalPollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.requests.SearchRequest;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RandomPollsActivity extends AppCompatActivity implements IncrementalPollsAdaptarWithHeader.NewPollsNeededListener,
        PollsAdaptarWithHeader.PollsAdapterWithHeaderListener{

    IncrementalPollsAdaptarWithHeader adapter = null;
    RecyclerView recyclerView;
    SearchView searchView;

    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_polls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.pollsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());

        searchView = (SearchView)findViewById(R.id.search);
        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = searchView.getQuery().toString();
                searchPolls();
                addPollsToAdapter();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchText = null;
                adapter.clearItems();
                addPollsToAdapter();
                return false;
            }
        });

        if(getIntent() != null && getIntent().getStringExtra("query") != null){
            searchText = getIntent().getStringExtra("query");
            searchView.setQuery(searchText, true);

        }else {
            addPollsToAdapter();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);

        if(getIntent() != null && getIntent().getStringExtra("query") != null){
            searchText = getIntent().getStringExtra("query");
            searchView.setQuery(searchText, true);
        }
    }

    private void searchPolls() {
        if(searchText.equals("")) searchText = null;
        if(searchText == null) return;

        if(adapter != null)
        adapter.clearItems();


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


    public void addPollsToAdapter(){
        addPollsToAdapter(null);
    }
    public void addPollsToAdapter(Poll lastPoll){
        rx.Observable<List<PollResponse>> observable = null;
        if(searchText == null){
            observable = ApiHelper.getApiService(this).getRandomPolls();
        }else{
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setText(searchText);
            if(lastPoll != null) searchRequest.setMaxUploadTime(lastPoll.getUploadTime()-1);

            observable = ApiHelper.getApiService(this).getFindPolls(searchRequest.getText(), searchRequest.getMaxUploadTime());
        }

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PollResponse>>() {
                    @Override
                    public void call(List<PollResponse> pollResponses) {
                        if(pollResponses.size() == 0) return;
                        List<Poll> pollList = new ArrayList<Poll>();
                        for(PollResponse pollResponse : pollResponses){
                            pollList.add(pollResponse.toPoll());
                        }
                        if(adapter == null) {
                            adapter = new IncrementalPollsAdaptarWithHeader(pollList, RandomPollsActivity.this,
                                    RandomPollsActivity.this, RandomPollsActivity.this, R.layout.header_random_polls_activity);
                            recyclerView.setAdapter(adapter);
                        }else{
                            adapter.addPolls(pollList);
                        }
                    }
                });
    }


    @Override
    public void getNewPolls(Poll lastPoll) {
        addPollsToAdapter(lastPoll);
    }

    @Override
    public void onBindHeaderView(View v) {

    }
}
