package com.android.ofoo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.android.ofoo.R;
import com.android.ofoo.service.UpdateCheckService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.rl_splash)
    RelativeLayout mRlSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {
        startService(new Intent(this, UpdateCheckService.class));
    }

    @Override
    protected void initializeEvent() {
        // load animation
        Animation animation = new AlphaAnimation(.3f, 1f);
        animation.setDuration(2 * 1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mRlSplash.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
