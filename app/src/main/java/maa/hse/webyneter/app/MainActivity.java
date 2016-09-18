package maa.hse.webyneter.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import maa.hse.webyneter.app.task1.Task1Activity;
import maa.hse.webyneter.app.task2.Task2Activity;
import maa.hse.webyneter.app.task4.Task4Activity;
import maa.hse.webyneter.app.task6.Task6Activity;

// TODO: 9/15/2016 preemptively request all necessary permissions
public class MainActivity extends AppCompatActivity {

    private Button btnTask1;
    private Button btnTask2;
    private Button btnTask3;
    private Button btnTask4;
    private Button btnTask5;
    private Button btnTask6;
    private Button btnTask7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUiControls();

//        ActionBar bar = getSupportActionBar();
//        if (bar != null) {
//            bar.setHomeButtonEnabled(true);
//            bar.setDisplayShowHomeEnabled(true);
//        }

//        android.app.ActionBar t = getActionBar();

        redirectToActivityOnButtonClick(btnTask1, Task1Activity.class);
        redirectToActivityOnButtonClick(btnTask2, Task2Activity.class);
//        redirectToActivityOnButtonClick(btnTask3, Task3Activity.class);
        redirectToActivityOnButtonClick(btnTask4, Task4Activity.class);
//        redirectToActivityOnButtonClick(btnTask5, Task5Activity.class);
        redirectToActivityOnButtonClick(btnTask6, Task6Activity.class);
//        redirectToActivityOnButtonClick(btnTask7, Task7Activity.class);
    }

    private void initializeUiControls() {
        btnTask1 = (Button) findViewById(R.id.main_btnTask1);
        btnTask2 = (Button) findViewById(R.id.main_btnTask2);
        btnTask3 = (Button) findViewById(R.id.main_btnTask3);
        btnTask4 = (Button) findViewById(R.id.main_btnTask4);
        btnTask5 = (Button) findViewById(R.id.main_btnTask5);
        btnTask6 = (Button) findViewById(R.id.main_btnTask6);
        btnTask7 = (Button) findViewById(R.id.main_btnTask7);
    }

    private void redirectToActivityOnButtonClick(final Button btn, final Class<?> redirectToActivityClass) {
        btn.setEnabled(true);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, redirectToActivityClass);
                startActivity(intent);
            }
        });
    }
}
