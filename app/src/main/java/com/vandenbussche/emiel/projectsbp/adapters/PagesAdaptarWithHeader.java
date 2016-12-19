package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.viewmodel.PageViewModel;

import java.util.List;

/**
 * Created by Stijn on 2/10/2016.
 */
public class PagesAdaptarWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public List<Page> pageList = null;

    private Context context;
    private int headerLayout;
    private PagesAdapterWithHeaderListener listener = null;

    public PagesAdaptarWithHeader(List<Page> pageList, Context context, PagesAdapterWithHeaderListener listener, int headerLayout) {
        this.pageList = pageList;
        this.context = context;     //extra erbij gekomen om activity te kunnen starten vanuit viewholder
        this.headerLayout = headerLayout;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            PageViewModel pageViewModel = new PageViewModel(parent);
            PagesAdaptarWithHeader.Viewholder vh = new PagesAdaptarWithHeader.Viewholder(pageViewModel);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            return new VHHeader(LayoutInflater.from(parent.getContext()).inflate(headerLayout, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PagesAdaptarWithHeader.Viewholder) {
            Viewholder Iviewholdder = (Viewholder) holder;
            Page page = getItem(position);
            Iviewholdder.viewModel.setPage(page);
        } else if (holder instanceof VHHeader) {
            if(listener != null) {
                listener.onBindHeaderView(((VHHeader) holder).headerView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return pageList.size() + 1;
    }

    private Page getItem(int index) {
        return pageList.get(index - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if ((position) == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        final PageViewModel viewModel;

        public Viewholder(PageViewModel viewModel) {
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

    public void setListener(PagesAdapterWithHeaderListener listener){
        this.listener = listener;
    }

    public interface PagesAdapterWithHeaderListener {
        public void onBindHeaderView(View v);
    }

}
