package com.brucetoo.yingke;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Bruce too
 * On 2016/2/26
 * At 17:44
 * //https://github.com/kaelaela/VerticalViewPager/blob/master/library/src/main/java/me/kaelaela/verticalviewpager/VerticalViewPager.java
 */
public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new ViewPagerTransformer());
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        //将x坐标 根据原始 y的 高度比(event.getY() / height) * 原先的宽度 = 现在x占宽度的比例
        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        //设置点击事件的位置
        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
        //If not intercept, touch event should not be swapped.
        swapTouchEvent(event);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }
}
