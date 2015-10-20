package com.idiotsapps.chaoenglish.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.idiotsapps.chaoenglish.R;

/**
 * Created by vantuegia on 10/20/2015.
 */
public class CustomDialogFragment extends DialogFragment {

    private static final String KEY = "word";

    private String mWord;
    private OnRialogDismissListener mDismissListener;

    public static CustomDialogFragment newInstance(String word) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY, word);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWord = getArguments().getString(KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_right_choice, container, false);
        TextView tvWord = (TextView) v.findViewById(R.id.tvWord);
        tvWord.setText(mWord);
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismissListener.onRialogDismissListener();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDismissListener = (OnRialogDismissListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRialogDismissListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDismissListener = null;
    }

    public interface OnRialogDismissListener {
        public void onRialogDismissListener();
    }
}
