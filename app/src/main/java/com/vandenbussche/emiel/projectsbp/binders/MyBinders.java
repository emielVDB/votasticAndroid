package com.vandenbussche.emiel.projectsbp.binders;

import android.animation.LayoutTransition;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.adapters.PollsAdaptar;
import com.vandenbussche.emiel.projectsbp.databinding.RowNewPollTagBinding;
import com.vandenbussche.emiel.projectsbp.databinding.RowTagBinding;
import com.vandenbussche.emiel.projectsbp.models.Poll;

import java.util.List;

/**
 * Created by emielPC on 10/11/16.
 */
public class MyBinders {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<Poll> seriesList) {
        if (seriesList != null) {
            PollsAdaptar adapter = new PollsAdaptar(seriesList, recyclerView.getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("android:layout_weight")
    public static void setLayoutWeight(View view, float weight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        view.setLayoutParams(new LinearLayout.LayoutParams(
                layoutParams.width,
                layoutParams.height, weight));
    }

    @BindingAdapter("tags")
    public static void setTags(LinearLayout layout, List<String> tags) {
        for (String tag : tags) {
            RowTagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(layout.getContext()), R.layout.row_tag, layout, false);
            binding.setTag(tag);
            layout.addView(binding.getRoot());
        }
    }

    @BindingAdapter("newPollTags")
    public static void setNewPollTags(final LinearLayout layout, final List<String> tags) {
        if (tags == null) return;
        LayoutTransition layoutTransition = layout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        layoutTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);

        for (final String tag : tags) {
            final RowNewPollTagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(layout.getContext()), R.layout.row_new_poll_tag, layout, false);
            binding.setTag(tag);
            layout.addView(binding.getRoot());
            binding.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tags.remove(tag);
                    layout.removeView(binding.getRoot());
                }
            });
        }

        ((ObservableList) tags).addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList>() {
            @Override
            public void onChanged(ObservableList sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                final String tag = (String) sender.get(positionStart);
                final RowNewPollTagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(layout.getContext()), R.layout.row_new_poll_tag, layout, false);
                binding.setTag(tag);
                layout.addView(binding.getRoot(), positionStart);
                binding.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tags.remove(tag);
                        layout.removeView(binding.getRoot());
                    }
                });
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

            }
        });

    }

    @BindingAdapter("newPollOptions")
    public static void setNewPollOptions(final LinearLayout layout, final List<String> options) {
        if (options == null) return;
        LayoutTransition layoutTransition = layout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        for (final String option : options) {
            final RowNewPollTagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(layout.getContext()), R.layout.row_new_poll_tag, layout, false);
            binding.setTag(option);
            layout.addView(binding.getRoot());
            binding.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    options.remove(option);
                    layout.removeView(binding.getRoot());
                }
            });
        }

        ((ObservableList) options).addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList>() {
            @Override
            public void onChanged(ObservableList sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                final String tag = (String) sender.get(positionStart);
                final RowNewPollTagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(layout.getContext()), R.layout.row_new_poll_tag, layout, false);
                binding.setTag(tag);
                layout.addView(binding.getRoot(), positionStart);
                binding.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        options.remove(tag);
                        layout.removeView(binding.getRoot());
                    }
                });
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

            }
        });

    }
}
