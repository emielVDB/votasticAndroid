package com.vandenbussche.emiel.projectsbp.viewmodel;

import android.databinding.BaseObservable;

import com.vandenbussche.emiel.projectsbp.BR;
import com.vandenbussche.emiel.projectsbp.databinding.FragmentNewsBinding;
import com.vandenbussche.emiel.projectsbp.models.Option;
import com.vandenbussche.emiel.projectsbp.models.Poll;
import com.vandenbussche.emiel.projectsbp.models.PollList;
import com.vandenbussche.emiel.projectsbp.models.Reaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by emielPC on 10/11/16.
 */
public class NewsFragmentViewModel extends BaseObservable {


    FragmentNewsBinding binding;
    public NewsFragmentViewModel(FragmentNewsBinding binding){
        this.binding = binding;
    }

    public void loadPolls() {
//        PollList pollList = new PollList();
//        List<Poll> myPolls = new ArrayList<Poll>();
//        Poll myPoll = new Poll();
//        myPoll.set_id(1+"");
//        myPoll.setChoiceIndex(-1);
//        myPoll.setQuestion("hoe gaat het met uw kinderen?");
//
//        List<Option> options = new ArrayList<>();
//        options.add(new Option(12,"heel goed"));
//        options.add(new Option(24, "goed"));
//        options.add(new Option(6, "minder goed"));
//        myPoll.setOptions(options);
//        myPoll.setReactions(new ArrayList<Reaction>());
//
//        List<String> tags = new ArrayList<>();
//        tags.add("kinderen");
//        tags.add("familie");
//        tags.add("ouderschap");
//        myPoll.setTags(tags);
//
//        myPoll.setTotalVotes(42);
//
//        myPolls.add(myPoll);
//        pollList.setData(myPolls);
//
//        binding.setPollList(pollList);
//
//        notifyPropertyChanged(BR.pollList);
    }
}
