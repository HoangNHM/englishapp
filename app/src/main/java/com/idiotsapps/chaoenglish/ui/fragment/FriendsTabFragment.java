package com.idiotsapps.chaoenglish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
//        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
//                    .setPreviewImageUrl(previewImageUrl)
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
//        FacebookSdk.sdkInitialize(MainActivity.mMainContext);
        // User name
        mUserName = (TextView) view.findViewById(R.id.userName);
        mCallbackManager = CallbackManager.Factory.create();
        mBtnFbLogin = (LoginButton) view.findViewById(R.id.btnFbLogin);
//        mBtnFbLogin.setReadPermissions("public_profile");
        // If using in a fragment
//        mBtnFbLogin.setFragment(FriendsTabFragment.this);
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
        mProfilePic = (ProfilePictureView) view.findViewById(R.id.profilePic);
        setProfile();
        mBtnFbInvite = (Button) view.findViewById(R.id.btnFbInvite);
        mBtnFbInvite.setOnClickListener(inviteFbListener);

        Button btnRateUs = (Button) view.findViewById(R.id.btnRateUs);
        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Rate us", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult\n" + requestCode + "\n" + resultCode + "\n" + data.toString());
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
