package com.spiralespace3;


class Circle {
     private int x;
     private int y;
     private int radius;
     private double speedw;
     private double speedh;
     private int borderHeight;
     private int borderWidth;

     private boolean isStopped;
     private int color;

     Circle(int x, int y, int radius, double speedw, double speedh, int color, int borderH, int borderW) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speedw = speedw;
        this.speedh = speedh;
        this.color = color;
        this.isStopped = false;
        this.borderHeight = borderH;
        this.borderWidth = borderW;
    }
     void toMove(){

         if ((Math.abs(speedw) < 0.001 && !isStopped) || (Math.abs(speedh) < 0.001 && !isStopped)) {speedw=0;speedh=0; isStopped = true;}

         if (x < 0)                             speedw=-0.95*speedw;
         if (y < 0)                             speedh=-0.95*speedh;
         if (x > borderWidth-2*radius-speedw)        speedw=-0.95*speedw;
         if (y > borderHeight-6*speedh)              speedh=-0.95*speedh;


         x = x +(int) speedw;
         y = y +(int) speedh;

     }

     int getColor() {


        return color;
    }

     int getX() {

        return x;
    }
     int getY() {

         return y;
    }
     int getRadius() {

         return radius;
     }

     void setSpeedw(double speedw) {

        this.speedw = speedw;
    }
     void setSpeedh(double speedh) {

        this.speedh = speedh;
    }

     double getSpeedw() {

        return speedw;
    }
     double getSpeedh() {

        return speedh;
    }


}


