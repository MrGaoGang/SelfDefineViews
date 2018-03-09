package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.RefreshView;

public class RefreshViewActivity extends AppCompatActivity {


    @BindView(R.id.refreshView)
    RefreshView mRefreshView;

    @BindView(R.id.pasue)
    Button pasue;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.restart)
    Button restart;
    @BindView(R.id.show)
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_view);
        ButterKnife.bind(this);


        mRefreshView.setOnProgressEndListener(new RefreshView.OnProgressEndListener() {
            @Override
            public void end() {
            show.setText("结束咯");
            }

            @Override
            public void pause() {
                show.setText("暂停");
            }

            @Override
            public void restart() {
                show.setText("重新开始");
            }
        });


    }

    @OnClick({R.id.pasue, R.id.start, R.id.restart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pasue:
                mRefreshView.pause();
                break;
            case R.id.start:
                mRefreshView.setStrokeWidth(3);
                mRefreshView.start();
                break;
            case R.id.restart:
                mRefreshView.restart();
                break;
        }
    }
}
