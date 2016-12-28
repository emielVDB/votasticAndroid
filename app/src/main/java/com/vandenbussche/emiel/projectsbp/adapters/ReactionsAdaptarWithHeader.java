package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.models.Reaction;
import com.vandenbussche.emiel.projectsbp.viewmodel.ReactionViewModel;

import java.util.List;

/**
 * Created by Stijn on 2/10/2016.
 */
public class ReactionsAdaptarWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    boolean headerFirstTime = true;

    public List<Reaction> reactionList = null;

    private Context context;
    private int headerLayout;
    private ReactionsAdapterWithHeaderListener listener = null;

    public ReactionsAdaptarWithHeader(List<Reaction> reactionList, Context context, ReactionsAdapterWithHeaderListener listener, int headerLayout) {
        this.reactionList = reactionList;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
        this.headerLayout = headerLayout;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            ReactionViewModel reactionViewModel = new ReactionViewModel(parent);
            ReactionsAdaptarWithHeader.Viewholder vh = new ReactionsAdaptarWithHeader.Viewholder(reactionViewModel);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            return new VHHeader(LayoutInflater.from(parent.getContext()).inflate(headerLayout, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReactionsAdaptarWithHeader.Viewholder) {
            Viewholder Iviewholdder = (Viewholder) holder;
            Reaction reaction = getItem(position);
            Iviewholdder.viewModel.setReaction(reaction);
        } else if (holder instanceof VHHeader) {
            if(listener != null) {

                listener.onBindHeaderView(((VHHeader) holder).headerView, headerFirstTime);
                headerFirstTime = false;
            }
        }
    }

    @Override
    public int getItemCount() {
        return reactionList.size() + 1;
    }

    private Reaction getItem(int index) {
        return reactionList.get(index - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if ((position) == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        final ReactionViewModel viewModel;

        public Viewholder(ReactionViewModel viewModel) {
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

    public void setListener(ReactionsAdapterWithHeaderListener listener){
        this.listener = listener;
    }

    public interface ReactionsAdapterWithHeaderListener {
        public void onBindHeaderView(View v, boolean isFirstTime);
    }

}
