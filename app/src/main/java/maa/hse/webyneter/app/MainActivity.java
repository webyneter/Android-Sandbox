package maa.hse.webyneter.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redirectToActivityOnButtonClick(R.id.btnTask1, Task1Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask2, Task2Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask3, Task3Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask4, Task4Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask5, Task5Activity.class);
//        redirectToActivityOnButtonClick(R.id.btnTask6, Task6Activity.class);
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
