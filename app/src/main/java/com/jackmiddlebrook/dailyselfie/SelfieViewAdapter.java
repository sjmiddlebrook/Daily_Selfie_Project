package com.jackmiddlebrook.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * adapter for listview
 *
 */
public class SelfieViewAdapter extends BaseAdapter {

    private ArrayList<SelfieRecord> mSelfieRecords;
    private static LayoutInflater layoutInflater = null;
    private Context mContext;

    public SelfieViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mSelfieRecords = new ArrayList<SelfieRecord>();
    }

    public SelfieViewAdapter(Context context, ArrayList<SelfieRecord> selfieRecords) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        mSelfieRecords = selfieRecords;
    }

    public int getCount() {
        return mSelfieRecords.size();
    }

    public Object getItem(int position) {
        return mSelfieRecords.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;
        ViewHolder holder;

        SelfieRecord currentSelfie = mSelfieRecords.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            newView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder.selfie = (ImageView) newView.findViewById(R.id.icon);
            holder.selfieText = (TextView) newView.findViewById(R.id.label);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        String selfieString = currentSelfie.getSelfieText();
        Uri selfieUri = currentSelfie.getSelfieUri();
        Bitmap selfieBitmap = currentSelfie.getSelfieBitmap();
        if (selfieBitmap == null) {
            // TODO find how to get Bitmap from the URI
            // the selfieBitmap should be the image in the list but it is not coming up
            try {
                selfieBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selfieUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (selfieBitmap != null) {
                selfieBitmap = Bitmap.createScaledBitmap(selfieBitmap, 250, 250, false);
            }
        }

        // set the image and text for selfie mSelfieRecords
        holder.selfieText.setText(selfieString);
        holder.selfie.setImageBitmap(selfieBitmap);


        return newView;
    }

    static class ViewHolder {
        ImageView selfie;
        TextView selfieText;
    }

    public void add(SelfieRecord listItem) {
        mSelfieRecords.add(listItem);
        notifyDataSetChanged();
    }

    public ArrayList<SelfieRecord> getmSelfieRecords() {
        return mSelfieRecords;
    }

    public void removeAllViews() {
        mSelfieRecords.clear();
        this.notifyDataSetChanged();
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}
