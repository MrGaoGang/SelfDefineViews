package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.ChargeBubbleView;

public class ChargeBubbleActivity extends AppCompatActivity {

    ChargeBubbleView mChargeBubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_bubble);
        mChargeBubbleView = (ChargeBubbleView) findViewById(R.id.charge);
        mChargeBubbleView.setProgress(20);


    }
}
