package com.jackmiddlebrook.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jackmiddlebrook.dailyselfie.Database.SelfieDataSource;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SelfieViewActivity extends ListActivity {

    public static final String TAG = SelfieViewActivity.class.getSimpleName();

    static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final long EIGHTY_SECOND_ALARM_DELAY = 80000L;

    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;
    private SelfieViewAdapter mAdapter;
    private AlarmManager mAlarmManager;
    private Uri mSelfieUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(),
                    "External Storage is not available.", Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        mAdapter = new SelfieViewAdapter(getApplicationContext());
        setListAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SelfieDataSource dataSource = new SelfieDataSource(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent expandSelfie = new Intent(this, SelfieDetailActivity.class);
        SelfieRecord selfieRecord = (SelfieRecord) mAdapter.getItem(position);
        String selfieUri = selfieRecord.getSelfieUri().toString();
        expandSelfie.putExtra("Uri", selfieUri);
        startActivity(expandSelfie);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_photos) {
            mAdapter.removeAllViews();
            return true;
        }

        if (id == R.id.enable_alarm) {

            // Create an Intent to broadcast to the AlarmNotificationReceiver
            mNotificationReceiverIntent = new Intent(SelfieViewActivity.this,
                    AlarmNotificationReceiver.class);

            // Create an PendingIntent that holds the NotificationReceiverIntent
            mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                    SelfieViewActivity.this, 0, mNotificationReceiverIntent, 0);

            // Get the AlarmManager Service
            mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            // Set repeating alarm
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + EIGHTY_SECOND_ALARM_DELAY,
                    EIGHTY_SECOND_ALARM_DELAY,
                    mNotificationReceiverPendingIntent);

            return true;
        }


        if (id == R.id.action_camera) {
            // create Intent to take a picture and return control to the calling application
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                mSelfieUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                if (mSelfieUri == null) {
                    // display error
                    Toast.makeText(this, "There was a problem accessing your external storage",
                            Toast.LENGTH_LONG).show();
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mSelfieUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            // Adds photo to the Media Provider's database
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mSelfieUri);
            sendBroadcast(mediaScanIntent);


            // Retrieves the selfie that was taken and adds it to an ImageView
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mSelfieUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageBitmap != null) {

                Date now = new Date();
                String timestamp = DateFormat.getDateTimeInstance().format(now);
                String selfieTag = "Selfie from " + timestamp;
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 250, 250, false);

                SelfieRecord newSelfie = new SelfieRecord(imageBitmap, selfieTag, mSelfieUri);
                mAdapter.add(newSelfie);
            }


        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Selfie Canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selfie failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getOutputMediaFileUri(int mediaType) {
        // For safety, check the SD card is mounted
        if (isExternalStorageAvailable()) {
            // Get the external storage directory
            String appName = SelfieViewActivity.this.getString(R.string.app_name);
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    appName);

            // Create our subdirectory
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory");
                    return null;
                }
            }
            // Create a file name and create the file
            File mediaFile;
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            String path = mediaStorageDir.getPath() + File.separator;
            if (mediaType == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            } else if (mediaType == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(path + "VID_" + timestamp + ".mp4");
            } else {
                return null;
            }

            Log.d(TAG, "File: " + Uri.fromFile(mediaFile));

            // Return the file's URI

            return Uri.fromFile(mediaFile);
        } else {

            return null;
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();

        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
