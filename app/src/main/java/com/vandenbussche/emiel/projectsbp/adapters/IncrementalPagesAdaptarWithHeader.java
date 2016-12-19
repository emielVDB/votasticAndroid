package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vandenbussche.emiel.projectsbp.models.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emielPC on 30/11/16.
 */

public class IncrementalPagesAdaptarWithHeader extends PagesAdaptarWithHeader {
    NewPagesNeededListener newPagesNeededListener;
    private int maxPos = 0;
    public IncrementalPagesAdaptarWithHeader(List<Page> pageList, Context context, PagesAdapterWithHeaderListener listener, NewPagesNeededListener newPagesNeededListener, int headerLayout) {
        super(pageList, context, listener, headerLayout);
        this.newPagesNeededListener = newPagesNeededListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(newPagesNeededListener != null) {
            if (position >= this.pageList.size() - 1 && maxPos < position) {
                maxPos = position;
                newPagesNeededListener.getNewPages(pageList.get(pageList.size() - 1));
            }
        }
    }

    public void addPages(List<Page> pages){
        int insertedIndex = this.pageList.size();
        this.pageList.addAll(pages);
        notifyItemRangeInserted(insertedIndex , pages.size());
    }

    public void clearItems() {
        maxPos = 0;
        pageList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface NewPagesNeededListener {
        public void getNewPages(Page lastPage);
    }


}
