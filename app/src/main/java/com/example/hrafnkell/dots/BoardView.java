package com.example.hrafnkell.dots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardView extends View {

    MediaPlayer [] sounds = {new MediaPlayer(), new MediaPlayer(),
            new MediaPlayer(), new MediaPlayer(), new MediaPlayer(), new MediaPlayer()};

    private GameHandler m_gameHandler = null;

    /* Constants */
    private final int INITIAL_MOVES = 30;
    // The number of rows and columns of dots
    private final int NUM_DOTS = 6;
    // A lower number equals a bigger touch area
    private final float TOUCH_AREA = 2.0f;
    // A lower number equals a bigger dot
    private final float DOT_DRAW_SIZE = 5.0f;
    // A selection of colors that a dot can be
    //0=purple
    //1=blue
    //2=orange
    //3=green
    //4=yellow
    private final int [] DOT_COLORS = {Color.rgb(126, 84, 124),
                                        Color.rgb(97, 187, 213),
                                        Color.rgb(218, 99, 65),
                                        Color.rgb(131, 174, 82),
                                        Color.rgb(244, 192, 57)};
    /* End of constants */

    private int m_score;
    private int m_moves;
    private TextView scoreView;

    private int colorIndex;

    // All dots that are on the grid
    private ArrayList<ArrayList<Dot>> m_dots;

    // All paths that the user draws
    private List<Point> m_cellPath = new ArrayList<Point>();

    private List<Dot> m_dotsTouched = new ArrayList<>();

    // Tells us if the user is currently moving his finger on the device screen
    private boolean m_moving = false;

    // The width and height of each cell that contains a dot on the grid
    private int m_cell_width;
    private int m_cell_height;

    // The rect object for drawing the grid
    private Rect m_rect = new Rect();
    private Paint m_paint = new Paint();

    // The path object for the drawn path of the user
    private Path m_path = new Path();
    private Paint m_paintPath = new Paint();

    // Constructor
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

        m_score = 0;
        m_moves = INITIAL_MOVES;

        sounds[0] = MediaPlayer.create(getContext(), R.raw.onedot);
        sounds[1] = MediaPlayer.create(getContext(), R.raw.twodot);
        sounds[2] = MediaPlayer.create(getContext(), R.raw.threedot);
        sounds[3] = MediaPlayer.create(getContext(), R.raw.fourdot);
        sounds[4] = MediaPlayer.create(getContext(), R.raw.fivedot);
        sounds[5] = MediaPlayer.create(getContext(), R.raw.sixdot);
    }

    // Returns a random color from the DOT_COLORS array
    protected int getRandomColor(){
        Random r = new Random();
        int randomNumber = r.nextInt(DOT_COLORS.length);
        colorIndex = randomNumber;
        return DOT_COLORS[randomNumber];
    }

    @Override
    protected void onSizeChanged( int xNew, int yNew, int xOld, int yOld ) {
        int boardWidth = (xNew - getPaddingLeft() - getPaddingRight());
        int boardHeight = (yNew - getPaddingTop() - getPaddingBottom());
        m_cell_width = boardWidth / NUM_DOTS;
        m_cell_height = boardHeight / NUM_DOTS;

        // Initalizing the dots list with new dots
        if(m_dots == null){
            m_dots = new ArrayList<>();
            // Initializing array with ArrayLists
            for (int col = 0; col < NUM_DOTS; col++) {
                m_dots.add(new ArrayList<Dot>());
                ArrayList colList = m_dots.get(col);
                for(int row = 0; row < NUM_DOTS; row++){
                    int y = (row * m_cell_width) + (m_cell_width/2);
                    int x = (col * m_cell_height) + (m_cell_height/2);
                    float touchArea = m_cell_height / TOUCH_AREA;
                    float dotDrawSize = m_cell_height / DOT_DRAW_SIZE;
                    int randColor = getRandomColor();

                    colList.add(new Dot(x + getPaddingLeft(), y + getPaddingTop(), row, col,
                            touchArea, dotDrawSize, randColor, colorIndex));
                }
            }
        }

        if(m_dots != null || !m_dots.isEmpty()){
                for (ArrayList<Dot> lst : m_dots) {
                    for (Dot dot : m_dots.get(m_dots.indexOf(lst))) {
                        dot.setDotSize(m_cell_height / DOT_DRAW_SIZE);
                        dot.setTouchSize(m_cell_height / TOUCH_AREA);
                    }
                }
        }

        /* Erase comments for grid */
        //m_rect.set(0, 0, boardWidth, boardHeight );
        //m_rect.offset( getPaddingLeft(), getPaddingTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Draw the grid background
        canvas.drawRect(m_rect, m_paint);

        // Draw all of the dots contained in the dots list
        try{
            drawDots(canvas);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // Draws the path that the user creates
        if( !m_cellPath.isEmpty() ){
            drawPath(canvas);
        }

        //scoreView.setText(String.valueOf(score));

        /* Erase comment for the grid to be drawn */
        //drawGrid(canvas);
    }


    @Override
    public boolean onTouchEvent( MotionEvent event ){


            int x = (int) event.getX();
            int y = (int) event.getY();

            int xMax = getPaddingLeft() + m_cell_width * NUM_DOTS;
            int yMax = getPaddingTop() + m_cell_height * NUM_DOTS;
            x = Math.max(getPaddingLeft(), Math.min(x, xMax));
            y = Math.max(getPaddingTop(), Math.min(y, yMax));

            if(x < getPaddingLeft() || y < getPaddingTop()){
                return true;
            }
            if(x > xMax || y > yMax){
                return true;
            }

            if( event.getAction() == MotionEvent.ACTION_DOWN){

                // Check each dot if your finger is in it
                for(ArrayList<Dot> col : m_dots){
                    for(Dot dot : col){
                        if(dot.getTouchAreaRectf().contains(x,y)){
                            m_moving = true;
                            m_cellPath.add(new Point(dot.getCol(), dot.getRow()));
                            m_dotsTouched.add(dot);
                            if(sounds[0].isPlaying()){
                                sounds[0].stop();
                            }
                            sounds[0].start();
                            m_paintPath.setColor(dot.getPaint().getColor());
                            //dot.getPaint().setColor(Color.BLACK);
                        }
                    }
                }
                invalidate();
            }
            else if( event.getAction() == MotionEvent.ACTION_MOVE){
                if( m_moving){

                    int col = xToCol(x);
                    int row = yToRow(y);

                    boolean touchedWithinBoard = (row <= (NUM_DOTS - 1) && col <= (NUM_DOTS - 1));

                    // If the touch coordinates are within the board,
                    // and the
                    if( touchedWithinBoard && !m_cellPath.isEmpty()){

                        Dot dot = m_dots.get(col).get(row);
                        Point lastPoint = m_cellPath.get(m_cellPath.size() - 1);
                        if((col != lastPoint.x || row != lastPoint.y))
                        {
                            boolean colorMatchesLast = lastDotColorMatches(dot);
                            boolean dotWithinReach = dotWithinReach(dot);

                            // If the dot is of the same color, is within reach, and hasn't
                            // been touched, add it
                            if(colorMatchesLast && dotWithinReach){
                                // If the dot has been touched, check if the user is
                                // backtracking/undoing his selection
                                if(userIsBackTracking(col,row)){
                                    m_cellPath.remove(m_cellPath.size()-1);
                                    m_dotsTouched.remove(m_dotsTouched.size()-1);
                                }
                                else{
                                    m_cellPath.add(new Point(col, row));
                                    m_dotsTouched.add(dot);
                                    int soundIndex = m_dotsTouched.size()-1;

                                    // If the index is out of bounds, set it to the last sound
                                    if(soundIndex > sounds.length-1) soundIndex = sounds.length-1;

                                    if(sounds[soundIndex].isPlaying()){
                                        sounds[soundIndex].stop();
                                    }
                                    sounds[soundIndex].start();
                                }
                            }
                        }
                    }
                    invalidate();
                }
            }
            else if( event.getAction() == MotionEvent.ACTION_UP){
                m_moving = false;

                if(m_dotsTouched.size() > 1) {
                    m_score += m_dotsTouched.size();
                    m_moves--;
                    m_gameHandler.setView(m_moves ,m_score);
                    if(m_moves == 0){
                        endGame();
                    }
                }

                if (m_dotsTouched.size() > 1) {
                    updateBoard();
                }

                m_cellPath.clear();
                m_dotsTouched.clear();
                invalidate();
            }

        return true;
    }

    public void updateBoard(){
        ArrayList<Integer> wholeDotRows = new ArrayList<>();
        ArrayList<Dot> wholeDots = new ArrayList<>();
        int countTotal = 0;
        int currCol = 0;

        for(List<Dot> col: m_dots){
            for(Dot dot : col){
                currCol = col.get(col.indexOf(dot)).getCol();
                if(!m_dotsTouched.contains(dot)){
                    int count = getTouchedCountBelow(dot);
                    if(col.indexOf(dot) == 0 && count == 0){
                        break;
                    }
                    countTotal += count;
                    wholeDots.add(dot);
                    wholeDotRows.add(count);
                }
            }

            if(countTotal != 0){
                for(int dot = wholeDots.size() - 1; dot >= 0; dot--){
                    Dot dotToMove = wholeDots.get(dot);
                    int amount = wholeDotRows.get(wholeDots.indexOf(dotToMove));
                    // move the color of the dot amount down the column
                    if(amount != 0){
                        moveDotDown(dotToMove, amount);
                    }
                }
            }

            int touchedInColumnCount = getTouchedInColumn(currCol);
            fillColWithRandom(currCol, touchedInColumnCount);

            countTotal = 0;
            wholeDotRows.clear();
            wholeDots.clear();
        }
    }

    int getTouchedInColumn(int col){
        int count = 0;
        for(Dot dot : m_dotsTouched){
            if(dot.getCol() == col) count++;
        }
        return count;
    }

    /* Counts and returns the amount of dots that have been touched below a given dot */
    int getTouchedCountBelow(Dot dot){
        int dotRow = dot.getRow();
        int dotCol = dot.getCol();
        int count = 0;
        for(int currDot = dotRow + 1; currDot < NUM_DOTS; currDot++){
            Dot suspect = m_dots.get(dotCol).get(currDot);
            if(m_dotsTouched.contains(suspect)){
                count++;
            }
        }

        return count;
    }

    /* Fills a column with random colors from the top row to the row that it stops at   */
    void fillColWithRandom(int col ,int stopRow){
        for(int dot = 0; dot < stopRow; dot++){
            m_dots.get(col).get(dot).getPaint().setColor(getRandomColor());
        }
    }

    void moveDotDown(Dot dot, int amount){
        Dot dotToChange = m_dots.get(dot.getCol()).get(dot.getRow() + amount);
        dotToChange.getPaint().setColor(dot.getPaint().getColor());
        dotToChange.colorIndex = dot.colorIndex;
    }

    public boolean lastDotColorMatches(Dot dot){
        Dot lastDot = m_dotsTouched.get(m_dotsTouched.size() - 1);
        int lastDotColor = lastDot.getPaint().getColor();
        int dotColor = dot.getPaint().getColor();
        return lastDotColor == dotColor;
    }

    public boolean userIsBackTracking(int touchCol, int touchRow){
        if(m_dotsTouched.size() >= 2) {
            Dot nextLastDot = m_dotsTouched.get(m_dotsTouched.size() - 2);

            return nextLastDot.getCol() == touchCol && nextLastDot.getRow() == touchRow;
        }
        else{
            return false;
        }
    }

    // Returns true if the dot is next to and in line to the last dot touched
    public boolean dotWithinReach(Dot dot){
        Dot lastDot = m_dotsTouched.get(m_dotsTouched.size() - 1);

        int lastDotCol = lastDot.getCol();
        int lastDotRow = lastDot.getRow();
        int dotCol = dot.getCol();
        int dotRow = dot.getRow();

        int up = lastDotRow - 1;
        int down = lastDotRow + 1;
        int right = lastDotCol + 1;
        int left = lastDotCol - 1;

        if(dotCol == left && dotRow == lastDotRow) return true;
        if(dotCol == right && dotRow == lastDotRow) return true;
        if(dotCol == lastDotCol && dotRow == up) return true;
        if(dotCol == lastDotCol && dotRow == down) return true;
        return false;
    }

    public void setGameHandler(GameHandler handler){
        m_gameHandler = handler;
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
        for(int row = 0; row < NUM_DOTS; ++row){
            for(int col = 0; col < NUM_DOTS; ++col){

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

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    void snapToGrid(RectF circle){
        int col = xToCol((int) circle.left);
        int row = yToRow((int) circle.top);
        float x = colToX(col) + (m_cell_width - circle.width())/2;
        float y = rowToY(row) + (m_cell_height - circle.height())/2.0f;
        circle.offsetTo(x, y);

        //circle.offsetTo(colToX(col), rowToY(row));
    }

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

    private void endGame(){
       // m_scoreHandler.setView(80085, 80085);
        m_gameHandler.endGame(m_score);
    }

    /*
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
    }*/

     /*public Dot getDotAbove(Dot dot){
        int dotRow = dot.getRow();

        if(dotRow == 0){
            Dot newDot = dot;
            newDot.getPaint().setColor(getRandomColor());
            newDot.colorIndex = colorIndex;
            return newDot;
            //return new Dot(dotX, dotY, dotRow, dotCol, touchSize, dotSize, randColor, colorIndex);
        }
        else{
            Dot newDot = m_dots.get(dot.getCol()).get(dot.getRow()-1);

            return new Dot(dot.getX(), dot.getY(), dot.getRow(), dot.getCol(), dot.getTouchSize(),
                    dot.getDotDrawSize(), newDot.getPaint().getColor(), newDot.colorIndex);
        }
    }*/

}
