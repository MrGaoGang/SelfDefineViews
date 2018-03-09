package mrgao.com.mrgaoviews.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.R;

public class ExtendsViewActivity extends AppCompatActivity {


    private Button mSimpleBtn;
    private Button mPanelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_view);


        mSimpleBtn = (Button) findViewById(R.id.simpleBtn);
        mSimpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, SimpleChartActivity.class));

            }
        });

        mPanelBtn = (Button) findViewById(R.id.panelBtn);
        mPanelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, PanelCircleActivity.class));

            }
        });
        ((Button) findViewById(R.id.huaweiBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, HuaWeiProgressActivity.class));
            }
        });

        ((Button) findViewById(R.id.jump)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, FlyBirdJumpActivity.class));
            }
        });

        ((Button) findViewById(R.id.water)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, WaterProgressActivity.class));
            }
        });

        ((Button) findViewById(R.id.qq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, QQBubbleActivity.class));
            }
        });
        ((Button) findViewById(R.id.charge)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, ChargeBubbleActivity.class));
            }
        });
        ((Button) findViewById(R.id.rotate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, RotateActivity.class));
            }
        });
        ((Button) findViewById(R.id.refreshView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExtendsViewActivity.this, RefreshViewActivity.class));
            }
        });
    }
}
