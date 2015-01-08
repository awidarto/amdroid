package com.kickstartlab.android.jwh.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.kickstartlab.android.jwh.R;
import com.kickstartlab.android.jwh.events.ScannerEvent;

import de.greenrobot.event.EventBus;

public class AlienDialogFragment extends DialogFragment {
    public interface AlienDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private AlienDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static AlienDialogFragment newInstance(String title, String message, AlienDialogListener listener) {
        AlienDialogFragment fragment = new AlienDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(AlienDialogFragment.this);
                }
            }
        });

        builder.setNeutralButton(R.string.action_create,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventBus.getDefault().post(new ScannerEvent("resume"));
            }
        });

        return builder.create();
    }
}
