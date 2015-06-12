package com.luozi.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * User: Luozi
 * Date: 2015-05-27
 * Content:
 */
public class SimpleGridView extends GridView {
    public SimpleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleGridView(Context context) {
        super(context);
    }

    public SimpleGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
