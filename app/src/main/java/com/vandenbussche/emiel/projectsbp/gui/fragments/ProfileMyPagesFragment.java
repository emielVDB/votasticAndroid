package com.vandenbussche.emiel.projectsbp.gui.fragments;



import android.content.res.Configuration;
import android.database.ContentObserver;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPagesBinding;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPagesFragmentViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPollsFragmentViewModel;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGE_UPLOADED_URI;
import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.POLL_UPLOADED_URI;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileMyPagesFragment extends Fragment {
    FragmentProfileMyPagesBinding binding;
    ProfileMyPagesFragmentViewModel newsFragmentViewModel;
    private ContentObserver mObserver;

    public ProfileMyPagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_my_pages, container, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            binding.pagesRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        }else{
            binding.pagesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        }
        binding.pagesRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new ProfileMyPagesFragmentViewModel(binding, getContext(), getFragmentManager());

        return binding.getRoot();
    }

    @Override
    public void onPause(){
        super.onPause();
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onResume(){
        super.onResume();

        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                newsFragmentViewModel.loadPages();
            }
        };
        getContext().getContentResolver().registerContentObserver(PAGE_UPLOADED_URI, true, mObserver);


        newsFragmentViewModel.loadPages();
    }

}
