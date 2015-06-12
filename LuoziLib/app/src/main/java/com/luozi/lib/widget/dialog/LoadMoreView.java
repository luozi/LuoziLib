package com.luozi.lib.widget.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.luozi.lib.R;

/**
 * User: Luozi
 * Date: 2015-05-25
 * Content:
 */
public class LoadMoreView {

    private static Context mContext;
    private static LoadMoreView mLoadMoreView;

    private int size;
    private AnimatedView[] spots;
    private AnimatorPlayer animator;

    private static final int DELAY = 150;
    private static final int DURATION = 1500;

    public static synchronized LoadMoreView getInstance(Context context, View textView, View progressLayout) {
        if (mContext != context) {
            mContext = context;
            return mLoadMoreView = new LoadMoreView(context, textView, progressLayout);
        } else {
            return mLoadMoreView;
        }
    }

    private LoadMoreView(Context context, View textView, View progressLayout) {
        init(context, textView, progressLayout);
    }

    private void init(Context context, View textView, View progressLayout) {
        initProgress(context, textView, progressLayout);

        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    public void start() {
    }

    private void initProgress(Context context, View textView, View progressLayout) {
        TextView messageView = (TextView) textView;
        messageView.setText("正在加载更多");

        ProgressLayout progress = (ProgressLayout) progressLayout;
        size = progress.getSpotsCount();

        spots = new AnimatedView[size];
        int size = context.getResources().getDimensionPixelSize(R.dimen.spot_size);
        int progressWidth = context.getResources().getDimensionPixelSize(R.dimen.progress_width);
        for (int i = 0; i < spots.length; i++) {
            AnimatedView v = new AnimatedView(context);
            v.setBackgroundResource(R.drawable.spot);
            v.setTarget(progressWidth);
            v.setXFactor(-1f);
            progress.addView(v, size, size);
            spots[i] = v;
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[size];
        for (int i = 0; i < spots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(spots[i], "xFactor", 0, 1);
            move.setDuration(DURATION);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(DELAY * i);
            animators[i] = move;
        }
        return animators;
    }
}
