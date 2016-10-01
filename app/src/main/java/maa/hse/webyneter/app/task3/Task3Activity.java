package maa.hse.webyneter.app.task3;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

import maa.hse.webyneter.app.R;

public class Task3Activity extends Activity implements SensorEventListener {
    private static final String PRESSURE_SENSOR_DATA_SET_LABEL = "Pressure Sensor";
    private static final String AMBIENT_TEMPERATURE_SENSOR_DATA_SET_LABEL = "Ambient Temperature Sensor";
    private static final String LIGHT_SENSOR_DATA_SET_LABEL = "Light Sensor";
    private static final String RELATIVE_HUMIDITY_SENSOR_DATA_SET_LABEL = "Relative Humidity Sensor";

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private Sensor ambientTemperatureSensor;
    private Sensor lightSensor;
    private Sensor relativeHumiditySensor;

    private LineChart lcPressure;
    private LineChart lcAmbientTemperature;
    private LineChart lcLight;
    private LineChart lcRelativeHumidity;

    private LineDataSet pressureDataSet = new LineDataSet(new ArrayList<Entry>(), PRESSURE_SENSOR_DATA_SET_LABEL);
    private LineDataSet ambientTemperatureDataSet = new LineDataSet(new ArrayList<Entry>(), AMBIENT_TEMPERATURE_SENSOR_DATA_SET_LABEL);
    private LineDataSet lightDataSet = new LineDataSet(new ArrayList<Entry>(), LIGHT_SENSOR_DATA_SET_LABEL);
    private LineDataSet relativeHumidityDataSet = new LineDataSet(new ArrayList<Entry>(), RELATIVE_HUMIDITY_SENSOR_DATA_SET_LABEL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);

        initUiVariables();
        initSensors();
        setUpCharts();
    }

    private void initUiVariables() {
        lcPressure = (LineChart) findViewById(R.id.task3_lcPressure);
        lcAmbientTemperature = (LineChart) findViewById(R.id.task3_lcAmbientTemperature);
        lcLight = (LineChart) findViewById(R.id.task3_lcLight);
        lcRelativeHumidity = (LineChart) findViewById(R.id.task3_lcRelativeHumidity);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    private void setUpCharts() {
        Utils.init(getApplicationContext());
        setUpChartOrSetNoDataText(pressureSensor, lcPressure, pressureDataSet);
        setUpChartOrSetNoDataText(ambientTemperatureSensor, lcAmbientTemperature, ambientTemperatureDataSet);
        setUpChartOrSetNoDataText(lightSensor, lcLight, lightDataSet);
        setUpChartOrSetNoDataText(relativeHumiditySensor, lcRelativeHumidity, relativeHumidityDataSet);
    }

    private void setUpChartOrSetNoDataText(Sensor sensor, LineChart lineChart, LineDataSet lineDataSet) {
        if (sensor != null) {
            setUpChart(lineChart, lineDataSet);
        } else {
            lineChart.setNoDataText(String.format("%s not available!", lineDataSet.getLabel()));
        }
    }

    private void setUpChart(LineChart lineChart, LineDataSet lineDataSet) {
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        customizeChartAndData(lineChart, lineData);
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                float mbar = event.values[0];
                addValueToChart(mbar, pressureDataSet, lcPressure);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                float degreeCelsius = event.values[0];
                addValueToChart(degreeCelsius, ambientTemperatureDataSet, lcAmbientTemperature);
                break;
            case Sensor.TYPE_LIGHT:
                float lx = event.values[0];
                addValueToChart(lx, lightDataSet, lcLight);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                float percent = event.values[0];
                addValueToChart(percent, relativeHumidityDataSet, lcRelativeHumidity);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void addValueToChart(float value, LineDataSet lineDataSet, LineChart lineChart) {
        Entry newEntry = createEntryOneXShiftFromLastOrZeroXShift(lineDataSet, value);
        lineDataSet.addEntry(newEntry);
        lineChart.getLineData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
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

    private void customizeChartAndData(LineChart lineChart, LineData lineData) {
        int whiteColor = Integer.MAX_VALUE;
        lineChart.setDescription("");
        lineChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        lineChart.getLegend().setTextColor(whiteColor);
        lineChart.getXAxis().setTextColor(whiteColor);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisLeft().setTextColor(whiteColor);
        lineChart.getAxisRight().setTextColor(whiteColor);
        lineData.setValueTextColor(whiteColor);
    }
}
