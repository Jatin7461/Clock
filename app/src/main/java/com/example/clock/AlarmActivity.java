package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.clock.Fragments.SwipeAlarm;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    private static final int alarms = 1;
    private ViewPager2 viewPager;
    //    private SwipeAlarmAdapter adapter;
    final String TAG = AlarmActivity.class.getName();

    Ringtone r;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

//        viewPager = findViewById(R.id.pager);
//        adapter = new SwipeAlarmAdapter(this);
//        viewPager.setAdapter(adapter);
//        viewPager.setPageTransformer(new ZoomOutPageTransformer());
//
//        viewPager.setOnTouchListener(new OnSwipeTouchListener(this) {
//            @Override
//            public void onSwipeLeft() {
//                super.onSwipeLeft();
//                Log.v(TAG, "swiping");
//            }
//
//            @Override
//            public void onSwipeRight() {
//                super.onSwipeRight();
//                Log.v(TAG, "swiping");
//            }
//        });

//        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.v(TAG, "pageScrolled");
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.v(TAG, "pageSelected");
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.v(TAG, "onPageScrollStateChanged");
//
//            }
//        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{1000, 1000}, 3));

            } else {
                //deprecated in API 26
                long a[] = new long[]{1000, 1000};

                vibrator.vibrate(new long[]{1000, 1000}, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

//        Intent i = new Intent(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder build = new NotificationCompat.Builder(this, "hello")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("alarm")
//                .setContentText("alarm")
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000})
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setFullScreenIntent(pendingIntent, true);
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
//        manager.notify(1, build.build());


    }

    @Override
    protected void onPause() {
        super.onPause();
        r.stop();
        vibrator.cancel();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_BUTTON_PRESS) {
            Log.v(TAG, "tapping");
        } else if (action == MotionEvent.ACTION_SCROLL) {
            Log.v(TAG, "swiping");
            finish();
            return true;
        } else {
            Log.v(TAG, "nothing done");

        }

        return false;
    }

//    private class SwipeAlarmAdapter extends FragmentStateAdapter {
//
//        private SwipeAlarmAdapter(FragmentActivity fa) {
//            super(fa);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return new SwipeAlarm();
//        }
//
//        @Override
//        public int getItemCount() {
//            return alarms;
//        }
//    }

//    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
//        private static final float MIN_SCALE = 0.85f;
//        private static final float MIN_ALPHA = 0.5f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//            int pageHeight = view.getHeight();
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                Log.v(TAG, "swiping");
//                finish();
//
//            }
//        }
//    }

//    public class OnSwipeTouchListener implements View.OnTouchListener {
//
//        private final GestureDetector gestureDetector;
//
//        public OnSwipeTouchListener(Context context) {
//            gestureDetector = new GestureDetector(context, new GestureListener());
//        }
//
//        public void onSwipeLeft() {
//        }
//
//        public void onSwipeRight() {
//        }
//
//        public boolean onTouch(View v, MotionEvent event) {
//            return gestureDetector.onTouchEvent(event);
//        }
//
//        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
//            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                float distanceX = e2.getX() - e1.getX();
//                float distanceY = e2.getY() - e1.getY();
//                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (distanceX > 0)
//                        onSwipeRight();
//                    else
//                        onSwipeLeft();
//                    return true;
//                }
//                return false;
//            }
//        }
//    }
}