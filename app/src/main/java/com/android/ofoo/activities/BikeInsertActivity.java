package com.android.ofoo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ofoo.R;
import com.android.ofoo.bean.BikeBean;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.manager.ActivitiesManager;
import com.android.ofoo.sql.DbHelper;
import com.android.ofoo.util.DateTimeUtils;
import com.android.ofoo.util.SpannableStringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BikeInsertActivity extends BaseActivity {

    private String strBikeNo;
    @Bind(R.id.tv_bike_no)
    TextView mTvBikeNo;
    @Bind(R.id.edt_bike_pass)
    EditText mEdtBikePass;
    @Bind(R.id.btn_bike_insert)
    Button mBtnBikeInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bike_insert);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {
        strBikeNo = getIntent().getStringExtra(Constant.EXT_BIKE_NO);
        String sup = String.format(getResources().getString(R.string.tv_bike_no), strBikeNo);
        mTvBikeNo.setText(SpannableStringUtils.style(sup, strBikeNo, new CharacterStyle() {
            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setTextSize(getResources().getDimension(R.dimen.text_size_20sp));
                paint.setColor(getResources().getColor(R.color.common_red));
            }
        }));
    }

    @Override
    protected void initializeEvent() {
        super.initializeEvent();
        mEdtBikePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mBtnBikeInsert.setEnabled(s.length() >  0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBtnBikeInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strBikePass = mEdtBikePass.getText().toString().trim();
                if(strBikePass.length() != 4){
                    Toast.makeText(BikeInsertActivity.this, "解锁码为4位数字", Toast.LENGTH_LONG);
                }else{
                    BikeBean bean = new BikeBean();
                    bean.setBikeNo(strBikeNo);
                    bean.setBikePass(strBikePass);
                    bean.setCreateTime(DateTimeUtils.parse(new Date(), "yyyy-MM-dd HH:mm"));
                    bean.setUpdateTime(DateTimeUtils.parse(new Date(), "yyyy-MM-dd HH:mm"));
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("bike_no", strBikeNo);
                    int result = new DbHelper<BikeBean>().create(BikeInsertActivity.this, bean);
                    //int result =  new DbHelper<BikeBean>().createIfNotExists(BikeInsertActivity.this, bean, where);
                    if(result == 1){
                        Toast.makeText(BikeInsertActivity.this, "插入解锁码成功", Toast.LENGTH_LONG);
                        ActivitiesManager.finishAllToMain();
                        finish();
                    }else{
                        Toast.makeText(BikeInsertActivity.this, "插入解锁码失败，请稍后再试", Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
