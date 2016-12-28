package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.binders.models.PollBinderModel;
import com.vandenbussche.emiel.projectsbp.databinding.HeaderDetailPollBinding;
import com.vandenbussche.emiel.projectsbp.models.PollBindable;

/**
 * Created by emielPC on 28/12/16.
 */

public class PollDetailViewModel extends PollBindable{
    public PollDetailViewModel(View viewRoot) {
        binding = DataBindingUtil.bind(viewRoot);
        optionsLinearLayout = (LinearLayout) binding.getRoot().findViewById(R.id.optionsLinearLayout);
    }

    @Override
    protected void bindingSetPoll(PollBinderModel poll) {
        ((HeaderDetailPollBinding)binding).setPoll(poll);
    }
}
