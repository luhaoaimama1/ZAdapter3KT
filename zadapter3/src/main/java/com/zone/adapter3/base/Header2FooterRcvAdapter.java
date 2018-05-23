package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zone.adapter.R;
import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.absorb.StickyOnScrollListener;
import com.zone.adapter3.bean.EHFViewDelegates;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ResViewDelegates;
import com.zone.adapter3.bean.StickyViewDelegates;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.bean.Wrapper;
import com.zone.adapter3.decoration.MarginItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 * <p>
 * 多类型 使用{@link #getItemViewType2(int)}
 */
public abstract class Header2FooterRcvAdapter<T> extends BaseRcvAdapter<T> {

    public static final int DEFAULT_VALUE = -1;
    public static final int EMPTY_VALUE = -2;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -3;
    public static final int STICKY_VALUE = -4;

    //Limit one thousand
    private List<EHFViewDelegates> mHeaderViews = new ArrayList<>();
    private List<EHFViewDelegates> mFooterViews = new ArrayList<>();
    private EHFViewDelegates mEmptyView;
    //no limit;
    private List<Wrapper> mViews = new ArrayList<>();

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager manager;

    public Header2FooterRcvAdapter(Context context, List<T> data) {
        super(context, data);
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Holder holder;
        if (viewType == ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
            QuickConfig.e("onCreateViewHolder header or footer :" + viewType);
            holder = new Holder(setFullspan(LayoutInflater.from(context)
                    .inflate(R.layout.base_vp, mRecyclerView, false)));
        } else if (viewType == EMPTY_VALUE) {
            QuickConfig.e("onCreateViewHolder Empty:" + viewType);
            holder = mEmptyView.tryEHFCreateView(context, this);
            setFullspan(holder.itemView);
        } else if (viewType == STICKY_VALUE) {
            QuickConfig.e("onCreateViewHolder Sticky :" + viewType);
            holder = new Holder(setFullspan(LayoutInflater.from(context)
                    .inflate(R.layout.base_vp, mRecyclerView, false)));
        } else {
            QuickConfig.e("onCreateViewHolder views:" + viewType);
            Wrapper targetWarpper = getWrapper(viewType);
            //这里 必须创建 而不能try try的含义是 仅仅能创建一次 用于头和脚  因为我是通过 ViewDelegates的类型去创建的
            holder = getContentHolder(targetWarpper, targetWarpper.getViewDelegates());

        }
        return holder;
    }

    @NonNull
    private Wrapper getWrapper(int viewType) {
        Wrapper targetWarpper = null;
        for (Wrapper mView : mViews) {
            if (mView.getStyle() == DEFAULT_VALUE) {
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
        return targetWarpper;
    }

    protected Holder getContentHolder(Wrapper targetWarpper, ViewDelegates viewDelegates) {
        Holder holder;
        holder = viewDelegates.reallyCreateView(context, this);
        holder.setViewDelegates(viewDelegates);
        if (targetWarpper.getViewDelegates().isFullspan())
            setFullspan(holder.itemView);
        return holder;
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
    public void onBindViewHolder(Holder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder.setPayloads(payloads), position, payloads);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (isEmptyData()) {
            mEmptyView.fillData(position, null, holder);
            return;
        }

        //sticky 判断优先
        if(stickyPostions!=null){
            for (int i = 0; i < stickyPostions.length; i++) {
                if (stickyPostions[i] == position) {
                    //todo
                    if (mStickyViews[i] == null)
                        mStickyViews[i] = findStickyDelegates(position);

                    mStickyViews[i].setParentHolder(holder);
                    QuickConfig.e("bind Sticky position:" + position);
                    bindStickyView(position, holder, mStickyViews[i], getFinalDataByPos(position));
                    return;
                }
            }
        }

        if (position >= mHeaderViews.size() && position < mHeaderViews.size() + getContentCount()) {
            if (holder.getViewDelegates() != null)
                holder.getViewDelegates().fillData(position, getFinalDataByPos(position), holder);
        } else if (position < mHeaderViews.size()) {
            QuickConfig.e("bind header position:" + position);
            bindHFView(position, holder, mHeaderViews.get(position));
        } else {
            QuickConfig.e("bind footer position:" + position);
            bindHFView(position, holder, mFooterViews.get(position - getHeaderViewsCount() - getContentCount()));
        }
    }

    private T getFinalDataByPos(int position) {
        T obj = null;
        if (contentDataMapListener != null)
            obj = contentDataMapListener.getData(data, getDataPosition(position));
        else
            obj = data.get(getDataPosition(position));
        return obj;
    }

    private int[] stickyPostions;
    //我的通过Postion找到原始的EHFViewDelegates 然后clone一份 去try
    private StickyViewDelegates[] mStickyViews;

    /**
     * @return layoutid
     * 因为getItemViewType不同 导致 头部底部view不会被重用!
     */
    public StickyViewDelegates findStickyDelegates(int position) {
        StickyViewDelegates stickyDelegates;

        if (position >= mHeaderViews.size() && position < mHeaderViews.size() + getContentCount()) {
            Wrapper wrapper = getWrapper(getItemViewType2(getDataPosition(position)));
            stickyDelegates = wrapper.getViewDelegates();

        } else if (position < mHeaderViews.size()) {
            QuickConfig.e("bind header position:" + position);
            stickyDelegates = (StickyViewDelegates) mHeaderViews.get(position);
        } else {
            QuickConfig.e("bind footer position:" + position);
            stickyDelegates = (StickyViewDelegates) mFooterViews.get(position - getHeaderViewsCount() - getContentCount());
        }
        try {
            stickyDelegates = (StickyViewDelegates) stickyDelegates.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException("不支持clone!");
        }
        stickyDelegates.tryEHFCreateView(context, this);
        return stickyDelegates;
    }

    StickyOnScrollListener stickyOnScrollListener;

    @Override
    public IAdapter addSticky(FrameLayout vpShow, int... stickyPostions) {
        this.stickyPostions = stickyPostions;
        mStickyViews = new StickyViewDelegates[stickyPostions.length];
        if (stickyOnScrollListener != null)
            getRecyclerView().removeOnScrollListener(stickyOnScrollListener);
        getRecyclerView().addOnScrollListener(stickyOnScrollListener = new StickyOnScrollListener(vpShow, stickyPostions, mStickyViews));
        return this;
    }

    @Override
    public IAdapter addSticky(@ColorInt int color, FrameLayout vpShow, int... stickyPostions) {
        addSticky(vpShow,stickyPostions);
        stickyOnScrollListener.setPlaceColor(color);
        return this;
    }

    /**
     * 仅仅初始化 的时候是他 ,剩下的操作都用滚动来 控制 add和remove。
     * 但是呢  本身的其他 还需要用到它的Holder去add和remove;
     * @param position
     * @param holder
     * @param viewDelegates
     * @param data
     */
    private void bindStickyView(int position, Holder holder, StickyViewDelegates viewDelegates, T data) {
        ViewGroup parent = (ViewGroup) holder.itemView;
        View child;
        if(viewDelegates.getPlaceholderView()!=null)
            child = viewDelegates.getPlaceholderView();
        else
            child = viewDelegates.getEHFHolder().itemView;
        parent.removeAllViews();
        ViewGroup vp = (ViewGroup) child.getParent();
        if (vp != null)
            vp.removeAllViews();
        parent.addView(child);
        viewDelegates.fillData(position, data, holder);
    }
    private void bindHFView(int position, Holder holder, EHFViewDelegates viewDelegates) {
        ViewGroup parent = (ViewGroup) holder.itemView;
        View child = viewDelegates.getEHFHolder().itemView;
        parent.removeAllViews();
        ViewGroup vp = (ViewGroup) child.getParent();
        if (vp != null)
            vp.removeAllViews();
        parent.addView(child);
        viewDelegates.fillData(position, data, holder);
    }

    private int getDataPosition(int mapPosition) {
        return mapPosition - mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        return getCHFItemCount() + (isEmptyData() ? 1 : 0);
    }

    @Override
    public int getCHFItemCount() {
        return getContentCount() + mHeaderViews.size() + mFooterViews.size();
    }


    @Override
    public int getContentCount() {
        if (contentDataMapListener != null)
            return contentDataMapListener.getContentCount(data);
        return data.size();
    }

    ContentDataMapListener contentDataMapListener = new ContentDataMapListener() {
        @Override
        public <T> int getContentCount(List<T> datas) {
            return datas.size();
        }

        @Override
        public <T> T getData(List<T> datas, int position) {
            return datas.get(position);
        }
    };

    @Override
    public IAdapter setContentDataMapListener(ContentDataMapListener contentDataMapListener) {
        this.contentDataMapListener = contentDataMapListener;
        return this;
    }

    public interface ContentDataMapListener {
        <T> int getContentCount(List<T> datas);

        <T> T getData(List<T> datas, int position);
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
            return EMPTY_VALUE;
        }


        //sticky 检查优先
        if(stickyPostions!=null){
            for (int stickyPostion : stickyPostions) {
                if (position == stickyPostion)
                    return STICKY_VALUE;
            }
        }

        if (position >= getHeaderViewsCount() && position < getHeaderViewsCount() + getContentCount()) {
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
                            + getContentCount()) {
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
        if (mViews.size() == 0 || mViews.get(0).getStyle() != DEFAULT_VALUE)
            mViews.add(0, new Wrapper(DEFAULT_VALUE, viewDelegates));
        else
            mViews.set(0, new Wrapper(DEFAULT_VALUE, viewDelegates));
        return this;
    }

    List<Integer> repeatCheckList = new ArrayList<>();

    @Override
    public IAdapter addViewHolder(int style, ViewDelegates viewDelegates) {
        if (DEFAULT_VALUE == style
                || EMPTY_VALUE == style
                || style == ITEM_VIEW_TYPE_HEADER_OR_FOOTER)
            throw new IllegalStateException(String.format("style不能为内设的值:%d,%d,%d"
                    , DEFAULT_VALUE, EMPTY_VALUE, ITEM_VIEW_TYPE_HEADER_OR_FOOTER));
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
            header.tryEHFCreateView(context, this);
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
                return mHeaderViews.size() + getContentCount() + index;
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
            //todo?
            footer.tryEHFCreateView(context, this);
            hfItemInserted(footer, notify);
        }
        return this;
    }

    @Override
    public IAdapter removeHeaderHolder(ViewDelegates header, boolean notify) {
        return hfItemRemoved(mHeaderViews, header, notify);
    }

    private IAdapter hfItemRemoved(List<EHFViewDelegates> hfViews, ViewDelegates header, boolean notify) {
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
        return DEFAULT_VALUE;
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
                && getContentCount() == 0;
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
        mRecyclerView.scrollToPosition(getCHFItemCount() - 1);
    }

    @Override
    public void smoothScrollToLast() {
        mRecyclerView.smoothScrollToPosition(getCHFItemCount() - 1);
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
