package mrgao.com.mrgaoviews.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.group.FlowLayout;

public class FlowActivity extends AppCompatActivity {

    Button mButton;
    FlowLayout mFlowLayout;
    Random mRandom = new Random();
    String[] text = new String[]{
            "加油呀",
            "会努力学习Android",
            "会努力学习自定义View",
            "努力",
            "是一步一步的",
            "哈哈哈哈啊哈哈啊啊啊啊啊"


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mButton = (Button) findViewById(R.id.add);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int m = mRandom.nextInt(5);
                TextView textView = new TextView(FlowActivity.this);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(mRandom.nextInt(10),mRandom.nextInt(20),mRandom.nextInt(10),mRandom.nextInt(15));
                textView.setBackgroundResource(R.drawable.flag_03);
                textView.setTextColor(Color.RED);
                textView.setLayoutParams(layoutParams);
                textView.setText(text[m]);
                mFlowLayout.addView(textView);
            }
        });
    }
}
