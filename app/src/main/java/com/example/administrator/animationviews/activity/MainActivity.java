package com.example.administrator.animationviews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.animationviews.R;
import com.example.administrator.animationviews.adapter.MainRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    private List<String> infoList;
    private List<Class> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("动画练习");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initList();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter(MainActivity.this, infoList);
        mainRecyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mainRecyclerAdapter);
    }

    private void initList(){
        activityList = new ArrayList<>();
        infoList = new ArrayList<>();

        activityList.add(RainActivity.class);
        infoList.add("下雨动画");

        activityList.add(SnowActivity.class);
        infoList.add("下雪动画");
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(MainActivity.this, activityList.get(position)));
    }
}
