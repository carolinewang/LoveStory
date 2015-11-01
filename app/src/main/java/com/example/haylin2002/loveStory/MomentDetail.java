package com.example.haylin2002.loveStory;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MomentDetail extends ActionBarActivity
        implements NoticeDialogFragment.NoticeDialogListener {

    protected String objectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        objectID = i.getStringExtra(LoveStory.PASSWORD);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Moment");
        query.setLimit(1);
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject moment, ParseException e) {
                if (e == null) {
                    ParseFile file = moment.getParseFile("image");
                    byte[] imgData;
                    try {
                        imgData = file.getData();
//                Toast.makeText(LoveStory.this,"" + img.getHeightRatio(),Toast.LENGTH_SHORT).show();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        Bitmap bm = BitmapFactory.decodeByteArray(imgData, 0, imgData.length,options);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
//                img.setMinimumHeight(dm.heightPixels);
//                img.setMinimumWidth(dm.widthPixels);
                        ImageView imageView = (ImageView)findViewById(R.id.imageView);
                        imageView.setImageBitmap(bm);
//                bm.recycle();
//                bm=null;
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                    TextView description = (TextView)findViewById(R.id.description);
                    String description1 = moment.getString("description");
                    if(description1!=null){
                        description.setText(description1);
                    }

                    TextView date = (TextView)findViewById(R.id.date);
                    date.setText(moment.getString("date"));
//                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.setTitle(moment.getString("date"));

                    TextView location = (TextView)findViewById(R.id.location);
                    String location1 = moment.getString("location");
                    if(location1!= null){
                        location.setText(location1);
                    }

                    TextView user = (TextView)findViewById(R.id.user);
                    String user1 = moment.getString("author");
                    if(user1!=null){
                        user.setText("by " + user1 );
                    }

                } else {
                    // something went wrong
                    Toast.makeText(MomentDetail.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_moment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            showNoticeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

public void showNoticeDialog() {
    // Create an instance of the dialog fragment and show it
    DialogFragment dialog = new NoticeDialogFragment();
    dialog.show(getFragmentManager(), "NoticeDialogFragment");
}

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        deleteMoment();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    protected void deleteMoment() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Moment");
        query.setLimit(1);
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject moment, ParseException e) {
                if (e == null) {
                    moment.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(MomentDetail.this, R.string.toast_delete,
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MomentDetail.this,LoveStory.class);
                            startActivity(i);
                        }
                    });
                } else {
                    Toast.makeText(MomentDetail.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
