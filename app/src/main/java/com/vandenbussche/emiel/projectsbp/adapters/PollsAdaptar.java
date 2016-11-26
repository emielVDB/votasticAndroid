package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.viewmodel.PollViewModel;

import java.util.List;

/**
 * Created by Stijn on 2/10/2016.
 */
public class PollsAdaptar extends RecyclerView.Adapter<PollsAdaptar.Viewholder> {


    private List<Poll> pollList = null;

    private Context context;

    public PollsAdaptar(List<Poll> pollList, Context context) {
        this.pollList = pollList;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        PollViewModel pollViewModel = new PollViewModel(parent);
        PollsAdaptar.Viewholder vh = new PollsAdaptar.Viewholder(pollViewModel);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Poll poll= pollList.get(position);
        holder.viewModel.setPoll(poll);


    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        final PollViewModel viewModel;

        public Viewholder(PollViewModel viewModel) {
            super(viewModel.getRoot());
            this.viewModel = viewModel;
            //opgelet: niet vergeten!

        }

    }

}
