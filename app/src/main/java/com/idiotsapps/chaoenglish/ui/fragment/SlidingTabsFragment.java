package com.idiotsapps.chaoenglish.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.ui.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class SlidingTabsFragment extends Fragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();
    private int[] imageResId = {
            R.drawable.ic_class,
            R.drawable.ic_friend
    };
    public SlidingTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Sliding facebook_error", "onActivityResult\n" + requestCode + "\n" + resultCode + "\n" + data.toString());
        SamplePagerItem.friendsTabFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabs.add(new SamplePagerItem(
                "Class", // Title
//                Color.BLUE, // Indicator color
                ContextCompat.getColor(getContext(), R.color.action_bar_bg),
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                "Friends", // Title
//                Color.RED, // Indicator color
                ContextCompat.getColor(getContext(), R.color.action_bar_bg),
                Color.GRAY // Divider color
        ));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter( new SlidingFragmentPagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        // set custom tab, icon not text
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);

        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });
    }

    class SlidingFragmentPagerAdapter extends FragmentPagerAdapter {

        SlidingFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
         * <p>
         * Here we return the value returned from {@link SamplePagerItem#createFriendFragment()}.
         */
        @Override
        public Fragment getItem(int i) {
            if (0 == i) {
                return mTabs.get(i).createClassFragment();
            } else {
                return mTabs.get(i).createFriendFragment();
            }
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we return the value returned from {@link SamplePagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            // set custom tab, icon not text
//            return mTabs.get(position).getTitle();
            Drawable image = getResources().getDrawable(imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
        // END_INCLUDE (pageradapter_getpagetitle)

    }

    /**
     * This class represents a tab to be displayed by {@link ViewPager} and it's associated
     * {@link SlidingTabLayout}.
     */
    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        public static FriendsTabFragment friendsTabFragment;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

//        Fragment createFriendFragment() {
//            return FriendsTabFragment.newInstance("str1", "str2");
//        }

        /**
         * @return A new {@link android.support.v4.app.Fragment} to be displayed by a {@link ViewPager}
         */
        Fragment createFriendFragment() {

            friendsTabFragment =  FriendsTabFragment.newInstance("str1", "str2");
            return friendsTabFragment;
        }

        Fragment createClassFragment() {
            return ClassTabFragment.newInstance("str1", "str2");
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            return mTitle;
        }

        /**
         * @return the color to be used for indicator on the {@link SlidingTabLayout}
         */
        int getIndicatorColor() {
            return mIndicatorColor;
        }

        /**
         * @return the color to be used for right divider on the {@link SlidingTabLayout}
         */
        int getDividerColor() {
            return mDividerColor;
        }
    }

}
