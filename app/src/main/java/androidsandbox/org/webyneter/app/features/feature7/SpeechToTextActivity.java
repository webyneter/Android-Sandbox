package androidsandbox.org.webyneter.app.features.feature7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

import androidsandbox.org.webyneter.app.R;

public class SpeechToTextActivity extends AppCompatActivity
        implements RecognitionListener {
    final private int RECORD_AUDIO_REQUEST_CODE = 4242;

    private Button btnRequestRecordAudioPermission;
    private ToggleButton tbtnRecognition;
    private ProgressBar pbRecognition;
    private TextView tvErrorMsg;
    private ListView lvRecognizedSpeech;
    private ArrayAdapter<String> lvRecognizedSpeechAdapter;
    private SpeechRecognizer recognizer = null;

    private static String getRecognizerErrorText(final int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                return "Audio recording error!";
            case SpeechRecognizer.ERROR_CLIENT:
                return "Client side error!";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "Insufficient permissions!";
            case SpeechRecognizer.ERROR_NETWORK:
                return "Network error!";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "Network timeout!";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "No match!";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "Recognition service is busy!";
            case SpeechRecognizer.ERROR_SERVER:
                return "Error from server!";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "No speech input!";
            default:
                return "Didn't understand, please try again...";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature7);

        initUiVariables();

        lvRecognizedSpeechAdapter = new ArrayAdapter<>(this, R.layout.fragment_feature7_recognition_result,
                R.id.feature7_tvRecognitionResult);
        lvRecognizedSpeech.setAdapter(lvRecognizedSpeechAdapter);

        btnRequestRecordAudioPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SpeechToTextActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                        RECORD_AUDIO_REQUEST_CODE);
            }
        });

        updateUiIfRecordAudioPermissionGrantedOrDenied();

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(this);

        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        tbtnRecognition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startingListening();
                    recognizer.startListening(intent);
                } else {
                    stoppingListening();
                    recognizer.stopListening();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recognizer != null) {
            recognizer.destroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (recordAudioPermissionGranted()) {
            updateUiControls(true);
        }
    }

    private boolean recordAudioPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        tvErrorMsg.setVisibility(View.GONE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        pbRecognition.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        tbtnRecognition.setChecked(false);
        pbRecognition.setIndeterminate(true);
    }

    @Override
    public void onError(int error) {
        tvErrorMsg.setText(getRecognizerErrorText(error));
        tvErrorMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResults(Bundle results) {
        float[] confidence_scores = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        float max_confidence_score = Float.MIN_VALUE;
        int ind_max_confidence_score = 0;
        for (int i = 0; i < confidence_scores.length; ++i) {
            float cs = confidence_scores[i];
            if (cs > max_confidence_score) {
                max_confidence_score = cs;
                ind_max_confidence_score = i;
            }
        }

        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        lvRecognizedSpeechAdapter.add(matches.get(ind_max_confidence_score));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private void initUiVariables() {
        btnRequestRecordAudioPermission = (Button) findViewById(R.id.feature7_btnRequestRecordAudioPermission);
        tbtnRecognition = (ToggleButton) findViewById(R.id.feature7_tbtnRecognition);
        pbRecognition = (ProgressBar) findViewById(R.id.feature7_pbRecognition);
        tvErrorMsg = (TextView) findViewById(R.id.feature7_tvErrorMsg);
        lvRecognizedSpeech = (ListView) findViewById(R.id.feature7_lvRecognizedSpeech);
    }

    private void updateUiControls(final boolean isEnabled) {
        tbtnRecognition.setEnabled(isEnabled);
        pbRecognition.setEnabled(isEnabled);
        lvRecognizedSpeech.setEnabled(isEnabled);
    }

    private void updateUiIfRecordAudioPermissionGrantedOrDenied() {
        if (recordAudioPermissionGranted()) {
            btnRequestRecordAudioPermission.setVisibility(View.GONE);
            updateUiControls(true);
        } else {
            btnRequestRecordAudioPermission.setVisibility(View.VISIBLE);
            updateUiControls(false);
        }
    }

    private void startingListening() {
        tvErrorMsg.setVisibility(View.GONE);
        pbRecognition.setIndeterminate(true);
        pbRecognition.setVisibility(View.VISIBLE);
    }

    private void stoppingListening() {
        pbRecognition.setIndeterminate(false);
        pbRecognition.setVisibility(View.INVISIBLE);
    }
}