package com.codepath.apps.MySimpleTweets;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.MySimpleTweets.R;
import com.codepath.apps.MySimpleTweets.fragments.UserTimelineFragment;
import com.codepath.apps.MySimpleTweets.models.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#06A9EE")));//#0ee0f0

        User user = (User) getIntent().getSerializableExtra("user");
        populateProfileHeader(user);
        
        //Get Screen name and load user timeline
        String screenName = user.getScreenName().toString();//getIntent().getStringExtra("screen_name");
        getSupportActionBar();
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Create user timeline fragment
            UserTimelineFragment fragmentUsertimeline = UserTimelineFragment.newInstance(screenName);
            //Display user fragment within this activity
            ft.replace(R.id.flContainer, fragmentUsertimeline);
            ft.commit();
        }

    }

    private void populateProfileHeader(User user) {

        ImageView ivUserBackgroundImageView = (ImageView) findViewById(R.id.ivProfileBackgroundImage);
        Picasso.with(this).load(user.getProfileBackgroundImageUrl()).into(ivUserBackgroundImageView);
        
        ImageView ivUserImageView = (ImageView) findViewById(R.id.ivProfileUserImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivUserImageView);
        TextView tvUserName = (TextView) findViewById(R.id.tvProfileUserName);
        tvUserName.setText(user.getName());
        TextView tvUserAlias = (TextView) findViewById(R.id.tvProfileUserTagline);
        tvUserAlias.setText(user.getTagline());
        TextView tvFollowers = (TextView)findViewById(R.id.tvFollowersCount);
        tvFollowers.setText(user.getFollowersCount() + " FOLLOWERS");
        TextView tvFollowing = (TextView)findViewById(R.id.tvFollowingCount);
        tvFollowing.setText(user.getFollowingCount() + " FOLLOWING");
        TextView tvTweets = (TextView)findViewById(R.id.tvTweetsCount);
        tvTweets.setText(user.getTweetsCount() + " TWEETS");

        RelativeLayout rlHeader = (RelativeLayout) findViewById(R.id.rlUserHeader);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rlHeader.getLayoutParams().width,rlHeader.getLayoutParams().height);
        ivUserBackgroundImageView.setLayoutParams(params);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
