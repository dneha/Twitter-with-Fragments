package com.codepath.apps.MySimpleTweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.MySimpleTweets.TwitterApplication;
import com.codepath.apps.MySimpleTweets.TwitterClient;
import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by nehadike on 2/15/15.
 */
public class UserTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragmentDemo = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        max_id = 0;
        populateTimeline();
    }

    //Send API request and fill list view by creating twitter objects from the json
    void populateTimeline() {
        String screenName = getArguments().getString("screen_name", "");
        client.getUserTimeline(max_id, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,  Header[] header, JSONArray json) {
                Log.d("DEBUG", json.toString());
                //De-serialize json, create models and load data into the list view
                addAll(Tweet.fromJsonArray(json));
                int lastIdx = tweets.size()-1;
                Tweet lastTweet = tweets.get(lastIdx);
                max_id = lastTweet.getUid()-1;
            }
            @Override
            public void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonError) {
                Log.d("DEBUG",jsonError.toString());

            }
        });
    }
}
