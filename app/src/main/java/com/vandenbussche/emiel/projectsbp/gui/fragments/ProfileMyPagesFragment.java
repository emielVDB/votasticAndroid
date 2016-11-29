package com.vandenbussche.emiel.projectsbp.gui.fragments;



import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPagesBinding;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPagesFragmentViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPollsFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileMyPagesFragment extends Fragment {
    FragmentProfileMyPagesBinding binding;
    ProfileMyPagesFragmentViewModel newsFragmentViewModel;

    public ProfileMyPagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_my_pages, container, false);
        binding.pagesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.pagesRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new ProfileMyPagesFragmentViewModel(binding, getContext(), getFragmentManager());
        newsFragmentViewModel.loadPages();

        return binding.getRoot();
    }

}
