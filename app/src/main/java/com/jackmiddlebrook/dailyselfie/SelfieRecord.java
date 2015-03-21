package com.jackmiddlebrook.dailyselfie;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * to store the selfies the user takes
 *
 */
public class SelfieRecord {

    private Bitmap mSelfieBitmap;
    private String mSelfieText;
    private Uri mSelfieUri;



    private int mSelfieId;

    public SelfieRecord(Bitmap selfieImage, String selfieText, Uri selfieUri) {
        this.mSelfieBitmap = selfieImage;
        this.mSelfieText = selfieText;
        this.mSelfieUri = selfieUri;
    }

    public SelfieRecord() {

    }

    public SelfieRecord(int selfieId, String selfieText, String selfieUriString) {
        this.mSelfieId = selfieId;
        this.mSelfieText = selfieText;
        this.mSelfieUri = Uri.parse(selfieUriString);

    }

    public String getSelfieText() {
        return mSelfieText;
    }

    public void setSelfieText(String selfieText) {
        this.mSelfieText = selfieText;
    }

    public Bitmap getSelfieBitmap() {
        return mSelfieBitmap;
    }

    public void setSelfieBitmap(Bitmap selfieImage) {
        this.mSelfieBitmap = selfieImage;
    }

    public Uri getSelfieUri() {
        return mSelfieUri;
    }

    public String getSelfieUriAsString() {

        return mSelfieUri.toString();

    }

    public void setSelfieUri(Uri selfieUri) {
        this.mSelfieUri = selfieUri;
    }

    public int getSelfieId() {
        return mSelfieId;
    }

    public void setSelfieId(int selfieId) {
        mSelfieId = selfieId;
    }


}
