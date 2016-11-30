package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
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
public class PollsAdaptarWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    protected List<Poll> pollList = null;

    private Context context;
    private int headerLayout;
    PollsAdapterWithHeaderListener listener = null;

    public PollsAdaptarWithHeader(List<Poll> pollList, Context context, PollsAdapterWithHeaderListener listener, int headerLayout) {
        this.pollList = pollList;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
        this.headerLayout = headerLayout;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            PollViewModel pollViewModel = new PollViewModel(parent);
            PollsAdaptarWithHeader.Viewholder vh = new PollsAdaptarWithHeader.Viewholder(pollViewModel);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            return new VHHeader(LayoutInflater.from(parent.getContext()).inflate(headerLayout, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PollsAdaptarWithHeader.Viewholder) {
            Viewholder Iviewholdder = (Viewholder) holder;
            Poll poll = getItem(position);
            Iviewholdder.viewModel.setPoll(poll);
        } else if (holder instanceof VHHeader) {
            if(listener != null) {
                listener.onBindHeaderView(((VHHeader) holder).headerView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return pollList.size() + 1;
    }

    private Poll getItem(int index) {
        return pollList.get(index - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if ((position) == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        final PollViewModel viewModel;

        public Viewholder(PollViewModel viewModel) {
            super(viewModel.getRoot());
            this.viewModel = viewModel;
            //opgelet: niet vergeten!

        }

    }

    class VHHeader extends RecyclerView.ViewHolder {
        View headerView;

        public VHHeader(View itemView) {
            super(itemView);
            this.headerView = itemView;
        }
    }

    public void setListener(PollsAdapterWithHeaderListener listener){
        this.listener = listener;
    }

    public interface PollsAdapterWithHeaderListener {
        public void onBindHeaderView(View v);
    }

}
