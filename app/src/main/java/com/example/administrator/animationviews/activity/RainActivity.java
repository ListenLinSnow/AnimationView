package com.example.administrator.animationviews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.administrator.animationviews.R;
import com.example.administrator.animationviews.customview.RainView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RainActivity extends AppCompatActivity {

    @BindView(R.id.rain_view)
    RainView rainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rain);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rainView.onRelease();
    }
}
