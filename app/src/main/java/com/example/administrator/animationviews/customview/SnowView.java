package com.example.administrator.animationviews.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.administrator.animationviews.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnowView extends View {

    List<Snow> snowList;
    int maxSnowListSize = 30;   //屏幕上最大雪花数量

    Timer snowTimer;            //下雪的计时器
    int snowPeriod = 500;       //随机生成雪花的间隔
    int refreshPeriod = 16;     //雪花动画的刷新间隔
    int snowSpeed = 5;          //雪花的下降速度，供随机函数使用
    int rotateSpeed = 36;       //雪花的旋转速度，同上

    Context context;
    Bitmap snowBitmap;          //雪花的图片资源

    public SnowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
        setBackgroundColor(Color.GRAY);
    }

    private void init(){
        snowList = new ArrayList<>();
        snowBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.snow);

        snowTimer = new Timer();
        snowTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        },0,snowPeriod);
        //每10ms刷新一次画面
        snowTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
            }
        },0,refreshPeriod);
    }

    /**
     * 生成雪花，水平位置、下降速度、旋转速度、缩放大小均随机
     * @return
     */
    private Snow createSnow(){
        Snow snow = new Snow();
        Random random = new Random();
        snow.x = random.nextInt(720);
        snow.y = 0;
        snow.downSpeed = random.nextInt(snowSpeed) + snowSpeed/2;
        snow.rotateSpeed = ((float)random.nextInt(rotateSpeed) + rotateSpeed/2) / (float)(1000 / refreshPeriod);
        snow.rotate = snow.rotateSpeed;
        snow.scale = random.nextInt(6-4) + 4;
        return snow;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSnow(canvas);
    }

    /**
     * 绘画雪花
     */
    private void drawSnow(Canvas canvas){
        Rect src = new Rect(0,0,snowBitmap.getWidth(),snowBitmap.getHeight());
        Rect dtc = null;

        for (int i=0;i<snowList.size();i++){

            Snow snow = snowList.get(i);

            int width = snowBitmap.getWidth() / snow.scale / 2;
            snow.y += snow.downSpeed;
            dtc = new Rect(snow.x - width, snow.y - width, snow.x + width, snow.y + width);

            canvas.save();

            snow.rotate += snow.rotateSpeed;
            canvas.rotate(snow.rotate, snow.x, snow.y);

            canvas.drawBitmap(snowBitmap,src,dtc,null);

            canvas.restore();

            //超出屏幕范围，则将雪花移出屏幕
            if(snow.y > 1280){
                snowList.remove(snow);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 新建一个 雪花 的类，属性：中心点坐标、旋转角度、旋转速度以及缩放大小
     */
    private class Snow{

        int x;
        int y;
        int downSpeed;
        float rotate;
        float rotateSpeed;
        int scale;

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //若未超出最大容量，每隔0.2s创建一个新的雪花
                    if(snowList.size() <= maxSnowListSize){
                        snowList.add(createSnow());
                    }
                    break;
                case 2:
                    //每10ms刷新一次画面
                    invalidate();
                    break;
            }
        }
    };

    /**
     * 释放资源
     */
    public void onRelease(){
        snowTimer.cancel();
    }

}
