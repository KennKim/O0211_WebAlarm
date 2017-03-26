package com.thread.webalarm.o0211_webalarm;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by conscious on 2017-03-27.
 */

public class ServiceWithWebview extends android.app.Service{


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
