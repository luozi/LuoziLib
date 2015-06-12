package com.luozi.lib.helper;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

/**
 * Describe: 按钮等待60秒.
 * User: luozi
 * Date: 15/4/20
 */
public class WaitTimeHelper {

    private Handler mHandler;
    private TextView mGetVerifyView;
    private int mCount = 60;

    private String mGetVerifyViewText;

    public WaitTimeHelper(Context context, TextView view) {
        mHandler = new Handler();
        mGetVerifyView = view;
        mGetVerifyViewText = mGetVerifyView.getText().toString();
    }

    public void onStart() {
        mGetVerifyView.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mGetVerifyView.setText(getCombinationValue("等待", mCount, "秒"));
                mCount = mCount - 1;

                if (mCount == 0) {
                    mCount = 60;
                    mGetVerifyView.setText(mGetVerifyViewText);
                    mGetVerifyView.setEnabled(true);
                } else {
                    onStart();
                }

                mHandler.removeCallbacks(this);
            }
        }, 1000);
    }

    private static String getCombinationValue(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object v : objects) {
            sb.append(v);
        }
        return sb.toString();
    }
}
