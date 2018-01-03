package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.HuaWeiProgressView;

public class HuaWeiProgressActivity extends AppCompatActivity {


    HuaWeiProgressView mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hua_wei_progress);
        mProgressView = (HuaWeiProgressView) findViewById(R.id.progress);
        mProgressView.setProgress(60);
    }
}
