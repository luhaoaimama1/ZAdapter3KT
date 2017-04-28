package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ResViewDelegates;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.bean.Wrapper;
import com.zone.adapter3.helper.Helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 * <p>
 * 多类型 使用{@link #getItemViewType2(int)}
 */
public abstract class Header2FooterRcvAdapter<T> extends BaseRcvAdapter<T> {

    private static int HEADER_TYPE = -1000;
    private static int FOOTER_TYPE = -2000;

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
        if (viewType <= HEADER_TYPE && viewType > HEADER_TYPE - 1000) {
            QuickConfig.e("onCreateViewHolder header:" + viewType);

//            if (mHeaderViews.get(HEADER_TYPE - viewType).getItemView() == null)
            mHeaderViews.get(HEADER_TYPE - viewType).tryCreateView(context, mRecyclerView);

            return new Holder(setFullspan(mHeaderViews.get(HEADER_TYPE - viewType).getItemView()));
        } else if (viewType <= FOOTER_TYPE && viewType > FOOTER_TYPE - 1000) {
            QuickConfig.e("onCreateViewHolder footer:" + viewType);

//            if (mFooterViews.get(FOOTER_TYPE - viewType).getItemView() == null)
            mFooterViews.get(FOOTER_TYPE - viewType).tryCreateView(context, mRecyclerView);
            return new Holder(setFullspan(mFooterViews.get(FOOTER_TYPE - viewType).getItemView()));
        } else if (viewType == Wrapper.EMPTY_VALUE) {
            QuickConfig.e("onCreateViewHolder Empty:" + viewType);
            mEmptyView.tryCreateView(context, mRecyclerView);
            return new Holder(setFullspan(mEmptyView.getItemView()));
        } else {
            //Viewtype is getitemviewtype (position ID) int
            QuickConfig.e("onCreateViewHolder views:" + viewType + " \t HEADER_TYPE-1000"
                    + (HEADER_TYPE - 1000) + " \t HEADER_TYPE" + HEADER_TYPE);
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
            targetWarpper.getViewDelegates().reallyCreateView(context, mRecyclerView);
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
        }
    }

    private int getDataPosition(int mapPosition) {
        return mapPosition - mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        return data.size() + mHeaderViews.size() + mFooterViews.size() + (isEmptyData() ? 1 : 0);
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
        if (position < getHeaderViewsCount()) {
            //头部id -1000 --> -2000
            QuickConfig.e("getItemViewType head:" + position);
            return HEADER_TYPE - position;
        } else if (position >= getHeaderViewsCount() && position < getHeaderViewsCount() + data.size()) {
            QuickConfig.e("getItemViewType ivews:" + position);
            int result = getItemViewType2(getDataPosition(position));
            if (result < HEADER_TYPE && result > FOOTER_TYPE - 1000)
                throw new IllegalStateException("layoutType is viewHolder or footer type");
            return result;
        } else {
            //底id -2000 --> -3000
            QuickConfig.e("getItemViewType footer:" + position);
            return FOOTER_TYPE - (position - getHeaderViewsCount() - data.size());
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
                            + data.size())
                        return 1;
                    else
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
        mViews.add(new Wrapper(Wrapper.DEFAULT_VALUE, viewDelegates));
        return this;
    }

    List<Integer> repeatCheckList = new ArrayList<>();

    @Override
    public IAdapter addViewHolder(int style, ViewDelegates viewDelegates) {
        if (Wrapper.DEFAULT_VALUE == style
                || Wrapper.EMPTY_VALUE == style
                || (style <= HEADER_TYPE && style > FOOTER_TYPE - 1000))
            throw new IllegalStateException("style不能为关键的值-1,-2,-1000到-3000");
        if (!repeatCheckList.contains(style))
            repeatCheckList.add(style);
        else
            throw new IllegalStateException("style重复");
        mViews.add(new Wrapper(style, viewDelegates));
        return this;
    }

    @Override
    public IAdapter addHeaderHolder(ViewDelegates header) {
        if (header == null)
            throw new RuntimeException("viewHolder is null");
        if (!mHeaderViews.contains(header))
            mHeaderViews.add(header);
        return this;
    }

    @Override
    public IAdapter addHeaderHolder(@LayoutRes int layout) {
        return addHeaderHolder(new ResViewDelegates(layout));
    }

    @Override
    public IAdapter addFooterHolder(@LayoutRes int layout) {
        return addFooterHolder(new ResViewDelegates(layout));
    }

    @Override
    public IAdapter addFooterHolder(ViewDelegates footer) {
        if (footer == null)
            throw new RuntimeException("footer is null");
        if (!mFooterViews.contains(footer))
            mFooterViews.add(footer);
        return this;
    }

    @Override
    public IAdapter removeHeaderHolder(ViewDelegates header) {
        mHeaderViews.remove(header);
        return this;
    }

    @Override
    public IAdapter removeFooterHolder(ViewDelegates footer) {
        mFooterViews.remove(footer);
        return this;
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
}
