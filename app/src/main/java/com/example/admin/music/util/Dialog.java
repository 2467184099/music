package com.example.admin.music.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.data.Const;
import com.example.admin.music.modle.inter.IMyDialogListener;
import com.example.admin.music.modle.inter.IMyDialogScreenIngListener;

/**
 * Created by Administrator on 2017/6/28.
 */

public class Dialog {
    private static AlertDialog alertDialog;

    /**
     * 显示自定义对话框
     *
     * @param context
     * @param message
     * @param iMyDialogListener
     */
    public static void dialog(final Context context, String message, final IMyDialogListener iMyDialogListener) {
        View diaLogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        diaLogView = Util.getView(context, R.layout.dialog);
        ImageButton okButton = (ImageButton) diaLogView.findViewById(R.id.dialog_ok);
        ImageButton cancelButton = (ImageButton) diaLogView.findViewById(R.id.dialog_cancel);
        TextView textMessage = (TextView) diaLogView.findViewById(R.id.text_dialog_message);
        textMessage.setText(message);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                if (iMyDialogListener != null) {
                    iMyDialogListener.onClick();
                }
                //添加音效
                MyPlayer.playTone(context, MyPlayer.INDEX_STONE_ENTER);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                MyPlayer.playTone(context, MyPlayer.INDEX_STONE_CANCEL);
            }
        });
        //为dialog设置view
        builder.setView(diaLogView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void ScreenIngDialog(final Context context, final IMyDialogScreenIngListener iMyDialogScreenIngListener) {
        View diaLogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        diaLogView = Util.getView(context, R.layout.screeningdialog);
        ImageButton okButton = (ImageButton) diaLogView.findViewById(R.id.dialog_screenIngOK);
        final ImageButton cancelButton = (ImageButton) diaLogView.findViewById(R.id.dialog_screenIngCancel);
        final EditText textMessage = (EditText) diaLogView.findViewById(R.id.text_dialog_screenIngMessage);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = textMessage.getText().toString();
                if (str.equals("")) {
                    str = "1";
                }
                int currentStageIndex = Integer.parseInt(str);

                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                if (iMyDialogScreenIngListener != null) {
                    if (currentStageIndex >= 1 && currentStageIndex <= Const.SONG_INFO.length) {
                        iMyDialogScreenIngListener.onClick(currentStageIndex);
                    } else {
                        Toast.makeText(context, "你输入的数字不符合规定或已超出最大关卡", Toast.LENGTH_SHORT).show();
                    }
                }
                //添加音效
                MyPlayer.playTone(context, MyPlayer.INDEX_STONE_ENTER);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                MyPlayer.playTone(context, MyPlayer.INDEX_STONE_CANCEL);
            }
        });
        //为dialog设置view
        builder.setView(diaLogView);
        alertDialog = builder.create();
        alertDialog.show();
    }
}
