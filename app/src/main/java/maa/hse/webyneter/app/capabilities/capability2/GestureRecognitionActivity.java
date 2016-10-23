package maa.hse.webyneter.app.capabilities.capability2;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

import maa.hse.webyneter.app.R;

public class GestureRecognitionActivity extends Activity
        implements GestureOverlayView.OnGestureListener, GestureOverlayView.OnGesturePerformedListener {
    private static final double PREDICTION_SCORE_THRESHOLD = 1.0;

    private static GestureLibrary gestureLibrary;

    private TextView tvRecognitionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap2_gesture_recognition);

        initializeUiVariables();

        gestureLibrary = GestureBuilderActivity.getGestureLibrary();
        if (!gestureLibrary.load()) {
            finish();
        }

        GestureOverlayView gov = (GestureOverlayView) findViewById(R.id.cap2_cvCreateOverlay);
        gov.addOnGesturePerformedListener(this);
        gov.addOnGestureListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initializeUiVariables() {
        tvRecognitionResult = (TextView) findViewById(R.id.cap2_tvRecognitionResult);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            if (prediction.score > PREDICTION_SCORE_THRESHOLD) {
                tvRecognitionResult.setText(String.format("Recognized \"%s\"", prediction.name));
            } else {
                tvRecognitionResult.setText(R.string.cap2_no_match_found);
            }
        }
    }

    @Override
    public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
        tvRecognitionResult.setText("");
    }

    @Override
    public void onGesture(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
    }

    @Override
    public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
    }
}
