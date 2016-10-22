/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package maa.hse.webyneter.app.tasks.task2;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import maa.hse.webyneter.app.R;


public class GestureCreationActivity extends Activity {
    private static final float LENGTH_THRESHOLD = 120.0f;

    private Gesture gesture;
    private View btnCreateDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2_create_gesture);

        btnCreateDone = findViewById(R.id.task2_btnCreateDone);

        GestureOverlayView gov = (GestureOverlayView) findViewById(R.id.task2_cvCreateOverlay);
        gov.addOnGestureListener(new GesturesProcessor());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (gesture != null) {
            outState.putParcelable("gesture", gesture);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        gesture = savedInstanceState.getParcelable("gesture");
        if (gesture != null) {
            final GestureOverlayView overlay =
                    (GestureOverlayView) findViewById(R.id.task2_cvCreateOverlay);
            overlay.post(new Runnable() {
                public void run() {
                    overlay.setGesture(gesture);
                }
            });

            btnCreateDone.setEnabled(true);
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void addGesture(View v) {
        if (gesture != null) {
            final TextView input = (TextView) findViewById(R.id.task2_etCreateName);
            final CharSequence name = input.getText();
            if (name.length() == 0) {
                input.setError(getString(R.string.error_missing_name));
                return;
            }

            final GestureLibrary store = GestureBuilderActivity.getGestureLibrary();
            store.addGesture(name.toString(), gesture);
            store.save();

            setResult(RESULT_OK);

            final String path = new File(Environment.getExternalStorageDirectory(), "gestures").getAbsolutePath();
            Toast.makeText(this, getString(R.string.save_success, path), Toast.LENGTH_LONG).show();
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void cancelGesture(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            btnCreateDone.setEnabled(false);
            gesture = null;
        }

        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
        }

        public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
            gesture = overlay.getGesture();
            if (gesture.getLength() < LENGTH_THRESHOLD) {
                overlay.clear(false);
            }
            btnCreateDone.setEnabled(true);
        }

        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }
    }
}