package com.example.admin.music.util;

import com.example.admin.music.modle.Song;
import com.example.admin.music.view.MyGridView;

import java.io.UnsupportedEncodingException;
import java.util.Random;



/**
 * 生成随机汉子类
 */

public class Char {
    public static char getRandomChar() {
        String str = "";
        //高位
        int hightPos;
        //低位
        int lowPos;
        //随机数字类
        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] bytes = new byte[2];
        bytes[0] = (Integer.valueOf(hightPos).byteValue());
        bytes[1] = (Integer.valueOf(lowPos).byteValue());
        try {
            str = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    /**
     * 生成所有的文字
     *
     * @return
     */
    public static String[] generateWords(Song currentSong) {
        Random random = new Random();
        String[] words = new String[MyGridView.COUNTS_WORDS];
        //存入歌名
        for (int i = 0; i < currentSong.getSongNameLength(); i++) {
            words[i] = currentSong.getSongNameCharacters()[i] + "";
        }
        //剩余空地存放随机生成数字
        for (int i = currentSong.getSongNameLength(); i < MyGridView.COUNTS_WORDS; i++) {
            words[i] = getRandomChar() + "";
        }
        //打乱文字顺序，首先随机选取一个元素与第一个元素进行交换，以此类推，确保每个元素在每个位置的概率一直
        for (int i = MyGridView.COUNTS_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i+1);
            String buf=words[index];
            words[index]=words[i];
            words[i]=buf;
        }

        return words;
    }
}
