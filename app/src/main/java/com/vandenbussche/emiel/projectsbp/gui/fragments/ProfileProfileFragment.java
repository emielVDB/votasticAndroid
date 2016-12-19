package com.vandenbussche.emiel.projectsbp.gui.fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalPagesAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.IncrementalPollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PagesAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.api.ApiHelper;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.responses.PollResponse;
import com.vandenbussche.emiel.projectsbp.models.responses.ProfileResponse;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewsFragmentViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileProfileFragment extends Fragment implements IncrementalPagesAdaptarWithHeader.NewPagesNeededListener, PagesAdaptarWithHeader.PagesAdapterWithHeaderListener {
    RecyclerView recyclerView;
    IncrementalPagesAdaptarWithHeader adapter = null;
    TextView lblGender,lblBirthday;

    public ProfileProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.recyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        adapter = new IncrementalPagesAdaptarWithHeader(new ArrayList<Page>(), view.getContext(),
                ProfileProfileFragment.this, ProfileProfileFragment.this, R.layout.header_profile_profile);
        recyclerView.setAdapter(adapter);

        loadProfile();
    }

    private void loadProfile(){
        ApiHelper.getApiService(getContext()).getMyProfile( )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ProfileResponse>() {
                    @Override
                    public void call(ProfileResponse profileResponse) {
                        Date date=new Date(profileResponse.getBirthDay());
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                        String dateText = df2.format(date);
                        lblBirthday.setText(dateText);
                        lblGender.setText(profileResponse.getGender() == 1? "Male" : "Female");
                    }
                });
    }



    @Override
    public void getNewPages(Page lastPage) {

    }

    @Override
    public void onBindHeaderView(View view) {
        Button btnProfile = (Button)view.findViewById(R.id.btnProfileProfile);
        btnProfile.getBackground().setColorFilter(getContext().getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.MULTIPLY);

        view.findViewById(R.id.btnProfileMyPages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frmContent, new ProfileMyPagesFragment()).commit();
            }
        });
        view.findViewById(R.id.btnProfileMyPolls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frmContent, new ProfileMyPollsFragment()).commit();
            }
        });

        lblBirthday = (TextView)view.findViewById(R.id.lblBirthday);
        lblGender = (TextView)view.findViewById(R.id.lblGender);
    }
}
