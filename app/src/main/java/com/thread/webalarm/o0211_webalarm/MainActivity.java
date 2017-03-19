package com.thread.webalarm.o0211_webalarm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String strTest="";

    private WebView webView;
    private ProgressDialog progressBar;

    private EditText et;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private TextView tv2;
    private CheckBox checkBox;
    private CheckBox checkBox2;

    private String address = "http://www.pgi88.com";
    private LinearLayout linear;

    private final String TAG = "tag";
    private final String HTML = "myHtml";

    private MediaPlayer mp;


    Handler myHandler = new Handler();

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String naverHtml = bun.getString(HTML);
            tv2.setText(naverHtml);
        }
    };

    public final static int REPEAT_DELAY = 15000;
    public final static int Atest = 1;
    int aa = 0;
    Handler aHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 할일들을 여기에 등록

            et.setText(webView.getUrl());
            myHtml();
            tv2.setText(strTest);


            Toast.makeText(MainActivity.this, strTest + "-aHandler : " + aa, Toast.LENGTH_LONG).show();
            aa++;
            this.sendEmptyMessageDelayed(Atest, REPEAT_DELAY);        // REPEAT_DELAY 간격으로 계속해서 반복하게 만들어준다
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startWebView();

//
//        MyThread myThread = new MyThread();
//        myThread.start();


        mp = MediaPlayer.create(MainActivity.this, R.raw.sound_sky);


//        webView.loadUrl(url);
//		 InputMethodManager immhide = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
//		 immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(getModiUrl());

            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(webView.getUrl());
//                Toast.makeText(MainActivity.this, webView.getUrl(), Toast.LENGTH_SHORT).show();

                // Thread로 웹서버에 접속
                new Thread() {
                    public void run() {
                        String naverHtml = getNaverHtml();

                        Bundle bun = new Bundle();
                        bun.putString(HTML, naverHtml);
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(webView.getUrl());
                myHtml();
                tv2.setText(strTest);
            }
        });

        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strTest.equals("0")) {
                    playMp3();
                } else {
                    Toast.makeText(MainActivity.this, strTest + "- not same! -" + strTest.length(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMp3();
            }
        });


        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (strTest.equals("1") ||strTest.equals("2") ||strTest.equals("3") ||strTest.equals("4") ||strTest.equals("5")) {
                    playMp3();

                } else {
//                    Toast.makeText(MainActivity.this, "stopMp3", Toast.LENGTH_SHORT).show();
                    stopMp3();
                }

            }
        });


        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    playMp3();
                } else {
                    stopMp3();
                }
            }
        });

        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv2.setText("start!");
                    aHandler.sendEmptyMessage(Atest);
                } else {
                    tv2.setText("STOP..");
                    aHandler.removeMessages(Atest);
                }
            }
        });


    }


    private void startWebView() {

        webView = (WebView) findViewById(R.id.webView1);

        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸

        et = (EditText) findViewById(R.id.editText1);
        et.setText(address);
        String url = et.getText().toString();


        //REDIRECT ADDRESS
        //REDIRECT ADDRESS
        //REDIRECT ADDRESS

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(MainActivity.this, "WebView Example", "Loading...");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
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


        webView.loadUrl(getModiUrl());
    }

    public void playMp3() {
        try {
            if (!mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(MainActivity.this, R.raw.sound_sky);
                mp.start();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMp3() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(MainActivity.this, R.raw.sound_sky);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getModiUrl() {
        String url = et.getText().toString();
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
            String address = et.getText().toString();
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


    public void myHtml() {


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


        webView.loadUrl(getModiUrl());


    }

    public class MyJavascriptInterface {

        @JavascriptInterface
        public void getHtml(String html) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
            System.out.println(html);
            strTest = html.trim();
        }
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
