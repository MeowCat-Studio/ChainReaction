package org.meowcat.wearable.chain.heytap.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import static android.view.View.MeasureSpec.EXACTLY;

/**
 * Created by luern0313 on 2020/5/5.
 */
public class ChessLinearLayout extends LinearLayout {
    int lines = 3;

    public ChessLinearLayout(Context context) {
        super(context);
    }

    public ChessLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChessLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) / lines, EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}
