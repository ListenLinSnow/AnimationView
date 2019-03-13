package com.example.administrator.animationviews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.animationviews.R;
import com.example.administrator.animationviews.customview.SnowView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnowActivity extends AppCompatActivity {

    @BindView(R.id.snow_snow_view)
    SnowView snowView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_snow);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        snowView.onRelease();
    }
}
