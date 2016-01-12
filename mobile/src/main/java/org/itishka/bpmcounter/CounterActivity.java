package org.itishka.bpmcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedList;

public class CounterActivity extends AppCompatActivity {


    private static final int MAX_COUNT = 36;
    private static final int MIN_COUNT = 3;
    private static final long MAX_DELAY = 10000;

    LinkedList<Long> mValues = new LinkedList<>();
    long mLastDown = 0;
    long mLastUp = 0;
    long mCount = 0;
    private TextView mCurrentBpmView;
    private TextView mCurrentCountView;
    private TextView mAverageBpmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        mAverageBpmView = (TextView) findViewById(R.id.average_bpm);
        mCurrentBpmView = (TextView) findViewById(R.id.current_bpm);
        mCurrentCountView = (TextView) findViewById(R.id.current_count);

        findViewById(R.id.imageButton1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonPressed(v);
                        break;
                    case MotionEvent.ACTION_UP:
                        buttonReleased(v);
                }
                return false;
            }
        });
    }

    private void buttonReleased(View v) {
        long now = System.currentTimeMillis();
        Log.d("CounterActivity", String.format("Pressed: %s, delay: %s", now - mLastDown, now - mLastUp));

        mCount++;
        mCurrentCountView.setText(String.valueOf(mCount));

        if (mLastUp != 0) {
            long delay = now - mLastUp;
            mValues.add(delay);
            if (mValues.size() > MAX_COUNT)
                mValues.pop();
            mCurrentBpmView.setText(String.valueOf(delayToBpm(delay)));
        }
        if (mValues.size()>MIN_COUNT) {
            mAverageBpmView.setText(String.valueOf(delayToBpm(average(mValues))));
        }
        mLastUp = now;

    }

    private Long delayToBpm(Long delay) {
        return 60*1000/delay;
    }
    private Long average(Collection<Long> values) {
        long sum = 0;
        for (Long v : values) {
            sum += v;
        }
        return sum / values.size();
    }

    private void buttonPressed(View v) {
        mLastDown = System.currentTimeMillis();
    }
}
