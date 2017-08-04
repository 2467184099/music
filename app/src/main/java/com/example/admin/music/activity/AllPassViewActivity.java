package com.example.admin.music.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.admin.music.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
;

/**
 * 通关界面
 */

public class AllPassViewActivity extends AppCompatActivity {
    //back键
    @BindView(R.id.title_bar_back)
    ImageButton titleBarBack;
    @BindView(R.id.allPass_button)
    ImageButton allPassButton;
    @BindView(R.id.allPass_rank_button)
    ImageButton allPassRankButton;
    //金币区域
    @BindView(R.id.title_bar_fragment)
    FrameLayout titleBarFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allpassview);
        ButterKnife.bind(this);
        //设置金币区域不可见
        titleBarFragment.setVisibility(View.GONE);
    }

    @OnClick(R.id.title_bar_back)
    public void backOnClick() {
        finish();
    }
}
