package com.taek_aaa.opencv;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by taek_aaa on 2017. 8. 4..
 */

public class ContentsTableActivity extends AppCompatActivity {

    public static int contentsTableIndex = 0;
    private Intent intent, thanksIntent, teaserIntent;
    private long lastTimeBackPressed;           //뒤로가기 버튼을 2번 누르면 종료하기 위해 담은 변수
    private VideoView videoView;
    private Uri video;
    private Button btn1,btn2,btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentstable);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView = (VideoView)findViewById(R.id.contents_table_video);
        video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.menu_background);
        videoView.setVideoURI(video);
        playVideo();

        btn1 = (Button) findViewById(R.id.first_btn);
        btn2 = (Button) findViewById(R.id.second_btn);
        btn3 = (Button) findViewById(R.id.finger_print_btn);
        btn1.setBackgroundResource(R.drawable.f_bb);
        btn2.setBackgroundResource(R.drawable.s_b);
        btn3.setBackgroundResource(R.drawable.fingerprint);

        intent = new Intent(this, SubMainActivity.class);
        thanksIntent = new Intent(this, ThanksActivity.class);
        teaserIntent = new Intent(this, TeaserActivity.class);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundResource(R.drawable.f_gg);
                btn2.setBackgroundResource(R.drawable.s_b);
                contentsTableIndex = 0;
                new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        startActivity(intent);
                    }
                }.start();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setBackgroundResource(R.drawable.f_gb);
                btn2.setBackgroundResource(R.drawable.s_g);
                contentsTableIndex = 1;
                new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        startActivity(intent);
                    }
                }.start();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(thanksIntent);
            }
        });

        videoView.setOnTouchListener(new OnSwipeTouchListener(ContentsTableActivity.this) {
            public void onSwipeRight() {
                startActivity(teaserIntent);
            }
        });
    }

    private void playVideo() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        btn1.setBackgroundResource(R.drawable.f_bb);
        btn2.setBackgroundResource(R.drawable.s_b);
    }


    @Override
    public void onBackPressed() {       //뒤로가기 2번 1.5초 안에 클릭시 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            ActivityCompat.finishAffinity(this);
            return;
        }
        Toast.makeText(ContentsTableActivity.this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }
}
