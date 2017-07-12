package zone.com.zadapter3.layoutmanager;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import zone.com.zadapter3.core.Pool;

/**
 * [2017] by Zone
 */

public class ZLayoutManager extends RecyclerView.LayoutManager {

    //边界
    private int mTotalWidth, mTotalHeight;
    private int mHorizontalOffset, mVerticalOffset;
    private Pool<ItemEntity> mItemFrames = new Pool<>(new Pool.New<ItemEntity>() {
        @Override
        public ItemEntity get() {
            return new ItemEntity();
        }
    });

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int line, sumLineCount;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() <= 0 || state.isPreLayout())
            return;
        line = 0;
        sumLineCount = 0;

        //解除所有的
        detachAndScrapAttachedViews(recycler);
        View first = recycler.getViewForPosition(0);
        //算上decorated测量
        measureChildWithMargins(first, 0, 0);
        //获取值
        int itemWidth = getDecoratedMeasuredWidth(first);
        int itemHeight = getDecoratedMeasuredHeight(first);

        for (int i = 0; i < getItemCount(); i++) {
            //进行迭代 初始化 frame的位置
            ItemEntity item = mItemFrames.get(i);

            //求 圈数 和index
            int lineCount;
            if (line == 0)
                lineCount = 1;
            else
                lineCount = 6 * line;

            if ((i + 1) - sumLineCount == lineCount) {
                setLine2Index(i, item, lineCount);
                line++;
                sumLineCount += lineCount;
            } else {
                setLine2Index(i, item, lineCount);
            }

            int realR = itemHeight * item.line;

            if (item.index == 0) {
                item.centerP.x = (int) (realR * Math.cos(Math.toRadians(150)) + getWidth() / 2);
                item.centerP.y = (int) (realR * Math.sin(Math.toRadians(150)) + getHeight() / 2) - (item.line != 0 ? itemHeight : 0);
            } else {
                caseByItem(i, item, itemHeight);
            }

            item.rect.set(item.centerP.x - itemWidth / 2, item.centerP.y - itemHeight / 2,
                    item.centerP.x + itemWidth / 2, item.centerP.y + itemHeight / 2);

            //  界面的大小 与 内容的大小;  来限定滑动范围;`
            mTotalWidth = Integer.MAX_VALUE;
            mTotalHeight = Integer.MAX_VALUE;
//            mTotalWidth = getHorizontalExcludeSpace();
//            mTotalHeight = getVerticalExcludeSpace();
        }
        fill(recycler, state);

    }

    private void caseByItem(int childIndex, ItemEntity item, int itemHeight) {
        int caseInt = item.index / item.line;
        ItemEntity itemOld = mItemFrames.get(childIndex - 1);
        switch (caseInt % 6) {
            case 0:
                item.centerP.y = itemOld.centerP.y - itemHeight;
                item.centerP.x = itemOld.centerP.x;
                break;
            case 1:
                item.centerP.x = (int) (itemOld.centerP.x + Math.sin(Math.toRadians(60)) * itemHeight);
                item.centerP.y = (int) (itemOld.centerP.y - Math.sin(Math.toRadians(30)) * itemHeight);
                break;
            case 2:
                item.centerP.x = (int) (itemOld.centerP.x + Math.sin(Math.toRadians(60)) * itemHeight);
                item.centerP.y = (int) (itemOld.centerP.y + Math.sin(Math.toRadians(30)) * itemHeight);
                break;
            case 3:
                item.centerP.y = itemOld.centerP.y + itemHeight;
                item.centerP.x = itemOld.centerP.x;
                break;
            case 4:
                item.centerP.x = (int) (itemOld.centerP.x - Math.sin(Math.toRadians(60)) * itemHeight);
                item.centerP.y = (int) (itemOld.centerP.y + Math.sin(Math.toRadians(30)) * itemHeight);
                break;
            case 5:
                item.centerP.x = (int) (itemOld.centerP.x - Math.sin(Math.toRadians(60)) * itemHeight);
                item.centerP.y = (int) (itemOld.centerP.y - Math.sin(Math.toRadians(30)) * itemHeight);
                break;
        }

    }


    private void setLine2Index(int i, ItemEntity item, int lineCount) {
        item.lineCount = lineCount;
        item.line = line;
        item.index = i - sumLineCount;
    }

    /**
     * 显示布局
     *
     * @param recycler
     * @param state
     */
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        //移动后 显示的rect;
        Rect displayRect = new Rect(mHorizontalOffset, mVerticalOffset,
                getHorizontalExcludeSpace() + mHorizontalOffset,
                getVerticalExcludeSpace() + mVerticalOffset);

        for (int i = 0; i < getItemCount(); i++) {
            ItemEntity item = mItemFrames.get(i);
            Rect frame = item.rect;
            if (Rect.intersects(displayRect, frame)) {
                //只有在显示在界面上的时候  才添加这个view
                View scrap = recycler.getViewForPosition(i);
                //真正添加
                addView(scrap);
                //添加的时候需要测量下 不然显示不出来
                measureChildWithMargins(scrap, 0, 0);
                //真正的布局
                layoutDecorated(scrap, frame.left - mHorizontalOffset, frame.top - mVerticalOffset,
                        frame.right - mHorizontalOffset, frame.bottom - mVerticalOffset);
            }
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //解除所有的
        detachAndScrapAttachedViews(recycler);
        if (mVerticalOffset + dy < 0) {
            //底部边界
            dy = -mVerticalOffset;
        } else if (mVerticalOffset + dy > mTotalHeight - getVerticalExcludeSpace()) {
            //顶部边界
            dy = mTotalHeight - getVerticalExcludeSpace() - mVerticalOffset;
        }
        //真正的移动
        offsetChildrenVertical(-dy);
        fill(recycler, state);
        //记录 竖直总偏移量
        mVerticalOffset += dy;
        return dy;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //解除所有的
        detachAndScrapAttachedViews(recycler);
        if (mHorizontalOffset + dx < 0) {
            //左边界
            //防止出界 就是出界的时候  dx等于纪录移动的反值  然后移动孩子 那么就到0了
            dx = -mHorizontalOffset;
        } else if (mHorizontalOffset + dx > mTotalWidth - getHorizontalExcludeSpace()) {
            //右边界
            dx = mTotalWidth - getHorizontalExcludeSpace() - mHorizontalOffset;
        }
        //真正的移动
        offsetChildrenHorizontal(-dx);
        fill(recycler, state);
        //记录 横向总偏移量
        mHorizontalOffset += dx;
        return dx;
    }


    private int getHorizontalExcludeSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalExcludeSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    class ItemEntity {
        Rect rect = new Rect();
        Point centerP = new Point();
        int line;
        // index 重0开始
        int index;
        int lineCount;
    }
}
