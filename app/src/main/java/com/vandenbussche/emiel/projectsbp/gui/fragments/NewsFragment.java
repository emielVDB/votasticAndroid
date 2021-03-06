package com.vandenbussche.emiel.projectsbp.gui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNewsBinding;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewsFragmentViewModel;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.FOLLOWS_CHANGED_URI;
import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.PAGE_UPLOADED_URI;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewsFragment extends Fragment {
    FragmentNewsBinding binding;
    NewsFragmentViewModel newsFragmentViewModel;

    private OnFragmentInteractionListener mListener;
    private ContentObserver mObserver;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news, container, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            binding.pollsRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        }else{
            binding.pollsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        }
        binding.pollsRecyclerView.setItemAnimator(new android.support.v7.widget.DefaultItemAnimator());
        newsFragmentViewModel = new NewsFragmentViewModel(binding);

        return binding.getRoot();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
            public void onChange(boolean selfChange)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsFragmentViewModel.loadPolls();
                    }
                });

            }
        };
        getContext().getContentResolver().registerContentObserver(FOLLOWS_CHANGED_URI, true, mObserver);


        newsFragmentViewModel.loadPolls();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    /*    if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
