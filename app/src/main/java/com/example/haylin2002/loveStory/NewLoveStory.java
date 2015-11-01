package com.example.haylin2002.loveStory;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewLoveStory extends ActionBarActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int PICK_RESULT = 1;
    private File imgFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_love_story);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_love_story, menu);
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
        if (id == R.id.action_done) {
            final EditText description = (EditText) findViewById(R.id.editText);
            final EditText location = (EditText) findViewById(R.id.location);
            final String date = new SimpleDateFormat("HH:mm MMdd/yyyy").format(new Date());
//            ImageView img = (ImageView)findViewById(R.id.imageView);
            byte[] imageData = readContentIntoByteArray(imgFile);
//            Toast.makeText(this, "" + imageData.length, Toast.LENGTH_LONG).show();

            final ParseFile file = new ParseFile(imgFile.getName(), imageData);
//                Toast.makeText(this, imgFile.getName(), Toast.LENGTH_LONG).show();

            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        ParseUser user = ParseUser.getCurrentUser();
                        String partnerUser = user.getString("partner");
                        ParseObject moment = new ParseObject("Moment");
                        moment.put("image", file);
                        moment.put("description", description.getText().toString());
                        moment.put("location",location.getText().toString());
                        moment.put("date",date);
                        moment.put("author",user.getUsername());

//                        moment.put("partner",MatchWithPartner.partnerUser);
//                        moment.setACL(new ParseACL(user));
//                        MatchWithPartner.partnerACL.setReadAccess(user,true);
//                        MatchWithPartner.partnerACL.setWriteAccess(user,true);
//                        moment.setACL(MatchWithPartner.partnerACL);
                        ParseACL momentACL = new ParseACL(user);
                        momentACL.setRoleWriteAccess(user.getUsername() + "Couples", true);
                        momentACL.setRoleReadAccess(user.getUsername() + "Couples", true);
                        moment.setACL(momentACL);
                        moment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(NewLoveStory.this, R.string.toast_done, Toast.LENGTH_SHORT).show();
                                NewLoveStory.this.finish();

                            }
                        });
                    } else
                        Toast.makeText(NewLoveStory.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            });


//            Intent intent = new Intent();
////            intent.putExtra(LoveStory.IMG,fileUri);
//            intent.putExtra(LoveStory.TEXT, description.toString());
//            intent.putExtra(LoveStory.DATE, date);
//            intent.putExtra(LoveStory.LOCATION, location.toString());
//            setResult(RESULT_OK, intent);
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void takePicture(View v) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Love Story");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Love Story", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }

        return result;
    }

    public void chooseFromGallery(View view) {
        Intent pickFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
        pickFromGallery.setType("image/*");
        startActivityForResult(pickFromGallery, PICK_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                //Sample code is wrong, have to change to this
                Toast.makeText(this, "Image saved to: \n" + fileUri.toString(), Toast.LENGTH_SHORT).show();
                //show picture that's taken
                imgFile = new File(fileUri.getPath());
                ImageView image = (ImageView) findViewById(R.id.imageView);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 6;
                Bitmap myBitmap =
                        BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                image.setImageBitmap(myBitmap);


            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, R.string.toast_userCancel, Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, R.string.toast_captureFail, Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == PICK_RESULT) {
            if (resultCode == RESULT_OK) {
                //get filepath using the getRealPathFromURI method we created
                String filePath = getRealPathFromURI(data.getData());
                imgFile = new File(filePath);

                //show the pic that's selected
                ImageView mImage = (ImageView) findViewById(R.id.imageView);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImage.setImageBitmap(myBitmap);
            }
        }
    }

    private static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++) {
                System.out.print((char) bFile[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }
}
