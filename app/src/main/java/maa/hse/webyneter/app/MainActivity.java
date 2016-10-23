package maa.hse.webyneter.app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import maa.hse.webyneter.app.tasks.ImmutableTaskDescriptionsAdapter;
import maa.hse.webyneter.app.tasks.TaskDescription;
import maa.hse.webyneter.app.tasks.task1.GestureHandlerActivity;
import maa.hse.webyneter.app.tasks.task2.GestureBuilderActivity;
import maa.hse.webyneter.app.tasks.task3.SensorDataChartingActivity;
import maa.hse.webyneter.app.tasks.task4.GpsActivity;
import maa.hse.webyneter.app.tasks.task5.ThirdPartyImageGalleryActivity;
import maa.hse.webyneter.app.tasks.task6.ContactsActivity;
import maa.hse.webyneter.app.tasks.task7.SpeechToTextActivity;

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
        lvTaskDescriptions.setAdapter(new ImmutableTaskDescriptionsAdapter(this, new TaskDescription[]{
                new TaskDescription(rs.getString(R.string.task1_name),
                        R.mipmap.ic_task1,
                        rs.getString(R.string.task1_desc)),

                new TaskDescription(rs.getString(R.string.task2_name),
                        R.mipmap.ic_task2,
                        rs.getString(R.string.task2_desc)),

                new TaskDescription(rs.getString(R.string.task3_name),
                        R.mipmap.ic_task3,
                        rs.getString(R.string.task3_desc)),

                new TaskDescription(rs.getString(R.string.task4_name),
                        R.mipmap.ic_task4,
                        rs.getString(R.string.task4_desc)),

                new TaskDescription(rs.getString(R.string.task5_name),
                        R.mipmap.ic_task5,
                        rs.getString(R.string.task5_desc)),

                new TaskDescription(rs.getString(R.string.task6_name),
                        R.mipmap.ic_task6,
                        rs.getString(R.string.task6_desc)),

                new TaskDescription(rs.getString(R.string.task7_name),
                        R.mipmap.ic_task7,
                        rs.getString(R.string.task7_desc)),
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
                redirectToActivity(GestureHandlerActivity.class);
                break;
            case 1:
                redirectToActivity(GestureBuilderActivity.class);
                break;
            case 2:
                redirectToActivity(SensorDataChartingActivity.class);
                break;
            case 3:
                redirectToActivity(GpsActivity.class);
                break;
            case 4:
                redirectToActivity(ThirdPartyImageGalleryActivity.class);
                break;
            case 5:
                redirectToActivity(ContactsActivity.class);
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
