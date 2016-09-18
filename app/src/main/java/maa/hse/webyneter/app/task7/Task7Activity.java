package maa.hse.webyneter.app.task7;

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

import maa.hse.webyneter.app.R;

public class Task7Activity extends AppCompatActivity implements RecognitionListener {
    final private int RECORD_AUDIO_REQUEST_CODE = 666;

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
        setContentView(R.layout.activity_task7);

        initializeUiControls();

        lvRecognizedSpeechAdapter = new ArrayAdapter<>(this, R.layout.fragment_task7_recognition_result, R.id.task7_tvRecognitionResult);
        lvRecognizedSpeech.setAdapter(lvRecognizedSpeechAdapter);

        btnRequestRecordAudioPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Task7Activity.this, new String[]{Manifest.permission.RECORD_AUDIO},
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
            updateUi(true);
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

    private void initializeUiControls() {
        btnRequestRecordAudioPermission = (Button) findViewById(R.id.task7_btnRequestRecordAudioPermission);
        tbtnRecognition = (ToggleButton) findViewById(R.id.task7_tbtnRecognition);
        pbRecognition = (ProgressBar) findViewById(R.id.task7_pbRecognition);
        tvErrorMsg = (TextView) findViewById(R.id.task7_tvErrorMsg);
        lvRecognizedSpeech = (ListView) findViewById(R.id.task7_lvRecognizedSpeech);
    }

    private void updateUi(final boolean isEnabled) {
        tbtnRecognition.setEnabled(isEnabled);
        pbRecognition.setEnabled(isEnabled);
        lvRecognizedSpeech.setEnabled(isEnabled);
    }

    private void updateUiIfRecordAudioPermissionGrantedOrDenied() {
        if (recordAudioPermissionGranted()) {
            btnRequestRecordAudioPermission.setVisibility(View.GONE);
            updateUi(true);
        } else {
            btnRequestRecordAudioPermission.setVisibility(View.VISIBLE);
            updateUi(false);
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


//public class VoiceRecognitionActivity extends Activity implements
//        RecognitionListener {
//
//    private TextView returnedText;
//    private ToggleButton toggleButton;
//    private ProgressBar progressBar;
//    private SpeechRecognizer speech = null;
//    private Intent recognizerIntent;
//    private String LOG_TAG = "VoiceRecognitionActivity";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        returnedText = (TextView) findViewById(R.id.textView1);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
//        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
//
//        progressBar.setVisibility(View.INVISIBLE);
//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
//                "en");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
//
//        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminate(true);
//                    speech.startListening(recognizerIntent);
//                } else {
//                    progressBar.setIndeterminate(false);
//                    progressBar.setVisibility(View.INVISIBLE);
//                    speech.stopListening();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (speech != null) {
//            speech.destroy();
//            Log.i(LOG_TAG, "destroy");
//        }
//
//    }
//
//    @Override
//    public void onBeginningOfSpeech() {
//        Log.i(LOG_TAG, "onBeginningOfSpeech");
//        progressBar.setIndeterminate(false);
//        progressBar.setMax(10);
//    }
//
//    @Override
//    public void onBufferReceived(byte[] buffer) {
//        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
//    }
//
//    @Override
//    public void onEndOfSpeech() {
//        Log.i(LOG_TAG, "onEndOfSpeech");
//        progressBar.setIndeterminate(true);
//        toggleButton.setChecked(false);
//    }
//
//    @Override
//    public void onError(int errorCode) {
//        String errorMessage = getErrorText(errorCode);
//        Log.d(LOG_TAG, "FAILED " + errorMessage);
//        returnedText.setText(errorMessage);
//        toggleButton.setChecked(false);
//    }
//
//    @Override
//    public void onEvent(int arg0, Bundle arg1) {
//        Log.i(LOG_TAG, "onEvent");
//    }
//
//    @Override
//    public void onPartialResults(Bundle arg0) {
//        Log.i(LOG_TAG, "onPartialResults");
//    }
//
//    @Override
//    public void onReadyForSpeech(Bundle arg0) {
//        Log.i(LOG_TAG, "onReadyForSpeech");
//    }
//
//    @Override
//    public void onResults(Bundle results) {
//        Log.i(LOG_TAG, "onResults");
//        ArrayList<String> matches = results
//                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        String text = "";
//        for (String result : matches)
//            text += result + "\n";
//
//        returnedText.setText(text);
//    }
//
//    @Override
//    public void onRmsChanged(float rmsdB) {
//        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);
//    }
//
//}