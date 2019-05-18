package com.zone.adapter3kt.holder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.SparseArray
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Checkable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.zone.adapter.R

/**
 * Allows an abstraction of the ViewHolder pattern.<br></br>
 * **Usage**
 * return T.getView(mContext, convertView, parent, R.layout.item)
 * .setText(R.id.tvName, contact.getName())
 * .setText(R.id.tvEmails, contact.getEmails().toString())
 * .setText(R.id.tvNumbers, contact.getNumbers().toString())
 * .getView();
 */
abstract class BaseHolder<out T : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.ViewHolder {

    abstract fun getReturnValue(): T
    // =======================================
    // ============ 内部使用属性  ==============
    // =======================================

    //Views indexed with their IDs
    private val views: SparseArray<View> by lazy { SparseArray<View>() }
    protected var mContext :Context
    val tagMaps: HashMap<Any, Any> by lazy { HashMap<Any, Any>() }

    constructor(view: View) : super(view) {
        mContext = view.context
    }

    constructor(baseHolder: BaseHolder<*>) : super(baseHolder.itemView) {
        mContext = baseHolder.mContext
        for(i in 0..baseHolder.views.size()){
            val keyAt = baseHolder.views.keyAt(i)
            views.put(keyAt,baseHolder.views[keyAt])
        }
    }

    // =======================================
    // ============ 方法  =====================
    // =======================================

    fun setText(@IdRes viewId: Int, value: String): T {
        val view = retrieveView<TextView>(viewId)
        view.text = value
        return getReturnValue()
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): T {
        val view = retrieveView<ImageView>(viewId)
        view.setImageResource(imageResId)
        return getReturnValue()
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): T {
        val view = retrieveView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return getReturnValue()
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): T {
        val view = retrieveView<ImageView>(viewId)
        view.setImageBitmap(bitmap)
        return getReturnValue()
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): T {
        val view = retrieveView<View>(viewId)
        view.setBackgroundColor(color)
        return getReturnValue()
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): T {
        val view = retrieveView<View>(viewId)
        view.setBackgroundResource(backgroundRes)
        return getReturnValue()
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): T {
        val view = retrieveView<TextView>(viewId)
        view.setTextColor(textColor)
        return getReturnValue()
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes textColorRes: Int): T {
        val view = retrieveView<TextView>(viewId)
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return getReturnValue()
    }


    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    fun setAlpha(@IdRes viewId: Int, value: Float): T {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView<View>(viewId).alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            retrieveView<View>(viewId).startAnimation(alpha)
        }
        return getReturnValue()
    }

    /**
     * @param viewId
     * @param visibility @param visibility One of [View.VISIBLE],
     * *                   [View.INVISIBLE], or [View.GONE].
     * @return
     */
    fun setVisible(@IdRes viewId: Int, visibility: Int): T {
        val view = retrieveView<View>(viewId)
        view.visibility = visibility
        return getReturnValue()
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    fun setTypeface(@IdRes viewId: Int, typeface: Typeface): T {
        val view = retrieveView<TextView>(viewId)
        view.typeface = typeface
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        return getReturnValue()
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    fun setTypeface(typeface: Typeface, vararg viewIds: Int): T {
        for (viewId in viewIds) {
            val view = retrieveView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return getReturnValue()
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): T {
        val view = retrieveView<ProgressBar>(viewId)
        view.progress = progress
        return getReturnValue()
    }

    /**
     * Sets the progress and max of a ProgressBar.
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The T for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): T {
        val view = retrieveView<ProgressBar>(viewId)
        view.max = max
        view.progress = progress
        return getReturnValue()
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     * @param max    The max value of a ProgressBar.
     * @return The T for chaining.
     */
    fun setMax(@IdRes viewId: Int, max: Int): T {
        val view = retrieveView<ProgressBar>(viewId)
        view.max = max
        return getReturnValue()
    }


    /**
     * Sets the useage of the view.
     * @param tag    The useage;
     * @return The T for chaining.
     */
    fun setTag(@IdRes viewId: Int, tag: Any): T {
        val view = retrieveView<View>(viewId)
        view.tag = tag
        return getReturnValue()
    }

    /**
     * Sets the useage of the view.
     * @param viewId The view id.
     * @param tag    The useage;
     * @return The T for chaining.
     */
    fun setTag(@IdRes viewId: Int, key: Int, tag: Any): T {
        val view = retrieveView<View>(viewId)
        view.setTag(key, tag)
        return getReturnValue()
    }

    fun setClickTag(@IdRes viewId: Int, tag: Any): T {
        val view = retrieveView<View>(viewId)
        view.setTag(R.id.OnClickListener_KEY, tag)
        return getReturnValue()
    }

    fun getClickTag(@IdRes viewId: Int): Any? =
        retrieveView<View>(viewId).getTag(R.id.OnClickListener_KEY)

    /**
     * Sets the checked status of a checkable.
     * @param checked The checked status;
     * @return The T for chaining.
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): T {
        val view = retrieveView<View>(viewId) as Checkable
        view.isChecked = checked
        return getReturnValue()
    }

    fun setHtml(@IdRes viewId: Int, source: String): T {
        val view = getView<TextView>(viewId)
        view.text = Html.fromHtml(source)
        return getReturnValue()
    }


    /**
     * 设置监听器
     * @param l   监听器
     * @param ids view 的 id
     */
    fun setOnClickListener(l: View.OnClickListener?, @IdRes vararg ids: Int): T {
        for (id in ids)
            getView<View>(id).setOnClickListener(l)
        return getReturnValue()
    }

    /**
     * 设置监听器
     * @param l   监听器
     * @param ids view 的 id
     */
    fun setOnHolderClickListener(l: HolderClickListener, @IdRes vararg ids: Int): T {
        for (id in ids)
            getView<View>(id).setOnClickListener {
                l.onClick(it, getReturnValue() as BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>)
            }
        return getReturnValue()
    }


    // =======================================
    // ============ 快捷方法  ==============
    // =======================================
    fun <V : View> getView(@IdRes viewId: Int): V {
        return retrieveView<View>(viewId) as V
    }

    protected fun <V : View> retrieveView(@IdRes viewId: Int): V {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = this.itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as V
    }

    fun clear(): T {
        views.clear()
        return getReturnValue()
    }

}