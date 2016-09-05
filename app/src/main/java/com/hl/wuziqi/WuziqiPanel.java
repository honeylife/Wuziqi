package com.hl.wuziqi;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhongqi on 16/9/5.
 *
 * @param:
 * @param:
 */
public class WuziqiPanel extends View {
    private int mPanelWidth;
    private float mLineHeight;
    private int Max_LINE = 10;
    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;
    private boolean mIsWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();
    private boolean mIsGameOver;
    private boolean mIsWhiteGameOver;
    private int MAX_COUNT_IN_LINE= 5;
    public WuziqiPanel(Context context) {
        this(context, null);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setColor(0x33333333);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthModel = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getSize(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / Max_LINE;
        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (blackWin || whiteWin) {
            mIsGameOver = true;
            mIsWhiteGameOver = whiteWin;
            String text = mIsWhiteGameOver ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x= p.x;
            int y = p.y;
            boolean win = checkHorizontal(x,y,points);
            if (win) {
                return  true;
            }
             win = checkVertical(x,y,points);
            if (win) {
                return  true;
            }
             win = checkLeftDiagonal(x,y,points);
            if (win) {
                return  true;
            }
             win = checkRightDiagnoal(x,y,points);
            if (win) {
                return  true;
            }

        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x-i,y))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return  true;
        }
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x+i,y))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return  false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x,y-i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return  true;
        }
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x,y+i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return  false;
    }
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x-i,y+i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return  true;
        }
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x+i,y-i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return  false;
    }
    private boolean checkRightDiagnoal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x-i,y-i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return  true;
        }
        for (int i = 1; i <MAX_COUNT_IN_LINE ; i++) {
            if (points.contains(new Point(x+i,y+i))) {
                count++;
            }else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) {
            return true;
        }
        return  false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) *
                    mLineHeight, (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) *
                    mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) *
                    mLineHeight, (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) *
                    mLineHeight, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < Max_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
    public void reStart(){
       mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteGameOver = false;
        invalidate();

    }
private  static final  String INSTANCE = "instance";
private  static final  String INSTANCE_GAVW_OVER = "instance_gave_over";
private  static final  String INSTANCE_WHITE_ARRAY = "instance_white_array";
private  static final  String INSTANCE_BLACK_ARRAY = "instance_black_array";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bunble = new Bundle();
        bunble.putParcelable(INSTANCE,super.onSaveInstanceState());
        bunble.putBoolean(INSTANCE_GAVW_OVER,mIsGameOver);
        bunble.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bunble.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bunble;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bunble= (Bundle) state;
            mIsGameOver = bunble.getBoolean(INSTANCE_GAVW_OVER);
            mWhiteArray = bunble.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bunble.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bunble.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
