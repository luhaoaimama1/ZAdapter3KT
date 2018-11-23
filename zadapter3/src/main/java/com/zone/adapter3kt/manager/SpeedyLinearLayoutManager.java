/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-04-10 20:16:42
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.zone.adapter3kt.manager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 支持快速返回的 LinerLayoutManager
 * http://blog.csdn.net/a86261566/article/details/50906456
 *
 * https://blog.csdn.net/u014099894/article/details/51855129
 */
public class SpeedyLinearLayoutManager extends LinearLayoutManager {

    private float milliseconds_per_inch = 6f; //default is 25f (bigger = slower)
    private final Context context;

    public SpeedyLinearLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    public SpeedyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.context = context;
    }

    public SpeedyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int
            position) {

        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView
                .getContext()) {

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return SpeedyLinearLayoutManager.this.computeScrollVectorForPosition
                        (targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                //返回滑动一个pixel需要多少毫秒
                return milliseconds_per_inch / displayMetrics.densityDpi;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //todo 0.3f是自己估摸的一个值，可以根据不同需求自己修改
        milliseconds_per_inch = context.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        milliseconds_per_inch = context.getResources().getDisplayMetrics().density * 0.03f;
    }
}