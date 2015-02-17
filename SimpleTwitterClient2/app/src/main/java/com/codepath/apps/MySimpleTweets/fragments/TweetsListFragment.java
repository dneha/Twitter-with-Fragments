package com.codepath.apps.MySimpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.MySimpleTweets.EndlessScrollListener;
import com.codepath.apps.MySimpleTweets.R;
import com.codepath.apps.MySimpleTweets.TweetsArrayAdapter;
import com.codepath.apps.MySimpleTweets.models.Tweet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nehadike on 2/15/15.
 */
public abstract class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    protected ArrayList<Tweet> tweets;
    ListView lvTweets;
    protected long max_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_tweets_list,parent,false);
        lvTweets = (ListView)view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        max_id = 0;

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeline();
            }
        });
        
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    abstract void populateTimeline();

    public void refreshTimeline() {
        max_id = 0;
        tweets.clear();
        populateTimeline();
        aTweets.notifyDataSetChanged();
    }
    
    public void addAll(List<Tweet> tweetsList)
    {
        aTweets.addAll(tweetsList);
    }
}
