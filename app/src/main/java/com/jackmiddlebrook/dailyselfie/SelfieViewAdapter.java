package com.jackmiddlebrook.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * adapter for listview
 *
 */
public class SelfieViewAdapter extends BaseAdapter {

    private ArrayList<SelfieRecord> mSelfieRecords = new ArrayList<SelfieRecord>();
    private static LayoutInflater layoutInflater = null;

    public SelfieViewAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
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

        SelfieRecord curr = mSelfieRecords.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            newView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder.selfie = (ImageView) newView.findViewById(R.id.icon);
            holder.selfieText = (TextView) newView.findViewById(R.id.label);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        String selfieString = curr.getSelfieText();
        Bitmap selfieBit = curr.getSelfieBitmap();
//        selfieBit = rotateBitmap(selfieBit, 270);

        // set the image and text for selfie mSelfieRecords
        holder.selfieText.setText(selfieString);
        holder.selfie.setImageBitmap(selfieBit);

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
