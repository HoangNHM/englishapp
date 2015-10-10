package com.idiotsapps.englishpicturedictionary4vietnam.item;

import android.graphics.Bitmap;

/**
 * Created by vantuegia on 10/1/2015.
 */
public class FriendsItem {
    private Bitmap mBitmap; // avatar
    private String mName;
    private String mPercent;

    public FriendsItem(Bitmap bitmap, String name, String percent) {
        this.mBitmap = bitmap;
        this.mName = name;
        this.mPercent = percent;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public String getName() {
        return mName;
    }

    public String getPercent() {
        return mPercent;
    }
}
