package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter.R;
import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ResViewDelegates;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.bean.Wrapper;
import com.zone.adapter3.helper.Helper;
import com.zone.adapter3.manager.MarginItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 * <p>
 * 多类型 使用{@link #getItemViewType2(int)}
 */
public abstract class Header2FooterRcvAdapter<T> extends BaseRcvAdapter<T> {

    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -3;

    //Limit one thousand
    private List<ViewDelegates> mHeaderViews = new ArrayList<>();
    private List<ViewDelegates> mFooterViews = new ArrayList<>();
    private ViewDelegates mEmptyView;
    //no limit;
    private List<Wrapper> mViews = new ArrayList<>();

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager manager;

    public Header2FooterRcvAdapter(Context context, List<T> data) {
        super(context, data);
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
            QuickConfig.e("onCreateViewHolder header or footer :" + viewType);
            return new Holder(setFullspan(LayoutInflater.from(context)
                    .inflate(R.layout.base_vp, mRecyclerView, false)));
        } else if (viewType == Wrapper.EMPTY_VALUE) {
            QuickConfig.e("onCreateViewHolder Empty:" + viewType);
            mEmptyView.tryCreateView(context, mRecyclerView);
            return new Holder(setFullspan(mEmptyView.getItemView()));
        } else {
            QuickConfig.e("onCreateViewHolder views:" + viewType);
            Wrapper targetWarpper = null;
            for (Wrapper mView : mViews) {
                if (mView.getStyle() == Wrapper.DEFAULT_VALUE) {
                    //default
                    targetWarpper = mView;
                }
                if (viewType == mView.getStyle()) {
                    targetWarpper = mView;
                    break;
                }
            }
            if (targetWarpper == null)
                throw new IllegalStateException("请设置默认o.style.switch的默认viewHold");
            //这里 必须创建  因为我是通过 ViewDelegates的类型去创建的
            targetWarpper.getViewDelegates().reallyCreateHFView(context, mRecyclerView);
            if (targetWarpper.getViewDelegates().isFullspan())
                setFullspan(targetWarpper.getViewDelegates().getItemView());
            final Holder holder = new Holder(context, targetWarpper);
            holder.setHelper(createHelper(holder, context, targetWarpper));
            return holder;
        }
    }


    protected Helper createHelper(Holder holder, Context context, Wrapper targetWarpper) {
        return new Helper(context, holder, this);
    }

    //viewHolder or footer 保留第一层的lp 这样第一层就不会无效了!
    private View setFullspan(View itemView) {
        if (manager != null && manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params =
                    new StaggeredGridLayoutManager.LayoutParams(itemView.getLayoutParams());
            params.setFullSpan(true);
            itemView.setLayoutParams(params);
        }
        if (manager != null && manager instanceof LinearLayoutManager) {
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(itemView.getLayoutParams());
            itemView.setLayoutParams(params);
        }
        return itemView;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (isEmptyData())
            return;
        if (position >= mHeaderViews.size() && position < mHeaderViews.size() + data.size()) {
            if (holder.wrapper != null)
                holder.wrapper.getViewDelegates().fillData(
                        position, data.get(getDataPosition(position)),
                        holder.helper);
        } else if (position < mHeaderViews.size()) {
            QuickConfig.e("bind header position:" + position);
            bindHFView((ViewGroup) holder.itemView, mHeaderViews.get(position).getItemView());
        } else {
            QuickConfig.e("bind footer position:" + position);
            bindHFView((ViewGroup) holder.itemView, mFooterViews.get(position - getHeaderViewsCount() - data.size()).getItemView());
        }
    }

    private void bindHFView(ViewGroup parent, View child) {
        parent.removeAllViews();
        ViewGroup vp = (ViewGroup) child.getParent();
        if (vp != null)
            vp.removeAllViews();
        parent.addView(child);
    }

    private int getDataPosition(int mapPosition) {
        return mapPosition - mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        return getRealItemCount() + (isEmptyData() ? 1 : 0);
    }

    @Override
    public int getRealItemCount() {
        return data.size() + mHeaderViews.size() + mFooterViews.size();
    }

    /**
     * @return layoutid
     * 因为getItemViewType不同 导致 头部底部view不会被重用!
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews.size() > 1000 || mFooterViews.size() > 1000)
            throw new RuntimeException("viewHolder or footer  max size is 1000!");

        if (position == 0 && isEmptyData()) {
            QuickConfig.e("getItemViewType empty:" + position);
            return Wrapper.EMPTY_VALUE;
        }
        if (position >= getHeaderViewsCount() && position < getHeaderViewsCount() + data.size()) {
            QuickConfig.e("getItemViewType views:" + position);
            int result = getItemViewType2(getDataPosition(position));
            if (result == ITEM_VIEW_TYPE_HEADER_OR_FOOTER)
                throw new IllegalStateException("layoutType is must not be" + ITEM_VIEW_TYPE_HEADER_OR_FOOTER);
            return result;
        } else {
            QuickConfig.e("getItemViewType healder or footer:" + position);
            return ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
        }
    }


    @Override
    public IAdapter relatedList(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        gridSetLookup();
        this.mRecyclerView.setAdapter(this);
        return this;
    }

    private void gridSetLookup() {
        if (mRecyclerView == null)
            throw new RuntimeException("method :relatedList is used after " +
                    " addHeaderView or addFooterView");
        manager = mRecyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position >= getHeaderViewsCount() && position < getHeaderViewsCount()
                            + data.size()) {
                        for (int i = 0; i < mViews.size(); i++) {
                            if (mViews.get(i).getStyle() == getItemViewType2(getDataPosition(position))
                                    && mViews.get(i).getViewDelegates().isFullspan())
                                return ((GridLayoutManager) manager).getSpanCount();
                        }
                        return 1;
                    } else
                        return ((GridLayoutManager) manager).getSpanCount();
                }
            });
            mRecyclerView.setLayoutManager(manager);
        }
    }


    /**
     * 默认 getItemViewType2 的值 如果找不到则是此view.
     *
     * @param viewDelegates
     * @return
     */
    @Override
    public IAdapter addViewHolder(ViewDelegates viewDelegates) {
        if (mViews.size()==0||mViews.get(0).getStyle() != Wrapper.DEFAULT_VALUE)
            mViews.add(0, new Wrapper(Wrapper.DEFAULT_VALUE, viewDelegates));
        else
            mViews.set(0, new Wrapper(Wrapper.DEFAULT_VALUE, viewDelegates));
        return this;
    }

    List<Integer> repeatCheckList = new ArrayList<>();

    @Override
    public IAdapter addViewHolder(int style, ViewDelegates viewDelegates) {
        if (Wrapper.DEFAULT_VALUE == style
                || Wrapper.EMPTY_VALUE == style
                || style == ITEM_VIEW_TYPE_HEADER_OR_FOOTER)
            throw new IllegalStateException(String.format("style不能为内设的值:%d,%d,%d"
                    , Wrapper.DEFAULT_VALUE, Wrapper.EMPTY_VALUE, ITEM_VIEW_TYPE_HEADER_OR_FOOTER));
        if (!repeatCheckList.contains(style)) {
            repeatCheckList.add(style);
            mViews.add(new Wrapper(style, viewDelegates));
        } else {
            for (int i = 0; i < mViews.size(); i++) {
                if (style == mViews.get(i).getStyle()) {
                    mViews.set(i, new Wrapper(style, viewDelegates));
                    break;
                }
            }
        }
        return this;
    }

    @Override
    public IAdapter addHeaderHolder(ViewDelegates header, boolean notify) {
        if (header == null)
            throw new RuntimeException("viewHolder is null");
        if (!mHeaderViews.contains(header)) {
            mHeaderViews.add(header);
            header.tryCreateView(context, null);
            hfItemInserted(header, notify);
        }
        return this;
    }

    private void hfItemInserted(ViewDelegates hf, boolean notify) {
        if (notify) {
            int index = indexOfHF(hf);
            if (index != -1)
                notifyItemInserted(index);
        }

    }

    @Override
    public IAdapter addHeaderHolder(@LayoutRes int layout) {
        return addHeaderHolder(new ResViewDelegates(layout));
    }

    @Override
    public IAdapter addHeaderHolder(@LayoutRes int layout, boolean notify) {
        return addHeaderHolder(new ResViewDelegates(layout), true);
    }

    @Override
    public IAdapter addHeaderHolder(ViewDelegates header) {
        return addHeaderHolder(header, false);
    }

    @Override
    public int indexOfHF(ViewDelegates item) {
        int index = mHeaderViews.indexOf(item);
        if (index != -1)
            return index;
        else {
            index = mFooterViews.indexOf(item);
            if (index != -1)
                return mHeaderViews.size() + data.size() + index;
            else
                return -1;
        }
    }

    @Override
    public IAdapter addFooterHolder(@LayoutRes int layout, boolean notify) {
        return addFooterHolder(new ResViewDelegates(layout), notify);
    }

    @Override
    public IAdapter addFooterHolder(ViewDelegates footer) {
        return addFooterHolder(footer, false);
    }

    @Override
    public IAdapter addFooterHolder(@LayoutRes int layout) {
        return addFooterHolder(new ResViewDelegates(layout));
    }


    @Override
    public IAdapter addFooterHolder(ViewDelegates footer, boolean notify) {
        if (footer == null)
            throw new RuntimeException("footer is null");
        if (!mFooterViews.contains(footer)) {
            mFooterViews.add(footer);
            footer.tryCreateView(context, null);
            hfItemInserted(footer, notify);
        }
        return this;
    }

    @Override
    public IAdapter removeHeaderHolder(ViewDelegates header, boolean notify) {
        return hfItemRemoved(mHeaderViews, header, notify);
    }

    private IAdapter hfItemRemoved(List<ViewDelegates> hfViews, ViewDelegates header, boolean notify) {
        if (notify) {
            int removeIndex = indexOfHF(header);
            hfViews.remove(header);
            if (removeIndex != -1)
                notifyItemRemoved(removeIndex);
        } else
            hfViews.remove(header);
        return this;
    }

    @Override
    public IAdapter removeHeaderHolder(ViewDelegates header) {
        return removeHeaderHolder(header, false);
    }

    @Override
    public IAdapter removeFooterHolder(ViewDelegates footer) {
        return removeFooterHolder(footer, false);
    }

    @Override
    public IAdapter removeFooterHolder(ViewDelegates footer, boolean notify) {
        return hfItemRemoved(mFooterViews, footer, notify);
    }

    @Override
    public IAdapter clearHeaderHolder() {
        mHeaderViews.clear();
        return this;
    }

    @Override
    public IAdapter clearFooterHolder() {
        mFooterViews.clear();
        return this;
    }

    @Override
    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    @Override
    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    @Override
    public boolean containHeaderHolder(ViewDelegates header) {
        return mHeaderViews.contains(header);
    }

    @Override
    public boolean containFooterHolder(ViewDelegates footer) {
        return mFooterViews.contains(footer);
    }

    //多类型 用此方法
    protected int getItemViewType2(int dataPosition) {
        return Wrapper.DEFAULT_VALUE;
    }

    @Override
    public IAdapter addEmptyHold(ViewDelegates emptyView) {
        mEmptyView = emptyView;
        return this;
    }

    @Override
    public IAdapter addEmptyHold(@LayoutRes int layout) {
        return addEmptyHold(new ResViewDelegates(layout));
    }

    private boolean isEmptyData() {
        return mEmptyView != null
                && mHeaderViews.size() == 0
                && mFooterViews.size() == 0
                && data.size() == 0;
    }

    @Override
    public void scrollToData(T o) {
        mRecyclerView.scrollToPosition(data.indexOf(o) + mHeaderViews.size());
    }

    @Override
    public void scrollToHF(ViewDelegates hf) {
        mRecyclerView.scrollToPosition(indexOfHF(hf));
    }

    @Override
    public void smoothScrollToData(T o) {
        mRecyclerView.smoothScrollToPosition(data.indexOf(o) + mHeaderViews.size());
    }

    @Override
    public void smoothScrollToHF(ViewDelegates hf) {
        mRecyclerView.smoothScrollToPosition(indexOfHF(hf));
    }

    @Override
    public void scrollToPosition(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void scrollToLast() {
        mRecyclerView.scrollToPosition(getRealItemCount() - 1);
    }

    @Override
    public void smoothScrollToLast() {
        mRecyclerView.smoothScrollToPosition(getRealItemCount() - 1);
    }

    @Override
    public IAdapter addItemDecoration(int space) {
        return addItemDecoration(new MarginItemDecoration(space, this));
    }

    @Override
    public IAdapter addItemDecoration(MarginItemDecoration itemDecoration) {
        if (mRecyclerView == null)
            throw new IllegalStateException("please first use relatedList(RecyclerView mRecyclerView)!");
        mRecyclerView.addItemDecoration(itemDecoration);
        return this;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public List<Wrapper> getDataWraps() {
        return mViews;
    }
}
