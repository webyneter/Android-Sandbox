package maa.hse.webyneter.app.task3;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.ArrayList;

import maa.hse.webyneter.app.R;

public class Task3Activity extends Activity implements SensorEventListener {
    private static final Duration SENSOR_CHANGED_HANDLE_DELAY = Duration.standardSeconds(15);

    private ListView lvCharts;

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private Sensor ambientTemperatureSensor;
    private Sensor lightSensor;
    private Sensor relativeHumiditySensor;

    private LineDataSet pressureDataSet = new LineDataSet(new ArrayList<Entry>(), "Pressure");
    private LineDataSet ambientTemperatureDataSet = new LineDataSet(new ArrayList<Entry>(), "Ambient Temperature");
    private LineDataSet lightDataSet = new LineDataSet(new ArrayList<Entry>(), "Light");
    private LineDataSet relativeHumidityDataSet = new LineDataSet(new ArrayList<Entry>(), "Relative Humidity");
    private Instant lastSensorChangedAt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);

        initUiVariables();

        initSensors();

        initCharts();
    }

    private void initUiVariables() {
        lvCharts = (ListView) findViewById(R.id.task3_lvCharts);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    private void initCharts() {
        LineData pressureData = new LineData(pressureDataSet);
        LineData ambientTemperatureData = new LineData(pressureDataSet);
        LineData lightData = new LineData(pressureDataSet);
        LineData relativeHumidityData = new LineData(pressureDataSet);

        ArrayList<LineChartItem> linechartItems = new ArrayList<>();
        linechartItems.add(new LineChartItem(pressureData, getApplicationContext()));
        linechartItems.add(new LineChartItem(ambientTemperatureData, getApplicationContext()));
        linechartItems.add(new LineChartItem(lightData, getApplicationContext()));
        linechartItems.add(new LineChartItem(relativeHumidityData, getApplicationContext()));

        ArrayAdapter<LineChartItem> lvChartsAdapter = new ArrayAdapter<>(getApplicationContext(), 0, linechartItems);
        lvCharts.setAdapter(lvChartsAdapter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastSensorChangedAt != null) {
            Instant nowAt = new Instant();
            Duration fromLastSensorChangedToNow = new Duration(lastSensorChangedAt, nowAt);
            if (fromLastSensorChangedToNow.isLongerThan(SENSOR_CHANGED_HANDLE_DELAY)) {
                return;
            }
        } else {
            lastSensorChangedAt = new Instant();
            return;
        }

        switch (event.sensor.getType()) {
            case Sensor.TYPE_PRESSURE:
                float mbarPressure = event.values[0];
                updatePressureChart(mbarPressure);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                break;
            case Sensor.TYPE_LIGHT:
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void updatePressureChart(float mbarPressure) {
        Entry newEntry = createEntryOneXShiftFromLastOrZeroXShift(pressureDataSet, mbarPressure);
        pressureDataSet.addEntry(newEntry);
        pressureDataSet.notifyDataSetChanged();
//        lcAudioRecording.getData().notifyDataChanged();
//        lcAudioRecording.invalidate();
    }

    private Entry createEntryOneXShiftFromLastOrZeroXShift(LineDataSet ds, float y) {
        Entry lastEntry = getLastEntryOrNull(pressureDataSet);
        Entry newEntry;
        if (lastEntry != null) {
            newEntry = new Entry(lastEntry.getX() + 1, y);
        } else {
            newEntry = new Entry(0, y);
        }
        return newEntry;
    }

    private Entry getLastEntryOrNull(LineDataSet ds) {
        int entryCount = ds.getEntryCount();
        if (entryCount == 0) {
            return null;
        }
        return ds.getEntryForIndex(entryCount - 1);
    }
}
