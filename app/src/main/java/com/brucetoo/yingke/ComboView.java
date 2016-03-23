package com.brucetoo.yingke;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Bruce too
 * On 2016/3/23
 * At 9:13
 */
public class ComboView extends View {

    public ComboView(Context context) {
        super(context);
        init();
    }

    public ComboView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ComboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ComboView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint;
    private TextPaint textPaint;
    private int mHeight;
    private int mWidth;
    private float mRectangleRadius = 40;
    private RectF mRectangleRect;
    private float centerX;
    private float centerY;
    private float circleRadius;
    private float currentOffset;
    private float strokePadding = 7;
    private String textPre = "赠送";
    private String textAfter = "连击";
    ObjectAnimator animator;
    boolean isReverse;
    boolean canClick = true;
    ObjectAnimator arcAnimator;
    private boolean arcAnimEnd = false;
    private float currentSweep;
    boolean drawRipple;
    float rippleRadius;


    private void init() {

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ff0000"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);//文字大小..
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        circleRadius = centerY - strokePadding;
        currentOffset = circleRadius;
    }

    RectF rectF;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentOffset == 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        String text = currentOffset == 0 ? textAfter : textPre;
        float textWidth = textPaint.measureText(text);
        float textCenterVerticalBaselineY = mHeight / 2 - fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
        if (currentOffset != 0) {
            canvas.drawRoundRect(createRectangleRect(), mRectangleRadius, mRectangleRadius, mPaint);
            canvas.drawText(text, centerX - textWidth / 2, textCenterVerticalBaselineY, textPaint);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centerX, centerY, circleRadius, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);
            canvas.drawArc(createRectF(), -90, currentSweep, false, mPaint);
            canvas.drawText(text, centerX - textWidth / 2, textCenterVerticalBaselineY, textPaint);
        }
    }

    private RectF createRectF() {
        rectF = new RectF(centerX - circleRadius - strokePadding, centerY - circleRadius - strokePadding + 1,
                centerX + circleRadius + strokePadding, centerY + circleRadius + strokePadding -1 );
        return rectF;
    }

    private RectF createRectangleRect() {
        mRectangleRect = new RectF(centerX - circleRadius - currentOffset, 0, centerX + currentOffset + circleRadius, mHeight);
        return mRectangleRect;
    }

    public void toggleMorph() {
        if (canClick) {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(this, "currentOffset", circleRadius, 0);
                animator.setDuration(500);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isReverse) {
                            startDrawArc();
                        }
                        canClick = true;
                    }
                });
                isReverse = false;
                canClick = true;
                animator.start();
            } else {
                if (arcAnimEnd) {
                    canClick = false;
                    isReverse = true;
                    arcAnimator.reverse();
                    animator.reverse();
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animator = null;
                        }
                    });
                } else {
                    startDrawArc();
                }
            }
        }
    }

    private void startDrawArc() {
        if (arcAnimator == null) {
            arcAnimator = ObjectAnimator.ofFloat(this, "currentSweep", 0, 360);
            arcAnimator.setDuration(1500);
        }
        if (arcAnimator.isRunning()) {
            setCurrentSweep(-90);
            //执行波纹动画
//            startRipple();
        }
        arcAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                arcAnimEnd = true;
                if (!isReverse) {
                    toggleMorph();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                arcAnimEnd = false;
            }
        });
        arcAnimator.start();
    }


//    ObjectAnimator rippleAnimator;
//
//    private void startRipple() {
//        if (rippleAnimator == null) {
//            rippleAnimator = ObjectAnimator.ofFloat(this, "rippleRadius", circleRadius / 3 * 2, 2 * circleRadius);
//            rippleAnimator.setDuration(500);
//        }
//        if (rippleAnimator.isRunning()) {
//            setRippleRadius(circleRadius / 3 * 2);
//        }
//        rippleAnimator.setInterpolator(new AccelerateInterpolator());
//        rippleAnimator.start();
//    }

    public void setPreAndAfterText(String textPre, String textAfter) {
        this.textPre = textPre;
        this.textAfter = textAfter;
        invalidate();
    }

//    public float getRippleRadius() {
//        return rippleRadius;
//    }
//
//    public void setRippleRadius(float rippleRadius) {
//        this.rippleRadius = rippleRadius;
//        invalidate();
//    }

    public float getCurrentSweep() {
        return currentSweep;
    }

    public void setCurrentSweep(float currentSweep) {
        this.currentSweep = currentSweep;
        invalidate();
    }

    public float getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(float currentOffset) {
        this.currentOffset = currentOffset;
        invalidate();
    }
}
