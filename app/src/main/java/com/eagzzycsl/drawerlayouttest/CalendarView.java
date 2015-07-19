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
    //view���ɷ��ӵĴ�С����û���޶�view��С��ʱ��view�Ĵ�С������wrapContent��ʱ��
    private final int defaultWidth = 240;
    private final int defaultHeight = 3000;
    //���վ��������õ���view�Ĵ�С
    private int myWidth;
    private int myHeight;

    private Business[] bs;
    //
    private int viewCount = 0;


    public void setBusiness(Business[] bs) {
        this.bs=bs;
    }

    private void showAddBusiness(float eventY) {
        //��ʾһ�����ƹȸ��������½����view

        int y = (int) eventY;
        int poor = y - lineStart;
        //������ķ�Χ����24�����ķ�Χʱ����ʾview
        if (poor > 0 && poor < (24 * height1h + 25 * lineWidth)) {

//�жϵ���ĵط������ĸ���������һ������������������������������߲����������
// ���ǵ�����������滭view��ʱ�����������߶�����������������ſ���ʵ�ָ��ǵ�Ч��
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
        //��������˴�С�Ļ����ø����Ĵ�С���������Լ�Ĭ�ϵĴ�С
        myWidth = (widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : defaultWidth;
        myHeight = (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : defaultHeight;
        setMeasuredDimension(myWidth, myHeight);

    }

    //����߿��������Ļ��ִ�С��Ϊ��������֮ż��
    private int lineWidth = 1;//�߿�
    private int textSize = 11;//�ı���С
    private int height1h = 120;//һ�����ĸ߶ȣ����������µ���
    private int lineStart = 80 + 1;//�ߵ���ʼ�߶�
    private int textStart = 80 + 1 + (textSize - lineWidth) / 2;//���ֵ���ʼ�߶�
    private int textPadLeft = 10;
    private int linePadLeft = 10;
    private int lineLeft = textPadLeft + 5 * textSize + linePadLeft;
    private int lineRight = myWidth - linePadLeft;//�˴����岻��

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setStrokeWidth(lineWidth);//�߿�
        //��˵�߿���ֺŻ��ͻ������Ŀǰû�з���
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
        //���棺�����return true�Ļ��������⣬�����������true�˻᲻��Ա�����Ӱ�졣
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