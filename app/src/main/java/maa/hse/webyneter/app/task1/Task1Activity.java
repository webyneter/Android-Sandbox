package maa.hse.webyneter.app.task1;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import maa.hse.webyneter.app.R;

public class Task1Activity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private TextView tvGesture;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        initializeUiVariables();

        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    private void initializeUiVariables() {
        tvGesture = (TextView) findViewById(R.id.tvGesture);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        tvGesture.setText(GestureKind.onDown.getValue());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        tvGesture.setText(GestureKind.onFling.getValue());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        tvGesture.setText(GestureKind.onLongPress.getValue());

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        tvGesture.setText(GestureKind.onScroll.getValue());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        tvGesture.setText(GestureKind.onShowPress.getValue());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        tvGesture.setText(GestureKind.onSingleTapUp.getValue());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        tvGesture.setText(GestureKind.onDoubleTap.getValue());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
//        tvGesture.setText(GestureKind.onDoubleTapEvent.getValue());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        tvGesture.setText(GestureKind.onSingleTap.getValue());
        return true;
    }

    /**
     * Created by webyn on 9/7/2016.
     */
    public enum GestureKind {
        onDown("Down"),
        onFling("Fling"),
        onLongPress("Long Press"),
        onScroll("Scroll"),
        onShowPress("Show Press"),
        onSingleTap("Single Tap"),
        onSingleTapUp("Single Tap Up"),
        onDoubleTap("Double Tap");

        private final String value;

        GestureKind(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
