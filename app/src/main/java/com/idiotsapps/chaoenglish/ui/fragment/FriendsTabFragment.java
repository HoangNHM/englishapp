package com.idiotsapps.chaoenglish.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.idiotsapps.chaoenglish.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsTabFragment extends Fragment {

    private static final String TAG = "facebook_error";
    private ImageView mIvRateUs;
    private Button mBtnFbInvite;
    private LoginButton mBtnFbLogin;
    private TextView mUserName;
    private ProfilePictureView mProfilePic;
    private CallbackManager mCallbackManager;
    private View.OnClickListener inviteFbListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String appLinkUrl, previewImageUrl;
            appLinkUrl = "https://fb.me/749567541854476";
            previewImageUrl = "https://scontent-hkg3-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/11745676_956819461007984_1887474383476805338_n.jpg?oh=f839a51646973b0f41eb984934ecf274&oe=56BDF978";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                        .build();
                AppInviteDialog appInviteDialog = new AppInviteDialog(getActivity());
                appInviteDialog.registerCallback(mCallbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Log.d(TAG, "AppInviteDialog onSuccess " + result.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "AppInviteDialog onCancel");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.d(TAG, "AppInviteDialog onError " + e.toString());
                    }
                });
                appInviteDialog.show(content);
            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FriendsTabFragment.
     */
    public static FriendsTabFragment newInstance(String param1, String param2) {
        FriendsTabFragment fragment = new FriendsTabFragment();
        return fragment;
    }

    public FriendsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);

        // User name
        mUserName = (TextView) view.findViewById(R.id.userName);

        // Login btn
        mCallbackManager = CallbackManager.Factory.create();
        mBtnFbLogin = (LoginButton) view.findViewById(R.id.btnFbLogin);
        // Callback registration
        mBtnFbLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setProfile();
                Log.d(TAG, "logged in onSuccess");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onError");

            }
        });

        // Invite
        mBtnFbInvite = (Button) view.findViewById(R.id.btnFbInvite);
        mBtnFbInvite.setOnClickListener(inviteFbListener);

        // Rate us
        mIvRateUs = (ImageView) view.findViewById(R.id.btnRateUs);
        mIvRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                final String appPackageName = "com.idiots.colldict"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        //code in here or onViewCreated is nearly the same, still not figure out which is better
        return view;
    }

    private void setProfile() {
        Profile profile = Profile.getCurrentProfile();
        if (null != profile) {
            mProfilePic.setProfileId(profile.getId());
            mUserName.setText(profile.getName());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Profile Pic
        mProfilePic = (ProfilePictureView) view.findViewById(R.id.profilePic);
        setProfile();

        // Add advertisement
        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult\n" + requestCode + "\n" + resultCode + "\n" + data.toString());
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
