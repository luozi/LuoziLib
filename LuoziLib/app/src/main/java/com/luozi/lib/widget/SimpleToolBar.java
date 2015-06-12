package com.luozi.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luozi.lib.R;


public class SimpleToolBar extends LinearLayout {

    public static final int ACTION_NAVIGATION = 1;
    public static final int ACTION_MENU = 2;
    public static final int ACTION_TITLE = 3;

    private Context mContext;

    private View mActionBgView;
    private TextView mNavigationView;
    private TextView mTitleView;
    private TextView mMenuView;
    private View mSolidShadowView;

    private OnActionClickListener mTitleClickListener;
    private OnActionClickListener mMenuClickListener;

    private OnActionClickListener mNavigationClickListener = new OnActionClickListener() {
        @Override
        public void onActionClick(View view, int action) {
            ((Activity) mContext).finish();
        }
    };

    public SimpleToolBar(Context context) {
        super(context);
        initView(context);
    }

    public SimpleToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        View bar = LayoutInflater.from(context).inflate(R.layout.item_bar, this, false);
        addView(bar);

        mActionBgView = bar.findViewById(R.id.action_bg);
        mNavigationView = (TextView) bar.findViewById(R.id.action_navigation);
        mTitleView = (TextView) bar.findViewById(R.id.title);
        mMenuView = (TextView) bar.findViewById(R.id.action_menu);
        mSolidShadowView = bar.findViewById(R.id.solid_shadow);

        mNavigationView.setTag(ACTION_NAVIGATION);
        mTitleView.setTag(ACTION_TITLE);
        mMenuView.setTag(ACTION_MENU);

        mNavigationView.setOnClickListener(mActionListener);
        mTitleView.setOnClickListener(mActionListener);
        mMenuView.setOnClickListener(mActionListener);

        //default
        setTitle(this.getClass().getSimpleName());
    }

    public void setBackgroundResource(int resid){
        mActionBgView.setBackgroundResource(resid);
    }

    public void setNavigationVisibility(int visibility) {
        mNavigationView.setVisibility(visibility);
    }

    public void setSolidShadowVisibility(int visibility) {
        mSolidShadowView.setVisibility(visibility);
    }

    public void setNavigationIcon(int resid) {
        setLeftDrawable(mContext, resid, mNavigationView);
    }

    public void setTitle(CharSequence text) {
        mTitleView.setText(text);
    }

    public void setTitle(int resid) {
        mTitleView.setText(resid);
    }

    public void setTitleColor(int color) {
        mTitleView.setTextColor(color);
    }


    public void setMenuIcon(int resid) {
        setRightDrawable(mContext, resid, mMenuView);
        mMenuView.setVisibility(View.VISIBLE);
    }

    public void setMenu(CharSequence text) {
        mMenuView.setText(text);
        mMenuView.setVisibility(View.VISIBLE);
    }

    public void setMenu(int resid) {
        mMenuView.setText(resid);
        mMenuView.setVisibility(View.VISIBLE);
    }

    public void setNavigationClickListener(OnActionClickListener l) {
        this.mNavigationClickListener = l;
    }

    public void setTitleClickListener(OnActionClickListener l) {
        this.mTitleClickListener = l;
    }

    public void setMenuClickListener(OnActionClickListener l) {
        this.mMenuClickListener = l;
    }

    public interface OnActionClickListener {
        public void onActionClick(View view, int action);
    }

    private OnClickListener mActionListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int action = (Integer) v.getTag();

            if (mTitleClickListener != null && action == ACTION_TITLE) {
                mTitleClickListener.onActionClick(v, action);
            } else if (mMenuClickListener != null && (mMenuView.getVisibility() == View.VISIBLE
                    && action == ACTION_MENU)) {
                mMenuClickListener.onActionClick(v, action);
            } else if (mNavigationClickListener != null && (mNavigationView.getVisibility() == View.VISIBLE)
                    && action == ACTION_NAVIGATION) {
                mNavigationClickListener.onActionClick(v, action);
            }
        }
    };

    private static void setLeftDrawable(Context context, int resId, TextView view) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    private static void setRightDrawable(Context context, int resId, TextView view) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(null, null, drawable, null);
    }
}
