package com.example.hrafnkell.dots;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardView extends View {

    private ArrayList<ArrayList<Dot>> m_dots;
    private int [] dotColors = {Color.rgb(126, 84, 124), Color.rgb(97, 187, 213),
            Color.rgb(218, 99, 65), Color.rgb(131, 174, 82), Color.rgb(244, 192, 57)};


    private Rect m_rect = new Rect();
    private Paint m_paint = new Paint();
    private int m_cell_width;
    private int m_cell_height;
    private Canvas m_canvas;

    private RectF m_circle = new RectF();
    private Path m_path = new Path();
    private  Paint m_paintPath = new Paint();

    private final int NUM_CELLS = 6;

    // A lower number equals a bigger dot
    private final float TOUCH_AREA = 2.0f;
    private final float DOT_DRAW_SIZE = 5.0f;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_paint.setColor(Color.BLACK);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(2);
        m_paint.setAntiAlias(true);

        m_paintPath.setColor(Color.BLACK);
        m_paintPath.setStyle(Paint.Style.STROKE);
        m_paintPath.setStrokeWidth(10.0f);
        m_paintPath.setStrokeCap(Paint.Cap.ROUND);
        m_paintPath.setAntiAlias(true);
    }

    protected int getRandomColor(){
        Random r = new Random();
        int randomNumber = r.nextInt(dotColors.length - 0);
        return dotColors[randomNumber];
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int   boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int   boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        m_cell_width = boardWidth / NUM_CELLS;
        m_cell_height = boardHeight / NUM_CELLS;

        if(m_dots == null){
            m_dots = new ArrayList<>();

            // Initializing array with ArrayLists
            for (int row = 0; row < NUM_CELLS; row++) {
                m_dots.add(new ArrayList<Dot>());
                ArrayList colList = m_dots.get(row);
                for(int col = 0; col < NUM_CELLS; col++){
                    int x = (col * m_cell_width) + (m_cell_width/2);
                    int y = (row * m_cell_height) + (m_cell_height/2);
                    float touchArea = m_cell_height / TOUCH_AREA;
                    float dotDrawSize = m_cell_height / DOT_DRAW_SIZE;
                    int randColor = getRandomColor();

                    colList.add(new Dot(x + getPaddingLeft(), y + getPaddingTop(), col, row, touchArea, dotDrawSize, randColor));
                }
            }
        }

        if(m_dots != null || !m_dots.isEmpty()){
                //m_grid_circle_size = m_cell_height / 5; // TODO: Make for loop
                for (ArrayList<Dot> lst : m_dots) {
                    for (Dot dot : m_dots.get(m_dots.indexOf(lst))) {
                        dot.setDotSize(m_cell_height / DOT_DRAW_SIZE);
                        dot.setTouchSize(m_cell_height / TOUCH_AREA);
                    }
                }
        }


        m_circle.set(0, 0, m_cell_width, m_cell_height);
        m_circle.offset(getPaddingLeft(), getPaddingTop());
        m_circle.inset(m_cell_width * 0.1f, m_cell_height * 0.1f);
        //m_rect.set(0, 0, boardWidth, boardHeight );
        //m_rect.offset( getPaddingLeft(), getPaddingTop());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        m_canvas = canvas;

        //Background
        canvas.drawRect(m_rect, m_paint);

        //drawGrid(canvas);

        try{
            drawDots(canvas);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        if( !m_cellPath.isEmpty() ){
            drawPath(canvas);
        }
        //canvas.drawOval(m_circle, m_paintCircle);
    }

    boolean m_moving = false;

    private int xToCol(int x){
        return (x - getPaddingLeft()) / m_cell_width;
    }

    private int yToRow(int y){
        return (y - getPaddingTop()) / m_cell_height;
    }

    private int colToX(int col){
        return col * m_cell_width + getPaddingLeft();
    }
    private int rowToY(int row){
        return row * m_cell_height + getPaddingTop();
    }

    void snapToGrid(RectF circle){
        int col = xToCol((int) circle.left);
        int row = yToRow((int) circle.top);
        float x = colToX(col) + (m_cell_width - circle.width())/2;
        float y = rowToY(row) + (m_cell_height - circle.height())/2.0f;
        circle.offsetTo(x, y);

        //circle.offsetTo(colToX(col), rowToY(row));
    }

    private List<Point> m_cellPath = new ArrayList<Point>();

    @Override
    public  boolean onTouchEvent( MotionEvent event ){

        int x = (int) event.getX();
        int y = (int) event.getY();

        if(x < getPaddingLeft() || y < getPaddingTop()){
            return true;
        }

        if( event.getAction() == MotionEvent.ACTION_DOWN){
            //m_circle.offsetTo( x - m_circle.width() / 2, y - m_circle.height() / 2 );

            // check each dot if your finger is in it
            for(ArrayList<Dot> row : m_dots){
                ArrayList<Dot> col = m_dots.get(m_dots.indexOf(row));
                for(Dot dot : col){

                    if(dot.getTouchAreaRectf().contains(x,y)){
                        m_moving = true;
                        m_cellPath.add(new Point(xToCol(x), yToRow(y)));
                        m_paintPath.setColor(dot.getPaint().getColor());
                        //dot.getPaint().setColor(Color.BLACK);
                    }
                }
            }
            invalidate();
        }
        else if( event.getAction() == MotionEvent.ACTION_MOVE){
            if( m_moving){

                if(!m_cellPath.isEmpty()){
                    int col = xToCol(x);
                    int row = yToRow(y);
                    Point last = m_cellPath.get(m_cellPath.size()-1);
                    if( col != last.x || row != last.y){
                        m_cellPath.add(new Point(col, row));
                        //Toast.makeText(getContext(), "New coordinate added....", Toast.LENGTH_SHORT ).show();
                    }
                }
                //m_circle.offsetTo(x, y);
                invalidate();
            }
        }
        else if( event.getAction() == MotionEvent.ACTION_UP){
            m_moving = false;
            //snapToGrid(m_circle);
            m_cellPath.clear();
            invalidate();
        }

        return true;
    }

    ValueAnimator animator = new ValueAnimator();

    private  void animateMovement (final float xFrom, final float yFrom, final float xTo, final float yTo){
        animator.removeAllUpdateListeners();
        animator.setDuration(1000);
        animator.setFloatValues(0.0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                int x = (int)( (1.0-ratio) * xFrom + ratio * xTo );
                int y = (int)( (1.0-ratio) * yFrom + ratio * yTo );
                m_circle.offsetTo( x, y );
                invalidate();
            }
        });
        animator.start();
    }

    public void drawDots(Canvas canvas){
        for(ArrayList<Dot> row : m_dots){
            ArrayList<Dot> col = m_dots.get(m_dots.indexOf(row));
            for(Dot dot : col){
                canvas.drawOval(dot.getDotDrawRectf(), dot.getPaint());
            }
        }
    }

    public void drawPath(Canvas canvas){
        m_path.reset();
        Point point = m_cellPath.get(0);
        m_path.moveTo(colToX(point.x) + m_cell_width / 2, rowToY(point.y) + m_cell_height / 2);
        for(int i=1; i<m_cellPath.size(); ++i){
            point = m_cellPath.get(i);
            m_path.lineTo( colToX(point.x) + m_cell_width/2, rowToY(point.y) + m_cell_height/2);
        }
        canvas.drawPath( m_path, m_paintPath);
    }

    public void drawGrid(Canvas canvas){
        for(int row = 0; row < NUM_CELLS; ++row){
            for(int col = 0; col < NUM_CELLS; ++col){

                int x = col * m_cell_width;
                int y = row * m_cell_height;

                //Grid
                m_rect.set(x, y, x + m_cell_width, y + m_cell_height);
                m_rect.offset(getPaddingLeft(), getPaddingTop());
                canvas.drawRect(m_rect, m_paint);

                //Draw circle
                //setDotCenter(x,y);
                //m_grid_circle.offset(getPaddingLeft(), getPaddingTop())
                //canvas.drawOval(m_circle, m_circle_paint)
            }
        }
    }
}
