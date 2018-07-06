package com.android.ofoo.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ofoo.R;
import com.android.ofoo.app.BuildInfo;
import com.android.ofoo.manager.ActivitiesManager;
import com.android.ofoo.service.FloatingService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.btn_use_bike)
    Button mBtnUseBike;
    @Bind(R.id.tv_version)
    TextView mTvVersion;
    @Bind(R.id.iv_query_bike)
    ImageView mIvQueryBike;

    private String strMsg;
    private String strMsg1;
    private String strMsg2;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        intent = new Intent(MainActivity.this, FloatingService.class);
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                startService(intent);
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            }
        } else {
            //SDK在23以下，不用管.
            startService(intent);
        }
    }

    protected void initializeData(){
        strMsg = "app_version=" + BuildInfo.VERSION_NAME + "\npatch_version=" + BuildInfo.PATCH_NAME;
    }

    @Override
    protected void initializeView() {
        mTvVersion.setText("v" + BuildInfo.PATCH_NAME);
    }

    protected void initializeEvent() {
        mBtnUseBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BikeQueryActivity.class));
            }
        });
        mIvQueryBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, strMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        stopService(intent);
        super.onDestroy();
    }

    public void onBackPressed(){
        super.onBackPressed();
        ActivitiesManager.finishAllActivity();
    }
}
