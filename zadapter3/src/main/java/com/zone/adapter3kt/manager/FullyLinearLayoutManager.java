package com.zone.adapter3kt.manager;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.zone.adapter3kt.QuickConfig;

/**
 * Created by yutao on 2016/4/26 0026.
 */
public class FullyLinearLayoutManager extends LinearLayoutManager {


    public FullyLinearLayoutManager(Context context) {
        super(context);
    }

    public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FullyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        if (getItemCount() <= 0 || state.getItemCount()<=0){
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        int width = 0;
        int height = 0;
        int[] mMeasuredDimension = new int[2];
        //解除所有的
//        detachAndScrapAttachedViews(recycler);
        for (int i = 0; i < getItemCount(); i++) {
            if (i < state.getItemCount()) {
                try {
                    measureScrapChild(recycler, i, widthSpec, heightSpec, mMeasuredDimension);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getOrientation() == HORIZONTAL) {
                width = width + mMeasuredDimension[0];
                if (i == 0) {
                    height = mMeasuredDimension[1];
                }
            } else {
                height = height + mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
            }
        }
        QuickConfig.e("width:" + width + "\t height:" + height);
        setMeasuredDimension(width, height);
    }


    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);
        QuickConfig.e("measureScrapChild_getViewForPosition");
        // For adding Item Decor Insets to view
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getDecoratedLeft(view) + getDecoratedRight(view), p.width);
//            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getDecoratedBottom(view) + getDecoratedTop(view), p.height);
//            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,getPaddingBottom() + getDecoratedBottom(view), p.height);
            view.measure(childWidthSpec, childHeightSpec);

            // Get decorated measurements
            measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin + p.rightMargin;
            measuredDimension[1] = getDecoratedMeasuredHeight(view) + p.bottomMargin + p.topMargin;
            QuickConfig.e("position:" + position + "\t width:" + measuredDimension[0]+"\t height:"+measuredDimension[1]);
            recycler.recycleView(view);
        }
    }
}
