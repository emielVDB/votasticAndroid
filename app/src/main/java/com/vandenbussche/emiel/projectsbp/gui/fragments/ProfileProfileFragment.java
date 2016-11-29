package com.vandenbussche.emiel.projectsbp.gui.fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vandenbussche.emiel.projectsbp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileProfileFragment extends Fragment {


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
    }
}
