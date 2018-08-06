package com.lectopia.tsop.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> imgList;
    private int curImgIndex;

    static final String TAG ="{life cycle info}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();

        imgList = new ArrayList<Integer>();
        curImgIndex = 0;
        imgList.add(R.drawable.bg1);
        imgList.add(R.drawable.bg2);
        imgList.add(R.drawable.bg3);
        imgList.add(R.drawable.bg4);
        imgList.add(R.drawable.bg5);
        imgList.add(R.drawable.bg6);

        final ImageView iv = (ImageView)findViewById(R.id.imgView);

        iv.setOnTouchListener(new View.OnTouchListener() {
            private boolean isScroll = false;
            private float x;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                Log.d(TAG, "" + action);
                if (action == MotionEvent.ACTION_DOWN) {
                    x = motionEvent.getX();
                    int width = iv.getWidth();
                    if (x < width / 2) {
                        Button btn = findViewById(R.id.btnDown);
                        btn.callOnClick();
                    } else {
                        Button btn = findViewById(R.id.btnUp);
                        btn.callOnClick();
                    }
                }
//                int action = motionEvent.getActionMasked();
//                Log.d(TAG, "" + action);
//                if (action == MotionEvent.ACTION_MOVE) {
//                    this.isScroll = true;
//                } else if (action == MotionEvent.ACTION_DOWN) {
//                    x = motionEvent.getX();
//                } else if (action == MotionEvent.ACTION_UP) {
//                    Log.d(TAG, "" + isScroll);
//                    if (isScroll) {
//                        if (x - motionEvent.getX() > 0) {
//                            Button btn = findViewById(R.id.btnUp);
//                            btn.callOnClick();
//                        } else {
//                            Button btn = findViewById(R.id.btnDown);
//                            btn.callOnClick();
//                        }
//                        Log.d(TAG,motionEvent.getAxisValue(MotionEvent.AXIS_HSCROLL)+ "");
//                        isScroll = false;
//                    }
//                }
                return true;
            }
        });

        Button btnUp = (Button)findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curImgIndex < imgList.size() - 1) {
                    curImgIndex += 1;
                } else {
                    curImgIndex = 0;
                }
                iv.setImageResource(imgList.get(curImgIndex));
                System.out.print("test");
            }
        });

        Button btnDown = (Button)findViewById(R.id.btnDown);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curImgIndex > 0) {
                    curImgIndex -= 1;
                } else {
                    curImgIndex = imgList.size() - 1;
                }
                iv.setImageResource(imgList.get(curImgIndex));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "pause");
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        restoreState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "delete");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "restart");
    }

    protected void restoreState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("initialIndex"))) {
            int index = pref.getInt("initialIndex", 0);
            curImgIndex = index;
            deleteSharedPreferences("pref");
        }
        ImageView iv = findViewById(R.id.imgView);
        iv.setImageResource(imgList.get(curImgIndex));
    }

    protected void saveState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("initialIndex", curImgIndex);
        editor.commit();
    }
}
