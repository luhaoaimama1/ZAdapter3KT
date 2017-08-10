package com.zone.adapter3.absorb;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.zone.adapter3.QuickConfig;

public class AbsorbOnScrollListener extends RecyclerView.OnScrollListener {

    private final FrameLayout vpShow;
    private int[] absorbPos;
    private View[] absorbViews;
    private FrameLayout[] itemViews;
    private View placeholderView;
    private View contentView;

    public AbsorbOnScrollListener(FrameLayout vpShow, int... absorbPos) {
        if (absorbPos == null || absorbPos.length == 0)
            throw new IllegalStateException("absorbPos is not null or empty");
        this.absorbPos = absorbPos;
        this.vpShow = vpShow;
        absorbViews = new View[absorbPos.length];
        itemViews = new FrameLayout[absorbPos.length];
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int pos = 0;
        try {
            //GridLayoutManager 继承LinearLayoutManager 所以也支持GridLayoutManager
            pos = ((LinearLayoutManager) (recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("仅仅支持继承LinearLayoutManager的布局!");
        }

        //缓存list中断头view
        for (int i = 0; i < absorbPos.length; i++) {
            if (pos == absorbPos[i] && itemViews[i] == null) {
                try {
                    itemViews[i] = (FrameLayout) recyclerView.findViewHolderForLayoutPosition(pos).itemView;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalStateException("断头的itemView必须是FrameLayout!");
                }
                absorbViews[i] = itemViews[i].getChildAt(0);
            }
        }
        int showPos = findShowPos(pos);

        if (showPos == -1) {//啥也不显示
            if (placeholderView != null && placeholderView.getParent() != null) {
                restoreItemView(0);
                contentView = null;
            }
        } else {
            if (placeholderView == null)
                placeholderView = new View(recyclerView.getContext());

            //找到对应的位置
            int arrayPos = 0;
            for (int i = 0; i < absorbPos.length; i++) {
                if (showPos == absorbPos[i]) {
                    arrayPos = i;
                    break;
                }
            }

            if (!(contentView != null && contentView == absorbViews[arrayPos])) {
                //如果断头view不改变的话仅仅更改位置  主要是节省了操作

                //找到之前那个itemView 还原了
                for (int i = 0; i < absorbViews.length; i++) {
                    if (absorbViews[i] != null && absorbViews[i] == contentView) {
                        restoreItemView(i);
                        break;
                    }
                }

                //ItemView的狸猫换太子 显示在VP中
                itemViews[arrayPos].removeView(absorbViews[arrayPos]);
                placeholderView.setLayoutParams(absorbViews[arrayPos].getLayoutParams());
                placeholderView.getLayoutParams().height = absorbViews[arrayPos].getHeight();
                itemViews[arrayPos].addView(placeholderView);
                addContentView(absorbViews[arrayPos]);
            }

            //位移计算!
            if (showPos != absorbPos[absorbPos.length - 1]) {//最后的那个不进行位移
                int next = absorbPos[arrayPos + 1];
                if (recyclerView.findViewHolderForLayoutPosition(next) == null)
                    absorbViews[arrayPos].setTranslationY(0);
                else {
                    View targetView = recyclerView.findViewHolderForLayoutPosition(next).itemView;
                    if (targetView.getTop() <= absorbViews[arrayPos].getHeight())
                        absorbViews[arrayPos].setTranslationY(targetView.getTop() - absorbViews[arrayPos].getHeight());
                }
            }


        }
    }

    private void restoreItemView(int i) {
        itemViews[i].removeView(placeholderView);
        vpShow.removeView(absorbViews[i]);
        absorbViews[i].setTranslationY(0);
        itemViews[i].addView(absorbViews[i]);
    }

    private void addContentView(View absorbView) {
        vpShow.addView(absorbView);
        contentView = absorbView;
    }

    // 3,6,9为例  [1,3)show 无 ,[3,6)show 3,[6,9)show 6,>=9 show 9,
    private int findShowPos(int pos) {
        for (int i = 0; i < absorbPos.length; i++) {
            if (pos < absorbPos[i]) {
                if (i == 0)//[1,3)show 无
                    return -1;
                //[3,6)show 3,[6,9)show 6
                System.out.println("Absorb_ i:" + i + "\t itemViews[i-1].getTop()" + itemViews[i - 1].getTop());
                return findWithOutDecTop(i, "Absorb_显示:" + (i - 1), "Absorb_显示:无", "Absorb_显示:" + (i - 2));
            }
        }
        // >=9 show 9
        return findWithOutDecTop(absorbPos.length, "Absorb_显示  Last:" + (absorbPos.length - 1), "Absorb_显示  Last:无", "Absorb_显示  Last:" + (absorbPos.length - 2));
    }

    private int findWithOutDecTop(int i, String x, String x2, String x3) {
        // getTop如果<=0的时候 才显示这个位置的断头
        if (itemViews[i - 1].getTop() <= 0) {//注意:getTop不包括 dector
            QuickConfig.i(x);
            return absorbPos[i - 1];
        } else {//不然还是显示上一个位置的断头
            if (i - 2 < 0) {//上一个位置是无的情况
                QuickConfig.i(x2);
                return -1;
            }
            QuickConfig.i(x3);
            return absorbPos[i - 2];
        }
    }

}