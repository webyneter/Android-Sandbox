package androidsandbox.org.webyneter.app.capabilities.capability4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidsandbox.org.webyneter.app.R;

public class HandlingGpsDataActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42;

    private TextView btnRequestGpsPermission;
    private TextView tvGpsStatus;
    private TextView tvLongitude;
    private TextView tvLongitudeValue;
    private TextView tvLatitude;
    private TextView tvLatitudeValue;
    private TextView tvAltitude;
    private TextView tvAltitudeValue;
    private TextView tvAccuracy;
    private TextView tvAccuracyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap4);

        initUiVariables();

        btnRequestGpsPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(HandlingGpsDataActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        requestGPSUpdatesIfGPSPermissionGranted();
        toggleUIEnabledIfGPSPermissionGrantedOrDenied();
    }

    private void initUiVariables() {
        btnRequestGpsPermission = (Button) findViewById(R.id.cap4_btnRequestGpsPermission);
        tvGpsStatus = (TextView) findViewById(R.id.cap4_tvGpsStatus);
        tvLongitude = (TextView) findViewById(R.id.cap4_tvLongitude);
        tvLongitudeValue = (TextView) findViewById(R.id.cap4_tvLongitudeValue);
        tvLatitude = (TextView) findViewById(R.id.cap4_tvLatitude);
        tvLatitudeValue = (TextView) findViewById(R.id.cap4_tvLatitudeValue);
        tvAltitude = (TextView) findViewById(R.id.cap4_tvAltitude);
        tvAltitudeValue = (TextView) findViewById(R.id.cap4_tvAltitudeValue);
        tvAccuracy = (TextView) findViewById(R.id.cap4_tvAccuracy);
        tvAccuracyValue = (TextView) findViewById(R.id.cap4_tvAccuracyValue);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeGPSUpdatesIfGPSPermissionGranted();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestGPSUpdatesIfGPSPermissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestGPSUpdatesIfGPSPermissionGranted();
        toggleUIEnabledIfGPSPermissionGrantedOrDenied();
    }

    @Override
    public void onLocationChanged(Location location) {
        populateValues(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                tvGpsStatus.setVisibility(View.VISIBLE);
                tvGpsStatus.setText("GPS is temporarily unavailable");
                toggleUIEnabled(false);
            }
            case LocationProvider.AVAILABLE: {
                tvGpsStatus.setVisibility(View.GONE);
                toggleUIEnabled(true);
            }
            case LocationProvider.OUT_OF_SERVICE: {
                tvGpsStatus.setVisibility(View.VISIBLE);
                tvGpsStatus.setText("GPS is out of service");
                toggleUIEnabled(false);
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        final LocationManager lm = getLocationManager();
        Location lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            populateValues(lastKnownLocation);
        }
        tvGpsStatus.setVisibility(View.GONE);
        toggleUIEnabled(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        tvGpsStatus.setText("GPS is off");
        tvGpsStatus.setVisibility(View.VISIBLE);
        toggleUIEnabled(false);
        clearLatitudeAndLongitudeValues();
    }


    private boolean GPSPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private LocationManager getLocationManager() {
        return (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void requestGPSUpdatesIfGPSPermissionGranted() {
        if (!GPSPermissionGranted()) {
            return;
        }
        final LocationManager lm = getLocationManager();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void removeGPSUpdatesIfGPSPermissionGranted() {
        if (!GPSPermissionGranted()) {
            return;
        }
        LocationManager lm = getLocationManager();
        lm.removeUpdates(this);
    }

    private void populateValues(Location location) {
        tvLatitudeValue.setText(String.valueOf(location.getLatitude()));
        tvLongitudeValue.setText(String.valueOf(location.getLongitude()));
        tvAltitudeValue.setText(String.valueOf(location.getAltitude()));
        tvAccuracyValue.setText(String.valueOf(location.getAccuracy()));
    }

    private void clearLatitudeAndLongitudeValues() {
        tvLatitudeValue.setText("");
        tvLongitudeValue.setText("");
        tvAltitudeValue.setText("");
        tvAccuracyValue.setText("");
    }

    private void toggleUIEnabled(final boolean isEnabled) {
        tvLatitude.setEnabled(isEnabled);
        tvLatitudeValue.setEnabled(isEnabled);
        tvLongitude.setEnabled(isEnabled);
        tvLongitudeValue.setEnabled(isEnabled);
        tvAltitude.setEnabled(isEnabled);
        tvAltitudeValue.setEnabled(isEnabled);
        tvAccuracy.setEnabled(isEnabled);
        tvAccuracyValue.setEnabled(isEnabled);
    }

    private void toggleUIEnabledIfGPSPermissionGrantedOrDenied() {
        if (GPSPermissionGranted()) {
            toggleUIEnabled(true);
            btnRequestGpsPermission.setVisibility(View.GONE);
        } else {
            toggleUIEnabled(false);
            btnRequestGpsPermission.setVisibility(View.VISIBLE);
        }
    }
}