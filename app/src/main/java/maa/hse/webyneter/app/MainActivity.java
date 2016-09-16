package maa.hse.webyneter.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import maa.hse.webyneter.app.task1.Task1Activity;
import maa.hse.webyneter.app.task4.Task4Activity;
import maa.hse.webyneter.app.task6.Task6Activity;

// TODO: 9/15/2016 preemptively request all necessary permissions
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar bar = getSupportActionBar();
//        if (bar != null) {
//            bar.setHomeButtonEnabled(true);
//            bar.setDisplayShowHomeEnabled(true);
//        }


        android.app.ActionBar t = getActionBar();

        redirectToActivityOnButtonClick(R.id.btnTask1, Task1Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask2, Task2Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask3, Task3Activity.class);
        redirectToActivityOnButtonClick(R.id.btnTask4, Task4Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask5, Task5Activity.class);
        redirectToActivityOnButtonClick(R.id.btnTask6, Task6Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask7, Task7Activity.class);
    }

    private void redirectToActivityOnButtonClick(final int buttonId,
                                                 final Class<?> redirectToActivityClass) {
        final Button btn = (Button) findViewById(buttonId);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, redirectToActivityClass);
                startActivity(intent);
            }
        });
    }
}
