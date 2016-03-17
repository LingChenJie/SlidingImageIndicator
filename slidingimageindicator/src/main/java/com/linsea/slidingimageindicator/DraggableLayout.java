package com.linsea.slidingimageindicator;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Content Draggable Layout.
 */
public class DraggableLayout extends RelativeLayout {

    private static String TAG = "DraggableLayout";
    private OnSelectChangeListener mSelectChangeListener;
    private ViewDragHelper mDragHelper;
    private View mDraggableView;
    private View mLastSelectedChild;
    private int mTop;

    public DraggableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDraggableView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child != mDraggableView) return 0;

                final int leftBound = getPaddingLeft() + ((MarginLayoutParams) child.getLayoutParams()).leftMargin;
                final int rightBound = getWidth() - child.getWidth() - getPaddingRight() - ((MarginLayoutParams) child.getLayoutParams()).rightMargin;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return mTop;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                if (state == ViewDragHelper.STATE_DRAGGING) {
                    mTop = mDraggableView.getTop();
                }
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d(TAG, String.format("onViewPositionChanged left=%d,top=%d,dx=%d,dy=%d,GroupWidth=%d", left, top, dx, dy, getWidth()));

                int centerX = left + changedView.getWidth() / 2;
                Log.d(TAG, String.format("onViewPositionChanged centerX=%d", centerX));

                for (int i = 0; i < 9; i++) {
                    View child = getChildAt(i);
                    if (Math.abs(child.getLeft() + child.getWidth() / 2 - centerX) < child.getWidth() / 2 && mLastSelectedChild != child) {
                        Log.d(TAG, String.format("onViewPositionChanged chile[%s],child.getRight()=%d", ((TextView) child).getText(), child.getRight()));
                        mLastSelectedChild = child;
                        if (mSelectChangeListener != null) {
                            mSelectChangeListener.onSelectChange(child);
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    public void setDraggableView(View draggableView) {
        if (draggableView == null && draggableView.getParent() != this) {
            throw new IllegalArgumentException("draggableView should be the DraggableLayout's direct Child!");
        }
        mDraggableView = draggableView;
    }


    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        this.mSelectChangeListener = listener;
    }

    public interface OnSelectChangeListener {
        void onSelectChange(View child);
    }


}
