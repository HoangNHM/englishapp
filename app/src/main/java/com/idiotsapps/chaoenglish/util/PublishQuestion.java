package com.idiotsapps.chaoenglish.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PublishQuestion {

    private static final String DIRECTORY = "images";

    public static void publish(Context context, ViewGroup viewGroup) {

        viewGroup.setDrawingCacheEnabled(true);

        viewGroup.buildDrawingCache();

        Bitmap bm = viewGroup.getDrawingCache();

        saveBmpToFile(context, bm);

        viewGroup.setDrawingCacheEnabled(false);

        sharedIntent(context, getUri(context));
    }

    private static void saveBmpToFile(Context context, Bitmap bmp) {
        try {
            File cachePath = new File(context.getCacheDir(), DIRECTORY);
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/question.png"); // overwrites this image every time
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Uri getUri(Context context) {
        File imagePath = new File(context.getCacheDir(), DIRECTORY);
        File newFile = new File(imagePath, "question.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.idiotsapps.chaoenglish.fileprovider", newFile);
        return contentUri;
    }

    private static void sharedIntent(Context context, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
//        shareIntent.setDataAndType(uri, context.getContentResolver().getType(uri));
        shareIntent.setType(context.getContentResolver().getType(uri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
    }
}
