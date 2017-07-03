package zone.com.zadapter3.layout;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.base.IAdapter;

import zone.com.zadapter3.layoutmanager.ZLayoutManager;
import zone.com.zadapter3.utils.ZLog;

/**
 * Created by yutao on 2016/4/26 0026.
 */
public class NFullyLinearLayoutManager extends LinearLayoutManager {


    public NFullyLinearLayoutManager(Context context) {
        super(context);
    }

    public NFullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NFullyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {

//        if (getItemCount() <= 0 || state.getItemCount()<=0){
//            super.onMeasure(recycler, state, widthSpec, heightSpec);
//            return;
//        }
        int width = 0;
        int height = 0;
        int[] mMeasuredDimension = new int[2];
        //解除所有的
//        detachAndScrapAttachedViews(recycler);
        for (int i = 0; i < getItemCount(); i++) {
//            if (i < state.getItemCount()) {
            try {
                measureScrapChild(recycler, i, widthSpec, heightSpec, mMeasuredDimension);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }

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
        ZLog.log("width:" + width + "\t height:" + height);
//        setMeasuredDimension(width, height);// width:1080height:1117
        setMeasuredDimension(1080, 1117);// width:1080height:1117
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
            ZLog.log("position:" + position + "\t width:" + measuredDimension[0]+"\t height:"+measuredDimension[1]);
            //真正添加
            addView(view);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() <= 0 || state.isPreLayout())
            return;

    }
//    /**
//     * 显示布局
//     *
//     * @param recycler
//     * @param state
//     */
//    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        if (getItemCount() <= 0 || state.isPreLayout()) {
//            return;
//            for (int i = 0; i < getItemCount(); i++) {
//                //只有在显示在界面上的时候  才添加这个view
//                View scrap = recycler.getViewForPosition(i);
//                //真正添加
//                addView(scrap);
//            }
//        }
//    }

}
