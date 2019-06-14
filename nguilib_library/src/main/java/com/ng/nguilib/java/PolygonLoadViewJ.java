package com.ng.nguilib.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.ng.nguilib.LogUtils;

public class PolygonLoadViewJ extends View {
    //common
    private Paint paintLine;
    private Paint paintPoint;

    private RectF roundRF;
    private Float mGridLinestrokeWidth = 30f;

    public static int SHOW_MODEL = 0;
    public static final int SHOW_MODEL_ROUND = 0x00;
    public static final int SHOW_MODEL_TRIANGLE = 0x01;
    public static final int SHOW_MODEL_SQUARE = 0x02;

    public static final long TIME_CIRCLE = 2200;

    private AnimatorSet animatorSet;
    private float mSideLenght;
    float mHalfSH;
    float thickness;

    //round
    private float pointX;
    private float pointY;
    private float startAngle;
    private static final float swipeAngle = 270f;
    //triangle

    public PolygonLoadViewJ(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(int model) {
        if (SHOW_MODEL == SHOW_MODEL_ROUND || SHOW_MODEL == SHOW_MODEL_TRIANGLE || SHOW_MODEL == SHOW_MODEL_SQUARE) {
            this.SHOW_MODEL = model;
            init();
            postInvalidate();
        } else {
            try {
                throw new Exception("error model");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        paintLine = new Paint();
        paintPoint = new Paint();
        animatorSet = new AnimatorSet();
        mHalfSH = mSideLenght / 2;
        thickness = mGridLinestrokeWidth / 2;
        switch (SHOW_MODEL) {
            case SHOW_MODEL_ROUND:
                initRound();
                break;
            case SHOW_MODEL_TRIANGLE:
                initTriangle();
                break;
            case SHOW_MODEL_SQUARE:
                initSquare();
                break;
        }

    }

    private void initSquare() {
        startAnimSquare();
    }

    private void startAnimSquare() {

    }

    private void initTriangle() {
        //paint
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.parseColor("#2D283C"));
        paintLine.setStrokeWidth(mGridLinestrokeWidth);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        roundRF = new RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2);
        paintPoint.setAntiAlias(true);
        paintPoint.setColor(Color.parseColor("#4A22EA"));
        paintPoint.setStyle(Paint.Style.STROKE);
        paintPoint.setStrokeWidth(mGridLinestrokeWidth);
        paintPoint.setStrokeCap(Paint.Cap.ROUND);


        //point
        pointX = mHalfSH / 3 + thickness;
        pointY = mHalfSH / 2;
        startAnimTriangle();
    }

    /**
     * x ->  width/3  -  width/2  - width*2/3 - width/3
     * y ->  width/2   -   0  -  width/2 - width/2
     * startAngle ->        315f 225f 135f 45f -45f
     */
    private void startAnimTriangle() {
        AccelerateInterpolator interpolator = new AccelerateInterpolator(1f);
        ValueAnimator pointAnimator1 = ValueAnimator.ofFloat(0, 100);
        pointAnimator1.setDuration(TIME_CIRCLE / 3);
        pointAnimator1.setInterpolator(interpolator);
        pointAnimator1.setStartDelay(30);//制造停顿感
        pointAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = mHalfSH - temp * (mHalfSH - thickness);
                pointY = thickness + temp * (mHalfSH - thickness);
                invalidate();
            }
        });

        ValueAnimator pointAnimator2 = ValueAnimator.ofFloat(0, 100);
        pointAnimator2.setDuration(TIME_CIRCLE / 3);
        pointAnimator2.setInterpolator(interpolator);
        pointAnimator2.setStartDelay(30);

        pointAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = thickness + temp * (mHalfSH - thickness);
                pointY = mHalfSH + temp * (mHalfSH - thickness);
                invalidate();
            }
        });
        ValueAnimator pointAnimator3 = ValueAnimator.ofFloat(0, 100);
        pointAnimator3.setDuration(TIME_CIRCLE / 3);
        pointAnimator3.setInterpolator(interpolator);
        pointAnimator3.setStartDelay(30);

        pointAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override

            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = mHalfSH + temp * (mHalfSH - thickness);
                pointY = mSideLenght - thickness - temp * (mHalfSH - thickness);
                invalidate();
            }
        });
        animatorSet.playSequentially(pointAnimator1, pointAnimator2, pointAnimator3);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet.start();
    }

    private void initRound() {
        //paint
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.parseColor("#2D283C"));
        paintLine.setStrokeWidth(mGridLinestrokeWidth);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        roundRF = new RectF(0 + mGridLinestrokeWidth / 2,
                0 + mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2,
                mSideLenght - mGridLinestrokeWidth / 2);
        paintPoint.setAntiAlias(true);
        paintPoint.setColor(Color.parseColor("#4A22EA"));
        paintPoint.setStyle(Paint.Style.STROKE);
        paintPoint.setStrokeWidth(mGridLinestrokeWidth);
        paintPoint.setStrokeCap(Paint.Cap.ROUND);


        //point
        pointX = mHalfSH;
        pointY = thickness;

        startAnimRound();

    }


    /**
     * x ->  width/2  -  0  - width/2 - width - width/2
     * y ->  0       -   height/2   - height  - height/2 - 0
     * startAngle ->        315f 225f 135f 45f -45f
     */
    private synchronized void startAnimRound() {
        // result = (float)(1.0f - Math.pow((1.0f - input), 2 * mFactor));
        AccelerateInterpolator interpolator = new AccelerateInterpolator(1f);
        ValueAnimator pointAnimator1 = ValueAnimator.ofFloat(0, 100);
        pointAnimator1.setDuration(TIME_CIRCLE / 4);
        pointAnimator1.setInterpolator(interpolator);
        pointAnimator1.setStartDelay(30);//制造停顿感
        pointAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = mHalfSH - temp * (mHalfSH - thickness);
                pointY = thickness + temp * (mHalfSH - thickness);
                startAngle = 315f - temp * 90;
                invalidate();
            }
        });

        ValueAnimator pointAnimator2 = ValueAnimator.ofFloat(0, 100);
        pointAnimator2.setDuration(TIME_CIRCLE / 4);
        pointAnimator2.setInterpolator(interpolator);
        pointAnimator2.setStartDelay(30);

        pointAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = thickness + temp * (mHalfSH - thickness);
                pointY = mHalfSH + temp * (mHalfSH - thickness);
                startAngle = 225f - temp * 90;
                invalidate();
            }
        });
        ValueAnimator pointAnimator3 = ValueAnimator.ofFloat(0, 100);
        pointAnimator3.setDuration(TIME_CIRCLE / 4);
        pointAnimator3.setInterpolator(interpolator);
        pointAnimator3.setStartDelay(30);

        pointAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override

            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = mHalfSH + temp * (mHalfSH - thickness);
                pointY = mSideLenght - thickness - temp * (mHalfSH - thickness);
                startAngle = 135f - temp * 90;
                invalidate();
            }
        });
        ValueAnimator pointAnimator4 = ValueAnimator.ofFloat(0, 100);
        pointAnimator4.setDuration(TIME_CIRCLE / 4);
        pointAnimator4.setInterpolator(interpolator);
        pointAnimator4.setStartDelay(30);
        pointAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override

            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = animation.getAnimatedFraction();
                pointX = mSideLenght - temp * (mHalfSH - thickness) - thickness;
                pointY = mHalfSH - temp * (mHalfSH - thickness);
                if (startAngle > 0) {
                    startAngle = 45 - temp * 90;
                } else {
                    startAngle = 405 - temp * 90;
                }
                invalidate();
            }
        });
//        animatorSet.play(pointAnimator1);
//        animatorSet.play(pointAnimator2).after(pointAnimator1);
//        animatorSet.play(pointAnimator3).after(pointAnimator2);
//        animatorSet.play(pointAnimator4).after(pointAnimator3);
        animatorSet.playSequentially(pointAnimator1, pointAnimator2, pointAnimator3, pointAnimator4);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSideLenght = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight() : getMeasuredWidth();
        //宽必须等于高
        LogUtils.d("宽： " + mSideLenght + "  高：  " + mSideLenght);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (SHOW_MODEL) {
            case SHOW_MODEL_ROUND:
                drawRound(canvas);
                break;
            case SHOW_MODEL_TRIANGLE:
                drawTriangle(canvas);
                break;
            case SHOW_MODEL_SQUARE:
                drawSquare(canvas);
                break;
            default:
                break;
        }
    }

    private void drawSquare(Canvas canvas) {
        canvas.drawPoint(pointX, pointY, paintPoint);
    }

    private void drawTriangle(Canvas canvas) {

    }

    private void drawRound(Canvas canvas) {
        canvas.drawArc(roundRF, startAngle, swipeAngle, false, paintLine);
        canvas.drawPoint(pointX, pointY, paintPoint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }




}
