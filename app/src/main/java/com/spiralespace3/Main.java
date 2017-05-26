package com.spiralespace3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.Vector;

public class Main extends AppCompatActivity{
    private Vector<Circle> circles = new Vector<Circle>();
    private Canvas canvas = new Canvas();
    private double speed = 10;
    private int circleColor = Color.parseColor("#A7AFFA"); //Color.argb(200, 104, 120 ,113);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        toFillVector();

        MySurfaceView surface = new MySurfaceView(this);
        surface.setOnTouchListener(new TouchListener());

        setContentView(surface);
}
    @Override
    public void onDestroy() {
        moveTaskToBack(true);

        super.onDestroy();

        //System.runFinalizersOnExit(true);
        this.finish();

        System.exit(0);
    }

    public void paint(Canvas canvas){

        for (Circle circle : circles) circle.toMove();

        Paint circlePaint = new Paint();

        for (Circle circle : circles){
            circlePaint.setColor(circle.getColor());
            canvas.drawCircle(circle.getX(),circle.getY(),circle.getRadius(),circlePaint);
        }
}


    private void toSetSpeed(Circle circle, MotionEvent e){

        double H, W;
        double coefw, coefh;
        if (e.getY() < circle.getY() && e.getX() > circle.getX()) //  I quant
        {
            coefw =  1.0001;
            coefh = -1.0001;

            H = circle.getY() - e.getY();
            W = e.getX() - circle.getX();

            circle.setSpeedw(coefw*(W*Math.sqrt(((circle.getSpeedh()*circle.getSpeedh() +  circle.getSpeedw()*circle.getSpeedw())/(H*H + W*W)))));
            circle.setSpeedh(coefh*(H*circle.getSpeedw())/W);
        }


        if (e.getY() < circle.getY() && e.getX() < circle.getX()) // II quant
        {
            coefw = -1.0001;
            coefh = -1.0001;

            H = circle.getY() - e.getY();
            W = circle.getX() - e.getX();

            circle.setSpeedw(coefw*(W*Math.sqrt(((circle.getSpeedh()*circle.getSpeedh() +  circle.getSpeedw()*circle.getSpeedw())/(H*H + W*W)))));
            circle.setSpeedh(-coefh*(H*circle.getSpeedw())/W);
        }


        if (e.getY() > circle.getY() && e.getX() < circle.getX()) //III quant
        {
            coefw =- 1.0001;
            coefh =  1.0001;

            H = e.getY() - circle.getY();
            W = circle.getX()- e.getX();

            circle.setSpeedw(coefw*(W*Math.sqrt(((circle.getSpeedh()*circle.getSpeedh() +  circle.getSpeedw()*circle.getSpeedw())/(H*H + W*W)))));
            circle.setSpeedh(-coefh*(H*circle.getSpeedw())/W);
        }


        if (e.getY() > circle.getY() && e.getX() > circle.getX()) // IV quant
        {
            coefw= 1.0001;
            coefh= 1.0001;

            H = e.getY() - circle.getY();
            W = e.getX() - circle.getX();

            circle.setSpeedw(coefw*(W*Math.sqrt(((circle.getSpeedh()*circle.getSpeedh() +  circle.getSpeedw()*circle.getSpeedw())/(H*H + W*W)))));
            circle.setSpeedh(coefh*(H*circle.getSpeedw())/W);
        }
    }
    private void toFillVector(){
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size); //заберём размеры экрана девайса, на котором это запущено

        for (int i=0; i< 80; i++)
            for (int j=0; j< 80; j++)
                circles.addElement(new Circle(20+i*40, 40+j*40, 1, speed, speed, circleColor, size.y, size.x));
    }
    private class TouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            System.out.println(event.getX()+" "+event.getY());
                for (Circle circle : circles) toSetSpeed(circle, event);

            return true;
        }
    }

    private class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private DrawThread drawThread;
        public MySurfaceView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new DrawThread(getHolder(), getResources());
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;

            drawThread.setRunning(false); // завершаем работу потока

            while (retry) {
                    drawThread.interrupt();
                    retry = false;
            }
        }
        }
    private class DrawThread extends Thread{
        private boolean runFlag = false;
        private SurfaceHolder surfaceHolder;

        private DrawThread(SurfaceHolder surfaceHolder, Resources resources){
            this.surfaceHolder = surfaceHolder;
        }
        private void setRunning(boolean run) {

            runFlag = run;
        }

        @Override
        public void run() {
            Canvas canvas;

            while (runFlag) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);   // получаем объект Canvas и выполняем отрисовку
                    canvas.drawColor(Color.BLACK);  //очистка экрана
                    synchronized (surfaceHolder) {
                                  paint(canvas);
                    }
                }
                finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas); // отрисовка выполнена. выводим результат на экран
                    }
                }
            }
        }
    }
}