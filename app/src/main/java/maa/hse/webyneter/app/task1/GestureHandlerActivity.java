package maa.hse.webyneter.app.task1;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import maa.hse.webyneter.app.R;

public class GestureHandlerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private TextView tvGesture;

    private RelativeLayout rlXandY;
    private TextView tvX;
    private TextView tvY;

    private RelativeLayout rlFlingVelocities;
    private TextView tvFlingVelocityX;
    private TextView tvFlingVelocityY;

    private RelativeLayout rlScrollDistances;
    private TextView tvScrollDistanceX;
    private TextView tvScrollDistanceY;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1);

        initUiVariables();

        gestureDetector = new GestureDetectorCompat(this, this);

        initUiHandlers();

        rlXandY.setVisibility(View.GONE);
        rlFlingVelocities.setVisibility(View.GONE);
        rlScrollDistances.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.onResumeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.onPauseActivity(this);
    }

    private void initUiHandlers() {
        gestureDetector.setOnDoubleTapListener(this);
    }

    private void initUiVariables() {
        tvGesture = (TextView) findViewById(R.id.task1_tvGesture);

        rlXandY = (RelativeLayout) findViewById(R.id.task1_rlXandY);
        tvX = (TextView) findViewById(R.id.task1_tvX);
        tvY = (TextView) findViewById(R.id.task1_tvY);

        rlFlingVelocities = (RelativeLayout) findViewById(R.id.task1_rlFlingVelocities);
        tvFlingVelocityX = (TextView) findViewById(R.id.task1_tvFlingVelocityX);
        tvFlingVelocityY = (TextView) findViewById(R.id.task1_tvFlingVelocityY);

        rlScrollDistances = (RelativeLayout) findViewById(R.id.task1_rlScrollDistances);
        tvScrollDistanceX = (TextView) findViewById(R.id.task1_tvScrollDistanceX);
        tvScrollDistanceY = (TextView) findViewById(R.id.task1_tvScrollDistanceY);
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
        handleGesture(GestureKind.onDown, event);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        handleFling(event1, event2, velocityX, velocityY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        handleGesture(GestureKind.onLongPress, event);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        handleScroll(e1, e2, distanceX, distanceY);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        handleGesture(GestureKind.onShowPress, event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        handleGesture(GestureKind.onSingleTap, event);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        handleGesture(GestureKind.onDoubleTap, event);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        handleGesture(GestureKind.onSingleTap, event);
        return true;
    }

    private void handleGesture(GestureKind gestureKind, MotionEvent event) {
        rlFlingVelocities.setVisibility(View.GONE);
        rlScrollDistances.setVisibility(View.GONE);

        tvGesture.setText(gestureKind.getValue());
        tvX.setText(Float.toString(event.getX()));
        tvY.setText(Float.toString(event.getY()));
        rlXandY.setVisibility(View.VISIBLE);
    }

    private void handleFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        rlXandY.setVisibility(View.GONE);

        tvFlingVelocityX.setText(Float.toString(velocityX));
        tvFlingVelocityY.setText(Float.toString(velocityY));
        rlScrollDistances.setVisibility(View.GONE);
        rlFlingVelocities.setVisibility(View.VISIBLE);
    }

    private void handleScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        rlXandY.setVisibility(View.GONE);

        tvScrollDistanceX.setText(Float.toString(distanceX));
        tvScrollDistanceY.setText(Float.toString(distanceY));
        rlFlingVelocities.setVisibility(View.GONE);
        rlScrollDistances.setVisibility(View.VISIBLE);
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
