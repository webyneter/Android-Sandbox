package androidsandbox.org.webyneter.app.features.feature3;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import androidsandbox.org.webyneter.app.R;
import androidsandbox.org.webyneter.app.util.TrackerHelper;

public class PlottingEnvironmentSensorsDataActivity extends Activity
        implements SensorEventListener, SeekBar.OnSeekBarChangeListener {
    private static final String PRESSURE_SENSOR_DATA_SET_LABEL = "Pressure (mBar) Sensor";
    private static final String AMBIENT_TEMPERATURE_SENSOR_DATA_SET_LABEL = "Ambient Temperature (°C) Sensor";
    private static final String LIGHT_SENSOR_DATA_SET_LABEL = "Light (lx) Sensor";
    private static final String RELATIVE_HUMIDITY_SENSOR_DATA_SET_LABEL = "Relative Humidity (%) Sensor";
    private static final int VISIBLE_X_RANGE_MAXIMUM_MIN = 1;
    private static final int VISIBLE_X_RANGE_MAXIMUM_MAX = 125;
    private static final float VISIBLE_X_RANGE_MAXIMUM_DEFAULT_PERCENT = 0.25f;

    private LineChart lcPressure;
    private LineChart lcAmbientTemperature;
    private LineChart lcLight;
    private LineChart lcRelativeHumidity;
    private TextView tvVisibleXRangeMaximum;
    private SeekBar sbVisibleXRangeMaximum;

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private Sensor ambientTemperatureSensor;
    private Sensor lightSensor;
    private Sensor relativeHumiditySensor;

    private LineDataSet pressureDataSet = new LineDataSet(new ArrayList<Entry>(), PRESSURE_SENSOR_DATA_SET_LABEL);
    private LineDataSet ambientTemperatureDataSet = new LineDataSet(new ArrayList<Entry>(),
            AMBIENT_TEMPERATURE_SENSOR_DATA_SET_LABEL);
    private LineDataSet lightDataSet = new LineDataSet(new ArrayList<Entry>(), LIGHT_SENSOR_DATA_SET_LABEL);
    private LineDataSet relativeHumidityDataSet = new LineDataSet(new ArrayList<Entry>(),
            RELATIVE_HUMIDITY_SENSOR_DATA_SET_LABEL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature3);

        initUiVariables();
        initUiHandlers();
        initSensors();
        initUiCharts();
        initUiState();
    }

    private void initUiVariables() {
        tvVisibleXRangeMaximum = (TextView) findViewById(R.id.feature3_tvVisibleXRangeMaximum);
        sbVisibleXRangeMaximum = (SeekBar) findViewById(R.id.feature3_sbVisibleXRangeMaximum);
        lcPressure = (LineChart) findViewById(R.id.feature3_lcPressure);
        lcAmbientTemperature = (LineChart) findViewById(R.id.feature3_lcAmbientTemperature);
        lcLight = (LineChart) findViewById(R.id.feature3_lcLight);
        lcRelativeHumidity = (LineChart) findViewById(R.id.feature3_lcRelativeHumidity);
    }

    private void initUiHandlers() {
        sbVisibleXRangeMaximum.setOnSeekBarChangeListener(this);
    }

    private void initUiState() {
        sbVisibleXRangeMaximum.setMax(VISIBLE_X_RANGE_MAXIMUM_MAX);
        int progress = calcPoint(VISIBLE_X_RANGE_MAXIMUM_DEFAULT_PERCENT,
                VISIBLE_X_RANGE_MAXIMUM_MIN,
                VISIBLE_X_RANGE_MAXIMUM_MAX);
        sbVisibleXRangeMaximum.setProgress(progress);
    }

    private int calcPoint(float minShiftPercent, int min, int max) {
        return Math.round(minShiftPercent * (max - min) + min);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    private void initUiCharts() {
        initUiChartOrSetNoDataText(pressureSensor, lcPressure, pressureDataSet);
        initUiChartOrSetNoDataText(ambientTemperatureSensor, lcAmbientTemperature, ambientTemperatureDataSet);
        initUiChartOrSetNoDataText(lightSensor, lcLight, lightDataSet);
        initUiChartOrSetNoDataText(relativeHumiditySensor, lcRelativeHumidity, relativeHumidityDataSet);
    }

    private void initUiChartOrSetNoDataText(Sensor sensor, LineChart lineChart, LineDataSet lineDataSet) {
        if (sensor != null) {
            initUiChart(lineChart, lineDataSet);
        } else {
            lineChart.setNoDataText(String.format("%s not available!", lineDataSet.getLabel()));
        }
    }

    private void initUiChart(LineChart lineChart, LineDataSet lineDataSet) {
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        customizeUiChart(lineChart);

        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackerHelper.sendWithDefaultTracker(this, "onResume");
        registerSensorListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TrackerHelper.sendWithDefaultTracker(this, "onPause");
        unregisterSensorListeners();
    }

    private void registerSensorListeners() {
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterSensorListeners() {
        sensorManager.unregisterListener(this, pressureSensor);
        sensorManager.unregisterListener(this, ambientTemperatureSensor);
        sensorManager.unregisterListener(this, lightSensor);
        sensorManager.unregisterListener(this, relativeHumiditySensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PRESSURE:
                float mBar = event.values[0];
                TrackerHelper.sendWithDefaultTracker(this, "onSensorChanged:TYPE_PRESSURE=" + String.valueOf(mBar));
                addValueToUiChart(mBar, pressureDataSet, lcPressure);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                float degreeCelsius = event.values[0];
                TrackerHelper.sendWithDefaultTracker(this, "onSensorChanged:TYPE_AMBIENT_TEMPERATURE="
                        + String.valueOf(degreeCelsius));
                addValueToUiChart(degreeCelsius, ambientTemperatureDataSet, lcAmbientTemperature);
                break;
            case Sensor.TYPE_LIGHT:
                float lx = event.values[0];
                TrackerHelper.sendWithDefaultTracker(this, "onSensorChanged:TYPE_LIGHT=" + String.valueOf(lx));
                addValueToUiChart(lx, lightDataSet, lcLight);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                float percent = event.values[0];
                TrackerHelper.sendWithDefaultTracker(this, "onSensorChanged:TYPE_RELATIVE_HUMIDITY="
                        + String.valueOf(percent));
                addValueToUiChart(percent, relativeHumidityDataSet, lcRelativeHumidity);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        TrackerHelper.sendWithDefaultTracker(this, "onAccuracyChanged:accuracy=" + String.valueOf(accuracy));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TrackerHelper.sendWithDefaultTracker(this, "onProgressChanged:progress=" + String.valueOf(progress));
        if (progress < VISIBLE_X_RANGE_MAXIMUM_MIN) {
            sbVisibleXRangeMaximum.setProgress(VISIBLE_X_RANGE_MAXIMUM_MIN);
            return;
        }
        tvVisibleXRangeMaximum.setText(Integer.toString(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        TrackerHelper.sendWithDefaultTracker(this, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        TrackerHelper.sendWithDefaultTracker(this, "onStopTrackingTouch");
    }

    private void addValueToUiChart(float value, LineDataSet lineDataSet, LineChart lineChart) {
        Entry newEntry = createEntryOneXShiftFromLastOrZeroXShift(lineDataSet, value);
        lineDataSet.addEntry(newEntry);

        lineChart.getLineData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        lineChart.setVisibleXRangeMaximum(sbVisibleXRangeMaximum.getProgress());
        lineChart.moveViewToX(getLastEntryOrNull(lineDataSet).getX());
    }

    private Entry createEntryOneXShiftFromLastOrZeroXShift(LineDataSet lineDataSet, float y) {
        Entry lastEntry = getLastEntryOrNull(lineDataSet);
        Entry newEntry;
        if (lastEntry != null) {
            newEntry = new Entry(lastEntry.getX() + 1, y);
        } else {
            newEntry = new Entry(0, y);
        }
        return newEntry;
    }

    private Entry getLastEntryOrNull(LineDataSet lineDataSet) {
        int entryCount = lineDataSet.getEntryCount();
        if (entryCount == 0) {
            return null;
        }
        return lineDataSet.getEntryForIndex(entryCount - 1);
    }

    private void customizeUiChart(LineChart lineChart) {
        int whiteColor = Integer.MAX_VALUE;

        LineData lineData = lineChart.getLineData();
        lineData.setValueTextColor(whiteColor);

        lineChart.setDescription("");
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        legend.setTextColor(whiteColor);
        legend.setTextSize(14f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(whiteColor);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setTextColor(whiteColor);
    }
}
