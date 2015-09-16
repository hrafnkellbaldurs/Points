package com.example.hrafnkell.dots;

import android.graphics.Paint;
import android.graphics.RectF;


/**
 * Created by Hrafnkell on 10/9/2015.
 *
 * This class represents a single dot on the grid of the game
 */
public class Dot {

    // Center x and y coordinates
    private int x, y;

    private int col, row;


    // Coordinates to make the touch area for the dot
    private RectF touchAreaRectf;

    // Coordinates to draw the dot
    private RectF dotDrawRectf;

    // Paint object for the dot to actually draw it
    private Paint paint;

    // The area that the user can touch to activate the dot
    private float touchArea;
    // The drawn graphic size for the dot
    private float dotDrawSize;

    // The color of the dot
    public int colorIndex;

    public Dot(int x,int y, int row, int col, float touchArea, float dotDrawSize, int color, int colorIndex){
        this.x = x;
        this.y = y;
        this.col = col;
        this.row = row;
        this.touchArea = touchArea;
        this.dotDrawSize = dotDrawSize;
        this.colorIndex = colorIndex;
        this.touchAreaRectf = new RectF(x - touchArea, y - touchArea, x + touchArea, y + touchArea );
        this.dotDrawRectf = new RectF(x - dotDrawSize, y - dotDrawSize, x + dotDrawSize, y + dotDrawSize );
        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setAntiAlias(true);
    }

    public void setX(int x){
        this.x = x;
        this.dotDrawRectf.set(x - dotDrawSize, y - dotDrawSize, x + dotDrawSize, y + dotDrawSize);
        this.touchAreaRectf.set(x - touchArea, y - touchArea, x + touchArea, y + touchArea);
    }

    public void setY(int y){
        this.y = y;
        this.dotDrawRectf.set(x - dotDrawSize, y - dotDrawSize, x + dotDrawSize, y + dotDrawSize);
        this.touchAreaRectf.set(x - touchArea, y - touchArea, x + touchArea, y + touchArea);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setCol(int col){
        this.col = col;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getCol(){
        return this.col;
    }

    public int getRow(){
        return this.row;
    }

    public void setTouchSize(float size){
        this.touchArea = size;
        this.touchAreaRectf.set(x - touchArea, y - touchArea, x + touchArea, y + touchArea);
    }

    public float getTouchSize(){
        return this.touchArea;
    }

    public RectF getTouchAreaRectf(){
        return this.touchAreaRectf;
    }

    public void setTouchAreaRectf(float f1, float f2, float f3, float f4){
        this.touchAreaRectf.set(f1,f2,f3,f4);
    }

    public void setDotSize(float size){
        this.dotDrawSize = size;
        this.dotDrawRectf.set(x - dotDrawSize, y - dotDrawSize, x + dotDrawSize, y + dotDrawSize);
    }

    public float getDotDrawSize(){
        return this.dotDrawSize;
    }

    public RectF getDotDrawRectf(){
        return this.dotDrawRectf;
    }

    public void setDotDrawRectf(float f1, float f2, float f3, float f4){
        this.dotDrawRectf.set(f1, f2, f3, f4);
    }

    public Paint getPaint(){
        return this.paint;
    }

    public void setPaint(Paint paint){
        this.paint = paint;
    }
}
