package com.example.haylin2002.loveStory;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//please click + to use camera, and the book icon to go to webpage
//currently the imgs taken by user cannot be displayed in the main page

public class LoveStory extends ActionBarActivity{
    public StaggeredGridView mGridView;
    public static ArrayList<ParseObject> moments = new ArrayList<ParseObject>();
    public CustomAdapter mAdapter;
    public ImageView bg;
    private boolean orderDescending = true;
    public final static String PASSWORD = "img";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_story);
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
         bg = (ImageView)findViewById(R.id.imageView);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(LoveStory.this,MomentDetail.class);

                ParseObject moment = mAdapter.getItem(position);
                String objectID = moment.getObjectId();
//                Toast.makeText(LoveStory.this, objectID, Toast.LENGTH_LONG).show();
                i.putExtra(PASSWORD,objectID);
                startActivity(i);
            }
        });
//        CardView cardView = (CardView)findViewById(R.id.card_view);
//        GridView gridView = (GridView)findViewById(R.id.gridView);
//        mAdapter = new CustomAdapter(moments);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        mAdapter.notifyDataSetChanged();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            if(orderDescending){
                orderByDescending();
            }else{
                orderByAscending();
            }
        } else {
            // show the signup or login screen
            Intent i = new Intent(this, Welcome.class);
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_love_story, menu);
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
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, NewLoveStory.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_profile) {
            Intent i = new Intent(this, MyAccount.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_sort) {
            if(orderDescending){
                orderByAscending();
            }else{
                orderByDescending();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected boolean orderByDescending() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Moment");
        query.orderByDescending("date");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> moments, ParseException e) {
                if (e == null) {
                    if (moments.size() == 0) {

                        bg.setImageResource(R.drawable.love_story_bg);

                    } else {
                        bg.setVisibility(View.GONE);
                        mAdapter = new CustomAdapter(moments);
                        mGridView.setAdapter(mAdapter);
                    }
                } else {
                    Toast.makeText(LoveStory.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return orderDescending = true;
    }

    protected boolean orderByAscending(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Moment");
        query.orderByAscending("date");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> moments, ParseException e) {
                if (e == null) {
                    if(moments.size()==0){

                        bg.setImageResource(R.drawable.love_story_bg);

                    } else{
                        bg.setVisibility(View.GONE);
                        mAdapter = new CustomAdapter(moments);
                        mGridView.setAdapter(mAdapter);
                    }
                } else {
                    Toast.makeText(LoveStory.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return orderDescending = false;
    }

    private class CustomAdapter extends ArrayAdapter<ParseObject> {

        public CustomAdapter(List<ParseObject> moments1) {
            super(LoveStory.this, android.R.layout.simple_list_item_1, moments1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView =
                        getLayoutInflater().inflate(R.layout.grid_item, null);
            }

            ParseObject moment = getItem(position);

            ParseFile file = moment.getParseFile("image");
            byte[] imgData;
            try {
                imgData = file.getData();

                DynamicHeightImageView img =
                        (DynamicHeightImageView) convertView.findViewById(R.id.image);
//                Toast.makeText(LoveStory.this,"" + img.getHeightRatio(),Toast.LENGTH_SHORT).show();
                img.setHeightRatio(1.8);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                Bitmap bm = BitmapFactory.decodeByteArray(imgData, 0, imgData.length,options);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
//                img.setMinimumHeight(dm.heightPixels);
//                img.setMinimumWidth(dm.widthPixels);
                img.setImageBitmap(bm);
//                bm.recycle();
//                bm=null;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TextView description = (TextView) convertView.findViewById(R.id.description);
            String description1 = moment.getString("description");
            if(description1!=null){
                description.setText(description1);
            }

            TextView date = (TextView) convertView.findViewById(R.id.date);
            date.setText(moment.getString("date"));

            TextView location = (TextView) convertView.findViewById(R.id.location);
            String location1 = moment.getString("location");
            if(location1!= null){
                location.setText(location1);
            }

            TextView user = (TextView) convertView.findViewById(R.id.user);
            String user1 = moment.getString("author");
            if(user1!=null){
                user.setText("by " + user1 );
            }
            return convertView;
        }
    }
}
