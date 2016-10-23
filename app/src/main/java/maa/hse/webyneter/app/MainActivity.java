package maa.hse.webyneter.app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import maa.hse.webyneter.app.capabilities.ImmutableCapabilityDescriptionsAdapter;
import maa.hse.webyneter.app.capabilities.CapabilityDescription;
import maa.hse.webyneter.app.capabilities.capability1.HandlingGesturesActivity;
import maa.hse.webyneter.app.capabilities.capability2.GestureBuilderActivity;
import maa.hse.webyneter.app.capabilities.capability3.PlottingEnvironmentSensorsDataActivity;
import maa.hse.webyneter.app.capabilities.capability4.HandlingGpsDataActivity;
import maa.hse.webyneter.app.capabilities.capability5.ImageLoadingActivity;
import maa.hse.webyneter.app.capabilities.capability6.AddressBookActivity;
import maa.hse.webyneter.app.capabilities.capability7.SpeechToTextActivity;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private ListView lvTaskDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUiVariables();
        initUiHandlers();

        Resources rs = getResources();
        lvTaskDescriptions.setAdapter(new ImmutableCapabilityDescriptionsAdapter(this, new CapabilityDescription[]{
                new CapabilityDescription(rs.getString(R.string.cap1_name),
                        R.mipmap.ic_task1,
                        rs.getString(R.string.cap1_desc)),

                new CapabilityDescription(rs.getString(R.string.cap2_name),
                        R.mipmap.ic_task2,
                        rs.getString(R.string.cap2_desc)),

                new CapabilityDescription(rs.getString(R.string.cap3_name),
                        R.mipmap.ic_task3,
                        rs.getString(R.string.cap3_desc)),

                new CapabilityDescription(rs.getString(R.string.cap4_name),
                        R.mipmap.ic_task4,
                        rs.getString(R.string.cap4_desc)),

                new CapabilityDescription(rs.getString(R.string.cap5_name),
                        R.mipmap.ic_task5,
                        rs.getString(R.string.cap5_desc)),

                new CapabilityDescription(rs.getString(R.string.cap6_name),
                        R.mipmap.ic_task6,
                        rs.getString(R.string.cap6_desc)),

                new CapabilityDescription(rs.getString(R.string.cap7_name),
                        R.mipmap.ic_task7,
                        rs.getString(R.string.cap7_desc)),
        }));

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                redirectToActivity(HandlingGesturesActivity.class);
                break;
            case 1:
                redirectToActivity(GestureBuilderActivity.class);
                break;
            case 2:
                redirectToActivity(PlottingEnvironmentSensorsDataActivity.class);
                break;
            case 3:
                redirectToActivity(HandlingGpsDataActivity.class);
                break;
            case 4:
                redirectToActivity(ImageLoadingActivity.class);
                break;
            case 5:
                redirectToActivity(AddressBookActivity.class);
                break;
            case 6:
                redirectToActivity(SpeechToTextActivity.class);
                break;
        }
    }

    private void initUiVariables() {
        lvTaskDescriptions = (ListView) findViewById(R.id.main_lvTaskDescriptions);
    }

    private void initUiHandlers() {
        lvTaskDescriptions.setOnItemClickListener(this);
    }

    private void redirectToActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }
}
