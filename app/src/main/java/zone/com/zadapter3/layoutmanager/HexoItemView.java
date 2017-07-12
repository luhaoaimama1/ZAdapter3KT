package zone.com.zadapter3.layoutmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class HexoItemView extends View {

    private final int centerX;
    private final int centerY;
    private int r;
    private Paint mPaint;
    private Path mDrawPath;
    private Region mRegion;

    public HexoItemView(Context context) {
        this(context, null, 0);
    }

    public HexoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Card, defStyleAttr, 0);
        r = ta.getDimensionPixelSize(R.styleable.Card_size, 10);
        mPaint.setColor(ta.getColor(R.styleable.Card_bgColor, 0));
        ta.recycle();

        int h = (int) (r * Math.sin(Math.toRadians(60)));

        centerX = r;
        centerY = h;
        mRegion = new Region();
        mDrawPath = new Path();

        for (int i = 0; i < 6; i++) {
            int x = (int) ((Math.cos(Math.toRadians(180 + 60 * i)) * r)) + centerX;
            int y = (int) ((Math.sin(Math.toRadians(180 + 60 * i)) * r)) + centerY;
            if (i == 0)
                mDrawPath.moveTo(x, y);
            else
                mDrawPath.lineTo(x, y);
        }
        mDrawPath.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(2 * centerX, 2 * centerY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isEventInPath(event)) {
                return false;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean isEventInPath(MotionEvent event) {
        RectF bounds = new RectF();
        mDrawPath.computeBounds(bounds, true);
        mRegion.setPath(mDrawPath, new Region((int) bounds.left,
                (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return mRegion.contains((int) event.getX(), (int) event.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(mDrawPath, mPaint);
    }

    public void setCardColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}
