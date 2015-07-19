package com.eagzzycsl.drawerlayouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class CalendarView extends ViewGroup {
    //view自由发挥的大小，即没有限定view大小的时候view的大小，比如wrapContent的时候
    private final int defaultWidth = 240;
    private final int defaultHeight = 3000;
    //最终经过计算后得到的view的大小
    private int myWidth;
    private int myHeight;

    private Business[] bs;
    //
    private int viewCount = 0;


    public void setBusiness(Business[] bs) {
        this.bs=bs;
    }

    private void showAddBusiness(float eventY) {
        //显示一个类似谷歌日历的新建活动的view

        int y = (int) eventY;
        int poor = y - lineStart;
        //当点击的范围在那24个条的范围时才显示view
        if (poor > 0 && poor < (24 * height1h + 25 * lineWidth)) {

//判断点击的地方属于哪个条，对于一个条，上面的线属于这个条，下面的线不属于这个条
// 但是当在这个条上面画view的时候上下两个线都属于这个条，这样才可以实现覆盖的效果
            int i = poor / (height1h + lineWidth);
            int addBusinessViewTop = lineStart + (height1h + lineWidth) * i;
            int addBusinessViewBottom = lineStart + (height1h + lineWidth) * (i + 1);
            View v = getChildAt(viewCount);
            v.layout(lineLeft, addBusinessViewTop, lineRight, addBusinessViewBottom);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //如果给定了大小的话就用给定的大小，否则用自己默认的大小
        myWidth = (widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : defaultWidth;
        myHeight = (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : defaultHeight;
        setMeasuredDimension(myWidth, myHeight);

    }

    //如果线宽是奇数的话字大小宜为奇数，反之偶数
    private int lineWidth = 1;//线宽
    private int textSize = 11;//文本大小
    private int height1h = 120;//一个条的高度，不包括上下的线
    private int lineStart = 80 + 1;//线的起始高度
    private int textStart = 80 + 1 + (textSize - lineWidth) / 2;//文字的起始高度
    private int textPadLeft = 10;
    private int linePadLeft = 10;
    private int lineLeft = textPadLeft + 5 * textSize + linePadLeft;
    private int lineRight = myWidth - linePadLeft;//此处定义不够

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setStrokeWidth(lineWidth);//线宽
        //据说线宽和字号会冲突，但是目前没有发现
//        System.out.println("textStart:" + textStart);
//        System.out.println("textSize:" + textSize);
//        System.out.println("lineWidth:" + lineWidth);
//        canvas.drawText("hello",50,41,paint);
        int lineY;
        for (int i = 0; i <= 24; i++) {
            canvas.drawText(String.format("%02d", i) + ":00", textPadLeft, textStart + (height1h + lineWidth) * i, paint);
            lineY = lineStart + (height1h + lineWidth) * i;
            lineRight = myWidth - linePadLeft;
            canvas.drawLine(lineLeft, lineY, lineRight, lineY, paint);
        }
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Button button = new Button(getContext());
        button.setText("+");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "show text", Toast.LENGTH_SHORT).show();
            }
        });

        addView(button);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        if(bs!=null){
            for(int i=0;i<bs.length;i++){
                Button businessView=new Button(getContext());
                businessView.setText(bs[i].getName());
                addView(businessView);
                int hpm;
                hpm = height1h/60;
                int bt=lineStart-1+lineWidth+bs[i].getStart().getHour()*(height1h+lineWidth)+bs[i].getStart().getMinute()*hpm;
                int bm=lineLeft-1+lineWidth+bs[i].getEnd().getHour()*(height1h+lineWidth)+bs[i].getEnd().getMinute()*hpm;
                getChildAt(i+1).layout(lineLeft,bt,lineRight,bm);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (event.getEventTime() - event.getDownTime() < 200) {
//                    Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
//                    addView(new Button(getContext()), new LayoutParams(20, 40));

                    showAddBusiness(event.getY());
//                    viewCount++;
                }
                break;
        }

        return true;
        //警告：如果不return true的话会有问题，但是如果返回true了会不会对别的造成影响。
//        return super.onTouchEvent(event);

    }


}

class Business {

    private String name;
    private MyTime start;
    private MyTime end;

    public Business(String name, MyTime start, MyTime end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return this.name;
    }

    public MyTime getStart() {
        return this.start;

    }

    public MyTime getEnd() {
        return this.end;
    }
}

class MyDate {
    int year;
    int month;
    int day;

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}

class MyTime {
    private int hour;
    private int minute;

    public MyTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return this.hour;

    }

    public int getMinute() {
        return this.minute;
    }
    public int toMinutes(){
        return this.getHour()*60+this.getMinute();
    }
    public int compareTo(MyTime t) {
        return this.toMinutes()-t.toMinutes();
    }

}