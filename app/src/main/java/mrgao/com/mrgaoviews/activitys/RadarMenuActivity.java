package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.RadarMenuView;

public class RadarMenuActivity extends AppCompatActivity {

    private RadarMenuView mRadarMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_menu);
        mRadarMenuView=(RadarMenuView)findViewById(R.id.radarView);
        mRadarMenuView.setOnMenuItemClickListener(new RadarMenuView.onMenuItemClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                Toast.makeText(RadarMenuActivity.this,"tag="+itemView.getTag()+" position="+position,Toast.LENGTH_LONG).show();
            }
        });
    }
}
