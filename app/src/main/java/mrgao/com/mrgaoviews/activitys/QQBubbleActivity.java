package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.QQBubbleView;

public class QQBubbleActivity extends AppCompatActivity {

    String TAG = "BubbleView";
    QQBubbleView mQQBubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqbubble);
        mQQBubbleView = (QQBubbleView) findViewById(R.id.qqView);
        ((Button) findViewById(R.id.resetBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQQBubbleView.resetView();
            }
        });
        mQQBubbleView.setBubbleStateListener(new QQBubbleView.BubbleStateListener() {
            @Override
            public void onDefault() {
                Log.i(TAG, "默认状态");
            }

            @Override
            public void onMove() {
                Log.i(TAG, "移动状态");
            }

            @Override
            public void onDrag() {
                Log.i(TAG, "拖拽状态");
            }

            @Override
            public void onDismiss() {
                Log.i(TAG, "消失状态");
            }
        });
    }
}
