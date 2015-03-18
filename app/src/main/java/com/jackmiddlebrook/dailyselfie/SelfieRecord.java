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

    public SelfieRecord(Bitmap selfieImage, String selfieText, Uri selfieUri) {
        this.mSelfieBitmap = selfieImage;
        this.mSelfieText = selfieText;
        this.mSelfieUri = selfieUri;
    }

    public SelfieRecord() {

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

    public void setSelfieUri(Uri selfieUri) {
        this.mSelfieUri = selfieUri;
    }
}
