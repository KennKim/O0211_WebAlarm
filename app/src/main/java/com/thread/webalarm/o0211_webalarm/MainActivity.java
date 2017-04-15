package com.thread.webalarm.o0211_webalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thread.webalarm.o0211_webalarm.setting.SettingsActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private BackPressCloseHandler backPressCloseHandler;
    private Dialog mMainDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String strCount = "";
    private String strVs = "1";

    private WebView webView;
    private ProgressDialog proDialog;

    private EditText etSearch;
    private RadioGroup radioGroup;
    private RadioButton rab0;
    private RadioButton rab1;
    private RadioButton rab2;
    private RadioButton rab3;
    private RadioButton rab4;
    private Button btnGo;
    private Button btnGetHtml;
    private Button btnStop;
    private Button btnC;
    private TextView tvResult;
    private TextView tvNo;
    private TextView tvNo2;
    private TextView tvGetUrl;
    private CheckBox cboxWeb;
    private CheckBox cboxZero;

    private static String firstUrl = "http://www.pgi88.com";
    private LinearLayout linear;

    private final String TAG = "tag";
    private final String HTML = "myHtml";

    private Ringtone ringtone;
    private MediaPlayer mp;
    private Vibrator vib;

    Handler handSource = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String strHtml = bun.getString(HTML);
            tvGetUrl.setText(strHtml);
        }
    };

    public final static int A_DELAY = 6000;
    public final static int A_NO = 1;
    int aa = 0;
    String aHanUrl;
    Handler aHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 할일들을 여기에 등록
            tvGetUrl.setText(webView.getUrl());
            tvNo.setText("" + (++aa));
            myHtml(aHanUrl);
            tvResult.setText(strCount);

            setCustomToast(MainActivity.this, "" + aa);
//            Toast.makeText(MainActivity.this, strCount + "-aHandler : " + aa, Toast.LENGTH_LONG).show();
//            Toast toast = Toast.makeText(MainActivity.this, strCount + "-aHandler : " + aa, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 50, 50);
//            toast.show();

            this.sendEmptyMessageDelayed(A_NO, A_DELAY);        // A_DELAY 간격으로 계속해서 반복하게 만들어준다
        }
    };

    public final static int B_DELAY = 1200000;
    public final static int B_NO = 2;
    int bb = 0;
    Handler bHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 할일들을 여기에 등록
            startWebView(getEtUrl());
            tvNo2.setText("" + bb++);

            this.sendEmptyMessageDelayed(B_NO, B_DELAY);        // B_DELAY 간격으로 계속해서 반복하게 만들어준다
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        backPressCloseHandler = new BackPressCloseHandler(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, strCount + "-aHandler : " + aa, Toast.LENGTH_LONG).show();
                startWebView("http://www.naver.com");
                swipeRefreshLayout.setRefreshing(false);

            }
        });
//                색상 변경
//        swipeRefreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

        webView = (WebView) findViewById(R.id.webView1);

        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        etSearch = (EditText) findViewById(R.id.editText1);
        etSearch.setText(firstUrl);

        startWebView(getEtUrl());

//
//        MyThread myThread = new MyThread();
//        myThread.start();

        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtone = ringtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

//        mp = MediaPlayer.create(MainActivity.this, R.raw.sound_sky);
//        mp.setAudioStreamType(AudioManager.STREAM_RING);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


//        webView.loadUrl(url);
//		 InputMethodManager immhide = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
//		 immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        btnGo = (Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebView(getEtUrl());
            }
        });
        btnGetHtml = (Button) findViewById(R.id.btnGetHtml);
        btnGetHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGetUrl.setText(webView.getUrl());
//                Toast.makeText(MainActivity.this, webView.getUrl(), Toast.LENGTH_SHORT).show();

                // Thread로 웹서버에 접속
                new Thread() {
                    public void run() {
                        String naverHtml = getNaverHtml();

                        Bundle bun = new Bundle();
                        bun.putString(HTML, naverHtml);
                        Message msg = handSource.obtainMessage();
                        msg.setData(bun);
                        handSource.sendMessage(msg);
                    }
                }.start();
            }
        });


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                rab0.setBackgroundResource(R.drawable.shape_corner_pink);
                rab1.setBackgroundResource(R.drawable.shape_corner_pink);
                rab2.setBackgroundResource(R.drawable.shape_corner_pink);
                rab3.setBackgroundResource(R.drawable.shape_corner_pink);
                rab4.setBackgroundResource(R.drawable.shape_corner_pink);


                if (checkedId == R.id.rab0) {
                    rab0.setBackgroundResource(R.drawable.shape_solid_pink);
                    strVs = "0";
                } else if (checkedId == R.id.rab1) {
                    rab1.setBackgroundResource(R.drawable.shape_solid_pink);
                    strVs = "1";
                } else if (checkedId == R.id.rab2) {
                    rab2.setBackgroundResource(R.drawable.shape_solid_pink);
                    strVs = "2";
                } else if (checkedId == R.id.rab3) {
                    rab3.setBackgroundResource(R.drawable.shape_solid_pink);
                    strVs = "3";
                } else if (checkedId == R.id.rab4) {
                    rab4.setBackgroundResource(R.drawable.shape_solid_pink);
                    strVs = "4";
                }
            }
        });


        rab0 = (RadioButton) findViewById(R.id.rab0);
        rab1 = (RadioButton) findViewById(R.id.rab1);
        rab2 = (RadioButton) findViewById(R.id.rab2);
        rab3 = (RadioButton) findViewById(R.id.rab3);
        rab4 = (RadioButton) findViewById(R.id.rab4);
        radioGroup.check(R.id.rab1);


        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMp3();
            }
        });
        btnC = (Button) findViewById(R.id.btnC);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);

                dialog.setTitle("Lorem Ipsum?");
                dialog.setMessage(R.string.ipsum);
                dialog.setCancelable(false);
                dialog.setPositiveButton("o", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


        tvResult = (TextView) findViewById(R.id.tvResult);
        tvResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (strCount.equals(strVs)) {
//                    callNotification();
                    playMp3();
                    startVib();
                } else {
//                    Toast.makeText(MainActivity.this, "stopMp3", Toast.LENGTH_SHORT).show();
                    stopMp3();
                }

            }
        });

        tvGetUrl = (TextView) findViewById(R.id.tvGetUrl);

        tvNo = (TextView) findViewById(R.id.tvNo);

        tvNo2 = (TextView) findViewById(R.id.tvNo2);


        cboxZero = (CheckBox) findViewById(R.id.cboxZero);
        cboxZero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aHanUrl = webView.getUrl();

                    tvResult.setText("start!");
                    aHandler.sendEmptyMessage(A_NO);
//                    bHandler.sendEmptyMessage(B_NO);
                } else {
                    tvResult.setText("STOP..");
                    aHandler.removeMessages(A_NO);
//                    bHandler.removeMessages(B_NO);
                }
            }
        });


        cboxWeb = (CheckBox) findViewById(R.id.cboxWeb);
        cboxWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    callNotification();
                    playMp3();
                    startVib();

                    String userAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
                    webView.getSettings().setUserAgentString(userAgent);

                    startWebView(webView.getUrl());


//                    String userAgent = webView.getSettings().getUserAgentString().replace("Mobile ","");
//                    webView.getSettings().setUserAgentString(userAgent);

//                    String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
//                    webView.getSettings().setUserAgentString(ua);
//                    startWebView(webView.getUrl());

                } else {
                    stopMp3();

                    String userAgent = "Android";
                    webView.getSettings().setUserAgentString(userAgent);

                    startWebView(webView.getUrl());

//                    String userAgent = webView.getSettings().getUserAgentString().replace("Mozila ", "");
//                    webView.getSettings().setUserAgentString(userAgent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish the activity
                onBackPressed();
                return true;
            case R.id.m1:
                Toast.makeText(MainActivity.this, "m1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.m2:
                Toast.makeText(MainActivity.this, "m2", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void startWebView(String url) {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

//        proDialog = ProgressDialog.show(MainActivity.this, "WebView Example", "Loading...");

        proDialog = new ProgressDialog(MainActivity.this);
        proDialog.setMessage("Loading...");
        proDialog.setCancelable(false);
        proDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        proDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (MainActivity.this.proDialog.isShowing()) {
                    MainActivity.this.proDialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });


        webView.loadUrl(url);
    }

    public void startService() {
        Intent intent = new Intent(MainActivity.this, ServiceWithWebview.class);
        intent.putExtra("Test", "test");
        startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(MainActivity.this, ServiceWithWebview.class);
        stopService(intent);
    }

    public void playMp3() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        am.getRingerMode() == AudioManager.RINGER_MODE_SILENT
//        am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE

        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {


            ringtone.play();



/*
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            try {
                if (!mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(getApplicationContext(), uri);
                    mp.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

    }


    public void stopMp3() {

        ringtone.stop();

        /*try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(MainActivity.this, R.raw.sound_sky);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void setRabOn(int i) {

    }

    private void setRabOff() {
    }

    public void startVib() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL || am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            long[] pattern = {0, 500, 100, 40, 100, 40, 100, 40, 100, 800, 100};
            vib.vibrate(pattern, -1);
        }
    }


    public String getEtUrl() {
        String url = etSearch.getText().toString();
//        Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
        return url;
    }


    private String getNaverHtml() {
        String naverHtml = "";

        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            String address = etSearch.getText().toString();
            url = new URL(address);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3 * 1000);
            http.setReadTimeout(3 * 1000);

            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                naverHtml += str + "\n";
            }

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            if (http != null) {
                try {
                    http.disconnect();
                } catch (Exception e) {
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        return naverHtml;
    }


    public void myHtml(String url) {


//        WebView webView = new WebView(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('span')[11].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
            }
        });

        webView.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
        webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");


        webView.loadUrl(url);


    }

    public class MyJavascriptInterface {

        @JavascriptInterface
        public void getHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            System.out.println(html);
            strCount = html.trim();
        }

    }

    public void callNotification() {

        Resources res = getResources();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("알람") //상태바 드래그시 보이는 타이틀
                .setContentText("알람이 왔다.") //상태바 드래그시 보이는 서브타이틀
                .setTicker("News!") //상태바 한줄 메시지
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
//                .setOngoing(true)
                .setNumber(1)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Bitmap bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.abs);

        NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle(builder);
        bigStyle.setBigContentTitle("bigpicture Expanded Title");
        bigStyle.setSummaryText("bigpicture Expanded Message");
        bigStyle.bigPicture(bigPicture);

        builder.setStyle(bigStyle);


        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(MainActivity.this, " onNewIntent ", Toast.LENGTH_LONG).show();

    }

    /**
     * 토스트 설정
     */
    public static void setCustomToast(Context context, String msg) {
        TextView tv = new TextView(context);
        tv.setText(msg);
        tv.setBackgroundResource(R.drawable.shape_corner_pink);
        tv.setTextColor(Color.RED);
        tv.setTextSize(10);

        final Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setView(tv);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

/*
    class MyThread extends Thread {
        @Override
        public void run() {
            for(int i =0; i<100; i++) {

                try {
                    Thread.sleep(1000);
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int i =+0;
                            Toast.makeText(MainActivity.this, "thread" + i, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }*/
}
