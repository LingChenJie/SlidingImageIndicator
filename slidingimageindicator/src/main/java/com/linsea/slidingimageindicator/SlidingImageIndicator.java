package com.linsea.slidingimageindicator;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class SlidingImageIndicator extends RelativeLayout implements DraggableLayout.OnSelectChangeListener {

    private DraggableLayout draggableLayout;
    private ViewGroup mThumbsLayout;
    private ImageView mSlider;

//    private java.util.ArrayList<Bitmap> mThumbList = new ArrayList<>();
    private HashMap<View,Bitmap> mThumbMap = new HashMap<>();
    private int mThumbWidth,mThumbHeight;
//    private int mSliderWidth,mSliderHeight;

    private OnSlidingChangeListener mListener;

    public SlidingImageIndicator(Context context) {
        super(context);
    }

    public SlidingImageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingImageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingImageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        draggableLayout = (DraggableLayout) LayoutInflater.from(getContext()).inflate(R.layout.sii_indicator_layout, this, true);
        mThumbsLayout = (ViewGroup) draggableLayout.findViewById(R.id.thumblist);
        mSlider = (ImageView) draggableLayout.findViewById(R.id.slider);
        draggableLayout.setOnSelectChangeListener(this);

        //int h =
        MarginLayoutParams layoutParams = (MarginLayoutParams) mThumbsLayout.getLayoutParams();
        mThumbHeight = mThumbsLayout.getMeasuredHeight() - layoutParams.bottomMargin - layoutParams.topMargin;

    }

    /**
     * create Bitmap thumb from Source, recycle the Source if recycleSource is true and the (thumb != source)
     * @param source Source Bitmap
     * @param recycleSource recycle input Source Bitmap if output thumb != source
     */
    public void addImageThumbs(Bitmap source, boolean recycleSource) {
        float w = source.getWidth();
        float h = source.getHeight();
        float ratio = w / h;
        Bitmap thumb = Bitmap.createScaledBitmap(source, (int) (mThumbHeight * ratio), mThumbHeight, false);

        ImageView iv = new ImageView(getContext());
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.height = MarginLayoutParams.MATCH_PARENT;
        params.width = MarginLayoutParams.WRAP_CONTENT;
        iv.setLayoutParams(params);
        iv.setImageBitmap(thumb);

        mThumbsLayout.addView(iv);
        mThumbMap.put(iv, thumb);

        if (recycleSource && thumb != source) {
            source.recycle();
        }
    }


    @Override
    public void onSelectChange(View child) {
        mSlider.setImageBitmap(mThumbMap.get(child));
        if (mListener != null) {
            mListener.onSlidingChange(child);
        }
    }


    public void setOnSlidingChangeListener(OnSlidingChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnSlidingChangeListener {
        void onSlidingChange(View child);
    }

    private float getDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }
}
