package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.databinding.RowPageBinding;
import com.vandenbussche.emiel.projectsbp.gui.activities.PageDetailActivity;
import com.vandenbussche.emiel.projectsbp.models.Page;

/**
 * Created by emielPC on 11/11/16.
 */
public class PageViewModel {
    private RowPageBinding binding;
    private Page page;

    public PageViewModel(ViewGroup parent){
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_page, parent, false);
    }

    public void setPage(final Page page){
        this.page = page;
        binding.setPage(this.page);
        binding.executePendingBindings();

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Page page = PageViewModel.this.page;
                Intent intent = new Intent(binding.getRoot().getContext(), PageDetailActivity.class);
                intent.putExtra("title", page.getTitle());
                intent.putExtra("_id", page.get_id());
                binding.getRoot().getContext().startActivity(intent);
            }
        });
    }

    public View getRoot(){
        return binding.getRoot();
    }
}
