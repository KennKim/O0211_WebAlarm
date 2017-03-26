package com.thread.webalarm.o0211_webalarm;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by conscious on 2016-11-27.
 */

public class BackPressCloseHandler {
    private BackPressCloseHandler backPressCloseHandler;

    private long backKeyPressedTime = 0;
    private Toast toast;
    private String message = "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.";

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }


    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }


    private void showGuide() {
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}