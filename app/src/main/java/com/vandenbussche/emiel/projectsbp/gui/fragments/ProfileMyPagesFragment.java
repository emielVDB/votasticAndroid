package com.vandenbussche.emiel.projectsbp.gui.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileMyPagesFragment extends Fragment {


    public ProfileMyPagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_my_pages, container, false);
    }

}
