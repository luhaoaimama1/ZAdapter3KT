//package com.zone.adapter3kt.holder
//
//import android.graphics.Bitmap
//import android.graphics.Typeface
//import android.graphics.drawable.Drawable
//import android.support.annotation.ColorInt
//import android.support.annotation.ColorRes
//import android.support.annotation.DrawableRes
//import android.support.annotation.IdRes
//import android.view.View
//
///**
// * Copyright (c) 2018 BiliBili Inc.
// *[2018/9/14] by Zone
// */
//interface IHolder<B> {
//    fun setText(viewId: Int, value: String): B
//    fun setImageResource( viewId: Int,  imageResId: Int): B
//    fun setImageDrawable( viewId: Int, drawable: Drawable?): B
//    fun setImageBitmap( viewId: Int, bitmap: Bitmap): B
//    fun setBackgroundColor( viewId: Int,  color: Int): B
//    fun setBackgroundRes( viewId: Int,  backgroundRes: Int): B
//    fun setTextColor( viewId: Int,  textColor: Int): B
//    fun setTextColorRes( viewId: Int,  textColorRes: Int): B
//    fun setAlpha( viewId: Int, value: Float): B
//    fun setVisible( viewId: Int, visibility: Int): B
//    fun setTypeface( viewId: Int, typeface: Typeface): B
//    fun setTypeface(typeface: Typeface, vararg viewIds: Int): B
//    fun setProgress( viewId: Int, progress: Int): B
//    fun setProgress( viewId: Int, progress: Int, max: Int): B
//    fun setMax( viewId: Int, max: Int): B
//    fun setTag( viewId: Int, tag: Any): B
//    fun setTag( viewId: Int, key: Int, tag: Any): B
//    fun setChecked( viewId: Int, checked: Boolean): B
//    fun setHtml( viewId: Int, source: String): B
//    fun setOnClickListener(l: View.OnClickListener?, vararg ids: Int): B
//    fun <V : View> getView( viewId: Int): V
//    fun clear(): B
//}