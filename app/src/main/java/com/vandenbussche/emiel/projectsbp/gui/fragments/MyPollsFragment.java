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
import com.vandenbussche.emiel.projectsbp.databinding.FragmentMyPollsBinding;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNewsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.MyPollsFragmentViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewsFragmentViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPollsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyPollsFragment extends Fragment {
    FragmentMyPollsBinding binding;
    MyPollsFragmentViewModel newsFragmentViewModel;
    private OnFragmentInteractionListener mListener;

    public MyPollsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_polls, container, false);
        binding.pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.pollsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new MyPollsFragmentViewModel(binding, getContext());
        newsFragmentViewModel.loadPolls();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
