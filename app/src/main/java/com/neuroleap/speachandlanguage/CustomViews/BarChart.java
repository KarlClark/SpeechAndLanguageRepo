package com.neuroleap.speachandlanguage.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.neuroleap.speachandlanguage.R;

/**
 * Created by Karl on 6/17/2015.
 */
public class BarChart extends View {
    private float mAxisLabelTextSize = 22;
    private float mTickMarkTextSize = 15;
    private float mBarLabelTextSize = 15;
    private int mTextColor = Color.BLACK;
    private int mAxisColor = Color.BLACK;
    private int mBarColor = Color.BLUE;
    private float mBarTextAngle = 30.0f;
    private String mYAxisLabel = "";
    private String mXAxisLabel = "";
    private String[] mTickmarkLabels = new String[] {"0", "20", "40", "60", "80", "100"};
    private float[] mTickmarkLabelWidths;
    private int mNumberOfMinorTickmarks = 1; // number of tick marks between labeled tick marks
    private float mMajorTickmarkLength = dipOrSpToPixels(10, "dp");
    private float mMinorTickmarkLength = dipOrSpToPixels(5, "dp");
    private float mBarLabelOffset = dipOrSpToPixels(5, "dp");
    private float mMaxTickMarkLabelWidth;
    private float mTickmarkLabelHeight;
    private Paint mAxisPaint, mAxisLabelTextPaint, mTickmarkPaint, mTickmarkLabelPaint, mBarLabelPaint, mBarPaint;
    private float mTotalWidth,mTotalHeight;
    private Rect mXLabelBounds = new Rect();
    private Rect mYLabelBounds = new Rect();
    private float mXAxisOffset, mYAxisOffset;
    private Path mYAxisTextPath= new Path();
    private float mBarLabelHorizontalSpace, mBarLabelVerticalSpace, mMaxBarLabelHeight, mMaxBarLabelHeightMinusBottom;
    private float[] mBarValues = new float[] {20,30, 50};
    private String[] mBarLabels = new String[] {"xxx","yyy","ggg"};
    private static final String TAG ="## My Info ##";

    public BarChart(Context context){
        super(context);
        initBarChart();
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
        initBarChart();
    }

    public BarChart(Context context, AttributeSet attrs,int defaultStyle){
        super(context, attrs, defaultStyle);
        getAttributes(context, attrs);
        initBarChart();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Save the width and height of our view and set the measured dimension
        // to the maximum value.
        mTotalWidth = measure(widthMeasureSpec);
        mTotalHeight = measure(heightMeasureSpec);

        setMeasuredDimension((int) mTotalWidth, (int) mTotalHeight);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED){
            result = 200;
        }else {
            result=specSize;
        }
        return result;
    }

    private void initBarChart(){
        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setColor(mAxisColor);
        mAxisPaint.setStrokeWidth(5);
        mAxisPaint.setStyle(Paint.Style.STROKE);

        mAxisLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisLabelTextPaint.setColor(mTextColor);
        mAxisLabelTextPaint.setTextSize(mAxisLabelTextSize);
        mAxisLabelTextPaint.setStrokeWidth(3);
        Log.i(TAG, "here1 X axis label= " + mXAxisLabel);
        mAxisLabelTextPaint.getTextBounds(mXAxisLabel, 0, mXAxisLabel.length(), mXLabelBounds);
        mAxisLabelTextPaint.getTextBounds(mYAxisLabel, 0, mYAxisLabel.length(), mYLabelBounds);
        Log.i(TAG, "xheight= " + mXLabelBounds.height() + "  xtop= " + mXLabelBounds.top + "  xbottom= " + mXLabelBounds.bottom);
        Log.i(TAG, "yheight= " + mYLabelBounds.height() + "  ytop= " + mYLabelBounds.top + "  ybottom= " + mYLabelBounds.bottom);

        mTickmarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickmarkPaint.setColor(mAxisColor);
        mTickmarkPaint.setStrokeWidth(5);
        mTickmarkPaint.setStyle(Paint.Style.STROKE);

        mTickmarkLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickmarkLabelPaint.setColor(mTextColor);
        mTickmarkLabelPaint.setTextSize(mTickMarkTextSize);
        mTickmarkLabelPaint.setStrokeWidth(3);
        mMaxTickMarkLabelWidth = 0;
        Rect rect = new Rect();
        mTickmarkLabelWidths = new float[mTickmarkLabels.length];
        for (int i = 0; i < mTickmarkLabels.length; i++){
            mTickmarkLabelPaint.getTextBounds(mTickmarkLabels[i], 0, mTickmarkLabels[i].length(), rect);
            mTickmarkLabelWidths[i] = rect.width();
            if(rect.width() > mMaxTickMarkLabelWidth){
                mMaxTickMarkLabelWidth = rect.width();
            }
            mTickmarkLabelHeight = rect.height();
        }

        mBarLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarLabelPaint.setColor(mTextColor);
        mBarLabelPaint.setTextSize(mBarLabelTextSize);
        mBarLabelPaint.setStrokeWidth(3);
        getBarLabelSpacing();

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);
    }

    public void getAttributes(Context context, AttributeSet attrs){

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarChart, 0, 0);
        String s;

        s = a.getString(R.styleable.BarChart_axisLabelTextSize);
        if (s == null){
            mAxisLabelTextSize = dipOrSpToPixels(22f, "dp");
        }else{
            mAxisLabelTextSize = getPixels(s);
        }

        s = a.getString(R.styleable.BarChart_tickMarkTextSize);
        if (s == null){
            mTickMarkTextSize = dipOrSpToPixels(15f, "dp");
        }else{
            mTickMarkTextSize = getPixels(s);
        }

        s = a.getString(R.styleable.BarChart_barLabelTextsize);
        if (s == null){
            mBarLabelTextSize = dipOrSpToPixels(15f, "dp");
        }else{
            mBarLabelTextSize = getPixels(s);
        }

        mTextColor = a.getColor(R.styleable.BarChart_textColor, Color.BLACK);
        mAxisColor = a.getColor(R.styleable.BarChart_axisColor, Color.BLACK);
        mBarColor = a.getColor(R.styleable.BarChart_barColor, Color.BLUE);
        mBarTextAngle = a.getFloat(R.styleable.BarChart_barTextAngle, 45);

        mYAxisLabel = a.getString(R.styleable.BarChart_yAxisLabel);
        mXAxisLabel = a.getString(R.styleable.BarChart_xAxisLabel);
        Log.i(TAG, "here2 X axis label= " + mXAxisLabel);

    }

    @Override
    protected void onDraw(Canvas canvas){

        mXAxisOffset = mXLabelBounds.height() + mBarLabelVerticalSpace + mBarLabelOffset + dipOrSpToPixels(15,"dp");
        Log.i(TAG,"mXAxisOffset= " + mXAxisOffset);
        mYAxisOffset = mYLabelBounds.height() + mMajorTickmarkLength + mMaxTickMarkLabelWidth + 5;

        canvas.drawText(mXAxisLabel, (mTotalWidth - mYAxisOffset - mXLabelBounds.width())/2 + mYAxisOffset, mTotalHeight - mXLabelBounds.bottom, mAxisLabelTextPaint);
        canvas.drawLine(mYAxisOffset, 0, mYAxisOffset, mTotalHeight - mXAxisOffset, mAxisPaint);
        canvas.drawLine(mYAxisOffset, mTotalHeight - mXAxisOffset, mTotalWidth, mTotalHeight - mXAxisOffset, mAxisPaint);

        float lengthForTickmarks = mTotalHeight - mXAxisOffset - mTickmarkLabelHeight/2;
        float numberOfTickmarks = mTickmarkLabels.length + mNumberOfMinorTickmarks*mTickmarkLabels.length - mNumberOfMinorTickmarks;
        float tickmarkSpacing = lengthForTickmarks/(numberOfTickmarks-1);
        float length;
        int j = 0;
        for (int i = 0; i<numberOfTickmarks; i++){
            if (i % (mNumberOfMinorTickmarks+1) == 0){
                length = mMajorTickmarkLength;
                canvas.drawText(mTickmarkLabels[j],mYAxisOffset-length-mTickmarkLabelWidths[j], mTotalHeight-mXAxisOffset-(i*tickmarkSpacing)+mTickmarkLabelHeight/2,
                                mTickmarkLabelPaint);
                j++;
            }else{
                length = mMinorTickmarkLength;
            }
            canvas.drawLine(mYAxisOffset-length , mTotalHeight-mXAxisOffset-(i*tickmarkSpacing),
                    mYAxisOffset, mTotalHeight-mXAxisOffset-(i*tickmarkSpacing), mTickmarkPaint);
        }


        Log.i(TAG, "mTotalHeight= " + mTotalHeight);
        mYAxisTextPath.moveTo(mYLabelBounds.height() - mYLabelBounds.bottom, mTotalHeight - mXAxisOffset);
        mYAxisTextPath.lineTo(mYLabelBounds.height() - mYLabelBounds.bottom, 0);
        float pathLength = mTotalHeight - mXAxisOffset;
        canvas.drawTextOnPath(mYAxisLabel, mYAxisTextPath, (pathLength - mYLabelBounds.width()) / 2, 0, mAxisLabelTextPaint);

        float lengthForBars = mTotalWidth - mYAxisOffset - mBarLabelHorizontalSpace;
        float units = 3 * mBarValues.length;  // each bar take 2 units, space between bars are 1 unit;
        float spacePerUnit = lengthForBars/units;
        float range = Float.parseFloat(mTickmarkLabels[mTickmarkLabels.length-1]);
        for (int i = 0; i < mBarValues.length; i++){
            Log.i(TAG, "drawRect called");
            canvas.drawRect(mYAxisOffset+((3*i + 1) * spacePerUnit),  mTotalHeight-mXAxisOffset - (mBarValues[i]/range)* lengthForTickmarks,
                    mYAxisOffset+((3*i +3) * spacePerUnit),mTotalHeight-mXAxisOffset, mBarPaint);
        }

        float cos = (float)Math.cos(Math.toRadians(mBarTextAngle));
        float sin = (float)Math.sin(Math.toRadians(mBarTextAngle));
        float yPoint = mTotalHeight - mXAxisOffset + mBarLabelOffset + mMaxBarLabelHeight*cos;
        float xPoint = mYAxisOffset - mMaxBarLabelHeightMinusBottom*sin/2;
        canvas.rotate(mBarTextAngle, xPoint, yPoint);
        for (int i = 0; i < mBarLabels.length; i++){
            canvas.drawText(mBarLabels[i], xPoint+(3*i+2)*spacePerUnit*cos, yPoint-(3*i +2)*spacePerUnit*sin, mBarLabelPaint);
        }
        //canvas.drawText("xxxxxxx", mYAxisOffset, yPoint, mAxisLabelTextPaint);
        //canvas.drawText("dfkhhkjj",mYAxisOffset+100*cos,yPoint-100*sin, mAxisLabelTextPaint);
       // canvas.drawText("aaaaaaa",mYAxisOffset+200*cos,yPoint-200*sin, mAxisLabelTextPaint);
    }

    private float getPixels(String textSize){
        String numberPart = textSize.replaceAll("[^0-9]", "");
        float value = Long.parseLong(numberPart);
        String alphaPart = textSize.replaceAll("[0-9]", "");
        if ( ! alphaPart.equals("")){
            return dipOrSpToPixels(value, alphaPart);
        }else{
            return value;
        }
    }

    private float dipOrSpToPixels(float value, String dpOrSp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        if (dpOrSp.equals("dp") || dpOrSp.equals("dip")) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
        }else{
            if (dpOrSp.equals("sp")){
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics);
            }else{
                return 22;
            }
        }
    }

    private void getBarLabelSpacing() {
        mBarLabelVerticalSpace = 0;
        mMaxBarLabelHeight = 0;
        mMaxBarLabelHeightMinusBottom = 0;
        float len;
        float height;
        float bottom;
        Rect rect = new Rect();
        for(int i = 0; i < mBarLabels.length; i++){
            mBarLabelPaint.getTextBounds(mBarLabels[i], 0, mBarLabels[i].length(), rect);
            len= rect.width()*(float)Math.sin(Math.toRadians(mBarTextAngle));
            Log.i(TAG, "rect.width= " + rect.width() +" len= " + len);
            if (len > mBarLabelVerticalSpace){
                mBarLabelVerticalSpace = len;
            }
            height = rect.height();
            bottom = rect.bottom;
            Log.i(TAG,"Label = " + mBarLabels[i] +" height= "+ height + ("  top= " + rect.top + "  Bottom= " + rect.bottom));
            if (height > mMaxBarLabelHeight){
                mMaxBarLabelHeight = height;
            }
            if (height-bottom > mMaxBarLabelHeightMinusBottom){
                mMaxBarLabelHeightMinusBottom = height - bottom;
            }
            mBarLabelHorizontalSpace = rect.width()*(float)Math.cos(Math.toRadians(mBarTextAngle));  // just use the last label.
        }
        Log.i(TAG ,"mBarLabelVeritcalSpace = " + mBarLabelVerticalSpace);
        mBarLabelVerticalSpace += mMaxBarLabelHeight * (float)Math.cos(Math.toRadians(mBarTextAngle));
        Log.i(TAG ,"mBarLabelVeritcalSpace = " + mBarLabelVerticalSpace);
    }

    public void setBarValues(float[] barValues) {
        Log.i(TAG,"setBarValues called");
        mBarValues = barValues;
        invalidate();
        requestLayout();
    }

    public void setBarLables(String[] barLabels) {
        Log.i(TAG, "setBarLabels called");
        mBarLabels = barLabels;
        getBarLabelSpacing();
        invalidate();
        requestLayout();
    }
}
