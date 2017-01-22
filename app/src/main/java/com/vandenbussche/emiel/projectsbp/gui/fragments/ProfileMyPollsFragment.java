package com.vandenbussche.emiel.projectsbp.gui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPollsFragmentViewModel;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.POLL_UPLOADED_URI;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileMyPollsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileMyPollsFragment extends Fragment{
    FragmentProfileMyPollsBinding binding;
    ProfileMyPollsFragmentViewModel newsFragmentViewModel;
    private OnFragmentInteractionListener mListener;
    private ContentObserver mObserver;

    public ProfileMyPollsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_my_polls, container, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            binding.pollsRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        }else{
            binding.pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        }
        binding.pollsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new ProfileMyPollsFragmentViewModel(binding, getContext(), getFragmentManager());


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
                newsFragmentViewModel.loadPolls();
            }
        };
        getContext().getContentResolver().registerContentObserver(POLL_UPLOADED_URI, true, mObserver);


        newsFragmentViewModel.loadPolls();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
