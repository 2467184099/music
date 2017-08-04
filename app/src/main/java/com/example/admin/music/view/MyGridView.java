package com.example.admin.music.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.admin.music.R;
import com.example.admin.music.activity.MainActivity;
import com.example.admin.music.modle.WordButton;
import com.example.admin.music.modle.inter.IWordButtonListener;
import com.example.admin.music.util.Util;

import java.util.ArrayList;


public class MyGridView extends GridView {
    public final static int COUNTS_WORDS = 24;
    //数据源
    private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
    //适配器
    private MyGridAdapter mAdapter;
    // 上下文
    private Context mContext;
    //动画
    private Animation scaleAnimation;

    // 接口
    private IWordButtonListener iWordButtonListener;

    public MyGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mAdapter = new MyGridAdapter();
        this.setAdapter(mAdapter);
    }

    public void updateData(ArrayList<WordButton> list) {
        mArrayList = list;
        // 重新设置数据源
        setAdapter(mAdapter);
    }



    class MyGridAdapter extends BaseAdapter {
        public int getCount() {
            return mArrayList.size();
        }

        public Object getItem(int pos) {
            return mArrayList.get(pos);
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(int pos, View v, ViewGroup p) {
            final WordButton holder;

            if (v == null) {
                v = Util.getView(mContext, R.layout.mygridview_item);
                v.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 130));
                holder = mArrayList.get(pos);
                // 加载动画
                scaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                // 设置动画的延迟时间
                scaleAnimation.setStartOffset(pos * 100);

                holder.index = pos;
                if (holder.viewButton == null) {
                    holder.viewButton = (Button) v.findViewById(R.id.button);
                }
                v.setTag(holder);
            } else {
                holder = (WordButton) v.getTag();
            }

            holder.viewButton.setText(holder.wordString);
            //动画播放
            v.startAnimation(scaleAnimation);
            holder.viewButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    iWordButtonListener.onWordClick(holder);

                }
            });
            return v;
        }
    }

    /**
     * 注册监听接口
     *
     * @param iWordButtonListener
     */
    public void registOnWordListener(MainActivity iWordButtonListener) {
        this.iWordButtonListener = iWordButtonListener;

    }
}
