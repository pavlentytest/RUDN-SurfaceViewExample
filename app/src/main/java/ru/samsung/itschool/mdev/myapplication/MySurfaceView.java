package ru.samsung.itschool.mdev.myapplication;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    MyThread mythread;

    public MySurfaceView(Context context) {
        super(context);
        // важная строчка, необходима для запуска surfaceCreated
        getHolder().addCallback(this);
    }

    // вызывается когда surfaceview появляется на экране
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mythread = new MyThread(getHolder());
        mythread.setRunning(true);
        mythread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    // вызывает onDestroy
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // корректно останавливаем поток
        boolean retry = true;
        mythread.setRunning(false);
        while(retry) {
            try {
                mythread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
