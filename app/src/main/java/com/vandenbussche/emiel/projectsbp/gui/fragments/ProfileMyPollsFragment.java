package com.vandenbussche.emiel.projectsbp.gui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.ProfileMyPollsFragmentViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileMyPollsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileMyPollsFragment extends Fragment {
    FragmentProfileMyPollsBinding binding;
    ProfileMyPollsFragmentViewModel newsFragmentViewModel;
    private OnFragmentInteractionListener mListener;

    public ProfileMyPollsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_my_polls, container, false);
        binding.pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.pollsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new ProfileMyPollsFragmentViewModel(binding, getContext());
        newsFragmentViewModel.loadPolls();

        return binding.getRoot();
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
