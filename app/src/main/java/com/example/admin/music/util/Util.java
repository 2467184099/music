package com.example.admin.music.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.example.admin.music.data.Const;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class Util {

    public static View getView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(layoutId, null);

        return layout;
    }

    /**
     * 界面跳转
     *
     * @param context
     * @param aClass
     */
    public static void startActivity(Context context, Class<?> aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);
    }

    /**
     * 保存金币和关卡索引
     *
     * @param context
     * @param stageIndex 索引
     * @param coins      金币
     */
    public static void setData(Context context, int stageIndex, int coins) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(Const.FILE_NAME_SAVE_DATA, Context.MODE_PRIVATE);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            //写数据
            dataOutputStream.writeInt(stageIndex);
            dataOutputStream.writeInt(coins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取数据
     * @param context
     * @return
     */
    public static int[] getData(Context context) {
        InputStream inputStream = null;
        int[] datas = {-1, Const.TOTAL_COINS};
        try {
            inputStream = context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            datas[Const.INDEX_LOAD_DATA_STAGE] = dataInputStream.readInt();
            datas[Const.INDEX_LOAD_DATA_MONEY] = dataInputStream.readInt();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
