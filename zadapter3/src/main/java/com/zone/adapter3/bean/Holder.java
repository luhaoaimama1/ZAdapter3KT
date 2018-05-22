package com.zone.adapter3.bean;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Allows an abstraction of the ViewHolder pattern.<br>
 * <br>
 * <b>Usage</b>
 * <pre>
 * return T.getView(context, convertView, parent, R.layout.item)
 *         .setText(R.id.tvName, contact.getName())
 *         .setText(R.id.tvEmails, contact.getEmails().toString())
 *         .setText(R.id.tvNumbers, contact.getNumbers().toString())
 *         .getView();
 * </pre>
 * Created by Administrator on 2016/3/27.
 */
public class Holder<T extends Holder<T>> extends RecyclerView.ViewHolder {

    private ViewDelegates viewDelegates;

    private List<Object> payloads;

    public ViewDelegates getViewDelegates() {
        return viewDelegates;
    }

    public void setViewDelegates(ViewDelegates viewDelegates) {
        this.viewDelegates = viewDelegates;
    }

    public List<Object> getPayloads() {
        return payloads;
    }

    public T setPayloads(List<Object> payloads) {
        this.payloads = payloads;
        return (T) this;
    }


    // =======================================
    // ============ 快捷扩展方法  ==============
    // =======================================


    /**
     * Views indexed with their IDs
     */
    private SparseArray<View> views;
    private View convertView;
    protected Context context;

    /**
     * 用于  其他工具类使用,与header 与footer
     *
     * @param view
     */
    public Holder(View view) {
        super(view);
        this.views = new SparseArray<>();
        context = view.getContext();
        convertView = view;
    }


    public T setText(@IdRes int viewId, String value) {
        TextView view = retrieveView(viewId);
        view.setText(value);
        return (T) this;
    }

    public T setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return (T) this;
    }

    public T setImageDrawable(@IdRes int viewId, @Nullable Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return (T) this;
    }

    public T setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);

        return (T) this;
    }

    public T setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);

        return (T) this;
    }

    public T setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);

        return (T) this;
    }

    public T setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);

        return (T) this;
    }

    public T setTextColorRes(@IdRes int viewId, @ColorRes int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(context.getResources().getColor(textColorRes));
        return (T) this;
    }


    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public T setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return (T) this;
    }

    /**
     * @param viewId
     * @param visibility @param visibility One of {@link View#VISIBLE},
     *                   {@link  View#INVISIBLE}, or {@link View#GONE}.
     * @return
     */
    public T setVisible(@IdRes int viewId, int visibility) {
        View view = retrieveView(viewId);
        view.setVisibility(visibility);
        return (T) this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public T setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

        return (T) this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public T setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return (T) this;
    }

    public T setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return (T) this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The T for chaining.
     */
    public T setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return (T) this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The T for chaining.
     */
    public T setMax(@IdRes int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return (T) this;
    }


    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The T for chaining.
     */
    public T setTag(@IdRes int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return (T) this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The T for chaining.
     */
    public T setTag(@IdRes int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);

        return (T) this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The T for chaining.
     */
    public T setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = (Checkable) retrieveView(viewId);
        view.setChecked(checked);

        return (T) this;
    }

    public T setHtml(@IdRes int viewId, String source) {
        TextView view = getView(viewId);
        view.setText(Html.fromHtml(source));
        return (T) this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The T for chaining.
     */
    public T setAdapter(@IdRes int viewId, Adapter adapter) {
        AdapterView view = retrieveView(viewId);
        view.setAdapter(adapter);
        return (T) this;
    }

    /**
     * 设置监听器
     *
     * @param l   监听器
     * @param ids view 的 id
     */
    public T setOnClickListener(View.OnClickListener l, int... ids) {
        if (ids == null)
            return (T) this;
        for (int id : ids)
            getView(id).setOnClickListener(l);
        return (T) this;
    }

    /**
     * Retrieve the convertView
     */
    public View getView() {
        return convertView;
    }

    // =======================================
    // ============ 快捷方法  ==============
    // =======================================
    public <V extends View> V getView(int viewId) {
        return (V) retrieveView(viewId);
    }

    protected <V extends View> V retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (V) view;
    }

    public T restore() {
        views.clear();
        return (T) this;
    }


}