package mrgao.com.mrgaoviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.activitys.ExtendsViewActivity;
import mrgao.com.mrgaoviews.activitys.ViewGroupActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((Button) findViewById(R.id.selftView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExtendsViewActivity.class));
            }
        });
        ((Button) findViewById(R.id.selfViewGroup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewGroupActivity.class));
            }
        });
    }


}
