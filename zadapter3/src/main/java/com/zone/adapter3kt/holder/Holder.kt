package com.zone.adapter3kt.holder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.SparseArray
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Checkable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Allows an abstraction of the ViewHolder pattern.<br></br>
 * **Usage**
 * return T.getView(context, convertView, parent, R.layout.item)
 * .setText(R.id.tvName, contact.getName())
 * .setText(R.id.tvEmails, contact.getEmails().toString())
 * .setText(R.id.tvNumbers, contact.getNumbers().toString())
 * .getView();
 */
open class Holder(val view: View) : RecyclerView.ViewHolder(view) {

    // =======================================
    // ============ 内部使用属性  ==============
    // =======================================

    //Views indexed with their IDs
    val views: SparseArray<View> by lazy { SparseArray<View>() }
    lateinit var context: Context
    init {
        context = view.context
    }

    // =======================================
    // ============ 方法  =====================
    // =======================================

    fun setText(@IdRes viewId: Int, value: String): Holder {
        val view = retrieveView<TextView>(viewId)
        view?.text = value
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): Holder {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageResource(imageResId)
        return this
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): Holder {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageDrawable(drawable)
        return this
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): Holder {
        val view = retrieveView<ImageView>(viewId)
        view?.setImageBitmap(bitmap)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): Holder {
        val view = retrieveView<View>(viewId)
        view?.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): Holder {
        val view = retrieveView<View>(viewId)
        view?.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): Holder {
        val view = retrieveView<TextView>(viewId)
        view?.setTextColor(textColor)

        return this
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes textColorRes: Int): Holder {
        val view = retrieveView<TextView>(viewId)
        if (context != null) view?.setTextColor(context!!.resources.getColor(textColorRes))
        return this
    }


    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    fun setAlpha(@IdRes viewId: Int, value: Float): Holder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView<View>(viewId)?.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            retrieveView<View>(viewId)?.startAnimation(alpha)
        }
        return this
    }

    /**
     * @param viewId
     * @param visibility @param visibility One of [View.VISIBLE],
     * *                   [View.INVISIBLE], or [View.GONE].
     * @return
     */
    fun setVisible(@IdRes viewId: Int, visibility: Int): Holder {
        val view = retrieveView<View>(viewId)
        view?.visibility = visibility
        return this
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    fun setTypeface(@IdRes viewId: Int, typeface: Typeface): Holder {
        val view = retrieveView<TextView>(viewId)
        if (view != null) {
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    fun setTypeface(typeface: Typeface, vararg viewIds: Int): Holder {
        for (viewId in viewIds) {
            val view = retrieveView<TextView>(viewId)
            if (view != null) {
                view.typeface = typeface
                view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): Holder {
        val view = retrieveView<ProgressBar>(viewId)
        view?.progress = progress
        return this
    }

    /**
     * Sets the progress and max of a ProgressBar.
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The T for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): Holder {
        val view = retrieveView<ProgressBar>(viewId)
        view?.max = max
        view?.progress = progress
        return this
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     * @param max    The max value of a ProgressBar.
     * @return The T for chaining.
     */
    fun setMax(@IdRes viewId: Int, max: Int): Holder {
        val view = retrieveView<ProgressBar>(viewId)
        view?.max = max
        return this
    }


    /**
     * Sets the userage of the view.
     * @param tag    The userage;
     * @return The T for chaining.
     */
    fun setTag(@IdRes viewId: Int, tag: Any): Holder {
        val view = retrieveView<View>(viewId)
        view?.tag = tag
        return this
    }

    /**
     * Sets the userage of the view.
     * @param viewId The view id.
     * @param tag    The userage;
     * @return The T for chaining.
     */
    fun setTag(@IdRes viewId: Int, key: Int, tag: Any): Holder {
        val view = retrieveView<View>(viewId)
        view?.setTag(key, tag)

        return this
    }

    /**
     * Sets the checked status of a checkable.
     * @param checked The checked status;
     * @return The T for chaining.
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): Holder {
        val view = retrieveView<View>(viewId) as Checkable
        view.isChecked = checked

        return this
    }

    fun setHtml(@IdRes viewId: Int, source: String): Holder {
        val view = getView<TextView>(viewId)
        view.text = Html.fromHtml(source)
        return this
    }


    /**
     * 设置监听器
     * @param l   监听器
     * @param ids view 的 id
     */
    fun setOnClickListener(l: View.OnClickListener?, vararg ids: Int): Holder {
        for (id in ids)
            getView<View>(id).setOnClickListener(l)
        return this
    }

    // =======================================
    // ============ 快捷方法  ==============
    // =======================================
    fun <V : View> getView(@IdRes viewId: Int): V {
        return retrieveView<View>(viewId) as V
    }

    protected fun <V : View> retrieveView(@IdRes viewId: Int): V? {
        var view: View? = views?.get(viewId)
        if (view == null) {
            view = this.view.findViewById(viewId)
            views?.put(viewId, view)
        }
        return view as V?
    }

    fun clear(): Holder {
        views?.clear()
        return this
    }

}