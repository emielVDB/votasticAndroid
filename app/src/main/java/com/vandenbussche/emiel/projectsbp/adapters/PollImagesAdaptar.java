package com.vandenbussche.emiel.projectsbp.adapters;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vandenbussche.emiel.projectsbp.R;

import java.io.File;

/**
 * Created by Stijn on 2/10/2016.
 */
public class PollImagesAdaptar extends RecyclerView.Adapter<PollImagesAdaptar.Viewholder> {


    private ObservableList<String> imagesList = null;
    private Context context;
    private boolean isNewPoll;

    public PollImagesAdaptar(ObservableList<String> imagesList, Context context, boolean isNewPoll) {
        this.imagesList = imagesList;
        this.context = context;//extra erbij gekomen om activity te kunnen starten vanuit viewholder
        this.isNewPoll = isNewPoll;

        imagesList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> strings) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<String> strings, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<String> strings, int i, int i1) {
                notifyItemRangeInserted(i, i1);
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> strings, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> strings, int i, int i1) {
                notifyItemRangeRemoved(i, i1);
            }
        });
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  null;
        if(isNewPoll){
            v = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_image_new_poll, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_image_poll, parent, false);
        }
        PollImagesAdaptar.Viewholder vh = new PollImagesAdaptar.Viewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        final String url= imagesList.get(position);

        if(isNewPoll)
            Picasso.with(context).load(new File(url)).into(holder.imageView);
        else
            Picasso.with(context).load(url).placeholder(R.drawable.loading_image).into(holder.imageView);

        if(isNewPoll) {
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagesList.remove(url);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button btnRemove;

        public Viewholder(View root) {
            super(root);


            imageView = (ImageView)root.findViewById(R.id.img);

            if(isNewPoll)
                btnRemove = (Button)root.findViewById(R.id.btnRemove);
        }

    }

}
