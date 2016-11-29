package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.PagesAdaptarWithHeader;
import com.vandenbussche.emiel.projectsbp.database.PagesAccess;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentProfileMyPagesBinding;
import com.vandenbussche.emiel.projectsbp.gui.fragments.ProfileMyPagesFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.ProfileMyPollsFragment;
import com.vandenbussche.emiel.projectsbp.gui.fragments.ProfileProfileFragment;
import com.vandenbussche.emiel.projectsbp.models.Page;
import com.vandenbussche.emiel.projectsbp.models.PageList;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by emielPC on 10/11/16.
 */
public class ProfileMyPagesFragmentViewModel extends BaseObservable implements PagesAdaptarWithHeader.PagesAdapterWithHeaderListener{
    private FragmentProfileMyPagesBinding binding;
    private Context context;
    private FragmentManager fragmentManager;

    public ProfileMyPagesFragmentViewModel(FragmentProfileMyPagesBinding binding, Context context, FragmentManager fragmentManager){
        this.binding = binding;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void loadPages() {
        //load empty list first
        PageList pageList = new PageList();
        binding.setPageList(pageList);
        notifyPropertyChanged(BR.pageList);

        PagesAccess.getAll(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Page>>() {
                    @Override
                    public void call(List<Page> pages) {
                        PageList pageList = new PageList();
                        pageList.addAll(pages);
                        binding.setAdapterListener(ProfileMyPagesFragmentViewModel.this);
                        binding.setPageList(pageList);
                        notifyPropertyChanged(BR.pageList);
                  }
                });
    }

    @Override
    public void onBindHeaderView(View v) {
        Button btnMyPages = (Button)v.findViewById(R.id.btnProfileMyPages);
        btnMyPages.getBackground().setColorFilter(context.getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.MULTIPLY);

        v.findViewById(R.id.btnProfileMyPolls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frmContent, new ProfileMyPollsFragment()).commit();
            }
        });
        v.findViewById(R.id.btnProfileProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frmContent, new ProfileProfileFragment()).commit();
            }
        });
    }
}
