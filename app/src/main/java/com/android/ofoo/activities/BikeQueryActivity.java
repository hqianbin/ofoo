package com.android.ofoo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ofoo.R;
import com.android.ofoo.bean.BikeBean;
import com.android.ofoo.constant.Constant;
import com.android.ofoo.sql.DbHelper;
import com.android.ofoo.util.SpannableStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BikeQueryActivity extends BaseActivity {

    @Bind(R.id.edt_bike_no)
    EditText mEdtBikeNO;
    @Bind(R.id.btn_bike_query)
    Button mBtnBikeQuery;
    @Bind(R.id.tv_bike_show)
    TextView mTvBikeShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bike_query);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {
    }

    @Override
    protected void initializeEvent() {
        super.initializeEvent();
        mEdtBikeNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mBtnBikeQuery.setEnabled(s.length() >  0);
                if(s.length() == 0){
                    mTvBikeShow.setText("输入车牌号，获取解锁码");
                }else if(s.length() > 0 && s.length() < 4){
                    mTvBikeShow.setText("车牌号一般为4-8位的数字");
                }else{
                    mTvBikeShow.setText("温馨提示：若输错车牌号，将无法打开车锁");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBtnBikeQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strBikeNO = mEdtBikeNO.getText().toString().trim();
                if(strBikeNO.length() < 4){
                    Toast.makeText(BikeQueryActivity.this, "请输入正确的车牌号", Toast.LENGTH_LONG);
                }else{
                    List<BikeBean> bikeList =  new DbHelper<BikeBean>().queryForEq(BikeQueryActivity.this, BikeBean.class, "bike_no", strBikeNO);
                    if(bikeList == null || bikeList.size() == 0){
                        Intent intent = new Intent(BikeQueryActivity.this, BikeInsertActivity.class);
                        intent.putExtra(Constant.EXT_BIKE_NO, strBikeNO);
                        startActivity(intent);
                    }else{
                        String sup = String.format(getString(R.string.tv_bike_pass), strBikeNO, bikeList.get(0).getBikePass());
                        mTvBikeShow.setText(SpannableStringUtils.style(sup, bikeList.get(0).getBikePass(), new CharacterStyle() {
                            @Override
                            public void updateDrawState(TextPaint paint) {
                                paint.setTextSize(getResources().getDimension(R.dimen.text_size_20sp));
                                paint.setColor(getResources().getColor(R.color.common_red));
                            }
                        }));
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
