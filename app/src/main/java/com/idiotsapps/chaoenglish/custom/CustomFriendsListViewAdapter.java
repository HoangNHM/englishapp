package com.idiotsapps.chaoenglish.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.item.FriendsItem;

import java.util.ArrayList;


/**
 * Created by vantuegia on 10/1/2015.
 */
public class CustomFriendsListViewAdapter extends ArrayAdapter<FriendsItem> {

    private ArrayList<FriendsItem> mArrFriendsItems;
    private Activity mActivity;

    public CustomFriendsListViewAdapter(Activity activity, ArrayList<FriendsItem> arrFriendsItems) {
        super(activity, R.layout.friend_list_item_layout, arrFriendsItems);
        this.mActivity = activity;
        this.mArrFriendsItems = arrFriendsItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            // first time creation
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            view = inflater.inflate(R.layout.friend_list_item_layout, null);
        } // else next time will recover

        // avatar
        ImageView mImageViewClassListItem = (ImageView) view.findViewById(R.id.imageViewFriendsListItem);
        mImageViewClassListItem.setImageBitmap(mArrFriendsItems.get(position).getBitmap());

        // name
        TextView mTextViewFriendName = (TextView) view.findViewById(R.id.textViewFriendName);
        mTextViewFriendName.setText(mArrFriendsItems.get(position).getName());

        // percent
        TextView mTextViewFriendPercent = (TextView) view.findViewById(R.id.textViewFriendPercent);
        mTextViewFriendPercent.setText(mArrFriendsItems.get(position).getPercent() + "%");


        return view;
    }

}
