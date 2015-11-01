package com.etsy.android.grid.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An {@link android.widget.ImageView} layout that maintains a consistent width to height aspect ratio.
 */
public class DynamicHeightImageView extends ImageView {
    public static float radius = 2.0f;

    Path clipPath = new Path();
    RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

    @Override
    protected void onDraw(Canvas canvas) {
        rect.left = 0;
        rect.top = 0;
        rect.right = this.getWidth();
        rect.bottom = this.getHeight();
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    private double mHeightRatio;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
