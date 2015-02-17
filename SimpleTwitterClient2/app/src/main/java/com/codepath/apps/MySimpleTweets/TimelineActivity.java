package com.codepath.apps.MySimpleTweets;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.MySimpleTweets.R;
import com.codepath.apps.MySimpleTweets.fragments.HomeTimelineFragment;
import com.codepath.apps.MySimpleTweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.codepath.apps.MySimpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 20;
    private TwitterClient client;

    private long max_id;
    private User loggedinUser;

    ViewPager vpPager;
    HomeTimelineFragment homeFragment;
    MentionsTimelineFragment mentionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        if (!isNetworkAvailable()) {
            Toast.makeText(this,"Internet Connection not available!",Toast.LENGTH_SHORT).show();
        }

        client = TwitterApplication.getRestClient();
        max_id = 0;
        getLoggedInUser();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#06A9EE")));


        // Get viewpager
        vpPager = (ViewPager)findViewById(R.id.viewpager);
        //Set the view pager adapter
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //Find the sliding tab strip
        PagerSlidingTabStrip tabstrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        //Attach the tabstrip to the view
        tabstrip.setViewPager(vpPager);
        
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void getLoggedInUser() {
        
        client.getLoggedinUserDetails(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode,  Header[] header, JSONObject json) {
                Log.d("DEBUG",json.toString());
                loggedinUser = User.fromJson(json);
                getSupportActionBar().setTitle("Home");
              //  getSupportActionBar().setTitle("@"+loggedinUser.getScreenName().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] header, Throwable throwable, JSONObject jsonError) {
                Log.d("DEBUG",jsonError.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.miSearch) {
            Toast.makeText(this, "Search Coming Soon..", Toast.LENGTH_LONG).show();
        } else */if (id == R.id.miCompose) {
            Intent composeIntent = new Intent(TimelineActivity.this, ComposeActivity.class);
            composeIntent.putExtra("user", loggedinUser);
            startActivityForResult(composeIntent, REQUEST_CODE);
            return true;
        } else if (id == R.id.miRefresh) {
            refreshTimeline();
            return true;
        } else if (id == R.id.miProfile) {
            Intent profileIntent = new Intent(TimelineActivity.this, ProfileActivity.class);
            profileIntent.putExtra("user", loggedinUser);
            startActivity(profileIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && isNetworkAvailable()) {
            homeFragment.refreshTimeline();
           // getSupportFragmentManager().findFragmentByTag("MYTAG");
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshTimeline() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this,"Internet Connection not available!",Toast.LENGTH_SHORT).show();
            return;
        }
        int currentTab = vpPager.getCurrentItem();
        if (currentTab == 0) {
            homeFragment.refreshTimeline();
        } else if (currentTab == 1) {
            mentionsFragment.refreshTimeline();
        } else {
            return;
        }
    }
    //Return order of fragments in the view pager

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        
        private String tabTitles[] = {"Home", "Mentions"};
        
        //Constructor
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //Returns order of fragments
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                //return new HomeTimelineFragment();
                homeFragment = new HomeTimelineFragment();
                return homeFragment;
            } else if (position == 1) {
                mentionsFragment = new MentionsTimelineFragment();
                return mentionsFragment;
            }
            else {
                return null;
            }
        }

        //Rerurns tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //How many tabs
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
