package com.example.administrator.animationviews.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.example.administrator.animationviews.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * created by QingMi on 2019/03/08
 * 实现：实现云朵、下雨
 */

public class RainView extends View {

    int backgroundColor;                //view整体背景颜色

    int leftCloudPointX = 150;          //设置左边云朵的绘制坐标
    int leftCloudPointY = 150;
    int cloudWidth = 300;               //设置云朵的宽度与厚度
    int cloudHeight = 100;
    int cloudTranslate = 50;            //设置云朵动画过程中的最大平移距离
    float cloudScale = 0.8f;

    ValueAnimator leftCloudAnimator;    //设置左边云朵的属性动画
    float leftCloudAnimatorValue;
    Matrix leftCloudMatrix;

    Timer rainTimer;                    //雨滴的生成计时器
    int rainDropPeriod = 200;           //创建雨滴的间隔

    int rainDropLength = 60;
    int rainDropSpeed = 10;
    int rainDropDegree = 75;            //雨滴的斜角角度
    int maxRainSize = 20;               //雨滴最大数量

    ArrayList<RainDrop> rainDropList;

    Path leftCloudPath = null;
    Paint leftCloudPaint = null;

    public RainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RainView);

        if(typedArray != null){
            backgroundColor = typedArray.getInt(R.styleable.RainView_background_color, Color.BLACK);
        }

        init();
        //设置属性动画
        setAnimator();
    }

    private void init(){
        rainDropList = new ArrayList<>();

        //设置生成新雨滴的间隔
        rainTimer = new Timer();
        rainTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        },0,rainDropPeriod);
    }

    /**
     * 生成一个新的raindrop
     * @return
     */
    private RainDrop createRainDrop(){
        RainDrop rainDrop = new RainDrop();
        Random random = new Random();
        rainDrop.x = random.nextInt(cloudWidth ) + (leftCloudPointX + cloudTranslate + 50);
        rainDrop.y = leftCloudPointY + cloudHeight;
        rainDrop.length = random.nextInt(rainDropLength - 10) + 10;
        rainDrop.speed = random.nextInt(rainDropSpeed - 5) + 5;
        rainDrop.degree = rainDropDegree;
        return rainDrop;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(backgroundColor);

        drawCloud(canvas);

        drawRain(canvas);
    }

    /**
     * 绘制云朵，由一条线和两个圆点组成
     * @param canvas
     */
    private void drawCloud(Canvas canvas){
        leftCloudPath = new Path();
        leftCloudPaint = new Paint();
        leftCloudMatrix = new Matrix();

        leftCloudPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        leftCloudPaint.setColor(getResources().getColor(R.color.light_gray));
        leftCloudPaint.setStrokeCap(Paint.Cap.ROUND);
        leftCloudPaint.setStrokeWidth(1);
        leftCloudPaint.setAntiAlias(true);

        //先绘制一个圆角矩形，再绘制两个圆形，组合成一个“云朵”的图案
        leftCloudPath.addRoundRect(new RectF( leftCloudPointX, leftCloudPointY,leftCloudPointX + cloudWidth,leftCloudPointY + cloudHeight),cloudHeight / 2,cloudHeight / 2, Path.Direction.CCW);
        leftCloudPath.addCircle(leftCloudPointX + cloudHeight, leftCloudPointY,cloudHeight / 2, Path.Direction.CCW);
        leftCloudPath.addCircle(leftCloudPointX + cloudHeight * 2, leftCloudPointY, cloudHeight * 3 / 4, Path.Direction.CCW);

        //左边云朵的移动动画
        leftCloudMatrix.reset();
        leftCloudMatrix.postTranslate(cloudTranslate * leftCloudAnimatorValue, 0);
        leftCloudPath.transform(leftCloudMatrix, leftCloudPath);

        canvas.drawPath(leftCloudPath, leftCloudPaint);

        //右边云朵的移动动画
        Matrix matrix = new Matrix();
        matrix.postTranslate(200, 0);
        matrix.postScale(cloudScale,cloudScale,200,200);
        leftCloudPath.transform(matrix, leftCloudPath);;

        leftCloudPaint.setColor(getResources().getColor(R.color.dark_gray));
        canvas.drawPath(leftCloudPath, leftCloudPaint);
    }

    /**
     * 绘制雨滴
     * @param canvas
     */
    private void drawRain(Canvas canvas){
        Paint paint = new Paint();
        Path path = null;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);

        for(int i=0;i<rainDropList.size();i++){
            RainDrop rainDrop = rainDropList.get(i);
            double degree = Math.toRadians(rainDrop.degree);

            //如果雨滴超出屏幕范围，将其移出list
            if(rainDrop.y > 1200){
                rainDropList.remove(rainDrop);
                continue;
            }

            path = new Path();
            path.moveTo(rainDrop.x, rainDrop.y);
            path.lineTo((float)(rainDrop.x - Math.cos(degree)*rainDrop.length),(float)(rainDrop.y + Math.sin(degree)*rainDrop.length));
            path.close();

            canvas.drawPath(path, paint);

            rainDrop.x += - Math.cos(degree) * rainDrop.speed;
            rainDrop.y += Math.sin(degree) * rainDrop.speed;
        }
    }

    /**
     * 设置属性动画
     */
    private void setAnimator(){
        leftCloudAnimator = ValueAnimator.ofFloat(1,0);
        leftCloudAnimator.setRepeatCount(ValueAnimator.INFINITE);       //设置执行次数无限次
        leftCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);         //设置会有颠倒计算，配合无限次数完成无限往复循环
        leftCloudAnimator.setDuration(1000);                            //执行时长
        leftCloudAnimator.setInterpolator(new LinearInterpolator());    //平滑插值器
        leftCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftCloudAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        leftCloudAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 资源释放
     */
    public void onRelease(){
        leftCloudAnimator.cancel();
        rainTimer.cancel();
    }

    /**
     * 为雨滴新建一个类，属性：起始点坐标、速度、长度、斜率
     */
    private class RainDrop{

        float x;
        float y;
        float speed;
        float length;
        int degree;

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //每隔0.5s创建一个新雨滴
                    if(rainDropList.size() < maxRainSize) {
                        rainDropList.add(createRainDrop());
                    }
                    Log.d("rain","list.size:"+rainDropList.size());
                    break;
            }
        }
    };

}
