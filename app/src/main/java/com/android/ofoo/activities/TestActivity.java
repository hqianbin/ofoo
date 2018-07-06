package com.android.ofoo.activities;

import android.os.Bundle;

import com.android.ofoo.R;
import com.android.ofoo.bean.ParentBean;
import com.android.ofoo.bean.SubBean;

public class TestActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);
        super.onCreate(savedInstanceState);

    }

    protected void initializeData(){
        SubBean c = new SubBean();
        ParentBean b = c;

        System.out.println("====parent b.s = " + b.s);
        System.out.println("====parent b.m = " + b.m);
        b.staticTest();

        System.out.println("====child c.s = " + c.s);
        System.out.println("====child c.m = " + c.m);
        c.staticTest();

    }

    @Override
    protected void initializeView() {

    }

    protected void initializeEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed(){
        super.onBackPressed();
    }
}
