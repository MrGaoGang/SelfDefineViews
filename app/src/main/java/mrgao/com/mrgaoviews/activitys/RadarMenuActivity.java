package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.group.RadarMenuView;

public class RadarMenuActivity extends AppCompatActivity {

    private RadarMenuView mRadarMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_menu);
        mRadarMenuView = (RadarMenuView) findViewById(R.id.radarView);
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams);
        imageView.setTag("我是新加的");
        imageView.setImageResource(R.mipmap.menu_phone);
        mRadarMenuView.addView(imageView);
        mRadarMenuView.setOnMenuItemClickListener(new RadarMenuView.onMenuItemClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                Toast.makeText(RadarMenuActivity.this, "tag=" + itemView.getTag() + " position=" + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}
