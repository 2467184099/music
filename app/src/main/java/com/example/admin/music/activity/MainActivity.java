package com.example.admin.music.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.data.Const;
import com.example.admin.music.modle.Song;
import com.example.admin.music.modle.WordButton;
import com.example.admin.music.modle.inter.IMyDialogListener;
import com.example.admin.music.modle.inter.IMyDialogScreenIngListener;
import com.example.admin.music.modle.inter.IWordButtonListener;
import com.example.admin.music.util.Char;
import com.example.admin.music.util.Dialog;
import com.example.admin.music.util.MyLog;
import com.example.admin.music.util.MyPlayer;
import com.example.admin.music.util.Util;
import com.example.admin.music.view.MyGridView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.admin.music.data.Const.INDEX_FILE_NAME;
import static com.example.admin.music.data.Const.INDEX_SONG_NAME;


public class MainActivity extends AppCompatActivity implements IWordButtonListener {
    /**
     * 返回键
     */
    @BindView(R.id.title_bar_back)
    ImageButton titleBarBack;
    /**
     * 金币的增加
     */
    @BindView(R.id.title_bar_coin)
    ImageButton titleBarCoin;

    /**
     * 金币
     */
    @BindView(R.id.title_bar_text_money)
    TextView titleBarTextMoney;
    public int currentCoins = Const.TOTAL_COINS;
    /**
     * 超过玩家数量
     */
    @BindView(R.id.answer_right_count)
    TextView answerRightCount;
    /**
     * 关卡的index
     */
    @BindView(R.id.answer_right_index)
    TextView answerRightIndex;
    /**
     * 音乐的名字
     */
    @BindView(R.id.answer_right_songName)
    TextView answerRightSongName;
    /**
     * 下一关
     */
    @BindView(R.id.answer_right_next)
    ImageButton answerRightNext;
    /**
     * 分享到微信
     */
    @BindView(R.id.answer_right_share)
    ImageButton answerRightShare;
    /**
     * 唱片
     */
    @BindView(R.id.imageView1)
    ImageView viewPan;
    /**
     * 播放按钮
     */
    @BindView(R.id.btn_play_start)
    ImageButton btnPlayStart;
    /**
     * 已选文字区域
     */
    @BindView(R.id.word_select_container)
    LinearLayout wordSelectContainer;
    /**
     * 关卡index
     */
    @BindView(R.id.float_button_text)
    TextView floatButtonText;
    /**
     * 删除
     */
    @BindView(R.id.float_button_delete)
    ImageButton floatButtonDelete;
    /**
     * 删除区域
     */
    @BindView(R.id.float_button_fragment1)
    FrameLayout floatButtonFragment1;
    /**
     * 提示
     */
    @BindView(R.id.float_button_hint)
    ImageButton floatButtonHint;
    /**
     * 提示区域
     */
    @BindView(R.id.float_button_fragment2)
    FrameLayout floatButtonFragment2;
    /**
     * 分享
     */
    @BindView(R.id.float_button_share)
    ImageButton floatButtonShare;
    /**
     * 关卡
     */
    @BindView(R.id.float_button_screening)
    ImageButton floatButtonScreening;
    //动画是否运行
    private boolean isRuning = false;
    /**
     * 播杆
     */
    @BindView(R.id.imageView2)
    ImageView viewPlayGan;
    /**
     * 唱片区域
     */
    @BindView(R.id.layout_pan)
    RelativeLayout layoutPan;
    /**
     * 解除butterkinfe绑定
     */
    Unbinder bind;
    /**
     * 唱片的相关动画
     */
    private Animation panAnimation;
    //线性的动画
    private LinearInterpolator panInterpolator;
    /**
     * 播杆回来的相关动画
     */
    private Animation barInAnimation;
    private LinearInterpolator barInInterpolator;
    /**
     * 播杆出去的相关动画
     */
    private Animation barOutAnimation;
    private LinearInterpolator barOutInterpolator;
    /**
     * 未选文字容器
     */
    private ArrayList<WordButton> allWords;
    /**
     * 已选文字容器
     */
    private ArrayList<WordButton> selectWords;
    /**
     * GridView對象
     */
    @BindView(R.id.word_name_myGridView)
    MyGridView wordNameMyGridView;
    /**
     * 当前的歌曲
     */
    private Song currentSong;
    /**
     * 当前关卡的索引
     */
    public int currentStageIndex = -1;
    //答案状态
    public static final int STATUS_ANSUER_RIGHT = 1;//正确
    public static final int STATUS_ANSUER_WRONG = 2;//错误
    public static final int STATUS_ANSUER_LACK = 3;//不完整
    //闪烁次数
    public static final int SPASH_TIMES = 6;
    //dialog ID
    public static final int ID_DIALOG_DELETE_WORD = 1;
    public static final int ID_DIALOG_MONEY_WORD = 2;
    public static final int ID_DIALOG_TIP_WORD = 3;
    public static final int ID_DIALOG_SCREENING = 4;

    /**
     * 过关奖励
     */
    public static final int AWARD_MONEY = 3;
    /**
     * 过关界面视图区域
     */
    @BindView(R.id.ansuer_right_pass_view)
    LinearLayout ansuerRightPassView;
    int[] data;
    /**
     * mainActivity创建时调用
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        //读取游戏数据
        data = Util.getData(this);
        currentStageIndex = data[Const.INDEX_LOAD_DATA_STAGE];
        currentCoins = data[Const.INDEX_LOAD_DATA_MONEY];
        //显示初始金币
        titleBarTextMoney.setText(currentCoins + "");
        //初始化动画
        initAnimation();
        //初始化游戏数据
        initCurrentStageData();
        wordNameMyGridView.registOnWordListener(this);

    }

    //初始化动画
    private void initAnimation() {
        //唱片
        panAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        panInterpolator = new LinearInterpolator();
        panAnimation.setInterpolator(panInterpolator);
        //动画监听器
        panAnimation.setAnimationListener(new Animation.AnimationListener() {
            //动画开始
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //播杆回去动画开始
                viewPlayGan.startAnimation(barOutAnimation);
            }

            //动画重复播放
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //播杆回来
        barInAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        barInInterpolator = new LinearInterpolator();
        //当动画播完保持结束是动作
        barInAnimation.setFillAfter(true);
        barInAnimation.setInterpolator(barInInterpolator);
        //动画监听器
        barInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //唱片动画开始
                viewPan.startAnimation(panAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //播杆出去
        barOutAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_45_d);
        barOutInterpolator = new LinearInterpolator();
        //当动画播完保持结束是动作
        barOutAnimation.setFillAfter(true);
        barOutAnimation.setInterpolator(barOutInterpolator);
        //动画监听器
        barOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRuning = false;
                btnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //盘片paly按钮事件
    @OnClick(R.id.btn_play_start)
    public void onClick() {
        handlePlayButton();
    }

    //控制动画
    private void handlePlayButton() {
        if (viewPlayGan != null) {
            if (!isRuning) {
                isRuning = true;
                viewPlayGan.startAnimation(barInAnimation);
                btnPlayStart.setVisibility(View.INVISIBLE);
                //播放音乐
                MyPlayer.palyString(MainActivity.this, currentSong.getSongFileName());
            }
        }
    }

    /**
     * 删除，提示，选关按钮的点击事件
     */
    //删除事件
    @OnClick(R.id.float_button_delete)
    public void deleteOnClick() {
        showConfigDialog(ID_DIALOG_DELETE_WORD);
    }

    //提示事件
    @OnClick(R.id.float_button_hint)
    public void hintOnClick() {
        showConfigDialog(ID_DIALOG_TIP_WORD);
    }

    //选择关卡点击事件
    @OnClick(R.id.float_button_screening)
    public void screenIngOnClick() {
        //停止未完成的动画
        viewPan.clearAnimation();
        //停止正在播放的音乐
        MyPlayer.stopSong(MainActivity.this);
        showConfigDialog(ID_DIALOG_SCREENING);
    }

    //分享
    @OnClick({R.id.float_button_share,R.id.answer_right_share})
    public void shareOnClick(View view) {
        switch (view.getId()) {
            case R.id.float_button_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, R.drawable.credit_content);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            case R.id.answer_right_share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("image/*");
                intent1.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent1.putExtra(Intent.EXTRA_TEXT, R.drawable.credit_content);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent1, "分享"));
                break;
        }
    }

    //自动选择一个答案
    private void tipAnswer() {
        boolean tipWord = false;
        for (int i = 0; i < selectWords.size(); i++) {
            if (selectWords.get(i).wordString.length() == 0) {
                //根据当前答案框的条件选择对应的文字并填入
                onWordClick(findIsAnswerWord(i));
                tipWord = true;
                //减少金币数量
                if (!handleCoins(-getHintWord())) {
                    //金币数量不够，提示错误并且返回
                    showConfigDialog(ID_DIALOG_MONEY_WORD);
                    return;
                }

                break;
            }
        }
        //没有找到可以填充的答案
        if (!tipWord) {
            //闪烁文字提示用户
            sparkTheWords();
        }
    }

    //删除文字
    private void deleteOnWord() {
        // 减少金币
        if (!handleCoins(-getDeleteWord())) {
            showConfigDialog(ID_DIALOG_MONEY_WORD);
            return;
        }

        // 将这个索引对应的WordButton设置为不可见
        setAllWord(findNotAnswerWord(), View.INVISIBLE);
    }

    //找到一个不是答案的文字，并且当前是可见的
    private WordButton findNotAnswerWord() {
        Random random = new Random();
        WordButton buf = null;

        while (true) {
            int index = random.nextInt(MyGridView.COUNTS_WORDS);

            buf = allWords.get(index);

            if (buf.isVisiable && !isTheAnswerWord(buf)) {
                return buf;
            }
        }
    }

    /**
     * 找到一个答案
     *
     * @param index 当前需要填入框的索引
     * @return
     */
    private WordButton findIsAnswerWord(int index) {
        WordButton buf = null;

        for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
            buf = allWords.get(i);

            if (buf.wordString.equals("" + currentSong.getSongNameCharacters()[index])) {
                return buf;
            }
        }

        return null;
    }

    //判断文字是否是答案
    private boolean isTheAnswerWord(WordButton wordButton) {
        boolean result = false;

        for (int i = 0; i < currentSong.getSongNameLength(); i++) {
            if (wordButton.wordString.equals
                    ("" + currentSong.getSongNameCharacters()[i])) {
                result = true;

                break;
            }
        }

        return result;
    }

    /**
     * 增加或减少一定的金币数量 true为成功
     *
     * @param data 增加或减少的金币数量
     * @return true为增加或减少成功
     */
    private boolean handleCoins(int data) {
        // 判断当前总的金币数量是否可被减少
        if (currentCoins + data >= 0) {
            currentCoins += data;

            titleBarTextMoney.setText(currentCoins + "");

            return true;
        } else {
            // 金币不够
            // TODO: 2017/6/29
            return false;
        }
    }

    //从配置文件读取删除操作所要用的金币
    private int getDeleteWord() {
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }

    //从配置文件读取提示操作所要用的金币
    private int getHintWord() {
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }

    //加载当前关的游戏数据
    private void initCurrentStageData() {

        //读取当前关的歌曲信息
        currentSong = loadStageInfo(++currentStageIndex);
        //初始化已选中
        selectWords = initSelect(currentSong);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        //清空原来答案
        wordSelectContainer.removeAllViews();
        //增加新的答案框
        for (int i = 0; i < selectWords.size(); i++) {
            wordSelectContainer.addView(selectWords.get(i).viewButton, params);
        }
        //显示当前关的索引
        if (floatButtonText != null) {
            floatButtonText.setText((currentStageIndex + 1) + "");
        }
        //获得数据
        allWords = initAllWord(currentSong);
        //更新数据
        wordNameMyGridView.updateData(allWords);
        //一开始就播放
        handlePlayButton();
    }


    //加载任意关的游戏数据
    private void initCurrentStageDataScreenIng(int currentStageIndex) {
        //读取当前关的歌曲信息
        currentSong = loadStageInfo(currentStageIndex - 1);
        //初始化已选中
        selectWords = initSelect(currentSong);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        //清空原来答案
        wordSelectContainer.removeAllViews();
        //增加新的答案框
        for (int j = 0; j < selectWords.size(); j++) {
            wordSelectContainer.addView(selectWords.get(j).viewButton, params);
        }
        //显示当前关的索引
        if (floatButtonText != null) {
            floatButtonText.setText(currentStageIndex + "");
        }
        //获得数据
        allWords = initAllWord(currentSong);
        //更新数据
        wordNameMyGridView.updateData(allWords);
        //一开始就播放
        handlePlayButton();


    }

    //确定音乐
    private Song loadStageInfo(int stageIndex) {
        Song song = new Song();
        String[] stage = Const.SONG_INFO[stageIndex];
        song.setSongFileName(stage[INDEX_FILE_NAME]);
        song.setSongName(stage[INDEX_SONG_NAME]);
        return song;
    }

    //初始化待选文字框
    private ArrayList<WordButton> initAllWord(Song currentSong) {
        ArrayList<WordButton> data = new ArrayList<>();
        //获得所有待选文字
        String[] words = Char.generateWords(currentSong);
        for (int i = 0; i < MyGridView.COUNTS_WORDS; i++) {
            WordButton button = new WordButton();
            button.wordString = words[i];
            data.add(button);
        }
        return data;
    }

    //初始化已选中文字框
    private ArrayList<WordButton> initSelect(Song currentSong) {
        ArrayList<WordButton> data = new ArrayList<>();
        for (int i = 0; i < currentSong.getSongNameLength(); i++) {
            View view = Util.getView(MainActivity.this, R.layout.mygridview_item);
            final WordButton hoder = new WordButton();
            hoder.viewButton = (Button) view.findViewById(R.id.button);
            hoder.viewButton.setTextColor(Color.WHITE);
            hoder.viewButton.setText("");
            hoder.isVisiable = false;
            hoder.viewButton.setBackgroundResource(R.drawable.game_wordblank);
            hoder.viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearWord(hoder);
                }
            });
            data.add(hoder);
        }
        return data;
    }


    //待选框文字点击事件
    @Override
    public void onWordClick(WordButton wordButton) {
        setSelectWord(wordButton);
        //答案状态
        int checkResult = checkTheAnswer();
        //判断答案
        if (checkResult == STATUS_ANSUER_RIGHT) {
            //过关并获得奖励
            handlePassEvent();
        } else if (checkResult == STATUS_ANSUER_WRONG) {
            //闪烁文字并提示用户
            sparkTheWords();
        } else if (checkResult == STATUS_ANSUER_LACK) {
            //在常规状态和缺失状态为白颜色
            for (int i = 0; i < selectWords.size(); i++) {
                selectWords.get(i).viewButton.setTextColor(
                        Color.WHITE);
            }

        }
    }

    /**
     * 过关界面的事件处理
     */
    private void handlePassEvent() {
        //显示过关界面
        ansuerRightPassView.setVisibility(View.VISIBLE);
        //停止未完成的动画
        viewPan.clearAnimation();
        //停止正在播放的音乐
        MyPlayer.stopSong(MainActivity.this);
        //播放音效
        MyPlayer.playTone(MainActivity.this, MyPlayer.INDEX_STONE_COIN);
        //当前关的索引
        if (answerRightIndex != null) {
            answerRightIndex.setText("" + (currentStageIndex + 1));
        }
        //显示歌曲的名称
        if (answerRightSongName != null) {
            answerRightSongName.setText(currentSong.getSongName());
        }
    }

    //设置答案
    private void setSelectWord(WordButton wordButton) {
        for (int i = 0; i < selectWords.size(); i++) {
            if (selectWords.get(i).wordString.length() == 0) {
                //设置文字及可见性
                selectWords.get(i).viewButton.setText(wordButton.wordString);
                selectWords.get(i).isVisiable = true;
                selectWords.get(i).wordString = wordButton.wordString;
                //记录索引
                selectWords.get(i).index = wordButton.index;
                //log
                MyLog.d(Const.TAG, selectWords.get(i).index + "");
                //设置待选框的可见性
                setAllWord(wordButton, View.INVISIBLE);
                break;
            }
        }

    }

    //清除答案
    private void clearWord(WordButton wordButton) {
        wordButton.viewButton.setText("");
        wordButton.wordString = "";
        wordButton.isVisiable = false;
        //设置待选框
        setAllWord(allWords.get(wordButton.index), View.VISIBLE);

    }

    /**
     * 待选框可见性
     */
    private void setAllWord(WordButton wordButton, int visibility) {
        wordButton.viewButton.setVisibility(visibility);
        wordButton.isVisiable = (visibility == View.VISIBLE) ? true : false;

        MyLog.d(Const.TAG, wordButton.isVisiable + "");
    }

    /**
     * 检查答案
     */
    private int checkTheAnswer() {
        //检查长度
        for (int i = 0; i < selectWords.size(); i++) {
            //如果有空，则不完整
            if (selectWords.get(i).wordString.length() == 0) {
                return STATUS_ANSUER_LACK;
            }
        }
        //答案完整 继续判断
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < selectWords.size(); i++) {
            buffer.append(selectWords.get(i).wordString);
        }
        return buffer.toString().equals(currentSong.getSongName()) ? STATUS_ANSUER_RIGHT : STATUS_ANSUER_WRONG;
    }

    //计时器
    TimerTask timerTask;
    Timer timer;

    //闪烁文字
    private void sparkTheWords() {
        //声明定时器
        timerTask = new TimerTask() {
            boolean change = false;
            int spardTimes = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //闪烁次数
                        if (++spardTimes > SPASH_TIMES) {
                            return;
                        }
                        //执行闪烁
                        for (int i = 0; i < selectWords.size(); i++) {
                            selectWords.get(i).viewButton.setTextColor(
                                    change ? Color.RED : Color.WHITE);
                        }
                        change = !change;
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1, 150);
    }

    //下一关点击事件
    @OnClick(R.id.answer_right_next)
    public void nextButtonOnClick() {
        if (judegAppPassed()) {
            //进入通关界面
            Util.startActivity(this, AllPassViewActivity.class);
            MainActivity.this.finish();

        } else {
            //进入下一关
            ansuerRightPassView.setVisibility(View.GONE);
            handleCoins(AWARD_MONEY);
            //加载关卡数据
            initCurrentStageData();
        }
    }

    //判断是否通关
    private boolean judegAppPassed() {
        return currentStageIndex == (Const.SONG_INFO.length - 1);
    }


    /**
     * 点击删除，提示，关卡按钮的回调接口
     */
    //删除错误答案回调事件
    private IMyDialogListener deleteWordListener = new IMyDialogListener() {
        @Override
        public void onClick() {
            //执行事件
            deleteOnWord();
        }
    };
    //金币不足回调事件
    private IMyDialogListener moneyListener = new IMyDialogListener() {
        @Override
        public void onClick() {
        }
    };
    //答案提示回调事件
    private IMyDialogListener tipListener = new IMyDialogListener() {
        @Override
        public void onClick() {
            //执行事件
            tipAnswer();
        }
    };

    //选择关卡回调事件
    private IMyDialogScreenIngListener screeningListener = new IMyDialogScreenIngListener() {

        @Override
        public void onClick(int currentStageIndex) {
            initCurrentStageDataScreenIng(currentStageIndex);
            Util.setData(MainActivity.this, currentStageIndex - 1, currentCoins);
        }
    };

    //调用dialog
    private void showConfigDialog(int id) {
        switch (id) {
            case ID_DIALOG_DELETE_WORD:
                Dialog.dialog(MainActivity.this, "确认花掉" + getDeleteWord() + "个金币去掉一个错误答案?", deleteWordListener);
                break;
            case ID_DIALOG_MONEY_WORD:
                Dialog.dialog(MainActivity.this, "金币不足，请充值", moneyListener);
                break;
            case ID_DIALOG_TIP_WORD:
                Dialog.dialog(MainActivity.this, "确认花掉" + getHintWord() + "个金币获取一个正确答案?", tipListener);
                break;
            case ID_DIALOG_SCREENING:
                Dialog.ScreenIngDialog(MainActivity.this, screeningListener);
                break;
        }

    }

    /**
     * 返回键的点击事件
     */
    @OnClick(R.id.title_bar_back)
    public void backOnClick() {
        finish();
    }

    /**
     * mainActivity停止时调用
     */
    @Override
    protected void onPause() {
        //保存游戏数据
        Util.setData(MainActivity.this, currentStageIndex - 1, currentCoins);
        viewPan.clearAnimation();
        //暂停音乐
        MyPlayer.stopSong(MainActivity.this);
        super.onPause();
    }

    /**
     * mainActivity销毁时调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
