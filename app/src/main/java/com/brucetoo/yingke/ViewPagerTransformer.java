package com.brucetoo.yingke;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Bruce too
 * On 2016/2/26
 * At 17:45
 */
public class ViewPagerTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
//        float alpha = 0;
//        if (0 <= position && position <= 1) {
//            alpha = 1 - position;
//        } else if (-1 < position && position < 0) {
//            alpha = position + 1;
//        }
//        view.setAlpha(alpha);
        /**
         * -----    -----   -----
         * |   |    |   |   |   |
         * |1  |    |2  |   |3  |
         * |   |    |   |   |   |
         * -----    -----   -----
         * -----
           |   |
           |2  |
           |   |
           -----
         * 要从横向变成纵向的变化
         * 1.当前View的 x值 左移view.getWidth * position
         * 2.y值 下移 position * view.getHeight()
         */
        view.setTranslationX(view.getWidth() * - position);
        float yPosition = position * view.getHeight();
        view.setTranslationY(yPosition);
    }
}
