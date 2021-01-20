package ru.samsung.itschool.mdev.myapplication;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

public class MyThread extends Thread {

    // частота обновления экрана
    private final int REDRAW_TIME = 5000;

    // константа для интерполяциии
    private final int FRACTION_TIME = 150000;

    // флажок запущен ли поток
    private boolean flag;

    // время начала анимации
    private long startTime;

    // предыдущее время перерисовки
    private long prevRedrawTime;

    private Paint paint;

    // переменная для интерполяции
    private ArgbEvaluator argbEvaluator;

    // указатель для получения canvas
    private SurfaceHolder surfaceHolder;

    MyThread(SurfaceHolder h) {
        flag = false;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
        surfaceHolder = h;
    }

    public long getTime() {
        return System.nanoTime()/1000; // микросек.
    }

    public void ourdraw(Canvas canvas) {
        long currentTime = getTime() - startTime;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // фон
        canvas.drawColor(Color.BLACK);
        int centerX = width/2;
        int centerY = height/2;
        // максимальный радиус круга
        float maxRadius = Math.min(width,height)/2;
        Log.d("RRRR maxradius=",Float.toString(maxRadius));
        // шаг интерполирования
        float fraction = (float)(currentTime%FRACTION_TIME)/FRACTION_TIME;
        Log.d("RRRR fraction=",Float.toString(fraction));

        Random r = new Random();
        int color = Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));

       // int color = (int)argbEvaluator.evaluate(fraction,Color.RED,Color.BLACK);
        Log.d("RRRR color=",Float.toString(color));

        paint.setColor(color);
        canvas.drawCircle(centerX,centerY,maxRadius*fraction,paint);
    }

    public void setRunning(boolean running) {
        flag = running;
        prevRedrawTime = getTime();
    }

    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while(flag) {
            long currTime = getTime();
            long elapsedTime = currTime - prevRedrawTime;
            if(elapsedTime < REDRAW_TIME) {
                continue;
            }
            canvas = null;
            // получаем результат canvas
            canvas = surfaceHolder.lockCanvas();
            // отрисовка в canvas
            ourdraw(canvas);
            // очищаем canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
            // обновление экрана
            prevRedrawTime = getTime();
        }
    }




}
