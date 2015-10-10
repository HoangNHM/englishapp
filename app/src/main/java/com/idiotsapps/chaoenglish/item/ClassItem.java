package com.idiotsapps.chaoenglish.item;

import android.util.SparseIntArray;

/**
 * Created by vantuegia on 10/1/2015.
 */
public class ClassItem {
    private int mClassPercent; // chart
    private SparseIntArray mUnitPercent;
    private String mClassName;

    public ClassItem(String className, int classPercent, SparseIntArray unitPercent) {
        this.mClassName = className;
        this.mClassPercent = classPercent;
        this.mUnitPercent = unitPercent;
    }

    public int getClassPercent() {
        return mClassPercent;
    }

    public String getClassName() {
        return mClassName;
    }

    public SparseIntArray getUnitPercent() {
        return mUnitPercent;
    }
}
