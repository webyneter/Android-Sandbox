package androidsandbox.org.webyneter.app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidsandbox.org.webyneter.app.features.FeatureDescription;
import androidsandbox.org.webyneter.app.features.ImmutableFeatureDescriptionsAdapter;
import androidsandbox.org.webyneter.app.features.feature1.HandlingGesturesActivity;
import androidsandbox.org.webyneter.app.features.feature2.GestureBuilderActivity;
import androidsandbox.org.webyneter.app.features.feature3.PlottingEnvironmentSensorsDataActivity;
import androidsandbox.org.webyneter.app.features.feature4.HandlingGpsDataActivity;
import androidsandbox.org.webyneter.app.features.feature5.ImageLoadingActivity;
import androidsandbox.org.webyneter.app.features.feature6.AddressBookActivity;
import androidsandbox.org.webyneter.app.features.feature7.SpeechToTextActivity;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    private ListView lvFeatureDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUiVariables();
        initUiHandlers();

        Resources rs = getResources();
        lvFeatureDescriptions.setAdapter(new ImmutableFeatureDescriptionsAdapter(this, new FeatureDescription[]{
                new FeatureDescription(rs.getString(R.string.feature1_name),
                        R.mipmap.ic_task1,
                        rs.getString(R.string.feature1_desc)),

                new FeatureDescription(rs.getString(R.string.feature2_name),
                        R.mipmap.ic_task2,
                        rs.getString(R.string.feature2_desc)),

                new FeatureDescription(rs.getString(R.string.feature3_name),
                        R.mipmap.ic_task3,
                        rs.getString(R.string.feature3_desc)),

                new FeatureDescription(rs.getString(R.string.feature4_name),
                        R.mipmap.ic_task4,
                        rs.getString(R.string.feature4_desc)),

                new FeatureDescription(rs.getString(R.string.feature5_name),
                        R.mipmap.ic_task5,
                        rs.getString(R.string.feature5_desc)),

                new FeatureDescription(rs.getString(R.string.feature6_name),
                        R.mipmap.ic_task6,
                        rs.getString(R.string.feature6_desc)),

                new FeatureDescription(rs.getString(R.string.feature7_name),
                        R.mipmap.ic_task7,
                        rs.getString(R.string.feature7_desc)),
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
        lvFeatureDescriptions = (ListView) findViewById(R.id.main_lvTaskDescriptions);
    }

    private void initUiHandlers() {
        lvFeatureDescriptions.setOnItemClickListener(this);
    }

    private void redirectToActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }
}
