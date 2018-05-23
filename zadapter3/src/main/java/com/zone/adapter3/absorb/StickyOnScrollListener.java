package com.zone.adapter3.absorb;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.StickyViewDelegates;

/**
 * [2018] by Zone
 * <p>
 */
public class StickyOnScrollListener extends RecyclerView.OnScrollListener {

    private final FrameLayout vpShow;
    private StickyViewDelegates[] mStickyViews;
    private int[] stickyPostions;
    private View placeholderView;
    private int preStickyIndex = -1;


    public StickyOnScrollListener(FrameLayout vpShow, int[] stickyPostions, StickyViewDelegates[] mStickyViews) {
        this.vpShow = vpShow;
        this.stickyPostions = stickyPostions;
        this.mStickyViews = mStickyViews;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    int placeColor =Color.TRANSPARENT;
    public void setPlaceColor(@ColorInt int color){
        this.placeColor =color;
    }

    @Override
    public synchronized void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int pos = 0;
        try {
            //GridLayoutManager 继承LinearLayoutManager 所以也支持GridLayoutManager
            pos = ((LinearLayoutManager) (recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
            QuickConfig.e("吸附-》findFirstVisibleItemPosition：" + pos);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("仅仅支持继承LinearLayoutManager与GridLayoutManager的布局!");
        }

        int showStickyPos = findShowPos(pos);
        //找到对应的 数组位置  用他去找view

        int nowStickyIndex = 0;
        for (int i = 0; i < stickyPostions.length; i++) {
            if (showStickyPos == stickyPostions[i]) {
                nowStickyIndex = i;
                break;
            }
        }

        if (showStickyPos == -1) {//啥也不显示
            if (preStickyIndex != -1) {
                restorePreSticky();
                QuickConfig.e("吸附-》未找到吸附位置：重置占位头");
                preStickyIndex = -1;
            }
        } else {
            if (placeholderView == null) {
                placeholderView = new View(recyclerView.getContext());
                placeholderView.setBackgroundColor(placeColor);
            }


            if (preStickyIndex == -1 || preStickyIndex != nowStickyIndex) {
                //如果当前吸附的与吸附的判断是一样的话 跳过 因为已经设置过了,或者preStickyIndex=-1代表之前没有设置过

                if (preStickyIndex != -1) {
                    //找到之前那个itemView 还原了
                    restorePreSticky();
                }

                QuickConfig.e("吸附-》吸附位置:" + nowStickyIndex + "狸猫换太子");
                //ItemView的狸猫换太子 显示在VP中
                View stickyView = mStickyViews[nowStickyIndex].getEHFHolder().itemView;
                removeParent(stickyView);
                removeParent(placeholderView);
                placeholderView.setLayoutParams(stickyView.getLayoutParams());
                //设置站位高度
                placeholderView.getLayoutParams().height = stickyView.getHeight();

                if (mStickyViews[nowStickyIndex].getParentHolder() != null) {
                    ViewGroup recyclerHolder = (ViewGroup) mStickyViews[nowStickyIndex].getParentHolder().itemView;
                    recyclerHolder.addView(placeholderView);
                    mStickyViews[nowStickyIndex].setPlaceholderView(placeholderView);
                    vpShow.addView(stickyView);
                    preStickyIndex = nowStickyIndex;

                }
            }

            // 位移计算!
            if (showStickyPos != stickyPostions[stickyPostions.length - 1]) {//最后的那个不进行位移
                int next = stickyPostions[nowStickyIndex + 1];
                View stickyView = mStickyViews[nowStickyIndex].getEHFHolder().itemView;
                if (stickyView != null) {
                    if (recyclerView.findViewHolderForLayoutPosition(next) == null)
                        stickyView.setTranslationY(0);
                    else {
                        View targetView = recyclerView.findViewHolderForLayoutPosition(next).itemView;
                        if (targetView.getTop() <= stickyView.getHeight())
                            stickyView.setTranslationY(targetView.getTop() - stickyView.getHeight());
                    }
                }

            }

        }
    }

    private void restorePreSticky() {
        QuickConfig.e("吸附-》还原了之前那个位置：" + stickyPostions[preStickyIndex]);
        View preStickyView = mStickyViews[preStickyIndex].getEHFHolder().itemView;
        removeParent(preStickyView);

        removeParent(placeholderView);
        mStickyViews[preStickyIndex].setPlaceholderView(null);

        preStickyView.setTranslationY(0);
        ViewGroup preVp = (ViewGroup) mStickyViews[preStickyIndex].getParentHolder().itemView;
        preVp.addView(preStickyView);
    }

    private void removeParent(View view) {
        ViewGroup vp = (ViewGroup) (view.getParent());
        if (vp != null)
            vp.removeView(view);
    }

    // 3,6,9为例  [1,3)show 无 ,[3,6)show 3,[6,9)show 6,>=9 show 9,
    private int findShowPos(int pos) {
        // if (itemViews[i - 1].getTop() <= 0) {//注意:getTop不包括 dector

        for (int i = 0; i < stickyPostions.length; i++) {
            if (pos < stickyPostions[i]) {
                if (i == 0)//[1,3)show 无
                    return -1;
                //[3,6)show 3,[6,9)show 6
//                System.out.println("Absorb_ i:" + i + "\t itemViews[i-1].getTop()" + itemViews[i - 1].getTop());
                return findWithOutDecTop(i);
            }
        }
        // >9 show 9   =9的时候看top ,如果top<=0 就是9 如果top>
        if (pos == stickyPostions[stickyPostions.length - 1]) {
            return findWithOutDecTop(stickyPostions.length);
        } else
            return stickyPostions[stickyPostions.length - 1];

    }

    // 主要是处理 dec的  如果3是断头，但是3有dec的话，getTop<=0才代表断头是3，不然就是3之前的断头
    private int findWithOutDecTop(int i) {
        if (mStickyViews[i - 1].getParentHolder() != null) {
            View itemView = mStickyViews[i - 1].getParentHolder().itemView;
            // getTop如果<=0的时候 才显示这个位置的断头
            if (itemView.getTop() <= 0) {//注意:getTop不包括 dector
                return stickyPostions[i - 1];
            } else {//不然还是显示上一个位置的断头
                if (i - 2 < 0) {//上一个位置是无的情况
                    return -1;
                }
                return stickyPostions[i - 2];
            }
        }
        return -1;
    }

}