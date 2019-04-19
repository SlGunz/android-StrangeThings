package com.example.strangethings.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.strangethings.mvp.Model;

import java.util.List;

public class SelectorView extends ViewGroup {

    public void setVariants(Context context, List<Model.Variant> variants, ThingsAdapter.Notification clickNotification) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for (Model.Variant variant : variants) {
            Button button = new Button(context);
            button.setLayoutParams(layoutParams);
            button.setText(variant.text());
            button.setOnClickListener(view -> clickNotification.show(variant));
            addView(button);
        }
    }

    private Rect childRect = new Rect();

    public SelectorView(Context context) {
        this(context, null);
    }

    public SelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int layoutHeight = 0;
        int layoutWidth = 0;

        int measuredState = 0;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            layoutHeight += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            layoutWidth = Math.max(layoutWidth, getMeasuredWidth() + lp.leftMargin + lp.rightMargin);

            measuredState = combineMeasuredStates(measuredState, child.getMeasuredState());
        }

        layoutHeight = Math.max(layoutHeight, getSuggestedMinimumHeight());
        layoutWidth = Math.max(layoutWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(
                resolveSizeAndState(layoutWidth, widthMeasureSpec, measuredState),
                resolveSizeAndState(layoutHeight, heightMeasureSpec, measuredState << MEASURED_HEIGHT_STATE_SHIFT)
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();

        int leftPos = getPaddingLeft();
        int topPos = getPaddingTop();

        int parentRightPos = right - left;  // width

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final int height = child.getMeasuredHeight();

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            childRect.left = leftPos + lp.leftMargin;
            childRect.right = parentRightPos - lp.rightMargin;

            childRect.top = topPos + lp.topMargin;
            childRect.bottom = childRect.top + height;

            topPos = childRect.bottom + lp.bottomMargin;

            child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LayoutParams
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new SelectorView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}